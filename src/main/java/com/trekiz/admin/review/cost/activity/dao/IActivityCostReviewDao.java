package com.trekiz.admin.review.cost.activity.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.review.cost.common.CostParam;

/**
 * 成本审批Dao接口
 * @author zhankui.zong
 *
 */
public interface IActivityCostReviewDao {

	Page<Map<String, Object>> getCostReviewList(Page<Map<String, Object>> page,
			CostParam costParam, Integer budgetType, UserReviewPermissionResultForm urprf);

	List<Map<String, Object>> getReviewLogNews(
			List<Map<String, Object>> costList);

}
