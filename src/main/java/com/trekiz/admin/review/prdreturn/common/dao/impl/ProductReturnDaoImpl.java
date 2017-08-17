package com.trekiz.admin.review.prdreturn.common.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.prdreturn.common.dao.IProductReturnDao;

@Repository
public class ProductReturnDaoImpl extends BaseDaoImpl<Map<String, Object>> implements
		IProductReturnDao {
	
	/**
	 * 查询订单详情
	 */
	@Override
	public Map<String, Object> queryAirTicketReturnDetailInfoById(String orderId) {

		// 查询退票订单详情
		String airTicketReturnDetailSql = "SELECT a.id, a.create_by,"
				+ "a.create_date, b.createBy, a.type, a.order_no,a.product_type_id,"
				+ "a.group_code, b.settlementAdultPrice,b.departureCity,b.arrivedCity,b.reservationsNum,b.currency_id currid,"
				+ "b.settlementcChildPrice, b.settlementSpecialPrice,b.freePosition,"
				+ "b.taxamt, a.adult_num, a.child_num,b.airType as activityAirType,"
				+ "a.special_num FROM airticket_order a,"
				+ "activity_airticket b, activity_flight_info c "
				+ "WHERE a.airticket_id = b.id "
				+ "AND a.airticket_id = c.airticketId and a.id = ?";

		// 查询航班信息
		String flightInfoSql = "SELECT b.airType, b.departureCity, "
				+ "b.arrivedCity,  "
				+ "c.leaveAirport, d.airport_name as leaveAirport_name, c.destinationAirpost, e.airport_name as destinationAirpost_name,"
				+ "c.startTime, c.arrivalTime, c.airlines, c.ticket_area_type,"
				+ "c.spaceGrade, c.airspace, c.number FROM "
				+ "airticket_order a, activity_airticket b, "
				+ "activity_flight_info c,sys_airport_info d,sys_airport_info e WHERE a.airticket_id = b.id "
				+ "AND a.airticket_id = c.airticketId AND d.id = c.leaveAirport and e.id = c.destinationAirpost AND a.id = ? order by c.number";

		// 查询退票游客信息
		String airTicketReturnTravelerSql = "SELECT b.id, b.name,b.isAirticketFlag, "
				+ "a.create_date, b.payPriceSerialNum as payPrice FROM "
				+ "airticket_order a, traveler b WHERE b.order_type = 7 and  b.delFlag = '0'  and "
				+ "a.id = b.orderid AND b.id not in(select c.travelerId from review c where c.orderId = b.orderid AND c.status in('1','2','3') AND c.productType = '7' and c.flowType = '3') AND a.id =?";

		List<Map<String, Object>> airTicketReturnDetailList = findBySql(
				airTicketReturnDetailSql, Map.class, orderId);// Long.parseLong(orderId)

		if (airTicketReturnDetailList == null
				|| airTicketReturnDetailList.size() <= 0)
			return new HashMap<String, Object>();

		Map<String, Object> airTicketReturnDetailListMap = airTicketReturnDetailList
				.get(0);// 订单信息
		// 把用户code转为名字
		Object createByObj = airTicketReturnDetailListMap.get("create_by");
		String createName = "";
		if(createByObj != null && StringUtils.isNotBlank(createByObj.toString())){
			User user = UserUtils.getUser(Long.parseLong(createByObj.toString()));
			if(user != null){
				createName = user.getName();
			}
		}
		airTicketReturnDetailListMap.put("createName", createName);
		String createByName ="";
		Object jdCreateBy = airTicketReturnDetailListMap.get("createBy");
		if(jdCreateBy != null && !"".equals(jdCreateBy.toString())){
			createByName =UserUtils.getUser(Long.parseLong(jdCreateBy.toString())).getName();
		}
		airTicketReturnDetailListMap.put("createByName", createByName);
		// 航段信息 Long.parseLong(orderId)
		List<Map<String, Object>> flightInfoList = findBySql(flightInfoSql,
				Map.class, orderId);

		airTicketReturnDetailListMap.put("flightInfoList", flightInfoList);
		// 游客信息 Long.parseLong(orderId)
		List<Map<String, Object>> airTicketReturnTravelerList = findBySql(
				airTicketReturnTravelerSql, Map.class, orderId);

		airTicketReturnDetailListMap.put("travelInfoList",
				airTicketReturnTravelerList);

		return airTicketReturnDetailListMap;
	}
}
