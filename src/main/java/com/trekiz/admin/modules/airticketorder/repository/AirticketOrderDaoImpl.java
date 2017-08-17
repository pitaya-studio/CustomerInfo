package com.trekiz.admin.modules.airticketorder.repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.OrderAgentAjax;
import com.trekiz.admin.modules.airticketorder.entity.OrderTravelAjax;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;

@Repository
public class AirticketOrderDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IAirticketOrderDao{
	
	@SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(AirticketOrderDaoImpl.class);
	/**
	 * 查询订单列表
	 */
		@Override
	public Page<Map<String,Object>> queryAirticketOrderListByCond(HttpServletRequest request, HttpServletResponse response,String whereSql,Map<String,Object> condMap) {
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
							+" ao.main_order_id main_order_id, "
							+" ao.salerName saler"
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
		String sqlString = baseSql+whereSql+condSql+endSql+orderByHead;
		return super.findPageBySql(new Page<Map<String,Object>>(request, response), sqlString, Map.class, condVals.toArray());
	}
	/**
	 * 查询订单详情
	 */
	@Override
	public Map<String, Object> queryAirticketOrderDetailInfoById(String orderId) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//订单信息
		String orderBaseInfoSql = "SELECT"
									+" ao.id orderId,"
									+" ore.old_rebates oreo,"
									+" ao.nagentName nagentName,"
									+" su.name userName,"
									+" ao.create_date orderCreateDate,"
									+" aa.createBy createBy,"
									+" aa.id airticketId,"
									+" ao.order_no orderNo,"
									+" ao.total_money total_money,"
									+" ao.group_code orderGroupCode,"
									+" ao.type type,"
									+" ao.person_num personNum,"
									+" ao.adult_num adultNum,"
									+" ao.child_num childNum,"
									+" ao.special_num specialNum,"
									+" ao.salerId AS saler,"
									+" ao.salerName AS salerName,"
									+" cast(aa.airType as signed) airType,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice,2)) adultPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice + aa.taxamt,2)) adultTaxPrice,"//成人含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice,2)) childPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice + aa.taxamt,2)) childTaxPrice,"//儿童含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice,2)) specialPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice + aa.taxamt,2)) specialTaxPrice,"//特殊人群含税价
									+" aa.remark remark,"
									+" concat(c.currency_mark,FORMAT(aa.taxamt,2)) taxamt,"
									+" aa.currency_id currencyId,"
									+" ao.agentinfo_id agentId,"
									+" au.name activityCreateName,"
									+" agent.agentName agentName,"
									+" ai.contactsName agentContact,"
									+" ai.contactsAddress agentAddress,"
									+" ai.contactsFax agentFax,"
									+" ai.contactsQQ agentQQ,"
									+" ai.contactsEmail agentEmail,"
									+" agent.remarks agentRemarks,"
									+" ai.contactsTel agentTel,"
									+" aa.freePosition freePosition,"
									+" aa.createDate productCreateDate,"
									+" ao.other_fee otherFee,"
									+" maoouter.moneyStr totalMoney,"
									+" maoouter2.moneyStr2 accountedMoney,"
									+" rebate.grpbt groupRebate,"
									+" ao.main_order_id mainOrderId,"
									+" agr.groupCode activityGroupCode,"
									+" aa.departureCity departureCity,"
									+" aa.arrivedCity arrivedCity,"
									+" aa.intermodalType intermodalType,"
									+" aa.reservationsNum reservationsNum,"
									+" aa.specialremark specialremark "
								+" FROM"
									+" airticket_order ao"
								+" LEFT JOIN sys_user su ON ao.create_by = su.id"
								+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
								+" LEFT JOIN sys_user au ON aa.createby = au.id"
								+" LEFT JOIN activitygroup agr ON ao.activitygroup_id = agr.id"
								+" LEFT JOIN  ordercontacts  ai ON ao.id = ai.orderId and ai.orderType=7 "
								+" LEFT JOIN agentinfo agent on ao.agentinfo_id=agent.id"
								+" LEFT JOIN currency c ON aa.currency_id = c.currency_id "
								+" LEFT JOIN order_rebates ore on ao.id = ore.orderId  "
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter ON maoouter.serialNum = ao.total_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr2"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 4 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum, CONCAT(c.currency_mark, mao.amount) as grpbt"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" WHERE c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) rebate on rebate.serialNum = ao.schedule_back_uuid"
								
								+" WHERE ao.id = ?";
		
		

		

		
		List<Map<String,Object>> orderList = findBySql(orderBaseInfoSql,Map.class, Long.parseLong(orderId));
		
		if(orderList==null || orderList.size()<=0) return new HashMap<String,Object>();
		
		Map<String,Object> orderInfoMap = orderList.get(0);//订单信息
		
		//处理订单中的航段信息
		handleAirticketOrderFlightInfo(orderInfoMap);
		
		
		//处理订单中的游客+联运
		handleTravelerStrategy(orderId,orderInfoMap);
		
		//处理机票订单中的游客信息
		handleAirticketOrderTravel(orderId, orderInfoMap);
		
		//处理机票订单中的支付信息
		handleAirticketOrderPayInfo(orderId, orderInfoMap);
		
