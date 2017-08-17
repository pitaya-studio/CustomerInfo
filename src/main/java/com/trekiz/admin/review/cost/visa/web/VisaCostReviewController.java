package com.trekiz.admin.review.cost.visa.web;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="${adminPath}/costReview/visa")
public class VisaCostReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IVisaProductsService visaProductsService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private DictService dictService;
	@Autowired
	private VisaOrderService visaOrderService;
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
	@RequestMapping(value="visaCostApply")
	public String visaCostApply(HttpServletRequest request, HttpServletResponse response, Model model) {
		String activityId = request.getParameter("activityId");	//签证产品id
//		String productType = request.getParameter("orderType");	//产品类型
//		String groupId = request.getParameter("groupId");	//团期id
		Long deptId = Long.parseLong(request.getParameter("deptId"));	//部门id
		String userId = UserUtils.getUser().getId().toString();	//用户id
		String companyUuid = UserUtils.getUser().getCompany().getUuid().toString();	//批发商id
		String costIds = request.getParameter("costList");	//多个签证成本id，以逗号分隔
		
		//产品信息
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(Long.parseLong(activityId));
		
		String msg = "";
		
		ReviewResult reviewResult = null;
		String[] costId = costIds.split(",");
		for (int i = 0; i < costId.length; i++) {
			if(!"0".equals(costId[i])) {
				CostRecord costRecord = costRecordDao.getById(Long.parseLong(costId[i]));
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_STOCK);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, ProductType.PRODUCT_VISA);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_COST_RECORD_UUID, costRecord.getUuid());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, costRecord.getCreateBy().getId());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, costRecord.getCreateBy().getName());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityId);
//				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID, visaProducts);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
				
				if(costRecord.getBudgetType() == 0){//预算
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, ProductType.PRODUCT_VISA, Context.REVIEW_FLOWTYPE_STOCK, deptId, null, variables);
				}else if(costRecord.getBudgetType() == 1){//实际
					reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, ProductType.PRODUCT_VISA, Context.REVIEW_FLOWTYPE_ACTUAL_COST, deptId, null, variables);
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
		
		return msg;
	}

	/**
	 * 签证产品成本录入详情页
	 * @param visaProductId 签证id
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="visaCostDetail/{visaProductId}", method=RequestMethod.GET)
	public String visaCostDetail(@PathVariable(value="visaProductId") Long visaProductId,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);

		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaProductId);
		model.addAttribute("visaProduct", visaProduct);

		Long deptId=visaProduct.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
		model.addAttribute("deptId",deptId);

		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();	
		orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString());
		model.addAttribute("orderList", orderList);
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));

		List<Map<String, Object>> incomeList = costManageService.getRefunifoForCastList(visaProductId, 6);	//总收入
		model.addAttribute("incomeList", incomeList);

		//审批日志
		model.addAttribute("costLog",stockDao.findCostRecordLog(visaProductId, 6));
		List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(visaProductId, 6, null);
		model.addAttribute("budgetReviewLog", budgetReviewLog);
		List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(visaProductId, 6, null);
		model.addAttribute("actualReviewLog", actualReviewLog);
		List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(visaProductId, 6, null);
		model.addAttribute("paymentReviewLog", paymentReviewLog);

		return "review/cost/visaCostDetail";
	}
	
	/**
	 * 签证成本审核、查看
	 * @param visaProductId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="visaCostReviewDetail/{visaProductId}/{budgetType}", method=RequestMethod.GET)
	public String visaCostReviewDetail(@PathVariable(value="visaProductId") Long visaProductId,Long costId, @PathVariable(value="budgetType") Integer budgetType,
			HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("costId",costId);
		model.addAttribute("read", request.getParameter("read"));
		model.addAttribute("head", request.getParameter("head"));
		model.addAttribute("budgetType", budgetType);

		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);		

		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(visaProductId);
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString());
		//需求0311，增加统计信息，订单总额，订单总人数。yudong.xu --begin--
		BigDecimal totalMoneySum = new BigDecimal(0);
		Integer orderPesonSum = 0;
		for (Map<String, Object> map : orderList){
			orderPesonSum += (int) map.get("travel_num");
			Double totalMoneyRMB = Double.parseDouble(map.get("totalMoneyRMB").toString().replace(",",""));
			totalMoneySum = totalMoneySum.add(new BigDecimal(totalMoneyRMB));
		}
		model.addAttribute("orderPesonSum", orderPesonSum);
		String totalMoneySumStr = MoneyNumberFormat.getRoundMoney(Double.valueOf(totalMoneySum.toString()), MoneyNumberFormat.POINT_TWO);
		model.addAttribute("totalMoneySum", totalMoneySumStr);
		//--end--
		//update by shijun.liu 2016.05.16    bug:13882
		model.addAttribute("groupCode", visaProduct.getGroupCode());
		model.addAttribute("orderList", orderList);
		
		List<Dict> ordertype = dictService.findByType("order_type");
		model.addAttribute("ordertype", ordertype);	
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
		model.addAttribute("costLog",stockDao.findCostRecordLog(visaProductId, 6));
		
		if(budgetType == 0) {
			List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(visaProductId, 6, costId);
			model.addAttribute("budgetReviewLog", budgetReviewLog);
		}else if(budgetType == 1) {
			List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(visaProductId, 6, costId);
			model.addAttribute("actualReviewLog", actualReviewLog);
		}else if(budgetType == 2){
			List<CostPaymentReviewNewLog> budgetReviewLog = commonReviewService.getBudgetReviewLog(visaProductId, 6, costId);
			model.addAttribute("budgetReviewLog", budgetReviewLog);
			List<CostPaymentReviewNewLog> actualReviewLog = commonReviewService.getActualReviewLog(visaProductId, 6, costId);
			model.addAttribute("actualReviewLog", actualReviewLog);
			List<CostPaymentReviewNewLog> paymentReviewLog = commonReviewService.getPaymentReviewLog(visaProductId, 6, costId);
			model.addAttribute("paymentReviewLog", paymentReviewLog);
		}
		
		return "review/cost/visaCostReviewDetail";
	}

}
