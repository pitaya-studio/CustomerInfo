/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.statisticAnalysis.common.GetEachCycleFirstDay;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.CountAndRateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.CountAndRateService;

/**
 * @author junhao.zhao
 * 2016年12月29日  下午6:08:48
 */
/**
 * 
 * @author gaoyang
 * @Time 2017-3-10 下午8:40:57
 */
@Service
public class CountAndRateServiceImpl implements CountAndRateService{
	@Autowired
	private CountAndRateDao countAndRateDao;
	
	@Autowired
	private AskOrderNumDao askOrderNumDao;
	
	@Autowired
	private OrderAnalysisDao orderAnalysisDao;
	

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.CountAndRateService#getCountAndRate(java.util.Map)
	 */
	@Override
	public Map<String, Object> getCountAndRate(Map<String, String> map) {
		//根据条件查询1：订单数，2：收客人数，3：订单金额
		List<Map<String,Object>> allList = countAndRateDao.getCountAndRate(map);
		Map<String,Object> mapSO= new HashMap<String,Object>();
		if(map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())){
			//格式化数字
			//NumberFormat nf = new DecimalFormat("#,###");
			NumberFormat nfm = new DecimalFormat("#,###.00");
			//将1：订单数，2：收客人数，3：订单金额封装进map
			for(Map<String,Object> mapKey : allList){
				String analysisType = map.get("analysisType").toString();
				if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
					if(mapKey.get("orderNum")!= null && StringUtils.isNotBlank(mapKey.get("orderNum").toString())){
						Integer dOrderNum = Integer.parseInt(mapKey.get("orderNum").toString());
					    mapSO.put("newNum", dOrderNum);
					    mapSO.put("newNumRate", mapKey.get("orderNum"));
					}else{
						mapSO.put("newNum", "0");
					}
				}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
					if(mapKey.get("orderPersonNum")!= null && StringUtils.isNotBlank(mapKey.get("orderPersonNum").toString())){
						Integer dOrderPersonNum = Integer.parseInt(mapKey.get("orderPersonNum").toString());
						mapSO.put("newNum", dOrderPersonNum);
						mapSO.put("newNumRate", mapKey.get("orderPersonNum"));
					}else{
						mapSO.put("newNum", "0");
					}
				}else{
					if(mapKey.get("orderMoney")!= null && StringUtils.isNotBlank(mapKey.get("orderMoney").toString())){
						Double dOrderMoney = Double.parseDouble(mapKey.get("orderMoney").toString());
					    String strOrderMoney = nfm.format(dOrderMoney);
						mapSO.put("newNum", strOrderMoney);
						mapSO.put("newNumber", mapKey.get("orderMoney"));
					}else{
						mapSO.put("newNum","0");
						mapSO.put("newNumber", "0");
					}
				}
			}
		}
		//计算增长率
		//根据1：今日 2：本周 3：本月 4：本年 获取上一日、上周、上个月、去年的1：订单数，2：收客人数，3：订单金额
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
			String searchDateType = map.get("searchDate").toString();
			//5：全部——不用计算
			if(!searchDateType.equals(Context.ORDER_DATA_STATISTICS_ALL)){
			
				//1：今日 2：本周 3：本月 4：本年
				if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
					//根据当前时间，获取昨天
					String yesterdayStart = GetEachCycleFirstDay.getYesterdayStartDate(new Date());
					map.put("searchDateSimple", yesterdayStart);
				}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
					//根据当前时间，获取上周
					String lastWeekSameDayDate = GetEachCycleFirstDay.getLastWeekSameDayDate(new Date());
					map.put("searchDateSimple", lastWeekSameDayDate);
				}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
					//根据当前时间，获取上月
					String firstDayofLastMonthNoDay = GetEachCycleFirstDay.getFirstDayofLastMonthNoDay();
					map.put("searchDateSimple", firstDayofLastMonthNoDay);
				}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
					//根据当前时间，获取去年
					String firstDayofLastYearNoMonth = GetEachCycleFirstDay.getFirstDayofLastYearNoMonth();
					map.put("searchDateSimple", firstDayofLastYearNoMonth);
				}
				
				List<Map<String,Object>> rateList = countAndRateDao.getCountAndRate(map);
				
				//计算百分比
				for(Map<String,Object> returnMap : rateList){
					String analysisType = map.get("analysisType").toString();
					String yesterdayNum="0";
					String todayNum="0";
					if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
						if(returnMap.get("orderNum")!= null && StringUtils.isNotBlank(returnMap.get("orderNum").toString())){
							//获取上周期订单数
							yesterdayNum = returnMap.get("orderNum").toString();
						}else{
							yesterdayNum = "0";
						}
						if(mapSO.get("newNumRate")!= null && StringUtils.isNotBlank(mapSO.get("newNumRate").toString())){
							//获取当前周期订单数
							todayNum = mapSO.get("newNumRate").toString();
						}
					}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
						if(returnMap.get("orderPersonNum")!= null && StringUtils.isNotBlank(returnMap.get("orderPersonNum").toString())){
							//获取上周期收客人数
							yesterdayNum = returnMap.get("orderPersonNum").toString();
						}else{
							yesterdayNum = "0";
						}
						if(mapSO.get("newNumRate")!= null && StringUtils.isNotBlank(mapSO.get("newNumRate").toString())){
							//获取当前周期收客人数
							todayNum = mapSO.get("newNumRate").toString();
						}
					}else{
						if(returnMap.get("orderMoney")!= null && StringUtils.isNotBlank(returnMap.get("orderMoney").toString())){
							//获取上周期订单金额
							yesterdayNum = returnMap.get("orderMoney").toString();
						}else{
							yesterdayNum = "0";
						}
						if(mapSO.get("newNumber")!= null && StringUtils.isNotBlank(mapSO.get("newNumber").toString())){
							//获取当前周期订单金额
							todayNum = mapSO.get("newNumber").toString();
						}
					}
					//计算增长率数值
					//被除数不能为0
					BigDecimal yesterdayVal = new BigDecimal(Double.parseDouble(yesterdayNum)); 
					String incrementRate = "0%";
					if(yesterdayVal.compareTo(new BigDecimal(0)) != 0){
						//当前周期数值-上一周期结束节点数值
						Double riseNumber = Double.parseDouble(todayNum)-Double.parseDouble(yesterdayNum);
						//百分数保留到个位
						BigDecimal riseNum = new BigDecimal(riseNumber+"").divide(new BigDecimal(yesterdayNum), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP);
						//为传值进行装配
						incrementRate = riseNum.toString();
						int compareTo = riseNum.compareTo(new BigDecimal(0));
						if(compareTo>0){
							incrementRate = "+"+riseNum+"%";
						}else{
							incrementRate = riseNum+"%";
						}
					}else{
						//被除数不能为0
						BigDecimal todayNumVal = new BigDecimal(Double.parseDouble(todayNum));
						if(todayNumVal.compareTo(new BigDecimal(0)) == 0){
							incrementRate = "0%";
						}else{
							incrementRate = "+100%";
						}
					}
					mapSO.put("incrementRate", incrementRate);
				}
			
			}	
		}
		
		return mapSO;
	}
	
	@Override
	public Map<String, Object> getPreOrderCountAndRate(Map<String, String> map) {
		List<Map<String,Object>> allList = countAndRateDao.getPreOrderCountAndRate(map);
		Map<String,Object> mapSO = new HashMap<String,Object>();
		String nowCycleNum = "0"; // 当前周期询单数
		String lastCycleNum = "0"; // 上一周期询单数
		for (Map<String,Object> mapKey : allList) {
			if(mapKey.get("orderPreNum")!= null && StringUtils.isNotBlank(mapKey.get("orderPreNum").toString())) {
				nowCycleNum = mapKey.get("orderPreNum").toString();
			}
		}
		mapSO.put("newNum", nowCycleNum);
		
		// 计算增长率
		// 根据 时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 
		// 时间周期选取今日、上月、昨天、去年、全部、自定义时，无增长率字段(只有本月和本年时才会有增长率字段)
		// 当上一周期的总数值为0时，不展示增长率字段 incrementRate:设置为-1
		String incrementRate = "-1";
		if (map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())) {
			String searchDate = map.get("searchDate").toString();
			// 只有本月和本年才会有增长率
			if (StringUtils.isBlank(map.get("startDate").toString()) 
					&& StringUtils.isBlank(map.get("endDate").toString())) {
				if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH) 
						|| searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
					List<Map<String,Object>> rateList = null;
					// 如果是本月就获取上个月的值
					if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
						map.put("searchDate", Context.ORDER_DATA_STATISTICS_LAST_MONTH);
						rateList = countAndRateDao.getPreOrderCountAndRate(map);
					}
					// 如果是今年就获取去年的值
					if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
						map.put("searchDate", Context.ORDER_DATA_STATISTICS_LAST_YEAR);
						rateList = countAndRateDao.getPreOrderCountAndRate(map);
					}
					
					for (Map<String, Object> returnMap : rateList) {
						// 获取上周期询单数
						if (returnMap.get("orderPreNum")!= null && StringUtils.isNotBlank(returnMap.get("orderPreNum").toString())) {
							lastCycleNum = returnMap.get("orderPreNum").toString();
						}
						//计算增长率数值
						incrementRate = getGrowthRate(nowCycleNum, lastCycleNum);
					}
				}
			}
		}
		mapSO.put("incrementRate", incrementRate);

		return mapSO;
	}
	
	@Override
	public Map<String, Object> getOrderCountAndRate(Map<String, String> map) {
		// 根据条件查询 1：订单数，2：收客人数，3：订单金额
		List<Map<String,Object>> allList = countAndRateDao.getOrderCountAndRate(map);
		Map<String,Object> mapSO = new HashMap<String,Object>();
		if (map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())) {
			// 将1：订单数，2：收客人数，3：订单金额封装进map
			for (Map<String,Object> mapKey : allList) {
				String analysisType = map.get("analysisType").toString();
				if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)) {
					if(mapKey.get("orderNum")!= null && StringUtils.isNotBlank(mapKey.get("orderNum").toString())) {
					    mapSO.put("newNum", mapKey.get("orderNum").toString());
					} else {
						mapSO.put("newNum", "0");
					}
				} else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)) {
					if (mapKey.get("orderPersonNum")!= null && StringUtils.isNotBlank(mapKey.get("orderPersonNum").toString())) {
						mapSO.put("newNum", mapKey.get("orderPersonNum").toString());
					} else {
						mapSO.put("newNum", "0");
					}
				} else {
					if (mapKey.get("orderMoney")!= null && StringUtils.isNotBlank(mapKey.get("orderMoney").toString())) {
						DecimalFormat decimalFormat = new DecimalFormat("#.00");
						mapSO.put("newNum", decimalFormat.format(Double.parseDouble(mapKey.get("orderMoney").toString())));
					} else {
						mapSO.put("newNum","0");
					}
				}
			}
		}
		
		// 计算增长率
		// 根据 时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部  1：订单数，2：收客人数，3：订单金额
		// 时间周期选取今日、上月、昨天、去年、全部、自定义时，无增长率字段(只有本月和本年时才会有增长率字段)
		// 当上一周期的总数值为0时，不展示增长率字段 incrementRate:设置为-1
		String incrementRate = "-1";
		if (map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())) {
			String searchDate = map.get("searchDate").toString();
			// 自定义无增长率
			if (StringUtils.isBlank(map.get("startDate").toString()) 
					&& StringUtils.isBlank(map.get("endDate").toString())) {
				// 只有本月和本年才会有增长率
				if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH) 
						|| searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
					List<Map<String,Object>> rateList = null;
					// 如果是本月就获取上个月的值
					if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
						map.put("searchDate", Context.ORDER_DATA_STATISTICS_LAST_MONTH);
						rateList = countAndRateDao.getOrderCountAndRate(map);
					}
					// 如果是今年就获取去年的值
					if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
						map.put("searchDate", Context.ORDER_DATA_STATISTICS_LAST_YEAR);
						rateList = countAndRateDao.getOrderCountAndRate(map);
					}
					for (Map<String, Object> returnMap : rateList) {
						String analysisType = map.get("analysisType").toString();
						String nowCycleNum = mapSO.get("newNum").toString(); // 获取当前周期(订单/收客/订单金额)数
						String lastCycleNum = "0";
						if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)) {
							// 获取上周期订单数
							if (returnMap.get("orderNum")!= null && StringUtils.isNotBlank(returnMap.get("orderNum").toString())) {
								lastCycleNum = returnMap.get("orderNum").toString();
							} else {
								lastCycleNum = "0";
							}
						} else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)) {
							//获取上周期收客人数
							if (returnMap.get("orderPersonNum")!= null && StringUtils.isNotBlank(returnMap.get("orderPersonNum").toString())) {
								lastCycleNum = returnMap.get("orderPersonNum").toString();
							} else {
								lastCycleNum = "0";
							}
						} else {
							//获取上周期订单金额
							if (returnMap.get("orderMoney")!= null && StringUtils.isNotBlank(returnMap.get("orderMoney").toString())) {
								lastCycleNum = returnMap.get("orderMoney").toString();
							} else {
								lastCycleNum = "0";
							}
						}
						//计算增长率数值
						incrementRate = getGrowthRate(nowCycleNum, lastCycleNum);
					}
				}
			}
		}
		mapSO.put("incrementRate", incrementRate);
		
		return mapSO;
	}
	
	/**
	 * 根据当前周期值与过去周期值计算增长率
	 * 公式(当前周期的数值-上一周期总数值) / 上一周期总数值
	 * @author gaoyang
	 * @Time 2017-3-10 下午2:48:57
	 * @param nowCycleNum 当前周期的数值
	 * @param lastCycleNum 上一周期总数值
	 * @return 返回结果，如果上一周期为0则返回"-1"
	 */
	private String getGrowthRate(String nowCycleNum, String lastCycleNum) {
		String incrementRate = "-1";
		BigDecimal lastCycleNumVal = new BigDecimal(Double.parseDouble(lastCycleNum));
		if (lastCycleNumVal.compareTo(new BigDecimal(0)) != 0) {
			//当前周期数值-上一周期结束节点数值
			Double riseNumber = Double.parseDouble(nowCycleNum)-Double.parseDouble(lastCycleNum);
			//百分数保留到个位
			BigDecimal riseNum = new BigDecimal(riseNumber + "")
				.divide(new BigDecimal(lastCycleNum), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal("100"))
				.setScale(0, BigDecimal.ROUND_HALF_UP);
			//为传值进行装配
			incrementRate = riseNum.toString();
			int compareTo = riseNum.compareTo(new BigDecimal(0));
			if (compareTo>0) {
				incrementRate = "+" + riseNum + "%";
			} else {
				incrementRate = riseNum + "%";
			}
		}
		return incrementRate;
	}
	
	@Override
	public List<Map<String, Object>> getFirstDateAskOrderList() {
		return askOrderNumDao.getList();
	}
	
	@Override
	public List<Map<String, Object>> getFirstDateOrderList() {
		return orderAnalysisDao.getList();
	}
}