package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderTypesDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * @author junhao.zhao
 * 2016年12月27日  下午7:07:07
 */
@Repository
public class OrderTypesDaoImpl extends BaseDaoImpl<OrderDataStatistics> implements OrderTypesDao{

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderTypesDao#getOrderTypes(java.lang.String)
	 * 判断顶栏——单团、散拼、游学、大客户、自由行、签证、(机票、)游轮--是否展示
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getOrderTypes(String orderType, Long companyId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT activity_kind FROM travelactivity ");
		sbf.append("WHERE ");
		sbf.append("proCompany = ? ");
		sbf.append("AND activity_kind = ?  LIMIT 1");
		SQLQuery createSqlQuery = super.createSqlQuery(sbf.toString(), companyId, orderType);
		List list = createSqlQuery.list();
		if(list != null && list.size() > 0 ){
			if(list.get(0) != null){
				Object object = list.get(0);
				return object.toString();
			}
		}
		return "";
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderTypesDao#getOrderTypesForVisa()
	 */
	public String getOrderTypesForVisa(Long companyId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT 6 FROM visa_products ");
		sbf.append("WHERE ");
		sbf.append("proCompanyId = ? ");
		sbf.append("LIMIT 1 ");
		SQLQuery createSqlQuery = super.createSqlQuery(sbf.toString(), companyId);
		List list = createSqlQuery.list();
		if(list != null && list.size() > 0 ){
			if(list.get(0) != null){
				Object object = list.get(0);
				return object.toString();
			}
		}
		return "";
	}

}