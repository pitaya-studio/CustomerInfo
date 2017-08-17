package com.trekiz.admin.modules.airticketorder.service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderListDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ActivityInfo;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 机票订单专用
 * @author yakun.bai
 * @Date 2015-9-25
 */
@Service
@Transactional(readOnly = true)
public class AirTicketOrderListServiceImpl implements IAirTicketOrderListService {
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private IAirticketOrderListDao airticketOrderListDao;
	
	@Autowired
    private SystemService systemService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ProductOrderCommonDao productOrderCommonDao;
	@Autowired
	private OfficeDao officeDao;
	
	/**
	 * 根据条件查询订单列表
	 */
//	@Override
//	public Page<Map<String, Object>> queryAirticketOrderListByCond(Page<Map<String,Object>> orderListPage, Map<String, String> condMap) {
//		
//		//2.查询机票订单信息
//		Page<Map<String,Object>> orderPage = airticketOrderListDao.queryAirticketOrderListByCond(orderListPage, condMap);
//		
//		//3.组织航段数据
//		orderPage = organizeAirticketOrderFlightsData(orderPage, orderListPage);
//		
//		return orderPage;
//	}
	
	/**
     * 订单列表查询
     * @param type 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；99 已取消；111 已删除订单
     * @param orderStatus 订单类型：1 单团；2 散拼；3 游学；4 大客户；5 自由行
     * @param page
     * @param travelActivity
     * @param orderBy 排序字段
     * @param map
     * @param common
     * @return
     */
    public Page<Map<Object, Object>> queryOrder(Page<Map<Object, Object>> page, Map<String, String> map, DepartmentCommon common) {
    	
    	//如果是按团期查询则需要把排序值字段改成以团期表名开头字段
    	String orderBy = map.get("orderBy");
    	if ("group".equals(map.get("orderOrGroup")) && orderBy.contains("ao")) {
    		orderBy = orderBy.replace("ao", "aa").replace("update_date", "updateDate");
    	} 
    	page.setOrderBy(orderBy);
    	
    	//sql语句where条件
        String where = this.getWhereSql(map, common);

        //获取订单查询sql语句
        String orderSql = "";
        orderSql = getOrderSql(where);
        map.put("orderSql", orderSql);
        
        //如果是订单查询，则直接返回结果，如果是团期查询则需要再处理sql语句
        if ("order".equals(map.get("orderOrGroup"))) {
        	page = getOrderList(page, orderSql);
        	//航段信息
            page = organizeAirticketOrderFlightsData(page);
		} else {
			page =  this.getGroupListByOrder(page, orderSql);
		}
		return page;
	}

	/**
	 * 获取订单查询where条件
	 * @param type 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；99 已取消；111 已删除订单
	 * @param orderStatus 订单类型：1 单团；2 散拼；3 游学；4 大客户；5 自由行
	 * @param travelActivity
	 * @param map
	 * @param common
	 * @return
	 */
	public String getWhereSql(Map<String,String> map, DepartmentCommon common) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Office office = officeDao.findById(companyId);
		StringBuffer sqlWhere = new StringBuffer("");

		//订单状态：等于0的时候表示查询全部
		String orderState = map.get("showType");
		if (StringUtils.isNotBlank(orderState) && !"0".equals(orderState)) {
			sqlWhere.append(" and ao.order_state = " + orderState + " ");
		} else {
//			//如果是大洋批发商，则默认按订单展示：C322需求，添加时间为2015-11-02 yakun.bai
//			String companyUuid = UserUtils.getUser().getCompany().getUuid();
//			if (companyUuid != null && "7a81a03577a811e5bc1e000c29cf2586".equals(companyUuid)) {
//				sqlWhere.append(" and ao.order_state != 99 and ao.order_state != 111 ");
//			}
			if(office != null) {
				if(office.getIsShowCancelOrder() == 1) {//如果勾选已取消订单
					sqlWhere.append(" and ao.order_state != " + Context.ORDER_PAYSTATUS_YQX + " ");
				}
				if(office.getIsShowDeleteOrder() == 1) {//如果勾选已删除订单
					sqlWhere.append(" and ao.order_state != " + Context.ORDER_PAYSTATUS_DEL + " ");
				}
			}

		}

