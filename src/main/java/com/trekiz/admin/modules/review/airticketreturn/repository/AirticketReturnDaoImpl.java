package com.trekiz.admin.modules.review.airticketreturn.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class AirticketReturnDaoImpl extends BaseDaoImpl<Map<String, Object>>
		implements IAirticketReturnDao {
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	/**
	 * 查询订单详情
	 */
	@Override
	public Map<String, Object> queryAirTicketReturnDetailInfoById(String orderId) {

		// 查询退票订单详情
		String airTicketReturnDetailSql = "SELECT a.id, a.create_by,"
				+ "a.create_date, b.createBy, a.type, a.order_no,a.product_type_id,"
				+ "a.group_code, b.settlementAdultPrice,b.departureCity,b.arrivedCity,b.reservationsNum,"
				+ "b.settlementcChildPrice, b.settlementSpecialPrice,b.currency_id,b.freePosition,"
				+ "b.taxamt, a.adult_num, a.child_num,b.airType as activityAirType,"
				+ "a.special_num FROM airticket_order a,"
				+ "activity_airticket b, activity_flight_info c "
				+ "WHERE a.airticket_id = b.id "
				+ "AND a.airticket_id = c.airticketId and a.id = ?";

		// 查询航班信息
		String flightInfoSql = "SELECT b.airType, b.departureCity, "
				+ "b.arrivedCity, c.ticket_area_type, "
				+ "c.leaveAirport, d.airport_name as leaveAirport_name, c.destinationAirpost, e.airport_name as destinationAirpost_name,"
				+ "c.startTime, c.arrivalTime, c.airlines, "// c.ticket_area_type,
				+ "c.spaceGrade, c.airspace, c.number FROM "
				+ "airticket_order a, activity_airticket b, "
				+ "activity_flight_info c,sys_airport_info d,sys_airport_info e WHERE a.airticket_id = b.id "
				+ "AND a.airticket_id = c.airticketId AND d.id = c.leaveAirport and e.id = c.destinationAirpost AND a.id = ? order by c.number";

		// 查询退票游客信息
		String airTicketReturnTravelerSql = "SELECT b.id, b.name,b.isAirticketFlag, "
				+ "a.create_date, b.payPriceSerialNum as payPrice FROM "
			//	+ "airticket_order a, traveler b WHERE b.order_type = 7 and "
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
		String createName = UserUtils.getUser(
				Long.parseLong(airTicketReturnDetailListMap.get("create_by")
						.toString())).getName();
		airTicketReturnDetailListMap.put("createName", createName);
		String createByName ="";
		Object jdCreateBy = airTicketReturnDetailListMap.get("createBy");
		if(jdCreateBy != null && !"".equals(jdCreateBy.toString())){
			createByName =UserUtils.getUser(Long.parseLong(jdCreateBy.toString())).getName();
		}
		airTicketReturnDetailListMap.put("createByName", createByName);
		// Integer airticketId = (Integer) orderInfoMap.get("airticketId");// 产品id
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

	/**
	 * 查询退票审批列表
	 */
	@Override
	public Page<Map<String, Object>> queryAirticketRetturnReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String saler, String jdsaler, String status, List<Integer> level, String cOrderBy, String uOrderBy, String orderId, UserJob userJob,Long reviewCompanyId, List<Long> subIds) {
		
		String querySql = "SELECT rev.id as revid, rev.productType as prdtype, rev.`status` as revstatus, rev.updateBy as lastoperator,"
				+ " rev.nowLevel as curlevel, rev.flowType as fltype, rev.active as active, rev.createDate as createtime, rev.updateDate as updatetime,"
				+ " a.order_no as orderno, a.id as orderid, a.group_code as groupcode, a.create_date as orderdate, a.agentinfo_id AS agentid,a.salerId as salerId,a.salerName as salerName"
				+ ",a.create_by AS salecreateby, b.createBy AS jidcreateby, b.product_code AS productcode, t.name as tname,t.id as tid, t.payPriceSerialNum as payprice "
				+ "FROM review rev, airticket_order a, activity_airticket b, traveler t "
				+ "WHERE rev.orderId = a.id AND a.airticket_id = b.id AND rev.travelerId = t.id AND rev.review_company_id = " + reviewCompanyId + " "
				+ "AND rev.companyId = "+UserUtils.getUser().getCompany().getId() +" "
				+ "AND rev.productType = 7 AND rev.flowType = 3 ";
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0) {
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if (subIdsStr != null && !"".equals(subIdsStr.trim())) {
			querySql += " and rev.deptId in(" + subIdsStr + ") ";
		}
		if (groupCode != null && !"".equals(groupCode.trim())) {
			querySql += " and a.group_code like '%" + groupCode + "%' ";
		}
		if (orderId != null && !"".equals(orderId.trim())) {
			querySql += " and a.id =" + orderId + " ";
		}
		if (startTime != null && !"".equals(startTime.trim())) {
//			startTime += " 00:00:00";
			querySql += " and rev.createDate > '" + startTime + " 00:00:00'";
		}
		if (endtime != null && !"".equals(endtime)) {
//			endtime += " 00:00:00";
			querySql += " and rev.createDate < '" + endtime + " 23:59:59'";
		}
		if (agent != null && !"".equals(agent.trim())) {
			querySql += " and a.agentinfo_id = " + Long.parseLong(agent);
		}
		if (saler != null && !"".equals(saler.trim())) {
			querySql += " and a.create_by = " + Long.parseLong(saler);
		}
		if (jdsaler != null && !"".equals(jdsaler.trim())) {
			querySql += " and b.createBy = " + Long.parseLong(jdsaler);
		}
		String truesaler = request.getParameter("truesaler");// 销售
		if (truesaler != null && "-99999".equals(truesaler.trim())) {
			truesaler = null;
		}
		if (truesaler != null && !"".equals(truesaler.trim())) {
			querySql += " and a.salerId = '" + Integer.parseInt(truesaler)
					+ "' ";
		}
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
			if(statusInt == 1){
				querySql += " and rev.`status` ='" + status + "' ";//审核状态
				querySql += " and rev.nowLevel = " + level.get(0);//审核层级
			}
			if(statusInt == 5){//审核中的
				querySql += " and rev.`status` ='1' ";//审核状态
			}
			if(statusInt == 0){//查询已驳回的
				querySql += " and rev.`status` ='" + status + "' ";//审核状态
			}
			if(statusInt == 4){//查询已取消的的
				querySql += " and rev.`status` ='" + status + "' ";//审核状态
			}
			//如果为2 审核成功
			if (statusInt == 2){
				querySql += "and (rev.`status` ='2' or rev.`status` ='3')";//审核状态

			}
		}
		
		// 排序条件
		querySql += " ORDER BY 1=1";
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			querySql+=", rev.createDate "+cOrderBy;
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			querySql+=", rev.updateDate "+uOrderBy;
		}
		return findBySql(new Page<Map<String, Object>>(request, response),
				querySql, Map.class);
	}
	
	/**
	 * 查询退票游客审批信息
	 */
	
}
