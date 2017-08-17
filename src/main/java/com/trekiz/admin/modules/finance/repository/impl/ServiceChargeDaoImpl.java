package com.trekiz.admin.modules.finance.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.finance.param.ServiceChargePayParam;
import com.trekiz.admin.modules.finance.repository.IServiceChargeDao;
import com.trekiz.admin.modules.finance.result.ServiceChargePayListResult;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 服务费付款DAO实现类
 * @author  shijun.liu
 * @date    2016.08.30
 */
@Repository
public class ServiceChargeDaoImpl extends BaseDaoImpl implements IServiceChargeDao{

	@Override
	public List<Map<String, Object>> getServiceCharge4Confirm(Long orderId, Integer serviceChargeType) {
				
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.orderNum, g.groupCode, osc.service_charge_uuid AS serviceChargeSerial,o.cut_service_charge, ")
		   .append(" ( CASE WHEN osc.type = 0 THEN 'QUAUQ' WHEN ai.agent_parent = - 1 THEN ai.agentName ")
		   .append(" ELSE ( SELECT b.agentName FROM agentinfo b WHERE b.id = ai.agent_parent ) END ) AS agentName,o.id AS orderId,osc.type AS type ")
		   .append(" FROM productorder o, activitygroup g, agentinfo ai, order_service_charge osc ")
		   .append(" WHERE g.id = o.productGroupId AND ai.id = o.orderCompany AND osc.order_id = o.id ")
		   .append(" AND o.id = ").append(orderId)
		   .append(" AND osc.type = ").append(serviceChargeType);
		
		return this.findBySql(sql.toString(), Map.class);	 
	}

	@Override
	public List<Map<String, Object>> getPrintPaymentInfo(Long orderId, Integer serviceChargeType) {

		StringBuffer sql = new StringBuffer();	
	
		sql.append("SELECT o.createDate, g.groupCode, '交易服务费' AS funds, ")
		   .append(" ( SELECT u.`name` FROM sys_user u WHERE u.id = g.createBy ) AS operatorName, ")
		   .append(" osc.pay_time AS confirmPayDate, osc.pay_status AS payStatus, ");
		if (serviceChargeType == 0) {//quauq服务费
			sql.append(" 'QUAUQ' AS orderCompanyName, ");
		} else if (serviceChargeType == 1) {//总社服务费
			sql.append(" ( CASE ai.agent_parent WHEN - 1 THEN ai.agentName ELSE ( SELECT b.agentName ")
			   .append(" FROM agentinfo b WHERE b.id = ai.agent_parent ) END ) AS orderCompanyName, ");
		}
		sql.append(" ( SELECT r.remarks FROM refund r WHERE r.record_id = osc.ID AND r.moneyType = osc.type + 14 ")
		   .append(" ORDER BY r.create_date LIMIT 1 ) AS remarks, ")
		   .append(" osc.service_charge_uuid AS serviceChargeSerial, osc.print_time AS firstPrintTime ")
		   .append(" FROM productorder o, activitygroup g, order_service_charge osc, agentinfo ai ")
		   .append(" WHERE g.id = o.productGroupId AND osc.order_id = o.id AND ai.id = o.orderCompany AND o.delFlag = 0 ")
		   .append(" AND o.id = ").append(orderId).append(" AND osc.type = ").append(serviceChargeType);
		
		return this.findBySql(sql.toString(), Map.class);
	}

