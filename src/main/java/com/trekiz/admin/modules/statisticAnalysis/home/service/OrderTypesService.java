package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.util.List;
import java.util.Map;

/**
 * @author junhao.zhao
 * 2016年12月27日  下午6:28:55
 */
public interface OrderTypesService {
	
	/** 功能： 顶栏——单团、散拼、游学、大客户、自由行、签证、(机票、)游轮--是否展示
	 * @param 
	 * @return ArrayList<String>
	 */
	public List<Map<String, Object>> getOrderTypes();
}
