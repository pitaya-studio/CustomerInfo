package com.trekiz.admin.modules.statisticAnalysis.channel.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

/**
 * @time 2016-12-22 
 * @author chao.zhang
 */
public interface AgentStatisticDao extends BaseDao{
	/**
	 * 渠道分析详情列表
	 * @param page
	 * @param map 存在查询条件
	 * @param oderBy 排序
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	public Page<Map<String,Object>> getAgentStatisticDatas(Page<Map<String,Object>> page,Map<String,Object> map,String orderBy);
	
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
	 * @data 2016-12-23
	 */
	public List<Map<String,Object>> getListForOrderType(Map<String,Object> map);
}