		//订单编号或团期编号
		String orderNumOrOrderGroupCode = map.get("orderNumOrOrderGroupCode");
		if (StringUtils.isNotBlank(orderNumOrOrderGroupCode) ) {
			sqlWhere.append(" and (ao.order_no like '%" + orderNumOrOrderGroupCode
					+ "%' or aa.group_code like '%" + orderNumOrOrderGroupCode + "%' or aa.product_code like '%" + orderNumOrOrderGroupCode + "%') ");
		}

		//参团订单编号或团期编号
		String joinGroupCodeOrOrderNum = map.get("joinGroupCodeOrOrderNum");
		if (StringUtils.isNotBlank(joinGroupCodeOrOrderNum) ) {
			sqlWhere.append(" and (po.orderNum like '%" + joinGroupCodeOrOrderNum  + "%' or ag.groupCode like '%" + joinGroupCodeOrOrderNum + "%') ");
		}

		//机票类型：1-多段 2-往返 3-单程
		String airType = map.get("airType");
		if (StringUtils.isNotBlank(airType)) {
			sqlWhere.append(" and aa.airType = " + airType + " ");
		}

		//机票区域类型 1-内陆+国际 2-国际 3-国内
		String ticketType = map.get("ticketType");
		if (StringUtils.isNotBlank(ticketType)) {
			sqlWhere.append(" and aa.ticket_area_type = " + ticketType + " ");
		}

		//出发地
		String fromAreaId = map.get("fromAreaId");
		if (StringUtils.isNotBlank(fromAreaId)) {
			sqlWhere.append(" and aa.departureCity = " + fromAreaId + " ");
		}

		//联系人
		String contact = map.get("contact");
		if (StringUtils.isNotBlank(contact)) {
			sqlWhere.append(" and tt.contactsName like '%" + contact + "%' ");
		}

		//目的地ID
		String targetAreaIdList = map.get("targetAreaIdList");
		if (StringUtils.isNotBlank(targetAreaIdList)) {
			sqlWhere.append(" and aa.arrivedCity in (" + targetAreaIdList + ") ");
		}

		//是否参团：0表示没有，1表示参团
		String joinGroup = map.get("joinGroup");
		if (StringUtils.isNotBlank(joinGroup)) {
			if ("0".equals(joinGroup)) {
				sqlWhere.append(" and ao.type = 1 ");
			} else {
				sqlWhere.append(" and ao.type = 2 and po.orderStatus = " + joinGroup + " ");
			}
		}

		//订单ID
		String airticket_id = map.get("airticket_id");
		if (StringUtils.isNotBlank(airticket_id)) {
			sqlWhere.append(" and ao.airticket_id = " + airticket_id + " ");
		}

		//销售
		String saler = map.get("saler");
		if (StringUtils.isNotBlank(saler)) {
			sqlWhere.append(" and ao.salerId = " + saler + " ");
		}

		//下单人
		String picker = map.get("picker");
		if (StringUtils.isNotBlank(picker)) {
			sqlWhere.append(" and ao.create_by = " + picker + " ");
		}

		//计调
		String op = map.get("op");
		if (StringUtils.isNotBlank(op)) {
			sqlWhere.append(" and aasu.name like '%" + op + "%' ");
		}

		//渠道ID
		String agentId = map.get("agentId");
		if (StringUtils.isNotBlank(agentId)) {
			sqlWhere.append(" and ai.id = " + agentId + " ");
		}

		//出发开始日期
		String startAirTime = map.get("startAirTime");
		if (StringUtils.isNotBlank(startAirTime)) {
			sqlWhere.append(" and aa.startingDate >= '" + startAirTime + " 00:00:00' ");
		}

		//出发结束日期
		String endAirTime = map.get("endAirTime");
		if (StringUtils.isNotBlank(endAirTime)) {
			sqlWhere.append(" and aa.startingDate <= '" + endAirTime + " 23:59:59' ");
		}

		//返回开始日期
		String returnStartAirTime = map.get("returnStartAirTime");
		if (StringUtils.isNotBlank(returnStartAirTime)) {
			sqlWhere.append(" and aa.returnDate >= '" + returnStartAirTime + " 00:00:00' ");
		}

