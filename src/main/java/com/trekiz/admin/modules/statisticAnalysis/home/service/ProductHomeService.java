/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.util.List;
import java.util.Map;

/**
 * @author junhao.zhao
 * 2016年12月28日  下午3:35:40
 */
public interface ProductHomeService {

	/**首页产品展示：按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮)、
	 * 分析类型（1：订单数，2：收客人数，3：订单金额）、时间 （1：今日 2：本周 3：本月 4：本年 5：全部）这三种条件查询的接口
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getListForProductHome(Map<String, Object> map);

}
