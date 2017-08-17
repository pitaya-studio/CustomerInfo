package com.trekiz.admin.review.payment.comment.dao.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.payment.comment.dao.ICommonPaymentReviewDao;
import com.trekiz.admin.review.payment.comment.entity.PaymentParam;
import com.trekiz.admin.review.payment.comment.utils.PayMentUtils;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批模块，成本付款审批对应的Dao接口实现类
 * @author shijun.liu
 * @date 2015年11月17日
 */
@Repository
public class CommonPaymentReviewDaoImpl extends BaseDaoImpl<Map<String, Object>> implements ICommonPaymentReviewDao{

	private static final Log LOG = LogFactory.getLog(CommonPaymentReviewDaoImpl.class);
	@Autowired
	public SystemService systemService;
	
	@Override
	public Page<Map<String, Object>> getPaymentReviewList(
			Page<Map<String, Object>> page,PaymentParam paymentParam) {
		Object orderType = paymentParam.getOrderType();
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("create_date desc");
		}
		if(orderType == null || Integer.parseInt(String.valueOf(orderType)) == Context.ORDER_TYPE_ALL){
			return getPaymentReviewListAll(page, paymentParam);
		}else if(Integer.parseInt(String.valueOf(orderType)) == Context.ORDER_TYPE_JP){
			return getPaymentReviewListJP(page, paymentParam);
		}else if(Integer.parseInt(String.valueOf(orderType)) == Context.ORDER_TYPE_QZ){
			return getPaymentReviewListQZ(page, paymentParam);
		}else if(Integer.parseInt(String.valueOf(orderType)) == Context.ORDER_TYPE_HOTEL){
			return null;	//酒店产品暂不做		2015.11.18
		}else if(Integer.parseInt(String.valueOf(orderType)) == Context.ORDER_TYPE_ISLAND){
			return null;	//海岛游产品暂不做	2015.11.18
		}else{
			return getPaymentReviewListDT(page, paymentParam);
		}
	}
	
	/**
	 * 根据传递的参数拼接Where条件
	 * @param paymentParam	参数对象
	 * @author shijun.liu
	 * @return
	 * @date 2015.11.18
	 */
	private String optionParams(PaymentParam paymentParam){
		Long currentUserId = UserUtils.getUser().getId();
		StringBuffer str = new StringBuffer();
		String gpCodePrdName = paymentParam.getGroupCodeProductName();
		//团号/产品名称
		if(StringUtils.isNotBlank(gpCodePrdName)){
			str.append(" and (r.group_code like '%").append(gpCodePrdName)
			   .append("%' or r.product_name like '%").append(gpCodePrdName).append("%') ");
		}
		//订单类型
		if(null != paymentParam.getOrderType() && 
				Context.ORDER_TYPE_ALL != paymentParam.getOrderType()){
			str.append(" and r.product_type = ").append(paymentParam.getOrderType());
		}
		//地接社
		if(null != paymentParam.getSupplyId()){
			str.append(" and c.supplyId = ").append(paymentParam.getSupplyId())
			   .append(" and c.supplyType = 0 ");//supplyType为0表示地接社，1表示渠道商
		}
		//渠道商
		if(null != paymentParam.getAgentId()){
			str.append(" and c.supplyId = ").append(paymentParam.getAgentId())
			   .append(" and c.supplyType = 1 ");//supplyType为0表示地接社，1表示渠道商
		}
		//申请日期
		if(StringUtils.isNotBlank(paymentParam.getApplyBeginDate())){
			str.append(" and r.create_date >= '").append(paymentParam.getApplyBeginDate()).append(" 00:00:00'");
		}
		if(StringUtils.isNotBlank(paymentParam.getApplyEndDate())){
			str.append(" and r.create_date <= '").append(paymentParam.getApplyEndDate()).append(" 23:59:59'");
		}
		//审批发起人
		if(null != paymentParam.getReviewerId()){
			str.append(" and r.create_by = ").append(paymentParam.getReviewerId());
		}
		//付款金额
		if(StringUtils.isNotBlank(paymentParam.getPayMoneyBegin())){
			str.append(" and c.price * c.quantity >= ").append(paymentParam.getPayMoneyBegin());
		}
		if(StringUtils.isNotBlank(paymentParam.getPayMoneyEnd())){
			str.append(" and c.price * c.quantity <= ").append(paymentParam.getPayMoneyEnd());
		}
		//出团日期
		if(StringUtils.isNotBlank(paymentParam.getGroupOpenDateBegin())){
			str.append(" and g.groupOpenDate >= '").append(paymentParam.getGroupOpenDateBegin()).append("'");
		}
		if(StringUtils.isNotBlank(paymentParam.getGroupOpenDateEnd())){
			str.append(" and g.groupOpenDate <= '").append(paymentParam.getGroupOpenDateEnd()).append("'");
		}
		//出纳确认
		if(null != paymentParam.getPayStatus() && -1 != paymentParam.getPayStatus()){
			str.append(" and c.payStatus = ").append(paymentParam.getPayStatus());
		}
		//审批状态
		if(null != paymentParam.getReviewStatus() && -1 != paymentParam.getReviewStatus()){
			str.append(" and r.status = ").append(paymentParam.getReviewStatus());
		}
		//渠道结算方式
		if(StringUtils.isNotBlank(paymentParam.getPaymentType())){
			str.append(" and supplyId in (select id from agentinfo where paymentType = "+paymentParam.getPaymentType()+")");
		}
		String tabStatus = paymentParam.getTabStatus();
		//全部，解释：显示本人参与的审批和本人有督查权限的数据
		if(PayMentUtils.ALL.equals(tabStatus)){
			str.append(" and ( find_in_set(").append(currentUserId).append(", r.all_reviewer) ");
			String deptProduct = getPermissionDeptAndProduct();
			if(StringUtils.isNotBlank(deptProduct)){
				str.append(" or ( ").append(deptProduct).append(" AND not find_in_set (").append(currentUserId)
				   .append(", r.all_reviewer) ) ");
			}
			str.append(" ) ");
		}
		//待本人审批，解释：显示所有待本人审批的数据
		if(PayMentUtils.CURRENT_USER_REVIEW.equals(tabStatus)){
			str.append(" and r.status = ").append(ReviewConstant.REVIEW_STATUS_PROCESSING)
			   .append(" and find_in_set(").append(currentUserId).append(", r.current_reviewer) ");
		}
		//本人已审批,解释：显示本人已做过审批或者驳回操作的数据
		if(PayMentUtils.CURRENT_USER_REVIEWED.equals(tabStatus)){
			str.append(" and exists (select review_id from review_log_new log where log.review_id = r.id and log.create_by = ")
			   .append(currentUserId).append(" AND log.operation in (1,2) AND log.active_flag = 1 ) ");
		}
		//非本人审批, 解释：本人未参与审批，但有查看权限的审批数据
		if(PayMentUtils.NOT_CURRENT_USER_REVIEW.equals(tabStatus)){
			str.append(" and not find_in_set(").append(currentUserId).append(", r.all_reviewer) ");
			String deptProduct = getPermissionDeptAndProduct();
			if(StringUtils.isNotBlank(deptProduct)){
				str.append(" and ").append(deptProduct);
			}
		}
		return str.toString();
	}
	
	/**
	 * 查询当前用户所拥有的督查权限
	 * @return
	 * @author shijun.liu
	 * @date   2015.12.07
	 */
	private String getPermissionDeptAndProduct(){
		StringBuffer str = new StringBuffer();
		StringBuffer dept = new StringBuffer();         	//有查看权限的部门
		StringBuffer productType = new StringBuffer();		//有查看权限的产品类型
		Long currentUserId = UserUtils.getUser().getId();	//当前登录用户
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();//当前登录用户所在公司
		UserReviewPermissionResultForm userReview = systemService.
				findPermissionByUserIdAndCompanyUuidAndFlowType(currentUserId,
						currentCompanyUuid, Context.REVIEW_FLOWTYPE_PAYMENT);
		Set<String> deptIdSet = userReview.getDeptId();
		Set<String> productTypeSet = userReview.getProductTypeId();
		for (String deptId:deptIdSet){
			dept.append(deptId).append(",");
		}
		for (String productTypeId : productTypeSet){
			productType.append(productTypeId).append(",");
		}
		if(dept.toString().length() != 0){
			dept.delete(dept.toString().length() - 1, dept.toString().length()).toString();
			str.append(" r.dept_id in (").append(dept.toString()).append(")");
		}
		if(productType.toString().length() != 0){
			productType.delete(productType.toString().length()-1, productType.toString().length());
			str.append(" and r.product_type in (").append(productType.toString()).append(") ");
		}
		return str.toString();
	}
	
	/**
	 * 查询所有产品(单团类，机票，签证)数据的成本付款审批数据
	 * @param page				分页对象
	 * @param paymentParam		参数对象
	 * @author shijun.liu
	 * @return
	 * @date 2015.11.18
	 */
	private Page<Map<String, Object>> getPaymentReviewListAll(
			Page<Map<String, Object>> page, PaymentParam paymentParam) {
		String dtWhere = optionParams(paymentParam);
		String groupOpenDateWhere = "";
		if(StringUtils.isNotBlank(paymentParam.getGroupOpenDateBegin()) || 
				StringUtils.isNotBlank(paymentParam.getGroupOpenDateEnd())){
			groupOpenDateWhere = " where groupOpenDate <> '--' ";
		}
		//机票签证产品无团期
		paymentParam.setGroupOpenDateBegin(null);
		paymentParam.setGroupOpenDateEnd(null);
		String otherWhere = optionParams(paymentParam);
		StringBuffer str = new StringBuffer();
		String currentCompanyId = UserUtils.getUser().getCompany().getUuid();
		str.append(" SELECT                                    ")
		   .append(" 	review_uuid,                           ")
		   .append("    cost_record_uuid,       			   ")
		   .append(" 	group_code,                            ")
		   .append(" 	product_name,                          ")
		   .append(" 	product_type,                          ")
		   .append(" 	create_date,                           ")
		   .append(" 	create_by,                             ")
		   .append(" 	supplyName,                            ")
		   .append(" 	agentName,                             ")
		   .append(" 	supplyId,                              ")
		   .append(" 	groupOpenDate,                         ")
		   .append(" 	groupCloseDate,                        ")
		   .append(" 	planPosition,                          ")
		   .append(" 	freePosition,                          ")
		   .append(" 	name,                                  ")
		   .append(" 	price,                      		   ")
		   .append(" 	currencyId,                            ")
		   .append(" 	rate,                                  ")
		   .append(" 	last_reviewer,                         ")
		   .append(" 	current_reviewer,                      ")
		   .append(" 	status,                                ")
		   .append(" 	payStatus,                             ")
		   .append(" 	groupId,                               ")
		   .append(" 	productId,                             ")
		   .append("    update_date,						   ")
		   .append("    comment,				 		       ")
		   .append("    costId					   		       ")
		   .append(" FROM                                      ")
		   .append(" 	(                                      ")
		   .append(" 		SELECT                             ")
		   .append(" 			r.id review_uuid,              ")
		   .append(" 			c.uuid cost_record_uuid,       ")
		   .append(" 			r.group_code,                  ")
		   .append(" 			r.product_name,                ")
		   .append(" 			r.product_type,                ")
		   .append(" 			r.create_date,                 ")
		   .append(" 			r.create_by,                   ")
		   .append("			CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	 		CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append(" 		c.supplyId,                        ")
		   .append(" 		g.groupOpenDate,                   ")
		   .append(" 		g.groupCloseDate,                  ")
		   .append(" 		g.planPosition,                    ")
		   .append(" 		g.freePosition,                    ")
		   .append(" 		c.`name`,                          ")
		   .append(" 		c.price * c.quantity as price ,    ")
		   .append(" 		c.currencyId,                      ")
		   .append(" 		c.rate,                            ")
		   .append(" 		r.last_reviewer,                   ")
		   .append(" 		r.current_reviewer,                ")
		   .append(" 		CAST(r.`status` AS INTEGER) as status,")
		   .append(" 		c.payStatus,                       ")
		   .append(" 		g.id AS groupId,                   ")
		   .append(" 		g.srcActivityId AS productId,      ")
		   .append(" 		r.update_date, 				       ")
		   .append(" 		c.comment, 				           ")
		   .append(" 		c.id AS costId  		           ")
		   .append(" 	FROM                                   ")
		   .append(" 		review_new r,                      ")
		   .append(" 		cost_record c,                     ")
		   .append(" 		activitygroup g                    ")
		   .append(" 	WHERE                                  ")
		   .append(" 		r.id = c.pay_review_uuid           ")
		   .append(" 	AND c.activityId = g.id                ")
		   .append(" 	AND r.need_no_review_flag = 0          ")
		   .append(" 	AND r.product_type = c.orderType       ")
		   .append(" 	AND r.product_type in (1,2,3,4,5,10)   ")
		   .append(" 	AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append(" 	AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("	AND r.company_id = '").append(currentCompanyId).append("' ").append(dtWhere)
		   .append(" 	UNION                                  ")
		   .append(" 		SELECT                             ")
		   .append(" 			r.id review_uuid,              ")
		   .append(" 			c.uuid cost_record_uuid,       ")
		   .append(" 			g.groupCode AS group_code,     ")
		   .append(" 			r.product_name,                ")
		   .append(" 			r.product_type,                ")
		   .append(" 			r.create_date,                 ")
		   .append(" 			r.create_by,                   ")
		   .append("			CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	 		CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append(" 		c.supplyId,                        ")
		   .append(" 		'--' AS groupOpenDate,             ")
		   .append(" 		'--' AS groupCloseDate,            ")
		   .append(" 		- 1 AS planPosition,               ")
		   .append(" 		- 1 AS freePosition,               ")
		   .append(" 		c.`name`,                          ")
		   .append(" 		c.price * c.quantity as price,     ")
		   .append(" 		c.currencyId,                      ")
		   .append(" 		c.rate,                            ")
		   .append(" 		r.last_reviewer,                   ")
		   .append(" 		r.current_reviewer,                ")
		   .append(" 		CAST(r.`status` AS INTEGER) as status,")
		   .append(" 		c.payStatus,                       ")
		   .append(" 		- 1 AS groupId,                    ")
		   .append(" 		g.id AS productId,                 ")
		   .append(" 		r.update_date, 				       ")
		   .append(" 		c.comment, 				           ")
		   .append(" 		c.id AS costId   		           ")
		   .append(" 	FROM                                   ")
		   .append(" 		review_new r,                      ")
		   .append(" 		cost_record c,                     ")
		   .append(" 		visa_products g                    ")
		   .append(" 	WHERE                                  ")
		   .append(" 		r.id = c.pay_review_uuid           ")
		   .append(" 	AND c.activityId = g.id                ")
		   .append(" 	AND r.product_type = c.orderType       ")
		   .append(" 	AND r.need_no_review_flag = 0          ")
		   .append(" 	AND r.product_type = ").append(Context.ORDER_TYPE_QZ)
		   .append(" 	AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append(" 	AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("	AND r.company_id = '").append(currentCompanyId).append("' ").append(otherWhere)
		   .append(" 	UNION                                  ")
		   .append(" 		SELECT                             ")
		   .append(" 			r.id review_uuid,              ")
		   .append(" 			c.uuid cost_record_uuid,       ")
		   .append(" 			r.group_code,                  ")
		   .append(" 			r.product_name,                ")
		   .append(" 			r.product_type,                ")
		   .append(" 			r.create_date,                 ")
		   .append(" 			r.create_by,                   ")
		   .append("			CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	 		CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append(" 		c.supplyId,                        ")
		   .append(" 		'--' AS groupOpenDate,             ")
		   .append(" 		'--' AS groupCloseDate,            ")
		   .append(" 		g.reservationsNum AS planPosition, ")
		   .append(" 		g.freePosition AS freePosition,    ")
		   .append(" 		c.`name`,                          ")
		   .append(" 		c.price * c.quantity as price,     ")
		   .append(" 		c.currencyId,                      ")
		   .append(" 		c.rate,                            ")
		   .append(" 		r.last_reviewer,                   ")
		   .append(" 		r.current_reviewer,                ")
		   .append(" 		CAST(r.`status` AS INTEGER) as status,")
		   .append(" 		c.payStatus,                       ")
		   .append(" 		- 1 AS groupId,                    ")
		   .append(" 		g.id AS productId,                 ")
		   .append(" 		r.update_date,                      ")
		   .append(" 		c.comment,                          ")
		   .append(" 		c.id AS costId                     ")
		   .append(" 	FROM                                   ")
		   .append(" 		review_new r,                      ")
		   .append(" 		cost_record c,                     ")
		   .append(" 		activity_airticket g               ")
		   .append(" 	WHERE                                  ")
		   .append(" 		r.id   = c.pay_review_uuid         ")
		   .append(" 	AND c.activityId = g.id                ")
		   .append(" 	AND r.product_type = c.orderType       ")
		   .append(" 	AND r.need_no_review_flag = 0          ")
		   .append(" 	AND r.product_type = ").append(Context.ORDER_TYPE_JP)
		   .append(" 	AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append(" 	AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("	AND r.company_id = '").append(currentCompanyId).append("' ").append(otherWhere)
		   .append(" 	) t ").append(groupOpenDateWhere);
		//LOG.info(str.toString());
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询单团类产品的成本付款审批记录
	 * @param page				分页对象
	 * @param paymentParam		参数对象
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.18
	 */
	private Page<Map<String, Object>> getPaymentReviewListDT(
			Page<Map<String, Object>> page, PaymentParam paymentParam) {
		String where = optionParams(paymentParam);
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("	r.id review_uuid,")
		   .append("	c.uuid cost_record_uuid,")
		   .append("	r.group_code,")
		   .append("	r.product_name,")
		   .append("	r.product_type,")
		   .append("	r.create_date,")
		   .append("	r.create_by,")
		   .append("	CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append("	c.supplyId,")
		   .append("	g.groupOpenDate,")
		   .append("	g.groupCloseDate,")
		   .append("	g.planPosition,")
		   .append("	g.freePosition,")
		   .append("	c.`name`,")
		   .append("	c.price * c.quantity as price,")
		   .append("	c.currencyId,")
		   .append("	c.rate,")
		   .append("	r.last_reviewer,")
		   .append("	r.current_reviewer,")
		   .append("	CAST(r.`status` AS INTEGER) as status,")
		   .append("	c.payStatus,")
		   .append("	g.id AS groupId,")
		   .append("	g.srcActivityId AS productId, ")
		   .append("	r.update_date ")
		   .append(" FROM ")
		   .append(" 	review_new r,")
		   .append(" 	cost_record c,")
		   .append(" 	activitygroup g ")
		   .append(" WHERE ")
		   .append(" 	r.id = c.pay_review_uuid ")
		   .append("  AND c.activityId = g.id ")
		   .append("  AND r.product_type = c.orderType")
		   .append("  AND r.need_no_review_flag = 0 ")
		   .append("  AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append("  AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("  AND r.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("'")
		   .append(where);
		//LOG.info(str.toString());
 		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询机票产品的成本付款审批记录
	 * @param page				分页对象
	 * @param paymentParam		参数对象
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.18
	 */
	private Page<Map<String, Object>> getPaymentReviewListJP(
			Page<Map<String, Object>> page, PaymentParam paymentParam) {
		//机票产品无团期
		paymentParam.setGroupOpenDateBegin(null);
		paymentParam.setGroupOpenDateEnd(null);
		String where = optionParams(paymentParam);
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("	r.id review_uuid,")
		   .append("	c.uuid cost_record_uuid,")
		   .append("	r.group_code,")
		   .append("	r.product_name,")
		   .append("	r.product_type,")
		   .append("	r.create_date,")
		   .append("	r.create_by,")
		   .append("	CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append("	c.supplyId,")
		   .append("	'--' as groupOpenDate,")
		   .append("	'--' as groupCloseDate,")
		   .append("	g.reservationsNum as planPosition,")
		   .append("	g.freePosition as freePosition,")
		   .append("	c.`name`,")
		   .append("	c.price * c.quantity as price,")
		   .append("	c.currencyId,")
		   .append("	c.rate,")
		   .append("	r.last_reviewer,")
		   .append("	r.current_reviewer,")
		   .append("	CAST(r.`status` AS INTEGER) as status,")
		   .append("	c.payStatus,")
		   .append("	-1 AS groupId,")
		   .append("	g.id AS productId, ")
		   .append("	r.update_date, ")
		   .append("	c.id AS costId ")
		   .append(" FROM ")
		   .append(" 	review_new r,")
		   .append(" 	cost_record c,")
		   .append(" 	activity_airticket g ")
		   .append(" WHERE ")
		   .append(" 	r.id = c.pay_review_uuid ")
		   .append("  AND c.activityId = g.id ")
		   .append("  AND r.product_type = c.orderType ")
		   .append("  AND r.need_no_review_flag = 0 ")
		   .append("  AND r.product_type = ").append(Context.ORDER_TYPE_JP)
		   .append("  AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append("  AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("  AND r.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("'")
		   .append(where);
		//LOG.info(str.toString());
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询签证产品的成本付款审批记录
	 * @param page				分页对象
	 * @param paymentParam		参数对象
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.18
	 */
	private Page<Map<String, Object>> getPaymentReviewListQZ(
			Page<Map<String, Object>> page, PaymentParam paymentParam) {
		//签证产品无团期
		paymentParam.setGroupOpenDateBegin(null);
		paymentParam.setGroupOpenDateEnd(null);
		String where = optionParams(paymentParam);
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("	r.id review_uuid,")
		   .append("	c.uuid cost_record_uuid,")
		   .append("	g.groupCode AS group_code,")
		   .append("	r.product_name,")
		   .append("	r.product_type,")
		   .append("	r.create_date,")
		   .append("	r.create_by,")
		   .append("	CASE c.supplyType WHEN 0 THEN c.supplyName END AS supplyName,")
		   .append("	CASE c.supplyType WHEN 1 THEN c.supplyName END AS agentName,")
		   .append("	c.supplyId,")
		   .append("	'--' as groupOpenDate,")
		   .append("	'--' as groupCloseDate,")
		   .append("	-1 as planPosition,")
		   .append("	-1 as freePosition,")
		   .append("	c.`name`,")
		   .append("	c.price * c.quantity as price,")
		   .append("	c.currencyId,")
		   .append("	c.rate,")
		   .append("	r.last_reviewer,")
		   .append("	r.current_reviewer,")
		   .append("	CAST(r.`status` AS INTEGER) as status,")
		   .append("	c.payStatus,")
		   .append("	-1 AS groupId,")
		   .append("	g.id AS productId, ")
		   .append("	r.update_date, ")
		   .append("	c.id AS costId ")
		   .append(" FROM ")
		   .append(" 	review_new r,")
		   .append(" 	cost_record c,")
		   .append(" 	visa_products g ")
		   .append(" WHERE ")
		   .append(" 	r.id = c.pay_review_uuid ")
		   .append("  AND c.activityId = g.id ")
		   .append("  AND r.product_type = c.orderType ")
		   .append("  AND r.need_no_review_flag = 0 ")
		   .append("  AND r.product_type = ").append(Context.ORDER_TYPE_QZ)
		   .append("  AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_PAYMENT + "' ")
		   .append("  AND r.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append("  AND r.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("'")
		   .append(where);
		//LOG.info(str.toString());
		return findBySql(page, str.toString(), Map.class);
	}
	
}
