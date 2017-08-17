package com.trekiz.admin.review.borrowing.airticket.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.review.borrowing.airticket.formbean.NewBorrowingBean;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

/**
 * 机票类借款审批控制类
 * @author 宋扬
 */
@Controller
@RequestMapping(value = "${adminPath}/activityOrder/lendmoney")
public class ActivityAirTicketOrderLendMoneyController {

	private static final int BORROWING_PRODUCTTYPE_AIRTICKET = 7;
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private AirlineInfoService airlineInfoService;

	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	/**
	 * 新行者机票订单借款明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyList")
	public String airticketLendMoneyList(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return "review/borrowing/airticket/airticketLendMoneyList";
	}
		/**
		 * 

		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws ParseException
		 */
		@RequestMapping(value="airticketLendMoneyByReviewId")
		public String airticketLendMoneyByReviewId(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
			String orderId = request.getParameter("orderId");
			String reviewId = request.getParameter("reviewId");
			
			User user = UserUtils.getUser();
			Long companyId = user.getCompany().getId();
			Map<String, Object> orderDetail = queryAirticketOrderDetailById(orderId);//查询订单明细
			List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
			Map<String,Object>  processMap = null;
			try{
				if(reviewId!=null){
					processMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
					rdlist.add(processMap);
				}
			}catch(Exception e){
				log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
			}
			//将总金额和币种显示出来
			List<Map<String,Object>> borrowPricesList = new ArrayList<Map<String,Object>>();
			Map<String,Object> borrowPricesMap = new HashMap<String, Object>();
//			rdlist.get(0).get("borrowPrices");
//			rdlist.get(0).get("borrowRemark");
			
			if(rdlist!=null&&rdlist.size()>0){
				for(int i = 0;i<rdlist.size();i++){
					if(rdlist.get(i).containsKey("borrowPrices")){
						borrowPricesMap.put("borrowPrices", rdlist.get(i).get("borrowPrices").toString());
					}
					if(rdlist.get(i).containsKey("currencyMarks")){
						borrowPricesMap.put("currencyMarks", rdlist.get(i).get("currencyMarks").toString());
					}
					if(rdlist.get(i).containsKey("borrowRemark")){
						model.addAttribute("borrowRemark",rdlist.get(i).get("borrowRemark"));
					}
				}
			}
			borrowPricesList.add(borrowPricesMap);
			 
			List<NewBorrowingBean> blist = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(rdlist);
			List<NewBorrowingBean> tralist = new ArrayList<NewBorrowingBean>();
			List<NewBorrowingBean> teamlist= new ArrayList<NewBorrowingBean>();
			List<NewBorrowingBean> borrowList = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(borrowPricesList);
			//将blist 拆分为团队借款和游客借款两部分
			if(blist!=null&&blist.size()>0){
				int size = blist.size();
				for(int i = 0;i<size;i++){
					NewBorrowingBean bean = blist.get(i);
					if("0".equals(bean.getTravelerId())){//团队借款
						teamlist.add(bean);
					}else{//游客借款
						tralist.add(bean);
					}
				}
				
			}
			
			if(reviewId!=null&&!"".equals(reviewId)){//显示动态审批的标志
				
		    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
		    	
		    	model.addAttribute("rLog",rLog);
		    }
	//		if(review!=null){
	//			model.addAttribute("applyDate", review.getCreateDate());
	//		}
			model.addAttribute("refundDate", blist.get(0).getRefundDate());
			model.addAttribute("tralist", tralist);
			model.addAttribute("rid", reviewId);
			model.addAttribute("teamlist",teamlist);
			model.addAttribute("borrowList", borrowList);
			model.addAttribute("totalsize", borrowList.size());
			model.addAttribute("reviewId", reviewId);
			List<Currency> currencyList = currencyService.findCurrencyList(companyId);
			model.addAttribute("orderDetail", orderDetail);
			model.addAttribute("currencyList", currencyList);
			model.addAttribute("orderId", orderId);
			model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
			model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
			model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
	        model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
			model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));
			
			return "review/borrowing/airticket/airticketLendMoneyDetail";
		}
		
	/**
	 * 根据订单编号，查询新行者借款订单的明细
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> queryAirticketOrderDetailById(String orderId){
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		return orderDetailInfoMap;
	}
	
	/**
	 * 新行者机票订单借款申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyApply")
	public String airticketLendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId= request.getParameter("orderId");
		String reviewId = request.getParameter("review_id");
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		RefundBean reviewObj = null;
		model.addAttribute("reviewObj", reviewObj);
		model.addAttribute("orderId", orderId);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("office", UserUtils.getUser().getCompany());
		return "review/borrowing/airticket/airticketLendMoneyList";
	}
	
	/**
	 * 流程互斥校验
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="beforeAddLendMoneyApply")
	public String beforeAddLendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
//		String orderId = request.getParameter("orderId");
//		int validate = airTicketOrderLendMoneyService.validateProcess(Integer.parseInt(orderId));
		String result = "";
		//注释掉暂时取消互斥
//		if(validate>0){
//		 result = "还有未完成申请"; 
//		}   
		return result;
	}
	
	/**
	 * 新行者机票订单借款列表
	 * @author songyang 2015年11月3日14:13:14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "borrowAmountList")
	public String borrowAmountList( HttpServletRequest request, HttpServletResponse response, Model model, String orderId,Integer flowType,Integer productType) {
		AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.valueOf(orderId));
		Long pid = order.getAirticketId();
		ActivityAirTicket p = activityAirTicketDao.findOne(pid);
		List<NewBorrowingBean> reviewList = new ArrayList<NewBorrowingBean>();
		Long deptId = p.getDeptId();
		if(null!=deptId){
			List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, Context.ORDER_TYPE_JP , Context.REVIEW_FLOWTYPE_BORROWMONEY, order.getId().toString(),OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
			reviewList = getBorrowingBeanListnew(processList);
			for(NewBorrowingBean borr :reviewList){
				if(borr.getTravelerId().contains(NewBorrowingBean.REGEX)||"0".equals(borr.getTravelerId())){
					borr.setTravelerName("团队");
				}
				if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(NewBorrowingBean.REGEX)){
					String compPrice = "";
					if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
						String[] cMarks = borr.getCurrencyMarks().split(NewBorrowingBean.REGEX);	
						String[] cPrices = borr.getBorrowPrices().split(NewBorrowingBean.REGEX);
						for(int i=0;i<cMarks.length;i++){
							compPrice+=cMarks[i]+cPrices[i]+"+";
						}
						borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
					}
					
				}else{
					    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
				}
			}
			Collections.reverse(reviewList);
		}
		model.addAttribute("processList", reviewList);
		model.addAttribute("orderId", orderId);
		return "review/borrowing/airticket/airticketborrowAmountList";
	}
	/**
	 * 组装BorrowingBean对象
	 */
