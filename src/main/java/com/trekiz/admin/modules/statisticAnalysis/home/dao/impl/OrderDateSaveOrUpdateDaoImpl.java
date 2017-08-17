package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;

/**
 * @author junhao.zhao
 * 2017年1月5日  下午8:28:39
 */
@Repository
public class OrderDateSaveOrUpdateDaoImpl extends BaseDaoImpl<OrderDataStatistics> implements OrderDateSaveOrUpdateDao{
	
	/**为了向表order_data_statistics中添加数据，需要从四张表查询UUID()、product_name、company_uuid、amount.
	 * @param productOrderId
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#getAddDate(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getAddDate(Long productOrderId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("UUID(),tr.acitivityName AS product_name,so.uuid AS company_uuid, ");
		sbf.append("SUM(ma.amount*ma.exchangerate) - ");
		sbf.append("IFNULL((SELECT	m.amount * m.exchangerate FROM money_amount m WHERE m.serialNum = o.differenceMoney),0) AS amount ");
		sbf.append("FROM ");
		sbf.append("productorder o ");
		sbf.append("LEFT JOIN travelactivity tr ON o.productId = tr.id ");
		sbf.append("LEFT JOIN sys_office so ON tr.proCompany = so.id ");
		sbf.append("LEFT JOIN money_amount ma ON o.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.payStatus NOT IN('"+Context.ORDER_PAYSTATUS_YQX+"','"+Context.ORDER_PAYSTATUS_DEL+"') ");
		sbf.append("AND o.delFlag = '"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		//sbf.append("AND o.placeHolderType <> '"+Context.PLACEHOLDERTYPE_QW+"' ");
		sbf.append("AND o.id= '"+productOrderId+"' ");
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对productorder
	 * @param productOrderId
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#getOrderDataStatisticsId(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getOrderDataStatisticsId(Long productOrderId) {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT id ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics ods ");
		sbf.append("WHERE ");
		sbf.append("ods.order_id= '"+productOrderId+"' ");
		sbf.append("AND ods.order_type not IN('"+Context.ORDER_STATUS_VISA+"','"+Context.ORDER_STATUS_AIR_TICKET+"') ");
				
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对visa_order
	 * @param visaOrderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#getOrderDataStatisticsId(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getOrderDataStaIdForVisa(Long visaOrderId, Integer orderType) {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT id ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics ods ");
		sbf.append("WHERE ");
		sbf.append("ods.order_id= ? ");
		sbf.append("AND ods.order_type = ? ");
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class, visaOrderId, orderType);
		return list;
	}

	/**表productorder订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param id
	 * @param orderPersonNum
	 * @param amount
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateForProductorder(java.lang.Long, java.lang.Integer, java.math.BigDecimal)
	 */
	@Override
	public int updateForProductorder(Long id, Integer orderPersonNum, BigDecimal amount, Integer orderStatus) {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.amount = ? , ods.order_person_num = ? , ods.order_status = ?  WHERE ods.id = ? ");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), amount, orderPersonNum, orderStatus, id);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}
	
	/**取消订单，将表order_data_statistics中的order_status改为99(针对productOrder表中的取消)
	 * @param productOrderId
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateOrderStatus(java.lang.Long)
	 */
	@Override
	public int updateOrderStatus(Long productOrderId, String orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.order_status = ? WHERE ods.order_id = ? AND ods.order_type = ? ");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), Context.ORDER_PAYSTATUS_YQX, productOrderId, orderType);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}
	
	/**激活或删除订单，将表order_data_statistics中的order_status改为激活或删除(针对productOrder表中的删除与激活)
	 * @param productOrderId
	 * @param payStatus
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateOrderStatus(java.lang.Long)
	 */
	@Override
	public int updateOrderStatusInvoke(Long productOrderId,Integer payStatus) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.order_status = ? WHERE ods.order_id = ? AND ods.order_type NOT IN(?,?) ");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), payStatus, productOrderId, Context.ORDER_STATUS_VISA, Context.ORDER_STATUS_AIR_TICKET);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}
	
	/**取消订单，将表order_data_statistics中的order_status改为取消、删除、激活(针对visa_order表的取消、激活与airticket_order表中的取消、删除、激活)
	 * @param orderId
	 * @param orderStatus
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateOrderStatus(java.lang.Long)
	 */
	@Override
	public int updateOrderStatus(Long orderId,Integer orderStatus,Integer orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.order_status = ? WHERE ods.order_id = ? AND ods.order_type = ? ");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), orderStatus, orderId, orderType);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}

	/**为了向表order_data_statistics中添加数据，需要根据visaOrderId从四张表查询UUID()、product_name、company_uuid、amount
	 * @param visaOrderId
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#getVisaAddDate(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getVisaAddDate(Long visaOrderId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("UUID(),tr.productName AS product_name,so.uuid AS company_uuid, ");
		sbf.append("SUM(ma.amount*ma.exchangerate) AS amount ");
		sbf.append("FROM ");
		sbf.append("visa_order o ");
		sbf.append("LEFT JOIN visa_products tr ON o.visa_product_id = tr.id ");
		sbf.append("LEFT JOIN sys_office so ON tr.proCompanyId = so.id ");
		sbf.append("LEFT JOIN money_amount ma ON o.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.visa_order_status!='"+Context.VISA_ORDER_PAYSTATUS_CANCEL+"' ");
		sbf.append("AND o.payStatus!='"+Context.ORDER_PAYSTATUS_YQX+"' ");
		sbf.append("AND o.del_flag = '"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		sbf.append("AND o.create_date IS NOT NULL AND o.create_date != '0000-00-00 00:00:00' ");
		sbf.append("AND o.id= '"+visaOrderId+"' ");
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}

	/**为了向表order_data_statistics中添加数据，需要根据airticket_order从四张表查询UUID()、product_name、company_uuid、amount
	 * @param airticketOrderId
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#getAirticketAddDate(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getAirticketAddDate(Long airticketOrderId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("UUID(),CONCAT('机票',CASE WHEN tr.product_code IS NULL THEN '' ELSE tr.product_code END) AS product_name, ");
		sbf.append("so.uuid AS company_uuid, ");
		sbf.append("SUM(ma.amount*ma.exchangerate) AS amount ");
		sbf.append("FROM ");
		sbf.append("airticket_order o ");
		sbf.append("LEFT JOIN activity_airticket tr ON o.airticket_id = tr.id ");
		sbf.append("LEFT JOIN sys_office so ON tr.proCompany = so.id ");
		sbf.append("LEFT JOIN money_amount ma ON o.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.order_state NOT IN('"+Context.ORDER_PAYSTATUS_YQX+"','"+Context.ORDER_PAYSTATUS_DEL+"') ");
		sbf.append("AND o.del_flag = '"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		sbf.append("AND o.place_holder_type <> '"+Context.PLACEHOLDERTYPE_QW+"' ");
		sbf.append("AND o.id= '"+airticketOrderId+"' ");
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
		
	}
	
	/**visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额、支付状态修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updatePeopleAndMoney(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public int updatePeopleAndMoney(Long orderId, Integer orderType) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE ");
		sbf.append("order_data_statistics ods, ");
		sbf.append("(SELECT ");
		sbf.append("IFNULL(SUM(ma.amount*ma.exchangerate),0.00) AS amount,o.travel_num AS order_person_num, ");
		sbf.append("o.total_money AS amount_uuid, o.visa_order_status AS visaOrderStatus ");
		sbf.append("FROM ");
		sbf.append("visa_order o ");
		sbf.append("LEFT JOIN money_amount ma ON o.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.id = ? ) vm ");
		sbf.append("SET ");
		sbf.append("ods.amount=vm.amount, ");
		sbf.append("ods.amount_uuid = vm.amount_uuid, ");
		sbf.append("ods.order_person_num = vm.order_person_num, ");
		sbf.append("ods.order_status = vm.visaOrderStatus ");
		sbf.append("WHERE ods.order_id = ?  AND ods.order_type = ? ");
		
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), orderId, orderId, orderType);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}
	
	/**productorder订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updatePeopleAndMoney(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public int updatePeopleAndMoneyPro(Long orderId, Integer orderType) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE ");
		sbf.append("order_data_statistics ods, ");
		sbf.append("(SELECT ");
		sbf.append("IFNULL(SUM(ma.amount*ma.exchangerate) - ");
		sbf.append("IFNULL((SELECT m.amount * m.exchangerate FROM money_amount m WHERE m.serialNum = o.differenceMoney),0),0.00) AS amount, ");
		sbf.append("o.orderPersonNum AS order_person_num, o.payStatus ");
		sbf.append("FROM ");
		sbf.append("productorder o ");
		sbf.append("LEFT JOIN money_amount ma ON o.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.id = ? ) pm ");
		sbf.append("SET ");
		sbf.append("ods.amount = pm.amount, ");
		sbf.append("ods.order_person_num = pm.order_person_num, ");
		sbf.append("ods.order_status = pm.payStatus ");
		sbf.append("WHERE ods.order_id = ?  AND ods.order_type = ? ");
		
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), orderId, orderId, orderType);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}
	
	

	/**airticket_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param totalAmount
	 * @param personNum
	 * @param orderId
	 * @param orderStatusAirTicket
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateForAirticket(java.math.BigDecimal, int, java.lang.Long, java.lang.String)
	 */
	@Override
	public int updateForAirticket(BigDecimal totalAmount, int personNum, Long orderId, String orderStatusAirTicket) {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.amount = ? , ods.order_person_num = ?  WHERE ods.order_id = ? AND ods.order_type = ?");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), totalAmount, personNum, orderId, orderStatusAirTicket);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}

	/**通过传来的参数更改表order_data_statistics订单金额与金额uuid
	 * @param id
	 * @param orderTypeQz
	 * @param visaPay
	 * @param payPriceSerialNum
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateMoneyAndUuid(java.lang.Long, java.lang.Integer, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public int updateMoneyAndUuid(Long id, Integer orderTypeQz, BigDecimal visaPay, String payPriceSerialNum) {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.amount = ? , ods.amount_uuid = ?  WHERE ods.order_id = ? AND ods.order_type = ?");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), visaPay, payPriceSerialNum, id, orderTypeQz);
		int executeUpdate = sqlCreateQuery.executeUpdate();
		return executeUpdate;
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao#updateDelFlag(java.lang.Long, java.lang.Integer)
	 * OrderDateSaveOrUpdateDao updateDelFlag
	 * 
	 * visa_order订单删除之后，修改表order_data_statistics中对应的订单状态
	 * 
	 * 2017年3月23日  下午3:29:13 
	 */
	@Override
	public void updateDelFlag(Long id, Integer orderTypeQz) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("UPDATE order_data_statistics ods SET ods.del_flag = ?  WHERE ods.order_id = ? AND ods.order_type = ?");
		SQLQuery sqlCreateQuery = super.createSqlQuery(sbf.toString(), BaseEntity.DEL_FLAG_DELETE, id, orderTypeQz);
		sqlCreateQuery.executeUpdate();
	}
	
}
