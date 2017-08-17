package com.trekiz.admin.modules.statisticAnalysis.home.dao;


import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;

/**
 * @author junhao.zhao
 * 2016年12月27日  下午7:06:29
 */
public interface OrderTypesDao extends BaseDao<OrderDataStatistics> {
	
		/** 功能： 顶栏——单团、散拼、游学、大客户、自由行、游轮--是否展示
		 * @param orderType
		 * @return  
		 */
		public String getOrderTypes(String orderType, Long companyId);
		
		/** 功能： 顶栏——签证--是否展示
		 * @param orderType
		 * @return  
		 */
		public String getOrderTypesForVisa(Long companyId);
}
