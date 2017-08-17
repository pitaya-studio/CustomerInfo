package com.trekiz.admin.modules.visa.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.depositereview.service.IDepositeRefundReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;

/**
 * 签证订单退押金Controller
 * @author chen.jia
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/order/manager/visaDeposit")
public class DepositRefundController extends BaseController {

	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private VisaService visaService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private IDepositeRefundReviewService depositeRefundReviewService;
	
	@RequestMapping(value="refundForm")
	public String refundForm(@RequestParam(value = "proId", required = false) String proId, 
			@RequestParam(value = "reviewId", required = false) String reviewId, 
			@RequestParam(value = "travelerIds", required = false) String travelerIds, 
			Model model) {
		
		//获得游客列表
		List<Object[]> travelerList = null;
		//获得订单和产品信息
		//如果是单个订单的游客申请
		if(StringUtils.isNotBlank(proId)) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
			VisaProducts visaProdcut = null;
			if(visaOrder.getVisaProductId() != null) {
				visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			}
			
			//如果没有勾选游客
			if(StringUtils.isBlank(travelerIds)){
				//这里需要根据签证订单列表选择交押金的游客来过滤回显的游客列表，并且获得押金金额和币种
				travelerList = visaService.findTravelerDepositRefundVisaByOrderId(proId);
			}
			
			model.addAttribute("visaProdcut",visaProdcut);
			model.addAttribute("visaOrder", visaOrder);
		}
		
		//如果是以游客申请退押金
		if(StringUtils.isNotBlank(travelerIds)){
			String[] travelerIdArr = travelerIds.split(",");
			if(null != travelerIdArr) {
				travelerList = visaService.findTravelerDepositRefundVisaByTravelerIds(travelerIdArr);
			}
		}
		
		//已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, proId, true, visaOrderService.getProductPept(proId));
		//如果是再次申请的请求，则直接查找指定的用户，不需要过滤
		if(StringUtils.isBlank(reviewId))	 {
			//过滤掉已经申请的游客列表
			int index = 0;
			for(Review review : appliedList) {
				//过滤已驳回的审批流
				if(0 != review.getStatus()) {
					for(Object[] o : travelerList) {
						if(o[0].equals(review.getTravelerId())) {
							travelerList.remove(index);
							break;
						}
						index++;
					}
				}
			}
		}else{
			Map<String, String> reviewMap = reviewService.findReview(StringUtils.toLong(reviewId));
			travelerList = visaService.findTravelerVisaByTravelerId(reviewMap.get("travelerId"));
		}
		
		//通过游客id和金额uuid分别查找游客姓名、币种和价格
		if(!travelerList.isEmpty()) {
			for(Object[] o : travelerList) {
				o[0] = travelerService.findTravelerById(StringUtils.toLong(o[0]));
				o[1] = moneyAmountService.findAmountBySerialNum(o[1].toString()).get(0);
				o[2] = moneyAmountService.findAmountBySerialNum(o[2].toString()).get(0);
//				o[1] = visaOrderService.getMoney(o[1].toString(),"true").split(" ");
//				o[2] = visaOrderService.getMoney(o[2].toString(),"true").split(" ");
			}
		}
			
		//返回多个游客信息
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("proId", proId);
		
		return "modules/visa/depositRefundForm";
	}
	
	
	/**
	 * 进入退签证押金审批详情页
	 * 签证押金是和游客关联的 一个游客一个签证 一个押金 
	 */
	@RequestMapping(value = "depositeRefundReviewDetail")
	public String queryRefundReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审核表id
		// 查询审批详情信息
		Map<String, Object> orderDetail = depositeRefundReviewService.queryVisaorderDeatail(orderId);
		// 处理多币种信息 start
		String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
		totalMoney = moneyAmountService.getMoney(totalMoney);
		orderDetail.remove("totalmoney");
		orderDetail.put("totalmoney", totalMoney);
