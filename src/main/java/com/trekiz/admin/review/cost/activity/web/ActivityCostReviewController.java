package com.trekiz.admin.review.cost.activity.web;

import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.engine.entity.ReviewProcess;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.NumberUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.supplier.entity.Bank;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.repository.BankDao;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.*;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.cost.activity.service.IActivityCostReviewService;
import com.trekiz.admin.review.cost.common.CostParam;
import com.trekiz.admin.review.cost.common.CostRecordModel;
import com.trekiz.admin.review.cost.common.ICostCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 团期产品成本审批
 * @author zhankui.zong
 *
 */
@Controller
@RequestMapping(value="${adminPath}/costReview/activity")
public class ActivityCostReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private DictService dictService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private IActivityCostReviewService activityCostReviewService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private SysOfficeConfigurationService sysOfficeConfigurationService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private ICostCommonService costCommonService;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private IStockDao stockDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	
	/**
	 * 发起普通审批
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="costApply")
	public String costApply(HttpServletRequest request, HttpServletResponse response, Model model) {
		String activityId = request.getParameter("activityId");	//产品id
		String productType = request.getParameter("orderType");	//产品类型
		String groupId = request.getParameter("groupId");	//团期id
		Long deptId = Long.parseLong(request.getParameter("deptId"));	//部门id
		String userId = UserUtils.getUser().getId().toString();	//用户id
		String companyUuid = UserUtils.getUser().getCompany().getUuid();	//批发商id
		String costIds = request.getParameter("costList");	//多个成本id，以逗号分隔
		String visaIds = request.getParameter("visaIds");

		//产品信息
		TravelActivity travelActivity = travelActivityService.findById(Long.parseLong(activityId));

		//团期信息
		ActivityGroup activityGroup = activityGroupService.findById(Long.parseLong(groupId));
		
		String msg = "";
		
		ReviewResult reviewResult = null;

		String[] visaId = visaIds.split(",");
		for(int i = 0; i < visaId.length; i++) {
			if(!"0".equals(visaId[i]) && !"".equals(visaId[i])) {
				CostRecord c = saveCostRecord(visaId[i], Long.parseLong(groupId), Integer.parseInt(productType), deptId.toString());
				costIds += "," + c.getId();
			}
		}
		String[] costId = costIds.split(",");
		for (int i = 0; i < costId.length; i++) {
			if(!"0".equals(costId[i])) {
				CostRecord costRecord = costRecordDao.getById(Long.parseLong(costId[i]));
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_STOCK);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityGroup.getGroupCode());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, travelActivity.getAcitivityName());
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_COST_RECORD_UUID, costRecord.getUuid());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, costRecord.getCreateBy().getId());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, costRecord.getCreateBy().getName());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID, travelActivity.getactivit);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, travelActivity.getAcitivityName());
				
				if(costRecord.getBudgetType() == 0) {//预算
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, Integer.parseInt(productType), Context.REVIEW_FLOWTYPE_STOCK, deptId, null, variables);
				}else if(costRecord.getBudgetType() == 1) {//实际
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, Integer.parseInt(productType), Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
				}
				
				if(reviewResult.getSuccess()) {
					if(reviewResult.getReviewStatus() == 2) {
						costRecord.setReview(2);
						if(costRecord.getBudgetType() == 0) {
							CostRecord cr = costManageService.copyCost(costRecord);
							cr.setUuid(UuidUtils.generUuid());
							cr.setReview(4);
							cr.setPayReview(4);
							cr.setBudgetType(1);
							cr.setIsNew(2);
							costRecordDao.save(cr);
						}
					}else{
						costRecord.setReview(1);
					}
					costRecord.setReviewUuid(reviewResult.getReviewId());
					costRecordDao.updateObj(costRecord);
					msg = "发起审批成功！";
				}else{
					msg = "发起审批失败！失败原因：" + ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode());
				}
			}
			
		}
		
//		if(reviewResult.getSuccess()) {
//			msg = "发起审批成功！";
//		}else{
//			msg = "发起审批失败！失败原因：" + ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode());
//		}
		return msg;
	}
	
	/**
	 * 流程撤销
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="revoke")
	public String revoke(HttpServletRequest request, HttpServletResponse response) {
		String reviewId = request.getParameter("reviewId");
//		Integer status = Integer.parseInt(request.getParameter("status"));
		ReviewResult result = null;
		String msg = "";
//		if(status != 1) {
//			msg = "当前状态不许撤销";
//			return msg;
//		}
		
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		result = reviewService.back(userId.toString(), companyUuid, null, reviewId, null, null);
		
		if(result.getSuccess()) {
			msg = "撤销成功！";
		}else{
			msg = "撤销失败！";
		}
		return msg;
	}
	
	/**
	 * 批量审批通过或驳回
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchApproveOrReject")
	public String batchApproveOrReject(HttpServletRequest request, HttpServletResponse response) {
		String reason = request.getParameter("reason"); //理由
		String typeStr = request.getParameter("type"); //通过或驳回标志
		Integer type = null;
		if(StringUtils.isNotBlank(typeStr)) {
			type = Integer.parseInt(typeStr);
		}
		
		String msg = "";
		
		String reviewIdStr = request.getParameter("reviewIds");
		String[] reviewIds = reviewIdStr.split(",");
		for (int i = 0; i < reviewIds.length; i++) {
			String reviewId = reviewIds[i];
			if(!"0".equals(reviewId)) {
				msg = approveOrReject(reviewId, reason, type, request, response);
			}
		}
		
		return msg;
	}
	
	/**
	 * 审批通过或驳回
	 * @param reviewId 审批id
	 * @param reason 理由
	 * @param type 通过驳回标志
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="approveOrReject")
	public String approveOrReject(@RequestParam String reviewId, @RequestParam String reason, @RequestParam Integer type, HttpServletRequest request, HttpServletResponse response) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
		ReviewResult result = null;
		String msg = "";
		
		CostRecord costRecord = costRecordDao.getByReviewUuid(reviewId);
		
		if(type == 2) { //通过
			result = reviewService.approve(userId.toString(), companyUuid, null, permissionChecker, reviewId, reason, null);
			if(result.getReviewStatus() == 1) {
				costRecord.setReview(1);
			}else if(result.getReviewStatus() == 2){
				costRecord.setReview(2);
				if(costRecord.getBudgetType() == 0) {
					CostRecord cr = costManageService.copyCost(costRecord);
					cr.setUuid(UuidUtils.generUuid());
					cr.setReview(4);
					cr.setPayReview(4);
					cr.setBudgetType(1);
					cr.setIsNew(2);
					costRecordDao.save(cr);
				}
			}
		}else if(type == 1) { //驳回
			result = reviewService.reject(userId.toString(), companyUuid, null, reviewId, reason, null);
			costRecord.setReview(0);
		}
		costRecordDao.updateObj(costRecord);
		
		if(result.getSuccess()) {
			msg = "审批成功！";
		}else{
			msg = "审批失败！";
		}
		return msg;
		
	}
	
	/**
	 * 取消审批流程
	 * @param id 审批id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancel")
	public String cancel(@RequestParam String id) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
//		String productType = request.getParameter("type");
		
		ReviewNew reviewNew = reviewService.getReview(id);
		if(ReviewConstant.REVIEW_STATUS_PASSED == reviewNew.getStatus()){
			return "取消失败";
		}
		ReviewResult result = reviewService.cancel(userId.toString(), companyUuid, null, id, null, null);;
		CostRecord costRecord = costRecordDao.getByReviewUuid(id);
		
//		if("6".equals(productType) || "7".equals(productType)) {
//			result = reviewService.cancel(userId.toString(), companyUuid, null, id, null, null);
//		}else{
//			costRecord = costRecordDao.getById(Long.parseLong(id));
//			result = reviewService.cancel(userId.toString(), companyUuid, null, costRecord.getReviewUuid(), null, null);
//		}
		
		String msg = "";
		if(result.getSuccess()) {
			costRecord.setReview(5);
			costRecordDao.updateObj(costRecord);
			msg = "取消成功！";
		}else{
			msg = "取消失败！";
		}
		return msg;
	}

	/**
	 * 获取审批列表
	 * @param costParam 查询条件参数
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="costReviewList/{budgetType}")
	public String costReviewList(@ModelAttribute("costParam") CostParam costParam, @PathVariable(value="budgetType") Integer budgetType,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
		if(costParam.getReviewer() == null) {
			costParam.setReviewer(1);
		}
		model.addAttribute("costParam", costParam);
		Page<Map<String, Object>> page = activityCostReviewService.getCostReviewList(new Page<Map<String, Object>>(request, response), costParam, budgetType);
		model.addAttribute("page", page);
		//产品类型
		model.addAttribute("productTypes", DictUtils.getDictList("order_type"));
		//地接社
		model.addAttribute("supplierList", supplierInfoService.findSupplierInfoByCompanyId(companyId));
		//渠道商
		model.addAttribute("agentList", agentinfoService.findAllAgentinfo(UserUtils.getUser().getCompany().getId()));
		
		//成本类型：预算、实际
		model.addAttribute("budgetType", budgetType);
		
		//根据批发商Uuid获取产品类型
		List<SysOfficeProductType> productTypes = sysOfficeConfigurationService.obtainOfficeProductTypes(companyUuid);
		model.addAttribute("productTypes", productTypes);
		
		return "review/cost/costReviewList";
	}

	/**
	 * 成本录入详情页
	 * @param activityId 产品id
	 * @param groupId 团期id
	 * @param typeId 产品类型
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="activityCostDetail/{activityId}/{groupId}/{typeId}", method=RequestMethod.GET)
	public String activityCostDetail(@PathVariable(value="activityId") Long activityId, @PathVariable(value="groupId") Long groupId, @PathVariable(value="typeId") Integer typeId,
									 Model model, HttpServletRequest request, HttpServletResponse response){
		//获取产品信息
		TravelActivity travelActivity = travelActivityService.findById(activityId);
		model.addAttribute("travelActivity", travelActivity);
		//获取团期信息
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		model.addAttribute("activityGroup", activityGroup);
		Long deptId=travelActivity.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
		model.addAttribute("deptId", deptId);

		String orderTypeName = OrderCommonUtil.getChineseOrderType(travelActivity.getActivityKind().toString());
		model.addAttribute("typename", orderTypeName);

		if(activityGroup != null && travelActivity != null &&
				activityGroup.getTravelActivity() != null &&
				activityGroup.getTravelActivity().getId().equals(travelActivity.getId())){
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = orderService.findOrderListByPayType(orderList, groupId);
			model.addAttribute("orderList", orderList.getList());

			model.addAttribute("orderType", typeId);

			List<Map<String, Object>> incomeList = costManageService.getRefunifoForCastList(groupId, typeId);	//总收入
			model.addAttribute("incomeList", incomeList);

			if(Context.ORDER_TYPE_SP == typeId){
				List<Map<String, String>> quauqServiceChargeList = costCommonService.getQuauqServiceAmount(typeId, groupId);
				List<Map<String, String>> agentServiceChargeList = costCommonService.getAgentServiceAmount(typeId, groupId);
				model.addAttribute("quauqServiceCharge", quauqServiceChargeList);
				model.addAttribute("agentServiceCharge", agentServiceChargeList);
			}
			//审批日志
			model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));
			List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(groupId, typeId, null);
			model.addAttribute("budgetReviewLog", budgetReviewLog);
			List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(groupId, typeId, null);
			model.addAttribute("actualReviewLog", actualReviewLog);
			List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(groupId, typeId, null);
			model.addAttribute("paymentReviewLog", paymentReviewLog);

		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		return "review/cost/activityCostDetail";
	}
	
	/**
	 * 保存成本录入
	 * @param costRecordModel 参数对象
	 * @return
	 */
	@RequestMapping(value="saveHQX")
	public String saveHQX(@ModelAttribute CostRecordModel costRecordModel, @RequestParam(value="DocInfoIds", required=false) Long[] DocInfoIds,
						  HttpServletRequest request, Model model){
		Long userId = UserUtils.getUser().getId();
		if(!NumberUtils.isDecimal(costRecordModel.getPrice().toString(), 12, 2)){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "单价数值过大，请检查");
			return Context.ERROR_PAGE;
		}
		if(!NumberUtils.isDecimal(costRecordModel.getPriceAfter().toString(), 12, 2)){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "转换后总价数值过大，请检查");
			return Context.ERROR_PAGE;
		}
		if(!StringUtils.isInnerLength(costRecordModel.getComment(), 1000)){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "备注内容太多，请检查");
			return Context.ERROR_PAGE;
		}

		CostRecord costRecord= new CostRecord();
		costRecord.setUuid(UuidUtils.generUuid());
		costRecord.setPayUpdateBy(Integer.parseInt(userId+""));
		costRecord.setPayUpdateDate(new Date());
		costRecord.setName(costRecordModel.getItemname());
		costRecord.setPrice(costRecordModel.getPrice());
		costRecord.setQuantity(costRecordModel.getQuantity());
		costRecord.setDelFlag(Context.DEL_FLAG_NORMAL);
		costRecord.setCreateBy(UserUtils.getUser());
		costRecord.setUpdateBy(UserUtils.getUser().getId());
		costRecord.setCreateDate(new Date());
		costRecord.setUpdateDate(new Date());
		costRecord.setIsNew(2);
		if(costRecordModel.getSupplyType() == 0) {
			costRecord.setKb(costRecordModel.getKb());
		}
		
		//拉美途预报单锁定后，添加的其他收入和实际成本forecast_locked_in字段设置为1
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String budgetLock = costCommonService.getLockStatus(costRecordModel.getTypeId(), costRecordModel.getGroupId());
		if(Context.SUPPLIER_UUID_LMT.equals(companyUuid) && "1".equals(budgetLock)) {
			costRecord.setForecastLockedIn("1");
		}
		
		// 获取银行账号
		if (costRecordModel.getBank() != null) {
			if (costRecordModel.getBank() == (long) -1) { /* 没有默认银行账号 */
				costRecord.setBankName(costRecordModel.getBankname());
				costRecord.setBankAccount(costRecordModel.getAccount());
			} else {
				Bank mybank = bankDao.findOne(costRecordModel.getBank());
				costRecord.setBankName(mybank.getBankName());
				costRecord.setBankAccount(mybank.getBankAccountCode());
			}
		}
		costRecord.setCurrencyAfter(costRecordModel.getCurrencyAfter());
		costRecord.setRate(costRecordModel.getRate());
		costRecord.setPriceAfter(costRecordModel.getPriceAfter());
		costRecord.setSupplierType(costRecordModel.getFirst());
		costRecord.setNowLevel(1);
		costRecord.setBudgetType(costRecordModel.getBudgetType());
		if(costRecordModel.getBudgetType()==1){
			costRecord.setPayReview(Context.REVIEW_COST_NEW);
			costRecord.setPayNowLevel(1);
		}
		costRecord.setSupplyType(costRecordModel.getSupplyType());
		costRecord.setOrderType(costRecordModel.getTypeId());
		costRecord.setPayStatus(0);
		//判断是地接社还是渠道商
		if(costRecordModel.getSupplyType()==Context.PLAT_TYPE_QD-1){	//渠道商
			costRecord.setSupplyId(costRecordModel.getAgentId());
			Agentinfo agentinfo = agentinfoService.findOne((long)costRecordModel.getAgentId());
			costRecord.setSupplyName(agentinfo.getAgentName());
		}else {	//地接社
			costRecord.setSupplyId(costRecordModel.getSupplier());
			SupplierInfo supplierInfo = supplierInfoService.findSupplierInfoById((long)costRecordModel.getSupplier());
			costRecord.setSupplyName(supplierInfo.getSupplierName());
		}
		costRecord.setActivityId(costRecordModel.getGroupId());
		if(null == costRecordModel.getOverseas()) {
			costRecord.setOverseas(0);
		}else{
			costRecord.setOverseas(costRecordModel.getOverseas());
		}		
		costRecord.setCurrencyId(costRecordModel.getCurrencyId());
		costRecord.setReview(costRecordModel.getReview());
		
