package com.trekiz.admin.review.changePrice.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.review.changePrice.common.dao.IChangePriceReviewNewDao;

@Repository
public class ChangePriceReviewNewDaoImpl extends BaseDaoImpl<Map<String, Object>> implements IChangePriceReviewNewDao {
	
	/**
	 * @Description 查询游客对应币种信息
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	@Override
	public List<Map<String,Object>> queryTravelerCurrency(Long orderId, Integer orderType) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, ma.amount, ma.moneyType, c.currency_name, t.name, c.currency_id, ma.serialNum, t.original_payPriceSerialNum ");
		sb.append("FROM money_amount ma, currency c, traveler t ");
		sb.append("WHERE (ma.serialNum = t.payPriceSerialNum ) AND ma.currencyId = c.currency_id ");
		sb.append("AND t.orderId = ? AND t.order_type = " + orderType);
		return this.findBySql(sb.toString(), Map.class, orderId); 
	}
	
	/**
	 * 查询机票订单审核详情
	 */
	@Override
	public Map<String, Object> queryAirticketReviewOrderDetail(String prdOrderId, String prdType) {
		String airticketSql;
		// 查询退票订单详情
		airticketSql = "SELECT a.id as orderid, a.create_by as ordermaker, a.salerId as ordercreate,a.agentinfo_id as qdid,"
				+ "a.create_date as orderdate,a.comments as comments, b.createBy as updateby, b.airType as type, a.order_no as orderno,a.product_type_id as prdtype,"
				+ "a.group_code as groupno, b.group_code as proGroupCode, b.id as proId , b.settlementAdultPrice,a.total_money as totalmoney,a.order_state as orderstatus, b.departureCity,b.arrivedCity,b.reservationsNum,"
				+ "b.settlementcChildPrice, b.settlementSpecialPrice,b.currency_id currid,"
				+ "b.taxamt, a.adult_num, a.child_num, "
				+ "a.special_num FROM airticket_order a,"
				+ "activity_airticket b "//, activity_flight_info c "
				+ "WHERE a.airticket_id = b.id "
				+ /*AND a.airticket_id = c.airticketId*/" and a.id = " + Long.parseLong(prdOrderId);
		// 查询航班信息
		String flightInfoSql = "SELECT b.airType, b.departureCity, "
				+ "b.arrivedCity,  "
				+ "c.leaveAirport, c.destinationAirpost, "
				+ "c.startTime, c.arrivalTime, c.airlines, c.ticket_area_type,"// 
				+ "c.spaceGrade, c.airspace, c.number FROM "
				+ "airticket_order a, activity_airticket b, "
				+ "activity_flight_info c WHERE a.airticket_id = b.id "
				+ "AND a.airticket_id = c.airticketId AND a.id = " + Long.parseLong(prdOrderId)
				+" order by c.number";
		
		List<Map<String, Object>> airticketOrderDetail = findBySql(airticketSql,
				Map.class);
		
		List<Map<String, Object>> airticketFlightDetail = findBySql(flightInfoSql,
				Map.class);
		if (airticketOrderDetail == null || airticketOrderDetail.size() != 1) {
			return null;
		}
		Map<String, Object> orderDetail = airticketOrderDetail.get(0);
		orderDetail.put("flightInfoList", airticketFlightDetail);
		return orderDetail;
	}
	
	/**
	 * 查询团旗订单审核详情
	 */
	@Override
	public Map<String, Object> queryGroupReviewOrderDetail(String prdOrderId) {

		String groupSql = "SELECT "
			+ "a.id as orderid, "/*订单id*/
			+ "b.id as cpid, "/*产品id*/
			+ "a.orderCompany as qdid,"
			+ "a.createBy AS ordermaker, "/*下单人*/
			+ "a.salerId AS ordercreate, "/*销售*/
			+ "a.createDate AS orderdate, "/*下单时间*/
			+ "b.createBy AS updateby, "/*操作人*/
			+ "a.orderNum AS orderno, "/*订单编号*/
			+ "a.specialDemand AS comments, "/*订单编号*/
			+ "a.orderStatus AS prdtype, "/*产品类型*/
			+ "c.groupCode AS groupno, "/*团号*/
			+ "a.total_money AS totalmoney, "/*订单总额*/
			+ "a.payStatus AS orderstatus, "/*订单状态*/
			+ "b.acitivityName as prdname, "/*产品名称*/
			+ "b.groupOpenDate AS opendate, "/*出团日期*/
//			+ "b.targetArea as targetarea, "/*目的地*/
			+ "b.fromArea as fromarea, "/*出发城市*/
			+ "b.activityDuration as tradays, "/*行程天数*/
//			+ "b.outArea as outarea, "/*离境城市*/
			+ "d.outArea as outarea, "/*离境城市*/
			+ "d.intermodalType as modaltype, "/*联运类型*/
			+ "a.orderPersonNumAdult as nadult, "/*成人数量*/
			+ "a.orderPersonNumChild as nchild, "/*儿童数量*/
			+ "a.orderPersonNumSpecial as nspecial, "/*特殊人数量*/
			+ "a.roomNumber as roomNumber, "/*特殊人数量*/
			+ "a.settlementAdultPrice, " 
			+ "a.settlementcChildPrice, "
			+ "a.settlementSpecialPrice " 
		+ "FROM "
			+ "productorder a, "/*团期订单表*/
			+ "activitygroup c, "/*团期表*/
			+ "travelactivity b LEFT JOIN  "/*团期产品表*/
			+ "activity_airticket d on b.airticket_id = d.id "/*机票产品表*/
		+ "where a.productId = b.id "
		 + "and a.productGroupId = c.id "
		 + "and a.id = " + prdOrderId;
		List<Map<String, Object>> groupOrderDetail = findBySql(groupSql,
				Map.class);
		if (groupOrderDetail == null || groupOrderDetail.size() != 1) {
			return null;
		}
		Map<String, Object> groupDetail = groupOrderDetail.get(0);
		String targetAreasSql = "select * from activitytargetarea where srcActivityId = " + groupDetail.get("cpid").toString();
		List<Map<String, Object>> targetAreas = findBySql(targetAreasSql, Map.class);
		groupDetail.put("targetAreas", targetAreas);
		return groupDetail;
	}

