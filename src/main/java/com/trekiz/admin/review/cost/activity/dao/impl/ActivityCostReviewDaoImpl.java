package com.trekiz.admin.review.cost.activity.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.cost.activity.dao.IActivityCostReviewDao;
import com.trekiz.admin.review.cost.common.CostParam;
import com.trekiz.admin.review.cost.common.CostUtils;

/**
 * 成本审批Dao实现类
 * @author zhankui.zong
 *
 */
@Repository
public class ActivityCostReviewDaoImpl extends BaseDaoImpl<Map<String, Object>> implements IActivityCostReviewDao {

	/**
	 * 成本审核列表
	 */
	@Override
	public Page<Map<String, Object>> getCostReviewList(
			Page<Map<String, Object>> page, CostParam costParam, Integer budgetType, UserReviewPermissionResultForm urprf) {
		Integer productType = costParam.getProductType();
		String where = sqlWhere(costParam, urprf);
		String field = "costId,productId,groupId,reviewId,groupCode,activityName,productType,createDate,updateDate,createBy,supplyType,supplyId,supplyName,budgetType,"
				+ "groupOpenDate,groupCloseDate,planPosition,freePosition,name,currencyId,totalPrice,rate,status,last_reviewer,current_reviewer,all_reviewer";
		Page<Map<String, Object>> result = null;
		if(productType == null || productType == Context.ORDER_TYPE_ALL) {//全部
			result = getCostReviewAll(page, field, where, budgetType);
		}else if(productType == Context.ORDER_TYPE_QZ) {//签证
			result = getCostReviewQz(page, field, where, budgetType);
		}else if(productType == Context.ORDER_TYPE_JP) {//机票
			result = getCostReviewJp(page, field, where, budgetType);
		}else{//团期产品
			result = getCostReviewDt(page, field, where, budgetType);
		}
		return result;
	}
	