	public void getServiceChargePayList(Page page, ServiceChargePayParam param){
		List<Object> paramList = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN c.type=0 THEN 'QUAUQ' WHEN a.agent_parent =-1 THEN a.agentName ")
		   .append("WHEN a.agent_parent !=-1 THEN(SELECT agentName FROM agentinfo ag WHERE ag.id=a.agent_parent) ")
		   .append("ELSE '' END AS settleName,p.activity_kind AS orderType,g.groupCode,p.acitivityName AS groupName,")
		   .append("o.orderNum,o.salerName AS saler,(SELECT u.`name` FROM sys_user u WHERE u.id=g.createBy) AS jd,")
		   .append("(SELECT u.`name` FROM sys_user u WHERE u.id=o.createBy) AS creator,c.print_time,g.id AS groupId,")
		   .append("c.service_charge_uuid AS payMoney,c.pay_status,c.pay_time,c.print_status,c.id AS chargeId, ")
		   .append("o.id AS orderId,c.type AS chargeType,c.create_date AS createTime,c.update_date AS updateTime, ")
		   .append("CASE WHEN a.agent_parent = -1 THEN a.id ELSE a.agent_parent END AS agentId, ")
		   .append("p.id AS productId ")
		   .append(" FROM productorder o,travelactivity p,activitygroup g,order_service_charge c,agentinfo a ")
		   .append(" WHERE o.productId = p.id AND o.productGroupId = g.id AND g.srcActivityId = o.productId ")
		   .append(" AND o.id = c.order_id AND p.activity_kind = c.product_type AND o.orderCompany=a.id ")
		   .append(" AND EXISTS(SELECT 'x' FROM money_amount ma WHERE ma.serialNum =c.service_charge_uuid  ")
		   .append(" HAVING SUM(ma.amount) !=0) AND o.delFlag = '0' AND c.del_flag=0 AND p.proCompany=")
		   .append(UserUtils.getUser().getCompany().getId());

		// 团号
		if (StringUtils.isNotBlank(param.getGroupCode())){
			sql.append(" AND g.groupCode LIKE ?");
			paramList.add("%" + param.getGroupCode().trim() + "%");
		}

		// 结算方
		if (param.getSettleAgentId() != null){
			if (param.getSettleAgentId() == -3){ // -3定义为 QUAUQ
				sql.append(" AND c.type = 0 ");
			}else { // 1是总社结算方,-1表示没有上级渠道,是总社
				sql.append(" AND c.type = 1 AND (a.id = ? OR a.agent_parent = ?) ");
				paramList.add(param.getSettleAgentId());
				paramList.add(param.getSettleAgentId());
			}
		}

		// 计调
		if (param.getJd() != null){
			sql.append(" AND g.createBy = ? ");
			paramList.add(param.getJd());
		}
		// 销售
		if (param.getSalerId() != null){
			sql.append(" AND o.salerId = ? ");
			paramList.add(param.getSalerId());
		}
		// 团队类型
		if (param.getOrderType() != null && param.getOrderType() != 0){
			sql.append(" AND p.activity_kind = ? ");
			paramList.add(param.getOrderType());
		}
		// 付款状态
		if (StringUtils.isNotBlank(param.getPayStatus())){
			sql.append(" AND c.pay_status = ? ");
			paramList.add(param.getPayStatus());
		}
		// 打印状态
		if (StringUtils.isNotBlank(param.getPrintStatus())){
			sql.append(" AND c.print_status = ?");
			paramList.add(param.getPrintStatus());
		}

		// 付款金额处理
		String moneyBegin = param.getPayMoneyBegin();
		String moneyEnd = param.getPayMoneyEnd();
		if (StringUtils.isNotBlank(moneyBegin) && StringUtils.isNotBlank(moneyEnd)){
			BigDecimal begin = new BigDecimal(moneyBegin);
			BigDecimal end = new BigDecimal(moneyEnd);
			if (begin.compareTo(end) == 1){ // 开始金额大于结束金额,把小的放前面
				paramList.add(end.toString());
				paramList.add(begin.toString());
			}else {
				paramList.add(begin.toString());
				paramList.add(end.toString());
			}
			sql.append(" AND EXISTS(SELECT 'x' FROM money_amount m WHERE m.serialNum=c.service_charge_uuid ")
			.append(" AND m.amount BETWEEN ? AND ?) ");
		}else if (StringUtils.isNotBlank(moneyBegin)){
			sql.append(" AND EXISTS(SELECT 'x' FROM money_amount m WHERE m.serialNum=c.service_charge_uuid ")
		       .append(" AND m.amount >= ?) ");
			paramList.add(moneyBegin);
		}else if (StringUtils.isNotBlank(moneyEnd)){
			sql.append(" AND EXISTS(SELECT 'x' FROM money_amount m WHERE m.serialNum=c.service_charge_uuid ")
			   .append(" AND m.amount <= ?) ");
			paramList.add(moneyEnd);
		}

		// 出纳确认时间开始
		if (StringUtils.isNotBlank(param.getCashierConfirmDateBegin())){
			sql.append(" AND c.pay_status=1 AND c.pay_time >= ? ");
			paramList.add(param.getCashierConfirmDateBegin() + " 00:00:00");
		}
		// 出纳确认时间结束
		if (StringUtils.isNotBlank(param.getCashierConfirmDateEnd())){
			sql.append(" AND c.pay_status=1 AND c.pay_time <= ? ");
			paramList.add(param.getCashierConfirmDateEnd() + " 23:59:59");
		}

		if(com.trekiz.admin.common.utils.StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy("updateTime DESC");
		}

		super.findCustomPageBySql(page,sql.toString(), ServiceChargePayListResult.class,paramList.toArray());
	}

	public Integer getServiceChargeCount(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM order_service_charge c, productorder o, travelactivity p ");
		sql.append(" WHERE c.order_id = o.id AND o.productId = p.id AND c.product_type = p.activity_kind ");
		sql.append(" AND c.pay_status = 0 AND c.del_flag = 0 AND o.delFlag = 0 AND p.proCompany = ?");
		sql.append(" AND EXISTS ( SELECT 'x' FROM money_amount ma WHERE ma.serialNum = c.service_charge_uuid");
		sql.append(" HAVING SUM(ma.amount) != 0 )");
		List<Object> list = super.findBySql(sql.toString(),companyId);
		return Integer.parseInt(list.get(0).toString());
	}
}
