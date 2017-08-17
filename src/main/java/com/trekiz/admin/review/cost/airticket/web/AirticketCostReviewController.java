package com.trekiz.admin.review.cost.airticket.web;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="${adminPath}/costReview/airticket")
public class AirticketCostReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AirportService airportService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private IStockDao stockDao;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	/**
	 * 发起普通审批--机票
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="airticketCostApply")
	public String airticketCostApply(HttpServletRequest request, HttpServletResponse response, Model model) {
		String activityId = request.getParameter("activityId");	//机票产品id
//		String productType = request.getParameter("orderType");	//产品类型
//		String groupId = request.getParameter("groupId");	//团期id
		Long deptId = Long.parseLong(request.getParameter("deptId"));	//部门id
		String userId = UserUtils.getUser().getId().toString();	//用户id
		String companyUuid = UserUtils.getUser().getCompany().getUuid();//批发商id
		String costIds = request.getParameter("costList");	//多个机票成本id，以逗号分隔
		
		//机票产品信息
		ActivityAirTicket activityAirTicket = activityAirTicketService.findById(Long.parseLong(activityId));
		
		String msg = "";
		
		ReviewResult reviewResult = null;
		String[] costId = costIds.split(",");
		for (int i = 0; i < costId.length; i++) {
			if(!"0".equals(costId[i])) {
				CostRecord costRecord = costRecordDao.getById(Long.parseLong(costId[i]));
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_STOCK);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, ProductType.PRODUCT_AIR_TICKET);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_COST_RECORD_UUID, costRecord.getUuid());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, costRecord.getCreateBy().getId());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, costRecord.getCreateBy().getName());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID, activityAirTicket);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());
				
				if(costRecord.getBudgetType() == 0) {//预算
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, ProductType.PRODUCT_AIR_TICKET, Context.REVIEW_FLOWTYPE_STOCK, deptId, null, variables);
				}else if(costRecord.getBudgetType() == 1){//实际
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, ProductType.PRODUCT_AIR_TICKET, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
				}
				if(reviewResult.getSuccess()) {
					costRecord.setReviewUuid(reviewResult.getReviewId());
					if(reviewResult.getReviewStatus() == 2) {
						costRecord.setReview(2);
					}else{
						costRecord.setReview(1);
					}
					costRecordDao.updateObj(costRecord);
					msg = "发起审批成功！";
				}else{
					msg = "发起审批失败！失败原因：" + ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode());
				}
			}
			
		}
		
//		String msg = "";
//		if(reviewResult.getSuccess()) {
//			msg = "发起审批成功！";
//		}else{
//			msg = "发起审批失败！失败原因：" + ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode());
//		}
		return msg;
	}

	/**
	 * 机票成本录入详情页
	 * @param model
	 * @param airTicketId 机票id
	 * @return
	 */
	@RequestMapping(value="airticketCostDetail/{airTicketId}")
	public String airticketCostDetail(Model model,@PathVariable Long airTicketId){
		model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
		ActivityAirTicket airTicket = activityAirTicketService.getActivityAirTicketById(airTicketId);
		model.addAttribute("activityAirTicket",airTicket);
		Long deptId=airTicket.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
		model.addAttribute("deptId",deptId);

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		model.addAttribute("companyId",companyId);

		model.addAttribute("airportlist", airportService.queryAirport(companyId));

		List<Map<String,Object>> airticketOrders = airTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());
		model.addAttribute("airTicketOrderList",airticketOrders );

		//计算机票剩余时间
		for(Map<String, Object> temp : airticketOrders){
			com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);
		}

		List<Map<String, Object>> incomeList = costManageService.getRefunifoForCastList(airTicketId, 7);	//总收入
		model.addAttribute("incomeList", incomeList);

		//审批日志
		model.addAttribute("costLog",stockDao.findCostRecordLog(airTicketId, 7));
		List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(airTicketId, 7, null);
		model.addAttribute("budgetReviewLog", budgetReviewLog);
		List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(airTicketId, 7, null);
		model.addAttribute("actualReviewLog", actualReviewLog);
		List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(airTicketId, 7, null);
		model.addAttribute("paymentReviewLog", paymentReviewLog);

		return "review/cost/airticketCostDetail";
	}
	
	/**
	 * 机票成本审核、查看
	 * @param model
	 * @param airTicketId 机票id
	 * @return
	 */
	@RequestMapping(value="airticketCostReviewDetail/{airTicketId}/{budgetType}")
	public String airticketCostReviewDetail(HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable(value="airTicketId") Long airTicketId, Long costId, @PathVariable(value="budgetType") Integer budgetType){
		model.addAttribute("costId", costId);
		model.addAttribute("read", request.getParameter("read"));
		model.addAttribute("head", request.getParameter("head"));
		model.addAttribute("budgetType", budgetType);

		ActivityAirTicket airTicket = activityAirTicketService.getActivityAirTicketById(airTicketId);
		model.addAttribute("activityAirTicket", airTicket);
		model.addAttribute("areas", areaService.findAirportCityList(""));
		model.addAttribute("airportlist", airportService.queryAirportInfos());

		List<Map<String,Object>> airticketOrders = airTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());

		//需求0311，增加统计信息，订单总额，订单总人数。yudong.xu --begin--
		BigDecimal totalMoneySum = new BigDecimal(0);
		Integer orderPesonSum = 0;
		for (Map<String, Object> map : airticketOrders) {
			orderPesonSum += (int) map.get("personNum");
			Double totalMoneyRMB = Double.parseDouble(map.get("totalMoneyRMB").toString().replace(",",""));
			totalMoneySum = totalMoneySum.add(new BigDecimal(totalMoneyRMB));
		}
		model.addAttribute("orderPesonSum", orderPesonSum);
		String totalMoneySumStr = MoneyNumberFormat.getRoundMoney(Double.valueOf(totalMoneySum.toString()), MoneyNumberFormat.POINT_TWO);
		model.addAttribute("totalMoneySum", totalMoneySumStr);
		//--end--

		model.addAttribute("airTicketOrderList", airticketOrders);

		//计算机票剩余时间
		for(Map<String, Object> temp : airticketOrders){
			com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);
		}

		model.addAttribute("costLog",stockDao.findCostRecordLog(airTicketId, 7));
		if(budgetType == 0) {
			List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(airTicketId, 7, costId);
			model.addAttribute("budgetReviewLog", budgetReviewLog);
		}else if(budgetType == 1) {
			List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(airTicketId, 7, costId);
			model.addAttribute("actualReviewLog", actualReviewLog);
		}else if(budgetType == 2){
			List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(airTicketId, 7, costId);
			model.addAttribute("budgetReviewLog", budgetReviewLog);
			List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(airTicketId, 7, costId);
			model.addAttribute("actualReviewLog", actualReviewLog);
			List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(airTicketId, 7, costId);
			model.addAttribute("paymentReviewLog", paymentReviewLog);
		}
		
		return "review/cost/airticketCostReviewDetail";
	}
	
}