		return orderInfoMap;
	}
	
	/**
	 * 查询订单详情(新返佣)
	 */
	@Override
	public Map<String, Object> queryAirticketOrderDetailInfoByIdNew(String orderId) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//订单信息
		String orderBaseInfoSql = "SELECT"
									+" ao.id orderId,"
									+" ore.oldRebates oreo,"
									+" ao.nagentName nagentName,"
									+" su.name userName,"
									+" ao.create_date orderCreateDate,"
									+" aa.createBy createBy,"
									+" aa.id airticketId,"
									+" ao.order_no orderNo,"
									+" ao.total_money total_money,"
									+" ao.group_code orderGroupCode,"
									+" ao.type type,"
									+" ao.person_num personNum,"
									+" ao.adult_num adultNum,"
									+" ao.child_num childNum,"
									+" ao.special_num specialNum,"
									+" ao.salerId AS saler,"
									+" ao.salerName AS salerName,"
									+" cast(aa.airType as signed) airType,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice,2)) adultPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice + aa.taxamt,2)) adultTaxPrice,"//成人含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice,2)) childPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice + aa.taxamt,2)) childTaxPrice,"//儿童含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice,2)) specialPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice + aa.taxamt,2)) specialTaxPrice,"//特殊人群含税价
									+" aa.remark remark,"
									+" concat(c.currency_mark,FORMAT(aa.taxamt,2)) taxamt,"
									+" aa.currency_id currencyId,"
									+" ao.agentinfo_id agentId,"
									+" au.name activityCreateName,"
									+" agent.agentName agentName,"
									+" ai.contactsName agentContact,"
									+" ai.contactsAddress agentAddress,"
									+" ai.contactsFax agentFax,"
									+" ai.contactsQQ agentQQ,"
									+" ai.contactsEmail agentEmail,"
									+" agent.remarks agentRemarks,"
									+" ai.contactsTel agentTel,"
									+" aa.freePosition freePosition,"
									+" aa.createDate productCreateDate,"
									+" ao.other_fee otherFee,"
									+" maoouter.moneyStr totalMoney,"
									+" maoouter2.moneyStr2 accountedMoney,"
									+" rebate.grpbt groupRebate,"
									+" ao.main_order_id mainOrderId,"
									+" agr.groupCode activityGroupCode,"
									+" aa.departureCity departureCity,"
									+" aa.arrivedCity arrivedCity,"
									+" aa.intermodalType intermodalType,"
									+" aa.reservationsNum reservationsNum"
								+" FROM"
									+" airticket_order ao"
								+" LEFT JOIN sys_user su ON ao.create_by = su.id"
								+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
								+" LEFT JOIN sys_user au ON aa.createby = au.id"
								+" LEFT JOIN activitygroup agr ON ao.activitygroup_id = agr.id"
								+" LEFT JOIN  ordercontacts  ai ON ao.id = ai.orderId and ai.orderType=7 "
								+" LEFT JOIN agentinfo agent on ao.agentinfo_id=agent.id"
								+" LEFT JOIN currency c ON aa.currency_id = c.currency_id "
								+" LEFT JOIN rebates ore on ao.id = ore.orderId  "
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter ON maoouter.serialNum = ao.total_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr2"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 4 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum, CONCAT(c.currency_mark, mao.amount) as grpbt"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" WHERE c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) rebate on rebate.serialNum = ao.schedule_back_uuid"
								
								+" WHERE ao.id = ?";
		
		

		

		
		List<Map<String,Object>> orderList = findBySql(orderBaseInfoSql,Map.class, Long.parseLong(orderId));
		
		if(orderList==null || orderList.size()<=0) return new HashMap<String,Object>();
		
		Map<String,Object> orderInfoMap = orderList.get(0);//订单信息
		
		//处理订单中的航段信息
		handleAirticketOrderFlightInfo(orderInfoMap);
		
		
		//处理订单中的游客+联运
		handleTravelerStrategy(orderId,orderInfoMap);
		
		//处理机票订单中的游客信息
		handleAirticketOrderTravelNew(orderId, orderInfoMap);
		
		//处理机票订单中的支付信息
		handleAirticketOrderPayInfo(orderId, orderInfoMap);
		
		return orderInfoMap;
	}
	
	
	
	
	/**
	 * 查询订单详情(新返佣)
	 */
	@Override
	public Map<String, Object> queryAirticketOrderDetailInfoByIdNewRefund(String orderId) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//订单信息
		String orderBaseInfoSql = "SELECT"
									+" ao.id orderId,"
									+" ore.oldRebates oreo,"
									+" ao.nagentName nagentName,"
									+" su.name userName,"
									+" ao.create_date orderCreateDate,"
									+" aa.createBy createBy,"
									+" aa.id airticketId,"
									+" ao.order_no orderNo,"
									+" ao.total_money total_money,"
									+" ao.group_code orderGroupCode,"
									+" ao.type type,"
									+" ao.person_num personNum,"
									+" ao.adult_num adultNum,"
									+" ao.child_num childNum,"
									+" ao.special_num specialNum,"
									+" ao.salerId AS saler,"
									+" ao.salerName AS salerName,"
									+" cast(aa.airType as signed) airType,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice,2)) adultPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice + aa.taxamt,2)) adultTaxPrice,"//成人含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice,2)) childPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice + aa.taxamt,2)) childTaxPrice,"//儿童含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice,2)) specialPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice + aa.taxamt,2)) specialTaxPrice,"//特殊人群含税价
									+" aa.remark remark,"
									+" concat(c.currency_mark,FORMAT(aa.taxamt,2)) taxamt,"
									+" aa.currency_id currencyId,"
									+" ao.agentinfo_id agentId,"
									+" au.name activityCreateName,"
									+" agent.agentName agentName,"
									+" ai.contactsName agentContact,"
									+" ai.contactsAddress agentAddress,"
									+" ai.contactsFax agentFax,"
									+" ai.contactsQQ agentQQ,"
									+" ai.contactsEmail agentEmail,"
									+" agent.remarks agentRemarks,"
									+" ai.contactsTel agentTel,"
									+" aa.freePosition freePosition,"
									+" aa.createDate productCreateDate,"
									+" ao.other_fee otherFee,"
									+" maoouter.moneyStr totalMoney,"
									+" maoouter2.moneyStr2 accountedMoney,"
									+" rebate.grpbt groupRebate,"
									+" ao.main_order_id mainOrderId,"
									+" agr.groupCode activityGroupCode,"
									+" aa.departureCity departureCity,"
									+" aa.arrivedCity arrivedCity,"
									+" aa.intermodalType intermodalType,"
									+" aa.reservationsNum reservationsNum"
								+" FROM"
									+" airticket_order ao"
								+" LEFT JOIN sys_user su ON ao.create_by = su.id"
								+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
								+" LEFT JOIN sys_user au ON aa.createby = au.id"
								+" LEFT JOIN activitygroup agr ON ao.activitygroup_id = agr.id"
								+" LEFT JOIN  ordercontacts  ai ON ao.id = ai.orderId and ai.orderType=7 "
								+" LEFT JOIN agentinfo agent on ao.agentinfo_id=agent.id"
								+" LEFT JOIN currency c ON aa.currency_id = c.currency_id "
								+" LEFT JOIN rebates ore on ao.id = ore.orderId  "
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter ON maoouter.serialNum = ao.total_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr2"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 4 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum, CONCAT(c.currency_mark, mao.amount) as grpbt"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" WHERE c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) rebate on rebate.serialNum = ao.schedule_back_uuid"
								
								+" WHERE ao.id = ?";
		
		

		

		
		List<Map<String,Object>> orderList = findBySql(orderBaseInfoSql,Map.class, Long.parseLong(orderId));
		
		if(orderList==null || orderList.size()<=0) return new HashMap<String,Object>();
		
		Map<String,Object> orderInfoMap = orderList.get(0);//订单信息
		
		//处理订单中的航段信息
		handleAirticketOrderFlightInfo(orderInfoMap);
		
		
		//处理订单中的游客+联运
		handleTravelerStrategy(orderId,orderInfoMap);
		
		//处理机票订单中的游客信息
		handleAirticketOrderTravelNewRefund(orderId, orderInfoMap);
		
		//处理机票订单中的支付信息
		handleAirticketOrderPayInfo(orderId, orderInfoMap);
		
		return orderInfoMap;
	}
	
	
	/**
	 * 查询订单详情
	 * by sy0908
	 * 之前的机票单办订单详情数据只取的第一条 ，此方法把详情联系人信息分离出去
	 * 新增handleAirticketOrderContact方法，重新组装联系人信息到MAP
	 */
	@Override
	public Map<String, Object> queryAirticketOrderDetailInfoByIdAddcontact(String orderId) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//订单信息
		String orderBaseInfoSql = "SELECT"
									+" ao.id orderId,"
									+" ore.old_rebates oreo,"
									+" ao.nagentName nagentName,"
									+" su.name userName,"
									+" ao.create_date orderCreateDate,"
									+" aa.createBy createBy,"
									+" ao.create_by orderCreateBy,"
									+" aa.id airticketId,"
									+" ao.order_no orderNo,"
									+" ao.total_money total_money,"
									+" ao.group_code orderGroupCode,"
									+" ao.type type,"
									+" ao.person_num personNum,"
									+" ao.adult_num adultNum,"
									+" ao.child_num childNum,"
									+" ao.special_num specialNum,"
									+" ao.salerId AS saler,"
									+" ao.salerName AS salerName,"
									+" ao.comments AS comments,"
									+" cast(aa.airType as signed) airType,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice,2)) adultPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementAdultPrice + aa.taxamt,2)) adultTaxPrice,"//成人含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice,2)) childPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementcChildPrice + aa.taxamt,2)) childTaxPrice,"//儿童含税价
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice,2)) specialPrice,"
									+" concat(c.currency_mark,FORMAT(aa.settlementSpecialPrice + aa.taxamt,2)) specialTaxPrice,"//特殊人群含税价
									+" aa.remark remark,"
									+" concat(c.currency_mark,FORMAT(aa.taxamt,2)) taxamt,"
									+" aa.currency_id currencyId,"
									+" ao.agentinfo_id agentId,"
									+" au.name activityCreateName,"
									+" agent.agentName agentName,"
//									+" ai.contactsName agentContact,"
//									+" ai.contactsAddress agentAddress,"
//									+" ai.contactsFax agentFax,"
//									+" ai.contactsQQ agentQQ,"
//									+" ai.contactsEmail agentEmail,"
									+" agent.remarks agentRemarks,"
//									+" ai.contactsTel agentTel,"
									+" aa.freePosition freePosition,"
									+" aa.createDate productCreateDate,"
									+" ao.other_fee otherFee,"
									+" maoouter.moneyStr totalMoney,"
									+" maoouter2.moneyStr2 accountedMoney,"
									+" rebate.grpbt groupRebate,"
									+" ao.main_order_id mainOrderId,"
									+" agr.groupCode activityGroupCode,"
									+" aa.departureCity departureCity,"
									+" aa.arrivedCity arrivedCity,"
									+" aa.intermodalType intermodalType,"
									+" aa.reservationsNum reservationsNum, "
									+" aa.specialremark specialremark "
								+" FROM"
									+" airticket_order ao"
								+" LEFT JOIN sys_user su ON ao.create_by = su.id"
								+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
								+" LEFT JOIN sys_user au ON aa.createby = au.id"
								+" LEFT JOIN activitygroup agr ON ao.activitygroup_id = agr.id"
//								+" LEFT JOIN  ordercontacts  ai ON ao.id = ai.orderId and ai.orderType=7 "
								+" LEFT JOIN agentinfo agent on ao.agentinfo_id=agent.id"
								+" LEFT JOIN currency c ON aa.currency_id = c.currency_id "
								+" LEFT JOIN order_rebates ore on ao.id = ore.orderId  "
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter ON maoouter.serialNum = ao.total_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,FORMAT(mao.amount,2)) separator '+') moneyStr2"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 4 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum, CONCAT(c.currency_mark, mao.amount) as grpbt"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" WHERE c.create_company_id = " + companyId + " and mao.uid = " + orderId
											+" group by mao.serialNum " 
											+" ) rebate on rebate.serialNum = ao.schedule_back_uuid"
								
								+" WHERE ao.id = ?";
		
		

		

		
		List<Map<String,Object>> orderList = findBySql(orderBaseInfoSql,Map.class, Long.parseLong(orderId));
		
		if(orderList==null || orderList.size()<=0) return new HashMap<String,Object>();
		
		Map<String,Object> orderInfoMap = orderList.get(0);//订单信息
		
		//处理订单中的航段信息
		handleAirticketOrderFlightInfo(orderInfoMap);
		
		//处理订单中的联系人信息
		handleAirticketOrderContact(orderId,orderInfoMap);
		
		//处理订单中的游客+联运
		handleTravelerStrategy(orderId,orderInfoMap);
		
		//处理机票订单中的游客信息
		handleAirticketOrderTravel(orderId, orderInfoMap);
		
		//处理机票订单中的支付信息
		handleAirticketOrderPayInfo(orderId, orderInfoMap);
		
		return orderInfoMap;
	}
	
	
	
	
	