	/**
	 * 查询签证订单审核详情
	 */
	@Override
	public Map<String, Object> queryVisaReviewOrderDetail(String prdOrderId) {
		String visaSql = "SELECT a.id as orderid,a.agentinfo_id as qdid,a.create_by as ordermaker,a.salerId as ordercreate,a.create_date as orderdate,a.product_type_id as prdtype,"+
					"a.order_no as orderno,a.group_code as groupno, a.remark as comments, a.total_money as totalmoney,a.payStatus as orderstatus, "+
					"b.createBy as updateby, b.id as visaproductid,b.productCode as prdcode,b.productName as prdname,b.sysCountryId as countryid, "+
					"b.visaType as visatype,b.collarZoning as collarea,b.currencyId as visaCurrency,b.visaPay as visapay,a.travel_num as tnum "+
					" FROM visa_order a,visa_products b "+
					" WHERE a.visa_product_id = b.id and a.id=" + prdOrderId;
		List<Map<String, Object>> visaOrderDetail = findBySql(visaSql,
				Map.class);
		if (visaOrderDetail == null || visaOrderDetail.size() != 1) {
			return null;
		}
		return visaOrderDetail.get(0);
	}

	/**
	 * 查询散拼详情信息
	 */
	@Override
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId) {
		//组织散拼查询SQL
		String sanPinSql = "SELECT "
			+ "a.id AS orderid, "
			+ "a.orderCompany as qdid,"
			+ "b.id as cpid, "/*产品id*/
			+ "a.createBy AS ordermaker, "
			+ "a.salerId AS ordercreate, "
			+ "a.createDate AS orderdate, "
			+ "b.createBy AS updateby, "
			+ "a.orderNum AS orderno, "
			+ "a.orderStatus AS prdtype, "
			+ "c.groupCode AS groupno, "
			+ "b.acitivityName as prdname, "/*产品名称*/
			+ "a.total_money AS totalmoney, "
			+ "a.payStatus AS orderstatus, "/*订单支付状态 由orderType改为了payStatus*/
			+ "c.groupCode AS grpcode, "
			+ "c.groupOpenDate AS opendate, "/*出团日期*/
//			+ "b.targetArea as targetarea, "/*目的地*/
			+ "b.fromArea as fromarea, "/*出发城市*/
			+ "b.activityDuration as tradays, "/*行程天数*/
			+ "b.createDate as tracreatedate, "/*创建时间*/
			+ "c.planPosition as pposition, "/*预收人数*/
			+ "c.freePosition as fposition, "/*余位*/
//			+ "a.orderPersonnum as pnum, "/*预报名数*/
			+ "a.orderPersonNumAdult as nadult, "/*成人数量*/
			+ "a.orderPersonNumChild as nchild, "/*儿童数量*/
			+ "a.orderPersonNumSpecial as nspecial, "/*特殊人数量*/
			+ "a.settlementAdultPrice, "
			+ "a.settlementcChildPrice, "
			+ "a.settlementSpecialPrice "
			+ "FROM "
			+ "productorder a, "/*团期订单表*/
			+ "travelactivity b, "/*团期产品表*/
			+ "activitygroup c "/*团期表*/
			+ "WHERE "
			+ "a.productId = b.id "
			+ "AND a.productGroupId = c.id and a.id = " + Long.parseLong(prdOrderId);
		//查询SQL
		List<Map<String, Object>> sanPinOrderDetail = findBySql(sanPinSql,
				Map.class);
		if (sanPinOrderDetail == null || sanPinOrderDetail.size() != 1) {
			return null;
		}
		Map<String, Object> groupDetail = sanPinOrderDetail.get(0);
		String targetAreasSql = "select * from activitytargetarea where srcActivityId = " + groupDetail.get("cpid").toString();
		List<Map<String, Object>> targetAreas = findBySql(targetAreasSql, Map.class);
		groupDetail.put("targetAreas", targetAreas);
		//返回数据
		return sanPinOrderDetail.get(0);
	}
}
