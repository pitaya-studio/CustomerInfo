package com.trekiz.admin.modules.visa.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;

/**
 * 押金转担保Controller
 * @author chen.jia
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/order/manager/visa")
public class DepositToWarrantController extends BaseController{

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
	private VisaProductsService visaProductsService;
	
	//押金转担保申请页
	@RequestMapping(value = "warrantForm")
	public String warrantForm(@RequestParam(value = "proId", required = true) String proId, 
			@RequestParam(value = "reviewId", required = false) String reviewId, 
			Model model) {
		
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
		VisaProducts visaProdcut = null;
		if(visaOrder.getVisaProductId() != null) {
			visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		}
		
		//已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 6, proId, true, visaOrderService.getProductPept(proId));
		
		
		//获得游客列表
		List<Object[]> travelerList = null;
		//如果是再次申请的请求，则直接查找指定的用户，不需要过滤
		if(StringUtils.isBlank(reviewId))	 {
			//这里需要根据签证订单列表选择交押金的游客来过滤回显的游客列表，并且获得押金金额和币种
			travelerList = visaService.findTravelerVisaByOrderId(proId);
			
			//过滤掉已经申请的游客列表
			for(Review review : appliedList) {
				int index = 0;
				for(Object[] o : travelerList) {
					if(o[0].equals(review.getTravelerId())) {
						if(0 != review.getStatus()) {
							travelerList.remove(index);
						}
						break;
					}
					index++;
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
				if(null != o[1] && StringUtils.isNotBlank(o[1].toString())){
					o[1] = visaOrderService.getMoney(o[1].toString(),"true").split(" ");
				}
			}
		}
		
		//返回多个游客信息
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("visaProdcut",visaProdcut);
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("proId", proId);
		
		return "modules/visa/warrantForm";
	}
	
	
	//押金转担保列表
	@RequestMapping(value = "warrantList")
	public String warrantList(@RequestParam(value = "proId", required = true) String proId, Model model) {
		
		//这个需要获取到已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 6, proId, false, visaOrderService.getProductPept(proId));
		//审核详情
		List<Map<String, String>> reviewDetailList = null;
		if(!appliedList.isEmpty()) {
			reviewDetailList = reviewService.findReviewListMap(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 6, proId, false, visaOrderService.getProductPept(proId));
		}
		for(Map<String,String> map : reviewDetailList){
			String price = map.get("price");
			map.put("price", fmtMicrometer(price));
		}
		
		//获取订单信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(proId));
		//下单人
		model.addAttribute("createBy",visaOrder.getCreateBy().getName());
		//销售
		model.addAttribute("saler",visaOrder.getSalerName());
		
		model.addAttribute("appliedList", appliedList);
		model.addAttribute("reviewDetailList", reviewDetailList);
		model.addAttribute("proId", proId);
		
		return "modules/visa/warrantList";
	}
	
	/**
	 * 数字格式化
	 * @param text
	 * @return
	 */
	public String fmtMicrometer(String text){
		text = text.replaceAll(",", "");
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}
	
	
	//押金转担保申请
	@RequestMapping(value = "warrantApply")
	public String warrantApply(@RequestParam(value = "proId", required = true) String proId, HttpServletRequest request) {
		
		String reviewId = request.getParameter("reviewId");
		//如果提交了reviewId,说明是重新申请的，则取消先前的
		if(StringUtils.isNotBlank(reviewId)) {
			removeWarrantReview(reviewId);
		}
		
		String[] travelerIds = request.getParameterValues("travelerIds");
		String[] travelerNames = request.getParameterValues("travelerName");
		String[] priceCurrency = request.getParameterValues("priceCurrency");
		String[] prices = request.getParameterValues("price");
		String[] visaStatus = request.getParameterValues("visaStatus");
		String[] warrantTypes = request.getParameterValues("warrantType");
		String[] reasonMarks = request.getParameterValues("reasonMark");
		if(travelerIds != null) {
			//各个游客
			for(int i = 0; i < travelerIds.length;) {
				//游客审核记录
				Review review = new Review();
				review.setTravelerId(StringUtils.toLong(travelerIds[i]));
				review.setReviewCompanyId(1L);
				
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
				//价格币种
				Detail priceCurrencyDetail = null;
				if(priceCurrency != null && StringUtils.isNotBlank(priceCurrency[i])) {
					priceCurrencyDetail = new Detail("priceCurrency", priceCurrency[i]);
				}
				//价格
				Detail priceDetail = null;
				if(prices != null && StringUtils.isNotBlank(prices[i])) {
					priceDetail = new Detail("price", prices[i]);
				}
				//游客约签状态
				Detail visaStatusDetail = null;
				if(visaStatus != null && StringUtils.isNotBlank(visaStatus[i])) {
					visaStatusDetail = new Detail("visaStatus", visaStatus[i]);
				}
				
				/**
				 *担保类型
				 *  1,担保   2,担保 + 押金
				 *  审核通过后需要修改 签证的审核状态，改为申请时的相应状态
				 */
				Detail warrantTypeDetail = null;
				if(warrantTypes != null && StringUtils.isNotBlank(warrantTypes[i])) {
					warrantTypeDetail = new Detail("warrantType", warrantTypes[i]);
				}
				//担保原因
				Detail reasonMarkDetail = null;
				if(reasonMarks != null && StringUtils.isNotBlank(reasonMarks[i])) {
					reasonMarkDetail = new Detail("reasonMark", reasonMarks[i]);
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
				detailList.add(priceDetail);
				detailList.add(priceCurrencyDetail);
				detailList.add(visaStatusDetail);
				detailList.add(warrantTypeDetail);
				if(reasonMarkDetail == null)
					reasonMarkDetail = new Detail("reasonMark", "");
				else
				detailList.add(reasonMarkDetail);
				detailList.add(createByDetail);
				detailList.add(createDateDetail);
				
				reviewService.addReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 6, 
						proId, StringUtils.toLong(travelerIds[i]), 0L, reasonMarkDetail.getValue(), new StringBuffer("申请失败"), 
						detailList, visaOrderService.getProductPept(proId));
				i++;
			}
		}
		
		return "redirect:"+Global.getAdminPath()+"/order/manager/visa/warrantList?proId=" + proId;
	}
	
	//取消申请
	@RequestMapping("removeWarrantReview")
	public String removeWarrantReview(@RequestParam(value="reviewId", required=true) String reviewId) {
		reviewService.removeReview(StringUtils.toLong(reviewId));
		String proId = reviewService.findReview(StringUtils.toLong(reviewId)).get("orderId");
		return "redirect:"+Global.getAdminPath()+"/order/manager/visa/warrantList?proId=" + proId;
	}
}
