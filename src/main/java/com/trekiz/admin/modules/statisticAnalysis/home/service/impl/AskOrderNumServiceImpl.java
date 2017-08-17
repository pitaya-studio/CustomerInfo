package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.sun.star.uno.RuntimeException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.AskOrderNumService;

@Service
public class AskOrderNumServiceImpl implements AskOrderNumService {
	@Autowired
	private AskOrderNumDao askOrderNumDao;
	
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
	@Override
	public Map<String, Object> getAskOrderNum(String searchDate,
			String startDate, String endDate, String year,
			String month) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//数据
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());//当前时间字符串
		map.put("today", format2.format(new Date()));
		switch (searchDate) {//判断时间类型
		case "1":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%H:00", year, month);
			break;
		case "-1":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%H:00", year, month);
			break;	
		case "3":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m-%d", year, month);
			break;
		case "-3":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m-%d", year, month);
			break;
		case "4":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m", year, month);
			break;
		case "-4":
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m", year, month);
			break;
		case "5":
			try {
				String date1 = askOrderNumDao.getList().get(0).get("ask_time").toString();
				long days = askOrderNumDao.getDistanceDays(date1, date);
				list = getList(searchDate, startDate, endDate, year, month, days,map);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("暂无数据");
			}
			break;	
		default:
			break;
		}
		if(StringUtils.isNotBlank(startDate) && StringUtils.isEmpty(endDate)){//自定义时间
			long days = askOrderNumDao.getDistanceDays(startDate,date);//获得两者之间的天数
			list = getList(searchDate, startDate, endDate, year, month, days,map);//按粒度分组sql
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isEmpty(startDate)){
			try {
				String date1 = askOrderNumDao.getList().get(0).get("ask_time").toString();//获得该批发商下第一次询单时的询单时间
				long days = askOrderNumDao.getDistanceDays(date1,endDate);//获得两者之间的天数
				list = getList(searchDate, startDate, endDate, year, month, days,map);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("暂无数据");
			}
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isNoneBlank(startDate)){
			long days = askOrderNumDao.getDistanceDays(startDate,endDate);//获得两者之间的天数
			list = getList(searchDate, startDate, endDate, year, month, days,map);//按粒度分组sql
		}
		List<Integer> askOrderNumList = new ArrayList<>();//询单的数据
		List<String> askOrderDateList = new ArrayList<>();//询单的日期
		//补全缺省值
		if("1".equals(searchDate) || "-1".equals(searchDate)){
			putHours(askOrderNumList, askOrderDateList, list, map);
		}else if("3".equals(searchDate)){
			List<Date> dates = getAllTheDateOftheMonth(new Date());
			putDays(dates, list, askOrderNumList, askOrderDateList, map);
		}else if("-3".equals(searchDate)){
			Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MONTH, - 1); // 设置为上一个月
	        Date day1 = calendar.getTime();//上月的时间
	        List<Date> dates = getAllTheDateOftheMonth(day1);
	        putDays(dates, list, askOrderNumList, askOrderDateList, map);
		}else if("4".equals(searchDate)){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
			String str = format1.format(new Date());
			String[] months = {str+"-01",str+"-02",str+"-03",str+"-04",str+"-05",str+"-06",str+"-07",str+"-08",str+"-09",str+"-10",str+"-11",str+"-12"}; 
			putMonth(months, list, askOrderNumList, askOrderDateList, map);
		}else if("-4".equals(searchDate)){
			String str = askOrderNumDao.getLastYear();
			String[] months = {str+"-01",str+"-02",str+"-03",str+"-04",str+"-05",str+"-06",str+"-07",str+"-08",str+"-09",str+"-10",str+"-11",str+"-12"}; 
			putMonth(months, list, askOrderNumList, askOrderDateList, map);
		}else if("5".equals(searchDate)){
				try {
					String sDate = askOrderNumDao.getList().get(0).get("ask_time").toString();
					putMap(sDate, format.format(new Date()), askOrderNumList, askOrderDateList, map, list, month, year);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("暂无数据");
				}
		}else{
			if(StringUtils.isNotBlank(startDate) && StringUtils.isEmpty(endDate)){
				putMap(startDate, format.format(new Date()), askOrderNumList, askOrderDateList, map, list, month, year);
			}else if(StringUtils.isNotBlank(endDate) && StringUtils.isEmpty(startDate)){
				try {
					List<Map<String, Object>> sDateList = askOrderNumDao.getList();
					String sDate = sDateList.get(0).get("ask_time").toString();
					putMap(sDate, endDate, askOrderNumList, askOrderDateList, map, list, month, year);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("暂无数据");
				}
			}else if(StringUtils.isNotBlank(startDate) && StringUtils.isNoneBlank(endDate)){
				putMap(startDate, endDate, askOrderNumList, askOrderDateList, map, list, month, year);
			}
		}
		
		return map;
	}
	
	/**
	 * 用于自定义或全年的时候
	 * 根据时间不同 填充map
	 * @param sDate
	 * @param endDate
	 * @param askOrderNumList
	 * @param askOrderDateList
	 * @param map
	 * @param list
	 * @param month
	 * @param year
	 */
	private void putMap(String sDate,String endDate,List<Integer> askOrderNumList,
			List<String> askOrderDateList,Map<String,Object> map,List<Map<String,Object>> list,
			String month,String year){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long days = askOrderNumDao.getDistanceDays(sDate, endDate);//天数
		if(days<=0){
			throw new RuntimeException("暂无数据");
		}
		if(days<=1){
			putHours(askOrderNumList, askOrderDateList, list, map);//一天按小时展示
		}else if(days >=2 && days <= 58){
			List<Date> dates = new ArrayList<Date>();//按天展示
			try {
				dates = getBetweenDates(format.parse(sDate), format.parse(endDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			putDays(dates, list, askOrderNumList, askOrderDateList, map);
		}else if(days >= 59 && days <= 365){//按月展示
			try {
				List<String> months = getMonthBetween(sDate, endDate);
				putMonth(months, list, askOrderNumList, askOrderDateList, map);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			if("month".equals(month)){//默认按年展示，按月展示
				try {
					List<String> months = getMonthBetween(sDate, endDate);
					putMonth(months, list, askOrderNumList, askOrderDateList, map);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				List<String> years = getYearBetween(sDate, endDate);
				if(list.size()>0){
					for(String year1 : years){
						for(Map<String,Object> askMap : list){
							if(!askOrderDateList.contains(year1)){
								if(askMap.get("askTime").toString().equals(year1)){
									askOrderNumList.add(Integer.parseInt(askMap.get("askOrderNum").toString()));
									askOrderDateList.add(askMap.get("askTime").toString());
								}else{
									boolean flag = false;
									for(Map<String,Object> askMap1 : list){
										if(askMap1.containsValue(year1)){
											flag = true;
										}
									}
									if(!flag){
										askOrderDateList.add(year1);
										askOrderNumList.add(0);
									}
								}
							}	
						}
					}
					map.put("askOrderNumList", askOrderNumList);
					map.put("askOrderDateList", askOrderDateList);
				}else{
					List<Integer> ask = new ArrayList<>();
					for(String y : years){
						ask.add(0);
					}
					map.put("askOrderNumList", ask);
					map.put("askOrderDateList", years);
				}
			}	
		}
	}
	
	
	/**
	 * 根据条件判断，获得数据集
	 * @param searchDate
	 * @param startDate
	 * @param endDate
	 * @param year
	 * @param month
	 * @param days
	 * @return
	 */
	private List<Map<String,Object>> getList(String searchDate,
			String startDate, String endDate, String year,
			String month,long days,Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		map.put("cutTime", days);
		if(days<=1){
			map.put("days", false);
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%H:00", year, month);//按小时
		}else if(days >= 2 && days<= 58){
			map.put("days", false);
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m-%d", year, month);//按天
		}else if(days >= 59 && days <= 365){
			map.put("days", false);
			list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m", year, month);//按月
		}else{
			map.put("days", true);
			if(StringUtils.isNotBlank(month) && "month".equals(month)){
				list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y-%m", year, month);//默认按月
			}else{
				list = askOrderNumDao.getAskOrderNum(searchDate, startDate, endDate, "%Y", year, month);//按年
			}
		}
		return list;
	}
	
	/**
	 * 获取两个日期之间的日期
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 日期集合
	 * @author chao.zhang
	 * @date 2017-3-8
	 */
	public List<Date> getBetweenDates(Date start, Date end) {
	    List<Date> result = new ArrayList<Date>();
	    result.add(start);
	    Calendar tempStart = Calendar.getInstance();
	    tempStart.setTime(start);
	    tempStart.add(Calendar.DAY_OF_YEAR, 1);
	    
	    Calendar tempEnd = Calendar.getInstance();
	    tempEnd.setTime(end);
	    while (tempStart.before(tempEnd)) {
	        result.add(tempStart.getTime());
	        tempStart.add(Calendar.DAY_OF_YEAR, 1);
	    }
	    result.add(end);
	    return result;
	}
	
	/**
	 * 根据时间获得月里的所有日期
	 * @param date
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-8
	 */
	public List<Date> getAllTheDateOftheMonth(Date date) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		while(cal.get(Calendar.MONTH) == month){
			list.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}
	
	/**
	 * 补小时
	 * @param askOrderNumList
	 * @param askOrderDateList
	 * @param list
	 * @param map
	 */
	private void putHours(List<Integer> askOrderNumList, List<String> askOrderDateList
			,List<Map<String,Object>> list,Map<String,Object> map){
		//今日内所有时间
		String[] hours = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00",
									"12:00","13:00","14:00","15:00","16:00","17:00",
									"18:00","19:00","20:00","21:00","22:00","23:00"}; 
		if(list.size()>0){
			for(String hour : hours){
				for(Map<String,Object> askMap : list){
					if(!askOrderDateList.contains(hour)){
						if(askMap.get("askTime").toString().equals(hour)){
							askOrderNumList.add(Integer.parseInt(askMap.get("askOrderNum").toString()));
							askOrderDateList.add(askMap.get("askTime").toString());
						}else{
							boolean flag = false;
							for(Map<String,Object> askMap1 : list){
								if(askMap1.containsValue(hour)){
									flag = true;
								}
							}
							if(!flag){
								askOrderDateList.add(hour);
								askOrderNumList.add(0);
							}	
						}
					}	
				}
			}
			map.put("askOrderNumList", askOrderNumList);
			map.put("askOrderDateList", askOrderDateList);
		}else{
			map.put("askOrderDateList", hours);
			int[] number = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			map.put("askOrderNumList", number);
		}
	}
	
	/**
	 * 补充天数默认值
	 * @param dates
	 * @param list
	 * @param askOrderNumList
	 * @param askOrderDateList
	 * @param map
	 */
	private void putDays(List<Date> dates,List<Map<String,Object>> list,List<Integer> askOrderNumList, List<String> askOrderDateList,
			Map<String,Object> map){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(list.size()>0){
			for(Date day : dates){
				for(Map<String,Object> askMap : list){
					if(!askOrderDateList.contains(format.format(day))){
						if(askMap.get("askTime").toString().equals(format.format(day))){
							askOrderNumList.add(Integer.parseInt(askMap.get("askOrderNum").toString()));
							askOrderDateList.add(askMap.get("askTime").toString());
						}else{
							boolean flag = false;
							for(Map<String,Object> askMap1 : list){
								if(askMap1.containsValue(format.format(day))){
									flag = true;
								}
							}
							if(!flag){
								askOrderDateList.add(format.format(day));
								askOrderNumList.add(0);
							}	
						}
					}	
				}
				map.put("askOrderNumList", askOrderNumList);
				map.put("askOrderDateList", askOrderDateList);
			}
		}else{
				List<Integer> ask = new ArrayList<Integer>();
				List<String> ask1 = new ArrayList<String>();
				for(Date date:dates){
					ask.add(0);
					ask1.add(format.format(date));
				}
				map.put("askOrderNumList", ask);
				map.put("askOrderDateList", ask1);
		}
	}
	
	/**
	 * 补充月份缺省值
	 * @param months
	 * @param list
	 * @param askOrderNumList
	 * @param askOrderDateList
	 * @param map
	 */
	private void putMonth(String[] months,List<Map<String,Object>> list,
			List<Integer> askOrderNumList, List<String> askOrderDateList,
			Map<String,Object> map){
		if(list.size()>0){
			for(String day : months){
				for(Map<String,Object> askMap : list){
					if(!askOrderDateList.contains(day)){
						if(askMap.get("askTime").toString().equals(day)){
							askOrderNumList.add(Integer.parseInt(askMap.get("askOrderNum").toString()));
							askOrderDateList.add(askMap.get("askTime").toString());
						}else{
							boolean flag = false;
							for(Map<String,Object> askMap1 : list){
								if(askMap1.containsValue(day)){
									flag = true;
								}
							}
							if(!flag){
								askOrderDateList.add(day);
								askOrderNumList.add(0);
							}
						}
					}	
				}
			}	
			map.put("askOrderNumList", askOrderNumList);
			map.put("askOrderDateList", askOrderDateList);
		}else{
			List<Integer> ask = new ArrayList<Integer>();
			for(String day : months){
				ask.add(0);
			}
			map.put("askOrderNumList", ask);
			map.put("askOrderDateList", months);
		}
	}
	/**
	 * 补充月份缺省值
	 * @param months
	 * @param list
	 * @param askOrderNumList
	 * @param askOrderDateList
	 * @param map
	 */
	private void putMonth(List<String> months,List<Map<String,Object>> list,
			List<Integer> askOrderNumList, List<String> askOrderDateList,
			Map<String,Object> map){
		if(list.size() > 0){
			for(String day : months){
				for(Map<String,Object> askMap : list){
					if(!askOrderDateList.contains(day)){
						if(askMap.get("askTime").toString().equals(day)){
							askOrderNumList.add(Integer.parseInt(askMap.get("askOrderNum").toString()));
							askOrderDateList.add(askMap.get("askTime").toString()); 
						}else{ 
							boolean flag = false;
							for(Map<String,Object> askMap1 : list){
								if(askMap1.containsValue(day)){
									flag = true;
								}
							}
							if(!flag){
								askOrderDateList.add(day);
								askOrderNumList.add(0);
							}
						}
					}
				}
			}	
			map.put("askOrderNumList", askOrderNumList);
			map.put("askOrderDateList", askOrderDateList);
		}else{
			List<Integer> ask = new ArrayList<Integer>();
			for(String day : months){
				ask.add(0);
			}
			map.put("askOrderNumList", ask);
			map.put("askOrderDateList", months);
		}
	}
	
	/**
	 * 获得两个时间点之间所有的月份
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();

	    min.setTime(sdf.parse(minDate));
	    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

	    max.setTime(sdf.parse(maxDate));
	    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }

	    return result;
	  }
	/**
	 * 获得两个时间点之间所有的年份
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public List<String> getYearBetween(String minDate, String maxDate) {
	    ArrayList<String> result = new ArrayList<String>();
	    int year1 = Integer.parseInt(minDate.split("-")[0]);
	    int year2 = Integer.parseInt(maxDate.split("-")[0]);
	    for(int i = year1;i<=year2;i++){
	    	result.add(i+"");
	    }

	    return result;
	  }
}
