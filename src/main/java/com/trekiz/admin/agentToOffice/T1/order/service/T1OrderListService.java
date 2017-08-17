package com.trekiz.admin.agentToOffice.T1.order.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class T1OrderListService  extends BaseService {
    
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
	private AgentinfoService agentinfoService;

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
    public Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page,
    		TravelActivity travelActivity, Map<String,String> map, String orderStatus) {
    	
    	//如果是按团期查询则需要把排序值字段改成以团期表名开头字段
    	String orderBy = map.get("orderBy");
    	if ("group".equals(map.get("orderOrGroup")) && orderBy.contains("pro")) {
    		orderBy = orderBy.replace("pro", "agp");
    	} 
    	page.setOrderBy(orderBy);
    	
    	//sql语句where条件
        String where = this.getTraveActivitySql(travelActivity, map, orderStatus);

        //获取订单查询sql语句
        String orderSql = "";
        if (Context.ORDER_PAYSTATUS_ALL.equals(orderStatus)) {
        	orderSql = getOrderSql(where, null);
        } else {
        	orderSql = getOrderSql(where, orderStatus);
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
     * @author yakun.bai
     * @Date 2016-5-10
     */
    private String getTraveActivitySql(TravelActivity travelActivity, Map<String,String> map, String orderStatus) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        // 订单类型
		sqlWhere.append(" and pro.orderStatus = " + Context.ORDER_TYPE_SP);
		
		// 当前用户只能查看自己渠道订单
		Long agentId = UserUtils.getUser().getAgentId();
		Agentinfo agentinfo = agentinfoService.findOne(agentId);
		if(Agentinfo.T1_AGENT_TYPE_STORES.equals(agentinfo.getAgentType())){
			// 当前用户所属渠道时门店的话只能查看自己渠道订单
			sqlWhere.append(" and userTwo.id = ").append(UserUtils.getUser().getId());
		}else{
			//总社和集团公司可以查看自己的订单以及下面门店的订单
			sqlWhere.append(" AND exists (SELECT id FROM agentinfo r WHERE (r.id = ").append(agentId)
					.append(" OR r.agent_parent = ").append(agentId).append(") ")
					.append(" AND r.id = userTwo.agentId) ");
		}

        // 产品名称或团期团号
        String orderNumOrProductNameOrGroupCode = map.get("orderNumOrProductNameOrGroupCode");
        if (StringUtils.isNotBlank(orderNumOrProductNameOrGroupCode)){
        	sqlWhere.append(" and (activity.acitivityName like '%" + orderNumOrProductNameOrGroupCode + "%' or agp.groupCode like '%" + orderNumOrProductNameOrGroupCode + "%' or pro.orderNum like '%" + orderNumOrProductNameOrGroupCode + "%') ");
        }
        
        // 出发城市
        Integer fromArea = travelActivity.getFromArea();
        if (fromArea != null) {
        	sqlWhere.append(" and activity.fromArea = " + fromArea + " ");
        }
        
        //支付状态(处理标题栏上的payStatus)
        String payStatus = map.get("payStatus");
        if(StringUtils.isNotBlank(payStatus)){
        	sqlWhere.append(" and pro.payStatus = '" + payStatus + "'");
        }
     
        //支付金额搜索
        String moneyStrMin = map.get("moneyStrMin");
        String moneyStrMax = map.get("moneyStrMax");
        if(StringUtils.isNotBlank(moneyStrMax)){
        	sqlWhere.append(" and totalOuter.moneyStrMin <= '" + moneyStrMax + "'");
        }
        if(StringUtils.isNotBlank(moneyStrMin)){
        	sqlWhere.append(" and totalOuter.moneyStrMax >= '" + moneyStrMin + "'");
        }
        
        
        // 目的地
        String targetAreaIds = travelActivity.getTargetAreaIds();
        if (StringUtils.isNotBlank(targetAreaIds)) {
        	sqlWhere.append(" and exists(select 1 from activitytargetarea where activity.id=activitytargetarea.srcActivityId " +
                    " and activitytargetarea.targetAreaId in(" + targetAreaIds + "))");
        }
        
        // 供应商
        String supplierId = map.get("supplierId");
        if (StringUtils.isNotBlank(supplierId)){
        	supplierId = "("+ supplierId.substring(0, supplierId.length()) + ")";
        	sqlWhere.append(" and office.id in " + supplierId + " ");
        }
        // 订单状态
        String orderType = map.get("orderType");
        if (StringUtils.isNotBlank(orderType)){
        	orderType = "("+ orderType.substring(0, orderType.length()) + ")";
        	sqlWhere.append(" and pro.payStatus in " + orderType + " ");
        }
        // 订单是否已查看
        String hasSeen = map.get("hasSeen");
        if (StringUtils.isNotBlank(hasSeen)){
        	sqlWhere.append(" and pro.hasSeen = '" + hasSeen + "' ");
        }
        
        // 订单创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        // 团期出团日期
        String groupOpenDateBegin = map.get("groupOpenDateBegin");
        String groupOpenDateEnd = map.get("groupOpenDateEnd");
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        if (StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        //批发商上架权限
        sqlWhere.append(" and agp.delFlag = 0 ") //产品是上架状态
        //.append("  and (office.shelfRightsStatus=0  ) ")  //开发商有上架权限,0启用  1禁用
        .append(" and (agp.quauqAdultPrice is not null or agp.quauqChildPrice is not null or agp.quauqSpecialPrice is not null) "); //有quauq价
        
        return sqlWhere.toString();
    }
    
    /**
     *  订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；7 计调占位；99 已取消；111 已删除订单
     * 设置查询订单sql语句
     * @param orderStatus
     * @param where 订单查询where语句
     * @return
     */
    private String getOrderSql(String where, String orderStatus) {
		String payStatusSql = "";
    	
    	
    	//订单查询sql语句
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT pro.id, pro.orderNum, pro.orderTime orderTime,pro.orderPersonNum, pro.orderPersonNumAdult, pro.orderPersonNumChild, pro.orderPersonNumSpecial, ")
					.append("pro.payStatus orderStatus,pro.confirmationFileId, pro.differenceFlag, pro.differenceMoney, users.name salerName, users.mobile salerMobile, ")
					.append("totalOuter.moneyStr AS totalMoney, accountedOuter.moneyStr AS accountedMoney, ")
					.append("activity.id activityId, activity.acitivityName, ")
					.append("agp.groupCode, agp.groupOpenDate, agp.freePosition, agp.is_t1 isT1, ")
					.append("office.name officeName, ")
					.append("totalOuter.moneyStrMin, ")
					.append("totalOuter.moneyStrMax ")
				.append("FROM travelactivity activity, ")
					.append("activitygroup agp, ")
					.append("sys_office office, ")
					.append("sys_user users, ")
					.append("sys_user userTwo, ")
					.append("agentinfo agent, ")
					.append("productorder pro ") 

				//订单应收金额多币种查询
				.append(" LEFT JOIN ( ")
					.append("SELECT mao.serialNum, ")
						.append("MIN(mao.amount ) as moneyStrMin, ")
						.append("MAX(mao.amount ) as moneyStrMax, ")
						.append("GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_YSH + " ")
			    			.append("AND mao.orderType = " + Context.ORDER_STATUS_LOOSE + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
					.append("GROUP BY mao.serialNum ")
					.append(") totalOuter ON totalOuter.serialNum = pro.total_money ")
					
				//订单已达帐金额多币种查询
				.append("LEFT JOIN ( ")
					.append("SELECT mao.serialNum ")
						.append(",GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_DZ + " ")
			    			.append("AND mao.orderType = " + Context.ORDER_STATUS_LOOSE + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
					.append("GROUP BY mao.serialNum ")
					.append(") accountedOuter ON accountedOuter.serialNum = pro.accounted_money ")
				
					//查询包含正常生成订单和待生成订单、未生成订单
				.append("WHERE (pro.delFlag = " + ProductOrderCommon.DEL_FLAG_NORMAL)
				.append(" OR pro.delFlag = " + ProductOrderCommon.DEL_FLAG_TO_GENERATE)
				.append(" OR pro.delFlag = ").append(ProductOrderCommon.DEL_FLAG_NOT_GENERATE).append(") ")
					.append("AND activity.id = pro.productId ")
					.append("AND office.id = activity.proCompany ")
					.append("AND pro.salerId = users.id ")
					.append("AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId ")
					.append("AND pro.orderCompany = agent.id AND agent.is_quauq_agent = '1' ")
					.append("AND agent.id = userTwo.agentId ")
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
    	orderSql = "SELECT distinct productGroupId " + orderSql.substring(orderSql.indexOf("FROM"));
    	orderSql = orderSql.split("LEFT JOIN")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE"));
    	
    	//查询团期sql
    	sql.append("SELECT ")
				.append("agp.id, agp.groupCode groupCode, activity.acitivityName, activity.id activityId, agp.groupOpenDate, ")
				.append("agp.freePosition, agp.open_date_file, activity.activityDuration, activity.activityStatus ")
			.append("FROM activitygroup agp, travelactivity activity ")
			.append("WHERE agp.srcActivityId = activity.id ")
			.append("AND agp.id in ")
			.append("(")
			.append(orderSql)
			.append(")");
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
    	sql.append("SELECT pro.id, pro.orderNum, pro.orderTime, pro.orderPersonNumAdult, pro.orderPersonNumChild, pro.orderPersonNumSpecial, ")
					.append("pro.productGroupId, pro.payStatus payStatus, pro.orderStatus orderStatus, users.name salerName, users.mobile salerMobile, ")
					.append("totalOuter.moneyStr AS totalMoney, payedOuter.moneyStr AS payedMoney, accountedOuter.moneyStr AS accountedMoney, ")
					.append("activity.id activityId, activity.acitivityName, ")
					.append("agp.groupCode, agp.groupOpenDate, agp.freePosition, ")
					.append("office.name officeName ")
    		.append(orderSql.substring(orderSql.indexOf("FROM")));
    	
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
		//查询
		page.setPageSize(300);
		Page<Map<Object, Object>> pageMap = productorderDao.findPageBySql(page, sql.toString(), Map.class);
    	return pageMap;
    }

	private void updateBatch(List<ProductOrderCommon> pOrderList) {
		for(ProductOrderCommon order:pOrderList){
			productorderDao.updateObj(order);
		}
	}

	private ProductOrderCommon getEntity(String string) {
		return productorderDao.getById(new Long(string));
	}
	
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void updateOrderSeenBatch(HttpServletResponse response, HttpServletRequest request, Map<String, String> mapRequest) {
		Page<Map<Object, Object>> pageOrder = getOrderList(new Page<Map<Object, Object>>(request,response,-1), new TravelActivity(), mapRequest, "0");
		List<ProductOrderCommon> pOrderList = new LinkedList<ProductOrderCommon>();
		for(Map<Object, Object> obj:pageOrder.getList()){
			ProductOrderCommon pOrder = getEntity(obj.get(new String("id")).toString());
			pOrder.setHasSeen("0");
			pOrderList.add(pOrder);
		}
		updateBatch(pOrderList);
	}
	
}