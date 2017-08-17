package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.util.Map;

public interface OrderAnalysisService {
	
	/**
	 * 订单总览折线图数据
	 * @param searchDate 搜索时间 （/时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 ）
	 * @param startDate 自定义开始时间
	 * @param endDate 自定义结束时间
	 * @param year 按年显示（超过365天）
	 * @param month 按月显示（超过365天）
	 * @param analysisType 分析类型: 1 订单数 2收客人数 3订单金额
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-09
	 */
	public Map<String,Object> getOrderAnalysisData(String searchDate,
			String startDate, String endDate, String year,
			String month, String analysisType);
}
