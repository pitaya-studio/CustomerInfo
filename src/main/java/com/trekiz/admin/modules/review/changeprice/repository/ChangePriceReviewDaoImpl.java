package com.trekiz.admin.modules.review.changeprice.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class ChangePriceReviewDaoImpl extends
		BaseDaoImpl<Map<String, Object>> implements IChangePriceReviewDao {

/**
	 * 查询退款审批列表信息 查询信息 groupCode : 团号 productType : 团队类型 startTime : 下单时间 起始
	 * endTime : 下单时间 结束 channel : 渠道 saler : 销售 meter : 计调
	 */
	@Override
	public Page<Map<String, Object>> findRefundReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime,
			String endTime, String channel, String saler, String meter,
			String status, List<Integer> level, String flowType, String cOrderBy, String uOrderBy, UserJob userJob, Long reviewCompanyId, List<Long> subIds) {
		String productType = null;
		//获取职位对应的产品类型
		Integer orderType = userJob.getOrderType();
		if(orderType == 7){//机票
			productType = "7,8";
		} else if(orderType == 0){//团期
			productType = "1,2,3,4,5,9,10";//新加游轮产品
		} else if(orderType == 2){//散拼切位
			productType = "2,9";;
		} else {
			productType = orderType.toString();
		}
		String refundReviewSql = "select " +
				"groupcode," +
				"groupid," +
				"orderid," +
				"orderno,comments,prdtype,flowtype,createdate," +
				"agentid,salecreateby,jidcreateby,supplycom," +
				"chanpid,chanpname,revid,revcom,lastoperator," +
				"revactive,createtime,updatetime,curlevel,toplevel," +
				"revstatus,printTime,cpid,deptId,printFlag,travelerid," +
				"payStatus,salerId,salerName " +
				"from refundreview_view where 1=1  and cpid = " + reviewCompanyId + " and revcom ="
				+ UserUtils.getUser().getCompany().getId() + " ";
		/*根据不同的状态 添加不同的SQL过滤条件*/
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0){
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if(subIdsStr != null && !"".equals(subIdsStr)){
			refundReviewSql += " and deptId in(" + subIdsStr + ") ";//部门过滤
		}
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
			if(statusInt == 1){
				refundReviewSql += " and revstatus ='" + status + "' ";//审核状态
				refundReviewSql += " and curlevel = " + level.get(0);//审核层级
			}
			if(statusInt == 5){//审核中的
				refundReviewSql += " and revstatus ='1' ";//审核状态
			}
			if(statusInt == 0){//查询已驳回的
				refundReviewSql += " and revstatus ='" + status + "' ";//审核状态
			}
			if(statusInt == 4){//查询已取消的的
				refundReviewSql += " and revstatus ='" + status + "' ";//审核状态
			}
			//如果为2 审核成功
			if (statusInt == 2){
				refundReviewSql += "and (revstatus ='2' or revstatus ='3')";//审核状态

			}
		}
		// 团号 对应页面的团号查询条件
		if (groupCode != null && !"".equals(groupCode.trim())) {
			refundReviewSql += " and groupcode like'%" + groupCode + "%' ";
		}
		// 机票表的产品类型id 对应页面的团队类型查询条件
		if (productType != null && !"".equals(productType.trim())) {
			refundReviewSql += " and prdtype in(" + productType + ") ";
		}
		// 时间范围 页面查询条件 审核的下单时间
		if (startTime != null) {

			refundReviewSql += " and createtime >= '" + startTime + " 00:00:00' ";
		}
		if (endTime != null) {
			refundReviewSql += " and createtime <= '" + endTime + " 23:59:59' ";
		}
		// 渠道 对应页面的渠道查询条件
		if (channel != null && !"".equals(channel.trim())) {
			refundReviewSql += " and agentid = '" + Integer.parseInt(channel)
					+ "' ";
		}
		// 下单人 
		if (saler != null && !"".equals(saler.trim())) {
			refundReviewSql += " and salecreateby = '" + Integer.parseInt(saler)
					+ "' ";
		}
		String truesaler = request.getParameter("truesaler");// 销售
		if (truesaler != null && "-99999".equals(truesaler.trim())) {
			truesaler = null;
		}
		if (truesaler != null && !"".equals(truesaler.trim())) {
			refundReviewSql += " and salerId = '" + Integer.parseInt(truesaler)
					+ "' ";
		}
		// 计调 对应页面的渠道查询条件
		if (meter != null && !"".equals(meter.trim())) {
			refundReviewSql += " and jidcreateby = '" + Integer.parseInt(meter)
					+ "' ";
		}
		refundReviewSql += " and flowtype = " + Long.parseLong(flowType);
		// 排序条件
		refundReviewSql += " ORDER BY 1=1";
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			refundReviewSql+=", createtime "+cOrderBy;
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			refundReviewSql+=", updatetime "+uOrderBy;
		}
		return findBySql(new Page<Map<String, Object>>(request, response),
				refundReviewSql, Map.class);
	}

	/**
	 * 查询团期订单详情
	 * 
	 */
