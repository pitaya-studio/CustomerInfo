package com.trekiz.admin.modules.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderListService  extends BaseService {
    
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
    private OrderCommonService orderCommonService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private AgentinfoDao agentinfoDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OfficeDao officeDao;
    
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
    public Page<Map<Object, Object>> findOrderListByPayType(String type, String orderStatus, Page<Map<Object, Object>> page,
    		TravelActivity travelActivity, Map<String,String> map, DepartmentCommon common) {
    	
    	//如果是按团期查询则需要把排序值字段改成以团期表名开头字段
    	String orderBy = map.get("orderBy");
    	if ("group".equals(map.get("orderOrGroup")) && orderBy.contains("pro")) {
    		orderBy = orderBy.replace("pro", "agp");
    	} 
    	page.setOrderBy(orderBy);
    	
    	//sql语句where条件
        String where = this.getTraveActivitySql(orderStatus, travelActivity, map, common);

        //获取订单查询sql语句
        String orderSql = "";
        if (Context.ORDER_PAYSTATUS_ALL.equals(type)) {
        	orderSql = getOrderSql(null, where, orderStatus);
        } else {
        	orderSql = getOrderSql(type, where, orderStatus);
        }
        map.put("orderSql", orderSql);
        
        //如果是订单查询，则直接返回结果，如果是团期查询则需要再处理sql语句
        if ("order".equals(map.get("orderOrGroup"))) {
	        return getOrderList(page, orderSql);
        } else {
        	return this.getGroupListByOrder(page, orderSql);
        }
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
    private String getTraveActivitySql(String orderStatus, TravelActivity travelActivity, Map<String,String> map, DepartmentCommon common) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        //用户类型，分为接待社内部用户和渠道用户，分别为3和1
        String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        Integer orderType =  Integer.parseInt(orderStatus);
        
        //订单类型
		sqlWhere.append(" and pro.orderStatus = " + orderType);
		
        //团期出团日期
        String groupOpenDateBegin = map.get("groupOpenDateBegin");
        String groupOpenDateEnd = map.get("groupOpenDateEnd");
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        if (StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        //产品创建者即计调
        String activityCreate = map.get("activityCreate");
        if (StringUtils.isNotBlank(activityCreate)){
        	sqlWhere.append(" and activity.createBy = " + activityCreate + " ");
        }
        
        //销售
        String salerId = map.get("salerId");
        if (StringUtils.isNotBlank(salerId)){
        	sqlWhere.append(" and pro.salerId = " + salerId + " ");
        }
        
        //下单人
        String proCreateBy = map.get("proCreateBy");
        if (StringUtils.isNotBlank(proCreateBy)){
        	sqlWhere.append(" and pro.createBy = " + proCreateBy + " ");
        }
        
        //订单创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        //区分渠道商用户或批发商用户
        if (Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(userType)) {
        	//渠道商用户
        	if (UserUtils.getUser().getAgentId() != null) {
        		sqlWhere.append(" and pro.orderCompany = " + UserUtils.getUser().getAgentId() + " ");
        	}
        } else if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)) {
            //批发商用户
            sqlWhere.append(" and activity.proCompany = " + userCompanyId + " ");
            
            //渠道
            String agentId = map.get("agentId");
            if(StringUtils.isNotBlank(agentId)) {
            	sqlWhere.append(" and pro.orderCompany = " + agentId);
        	}
        }
        
        //目的地
        String targetAreaIds = travelActivity.getTargetAreaIds();
        if (StringUtils.isNotBlank(targetAreaIds)) {
        	sqlWhere.append(" and exists(select 1 from activitytargetarea where activity.id=activitytargetarea.srcActivityId " +
                    " and activitytargetarea.targetAreaId in(" + targetAreaIds + "))");
        }
        
        //产品名称
        if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
        	sqlWhere.append(" and activity.acitivityName like '%" + travelActivity.getAcitivityName() + "%' ");
        }
        
        //订单号或团号
        String orderNumOrGroupCode = map.get("orderNumOrGroupCode");
        if (StringUtils.isNotBlank(orderNumOrGroupCode)){
        	sqlWhere.append(" and (pro.orderNum like '%" + orderNumOrGroupCode + "%' or agp.groupCode like '%" + orderNumOrGroupCode + "%') ");
        }
        
        //联系人
        String orderPersonName = map.get("orderPersonName");
        if (StringUtils.isNotBlank(orderPersonName)) {
        	sqlWhere.append(" and pro.orderPersonName like '%" + orderPersonName + "%' ");
        }
        //开发票情况(筛选条件： ""0 全部   1未开    2已开）  
        String invoiceStatus = map.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
        	if("1".equals(invoiceStatus)){
        		sqlWhere.append(" and not exists(select 1 from orderinvoice oi where pro.id=oi.orderId " +
                        " and  pro.orderStatus=oi.orderType and oi.createStatus = 1)");
        	}
        	if("2".equals(invoiceStatus)){
        		sqlWhere.append(" and exists(select 1 from orderinvoice oi where pro.id=oi.orderId " +
                        " and  pro.orderStatus=oi.orderType and oi.createStatus = 1)");
        	}
        }
        //开收据情况 (筛选条件：  ""0全部  1未开  2已开)  
        String receiptStatus = map.get("receiptStatus");
        if (StringUtils.isNotBlank(receiptStatus)) {
        	if("1".equals(receiptStatus)){
        		sqlWhere.append(" and not exists(select 1 from orderreceipt oi where pro.id=oi.orderId " +
                        " and pro.orderStatus=oi.orderType and oi.createStatus = 1)");
        	}
        	if("2".equals(receiptStatus)){
        		sqlWhere.append(" and exists(select 1 from orderreceipt oi where pro.id=oi.orderId " +
        				" and pro.orderStatus=oi.orderType and oi.createStatus = 1)");
        	}
        }
        //借款：0 全部 1 审批中 2 已借 3 未借 
        String jiekuanStatus = map.get("jiekuanStatus");
        if (StringUtils.isNotBlank(jiekuanStatus)) {
        	String companyUuid = UserUtils.getUser().getCompany().getUuid();
        	if("1".equals(jiekuanStatus)){
        		sqlWhere.append(" and pro.id in(select order_id from review_new where status = 1 and process_type = '" + Context.REVIEW_FLOWTYPE_BORROWMONEY 
        				+ "' and product_type = '" + orderType + "' and company_id = '" + companyUuid + "')");
        	}
        	if("2".equals(jiekuanStatus)){
        		sqlWhere.append(" and pro.id in(select order_id from review_new where status = 2 and process_type = '" + Context.REVIEW_FLOWTYPE_BORROWMONEY 
        				+ "' and product_type = '" + orderType + "' and company_id = '" + companyUuid + "')");
        	}
        	if("3".equals(jiekuanStatus)){
        		sqlWhere.append(" and pro.id not in(select order_id from review_new where (status = 1 or status = 2) and process_type = '" 
        				+ Context.REVIEW_FLOWTYPE_BORROWMONEY + "' and product_type = '" + orderType + "' and company_id = '" + companyUuid + "')");
        	}
        }
		// 是否确认占位
		String confirmOccupy = map.get("confirmOccupy");
		if(StringUtils.isNotBlank(confirmOccupy)) {
			if("1".equals(confirmOccupy)){
				sqlWhere.append(" and pro.seized_confirmation_status = 1 and pro.payStatus != 99 and pro.payStatus != 111 ");
			}
			if("0".equals(confirmOccupy)){
				sqlWhere.append(" and (pro.seized_confirmation_status <> 1 or isnull(pro.seized_confirmation_status)) and pro.payStatus != 99 and pro.payStatus != 111 ");
			}
		}
		
		//渠道结算方式
		String paymentType = map.get("payamentType");
		if(StringUtils.isNotBlank(paymentType)) {
			sqlWhere.append(" and pro.orderCompany in (select id from agentinfo where paymentType = "+paymentType+")");
		}

		//分部门显示
        systemService.getDepartmentSql("order", null, sqlWhere, common, orderType);
        
        return sqlWhere.toString();
    }
    
    /**
     * 设置查询订单sql语句
     * @param payStatus 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；7 计调占位；99 已取消；111 已删除订单
     * @param where 订单查询where语句
     * @return
     */
    private String getOrderSql(String payStatus, String where, String orderStatus) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Office office = officeDao.findById(companyId);
		String payStatusSql = "";
    	if (payStatus != null) {
				payStatusSql = "AND pro.payStatus = " + payStatus + " ";
    	} else {
			if(office != null) {
				if(office.getIsShowCancelOrder() == 1) {//如果勾选已取消订单
					payStatusSql = " and pro.payStatus != " + Context.ORDER_PAYSTATUS_YQX + " ";
				}
				if(office.getIsShowDeleteOrder() == 1) {//如果勾选已删除订单
					payStatusSql += " and pro.payStatus != " + Context.ORDER_PAYSTATUS_DEL + " ";
				}
			}
    	}
    	
    	//订单查询sql语句
    	StringBuilder sql = new StringBuilder("");
    	String childStr = "LEFT JOIN (SELECT orderid, GROUP_CONCAT(name,',') traName " +
    			"FROM traveler WHERE order_type = "+orderStatus+" AND delFlag IN (0,2,4) GROUP BY orderid) tl ON tl.orderid = pro.id ";
    	sql.append("SELECT activity.activitySerNum, activity.acitivityName, activity.id activityId, activity.createBy activityCreateBy, ")
    				.append("activity.payMode, activity.remainDays, activity.proCompany, users.name AS activityCreateUserName, ")
    				.append("agp.groupOpenDate, agp.groupCloseDate, agp.groupCode, agp.open_date_file AS open_date_file, ")
    				.append("agp.id gruopId, agp.planPosition, agp.freePosition, agp.remarks, agp.lockStatus settleLockStatus, ")
    				.append("pro.id, pro.orderTime, pro.createBy, pro.orderNum, pro.orderSalerId, pro.orderPersonName, pro.orderPersonPhoneNum, ")
    				.append("pro.payStatus, pro.orderCompanyName,  pro.orderPersonNum, pro.confirmationFileId, pro.priceType,")
    				.append("pro.front_money, pro.total_money, pro.quauq_service_charge, pro.payed_money, pro.accounted_money, ")
    				.append("pro.salerId, pro.confirmFlag, pro.salerName, pro.differenceFlag, pro.differenceMoney, ")
    				.append(getMoneySql() + "'' as remainderMoney, " )
    				.append("pro.delFlag AS delFlag, pro.specialDemandFileIds, pro.seized_confirmation_status seizedConfirmationStatus, ")
					.append("pro.placeHolderType, pro.activationDate, pro.seen_flag seenFlag, ")
    				.append("pro.orderCompany, pro.payMode proPayMode, pro.lockStatus,")
    				.append("pro.remainDays proRemainDays, pro.cancel_description AS cancelDescription, pro.paymentType,pro.openNoticeFileId, ")
    				.append("max(limits.applyInvoiceWay) applyInvoiceWay,tl.traName traName,pro.orderStatus orderStatus, ")

    				// 0434需求开始
    				.append("review.group_code,review.order_no ")
    				// 0434需求结束
    				
    			.append("FROM travelactivity activity, ")
    				.append("activitygroup agp, ")
    				.append("sys_user users, ")
    				.append("productorder pro ")
    			.append("LEFT JOIN orderinvoice limits ON limits.orderId = pro.id ")
