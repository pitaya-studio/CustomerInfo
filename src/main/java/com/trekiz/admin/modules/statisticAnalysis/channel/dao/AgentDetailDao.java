package com.trekiz.admin.modules.statisticAnalysis.channel.dao;

import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

public interface AgentDetailDao extends BaseDao {

	/**
	 * 渠道详情页
	 * @author yang.wang
	 * @date 2017.3.9
	 * */
	public Page<Map<String, Object>> getAgentDetailList(Page<Map<String, Object>> page, Map<String, Object> map, String orderBy);
	
	/**
	 * 获取第一条询单时间 yyyy-MM-dd
	 * @author yang.wang
	 * @date 2017.3.10
	 * */
	public String getEarliestAskTime();
}