		//返回结束日期
		String returnEndAirTime = map.get("returnEndAirTime");
		if (StringUtils.isNotBlank(returnEndAirTime)) {
			sqlWhere.append(" and aa.returnDate <= '" + returnEndAirTime + " 23:59:59' ");
		}

		//发票状态
		String invoiceStatus = map.get("invoiceStatus");
		if (StringUtils.isNotBlank(invoiceStatus)) {
			if("1".equals(map.get("invoiceStatus"))){
				sqlWhere.append(" and not exists(select 1 from orderinvoice oi where ao.id=oi.orderId " +
						" and oi.createStatus = 1 and oi.orderType = 7) ");
			}
			if("2".equals(map.get("invoiceStatus"))){
				sqlWhere.append(" and exists(select 1 from orderinvoice oi where ao.id=oi.orderId " +
						" and oi.createStatus = 1 and oi.orderType = 7) ");
			}
		}

		//收据状态
		String receiptStatus = map.get("receiptStatus");
		if (StringUtils.isNotBlank(receiptStatus)) {
			if("1".equals(map.get("receiptStatus"))){
				sqlWhere.append(" and not exists(select 1 from orderreceipt oi where ao.id=oi.orderId " +
						" and oi.createStatus = 1 and oi.orderType = 7) ");
			}
			if("2".equals(map.get("receiptStatus"))){
				sqlWhere.append(" and exists(select 1 from orderreceipt oi where ao.id=oi.orderId " +
						" and oi.createStatus = 1 and oi.orderType = 7) ");
			}
		}
		//借款：0 全部 1 审批中 2 已借 3 未借 
        String jiekuanStatus = map.get("jiekuanStatus");
        if (StringUtils.isNotBlank(jiekuanStatus)) {
        	String companyUuid = UserUtils.getUser().getCompany().getUuid();
        	if("1".equals(jiekuanStatus)){
        		sqlWhere.append(" and ao.id in(select order_id from review_new where status = 1 and process_type = '" + Context.REVIEW_FLOWTYPE_BORROWMONEY 
        				+ "' and product_type = '7' and company_id = '" + companyUuid + "')");
        	}
        	if("2".equals(jiekuanStatus)){
        		sqlWhere.append(" and ao.id in(select order_id from review_new where status = 2 and process_type = '" + Context.REVIEW_FLOWTYPE_BORROWMONEY 
        				+ "' and product_type = '7' and company_id = '" + companyUuid + "')");
        	}
        	if("3".equals(jiekuanStatus)){
        		sqlWhere.append(" and ao.id not in(select order_id from review_new where (status = 1 or status = 2) and process_type = '" 
        				+ Context.REVIEW_FLOWTYPE_BORROWMONEY + "' and product_type = '7' and company_id = '" + companyUuid + "')");
        	}
        }
		// 是否确认占位
		String confirmOccupy = map.get("confirmOccupy");
		if(StringUtils.isNotBlank(confirmOccupy)) {
			if("1".equals(confirmOccupy)){
				sqlWhere.append(" and ao.seized_confirmation_status = 1 and ao.order_state != 99 and ao.order_state != 111 ");
			}
			if("0".equals(confirmOccupy)){
				sqlWhere.append(" and (ao.seized_confirmation_status <> 1 or isnull(ao.seized_confirmation_status)) and ao.order_state != 99 and ao.order_state != 111 ");
			}
		}
		//分部门显示
		systemService.getDepartmentSql("order", null, sqlWhere, common, Context.ORDER_TYPE_JP);

		//联系人只取机票的联系人
		sqlWhere.append(" and tt.orderType = " + Context.ORDER_TYPE_JP + " ");
		
		//渠道结款方式
		String paymentType = map.get("paymentType");
		if(!StringUtils.isBlank(paymentType)){
			sqlWhere.append(" and ai.id in(select id from agentinfo where paymentType = "+paymentType+") ");
		}
		//200 取出渠道联系人，加入 GROUP BY order_id
		sqlWhere.append(" GROUP BY ao.id ");

