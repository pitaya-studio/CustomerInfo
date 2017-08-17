package com.trekiz.admin.modules.visa.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
public class VisaOrderListDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IVisaOrderListDao {
	@Override
	public List<Map<String, Object>> queryVisaOrdersByProductId(String productId) {
		StringBuffer strs = new StringBuffer();
		strs.append("SELECT ao.id orderId,ao.group_code groupCode, ao.order_no orderNo, ao.travel_num travel_num, ao.create_date createDate,")
			.append(" agent.agentName agentName, ao.total_money totalMoney, ao.payed_money payedMoney,")
			.append(" ao.accounted_money accountedMoney, (SELECT s.name FROM sys_user s WHERE s.id = ao.salerId) ")
			.append(" AS orderUserName,")
			.append("( SELECT FORMAT( SUM( mao.amount * IFNULL(mao.exchangerate, 0)), 2 ) FROM money_amount mao ")
			.append(" WHERE mao.moneyType = 13 AND mao.orderType = 6 ")
			.append(" AND mao.serialNum = ao.total_money ) AS totalMoneyRMB ")
			.append(" FROM visa_order ao ")
			.append(" LEFT JOIN agentinfo agent ON ao.agentinfo_id = agent.id ")
			.append(" WHERE ao.visa_product_id = ? AND ao.visa_order_status NOT IN (111, 100) AND ao.payStatus <> 99")
			.append(" AND ao.del_flag = '0' ");
		/** String queryOrdersByProductSql = "SELECT"
				+" ao.id orderId,"
				+" agent.agentName agentName,"											
				+" ao.order_no orderNo,"
				+" ao.travel_num travel_num,"		
				+" ao.create_date createDate,"				
				+" maoouter.moneyStr totalMoney,"
				+" maoouter1.moneyStr1 payedMoney,"
				+" maoouter2.moneyStr2 accountedMoney,"				
				+" su.name orderUserName,"				
				+" ag.groupOpenDate groupOpenDate,"
				+" ag.groupCode groupCode,"
				+" ag.activityKind activityKind,"
				+" ag.groupCloseDate groupCloseDate"
			+" FROM"
				+" visa_order ao"
			+" LEFT JOIN sys_user su ON ao.create_by = su.id"
			/**
			 * 将LEFT JOIN productorder po ON ao.main_order_code = po.id
			 * 改为LEFT JOIN productorder po ON ao.visa_product_id = po.id
			 * 原因：visa_order表中没有main_order_code字段，visa_order表通过visa_product_id（产品ID）关联产品productorder表
			 */
			/** +" LEFT JOIN productorder po ON ao.visa_product_id = po.id"
			+" LEFT JOIN activitygroup_view ag ON po.productGroupId = ag.id"
			+" LEFT JOIN agentinfo agent ON ao.agentinfo_id = agent.id"			
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where  mao.orderType = 6 and mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter ON maoouter.serialNum = ao.total_money"
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr1"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.orderType = 6 and mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter1 ON maoouter1.serialNum = ao.payed_money"
          +" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr2"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.orderType = 6 and mao.businessType = 1"
			+" group by mao.serialNum " 			
			+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
			
			+" WHERE"
				+" ao.visa_product_id = ? and ao.visa_order_status not in(111,100) and ao.payStatus <> 99 and ao.del_flag='0' "; */
		List<Map<String,Object>> visaOrders = findBySql(strs.toString(), Map.class, Long.parseLong(productId));
		return visaOrders;
	}
}
