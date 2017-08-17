package com.trekiz.admin.review.cost.common;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewProcess;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.review.cost.activity.service.IActivityCostReviewService;
/**
 * 成本审批工具类
 * @author zzk
 *
 */
public class CostUtils {
	
	private static ReviewProcessService reviewProcessService = SpringContextHolder.getBean(ReviewProcessService.class);
	private static IActivityCostReviewService activityCostReviewService = SpringContextHolder.getBean(IActivityCostReviewService.class);
	

	/**
	 * 获取无需审批状态
	 * @param companyUuid
	 * @param deptId
	 * @param productType
	 * @param processType
	 * @return
	 */
//	public static String getApproveStatus(String companyUuid, String deptId, String productType, String processType) {
//		ReviewProcess reviewProcess = reviewProcessService.obtainReviewProcess(companyUuid, deptId, productType, processType);
//		if(reviewProcess != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(reviewProcess.getProcessKey())) {
//			return "1";
//		}else{
//			return "0";
//		}
//	}
	
	/**
	 * 根据budgetType获取审批流程
	 * @param budgetType
	 * @return
	 */
	public static Integer getFlowType(Integer budgetType) {
		Integer flowType = null;
		if (budgetType == 0) {
			flowType = Context.REVIEW_FLOWTYPE_STOCK;
		} else if (budgetType == 1) {
			flowType = Context.REVIEW_FLOWTYPE_ACTUAL_COST;
		}
		return flowType;
	}
	
	/**
	 * 获取无需审批状态
	 * @param companyUuid
	 * @param deptId
	 * @param productType
	 * @param processType
	 * @param model
	 */
	public static void getApproveStatus(String companyUuid, String deptId, String productType, Model model) {
		//预算成本无需审批
		ReviewProcess reviewProcess1 = reviewProcessService.obtainReviewProcess(companyUuid, deptId, productType, Context.REVIEW_FLOWTYPE_STOCK.toString());
		if(reviewProcess1 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(reviewProcess1.getProcessKey())) {
			model.addAttribute("budgetApproveStatus", 1);
			model.addAttribute("budgetEnble", reviewProcess1.getEnable());
		}else{
			model.addAttribute("budgetApproveStatus", 0);
		}
		
		//实际成本无需审批
		ReviewProcess reviewProcess2 = reviewProcessService.obtainReviewProcess(companyUuid, deptId, productType, Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString());
		if(reviewProcess2 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(reviewProcess2.getProcessKey())) {
			model.addAttribute("actualApproveStatus", 1);
			model.addAttribute("actualEnble", reviewProcess2.getEnable());
		}else{
			model.addAttribute("actualApproveStatus", 0);
		}
		
		//成本付款无需审批
		ReviewProcess reviewProcess3 = reviewProcessService.obtainReviewProcess(companyUuid, deptId, productType, Context.REVIEW_FLOWTYPE_PAYMENT.toString());
		if(reviewProcess3 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(reviewProcess3.getProcessKey())) {
			model.addAttribute("payApproveStatus", 1);
		}else{
			model.addAttribute("payApproveStatus", 0);
		}
		
	}
	
	public static void getCostLog(List<Map<String, Object>> InList, List<Map<String, Object>> OutList, Model model) {
		List<Map<String, Object>> costList = activityCostReviewService.getCostList(InList, OutList);
		List<Map<String, Object>> costLog = activityCostReviewService.getReviewLogNews(costList);
		model.addAttribute("costLog", costLog);
	}
	
}
