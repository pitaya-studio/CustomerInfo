package com.trekiz.admin.modules.mtourOrder.dao.impl;


import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderMtourDao;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderDetail;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderMoney;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderParam;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderSortInfo;
import com.trekiz.admin.modules.mtourOrder.jsonbean.*;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class AirticketOrderMtourDaoImpl extends BaseDaoImpl<MtourOrderDetail> implements AirticketOrderMtourDao {

	@Override
	public Map<String,Object> getMtourOrderJsonBean(
			MtourOrderParam mtourOrderParam) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<MtourOrderDetail> backList = new ArrayList<MtourOrderDetail>();
		Map<String,Object> back = new HashMap<String,Object>();
		StringBuffer sqlAll = new StringBuffer("select count(DISTINCT back.id) as allCounts FROM"); // 用于查询总数量
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select DISTINCT back.groupCode as groupCode,"
				+ "back.activityID as activityID,"
				+ "back.activity_airticket_name as activity_airticket_name,"
				+ "back.agentName as agentName,"
				+ "back.id as id,"
				+" back.orderNum as orderNum,"
				+ "back.NAME as name,"
				+ "back.lockStatus as lockStatus,"
				+ "back.actLockStatus as actLockStatus,"//产品表中的锁定状态
				+ "back.paymentStatus as paymentStatus,"
				+ "back.front_money as front_money,"
				+ "back.total_money as total_money, " // 订单总金额uuid
				+ "back.payed_money as payed_money,"
				+ "back.accounted_money as accounted_money,"
				+ "DATE_FORMAT(back.create_date,'%Y-%m-%d') as create_date,"
				+ "back.update_date as update_date,"
				+ "back.startingDate as groupOpenDate"
				+ " FROM ");
		StringBuffer sql2 = new StringBuffer();
		sql2.append("(SELECT act.group_code as groupCode,"
				+ " act.id as activityID,"
				+ " act.activity_airticket_name as activity_airticket_name,"
				+ " actorder.id as id,"
				+ " actorder.order_no as orderNum,"
				+ "`user`.`name` as `name`,"
				+ " actorder.`lockStatus` as lockStatus,"
				+ " act.`lockStatus` as actLockStatus,"
				+ " actorder.`paymentStatus` as paymentStatus,"
				+ " actorder.front_money as front_money, " // 定金金额uuid
				+ " actorder.total_money as total_money, " // 订单总金额uuid
				+ " actorder.payed_money as payed_money, " // 已付金额 使用UUID
				+ " actorder.accounted_money as accounted_money, " // 到账金额 使用UUID
				+ " actorder.create_date as create_date," // 下单时间
				+ " actorder.update_date as update_date," // 最后修改时间
				+ " act.startingDate as startingDate, "// 出团日期
				+ " actorder.nagentName as agentName,"  // 订单列表中的渠道名称
				//+ " (SELECT agentName from `agentinfo` where id=actorder.agentinfo_id) as agentName," //渠道名称
				+ " (SELECT id FROM `agentinfo`  WHERE id=actorder.agentinfo_id) AS agentID ");// 渠道ID
			
		// 搜索类型
		String searchType = mtourOrderParam.getSearchType();
		String searchKey = mtourOrderParam.getSearchKey();
		if(StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)){
			if(Integer.valueOf(searchType)==4){
				sql2.append(" FROM `activity_airticket` act,") // 机票产品表
			       .append(" `airticket_order` actorder,")// 机票订单表
			       .append(" `sys_user` `user`")// 用户表
				   .append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
				   .append("  AND act.proCompany = ").append(companyId);
			}else if(Integer.valueOf(searchType)>0 && Integer.valueOf(searchType)<=3 ||Integer.valueOf(searchType)==5){ // 查询条件中，各表关联键均为一一对应
				sql2.append(" FROM `activity_airticket` act,") // 机票产品表
						.append(" `airticket_order` actorder,")// 机票订单表
						.append(" `sys_user` `user`")// 用户表
						.append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
						.append("  AND act.proCompany = ").append(companyId);
				if("1".equals(searchType)){ // 团号
					sql2.append(" and act.group_code like '"+"%"+searchKey+"%'");
				}else if("2".equals(searchType)){ // 订单号
					sql2.append(" and actorder.order_no like '"+"%"+searchKey+"%'");
				}else if("3".equals(searchType)){ // 产品名称
					sql2.append(" and act.activity_airticket_name like '"+"%"+searchKey+"%'");
				}else if("5".equals(searchType)){ // 下单人
					sql2.append(" and user.`name` like '"+"%"+searchKey+"%'");
				}
			}else if(Integer.valueOf(searchType)>5 && Integer.valueOf(searchType)<=8){ // 查询条件中，PNR，航空公司，航段名称 三表和订单关系均为多对一。
				if("6".equals(searchType)){ // PNR
					sql2.append(" FROM `activity_airticket` act,") // 机票产品表
							.append(" `airticket_order` actorder,")// 机票订单表
							.append(" `sys_user` `user`,")// 用户表
							.append(" airticket_order_pnr pnr ") //PNR表
							.append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
							.append("  AND actorder.id = pnr.airticket_order_id AND act.proCompany = ").append(companyId)
					        .append(" AND pnr.flight_pnr like '%").append(searchKey).append("%'");
				}else if("7".equals(searchType)){ // 航空公司表
					sql2.append(" FROM `activity_airticket` act,") // 机票产品表
						.append(" `airticket_order` actorder,")// 机票订单表
						.append(" `sys_user` `user`,")// 用户表
						.append("  airticket_order_pnr pnr, ") //PNR表
						.append("  `sys_airline_info` sysairinfo ") //PNR表
						.append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
						.append("  AND pnr.airline = sysairinfo.airline_code ")
						.append("  AND actorder.id = pnr.airticket_order_id AND act.proCompany = ").append(companyId)
						.append("  AND sysairinfo.airline_name like '%").append(searchKey).append("%' ");
				}else if("8".equals(searchType)){ // 航段表
					sql2.append(" FROM `activity_airticket` act,") // 机票产品表
						.append(" `airticket_order` actorder,")// 机票订单表
						.append(" `sys_user` `user`,")// 用户表
						.append(" airticket_order_pnr_airline airline ") //PNR航段表
						.append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
						.append("  AND actorder.id = airline.airticket_order_id AND act.proCompany = ").append(companyId)
						.append("  AND airline.airline_name like '%").append(searchKey).append("%' ");
				}
			}
		}else{
			sql2.append(" FROM `activity_airticket` act,") // 机票产品表
				.append(" `airticket_order` actorder,")// 机票订单表
				.append(" `sys_user` `user` ")// 用户表
				.append(" WHERE actorder.`airticket_id` = act.`id` AND actorder.`create_by`=user.`id` ")
				.append("  AND act.proCompany = ").append(companyId);
		}
		// 指定，只能察看到当前用户/产品创建人/产品操作人创建的订单
		User currentUser = UserUtils.getUser();
		Long myselfID = currentUser.getId();
		//当前用户是否拥有管理员角色
		boolean isManager = UserUtils.hasRole(currentUser, Context.ROLE_TYPE_MANAGER);
		//当前用户是否拥有销售主管角色
		boolean isSalerManager = UserUtils.hasRole(currentUser, Context.ROLE_TYPE_SALES_EXECUTIVE);
		// 需求 0220 update by shijun.liu 2016.06.15
		/**
		 * 管理员能看到所有人的订单, 销售主管能看到所有销售的订单
		 * 注：下订单时，只有销售不选择时，计调主管看不到，否则计调主管和管理员看到的数据信息是一样的.
		 */
		if(isManager){
			//不做任何处理
		}else {
			//默认是下单人，或者销售，产品操作人才可以看到当前订单
			sql2.append(" AND (actorder.create_by = ").append(myselfID); // 当前用户
			if(UserUtils.isMtourUser()){
				sql2.append(" OR FIND_IN_SET('").append(myselfID).append("', actorder.salerName)") // 销售
				    .append(" OR FIND_IN_SET('").append(myselfID).append("', act.operator) "); // 产品操作人
			}else if(UserUtils.isHuaerUser()){
				sql2.append(" OR act.createBy = '").append(myselfID).append("' ") // 销售
					.append(" OR act.operator = '").append(myselfID).append("' "); // 产品操作人
			}
			if(isSalerManager){
				sql2.append(" OR EXISTS (SELECT userId FROM sys_user_role r WHERE r.userId = actorder.create_by ")
					.append("             AND EXISTS (SELECT id FROM sys_role t WHERE t.delFlag = 0 AND t.id = r.roleId ")
					.append("    			AND t.roleType = ").append(Context.ROLE_TYPE_SALES).append(" AND t.companyId = ")
					.append(companyId).append(") ) ");
			}
			sql2.append(" ) ");
		}

		String[] channelUuid = mtourOrderParam.getChannelUuid();// 渠道
		String[] ordererId = mtourOrderParam.getOrdererId();	// 下单人
		String[] orderStatusCode = mtourOrderParam.getOrderStatusCode(); // 订单状态
		String[] receiveStatusCode = mtourOrderParam.getReceiveStatusCode(); // 收款状态
		
		//下单日期查询条件
		if(StringUtils.isNotBlank(mtourOrderParam.getCreateDateStart())){
			sql2.append(" and actorder.create_date >= "+"'" + mtourOrderParam.getCreateDateStart() + " 00:00:00'"); 
			
		}
		if(StringUtils.isNotBlank(mtourOrderParam.getCreateDateEnd())){
			sql2.append(" and actorder.create_date <= "+"'" + mtourOrderParam.getCreateDateEnd() + " 23:59:59'");
		}

		if(ordererId!=null && ordererId.length>0){
			sql2.append(" and user.`id` in ( ");
			int size = ordererId.length;
			int index = 0;
			for(String chan:ordererId){
				sql2.append( chan );
				if(index<(size-1)){
					sql2.append(",");
				}
				index++;
			}
			sql2.append(" ) ");
		}
		if(orderStatusCode!=null && orderStatusCode.length>0){
			sql2.append(" and actorder.`lockStatus` in ( ");
			int size = orderStatusCode.length;
			int index = 0;
			for(String chan:orderStatusCode){
				sql2.append( chan );
				if(index<(size-1)){
					sql2.append(",");
				}
				index++;
			}
			sql2.append(" ) ");
		}
		if(receiveStatusCode!=null && receiveStatusCode.length>0){
			sql2.append(" and actorder.`paymentStatus` in ( ");
			int size = receiveStatusCode.length;
			int index = 0;
			for(String chan:receiveStatusCode){
				sql2.append( chan );
				if(index<(size-1)){
					sql2.append(",");
				}
				index++;
			}
			sql2.append(" ) ");
		}
		sql2.append("  ) back where 1=1");
		
		if("4".equals(searchType)&&StringUtils.isNotBlank(searchKey)){ // 按渠道名称模糊查询，因这个查询条件在最外层，所以searcheType = 4 的情况在这里。
			sql2.append(" and back.agentName like '"+"%"+searchKey+"%' ");
		}
		if(channelUuid!=null && channelUuid.length>0){
			sql2.append(" and back.agentID in ( ");
			int size = channelUuid.length;
			int index = 0;
			for(String chan:channelUuid){
				sql2.append( chan );
				if(index<(size-1)){
					sql2.append(",");
				}
				index++;
			}
			sql2.append(" ) ");
		}
		// 条件排序
		StringBuffer sqlDec = new StringBuffer(" order by ");
		if(StringUtils.isNotBlank(mtourOrderParam.getSortKey())){
			if(MtourOrderSortInfo.SORTKEY_MODIFIEDDATETIME.equals(mtourOrderParam.getSortKey())){
				sqlDec.append(" back.update_date "); // 更新时间
			}else if(MtourOrderSortInfo.SORTKEY_DEPARTUREDATETIME.equals(mtourOrderParam.getSortKey())){
				sqlDec.append(" back.startingDate "); // 出团日期
			}else{
				sqlDec.append(" back.create_date "); // 创建时间
			}
		}
		if(mtourOrderParam.getDec()){ 
			sqlDec.append(" desc ");	// 倒序
		}else{
			sqlDec.append(" asc "); // 顺序
		}
		// 分页条件
		StringBuffer sql3 = new StringBuffer();
		String currentIndex = mtourOrderParam.getCurrentIndex();// 当前页码'
		String rowCount = mtourOrderParam.getRowCount(); // 每页总行数'
		if(StringUtils.isNotBlank(currentIndex)&&StringUtils.isNotBlank(rowCount)){
			int current = Integer.parseInt(currentIndex);
			int row = Integer.parseInt(rowCount);
			if(row>1 && current>1){ // 每页行数大于1，当前页码非首页
				sql3.append(" limit "+(current-1)*row+","+row);
			}else if(row>1 && current==1){// 每页行数大于1，当前页码为首页
				sql3.append(" limit 0,"+row);
			}else{ // 其他情况统一为只显示第一条
				sql3.append(" limit 0,1");
			}
		}

		List<Map<String,Object>> templist = new ArrayList<Map<String,Object>>();  // 查询数据列表
		try{
			templist = findBySql(sql1.toString()+sql2.toString()+sqlDec.toString()+sql3.toString(), Map.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<Map<String,Object>> tempCount = new ArrayList<Map<String,Object>>();  // 查询数据总量
		try{
			tempCount = findBySql(sqlAll.toString()+sql2.toString(), Map.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!templist.isEmpty()){
			for(Map<String,Object> map : templist){
				MtourOrderDetail detail = new MtourOrderDetail();
				if(map.get("groupCode")!=null && StringUtils.isNotBlank(map.get("groupCode").toString())){
					detail.setGroupNo(map.get("groupCode").toString());
				}
				if(map.get("activity_airticket_name")!=null && StringUtils.isNotBlank(map.get("activity_airticket_name").toString())){
					detail.setProductName(map.get("activity_airticket_name").toString());
				}
				if(map.get("orderNum")!=null && StringUtils.isNotBlank(map.get("orderNum").toString())){
					detail.setOrderNum(map.get("orderNum").toString());			
				}
				if(map.get("id")!=null && StringUtils.isNotBlank(map.get("id").toString())){
					detail.setOrderUuid(map.get("id").toString());			
				}
				if(map.get("agentName")!=null && StringUtils.isNotBlank(map.get("agentName").toString())){
					detail.setChannelName(map.get("agentName").toString());
				}
				if(map.get("name")!=null && StringUtils.isNotBlank(map.get("name").toString())){
					detail.setOrderer(map.get("name").toString());
				}
				if(map.get("lockStatus")!=null && StringUtils.isNotBlank(map.get("lockStatus").toString())){
					detail.setOrderStatusCode(map.get("lockStatus").toString());
					detail.setOrderStatus(backOrderStatus(map.get("lockStatus").toString()));
				}
				if(map.get("actLockStatus") == null){
					detail.setLockStatus("0");
				}else{
					detail.setLockStatus(map.get("actLockStatus").toString());
				}
				if(map.get("paymentStatus")!=null && StringUtils.isNotBlank(map.get("paymentStatus").toString())){
					detail.setReceiveStatusCode(map.get("paymentStatus").toString());
					detail.setReceiveStatus(backReceiveStatus(map.get("paymentStatus").toString()));
				}
				if(map.get("front_money")!=null && StringUtils.isNotBlank(map.get("front_money").toString())){
					detail.setDepositUuid(map.get("front_money").toString());
				}
				if(map.get("total_money")!=null && StringUtils.isNotBlank(map.get("total_money").toString())){
					detail.setFullPaymentUuid(map.get("total_money").toString());
				}
				if(map.get("payed_money")!=null && StringUtils.isNotBlank(map.get("payed_money").toString())){
					detail.setReceivedAmountUuid(map.get("payed_money").toString());
				}
				if(map.get("accounted_money")!=null && StringUtils.isNotBlank(map.get("accounted_money").toString())){
					detail.setArrivedAmountUuid(map.get("accounted_money").toString());
				}
				if(map.get("create_date")!=null && StringUtils.isNotBlank(map.get("create_date").toString())){
					detail.setOrderDateTime((String)map.get("create_date"));
				}
				if(map.get("update_date")!=null && StringUtils.isNotBlank(map.get("update_date").toString())){
					detail.setModifiedDateTime(DateUtils.formatCustomDate((Date)map.get("update_date"), "yyyy-MM-dd HH:mm:ss"));
				}
				if(map.get("groupOpenDate")!=null && StringUtils.isNotBlank(map.get("groupOpenDate").toString())){
					detail.setDepartureDate(DateUtils.formatCustomDate((Date)map.get("groupOpenDate"), "yyyy-MM-dd HH:mm:ss"));
				}
				if(map.get("activityID")!=null && StringUtils.isNotBlank(map.get("activityID").toString())){
					detail.setProductId(map.get("activityID").toString());
				}
				backList.add(detail);
			}
			// 数据总量
			Integer allCounts = new Integer(0);
			if(tempCount!=null && !tempCount.isEmpty()){
				for(Map<String,Object> map : tempCount){
					if(map.get("allCounts")!=null && StringUtils.isNotBlank(map.get("allCounts").toString())){
						allCounts=Integer.valueOf(map.get("allCounts").toString());
					}
				}
			}
			back.put("backList", backList); // 导入数据列表
			back.put("allCounts", allCounts); // 导入数据总额
		}
		
		return back;
	}
	/**
	 * 根据订单状态code，返回状态名称
	 * @author gao
	 * 2015年10月22日
	 * @param orderStatusCode
	 * @return
	 */
	private String backOrderStatus(String orderStatusCode){
		if(StringUtils.isBlank(orderStatusCode)){
			return "";
		}
		if("0".equals(orderStatusCode)){
			return "已生成";
		}else if("1".equals(orderStatusCode)){
			return "锁定";
		}else if("2".equals(orderStatusCode)){
			return "已取消";
		}else if("3".equals(orderStatusCode)){
			return "草稿";
		}else if("4".equals(orderStatusCode)){
			return "待确认";
		}else{
			return "";
		}
	}
	/**
	 * 根据收款状态code，返回状态名称 支付状态 美途项目新增（101：代收款，102：部分定金，103：已收定金，104：已收全款）
	 * @author gao
	 * 2015年10月22日
	 * @param receiveStatus
	 * @return
	 */
	private String backReceiveStatus(String receiveStatus){
		if(StringUtils.isBlank(receiveStatus)){
			return "";
		}
		if("101".equals(receiveStatus)){
			return "待收款";
		}else if("102".equals(receiveStatus)){
			return "部分定金";
		}else if("103".equals(receiveStatus)){
			return "已收定金";
		}else if("104".equals(receiveStatus)){
			return "已收全款";
		}else{
			return "";
		}
	}
	
	/**
	 * 根据查询信息获取美途付款列表-订单列表数据集合
	 * @Description: 
	 * @param @param orderSearch
	 * @param @return   
	 * @return List<MtourOrderDetail>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public Page<Map<String, Object>> getMtourOrderList(MtourOrderSearchJsonBean orderSearch) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" airOrder.id AS orderUuid, ");
		sql.append(" airOrder.group_code AS groupNo, ");
		sql.append(" airOrder.create_date AS orderDateTime, ");
		sql.append(" user.name AS orderer, ");
		sql.append(" airOrder.refund_flag AS financePayOrderListPayStatusCode, ");
		sql.append(" CASE  ");
		sql.append(" WHEN airOrder.refund_flag = 0 THEN '未付全款' ");
		sql.append(" WHEN airOrder.refund_flag = 1 THEN '已付全款' ");
		sql.append(" END  ");
		sql.append(" AS financePayOrderListPayStatusName, ");
		sql.append(" airticket.lockStatus AS lockStatus, ");
		sql.append(" airticket.startingDate AS departureDate ");
		sql.append(" FROM airticket_order airOrder ");
		sql.append(" LEFT JOIN activity_airticket airticket ON airticket.id = airOrder.airticket_id ");
		sql.append(" LEFT JOIN sys_user user ON user.id = airOrder.create_by ");
		sql.append(" WHERE airOrder.del_flag=? ");
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 指定，只能察看到当前用户/产品创建人/产品操作人创建的订单
		/*Long userId = UserUtils.getUser().getId();
		sql.append(" AND (airOrder.create_by = "+userId); // 订单创建人
		sql.append(" OR airticket.createBy = "+userId); // 产品创建人
		sql.append(" OR airticket.operator = "+userId+") "); // 产品操作人
*/		sql.append(" AND user.companyId = ").append(companyId);
		
		if(orderSearch.getSearchParam() != null) {
			OrderSearchParam searchParam = orderSearch.getSearchParam();
			if("1".equals(searchParam.getSearchType()) && StringUtils.isNotBlank(searchParam.getSearchKey())) {//团号
				sql.append(" AND airOrder.group_code LIKE '%").append(searchParam.getSearchKey()).append("%' ");
			} else if("3".equals(searchParam.getSearchType()) && StringUtils.isNotBlank(searchParam.getSearchKey())){//产品名称
				sql.append(" AND airticket.activity_airticket_name LIKE '%").append(searchParam.getSearchKey()).append("%' ");
			}
		}
		
		if(orderSearch.getFilterParam() != null) {
			OrderFilterParam filterParam = orderSearch.getFilterParam();
			if(StringUtils.isNotEmpty(filterParam.getOrderDateTime())) {//下单日期
				String createDate = filterParam.getOrderDateTime();
				String createDateStart = createDate.substring(0, createDate.indexOf("~"));
				String createDateEnd = createDate.substring(createDate.indexOf("~")+1, createDate.length());
				if(StringUtils.isNotEmpty(createDateStart)) {
					sql.append(" AND airOrder.create_date >= '").append(createDateStart).append(" 00:00:00' ");
				}
				if(StringUtils.isNotEmpty(createDateEnd)) {
					sql.append(" AND airOrder.create_date <= '").append(createDateEnd).append(" 23:59:59' ");
				}
			}
			
			if(StringUtils.isNotEmpty(filterParam.getDepartureDate())) {//出团日期
				String departureDate = filterParam.getDepartureDate();
				String departureDateStart = departureDate.substring(0, departureDate.indexOf("~"));
				String departureDateEnd = departureDate.substring(departureDate.indexOf("~")+1, departureDate.length());
				if(StringUtils.isNotEmpty(departureDateStart)) {
					sql.append(" AND airticket.startingDate >= '").append(departureDateStart).append(" 00:00:00' ");
				}
				if(StringUtils.isNotEmpty(departureDateEnd)) {
					sql.append(" AND airticket.startingDate <= '").append(departureDateEnd).append(" 23:59:59' ");
				}
			}
			
			if(StringUtils.isNotEmpty(filterParam.getOrdererId())) {//'下单人Id'
				sql.append(" AND airOrder.create_by in (");
				for(String createBy : filterParam.getOrdererId().split(",")) {
					sql.append(createBy);
					sql.append(",");
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
			}
			
			if(StringUtils.isNotEmpty(filterParam.getPayment_orderPaymentStatusCode())) {//['订单付款状态']//1-已付全款，0-未付全款
				sql.append(" AND airOrder.refund_flag in (");
				for(String refundFlag : filterParam.getPayment_orderPaymentStatusCode().split(",")) {
					sql.append(refundFlag);
					sql.append(",");
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
			}

			//PNR 条件过滤，  add by shijun.liu 2016.06.12 0268需求
			if(StringUtils.isNotEmpty(filterParam.getPnrValue())) {
				sql.append(" AND EXISTS (SELECT flight_pnr FROM airticket_order_pnr t WHERE t.code_type = 0")
				   .append(" AND t.airticket_order_id = airOrder.id AND (");
				for(String pnr : filterParam.getPnrValue().split(",")) {
					sql.append(" t.flight_pnr LIKE '%").append(pnr).append("%' OR");
				}
				sql.delete(sql.length()-2, sql.length());
				sql.append(") ) ");
			}
		}

		//排序字段
		StringBuffer sortSql = new StringBuffer();
		if(orderSearch.getSortInfo() != null) {
			OrderSortInfo sortInfo = orderSearch.getSortInfo();
			if(StringUtils.isNotEmpty(sortInfo.getSortKey())) {
				if("orderDateTime".equals(sortInfo.getSortKey())) {
					sortSql.append(" ORDER BY airOrder.create_date ");
				} else if("departureDate".equals(sortInfo.getSortKey())) {
					sortSql.append(" ORDER BY airticket.startingDate ");
				}
				if(sortInfo.getDec()) {
					sortSql.append(" desc ");
				} else {
					sortSql.append(" asc ");
				}
			}
		}
		
		//分页默认值
		Integer currentIndex = 1;//'当前页码'
		Integer rowCount = 20;//'每页总行数'
		if(orderSearch.getPageParam() != null) {
			OrderPageParam pageParam = orderSearch.getPageParam();
			if(pageParam.getCurrentIndex() != null) {
				currentIndex = pageParam.getCurrentIndex();
			}
			if(pageParam.getRowCount() != null) {
				rowCount = pageParam.getRowCount();
			}
		}
		
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(currentIndex, rowCount);
		Page<Map<String, Object>> results = super.findBySql(page, sql.append(sortSql).toString(), Map.class, BaseEntity.DEL_FLAG_NORMAL);
		
		return results;
	}
	
	/**
	 * 根据订单id获取订单的应付金额
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	@SuppressWarnings("unchecked")
	public List<MtourOrderMoney> getPayableAmountByOrderId(Long orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.currencyUuid,SUM(a.amount) AS amount FROM ( ");
		sql.append(" SELECT currency_id AS currencyUuid,amount FROM airticket_order_moneyAmount montyAmount ");
		sql.append(" WHERE montyAmount.status='1' AND montyAmount.airticket_order_id=? ");
		sql.append(" UNION 	ALL ");
		sql.append(" SELECT currencyId AS currencyUuid,price*quantity AS amount FROM cost_record costRecord WHERE ");
		sql.append(" costRecord.orderType=7 AND costRecord.payStatus in(?,?,?) AND costRecord.orderId=? and budgetType <> 2 ");
		sql.append(" ) a GROUP BY a.currencyUuid ");
		return (List<MtourOrderMoney>) super.findCustomObjBySql(sql.toString(), MtourOrderMoney.class, orderId,
				CostRecord.PAY_STATUS_PENDING, CostRecord.PAY_STATUS_ALREADY, CostRecord.PAY_STATUS_SUBMIT, orderId);
	}
	
	/**
	 * 根据订单id获取订单的已付金额
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return List<MtourOrderMoney>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	@SuppressWarnings("unchecked")
	public List<MtourOrderMoney> getPaidAmountByOrderId(Long orderId) {
		StringBuffer sql = new StringBuffer();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		sql.append("SELECT a.currency_id AS currencyUuid, SUM(a.amount) AS amount FROM ( ");
		sql.append(" (SELECT moneyAmount.currency_id,moneyAmount.amount FROM airticket_order_moneyAmount moneyAmount ");
		sql.append(" LEFT JOIN refund refund ON refund.money_serial_num = moneyAmount.serialNum ");
		sql.append(" LEFT JOIN cost_record costRecord ON costRecord.id = refund.record_id ");
		sql.append(" WHERE refund.moneyType=1 AND costRecord.orderId = ? AND costRecord.payStatus not in(2,4)  ");
		sql.append(" AND refund.status IS NULL AND refund.companyUuid='").append(companyUuid).append("' )");
		sql.append(" UNION ALL ");
		sql.append(" (SELECT moneyAmount.currency_id,moneyAmount.amount FROM airticket_order_moneyAmount moneyAmount ");
		sql.append(" LEFT JOIN refund refund ON refund.money_serial_num = moneyAmount.serialNum ");
		sql.append(" LEFT JOIN airticket_order_moneyAmount moneyRecord ON moneyRecord.id = refund.record_id ");
		sql.append(" WHERE refund.moneyType = 2 AND moneyRecord.moneyType=2 AND moneyRecord.airticket_order_id=? ");
		sql.append(" AND moneyRecord.status=1 AND refund.status IS NULL AND refund.companyUuid='").append(companyUuid).append("' )");
		sql.append(" UNION ALL  ");
		sql.append(" (SELECT moneyAmount.currency_id,moneyAmount.amount FROM airticket_order_moneyAmount moneyAmount ");
		sql.append(" LEFT JOIN refund refund ON refund.money_serial_num = moneyAmount.serialNum ");
		sql.append(" LEFT JOIN airticket_order_moneyAmount moneyRecord ON moneyRecord.id = refund.record_id ");
		sql.append(" WHERE refund.moneyType = 4 AND moneyRecord.moneyType=1 AND moneyRecord.airticket_order_id=? ");
		sql.append(" AND moneyRecord.status=1 AND refund.status IS NULL AND refund.companyUuid='").append(companyUuid).append("' )");
		sql.append(" UNION ALL  ");
		sql.append(" (SELECT moneyAmount.currency_id,moneyAmount.amount FROM airticket_order_moneyAmount moneyAmount ");
		sql.append(" LEFT JOIN refund refund ON refund.money_serial_num = moneyAmount.serialNum ");
		sql.append(" LEFT JOIN airticket_order_moneyAmount moneyRecord ON moneyRecord.id = refund.record_id ");
		sql.append(" WHERE refund.moneyType = 6 AND moneyRecord.moneyType=3 AND moneyRecord.airticket_order_id=? AND ");
		sql.append(" moneyRecord.status=1 AND refund.status IS NULL AND refund.companyUuid='").append(companyUuid).append("' )");
		sql.append(" ) a GROUP BY a.currency_id ");
		return (List<MtourOrderMoney>) super.findCustomObjBySql(sql.toString(), MtourOrderMoney.class, orderId, orderId, orderId, orderId);
	}
	
	@Override
	public Page<Map<String, Object>> getMtourOrderListForReceive(
			MtourOrderSearchJsonBean orderSearch) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append(" SELECT ")
		   .append(" 	o.id AS orderUuid, ")
		   .append(" 	o.group_code AS groupNo, ")
		   .append(" 	o.create_date AS orderDateTime, ")
		   .append(" 	p.startingDate AS departureDate, ")
		   .append(" 	(SELECT NAME FROM sys_user u WHERE u.id = o.create_by) AS orderer, ")
		   .append(" 	ROUND(IFNULL((SELECT sum(amount * exchangerate) FROM money_amount m WHERE m.serialNum = o.total_money), 0), 2) AS orderAmount ")
		   .append(" FROM ")
		   .append(" 	airticket_order o, ")
		   .append(" 	activity_airticket p ")
		   .append(" WHERE ")
		   .append(" 	p.id = o.airticket_id ")
		   .append(" AND o.del_flag = '").append(BaseEntity.DEL_FLAG_NORMAL).append("'")
		   .append(" AND p.proCompany = ").append(companyId);
		List<Map<String, Object>> list = super.findBySql(str.toString(), Map.class);
		
		return null;
	}

}
