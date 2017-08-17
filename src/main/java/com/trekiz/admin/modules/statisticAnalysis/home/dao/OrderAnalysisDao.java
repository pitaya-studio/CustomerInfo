package com.trekiz.admin.modules.statisticAnalysis.home.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;

public interface OrderAnalysisDao extends BaseDao {
	
	/**
	 * 订单总览折线图数据
	 * @param searchDate
	 * @param startDate
	 * @param endDate
	 * @param dataFormat
	 * @param year
	 * @param month
	 * @param analysisType
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-09
	 */
	public List<Map<String, Object>> getOrderAnalysisData(String searchDate,
			String startDate, String endDate, String dataFormat, String year,
			String month, String analysisType);
	
	 /**
     * 获得该批发商被第一次询单转化成订单的数据
     * @return
	 * @author chao.zhang
	 * @date 2017-3-7
     */
	public List<Map<String,Object>> getList();
}
