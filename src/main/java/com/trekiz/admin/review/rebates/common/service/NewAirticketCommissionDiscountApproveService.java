package com.trekiz.admin.review.rebates.common.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.rebates.common.input.FormInput;

@Service
@Transactional(readOnly = true)
public class NewAirticketCommissionDiscountApproveService extends BaseService  {
	
	@Autowired
	private IAirticketOrderDao  iAirticketOrderDao;
	
	public Page<Map<String, Object>> getComdiscountList(Page<Map<String, Object>> page, FormInput formInput){
		Long userId = UserUtils.getUser().getId();
		StringBuffer querySql = new StringBuffer();
		querySql.append(" select distinct r.order_no orderno,r.id reviewid,r.order_id orderid,r.group_code groupcode,r.product_name productname,");
		querySql.append(" r.product_id productid,r.product_type producttype,DATE_FORMAT(r.create_date,'%Y-%m-%d') createdate, DATE_FORMAT(r.create_date,'%H-%m-%s') createtime,r.create_by createby,r.agent agent,");
		//TODO  各产品添加相关的订单表 查询订单相关金额,相关列的别名要一致啊！
		switch (formInput.getProductType()) {
		case "7":
			querySql.append(" ao.total_money totalmoney,ao.payed_money payedmoney,ao.accounted_money accountedmoney,'机票返佣' costname,");
			break;
		}  
		querySql.append("r.operator operator,r.last_reviewer lastreviewer,r.status status,r.pay_status paystatus");
		querySql.append(" from review_new r left join review_process_money_amount rpma on r.id = rpma.reviewId");
		//TODO  各产品添加相关的订单表 别名统一为 ao
		switch (formInput.getProductType()) {
		case "7":
			querySql.append(" left join airticket_order ao on r.order_id = ao.id");
			break;
		}
		querySql.append(" where r.process_type = '" + Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN + "' and FIND_IN_SET ('" + userId + "', all_reviewer) ");
		//查询条件处理
		String key = formInput.getWholeSalerKey();
		if(StringUtils.isNotBlank(key)){
			querySql.append(" and (r.group_code like '%" + key).append("%' or r.product_name like '%" + key).append("%' or r.order_no like '%" + key + "%') ");
		}
		String productType = formInput.getProductType();
		if(StringUtils.isNotBlank(productType) && !"0".equals(productType)){
			querySql.append(" and r.product_type = " + productType+ " ");
		}
		String agentId = formInput.getAgent();
		if(StringUtils.isNotBlank(productType)){
			querySql.append(" and r.agent = " + agentId + " ");
		}
		String payStatus = formInput.getPayStatus();
		if(StringUtils.isNotBlank(payStatus)){
			querySql.append(" and r.pay_status = " + payStatus + " ");
		}
		String createBy = formInput.getCreateBy();
		if(StringUtils.isNotBlank(createBy)){
			querySql.append(" and r.agent = " + createBy + " ");
		}
		String status = formInput.getStatus();
		if(StringUtils.isNotBlank(status)){
			querySql.append(" and r.status = " + status + " ");
		}
		String rebateAmountFrom = formInput.getRebateAmountFrom();
		//金额过滤
		if(StringUtils.isNotBlank(rebateAmountFrom)){
			querySql.append(" and rpma.amount >= " + rebateAmountFrom + " ");
		}
		String rebateAmountTo = formInput.getRebateAmountTo();
		if(StringUtils.isNotBlank(rebateAmountTo)){
			querySql.append(" and rpma.amount <= " + rebateAmountTo + " ");
		}
		String approveDateFrom = formInput.getApproveDateFrom();
		if(StringUtils.isNotBlank(approveDateFrom)){
			querySql.append(" and r.create_date >= '" + approveDateFrom + " 00:00:00' ");
		}
		String approveDateTo = formInput.getApproveDateTo();
		if(StringUtils.isNotBlank(approveDateTo)){
			querySql.append(" and r.create_date <= '" + approveDateTo + " 23:59:59' ");
		}
		String operator = formInput.getOperator();
		if(StringUtils.isNotBlank(operator)){
			querySql.append(" and r.operator = " + operator + " ");
		}
		String isPrinted = formInput.getIsPrinted();
		if(StringUtils.isNotBlank(isPrinted)){
			querySql.append(" and r.print_status = " + isPrinted + " ");
		}
		//状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批
		String tabStatus= formInput.getTabStatus();
		if(StringUtils.isNotBlank(tabStatus)){
			int tabStatusInt = Integer.parseInt(tabStatus.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', (select l.create_by from review_log_new l where l.review_id = r.id and operation = 1 and l.active_flag = 1)) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', all_reviewer) and FIND_IN_SET('" + userId + "', r.skipped_assignee)");
			}
		}
		String orderBy = formInput.getOrderBy();
		if(StringUtils.isNotBlank(orderBy)){
			page.setOrderBy(orderBy);
		}else{
			page.setOrderBy(" r.critical_level");
		}
		return iAirticketOrderDao.findBySql(page, querySql.toString(), Map.class);
	}
}
