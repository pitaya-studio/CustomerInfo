package com.trekiz.admin.review.airticketChange.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.params.Params;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.airticketChange.service.INewAirticketChangeService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;



/**
 * 新审批流程机票改签
 * @author songyang 2015年11月12日15:52:03
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/airticketChange/airChange")
public class NewAirticketChangeController {

	private static final Logger log = Logger.getLogger(NewAirticketChangeController.class);
	
	private static final int CHANGE_PRODUCTTYPE_AIRTICKET = 7;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private AreaService areaService;

	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private AirportInfoService airportInfoService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private INewAirticketChangeService iNewAirticketChangeService;
	
	@Autowired
	MoneyAmountService  moneyAmountService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService reviewService;
	
	@Autowired
	private com.trekiz.admin.modules.reviewflow.service.ReviewService reviewService2;
	/**
	 * 进入申请列表
	 * by 宋扬2015年11月12日16:14:07
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value="airticketApprovalHistoryList")
	public String airticketApprovalHistoryList(Model model,HttpServletRequest request,HttpServletResponse response){
		Params params = new Params(request);
		
		//测试
//		List<MoneyAmount> newOrderTotalPriceList = moneyAmountService.decreaseMoney("596bf314-67d8-4f5e-9b38-cb672dff4b8a","724cdfba-5487-420f-945f-645e900279b3");
//		List<MoneyAmount> mList = new ArrayList<MoneyAmount>();
//		String scheduleBackUuid = UuidUtils.generUuid();
//		for (int i = 0; i < newOrderTotalPriceList.size(); i++) {
//			Integer currencyId  = newOrderTotalPriceList.get(i).getCurrencyId();
//			BigDecimal totprice = newOrderTotalPriceList.get(i).getAmount();
//			Currency currency = currencyService.findCurrency(currencyId.longValue());
//			BigDecimal ex = currency.getCurrencyExchangerate();
//			MoneyAmount moneyAmount = new MoneyAmount(scheduleBackUuid,currencyId,totprice , 14, 7, Context.BUSINESS_TYPE_ORDER, UserUtils.getUser().getId(),ex);
//			mList.add(moneyAmount);
//		}
//		moneyAmountService.saveMoneyAmounts(mList);
		//测试
		
		model.addAttribute("deparCity",areaService.findFromCityList(""));
		model.addAttribute("area",areaService.findAirportCityList(""));
		model.addAttribute("params", params);
		String orderId = params.get("orderId").toString();
		Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketOrderService.getAirticketorderById(Long.valueOf(orderId)).getAirticketId()).getDeptId();
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, Context.ORDER_TYPE_JP, Context.REVIEW_FLOWTYPE_VISA_CHANGE, orderId,OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		model.addAttribute("page", processList);
		return "review/change/airticketchange/airticketApprovalHistoryList";
	}
	
	
	
	/**
	 * 根据订单id查询订单详情
	 * by
	 * songyang 2015年11月23日20:16:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="airticketOrderDetail")
	public String queryOrderDetailById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoByIdAddcontacts(orderId);
		//FIXME HPT~ 机票订单详情修改
			// ============== 订单详情修改 开始
		if(null != orderDetailInfoMap.get("mainOrderId")){
			String mainOrderId = String.valueOf(orderDetailInfoMap.get("mainOrderId")); // 主订单id 如果该订单类型是参团 则录入主订单id
			ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(mainOrderId)); 
			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId()); 
			model.addAttribute("productOrder", productOrder);
			model.addAttribute("activityGroup", activityGroup);
		}
	
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
			// ============== 订单详情修改 结束
		//by sy  2015.7.15
		if(null != orderDetailInfoMap.get("mainOrderId")){
			String mainorderId = orderDetailInfoMap.get("mainOrderId").toString();
			String groupNum = activityAirTicketService.getActivitygroupById(new Long(mainorderId));
			ProductOrder  porder=	activityAirTicketService.getProductById(new Long(mainorderId));
			model.addAttribute("porder", porder);
			model.addAttribute("groupNum", groupNum);
		}
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("operate", UserUtils.getUser().getName());
	    String reviewId = request.getParameter("reviewId");
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审批的标志
			
	    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
	    	
	    	model.addAttribute("rLog",rLog);
	    }
	    return "review/change/airticketchange/airticketOrderDetail";
	}
	
	
	
	
	/**
	 * 跳转到申请页
	 * by 宋扬 2015年11月12日15:55:16
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="airticketOrderDetailChange")
	public String queryChangeOrderDetailById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("operate", UserUtils.getUser().getName());
		
		//----------以下代码为获取出发城市名称
		String fromAreaId = (String) orderDetailInfoMap.get("departureCity");
		String fromArea = "";
		List<Dict> list = areaService.findFromCityList("");
		for(Dict t : list){
			if((t.getId().toString()).equals(fromAreaId)){
				fromArea = t.getLabel();
			}
		}
		model.addAttribute("fromArea",fromArea);//出发城市
		
		return "review/change/airticketchange/airticketOrderChange";
	}
	
	
	/**
	 * 检查能否改签
	 * by 宋扬 2015年11月12日17:09:26
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("all")
	@RequestMapping(value="areaGaiQianCheck")
	public String areaGaiQianCheck(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		String travelerIds = request.getParameter("travelerIds");
		String orderId = request.getParameter("orderId");
		//String oldProId = request.getParameter("oldProId");
		String newProId = request.getParameter("newProId");
//		ReviewService reviewService = SpringContextHolder.getBean("reviewService");
		String[] tIds = travelerIds.split(",");
//		Detail d = new Detail();
		String ids="";
		for(int i=0;i<tIds.length;i++){
			if(i==tIds.length-1){
				ids +=tIds[i];
			}else{
				ids +=tIds[i]+",";
			}
		}
		Map map = new HashMap();
		map.put("ids", ids);
		map.put("orderId", orderId);
		List list = airTicketOrderService.areaGaiQianCheck(map);
		Map jsonMap = new HashMap();
		String names="";
//		if(list==null||list.size()==0){
		if(1==1){
			 jsonMap.put("result", "1");
		}else{
			 for(int i=0;i<list.size();i++){
				 String tid = ((Map)list.get(i)).get("travelerId").toString();
				 //User u = UserUtils.getUser();
				 TravelerService ts = SpringContextHolder.getBean("travelerService");
				 Traveler t = ts.findTravelerById(Long.valueOf(tid));
				 names += t.getName()+",";
			 }
			 jsonMap.put("result", "0");
			 jsonMap.put("msg", names+"正在审批");
		
		}
		String data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
		return null;
	}
	
	/**
	 * 流程互斥
	 * add by songyang 二〇一五年十二月十一日 15:44:12
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value="airticketChangeCheck")
	public void airticketChangeCheck(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		Map<String , String > jsonMap = new HashMap<String, String>();
		String data = "";
		Boolean isGroup = false;
		String travelerIds = request.getParameter("travelerIds");
		String orderId = request.getParameter("orderId");
		String[] tld = travelerIds.split(",");
		List<String> tIdsList = new ArrayList<String>();
		for (String tIdsStr : tld) {
			tIdsList.add(tIdsStr);
		}
		CommonResult  commonResult =  reviewMutexService.check(orderId, tIdsList, Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_AIRTICKET_CHANGE);
		if(commonResult.getSuccess()){
			jsonMap.put("result", "0");
		}else{
			jsonMap.put("message", commonResult.getMessage());
		}
		data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
	}
	;
	
	/**
	 * 改签审批申请
	 * by 宋扬2015年11月12日17:10:21
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("all")
	@RequestMapping(value="areaGaiQian")
	public void areaGaiQian(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		String travelerIds = request.getParameter("travelerIds");
		String orderId = request.getParameter("orderId");
		String newProId = request.getParameter("newProId");
		String[] tIds = travelerIds.split(",");
		StringBuffer reply= new StringBuffer("");
		String data="";
		Map jsonMap = new HashMap();
//		Long result=0L;
		Long deptId=0L;
		deptId = activityAirTicketService.getActivityAirTicketById(airTicketOrderService.getAirticketorderById(Long.valueOf(orderId)).getAirticketId()).getDeptId();
		//机票产品查询
		ActivityAirTicket newactivityAirTicket = activityAirTicketService.getActivityAirTicketById(Long.parseLong(newProId));
		//机票订单查询
		AirticketOrder airOrder  =airTicketOrderService.getAirticketorderById(Long.valueOf(orderId));
		
		Traveler traveler = null;
		
		ReviewResult result = null;
		
		//判断流程互斥是否是团队
		Boolean  isGroup  = false; 
		
		List<String> tIdsList = new ArrayList<String>();
		for (String tIdsStr : tIds) {
			tIdsList.add(tIdsStr);
		}
		List<ReviewNew> reviewNews = new ArrayList<ReviewNew>();
		int nFlag = 0;
		//所有互斥的流程idlist
		String idStrings = "";
		String oldStrings = "";
		//流程互斥判断
		CommonResult commonResult = reviewMutexService.check(orderId, tIdsList, Context.ORDER_STATUS_AIR_TICKET,  Context.REVIEW_FLOWTYPE_AIRTICKET_CHANGE);
		//是否有取消权限
		Boolean isCanCancel = true;
		//是否互斥标志
		Boolean isMutexBoolean = false;
//		CommonResult commonResult = reviewMutexService.check(orderId, tIds[j], Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_AIRTICKET_CHANGE, isGroup);
		if(commonResult.getSuccess()){
			try {
				 for(int i=0;i<tIds.length;i++){
						//保存数据变量
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("newProId", newProId);
						//新出发城市
						variables.put("newdepartureCity", newactivityAirTicket.getDepartureCity());
						//新到达城市
						variables.put("newArrivedCity", newactivityAirTicket.getArrivedCity());
						//根据游客ID查询
						traveler = travelerService.findTravelerById(Long.parseLong(tIds[i]));
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName());
						//出发机场
						String leaveAirport  = newactivityAirTicket.getFlightInfos().get(0).getLeaveAirport();
						String leaveAirportName = airportInfoService.getAirportInfo(Long.parseLong(leaveAirport)).getAirportName();
						//到达机场
						String destinationAirpost = newactivityAirTicket.getFlightInfos().get(newactivityAirTicket.getFlightInfos().size()-1).getDestinationAirpost();
						String destinationAirpostName = airportInfoService.getAirportInfo(Long.parseLong(destinationAirpost)).getAirportName();
						Long agentId = airOrder.getAgentinfoId();
						//根据渠道ID获取渠道信息
						Agentinfo agentInfo = agentinfoService.loadAgentInfoById(agentId);
						if(agentId!=-1){
							variables.put("agentName", agentInfo.getAgentName());
						}else{
							variables.put("agentName", "非签约渠道");
						}
//						variables.put("startDate", newactivityAirTicket.getStartingDate());
						variables.put("startDate", newactivityAirTicket.getFlightInfos().get(0).getStartTime());
						variables.put("leaveAirport", leaveAirportName);
						variables.put("destinationAirpost", destinationAirpostName);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, tIds[i]);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, newactivityAirTicket.getCreateBy().getName());
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, "机票改签");
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.ORDER_STATUS_AIR_TICKET);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, newProId);
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, airOrder.getSalerId());
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME,airOrder.getSalerName());
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, airOrder.getOrderNo());
						//应付金额
						if (StringUtils.isNotBlank(traveler.getPayPriceSerialNum())) {
							String money = moneyAmountService.getMoney(traveler.getPayPriceSerialNum());
							variables.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, money); 
						}
						//转出团
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicketService.getActivityAirTicketById(airTicketOrderService.getAirticketorderById(Long.valueOf(orderId)).getAirticketId()).getGroupCode());
						//转入团
						variables.put(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_CODE, newactivityAirTicket.getGroupCode());
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, newactivityAirTicket.getCreateBy().getId());
						variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, airOrder.getCreateBy().getId());
						//发送请求
						result = processReviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),userReviewPermissionChecker,null, CHANGE_PRODUCTTYPE_AIRTICKET , Context.REVIEW_FLOWTYPE_VISA_CHANGE, deptId , "", variables);
						if(result.getReviewStatus()==2){
							//审批通过之后执行的业务操作  (forbug 机票改签余位不归还 放开这段代码)
							iNewAirticketChangeService.planeReviewNew(result.getReviewId());
						}
					 }
			} catch (Exception e) {
			    e.printStackTrace();
	            throw new Exception(e.getMessage());
			}
			 //返回结果
			 Boolean rResult = result.getSuccess(); 
			 if(rResult){
				 jsonMap.put("result", "申请成功");
			 }else{
				 if(ReviewErrorCode.ERROR_CODE_MSG_CN.containsKey(result.getCode())){
					jsonMap.put("result", ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode()));
				 }
			 }
		}else{
			isMutexBoolean = true; 
			Map<String, Object> params = commonResult.getParams();
			 //互斥的审核流程list
			 //新流程
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
			 //旧流程
			 nFlag = 0;
			 List<Review> oldProcessingReviews=(List)params.get(ReviewMutexContext.PROCESSING_OLD_REVIEWS);//旧的在审状态的审批记录列表
			 if(oldProcessingReviews != null && oldProcessingReviews.size() > 0){
				for(Review r : oldProcessingReviews){
					if(nFlag == 0){
						oldStrings += r.getId();
						nFlag++;
					} else {
						oldStrings += "," + r.getId();
					}
				}
			 }
			 jsonMap.put("message", commonResult.getMessage());
			 reviewNews = new ArrayList<ReviewNew>();
			 //是否有取消权限
		 	 Boolean canCancel = (Boolean) params.get("canCancel");
			 if(!canCancel){
				isCanCancel = false;
			 }
		}
		if(isCanCancel){
			jsonMap.put("message", commonResult.getMessage()+"</br>"+"确认取消其它审核并发起改签吗?");
		} else {
			jsonMap.put("message", commonResult.getMessage()+"</br>请重新选择游客，或等待以上审批结束");
		}
		
		jsonMap.put("ids", idStrings);
		jsonMap.put("oids", oldStrings);
		jsonMap.put("canCancel", isCanCancel);
		jsonMap.put("isMutexBoolean", isMutexBoolean);
		data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
	}
	
	/**
	 * 取消互斥的申请
	 * by 宋扬 二〇一五年十二月二十三日 14:44:24
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	
	@ResponseBody
	@RequestMapping(value="cancelOtherReview")
	public Map<String, Object> cancelOtherReview(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取要取消的ids
		String ids = request.getParameter("ids");
		String oids = request.getParameter("oids");
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
		if(!StringUtils.isBlank(oids)){
			String[] strings = oids.split(",");
			for(String str : strings ){
				reviewService2.CancelReview(Long.parseLong(str));
			}
		}
	
		if(StringUtils.isBlank(msg)){
			resultMap.put("success", "success");
		}
		resultMap.put("msg", msg);
		return resultMap;
	}
	

	/**
	 * 取消申请
	 * by 宋扬
	 * 2015年11月23日20:46:20
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@RequestMapping(value="cancelApp")
	public void cancelApp(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Params p = new Params(request);
//		r.setStatus(new Integer(4));
		Map jsonMap = new HashMap();
		ReviewResult r = null;
		String rid = request.getParameter("reviewId");
		Integer status = Integer.parseInt(request.getParameter("status"));
		if(status!=1){
			jsonMap.put("result", "当前状态不许取消");
		}
		r =processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(), "", rid, "", null);
		if(r.getSuccess()){
			jsonMap.put("result", "操作成功");
		}else{
			jsonMap.put("result", "操作成功");
		}
		String data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
	}
	
	
	
	/**
	 * 进入审批操作页面
	 * by sy 二〇一五年十二月五日 15:09:12
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	
	@SuppressWarnings("all")
	@RequestMapping(value = "airticketApprovalDetail")
	public String airticketApprovalDetail(Model model,
			HttpServletRequest request, HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		String trvalerId = request.getParameter("trvalerId");
		String reviewId = request.getParameter("reviewId");
		
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		orderDetailInfoMap.put("travlerDetail",iNewAirticketChangeService.queryApprovalDetailTravel(request, response, reviewId,orderId));
		//Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId,trvalerId);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
    //	model.addAttribute("from_areaslist", areaService.findFromCityList(""));//出发城市
	//	model.addAttribute("arrivedCitys", areaService.findAirportCityList2("")); //到达城市
		model.addAttribute("from_areaslist",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("orderId", orderId);
		model.addAttribute("trvalerId", trvalerId);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("isReview", request.getParameter("isReview"));
		return "review/change/airticketchange/airticketApprovalDetail";
	}
	
	
	/**
	 * 审批通过或者驳回
	 * by sy 二〇一五年十二月五日 15:09:32
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value = "planeReview")
	public void planeReview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reviewId = request.getParameter("reviewId");
		String operate = request.getParameter("operate");
		String reason = request.getParameter("reason");
		ReviewResult r = null;
		Map jsonMap = new HashMap();
		//operate = 1 审批通过， operate = 0驳回
		if(operate.equals("1")){
			r=processReviewService.approve(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),"", userReviewPermissionChecker, reviewId,reason, null);
			if(r.getReviewStatus()==2){
				//审批通过之后执行的业务操作
				iNewAirticketChangeService.changeOrExit(reviewId, null, null);
			}
			if(r.getSuccess()){
				jsonMap.put("result", "审批成功");
			}else{
				jsonMap.put("result", "审批失败");
			}
		}else if(operate.equals("0")){
			r=processReviewService.reject(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid(), "", reviewId, reason, null);
			if(r.getSuccess()){
				jsonMap.put("result", "驳回操作成功");
			}else{
				jsonMap.put("result", "驳回操作失败");
			}
		}
		
		 String data = JSONObject.fromObject(jsonMap).toString();
		 ReviewUtils.printJSON(data, response);
		
	}
}