	/**
	 * 审批列表--全部
	 * @param page
	 * @param field
	 * @param where
	 * @return
	 */
	private Page<Map<String, Object>> getCostReviewAll(
			Page<Map<String, Object>> page, String field, String where, Integer budgetType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(field).append(" FROM (")
			.append(" SELECT ")
			.append(" t.id productId, ")
			.append(" g.id groupId, ")
			.append(" r.id reviewId, ")
			.append(" g.groupCode, ")
			.append(" t.acitivityName activityName, ")
			.append(" t.activity_kind productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" g.groupOpenDate, ")
			.append(" g.groupCloseDate, ")
			.append(" g.planPosition, ")
			.append(" g.freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" travelactivity t, ")
			.append(" activitygroup g, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" t.id = g.srcActivityId ")
			.append(" AND g.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND t.proCompany = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType in (1,2,3,4,5,10)")
			.append(" UNION ")
			.append(" SELECT ")
			.append(" visa.id productId, ")
			.append(" '' groupId, ")
			.append(" r.id reviewId, ")
			.append(" visa.groupCode, ")
			.append(" visa.productName activityName, ")
			.append(" 6 productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" '--' groupOpenDate, ")
			.append(" '--' groupCloseDate, ")
			.append(" '' planPosition, ")
			.append(" '' freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" visa_products visa, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" visa.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND visa.proCompanyId = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType = 6")
			.append(" UNION ")
			.append(" SELECT ")
			.append(" airticket.id productId, ")
			.append(" '' groupId, ")
			.append(" r.id reviewId, ")
			.append(" airticket.group_code groupCode, ")
			.append(" airticket.activity_airticket_name activityName, ")
			.append(" 7 productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" airticket.startingDate groupOpenDate, ")
			.append(" airticket.returnDate groupCloseDate, ")
			.append(" airticket.reservationsNum planPosition, ")
			.append(" airticket.freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" activity_airticket airticket, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" airticket.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND airticket.proCompany = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType = 7")
			.append(" ) t WHERE 1 = 1 ").append(where);
		if(StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy("createDate desc");
		}
		return findBySql(page, sb.toString(), Map.class);
	}
	
	/**
	 * 审批列表--签证
	 * @param page
	 * @param field
	 * @param where
	 * @return
	 */
	private Page<Map<String, Object>> getCostReviewQz(
			Page<Map<String, Object>> page, String field, String where, Integer budgetType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(field).append(" FROM (")
			.append(" SELECT ")
			.append(" visa.id productId, ")
			.append(" '' groupId, ")
			.append(" r.id reviewId, ")
			.append(" visa.groupCode, ")
			.append(" visa.productName activityName, ")
			.append(" 6 productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" '--' groupOpenDate, ")
			.append(" '--' groupCloseDate, ")
			.append(" '' planPosition, ")
			.append(" '' freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" visa_products visa, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" visa.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND visa.proCompanyId = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType = 6")
			.append(" ) t WHERE 1 = 1 ").append(where);
		return findBySql(page, sb.toString(), Map.class);
	}
	
	/**
	 * 审批列表--机票
	 * @param page
	 * @param field
	 * @param where
	 * @return
	 */
	private Page<Map<String, Object>> getCostReviewJp(
			Page<Map<String, Object>> page, String field, String where, Integer budgetType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(field).append(" FROM (")
			.append(" SELECT ")
			.append(" airticket.id productId, ")
			.append(" '' groupId, ")
			.append(" r.id reviewId, ")
			.append(" airticket.group_code groupCode, ")
			.append(" airticket.activity_airticket_name activityName, ")
			.append(" 7 productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" airticket.startingDate groupOpenDate, ")
			.append(" airticket.returnDate groupCloseDate, ")
			.append(" airticket.reservationsNum planPosition, ")
			.append(" airticket.freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" activity_airticket airticket, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" airticket.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND airticket.proCompany = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType = 7")
			.append(" ) t WHERE 1 = 1 ").append(where);
		return findBySql(page, sb.toString(), Map.class);
	}
	
	/**
	 * 审批列表--团期产品
	 * @param page
	 * @param field
	 * @param where
	 * @return
	 */
	private Page<Map<String, Object>> getCostReviewDt(
			Page<Map<String, Object>> page, String field, String where, Integer budgetType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(field).append(" FROM (")
			.append(" SELECT ")
			.append(" t.id productId, ")
			.append(" g.id groupId, ")
			.append(" r.id reviewId, ")
			.append(" g.groupCode, ")
			.append(" t.acitivityName activityName, ")
			.append(" t.activity_kind productType, ")
			.append(" r.create_date createDate, ")
			.append(" r.update_date updateDate, ")
			.append(" r.create_by createBy, ")
			.append(" cost.supplyType, ")
			.append(" cost.supplyId, ")
			.append(" cost.supplyName, ")
			.append(" cost.budgetType,")
			.append(" g.groupOpenDate, ")
			.append(" g.groupCloseDate, ")
			.append(" g.planPosition, ")
			.append(" g.freePosition, ")
			.append(" cost.id AS costId,")
			.append(" cost.name, ")
			.append(" cost.currencyId, ")
			.append(" cost.price * cost.quantity totalPrice, ")
			.append(" cost.rate, ")
			.append(" CONCAT(r.status,'') status, ")
			.append(" r.last_reviewer, ")
			.append(" r.current_reviewer, ")
			.append(" r.all_reviewer ")
			.append(" FROM ")
			.append(" travelactivity t, ")
			.append(" activitygroup g, ")
			.append(" cost_record cost, ")
			.append(" review_new r, ")
			.append(" currency c ")
			.append(" WHERE ")
			.append(" t.id = g.srcActivityId ")
			.append(" AND g.id = cost.activityId ")
			.append(" AND cost.reviewUuid = r.id ")
			.append(" AND c.currency_id = cost.currencyId ")
			.append(" AND need_no_review_flag=0 ")
			.append(" AND cost.delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND process_type = '").append(CostUtils.getFlowType(budgetType) + "' ")
			.append(" AND t.proCompany = ").append(companyId)
			.append(" AND cost.budgetType = ").append(budgetType)
			.append(" AND cost.orderType in (1,2,3,4,5,10)")
			.append(" ) t WHERE 1 = 1 ").append(where);
		return findBySql(page, sb.toString(), Map.class);
	}
	
	/**
	 * SQL查询条件
	 * @param costParam
	 * @return
	 */
	private String sqlWhere(CostParam costParam, UserReviewPermissionResultForm urprf) {
		String groupCode = costParam.getGroupCode(); // 团号
		Integer productType = costParam.getProductType(); // 产品类型
		Integer supplyId = costParam.getSupplyId(); // 地接社id
		Integer agentId = costParam.getAgentId(); // 渠道商id
		String createDateStart = costParam.getCreateDateStart(); // 申请日期--开始
		String createDateEnd = costParam.getCreateDateEnd(); // 申请日期--结束
		Integer createBy = costParam.getCreateBy(); // 审批发起人
		Integer status = costParam.getStatus(); // 审批状态
		String priceStart = costParam.getPriceStart(); // 成本金额--开始
		String priceEnd = costParam.getPriceEnd(); // 成本金额--结束
		String groupOpenDateStart = costParam.getGroupOpenDateStart(); // 出团日期--开始
		String groupOpenDateEnd = costParam.getGroupOpenDateEnd(); // 出团日期--结束
		Integer reviewer = costParam.getReviewer(); //审批人
		String paymentType = costParam.getPaymentType();//渠道结算方式
		
		Long userId = UserUtils.getUser().getId();
		
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(groupCode)) {
			sb.append(" and (groupCode like '%").append(groupCode).append("%' or activityName like '%").append(groupCode).append("%')");
		}
		if(productType != null && productType != 0) {
			sb.append(" and productType = ").append(productType);
		}
		if(supplyId != null && supplyId != 0) {
			sb.append(" and supplyId = ").append(supplyId);
		}
		if(agentId != null && agentId != 0) {
			sb.append(" and supplyId = ").append(agentId);
		}
		if(StringUtils.isNotBlank(createDateStart)) {
			sb.append(" and date_format(createDate,'%Y-%m-%d') >= '").append(createDateStart).append("'");
		}
		if(StringUtils.isNotBlank(createDateEnd)) {
			sb.append(" and date_format(createDate,'%Y-%m-%d') <= '").append(createDateEnd).append("'");
		}
		if(createBy != null && createBy != 0) {
			sb.append(" and createBy = ").append(createBy);
		}
		if(status != null) {
			sb.append(" and status = ").append(status);
		}
		if(StringUtils.isNotBlank(priceStart)) {
			sb.append(" and totalPrice >= ").append(priceStart);
		}
		if(StringUtils.isNotBlank(priceEnd)) {
			sb.append(" and totalPrice <= ").append(priceEnd);
		}
		if(StringUtils.isNotBlank(groupOpenDateStart)) {
			sb.append(" and groupOpenDate >= '").append(groupOpenDateStart).append("'");
		}
		if(StringUtils.isNotBlank(groupOpenDateEnd)) {
			sb.append(" and groupOpenDate <= '").append(groupOpenDateEnd).append("'");
		}
		
		//部门
		Set<String> deptIds = urprf.getDeptId();
		//产品
		Set<String> prds = urprf.getProductTypeId();
		if(reviewer != null) {
			if(reviewer == Integer.parseInt(Context.REVIEW_TAB_ALL)) {//全部
//				if(prds != null && prds.size() > 0 && deptIds != null && deptIds.size() > 0) {
//					sb.append(" and (FIND_IN_SET ('" + userId + "', all_reviewer) or (productType in :prds and dept_id in :deptIds and not FIND_IN_SET ('" + userId + "', all_reviewer))) ");
//				}else{
					sb.append(" and FIND_IN_SET ('" + userId + "', all_reviewer)");
//				}
			}else if(reviewer == Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED)) {//待本人审批
				sb.append(" and status = 1 and FIND_IN_SET ('" + userId + "', current_reviewer) ");
			}else if(reviewer == Integer.parseInt(Context.REVIEW_TAB_REVIEWED)) {//本人已审批
//				sb.append(" and EXISTS (select log.review_id from review_log_new log where create_by = " + userId + " and log.operation = 1)");
				sb.append(" and reviewId in (select log.review_id from review_log_new log where (log.operation = 1 or log.operation=2) and active_flag = 1 and create_by = " + userId + ")");
			}else if(reviewer == Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED)) {//非本人审批
//				if(prds != null && prds.size() > 0 && deptIds != null && deptIds.size() > 0) {
//					sb.append(" and productType in :prds and dept_id in :deptIds and not FIND_IN_SET ('" + userId + "', all_reviewer) ");
//				}else{
					sb.append(" and not FIND_IN_SET ('" + userId + "', all_reviewer) ");
//				}
				
			}
		}
		
		if(StringUtils.isNotBlank(paymentType)){
			sb.append("and supplyId in (select id from agentinfo where paymentType = "+paymentType+")");
		}
		
		return sb.toString();
	}

	@Override
	public List<Map<String, Object>> getReviewLogNews(
			List<Map<String, Object>> costList) {
		StringBuffer sb = new StringBuffer();
		sb.append("select log.create_date, cost.`name`, CONCAT(log.operation,'') operation, log.create_by, log.remark ")
			.append(" from cost_record cost, review_new review, review_log_new log ")
			.append(" where cost.reviewUuid = review.id ")
			.append(" and review.id = log.review_id ");
		if(costList != null && costList.size() > 0) {
			sb.append("and review.id in (");
			String reviewUuids = "";
			for (Iterator<Map<String, Object>> iterator = costList.iterator(); iterator.hasNext();) {
				Map<String, Object> map = iterator.next();
				if(map.get("reviewUuid") != null) {
					reviewUuids += "'" + map.get("reviewUuid") + "',";
				}
			}
			reviewUuids = reviewUuids.substring(0, reviewUuids.length() - 1);
			sb.append(reviewUuids + ")");
		}
		
		sb.append(" ORDER BY log.create_date");
		return findBySql(sb.toString(), Map.class);
	}

}
