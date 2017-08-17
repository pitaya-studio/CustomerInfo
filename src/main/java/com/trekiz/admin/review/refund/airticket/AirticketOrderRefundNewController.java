package com.trekiz.admin.review.refund.airticket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.quauq.review.core.engine.ReviewService;
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
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

@Controller
@RequestMapping(value = "${adminPath}/order/refundNew")
public class AirticketOrderRefundNewController {

	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AirlineInfoService airlineInfoService;

	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
    private AreaService areaService;
	
	@Autowired
	private IAirTicketOrderService airticketOrderService;
	
	@Autowired
	private ActivityAirTicketServiceImpl activityAirTicketService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	@Autowired
	private ICommonReviewService commonReviewService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;

	@Autowired
	private MoneyAmountService moneyAmountService;
	
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
	@RequestMapping(value="refundApplyCheck")
	public void  refundApplyCheck(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		Map<String , String > jsonMap = new HashMap<String, String>();
		String data = "";
		Boolean isGroup = false;
		String travellerId = "";
		String orderId = request.getParameter("orderId");
		String refundRecordsStr = request.getParameter("refundRecords");
		List<RefundBean> refundRecords = JSONArray.parseArray(refundRecordsStr, RefundBean.class);
		List<String> trList = new ArrayList<String>();
		CommonResult  commonResult =null;
		for (RefundBean bean : refundRecords) {
//			if(("0").equals(bean.getTravelerId())){
//				isGroup = true;
//				travellerId = "0";
//			}else{
//				travellerId = bean.getTravelerId();
//			}
			if(!("0").equals(bean.getTravelerId())){
				trList.add(bean.getTravelerId());
			}
		}
		if(trList!=null&&trList.size()>0){
			commonResult =  reviewMutexService.check(orderId, trList , Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_REFUND.toString());
		}else{
			commonResult =  reviewMutexService.check(orderId,"0", Context.ORDER_STATUS_AIR_TICKET, Context.REVIEW_FLOWTYPE_REFUND.toString(),true);
		}
		
		if(commonResult.getSuccess()){
			jsonMap.put("result", "0");
		}else{
			jsonMap.put("message", commonResult.getMessage());
		}
		data = JSONObject.fromObject(jsonMap).toString();
		ReviewUtils.printJSON(data, response);
	}
	
