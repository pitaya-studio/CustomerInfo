package com.trekiz.admin.review.refund.visa.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.changePrice.visa.service.INewVisaChangePriceService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.refund.singleGroup.service.ISingleGroupRefundService;

/**
 * 新签证退款Controller
 * @author ang.gao
 * @date 2015年12月4日
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/newRefund/visa")
public class NewVisaRefundController {
	
	@Autowired
	private INewVisaChangePriceService newVisaChangePriceService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private IVisaProductsService visaProductsService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private AgentinfoDao agentinfoDao;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
	private ISingleGroupRefundService singleGroupRefundService;
	
	@Autowired
    private UserReviewPermissionChecker permissionChecker;
	
	
	private static final int REFUND_PRODUCTTYPE_VISA = 6;//签证产品
	private static final String REFUND_PRODUCTTYPE_VISA_STR = "6";
	
	
	/**
	 * 查询签证退款记录
	 * @param proId(orderId)
	 * @param model
	 * @return
	 */
	@RequestMapping("refundList")
	public String refundList(@RequestParam(value="proId", required=true)String proId, Model model) {
		Integer reveiwFlowType = Context.REVIEW_FLOWTYPE_REFUND;
		//if(UserUtils.getUser().getCompany().getUuid().equals("7a8175bc77a811e5bc1e000c29cf2586") && 0 != reviewService.getOperTotal()){//新行者计调退款
		if(UserUtils.getUser().getCompany().getUuid().equals("7a8175bc77a811e5bc1e000c29cf2586")){
			reveiwFlowType = Context.REVIEW_FLOWTYPE_OPER_REFUND;
		}
		//查询产品部门ID
		List<Object> deptList = newVisaChangePriceService.getDeptIdByOrderId(proId);
		Long deptId = Long.parseLong(deptList.get(0).toString());
		
		List<ReviewNew> reviewList = processReviewService.getOrderReviewList(proId, REFUND_PRODUCTTYPE_VISA_STR, reveiwFlowType.toString());
		
		List<Map<String, Object>> reviewDetailList = processReviewService.getReviewDetailMapListByOrderId(deptId, REFUND_PRODUCTTYPE_VISA, 
				reveiwFlowType, proId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
		
		model.addAttribute("appliedList", reviewList);
		model.addAttribute("reviewDetailList", reviewDetailList);
		model.addAttribute("proId", proId);
		model.addAttribute("loginUser", UserUtils.getUser().getId().toString());
		
		return "review/refund/visa/visaRefundList";
	}
	
	/**
	 * 跳转到申请页面/详情页面
	 * @param proId(orderId)
	 * @param model
	 * @param type:1申请页面  else 详情页面
	 * @return
	 */
	@RequestMapping(value = "refundForm")
	public String refundForm(@RequestParam(value = "proId", required = true) String proId, Model model,@RequestParam(value = "type", required = false) String type) {
		
		//订单和团期信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
		
		//根据产品id获取产品
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		
		Integer reveiwFlowType = Context.REVIEW_FLOWTYPE_REFUND;
		//if(UserUtils.getUser().getCompany().getUuid().equals("7a8175bc77a811e5bc1e000c29cf2586") && 0 != reviewService.getOperTotal()){//新行者计调退款
		if(UserUtils.getUser().getCompany().getUuid().equals("7a8175bc77a811e5bc1e000c29cf2586")){
			reveiwFlowType = Context.REVIEW_FLOWTYPE_OPER_REFUND;
		}
		//该订单下所有审批记录
		/*依据审批流程互斥文档说明，审批中的游客仍可以进行申请*/
		//List<ReviewNew> reviewList = processReviewService.getOrderReviewList(proId, REFUND_PRODUCTTYPE_VISA_STR, reveiwFlowType.toString());
		//游客信息
		List<Object[]> travelerList = null;
		if(null != visaOrder) {
			travelerList = visaService.findTravelerRefundVisaByOrderId(proId);
		}
		
		/*依据审批流程互斥文档说明，审批中的游客仍可以进行申请*/
//		//过滤掉处理中(审批中)的游客列表
//		for(ReviewNew review : reviewList) {
//			if(review.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING) {
//				for(int i = 0;i<travelerList.size();i++){
//					if(travelerList.get(i)[0].toString().equals(review.getTravellerId())){
//						travelerList.remove(i);
//						break;
//					}
//				}
//			}
//		}

		//通过游客id和金额uuid分别查找游客姓名、币种和价格
		if(!travelerList.isEmpty()) {
			for(Object[] o : travelerList) {
				o[0] = travelerService.findTravelerById(StringUtils.toLong(o[0]));
				//o[1] = visaOrderService.getMoney(o[1].toString(),"true");
			}
		}
	//--------------------------------------------------------------------------------------
		if(!type.equals("1")){//此时加载审批详情页面
			//根据传递的来的uuid查询ReviewNew对象,该对象中包含着审批的详细信息
			ReviewNew rn=  ReviewUtils.getReviewNewByUuid(type);
			//通过rn获得travelerId
			String travelerId =rn.getTravellerId();
			//通过travelerId获得travelerInfo
			String sql="("+travelerId+")";
			List<Map<Object,Object>> travelerInfo=CommonUtils.getTravelerInfoForReview(sql);		
			//查询审批日志根据uuid
			List<ReviewLogNew> reviewLogInfo=ReviewUtils.obtainReviewLogs(type);
	       //获得审批详情-新方法,从别处copy过来的
			Long deptId=Long.parseLong(rn.getDeptId());
			String orderId=rn.getOrderId();
			List<Map<String, Object>> reviewDetailList = processReviewService.getReviewDetailMapListByOrderId(deptId, REFUND_PRODUCTTYPE_VISA, 
					reveiwFlowType, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
			
			model.addAttribute("reviewInfo",rn);
			model.addAttribute("travelerInfo", travelerInfo);
			model.addAttribute("reviewLogInfo", reviewLogInfo);
			model.addAttribute("review",reviewDetailList);
		}
	//---------------------------------------------------------------------------------------
		model.addAttribute("rid",type);
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("visaProduct", visaProduct);//获取产品
		
		if("1".equals(type)){
			return "review/refund/visa/visaRefundForm";
		}else{//审批详情页
			return "review/refund/visa/newViewVisaRefundInfo";
		}
		
	}
	
	/**
	 * 提交申请
	 * @param proId(orderId)
	 * @param request
	 * @return
	 */
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
			if ("10".equals(visaProducts.getForcastStatus())) {// 10为锁 00标示未锁
				yubao_locked = true;
			}
			//对结算单状态进行判断
			if (1 == visaProducts.getLockStatus()) {// 0标示未锁 1标示锁定
				jiesuan_locked = true;
				return "结算订已锁定，不能发起退款申请";
			} 
			
			for(int i = 0; i < onceChks.length; i++) {
				List<Detail> detailList = null;
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
						Detail remarkDetail = new Detail("remark", remarks[i]);
						detailList.add(remarkDetail);
					}else{
						Detail remarkDetail = new Detail("remark", "");
						detailList.add(remarkDetail);
					}
					detailList.add(travelerIdDetail);
					detailList.add(travelerNameDetail);
					detailList.add(refundNameDetail);
					detailList.add(payPriceDetail);
					detailList.add(currencyIdDetail);
					detailList.add(refundPriceDetail);
					
					Map<String, Object> variables = new HashMap<String, Object>();
					for(Detail detail : detailList){
						variables.put(detail.getKey(), detail.getValue());
					}
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentName);
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, proId);//orderId
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6");
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, visaOrder.getSalerName());
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, variables.get("travelerId"));
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, variables.get("travelerName"));
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, variables.get("remark"));
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());

					int reveiwFlowType = Context.REVIEW_FLOWTYPE_REFUND;
					//如果是新行者的计调申请退款
					if(UserUtils.getUser().getCompany().getUuid().equals("7a8175bc77a811e5bc1e000c29cf2586") && 0 != reviewService.getOperTotal()) {
						reveiwFlowType = Context.REVIEW_FLOWTYPE_OPER_REFUND;
					}
					ReviewResult result = processReviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), permissionChecker, "", REFUND_PRODUCTTYPE_VISA, reveiwFlowType, deptId, "", variables);
					if(result.getSuccess()){// start 成功)
						//退款申请添加成本记录
						//组织一个RefundBean    getRefundPrice    getCurrencyId    getRemark
						RefundBean bean = new RefundBean();
						bean.setCurrencyId(currencyIds[i]);
						bean.setRefundPrice(refundPrices[i]);
						bean.setRemark(remarks[i]);
						// 审核通过之后对成本进行更改
						ReviewNew reviewInfo = processReviewService.getReview(result.getReviewId());
						costManageService.saveRefundCostRecordNew(reviewInfo, bean, visaOrder, yubao_locked, jiesuan_locked);
						
						
						//保存退款金额
						NewProcessMoneyAmount refundMoneyAmount = new NewProcessMoneyAmount();

						// 获取退款金额对应的汇率
						BigDecimal exchangerate = null;
						List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
						for (Currency currency : currencyList) {
							Long currencyId = currency.getId();
							if (currencyIds[i].equals(currencyId)) {
								exchangerate = currency.getCurrencyExchangerate();
								break;
							}
						}

						refundMoneyAmount.setAmount(new BigDecimal(refundPrices[i]));
						refundMoneyAmount.setCompanyId(UserUtils.getUser().getCompany().getUuid());
						refundMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
						refundMoneyAmount.setCreateTime(new Date());
						refundMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
						refundMoneyAmount.setDelFlag("0");
						refundMoneyAmount.setExchangerate(exchangerate);
						refundMoneyAmount.setMoneyType(reveiwFlowType);
						refundMoneyAmount.setOrderType(REFUND_PRODUCTTYPE_VISA);
						refundMoneyAmount.setReviewId(result.getReviewId());
						refundMoneyAmount.setId(UuidUtils.generUuid());
						// 保存退款金额
						processMoneyAmountService.saveNewProcessMoneyAmount(refundMoneyAmount);
						
					}else{
						return result.getMessage();
					}
				}
			}
		}
		return "success";
	}
	
	/**
	 * 取消申请
	 * @param reviewId
	 * @param visaOrderId
	 * @return
	 */
	@RequestMapping("removeRefundReview")
	public String removeRefundReview(@RequestParam(value="reviewId", required=true) String reviewId,@RequestParam(value="visaOrderId", required=true) String orderId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		ReviewResult reviewResult = processReviewService.cancel(userId, companyId, "", reviewId, "", null);
		boolean flag = reviewResult.getSuccess();
		if (flag) { // 取消退款成功，改变金额状态
			singleGroupRefundService.handleMoneyAmount(reviewId);
		}
		
		return "redirect:" + Global.getAdminPath() + "/newRefund/visa/refundList?proId=" + orderId;
	}
	
}