public Map<String, Object> queryAirticketOrderDetailInfoById(String orderId,String travelId) {
		
		//订单信息
		String orderBaseInfoSql = "SELECT"
									+" ao.id orderId,"
									+" su.name userName,"
									+" ao.create_date orderCreateDate,"
									+" aa.createBy createBy,"
									+" aa.id airticketId,"
									+" ao.order_no orderNo,"
									+" ao.group_code orderGroupCode,"
									+" ao.type type,"
									+" ao.person_num personNum,"
									+" ao.adult_num adultNum,"
									+" ao.child_num childNum,"
									+" ao.special_num specialNum,"
									+" cast(aa.airType as signed) airType,"
									+" concat(c.currency_mark,aa.settlementAdultPrice) adultPrice,"
									+" concat(c.currency_mark,aa.settlementcChildPrice) childPrice,"
									+" concat(c.currency_mark,aa.settlementSpecialPrice) specialPrice,"
									+" aa.remark remark,"
									+" concat(c.currency_mark,aa.taxamt) taxamt,"
									+" aa.currency_id currencyId,"
									+" ai.id agentId,"
									+" au.name activityCreateName,"
									+" ai.agentName agentName,"
									+" ai.agentContact agentContact,"
									+" ai.agentAddress agentAddress,"
									+" ai.agentFax agentFax,"
									+" ai.agentQQ agentQQ,"
									+" ai.agentEmail agentEmail,"
									+" ai.remarks agentRemarks,"
									+" ai.agentTel agentTel,"
									+" aa.freePosition freePosition,"
									+" aa.createDate productCreateDate,"
									+" ao.other_fee otherFee,"
									+" maoouter.moneyStr totalMoney,"
									+" maoouter2.moneyStr2 accountedMoney,"
									+" ao.main_order_id mainOrderId,"
									+" agr.groupCode activityGroupCode,"
									+" aa.departureCity departureCity,"
									+" aa.arrivedCity arrivedCity,"
									+" aa.reservationsNum reservationsNum"
								+" FROM"
									+" airticket_order ao"
								+" LEFT JOIN sys_user su ON ao.create_by = su.id"
								+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
								+" LEFT JOIN sys_user au ON aa.createby = au.id"
								+" LEFT JOIN activitygroup agr ON ao.activitygroup_id = agr.id"
								+" LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id"
								+" LEFT JOIN currency c ON aa.currency_id = c.currency_id "
								
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1"
											+" group by mao.serialNum " 
											+" ) maoouter ON maoouter.serialNum = ao.total_money"
											
								+" LEFT JOIN ("
											+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr2"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
											+" where mao.moneyType = 4 and mao.orderType =7 and mao.businessType = 1"
											+" group by mao.serialNum " 
											+" ) maoouter2 ON maoouter2.serialNum = ao.accounted_money"
								+" WHERE ao.id = ?";
		
		

		

		
		List<Map<String,Object>> orderList = findBySql(orderBaseInfoSql,Map.class, Long.parseLong(orderId));
		
		if(orderList==null || orderList.size()<=0) return new HashMap<String,Object>();
		
		Map<String,Object> orderInfoMap = orderList.get(0);//订单信息
		
		//处理订单中的航段信息
		handleAirticketOrderFlightInfo(orderInfoMap);
		
		
		//处理订单中的游客+联运
		//handleTravelerStrategy(orderInfoMap);
		handleTravelerStrategyForOne(orderInfoMap, travelId);
		
		//处理机票订单中的游客信息
		handleAirticketOrderTravel(orderId, orderInfoMap);
		
		//处理机票订单中的支付信息
		handleAirticketOrderPayInfo(orderId, orderInfoMap);
		
		return orderInfoMap;
	}

	
	private void handleAirticketOrderPayInfo(String orderId,Map<String, Object> orderInfoMap) {
		
		//支付信息
		String orderPayInfoSql = "SELECT"
									+" o.payTypeName payTypeName,"
									+" o.payPriceType payPriceType,"
									+" result.moneyStr payPrice,"
									+" o.createDate createDate,"
//									+" d.id docid,"
									+" o.payVoucher payVoucher,"
//									+" d.docName docName,"
//									+" d.docPath docPath"
									+"	o.isAsAccount isAsAccount"
								+" FROM"
									+" orderpay o"
//								+" LEFT JOIN docinfo d ON o.payVoucher = d.id"//这里注释掉了关于docinfo表的关联  具体操作由程序实现
								+" LEFT JOIN (select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,format(ma.amount,2)) separator '+') moneyStr "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.orderType = 7 and ma.businessType = 1 group by ma.serialNum ) result ON result.serialNum = o.moneySerialNum"
								+" WHERE o.orderType = 7 and "
									+" o.orderId = ?";
		
		List<Map<String,Object>> orderPayInfoList = findBySql(orderPayInfoSql,Map.class, Long.parseLong(orderId));//支付信息
		orderInfoMap.put("orderPayInfoList", orderPayInfoList);
	}
	
	//by sy0908
	//获取单办或者参团的订单联系人信息
	private void handleAirticketOrderContact(String orderId ,Map<String,Object> orderInfoMap){
		//订单类型 1-单办 2-参团
		String type = orderInfoMap.get("type").toString();
		//主订单id
		Integer mainOrderId = (Integer) orderInfoMap.get("mainOrderId");
		String contactSql = "select  ai.contactsName agentContact,"
								+"ai.contactsAddress agentAddress,"
								+"ai.contactsFax agentFax,"
								+"ai.contactsQQ agentQQ,"
								+"ai.contactsEmail agentEmail,"
								+"ai.contactsTel agentTel  from	airticket_order ao,ordercontacts ai " ;
	   if("2".equals(type)){
		   contactSql += "where ao.main_order_id = ai.orderId and ai.orderId = ?";
	   }else{
		   contactSql += "where ao.id = ai.orderId AND ai.orderType = 7 and ao.id = ?";
	   }
	   
	   List<Map<String,Object>> contactsLists = findBySql(contactSql,Map.class,StringUtils.equals(type, "2")? mainOrderId : Long.parseLong(orderId));//游客信息
	   
	   orderInfoMap.put("contactsLists", contactsLists);
	   
	}
	
	
	private void handleAirticketOrderTravel(String orderId, Map<String, Object> orderInfoMap) {
		int orderType = 7;
		//订单类型 1-单办 2-参团
		String type = orderInfoMap.get("type").toString();
		//如果是参团的使用orderType=7类型
		if("2".equals(type)){
			orderType=1;
		}
		//游客信息
		String travelBaseInfoSql = "SELECT"
									+" t.id id,"
									+" t.personType personType,"
									+" t.name travelName,"
									+" t.nameSpell travelEName,"
									+" t.sex sex,"
									+" t.nationality nationality,"
									+" t.birthDay birthDay,"
									+" t.telephone telephone,"
									+" t.idCard idCard,"
									+" t.remark remark,"
									+" t.isAirticketFlag isAirticketFlag,"
									+" t.passportCode passportCode,"
									+" t.passportValidity passportValidity,"
									+" t.passportType passportType,"
									+" t.intermodalType intermodalType,"
									+" t.delFlag delFlag,"
									+" t.rebates_moneySerialNum rebatesMoneySerialNum,"
									+" s.id intermodalId,"
									+" s.group_part groupPart,"
									+" s.icurrency icurrency,"
									+" s.price price,"
									+" result1.payPrice payPrice,"
									+" rebate.trvbt travelerRebate,"
									+" t.payPriceSerialNum traPayPrice,"
									+" v.visa_stauts visaStauts"
								+" FROM"
									+" traveler t"
								+" LEFT JOIN visa v ON t.id = v.traveler_id"
								+" LEFT JOIN (select a.id id,a.price_currency icurrency, a.group_part group_part ,CONCAT(b.currency_mark,FORMAT(a.price,2)) price from intermodal_strategy a LEFT JOIN currency b on a.price_currency=b.currency_id) s ON t.intermodalId = s.id"
								+" LEFT JOIN (select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,format(ma.amount,2)) separator '+') payPrice "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 14 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) result1 ON result1.serialNum = t.payPriceSerialNum"
												
								+" LEFT JOIN (SELECT ma.serialNum, CONCAT(c.currency_mark, ma.amount) as trvbt "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 6 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) rebate on rebate.serialNum = t.rebates_moneySerialNum"				
								
								+" WHERE"
												+" t.order_type = "+orderType+" and isAirticketFlag = 1 and "
