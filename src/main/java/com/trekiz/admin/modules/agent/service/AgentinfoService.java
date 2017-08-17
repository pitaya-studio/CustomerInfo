package com.trekiz.admin.modules.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.repository.SupplyContactsDao;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.hotel.service.SysdefinedictService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 渠道商信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class AgentinfoService {

	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ProductOrderCommonDao productorderDao;
	@Autowired
	private AreaService areaService;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SupplyContactsDao supplyContactsDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private PlatBankInfoDao platBankInfoDao;
	@Autowired
	private SupplyContactsService supplyContactsService;
	
	@Autowired
	private QuauqAgentInfoService quauqAgentInfoService;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private SysdefinedictService sysdefinedictService;
	
	/**
	 * 渠道查询
	 * @param page
	 * @return
	 */
	public Page<Map<Object, Object>> getAgentInfo(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
		
		String isSignChannel = mapRequest.get("isSignChannel");
		
		if (isSignChannel == null || "0".equals(isSignChannel) || "1".equals(isSignChannel)) {
			return getCommonAgentInfo(page, mapRequest);
		} else {
			return getQuauqAgentInfo(page, mapRequest);
		}
	}
	
	/**
	 * 渠道查询
	 * @param page
	 * @return
	 */
	private Page<Map<Object, Object>> getCommonAgentInfo(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
		
		//订单类型：单团、散拼、游学、大客户、自由行、签证、机票  分别用1、2、3、4、5、6、7表示
		String orderType = mapRequest.get("orderType");
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		//标识是否是签约渠道查询：0或空的时候表示签约渠道；1表示非签约渠道
		String isSignChannel = mapRequest.get("isSignChannel");
		Boolean signChannel = true;
		if (isSignChannel == null || "0".equals(isSignChannel)) {
			signChannel = true;
		} else {
			signChannel = false;
		}
		mapRequest.put("isSignChannel", signChannel.toString());
		
		//查询当前用户是否是系统管理员或销售经理：如果是则能查询本批发商所有数据，否则签约渠道查询时只能查询自己跟进渠道，非签约渠道时查询自己订单
		Boolean isManager = false;
		User user = UserUtils.getUser();
    	boolean isSaleManager = false;
    	boolean isSystemManager = false;
    	for(Role role : user.getRoleList()) {
    		if(Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
    			isSystemManager = true;
    		}
    		if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
    			isSaleManager = true;
    		}
    	}
        if(isSystemManager || isSaleManager) {
        	isManager = true;
		}
        mapRequest.put("isManager", isManager.toString());
		
        //获取基础订单查询sql条件语句：单团、散拼、游学、大客户、自由行
		String sumWhere = getSumWhere(mapRequest);
		//获取统计订单sql语句
		String sql = getSelectSql(mapRequest, sumWhere);
		//如果是签约渠道，则左连接统计语句查询所有渠道
		if (signChannel) {
			sql = "SELECT agent.id agentId, agent.paymentType,agent.salesRoom,agent.agentTelAreaCode,agent.agentTel,agent.agentAddressStreet," +
					"sumTotalMoney, agent.agentSalerId, sumPayedMoney, sumAccountedMoney, sumOrderNum, agent.agentName, agent.agentBrand, " +
					"agent_contacts.contacts AS contacts " + // 0574 Excel中添加联系人信息列
					"FROM agentinfo agent LEFT JOIN (" + sql + ") agentSumOrder ON agent.id = agentSumOrder.agentId " +
					"LEFT JOIN ( SELECT GROUP_CONCAT(tt.contact SEPARATOR '、') AS contacts, tt.supplierId " +
					// 0574 Excel中添加联系人信息列
					"FROM ( SELECT CONCAT( ai.agentContact, ':', ai.agentContactMobile ) AS contact, ai.id AS supplierId " +
					"FROM agentinfo ai WHERE ai.supplyId = " + (signChannel ? companyId : "0") + " AND ai.delFlag = '0' " +
					"UNION SELECT CONCAT( sc.contactName, ':', sc.contactMobile ) AS contact, ai.id AS supplierId " +
					"FROM agentinfo ai LEFT JOIN supplier_contacts sc ON sc.supplierId = ai.id " +
					"WHERE ai.supplyId = " + (signChannel ? companyId : "0") + " AND ai.delFlag = '0' AND sc.type = 0 GROUP BY sc.id " +
					") tt GROUP BY tt.supplierId ) agent_contacts ON agent_contacts.supplierId = agent.id " +
					"WHERE agent.delFlag = '0' AND status = '1' AND agent.supplyId = " + 
					user.getCompany().getId() + " " + getSqlWhere(mapRequest, signChannel, true);
			sql = sql +" ORDER BY agentId desc";
		}
		//处理订单sql查询语句：会根据orderType不同处理相对应不一致的订单字段
		sql = handleSql(sql, orderType);
		mapRequest.put("sql", sql);
		Page<Map<Object, Object>> pageMap = agentinfoDao.findPageBySql(page, sql, Map.class);
		return pageMap;
	}
	
	/**
	 * 基础订单查询sql条件语句
	 * @param mapRequest
	 * @return
	 */
	private String getSumWhere(Map<String, String> mapRequest) {
		
		String orderType = mapRequest.get("orderType");
		boolean isSignChannel = Boolean.parseBoolean(mapRequest.get("isSignChannel"));
		boolean isManager = Boolean.parseBoolean(mapRequest.get("isManager"));
		User user = UserUtils.getUser();
		
		String createBySql = "";
		if (!isSignChannel) {
			if (isManager) {
				String createByIds = "";
				List<User> userList = userDao.getUserByCompany(user.getCompany());
				for (int i=0;i<userList.size();i++) {
					User temp = userList.get(i);
					//如果不是正常用户，则跳过循环
					if (!Context.DEL_FLAG_NORMAL.equals(temp.getDelFlag())) {
						continue;
					}
					createByIds += temp.getId() + ",";
				}
				createBySql = "AND pro.createBy in (" + createByIds.substring(0, createByIds.length()-1) + ") " ;
			} else {
				createBySql = "AND pro.createBy = " + user.getId() + " " ;
			}
		}
		
		StringBuilder sqlWhere = new StringBuilder("");
		
		sqlWhere.append("pro.delFlag = '" + Context.DEL_FLAG_NORMAL + "' ")
				.append("AND pro.payStatus != " + Context.ORDER_PAYSTATUS_YQX + " ")
				.append("AND pro.payStatus != " + Context.ORDER_PAYSTATUS_DEL + " ")
				.append("AND pro.orderCompany IS NOT NULL ")
				.append(isSignChannel ? "" : "AND pro.orderCompanyName IS NOT NULL ")
				.append("AND pro.total_money = mao.serialNum ")
				.append(createBySql)
				.append("AND mao.moneyType = 13 ");
		
		if (StringUtils.isNotBlank(orderType) 
				&& !Context.ORDER_STATUS_VISA.equals(orderType) 
				&& !Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
			sqlWhere.append("AND orderStatus = " + orderType + " ");
		}
		return sqlWhere.toString();
	}
	
	/**
	 * 组织批发商对应下的订单creator的sql
	 * @param mapRequest
	 * @return
	 */
	private String getCreateBySql(Map<String, String> mapRequest){
		String createBySql = "";
		boolean isSignChannel = Boolean.parseBoolean(mapRequest.get("isSignChannel"));
		boolean isManager = Boolean.parseBoolean(mapRequest.get("isManager"));
		User user = UserUtils.getUser();
		if (!isSignChannel) {
			if (isManager) {
				String createByIds = "";
				List<User> userList = userDao.getUserByCompany(user.getCompany());
				for (int i=0;i<userList.size();i++) {
					User temp = userList.get(i);
					//如果不是正常用户，则跳过循环
					if (!Context.DEL_FLAG_NORMAL.equals(temp.getDelFlag())) {
						continue;
					}
					createByIds += temp.getId() + ",";
				}
				createBySql = "AND pro.createBy in (" + createByIds.substring(0, createByIds.length()-1) + ") " ;
			} else {
				createBySql = "AND pro.createBy = " + user.getId() + " " ;
			}
		}
		return createBySql;
	}
	
	/**
	 * 渠道订单统计sql
	 * @param mapRequest
	 * @param sumWhere
	 * @return
	 */
	private String getSelectSql(Map<String, String> mapRequest, String sumWhere) {
		
		boolean isSignChannel = Boolean.parseBoolean(mapRequest.get("isSignChannel"));
		String orderType = mapRequest.get("orderType");
		
		//查询sql语句
		StringBuilder sql = new StringBuilder("");
		//查询订单应收、已收、达帐金额
		StringBuilder moneySql = new StringBuilder("");
		
		sql.append("SELECT sum_total.agentId agentId, agent.paymentType paymentType, ")
		   .append("sum_total_money sumTotalMoney, sum_payed_money sumPayedMoney, sum_accounted_money sumAccountedMoney, sumOrderNum ")
		   .append(isSignChannel ? ", agent.agentName agentName " : ", sum_total.agentName agentName ")
		   .append("FROM agentinfo agent,");
			
		moneySql.append("(SELECT total.agentId, GROUP_CONCAT(total.currency_mark, ' ', total.amount ORDER BY total.currency_mark SEPARATOR '+') sum_total_money ")
				.append(isSignChannel ? "" : ", total.agentName ")
				.append("FROM ( ")
				/* 根据渠道和币种分组查询总额 */
				.append("SELECT agent.id agentId, cu.currency_mark, sum(totalMoney.amount) amount ")
				.append(isSignChannel ? "" : ", totalMoney.agentName ")
				.append("FROM ( ")
				/* 查询订单应收金额 */
				.append("SELECT pro.orderCompany agentId, mao.uid, mao.currencyId, mao.amount ")
				.append(isSignChannel ? "" : ", pro.orderCompanyName agentName ")
				.append("FROM productorder pro, money_amount mao ")
				.append("WHERE " + sumWhere);
		if (StringUtils.isBlank(orderType)) {
			//关联签证订单
			moneySql.append(" UNION ALL ")
					.append("SELECT pro.agentinfo_id agentId, mao.uid, mao.currencyId, mao.amount ")
					.append(isSignChannel ? "" : ", pro.agentinfo_name agentName ")
					.append("FROM visa_order pro, money_amount mao ")
					.append("WHERE " + handleSql(sumWhere, Context.ORDER_STATUS_VISA));
			moneySql.append(" UNION ALL ")
					.append("SELECT pro.agentinfo_id agentId, mao.uid, mao.currencyId, mao.amount ")
					.append(isSignChannel ? "" : ", pro.nagentName agentName ")
					.append("FROM airticket_order pro, money_amount mao ")
					.append("WHERE " + handleSql(sumWhere, Context.ORDER_STATUS_AIR_TICKET));
		}
		moneySql.append(") totalMoney, currency cu, agentinfo agent ")
				.append("WHERE totalMoney.currencyId = cu.currency_id ")
				.append("AND totalMoney.agentId = agent.id ")
				.append("AND agent.supplyId = " + (isSignChannel ? UserUtils.getUser().getCompany().getId() : "0") + " ")
				.append("GROUP BY cu.currency_mark, agent.id").append(isSignChannel ? "" : ", totalMoney.agentName ")
				.append(") total ")
				.append("GROUP BY total.agentId").append(isSignChannel ? "" : ", total.agentName ").append(") sum_total ");
		
		//应收金额
		sql.append(moneySql);
		//实收金额
		sql.append("LEFT JOIN ").append(moneySql.toString().replaceAll("total", "payed").replace("13", "5"))
			.append(isSignChannel ? "ON sum_total.agentId = sum_payed.agentId " : "ON sum_total.agentName = sum_payed.agentName ");
		//达帐金额
		sql.append("LEFT JOIN ").append(moneySql.toString().replaceAll("total", "accounted").replace("13", "4"))
			.append(isSignChannel ? "ON sum_total.agentId = sum_accounted.agentId " : "ON sum_total.agentName = sum_accounted.agentName ");
		//游客人数
		sql.append("LEFT JOIN ").append(getOrderNum(mapRequest, orderType, isSignChannel))
			.append(isSignChannel ? "ON sum_total.agentId = sum_orderNum.agentId " : "ON sum_total.agentName = sum_orderNum.agentName ");
		//查询where
		sql.append(getSqlWhere(mapRequest, isSignChannel, false));
		return sql.toString();
	}
	
	/**
	 * 查询订单总人数
	 * @return
	 */
	private String getOrderNum(Map<String, String> mapRequest, String orderType, boolean isSignChannel) {
		
		String createBySql = getCreateBySql(mapRequest);
		
		String where = "pro.orderCompany = agent.id " 
					+ "AND agent.supplyId = " + (isSignChannel ? UserUtils.getUser().getCompany().getId() : "0") + " "
					+ "AND pro.delFlag = '" + Context.DEL_FLAG_NORMAL + "' " 
					+ "AND pro.payStatus != " + Context.ORDER_PAYSTATUS_YQX + " "
					+ "AND pro.payStatus != " + Context.ORDER_PAYSTATUS_DEL + " "
					+ "AND pro.orderCompany IS NOT NULL "
					+ createBySql;
		where += isSignChannel ? "" : "AND pro.orderCompanyName IS NOT NULL ";
		//查询sql语句
		StringBuilder sql = new StringBuilder("");
		sql.append("(SELECT agentId, sum(orderNum) sumOrderNum ")
			.append(isSignChannel ? "" : ", agentName ")
			.append("FROM ")
				.append("(SELECT agent.id agentId, ifnull(pro.orderPersonNum, 0) orderNum ")
				.append(isSignChannel ? "" : ", pro.orderCompanyName agentName ")
				.append("FROM productorder pro, agentinfo agent ")
				.append("WHERE " + where);
		if (StringUtils.isBlank(orderType)) {
			//关联签证订单
			sql.append(" UNION ALL ")
				.append("SELECT pro.agentinfo_id agentId, ifnull(pro.travel_num, 0) orderNum ")
				.append(isSignChannel ? "" : ", pro.agentinfo_name agentName ")
				.append("FROM visa_order pro, agentinfo agent ")
				.append("WHERE " + handleSql(where, Context.ORDER_STATUS_VISA));
			sql.append(" UNION ALL ")
				.append("SELECT pro.agentinfo_id agentId, ifnull(pro.person_num, 0) orderNum ")
				.append(isSignChannel ? "" : ", pro.nagentName agentName ")
				.append("FROM airticket_order pro, agentinfo agent ")
				.append("WHERE " + handleSql(where, Context.ORDER_STATUS_AIR_TICKET));
		}
		sql.append(") sumOrderNum GROUP BY agentId").append(isSignChannel ? "" : ", sumOrderNum.agentName ").append(") sum_orderNum ");
		return sql.toString();
	}
	
	/**
	 * 根据查询条件组件查询语句
	 * @param mapRequest
	 * @return
	 */
	private String getSqlWhere(Map<String, String> mapRequest, boolean isSignChannel, boolean isTotal) {
		
		StringBuilder sql = new StringBuilder("");
		if (!isTotal) {
			sql.append("WHERE agent.id = sum_total.agentId ");
		}

		/**
		 * 29 搜索条件输入框由 "输入渠道名称" 改为 "输入渠道名称，渠道品牌" 模糊搜索
		 * @date 2016年3月7日14:58:11
		 */
		//渠道名称
		if (StringUtils.isNotBlank(mapRequest.get("agentName"))) {
			if (isSignChannel) {
				sql.append("AND (agent.agentName like '%" + mapRequest.get("agentName")+ "%' OR agent.agentBrand like '%" + mapRequest.get("agentName") + "%') ");
			} else {
				sql.append("AND sum_orderNum.agentName like '%" + mapRequest.get("agentName") + "%' ");
			}
		}
		if (StringUtils.isNotBlank(mapRequest.get("salesRoom"))) {
			if (isSignChannel) {
				sql.append("AND (agent.salesRoom like '%" + mapRequest.get("salesRoom")+ "%' )");
			} else {
//				sql.append("AND sum_orderNum.agentName like '%" + mapRequest.get("agentName") + "%' ");
			}
		}
		if (StringUtils.isNotBlank(mapRequest.get("agentTel"))) {
			if (isSignChannel) {
				sql.append("AND (CONCAT(agent.agentTelAreaCode,'-',agent.agentTel) like '%" + mapRequest.get("agentTel") + "%') ");
			} else {
//				sql.append("AND sum_orderNum.agentName like '%" + mapRequest.get("agentName") + "%' ");
			}
		}
		
		//渠道销售
		User user = UserUtils.getUser();
		//如果不是销售经理或管理员，则用户只能查看自己负责的渠道
		boolean isSaleManager = false;
		boolean isManager = false;
		for(Role role : user.getRoleList()) {
			if(Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
				isManager = true;
			}
			if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
				isSaleManager = true;
			}
		}
		if (StringUtils.isNotBlank(mapRequest.get("salerUserId")) && isSignChannel) {
			if (!isManager && !isSaleManager) {
				sql.append("AND FIND_IN_SET(" + UserUtils.getUser().getId() + ", agent.agentSalerId) " + " ");
			} else {
				sql.append("AND FIND_IN_SET(" + mapRequest.get("salerUserId") + ", agent.agentSalerId) " + " ");
			}
		} else {
			if(!isManager && !isSaleManager && isSignChannel) {
				sql.append("AND FIND_IN_SET(" + UserUtils.getUser().getId() + ", agent.agentSalerId) " + " ");
			}
		}
		//创建时间
		String beginCreateDate = mapRequest.get("beginCreateDate");
		String endCreateDate = mapRequest.get("endCreateDate");
		if (StringUtils.isNotBlank(beginCreateDate)) {
			sql.append("AND agent.createDate >= '" + beginCreateDate + "' ");
		}
		if (StringUtils.isNotBlank(endCreateDate)) {
			sql.append("AND agent.createDate <= '" + endCreateDate + "' ");
		}
		
		return sql.toString();
	}
	
	/**
	 * 处理sql语句：单团、散拼、游学、大客户、自由行、机票、签证
	 * @param sql
	 * @param orderType
	 * @return
	 */
	private String handleSql(String sql, String orderType) {
		if (StringUtils.isNotBlank(orderType)) {
			//签证
			if (Context.ORDER_STATUS_VISA.equals(orderType)) {
				//单团：orderCompany、payStatus、delFlag、orderPersonNum   对应签证：agentinfo_id、order_state、del_flag、travel_num
				sql = sql.replaceAll("productorder", "visa_order")
						.replaceAll("orderCompanyName", "agentinfo_name")
						.replaceAll("orderCompany", "agentinfo_id")
						.replaceAll("pro.delFlag", "pro.del_flag")
						.replaceAll("createBy", "create_by")
						.replaceAll("orderPersonNum", "travel_num");
			} 
			//机票
			else if(Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
				//单团：orderCompany、payStatus、delFlag、orderPersonNum   对应机票：agentinfo_id、order_state、del_flag、person_num
				sql = sql.replaceAll("productorder", "airticket_order")
						.replaceAll("orderCompanyName", "nagentName")
						.replaceAll("orderCompany", "agentinfo_id")
						.replaceAll("payStatus", "order_state")
						.replaceAll("pro.delFlag", "pro.del_flag")
						.replaceAll("createBy", "create_by")
						.replaceAll("orderPersonNum", "person_num");
			} 
		}
		return sql;
	}
	
	/**
	 * quauq渠道查询
	 * @param page
	 * @return
	 */
	private Page<Map<Object, Object>> getQuauqAgentInfo(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
			
		// 获取查询条件
		String where = getWhereSql(mapRequest);
		
		// 获取查询语句
		String sql = getStatisticsSql(where);
		
		// 数据查询
		Page<Map<Object, Object>> pageMap = agentinfoDao.findPageBySql(page, sql, Map.class);
		
		mapRequest.put("sql", sql);
		
		return pageMap;
	}
	
	 /**
     * @Description 获取查询条件
     * @author yakun.bai
     * @Date 2016-5-5
     */
    private String getWhereSql(Map<String, String> map) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        // 渠道名称、渠道品牌
        String agentName = map.get("agentName");
        
        if (StringUtils.isNotBlank(agentName)) {
        	sqlWhere.append("AND (agent.agentName like '%" + agentName + "%' OR agent.agentBrand like '%" + agentName + "%') ");
		}
        
        return sqlWhere.toString();
    }
	
    /**
     * @Description 获取统计查询sql
     * @author yakun.bai
     * @Date 2016-5-4
     */
    private String getStatisticsSql(String where) {
    	
    	String ququaSql = getQuauqStatisticsSql(where);
    	
		String sql = "SELECT agent.id agentId, agent.paymentType, user.name userName, sumTotalMoney" +
				", sumPayedMoney, sumAccountedMoney, sumOrderNum, agent.agentName, agent.agentBrand " +
				"FROM agentinfo agent LEFT JOIN (" + ququaSql + ") agentSumOrder ON agent.id = agentSumOrder.agentId " +
				"LEFT JOIN sys_user user ON user.id = agent.agentSalerId " +
				"WHERE agent.delFlag = '0' AND status = '1' AND agent.is_quauq_agent = 1 ";
    	return sql.toString();
    }
	
    /**
     * @Description 获取quauq渠道查询sql
     * @author yakun.bai
     * @Date 2016-5-5
     */
    private String getQuauqStatisticsSql(String where) {
		
		//查询sql语句
		StringBuilder sql = new StringBuilder("");
		//查询订单应收、已收、达帐金额
		StringBuilder moneySql = new StringBuilder("");
		//查询条件
		String sumWhere = "pro.delFlag = '0' AND pro.payStatus != 99 AND pro.payStatus != 111 AND pro.orderCompany IS NOT NULL " +
				"AND pro.total_money = mao.serialNum AND mao.moneyType = 13 AND orderStatus = 2";
		
		sql.append("SELECT sum_total.agentId agentId, agent.paymentType paymentType, " +
						"sum_total_money sumTotalMoney, sum_payed_money sumPayedMoney, sum_accounted_money sumAccountedMoney, sumOrderNum ")
			.append(", agent.agentName agentName ")
			.append("FROM agentinfo agent,");
			
		moneySql.append("(SELECT total.agentId, GROUP_CONCAT(total.currency_mark, ' ', total.amount ORDER BY total.currency_mark SEPARATOR '+') sum_total_money ")
				.append("FROM ( ")
				
					/* 根据渠道和币种分组查询总额 */
					.append("SELECT agent.id agentId, cu.currency_mark, sum(totalMoney.amount) amount ")
					.append("FROM ( ")
					
						/* 查询订单应收金额 */
						.append("SELECT pro.orderCompany agentId, mao.uid, mao.currencyId, mao.amount ")
						.append("FROM productorder pro, money_amount mao ")
						.append("WHERE " + sumWhere);
						moneySql.append(") totalMoney, currency cu, agentinfo agent ")
						
					.append("WHERE totalMoney.currencyId = cu.currency_id ")
						.append("AND totalMoney.agentId = agent.id ")
						.append("AND agent.is_quauq_agent = 1 ")
						.append("AND cu.create_company_id = " + UserUtils.getUser().getCompany().getId() + " ")
					.append("GROUP BY cu.currency_mark, agent.id")
					
				.append(") total ")
			.append("GROUP BY total.agentId").append(") sum_total ");
		
		//应收金额
		sql.append(moneySql);
		//实收金额
		sql.append("LEFT JOIN ").append(moneySql.toString().replaceAll("total", "payed").replace("13", "5"))
			.append("ON sum_total.agentId = sum_payed.agentId ");
		//达帐金额
		sql.append("LEFT JOIN ").append(moneySql.toString().replaceAll("total", "accounted").replace("13", "4"))
			.append("ON sum_total.agentId = sum_accounted.agentId ");
		//游客人数
		sql.append("LEFT JOIN ").append(getQuauqOrderNumSql(where))
			.append("ON sum_total.agentId = sum_orderNum.agentId ");
		//查询where
		sql.append(where);
		return sql.toString();
    }
    
    private String getQuauqOrderNumSql(String queryWhere) {
		
		String where = "pro.orderCompany = agent.id " 
					+ "AND pro.delFlag = '" + Context.DEL_FLAG_NORMAL + "' " 
					+ "AND pro.payStatus != " + Context.ORDER_PAYSTATUS_YQX + " "
					+ "AND pro.payStatus != " + Context.ORDER_PAYSTATUS_DEL + " "
					+ "AND pro.orderCompany IS NOT NULL "
					+ "AND pro.orderStatus = 2 "
					+ "AND agent.is_quauq_agent = 1 "
					+ "AND pro.createBy = users.id "
					+ "AND users.companyId = " + UserUtils.getUser().getCompany().getId() + " ";
		//查询sql语句
		StringBuilder sql = new StringBuilder("");
		sql.append("(SELECT agentId, sum(orderNum) sumOrderNum ")
			.append("FROM ")
				.append("(SELECT agent.id agentId, ifnull(pro.orderPersonNum, 0) orderNum ")
				.append("FROM productorder pro, agentinfo agent, sys_user users ")
				.append("WHERE " + where + queryWhere);
		sql.append(") sumOrderNum GROUP BY agentId").append(") sum_orderNum ");
		return sql.toString();
    }
    
    
	public String getAgentNameById(Long id){;
		return agentinfoDao.getAgentNameById(id);
	}
	
	 public List<Map<Object, Object>> getAllAgentBySql(String sql) {
			return  agentinfoDao.findBySql(sql, Map.class);
	   }
	
	public Agentinfo loadAgentInfoById(Long agentid){
		return agentinfoDao.findOne(agentid);
	}
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delAgentinfo(Long agentid){
		UserUtils.removeCache(AgentInfoUtils.CACHE_AGENT_LIST);
		agentinfoDao.delAgentinfo(agentid);
	}
	
	/**
	 * 
	 * 功能:返回批发商对应的所有渠道商
	 *  
	 * wangxinwei added 2015-10-13
     * 
     * 签证产品预定评审后修改：对代码中的注释进行进准描述，findAllAgentinfo逻辑优化
     * 1.Context.USER_TYPE_RECEPTION,如不是接待社：总社或渠道商登录，则不显示渠道信息
     * 2.如登录用户不是接待社的管理员或销售经理
     *   Context.ROLE_TYPE_MANAGER，
     *   Context.ROLE_TYPE_SALES_EXECUTIVE
     *   则只能看到登录人跟踪的渠道
     * 3.如登录用户为接待社的管理员，财务，或销售主管则显示该公司（供应商）的全部渠道
	 *
	 *  @author zj
	 *  @DateTime 2014-4-1 下午12:25:17
	 *  @return
	 */
	@Transactional(readOnly = true)
	public List<Agentinfo> findAllAgentinfo() {
		List<Agentinfo> agentList = Lists.newArrayList();
		User user = UserUtils.getUser();
	    String userType = user.getUserType();
        Long userCompanyId = user.getCompany().getId();
        //如果不是销售经理或管理员，则用户只能查看自己负责的渠道
        if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
        	Long userId = user.getId();
        	boolean isSaleManager = false;
        	boolean isManager = false;
        	boolean isFinance = false;
        	for(Role role : user.getRoleList()) {
        		if(Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
        			isManager = true;
        		}
        		if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
        			isSaleManager = true;
        		}
        	}
        	List<UserJob> list = userJobDao.getUserJobList(userId);
        	for (UserJob userJob:list) {
        		String jobName = userJob.getJobName();
        		if(jobName.indexOf("财务") != -1){
        			isFinance = true;
        			break;
        		}
			}
        	
        	/**
        	 * wangxinwei modified 2015-10-13
             * 重写上面一段逻辑
        	 */
        	if(isManager||isFinance||isSaleManager){
        		List<Map<String, Object>> agentInfoList = getAgentInfo(userCompanyId);
        		if (CollectionUtils.isNotEmpty(agentInfoList)) {
        			Agentinfo agentInfo;
        			for (Map<String, Object> map : agentInfoList) {
        				agentInfo = new Agentinfo();
        				try {
        					agentInfo.setId(Long.parseLong(map.get("id").toString()));
            				agentInfo.setAgentName(map.get("agentName").toString());
            				
            				
            				//解决线上bug14113
            				agentInfo.setAgentContact(map.get("agentContact")==null?"":map.get("agentContact").toString());
            				agentInfo.setAgentContactMobile(map.get("agentContactMobile")==null?"":map.get("agentContactMobile").toString());
            				agentInfo.setAgentContactTel(map.get("agentContactTel")==null?"":map.get("agentContactTel").toString());
            				agentInfo.setAgentContactFax(map.get("agentContactFax")==null?"":map.get("agentContactFax").toString());
            				agentInfo.setAgentContactEmail(map.get("agentContactEmail")==null?"":map.get("agentContactEmail").toString());
            				agentInfo.setAgentContactQQ(map.get("agentContactQQ")==null?"":map.get("agentContactQQ").toString());
            				agentInfo.setAgentAddress(map.get("agentAddress")==null?"":map.get("agentAddress").toString());
            				
            				// 如果此渠道没有跟进销售，则跳过
            				if (null != map.get("agentSalerId")) {
            					agentInfo.setAgentSalerId(map.get("agentSalerId").toString());
            				}
            				agentList.add(agentInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
        				
        			}
        		}
       		    return agentList;
       	    }else {
//       	    	return agentinfoDao.findAgentBySalerId(userId);
       	    	return findSubstituteAgent(userId);
       	    	
			}
            
        }
	    return agentList;
	}
	

	/**
	 * 不添加角色权限控制，查询出批发商所有渠道
	 * @param userCompanyId
	 * @return
	 */
	public List<Agentinfo> findAllAgentinfosWithoutPemission(Long userCompanyId) {
		List<Agentinfo> agentList = Lists.newArrayList();
		List<Map<String, Object>> agentInfoList = getAgentInfo(userCompanyId);
		if (CollectionUtils.isNotEmpty(agentInfoList)) {
			Agentinfo agentInfo;
			for (Map<String, Object> map : agentInfoList) {
				agentInfo = new Agentinfo();
				try {
					agentInfo.setId(Long.parseLong(map.get("id").toString()));
    				agentInfo.setAgentName(map.get("agentName").toString());
    				agentInfo.setAgentSalerId(map.get("agentSalerId").toString());
    				//修改bug  13451  wangxinwei added 2016-04-14
    				if(!"0".equals(map.get("agentSalerId").toString())&&!"-1".equals(map.get("agentSalerId").toString())){
    					agentInfo.setAgentSalerUser(UserUtils.getUserListByIds(map.get("agentSalerId").toString()));
    				}
    				agentList.add(agentInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return agentList;
	}
	
	@Transactional(readOnly = true)
	public List<Agentinfo> newGetAllAgent() {
		List<Agentinfo> agentList = Lists.newArrayList();
		User user = UserUtils.getUser();
        Long userCompanyId = user.getCompany().getId();
		List<Map<String, Object>> agentInfoList = getAgentInfo(userCompanyId);
		if (CollectionUtils.isNotEmpty(agentInfoList)) {
			Agentinfo agentInfo;
			for (Map<String, Object> map : agentInfoList) {
				agentInfo = new Agentinfo();
				agentInfo.setId(Long.parseLong(map.get("id").toString()));
				agentInfo.setAgentName(map.get("agentName").toString());
				agentList.add(agentInfo);
			}
		}
		return agentList;
	}
	
	@Transactional(readOnly = true)
	private List<Map<String, Object>> getAgentInfo(Long companyId) {
		String Sql = "SELECT id, agentName, agentSalerId,agentContact,agentContactMobile,agentContactTel,agentContactFax,agentContactEmail,agentContactQQ,agentAddress "
				+ " FROM agentinfo WHERE delFlag = 0 AND status = '1' AND supplyId = " + companyId + " ORDER BY agentFirstLetter";
		return  productorderDao.findBySql(Sql, Map.class);
	}
	
	/**
	 * 
	 *  功能:返回批发商对应的所有渠道商
	 *
	 *  @author zj
	 *  @DateTime 2014-4-1 下午12:25:17
	 *  @return
	 */
	public List<Agentinfo> findAllAgentinfoBySalerId(Integer salerId) {
		User user = null;
		if (salerId != null) {
			user = UserUtils.getUser(salerId.longValue());
		} else {
			user = UserUtils.getUser();
		}
	    String userType = user.getUserType();
        //如果不是销售经理或管理员，则用户只能查看自己负责的渠道
        if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
        	Long userId = user.getId();
        	List<Agentinfo> resultAgentinfo = new ArrayList<>();
        	// 由于渠道销售业务含义发生变动，表结构随之调整，此处原方法已不能满足合理的业务逻辑。(下面这些code比较繁冗，使用最下面的方法代替)
//        	Long officeId = user.getCompany().getId();
//        	List<Agentinfo> tempAgentinfos = agentinfoDao.findAgentsByCompanyId(officeId);
//        	if (CollectionUtils.isNotEmpty(tempAgentinfos)) {				
//        		for (Agentinfo agentinfo : tempAgentinfos) {
//        			if (StringUtils.isNotBlank(agentinfo.getAgentSalerId())) {
//        				String[] salerIds = agentinfo.getAgentSalerId().split(",");
//        				if (salerIds != null && salerIds.length > 0) {
//							for (int i = 0; i < salerIds.length; i++) {
//								if (userId.toString().equals(salerIds[i])) {
//									resultAgentinfo.add(agentinfo);
//									break;
//								}
//							}
//						}
//        			}
//        		}
//			}
        	resultAgentinfo = agentinfoDao.findAgentsThatHasTheSalerId("," + userId.toString() + ",");
            return resultAgentinfo;
        	
        }
	    return new ArrayList<Agentinfo>();
	}
	
	/**
	 * 订单修改渠道处待选渠道列表
	 */
	public List<Agentinfo> findAgents4Ordermod(ProductOrderCommon orderCommon) {
		List<Agentinfo> result = new ArrayList<>();
		if (orderCommon == null) {
			return result;
		}
		if (orderCommon.getPriceType() == Context.PRICE_TYPE_QUJ) {  // 如果使用quauq价报名，则取quauq渠道
//			result.addAll(quauqAgentInfoService.getAllQuauqAgentinfos());
			// 暂时不允许修改
			result.add(findOne(orderCommon.getOrderCompany()));
		} else {
			result.addAll(findAllAgentinfoBySalerId(orderCommon.getSalerId()));
		}
	    
	    return result;
	}
	
	/**
	 * 获取（助理的）所有被代替人维护的渠道
	 * @param salerId
	 * @return
	 */
	private List<Agentinfo> findSubstituteAgent(Long salerId) {
		List<Agentinfo> resultList;
		Set<Agentinfo> subAgentinfos = new HashSet<>();
		// 获取所有被代替人
		List<User> allBeSubstitutedUsers = systemService.findAllBeSubstitutes(salerId);
		// 遍历之，查找并添加所有维护的渠道
		if (CollectionUtils.isNotEmpty(allBeSubstitutedUsers)) {
			for (User user : allBeSubstitutedUsers) {				
//				subAgentinfos.addAll(agentinfoDao.findAgentBySalerId(user.getId()));
				subAgentinfos.addAll(agentinfoDao.findAgentsThatHasTheSalerId("," + user.getId().toString() + ","));
			}
		}
		// 添加自己跟进的渠道
//		subAgentinfos.addAll(agentinfoDao.findAgentBySalerId(salerId));
		subAgentinfos.addAll(agentinfoDao.findAgentsThatHasTheSalerId("," + salerId.toString() + ","));
		resultList = new ArrayList<>(subAgentinfos);
		return resultList;
	}
	
	
	
	/**
	 * 
	 *  功能:返回批发商切库存时候显示的渠道列表
	 *  包含非签约渠道、本社
	 *
	 *  @author zj
	 *  @DateTime 2014-4-1 下午12:25:39
	 *  @return
	 */
	public List<Agentinfo> findStockAgentinfo(){
	    String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
            return agentinfoDao.findStockAgentinfo(userCompanyId);
        }
	    return new ArrayList<Agentinfo>();
	}
	
	/**
	 * 获取内部销售(包括一线销售人员)人员的名单
	 */
	public Map<String,String> findInnerSales(Long companyId) {
		Map<String,String> userMap = new HashMap<String,String>();
		List<Object[]> userList = agentinfoDao.getInnerSales(companyId);
		if(!userList.isEmpty()) {
			for(Object[] l:userList) 
				userMap.put(l[0].toString(), l[1].toString());
		}
		return userMap;
	}

	
	/**
	 * 获取批发商下所有拥有计调角色人员的名单
	 * @author shijun.liu 2015.03.26
	 * @param  companyId   批发商ID
	 */
	public Map<String,String> findInnerOperator(Long companyId) {
		Map<String,String> operatorMap = Maps.newHashMap();
		List<Object[]> operatorList = agentinfoDao.getInnerOperator(companyId);
		if(!operatorList.isEmpty()) {
			for(Object[] l:operatorList) 
				operatorMap.put(l[0].toString(), l[1].toString());
		}
		return operatorMap;
	}
	
    public Agentinfo findOne(Long agentId){
    	return this.agentinfoDao.findOne(agentId);
    }

	/**
	 * 根据sys_area的ParentId查当前父节点下的当前子节点，不做递归查询
	 * @param areaParentId 父节点ID
	 * @return  map<地区ID,地区名称>
	 */
	@SuppressWarnings("rawtypes")
    public Map<String,String> findAreaInfo(Long areaParentId){
		Map<String,String> map = new LinkedHashMap<String,String>();
		List<Object[]> areaInfoList = agentinfoDao.getAreaInfo(areaParentId);
		for (Iterator iterator = areaInfoList.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			map.put(objects[0].toString(), objects[1].toString());
		}
		return map;
	}
	/**
	 * 进一步封装区域信息，放入model中
	 * @param model
	 * @param areaParentId
	 * @param key
	 */
	public void addAreaToModel(Model model,Long areaParentId,String key){
		Map<String, String> findAreaInfo = findAreaInfo(areaParentId);
		model.addAttribute(key, findAreaInfo);
	}
	/**
	 * 获取所有结款方式
	 * @param
	 * @return  Map<主键,名称>
	 */
//	public Map<String,String> findAllPaymentType(String paymentType) {
//		Map<String,String> map = new LinkedHashMap<String,String>();
//		List<Object[]> paymentTypeList = agentinfoDao.findAllPaymentType(paymentType);
//		for (Iterator iterator = paymentTypeList.iterator(); iterator.hasNext();) {
//			Object[] objects = (Object[]) iterator.next();
//			map.put(objects[0].toString(), objects[1].toString());
//		}
//		return map;
//	}
	public List<Object[]> findAllPaymentType() {
	//	List<Object[]> paymentTypeList = agentinfoDao.findAllPaymentType(paymentType);
		List<Object[]> paymentTypeList = agentinfoDao.findAllPaymentType();
		//如果是鼎鸿假期  则添加鼎鸿假期特有的结款方式
		if(UserUtils.getUser().getCompany().getUuid().equals("049984365af44db592d1cd529f3008c3")){
			Sysdefinedict sysdefinedict = new Sysdefinedict();
			sysdefinedict.setUuid("049984365af44db592d1cd529f3008c3");
			sysdefinedict.setType("payment_type");
			sysdefinedict.setDefaultFlag("0");
			List<Sysdefinedict> list = sysdefinedictService.find(sysdefinedict);
			for(Sysdefinedict dict :list){
				Object[] tempArray= new Object[2];
				tempArray[0] = dict.getValue();
				tempArray[1] = dict.getLabel();
				paymentTypeList.add(tempArray);
			}
		}
		return paymentTypeList;
	}
	
	public List<Map<String, Object>> findAllAgentinfo(Long supplyId){
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, agentName FROM agentinfo WHERE status = '1' AND delFlag = '0' ")
			.append(" AND supplyId = ? ORDER BY agentFirstLetter ");
		List<Map<String,Object>> dataList = agentinfoDao.findBySql(sql.toString(), Map.class, supplyId);
		//List<Agentinfo> dataList = agentinfoDao.findAllAgentinfo(supplyId);
//		if(CollectionUtils.isNotEmpty(dataList)){
//			for (Map<String, Object> map : dataList){
//				Agentinfo agentinfo = new Agentinfo();
//				agentinfo.setId(Long.valueOf(String.valueOf(map.get("id"))));
//				agentinfo.setAgentName(String.valueOf(map.get("agentName")));
//				list.add(agentinfo);
//			}
//		}
		return dataList;
	}
	/**
	 * @author wuqiang
	 * @since 2015-03-18
	 * 获取批发商下所有拥有计调角色人员的名单
	 */
	public Map<String,String> findInnerJd(Long companyId) {
		Map<String,String> userMap = new HashMap<String,String>();
		List<Object[]> userList = agentinfoDao.getInnerJd(companyId);
		if(!userList.isEmpty()) {
			for(Object[] l:userList) 
				userMap.put(l[0].toString(), l[1].toString());
		}
		return userMap;
	}
	
	/**
	 * 获取可发布产品的用户
	 * @param companyId
	 * @return
	 */
	public Map<String,String> findAllUsers(Long companyId) {
		Map<String,String> userMap = new HashMap<String,String>();
		List<Object[]> userList = agentinfoDao.getAllUsers(companyId);
		if(!userList.isEmpty()) {
			for(Object[] l:userList) 
				userMap.put(l[0].toString(), l[1].toString());
		}
		return userMap;
	}
	
	/**
	 * 
	 * @param companyId
	 * @return
	 */
	public Map<String, String> findSalers(Long companyId) {
		Map<String,String> userMap = new HashMap<String,String>();
		String sql = " SELECT DISTINCT a.id, a.name from (" + 
				" select u.id,u.name from sys_user u where u.delFlag=0 and u.companyId=" + companyId + " and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType in (1, 2)))" +
				" UNION " +
				" select su.id, su.name from productorder o, sys_user su where o.createBy = su.id and su.companyId = " + companyId + " and su.delFlag=0" +
				" UNION " +
				" select su.id, su.name from visa_order o, sys_user su where o.create_by = su.id and su.companyId = " + companyId + " and su.delFlag=0" +
				" UNION " +
				" select su.id, su.name from airticket_order o, sys_user su where o.create_by = su.id and su.companyId = " + companyId + " and su.delFlag=0) a";
		List<Object[]> userList = agentinfoDao.findBySql(sql);
		if(!userList.isEmpty()) {
			for(Object[] l:userList) 
				userMap.put(l[0].toString(), l[1].toString());
		}
		return userMap;
	}
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Agentinfo save(Agentinfo agentInfo) {
		return agentinfoDao.save(agentInfo);
	}

	public Agentinfo findAgentInfoById(Long id) {
		return agentinfoDao.findAgentInfoById(id);
	}
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void updateAgentInfo(Agentinfo ai) {
		 agentinfoDao.save(ai);
//		 String hqlStr = "update Agentinfo set license = ? , set businessLicense = ? ,set taxCertificate=?,set organizeCertificate=? ,set idCard = ? ,set bankOpenLicense = ? ,set travelAptitudes = ?,set elseFile = ? where id = ? ";
//		 agentinfoDao.update(hqlStr, new Object[]{ai.getLicense(),ai.getBusinessLicense(),ai.getTaxCertificate(),ai.getOrganizeCertificate(),ai.getIdCard(),ai.getBankOpenLicense(),ai.getTravelAptitudes(),ai.getElseFile(),ai.getId()});
		 
	}
	/**
	 * 删除联系人(根据联系人的渠道商ID进行删除)
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delSupplyContacts(long id) {
		agentinfoDao.deleteSupplyContacts(id);
	}
	/**
	 * 删除公司LOgo
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delLogo(Long id) {
		docInfoDao.delete(id);
	}
	/**
	 *删除银行
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delBankByAngentinfoId(Long id) {
		agentinfoDao.delBankByAgentinfoId(Context.PLAT_TYPE_QD, id);
	}
	
	/**
	 * 根据联系人主键ID进行删除
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void deleteContactById(long id) {
		supplyContactsDao.delete(id);
	}
	
	/**
	 * 根据银行账户信息主键ID进行删除
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delBankInfoById(long id) {
		platBankInfoDao.delete(id);
	}
	
	/**
	 * 删除资质根据主键删除
	 * @param id
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delAptitudesById(long id) {
		docInfoDao.delete(id);
	}
	
	/**
	 * 所属地国家信息
	 * @return
	 */
	public Map<String,String> findCountryInfo() {
		Map<String,String> map = new HashMap<String,String>();
		List<Object[]> areaInfoList = agentinfoDao.getCountryInfo();
		for (Iterator<Object[]> iterator = areaInfoList.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			map.put(objects[0].toString(), objects[1].toString());
		}
		return map;
	}

	/**
	 * 根据跟进销售人员ID查询（Long）
	 * @param salerId
	 * @return
	 */
	public List<Agentinfo> findAgentBySalerId(Long salerId){
//		return this.agentinfoDao.findAgentBySalerId(salerId);
		List<Agentinfo> returnAgentinfos = agentinfoDao.findAgentsThatHasTheSalerId("," + salerId.toString() + ",");
		return returnAgentinfos;
	}
	
	/**
	 * 根据id集合获取渠道信息集合
	 * @Description: 
	 * @param @param idList
	 * @param @return   
	 * @return List<Agentinfo>  
	 * @throws
	 */
	public List<Agentinfo> findAgentByIdsWithSaler(List<String> idList) {
		List<Agentinfo> baseAgentinfos = this.agentinfoDao.findAgentinfosByIds(idList);
		if (CollectionUtils.isNotEmpty(baseAgentinfos)) {
			for (Agentinfo agentinfo : baseAgentinfos) {
				agentinfo.setAgentSalerUser(UserUtils.getUserListByIds(agentinfo.getAgentSalerId()));
			}
		}
		return baseAgentinfos;
	}
	
	
	/**
	 * 根据渠道集合信息返回固定json格式数据--->例：{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
	 * @Description: 
	 * @param @param agentinfos
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-1 下午6:00:19
	 */
	public String getAgentinfoJsonBean(List<Agentinfo> agentinfos) {
		
		//初始化页面所需的渠道json信息{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
		List<Map<String, Object>> agentJsonInfos = new ArrayList<Map<String, Object>>();
		if(CollectionUtils.isNotEmpty(agentinfos)) {
			for(Agentinfo agentinfo : agentinfos) {
				Map<String, Object> agentJsonInfo = new HashMap<String, Object>();
				agentJsonInfo.put("channelName", agentinfo.getAgentName());
				agentJsonInfo.put("channelCode", agentinfo.getId());
				List<User> salers = agentinfo.getAgentSalerUser();
				agentJsonInfo.put("agentSalerUser", getSalerJsonStr(salers));
				agentJsonInfos.add(agentJsonInfo);
			}
		}
		
		return JSON.toJSONStringWithDateFormat(agentJsonInfos, "yyyy-MM-dd");
	}
	
	/**
	 * userList解析为json ，id和name
	 * @param userList
	 * @return
	 */
	public String getSalerJsonStr(List<User> userList) {
		String salerString = "[";
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				String tempString = "";
				tempString += "{";
				tempString += "id:" + user.getId().toString();
				tempString += ",";
				tempString += "name:\\\"" + user.getName() + "\\\"";
				tempString += ",";
				tempString += "mobile:" + user.getMobile().toString();
				tempString += "}";
				salerString += tempString + ",";
			}
			salerString = salerString.substring(0, salerString.length() - 1);
		}
		salerString += "]";
		return salerString;
	}
	
	/**
	 * 根据渠道商Id 获取地址字符串
	 * @param agentId
	 * @return
	 */
	public String getAddressStrById(Long agentId){
		//国家 + 省份 + 城市 + 街道
		if (agentId == null || agentId == -1) {
			return "";
		}
		Agentinfo agentinfo = findAgentInfoById(agentId);
		if (agentinfo == null) {
			return "";
		}
		String countryCode = agentinfo.getAgentAddress(); //国家
		Long provinceCode = agentinfo.getAgentAddressProvince();  //省份
		Long cityCode = agentinfo.getAgentAddressCity();  //城市
		String street = agentinfo.getAgentAddressStreet();  //街道(直接是名称)
		//由编号转成名称
		String country = "";
		if (StringUtils.isNotBlank(countryCode)) {
			country = areaService.findNameById(Long.parseLong(countryCode));
		}
		String province = areaService.findNameById(provinceCode);
		String city = areaService.findNameById(cityCode);
		
		return (country + province + city + street) == null ? "" : country + province + city + street;
	}
	

	public Long saveAgent(List<OrderContacts> orderCotacts,String orderCompanyNameShow, String salerId){
		Agentinfo agentInfo = null;
		
		//是否需要检查 渠道商是否重名
	/*	agentInfo = this.findAgentInfoByName(orderCompanyNameShow);
		if(agentInfo != null){
			return null;
		}*/
		//第一个
		boolean first = true;
		for(OrderContacts contact:orderCotacts){
			if(first){
				Agentinfo ai = new Agentinfo();
				List<User> salers = new ArrayList<>();
				salers.add(UserUtils.getUser(salerId));
				ai.setAgentSalerUser(salers);//
				ai.setAgentSalerId(salerId);
				ai.setStatus("1");//完全保存了
				ai.setSupplyId(UserUtils.getUser().getCompany().getId());
				List<Object[]> list = this.findAllPaymentType();
				String paymentType = "";
				for(Object[] objs:list){
					if(objs.length >=2 && objs[1].toString().equals("即时结算")){
						paymentType=objs[0].toString();
					}
				}
				ai.setPaymentType(paymentType);
				String agentName = orderCompanyNameShow;
				ai.setAgentBrand(agentName);//品牌
				ai.setAgentName(agentName);//名称
				ai.setAgentNameEn(ChineseToEnglish.getPingYin(agentName));//英文名称
				ai.setAgentContact(contact.getContactsName());
				ai.setAgentContactMobile(contact.getContactsTel());
				ai.setAgentContactTel(contact.getContactsTixedTel());
				ai.setAgentContactFax(contact.getContactsFax());
				ai.setAgentContactQQ(contact.getContactsQQ());
				ai.setAgentContactEmail(contact.getContactsEmail());
				ai.setAgentFirstLetter(ChineseToEnglish.getFirstLetter(agentName));//英文首字母
				agentInfo = this.save(ai);
				first = false;
			}else{
				if(contact.getContactsName() != null && contact.getContactsName() != ""){
					SupplyContacts supplyContacts = new SupplyContacts();
					supplyContacts.setSupplierId(agentInfo.getId());
					supplyContacts.setContactName(contact.getContactsName());
					supplyContacts.setContactPhone(contact.getContactsTixedTel());
					supplyContacts.setContactMobile(contact.getContactsTel());
					supplyContacts.setAgentAddressFull(contact.getContactsAddress());
					supplyContacts.setContactQQ(contact.getContactsQQ());
					supplyContacts.setContactEmail(contact.getContactsEmail());
					supplyContacts.setContactFax(contact.getContactsFax());
					supplyContacts.setType("0");
					supplyContactsService.save(supplyContacts);
				}
			}
		}
		return agentInfo.getId();
	}

	/**
	 * 销售id 数组 ---> string
	 * @return
	 */
	public String getIdStrFromSalerIdArray(String[] salerIdArray) {
		if (salerIdArray == null || salerIdArray.length == 0) {
			return null;
		}
		String idString = "";
		for (int i = 0; i < salerIdArray.length; i++) {
			if (i != 0) {
				idString += ",";
			}
			idString += salerIdArray[i];
		}
		return idString;
	}

	

	/**
	 * 销售id String ---> 数组
	 * @return
	 */
	public String[] getIdArrayFromSalerIdStr(String agentSalerId) {
		if (StringUtils.isBlank(agentSalerId)) {
			return null;
		}
		String[] idArray = agentSalerId.split(",");
		return idArray;
	}

	/**
	 * 获取通讯录
	 * @return
	 */
	public List<Map<String, String>> getAddressList() {
		return agentinfoDao.getAddressList();
	}

	/**
	 * 根据名称  模糊查询quauq渠道
	 * @param name
	 * @param type
	 * @return
	 */
	public List<Agentinfo> getAgentinfoListByName(String name, String type){

		String sql = "SELECT * FROM agentinfo aio WHERE  aio.is_quauq_agent = 1 AND aio.agentName LIKE '%" + name + "%' ORDER BY aio.agentName";
		List<Agentinfo> list = agentinfoDao.findBySql(sql,Agentinfo.class);
		return list;
	}

	/**
	 * 查询所有的quauq渠道的名字
	 * @return
     */
	public List<String> findAllQuauqAgentinfoName(){
		String sql = "SELECT agentName FROM agentinfo WHERE is_quauq_agent = 1 AND enable_quauq_agent = 1 AND delFlag = " + Agentinfo.DEL_FLAG_NORMAL;
		List<String> list = agentinfoDao.findBySql(sql);
		return list;
	}
	
	/**
	 * 获取全部已启用的quauq渠道
	 * */
	public List<Agentinfo> getAllQuauqAgentinfo() {
		String sql = "SELECT * FROM agentinfo a WHERE a.is_quauq_agent = 1 AND a.enable_quauq_agent = 1 AND a.delFlag = 0;";
		List<Agentinfo> list = agentinfoDao.findBySql(sql, Agentinfo.class);
		return list;
	}
	
	/**
	 * 将产品列表或团期列表设为t1默认展示列表
	 * 
	 * */
	public Map<String , String> changeT1ListShowFlag(Long id,Integer t1ListFlag) {
		Map<String, String> result = new HashMap<>();
		int flag = agentinfoDao.changeT1ListShowFlag(id, t1ListFlag);
		if(flag == 1){
			result.put("result", "success");
		}else{
			result.put("result", "fail");
		}
		return result;
	}
	
	/**
	 * 分页查询自有渠道
	 * @param page
	 * @param agentName
	 * @param companyId
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-24
	 */
	public Page<Map<String,Object>> getAgentForSelfByCompanyId(Page<Map<String,Object>> page,String agentName,String companyId){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT id,agentName FROM agentinfo WHERE delFlag = 0 AND `status` = 1 AND supplyId = ? ");
		if(StringUtils.isNotBlank(agentName)){
			sbf.append("AND agentName LIKE '%").append(agentName).append("%' ");
		}
		page =agentinfoDao.findPageBySql(page, sbf.toString(), Map.class, companyId);
		return page;
	}
	
	/**
	 * 分页查询quauq渠道
	 * @param page
	 * @param agentName
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-24
	 */
	public Page<Map<String,Object>> getAgentOfQuauq(Page<Map<String,Object>> page,String agentName){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT a.id,a.agentName FROM agentinfo a WHERE a.is_quauq_agent = 1 AND a.enable_quauq_agent = 1 AND a.delFlag = 0 AND `status` = 1 ");
		if(StringUtils.isNotBlank(agentName)){
			sbf.append("AND a.agentName LIKE '%").append(agentName).append("%' ");
		}
		page =agentinfoDao.findPageBySql(page, sbf.toString(), Map.class);
		return page;
	}
}
