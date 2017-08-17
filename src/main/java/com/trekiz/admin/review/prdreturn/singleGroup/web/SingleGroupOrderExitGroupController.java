package com.trekiz.admin.review.prdreturn.singleGroup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.prdreturn.singleGroup.service.ISingleGroupOrderExitGroupService;
import com.trekiz.admin.review.transfersMoney.singlegroup.service.NewTransferMoneyService;

/**
 * 单团类退团申请，退团申请记录控制器
 * 
 * @author yunpeng.zhang
 * 
 */
@Controller
@RequestMapping("${adminPath}/singleOrder/exitGroup")
public class SingleGroupOrderExitGroupController {

	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private com.trekiz.admin.modules.reviewflow.service.ReviewService oldReviewService;
	@Autowired
	private ISingleGroupOrderExitGroupService singleGroupOrderExitGroupService;
	@Autowired
	public ReviewMutexService reviewMutexService;
	@Autowired
	public NewTransferMoneyService transferMoneyService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private NewProcessMoneyAmountService newAmountService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private OrderReviewService orderReviewService;
	/**
	 * 发起人取消退团申请
	 * 
	 * @author yunpeng.zhang
	 * @createTime 2015年11月20日
	 * @param status
	 * @param reviewId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancelExitGroup")
	public Map<String, Object> cancelExitGroup(String status, String reviewId, Long travelerId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = new HashMap<>();

		// 取消退团
		ReviewResult reviewResult = processReviewService.cancel(userId, companyId, "", reviewId, "", null);

		boolean flag = reviewResult.getSuccess();
		if (flag) {
			result.put("msg", "success");
			result.put("reply", reviewResult.getMessage());

			// 如果返回为 success,需要改变游客状态和对金额状态改变(游客状态 →删除标记 0:正常 1：删除 2:退团审批中
			// 3：已退团)
			// 改变游客状态和金额状态
			singleGroupOrderExitGroupService.handleTravelerAndMoneyAmount(travelerId, reviewId);
		} else {
			result.put("msg", "faild");
			result.put("repley", reviewResult.getMessage());
		}

		return result;
	}

	/**
	 * 退团详情
	 * 
	 * @author yunpeng.zhang
	 * @createTime 2015年11月19日
	 * @param orderId
	 *            订单id
	 * @param reviewId
	 *            审批id
	 * @return
	 */
	@RequestMapping("/newExitGroupInfo")
	public String newExitGroupInfo(Long orderId, String reviewId, String productType, Map<String, Object> map) {
		if (orderId != null) {
			// 订单信息
			ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
			// 团期信息
			ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
			// 产品信息
			TravelActivity product = travelActivityService.findById(productOrder.getProductId());
			map.put("productOrder", productOrder);
			map.put("product", product);
			map.put("productGroup", productGroup);
			// 团队类型
			map.put("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
			// 订单状态
			map.put("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
			// 产品类型
			map.put("productType", productType);
		} else {
			map.put("msg", "订单信息和产品信息没有获取到！");
		}
		// 游客退团信息
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		singleGroupOrderExitGroupService.handleAfterAndRefundEvery(reviewDetailMap);
		map.put("reviewDetail", reviewDetailMap);
		map.put("rid", reviewId);
		return "/review/prdreturn/singleGroup/newExitGroupInfo";
	}

	/**
	 * 提交保存退团申请，发起审批流程(验证流程互斥情况，只有当无互斥流程及其他流程性操作时，发起退团审批流程，否则返回错误信息)
	 * 
	 * @author yunpeng.zhang
	 * @createTime 2015年11月18日
	 * @param travelerIds
	 * @param exitReasons
	 *            退团原因
	 * @param afterExitGroupMoneys
	 *            退团后应收
	 * @param productType
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/startSingleGroupExitGroup")
	public Map<String, Object> startSingleGroupExitGroup(String[] travelerNames, String[] travelerIds,
			String[] exitReasons, String[] afterExitGroupMoneys, Integer productType, Long orderId, HttpServletRequest request) {
		// 要返回的result
		Map<String, Object> result = new HashMap<String, Object>();

		// 获取订单、团期、产品信息，并进行校验
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		if (productOrder == null) {
			result.put("reply", "订单信息获取失败！");
			result.put("msg", "faild");
			return result;
		}
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		if (productGroup == null) {
			result.put("reply", "团期信息获取失败！");
			result.put("msg", "faild");
			return result;
		}
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		if (product == null) {
			result.put("reply", "产品信息获取失败！");
			result.put("msg", "faild");
			return result;
		}

		// 获取部门 id
		Long deptId = product.getDeptId();
		if (deptId == null) {
			result.put("reply", "部门信息获取失败！");
			result.put("msg", "faild");
			return result;
		}

		Map<String, String[]> parameters = new HashMap<>();

		parameters.put("travelerNames", travelerNames);
		parameters.put("travelerIds", travelerIds);
		parameters.put("exitReasons", exitReasons);
		String afterExitGroupMoneyTemp = request.getParameter("afterExitGroupMoneys");
		String[] afterExitGroupMoneyso = afterExitGroupMoneyTemp.split("##");
		String[] refundMoneyso = request.getParameter("refundMoneys").split("##");
//		parameters.put("afterExitGroupMoneys", afterExitGroupMoneys);
		parameters.put("afterExitGroupMoneys", afterExitGroupMoneyso);
		parameters.put("refundMoneyso", refundMoneyso);
		// 发起退团申请
		Map<String, Object> returnResult = null;
		try {
			result = singleGroupOrderExitGroupService.startExitGroupAndHandleTravler(productOrder,
                    productGroup, product, parameters, deptId, productType, request);
		} catch (Exception e) {
			result.put("reply", e.getMessage());
			result.put("msg", "faild");
		}
		return result;
	}

	/**
	 * @author yunpeng.zhang
	 * @createDate 2015年12月16日12:01:57
	 * @param ids 要取消的 reivewIds
	 * @return
     */
	@ResponseBody
	@RequestMapping(value="/cancelOtherReview")
	public Map<String, Object> cancelOtherReview(String ids, String oldReviewIds){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(ids)){
			resultMap.put("success", "success");
			return resultMap;
		}
		//遍历取消旧审核流程
		if(StringUtils.isNoneBlank(oldReviewIds)) {
			String[] ordReviewIdArrs = oldReviewIds.split(",");
			for (String oldReivewId : ordReviewIdArrs) {
				oldReviewService.removeReview(Long.parseLong(oldReivewId));
				orderReviewService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
						"取消审核流程：" + oldReivewId, Context.log_state_del,  null, null);
			}
		}

		
		//遍历取消新审核流程
		String[] strings = ids.split(",");
		Long userId = UserUtils.getUser().getId();
		String uuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer msg = new StringBuffer();
		for(String reviewId : strings){
			ReviewNew reviewNew = reviewService.getReview(reviewId);
			Integer processType = Integer.parseInt(reviewNew.getProcessType());
			// 退团申请与转团、转款、退款、改价、返佣、借款互斥
			ReviewResult reviewResult = null;
			if(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP == processType) {
				// 转团没有后续的逻辑，所以直接取消
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
				
			} else if(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY == processType) {
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
				if (reviewResult.getSuccess()) {
					String travellerId = reviewNew.getTravellerId();
					//改变游客状态
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travellerId));
					traveler.setDelFlag(Integer.valueOf(Context.DEL_FLAG_NORMAL));
					travelerService.saveTraveler(traveler);
					//金额状态改变
					NewProcessMoneyAmount moneyAmount = newAmountService.findByReviewId(reviewId);
					moneyAmount.setDelFlag(Context.DEL_FLAG_DELETE);
				}
			} else if(Context.REVIEW_FLOWTYPE_CHANGE_PRICE == processType) {
				// 改价也没有后续的逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			} else if(Context.REBATES_FLOW_TYPE == processType) {
				// 返佣之后有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
				boolean flag = reviewResult.getSuccess();
				if (flag) {
					// 对成本录入进行更改
					ReviewNew review = reviewService.getReview(reviewId);
					commonReviewService.updateCostRecordStatus(review, 3);
				}
			} else if(Context.REVIEW_FLOWTYPE_BORROWMONEY == processType) {
				// 借款之后有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			} else if(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE == processType) {
				// 优惠之后没有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			}
			if(!reviewResult.getSuccess()){
				msg.append(reviewResult.getMessage());
			}
		}
		if(StringUtils.isBlank(msg)){
			resultMap.put("flag", "success");
			return resultMap;
		}
		resultMap.put("flag", "fail");
		resultMap.put("msg", msg);
		return resultMap;
	}

	/**
	 * 申请退团互斥验证
	 * @createDate 2015年12月15日17:58:31
	 * @param orderId
	 * @param productType
	 * @param travelerIds
     * @return
     */
	@ResponseBody
	@RequestMapping("/checkSingleGroupExitGroup")
	public Map<String, Object> checkSingleGroupExitGroup(String orderId, String productType, String travelerIds) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(travelerIds)) {
			resultMap.put("flag", false);
			resultMap.put("msg", "游客信息不能为空");
			return resultMap;
		}
		String[] tras = travelerIds.split(",");
		if (tras == null || tras.length == 0) {
			resultMap.put("flag", false);
			resultMap.put("msg", "游客信息不能为空");
			return resultMap;
		}
		//遍历调用接口组织返回的数据
		CommonResult checkResult = null;
		StringBuffer showMsg = new StringBuffer();
		//所有互斥的流程idlist
		String idStrings = "";
		String oldReviewIds = "";
		int nFlag = 0;
		int oFlag = 0;
		List<ReviewNew> reviewNews = new ArrayList<ReviewNew>();
		//是否互斥标志
		Boolean isMutexBoolean = false;
		//是否有取消权限
		Boolean isCanCancel = true;
		for(String travellerId : tras){
			//调用互斥接口
			checkResult = reviewMutexService.check(orderId, travellerId, productType, Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString(), false);
			if(!checkResult.getSuccess()){
				isMutexBoolean = true;
				Map<String, Object> params = checkResult.getParams();
				//互斥的审批流程list
				reviewNews = (List<ReviewNew>) params.get(ReviewMutexContext.PROCESSING_REVIEWS);
				if(reviewNews != null && reviewNews.size() > 0){
					for(ReviewNew r : reviewNews){
						if(nFlag == 0){
							idStrings += r.getId();
							nFlag++;
						} else {
							idStrings += "," + r.getId();
						}
					}
				}
				List<Review> oldProcessingReviews = (List<Review>)params.get(ReviewMutexContext.PROCESSING_OLD_REVIEWS);
				if(oldProcessingReviews != null && oldProcessingReviews.size() > 0){
					for(Review r : oldProcessingReviews){
						if(oFlag == 0){
							oldReviewIds += r.getId();
							oFlag++;
						} else {
							oldReviewIds += "," + r.getId();
						}
					}
				}
				
				showMsg.append(checkResult.getMessage() + "</br>");
				reviewNews = new ArrayList<ReviewNew>();
				//是否有取消权限
				Boolean canCancel = (Boolean) params.get("canCancel");
				if(!canCancel){
					isCanCancel = false;
				}
			}
			//清空互斥返回结果
			checkResult = null;
		}
		if(isCanCancel){
			showMsg.append("确定要取消以上流程，并发起退团流程吗？");
		} else {
			showMsg.append("请重新选择游客，或等待以上审批结束");
		}
		resultMap.put("showMsg", showMsg);
		resultMap.put("isMutexBoolean", isMutexBoolean);
		resultMap.put("canCancel", isCanCancel);
		resultMap.put("ids", idStrings);
		resultMap.put("oldReviewIds", oldReviewIds);
		return resultMap;
	}


	/**
	 * 跳转到申请退团页面
	 * 
	 * @author yunpeng.zhang
	 * @createTime 2015年11月18日
	 * @param id	订单id
	 * @param productType
	 * @param map
	 * @return
	 */
	@RequestMapping("/newExitGroupDetails")
	public String newExitGroupDetails(@RequestParam("id") Long id, @RequestParam("productType") Integer productType,
			Map<String, Object> map) {
		List<Map<Object, Object>> travelerList = null;
		ActivityGroup productGroup = null;
		TravelActivity product = null;
		// 如果订单id和产品类型不为空，获取符合退团要求的游客列表
		if (id != null && productType != null) {
//			travelerList = orderService.getTravelerByOrderId4ExitGroup(id, productType);
			travelerList = singleGroupOrderExitGroupService.getTravelerByOrderId4ExitGroup(id, productType);
		}
		ProductOrderCommon productOrder = orderService.getProductorderById(id);
		if (productOrder != null) {
			productGroup = activityGroupService.findById(productOrder.getProductGroupId());
			product = travelActivityService.findById(productOrder.getProductId());
		}

		map.put("orderId", id);
		map.put("flowType", Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		map.put("productType", productType);
		map.put("travelerList", travelerList);
		org.json.JSONArray travelerListJsonArray = orderService.handleRefundMoneyTravelerJson(travelerList);
		map.put("travelerListJsonArray", travelerListJsonArray);
		map.put("productOrder", productOrder);
		map.put("productGroup", productGroup);
		map.put("product", product);
		map.put("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		map.put("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));

		return "/review/prdreturn/singleGroup/newExitGroupDetails";
	}

	/**
	 * 跳转到退团记录页
	 * 
	 * @author yunpeng.zhang
	 * @createTime 2015年11月18日
	 * @param orderId 订单id
	 * @param productType 产品类型
 	 * @return
	 */
	@RequestMapping("/newExitGroupList")
	public String newExitGroupList(@RequestParam("orderId") Long orderId,
			@RequestParam("productType") Integer productType, Map<String, Object> map) {
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.valueOf(orderId));
		TravelActivity product = null;
		Long deptId = null;
		if (productOrder != null) {
			product = travelActivityService.findById(productOrder.getProductId());
			deptId = product.getDeptId();
		}
		// 从新的流程审批表中获取数据
		List<Map<String, Object>> processList = processReviewService.getReviewDetailMapListByOrderId(deptId,
				productType, Context.REVIEW_FLOWTYPE_EXIT_GROUP, orderId.toString(), OrderByPropertiesType.CREATE_DATE,
				OrderByDirectionType.DESC);
		map.put("processList", processList);
		// 处理退团后金额、退款金额显示
		singleGroupOrderExitGroupService.handleAfterAndRefund(processList);
		map.put("orderId", orderId);
		map.put("productType", productType);
		map.put("userId", UserUtils.getUser().getId());
		return "/review/prdreturn/singleGroup/newExitGroupList";
	}
	
	
	@ResponseBody
	@RequestMapping("/getMaxPriceUseSelectedCurrency")
	public Map<String, Object> getMaxPriceUseSelectedCurrency(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String travelerId = request.getParameter("travelerId");
		String selectedCurrencyId = request.getParameter("selectedCurrencyId");
		if (StringUtils.isBlank(travelerId)) {
			result.put("msg", "faild");
			result.put("data", "获取游客id失败");
			return result;
		}
		if (StringUtils.isBlank(selectedCurrencyId)) {
			result.put("msg", "faild");
			result.put("data", "获取币种id失败");
			return result;
		}
		List<MoneyAmount> travlerMoneyAmounts = travelerService.findTravelerPayPrice(Long.parseLong(travelerId));
		MoneyAmount maxPrice = MoneyAmountUtils.translateMultplyCurrency2Specified(travlerMoneyAmounts, selectedCurrencyId);
		if (maxPrice == null) {
			result.put("msg", "faild");
			result.put("data", "计算失败");
			return result;
		}
		result.put("msg", "success");
		result.put("data", maxPrice.getAmount());
		return result;
	}
	
	

}
