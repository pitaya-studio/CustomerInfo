package com.trekiz.admin.review.prdreturn.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.airticketChange.service.NewAirticketChangeServiceImpl;
import com.trekiz.admin.review.prdreturn.common.service.IProductReturnService;
import com.trekiz.admin.review.prdreturn.common.service.ProductReturnAppService;
import com.trekiz.admin.review.prdreturn.singleGroup.service.ISingleGroupOrderExitGroupService;

/**
 * 产品退（退票、退团、撤签）审核处理
 * @author chy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/prdreturn")
public class ProductReturnController {
	
	@Autowired
	private IProductReturnService productReturnService;
	@Autowired
	private ISingleGroupOrderExitGroupService singleGroupOrderExitGroupService;
	@Autowired
	private NewAirticketChangeServiceImpl newAirticketChangeService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ProductOrderService orderService;
	
	@Autowired
	private TravelActivityService travelActivityService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private SysOfficeConfigurationService sysOfficeConfigurationService;

	@Autowired
	private ProductReturnAppService productReturnAppService;
	
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;

	/**
	 * 查询退团退票审核列表 by chy 2015年11月13日11:48:43
	 * 
	 */
	@RequestMapping(value = "returnreviewlist")
	public String queryReturnReviewList(Model model, HttpServletRequest request, HttpServletResponse response){
		List<SysOfficeProductType> processTypes = sysOfficeConfigurationService.obtainOfficeProductTypes(UserUtils.getUser().getCompany().getUuid());
		Map<String, Object> params = prepareParams(request, response);
		Page<Map<String, Object>> page = productReturnService.queryReturnReviewList(params);
		singleGroupOrderExitGroupService.handleAfterAndRefund(page.getList());
		model.addAttribute("conditionsMap", params);
		model.addAttribute("processTypes", processTypes);
		model.addAttribute("page", page);
		return "review/prdreturn/common/returnreviewlist";
	}
	
	/**
	 * 组织参数
	 * @param request
	 * @param response
	 * @return
	 */
	private Map<String, Object> prepareParams(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		/**获取参数 start*/
		//团号/产品名称/订单号
		String groupCode = request.getParameter("groupCode") == null ? null : request.getParameter("groupCode").toString();
		//产品类型(id)
		String productType = request.getParameter("productType") == null ? null : request.getParameter("productType").toString();
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		//申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
		//申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		//计调(id)
		String operator = request.getParameter("operator") == null ? null : request.getParameter("operator").toString();
		//游客（id）
		String travelerId = request.getParameter("travelerId") == null ? null : request.getParameter("travelerId").toString();
		//审批状态
		String reviewStatus = request.getParameter("reviewStatus") == null ? null : request.getParameter("reviewStatus").toString();
		//页签选择状态
		String tabStatus = request.getParameter("tabStatus") == null ? null : request.getParameter("tabStatus").toString();
		if(tabStatus == null || "".equals(tabStatus)){//默认为待本人审核
			tabStatus = Context.NumberDef.NUMER_ONE.toString();
		}
		// 创建日期排序标识
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
		//订单创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");
		//订单更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		if(StringUtils.isBlank(orderCreateDateSort) && StringUtils.isBlank(orderUpdateDateSort) && StringUtils.isBlank(orderCreateDateCss) && StringUtils.isBlank(orderUpdateDateCss)){
			orderCreateDateSort = "desc";
			orderCreateDateCss = "activitylist_paixu_moren";
		}
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		/**获取参数 end*/
		/**组装参数 start*/
		result.put("groupCode", groupCode);
		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("applyPerson", applyPerson);
		result.put("operator", operator);
		result.put("travelerId", travelerId);
		result.put("reviewStatus", reviewStatus);
		result.put("tabStatus", tabStatus);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("paymentType", request.getParameter("paymentType"));
		result.put("pageP", page);
		/**组装参数 end*/
		return result;
	}

	/**
	 * 退团退票审核的通过和驳回
	 */
	@ResponseBody
	@RequestMapping(value = "returnreview")
	@Transactional(readOnly = false, rollbackFor = {ServiceException.class})
	public Map<String, Object> returnReview(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer reply = new StringBuffer();
		String processType = request.getParameter("processType");
		String travelerIdStr = request.getParameter("travelerId");
		String orderIdStr = request.getParameter("orderId");
		
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderIdStr));
		
		String revid = request.getParameter("revid");
		if (revid == null || "".equals(revid)) {
			reply.append("审批id不能为空");
			params.put("flag", "fail");
			params.put("msg", reply);
			return params;
		}
		String denyReason = request.getParameter("denyReason");
		String result = request.getParameter("result");
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
			params.put("flag", "fail");
			params.put("msg", reply);
			return params;
		}
		//当前登录用户id
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		ReviewResult reviewResult = new ReviewResult();
		if(Context.REVIEW_ACTION_PASS.equals(result.trim())){
			reviewResult = reviewService.approve(userId, companyId, null, userReviewPermissionChecker, revid, denyReason, null);
		} else {
			reviewResult = reviewService.reject(userId, companyId, null, revid, denyReason, null);
		}
		if(!reviewResult.getSuccess()){
			params.put("flag", "fail");
			params.put("msg", reviewResult.getMessage());
			return params;
		}
		if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){//退票审核审批完毕后 处理业务
			try{
				// 退票
				if(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString().equals(processType)){
					//还余位
					newAirticketChangeService.changeOrExit(null, request.getParameter("orderId"), request.getParameter("travelerId"));
				// 退团
				} else if (Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString().equals(processType)) {
					//退团审核流程通过后，更新余位，更新游客状态
					productReturnService.handleFreePositionAndTraveler(travelerIdStr, productOrder, reviewResult.getReviewId(), request);
					//-------by------junhao.zhao-----2017-01-20-----主要通过productorder更改表order_data_statistics订单金额与人数---开始-----
					if(productOrder != null){
						orderDateSaveOrUpdateService.updatePeopleAndMoneyPro(productOrder.getId(), productOrder.getOrderStatus());
					}
					//-------by------junhao.zhao-----2017-01-20-----主要通过productorder更改表order_data_statistics订单金额与人数---结束-----
				}
			} catch (Exception e){
				e.printStackTrace();
				params.put("flag", "fail");
				params.put("msg", "审核失败!");
				return params;
			}
		//退团审批驳回后有逻辑需要处理, add by yunpeng.zhang, add date 2015年12月23日11:56:40 
		} else if (ReviewConstant.REVIEW_STATUS_REJECTED == reviewResult.getReviewStatus()) {
			try {
				// 退团
				if(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString().equals(processType)) {
					productReturnService.updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, Long.parseLong(request.getParameter("travelerId")));
				}
			} catch (Exception e) {
				e.printStackTrace();
				params.put("flag", "fail");
				params.put("msg", "驳回失败!");
				return params;
			}
			
		}
		params.put("flag", "success");
		return params;
	}
	
	/**
	 * 退票审核的撤销
	 */
	@ResponseBody
	@RequestMapping(value = "backreturnreview/{reviewId}")
	public Map<String, Object> backReturnReview(@PathVariable String reviewId, HttpServletRequest request, HttpServletResponse response){
		/*声明返回对象*/
		Map<String, Object> result = new HashMap<String, Object>();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		/*调用审核接口*/
		ReviewResult reviewResult = reviewService.back(UserUtils.getUser().getId().toString(), companyId, null, reviewId, null, null);
		if(reviewResult.getSuccess()){
			/*撤销成功 组织数据返回*/
			result.put("flag", "success");
			return result;
		}
		/*失败 组织数据返回*/
		result.put("flag", "error");
		result.put("msg", reviewResult.getMessage());
		return result;
	}
	
	/**
	 * 退团退票审核审批详情页
	 */
	@RequestMapping(value = "returnreviewdetail")
	public String returnReviewDetail(Model model, HttpServletRequest request, HttpServletResponse response){
		String orderId = request.getParameter("orderId");
		String traveleriId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String flag = request.getParameter("flag");
		
		Map<String, Object> reviewInfo = reviewService.getReviewDetailMapByReviewId(revid);
		//获取流程类型
		String processType = reviewInfo.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE).toString();
		
		if(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString().equals(processType)) {
			// 获取订单和产品的基本信息
			ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
			model.addAttribute("productOrder", productOrder);
			if(productOrder != null) {
				TravelActivity product = travelActivityService.findById(productOrder.getProductId());
				model.addAttribute("product", product);
				ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
				model.addAttribute("productGroup",productGroup);
				model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
				model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
			}
		} else if(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString().equals(processType)) {
			Map<String, Object> airticketReturnDetailInfoMap = productReturnService
					.queryAirTicketReturnInfoById(orderId);
			model.addAttribute("airticketReturnDetailInfoMap", airticketReturnDetailInfoMap);
		}
		
		Traveler traveler = new Traveler();
		String payPrice = "";
		if(!StringUtils.isBlank(traveleriId)){
			 traveler = travelerService.findTravelerById(Long.parseLong(traveleriId));
			 payPrice = moneyAmountService.getMoney(traveler.getPayPriceSerialNum());
		}
		singleGroupOrderExitGroupService.handleAfterAndRefundEvery(reviewInfo);
		model.addAttribute("reviewInfo",reviewInfo);
		model.addAttribute("rid",revid);
		model.addAttribute("traveler",traveler);
		model.addAttribute("payPrice",payPrice);
		model.addAttribute("flag", flag);
		model.addAttribute("processType", processType);
		
		return "review/prdreturn/common/returnReviewDetail";
	}
	
	/**
	 * 退团退票审核的批量通过和驳回
	 */
	@ResponseBody
	@RequestMapping(value = "batchreturnreview")
	public Map<String, Object> batchReturnReview(HttpServletRequest request, HttpServletResponse response){
		return productReturnAppService.batchReturnReview(request, response);
	}
	
	/**
	 * 退票审核的取消
	 */
	@ResponseBody
	@RequestMapping(value="cancelReview")
	public Map<String, Object> cancelReview(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		String revid = request.getParameter("revid");
		if(StringUtils.isBlank(revid)){
			result.put("flag", "fail");
			result.put("msg", "数据异常，不能识别的审批id:" + revid);
			return result;
		}
		ReviewResult reviewResult = reviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), null, revid, null, null);
		if(!reviewResult.getSuccess()){
			result.put("flag", "fail");
			result.put("msg", reviewResult.getMessage());
			return result;
		}
		result.put("flag", "success");
		return result;
	}
}