//    					.append("AND limits.applyInvoiceWay = ( ")
//    					.append("SELECT MAX(applyInvoiceWay) ")
//    					.append("FROM orderinvoice ")
//    					.append("WHERE pro.id = orderId ")
//    					.append(") ")
    					.append(childStr)
    			
	    			.append("LEFT JOIN ( ")
	    			.append("SELECT rev.group_code,rev.order_no,rev.extend_1 ")
	    			.append("FROM review_new rev ")
	    			.append("WHERE rev.process_type = '11' AND rev.status = 2 ")
	    			.append(") review ON review.extend_1 = pro.id ")
    			
	    			//查询包含正常生成订单和待生成订单、未生成订单
    			.append("WHERE (pro.delFlag = " + ProductOrderCommon.DEL_FLAG_NORMAL)
    			.append(" OR pro.delFlag = " + ProductOrderCommon.DEL_FLAG_TO_GENERATE)
    			.append(" OR pro.delFlag = ").append(ProductOrderCommon.DEL_FLAG_NOT_GENERATE).append(") ")
    				.append("AND activity.id = pro.productId ")
    				.append("AND activity.createBy = users.id ")
    				.append("AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId ")
    				.append(payStatusSql)
    				.append(where);
    	return sql.toString();
    }
    
    /**
     * 查询订单列表
     * @param page
     * @param sql
     * @return
     */
    private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, String sql) {
		sql += " GROUP BY pro.id ";
        Page<Map<Object, Object>> pageMap = productorderDao.findPageBySql(page, sql, Map.class);
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
    	orderSql = "SELECT distinct productGroupId, SUM(IFNULL(pro.confirmFlag, 0)) confirmFlag " + orderSql.substring(orderSql.indexOf("FROM travelactivity activity"));
    	orderSql = orderSql.split("LEFT JOIN")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE"))+" GROUP BY productGroupId";
    	
    	//查询团期sql
    	sql.append("SELECT ")
				.append("agp.id, agp.groupCode groupCode, activity.acitivityName, activity.id activityId, activity.createBy createBy, agp.groupOpenDate, ")
				.append("agp.groupCloseDate, agp.planPosition, agp.freePosition, NOW() nowDate,agp.remarks, agp.open_date_file," +
						"activity.activityDuration, activity.activityStatus,tempTablelx.confirmFlag ")
			.append("FROM activitygroup agp, travelactivity activity,  ")
			.append("( "+ orderSql +") tempTablelx ")
			.append("WHERE agp.srcActivityId = activity.id ")
			.append("AND agp.id in ")
			.append("( tempTablelx.productGroupId)");

        Page<Map<Object, Object>> pageMap = productorderDao.findBySql(page, sql.toString(), Map.class);
        
        return pageMap;
    }
    
    /**
     * 根据团期IDS查询订单
     * @param groupList
     * @param orderSql
     * @return 订单列表
     */
    public Page<Map<Object, Object>> findByGroupIds(Page<Map<Object, Object>> page, List<String> groupList, String orderSql) {
    	//给排序值加别名，防止查询过程中出现不认识情况
    	String orderBy = page.getOrderBy();
    	if(StringUtils.isNotBlank(orderBy) && orderBy.indexOf("group") == 0) {
    		orderBy = orderBy.replace("group", "agp.group");
    		page.setOrderBy(orderBy);
    	}
    	//构造sql语句
    	StringBuffer sql = new StringBuffer("");
    	sql.append("SELECT ")
    		.append("pro.productGroupId,pro.createBy,pro.orderNum,pro.orderSalerId,pro.orderPersonPhoneNum,pro.confirmationFileId, pro.total_money, pro.quauq_service_charge,")
    		.append("pro.payStatus,pro.orderCompanyName,pro.orderPersonNum, '' as remainderMoney," + getMoneySql())
    		.append("pro.id,pro.orderTime,pro.activationDate,pro.placeHolderType,pro.payMode, pro.priceType,")
    		.append("pro.orderCompany,pro.lockStatus,")
    		.append("pro.payMode proPayMode,pro.remainDays proRemainDays,pro.cancel_description AS cancelDescription,")
    		.append("pro.seen_flag seenFlag,pro.salerId,pro.salerName, pro.paymentType, pro.confirmFlag, pro.differenceFlag, pro.differenceMoney, ")
    		.append("pro.delFlag as delFlag, pro.specialDemandFileIds, pro.seized_confirmation_status seizedConfirmationStatus,pro.openNoticeFileId, ")
    		.append("agp.lockStatus settleLockStatus,agp.open_date_file AS open_date_file,agp.groupOpenDate,activity.id activityId,tl.traName traName, ")
    		.append("review.group_code,review.order_no,max(limits.applyInvoiceWay) applyInvoiceWay ")
    		.append(orderSql.substring(orderSql.indexOf("FROM travelactivity activity")));
    	
    	//加入团期IDS限制条件
    	StringBuffer groupIdSql = new StringBuffer("");
		for(int i=0;i<groupList.size();i++) {
			if(i != groupList.size()-1) {
				groupIdSql.append(groupList.get(i) + ",");
			} else {
				groupIdSql.append(groupList.get(i));
			}
		}
		sql.append(" and pro.productGroupId in (" + groupIdSql + ")");
		// 0444需求
		sql.append(" GROUP BY pro.id ");
		//查询
		page.setPageSize(300);
		Page<Map<Object, Object>> pageMap = productorderDao.findPageBySql(page, sql.toString(), Map.class);
    	return pageMap;
    }
    
    /**
     * 获取订单查询金额sql
     * @Description 
     * @author yakun.bai
     * @Date 2016-7-7
     */
    private StringBuffer getMoneySql() {
    	// 订单应收金额sql
    	StringBuffer moneySql = new StringBuffer();
    	moneySql.append("(SELECT GROUP_CONCAT(CONCAT (c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId SEPARATOR '+') moneyStr ")
    			.append("FROM money_amount mao, currency c  ")
    			.append("WHERE mao.currencyId = c.currency_id AND mao.serialNum = pro.total_money GROUP BY mao.serialNum) AS totalMoney, ");
    	// 订单实收和达帐sql
    	moneySql.append(moneySql.toString().replace("total", "payed") + moneySql.toString().replace("total", "accounted"));
    	return moneySql;
    }
}