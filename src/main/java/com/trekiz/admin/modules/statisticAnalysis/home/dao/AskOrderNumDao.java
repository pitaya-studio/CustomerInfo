package com.trekiz.admin.modules.statisticAnalysis.home.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.persistence.BaseDao;

/**
 * 询单的折线图dao
 * @author chao.zhang
 */
public interface AskOrderNumDao extends BaseDao {
	
	/**
	 * 获得询单折线图数据
	 * @param searchDate 时间搜索类型
	 * @param startDate 自定义开始时间
	 * @param endDate 自定义结束时间
	 * @param dataFormat 粒度类型
	 * @param year 年
	 * @param month 月
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public List<Map<String,Object>> getAskOrderNum(String searchDate,String startDate,String endDate,String dataFormat,String year,String month);
	  /**
     * 获得该批发商被第一次询单的询单数据
     * @return
	 * @author chao.zhang
	 * @date 2017-3-7
     */
	public List<Map<String,Object>> getList();
	/** 
     * 两个时间之间相差距离多少天 
     * @param one 时间参数 1： 
     * @param two 时间参数 2： 
     * @return 相差天数 
	 * @author chao.zhang
	 * @date 2017-3-7
     */  
	public long getDistanceDays(String str1, String str2);
	
	/**
	 * 获得去年
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getLastYear();
	
	/**
	 * 获得昨天日期
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getYesterday();
	
	/**
	 * 获得本月，本年
	 * @param type
	 * @return
	 */
	public String getTime(String type);
	
	/**
	 * 获得上月
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getLastMonth();
}
