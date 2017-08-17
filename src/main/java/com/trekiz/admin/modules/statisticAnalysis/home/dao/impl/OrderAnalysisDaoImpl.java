package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderAnalysisDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class OrderAnalysisDaoImpl extends BaseDaoImpl implements OrderAnalysisDao{
	@Autowired
	private AskOrderNumDao askOrderNumDao;
	/**
	 * 订单总览折线图数据
	 * @param searchDate 搜索时间 （/时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 ）
	 * @param startDate 自定义开始时间
	 * @param endDate 自定义结束时间
	 * @param dataFormat 显示日期的格式 
	 * @param year 按年显示（超过365天）
	 * @param month 按月显示（超过365天）
	 * @param analysisType 分析类型: 1 订单数 2收客人数 3订单金额
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-09
	 */
	@Override
	public List<Map<String, Object>> getOrderAnalysisData(String searchDate,
			String startDate, String endDate, String dataFormat, String year,
			String month, String analysisType) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());//当前时间字符串
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("DATE_FORMAT(p.orderTime,'").append(dataFormat).append("') AS orderTime, ");
		switch (analysisType) {//不同的分析方向
		case "1":
			sbf.append("count(p.id) as num ");
			break;
		case "2":
			sbf.append("SUM(p.orderPersonNum) AS num ");
			break;
		case "3":
			sbf.append("SUM( ");
			sbf.append("ma.amount*ma.exchangerate -IFNULL(( ");
			sbf.append("SELECT ");
			sbf.append("m.amount*m.exchangerate ");
			sbf.append("FROM money_amount m WHERE ");
			sbf.append("m.serialNum = p.differenceMoney),0) ");
			sbf.append(") AS num "); 	
			break;
		default:
			break;
		}
		sbf.append("FROM ");
		sbf.append("order_progress_tracking opt ");
		sbf.append("LEFT JOIN productorder p ON opt.order_id = p.id ");
		if(analysisType.equals("3")){
			sbf.append("LEFT JOIN money_amount ma ON p.total_money = ma.serialNum ");
		}
		sbf.append("WHERE ");
		sbf.append("opt.ask_time IS NOT NULL ");
		sbf.append("AND opt.company_id = ? ");
		sbf.append("AND p.payStatus  IN (3,4,5) ");
		sbf.append("AND p.delFlag = 0 AND ask_num IS NOT NULL ");
		switch(searchDate){//条件查询
		case "1": sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m-%d')='").append(date).append("' ");
		break;
		case "-1":  sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m-%d')='").append(askOrderNumDao.getYesterday()).append("' ");
		break;
		case "3": sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m')='").append(askOrderNumDao.getTime("yyyy-MM")).append("' ");;
		break;
		case "-3": sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m')='").append(askOrderNumDao.getLastMonth()).append("' ");;
		break;
		case "4": sbf.append("AND DATE_FORMAT(p.orderTime,'%Y')='").append(askOrderNumDao.getTime("yyyy")).append("' ");
		break;	
		case "-4": sbf.append("AND DATE_FORMAT(p.orderTime,'%Y')='").append(askOrderNumDao.getLastYear()).append("' ");
		break; 
		case "5": sbf.append("");
		break;
		default: sbf.append("");
		}
		if(StringUtils.isNotBlank(startDate)){
			sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m-%d')>='").append(startDate).append("' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sbf.append("AND DATE_FORMAT(p.orderTime,'%Y-%m-%d')<='").append(endDate).append("' "); 
		}
		sbf.append("GROUP BY ");
		switch(searchDate){
		case "1": sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d %H') ");//今日 按小时分组
					break;
		case "-1":  sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d %H') ");//昨日 按小时分组
		break;
		case "3": sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d') ");//本月 按天分组
		break;
		case "-3": sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d') ");//上月 按天分组
		break;
		case "4": sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m') ");//本年 按月分组
		break;	
		case "-4": sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m') ");//去年 按月分组
		break;
		case "5": String date1 = getList().get(0).get("ask_time").toString();//获得该批发商下第一次询单转订单时的询单时间
						long days = askOrderNumDao.getDistanceDays(date1,date);//获得两者之间的天数
						sbf.append(groupBySql(days,month));//按粒度分组sql 
		break;
		default: sbf.append(""); 
		}
		if(StringUtils.isNotBlank(startDate) && StringUtils.isEmpty(endDate)){
			long days = askOrderNumDao.getDistanceDays(startDate,date);//获得两者之间的天数
			sbf.append(groupBySql(days,month));//按粒度分组sql 
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isEmpty(startDate)){
			String date1 = getList().get(0).get("ask_time").toString();//获得该批发商下第一次询单转订单时的询单时间
			long days = askOrderNumDao.getDistanceDays(date1,endDate);//获得两者之间的天数
			sbf.append(groupBySql(days,month));
		}else if(StringUtils.isNotBlank(endDate) && StringUtils.isNoneBlank(startDate)){
			long days = askOrderNumDao.getDistanceDays(startDate,endDate);//获得两者之间的天数
			sbf.append(groupBySql(days,month));//按粒度分组sql
		}
		sbf.append("order by p.orderTime ASC ");
		List<Map<String,Object>> list = this.findBySql(sbf.toString(),Map.class, UserUtils.getUser().getCompany().getId());
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
			sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d %H') ");//按小时
		}else if(days >= 2 && days<= 58){
			sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m-%d') ");//按天
		}else if(days >= 59 && days <= 365){
			sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m') ");//按月
		}else{
			if(StringUtils.isNotBlank(month) && "month".equals(month)){
				sbf.append("DATE_FORMAT(p.orderTime,'%Y-%m') ");//默认按月
			}else{
				sbf.append("DATE_FORMAT(p.orderTime,'%Y') ");//按年
			}
		}
		return sbf.toString();
	}
	
	 /**
     * 获得该批发商被第一次询单转化成订单的数据
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
}
