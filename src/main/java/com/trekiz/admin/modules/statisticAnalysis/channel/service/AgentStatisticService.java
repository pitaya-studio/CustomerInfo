package com.trekiz.admin.modules.statisticAnalysis.channel.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;

public interface AgentStatisticService {
	
	/**
	 * 获得渠道分析列表
	 * @param page
	 * @param map
	 * @param orderBy
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	public Page<Map<String,Object>> getAgentStatisticPageList(Page<Map<String,Object>> page,Map<String,Object> map,String orderBy);
	
	/**
	 * 查询订单总数，收客总人数，订单总金额
	 * @param map
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	public Map<String,Object> getTotalNum(Map<String,Object> map);
	
	/**
	 * 渠道分析图表的数据查询
	 * @param map
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	public Map<String,Object> getListForOrderType(Map<String,Object> map);
}
