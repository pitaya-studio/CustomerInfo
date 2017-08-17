package com.trekiz.admin.modules.statisticAnalysis.channel.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;

/**
 * @author junhao.zhao
 *
 * 2017年3月8日  下午6:11:36
 */
@SuppressWarnings("rawtypes")
public interface EnquiryAgentStatisticDao extends BaseDao {
	
	
	/**根据条件查询最多6条-询单-数据
	 * @param map
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> getListForAgentPercentChart(Map<String, Object> map, Long companyId);
	
	
	/**查询满足条件的-总询单-条数
	 * @param map
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> getEnquiryTotalNum(Map<String, Object> map, Long companyId);


	/**根据条件查询最多6条-订单-数据
	 * @param map
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> getListForOrderPercentChart(Map<String, Object> map, Long companyId);
	
	/**查询满足条件的-总订单-条数
	 * @param map
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> getOrderTotalNum(Map<String, Object> map, Long companyId);

}