//												+" t.isAirticketFlag <> '0' and " 
									+" orderId = ?  and t.delFlag in (0,6)";
		
		//游客附件信息
		String travelAttachmentInfoSql = "SELECT"
											+" tf.fileName fileName,"
											+" tf.fileType fileType"
										+" FROM"
											+" travelerfile tf"
										+" WHERE"
											+" tf.srcTravelerId = ?";
		
		//主订单id
		Integer mainOrderId = (Integer) orderInfoMap.get("mainOrderId");
	
		//游客信息，如果是参团的使用主订单id去查询游客
		List<Map<String,Object>> travelInfoList = findBySql(travelBaseInfoSql,Map.class,StringUtils.equals(type, "2")? mainOrderId : Long.parseLong(orderId));//游客信息
		
		for(Map<String,Object> travelMap: travelInfoList){
			
			Integer travelId = (Integer) travelMap.get("id");
			List<Map<String,Object>> travelAttachInfoList = findBySql(travelAttachmentInfoSql,Map.class,travelId);
			travelMap.put("travelAttachInfoList", travelAttachInfoList);
		}
		
		orderInfoMap.put("travelInfoList", travelInfoList);
		
		orderInfoMap.put("travelNum", travelInfoList.size());
	}
	
	/**
	 * 机票返佣（新返佣）
	 * @param orderId
	 * @param orderInfoMap
	 */
	private void handleAirticketOrderTravelNew(String orderId, Map<String, Object> orderInfoMap) {
		int orderType = 7;
		//订单类型 1-单办 2-参团
		String type = orderInfoMap.get("type").toString();
		//如果是参团的使用orderType=7类型
		if("2".equals(type)){
			orderType=1;
		}
		//游客信息
		String travelBaseInfoSql = "SELECT"
									+" t.id id,"
									+" t.personType personType,"
									+" t.name travelName,"
									+" t.nameSpell travelEName,"
									+" t.sex sex,"
									+" t.nationality nationality,"
									+" t.birthDay birthDay,"
									+" t.telephone telephone,"
									+" t.idCard idCard,"
									+" t.remark remark,"
									+" t.isAirticketFlag isAirticketFlag,"
									+" t.passportCode passportCode,"
									+" t.passportValidity passportValidity,"
									+" t.passportType passportType,"
									+" t.intermodalType intermodalType,"
									+" t.delFlag delFlag,"
									+" t.rebates_moneySerialNum rebatesMoneySerialNum,"
									+" s.id intermodalId,"
									+" s.group_part groupPart,"
									+" s.icurrency icurrency,"
									+" s.price price,"
									+" result1.payPrice payPrice,"
									+" rebate.trvbt travelerRebate,"
									+" t.payPriceSerialNum traPayPrice,"
									+" v.visa_stauts visaStauts"
								+" FROM"
									+" traveler t"
								+" LEFT JOIN visa v ON t.id = v.traveler_id"
								+" LEFT JOIN (select a.id id,a.price_currency icurrency, a.group_part group_part ,CONCAT(b.currency_mark,FORMAT(a.price,2)) price from intermodal_strategy a LEFT JOIN currency b on a.price_currency=b.currency_id) s ON t.intermodalId = s.id"
								+" LEFT JOIN (select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,' ', format(ma.amount,2)) separator" +
				" " +
				"'+') payPrice "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 14 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) result1 ON result1.serialNum = t.payPriceSerialNum"
												
								+" LEFT JOIN (SELECT ma.serialNum, CONCAT(c.currency_mark, ma.amount) as trvbt "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 6 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) rebate on rebate.serialNum = t.rebates_moneySerialNum"				
								
								+" WHERE"
												+" t.order_type = "+orderType+" and "
												+" t.isAirticketFlag <> '0' and " 
									+" orderId = ?  and t.delFlag = 0 ";
		
		//游客附件信息
		String travelAttachmentInfoSql = "SELECT"
											+" tf.fileName fileName,"
											+" tf.fileType fileType"
										+" FROM"
											+" travelerfile tf"
										+" WHERE"
											+" tf.srcTravelerId = ?";
		
		//主订单id
		Integer mainOrderId = (Integer) orderInfoMap.get("mainOrderId");
	
		//游客信息，如果是参团的使用主订单id去查询游客
		List<Map<String,Object>> travelInfoList = findBySql(travelBaseInfoSql,Map.class,StringUtils.equals(type, "2")? mainOrderId : Long.parseLong(orderId));//游客信息
		
		for(Map<String,Object> travelMap: travelInfoList){
			
			Integer travelId = (Integer) travelMap.get("id");
			List<Map<String,Object>> travelAttachInfoList = findBySql(travelAttachmentInfoSql,Map.class,travelId);
			travelMap.put("travelAttachInfoList", travelAttachInfoList);
		}
		
		orderInfoMap.put("travelInfoList", travelInfoList);
		
		orderInfoMap.put("travelNum", travelInfoList.size());
	}
	
	/**
	 * 机票返佣（新退款）
	 * @param orderId
	 * @param orderInfoMap
	 */
	private void handleAirticketOrderTravelNewRefund(String orderId, Map<String, Object> orderInfoMap) {
		int orderType = 7;
		//订单类型 1-单办 2-参团
		String type = orderInfoMap.get("type").toString();
		//如果是参团的使用orderType=7类型
		if("2".equals(type)){
			orderType=1;
		}
		//游客信息
		String travelBaseInfoSql = "SELECT"
									+" t.id id,"
									+" t.personType personType,"
									+" t.name travelName,"
									+" t.nameSpell travelEName,"
									+" t.sex sex,"
									+" t.nationality nationality,"
									+" t.birthDay birthDay,"
									+" t.telephone telephone,"
									+" t.idCard idCard,"
									+" t.remark remark,"
									+" t.isAirticketFlag isAirticketFlag,"
									+" t.passportCode passportCode,"
									+" t.passportValidity passportValidity,"
									+" t.passportType passportType,"
									+" t.intermodalType intermodalType,"
									+" t.delFlag delFlag,"
									+" s.id intermodalId,"
									+" s.group_part groupPart,"
									+" s.icurrency icurrency,"
									+" s.price price,"
									+" result1.payPrice payPrice,"
									+" rebate.trvbt travelerRebate,"
									+" t.payPriceSerialNum traPayPrice,"
									+" v.visa_stauts visaStauts"
								+" FROM"
									+" traveler t"
								+" LEFT JOIN visa v ON t.id = v.traveler_id"
								+" LEFT JOIN (select a.id id,a.price_currency icurrency, a.group_part group_part ,CONCAT(b.currency_mark,FORMAT(a.price,2)) price from intermodal_strategy a LEFT JOIN currency b on a.price_currency=b.currency_id) s ON t.intermodalId = s.id"
								+" LEFT JOIN (select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,format(ma.amount,2)) separator '+') payPrice "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 14 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) result1 ON result1.serialNum = t.payPriceSerialNum"
												
								+" LEFT JOIN (SELECT ma.serialNum, CONCAT(c.currency_mark, ma.amount) as trvbt "
												+" from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id "
												+" where ma.moneyType = 6 and ma.orderType = "+orderType+" and ma.businessType = 2 group by ma.serialNum ) rebate on rebate.serialNum = t.rebates_moneySerialNum"				
								
								+" WHERE"
												+" t.order_type = "+orderType+" and "
