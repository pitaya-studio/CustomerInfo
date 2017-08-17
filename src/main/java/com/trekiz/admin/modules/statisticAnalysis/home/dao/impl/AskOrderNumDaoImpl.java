package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class AskOrderNumDaoImpl extends BaseDaoImpl implements AskOrderNumDao{

	@Override
	public List<Map<String, Object>> getAskOrderNum(String searchDate,
			String startDate, String endDate,String dataFormat,String year,String month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());//当前时间字符串
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("DATE_FORMAT(ask_time,'").append(dataFormat).append("') AS askTime, ");
		sbf.append("COUNT(id) AS askOrderNum ");
		sbf.append("FROM ");
		sbf.append("order_progress_tracking ");
		sbf.append("WHERE ");
		sbf.append("company_id = ? ");
		sbf.append("AND ask_time IS NOT NULL AND ask_num IS NOT NULL ");
		switch(searchDate){
		case "1": sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m-%d')='").append(date).append("' ");
					break;
		case "-1":  sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m-%d')='").append(getYesterday()).append("' ");
		break;
		case "3": sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m')='").append(getTime("yyyy-MM")).append("' ");;
		break;
		case "-3": sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m')='").append(getLastMonth()).append("' ");;
		break;
		case "4": sbf.append("AND DATE_FORMAT(ask_time,'%Y')='").append(getTime("yyyy")).append("' ");
		break;	
		case "-4": sbf.append("AND DATE_FORMAT(ask_time,'%Y')='").append(getLastYear()).append("' ");
		break; 
		case "5": sbf.append("");
		break;
		default: sbf.append("");
		}
		if(StringUtils.isNotBlank(startDate)){
			sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m-%d')>='").append(startDate).append("' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sbf.append("AND DATE_FORMAT(ask_time,'%Y-%m-%d')<='").append(endDate).append("' "); 
		}
		sbf.append("GROUP BY ");
		switch(searchDate){
		case "1": sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d %H') ");
					break;
		case "-1":  sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d %H') ");
		break;
		case "3": sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d') ");
		break;
		case "-3": sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d') ");
		break;
		case "4": sbf.append("DATE_FORMAT(ask_time,'%Y-%m') ");
		break;	
		case "-4": sbf.append("DATE_FORMAT(ask_time,'%Y-%m') ");
		break;
		case "5": String date1 = getList().get(0).get("ask_time").toString();//获得该批发商下第一次询单时的询单时间
						long days = getDistanceDays(date1,date);//获得两者之间的天数
						sbf.append(groupBySql(days,month));//按粒度分组sql
		break;
		default: sbf.append("");
		}
		if(StringUtils.isNotBlank(startDate) && StringUtils.isEmpty(endDate)){
			long days = getDistanceDays(startDate,date);//获得两者之间的天数
			sbf.append(groupBySql(days,month));//按粒度分组sql
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isEmpty(startDate)){
			String date1 = getList().get(0).get("ask_time").toString();//获得该批发商下第一次询单时的询单时间
			long days = getDistanceDays(date1,endDate);//获得两者之间的天数
			sbf.append(groupBySql(days,month));
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isNoneBlank(startDate)){
			long days = getDistanceDays(startDate,endDate);//获得两者之间的天数
			sbf.append(groupBySql(days,month));//按粒度分组sql
		}
		sbf.append("order by ask_time ASC ");
		List<Map<String,Object>> list = this.findBySql(sbf.toString(), Map.class,UserUtils.getUser().getCompany().getId());
		return list;
	}
	
	/**
	 * 用来判断按什么粒度展示
	 * @param days 天数
	 * @param month 月
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	private String groupBySql(long days,String month){
		StringBuffer sbf = new StringBuffer();
		if(days<=1){
			sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d %H') ");//按小时
		}else if(days >= 2 && days<= 58){
			sbf.append("DATE_FORMAT(ask_time,'%Y-%m-%d') ");//按天
		}else if(days >= 59 && days <= 365){
			sbf.append("DATE_FORMAT(ask_time,'%Y-%m') ");//按月
		}else{
			if(StringUtils.isNotBlank(month) && "month".equals(month)){
				sbf.append("DATE_FORMAT(ask_time,'%Y-%m') ");//默认按月
			}else{
				sbf.append("DATE_FORMAT(ask_time,'%Y') ");//按年
			}
		}
		return sbf.toString();
	}
	
	/** 
     * 两个时间之间相差距离多少天 
     * @param one 时间参数 1： 
     * @param two 时间参数 2： 
     * @return 相差天数 
	 * @author chao.zhang
	 * @date 2017-3-7
     */  
	public long getDistanceDays(String str1, String str2){  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date one;  
        Date two;  
        long days=0;  
        try {  
        	if(StringUtils.isNotBlank(str1) && StringUtils.isNotBlank(str2)){
        		 one = df.parse(str1);  
                 two = df.parse(str2);  
                 long time1 = one.getTime();  
                 long time2 = two.getTime();  
                 long diff = time2 - time1;  
                 days = diff / (1000 * 60 * 60 * 24);  
        	}
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return days+1;  
    }  
	
    /**
     * 获得该批发商被第一次询单的询单数据
     * @return
	 * @author chao.zhang
	 * @date 2017-3-7
     */
	public List<Map<String,Object>> getList(){
    	StringBuffer sbf = new StringBuffer();
    	sbf.append("SELECT * FROM order_progress_tracking WHERE company_id = ? AND ask_time IS NOT NULL order by ask_time asc limit 1 ");
    	List<Map<String,Object>> list = this.findBySql(sbf.toString(), Map.class,UserUtils.getUser().getCompany().getId());
    	return list;
    }
	
	/**
	 * 获得昨天日期
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getYesterday(){
		 Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,   -1);
        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(c.getTime());
        return yesterday;
	}
	/**
	 * 获得上月
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getLastMonth(){
		 Calendar c = Calendar.getInstance();
		 c.setTime(new Date()); 
		 c.add(Calendar.MONTH,   -1);
		 String lastMonth = new SimpleDateFormat( "yyyy-MM").format(c.getTime());
        return lastMonth;
	}
	/**
	 * 获得去年
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	public String getLastYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.YEAR,   -1);
        String lastYear = new SimpleDateFormat( "yyyy").format(c.getTime());
        return lastYear;
	}
	
	/**
	 * 获得本月，本年
	 * @param type
	 * @return
	 */
	public String getTime(String type){
		SimpleDateFormat format = new SimpleDateFormat(type);
		return format.format(new Date());
	}
}
