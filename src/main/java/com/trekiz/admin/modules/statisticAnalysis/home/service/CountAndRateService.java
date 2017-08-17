package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.util.List;
import java.util.Map;

/**
 * @author junhao.zhao
 * 2016年12月29日  下午6:08:24
 */
public interface CountAndRateService {
	
	/**xx新增与增长率：按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票)10：游轮)、
	 * 分析类型（1：订单数，2：收客人数，3：订单金额）、时间 （1：今日 2：本周 3：本月 4：本年 5：全部  自定义）这三种条件查询的接口
	 * @param map
	 * @return
	 */
	public Map<String,Object> getCountAndRate(Map<String,String> map);
	
	/**
	 * 根据分析类型及时间获取新增询单数与增长率(数据统计分析V2版--询单)
	 * 按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票)10：游轮) 当前只有散拼
	 * 时间        (1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 )
	 * @author gaoyang
	 * @Time 2017-3-10 上午10:52:47
	 * @param map
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getPreOrderCountAndRate(Map<String,String> map);
	
	/**
	 * 根据分析类型及时间获取数据新增与增长率(数据统计分析V2版--订单)
	 * 按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票)10：游轮) 当前只有散拼
	 * 分析类型(1：订单数, 2：收客人数, 3：订单金额)
	 * 时间        (1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 )
	 * @author gaoyang
	 * @Time 2017-3-10 上午10:52:47
	 * @param map
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getOrderCountAndRate(Map<String,String> map);
	
	/**
	 * 获得该批发商被第一次询单的询单数据
	 * @Time 2017-3-10 下午8:37:38
	 * @param
	 * @return
	 */
	public List<Map<String,Object>> getFirstDateAskOrderList();
	
	/**
	 * 获得该批发商被第一次询单转化成订单的数据
	 * @Time 2017-3-10 下午8:37:38
	 * @param
	 * @return
	 */
	public List<Map<String,Object>> getFirstDateOrderList();
}