//	private List<NewBorrowingBean> getBorrowingBeanList(
//			List<Map<String, String>> reviewMapList) {
//		List<NewBorrowingBean> aList = new ArrayList<NewBorrowingBean>();
//		if (null == reviewMapList || reviewMapList.isEmpty()) {
//			return aList;
//		}
//		for (Map<String, String> map : reviewMapList) {
//			aList.add(new NewBorrowingBean(null));
//		}
//		return aList;
//	}
//	
	
	/**
	 * by sy  2015年11月3日13:49:23
	 * 
	 * 组装BorrowingBean对象
	 */
	private List<NewBorrowingBean> getBorrowingBeanListnew(
			List<Map<String, Object>> reviewMapList) {
		List<NewBorrowingBean> aList = new ArrayList<NewBorrowingBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, Object> map : reviewMapList) {
			aList.add(new NewBorrowingBean(map));
		}
		return aList;
	}

	/**
	 * by  sy  2015年11月4日15:25:31
	 * 借款撤销申请
	 */
	@ResponseBody
	@RequestMapping(value = "backBorrowAmount")
	public String backBorrowAmount( HttpServletRequest request) {
		String rid = request.getParameter("rid");
//		Integer status = Integer.parseInt(request.getParameter("status"));
		ReviewResult r = null;
		String msg = "";
//		if(status!=1){
//			msg="当前状态不许撤销";
//		    return msg;
//		}
		r =processReviewService.back(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid(),"", rid, "", null);
		if(r.getSuccess()){
			msg="1";//撤销成功
		}else if(!r.getSuccess()){
			msg="2";//撤销失败
		}
		
        return msg;
	}
	
	
	/**
	 *by 宋扬 2015年11月20日15:41:29
	 *取消申请
	 */
	@ResponseBody
	@RequestMapping(value = "cancelBorrowAmount")
	public String cancelBorrowAmount( HttpServletRequest request) {
		String rid = request.getParameter("id");
		Integer status = Integer.parseInt(request.getParameter("status"));
		ReviewResult r = null;
		String msg = "";
		if(status!=1){
			msg="当前状态不许取消";
		    return msg;
		}
			
		r =processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(), "", rid, "", null);
		
		if(r.getSuccess()){
			msg="取消申请成功";
		}else if(!r.getSuccess()){
			msg="取消申请失败";
		}
		
        return msg;
	}
	
	/**
	 * 流程互斥
	 * add by songyang 二〇一五年十二月十一日 15:44:12
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value="lendMoneyApplyCheck")
	public void  lendMoneyApplyCheck(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		Map<String , String > jsonMap = new HashMap<String, String>();
		String data = "";
		Boolean isGroup = true;
		String travellerId = "0";
		List<String> paramNamesList = new ArrayList<String>();
 		paramNamesList.add("travelerId");
		List<String> paramList = new ArrayList<String>();
		//借款审批数据组装成MAP
		Map<String,String> mapReview = NewBorrowingBean.exportDetail4RequestMap(request, paramNamesList,paramList,"lendPrice");
		String[] travels = mapReview.get("travelerId").split("#_");
//		if(travels!=null&&travels.length>0){
//			if(travels.length>1){
//				travellerId="0";
//			}else if(travels.length==1 && ("0").equals(travels[0])){
//				travellerId="0";
//			}else{
//				travellerId=travels[0];
//				isGroup = false;
//			}
//		}
		List<String> travelsList = new ArrayList<String>();
		for(String traveStr : travels){
			travelsList.add(traveStr);
		}
		
		String orderId = request.getParameter("orderId");
		
		CommonResult  commonResult =  reviewMutexService.check(orderId, travelsList, Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());
//		CommonResult  commonResult =  reviewMutexService.check(orderId, travellerId, Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(), isGroup);
		if(commonResult.getSuccess()){
			jsonMap.put("result", "0");
		}else{
			jsonMap.put("message", commonResult.getMessage());
		}
		data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
	}
	
	/**
	 * 流程申请
	 * by sy 2015年11月4日13:49:52
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="LendMoneyApply")
	public String LendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		String msg="";
//		String[] travelerIds = request.getParameterValues("travelerId");
		String travelerId="0";
		List<String> paramNamesList = new ArrayList<String>();
 		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
		paramNamesList.add(Context.REVIEW_VARIABLE_KEY_REFUND_DATE);
//		paramNamesList.add("borrowRemark");
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		//借款审批数据组装成MAP
		Map<String,String> mapReview = NewBorrowingBean.exportDetail4RequestMap(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			mapReview.put("currencyConverter", fc+"");
		}
		if(request.getParameter("borrowRemark") != null&& request.getParameter("borrowRemark")!= null ){
			mapReview.put("borrowRemark", request.getParameter("borrowRemark"));
		}
		if(request.getParameter("currencyExchangerates") != null ){
			mapReview.put("currencyExchangerates",request.getParameter("currencyExchangerates"));
		}
		
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		AirticketOrder airOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		
		Long airticketId = airOrder.getAirticketId();
		if(airticketId == null){
			return null;
		}
		ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketId);
		if(activityAirTicket == null){
			return null;
		}

		//travelerId 一个游客 --游客ID 多个游客--0
		String[] travels = mapReview.get("travelerId").split("#_");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
			}
		}
		
		//流程变量表储存数据
		Map<String, Object> variables = new HashMap<String, Object>();
		//借款审批数据全部放到变量中
		variables.putAll(mapReview);
		//获取渠道ID
		Long agentId = airOrder.getAgentinfoId();
		//根据渠道ID获取渠道信息
		Agentinfo agentInfo = agentinfoService.loadAgentInfoById(agentId);
		if(agentId!=-1){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentInfo.getAgentName());
		}else{
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, airOrder.getNagentName());
		}
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
		if(("0").equals(travelerId)){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, "团队");
		}else{
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, mapReview.get("travelerName"));
		}
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, airOrder.getSalerId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, airOrder.getSalerName());
//		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "7");
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,airticketId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, airOrder.getOrderNo());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, activityAirTicket.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, airOrder.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, mapReview.get("travelerId").replaceAll("#_", ","));
		
		//把游客ID循环放到list里面
		List<String> travellerIsList = new ArrayList<String>();
		for(String travellerStr : travels){
			travellerIsList.add(travellerStr);
		}
		
		//判断流程互斥是否是团队
//		Boolean  isGroup  = false; 
//		if(("0").equals(travelerId)){
//			isGroup = true;
//		}
		
		CommonResult commonResult = null;
		
		//流程互斥判断
//		if(travels.length>1){
			 commonResult = reviewMutexService.check(orderId, travellerIsList, Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());
//		}else{
//			 commonResult = reviewMutexService.check(orderId, travelerId, Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(), isGroup);
//		}
		
		if(activityAirTicket.getDeptId()==null){
			msg = "产品部门为空！";
			return msg;
		}
		
		//如果不互斥
		if(commonResult.getSuccess()){
			
			//发送流程申请
			ReviewResult result = processReviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),userReviewPermissionChecker,null, BORROWING_PRODUCTTYPE_AIRTICKET , Context.REVIEW_FLOWTYPE_BORROWMONEY, activityAirTicket.getDeptId(), "", variables);
			//流程ID
			String  rid = result.getReviewId();
			Boolean rResult = result.getSuccess(); 
			//返回结果
			if(rResult){ 
				//审批通过时候的业务数据操作
//				if(result.getReviewStatus()==2){
//					map = newOrderReviewService.reviewBorrowing(result.getReviewId(), 1, request);
//				}
				NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
				String[] currencyIds = mapReview.get("currencyIds").split(NewBorrowingBean.REGEX);
				String[] borrowPrices = mapReview.get("borrowPrices").split(NewBorrowingBean.REGEX);
				String[] currencyExchangerates = mapReview.get("currencyExchangerates").split(NewBorrowingBean.REGEX);
				for (int i = 0; i < currencyIds.length; i++) {
					//审批申请金额保存到新审批流程金额表
					newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
					newProcessMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
					newProcessMoneyAmount.setAmount(new BigDecimal(borrowPrices[i]));
					newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
					newProcessMoneyAmount.setOrderType(7);
					newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
					newProcessMoneyAmount.setCreateTime(new Date());
					newProcessMoneyAmount.setExchangerate(new BigDecimal(currencyExchangerates[i]));
					newProcessMoneyAmount.setReviewId(rid);
					newProcessMoneyAmount.setCompanyId(UserUtils.getUser().getCompany().getUuid().toString());
					processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);

					if(result.getReviewStatus() == 2) {
						MoneyAmount moneyAmount = new MoneyAmount();
						moneyAmount.setSerialNum(UUID.randomUUID().toString());
						moneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
						moneyAmount.setAmount(new BigDecimal(borrowPrices[i]));
						moneyAmount.setUid(Long.parseLong(orderId));
						moneyAmount.setMoneyType(12);
						moneyAmount.setOrderType(7);
						moneyAmount.setBusindessType(1);
						moneyAmount.setExchangerate(new BigDecimal(request.getParameter("currencyExchangerates")));
						moneyAmount.setReviewUuid(rid);
						moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
					}
				}

				msg="借款申请成功!";
			} else {
				if(ReviewErrorCode.ERROR_CODE_MSG_CN.containsKey(result.getCode())){
					msg=ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
				}
			}
		}else{
			msg=commonResult.getMessage()+"</br>，请重新选择游客！";
		}
		return msg;
	}
	
	/**
	 * 其他币种转换成人民币
	 */
	@ResponseBody
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split("#");
		String [] ci= currencyId.split("#");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			if(StringUtils.isNotBlank(ct[i])){
				String ctStr = ct[i];
				if(ctStr.indexOf(",")>-1){
					ctStr = ct[i].replaceAll(",", "");
				}
				//String ctStr = ct[i].replaceAll(",", "");
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=?");
				//buffer.append(ci[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class,ci[i]);
				Map<String, Object>  mp =  list.get(0);
				totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ctStr);
			}
			
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	/**
	 * add by 宋扬  2015年11月5日11:20:57
	 * add date 2015-05-12
	 * 审批借款申请信息
	 */
	/**
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	/**
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="reviewPlaneBorrowingInfo")
	public String reviewPlaneBorrowingInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String reviewId = request.getParameter("rid");
		String orderId = request.getParameter("orderId");
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		Map<String, Object> orderDetail = queryAirticketOrderDetailById(orderId);//查询订单明细
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>  processMap = null;
		try{
			if(reviewId!=null){
				processMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
				rdlist.add(processMap);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ", e);
		}
		//将总金额和币种显示出来
		List<Map<String,Object>> borrowPricesList = new ArrayList<Map<String,Object>>();
		Map<String,Object> borrowPricesMap = new HashMap<String, Object>();

		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if(rdlist.get(i).containsKey("borrowPrices")){
					borrowPricesMap.put("borrowPrices", rdlist.get(i).get("borrowPrices").toString());
				}
				if(rdlist.get(i).containsKey("currencyMarks")){
					borrowPricesMap.put("currencyMarks", rdlist.get(i).get("currencyMarks").toString());
				}
				if(rdlist.get(i).containsKey("borrowRemark")){
					model.addAttribute("borrowRemark",rdlist.get(i).get("borrowRemark"));
				}
			}
		}
		borrowPricesList.add(borrowPricesMap);
		List<NewBorrowingBean> blist = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(rdlist);
		List<NewBorrowingBean> tralist = new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> teamlist= new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> borrowList = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(borrowPricesList);

		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			for(int i = 0;i<size;i++){
				NewBorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}

		}

		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审批的标志
			List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
			model.addAttribute("rLog",rLog);
		}
		model.addAttribute("refundDate", blist.get(0).getRefundDate());
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist", teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));
		model.addAttribute("rid", reviewId);
		return "review/borrowing/common/newreviewPlaneBorrowingInfo";
	}
	
	

}
