/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;

/**
 * @author junhao.zhao
 * 2016年12月28日  下午3:36:39
 */
public interface ProductHomeDao extends BaseDao<OrderDataStatistics>{

	/**首页产品展示：按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票) 10：游轮)、
	 * 分析类型（1：订单数，2：收客人数，3：订单金额）、时间 （1：今日 2：本周 3：本月 4：本年 5：全部）这三种条件查询的接口
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getListForOrderType(Map<String, Object> map);

	/**（下订单的产品线小于5，只有0-4条，剩下的从团类产品表travelactivity中按产品名称降序排序，查出名称来展示）改为：
	 * 大于0小于5，只有1-4条，剩下的从团类产品表travelactivity中按产品名称降序排序，查出名称来展示;为0，不展示。团类产品表不足，从签证产品表visa_products中查，仍不够，不管。
	 * 订单数为0时，先团期后签证，先上架后下架，按产品线分，不按时间分。
	 * @param productId
	 * @param visaId
	 * @param size 
	 */
	public List<Map<String, Object>> getListForProductId(Map<String, Object> map, String productId, String visaId, int size);

}
