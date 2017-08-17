package com.trekiz.admin.modules.stock.repository;

import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class StockDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IStockDao {
	
	/*散拼售出切位明细*/
	@Override
	public List<Map<String, Object>> findSoldNopayPosition(String productId) {
		       String queryOrdersByProductSql = "SELECT agent.agentName,t1.agentId,t1.payReservePosition,t1.leftpayReservePosition,"
		   		+" po.orderNum as orderNo, po.orderPersonNum as personNum, po.payStatus as pay,t1.remark,po.orderCompany, "							
				+" maoouter.moneyStr totalMoney,"
				+" maoouter1.moneyStr1 payMoney,"						
				+" su.name createUserName,"	
				+" po.specialDemand orderRemark,"
				+" po.salerName "
			  +" FROM"
				+" activitygroupreserve t1"
				+" JOIN productorder po ON t1.activityGroupId = po.productGroupId and t1.agentId = po.orderCompany "
			+" LEFT JOIN sys_user su ON po.createBy = su.id"		
			+" LEFT JOIN agentinfo agent ON t1.agentId = agent.id"			
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.moneyType = 13 and  mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter ON maoouter.serialNum = po.total_money"
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr1"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.moneyType = 5  and mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter1 ON maoouter1.serialNum = po.payed_money"      
			+" WHERE"
				+" t1.activityGroupId = ? and (po.payStatus = 4 or po.payStatus = 5) and po.placeHolderType =1 order by t1.agentId";
           List<Map<String,Object>> stockOrders = findBySql(queryOrdersByProductSql, Map.class, Long.parseLong(productId));
          return stockOrders;

	}
	
	/*机票搜出切位明细*/
	@Override
	public List<Map<String, Object>> findAirSoldNopayPosition(String productId) {
		       String queryOrdersByProductSql = "SELECT agent.agentName,t1.agentId,t1.payReservePosition,t1.leftpayReservePosition,"
		   		+" po.order_no as orderNo, po.person_num as personNum, po.order_state as pay,t1.remark, "							
				+" maoouter.moneyStr totalMoney,"
				+" maoouter1.moneyStr1 payMoney,"						
				+" su.name createUserName,"	
				+" po.comments orderRemark,"
				+" po.salerName "
			  +" FROM"
				+" airticketactivityreserve t1"
				+" JOIN airticket_order po ON t1.activityId = po.airticket_id and t1.agentId = po.agentinfo_id "
			+" LEFT JOIN sys_user su ON po.create_by = su.id"		
			+" LEFT JOIN agentinfo agent ON t1.agentId = agent.id"			
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.moneyType = 13 and  mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter ON maoouter.serialNum = po.total_money"
			+" LEFT JOIN ("
			+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr1"
			+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
			+" where mao.moneyType = 5  and mao.businessType = 1"
			+" group by mao.serialNum " 
			+" ) maoouter1 ON maoouter1.serialNum = po.payed_money"      
			+" WHERE"
				//+" t1.activityId = ?  AND  po.order_state != 99  and po.place_holder_type=1 order by t1.agentId";
		      	+" t1.activityId = ?  AND  (po.order_state = 4 or po.order_state = 5) AND po.place_holder_type = 1 order by t1.agentId";   
		       List<Map<String,Object>> stockOrders = findBySql(queryOrdersByProductSql, Map.class, Long.parseLong(productId));
          return stockOrders;

	}
	
	
	/*散团订单明细*/
	@Override
	public List<Map<String, Object>> findProductGroupOrders(Long productId, String status){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String baseSql = "SELECT"
				+" ai.agentName agentName,"
				+" ao.id id,"
				+" ao.orderNum orderNo,"
				+" ao.payStatus payStatus,"
				+" maoouter.moneyStr totalMoney,"
				+" maoouter1.moneyStr1 payedMoney,"				
				+" ao.placeHolderType place_holder_type,"			
				+" ao.orderPersonNum orderPersonNum,"						
				+" aasu.name createUserName,"
				+" ao.salerName "
			+" FROM"
				+"  productorder ao"
			+" LEFT JOIN agentinfo ai ON ao.orderCompany = ai.id "
			+" LEFT JOIN activitygroup aa ON ao.productGroupId = aa.id"
			+" LEFT JOIN sys_user aasu ON ao.createBy = aasu.id"			
			+" LEFT JOIN ("
						+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr"
						+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
						+" where mao.moneyType = 13  and mao.businessType = 1 and c.create_company_id = " + companyId
						+" group by mao.serialNum " 
						+" ) maoouter ON maoouter.serialNum = ao.total_money"
						
			+" LEFT JOIN ("
						+" SELECT mao.serialNum,GROUP_CONCAT(CONCAT(c.currency_mark,mao.amount) separator '+') moneyStr1"
						+" FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id"
						+" where mao.moneyType = 5  and mao.businessType = 1 and c.create_company_id = " + companyId
						+" group by mao.serialNum " 
						+" ) maoouter1 ON maoouter1.serialNum = ao.payed_money"		
			 +" WHERE ao.delFlag = 0 and (ao.productGroupId = ? ) order by ao.orderCompany";
		
           List<Map<String,Object>> stockOrders = findBySql(baseSql, Map.class, productId);
          return stockOrders;
	}
	
	/*散团订单明细--渠道已切位团期 */
	@Override
	public List<Map<String, Object>> findReserveOrder(Long activityGroupId, Long agentId){
		String baseSql = "SELECT ag.groupOpenDate,"
				+" ao.orderMoney,"
				+" agr.payReservePosition,"			       
                +" ao.reservation,"
                +" dict.label,"
                +" ag.settlementAdultPrice,"
                +" ag.settlementcChildPrice,"
                +" ag.settlementSpecialPrice,"
                +" ag.currency_type currencys,"
                +" ag.planPosition,"
                +" ao.agentId,"
                +" ao.createDate,"
                +" ao.payType,"			        
                +" ao.remark,"	
                +" ee.filelist "	
                +" FROM activitygroup ag "
			 +" JOIN activityreserveorder ao ON ag.id=ao.activityGroupId join activitygroupreserve agr on ag.id = agr.activityGroupId "
			 + " and ao.agentId = agr.agentId and ao.reserveType=0"
			 +" LEFT JOIN sys_dict dict ON dict.value=ao.payType AND dict.type='offlineorder_pay_type'"
			 +"  LEFT JOIN  ("
			 +"   		SELECT reserveOrderId,GROUP_CONCAT(srcDocId) as filelist FROM activityreservefile where activityreservefile.activityGroupId=? GROUP BY reserveOrderId "	   
		     +" ) ee ON ee.reserveOrderId=ao.id " 			
			 +" WHERE ao.activityGroupId=? and ao.agentId=? "
			 +" and dict.type='offlineorder_pay_type' ";
	    List<Map<String,Object>> airOrders = findBySql(baseSql, Map.class, activityGroupId,activityGroupId,agentId);
        return airOrders;
	}
	
	/*机票订单明细--渠道已切位团期 */
	@Override
	public List<Map<String, Object>> findAirReserveOrder(Long productId, Long agentId){
		String baseSql = "SELECT air.startingDate,"
				+" ao.orderMoney,"
				+" ao.payReservePosition,"			       
                +" ao.reservation,"
                +" dict.label,"
                +" air.settlementAdultPrice,"
                +" air.settlementcChildPrice,"
                +" air.settlementSpecialPrice,"
                +" air.reservationsNum,"
                +" air.currency_id,"
                +" ao.agentId,"
                +" ao.createDate,"
                +" ao.payType,"			        
                +" ao.remark,"
                +" ee.filelist "	
                +" FROM activity_airticket air "
			 +" JOIN activityreserveorder ao ON air.id=ao.activityGroupId  and ao.reserveType=1"
			 +" LEFT JOIN sys_dict dict ON dict.value=ao.payType AND dict.type='offlineorder_pay_type'"
			 +"  LEFT JOIN  ("
			 +"   		SELECT reserveOrderId,GROUP_CONCAT(srcDocId) as filelist FROM airticketreservefile where airticketreservefile.airticketActivityId=? GROUP BY reserveOrderId "	   
		     +" ) ee ON ee.reserveOrderId=ao.id "
			 +" WHERE ao.activityGroupId=? and ao.agentId=? "
			 +" and dict.type='offlineorder_pay_type' ";		
		
	    List<Map<String,Object>> airOrders = findBySql(baseSql, Map.class, productId,productId,agentId);
        return airOrders;
	}
	
	/*机票订单明细--渠道已切位团期 */
	@Override
	public List<Map<String, Object>> findCostRecordLog(Long activityId, Integer orderType){
		String baseSql = "SELECT cost.costName,cost.result,cost.nowLevel, cost.remark, cost.createDate, user.name "               	
                +" FROM cost_record_log cost "
			 +" left JOIN sys_user user on cost.createBy= user.id "	
			 
			 +" WHERE cost.rid=? and cost.orderType=? ";	
		
	    List<Map<String,Object>> cost = findBySql(baseSql, Map.class, activityId,orderType);
        return cost;
	}
	
	/**
	 * 根据切位类型和对应的 散拼团期ID/机票产品ID 查询切位订单数据（散拼或者机票）
	 * 
	 * @param sourceId 散拼团期ID/机票产品ID
	 * @param reserveType 散拼0,机票1
	 * @return 
	 */
	@Override
	public List<Map<String, Object>> queryReserveList4ReserveType(Long sourceId,Integer reserveType) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("ao.createDate,ag.agentName,ao.payReservePosition,ao.reservation,ao.orderMoney,dict.label ");
		sb.append("FROM activityreserveorder ao ");
		sb.append("LEFT JOIN agentinfo ag on ao.agentId=ag.id ");
		sb.append("LEFT JOIN sys_dict dict ON dict.VALUE= ao.payType AND dict.type = 'offlineorder_pay_type' ");
		sb.append("WHERE ao.activityGroupId=? and ao.reserveType=?");
		List<Map<String,Object>> list = findBySql(sb.toString(), Map.class, sourceId,reserveType);
		return list;
	} 
	
	/**
	 * 根据团期id集合和批发商id获取切位集合
	 * @Description: 
	 * @param @param activityGroupIds
	 * @param @param agentId
	 * @param @return   
	 * @return List<ActivityGroupReserve>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-9
	 */
	public List<ActivityGroupReserve> getReservesByGroupIds(List<String> activityGroupIds, Long agentId) {
		if(CollectionUtils.isEmpty(activityGroupIds)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String activityGroupId : activityGroupIds) {
			sb.append("'");
			sb.append(activityGroupId);
			sb.append("'");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return super.find("from ActivityGroupReserve reserve where reserve.activityGroupId in ("+sb.toString()+") and reserve.agentId=? and reserve.delFlag=?", agentId, BaseEntity.DEL_FLAG_NORMAL);
	}

}
