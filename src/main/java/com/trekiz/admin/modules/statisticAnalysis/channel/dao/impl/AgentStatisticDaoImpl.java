package com.trekiz.admin.modules.statisticAnalysis.channel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.AgentStatisticDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author chao.zhang
 * @date 2016-12-22
 */
@Repository
public class AgentStatisticDaoImpl extends BaseDaoImpl implements AgentStatisticDao{
	/**
	 * 渠道分析列表查询
	 * @param page
	 * @param map 存在查询条件
	 * @param oderBy 排序
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	@Override
	public Page<Map<String, Object>> getAgentStatisticDatas(
			Page<Map<String, Object>> page, Map<String, Object> map,
			String orderBy) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("t.agentId, ");
		sbf.append("CASE ");
		sbf.append("WHEN t.agentName = '' THEN ");
		sbf.append("' ' ");
		sbf.append("ELSE ");
		sbf.append("t.agentName ");
		sbf.append("END AS agentName, ");
		sbf.append("t.orderNum orderNum, ");
		sbf.append("FORMAT(IFNULL(t.orderMoney,0),2) orderMoney, ");
		sbf.append("IFNULL(t.orderPersonNum,0) orderPersonNum, ");
		sbf.append("t.currencyMark ");
		sbf.append("FROM ");
		sbf.append("( ");
		sbf.append("SELECT ");
		sbf.append("ai.id AS agentId, ");
		sbf.append("ai.agentName AS agentName, ");
		sbf.append("COUNT(o.id) AS orderNum, ");
		sbf.append("SUM(o.amount) AS orderMoney, ");
		sbf.append("SUM(o.order_person_num) AS orderPersonNum, ");
		sbf.append("'￥' AS currencyMark ");
		sbf.append("FROM ");
		sbf.append("agentinfo ai ");
		sbf.append("LEFT JOIN order_data_statistics o ON ai.id = o.agentinfo_id ");
		sbf.append("AND o.del_flag = 0 ");
		sbf.append("AND CASE ");
		sbf.append("WHEN order_type = 6 THEN ");
		sbf.append("o.order_status NOT IN(2,99) ");
		sbf.append("ELSE ");
		sbf.append("o.order_status NOT IN (99, 111) ");
		sbf.append("END ");
		sbf.append("AND o.agentinfo_id <> - 1 ");
		sbf.append("AND o.order_type <> 7 ");
		sbf.append("AND o.company_uuid = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
		whereSql(map, sbf);
		sbf.append("WHERE ");
		sbf.append("ai.delFlag = 0 ");
		sbf.append("AND ai.supplyId = '").append(UserUtils.getUser().getCompany().getId()).append("' ");
		sbf.append("AND ai.`status` = 1 ");
		sbf.append("GROUP BY ");
		sbf.append("ai.id ");
		sbf.append("UNION ");
		sbf.append("SELECT ");
		sbf.append("ai.id AS agentId, ");
		sbf.append("ai.agentName AS agentName, ");
		sbf.append("COUNT(o.id) AS orderNum, ");
		sbf.append("SUM(o.amount) AS orderMoney, ");
		sbf.append("SUM(o.order_person_num) AS orderPersonNum, ");
		sbf.append("'￥' AS currencyMark ");
		sbf.append("FROM ");
		sbf.append("agentinfo ai ");
		sbf.append("LEFT JOIN order_data_statistics o ON ai.id = o.agentinfo_id ");
		sbf.append("AND o.del_flag = 0 ");
		sbf.append("AND CASE ");
		sbf.append("WHEN order_type = 6 THEN ");
		sbf.append("o.order_status NOT IN (2, 99) ");
		sbf.append("ELSE ");
		sbf.append("o.order_status NOT IN (99, 111) ");
		sbf.append("END ");
		sbf.append("AND o.agentinfo_id <> - 1 ");
		sbf.append("AND o.order_type <> 7 ");
		sbf.append("AND o.company_uuid = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
		whereSql(map, sbf);
		sbf.append("WHERE ai.delFlag = 0 ");
		sbf.append("AND ai.`status` = 1 ");
		sbf.append("AND ai.is_quauq_agent = 1 ");
		sbf.append("AND ai.enable_quauq_agent = 1 ");
		sbf.append("GROUP BY ");
		sbf.append("ai.id ");
		sbf.append("UNION ");
		sbf.append("SELECT ");
		sbf.append("- 1 AS agentId, ");
		sbf.append("'非签约渠道' AS agentName, ");
		sbf.append("COUNT(o.id) AS orderNum, ");
		sbf.append("SUM(o.amount) AS orderMoney, ");
		sbf.append("SUM(o.order_person_num) AS orderPersonNum, ");
		sbf.append("'￥' AS currencyMark ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics o ");
		sbf.append("WHERE ");
		sbf.append("o.del_flag = 0 ");
		sbf.append("AND CASE ");
		sbf.append("WHEN o.order_type = 6 THEN ");
		sbf.append("o.order_status NOT IN (2, 99) ");
		sbf.append("ELSE ");
		sbf.append("o.order_status NOT IN (99, 111) ");
		sbf.append("END ");
		sbf.append("AND o.agentinfo_id = - 1 ");
		sbf.append("AND o.order_type <> 7 ");
		sbf.append("AND o.company_uuid = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
		whereSql(map, sbf);
		sbf.append("GROUP BY ");
		sbf.append("o.agentinfo_id ");
		sbf.append(") t ");
		sbf.append("WHERE ");
		sbf.append("1 = 1 ");
		createTwhereSql(map,sbf);
		//输入框渠道名称搜索
		if(map.get("searchValue") != null && StringUtils.isNotBlank(map.get("searchValue").toString())){
			sbf.append("AND t.agentName LIKE '%").append(map.get("searchValue").toString()).append("%' ");
		}
		sbf.append(orderBy);
		page = this.findPageBySql(page, sbf.toString(),Map.class);
		return page;
	}
	
	/**
	 * 订单数搜索，收客人数搜索，订单金额搜索过滤条件
	 * @param map
	 * @param sbf
	 * @author chao.zhang
	 * @data 2016-12-29
	 */
	private void createTwhereSql(Map<String,Object> map,StringBuffer sbf){
		//订单数搜索
		if(map.get("orderNumBegin") != null && StringUtils.isNotBlank(map.get("orderNumBegin").toString())){
			sbf.append("AND t.orderNum >= ").append(map.get("orderNumBegin").toString()).append(" ");
		}
		if(map.get("orderNumEnd") != null && StringUtils.isNotBlank(map.get("orderNumEnd").toString())){
			sbf.append("AND t.orderNum <= ").append(map.get("orderNumEnd").toString()).append(" ");
		}
		//收客人数搜索
		if(map.get("orderPersonNumBegin") != null && StringUtils.isNotBlank(map.get("orderPersonNumBegin").toString())){
			sbf.append("AND t.orderPersonNum >= ").append(map.get("orderPersonNumBegin").toString()).append(" ");
		}
		if(map.get("orderPersonNumEnd") != null && StringUtils.isNotBlank(map.get("orderPersonNumEnd").toString())){
			sbf.append("AND t.orderPersonNum <= ").append(map.get("orderPersonNumEnd").toString()).append(" ");
		}
		//订单金额搜索
		if(map.get("orderMoneyBegin") != null && StringUtils.isNotBlank(map.get("orderMoneyBegin").toString())){
			sbf.append("AND t.orderMoney >= ").append(map.get("orderMoneyBegin").toString()).append(" ");
		}
		if(map.get("orderMoneyEnd") != null && StringUtils.isNotBlank(map.get("orderMoneyEnd").toString())){
			sbf.append("AND t.orderMoney <= ").append(map.get("orderMoneyEnd").toString()).append(" ");
		}
	}
	
