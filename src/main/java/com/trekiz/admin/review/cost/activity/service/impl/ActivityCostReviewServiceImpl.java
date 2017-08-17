package com.trekiz.admin.review.cost.activity.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.cost.activity.dao.IActivityCostReviewDao;
import com.trekiz.admin.review.cost.activity.service.IActivityCostReviewService;
import com.trekiz.admin.review.cost.common.CostParam;
import com.trekiz.admin.review.cost.common.CostUtils;

/**
 * 成本审批Service实现类
 * @author zhankui.zong
 *
 */
@Service
public class ActivityCostReviewServiceImpl implements IActivityCostReviewService {

	@Autowired
	private SystemService systemService;
	@Autowired
	private IActivityCostReviewDao activityCostReviewDao;
	
	/**
	 * 审批列表
	 */
	@Override
	public Page<Map<String, Object>> getCostReviewList(
			Page<Map<String, Object>> page, CostParam costParam, Integer budgetType) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		//获取登录用户督查权限：
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, CostUtils.getFlowType(budgetType));
		Page<Map<String, Object>> pageMap = activityCostReviewDao.getCostReviewList(page, costParam, budgetType, userReviewPermissionResultForm);
		
		BigDecimal totalPrice = new BigDecimal(0);
		for (Map<String, Object> map : pageMap.getList()) {
			 totalPrice = (BigDecimal) map.get("totalPrice");
			 map.put("totalPrice", MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));
		}
		return pageMap;
	}

	@Override
	public List<Map<String, Object>> getCostList(
			List<Map<String, Object>> budgetInList,
			List<Map<String, Object>> budgetOutList,
			List<Map<String, Object>> actualInList,
			List<Map<String, Object>> actualOutList) {
		
		List<Map<String, Object>> costList = new ArrayList<Map<String, Object>>();
		costList.addAll(budgetInList);
		costList.addAll(budgetOutList);
		costList.addAll(actualInList);
		costList.addAll(actualOutList);
		return costList;
	}
	
	@Override
	public List<Map<String, Object>> getCostList(
			List<Map<String, Object>> InList,
			List<Map<String, Object>> OutList) {
		
		List<Map<String, Object>> costList = new ArrayList<Map<String, Object>>();
		costList.addAll(InList);
		costList.addAll(OutList);
		return costList;
	}

	@Override
	public List<Map<String, Object>> getReviewLogNews(
			List<Map<String, Object>> costList) {
		return activityCostReviewDao.getReviewLogNews(costList);
	}

}
