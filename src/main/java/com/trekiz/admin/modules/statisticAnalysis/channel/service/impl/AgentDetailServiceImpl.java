package com.trekiz.admin.modules.statisticAnalysis.channel.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.AgentDetailDao;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.AgentDetailService;

@Service
public class AgentDetailServiceImpl implements AgentDetailService {

	@Autowired
	private AgentDetailDao agentDetailDao;
	
	@Override
	public Page<Map<String, Object>> getAgentDetailList(
			Page<Map<String, Object>> page, Map<String, Object> map) {
		
		// 获取排序字段和方式
		String orderBy = (String) map.get("orderBy");
		switch (orderBy) {
			case "1": 	orderBy = "orderNum DESC"; 	break;
			case "2": 	orderBy = "orderNum ASC"; 	break;
			case "3": 	orderBy = "orderAmount DESC"; 	break;
			case "4": 	orderBy = "orderAmount ASC"; 	break;
			case "5": 	orderBy = "orderPersonNum DESC"; 	break;
			case "6": 	orderBy = "orderPersonNum ASC"; 	break;
			case "7": 	orderBy = "enquiryNum DESC"; 	break;
			case "8": 	orderBy = "enquiryNum ASC"; 	break;
			default:	break;
		}
		
		return agentDetailDao.getAgentDetailList(page, map, orderBy);
	}

}
