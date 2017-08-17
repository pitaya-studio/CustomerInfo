package com.trekiz.admin.modules.statisticAnalysis.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.AgentDetailDao;

/**
 * 数据统计分析详情页生成Excel工具类
 * @author yang.wang
 * @date 2017.3.23
 * */
public class ObtainDetailStatisticsExcelUtils {
	
	private static AgentDetailDao agentDetailDao = SpringContextHolder.getBean(AgentDetailDao.class);
	
	/**
	 * 获取数据统计分析详情页导出excel文件名称（无后缀名）
	 * @param map
	 * @param analysisTypeName
	 * @return
	 */
	public static String getExcelFileName(Map<String, Object> map, String analysisTypeName) {
		
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String searchDate = (String) map.get("searchDate");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startRealDate = null;
		String endRealDate = null;
		
		if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){	// 自定义时间
			startRealDate = startDate;
			endRealDate = endDate;
		}else{
			if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
				if (StringUtils.isNotBlank(startDate)) {
					startRealDate = startDate;
					if (StringUtils.isBlank(endDate)) {
						endRealDate = format.format(new Date());
					}
				} else {
					startRealDate = agentDetailDao.getEarliestAskTime();
					endRealDate = endDate;
				}
			} else {
				switch (searchDate) {	// 选择时间区间
					case Context.ORDER_DATA_STATISTICS_ALL:
						startRealDate = agentDetailDao.getEarliestAskTime();
						endRealDate = format.format(new Date());
						break;
					case Context.ORDER_DATA_STATISTICS_TODAY:
						startRealDate = format.format(new Date());
						endRealDate = format.format(new Date());
						break;
					case Context.ORDER_DATA_STATISTICS_YESTERDAY:
						startRealDate = getYesterday(format, new Date());
						endRealDate = getYesterday(format, new Date());
						break;
					case Context.ORDER_DATA_STATISTICS_MONTH:
						startRealDate = getMonthFirstDay(format);
//						endRealDate = getMonthLastDay(format);
						endRealDate = format.format(new Date());
						break;
					case Context.ORDER_DATA_STATISTICS_LAST_MONTH:
						startRealDate = getLastMonthFirstDay(format);
						endRealDate = getLastMonthLastDay(format);
						break;
					case Context.ORDER_DATA_STATISTICS_YEAR:
						startRealDate = getYear() + "-01-01";
//						endRealDate = getYear() + "-12-31";
						endRealDate = format.format(new Date());
						break;
					case Context.ORDER_DATA_STATISTICS_LAST_YEAR:
						startRealDate = getLastYear() + "-01-01";
						endRealDate = getLastYear() + "-12-31";
						break;
					default:
						break;
				}
			}
		}
		
		// 当该批发商下不存在任何询单数据时，excel文件名称不显示时间区间
		Integer compare = compareDateStr(startRealDate, endRealDate);
		if (StringUtils.isBlank(startRealDate) || compare > 0) {
			return analysisTypeName;
		} 
		
		
		return startRealDate + "-" + endRealDate + analysisTypeName;
	}
	
	
	/**
	 * 比较两日期格式（yyyy-MM-dd）的字符串所表示日期的大小
	 * <br>	dateStr1 > dateStr2 --->  1
	 * <br>	dateStr1 = dateStr2 --->  0
	 * <br>	dateStr1 < dateStr2 ---> -1
	 * <br> 参数非日期格式时返回 			 2
	 * @param dateStr1	 参数1
	 * @param dateStr2 	参数2
	 * */
	public static Integer compareDateStr(String dateStr1, String dateStr2) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		
		if (StringUtils.isBlank(dateStr1)) {
			dateStr1 = "9999-12-31";
		}
		
		try {
			date1 = format.parse(dateStr1);
			date2 = format.parse(dateStr2);
		} catch (ParseException e) {
			e.printStackTrace();
			return 2;
		}
		
		int compareTo = date1.compareTo(date2);
		if (compareTo > 0)	return 1;
		else if (compareTo < 0)	return -1;
		else	return 0;
	}
	
	
	/**
	 * 获取昨日 yyyy-MM-dd
	 * */
	private static String getYesterday(SimpleDateFormat format, Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return format.format(calendar.getTime());
	}
	
	/**
	 * 获取本月第一天 yyyy-MM-dd
	 * */
	private static String getMonthFirstDay(SimpleDateFormat format) {
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return format.format(c.getTime());
	}
	
	/**
	 * 获取本月最后一天 yyyy-MM-dd
	 * */
	private static String getMonthLastDay(SimpleDateFormat format) {
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return format.format(ca.getTime());
	}
	
	/**
	 * 获取上月第一天 yyyy-MM-dd
	 * */
	private static String getLastMonthFirstDay(SimpleDateFormat format) {
		Calendar cal_1 = Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
        return format.format(cal_1.getTime());
	}
	
	/**
	 * 获取上月最后一天 yyyy-MM-dd
	 * */
	private static String getLastMonthLastDay(SimpleDateFormat format) {
		Calendar cale = Calendar.getInstance();   
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天 
        return format.format(cale.getTime());
	}
	
	/**
	 * 获取今年年份 yyyy
	 * */
	private static int getYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 获取去年年份 yyyy
	 * */
	private static int getLastYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) - 1;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
