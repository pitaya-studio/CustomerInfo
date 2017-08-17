/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.common;

import com.trekiz.admin.common.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 赵俊豪
 *
 * 2016年12月25日 下午9:43:10
 */
/**
 * 1.根据当前时间，获取今天、本周第一天、本月第一天、本年第一天 2.根据当前时间，获取今天、昨天、上周同一天 3.根据当前时间，获取本周第一天、上周第一天
 * 4.根据当前时间，获取本月第一天、上个月第一天、去年同月 5.根据当前1天时间，获取上一天、上周同一天、上月同一天。
 * 6.根据当前时间,获取本年第一天，去年区间。
 *
 */
public class GetEachCycleFirstDay {
	
	public static final SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdfym = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
	
	// 获取这天最开始时间
	public String getThisDayStart(Date date) {
		String ft = sdfymd.format(date);
		String today = ft + " 00:00:00";
		return today;
	}
//---数据统计分析中----//1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部---获取各个类型的时间-------------------------------------------	
	// 根据当前时间，获取今日时间，格式：yyyy-MM-dd
	public static String getThisDaySimple() {
		return sdfymd.format(new Date());
	}
	// 根据当前时间，获取昨日时间，格式：yyyy-MM-dd
	public static String getYesterdaySimple() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		Date date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return sdfymd.format(date);
	}
	// 根据当前时间，获取本月时间，格式：yyyy-MM
	public static String getThisMonthSimple() {
		return sdfym.format(new Date());
	}
	// 根据当前时间，获取上月时间，格式：yyyy-MM
	public static String getLastMonthSimple() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return sdfym.format(calendar.getTime());
	}
	// 根据当前时间，获取本年时间，格式：yyyy
	public static String getThisYearSimple() {
		return sdfy.format(new Date());
	}
	// 根据当前时间，获取去年时间，格式：yyyy
	public static String getLastYearSimple() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		return sdfy.format(calendar.getTime());
	}
