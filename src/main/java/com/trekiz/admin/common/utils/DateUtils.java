/**
 *
 */
package com.trekiz.admin.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author zj
 * @version 2013-11-19
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	
	
	public static final String DATE_PATTERN_YYYY_MM_DD="yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM="yyyy-MM-dd HH:mm";
	public static final String DATE_PATTERN_HH_MM_SS="HH:mm:ss";
	public static final String DATE_PATTERN_YYYYMMDD="yyyyMMdd";
	public static final String DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY = "yyyy年MM月dd日";
	
	/**
	 * 时间转成字符串 默认转换格式yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String date2String(Date date){
		return date2String(date, DATE_PATTERN_YYYY_MM_DD);
	}
	/**
	 * 根据指定格式 ， 时间转成字符串 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2String(Date date,String format){
		if(date==null||format==null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 字符串转成时间 默认转换格式yyyy-MM-dd
	 * @param dateStr
	 * @return
	 */
	public static Date string2Date(String dateStr){
		return string2Date(dateStr, DATE_PATTERN_YYYY_MM_DD);
	}
	/**
	 * 根据指定格式 ， 字符串转成时间
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date string2Date(String dateStr,String format){
		if(dateStr==null||format==null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}
	
	/**
	 * 将字符串转换为日期
	 * 
	 * @param date
	 *            字符串日期
	 * @return null或转换结果
	 */
	public static Date dateFormat(String date) {
		String[] dfs = new String[] { "yyyy-MM-dd HH:mm:ss" , 
				"yyyy-MM-dd HH:mm" ,
				"yyyyMMdd HH:mm:ss" ,
				"yy/MM/dd HH:mm:ss" , 
				"yyyyMMddHHmmss" , 
				"yyyyMMdd" , 
				"yyyy/MM/dd" ,
				"0000-00-00", "yyyy-MM-dd", "yyyy年MM月dd日" };
		for (String df : dfs) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(df);
				return sdf.parse(date);
			} catch (ParseException e) {
			}
		}
		return null;
	}
	
	/**
	 * 格式化指定日期
	 * 
	 * @param value
	 * @param format
	 *            指定格式化后格式
	 * @return
	 */
	public static String formatCustomDate(Date value, String format) {
		if (value == null) {
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(format);
		return sd.format(value);
	}
	
	/**
	 * 将字符串按照指定格式转换为日期
	 * 
	 * @param date 字符串日期
	 * @param format 制定的日期格式
	 * @return null或转换结果
	 */
	public static Date dateFormat(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
    
    /**
     * 字符串的日期格式的计算 
     * @param smdate 较小的时间 
     * @param bdate 较大的时间 
     * @return 相差天数 
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException {  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }  
    
    
    
    /**
     * 比较日期
     * @param DATE1 时间 
     * @param dfStr1 格式 
     * @param DATE2 时间 
     * @param dfStr2 格式  
     * @return 1 时间1大于时间2  ； 0时间1=时间2；  -1 时间1小于时间2； 9未知错误
     * @throws ParseException
     */
    
    public static int compareDate(String DATE1,String dfStr1, String DATE2,String dfString2) {
        DateFormat df1 = new SimpleDateFormat(dfStr1);
        DateFormat df2 = new SimpleDateFormat(dfString2);
        try {
            Date dt1 = df1.parse(DATE1);
            Date dt2 = df2.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 9;
    }
    
    /**
     * 取日期数组中的最小日期，如果有两个最小的日期相等则返回任何一个
     * @param dates		日期数组，格式为 yyyy-MM-dd
     * @return 最小日期
     * @author shijun.liu
     */
    public static String getMinDate(String[] dates){
    	String minDate = "9999-12-30";//默认最小日期
    	if(null == dates || dates.length == 0){
    		return null;
    	}
    	for (String date:dates) {
			int index = compareDate(minDate, DATE_PATTERN_YYYY_MM_DD, date, DATE_PATTERN_YYYY_MM_DD);
			if(index == 1){
				minDate = date;
			}
		}
    	return minDate;
    }
    
    /**
     * 取日期数组中的最大日期，如果有两个最大的日期相等则返回任何一个
     * @param dates		日期数组，格式为 yyyy-MM-dd
     * @return 最大日期
     * @author shijun.liu
     */
    public static String getMaxDate(String[] dates){
    	String maxDate = "1991-01-01";//默认最大日期
    	if(null == dates || dates.length == 0){
    		return null;
    	}
    	for (String date:dates) {
			int index = compareDate(maxDate, DATE_PATTERN_YYYY_MM_DD, date, DATE_PATTERN_YYYY_MM_DD);
			if(index == -1){
				maxDate = date;
			}
		}
    	return maxDate;
    }
    
}
