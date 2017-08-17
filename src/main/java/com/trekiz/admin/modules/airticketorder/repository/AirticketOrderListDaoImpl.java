package com.trekiz.admin.modules.airticketorder.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class AirticketOrderListDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IAirticketOrderListDao {
	
	@SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(AirticketOrderListDaoImpl.class);
	/**
	 * 查询订单列表
	 */
		@Override
	public Page<Map<String,Object>> queryAirticketOrderListByCond(Page<Map<String,Object>> orderListPage, Map<String,String> condMap) {
		Long companyId = UserUtils.getCompanyIdForData();
			String baseSql = "SELECT"
							//by sy 去重复
//							+" ao.id id,"
							+" DISTINCT ao.id  AS id,"
							+" ao.paymentStatus paymentStatus,"
							+" ao.nagentName nagentName,"
							+" ao.order_no orderNo,"
							+" ao.order_state order_state,"
							+" ao.front_money front_money,"
							+" ao.total_money total_money,"
							+" ao.payed_money payed_money,"
							+" ao.accounted_money accounted_money,"
							+" ao.salerId salerId,"
							+" ao.salerName salerName,"
							+" maoouter.moneyStr totalMoney,"
							+" maoouter1.moneyStr1 payedMoney,"
							+" maoouter2.moneyStr2 accountedMoney,"
							+" ao.create_date createDate,"
							+" ao.comments comments,"
							+" ao.place_holder_type place_holder_type,"
							+" ao.remaind_days remaindDays,"
							+" ao.person_num personNum,"
							+" ao.product_type_id realOrderType,"//add by jiangyang
							+" ao.type orderType,"
							+" ao.group_code orderGroupCode,"
							+" ao.seen_flag seenFlag,"
							+" ai.id agentId,"
							+" ai.agentName agentName,"
//							+" tt.contactsName conu,"
							+" su.name createUserName,"
							+" aa.airlines airlines,"
							+" aa.id airticketId,"
							+" aa.product_code as chanpName,"
							+" cast(aa.airType as signed) airType,"
							+" ao.comments remark,"
							+" ag.groupOpenDate groupOpenDate,"
							+" ag.groupCloseDate groupCloseDate,"
							+" ao.lockStatus lockStatus,"
							+" ao.activationDate activationDate,"
							+" ao.occupyType occupyType,"
							+" aa.settlementAdultPrice settlementAdultPrice,"
							+" aa.lockStatus activityLockStatus,"
							+" cur.currency_mark currencyMark,"
							+" aa.isSection isSection,"
							+" aa.createBy activityCreateBy,"
							+" ao.agentinfo_id agentinfoId,"
							+" ao.main_order_id main_order_id "
						+" FROM"
							+" airticket_order ao"
						+" LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id"					
						+" LEFT JOIN ordercontacts tt ON tt.orderId = ao.id "
						+" LEFT JOIN sys_user su ON ao.create_by = su.id"
						+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
						+" LEFT JOIN sys_user aasu ON aa.createBy = aasu.id"
						+" LEFT JOIN productorder po ON ao.main_order_id = po.id"
						+" LEFT JOIN activitygroup ag ON po.productGroupId = ag.id"
						+" LEFT JOIN currency cur ON aa.currency_id = cur.currency_id "
						+" LEFT JOIN ("
									+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId separator '+') moneyStr"
									+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
									+" where mao.moneyType = 13  and mao.businessType = 1 and c.create_company_id = " + companyId
									+" group by mao.serialNum " 
									+" ) maoouter ON maoouter.serialNum = ao.total_money"
									
						+" LEFT JOIN ("
									+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId separator '+') moneyStr1"
									+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
									+" where mao.moneyType = 5  and mao.businessType = 1 and c.create_company_id = " + companyId
									+" group by mao.serialNum " 
									+" ) maoouter1 ON maoouter1.serialNum = ao.payed_money"
						
						+" LEFT JOIN ("
									+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId separator '+') moneyStr2"
									+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
									+" where mao.moneyType = 4  and mao.businessType = 1 and c.create_company_id = " + companyId
									+" group by mao.serialNum " 
									+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
									
						+" WHERE"
							+" ( 1!=1 ";
		String endSql = ")";
		
		List<Object> condVals = new ArrayList<Object>();
		
		//动态查询条件
		String condSql = "";
		if(condMap.get("showType")!=null && !"0".equals(condMap.get("showType").toString().trim()) && !"".equals(condMap.get("showType").toString().trim())){
			condSql+=" and ao.order_state = ?";
			condVals.add(Integer.parseInt((String)condMap.get("showType")));
		}
		if(condMap.get("orderNumOrOrderGroupCode")!=null && condMap.get("orderNumOrOrderGroupCode")!=""){
			condSql+= " and (ao.order_no = ? or ao.group_code = ?)";
			condVals.add((String)condMap.get("orderNumOrOrderGroupCode"));
			condVals.add((String)condMap.get("orderNumOrOrderGroupCode"));
		}
		if(condMap.get("airType")!=null && condMap.get("airType")!=""){
			condSql+=" and aa.airType = ?";
			condVals.add((String)condMap.get("airType"));
		}
		if(condMap.get("ticketType")!=null && condMap.get("ticketType")!=""){
			condSql+=" and aa.ticket_area_type = ?";
			condVals.add((String)condMap.get("ticketType"));
		}
		if(condMap.get("fromAreaId")!=null && condMap.get("fromAreaId")!=""){
			condSql+=" and aa.departureCity = ?";
			condVals.add((String)condMap.get("fromAreaId"));
		}
		if(condMap.get("targetAreaId")!=null && condMap.get("targetAreaId")!=""){
			condSql+=" and aa.arrivedCity in ( ? ) ";
			condVals.add((String)condMap.get("targetAreaId"));
		}
		if(condMap.get("contact")!=null && condMap.get("contact")!=""){
			condSql+=" and tt.contactsName like ?";
			condVals.add("%"+(String)condMap.get("contact")+"%");
		}
		if(condMap.get("airticket_id")!=null && condMap.get("airticket_id")!=""){
			condSql+=" and ao.airticket_id = ?";
			condVals.add(condMap.get("airticket_id"));
		}
		//Mod start by jiangyang
		if(condMap.get("op")!=null && condMap.get("op")!=""){
			condSql+=" and aasu.id = ?";
			condVals.add((String)condMap.get("op"));
		}
		if(condMap.get("saler")!=null && condMap.get("saler")!=""){
			condSql+=" and ao.salerId = ?";
			condVals.add((String)condMap.get("saler"));
		}
		if(condMap.get("picker")!=null && condMap.get("picker")!=""){
			condSql+=" and ao.create_by = ?";
			condVals.add((String)condMap.get("picker"));
		}
		if(condMap.get("invoiceStatus")!=null && condMap.get("invoiceStatus")!=""){
			
			if("1".equals(condMap.get("invoiceStatus"))){
				condSql+=" and not exists(select 1 from orderinvoice oi where ao.id=oi.orderId " +
                        " and oi.verifyStatus in(0,1) and oi.orderType = 7)";
        	}
        	if("2".equals(condMap.get("invoiceStatus"))){
        		condSql+=" and exists(select 1 from orderinvoice oi where ao.id=oi.orderId " +
                        " and oi.verifyStatus in(0,1) and oi.orderType = 7)";
        	}

		}
		if(condMap.get("receiptStatus")!=null && condMap.get("receiptStatus")!=""){
			
			if("1".equals(condMap.get("receiptStatus"))){
				condSql+=" and not exists(select 1 from orderreceipt oi where ao.id=oi.orderId " +
                        " and oi.verifyStatus in(0,1)and oi.orderType = 7)";
        	}
        	if("2".equals(condMap.get("receiptStatus"))){
        		condSql+=" and exists(select 1 from orderreceipt oi where ao.id=oi.orderId " +
        				" and oi.verifyStatus in(0,1)and oi.orderType = 7)";
        	}
			
		}
		//Mod end   by jiangyang
		
		if(condMap.get("agentId")!=null && condMap.get("agentId")!=""){
			condSql+=" and ai.id = ? ";
			condVals.add(Long.parseLong((String)condMap.get("agentId")));
		}
		//出发日期
		if(condMap.get("startAirTime")!=null && condMap.get("startAirTime")!=""){
			condSql+=" and aa.startingDate >=?";
			condVals.add(condMap.get("startAirTime").toString()+" 00:00:00 ");
		}
		if(condMap.get("endAirTime")!=null && condMap.get("endAirTime")!=""){
			condSql+=" and aa.startingDate <= ? ";
			condVals.add(condMap.get("endAirTime").toString()+" 23:59:59 ");
		}
		//返回日期
		if(condMap.get("returnStartAirTime")!=null && condMap.get("returnStartAirTime")!=""){
			condSql+=" and aa.returnDate >= ? ";
			condVals.add(condMap.get("returnStartAirTime").toString()+" 00:00:00 ");
		}
		if(condMap.get("returnEndAirTime")!=null && condMap.get("returnEndAirTime")!=""){
			condSql+=" and aa.returnDate <= ? ";
			condVals.add(condMap.get("returnEndAirTime").toString()+" 23:59:59 ");
		}
		condSql+=" and ao.del_flag = '0' ";
		//排序条件
		String orderByHead = " order by  ";//去掉了按照渠道id排序
		String orderBySql = "";
		boolean orderFlag = false;
		if(condMap.get("orderCreateDateSort")!=null && condMap.get("orderCreateDateSort")!=""){
			orderBySql+=" ao.create_date "+(String)condMap.get("orderCreateDateSort");
			orderFlag = true;
		}
		if(condMap.get("orderUpdateDateSort")!=null && condMap.get("orderUpdateDateSort")!=""){
			if(orderFlag){
				orderBySql+=", ao.update_date "+(String)condMap.get("orderUpdateDateSort");
			}else{
				orderBySql+=" ao.update_date "+(String)condMap.get("orderUpdateDateSort");
				orderFlag = true;
			}
		}
		if(condMap.get("startFlightDateSort")!=null && condMap.get("startFlightDateSort")!=""){
			if(orderFlag){
				orderBySql+=", aa.startingDate "+(String)condMap.get("startFlightDateSort");
			} else {
				orderBySql+=" aa.startingDate "+(String)condMap.get("startFlightDateSort");
				orderFlag = true;
			}
		}
		if(condMap.get("arrivalFlightDateSort")!=null && condMap.get("arrivalFlightDateSort")!=""){
			if(orderFlag){
				orderBySql+=", aa.returnDate "+(String)condMap.get("arrivalFlightDateSort");
			} else {
				orderBySql+=" aa.returnDate "+(String)condMap.get("arrivalFlightDateSort");
				orderFlag = true;
			}
		}
		if(!"".equals(orderBySql.trim())){
			orderByHead += orderBySql;
		} else {
			orderByHead += " ao.agentinfo_id";
		}
		//组织完整的sql
		String sqlString = baseSql + condSql + endSql + orderByHead;
		return super.findPageBySql(orderListPage, sqlString, Map.class, condVals.toArray());
	}
}
