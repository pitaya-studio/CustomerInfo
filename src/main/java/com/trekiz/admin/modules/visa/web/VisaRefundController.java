package com.trekiz.admin.modules.visa.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaService;

/**
 * 签证退款Controller
 * @author chen.jia
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/order/manager/visaRefund")
public class VisaRefundController extends BaseController {

	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private IVisaProductsService visaProductsService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private AgentinfoDao agentinfoDao;
	
	@RequestMapping(value = "refundForm")
	public String refundForm(@RequestParam(value = "proId", required = true) String proId, Model model,@RequestParam(value = "type", required = false) String type) {
		
		//订单和团期信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
		VisaProducts visaProdcut = null;
		if(visaOrder.getVisaProductId() != null) {
			visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		}
		
		//根据产品id获取产品
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		
		List<Integer> flowTypeList = new ArrayList<Integer>();
		flowTypeList.add(1);
		flowTypeList.add(16);
		//已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), flowTypeList, proId, true, visaOrderService.getProductPept(proId));
		//游客信息
		List<Object[]> travelerList = null;
		if(null != visaOrder) {
			travelerList = visaService.findTravelerRefundVisaByOrderId(proId);
		}
		
		//过滤掉已经申请的游客列表
		int index = 0;
		for(Review review : appliedList) {
			//过滤已驳回的审批流
			if(0 != review.getStatus() && 2 != review.getStatus()) {
				for(Object[] o : travelerList) {
					if(o[0].equals(review.getTravelerId())) {
						travelerList.remove(index);
						break;
					}
					index++;
				}
			}
		}

		//通过游客id和金额uuid分别查找游客姓名、币种和价格
		if(!travelerList.isEmpty()) {
			for(Object[] o : travelerList) {
				o[0] = travelerService.findTravelerById(StringUtils.toLong(o[0]));
				//o[1] = visaOrderService.getMoney(o[1].toString(),"true");
			}
		}
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaProdcut", visaProdcut);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("visaProduct", visaProduct);//获取产品
		//return "modules/visa/visaRefundForm";
		if("1".equals(type)){
			return "modules/visa/visaRefundForm";
		}else{
			return "modules/visa/viewVisaRefundInfo";
		}
		
	}
	
	@ResponseBody
	@RequestMapping("refundApply")
	public String refundApply(@RequestParam(value="proId", required=true)String proId, HttpServletRequest request) {

		String[] onceChks = request.getParameterValues("onceChk");
		//游客id
		String[] travelerIds = request.getParameterValues("travelerId");
		//游客名称
		String[] travelerNames = request.getParameterValues("travelerName");
		//款项
		String[] refundNames = request.getParameterValues("refundProject");
		//应收
		String[] payPrices = request.getParameterValues("payPrice");
		//币种
		String[] currencyIds = request.getParameterValues("refundCurrency");
		//金额
		String[] refundPrices = request.getParameterValues("refund");
		//备注
		String[] remarks = request.getParameterValues("refundMark");
		
		if(null != onceChks) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
			Long agentId = visaOrder.getAgentinfoId();
			Long deptId = visaOrderService.getProductPept(proId);
			String agentName = "";
			if (agentId == -1) {
				agentName = visaOrder.getAgentinfoName();
			} else {
				agentName = agentinfoDao.findOne(agentId).getAgentName();
			}
			
			boolean yubao_locked = false; //预报单是否锁定标识
			boolean jiesuan_locked = false; //结算单是否锁定标识
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			//对预报单状态进行判断
			if ("10".equals(visaProducts.getForcastStatus())) {//!=改为了== by chy 2015年6月8日17:34:52 1为锁 0 标示未锁
				yubao_locked = true;
			}
			//对结算单状态进行判断
			if (1 == visaProducts.getLockStatus()) {//00改为了10 by chy 2015年6月8日17:35:24 00标示未锁 10标示锁定
				jiesuan_locked = true;
				return "结算订已锁定，不能发起退款申请";
			} 
			
			for(int i = 0; i < onceChks.length; i++) {
				List<Detail> detailList = null;
				String mark = "";
				if(StringUtils.isNotBlank(onceChks[i])) {
					detailList = new ArrayList<Detail>();
					//如果是团队退款
					Detail travelerIdDetail = null;
					Detail travelerNameDetail = null;
					Detail refundNameDetail = null;
					Detail payPriceDetail = null;
					Detail currencyIdDetail = null;
					String currencyName = null;
					Detail currencyNameDetail = null;
					if("-1".equals(onceChks[i])) {
						travelerIdDetail = new Detail("travelerId", "0");
						travelerNameDetail = new Detail("travelerName", "团队退款");
						refundNameDetail = new Detail("refundName", refundNames[i]);
						payPriceDetail = new Detail("payPrice", payPrices[i]);
						currencyIdDetail = new Detail("currencyId", currencyIds[i]);
						//币种名称
						currencyName = currencyService.findCurrency(StringUtils.toLong(currencyIds[i])).getCurrencyName(); 
						currencyNameDetail = new Detail("currencyName", currencyName);
					}else{
						travelerIdDetail = new Detail("travelerId", travelerIds[i]);
						travelerNameDetail = new Detail("travelerName", travelerNames[i]);
						refundNameDetail = new Detail("refundName", refundNames[i]);
						payPriceDetail = new Detail("payPrice", payPrices[i]);
						currencyIdDetail = new Detail("currencyId", currencyIds[i]);
						//币种名称
						currencyName = currencyService.findCurrency(StringUtils.toLong(currencyIds[i])).getCurrencyName(); 
					}
					currencyNameDetail = new Detail("currencyName", currencyName);
					detailList.add(currencyNameDetail);
					
					Detail refundPriceDetail = new Detail("refundPrice", refundPrices[i]);
					if(null != remarks && StringUtils.isNotBlank(remarks[i])) {
						mark = refundNames[i];
						Detail remarkDetail = new Detail("remark", remarks[i]);
						detailList.add(remarkDetail);
					}
					detailList.add(travelerIdDetail);
					detailList.add(travelerNameDetail);
					detailList.add(refundNameDetail);
					detailList.add(payPriceDetail);
					detailList.add(currencyIdDetail);
					detailList.add(refundPriceDetail);

					//如果是新行者的计调申请退款
					int reveiwFlowType = Context.REVIEW_FLOWTYPE_REFUND;
					if(71 == UserUtils.getUser().getCompany().getId() && 0 != reviewService.getOperTotal()) {
						reveiwFlowType = Context.REVIEW_FLOWTYPE_OPER_REFUND;
					}
					StringBuffer reply = new StringBuffer("");
					Long result = reviewService.addReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 
							reveiwFlowType, proId, StringUtils.toLong(travelerIds[i]), 0L, mark, reply, 
							detailList, deptId);
					//退款申请添加成本记录
					if(0 != result) {
						//组织一个RefundBean    getRefundPrice    getCurrencyId    getRemark
						RefundBean bean = new RefundBean();
						bean.setCurrencyId(currencyIds[i]);
						bean.setRefundPrice(refundPrices[i]);
						bean.setRemark(remarks[i]);
						costManageService.saveRefundCostRecord(Context.ORDER_TYPE_QZ, bean, visaOrder, agentName, result, deptId, yubao_locked, jiesuan_locked);
					}else{
						if("".equals(reply)) {
							return reply.toString();
						}
					}
				}
			}
		}
		return "";
//		return "redirect:" + Global.getAdminPath() + "/order/manager/visaRefund/refundList?proId=" + proId;
	}
	
	@RequestMapping("refundList")
	public String refundList(@RequestParam(value="proId", required=true)String proId, Model model) {
		
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(1);
		typeList.add(16);
		//这个需要获取到已经申请的游客列表
		List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), typeList, proId, false, visaOrderService.getProductPept(proId));
		//审核详情
		List<Map<String, String>> reviewDetailList = null;
		if(!appliedList.isEmpty()) {
			reviewDetailList = new ArrayList<Map<String, String>>();
			for(Integer i : typeList) {
				reviewDetailList.addAll(reviewService.findReviewListMap(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), i, proId, false, visaOrderService.getProductPept(proId)));
			}
		}
		
		model.addAttribute("appliedList", appliedList);
		model.addAttribute("reviewDetailList", reviewDetailList);
		model.addAttribute("proId", proId);
		
		return "modules/visa/visaRefundList";
	}
	
	//取消申请
	@RequestMapping("removeRefundReview")
	public String removeRefundReview(@RequestParam(value="reviewId", required=true) String reviewId) {
		reviewService.removeReview(StringUtils.toLong(reviewId));
		String proId = reviewService.findReview(StringUtils.toLong(reviewId)).get("orderId");
		return "redirect:" + Global.getAdminPath() + "/order/manager/visaRefund/refundList?proId=" + proId;
	}
}