		return sqlWhere.toString();
	}

	/**
	 * 设置查询订单sql语句
	 * @param payStatus 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；7 计调占位；99 已取消；111 已删除订单
	 * @param where 订单查询where语句
	 * @return
	 */
	private String getOrderSql(String where) {

		Long companyId = UserUtils.getCompanyIdForData();
		String sql = "SELECT"
				+" DISTINCT ao.id  AS id,"
				+" GROUP_CONCAT(tt.contactsName) contactsName,"
				+" ao.paymentStatus paymentStatus,"
				+" ao.nagentName nagentName,"
				+" ao.order_no orderNo,"
				+" ao.order_state order_state,"
				+" ao.front_money front_money,"
				+" ao.total_money total_money,"
				+" ao.payed_money payed_money,"
				+" ao.accounted_money accounted_money,"
				+" ao.salerId salerId,"
				+" ao.seized_confirmation_status seizedConfirmationStatus,"
				+" ao.salerName salerName,"
				+" maoouter.moneyStr totalMoney,"
				+" maoouter1.moneyStr1 payedMoney,"
				+" maoouter2.moneyStr2 accountedMoney,"
				+" ao.create_date createDate,"
				+" ao.comments comments,"
				+" ao.place_holder_type place_holder_type,"
				+" ao.remaind_days remaindDays,"
				+" ao.person_num personNum,"
				+" ao.product_type_id realOrderType,"
				+" ao.type orderType,"
				+" po.orderStatus orderStatus,"
				+" po.orderPersonName orderPersonName,"
				+" po.orderNum joinOrderNum,"
				+" ag.groupCode joinGroupCode,"
				+" po.id joinOrderId,"
				+" ag.srcActivityId srcActivityId,"
				+" ao.group_code orderGroupCode,"
				+" ao.seen_flag seenFlag,"
				+" ai.id agentId,"
				+" ai.agentName agentName,"
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
				+" ao.confirmationFileId confirmationFileId,"
				+" ao.downloadFileIds downloadFileIds,"
				+" ao.confirmFlag confirmFlag,"
				+" aa.settlementAdultPrice settlementAdultPrice,"
				+" aa.lockStatus activityLockStatus,"
				+" aa.forcastStatus activityForcastStatus,"
				+" cur.currency_mark currencyMark,"
				+" aa.taxamt taxamt,"
				+" aa.isSection isSection,"
				+" aa.createBy activityCreateBy,"
				+" ao.agentinfo_id agentinfoId,"
				+" ao.main_order_id main_order_id,"
				+" TL.traName traName,"
				// 0434需求
				+" review.group_code,"
				+" review.order_no,"
				// 0434需求
				// 0444需求
				+" MAX(inv.applyInvoiceWay) applyInvoiceWay"
				// 0444需求
				+" FROM"
				+" airticket_order ao"
				+" LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id"
				// 0444需求
				+" LEFT JOIN orderinvoice inv ON inv.orderId = ao.id"
				// 0444需求
//				+ (where.contains("contactsName") ? " LEFT JOIN ordercontacts tt ON tt.orderId = ao.id " : "")
				+" LEFT JOIN ordercontacts tt ON tt.orderId = ao.id"
				+" LEFT JOIN sys_user su ON ao.create_by = su.id"
				+" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id"
				+" LEFT JOIN sys_user aasu ON aa.createBy = aasu.id"
				+" LEFT JOIN productorder po ON ao.main_order_id = po.id"
				+" LEFT JOIN activitygroup ag ON po.productGroupId = ag.id"
				+" LEFT JOIN currency cur ON aa.currency_id = cur.currency_id "
				+" LEFT JOIN (select orderId, GROUP_CONCAT(name SEPARATOR ',') traName from traveler where order_type = 7 AND isAirticketFlag = 1 AND (delFlag = 0 OR delFlag = 4 OR delFlag = 5) GROUP BY orderId) TL ON TL.orderId =ao.id"
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
				// 0434需求
				+" LEFT JOIN ("
				+" SELECT ord.group_code,rev.order_no,rev.extend_1"
				+" FROM `review_new` rev LEFT JOIN airticket_order ord"
				+" on rev.order_id = ord.id"
				+" WHERE rev.process_type = '14' AND rev.`status` = 2"
				+" ) review ON review.extend_1 = ao.id"
				// 0434需求

				+" WHERE ao.del_flag = " + Context.DEL_FLAG_NORMAL + " and aa.proCompany = " + UserUtils.getUser().getCompany().getId() + " ";
		return sql + where;
	}

	/**
	 * 查询订单列表
	 * @param page
	 * @param sql
	 * @return
	 */
	private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, String sql) {
		Page<Map<Object, Object>> pageMap = airticketOrderDao.findPageBySql(page, sql, Map.class);
		if(pageMap != null) {
			for(Map<Object, Object> m : pageMap.getList()) {
				Object orderSalerId = m.get("orderSalerId");
				if (orderSalerId != null) {
					m.put("orderSalerId", UserUtils.getUser(StringUtils.toLong(orderSalerId)).getName());
				} else {
					m.put("orderSalerId","");
				}
			}
		}
		return pageMap;
	}

	/**
	 * 根据订单查询条件查询涉及到团期，并根据团期id查询团期
	 * @param page
	 * @param orderSql 订单查询语句
	 * @return
	 */
	private Page<Map<Object, Object>> getGroupListByOrder(Page<Map<Object, Object>> page, String orderSql) {

		StringBuffer sql = new StringBuffer("");
		//获取根据订单查询条件查询团期ids的sql语句
		orderSql = "SELECT distinct aa.id, SUM(IFNULL(ao.confirmFlag, 0)) confirmFlag  " + orderSql.substring(orderSql.indexOf("FROM"));
		orderSql = orderSql.split("LEFT JOIN currency")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE"));

		//查询团期sql
		sql.append("SELECT ")
				.append("aa.id, aa.group_code groupCode, aa.product_code productCode, aa.airType, aa.createBy createBy, ")
				.append("aa.reservationsNum planPosition, aa.freePosition, tempTableLx.confirmFlag ")
				.append("FROM activity_airticket aa, ")
				.append("( "+orderSql+" ) tempTableLx")
				.append(" WHERE ")
				.append("aa.id in ")
				.append("( tempTableLx.id ) GROUP BY aa.id ");
		Page<Map<Object, Object>> pageMap = airticketOrderDao.findBySql(page, sql.toString(), Map.class);

		return pageMap;
	}

	/**
	 * 根据团期IDS查询订单
	 * @param groupList
	 * @param orderSql
	 * @return 订单列表
	 */
	public Page<Map<Object, Object>> findByActivityIds(Page<Map<Object, Object>> page, List<String> groupList, String orderSql) {

		//构造sql语句
		StringBuffer sql = new StringBuffer(orderSql);

		//加入产品IDS限制条件
		StringBuffer groupIdSql = new StringBuffer("");
		for (int i=0;i<groupList.size();i++) {
			if (i != groupList.size()-1) {
				groupIdSql.append(groupList.get(i) + ",");
			} else {
				groupIdSql.append(groupList.get(i));
			}
		}

		//将where条件插入到GROUP BY 语句之前
		int indexOfGroupBy = sql.lastIndexOf("GROUP BY ao.id");
		sql.insert(indexOfGroupBy, " and aa.id in (" + groupIdSql + ") ");
//		sql.append(" and aa.id in (" + groupIdSql + ")");

		//查询
		page.setPageSize(300);
		Page<Map<Object, Object>> pageMap = airticketOrderDao.findPageBySql(page, sql.toString(), Map.class);

		//航段信息
		pageMap = organizeAirticketOrderFlightsData(pageMap);

		return pageMap;
	}

	private Page<Map<Object, Object>> organizeAirticketOrderFlightsData(Page<Map<Object, Object>> orderPage) {

		List<Map<Object, Object>> airticketOrderList = orderPage.getList();

		if (CollectionUtils.isEmpty(orderPage.getList())) {
			return orderPage;
		}

		for(Map<Object, Object> airticketOrderMap : airticketOrderList){

			//机票产品id
			Integer airticketId = (Integer)airticketOrderMap.get("airticketId");
			Integer orderId = (Integer) airticketOrderMap.get("id");
			//航段信息
			List<Map<String, Object>> airticketOrderFlights = airticketOrderDao.queryAirticketOrderFlights(airticketId);
			//附件信息
			List<Map<String,Object>> airticketOrderAttachment = airticketOrderDao.queryAirticketOrderAttachment(orderId);
			//返佣信息 add start by jiangyang 2015-8-4
			String orderNo = (String) airticketOrderMap.get("orderNo");
			List<Map<String, Object>> airticketOrderRebatesList = new ArrayList<Map<String,Object>>();
			airticketOrderRebatesList.addAll(airticketOrderDao.queryAirticketOrderRebates(orderNo));

			List<Map<String, Object>> oldRebatesInf= airticketOrderDao.queryAirticketOrderRebatesInf(orderNo);
			List<Map<String, Object>> newRebatesInf= airticketOrderDao.queryAirticketOrderNewRebatesInf(orderNo);
			String oldRebatesInfMoney = (String) oldRebatesInf.get(0).get("infbt");
			String newRebatesInfMoney = (String) newRebatesInf.get(0).get("infbt");

			String totalInfMoney = "";
			if(StringUtils.isNotBlank(oldRebatesInfMoney) && StringUtils.isNotBlank(newRebatesInfMoney)) {
				totalInfMoney = moneyAmountService.addOrSubtract(oldRebatesInfMoney, newRebatesInfMoney, true);
			} else if(StringUtils.isNotBlank(oldRebatesInfMoney) && StringUtils.isBlank(newRebatesInfMoney)) {
				totalInfMoney = oldRebatesInfMoney;
			} else if(StringUtils.isBlank(oldRebatesInfMoney) && StringUtils.isNotBlank(newRebatesInfMoney)) {
				totalInfMoney = newRebatesInfMoney;
			}

			List<Map<String, Object>> airticketOrderRebatesInf = new ArrayList<>();
			Map<String, Object> tempMap = new HashMap();
			tempMap.put("infbt", totalInfMoney);
			airticketOrderRebatesInf.add(tempMap);
			airticketOrderRebatesList.addAll(airticketOrderRebatesInf);
			airticketOrderMap.put("airticketOrderRebatesList", airticketOrderRebatesList);
			//返佣信息 add end   by jiangyang 2015-8-4
			
			airticketOrderMap.put("airticketOrderFlights", airticketOrderFlights);
			airticketOrderMap.put("airticketOrderAttachmentId", airticketOrderAttachment.size()>0?airticketOrderAttachment.get(0).get("attachmentId"):null);
		}
		
		orderPage.setList(airticketOrderList);
		
		return orderPage;
	}


	/**
	 * 按照团期，查询订单详情
	 * @author gao
	 * @date 20151116
	 * @param ticketGroupID
	 * @return
	 */
	public List<ActivityInfo> getAirticketInfo(Long ticketGroupID){
		List<ActivityInfo> list = new ArrayList<ActivityInfo>();
		List<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>(); // 返回列表
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(" ao.id as id, ");
		sql.append(" ao.order_no as orderNum, ");
		sql.append(" agin.agentName as agentName, ");
		sql.append(" ao.salerName as salerName, ");
		sql.append(" ao.create_by as createBy, ");
		sql.append(" ao.create_date as orderTime, ");
		sql.append(" ao.person_num as orderPersonNum, ");
		sql.append(" ao.order_state as orderType, ");
		sql.append(" ao.total_money as totalMoney, ");
		sql.append(" ao.payed_money as payedMoney, ");
		sql.append(" ao.accounted_money as accountedMoney, ");
		sql.append(" ao.main_order_id as mainOrderId, ");
		sql.append(" ao.type as type, ");
		sql.append(" at.airType as airTypeID, ");
		sql.append(" po.orderStatus as orderStatus ");
		sql.append(" from activity_airticket at,activitygroup ag,agentinfo agin,airticket_order ao ");
		sql.append(" LEFT JOIN productorder po ON ao.main_order_id = po.id ");
		sql.append(" where ao.airticket_id = at.id AND at.id = ag.srcActivityId and ao.agentinfo_id = agin.id and ao.order_state != 99 and ao.order_state != 111");
		sql.append(" AND at.id= "+ticketGroupID);
		sql.append(" order by id desc ");
		List<Map<String,Object>> templist = new ArrayList<Map<String,Object>>();  // 查询数据列表
		try{
			templist = airticketOrderDao.findBySql(sql.toString(),Map.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!templist.isEmpty()){
			// 订单详情写入列表
			for(Map<String,Object> themap:templist){
				ActivityInfo info = new ActivityInfo();
				info.setOrderNo(themap.get("orderNum")!=null?themap.get("orderNum").toString():null);// 订单号
				info.setShell(themap.get("salerName")!=null?themap.get("salerName").toString():null);// 销售名
				info.setAgentName(themap.get("agentName")!=null?themap.get("agentName").toString():null);//渠道
				if(themap.get("createBy")!=null){
					User user = UserUtils.getUser(Long.valueOf(themap.get("createBy").toString()));
					info.setOrderUser(user.getName());// 下单人
				}
				if(themap.get("orderTime")!=null){
					info.setReserveDate(DateUtils.formatCustomDate((Date)themap.get("orderTime"), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));// 预订时间
				}
				info.setPersonNum(themap.get("orderPersonNum")!=null?Integer.valueOf(themap.get("orderPersonNum").toString()):0); // 总人数
				info.setOrderStatus(backOrderStatus(themap.get("orderType")!=null?Integer.valueOf(themap.get("orderType").toString()):99));
				info.setTotalAmountUuid(themap.get("totalMoney")!=null?themap.get("totalMoney").toString():null);
				info.setPayedAmountUuid(themap.get("payedMoney")!=null?themap.get("payedMoney").toString():null);
				info.setAccountedAmountUuid(themap.get("accountedMoney")!=null?themap.get("accountedMoney").toString():null);
				info.setTypeId(themap.get("type")!=null?themap.get("type").toString():null);// 参团类型ID
				info.setMainOrderStatus(themap.get("orderStatus")!=null?themap.get("orderStatus").toString():null);// 主订单状态
				info.setAirTypeID(themap.get("airTypeID")!=null?Integer.valueOf(themap.get("airTypeID").toString()):null);//机票类型ID
				list.add(info);
			}
			// 根据金额uuid，将金额写入返回类型
			if(!list.isEmpty()){
				Iterator<ActivityInfo> iter = list.iterator();
				while(iter.hasNext()){
					ActivityInfo info = iter.next();
					info.setTotalAmount(backMoneyAmount(info.getTotalAmountUuid()));
					info.setPayedAmount(backMoneyAmount(info.getPayedAmountUuid()));
					info.setAccountedAmount(backMoneyAmount(info.getAccountedAmountUuid()));
					info.setAirTypeName(backAirTypeName(info.getAirTypeID()));// 机票类型名称
					info.setTypeName(backTypeName(info.getTypeId(), info.getMainOrderStatus()));
					activityInfoList.add(info);
				}
			}
		}
		return activityInfoList;
	}
	/**
	 * 根据订单状态数值,返回文字
	 * @param orderStatus
	 * @return
	 */
	private String backOrderStatus(Integer orderStatus){
		String str = null;
		if(orderStatus!=null){
			switch(orderStatus){
				case 1:
					str =  "未支付全款";
				break;
				case 2:
					str =  "未支付订金";
				break;
				case 3:
					str =  "已占位";
				break;
				case 4:
					str =  "已支付订金";
				break;
				case 5:
					str =  "已支付全款";
				break;
				case 7:
					str =  "待计调确认";
					break;
				case 8:
					str =  "待财务确认";
					break;
				case 9:
					str =  "已撤销占位";
					break;
				case 99:
					str =  "已取消";
				break;
				case 111:
					str =  "已删除";
					break;
				
			}
		}
		return str;
	}
	
	private String backAirTypeName(Integer airTypeID){
		String str = null;
		if(airTypeID!=null){
			switch(airTypeID){
				case 1:
					str =  "多段 ";
				break;
				case 2:
					str =  "往返";
				break;
				case 3:
					str =  "单程";
				break;
			}
		}
		return str;
	}
	/**
	 * 查询 机票类型名称
	 * @param typeId
	 * @return
	 */
	private String backTypeName(String typeId, String orderStatus){
		if (StringUtils.isBlank(typeId) || StringUtils.isBlank(orderStatus)) {
			return "单办";
		} else {
			return OrderCommonUtil.getChineseOrderType(orderStatus);
		}
		
	}

	/**
	 * 根据uuid，获取多币种字符串
	 * @param uuid
	 * @return
	 */
	private String backMoneyAmount(String uuid){
		if(StringUtils.isBlank(uuid)){
			return "￥0.00";
		}
		String strAmount = moneyAmountService.getMoney(uuid);
		if(StringUtils.isBlank(strAmount)){
			strAmount = "￥0.00";
		}
		return strAmount;
	}
}