	/**
	 * 组装where后 查询条件
	 * @param map
	 * @param sbf
	 * @data 2016-12-22
	 */
	private void whereSql(Map<String,Object> map,StringBuffer sbf){
			//订单类型
			if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString()) && ! map.get("orderType").toString().equals("0")){
				sbf.append("AND order_type = ").append(map.get("orderType").toString()).append(" ");
			}
			//时间搜索
			if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
				//1：今日 2：本周 3：本月 4：本年 5： 全部
				String searchDate = map.get("searchDate").toString();
				String  searchDateReal = map.get("searchDateReal").toString();
				if(searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
					sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') ='").append(searchDateReal).append("' ");
				}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
					sbf.append("AND order_createtime>=(select subdate(curdate(),date_format(curdate(),'%w')-1))").append(" ");
					sbf.append(" AND order_createtime<=(select subdate(curdate(),date_format(curdate(),'%w')-7))").append(" ");
				}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
					sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m') = '").append(searchDateReal).append("' ");
				}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
					sbf.append("AND DATE_FORMAT(order_createtime, '%Y') = '").append(searchDateReal).append("' ");
				}
			}
			//自定义时间搜索
			if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') >= '").append(map.get("startDate").toString()).append("' ");
			}
			if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') <= '").append(map.get("endDate").toString()).append("' ");
			}
	}
	
	/**
	 * 查询订单总数，订单总额，收客总人数
	 * @author chao.zhang
	 * 2016-12-22
	 */
	@Override
	public Map<String, Object> getTotalNum(Map<String, Object> map) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT IFNULL(SUM(t.orderNum),0) orderTotalNum,FORMAT(IFNULL(SUM(t.orderMoney),0), 2) orderTotalMoney, ");
		sbf.append("IFNULL(SUM(t.orderPersonNum),0) orderTotalPersonNum FROM( ");
		sbf.append("SELECT ");
		sbf.append("COUNT(o.id) AS orderNum,IFNULL(SUM(o.amount),0) AS orderMoney, ");
		sbf.append("IFNULL(SUM(o.order_person_num),0) AS orderPersonNum ");
		sbf.append("FROM ");
		sbf.append("agentinfo ai LEFT JOIN ");
		sbf.append("order_data_statistics o ");
		sbf.append("on ai.id = o.agentinfo_id ");
		sbf.append("WHERE del_flag = 0 ");
		sbf.append("AND o.order_type <> 7 ");
		sbf.append("AND CASE WHEN order_type = 6 THEN order_status NOT IN(2,99) ELSE order_status NOT IN(99,111) END  ");
		sbf.append("AND company_uuid='").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
		whereSql(map,sbf);
		//输入框渠道名称搜索
		if(map.get("searchValue") != null && StringUtils.isNotBlank(map.get("searchValue").toString())){
			sbf.append("AND ai.agentName  LIKE '%").append(map.get("searchValue").toString()).append("%' ");
		}
		sbf.append("GROUP BY agentinfo_id ");
		sbf.append(")t ");
		sbf.append("WHERE 1=1 ");
		createTwhereSql(map,sbf);
		List<Map<String,Object>> list = this.findBySql(sbf.toString(), Map.class);
		if(list.size() > 0 ){
			list.get(0).put("orderTotalMoney", "￥"+list.get(0).get("orderTotalMoney").toString());
			return list.get(0);
		}
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("orderTotalNum", 0);
		map1.put("orderTotalMoney", "￥"+0.00);
		map1.put("orderTotalPersonNum", 0);
		return map1;
	}
	
	/**
	 * 渠道分析图表的数据查询
	 * @param map
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-23
	 */
	@Override
	public List<Map<String, Object>> getListForOrderType(Map<String, Object> map) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("agentinfo_id AS agentId, ");
		sbf.append("COUNT(o.id) AS orderNum, ");
		sbf.append("SUM(amount) AS orderMoney, ");
		sbf.append("SUM(order_person_num) AS orderPersonNum, ");
		sbf.append("CASE WHEN agentinfo_id = - 1 THEN'非签约渠道' ELSE ai.agentName END AS agentName ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics o LEFT JOIN agentinfo ai ON o.agentinfo_id = ai.id ");//查询order_data_statistics订单数据分析表
		sbf.append("WHERE del_flag = 0 ");
		sbf.append("AND order_type <> 7 ");
		sbf.append("AND CASE WHEN order_type = 6 THEN order_status != 2 ELSE order_status NOT IN(99,111) END ");
		sbf.append("AND company_uuid='").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
		whereSql(map, sbf);
		sbf.append("GROUP BY ");
		sbf.append("agentinfo_id ");
		sbf.append("ORDER BY ");
		String analysisType = map.get("analysisType").toString();
		if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){//根据不同类型按照来进行倒序
			sbf.append("COUNT(o.id)");
		}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
			sbf.append("SUM(order_person_num)");
		}else{
			sbf.append("SUM(amount)");
		}
		sbf.append(" DESC ");
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
}