//		String companyUuid = UserUtils.getUser().getCompany().getUuid();
//		Integer budgetCostAutoPass = UserUtils.getUser().getCompany().getBudgetCostAutoPass();//预算成本配置
		Integer budgetCostAutoPass = 0;
		ReviewProcess rp1 = reviewProcessService.obtainReviewProcess(companyUuid, request.getParameter("deptId"), costRecordModel.getTypeId().toString(), Context.REVIEW_FLOWTYPE_STOCK.toString());
		if(rp1 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp1.getProcessKey())) {
			budgetCostAutoPass = 1;
		}
//		Integer costAutoPass = UserUtils.getUser().getCompany().getCostAutoPass();//实际成本配置
		Integer costAutoPass = 0;
		ReviewProcess rp2 = reviewProcessService.obtainReviewProcess(companyUuid, request.getParameter("deptId"), costRecordModel.getTypeId().toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString());
		if(rp2 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp2.getProcessKey())) {
			costAutoPass = 1;
		}
		//if((companyId==68|| companyId==83 || companyId==75|| companyId==72) && review==1){
//		if(costAutoPass!=null && costAutoPass==1 && costRecordModel.getReview()==Context.REVIEW_COST_WAIT){
//			costRecord.setReview(Context.REVIEW_COST_PASS);
//		}
		
		//实际成本添加附件
		String docInfos = "";
		if(DocInfoIds != null) {
			for (int i = 0; i < DocInfoIds.length; i++) {
				docInfos += DocInfoIds[i] + ",";
			}
		}
		costRecord.setCostVoucher(docInfos);
		
		costRecord.setReviewType(0);
		if(StringUtils.isNotEmpty(costRecordModel.getComment())){
			costRecord.setComment(costRecordModel.getComment());
		}
		
		Long deptId = Long.parseLong(request.getParameter("deptId"));	//部门id
		Integer productType = Integer.parseInt(request.getParameter("typeId")); //产品类型