	/**
	 * 提交申请
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "submitReviewNew")
	public Map<String, Object> submitReviewflowNew(HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String createReason = request.getParameter("refundRemark");//个人退款备注
		String refundRecordsStr = request.getParameter("refundRecords");
		String deny_reason = request.getParameter("deny_reason");
		StringBuffer reply = new StringBuffer();
		List<RefundBean> refundRecords = JSONArray.parseArray(refundRecordsStr, RefundBean.class);
		ReviewResult reviewResult = null;
		
		AirticketOrder airOrder = airticketOrderService.getAirticketorderById(Long.parseLong(orderId));
		String companyId = UserUtils.getUser().getCompany().getUuid();
		if (companyId == null) {
			reply.append("用户所属经销商Id不存在");
			return null;
		}
		Long airticketId = airOrder.getAirticketId();
		if(airticketId == null){
			reply.append("产品Id不能为空.");
			return null;
		}
		ActivityAirTicket activityAirTicket = activityAirTicketService.findById(airticketId);
		if(activityAirTicket == null){
			reply.append("机票审批产品不能为空.");
			return null;
		}
		Long deptId = activityAirTicket.getDeptId();
		if(deptId == null){
			reply.append("所属部门不能为空.");
			return null;
		}
		
		Long agentId = airOrder.getAgentinfoId();//预定渠道
		String agentName = "";
		if (agentId == -1) {
			agentName = airOrder.getNagentName();//非签约渠道
		} else {
			agentName = agentinfoService.findOne(agentId).getAgentName();//签约渠道
		}
		
		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		//对预报单状态进行判断
		if ("10".equals(activityAirTicket.getForcastStatus())) {//!=改为了== by chy 2015年6月8日17:34:52 10为锁 00 标示未锁
			yubao_locked = true;
		}
		//对结算单状态进行判断
		if (1 == activityAirTicket.getLockStatus()) {//00改为了10 by chy 2015年6月8日17:35:24 00标示未锁 10标示锁定
			jiesuan_locked = true;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "结算单已锁定，不能发起审批");
			return map;
		} 
		Map<String, Object> variables = new HashMap<String, Object>();
		
		String userId = UserUtils.getUser().getId().toString();
//		String businessKey = "";
		Integer productType = Context.ProductType.PRODUCT_AIR_TICKET;
		Integer processType = 1;
		String comment = "";
		
		if (orderId == null || orderId.trim().length() == 0) {
			reply.append("订单ID不能为空");
		}
		//新行者计调 退款流程 是16，其他退款都是1 
		Integer flowType = 1;
		//当用户的公司编号 UserUtils.getUser().getCompany().getId() 等于 71 时说明是新行者，调用 ReviewService.getOperTotal() 函数，
		//如果返回 0 时（不是计调）
//		List<Long> jobList=userJobDao.getUserOperList(UserUtils.getUser().getId());
//		if(71 == UserUtils.getUser().getCompany().getId() && (jobList != null && 0 != jobList.size())){
//			flowType = 16;
//		}else{
//			flowType=1;
//		}
		Boolean  isOperator = systemService.hasOperatorJob(new Long(userId));
		variables.put("isOperator",isOperator);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE,Context.ProductType.PRODUCT_AIR_TICKET);//product_type 产品类型
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID,deptId);//deptId 部门Id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT,agentId);//agent 渠道
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, activityAirTicket.getCreateBy().getId());//operator 计调
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, airOrder.getCreateBy().getId());//order_creator 下单人
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());//group_code 团号
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO,airOrder.getOrderNo());//order_no 订单编号
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,airticketId);//product_id 产品Id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID,orderId);//order_id 订单Id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DENY_REASON,deny_reason);//deny_reason 拒绝理由
		variables.put("createReason",createReason);
		variables.put("flowType", flowType);// 退款流程 
		variables.put("companyId", companyId);
		if(agentId!=-1){
			variables.put("agentName", agentName);
		}else{
			variables.put("agentName", "非签约渠道");
		}
		
		
		NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
		
		for (RefundBean bean : refundRecords) {
			//保存review_new中的值
			
			if(productType == 7) {//机票
				AirticketOrder airticketOrder = airticketOrderService.getAirticketorderById(Long.parseLong(orderId));
				if(airticketOrder == null || airticketOrder.getAirticketId() == null){
					reply.append("错误的订单数据");
					break;
				}
				ActivityAirTicket airTicket = activityAirTicketService.findById(airticketOrder.getAirticketId());
				if(airTicket == null){
					reply.append("错误的产品数据");
					break;
				}
				if(airTicket.getLockStatus() != null && 1 == airTicket.getLockStatus()){
					reply.append("结算单已锁定，不能发起流程");
					break;
				}
			}
			
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, airOrder.getSalerId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, airOrder.getSalerName());
			variables.put("travelerId", bean.getTravelerId());
			variables.put("travelerName", bean.getTravelerName());
			variables.put("refundName", bean.getRefundName());
			variables.put("applyDate", bean.getApplyDate()==null?new Date():bean.getApplyDate());
			variables.put("currencyId", bean.getCurrencyId());
			variables.put("currencyName",bean.getCurrencyName());
			variables.put("currencyMark", bean.getCurrencyMark());
			variables.put("payPrice", bean.getPayPrice());
			variables.put("refundPrice", bean.getRefundPrice());
			variables.put("remark", bean.getRemark());
			variables.put("createBy", bean.getCreateBy());
			reviewResult = reviewService.start(userId, companyId,userReviewPermissionChecker,null, productType, processType, deptId, comment, variables);
			//reviewResult.getReviewId();//其余的数据是保存在 act_ru_variable 表中 
			//保存  review_process_money_amount ;
			newProcessMoneyAmount = new NewProcessMoneyAmount();
			//审批申请金额保存到新审批流程金额表
			newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
			newProcessMoneyAmount.setCurrencyId(Integer.parseInt(bean.getCurrencyId()));
			newProcessMoneyAmount.setAmount(new BigDecimal(bean.getRefundPrice()));
			
			newProcessMoneyAmount.setOrderType(7);
			newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
			newProcessMoneyAmount.setCreateTime(new Date());
			newProcessMoneyAmount.setReviewId(reviewResult.getReviewId());
			newProcessMoneyAmount.setCompanyId(UserUtils.getUser().getCompany().getId().toString());
			processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
			if (reviewResult.getSuccess()) {
				// 单个审核通过之后对成本进行更改
				ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
				costManageService.saveRefundCostRecordNew(reviewInfo, bean, airOrder, yubao_locked, jiesuan_locked);
				//如果审核全部通过，把数据写入money_amount表中。update by yudong.xu 2016.5.25
				if(reviewResult.getReviewStatus() == ReviewConstant.REVIEW_STATUS_PASSED){
					List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
					MoneyAmount ma = new MoneyAmount();
					ma.setSerialNum(UuidUtils.generUuid());
					ma.setAmount(new BigDecimal(bean.getRefundPrice()));//款数
					ma.setOrderType(7);//订单类型 即 产品类型
					ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
					ma.setUid(Long.parseLong(orderId));//订单id
					ma.setReviewUuid(reviewResult.getReviewId());//revId
					ma.setCurrencyId(Integer.parseInt(bean.getCurrencyId()));//币种
					ma.setBusindessType(1);//1标示订单退款
					moneyList.add(ma);
					moneyAmountService.saveMoneyAmounts(moneyList);
				}
			}
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(!reviewResult.getSuccess()){
			if(ReviewErrorCode.ERROR_CODE_MSG_CN.containsKey(reviewResult.getCode())){
				map.put("error",ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode()));
			}
		} else {
			map.put("success", "success");
		}

		return map;
	}
	/**根据订单ID查询审批记录
	 * add by WangXK
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "viewListNew")
	public String viewRefundRecordsNew(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		//当 ReviewService.getOperTotal() 是 0 时（不是计调），查询 flowType=1 的审批记录；否则查询 flowType=16 的审批记录
		/*Integer flowType;
		List<Long> jobList=userJobDao.getUserOperList(UserUtils.getUser().getId());
		if(71 == UserUtils.getUser().getCompany().getId() && (jobList != null && 0 != jobList.size())){
			flowType = 16;
		}else{
			flowType=1;
		}*/
		//AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.valueOf(orderId));
		//Long pid = order.getAirticketId();
		//ActivityAirTicket p = activityAirTicketDao.findOne(pid);
		Long deptId = null;
		AirticketOrder airticketOrder = airticketOrderService.getAirticketorderById(Long.parseLong(orderId));
		if(airticketOrder != null && airticketOrder.getAirticketId() != null){
			ActivityAirTicket airTicket = activityAirTicketService.findById(airticketOrder.getAirticketId());
			if(airTicket != null){
				deptId = airTicket.getDeptId();
			}
		}
		