//--------------------------获取各个类型的时间结束---------------------------------------------------------------------------------

	// 根据当前时间，获取本周第一天
	public static String getThisWeekFirstDay(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String ThisWeekBegin = sdf.format(cal.getTime());
		return ThisWeekBegin + " 00:00:00";
	}

	// 根据当前时间，获取本周第一天
	public static Date getThisWeekFirstDate(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		Date ThisWeekBegin = cal.getTime();
		return ThisWeekBegin;
	}
	
	/**
	 * 根据当前时间，获取本周第一天  2016-12-12
	 * @param time
	 * @return
     */
	public static String getThisWeekFirstDayDate(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		//String ThisWeekBegin = sdf.format(cal.getTime());
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取下周一时间
	 * @param date
	 * @return
	 * @throws Exception
     */
	public static String getNextWeekFirstDay(String date) throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		if (i!=1) {//不是周日
			calendar.add(Calendar.DATE, -(i - 2));
			//calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			calendar.add(Calendar.DATE, 7);
		} else { //是周日
			//calendar.add(Calendar.DATE, -(i - 1));
			//calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			calendar.add(Calendar.DATE, 1);
		}
		return sdf.format(calendar.getTime());
	}

	// 根据当前时间获取本月第一天
	public String getThisMonthFirstDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String ft = sdf.format(date);
		String today = ft + "-01 00:00:00";
		return today;
	}

	// 根据当前时间获取本年第一天
	public static String getThisYearFirstDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String ft = sdf.format(date);
		String today = ft + "-01-01 00:00:00";
		return today;
	}

	/**
	 * 根据当前时间获取明年第一天
	 * @param date
	 * @return
	 * @throws Exception
     */
	public static String getFirstDayofNextYear(String date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String day = sdf.format(calendar.getTime());
		return day;
	}

	/**
	 * 根据当前时间获取本年第一天 yyyy-MM-dd
	 * @param date
	 * @return
     */
	public static String getThisYearFirstDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String ft = sdf.format(date);
		String today = ft + "-01-01";
		return today;
	}

	// * 2.根据当前时间，获取昨天最开始时间
	@SuppressWarnings("static-access")
	public String getYesterdayStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString + " 00:00:00";
	}
	
	/**
	 * 根据当前时间，获取昨天最开始时间yyyy-MM-dd
	 * @param date
	 * @return
     */
	@SuppressWarnings("static-access")
	public static String getYesterdayStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}
	
	// 2.根据当前时间，获取上周同一天
	public String getLastWeekSameDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(calendar.getTime());
		return dateString + " 00:00:00";
	}

	// 3.根据当前时间，获取上周第一天
	public String getLastWeekFirstDay() {
		Calendar calendar1 = Calendar.getInstance();
		int dayWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			calendar1.add(Calendar.DAY_OF_MONTH, -1);
		}
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset1 = 1 - dayOfWeek;
		calendar1.add(Calendar.DATE, offset1 - 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastWeekBegin = sdf.format(calendar1.getTime());
		return lastWeekBegin + " 00:00:00";
	}

	// 4.根据当前时间，获取上个月第一天
	public String getFirstDayofLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayofLastMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastMonth + " 00:00:00";
	}
	
	// 4.根据给定时间，获取下个月第一天
	/**
	 * @param stringTime  yyyy-MM-dd
	 * @return yyyy-MM-dd
	 */
	public static String getFirstDayofNextMonth(String stringTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = new Date();
		try {
			dateTime = sdf.parse(stringTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			String firstDayofNextMonth = sdf.format(calendar.getTime());
			return firstDayofNextMonth;
	}
	
	/**
	 * 根据当前时间，获取上个月第一天
	 * @return yyyy-MM-dd
     */
	public static String getFirstDayofLastMonthDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayofLastMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastMonth;
	}
	
	/**
	 * 根据当前时间，获取上个月
	 * @return yyyy-MM
	 */
	public static String getFirstDayofLastMonthNoDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayofLastMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		return firstDayofLastMonth;
	}
	
	// 4.根据当前时间，获取去年同月第一天
	public static String getFirstDayofLastYearMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayofLastYearMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastYearMonth + " 00:00:00";
	}

	// 4.根据当前时间，获取去年同月第一天
	public static String getFirstDateofLastYearMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayofLastYearMonth = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastYearMonth;
	}


	// 6.根据当前时间,获取去年第一天。
	public static String getFirstDayofLastYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		String firstDayofLastYear = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastYear + " 00:00:00";
	}
	
	// 6.根据当前时间,获取去年。
	/**
	 * @return yyyy
	 */
	public static String getFirstDayofLastYearNoMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		String firstDayofLastYear = new SimpleDateFormat("yyyy").format(calendar.getTime());
		return firstDayofLastYear;
	}

	// 6.根据当前时间,获取去年第一天。
	public static String getLastYearFirstDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		String firstDayofLastYear = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return firstDayofLastYear;
	}

	/**
	 * 获取去年同一天时间
	 * @return
     */
	public static String getLastYearSameDate(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		String sameDayofLastYear = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return sameDayofLastYear;
	}
	// 5.根据当前1天时间，获取上一天。
	public String getLastDay(String date) {
		int val = validity(date);
		if (val == 0) {
			return "0";// "日期不合法"
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = new Date();
		try {
			dateTime = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
			int day = calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE, day - 1);
			String dayBefore = sdf.format(calendar.getTime());
			return dayBefore;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	// 5.根据当前1天时间，获取上周同一天。
	public static String getLastWeekSameDay(String date) {
		int val = validity(date);
		if (val == 0) {
			return "0";// "日期不合法"
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = new Date();
		try {
			dateTime = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
			int day = calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE, day - 7);
			String dayBefore = sdf.format(calendar.getTime());
			return dayBefore;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	// 5.根据当前1天时间，获取上月同一天。
	public String getFirstDayofLastYear(String date) {
		int val = validity(date);
		if (val == 0) {
			return "0";// "日期不合法"
		}
		Calendar calendar = Calendar.getInstance();
		int m = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, m - 1);
		Date lastMonth = calendar.getTime();
		Date thisMonthFirst = getThisMonthFirst(date);
		if (lastMonth.getTime() >= thisMonthFirst.getTime()) {
			return "";
		} else {
			String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(lastMonth);
			return dayBefore;
		}
	}

	// 获取此月的最后一天
	public static Date getThisMonthEnd(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date2 = new Date();
		try {
			date2 = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date2);

			int m = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, m + 1);
			Date time = calendar.getTime();
			String dayBefore = new SimpleDateFormat("yyyy-MM").format(time);
			date2 = sdf.parse(dayBefore);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date2;
	};

	// 验证是否合法
	public static int validity(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		Calendar calendar = Calendar.getInstance();
		try {
			time = sdf.parse(date);
			calendar.setTime(time);
			Date dateTime = calendar.getTime();
			Date thisMonth = getThisMonthEnd(date);
			if (dateTime.getTime() >= thisMonth.getTime()) {
				return 0;// "日期不合法"
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	// 获取此月的第一天
	public static Date getThisMonthFirst(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = new Date();
		try {
			date2 = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date2);
			Date time = calendar.getTime();
			String dayBefore = new SimpleDateFormat("yyyy-MM").format(time);
			String dayBefore2 = dayBefore + "-01 00:00:00";
			date2 = sdf.parse(dayBefore2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date2;
	};

	/**
	 * 获取本月第一天时间
	 * @param date
	 * @return
     */
	public static String getThisMonthFirst(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String dayBefore = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		String time = dayBefore + "-01";

		return time;
	}

	/**
	 * 获取本月最后一天
	 * @return
     */
	public static String getThisMonthEndDay() {

		Calendar calendar = Calendar.getInstance();

		//int m = calendar.get(Calendar.MONTH);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

		return dayBefore;
	}

	/**
	 * 获取给定月最后一天
	 * @return
	 */
	public static String getSomeMonthEndDay(String date) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		//int m = calendar.get(Calendar.MONTH);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

		return dayBefore;
	}


	// 2.根据当前时间，获取上周同一天yyyy-MM-dd
	public static String getLastWeekSameDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(calendar.getTime());
		return dateString;
	}

	/**
	 * 获得本周最后一天的时间
	 * @param date
	 * @return
     */
	public  static Date getThisWeekLastDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		//String ThisWeekBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		return cal.getTime();
	}

	/**
	 * 获得本周最后一天的时间
	 * @param date
	 * @return
	 */
	public  static String getThisWeekLastDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		//String ThisWeekBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		return sdf.format(cal.getTime());
	}

	
	/**
	 * 获取给定日期的明天
	 * @param date
	 * @return
	 * @throws Exception
     */
	public static String getTomorrowOfThisDay(String date, int i) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		calendar.add(Calendar.DATE, i);
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return dateString;
	}

	/**
	 * 获取给定日期的明天
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date getTomorrowOfThisDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
}