//		String visapay = orderDetail.get("visapay") == null ? null : orderDetail.get("visapay").toString();
//		visapay = moneyAmountService.getMoney(visapay);
//		orderDetail.remove("visapay");
//		orderDetail.put("visapay", visapay);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(Long.parseLong(orderDetail.get("visaproductid").toString()));
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("country", country);
		
		//处理多币种 end
		model.addAttribute("orderDetail", orderDetail);
		// 查询退款信息
		Map<String, String> review = reviewService.findReview(Long
				.parseLong(reviewId));
		//把币种id转为币种名称
		Currency currency = currencyDao.findOne(Long.parseLong(review.get("depositPriceCurrency")));
		review.put("currencyName", currency.getCurrencyName());
		model.addAttribute("reviewdetail", review);
		model.addAttribute("rid", reviewId);
		return "modules/visa/depositeRefundReviewDetail";
	}
	
	
	//签证退押金列表
	@RequestMapping(value = "refundList")
	public String warrantList(@RequestParam(value = "proId", required = true) String proId, Model model) {
		
		//这个需要获取到已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, proId, false, visaOrderService.getProductPept(proId));
		//审核详情
		List<Map<String, String>> reviewDetailList = null;
		if(!appliedList.isEmpty()) {
			reviewDetailList = reviewService.findReviewListMap(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, proId, false, visaOrderService.getProductPept(proId));
		}
		
		model.addAttribute("appliedList", appliedList);
		model.addAttribute("reviewDetailList", reviewDetailList);
		model.addAttribute("proId", proId);
		
		return "modules/visa/depositRefundList";
	}
	
	
	//押金转担保申请
		@RequestMapping(value = "tuiYaJinShenQing")
		public String tuiYaJinShenQing(@RequestParam(value = "proId", required = true) String proId, HttpServletRequest request, HttpServletResponse response) {
			
			String reviewId = request.getParameter("reviewId");
			//如果提交了reviewId,说明是重新申请的，则取消先前的
			if(StringUtils.isNotBlank(reviewId)) {
				removeRefundReview(reviewId);
			}
			
			String[] travelerIds = request.getParameterValues("travelerIds");
			String[] travelerNames = request.getParameterValues("travelerName");
			String[] priceCurrency = request.getParameterValues("priceCurrency");
			String[] prices = request.getParameterValues("price");
			String[] totalPrice = request.getParameterValues("totalPrice");
			String[] applyDates = request.getParameterValues("applyDates");
			String[] applyPrice = request.getParameterValues("applyPrice");
			String[] reasonMarks = request.getParameterValues("reasonMark");
			
			if(travelerIds != null) {
				//本次请求游客所有的申请结果，默认为失败。
				Long refundResult = 0L;
				//申请失败的原因
				StringBuffer reply = new StringBuffer();
				//各个游客
				for(int i = 0; i < travelerIds.length;) {
					//游客审核记录
//					Review review = new Review();
//					review.setTravelerId(StringUtils.toLong(travelerIds[i]));
//					review.setCpid(1L);
					
					/**
					 * set more attribute for Review
					 */
					
					List<Detail> detailList = new ArrayList<Detail>();
					
					//申请项目详情
					//游客id
					Detail travelerIdDetail = null;
					if(travelerIds != null && StringUtils.isNotBlank(travelerIds[i])) {
						travelerIdDetail = new Detail("travelerId", travelerIds[i]);
					}
					//游客姓名
					Detail travelerNameDetail = null;
					if(travelerNames != null && StringUtils.isNotBlank(travelerNames[i])) {
						travelerNameDetail = new Detail("travelerName", travelerNames[i]);
					}
					//押金币种
					Detail priceCurrencyDetail = null;
					if(priceCurrency != null && StringUtils.isNotBlank(priceCurrency[i])) {
						priceCurrencyDetail = new Detail("depositPriceCurrency", priceCurrency[i]);
					}
					//押金金额
					Detail priceDetail = null;
					if(prices != null && StringUtils.isNotBlank(prices[i])) {
						priceDetail = new Detail("depositPrice", prices[i]);
					}
					//达帐金金额
					Detail totalPriceDetail = null;
					if(totalPrice != null && StringUtils.isNotBlank(totalPrice[i])) {
						totalPriceDetail = new Detail("payPrice", totalPrice[i]);
					}
					//申请时间
					Detail applyDate = new Detail("applyDate", "");
					if(null != applyDates && StringUtils.isNotBlank(applyDates[i])) {
						applyDate.setValue(applyDates[i]);
					}
					//申请押金金额
					Detail applyPriceDetail = new Detail("refundPrice", "");
					if(applyPrice != null && StringUtils.isNotBlank(applyPrice[i])) {
						applyPriceDetail.setValue(applyPrice[i]);;
					}
					//原因
					Detail reasonMarkDetail = new Detail("remark", "");
					if(reasonMarks != null && StringUtils.isNotBlank(reasonMarks[i])) {
						reasonMarkDetail.setValue(reasonMarks[i]);;
					}
					Detail createByDetail = null;
					Detail createDateDetail = null;
					if(StringUtils.isNotBlank(proId)) {
						VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
						//订单销售(创建人)
						createByDetail = new Detail("orderCreateBy", visaOrder.getCreateBy().getId().toString());
						//订单创建时间
						createDateDetail = new Detail("orderCreateDate", visaOrder.getCreateDate().toString());
					}
					
					detailList.add(travelerIdDetail);
					detailList.add(travelerNameDetail);
					detailList.add(priceCurrencyDetail);
					detailList.add(priceDetail);
					detailList.add(totalPriceDetail);
					detailList.add(applyDate);
					detailList.add(applyPriceDetail);
					detailList.add(reasonMarkDetail);
					detailList.add(createByDetail);
					detailList.add(createDateDetail);
					
					refundResult = reviewService.addReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, 
							proId, StringUtils.toLong(travelerIds[i]), 0L, reasonMarkDetail.getValue(), reply, 
							detailList, visaOrderService.getProductPept(proId));
					i++;
				}
				//如果是批量申请
				String isAjax = request.getParameter("ajax");
				if(StringUtils.isNotBlank(isAjax)) {
					try {
						if(0L == refundResult) {
							response.getWriter().print("申请失败：" + reply);
						}else{
							response.getWriter().print("申请成功！");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return null;
				}
			}
			return "redirect:"+Global.getAdminPath()+"/order/manager/visaDeposit/refundList?proId=" + proId;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//押金转担保申请
	@RequestMapping(value = "refundApply")
	public String warrantApply(@RequestParam(value = "proId", required = true) String proId, HttpServletRequest request, HttpServletResponse response) {
		
		String reviewId = request.getParameter("reviewId");
		//如果提交了reviewId,说明是重新申请的，则取消先前的
		if(StringUtils.isNotBlank(reviewId)) {
			removeRefundReview(reviewId);
		}
		
		/*String[] travelerIds = request.getParameterValues("travelerIds");
		String[] travelerNames = request.getParameterValues("travelerName");
		String[] priceCurrency = request.getParameterValues("priceCurrency");
		String[] prices = request.getParameterValues("price");
		String[] totalPrice = request.getParameterValues("totalPrice");
		String[] applyDates = request.getParameterValues("applyDates");
		String[] applyPrice = request.getParameterValues("applyPrice");
		String[] reasonMarks = request.getParameterValues("reasonMark");*/
		
		//修改批量退签证押金报错的问题。
		String[] travelerIds = request.getParameterValues("travelerIds")[0].split(",");
		String[] travelerNames = request.getParameterValues("travelerName")[0].split(",");
		String[] priceCurrency = request.getParameterValues("priceCurrency")[0].split(",");
		String[] prices = request.getParameterValues("price")[0].split(",");
		String[] totalPrice = request.getParameterValues("totalPrice")[0].split(",");
		String[] applyDates = request.getParameterValues("applyDates")[0].split(",");
		String[] applyPrice = request.getParameterValues("applyPrice")[0].split(",");
		String[] reasonMarks = request.getParameterValues("reasonMark")[0].split(",");
		if(travelerIds != null) {
			//本次请求游客所有的申请结果，默认为失败。
			Long refundResult = 0L;
			//申请失败的原因
			StringBuffer reply = new StringBuffer();
			//各个游客
			for(int i = 0; i < travelerIds.length;) {
				//游客审核记录
//				Review review = new Review();
//				review.setTravelerId(StringUtils.toLong(travelerIds[i]));
//				review.setCpid(1L);
				
				/**
				 * set more attribute for Review
				 */
				
				List<Detail> detailList = new ArrayList<Detail>();
				
				//申请项目详情
				//游客id
				Detail travelerIdDetail = null;
				if(travelerIds != null && StringUtils.isNotBlank(travelerIds[i])) {
					travelerIdDetail = new Detail("travelerId", travelerIds[i]);
				}
				//游客姓名
				Detail travelerNameDetail = null;
				if(travelerNames != null && StringUtils.isNotBlank(travelerNames[i])) {
					travelerNameDetail = new Detail("travelerName", travelerNames[i]);
				}
				//押金币种
				Detail priceCurrencyDetail = null;
				if(priceCurrency != null && StringUtils.isNotBlank(priceCurrency[i])) {
					priceCurrencyDetail = new Detail("depositPriceCurrency", priceCurrency[i]);
				}
				//押金金额
				Detail priceDetail = null;
				if(prices != null && StringUtils.isNotBlank(prices[i])) {
					priceDetail = new Detail("depositPrice", prices[i]);
				}
				//达帐金金额
				Detail totalPriceDetail = null;
				if(totalPrice != null && StringUtils.isNotBlank(totalPrice[i])) {
					totalPriceDetail = new Detail("payPrice", totalPrice[i]);
				}
				//申请时间
				Detail applyDate = new Detail("applyDate", "");
				if(null != applyDates && StringUtils.isNotBlank(applyDates[i])) {
					applyDate.setValue(applyDates[i]);
				}
				//申请押金金额
				Detail applyPriceDetail = new Detail("refundPrice", "");
				if(applyPrice != null && StringUtils.isNotBlank(applyPrice[i])) {
					applyPriceDetail.setValue(applyPrice[i]);;
				}
				//原因
				Detail reasonMarkDetail = new Detail("remark", "");
				if(reasonMarks != null && StringUtils.isNotBlank(reasonMarks[i])) {
					reasonMarkDetail.setValue(reasonMarks[i]);;
				}
				Detail createByDetail = null;
				Detail createDateDetail = null;
				if(StringUtils.isNotBlank(proId)) {
					VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
					//订单销售(创建人)
					createByDetail = new Detail("orderCreateBy", visaOrder.getCreateBy().getId().toString());
					//订单创建时间
					createDateDetail = new Detail("orderCreateDate", visaOrder.getCreateDate().toString());
				}
				
				detailList.add(travelerIdDetail);
				detailList.add(travelerNameDetail);
				detailList.add(priceCurrencyDetail);
				detailList.add(priceDetail);
				detailList.add(totalPriceDetail);
				detailList.add(applyDate);
				detailList.add(applyPriceDetail);
				detailList.add(reasonMarkDetail);
				detailList.add(createByDetail);
				detailList.add(createDateDetail);
				
				refundResult = reviewService.addReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, 
						proId, StringUtils.toLong(travelerIds[i]), 0L, reasonMarks[i], reply, 
						detailList, visaOrderService.getProductPept(proId));
				i++;
			}
			//如果是批量申请
			String isAjax = request.getParameter("ajax");
			if(StringUtils.isNotBlank(isAjax)) {
				try {
					if(0L == refundResult) {
						response.getWriter().print("申请失败：" + reply);
					}else{
						response.getWriter().print("申请成功！");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
		}
		return "redirect:"+Global.getAdminPath()+"/order/manager/visaDeposit/refundList?proId=" + proId;
	}
	
	//取消申请
	@RequestMapping("removeRefundReview")
	public String removeRefundReview(@RequestParam(value="reviewId", required=true) String reviewId) {
		reviewService.removeReview(StringUtils.toLong(reviewId));
		String proId = reviewService.findReview(StringUtils.toLong(reviewId)).get("orderId");
		return "redirect:"+Global.getAdminPath()+"/order/manager/visaDeposit/refundList?proId=" + proId;
	}
	
	/**
	 * 验证游客是否符合退押金的需求
	 * @author jiachen
	 * @DateTime 2015年3月19日 下午7:46:01
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("checkDepositRefund")
	public Map<String, Object> checkDepositRefund(String travelerIds) {
		Map<String, Object> travelerInfoMap = null;
		if(StringUtils.isNotBlank(travelerIds)) {
			travelerInfoMap = visaOrderService.checkDepositRefund(travelerIds);
		}
		return travelerInfoMap;
	}
}