//												+" t.isAirticketFlag <> '0' and " 
									+" orderId = ?  and t.delFlag in (0,6) ";
		
		//游客附件信息
		String travelAttachmentInfoSql = "SELECT"
											+" tf.fileName fileName,"
											+" tf.fileType fileType"
										+" FROM"
											+" travelerfile tf"
										+" WHERE"
											+" tf.srcTravelerId = ?";
		
		//主订单id
		Integer mainOrderId = (Integer) orderInfoMap.get("mainOrderId");
	
		//游客信息，如果是参团的使用主订单id去查询游客
		List<Map<String,Object>> travelInfoList = findBySql(travelBaseInfoSql,Map.class,StringUtils.equals(type, "2")? mainOrderId : Long.parseLong(orderId));//游客信息
		
		for(Map<String,Object> travelMap: travelInfoList){
			
			Integer travelId = (Integer) travelMap.get("id");
			List<Map<String,Object>> travelAttachInfoList = findBySql(travelAttachmentInfoSql,Map.class,travelId);
			travelMap.put("travelAttachInfoList", travelAttachInfoList);
		}
		
		orderInfoMap.put("travelInfoList", travelInfoList);
		
		orderInfoMap.put("travelNum", travelInfoList.size());
	}
	
	
	
	@SuppressWarnings("all")
	private void handleTravelerStrategy(String orderId,Map<String, Object> orderInfoMap) {
		Integer airticketId = (Integer) orderInfoMap.get("orderId");//产品id
		String sql = "SELECT t.id,t.name,t.intermodalId,t.delFlag,s.price,t.idCard,s.type from (select id,name ,intermodalId,idCard,delFlag from traveler where order_type=7 and orderId = ? and delFlag in(0) and isAirticketFlag <> '0'  )  t LEFT JOIN "+
                        " (select a.id id,CONCAT(b.currency_mark,FORMAT(a.price,2)) price,a.type from intermodal_strategy a LEFT JOIN currency b on a.price_currency=b.currency_id) s on  t.intermodalId = s.id;";
		List<Map<String,Object>> travelerStrategy = findBySql(sql,Map.class,orderId);//游客信息+联运信息  
		orderInfoMap.put("travelerStrategy", travelerStrategy);
	}
	
	@SuppressWarnings("all")
	private void handleTravelerStrategyForOne(Map<String, Object> orderInfoMap,String travelId) {
		Integer airticketId = (Integer) orderInfoMap.get("airticketId");//产品id
		String sql = "SELECT t.id,t.name,t.intermodalId,s.price,s.type,t.idCard from (select id,name ,intermodalId,idCard from traveler where order_type=7 and  id = ? and orderId = ?)  t LEFT JOIN  intermodal_strategy s on  t.intermodalId = s.id";
		List<Map<String,Object>> travelerStrategy = findBySql(sql,Map.class,Long.parseLong(travelId),airticketId);//游客信息+联运信息
		orderInfoMap.put("travelerStrategy", travelerStrategy);
	}
	
	public List<Map<String, Object>> queryAirtickeByProcode(String procode){
		
		String sql = "SELECT currency_id,settlementAdultPrice,id ,airType,departureCity,arrivedCity,freePosition,cancelTimeLimit,depositTime" +
				" from activity_airticket where product_code = ?";
		List<Map<String,Object>> list = findBySql(sql,Map.class,procode);//
		return list;
		
	}
	private void handleAirticketOrderFlightInfo(Map<String, Object> orderInfoMap) {
		
		Integer airticketId = (Integer) orderInfoMap.get("airticketId");//产品id
		
		List<Map<String,Object>> flightInfoList = queryAirticketOrderFlights(airticketId);//航段信息
		
		orderInfoMap.put("flightInfoList", flightInfoList);
	}
	
	/**
	 * 根据产品id查询航班信息
	 */
	@Override
	public List<Map<String,Object>> queryAirticketOrderFlights(Integer airticketId){
		
		//航段信息
		String flightInfoSql = "SELECT"
									+" sai.airport_name startAirportName,"
									+" afi.taxamt taxamt,"
									+" sai1.airport_name endAirportName,"
									+" afi.startTime startTime,"
									+" afi.airlines airlines,"
									+" cast(afi.spaceGrade as signed) spaceGrade,"
									+" afi.arrivalTime arrivalTime,"
									+" afi.number orderNumber,"
									+" afi.airspace airspace,"
									+" afi.ticket_area_type ticketAreaType,"
									+" afi.settlementAdultPrice settlementAdultPrice,"
									+" afi.settlementcChildPrice settlementcChildPrice,"
									+" afi.settlementSpecialPrice settlementSpecialPrice," 
									+" afi.flightNumber flight_number,"
									+" saline.airline_name airlineName,"
									+" afi.flightNumber flightNumber,"
									+" cur.currency_mark currencyMark"
								+" FROM"
								+" activity_flight_info afi"
								+" LEFT JOIN currency cur ON afi.currency_id = cur.currency_id"
								+" LEFT JOIN sys_airport_info sai ON afi.leaveAirport = sai.id"
								+" LEFT JOIN sys_airport_info sai1 ON afi.destinationAirpost = sai1.id"
								+" LEFT JOIN sys_airline_info saline ON afi.airlines = saline.id"
								+" WHERE"
									+" airticketId = ?"
								+" order by afi.number asc";
		List<Map<String,Object>> flightInfoList = findBySql(flightInfoSql,Map.class, airticketId);//航段信息
		
		return flightInfoList;
	}
	
	/**
	 * 查询游客最大id
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderTravelMaxId(OrderTravelAjax orderTravelAjax) {
		
		String airticketOrderTravelSql = "select max(t.id) travelId from traveler t where t.orderId = ?";
		List<Map<String, Object>> travelInfoList = findBySql(airticketOrderTravelSql, Map.class,Long.parseLong(orderTravelAjax.getOrderId()));
		return travelInfoList;
	}
	
	/**
	 * 查询LastInsertId
	 */
	@Override
	public List<Object> queryLastInsertId() {
		
		String queryLastInsertIdSql = "select last_insert_id() lastInsertId";
		List<Object> lastInsertIdList = findBySql(queryLastInsertIdSql);
		return lastInsertIdList;
	}
	
	
	
	/**
	 * 查询游客信息
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderTravel(String orderId) {
		
		String airticketOrderTravelSql = "select t.id travelId,t.name travelName,t.nameSpell nameSpell,t.sex travelSex,t.birthDay travelBirthDay,t.idCard travelIdCard,t.passportCode,t.passportValidity,t.telephone,t.remark from traveler t left join airticket_order ao on ao.id = t.orderId and t.order_type = 7 where ao.order_state != 99 and ao.order_state != 111 and t.orderId = ? and t.delFlag = 0 and t.isAirticketFlag <> 0 ORDER BY ao.create_date ASC";
		List<Map<String, Object>> travelInfoList = findBySql(airticketOrderTravelSql, Map.class,Long.parseLong(orderId));
		
		return travelInfoList;
	}
	
	/**
	 * 保存机票订单中的游客信息
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@Override
	public Integer saveAirticketOrderTravel(OrderTravelAjax orderTravelAjax) throws NumberFormatException, ParseException {
		
			
			String saveAirticketOrderTravelSql = "INSERT INTO traveler (orderId,name,nameSpell,idCard,nationality,sex,birthDay,telephone,remark,personType,passportCode,passportValidity,intermodalType,intermodalId,payPriceSerialNum,order_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			int updateCount = updateBySql(saveAirticketOrderTravelSql,
											Long.parseLong(orderTravelAjax.getOrderId()),
											orderTravelAjax.getTravelName(),
											orderTravelAjax.getTravelNamePinyin(),
											orderTravelAjax.getIdCard(),
											Integer.parseInt(orderTravelAjax.getNationality()),
											Integer.parseInt(orderTravelAjax.getTravelerSex()),
											StringUtils.isEmpty(orderTravelAjax.getBirthDay())?null:new SimpleDateFormat("yyyy-MM-dd").parse(orderTravelAjax.getBirthDay()),
											orderTravelAjax.getTelephone(),
											orderTravelAjax.getRemarks(),
											Integer.parseInt(orderTravelAjax.getPersonType()),
											orderTravelAjax.getPassportCode(),
											StringUtils.isEmpty(orderTravelAjax.getPassportValidity())?null:new SimpleDateFormat("yyyy-MM-dd").parse(orderTravelAjax.getPassportValidity()),
											StringUtils.isEmpty(orderTravelAjax.getIntermodalType())?null:Integer.parseInt(orderTravelAjax.getIntermodalType()),
											StringUtils.isEmpty(orderTravelAjax.getIntermodalId())?null:Integer.parseInt(orderTravelAjax.getIntermodalId()),
											StringUtils.isEmpty(orderTravelAjax.getPayPriceSerialNum())?null:orderTravelAjax.getPayPriceSerialNum(),7);
			
			return updateCount;
			
		
	}
	
	/**
	 * 更新机票订单中的游客信息
	 */
	@Override
	public Integer updateAirticketOrderTravel(OrderTravelAjax orderTravelAjax) {
		
		try {
			
			String updateAirticketOrderTravelSql = "UPDATE traveler set name=?,nameSpell=?,idCard=?,nationality=?,sex=?,birthDay=?,telephone=?,remark=?,personType=?,passportCode=?,passportValidity=?,intermodalType=?,intermodalId=? WHERE id = ? and orderId = ?";
			
			int updateCount = updateBySql(updateAirticketOrderTravelSql, 
					orderTravelAjax.getTravelName(),
					orderTravelAjax.getTravelNamePinyin(),
					orderTravelAjax.getIdCard(),
					Integer.parseInt(orderTravelAjax.getNationality()),
					Integer.parseInt(orderTravelAjax.getTravelerSex()),
					StringUtils.isEmpty(orderTravelAjax.getBirthDay())?null:(new SimpleDateFormat("yyyy-MM-dd").parse(orderTravelAjax.getBirthDay())),
					orderTravelAjax.getTelephone(),
					orderTravelAjax.getRemarks(),
					Integer.parseInt(orderTravelAjax.getPersonType()),
					orderTravelAjax.getPassportCode(),
					StringUtils.isEmpty(orderTravelAjax.getPassportValidity())?null:(new SimpleDateFormat("yyyy-MM-dd").parse(orderTravelAjax.getPassportValidity())),
					StringUtils.isEmpty(orderTravelAjax.getIntermodalType())?null:Integer.parseInt(orderTravelAjax.getIntermodalType()),
					StringUtils.isEmpty(orderTravelAjax.getIntermodalId())?null:Integer.parseInt(orderTravelAjax.getIntermodalId()),
					Long.parseLong(orderTravelAjax.getTravelId()),
					Long.parseLong(orderTravelAjax.getOrderId()));
			
			return updateCount;
			
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询机票订单中的渠道信息
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderAgent(String agentId) {
		String queryAgentInfoSql = "select a.agentName,a.agentAddress,a.agentContact,a.agentTel,a.agentFax,a.agentEmail,a.agentQQ from agentinfo a where a.id = ?";
		List<Map<String,Object>> agentInfoList = findBySql(queryAgentInfoSql, Map.class, Long.parseLong(agentId));
		return agentInfoList;
	}
	
	/**
	 * 更新机票订单中航空备注
	 */
	@Override
	public boolean updateAirticketOrderFlightRemark(OrderAgentAjax orderAgentAjax) {
		String updateFlightRemarkSql = "update activity_airticket aa set aa.remark = ? where aa.id = ?";
		int count = updateBySql(updateFlightRemarkSql,StringUtils.isEmpty(orderAgentAjax.getFlightRemark())?null:orderAgentAjax.getFlightRemark(),orderAgentAjax.getAirticketId());
		return count==1?true:false;
	}
	
	/**
	 * 更新机票订单中的渠道id
	 */
	@Override
	public boolean updateAirticketOrderAgentId(OrderAgentAjax orderAgentAjax) {
		String updateAirticketAgentIdSql = "update airticket_order ao set ao.agentinfo_id=? where ao.id = ?";
		Long agentId = (orderAgentAjax.getAgentId() == null || "".equals(orderAgentAjax.getAgentId())) ? null : Long.parseLong(orderAgentAjax.getAgentId());
		if(agentId == null){
			return true;
		}
		int count = updateBySql(updateAirticketAgentIdSql, agentId,Long.parseLong(orderAgentAjax.getOrderId()));
		return count==1?true:false;
	}
	
	/**
	 * 更新机票订单中的渠道信息
	 */
	@Override
	public boolean updateAirticketOrderAgentInfo(OrderAgentAjax orderAgentAjax) {
		if(orderAgentAjax.getAgentId() == null || "".equals(orderAgentAjax.getAgentId())){
			return true;
		}
		String updateAgentInfoSql = "update agentinfo a set a.agentAddress = ?,a.agentContact = ?,a.agentTel = ?,a.agentFax = ?,a.agentEmail = ?,a.agentQQ = ?,a.remarks = ? where a.id = ? ";
		int count = updateBySql(updateAgentInfoSql, 
								StringUtils.isEmpty(orderAgentAjax.getAgentAddress())?null:orderAgentAjax.getAgentAddress(),
								StringUtils.isEmpty(orderAgentAjax.getAgentContact())?null:orderAgentAjax.getAgentContact(),
								StringUtils.isEmpty(orderAgentAjax.getAgentTel())?null:orderAgentAjax.getAgentTel(),
								StringUtils.isEmpty(orderAgentAjax.getAgentFax())?null:orderAgentAjax.getAgentFax(),
								StringUtils.isEmpty(orderAgentAjax.getAgentEmail())?null:orderAgentAjax.getAgentEmail(),
								StringUtils.isEmpty(orderAgentAjax.getAgentQQ())?null:orderAgentAjax.getAgentQQ(),
								StringUtils.isEmpty(orderAgentAjax.getAgentRemarks())?null:orderAgentAjax.getAgentRemarks(),
								Long.parseLong(orderAgentAjax.getAgentId()));
		return count==1?true:false;
	}
	
	/**
	 * 根据产品id查询所有机票订单
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrdersByProductId(String productId) {
		
		String queryOrdersByProductSql = "SELECT"
											+" ao.id orderId,"
											+" agent.agentName agentName,"											
											+" ao.order_no orderNo,"
											+" ao.activationDate,"
											+" ao.occupyType,"
											+" ao.create_date createDate,"
											+" ao.remaind_days remaindDays,"
											+" ao.person_num personNum,"
											+" ao.order_state,"
											+" ao.comments comments,"
											+" su.name orderUserName,"
											+" aa.airType airType,"
											+" ag.groupOpenDate groupOpenDate,"
											+" ag.groupCloseDate groupCloseDate,"
											//优化sql，yudong.xu
											+"( SELECT GROUP_CONCAT( CONCAT( c.currency_mark, ' ', mao.amount ) ORDER BY mao.currencyId SEPARATOR '+' )"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id "
											+" WHERE mao.moneyType = 13 AND mao.orderType =7 AND mao.businessType = 1 "
											+" AND mao.serialNum = ao.total_money GROUP BY mao.serialNum ) AS totalMoney,"
											//
											+"( SELECT GROUP_CONCAT( CONCAT( c.currency_mark, ' ', mao.amount ) ORDER BY mao.currencyId SEPARATOR '+' )"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id "
											+" WHERE mao.moneyType = 5 AND mao.orderType =7 AND mao.businessType = 1 "
											+" AND mao.serialNum = ao.payed_money GROUP BY mao.serialNum ) AS payedMoney,"

											+"( SELECT GROUP_CONCAT( CONCAT( c.currency_mark, ' ', mao.amount ) ORDER BY mao.currencyId SEPARATOR '+' )"
											+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id "
											+" WHERE mao.moneyType = 4 AND mao.orderType =7 AND mao.businessType = 1 "
											+" AND mao.serialNum = ao.accounted_money GROUP BY mao.serialNum ) AS accountedMoney,"
											//查询转换成人民币的应收金额
											+"( SELECT FORMAT( SUM( mao.amount * IFNULL(mao.exchangerate, 0)), 2 ) FROM money_amount mao "
											+" WHERE mao.moneyType = 13 AND mao.orderType =7 AND mao.businessType = 1 "
											+" AND mao.serialNum = ao.total_money ) AS totalMoneyRMB "
										+" FROM"
											+" airticket_order ao"
										+" LEFT JOIN sys_user su ON ao.create_by = su.id"
										+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
										+" LEFT JOIN activitygroup ag ON ao.activitygroup_id = ag.id"
										+" LEFT JOIN agentinfo agent ON ao.agentinfo_id = agent.id "
										+" WHERE"
											+" ao.airticket_id = ? and ao.order_state<>7 and ao.order_state<>99 and ao.order_state<>111 and ao.del_flag='0' ";
		List<Map<String,Object>> airticketOrders = findBySql(queryOrdersByProductSql, Map.class, Long.parseLong(productId));
	
		
		return airticketOrders;
	}	
	

	
	@Override
	public List<Map<String, Object>> findSoldNopayPosition(String productId) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		String queryOrdersByProductSql = "SELECT agent.agentName,  agent.agentName as orderCompany, ro.payReservePosition,ro.reservation as createUserName"
				                        +"ro.leftpayReservePosition,u.name as orderPersonName, ao.order_no as orderNum,"
			                        	+"ao.total_money as totalMoney,ao.payed_money as payMoney,ao.person_num as orderPersonNum,"
				                        + "ao.order_state as payStatus,ro.remark "	
			                        	
										+" FROM activityreserveorder  ro  "
										+" LEFT JOIN airticket_order ao on ro.srcActivityId = ao.airticket_id "
										+" LEFT JOIN sys_user su ON ao.create_by = su.id"
										+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
										+" LEFT JOIN activitygroup ag ON ao.activitygroup_id = ag.id"
										+" LEFT JOIN agentinfo agent ON ao.agentinfo_id = agent.id"
										+" LEFT JOIN ("
										+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
										+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
										+" where mao.moneyType = 13 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId
										+" group by mao.serialNum " 
										+" ) maoouter ON maoouter.serialNum = ao.total_money"
										+" LEFT JOIN ("
										+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr1"
										+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
										+" where mao.moneyType = 5 and mao.orderType =7 and mao.businessType = 1 and c.create_company_id = " + companyId
										+" group by mao.serialNum " 
										+" ) maoouter1 ON maoouter1.serialNum = ao.payed_money"		
							         
										+" WHERE"
											+" ro.srcActivityId = ? and ro.reserveType=1 ";
		List<Map<String,Object>> airticketOrders = findBySql(queryOrdersByProductSql, Map.class, Long.parseLong(productId));
		return airticketOrders;
	}
	
	/**
	 * 根据ID查询机票订单
	 * @param id
	 * @return
	 */
	public AirticketOrder getAirticketOrderById(Long id){
		return (AirticketOrder) getSession().get("com.trekiz.admin.modules.airticketorder.entity.AirticketOrder", id);
	}
	
	/**
	 * 保存或更新机票订单
	 * @param id
	 * @return
	 */
	public void saveOrUpdateAirticketOrderById(AirticketOrder airticketOrder){
		getSession().saveOrUpdate(airticketOrder);
	}
	
	/**
	 * 保存订单附件和订单的关系
	 */
	@Override
	public boolean saveDocinfoAndOrderRelation(Long orderId,Long docInfoId) {
		String docinfoAndOrderRelationSql = "insert into airticket_order_file (airticket_order_id,docInfo_id,create_date) values (?,?,sysdate())";
		int updateCount = updateBySql(docinfoAndOrderRelationSql, orderId, docInfoId);
		return updateCount == 0?false:true;
	}
	
	/**
	 * 根据订单id查询附件
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderAttachment(Integer airticketId) {
		String orderAttachmentSql = "SELECT d.id attachmentId,d.docName,d.docPath from airticket_order_file aof LEFT JOIN docinfo d ON aof.docInfo_id = d.id WHERE aof.airticket_order_id = ? order by attachmentId desc";
		List<Map<String, Object>> orderAttachmentList = findBySql(orderAttachmentSql, Map.class, airticketId);
		return orderAttachmentList;
	}
	
	
	public List<Map<String, Object>> queryGaiQianRoles(Long companyId) {
		
		String orderAttachmentSql = "SELECT r.roleId FROM review_company c , review_flow f,review_role_level r WHERE" +
				" c.companyId=? and r.cpid=c.id AND f.flowType=14 and f.productType=7 and f.id=c.reviewId";
		return findBySql(orderAttachmentSql, Map.class, companyId);
	}
	
	
	
	public Page<Map<String, Object>> queryAirtickeToDo(HttpServletRequest request,HttpServletResponse response,@SuppressWarnings("rawtypes") Map condition){
		//by sy  20150730   去掉,t1.nowLevel  t1n,t1.zt tzz
		String sql =" SELECT t1.*,o.group_code,o.create_by AS picker, o.salerId AS saler,o.order_no orderNo,t2.name,t2.moneyStr,a.createBy as jdCreateBy,o.create_date from " +
				"airticket_order o, (" ;
				//待审核
		String sqlcon="";
				if(condition.get("status").equals("todo")){
					sqlcon+="SELECT r.*,'待审核' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id and r.nowLevel=temp.reviewLevel  and r.status=1";
				}
				
				if(condition.get("status").equals("todoing")){
					sqlcon+="SELECT r.*,'审核中' as 'zt',r.nowLevel nl  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id  and r.status=1";  /*and r.nowLevel=temp.reviewLevel */
				}
				
				if(condition.get("status").equals("cancel")){
					sqlcon+="SELECT r.*,'已取消' as 'zt'  from review r,("+
							" select reviewLevel,review_company_id as id from review_role_level WHERE"+
							" delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							" where r.active='1' and temp.id = r.review_company_id  and r.status=4";  /*and r.nowLevel=temp.reviewLevel */
				}
				
				//已经通过
				if(condition.get("status").equals("pass")){
					  //中间节点
//					sqlcon+="SELECT r.*,'已通过' as 'zt'  from review r,("+
//							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
//							 " delFlag='0' and sys_job_id= '"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
//							 " where r.active='1' and temp.id = r.review_company_id and r.nowLevel>temp.reviewLevel";
//					
//					//最后一个节点
//					sqlcon +=" union all ";
					sqlcon+="SELECT r.*,'已通过' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id and r.status=2";/*and r.nowLevel=temp.reviewLevel*/
				}

				//未通过
				if(condition.get("status").equals("reject")){
					sqlcon+="SELECT r.*,'未通过' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id  and r.status=0";/*and r.nowLevel=temp.reviewLevel*/
					
				}
//				//全部  	//已通过status  0: 已驳回 (审核失败);1: 待审核;2: 审核成功;3: 操作完成
//				if(condition.get("status").equals("all")){
//					sqlcon+="SELECT r.*,'待审核' as 'zt'  from review r,("+
//							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
//							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
//							 " where r.active='1' and temp.id = r.review_company_id and r.status=1";/*and r.nowLevel=temp.reviewLevel  */
//					sqlcon +=" union all ";
//					sqlcon+="SELECT r.*,'已通过' as 'zt'  from review r,("+
//							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
//							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
//							 " where r.active='1' and temp.id = r.review_company_id  ";/*and r.nowLevel>temp.reviewLevel*/
//					
//					
//					//最后一个节点
//					sqlcon +=" union all ";
//					sqlcon+="SELECT r.*,'已通过' as 'zt'  from review r,("+
//							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
//							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
//							 " where r.active='1' and temp.id = r.review_company_id  and r.status=2";/*and r.nowLevel=temp.reviewLevel*/
//				
//					//未通过
//					sqlcon +=" union all ";
//					sqlcon+="SELECT r.*,'未通过' as 'zt'  from review r,("+
//							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
//							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
//							 " where r.active='1' and  temp.id = r.review_company_id  and r.status=0";/*and r.nowLevel=temp.reviewLevel*/
//				}
//			  
//			   
				if(condition.get("status").equals("all")){
					
					sqlcon+="SELECT r.*,'已通过' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id and r.status=2";/*and r.nowLevel=temp.reviewLevel*/
				
					//未通过
					sqlcon +=" union all ";
					sqlcon+="SELECT r.*,'未通过' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and  temp.id = r.review_company_id  and r.status=0";/*and r.nowLevel=temp.reviewLevel*/
					sqlcon +=" union all ";
					
					sqlcon+="SELECT r.*,'审核中' as 'zt'  from review r,("+
							 " select reviewLevel,review_company_id as id from review_role_level WHERE"+
							 " delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							 " where r.active='1' and temp.id = r.review_company_id  and r.status=1";  /*and r.nowLevel=temp.reviewLevel */
					sqlcon +=" union all ";
					
					sqlcon+="SELECT r.*,'已取消' as 'zt'  from review r,("+
							" select reviewLevel,review_company_id as id from review_role_level WHERE"+
							" delFlag='0' and sys_job_id='"+condition.get("userJob")+"'  and review_company_id = "+condition.get("rcid")+" )temp"+
							" where r.active='1' and temp.id = r.review_company_id  and r.status=4";  /*and r.nowLevel=temp.reviewLevel */
					
				}
				
			   
				sql =sql+sqlcon;
			    sql+=")t1, activity_airticket a , "	;	   
				sql+="(SELECT tr.id,tr.name ,mm.moneyStr from traveler tr,( ";	   
				sql +="select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,ma.amount) separator '+') moneyStr ";	  
				sql+="from money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id group by ma.serialNum)mm WHERE mm.serialNum=tr.payPriceSerialNum"
						+ ")t2 ";	   
				sql+="where t1.orderid=o.id  AND o.airticket_id=a.id AND t2.id=t1.travelerId  "	 ;  
		
				
				
				
		if(condition.get("saler")!=null&&!condition.get("saler").equals("")){
			sql +=" and o.salerId="+condition.get("saler");//销售
		}
		if(condition.get("picker")!=null&&!condition.get("picker").equals("")){
			sql +=" and o.create_by="+condition.get("picker");//下单人
		}
		if(condition.get("jd")!=null&&!condition.get("jd").equals("")){
			sql +=" AND a.createBy="+ condition.get("jd");//计调
		}
		if(condition.get("agentId")!=null&&!condition.get("agentId").equals("")){
			sql +=" and  o.agentinfo_id="+condition.get("agentId");//渠道
		}
		if(condition.get("wholeSalerKey")!=null&&!condition.get("wholeSalerKey").equals("")){
			sql +=" and o.group_code like'%"+ condition.get("wholeSalerKey")+"%'";//团号
		}
		if(condition.get("start")!=null&&!condition.get("start").equals("")){
			sql += " and o.create_date >='"+condition.get("start")+"'";	
		}
		if(condition.get("end")!=null&&!condition.get("end").equals("")){
			sql += " and o.create_date <='"+condition.get("end")+"'";	
		}
		
		if(condition.get("orderCreateDateSort")!=null && !("").equals(condition.get("orderCreateDateSort"))){
			sql+= "ORDER BY t1.createDate "+condition.get("orderCreateDateSort");
			
		}
		if(condition.get("orderUpdateDateSort")!=null && !("").equals(condition.get("orderUpdateDateSort"))){
			sql+= "ORDER BY t1.updateDate "+condition.get("orderUpdateDateSort");
			
		}
		Page<Map<String, Object>>  page=null;
		page = super.findBySql(new Page<Map<String,Object>>(request, response),sql,Map.class);
		return page;
	}
			
			
			
	public Review queryOneRview(Long id){
		return (Review) getSession().get("com.trekiz.admin.modules.reviewflow.entity.Review", id);
	}		
			
	public ActivityAirTicket queryOneActivityAirTicket(Long id){
		return (ActivityAirTicket) getSession().get("com.trekiz.admin.modules.airticket.entity.ActivityAirTicket", id);
	}		
	
	public Traveler queryoneTravler(Long id){
		return (Traveler)getSession().get("com.trekiz.admin.modules.traveler.entity.Traveler", id);
	}
	
	public AirticketOrder queryoneAirticketOrder(Long id){
		//return (AirticketOrder)getSession().get("AirticketOrder", id);
		return (AirticketOrder) getSession().get("com.trekiz.admin.modules.airticketorder.entity.AirticketOrder", id);
	}
	
	public Agentinfo queryAgentinfo(Long id){
		//return (AirticketOrder)getSession().get("AirticketOrder", id);
		return  (Agentinfo)getSession().get("com.trekiz.admin.modules.agent.entity.Agentinfo", id);
	}
	
	
	@SuppressWarnings("rawtypes")
    public Page<Map<String, Object>> airticketApprovalHistoryList(HttpServletRequest request,HttpServletResponse response,Map condition){
		
		
//		String sql ="select t1.*,t2.* from("+
//"select t.*,city.name as arrivedCityName from("+
//"SELECT r.*,p.arrivedCity,city.name as departureCity,p.id AS airticketId from review r ,review_detail d ,activity_airticket p,"+
//"(select area.id , area.name from sys_area area,sys_airport_info air where area.id = air.city_Id and area.type ='4'  group by area.id) city"+ 
//"where  r.orderId=?" +
//"r.productType=7 AND r.flowType=14 and d.rid=r.id and d.myValue=p.id and city.id = p.departureCity)t,"+
//"(select area.id,area.name from sys_area area,sys_airport_info air where area.id = air.city_Id and area.type ='4'  group by area.id)city"+
//"where t.arrivedCity=city.id  )t1 ,"+
//"(select a.startTime,b.airticketId,b.arrivalTime from"+ 
//"(SELECT m.startTime,m.airticketId from activity_flight_info m WHERE m.id IN( "+
//"SELECT MIN(i.id) from activity_airticket a,activity_flight_info i  where a.id=i.airticketId GROUP BY i.airticketId  ))a,"+
//"(SELECT m.arrivalTime,m.airticketId from activity_flight_info m WHERE m.id IN("+ 
//"SELECT max(i.id) from activity_airticket a,activity_flight_info i  where a.id=i.airticketId GROUP BY i.airticketId  ) ) b  where a.airticketId = b.airticketId"+ 
//"GROUP BY a.airticketId)t2 where t1.airticketId=t2.airticketId";
		
		
//		String sql ="SELECT t1.* ,t2.startTime,t2.arrivalTime from "+
//" (SELECT r.*,p.arrivedCity,p.departureCity,p.id AS airticketId from review r ,review_detail d ,activity_airticket p"+
//" where  r.orderId =" +condition.get("orderId")+" and r.productType=7 AND r.flowType=14 and d.review_id=r.id and d.myValue=p.id)t1, "+
//" (select a.startTime,b.airticketId,b.arrivalTime from"+ 
//" (SELECT m.startTime,m.airticketId from activity_flight_info m WHERE m.id IN("+ 
//" SELECT MIN(i.id) from activity_airticket a,activity_flight_info i  where a.id=i.airticketId GROUP BY i.airticketId  ))a,"+
//" (SELECT m.arrivalTime,m.airticketId from activity_flight_info m WHERE m.id IN("+ 
//" SELECT max(i.id) from activity_airticket a,activity_flight_info i  where a.id=i.airticketId GROUP BY i.airticketId  ) ) b  where a.airticketId = b.airticketId"+ 
//" GROUP BY a.airticketId)t2 WHERE t1.airticketId=t2.airticketId";
		
		//by sy 20150915
		
		String sql = "SELECT t1.*,  "+
      "  t2.startTime, "+
     "  t2.arrivalTime"+
" FROM   (SELECT r.*, "+
             "  p.arrivedCity,"+
               " p.departureCity,"+
              " p.id AS airticketId"+
      "  FROM   review r,"+
               "review_detail d,"+
             "  activity_airticket p "+
      "  WHERE  r.orderId = "+condition.get("orderId")+""+
             "  AND r.productType = 7"+
             "  AND r.flowType = 14"+
              " AND d.review_id = r.id"+
               " AND d.myValue = p.id) t1"+
" LEFT JOIN "+
       "(SELECT a.startTime,"+
             "  a.airticketId,"+
             "  a.arrivalTime "+
     "   FROM   (SELECT m.startTime,"+
                     "  m.airticketId,"+
                      " m.arrivalTime"+
                " FROM   activity_flight_info m "+
               " WHERE  m.id IN (SELECT MIN(i.id)"+
                               " FROM   activity_flight_info i"+
                               " GROUP  BY i.airticketId) "+
                      " AND m.id IN("+
				" SELECT MAX(i.id) "+
                              "  FROM   activity_flight_info i "+
                             "   GROUP  BY i.airticketId"+
                      " )) a"+
       " GROUP  BY a.airticketId) t2 "+
" ON  t1.airticketId = t2.airticketId";
		
		
		Page<Map<String, Object>> page = super.findBySql(new Page<Map<String,Object>>(request, response),sql,Map.class);
		return page;
	}
			
	public void updateairticketorder(String sql){
		
		super.updateBySql(sql);
	}
	
	public Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId){
		
		String sql ="SELECT r.id,t.`name`,r.orderId,r.createReason,r.travelerId,o.total_money ," +
				"(SELECT GROUP_CONCAT(CONCAT(c.currency_name) SEPARATOR '+') bz FROM money_amount ma " +
				"LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE ma.serialNum = t.payPriceSerialNum GROUP BY ma.serialNum) bz," +
				"(SELECT GROUP_CONCAT(CONCAT(ma.amount) SEPARATOR '+') je FROM money_amount ma " +
				"LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE ma.serialNum = t.payPriceSerialNum GROUP BY ma.serialNum) je," +
				"o.create_date" +
				" FROM review r ,airticket_order o,traveler t " +
				" WHERE t.orderId=r.orderId and t.id=r.travelerId" +
				" AND o.id = r.orderId AND r.id = " + reviewId;
		
		Page<Map<String, Object>> page = super.findBySql(new Page<Map<String,Object>>(request, response),sql,Map.class);
		return page.getList().get(0);
		
	}
	
	/**
	 * 保存备注信息
	 */
	public boolean updateRemark(Long id, String remark) {
		String sql = "update airticket_order ao set ao.comments=? where ao.id = ?";
		int count = updateBySql(sql, remark, id);
		return count == 1 ? true : false;
	}
	@Override
	public List<Map<String, Object>> areaGaiQianCheck(@SuppressWarnings("rawtypes") Map map) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT travelerId from review where flowType=14 and productType=7 and companyId="+UserUtils.getUser().getCompany().getId()
				+" and orderId="+map.get("orderId")+" and status=1 and travelerId in ("+map.get("ids")+")";
		List<Map<String, Object>> list = findBySql(sql, Map.class);
		if(list.size()==0){
			return null;
		}else{
			return list;
		}
	}
	
	/**
	 * 查询所有订单列表
	 */
	@Override
	public List<AirticketOrder> allAirticketOrderList() {
		String sql = "select * from airticket_order where lockStatus = '0' and paymentStatus is NULL or paymentStatus='0' and occupyType in('2','3')";
	    List<AirticketOrder> orderList = findBySql(sql,AirticketOrder.class);
		return orderList;
	}
	
	/**
	 * 把机票返佣 总额
	 * @author jiangyang
	 * @param orderId
	 * @return 机票订单对应的返佣信息，包括个人返佣和团队返佣
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderRebates(String orderNo) {
		String orderRebatesSql = "" +
		" select GROUP_CONCAT(yy.money separator '+') as prebt" +
		" from (" +
		" select ma.currencyId, CONCAT(c.currency_mark, ' ', sum(ma.amount)) as money, r.`status`, serial.odno as orderNo " +
		" from money_amount ma " +
		" left join review_new r on ma.reviewId = r.id" +
		" left join currency c on c.currency_id = ma.currencyId  " +
		" inner join (" +
		" select ao.order_no as odno, t.rebates_moneySerialNum as sr " +
		" from airticket_order ao left join traveler t on t.orderId = ao.id where t.order_type = '7' and delFlag = '0' and ao.order_no = '" + orderNo + "' " +
		" UNION " +
		" select ao.order_no as odno, ao.schedule_back_uuid as sr " +
		" from airticket_order ao where ao.order_no = '" + orderNo + "' " +
		" ) serial on serial.sr = ma.serialNum 	" +		
		" where ma.amount != 0.00 and serial.odno='" +
		orderNo + 
		" '" +
		" group by r.`status`, ma.currencyId " +
		" ) yy " +
		" where yy.`status` <> 2 and yy.`status` <> 3 or yy.`status` is NULL " +
		" group by yy.status";
		
		List<Map<String, Object>> orderRebatesList = findBySql(orderRebatesSql, Map.class);
		return orderRebatesList;
	}
	
	/**
	 * 机票实际返佣 总额
	 * @author jiangyang
	 * @param orderId
	 * @return 机票订单对应的返佣信息，包括个人返佣和团队返佣
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderRebatesInf(String orderNo) {
		
		String orderRebatesSql = "" +
		
		" select GROUP_CONCAT(inf.money SEPARATOR '+') as infbt" +
		" from(" +
		" select orb.currencyId,CONCAT(c.currency_mark,' ', sum(orb.rebatesDiff)) as money, r.`status`, ao.order_no from airticket_order " +
				"ao" +
		" left join order_rebates orb on ao.id = orb.orderId" +
		" left join review r on orb.rid =  r.id" +
		" left join currency c on c.currency_id = orb.currencyId" +
		" where ao.order_no = '" + 
		orderNo +
		" ' and (r.`status` = 2 or r.`status` = 3) and orb.delFlag = 0 and orb.order_type = '7'" +
		" group by orb.currencyId" +
		" ) inf";				
		
		List<Map<String, Object>> orderRebatesList = findBySql(orderRebatesSql, Map.class);
		return orderRebatesList;
	}

	/**
	 * 机票实际返佣 总额
	 * @author jiangyang
	 * @return 机票订单对应的返佣信息，包括个人返佣和团队返佣
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderNewRebatesInf(String orderNo) {

		String orderRebatesSql = "" +

				" select GROUP_CONCAT(inf.money SEPARATOR '+') as infbt" +
				" from(" +
				" select orb.currencyId,CONCAT(c.currency_mark,' ',sum(orb.rebatesDiff)) as money, r.`status`, ao.order_no from " +
				"airticket_order ao" +
				" left join rebates orb on ao.id = orb.orderId" +
				" left join review_new r on orb.rid =  r.id" +
				" left join currency c on c.currency_id = orb.currencyId" +
				" where ao.order_no = '" +
				orderNo +
				" ' and (r.`status` = 2 ) and orb.delFlag = 0 and orb.orderType = '7'" +
				" group by orb.currencyId" +
				" ) inf";

		List<Map<String, Object>> orderRebatesList = findBySql(orderRebatesSql, Map.class);
		return orderRebatesList;
	}
	
	/**
	 * 根据订单id获取收入单
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-11 上午11:28:22
	 */
	public Map<String, Object> findMeituIncomeInfoByOrderId(Long orderId) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT airticket.startingDate,airticket.journey,airticket.group_code as airGroupCode,air_order.agentinfo_id AS agentinfoId,air_order.nagentName,air_order.person_num AS personNum,air_order.group_code AS orderGroupCode FROM airticket_order air_order ");
		sb.append("LEFT JOIN activity_airticket airticket ON airticket.id = air_order.airticket_id WHERE air_order.id=? ");
		
		List<Map<String, Object>> orderRebatesList = findBySql(sb.toString(), Map.class, orderId);
		if(CollectionUtils.isEmpty(orderRebatesList)) {
			return null;
		}
		return orderRebatesList.get(0);
	}
	
	/**
	 * 根据创建人获取该创建人下的所有订单
	 * @Description: 
	 * @param @param createBys
	 * @param @return   
	 * @return List<AirticketOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<AirticketOrder> getAirticketOrderByCreateBys(List<Long> createBys) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from airticket_order where del_flag=? ");
		if(!CollectionUtils.isEmpty(createBys)) {
			sql.append(" and create_by in (");
			for(Long createBy : createBys) {
				sql.append(createBy);
				sql.append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
		}
	    List<AirticketOrder> orderList = findBySql(sql.toString(),AirticketOrder.class, BaseEntity.DEL_FLAG_NORMAL);
		return orderList;
	}

	@Override
	public List<AirticketOrder> findByAirticketActivityCode(Long activityAirticketId) {
		String sql = "select * from airticket_order where airticket_id = ? and del_flag = '0'";
		return findBySql(sql.toString(),AirticketOrder.class, activityAirticketId);
	}
	
	/*
	 * 根据产品id获取该产品的所有订单id和订单No
	 * @param activityAirticketId  机票产品id
	 * @return 订单id和No的Map 列表
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> findOrderIdAndNoByActivityId(Long activityAirticketId) {
		String sql = "select ao.id as orderId, ao.order_no as orderNo from airticket_order ao where ao.order_state<>7 " +
				"and ao.order_state<>99 and ao.order_state<>111 and ao.del_flag='0' and ao.airticket_id =" + activityAirticketId;
		List<Map<String, Object>> list = findBySql(sql, Map.class);
		return list;
	}
}