//	@Override
//	public Map<String, Object> queryGroupOrderDetail(String prdOrderId) {
//		String groupOrderSql = "SELECT pro.createBy as ordercreate, pro.createDate as orderdate, pro.orderType as orderstatus, pro.orderNum as orderno, act.groupCode as groupno, "
//				+ "pro.orderTotalSerialNum as totalmoney, pro.orderStatus as prdtype, tra.createBy as updateby, tra.fromArea, tra.targetArea as targetarea, tra.groupOpenDate, "
//				+ "tra.activityDuration, tra.outArea, pro.settlementAdultPrice, pro.orderPersonNumAdult, "
//				+ "pro.settlementcChildPrice, pro.orderPersonNumChild, pro.settlementSpecialPrice, pro.orderPersonNumSpecial  "
//				+ "FROM productorder pro, activitygroup act, travelactivity tra "
//				+ "WHERE pro.productGroupId = act.id AND act.srcActivityId = tra.id AND pro.id = "
//				+ Integer.parseInt(prdOrderId);
//		List<Map<String, Object>> groupOrderDetail = findBySql(groupOrderSql,
//				Map.class);
//		if (groupOrderDetail == null || groupOrderDetail.size() == 0) {
//			return null;
//		}
//		return groupOrderDetail.get(0);
//	}

	/**
	 * 查询机票订单审核详情
	 */
	@Override
	public Map<String, Object> queryAirticketReviewOrderDetail(String prdOrderId, String prdType) {
		String airticketSql;
		// 查询退票订单详情
//		if("7".equals(prdType.trim())){
			airticketSql = "SELECT a.id as orderid, a.create_by as ordermaker, a.salerId as ordercreate,a.agentinfo_id as qdid,"
					+ "a.create_date as orderdate,a.comments as comments, b.createBy as updateby, b.airType as type, a.order_no as orderno,a.product_type_id as prdtype,"
					+ "a.group_code as groupno, b.settlementAdultPrice,a.total_money as totalmoney,a.order_state as orderstatus, b.departureCity,b.arrivedCity,b.reservationsNum,"
					+ "b.settlementcChildPrice, b.settlementSpecialPrice,"
					+ "b.taxamt, a.adult_num, a.child_num, "
					+ "a.special_num FROM airticket_order a,"
					+ "activity_airticket b "//, activity_flight_info c "
					+ "WHERE a.airticket_id = b.id "
					+ /*AND a.airticket_id = c.airticketId*/" and a.id = " + Long.parseLong(prdOrderId);
//		}
//		else{ 机票切位无改价 注释掉
//			airticketSql = "SELECT a.id as orderid, a.saleId as ordercreate,"
//					+ "a.createDate as orderdate, b.createBy as updateby, b.airType as type, a.orderNum as orderno,8 as prdtype,"
//					+ "a.groupCode as groupno, b.settlementAdultPrice,a.orderMoney as totalmoney,a.orderStatus as orderstatus, b.departureCity,b.arrivedCity,b.reservationsNum,"
//					+ "b.settlementcChildPrice, b.settlementSpecialPrice,"
//					+ "b.taxamt FROM activityreserveorder a,"
//					+ "activity_airticket b, activity_flight_info c "
//					+ "WHERE a.srcActivityId = b.id "
//					+ "AND a.srcActivityId = c.airticketId  and a.id = " + Long.parseLong(prdOrderId);
//		}
		
		// 查询航班信息
		String flightInfoSql = "SELECT b.airType, b.departureCity, "
				+ "b.arrivedCity,  "
				+ "c.leaveAirport, c.destinationAirpost, "
				+ "c.startTime, c.arrivalTime, c.airlines, "// c.ticket_area_type,
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
			+ "b.groupOpenDate AS opendate, "/*出团日期*/
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

//	/** 改价无切位 注释掉
//	 * 散拼切位订单详情查询
//	 */
//	@Override
//	public Map<String, Object> querySanPinReserveOrderDetail(String prdOrderId) {
//		String spReserviceSql = "SELECT "
//			+ "a.id as orderid, "/*订单id*/
//			+ "a.saleId as ordercreate, "/*销售*/
//			+ "a.createDate as orderdate, "/*下单时间*/
//			+ "b.createBy as updateby, "/*操作人*/
//			+ "a.orderNum as orderno, "/*订单编号*/
//			+ "9 as prdtype, "/* 产品类型*/
//			+ "c.groupCode as groupno, "/*团号*/
//			+ "a.orderMoney as totalmoney, "/*订单总额*/
//			+ "a.orderStatus as orderstatus, "/*订单状态*/
//			+ "b.groupOpenDate AS opendate, "/*出团日期*/
//			+ "b.targetArea as targetarea, "/*目的地*/
//			+ "b.fromArea as fromarea, "/*出发城市*/
//			+ "b.activityDuration as tradays, "/*行程天数*/
//			+ "b.createDate as tracreatedate, "/*创建时间*/
//			+ "a.payReservePosition as payreserve, "/*已切位数*/
//			+ "c.settlementAdultPrice, "
//			+ "c.settlementcChildPrice, "
//			+ "c.settlementSpecialPrice "
//		+ "FROM "
//			+ "activityreserveorder a, "/*切位订单表*/
//			+ "travelactivity b, "/*团期产品表*/
//			+ "activitygroup c "/*团期表*/
//		 + "where a.srcActivityId = b.id  "
//			+ "and a.activityGroupId = c.id and a.id = " + Long.parseLong(prdOrderId);
//		//查询SQL
//		List<Map<String, Object>> sanPinReserveOrderDetail = findBySql(spReserviceSql,
//				Map.class);
//		if (sanPinReserveOrderDetail == null || sanPinReserveOrderDetail.size() != 1) {
//			return null;
//		}
//		//返回数据
//		return sanPinReserveOrderDetail.get(0);
//	}
//	

}
