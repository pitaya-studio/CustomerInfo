package com.trekiz.admin.review.refund.singleGroup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quauq.review.core.engine.config.ReviewConstant;
import net.sf.json.JSONArray;

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
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.refund.singleGroup.service.ISingleGroupRefundService;

@Controller
@RequestMapping("${adminPath}/singleOrder/refund")
public class SingleGroupRefundController {
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private ISingleGroupRefundService singleGroupRefundService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	public ReviewMutexService reviewMutexService;

	/**
	 * 申请人取消退款
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年12月2日13:48:31
	 * @param reviewId
	 * @param travelerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancelGroupRefund")
	public Map<String, Object> cancelGroupRefund(String reviewId, Long travelerId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = new HashMap<>();

		ReviewNew reviewNew = processReviewService.getReview(reviewId);
		//如果是已驳回状态，则不进行取消处理
		if(ReviewConstant.REVIEW_STATUS_REJECTED == reviewNew.getStatus().intValue()){
			result.put("msg", "faild");
			result.put("repley", "该退款申请已被驳回");
			return result;
		}
		// 取消退款
		ReviewResult reviewResult = processReviewService.cancel(userId, companyId, "", reviewId, "", null);
		boolean flag = reviewResult.getSuccess();

		if (flag) { // 取消退款成功
			result.put("msg", "success");
			result.put("reply", reviewResult.getMessage());
			singleGroupRefundService.handleMoneyAmount(reviewId);
		} else { // 取消退款失败
			result.put("msg", "faild");
			result.put("repley", reviewResult.getMessage());
		}

		return result;
	}

	/**
	 * 退款详情
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年12月2日13:52:36
	 * @param orderId
	 * @param reviewId
	 * @param productType
	 * @param map
	 * @return
	 */
	@RequestMapping("/newGroupRefundInfo")
	public String newGroupRefundInfo(Long orderId, String reviewId, String productType, Map<String, Object> map) {
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
		// 游客退款信息
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		map.put("reviewDetail", reviewDetailMap);
		map.put("rid", reviewId);
		return "/review/refund/singleGroup/newGroupRefundInfo";
	}

	/**
	 * 申请退款
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年12月2日13:53:41
	 * @param refundRecordsStr
	 *            封装了游客和订单申请退款信息的 Json对象
	 * @param orderId
	 * @param productType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/startSingleGroupRefund")
	public Map<String, Object> startGroupRefund(String refundRecordsStr, Long orderId, Integer productType) {
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<RefundBean> refundRecords = JSONArray.toList(JSONArray.fromObject(refundRecordsStr), RefundBean.class);
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
		//执行申请退款操作
		Map<String, Object> returnResult = new HashMap<>();
		try {
			returnResult = singleGroupRefundService.startGroupRefundAndHandleTravler(productOrder,
                    productGroup, product, refundRecords, deptId, productType);
		} catch (Exception e) {
			returnResult.put("reply", e.getMessage());
			returnResult.put("msg", "faild");
		}

		return returnResult;
	}

	/**
	 * 进行退款互斥验证
	 * @createDate 2015年12月15日17:49:25
	 * @param travelerIds
	 * @param productType
	 * @param orderId
     * @return
     */
	@ResponseBody
	@RequestMapping("/checkSingleGroupRefund")
	public Map<String, Object> checkSingleGroupRefund(String refundRecordsStr, String productType, String orderId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<RefundBean> refundRecords = JSONArray.toList(JSONArray.fromObject(refundRecordsStr), RefundBean.class);
		//遍历调用接口组织返回的数据
		CommonResult checkResult = null;
		StringBuffer showMsg = new StringBuffer();
		//所有互斥的流程idlist
		String idStrings = "";
		int nFlag = 0;
		List<ReviewNew> reviewNews = new ArrayList<ReviewNew>();
		//是否互斥标志
		Boolean isMutexBoolean = false;
		//是否有取消权限
		Boolean isCanCancel = true;
		for(RefundBean refundBean : refundRecords){
			String travellerId = refundBean.getTravelerId();
			//调用互斥接口
			boolean isGroup = false;
			if("0".equals(travellerId.trim())) {
				isGroup = true;
			}
			checkResult = reviewMutexService.check(orderId, travellerId, productType, Context.REVIEW_FLOWTYPE_REFUND.toString(), isGroup);
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
				showMsg.append(checkResult.getMessage());
				reviewNews = new ArrayList<>();
			}

		}
		showMsg.append("请重新选择游客！");
		resultMap.put("showMsg", showMsg);
		resultMap.put("isMutexBoolean", isMutexBoolean);
		resultMap.put("ids", idStrings);
		return resultMap;
	}

	/**
	 * 跳转到退款申请页面
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年11月30日
	 * @param id
	 * @param productType
	 * @param map 要返回的 map
	 * @return
	 */
	@RequestMapping("/newExitRefundDetails")
	public String newGroupRefundDetails(@RequestParam("orderId") Long id,
			@RequestParam("productType") Integer productType, Map<String, Object> map) {
		// 查询该订单下的游客
		List<Map<Object, Object>> travelerList = null;
		// 订单基本信息和产品基本信息和团期基本信息
		ActivityGroup productGroup = null;
		TravelActivity product = null;

		if (id != null && productType != null) {
			travelerList = orderService.getTravelerByOrderId4ExitRefund(id, productType);
		}
		ProductOrderCommon productOrder = orderService.getProductorderById(id);
		if (productOrder != null) {
			productGroup = activityGroupService.findById(productOrder.getProductGroupId());
			product = travelActivityService.findById(productOrder.getProductId());
		}
		// 币种列表
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());

		map.put("orderId", id);
		map.put("flowType", Context.REVIEW_FLOWTYPE_REFUND);
		map.put("productType", productType);
		map.put("travelerList", travelerList);
		map.put("productOrder", productOrder);
		map.put("productGroup", productGroup);
		map.put("product", product);
		map.put("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		map.put("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		map.put("currencyList", currencyList);

		return "/review/refund/singleGroup/newGroupRefundDetails";
	}

	/**
	 * 退款记录列表
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年11月30日
	 * @param productType
	 * @param orderId
	 * @param map
	 * @return
	 */
	@RequestMapping("/newGroupRefundList")
	public String newGroupRefundList(Integer productType, Long orderId, Map<String, Object> map) {
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.valueOf(orderId));
		TravelActivity product = null;
		Long deptId = null;
		if (productOrder != null) {
			product = travelActivityService.findById(productOrder.getProductId());
			deptId = product.getDeptId();
		}
		// 从新的流程审批表中获取退款数据
		List<Map<String, Object>> processList = processReviewService.getReviewDetailMapListByOrderId(deptId,
				productType, Context.REVIEW_FLOWTYPE_REFUND, orderId.toString(), OrderByPropertiesType.CREATE_DATE,
				OrderByDirectionType.DESC);
		map.put("processList", processList);
		map.put("orderId", orderId);
		map.put("productType", productType);
		return "/review/refund/singleGroup/newGroupRefundList";
	}

}
