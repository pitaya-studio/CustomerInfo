/**
 * agentPercentChartService.java
 */
package com.trekiz.admin.modules.statisticAnalysis.channel.service;

import java.util.List;
import java.util.Map;

/**
 * @author junhao.zhao
 *
 * 2017年3月8日  下午6:10:02
 */
public interface EnquiryAgentStatisticService {

	/**根据条件查询——询单——前五条数据与“其他”数据
	 * @param map
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> getListForAgentPercentChart(Map<String, Object> map, Long companyId);

	/**根据条件查询——订单——前五条数据与“其他”数据
	 * @param map
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getListForOrderPercentChart(Map<String, Object> map, Long companyId);
}
