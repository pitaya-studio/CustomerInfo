package com.trekiz.admin.review.cost.activity.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.review.cost.common.CostParam;

/**
 * 成本审批Service接口
 * @author zhankui.zong
 * 
 */
public interface IActivityCostReviewService {

	Page<Map<String, Object>> getCostReviewList(Page<Map<String, Object>> page,
			CostParam costParam, Integer budgetType);
	
	List<Map<String, Object>> getCostList(List<Map<String, Object>> budgetInList, List<Map<String, Object>> budgetOutList,
			List<Map<String, Object>> actualInList, List<Map<String, Object>> actualOutList);
	
	List<Map<String, Object>> getCostList(List<Map<String, Object>> InList, List<Map<String, Object>> OutList);
	
	List<Map<String, Object>> getReviewLogNews(List<Map<String, Object>> costList);

}
