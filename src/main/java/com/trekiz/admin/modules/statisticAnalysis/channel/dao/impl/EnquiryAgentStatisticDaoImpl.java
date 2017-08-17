/**
 * agentPercentChartDaoImpl.java
 */
package com.trekiz.admin.modules.statisticAnalysis.channel.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao;

/**
 * @author junhao.zhao
 *
 * 2017年3月8日  下午6:11:49
 */
@SuppressWarnings("rawtypes")
@Repository
public class EnquiryAgentStatisticDaoImpl extends BaseDaoImpl implements EnquiryAgentStatisticDao{

	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao#getListForAgentPercentChart(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticDaoImpl getListForAgentPercentChart
	 * 
	 * 根据条件查询最多6条-询单-数据
	 * 
	 * 2017年3月9日  上午9:46:48 
	 */
	@Override
	public List<Map<String, Object>> getListForAgentPercentChart(Map<String, Object> map, Long companyId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("ai.agentName AS agentName, ");
		sbf.append("opt.enquiryNum AS enquiryNum ");
		sbf.append("FROM ");
		sbf.append("(SELECT ask_agent_id, COUNT(id) AS enquiryNum ");
		sbf.append("FROM ");
		sbf.append("order_progress_tracking ");
		sbf.append("WHERE company_id = '"+companyId+"' ");
		sbf.append("AND ask_time IS NOT NULL AND ask_num IS NOT NULL ");
		//时间搜索
		timeSearchSql(map, sbf, "ask_time");
		sbf.append("GROUP BY ask_agent_id) opt ");
		sbf.append("LEFT JOIN agentinfo ai ON opt.ask_agent_id=ai.id ");
		
		sbf.append("WHERE ai.is_quauq_agent= '"+Context.QUAUQ_AGENT_YES+"' ");
		
		sbf.append("ORDER BY ");
		
		sbf.append("enquiryNum DESC,CONVERT(agentName USING gbk) ASC LIMIT 0, 6");
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao#getEnquiryTotalNum(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticDaoImpl getEnquiryTotalNum
	 * 查询满足条件的-总询单-条数
	 * 
	 * 2017年3月9日  上午9:45:49
	 */
	@Override
	public List<Map<String, Object>> getEnquiryTotalNum(Map<String, Object> map, Long companyId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("SUM(enquiryNum) AS enquiryTotalNum ");
		sbf.append("FROM ");
		sbf.append("(SELECT id,ask_agent_id, COUNT(id) AS enquiryNum  ");
		sbf.append("FROM ");
		sbf.append("order_progress_tracking ");
		
		sbf.append("WHERE company_id = '"+companyId+"' ");
		sbf.append("AND ask_time IS NOT NULL AND ask_num IS NOT NULL ");
		//时间搜索
		timeSearchSql(map, sbf, "ask_time");
		
		sbf.append("GROUP BY ask_agent_id) opt ");
		sbf.append("LEFT JOIN agentinfo ai ON opt.ask_agent_id=ai.id ");
		sbf.append("WHERE ai.is_quauq_agent= '"+Context.QUAUQ_AGENT_YES+"' ");
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	

	/**
	 * 添加日期搜索条件
	 * @param map
	 * @param sbf
	 */
	private void timeSearchSql(Map<String, Object> map, StringBuffer sbf, String createTime) {
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
			//1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部
			String searchDate = map.get("searchDate").toString();
			String searchDateSimple = map.get("searchDateSimple").toString();
			
			switch (searchDate) {
            case Context.ORDER_DATA_STATISTICS_TODAY:
            case Context.ORDER_DATA_STATISTICS_YESTERDAY:
            	sbf.append("AND DATE_FORMAT("+createTime+", '%Y-%m-%d') = '").append(searchDateSimple).append("' ");
                break;
            case Context.ORDER_DATA_STATISTICS_MONTH:
            case Context.ORDER_DATA_STATISTICS_LAST_MONTH:
            	sbf.append("AND DATE_FORMAT("+createTime+", '%Y-%m') = '").append(searchDateSimple).append("' ");
            	break;
            case Context.ORDER_DATA_STATISTICS_YEAR:
            case Context.ORDER_DATA_STATISTICS_LAST_YEAR:
            	sbf.append("AND DATE_FORMAT("+createTime+", '%Y') = '").append(searchDateSimple).append("' ");
            	break;
               
            default:
                // do nothing
			}
			
		}
		//自定义时间搜索
		if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
			sbf.append("AND DATE_FORMAT("+createTime+", '%Y-%m-%d') >= '").append(map.get("startDate").toString()).append("' ");
		}
		if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
			sbf.append("AND DATE_FORMAT("+createTime+", '%Y-%m-%d') <= '").append(map.get("endDate").toString()).append("' ");
		}
	}


	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao#getListForOrderPercentChart(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticDaoImpl getListForOrderPercentChart
	 * 
	 * 根据条件查询最多6条-订单-数据
	 * 
	 * 2017年3月9日  下午5:14:41 
	 */
	@Override
	public List<Map<String, Object>> getListForOrderPercentChart(Map<String, Object> map, Long companyId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("ai.agentName AS agentName, ");
		sbf.append("orderNum, orderPersonNum, orderMoney ");
		sbf.append("FROM ");
		//调用订单查询中的公用sql拼接
		orderCommonSql(sbf,companyId,map);
		
		sbf.append("ORDER BY ");
		
		//分析类型搜索
		if(map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())){
			String analysisType = map.get("analysisType").toString();
			switch (analysisType) {
			case Context.ORDER_DATA_STATISTICS_ORDER_NUM:
				sbf.append("orderNum");
				break;
			case Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM:
				sbf.append("orderPersonNum");
				break;
			default:
				sbf.append("orderMoney");
			}
					
			sbf.append(" DESC, ");
		}
		
		sbf.append("CONVERT(agentName USING gbk) ASC LIMIT 0, 6");
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao#getOrderTotalNum(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticDaoImpl getOrderTotalNum
	 * 
	 * 查询满足条件的-总订单-条数,总收客人数,总金额
	 * 2017年3月10日  上午9:53:44 
	 */
	@Override
	public List<Map<String, Object>> getOrderTotalNum(Map<String, Object> map, Long companyId) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT SUM(orderNum) AS orderNum,SUM(orderPersonNum) AS orderPersonNum,SUM(orderMoney) AS orderMoney ");
		sbf.append("FROM ");
		//调用订单查询中的公用sql拼接
		orderCommonSql(sbf,companyId,map);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	/**订单查询中的公用sql拼接
	 * @param sbf
	 * @param companyId
	 * @param map
	 */
	private void orderCommonSql(StringBuffer sbf, Long companyId, Map<String, Object> map) {
		sbf.append("(SELECT ");
		sbf.append("COUNT(everyod.id) AS orderNum, ");
		sbf.append("SUM(everyod.orderPersonNum) AS orderPersonNum, ");
		sbf.append("SUM(difMoney) AS orderMoney, everyod.ask_agent_id ");
		
		sbf.append("FROM ");
		sbf.append("(SELECT  ");
		
		sbf.append("SUM(ma.amount * ma.exchangerate)- ");
		sbf.append("IFNULL((SELECT	m.amount * m.exchangerate FROM money_amount m WHERE m.serialNum = po.differenceMoney),0) AS difMoney, ");
		sbf.append("optt.ask_agent_id,po.id,po.orderPersonNum ");
		sbf.append("FROM ");
		sbf.append("(SELECT ");
		sbf.append("opt.order_id, opt.ask_agent_id ");
		sbf.append("FROM ");
		sbf.append("order_progress_tracking opt ");
		sbf.append("WHERE ");
		sbf.append("opt.company_id = '"+companyId+"' ");
		sbf.append("AND opt.ask_time IS NOT NULL AND ask_num IS NOT NULL ");
		sbf.append("AND opt.order_id IS NOT NULL AND opt.order_id <> '') optt ");
		sbf.append("LEFT JOIN productorder po ON optt.order_id = po.id ");
		sbf.append("LEFT JOIN money_amount ma ON po.total_money = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("po.delFlag='"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		sbf.append("AND po.payStatus IN ('"+Context.ORDER_PAYSTATUS_YZW+"','"+Context.ORDER_PAYSTATUS_YZFDJ+"','"+Context.ORDER_PAYSTATUS_YZF+"') ");
		
		//时间搜索
		timeSearchSql(map, sbf, "po.orderTime");
		
		sbf.append("GROUP BY po.id) everyod  ");
		sbf.append("GROUP BY everyod.ask_agent_id) css  ");
		sbf.append("LEFT JOIN agentinfo ai ON ai.id=css.ask_agent_id ");
		sbf.append("WHERE ai.is_quauq_agent = '"+Context.QUAUQ_AGENT_YES+"' ");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