//		String companyUuid = UserUtils.getUser().getCompany().getUuid();	//批发商id
		String activityId = request.getParameter("activityId");	//产品id
		String groupId = request.getParameter("groupId");	//团期id
		Map<String, Object> variables = new HashMap<String, Object>();
		
		//根据产品类型获取不同产品信息
		if (productType < 6 || productType == 10) {
			//产品信息
			TravelActivity travelActivity = travelActivityService.findById(Long.parseLong(activityId));
			//团期信息
			ActivityGroup activityGroup = activityGroupService.findById(Long.parseLong(groupId));
			
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityGroup.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, travelActivity.getAcitivityName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
		} else if(productType == 6) {
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(Long.parseLong(activityId));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
		} else if(productType == 7) {
			ActivityAirTicket activityAirTicket = activityAirTicketService.findById(Long.parseLong(activityId));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());
		}

		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_STOCK);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
//		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, travelActivity.getAcitivityName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
		
		ReviewResult reviewResult = null;
		//提交审核
		if(costRecordModel.getReview() == 1) {
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, costRecord.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, costRecord.getCreateBy().getName());
			if(reviewProcessService.exist(deptId.toString(), productType.toString(), Context.REVIEW_FLOWTYPE_STOCK.toString(), companyUuid)) {
				if(costRecordModel.getBudgetType() == 0) {
					reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_STOCK, deptId, null, variables);
				}
			}
			if(reviewProcessService.exist(deptId.toString(), productType.toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString(), companyUuid)) {
				if(costRecordModel.getBudgetType() == 1){
					reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
				}
			}
			if(reviewResult != null) {
				costRecord.setReviewUuid(reviewResult.getReviewId());
				if(reviewResult.getSuccess()) {
					costRecord.setReview(1);
				}else{
					costRecord.setReview(4);
				}
			}else{
				costRecord.setReview(4);
			}
			
		}else{
			costRecord.setReview(4);
		}
		
		if(costRecordModel.getBudgetType() == 0 && budgetCostAutoPass == 1 && costRecordModel.getReview()==1 && reviewResult.getReviewStatus() != null && reviewResult.getReviewStatus() == 2) {//预算成本自动审核通过
			costRecord.setReview(2);
			
			//添加实际成本
			CostRecord cr=costManageService.copyCost(costRecord);
//			cr.setReviewCompanyId(reviewCompanyId);
//			cr.setPayReviewCompanyId(payReviewCompanyId);
			cr.setUuid(UuidUtils.generUuid());
			cr.setPayNowLevel(1);
			cr.setPayReview(4);
			cr.setBudgetType(1);
			
			if (costAutoPass == 1){
				cr.setNowLevel(4);
//				cr.setReview(2);
				cr.setIsNew(2);
				ReviewResult rr = null;
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, cr.getCreateBy().getId());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, cr.getCreateBy().getName());
				if(reviewProcessService.exist(deptId.toString(), productType.toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString(), companyUuid)) {
					rr = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
					cr.setReviewUuid(rr.getReviewId());
				}
				if(rr.getReviewStatus() != null && rr.getReviewStatus() == 2) {
					cr.setReview(2);
				}else{
					cr.setReview(4);
				}
				
			} else if (costAutoPass == 0) {
				cr.setNowLevel(1);
				cr.setReview(4);
			}
			costManageService.saveCostRecord(cr);//保存实际成本
		}
		if(costRecordModel.getBudgetType() == 1 && costAutoPass == 1 && costRecordModel.getReview()==1 && reviewResult.getReviewStatus()!=null && reviewResult.getReviewStatus()==2) {//实际成本自动审核通过
			costRecord.setReview(2);
		}
		
		costManageService.saveCostRecord(costRecord);
		
		//根据产品类型重定向到不同页面
		if(costRecordModel.getTypeId() < ProductType.PRODUCT_VISA || costRecordModel.getTypeId()==ProductType.PRODUCT_CRUISE) {
			return "redirect:"+Global.getAdminPath()+"/costReview/activity/activityCostDetail/"+costRecordModel.getActivityId()+"/"+costRecordModel.getGroupId() +"/"+costRecordModel.getTypeId();
		}else if(costRecordModel.getTypeId()==ProductType.PRODUCT_VISA) {
			return "redirect:"+Global.getAdminPath()+"/costReview/visa/visaCostDetail/"+costRecordModel.getActivityId();
		}else{
			return "redirect:"+Global.getAdminPath()+"/costReview/airticket/airticketCostDetail/"+costRecordModel.getActivityId();
		}
			
	}
	
	/**
	 * 更新成本录入
	 * @param costRecordModel 参数对象
	 * @return
	 */
	@RequestMapping(value="updateHQX")
	public String updateHQX(@ModelAttribute CostRecordModel costRecordModel,
							@RequestParam(value="DocInfoIds", required=false) Long[] DocInfoIds, HttpServletRequest request){
		String companyUuid = UserUtils.getUser().getCompany().getUuid();	//批发商id
		Long userId = UserUtils.getUser().getId();
		CostRecord costRecord=costRecordDao.findOne(costRecordModel.getCostId());
		costRecord.setName(costRecordModel.getItemname());
		costRecord.setPrice(costRecordModel.getPrice());
		costRecord.setQuantity(costRecordModel.getQuantity());
		costRecord.setBankName(costRecordModel.getBankname());
		costRecord.setBankAccount(costRecordModel.getAccount());
		
		costRecord.setCurrencyAfter(costRecordModel.getCurrencyAfter());
		costRecord.setRate(costRecordModel.getRate());
		costRecord.setPriceAfter(costRecordModel.getPriceAfter());
		
		costRecord.setNowLevel(1);
		costRecord.setKb(costRecordModel.getKb());
		//costRecord.setReviewCompanyId((long)1);
		
		if (costRecordModel.getBank() != null) {
			if (costRecordModel.getBank() == (long) -1) { /* 没有默认银行账号 */
				costRecord.setBankName(costRecordModel.getBankname());
				costRecord.setBankAccount(costRecordModel.getAccount());
			} else {
				Bank mybank = bankDao.findOne(costRecordModel.getBank());
				costRecord.setBankName(mybank.getBankName());
				costRecord.setBankAccount(mybank.getBankAccountCode());
			}
		}
		
		costRecord.setOrderType(costRecordModel.getTypeId());
		// 2 表示审批通过
		if(costRecord.getReview() != 2){
			costRecord.setReview(costRecordModel.getReview());
		}
		costRecord.setPayReview(costRecordModel.getPayReview());
		costRecord.setSupplierType(costRecordModel.getFirst());
		if(costRecordModel.getSupplyType() != null){
			costRecord.setSupplyType(costRecordModel.getSupplyType());
		}

		//实际成本添加附件
		String docInfos = "";
		if(DocInfoIds != null) {
			for (int i = 0; i < DocInfoIds.length; i++) {
				docInfos += DocInfoIds[i] + ",";
			}
		}
		costRecord.setCostVoucher(docInfos);

		Long deptId = Long.parseLong(request.getParameter("deptId"));	//部门id
		Integer productType = Integer.parseInt(request.getParameter("typeId")); //产品类型
		String activityId = request.getParameter("activityId");	//产品id
		String groupId = request.getParameter("groupId");	//团期id
		Map<String, Object> variables = new HashMap<String, Object>();
		//根据产品类型获取不同产品信息
		if (productType < Context.ORDER_TYPE_QZ || productType == Context.ORDER_TYPE_CRUISE) {
			//产品信息
			TravelActivity travelActivity = travelActivityService.findById(Long.parseLong(activityId));
			//团期信息
			ActivityGroup activityGroup = activityGroupService.findById(Long.parseLong(groupId));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityGroup.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, travelActivity.getAcitivityName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
		} else if(productType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(Long.parseLong(activityId));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
		} else if(productType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketService.findById(Long.parseLong(activityId));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());
		}
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, costRecord.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, costRecord.getCreateBy().getName());


		// 预算成本和实际成本中没有客户类别，而其它收入有客户类别，所以需要分别判断
		if(null != costRecordModel.getSupplyType() && costRecordModel.getSupplyType() == 1){
			costRecord.setSupplyId(costRecordModel.getAgentId());
			Agentinfo agentinfo = agentinfoService.findOne((long) costRecordModel.getAgentId());
			costRecord.setSupplyName(agentinfo.getAgentName());
		} else if(null != costRecordModel.getSupplyType() && costRecordModel.getSupplyType() == 0){
			costRecord.setSupplyId(costRecordModel.getSupplier());
			SupplierInfo supplierInfo = supplierInfoService.findSupplierInfoById((long) costRecordModel.getSupplier());
			costRecord.setSupplyName(supplierInfo.getSupplierName());
		}

		//撤销审核时，设置 nowLevel=1
		if(null != costRecordModel.getReview() && costRecordModel.getReview()==Context.REVIEW_COST_NEW){
			costRecord.setNowLevel(1);
		}
		costRecord.setPayStatus(0);
		if(null == costRecordModel.getOverseas()) {
			costRecord.setOverseas(0);
		}else{
			costRecord.setOverseas(costRecordModel.getOverseas());
		}	
		costRecord.setCurrencyId(costRecordModel.getCurrencyId());
		if(StringUtils.isNotBlank(costRecordModel.getComment())){
			costRecord.setComment(costRecordModel.getComment());
		}
		
		Integer budgetCostAutoPass = 0;
		ReviewProcess rp1 = reviewProcessService.obtainReviewProcess(companyUuid, request.getParameter("deptId"),
				costRecordModel.getTypeId().toString(), Context.REVIEW_FLOWTYPE_STOCK.toString());
		if(rp1 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp1.getProcessKey())) {
			budgetCostAutoPass = 1;
		}
		Integer costAutoPass = 0;
		ReviewProcess rp2 = reviewProcessService.obtainReviewProcess(companyUuid, request.getParameter("deptId"),
				costRecordModel.getTypeId().toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString());
		if(rp2 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp2.getProcessKey())) {
			costAutoPass = 1;
		}
		if(0 == costRecordModel.getBudgetType()){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_STOCK);
		}else if(1 == costRecordModel.getBudgetType()){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_ACTUAL_COST);
		}
		ReviewResult reviewResult = null;
		//提交审核
		if(null != costRecordModel.getReview() && costRecordModel.getReview() == 1) {
			if(0 == costRecordModel.getBudgetType() && reviewProcessService.exist(deptId.toString(),
					productType.toString(), Context.REVIEW_FLOWTYPE_STOCK.toString(), companyUuid)) {
				reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_STOCK, deptId, null, variables);
			}
			if(1 == costRecordModel.getBudgetType() && reviewProcessService.exist(deptId.toString(),
					productType.toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString(), companyUuid)) {
				reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
			}
			if(reviewResult != null) {
				costRecord.setReviewUuid(reviewResult.getReviewId());
				if(reviewResult.getSuccess()) {
					costRecord.setReview(1);//审批中
				}else{
					costRecord.setReview(4);
				}
			}else{
				costRecord.setReview(4);
			}
			
		}else if(costRecord.getReview() != 2){
			costRecord.setReview(4);
		}
		
		if(null != costRecord.getPayReview() && 0 == costRecord.getPayReview()){
			costRecord.setPayReview(4);
		}
		//预算成本自动审核通过
		if(costRecordModel.getBudgetType() == 0 && budgetCostAutoPass == 1 &&
				costRecordModel.getReview()==1 && reviewResult.getReviewStatus() == 2) {
			costRecord.setReview(2);
			
			//添加实际成本
			CostRecord cr = costManageService.copyCost(costRecord);
			cr.setUuid(UuidUtils.generUuid());
			cr.setPayNowLevel(1);
			cr.setPayReview(4);
			cr.setBudgetType(1);
			
			if (costAutoPass == 1){
				cr.setNowLevel(4);
				cr.setReview(2);
				if(reviewProcessService.exist(deptId.toString(), productType.toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString(), companyUuid)) {
					ReviewResult rr = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
					cr.setReviewUuid(rr.getReviewId());
				}
			}else if (costAutoPass == 0) {
				cr.setNowLevel(1);
				cr.setReview(4);
			}
			costManageService.saveCostRecord(cr);//保存实际成本
		}
		//实际成本自动审核通过
		if(costRecordModel.getBudgetType() == 1 && costAutoPass == 1
				&& costRecordModel.getReview()==1 && reviewResult.getReviewStatus() == 2) {
			costRecord.setReview(2);
		}
		costRecordDao.save(costRecord);
		if(costRecordModel.getTypeId()<ProductType.PRODUCT_VISA || costRecordModel.getTypeId()==ProductType.PRODUCT_CRUISE) {
			return "redirect:" + Global.getAdminPath() + "/costReview/activity/activityCostDetail/" + costRecordModel.getActivityId() + "/" + costRecordModel.getGroupId() + "/" + costRecordModel.getTypeId();
		}else if(costRecordModel.getTypeId()==ProductType.PRODUCT_VISA){
			return "redirect:"+Global.getAdminPath() + "/costReview/visa/visaCostDetail/" + costRecordModel.getActivityId();
		}else {
			return "redirect:" + Global.getAdminPath() + "/costReview/airticket/airticketCostDetail/" + costRecordModel.getActivityId();
		}
	}
	
	/**
	 * 成本审批、查看页面 
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="activityCostReviewDetail/{activityId}/{groupId}/{budgetType}", method=RequestMethod.GET)
	public String activityCostReviewDetail(@PathVariable(value="activityId") Long activityId, Long costId, 
			@PathVariable(value="groupId") Long groupId, @PathVariable(value="budgetType") Integer budgetType,
			Model model, HttpServletRequest request, HttpServletResponse response){
		
		model.addAttribute("costId", costId);
		model.addAttribute("read", request.getParameter("read"));
		model.addAttribute("head", request.getParameter("head"));
		model.addAttribute("budgetType", budgetType);
		TravelActivity travelActivity = travelActivityService.findById(activityId);
		model.addAttribute("travelActivity", travelActivity);
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		model.addAttribute("activityGroup", activityGroup);
		
		Dict dict = dictService.findByValueAndType(travelActivity.getActivityKind().toString(), "order_type");
		model.addAttribute("typename", dict.getLabel());
		Integer typeId = travelActivity.getActivityKind();
		model.addAttribute("typeId",typeId);

		if (activityGroup != null && travelActivity != null && activityGroup.getTravelActivity() != null 
				&& activityGroup.getTravelActivity().getId().equals(travelActivity.getId())) {
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = orderService.findOrderListByPayType(orderList, groupId);
			//需求0311，增加统计信息，订单总额，订单总人数。yudong.xu
			BigDecimal totalMoneySum = new BigDecimal(0);
			Integer orderPesonSum = 0;
			for (Map<Object, Object> map : orderList.getList()){
				orderPesonSum += (int) map.get("orderPersonNum");
				Double totalMoneyRMB = Double.parseDouble(map.get("totalMoneyRMB").toString().replace(",",""));
				totalMoneySum = totalMoneySum.add(new BigDecimal(totalMoneyRMB));
			}
			model.addAttribute("orderPesonSum", orderPesonSum);
			String totalMoneySumStr = MoneyNumberFormat.getRoundMoney(Double.valueOf(totalMoneySum.toString()), MoneyNumberFormat.POINT_TWO);
			model.addAttribute("totalMoneySum", totalMoneySumStr);
			model.addAttribute("orderList", orderList.getList());
			model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));
			
			if(budgetType == 0) {
				List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(groupId, typeId, costId);
				model.addAttribute("budgetReviewLog", budgetReviewLog);
			}else if(budgetType == 1) {
				List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(groupId, typeId, costId);
				model.addAttribute("actualReviewLog", actualReviewLog);
			}else if(budgetType == 2){
				List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(groupId, typeId, costId);
				model.addAttribute("budgetReviewLog", budgetReviewLog);
				List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(groupId, typeId, costId);
				model.addAttribute("actualReviewLog", actualReviewLog);
				List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(groupId, typeId, costId);
				model.addAttribute("paymentReviewLog", paymentReviewLog);
			}
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		return "review/cost/activityCostReviewDetail";
	}

	private CostRecord saveCostRecord(String visaId, Long groupId, Integer orderType, String deptIdStr) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		CostRecord costRecordReturn = new CostRecord();
		List<CostRecord> visaList = costManageService.getVisaCost(groupId);
		for (CostRecord costRecord : visaList) {
			if(Long.parseLong(visaId) == costRecord.getId()) {
				CostRecord cr = costManageService.copyCost(costRecord);
				cr.setActivityId(groupId);
				cr.setOrderType(orderType);
				cr.setDelFlag("0");
				cr.setReview(4);
				cr.setBudgetType(0);
				Date date = new Date();
				User user = UserUtils.getUser();
				cr.setCreateBy(user);
				cr.setCreateDate(date);
				cr.setUpdateBy(user.getId());
				cr.setUpdateDate(date);
				cr.setVisaId(Long.parseLong(visaId));
				cr.setOverseas(0);
				cr.setSupplyType(1);
				String bankName = "";
				String bankAccountCode = "";
				String sql = "select bank.bankName, bank.bankAccountCode from plat_bank_info bank, agentinfo agent " +
						"where bank.beLongPlatId = agent.id and bank.platType = 2 and bank.delFlag = 0 and agent.agentName = '拉美途'";
				List<Map<String, String>> bankList = costRecordDao.findBySql(sql, Map.class);
				if(bankList != null && bankList.size() > 0) {
					bankName = bankList.get(0).get("bankName");
					bankAccountCode = bankList.get(0).get("bankAccountCode");
				}
				cr.setBankName(bankName);
				cr.setBankAccount(bankAccountCode);
				Long reviewCompanyId=(long)0;
				Long payReviewCompanyId=(long)0;
//				String deptIdStr = request.getParameter("deptId");
				Long userId = UserUtils.getUser().getId();
				List<Long> deptList=  costManageService.getDeptList(userId);
				Long deptId = 0L;
				if(StringUtils.isNotEmpty(deptIdStr)) {
					deptId = Long.parseLong(deptIdStr);
				}
				if(deptId==0){ //部门为空
					reviewCompanyId=costManageService.findReviewCompanyId(companyId, 15, deptList);
					payReviewCompanyId=costManageService.findReviewCompanyId(companyId, 18, deptList);
				}else{
					Department department= departmentDao.findOne(deptId);
					long parentDept=department.getParentId();
					List<Long> listCompany = new ArrayList<Long>();
					listCompany = reviewCompanyDao.findReviewCompanyId(companyId,15,parentDept);
					if(listCompany.size()>0) {
						reviewCompanyId= listCompany.get(0);
					} else{
						reviewCompanyId=costManageService.findReviewCompanyId(companyId,15,deptList);
					}
					listCompany = reviewCompanyDao.findReviewCompanyId(companyId,18,parentDept);
					if(listCompany.size()>0) {
						payReviewCompanyId= listCompany.get(0);
					} else{
						payReviewCompanyId=costManageService.findReviewCompanyId(companyId,18,deptList);
					}
				}
				cr.setReviewCompanyId(reviewCompanyId);
				cr.setPayReviewCompanyId(payReviewCompanyId);
				cr.setPayReview(4);
				cr.setPayUpdateBy(Integer.parseInt(userId.toString()));
				cr.setPayUpdateDate(date);
				cr.setIsNew(2);
				costManageService.saveCostRecord(cr);
				costRecordReturn = cr;

//				CostRecord c = costManageService.copyCost(costRecord);
//				c.setActivityId(groupId);
//				c.setOrderType(orderType);
//				c.setReview(4);
//				c.setBudgetType(1);
//				c.setCreateBy(UserUtils.getUser());
//				c.setCreateDate(date);
//				c.setOverseas(0);
//				c.setSupplyType(1);
//				c.setUpdateBy(UserUtils.getUser().getId());
//				c.setUpdateDate(date);
////							c.setVisaId(Long.parseLong(visaId));
//				c.setBankName(bankName);
//				c.setBankAccount(bankAccountCode);
//				c.setPayStatus(0);
//				c.setPayReview(4);
//				c.setPayNowLevel(1);
//				c.setPayUpdateBy(Integer.parseInt(user.getId().toString()));
//				c.setPayUpdateDate(date);
//				c.setReviewCompanyId(reviewCompanyId);
//				c.setPayReviewCompanyId(payReviewCompanyId);
//				c.setPayUpdateBy(Integer.parseInt(userId.toString()));
//				c.setPayUpdateDate(date);
//				costManageService.saveCostRecord(c);

			}
		}
		return costRecordReturn;
	}
	
}
