package com.trekiz.admin.review.prdreturn.airticket.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.airticketChange.service.NewAirticketChangeServiceImpl;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.prdreturn.airticket.bean.AirticketReturn;
import com.trekiz.admin.review.prdreturn.airticket.service.IAirTicketReturnNewService;
import com.trekiz.admin.review.prdreturn.common.service.IProductReturnService;

@Controller
@RequestMapping(value="${adminPath}/airticketreturnnew")
public class AirticketReturnNewController {

	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private NewAirticketChangeServiceImpl newAirticketChangeService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private IAirTicketReturnNewService airTicketReturnService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	@Autowired
	private com.trekiz.admin.modules.reviewflow.service.ReviewService reviewService2;
	
	@Autowired
	private IProductReturnService productReturnService;
	
	/**
	 * 申请退票（发起退票审核）
	 * @throws Exception 
	 */
	@RequestMapping(value = "startAirticketReturn")
//	@ResponseBody
	public String startAirticketReturn(Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 退票原因数组
		String[] reasons = request.getParameterValues("returnReason");
		// 旅客id数组
		String[] tIds = request.getParameterValues("travelerId");
		// checkbox数组
		String activityIds = request.getParameter("activityIds");
		// orderId
		String orderId = request.getParameter("orderId");
		// productType产品类型
		Integer productType = Integer.parseInt(request.getParameter("productType"));
		Map<String, Object> result = new HashMap<String, Object>();
		String[] temps = activityIds.split(",");
		AirticketOrder airticketorder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		if(airticketorder == null){
			result.put("reply", "订单信息有误");
			result.put("success", false);
			model.addAttribute("err", "订单信息有误");
			return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnDetail?orderId=" + orderId + "&err = " + "订单信息有误";
		}
		Long airticketId = airticketorder.getAirticketId();
		if(airticketId == null){
			result.put("reply", "产品信息有误");
			result.put("success", false);
			model.addAttribute("err", "产品信息有误");
			return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnDetail?orderId=" + orderId + "&err = " + "产品信息有误";
		}
		ActivityAirTicket activityAirTicket = activityAirTicketService.findById(airticketId);
		if(activityAirTicket == null){
			result.put("reply", "产品信息有误");
			result.put("success", false);
			model.addAttribute("err", "产品信息有误");
			return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnDetail?orderId=" + orderId + "&err = " + "产品信息有误";
		}
		Long deptId = activityAirTicket.getDeptId();
		if(deptId == null){
			result.put("reply", "部门信息有误");
			result.put("success", false);
			model.addAttribute("err", "部门信息有误");
			return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnDetail?orderId=" + orderId + "&err = " + "部门信息有误";
		}
		for (int i = 0; i < temps.length; i++) {
			if (temps[i] == null || "".equals(temps[i])) {
				continue;
			}
			if ("1".equals(temps[i])) {
				// 组织review数据
				Long travelerId = Long.parseLong(tIds[i]);
				String createReason = reasons[i];
				/*变量*/
				Map<String, Object> variables = new HashMap<String, Object>();
				//流程类型
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);
				//产品类型
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.ProductType.PRODUCT_AIR_TICKET);
				//渠道
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, airticketorder.getAgentinfoId());
				//销售
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, airticketorder.getSalerId());
				//计调
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, activityAirTicket.getCreateBy().getId());
				//下单人
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, airticketorder.getCreateBy().getId());
				//团号
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, airticketorder.getGroupCode());
				//订单编号
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, airticketorder.getOrderNo());
				//产品id
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, airticketId);
				//产品名称
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());
				//订单id
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
				//备注
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, createReason);
				//部门id
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
				//游客id
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
				//游客名称
				Traveler traveler = travelerService.findTravelerById(travelerId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler == null ? "" : traveler.getName());
				ReviewResult reviewResult = reviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),userReviewPermissionChecker, null, productType, Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN, deptId, "", variables);
				if(!reviewResult.getSuccess()){
					result.put("reply", reviewResult.getMessage());
					result.put("success", false);
					model.addAttribute("err", reviewResult.getMessage());
					return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnDetail?orderId=" + orderId + "&err = " + reviewResult.getMessage();
				}
				if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){//如果审核直接通过，则调用退票接口处理退票业务 2015年12月30日11:42:07
					newAirticketChangeService.changeOrExit(null, orderId, travelerId.toString());
				}
			}
		}
		result.put("success", true);
		return "redirect:"+Global.getAdminPath()+"/airticketreturnnew/airticketReturnList?orderId=" + orderId + "&flowType=3&orderType=7";
	}

	/**
	 * 根据订单id 业务类型orderType 流程类型 flowType active： 是否只查询 active=1的记录 查询退票审核列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketReturnList")
	public String queryTicketReturnList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		String flowType = request.getParameter("flowType");
		List<AirticketReturn> airticketReturnList = getAirticketReturnReviewList(orderId, orderType, flowType);
		model.addAttribute("orderId", orderId);
		model.addAttribute("airticketReturnList", airticketReturnList);
		return "review/prdreturn/airticket/airticketReturnList";
	}
	
	private List<AirticketReturn> getAirticketReturnReviewList(String orderId, String orderType, String flowType) {
		
		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		if(airticketOrder == null){
			return null;
		}
		Long airticketId = airticketOrder.getAirticketId();
		if(airticketId == null){
			return null;
		}
		ActivityAirTicket airTicket = activityAirTicketService.getActivityAirTicketById(airticketId);
		if(airTicket == null){
			return null;
		}
		Long deptId = airTicket.getDeptId();
		if(deptId == null){
			return null;
		}
		List<Map<String, Object>> airticketReturnReviewList = reviewService.getReviewDetailMapListByOrderId(deptId, Context.ProductType.PRODUCT_AIR_TICKET, Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN, orderId, null, null);
		List<AirticketReturn> airticketReturnList = new ArrayList<AirticketReturn>();
		if (airticketReturnReviewList != null// 如果审核记录为空或者size为0则无记录 不走下面的操作
				&& airticketReturnReviewList.size() != 0) {
			for (Map<String, Object> r : airticketReturnReviewList) {
				AirticketReturn temp = new AirticketReturn();
				temp.setId(r.get("id").toString());
				temp.setProductType(Context.ProductType.PRODUCT_AIR_TICKET);
				temp.setOrderId(Long.valueOf(orderId));
				temp.setFlowType(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);
				temp.setTravelerId(Long.parseLong(r.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString()));
				temp.setCreateReason(r.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK) == null ? null : r.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK).toString());
				temp.setDenyReason(r.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_DENY_REASON) == null ? null : r.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_DENY_REASON).toString());
				temp.setCreateBy(Long.parseLong(r.get("createBy").toString()));
				temp.setCreateDate(r.get("createDate") == null ? null : DateUtils.dateFormat(r.get("createDate").toString()));
				temp.setUpdateDate(r.get("updateDate") == null ? null : DateUtils.dateFormat(r.get("updateDate").toString()));
				temp.setStatus(Integer.parseInt(r.get("status").toString()));
				if(temp.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING){
					Object cReviewer = r.get("currentReviewer");
					if(cReviewer == null || StringUtils.isBlank(cReviewer.toString())){
						temp.setStatusDesc("未分配审核人");
					} else {
						String person = "";
						if(cReviewer != null){
							person = getReviewerDesc(cReviewer);
						}
						temp.setStatusDesc("待" + person + "审核");
						
					}
				} else {
					temp.setStatusDesc(getReviewStatus(temp.getStatus()));
				}
				Traveler travelerTemp = travelerService.findTravelerById(temp.getTravelerId());
				temp.setOrderCreateDate(airticketOrder.getCreateDate());
				// 新加了多币种处理 add by chy 2014年11月28日16:02:43
				String srcPrice = moneyAmountService.getMoney(travelerTemp.getPayPriceSerialNum());
				if(StringUtils.isBlank(srcPrice)){
					srcPrice = Context.CURRENCY_MARK_RMB + " 0.00";
				}
				temp.setPayPrice(srcPrice);//
				airticketReturnList.add(temp);
			}
		}
		return airticketReturnList;
	}

	private String getReviewStatus(Integer status) {
		if(ReviewConstant.REVIEW_STATUS_CANCELED == status){
			return ReviewConstant.REVIEW_STATUS_CANCELED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_PASSED == status){
			return ReviewConstant.REVIEW_STATUS_PASSED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_REJECTED == status){
			return ReviewConstant.REVIEW_STATUS_REJECTED_DES;
		} 
		return "无";
	}

	/**
	 * 获取当前审核人描述 由id转化为name
	 * @param cReviewer
	 * @return
	 */
	private String getReviewerDesc(Object cReviewer) {
		String reviewers = cReviewer.toString();
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for(String temp : reviewArr){
			if(StringUtils.isBlank(temp)){
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if(n == 0){
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		return result;
	}
	
	/**
	 * 取消游客退票申请
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "cancelTravlerReturnRequest")
	@ResponseBody
	public Map<String, Object> cancelTravlerReturnRequest(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String revid = request.getParameter("revid");
		ReviewResult reviewResult = reviewService.cancel(UserUtils.getUser().getId().toString(),  UserUtils.getUser().getCompany().getId().toString(), "", revid, "", null);
		Map<String, Object> result = new HashMap<String, Object>();
		if(!reviewResult.getSuccess()){
			result.put("success", false);
			result.put("err", reviewResult.getMessage());
		} else {
			result.put("success", true);
		}
		return result;
	}
	
	/**
	 * 根据订单id查询退票详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketReturnDetail")
	public String queryOrderDetail(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String err = request.getParameter("err");
		String orderId = request.getParameter("orderId");
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService
				.queryAirTicketReturnInfoById(orderId);
		@SuppressWarnings({"unchecked"})
		List<Map<String, Object>> travelInfoList = (List<Map<String, Object>>) airticketReturnDetailInfoMap
				.get("travelInfoList");
		// 新加了多币种处理 add by chy 2014年11月28日16:02:43
		String srcmoney = "";
		if(travelInfoList == null){
			travelInfoList = new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> temp : travelInfoList) {
			String srcPrice = temp.get("payPrice") == null ? null : temp.get(
					"payPrice").toString();
			srcmoney = moneyAmountService.getMoney(srcPrice);
			temp.remove("payPrice");
			temp.put("payPrice", srcmoney);
		}
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		airticketReturnDetailInfoMap.remove("travelInfoList");
		airticketReturnDetailInfoMap.put("travelInfoList", travelInfoList);
		model.addAttribute("airticketReturnDetailInfoMap", airticketReturnDetailInfoMap);
		model.addAttribute("err", err);
		return "review/prdreturn/airticket/airticketReturnDetail";
	}
	
	/**
	 * 退票申请的互斥验证
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="beforeAddReview")
	public Map<String, Object> beforeAddReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取订单的id
		String orderId = request.getParameter("orderId");
		//获取游客的ids
		String travelerids = request.getParameter("travelerids");
		if (StringUtils.isBlank(travelerids)) {
			resultMap.put("flag", false);
			resultMap.put("msg", "游客信息不能为空");
			return resultMap;
		}
		String[] tras = travelerids.split(",");
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
		String idStringsOld = "";
		int nFlag = 0;
		List<ReviewNew> reviewNews = new ArrayList<ReviewNew>();
		List<Review> reviewOlds = new ArrayList<Review>();
		//是否互斥标志
		Boolean isMutexBoolean = false;
		//是否有取消权限
		Boolean isCanCancel = true;
		for(String travellerId : tras){
			//调用互斥接口
			checkResult = reviewMutexService.check(orderId, travellerId, Context.PRODUCT_TYPE_AIRTICKET.toString(), Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString(), false);
			if(!checkResult.getSuccess()){
				isMutexBoolean = true;
				Map<String, Object> params = checkResult.getParams();
				//互斥的审核流程list
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
				nFlag = 0;
				showMsg.append(checkResult.getMessage() + "<br/>");
				reviewNews = new ArrayList<ReviewNew>();
				reviewOlds =(List<Review>)params.get(ReviewMutexContext.PROCESSING_OLD_REVIEWS);//旧的在审状态的审批记录列表
				if(reviewOlds != null && reviewOlds.size() > 0){
					for(Review r : reviewOlds){
						if(nFlag == 0){
							idStringsOld += r.getId();
							nFlag++;
						} else {
							idStringsOld += "," + r.getId();
						}
					}
				}
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
			showMsg.append("确认取消以上流程，并发起退票审批流程");
		} else {
			showMsg.append("请重新选择游客，或等待以上审批结束");
		}
		resultMap.put("showMsg", showMsg);
		resultMap.put("isMutexBoolean", isMutexBoolean);
		resultMap.put("canCancel", isCanCancel);
		resultMap.put("ids", idStrings);
		resultMap.put("ids2", idStringsOld);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="cancelOtherReview")
	public Map<String, Object> cancelOtherReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取要取消的ids
		String ids = request.getParameter("ids");
		String idStringsOld = request.getParameter("ids2");
		StringBuffer msg = new StringBuffer();
		if(!StringUtils.isBlank(ids)){
			//遍历取消
			String[] strings = ids.split(",");
			Long userId = UserUtils.getUser().getId();
			String uuid = UserUtils.getUser().getCompany().getUuid();
			ReviewResult cancel;
			for(String str : strings){
				cancel = reviewService.cancel(userId.toString(), uuid, null, str, null, null);
				if(!cancel.getSuccess()){
					msg.append(cancel.getMessage());
				}
			}
		}
		if(!StringUtils.isBlank(idStringsOld)){
			//遍历取消
			String[] strings = idStringsOld.split(",");
			for(String str : strings){
				reviewService2.CancelReview(Long.parseLong(str));
			}
		}
		if(StringUtils.isBlank(msg)){
			resultMap.put("success", "success");
			return resultMap;
		}
		resultMap.put("success", "fail");
		resultMap.put("msg", msg);
		return resultMap;
	}
}
