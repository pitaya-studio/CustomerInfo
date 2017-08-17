/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.CountAndRateDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author junhao.zhao
 * 2016年12月29日  下午6:07:48
 */
@Repository
public class CountAndRateDaoImpl extends BaseDaoImpl<OrderDataStatistics> implements CountAndRateDao{

	@Autowired
	private AskOrderNumDao askOrderNumDao;
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.CountAndRateDao#getCountAndRate(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getCountAndRate(Map<String, String> map) {
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("COUNT(id) AS orderNum, ");
		sbf.append("SUM(amount) AS orderMoney, ");
		sbf.append("SUM(order_person_num) AS orderPersonNum ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics ods ");
		sbf.append("WHERE del_flag = '"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		sbf.append("AND ods.company_uuid = '"+companyUuid+"' ");
		sbf.append("AND CASE WHEN order_type = '"+Context.ORDER_STATUS_VISA+"' THEN order_status != '"+Context.VISA_ORDER_PAYSTATUS_CANCEL+"' ELSE order_status NOT IN('"+Context.ORDER_PAYSTATUS_YQX+"','"+Context.ORDER_PAYSTATUS_DEL+"') END ");
		//订单类型
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString()) && ! map.get("orderType").toString().equals("0")){
			sbf.append("AND order_type = ").append(map.get("orderType").toString()).append(" ");
		}
		//时间搜索
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
			//对时间处理
			String searchDateType = map.get("searchDate");//时间类型
			String searchDate = "";
			Date date = new Date();
			SimpleDateFormat format = null;
			//1：今日 2：本周 3：本月 4：本年 5：全部
			if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
				format = new SimpleDateFormat("YYYY-MM-dd");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
				format = new SimpleDateFormat("YYYY-MM-dd");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
				format = new SimpleDateFormat("YYYY-MM");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
				format = new SimpleDateFormat("YYYY");
				searchDate = format.format(date);
			}else{
				searchDate = "";
			}
			map.put("searchDate", searchDateType);//时间类型
			//防止本周期与上周期混淆，有值，就是上周期
			if(map.get("searchDateSimple") == null || StringUtils.isBlank(map.get("searchDateSimple").toString())){
				map.put("searchDateSimple",searchDate);//精简后的时间
			}
			
			//时间处理完成
			
			//1：今日 2：本周 3：本月 4：今年  5：全部
			String  searchDateSimple = map.get("searchDateSimple");
			if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') ='").append(searchDateSimple).append("' ");
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
				sbf.append("AND DATE_FORMAT(order_createtime,'%x%v') = DATE_FORMAT('").append(searchDateSimple).append("','%x%v') ");
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m') = '").append(searchDateSimple).append("' ");
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y') = '").append(searchDateSimple).append("' ");
			}
		}
		//自定义时间搜索
		if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
			sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') >= '").append(map.get("startDate").toString()).append("' ");
		}
		if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
			sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') <= '").append(map.get("endDate").toString()).append("' ");
		}
	
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getPreOrderCountAndRate(Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId(); // 公司Id
		StringBuffer sql = new StringBuffer();
    	sql.append("    SELECT ");
    	sql.append(" 		   count(optPreOrder.id) AS orderPreNum ");
    	sql.append("      FROM order_progress_tracking optPreOrder ");
    	sql.append("     WHERE optPreOrder.company_id = '").append(companyId).append("' ");
    	sql.append(" 	   AND optPreOrder.ask_num IS NOT NULL ");
    	sql.append(" 	   AND optPreOrder.ask_time IS NOT NULL ");
		// 时间搜索条件追加
    	appendDateCondition(sql, map, "optPreOrder.ask_time");
		List<Map<String, Object>> list = this.findBySql(sql.toString(), Map.class);

		return list;
	}
	
	@Override
	public List<Map<String, Object>> getOrderCountAndRate(Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId(); // 公司Id
		StringBuffer sql = new StringBuffer();
    	sql.append("     SELECT ");
    	sql.append(" 		    COUNT(optOrder.order_id) AS orderNum, ");
    	sql.append(" 			SUM( ");
    	sql.append(" 					(SELECT IFNULL(SUM(ma.amount * ma.exchangerate), 0) "); // 订单总额
    	sql.append(" 		   			   FROM money_amount ma ");
    	sql.append("         			  WHERE ma.serialNum = odr.total_money) ");
    	sql.append(" 					 - ");
    	sql.append("        			(SELECT IFNULL(SUM(ma.amount * ma.exchangerate), 0) "); // 差额返还
    	sql.append("  		   			   FROM money_amount ma ");
    	sql.append(" 		  			  WHERE ma.serialNum = odr.differenceMoney) ");
    	sql.append("     			) AS orderMoney, ");
    	sql.append("            IFNULL(SUM(odr.orderPersonNum), 0) AS orderPersonNum ");
    	sql.append("       FROM order_progress_tracking optOrder ");
    	sql.append(" INNER JOIN productorder odr  ");
    	sql.append(" 	   	 ON optOrder.order_id = odr.id ");
    	sql.append(" 	    AND odr.delFlag = 0 ");
    	sql.append(" 	    AND odr.orderStatus = 2 "); // 现在只有散拼
    	sql.append(" 	    AND odr.payStatus IN (3, 4, 5) ");
    	sql.append("      WHERE optOrder.order_id != ''  ");
    	sql.append(" 	    AND optOrder.order_id IS NOT NULL ");
    	sql.append(" 	    AND optOrder.ask_num IS NOT NULL ");
    	sql.append(" 	    AND optOrder.ask_time IS NOT NULL ");
    	sql.append(" 	    AND optOrder.company_id = '").append(companyId).append("' ");
		// 时间搜索条件追加
    	appendDateCondition(sql, map, "optOrder.order_create_time");
		List<Map<String, Object>> list = this.findBySql(sql.toString(), Map.class);

		return list;
	}

	/**
     * 追加时间搜索条件(1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部)
     * @author gaoyang
     * @Time 2017-3-9 下午6:11:39
     * @param sql
     * @param param
     * @param column 要搜索的字段名称
     */
    private void appendDateCondition(StringBuffer sql, Map<String, String> map, String column) {
        
        //自定义时间搜索
        if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
        	sql.append(" AND " + column + " >= '").append(map.get("startDate")).append(" 00:00:00'");
		}
		if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
			sql.append(" AND " + column + " <= '").append(map.get("endDate")).append(" 23:59:59'");
		}
        
		// 1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())) {
			if (StringUtils.isBlank(map.get("startDate").toString()) 
	        		&& StringUtils.isBlank(map.get("endDate").toString())) {
				String searchDate = map.get("searchDate").toString();
	        	// 1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部
	        	switch (searchDate) {
	        		case Context.ORDER_DATA_STATISTICS_TODAY: // 今日
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m-%d') = '")
	                       .append(askOrderNumDao.getTime("yyyy-MM-dd") + "' ");
	                    break;
	                  case Context.ORDER_DATA_STATISTICS_YESTERDAY: // 昨日
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m-%d') = '")
	                       .append(askOrderNumDao.getYesterday() + "' ");
	                    break;
	                  case Context.ORDER_DATA_STATISTICS_MONTH: // 本月
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m') = '")
	                       .append(askOrderNumDao.getTime("yyyy-MM") + "' ");
	                    break;
	                  case Context.ORDER_DATA_STATISTICS_LAST_MONTH: // 上月
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m') = '")
	                       .append(askOrderNumDao.getLastMonth() + "' ");
	                    break;
	                  case Context.ORDER_DATA_STATISTICS_YEAR: // 今年
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y') = '")
	                       .append(askOrderNumDao.getTime("yyyy") + "' ");
	                    break;
	                  case Context.ORDER_DATA_STATISTICS_LAST_YEAR: // 去年
	                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y') = '")
	                       .append(askOrderNumDao.getLastYear() + "' ");
	                    break;
					default:
						break;
		    	}
			}
		}
    }
}