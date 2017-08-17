package com.trekiz.admin.modules.statisticAnalysis.channel.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;

public interface AgentDetailService {

	/**
	 * 渠道详情页
	 * @author yang.wang
	 * @date 2017.3.9
	 * */
	public Page<Map<String, Object>> getAgentDetailList(Page<Map<String, Object>> page, Map<String, Object> map);
	
//	/**
//	 * 获取excel文件名称 : 起始时间（yyyy-MM-dd）-结束时间（yyyy-MM-dd）
//	 * @author yang.wang
//	 * @date 2017.3.10
//	 * */
//	public String getExcelFileName(Map<String, Object> map);
}