		Integer processType = Context.REVIEW_FLOWTYPE_REFUND;
		
		List<Map<String, Object>> reviewNewMapList  = null;
		try{
			reviewNewMapList= reviewService.getReviewDetailMapListByOrderId(deptId, 7, processType, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//List<RefundBean> reviewList = getRefundBeanList(reviewNewMapList);
		//model.addAttribute("viewMap", getShowMap(reviewList));
		model.addAttribute("viewMap", reviewNewMapList);
		model.addAttribute("orderId", orderId);
		return "review/refund/airticket/order/airticketOrderRefundListNew";
	}
	
	/**
	 * 查询单个退款信息(退款详情页面)
	 */
	@RequestMapping(value="refundPriceNew/{orderId}/{idStr}")
	public String querySingleRefundPriceNew(Model model,HttpServletResponse response,HttpServletRequest request,
		   @PathVariable String orderId,@PathVariable String idStr){
		if(idStr != null &&!"".equals(idStr)){
			List<Integer> flowtypes = new ArrayList<Integer>();
			flowtypes.add(Context.REVIEW_FLOWTYPE_REFUND);
			flowtypes.add(Context.REVIEW_FLOWTYPE_OPER_REFUND);
			Map<String,Object> reviewNew = reviewService.getReviewDetailMapByReviewId(idStr);
			model.addAttribute("viewMap", reviewNew);
			//=========订单详情开始
			Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
			model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
			//=========订单详情结束
			//查询审批动态，原来的页面需要进行改动
			model.addAttribute("reviewId", idStr);
			model.addAttribute("rid", idStr);
			if(idStr!=null&&!"".equals(idStr)){//显示动态审批的标志
				
		    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(idStr);
		    	
		    	model.addAttribute("rLog",rLog);
		    }
			return "review/refund/airticket/order/airticketRefundPricesNew";
		}else{
			return "redirect:" + Global.getAdminPath() + "/order/refund/viewListNew?orderId=" + orderId;
		}
	}

	/**
	 * 按游客Id转换为map分组
	 * 
	 * @param reviewList
	 * @return
	 */
	@SuppressWarnings("unused")
	private LinkedHashMap<String, List<RefundBean>> getShowMap(
			List<RefundBean> reviewList) {
		LinkedHashMap<String, List<RefundBean>> map = new LinkedHashMap<String, List<RefundBean>>();
		// 根据游客id排序
		Collections.sort(reviewList, new Comparator<RefundBean>() {
			@Override
			public int compare(RefundBean o1, RefundBean o2) {
				if (Integer.valueOf(o1.getCurrencyId()) > Integer.valueOf(o2
						.getCurrencyId())) {
					return 1;
				} else if (Integer.valueOf(o1.getCurrencyId()) < Integer
						.valueOf(o2.getCurrencyId())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		String key = null;
		List<RefundBean> aList = null;
		for (RefundBean bean : reviewList) {
			key = bean.getTravelerId();
			aList = map.get(key);
			if (aList == null) {
				aList = new ArrayList<RefundBean>();
				map.put(key, aList);
			}
			aList.add(bean);
//			System.out.println("review status: " + bean.getStatusDesc());
		}
		return map;
	}

	/**
	 * 获取退款bean列表
	 * 
	 * @return
	 */
	/*private List<RefundBean> getRefundBeanList(
			List<Map<String, Object>> reviewNewMapList) {
		List<RefundBean> aList = new ArrayList<RefundBean>();
		if (null == reviewNewMapList || reviewNewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, Object> map : reviewNewMapList) {
			aList.add(new RefundBean(map,Boolean.TRUE));
		}
		return aList;
	}*/

	@RequestMapping(value = "createRefundNew")
	public String createRefundNew(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoByIdRefund(orderId);
		/**
		String reviewId = request.getParameter("reviewId");
		RefundBean reviewObj = null;
		String refundRemark = null;
		ReviewNew reviewNew = null;
		Map<String,Object> reviewMap = null;
		try{
			reviewNew = reviewService.getReview(reviewId);
			reviewMap= reviewService.getReviewDetailMapByReviewId(reviewId);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(reviewId)) {
			List<Review> review = null;//reviewService.findReview(Long.valueOf(reviewId), true);
			refundRemark = review.get(0).getCreateReason();
			reviewObj = null;//new RefundBean(reviewService.findReview(Long.valueOf(reviewId)));
		}
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("reviewObj", reviewNew);
		model.addAttribute("refundRemark", reviewMap);
		*/
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist",DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist",DictUtils.getDictList("airspace_Type"));// 舱位
        model.addAttribute("fromAreas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airlineList",airlineInfoService.getAirlineInfoList(companyId));// 航空公司

		return "review/refund/airticket/order/airticketOrderRefundNew";
	}

	/**
	 * 申请审批之前的校验
	 * 返回map 若result为空表示校验通过
	 * 返回map 若result不为空表示校验不通过
	 */
	@RequestMapping(value = "beforeAddReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	@ResponseBody
	public Map<String, Object> beforeAddReview(Model model, HttpServletRequest request, HttpServletResponse response){
		//获取当前要申请的审批流程id
		String reviewFlowId = request.getParameter("reviewFlowId");
		//获取订单的id
		String orderId = request.getParameter("orderId");
		//获取游客的id
		String travelerids = request.getParameter("travelerids");
		
		//获取类型 是订单(1)还是游客(2)
		String rType = request.getParameter("rType");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reviewFlowId", reviewFlowId);
		params.put("orderId", orderId);
		params.put("travelerids", travelerids);
		params.put("rType", rType);
		String result = beforeAddReview(params);
		params.put("result", result);
		return params;
	}
	
	public String beforeAddReview(Map<String, Object> params) {
		// 取出业务数据
		String reviewFlowId = params.get("reviewFlowId").toString();
		params.get("orderId").toString();
		String travelerids = params.get("travelerids").toString();
		//update by zhanghao at 2015-04-24 04:45 空字符串split的时候 长度会是“1”。无法判断游客为空的情况
		if(StringUtils.isBlank(travelerids)){
			return "游客不能为空";
		}
		String[] tras = travelerids.split(",");
		List<Long> travelerIds = new ArrayList<Long>();
		for(String temp : tras){
			travelerIds.add(Long.parseLong(temp));
		}
		//初始化返回值
		String result = "";
		new HashSet<Integer>();
		//根据不同的审批流程进行不同的校验
		 if("1".equals(reviewFlowId) || "16".equals(reviewFlowId)){//如果是退款
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(1);//退款
			flowTypes.add(16);//计调退款
//			List<Long> travelerIds = new ArrayList<Long>();
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个订单或游客是否有其它退款流程
			/*list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				result = "已经有退款流程在审批了，不能再次进行申请";
			}*/
			result = "";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "cancelRefundNew")
	public Map<String, Object> cancelRefundNew(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reviewId = request.getParameter("reviewId");
		StringBuffer reply = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (companyId == null) {
			 reply.append("取消申请退款的操作人的公司不能为空！");
			return null;
		}
		
		String userId = UserUtils.getUser().getId().toString();
		
		ReviewResult reviewResult = reviewService.cancel(userId, companyId.toString(), null, reviewId, null,null);
		if(!reviewResult.getSuccess()){
			map.put("error", reply.toString());
		} else {
			map.put("success", "success");
			// 对成本录入进行更改
			ReviewNew review = reviewService.getReview(reviewId);
			commonReviewService.updateCostRecordStatus(review, 3);
		}
		
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "currencyJson")
	public Map<String, Object> getCurrencyJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		List<Currency> currencyList = currencyService
				.findCurrencyList(companyId);
		map.put("currencyList", currencyList);
		return map;
	}

	
	@SuppressWarnings("unused")
	private List<Detail> getDetail(RefundBean bean) {
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			detailList.add(new Detail(entry.getKey(), entry.getValue()));
		}

		return detailList;
	}
}

