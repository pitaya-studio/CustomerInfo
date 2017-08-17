package com.trekiz.admin.modules.order.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
/**
 * 
 * @创建人 yakun.bai
 * @修改人 ruyi.chen
 * @修改时间 2015-2-28下午4:23:59
 * @描述  补充OrderCommonService,提供订单类操作方法
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class OrderStatisticsService  extends BaseService {
    
	@Autowired
	private SystemService systemService;
    @Autowired
    private ProductOrderCommonDao productOrderDao;
    @Autowired
    private AirticketPreOrderDao airticketPreOrderDao;
    @Autowired
    private AirticketActivityReserveDao airticketActivityReserveDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private TravelerDao travelerDao;
    @Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
    @Autowired
	private MoneyAmountService moneyAmountService;
    @Autowired
    private ActivityGroupReserveDao activityGroupReserveDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
    private CurrencyService currencyService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private GroupControlBoardService groupControlBoardService;
	/**
	 * 单团订单统计
	 * @param mapRequest 条件
	 * @param createIds 部门下所有人员
	 * @return
	 */
	public List<Object[]> orderStatistics(Map<String,String> mapRequest, DepartmentCommon common, List<User> salerList) {
		//分部门显示
		StringBuffer sqlWhere = new StringBuffer("");
		//销售查询
		if(StringUtils.isNotBlank(mapRequest.get("salerId"))) {
			sqlWhere.append(" AND pro.createBy = " + mapRequest.get("salerId") + " ");
		} else if(CollectionUtils.isNotEmpty(salerList)) {
			String createByIds = "";
			Iterator<User> it = salerList.iterator();
			int i = 0;
			while(it.hasNext()) {
				if(i == salerList.size() - 1) {
					createByIds += it.next().getId();
				} else {
					createByIds += it.next().getId() + ",";
				}
				i++;
			}
			if(StringUtils.isNotBlank(createByIds)) {
				sqlWhere.append(" AND pro.createBy in (" + createByIds  + ")");
			}
		} else {
			return null;
		}
		
		//订单状态：已占位、未支付订金、未支付全款、已支付订金、已支付全款
		String payStatus = Context.ORDER_PAYSTATUS_WZF + "," + Context.ORDER_PAYSTATUS_DJWZF + "," + 
							Context.ORDER_PAYSTATUS_YZW + "," + Context.ORDER_PAYSTATUS_YZFDJ + "," + Context.ORDER_PAYSTATUS_YZF;
		
		//时间查询
		if(StringUtils.isNotBlank(mapRequest.get("startDate"))) {
			sqlWhere.append(" AND pro.createDate >= '" + mapRequest.get("startDate") + "' ");
		}
		if(StringUtils.isNotBlank(mapRequest.get("endDate"))) {
			sqlWhere.append(" AND pro.createDate <= '" + mapRequest.get("endDate") + "' ");
		}
		
		List<Object[]> orderList = productOrderDao.findBySql(getOrderStatisticsSql(payStatus, sqlWhere, mapRequest.get("orderStatus")));
		List<Object[]> preOrderList = productOrderDao.findBySql(getPreOrderStatisticsSql(sqlWhere));
		
		return getOrderStatisticsData(orderList, preOrderList, common);
	}
	
	/**
	 * 获取单团订单统计查询语句
	 * @param payStatus
	 * @param sqlWhere
	 * @return
	 */
	private String getOrderStatisticsSql(String payStatus, StringBuffer sqlWhere, String orderStatus) {
		StringBuilder sql = new StringBuilder("");
		
		
		//订单和预订单统计sql：按创建人和渠道分组且按创建人和渠道排序
		sql.append("SELECT ")
				.append("IFNULL(depts.deptName,'未分配部门'), orders.name, orders.groupOpenDate, orders.orderCompanyName, orders.acitivityName, IFNULL(orders.orderPersonNum,0), 0, parentIds ")
			.append("FROM( ")
			
				//查询订单统计信息
				.append("SELECT ")
					.append("user.name, user.id userId, pro.productId, pro.productGroupId, pro.createBy, pro.orderCompany, groups.groupOpenDate, pro.orderCompanyName, activity.acitivityName, sum(orderPersonNum) AS orderPersonNum ")
				.append("FROM ")
					.append("productorder pro, sys_user user, activitygroup groups, travelactivity activity ")
				.append("WHERE ")
					.append("pro.createBy = user.id AND ")
					.append("pro.payStatus in (" + payStatus + ") AND ")
					.append("pro.productGroupId = groups.id AND ")
					.append("pro.productId = activity.id AND ")
					.append("groups.srcActivityId = activity.id AND ")
					.append("pro.orderStatus = " + orderStatus + " AND ")
					.append("user.companyId = " + UserUtils.getUser().getCompany().getId() + " AND ")
					.append("activity.proCompany = " + UserUtils.getUser().getCompany().getId() + " ")
					.append(sqlWhere)
				.append("GROUP BY pro.createBy, pro.productGroupId, pro.productId, pro.orderCompany ")
				.append("ORDER BY pro.createBy, pro.orderCompany ")
				.append(") orders ")
				
				//左连接查询用户所在部门
				.append("LEFT JOIN ( ")
				
				//查询用户所在部门
				.append("SELECT ")
					.append("GROUP_CONCAT(dept.NAME) deptName, GROUP_CONCAT(dept.parent_ids) parentIds, user.id userId ")
				.append("FROM ")
					.append("department dept, sys_role role, sys_user user, sys_user_role userRole ")
				.append("WHERE ")
					.append("userRole.roleId = role.id AND ")
					.append("userRole.userId = user.id AND ")
					.append("role.deptId = dept.id ")
				.append("GROUP BY userId ")
				.append(")depts ")
				
				//左连接查询关联条件
				.append("ON ")
				.append("depts.userId = orders.userId order by depts.deptName");
		return sql.toString();
	}
	
	/**
	 * 获取单团预报名订单统计查询语句
	 * @param sqlWhere
	 * @return
	 */
	private String getPreOrderStatisticsSql(StringBuffer sqlWhere) {
		StringBuilder sql = new StringBuilder("");
		
		
		//订单和预订单统计sql：按创建人和渠道分组且按创建人和渠道排序
		sql.append("SELECT ")
				.append("IFNULL(depts.deptName,'未分配部门'), orders.name, orders.groupOpenDate, orders.orderCompanyName, orders.acitivityName, 0, IFNULL(orders.orderPersonNum, 0), parentIds ")
			.append("FROM( ")
			
				//查询订单统计信息
				.append("SELECT ")
					.append("user.name, user.id userId, pro.productId, pro.productGroupId, pro.createBy, pro.orderCompany, groups.groupOpenDate, pro.orderCompanyName, activity.acitivityName, sum(orderPersonNum) AS orderPersonNum ")
				.append("FROM ")
					.append("preproductorder pro, sys_user user, activitygroup groups, travelactivity activity ")
				.append("WHERE ")
					.append("pro.createBy = user.id AND ")
					.append("pro.orderType = 0 AND ")
					.append("pro.productGroupId = groups.id AND ")
					.append("pro.productId = activity.id AND ")
					.append("groups.srcActivityId = activity.id AND ")
					.append("user.companyId = " + UserUtils.getUser().getCompany().getId() + " AND ")
					.append("activity.proCompany = " + UserUtils.getUser().getCompany().getId() + " ")
					.append(sqlWhere)
				.append("GROUP BY pro.createBy, pro.productGroupId, pro.productId, pro.orderCompany ")
				.append("ORDER BY pro.createBy, pro.orderCompany ")
				.append(") orders ")
				
				//左连接查询用户所在部门
				.append("LEFT JOIN ( ")
				
				//查询用户所在部门
				.append("SELECT ")
					.append("GROUP_CONCAT(dept.NAME) deptName, GROUP_CONCAT(dept.parent_ids) parentIds, user.id userId ")
				.append("FROM ")
					.append("department dept, sys_role role, sys_user user, sys_user_role userRole ")
				.append("WHERE ")
					.append("userRole.roleId = role.id AND ")
					.append("userRole.userId = user.id AND ")
					.append("role.deptId = dept.id ")
				.append("GROUP BY userId ")
				.append(")depts ")
				
				//左连接查询关联条件
				.append("ON ")
				.append("depts.userId = orders.userId order by depts.deptName");
		return sql.toString();
	}
	
	/**
	 * 获取单团统计结果
	 * @param orderList
	 * @param preOrderList
	 * @return
	 */
	private List<Object[]> getOrderStatisticsData(List<Object[]> orderList, List<Object[]> preOrderList, DepartmentCommon common) {
		if(CollectionUtils.isNotEmpty(orderList)) {
			for(Object[] order : orderList) {
				setDeptName(order, common);
				Iterator<Object[]> it = preOrderList.iterator();
				while(it.hasNext()) {
					Object[] preOrder = it.next();
					if(order[0].toString().equals(preOrder[0].toString()) && 
							order[1].toString().equals(preOrder[1].toString()) && 
							order[2].toString().equals(preOrder[2].toString()) && 
							order[3].toString().equals(preOrder[3].toString()) && 
							order[4].toString().equals(preOrder[4].toString())) {
						order[6] = preOrder[6];
						it.remove();
					}
				}
			}
			if(CollectionUtils.isNotEmpty(preOrderList)) {
				for(Object[] order : preOrderList) {
					setDeptName(order, common);
				}
			}
			orderList.addAll(preOrderList);
		} else {
			if(CollectionUtils.isNotEmpty(preOrderList)) {
				for(Object[] order : preOrderList) {
					setDeptName(order, common);
				}
			}
			orderList.addAll(preOrderList);
		}
		return orderList;
	}

	/**
	 * 把子部门部门名称改为父名称
	 * @param order
	 * @param common
	 */
	private void setDeptName(Object[] order, DepartmentCommon common) {
		Set<Department> deptList = common.getShowAreaList();
		Set<Department> deptSet = Sets.newHashSet();
		if(CollectionUtils.isNotEmpty(deptList) && StringUtils.isNotBlank(common.getDepartmentId())) {
			Department parentDept = departmentDao.findOne(Long.parseLong(common.getDepartmentId()));
			deptSet.add(parentDept);
			if(CollectionUtils.isNotEmpty(parentDept.getChildList())) {
				for(Department dept : parentDept.getChildList()) {
					deptSet.add(dept);
				}
			}
		} else {
			if(StringUtils.isNotBlank(common.getDepartmentId())) {
				Department dept = departmentDao.findOne(Long.parseLong(common.getDepartmentId()));
				deptSet.add(dept);
			} 
		}
		Iterator<Department> deptIt = deptSet.iterator();
		while(deptIt.hasNext()) {
			Department dept = deptIt.next();
			if(!dept.getId().toString().equals(common.getDepartmentId()) && 
					order[7] != null && order[7].toString().contains(dept.getId().toString())) {
				order[0] = dept.getName();
				break;
			}
		}
	}
	
	/**
	 * 非登陆状态查看所有批发商订单
	 * @param page
	 * @param mapRequest
	 * @return
	 */
	public Page<Map<Object, Object>> findAllProductOrder(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
		
        //获取基础订单查询sql条件语句：单团、散拼、游学、大客户、自由行
		String sql = getSelectSql(mapRequest);
		Page<Map<Object, Object>> pageMap = productOrderDao.findPageBySql(page, sql, Map.class);
		return pageMap;
	}
	
	/**
	 * 非登陆状态查看所有批发商订单
	 * @param page
	 * @param mapRequest
	 * @return
	 */
	public List<Object[]> findAllProductOrder(Map<String, String> mapRequest) {
		
		//获取基础订单查询sql条件语句：单团、散拼、游学、大客户、自由行
		String sql = getSelectSql(mapRequest);
		List<Object[]> list = productOrderDao.findBySql(sql);
		return list;
	}
	
	/**
	 * 基础订单查询sql条件语句
	 * @param mapRequest
	 * @return
	 */
	private String getSelectSql(Map<String, String> mapRequest) {
		
		String orderDateBegin = mapRequest.get("orderDateBegin");
		String orderDateEnd = mapRequest.get("orderDateEnd");
		
		StringBuilder sqlWhere = new StringBuilder("");
		
		sqlWhere.append("SELECT office.name officeName, sumTotalMoney, countNum, sumOrderNum ")
				.append("FROM sys_office office ")
				.append("LEFT JOIN ( ")
					.append("SELECT sum_total.officeId officeId,sum_total_money sumTotalMoney,sumOrderNum,countNum ")
					.append("FROM ( ")
						.append("SELECT total.officeId, " +
									"GROUP_CONCAT(total.currency_mark, ' ', total.amount ORDER BY total.currency_mark SEPARATOR '+') sum_total_money ")
						.append("FROM ( ")
							.append("SELECT cu.create_company_id officeId, cu.currency_mark, sum(totalMoney.amount) amount ")
							.append("FROM ( ")
								.append("SELECT pro.orderCompany agentId, mao.currencyId, mao.amount ")
								.append("FROM productorder pro, money_amount mao ")
								.append("WHERE pro.delFlag = '0' ")
									.append("AND pro.payStatus != 99 ")
									.append("AND pro.payStatus != 111 ")
									.append("AND pro.orderCompany IS NOT NULL ")
									.append("AND pro.total_money = mao.serialNum ")
									.append("AND mao.moneyType = 13 ")
									.append(orderDateBegin != null ? "AND pro.createDate >= " + orderDateBegin + " " : "")
									.append(orderDateBegin != null ? "AND pro.createDate <= " + orderDateEnd + " " : "")
							
								.append("UNION ALL ")
		
								.append("SELECT pro.agentinfo_id agentId, mao.currencyId, mao.amount ")
								.append("FROM visa_order pro, money_amount mao ")
								.append("WHERE pro.del_flag = '0' ")
									.append("AND pro.payStatus != 99 ")
									.append("AND pro.payStatus != 111 ")
									.append("AND pro.agentinfo_id IS NOT NULL ")
									.append("AND pro.total_money = mao.serialNum ")
									.append("AND mao.moneyType = 13 ")
									.append(orderDateBegin != null ? "AND pro.create_date >= " + orderDateBegin + " " : "")
									.append(orderDateBegin != null ? "AND pro.create_date <= " + orderDateEnd + " " : "")
								
								.append("UNION ALL ")
		
								.append("SELECT pro.agentinfo_id agentId, mao.currencyId, mao.amount ")
								.append("FROM airticket_order pro, money_amount mao ")
								.append("WHERE pro.del_flag = '0' ")
									.append("AND pro.order_state != 99 ")
									.append("AND pro.order_state != 111 ")
									.append("AND pro.agentinfo_id IS NOT NULL ")
									.append("AND pro.total_money = mao.serialNum ")
									.append("AND mao.moneyType = 13 ")
									.append(orderDateBegin != null ? "AND pro.create_date >= " + orderDateBegin + " " : "")
									.append(orderDateBegin != null ? "AND pro.create_date <= " + orderDateEnd + " " : "")
								.append(") totalMoney, currency cu ")
							.append("WHERE totalMoney.currencyId = cu.currency_id ")
							.append("GROUP BY cu.currency_mark, cu.create_company_id ")
							.append(") total ")
							.append("GROUP BY total.officeId ")
							.append(") sum_total ")
					.append("LEFT JOIN ( ")
						.append("SELECT officeId, sum(orderNum) sumOrderNum, count(*) countNum ")
						.append("FROM ( ")
							.append("SELECT product.proCompany officeId, ifnull(pro.orderPersonNum, 0) orderNum ")
							.append("FROM productorder pro, travelactivity product ")
							.append("WHERE pro.productId = product.id ")
								.append("AND pro.delFlag = '0' ")
								.append("AND pro.payStatus != 99 ")
								.append("AND pro.payStatus != 111 ")
								.append("AND pro.orderCompany IS NOT NULL ")
								.append(orderDateBegin != null ? "AND pro.createDate >= " + orderDateBegin + " " : "")
								.append(orderDateBegin != null ? "AND pro.createDate <= " + orderDateEnd + " " : "")
								
							.append("UNION ALL ")
		
							.append("SELECT product.proCompanyId officeId, ifnull(pro.travel_num, 0) orderNum ")
							.append("FROM visa_order pro, visa_products product ")
							.append("WHERE pro.visa_product_id = product.id ")
								.append("AND pro.del_flag = '0' ")
								.append("AND pro.payStatus != 99 ")
								.append("AND pro.payStatus != 111 ")
								.append("AND pro.agentinfo_id IS NOT NULL ")
								.append(orderDateBegin != null ? "AND pro.create_date >= " + orderDateBegin + " " : "")
								.append(orderDateBegin != null ? "AND pro.create_date <= " + orderDateEnd + " " : "")
								
							.append("UNION ALL ")
							
							.append("SELECT product.proCompany officeId, ifnull(pro.person_num, 0) orderNum ")
							.append("FROM airticket_order pro, activity_airticket product ")
							.append("WHERE pro.airticket_id = product.id ")
								.append("AND pro.del_flag = '0' ")
								.append("AND pro.order_state != 99 ")
								.append("AND pro.order_state != 111 ")
								.append("AND pro.agentinfo_id IS NOT NULL ")
								.append(orderDateBegin != null ? "AND pro.create_date >= " + orderDateBegin + " " : "")
								.append(orderDateBegin != null ? "AND pro.create_date <= " + orderDateEnd + " " : "")
							.append(") sumOrderNum ")
						.append("GROUP BY officeId ")
						.append(") sum_orderNum ON sum_total.officeId = sum_orderNum.officeId ")
					.append(") agentSumOrder ON office.id = agentSumOrder.officeId ")
				.append("WHERE office.delFlag = '0' ")
					.append("AND office.id != 1 ")
					.append("AND sumOrderNum IS NOT NULL ")
				.append("ORDER BY sumOrderNum DESC ");
		return sqlWhere.toString();
	}
	
	
	/**
	 * add by ruyi.chen
	 * add date 2015-02-28
	 * describe 多游客签证参团时复制保存游客信息
	 * @param productOrder
	 * @param travelerList
	 * @return Map  result 0:失败  1 ：成功   message:操作结果
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,String> copyTravelersInfoForVisaJoinGroup(ProductOrderCommon productOrder,List<Traveler> travelerList) throws Exception{
		Map<String,String> resultMap = new HashMap<String,String>();
		if(null == productOrder || 0 >= productOrder.getId().longValue()){
			resultMap.put("result", Context.DICT_TYPE_NO);
			resultMap.put("message", "找不到订单!");
			return resultMap;
		}
		if(null == travelerList || 0 >= travelerList.size()){
			resultMap.put("result", Context.DICT_TYPE_NO);
			resultMap.put("message", "没有游客信息!");
			return resultMap;
		}
		ActivityGroup newGroup = activityGroupService.findById(productOrder.getProductGroupId());
		if(null == newGroup || 0 >= newGroup.getId().longValue()){
			resultMap.put("result", Context.DICT_TYPE_NO);
			resultMap.put("message", "找不到团期信息！");
			return resultMap;
		}
		for(Traveler t : travelerList){
			try{
				Traveler tt = saveTravelerForVisaJoinGroup(newGroup,t,productOrder);
				if(null == tt || 0 >= tt.getId().longValue()){
					resultMap.put("result", Context.DICT_TYPE_NO);
					resultMap.put("message", "生成新游客失败！");
					return resultMap;
				}
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
						
		}
		resultMap.put("result", Context.DICT_TYPE_YES);
		resultMap.put("message", "操作成功!");
		return resultMap;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-02-28
	 * describe 游客签证参团单个复制保存游客信息
	 * @param newGroup
	 * @param oldTraveler
	 * @param newOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private Traveler  saveTravelerForVisaJoinGroup(ActivityGroup newGroup,Traveler oldTraveler, ProductOrderCommon newOrder) throws Exception{
		// 创建新旅客
		Traveler newTraveler = new Traveler();			
		//签证返佣金额为0参团时，复制为null
		String rebatesAmount = oldTraveler.getRebatesMoneySerialNum();
		if(StringUtils.isNotBlank(rebatesAmount)){
			MoneyAmount moneyAmount = moneyAmountDao.findOneAmountBySerialNum(rebatesAmount);
			if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO)==0){
				oldTraveler.setRebatesMoneySerialNum(null);
			}
		}
		BeanUtils.copyProperties(oldTraveler, newTraveler);		
		newTraveler.setId(null);
		newTraveler.setOrderId(newOrder.getId()); // 新订单ID
		newTraveler.setOrderType(newOrder.getOrderStatus());//订单类型
		newTraveler.setOriginalPayPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setPayPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setCostPriceSerialNum(UUID.randomUUID().toString());
		//TODO 其他游客需要更新的内容
		// 保存游客
		newTraveler = travelerDao.save(newTraveler);
		//签证游客记录回填 主订单号，签证游客相对应的主订单游客信息
		if(null == newTraveler || 0 >= newTraveler.getId().longValue()){
			throw new Exception("保存签证游客参团信息失败！");
			
		}else{
			if(StringUtils.isNotBlank(rebatesAmount)){
				oldTraveler.setRebatesMoneySerialNum(rebatesAmount);
			}
			oldTraveler.setMainOrderId(newOrder.getId());
			oldTraveler.setMainOrderTravelerId(newTraveler.getId());
			
			//modified by wangxinwei  添加参团标识，已跟签证子订单做区别 20151219
			oldTraveler.setIsjoingroup(1);
			
			travelerDao.save(oldTraveler);
		}
		// 根据游客类型，计算游客原始应收价
		BigDecimal amount = new BigDecimal(0);
		String currency = "1";
		if (newTraveler.getPersonType().equals(1)) {
			amount = newGroup.getSettlementAdultPrice();
			currency = newGroup.getCurrencyType().split(",")[0];
		} else if(newTraveler.getPersonType().equals(2)) {
			amount = newGroup.getSettlementcChildPrice();
			currency = newGroup.getCurrencyType().split(",")[1];
		} else {
			amount = newGroup.getSettlementSpecialPrice();
			currency = newGroup.getCurrencyType().split(",")[2];
		}
		
		newTraveler.setSrcPrice(amount);
		if (StringUtils.isNotBlank(currency)) {
			newTraveler.setSrcPriceCurrency(currencyService.findCurrency(Long.parseLong(currency)));
		}
		
		// 添加游客原始应收价流水表记录
		boolean flag = this.MoneyAmountList(newGroup, newTraveler.getOriginalPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		if(!flag){
			throw new Exception("保存游客原始应收价失败！");
		}
		// 添加游客结算价流水表记录
		boolean flag2 = this.MoneyAmountList(newGroup, newTraveler.getPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		if(!flag2){
			throw new Exception("保存游客结算价失败！");
		}
		// 添加游客成本价流水表记录
		boolean flag3 = this.MoneyAmountList(newGroup, newTraveler.getCostPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		if(!flag3){
			throw new Exception("保存游客成本价失败！");
		}
		return newTraveler;

	}
	/**
	 * 生成流水表金额记录
	 * @param newGroup 新团期实体,
	 * @param serialNum UUID
	 * @param amount 金额币种id
	 * @param amount 金额
	 * @param uid 订单ID或游客ID
	 * @param moneyType 款项类型
	 * @param orderType 产品类型
	 * @param busindessType 业务类型
	 * @param createdBy 创建者
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private boolean MoneyAmountList(ActivityGroup newGroup, String serialNum, String currency, BigDecimal amount,
			Long uid, Integer moneyType, Integer orderType,
			Integer busindessType, Long createdBy) {
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		if(null == newGroup || StringUtils.isBlank(newGroup.getCurrencyType())){
			return false;
		}
		MoneyAmount moneyAmount = null;
		moneyAmount = new MoneyAmount(serialNum, Integer.parseInt(currency), 
				amount, uid, moneyType, orderType, busindessType, UserUtils.getUser().getId());
		moneyAmountList.add(moneyAmount);
		return moneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-03-02
	 * 根据订单唯一标识、订单类型获取订单占位情况
	 * @param id   订单唯一标识
	 * @param orderType   订单类型
	 * @return result 0:未占位  1：已占位  2:其他错误      message  错误信息
	 */
	public Map<String,String> getPlaceHolderInfo(Long id, String orderType) {
		Map<String,String> resultMap = new HashMap<String ,String>();
		switch (orderType) {
		//TODO 添加机票订单余位判断，后续1.6版本 酒店余位 判断 等
			case Context.ORDER_STATUS_AIR_TICKET :
				resultMap = getAirTicketOrderPlaceHolderInfo(id);
			break;
			default :
				resultMap = getOrderPlaceHolderInfo(id);
			break;
		}
		return resultMap;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-03-02
	 * 获取参团订单占位情况
	 * @param id
	 * @return result 0:未占位  1：已占位  2:其他错误      message  错误信息
	 */
	private Map<String,String> getOrderPlaceHolderInfo(Long id) {
		Map<String, String> resultMap = Maps.newHashMap();
		ProductOrderCommon order = productOrderDao.findOne(id);
		if (null == order) {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_ERROR.toString());
			resultMap.put(Context.MESSAGE, "找不到该订单!");
			return resultMap;
		}
		//订单支付类型为  按月结算、担保结算、后续费情况时，直接判定订单占位
		if (StringUtils.equalsIgnoreCase(order.getPaymentType().toString(), Context.PAYMENT_TYPE_AY.toString()) || 
				StringUtils.equalsIgnoreCase(order.getPaymentType().toString(), Context.PAYMENT_TYPE_DB.toString()) || 
				StringUtils.equalsIgnoreCase(order.getPaymentType().toString(), Context.PAYMENT_TYPE_HXF.toString())) {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
			resultMap.put(Context.MESSAGE, "订单占位!");
			return resultMap;
		}
		//订单支付类型为即时支付时 根据订单状态判定支付情况
		if (StringUtils.equalsIgnoreCase(order.getPaymentType().toString(), Context.PAYMENT_TYPE_JS.toString())) {
			//已占位（非订金占位）已支付定金，已支付全款
            if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, order.getPayStatus().toString())                       
            		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getPayStatus().toString())
            		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getPayStatus().toString())
            		|| (order.getPlaceHolderType() != null && StringUtils.equals(Context.PLACEHOLDERTYPE_QW, order.getPlaceHolderType().toString()))) {
            	resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
    			resultMap.put(Context.MESSAGE, "订单占位!");
    			return resultMap;
            } else {
            	resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_NO.toString());
    			resultMap.put(Context.MESSAGE, "订单未占位!");
    			return resultMap;
            }
		} else {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_NO.toString());
			resultMap.put(Context.MESSAGE, "订单未占位!");
			return resultMap;
		}
	}
	
	/**
	 * 获取参团订单占位情况
	 * @param id
	 * @return 0:未占位  1：已占位  2:其他错误      message  错误信息
	 */
	private Map<String,String> getAirTicketOrderPlaceHolderInfo(Long id) {
		Map<String, String> resultMap = Maps.newHashMap();
		AirticketOrder order = airticketPreOrderDao.findOne(id);
		if (null == order) {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_ERROR.toString());
			resultMap.put(Context.MESSAGE, "找不到该订单!");
			return resultMap;
		}
		
		if (order.getPaymentStatus() == null) {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_NO.toString());
			resultMap.put(Context.MESSAGE, "订单未占位!");
			return resultMap;
		}
		
		//订单支付类型为  按月结算、担保结算、后续费情况时，直接判定订单占位
		if (StringUtils.equalsIgnoreCase(order.getPaymentStatus().toString(), Context.PAYMENT_TYPE_AY.toString()) || 
				StringUtils.equalsIgnoreCase(order.getPaymentStatus().toString(), Context.PAYMENT_TYPE_DB.toString()) || 
				StringUtils.equalsIgnoreCase(order.getPaymentStatus().toString(), Context.PAYMENT_TYPE_HXF.toString())) {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
			resultMap.put(Context.MESSAGE, "订单占位!");
			return resultMap;
		}
		//订单支付类型为即时支付时 根据订单状态判定支付情况
		if (StringUtils.equalsIgnoreCase(order.getPaymentStatus().toString(), Context.PAYMENT_TYPE_JS.toString())) {
			//已占位（非订金占位）已支付定金，已支付全款
            if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, order.getOrderState().toString())                       
            		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getOrderState().toString())
            		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getOrderState().toString())
            		|| (order.getPlaceHolderType() != null && StringUtils.equals(Context.PLACEHOLDERTYPE_QW, order.getPlaceHolderType().toString()))) {
            	resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
    			resultMap.put(Context.MESSAGE, "订单占位!");
    			return resultMap;
            } else {
            	resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_NO.toString());
    			resultMap.put(Context.MESSAGE, "订单未占位!");
    			return resultMap;
            }
		} else {
			resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_NO.toString());
			resultMap.put(Context.MESSAGE, "订单未占位!");
			return resultMap;
		}
	}
	
	/**
	 * 
	 * @param orderId  订单唯一标识
	 * @param orderType 订单类型
	 * @param number  更新余位数(number小于0时为占位操作，即団期余位减少     number大于0时为归还余位操作，団期余位增加)
	 * @param request
	 * @param groupFlag 0524需求 区分订单取消、删除、转团、退团标识     而财务驳回取消占位不在此处记录
	 * @return 0:操作失败  1：操作成功  2:其他错误      message  错误信息
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,String> saveActivityGroupPlaceHolderChange(Long orderId, String orderType, int number, HttpServletRequest request, int groupFlag) throws Exception {
		Map<String,String> resultMap = new HashMap<String,String>();
		//参团类余位操作
		if (StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_SINGLE, orderType)                       
        		|| StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_LOOSE, orderType)
        		|| StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_STUDY, orderType)
        		|| StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_BIG_CUSTOMER, orderType)
        		|| StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_FREE, orderType)
        		|| StringUtils.equalsIgnoreCase(Context.ORDER_STATUS_CRUISE, orderType)){
			
			ProductOrderCommon productOrder = productOrderDao.findOne(orderId);
			if (null == productOrder) {
				resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_ERROR.toString());
				resultMap.put(Context.MESSAGE, "找不到该订单!");
				return resultMap;
			}
			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
			if (null == activityGroup) {
				resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_ERROR.toString());
				resultMap.put(Context.MESSAGE, "找不到団期!");
				return resultMap;
			}
			//占位订单
	        if (productOrder.getPlaceHolderType() == null || StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, productOrder.getPlaceHolderType().toString())) {
	        	
	            //全款未支付、订金未支付、已占位（非订金占位） 已支付定金，已支付全款
	            if (productOrder.getPayStatus() == null
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, productOrder.getPayStatus().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, productOrder.getPayStatus().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, productOrder.getPayStatus().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString())) {
	                //已占位（非订金占位）已支付定金，已支付全款    后付费等其他情况
	                if (productOrder.getPayStatus() != null
	                        &&( StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, productOrder.getPayStatus().toString()))                       
	                		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString()) 
	                		//按月、担保、后付费等结算方式时 
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_AY.toString(), productOrder.getPaymentType().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_DB.toString(), productOrder.getPaymentType().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_HXF.toString(), productOrder.getPaymentType().toString())) {
	                	//返还余位数
	                	activityGroup.setPlusFreePosition(number);
	                    activityGroup.setFreePosition(activityGroup.getFreePosition() + number);
	                    //返还售出占位（仅针对已支付订单）
	                    if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString()) 
	                    		||StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString())) {
	                    	activityGroup.setSoldNopayPosition(activityGroup.getSoldNopayPosition() - number);
		                    activityGroup.setPlusSoldNopayPosition(-number);
	                    }
	                    
	                    //返还已占位人数
		                activityGroup.setPlusNopayReservePosition(-number);
		                activityGroup.setNopayReservePosition(activityGroup.getNopayReservePosition() - number);
		                String newVersion;
		                if (request != null) {
							newVersion = com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request);
						} else {
							newVersion = com.trekiz.admin.common.utils.StringUtils.getVersionNumber();
						}
		                activityGroupService.updatePositionNumByOptLock(activityGroup, newVersion);
	                }
	            }
	        } 
	        //切位订单：
	        else if (productOrder.getPlaceHolderType() != null
	                && productOrder.getPayStatus() != null
	                && StringUtils.equals(Context.PLACEHOLDERTYPE_QW, productOrder.getPlaceHolderType().toString())) {
	        		
        		ActivityGroupReserve groupReserve = activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId
                        (productOrder.getOrderCompany(), productOrder.getProductId(),activityGroup.getId());
        		
        		if (groupReserve != null) {
        			//更新切位余位
            		Integer leftpayReservePosition = groupReserve.getLeftpayReservePosition();
            		if (leftpayReservePosition == null) {
            			leftpayReservePosition = 0;
            		}
                    groupReserve.setLeftpayReservePosition(leftpayReservePosition + number);
                    
                    //仅针对已支付订单
                    if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString()) 
                    		||StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString())) {
                    	//更新售出切位
                    	groupReserve.setSoldPayPosition(groupReserve.getSoldPayPosition() - number);
                    	//更新售出切位
                        activityGroup.setSoldPayPosition(activityGroup.getSoldPayPosition() - number);
                    }
                    
                    //保存切位
                    activityGroupReserveDao.save(groupReserve);
        		}
	        }
	        
	        // 0524需求 团期余位变化,记录在团控板中
			if (groupFlag == 5) {// 由于退团操作,操作人要记录流程的发起人,故在别处进行记录
//	        	groupControlBoardService.insertGroupControlBoard(5, number, "订单号"+productOrder.getOrderNum()+","+number+"人退团", activityGroup.getId(), -1);
			} else if (groupFlag == 8) {// 订单取消(分为 8人为操作的订单取消 11系统自动取消的订单 这两种情况操作人需显示不同,要区分开来)
				groupControlBoardService.insertGroupControlBoard(groupFlag, number, "订单号"+productOrder.getOrderNum()+"已取消,归还余位"+number+"个", activityGroup.getId(), -1);
			} else if (groupFlag == 11) {// 订单取消(分为 8人为操作的订单取消 11系统自动取消的订单 这两种操作人需显示不同,要区分开来)
	        	groupControlBoardService.insertGroupControlBoard(groupFlag, number, "订单号"+productOrder.getOrderNum()+"已取消,归还余位"+number+"个", activityGroup.getId(), productOrder.getCreateBy().getId());
			} else if (groupFlag == 9) {// 订单删除
	        	groupControlBoardService.insertGroupControlBoard(groupFlag, number, "订单号"+productOrder.getOrderNum()+"已删除,归还余位"+number+"个", activityGroup.getId(), -1);
			}
	        // 0524需求 团期余位变化,记录在团控板中
	        
	        resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
			resultMap.put(Context.MESSAGE, "操作完成!");
			return resultMap;
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @param orderId  订单唯一标识
	 * @param orderType 订单类型
	 * @param number  更新余位数(number小于0时为占位操作，即団期余位减少     number大于0时为归还余位操作，団期余位增加)
	 * @param request
	 * @return 0:操作失败  1：操作成功  2:其他错误      message  错误信息
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,String> saveAirTicketActivityPlaceHolderChange(Long orderId, String orderType, int number, ActivityAirTicket activity) throws Exception {
		Map<String,String> resultMap = new HashMap<String,String>();
			

			AirticketOrder order = airticketPreOrderDao.findOne(orderId);
			//占位订单
	        if (order.getPlaceHolderType() == null || StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, order.getPlaceHolderType().toString())) {
	        	
	            //全款未支付、订金未支付、已占位（非订金占位） 已支付定金，已支付全款
	            if (order.getOrderState() == null
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, order.getOrderState().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, order.getOrderState().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, order.getOrderState().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getOrderState().toString())
	                    || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getOrderState().toString())) {
	                //已占位（非订金占位）已支付定金，已支付全款    后付费等其他情况
	                if (order.getOrderState() != null
	                        &&( StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, order.getOrderState().toString()))                       
	                		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getOrderState().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getOrderState().toString()) 
	                		//按月、担保、后付费等结算方式时 
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_AY.toString(), order.getPaymentStatus().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_DB.toString(), order.getPaymentStatus().toString())
	                		|| StringUtils.equalsIgnoreCase(Context.PAYMENT_TYPE_HXF.toString(), order.getPaymentStatus().toString())) {
	                	//返还余位数
	                	activity.setFreePosition(activity.getFreePosition() + number);
	                    //返还售出占位
	                	if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getOrderState().toString())
	                			|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getOrderState().toString())) {
	                		activity.setSoldNopayPosition(activity.getSoldNopayPosition() - number);
	                	}
	                	//返还已占位人数
		                activity.setNopayReservePosition(activity.getNopayReservePosition() - number);
	                }
	            }
	        } 
	        //切位订单：
	        else if (order.getPlaceHolderType() != null
	                && order.getOrderState() != null
	                && StringUtils.equals(Context.PLACEHOLDERTYPE_QW, order.getPlaceHolderType().toString())) {
	        		
        		//切位产品  
                AirticketActivityReserve reserve = airticketActivityReserveDao.findAgentReserve(order.getAirticketId(), order.getAgentinfoId());
                if(null!=reserve){
                	//更新切位余位
                    reserve.setLeftpayReservePosition(reserve.getLeftpayReservePosition() + number);
                    //保存切位
                    airticketActivityReserveDao.save(reserve);
                }
                //更新售出切位
                if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, order.getOrderState().toString())
            			|| StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, order.getOrderState().toString())) {
                	activity.setSoldPayPosition(activity.getSoldPayPosition() - number);
                }
	        }
	        
	        resultMap.put(Context.RESULT, Context.ORDER_PLACEHOLDER_YES.toString());
			resultMap.put(Context.MESSAGE, "操作完成!");
			return resultMap;
		}

	/**
	 * 获取单团没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getDTNotSeenOrderNum() {
		// 获取统计查询条件
		StringBuffer sqlWhere = new StringBuffer("");
		sqlWhere.append("AND activity.createBy = " + UserUtils.getUser().getId() + " ");
		// 分组统计单团类未查看订单
		return getDTNotSeenSql(sqlWhere);
	}
	
	/**
	 * 获取单团没有查看订单数量：日新观光
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getRXGG_DTNotSeenOrderNum() {
		// 获取统计查询条件
		DepartmentCommon common = departmentService.setDepartmentPara("order", null);
		StringBuffer sqlWhere = new StringBuffer("");
		sqlWhere.append("AND activity.proCompany = " + UserUtils.getUser().getCompany().getId() + " ");
		
		StringBuffer result = new StringBuffer("");
		StringBuffer temp = new StringBuffer("");
		
		// 单团
		common.setPermission("singleOrder:list:allorder");
		Set<Department> deptSet = common.getShowAreaList();
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 1);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 1 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		// 散拼
		common.setPermission("looseOrder:list:allorder");
		temp = new StringBuffer("");
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 2);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 2 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		// 游学
		common.setPermission("studyOrder:list:allorder");
		temp = new StringBuffer("");
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 3);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 3 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		// 大客户
		common.setPermission("bigCustomerOrder:list:allorder");
		temp = new StringBuffer("");
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 4);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 4 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		// 自由行
		common.setPermission("freeOrder:list:allorder");
		temp = new StringBuffer("");
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 5);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 5 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		// 游轮
		common.setPermission("cruiseOrder:list:allorder");
		temp = new StringBuffer("");
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, temp, common, 10);
		}
		if (temp.length() != 0) {
			temp = new StringBuffer(temp.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		temp.append(" AND pro.orderStatus = 10 ");
		result.append(getDTNotSeenSql(new StringBuffer("").append(sqlWhere).append(temp)));
		
		// 分组统计单团类未查看订单
		return result.toString();
	}
	
	/**
	 * 获取单团类订单未查看订单统计sql 
	 * @author yakun.bai
	 * @Date 2016-5-23
	 */
	private String getDTNotSeenSql(StringBuffer sqlWhere) {
		StringBuffer sb = new StringBuffer("");
		String sqlString = "SELECT orderStatus ,count(*) FROM productorder pro, travelactivity activity " +
				"WHERE pro.productId = activity.id AND pro.delFlag = 0 AND seen_flag = 0 " + sqlWhere 
				+ " AND orderStatus IS NOT NULL GROUP BY orderStatus ORDER BY orderStatus";
		List<Object[]> resultList = productOrderDao.findBySql(sqlString);
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (Object[] obj : resultList) {
				sb.append(obj[0] + "," + obj[1] + ";");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取机票没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getJPNotSeenOrderNum() {
		// 获取统计查询条件
		StringBuffer sqlWhere = new StringBuffer("");
		sqlWhere.append("AND aa.createBy = " + UserUtils.getUser().getId() + " ");
		// 分组统计单团类未查看订单
		return getJPNotSeenSql(sqlWhere);
	}
	
	/**
	 * 获取机票没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getRXGG_JPNotSeenOrderNum() {
		// 获取统计查询条件
		DepartmentCommon common = departmentService.setDepartmentPara("airticketOrder", null);
		
		Set<Department> deptSet = common.getShowAreaList();
		StringBuffer sqlWhere = new StringBuffer();
		for (Department dept : deptSet) {
			common.setDepartmentId(dept.getId().toString());
			common = departmentService.getRoleTypeList(UserUtils.getUser(), common);
			systemService.getDepartmentSql("order", null, sqlWhere, common, Context.ORDER_TYPE_JP);
		}
		
		if (sqlWhere.length() != 0) {
			sqlWhere = new StringBuffer(sqlWhere.toString().replaceAll("and", "or").replaceFirst("or", "and (") + ")");
		}
		
		sqlWhere.append("AND aa.proCompany = " + UserUtils.getUser().getCompany().getId() + " ");
		return getJPNotSeenSql(sqlWhere);
	}
	
	/**
	 * 获取机票订单未查看订单统计sql 
	 * @author yakun.bai
	 * @Date 2016-5-23
	 */
	private String getJPNotSeenSql(StringBuffer sqlWhere) {
		String sqlString = "SELECT count(*) FROM airticket_order ao, activity_airticket aa " +
				"WHERE ao.airticket_id = aa.id AND ao.del_flag = 0 AND seen_flag = 0 " + sqlWhere;
		List<BigInteger> resultList = productOrderDao.findBySql(sqlString);
		if (CollectionUtils.isNotEmpty(resultList)) {
			return resultList.get(0).toString();
		}
		return "";
	}
	
	/**
	 * 获取签证没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getQZNotSeenOrderNum(StringBuffer sqlBuffer) {
		
		StringBuffer sql = new StringBuffer("select count(DISTINCT vo.id) ct ");
		sql.append(sqlBuffer.substring(sqlBuffer.indexOf("FROM")));
		sql = new StringBuffer(sql.substring(0, sql.lastIndexOf("GROUP BY")));
		sql.append(" and vo.isRead = 0");
		
		List<Map<String,Object>> resultList = visaOrderDao.findBySql(sql.toString(),Map.class);
		
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0).get("ct").toString();
		}
		return "";
	}
	
	/**
	 * 获取T1预报名订单没有查看数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getT1NotSeenOrderNum() {
		// 获取统计查询条件
		StringBuffer sqlWhere = new StringBuffer("");
		User currentUser = UserUtils.getUser();
		sqlWhere.append(" and activity.proCompany = " + currentUser.getCompany().getId() + " ");
    	if (!UserUtils.hasRole(currentUser, Context.ROLE_TYPE_MANAGER)) {
    		sqlWhere.append(" and pro.salerId = " + currentUser.getId() + " ");
    	}
		// 分组统计单团类未查看订单
		return getT1NotSeenSql();
	}
	
	/**
	 * 获取T1预报名订单未查看统计sql 
	 * @author yakun.bai
	 * @Date 2016-10-25
	 */
	private String getT1NotSeenSql() {
		StringBuffer sqlWhere = new StringBuffer();
		User currentUser = UserUtils.getUser();
		sqlWhere.append(" AND activity.proCompany = " + currentUser.getCompany().getId() + " ");
    	if (!UserUtils.hasRole(currentUser, Context.ROLE_TYPE_MANAGER)) {
    		sqlWhere.append(" AND pro.salerId = " + currentUser.getId() + " ");
    	}
		String sqlString = "SELECT count(*) FROM t1_pre_order pro, travelactivity activity " +
				"WHERE pro.productId = activity.id AND pro.createDate >= CURDATE() AND pro.hasSeen = 0" + sqlWhere;
		List<BigInteger> resultList = productOrderDao.findBySql(sqlString);
		return resultList.get(0).toString();
	}
	
}