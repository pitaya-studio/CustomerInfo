package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AskOrderNumService {
	/**
	 * 获得数据
	 * @param searchDate
	 * @param startDate
	 * @param endDate
	 * @param year
	 * @param month
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public Map<String,Object> getAskOrderNum(String searchDate,String startDate,String endDate,String year,String month);
	
	/**
	 * 根据时间获得月里的所有日期
	 * @param date
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-8
	 */
	public List<Date> getAllTheDateOftheMonth(Date date);
	
	/**
	 * 获取两个日期之间的日期
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 日期集合
	 * @author chao.zhang
	 * @date 2017-3-8
	 */
	public List<Date> getBetweenDates(Date start, Date end);
	
	/**
	 * 获得两个时间点之间所有的月份
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public List<String> getMonthBetween(String minDate, String maxDate) throws ParseException;
	/**
	 * 获得两个时间点之间所有的年份
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public List<String> getYearBetween(String minDate, String maxDate);
}
