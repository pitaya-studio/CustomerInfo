/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.ProductHomeDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * @author junhao.zhao
 * 2016年12月28日  下午3:36:56
 */
@Repository
public class ProductHomeDaoImpl extends BaseDaoImpl<OrderDataStatistics> implements ProductHomeDao{

	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.ProductHomeDao#getListForOrderType(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getListForOrderType(Map<String, Object> map) {
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("product_name AS productName, ");
		sbf.append("product_id AS productId, ");
		sbf.append("order_type AS orderType, ");
		sbf.append("COUNT(id) AS orderNum, ");
		sbf.append("SUM(amount) AS orderMoney, ");
		sbf.append("SUM(order_person_num) AS orderPersonNum ");
		sbf.append("FROM ");
		sbf.append("order_data_statistics ods ");
		sbf.append("WHERE del_flag = '"+BaseEntity.DEL_FLAG_NORMAL+"' ");
		sbf.append("AND ods.company_uuid = '"+companyUuid+"' ");
		sbf.append("AND CASE WHEN order_type = '"+Context.ORDER_STATUS_VISA+"' THEN order_status != '"+Context.VISA_ORDER_PAYSTATUS_CANCEL+"' ELSE order_status NOT IN('"+Context.ORDER_PAYSTATUS_YQX+"','"+Context.ORDER_PAYSTATUS_DEL+"') END ");
		//订单类型
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString()) && ! map.get("orderType").toString().equals("0")){
			sbf.append("AND order_type = ").append(map.get("orderType").toString()).append(" ");
		}
		//时间搜索
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
			//1：今日 2：本周 3：本月 4：今年  5：全部
			String searchDate = map.get("searchDate").toString();
			String  searchDateSimple = map.get("searchDateSimple").toString();
			if(searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') ='").append(searchDateSimple).append("' ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
				sbf.append("AND DATE_FORMAT(order_createtime,'%x%v') = DATE_FORMAT('").append(searchDateSimple).append("','%x%v') ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m') = '").append(searchDateSimple).append("' ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
				sbf.append("AND DATE_FORMAT(order_createtime, '%Y') = '").append(searchDateSimple).append("' ");
			}
		}
		//自定义时间搜索
		if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
			sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') >= '").append(map.get("startDate").toString()).append("' ");
		}
		if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
			sbf.append("AND DATE_FORMAT(order_createtime, '%Y-%m-%d') <= '").append(map.get("endDate").toString()).append("' ");
		}
		
		sbf.append("GROUP BY ");
		sbf.append("product_id ");
		
		sbf.append("ORDER BY ");
		//分析类型搜索
		if(map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())){
			String analysisType = map.get("analysisType").toString();
			if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
				sbf.append("COUNT(id)");
			}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
				sbf.append("SUM(order_person_num)");
			}else{
				sbf.append("SUM(amount)");
			}
			
			sbf.append(" DESC, ");
		}
		//限定五条数据
		sbf.append("orderType ASC, CONVERT(productName USING gbk) ASC LIMIT 0,5");
		
		List<Map<String, Object>> list = this.findBySql(sbf.toString(), Map.class);
		return list;
	}

	
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.dao.ProductHomeDao#getListForProductId(java.lang.String, java.lang.String, int)
	 * ProductHomeDaoImpl getListForProductId
	 * 
	 */
	@Override
	public List<Map<String, Object>> getListForProductId(Map<String, Object> map, String productId, String visaId, int size) {
		// TODO Auto-generated method stub
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sbf = new StringBuffer();
		//获取团期类上架的产品
		sbf.append("(SELECT ");
		sbf.append("tr.acitivityName AS productName ");
		sbf.append("FROM ");
		sbf.append("travelactivity tr ");
		sbf.append("LEFT JOIN sys_office so ON tr.proCompany = so.id ");
		sbf.append("WHERE tr.delFlag = '"+Context.DEL_FLAG_NORMAL+"' ");
		if(StringUtils.isNotBlank(productId)){
			sbf.append("AND tr.id not IN("+productId+") ");
		}
		sbf.append("AND tr.activityStatus = '"+Context.PRODUCT_ONLINE_STATUS+"' ");
		//订单类型筛选
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString()) && ! map.get("orderType").toString().equals("0")){
			sbf.append("AND tr.activity_kind = ").append(map.get("orderType").toString()).append(" ");
		}
		//时间搜索
		//暂时不加时间限制
//		StringBuffer searchDate = searchDate(map);
//		sbf.append(searchDate);
		
		sbf.append("AND so.uuid = '"+companyUuid+"' ");
		sbf.append("ORDER BY CONVERT(productName USING gbk) ASC LIMIT 5) ");
		//订单类型筛选
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString())){
			if(map.get("orderType").toString().equals("0") || map.get("orderType").toString().equals("6")){
				//获取签证类上架的产品
				sbf.append("UNION ALL ");
				sbf.append("(SELECT ");
				sbf.append("tr.productName AS productName ");
				sbf.append("FROM ");
				sbf.append("visa_products tr ");
				sbf.append("LEFT JOIN sys_office so ON tr.proCompanyId = so.id ");
				sbf.append("WHERE tr.delFlag = '"+Context.DEL_FLAG_NORMAL+"' ");
				if(StringUtils.isNotBlank(visaId)){
					sbf.append("AND tr.id not IN("+visaId+") ");
				}
				sbf.append("AND tr.productStatus = '"+Context.PRODUCT_ONLINE_STATUS+"' ");
				sbf.append("AND so.uuid = '"+companyUuid+"' ");
				//时间搜索
//				StringBuffer searchDateVisaOn = searchDate(map);
//				sbf.append(searchDateVisaOn);
				sbf.append("ORDER BY CONVERT(productName USING gbk) ASC LIMIT 5) ");
			}
		}
		//获取团期类下架的产品
		sbf.append("UNION ALL ");
		sbf.append("(SELECT ");
		sbf.append("tr.acitivityName AS productName ");
		sbf.append("FROM ");
		sbf.append("travelactivity tr ");
		sbf.append("LEFT JOIN sys_office so ON tr.proCompany = so.id ");
		sbf.append("WHERE tr.delFlag = '"+Context.DEL_FLAG_NORMAL+"' ");
		if(StringUtils.isNotBlank(productId)){
			sbf.append("AND tr.id not IN("+productId+") ");
		}
		sbf.append("AND tr.activityStatus = '"+Context.PRODUCT_OFFLINE_STATUS+"' ");
		//订单类型筛选
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString()) && ! map.get("orderType").toString().equals("0")){
			sbf.append("AND tr.activity_kind = ").append(map.get("orderType").toString()).append(" ");
		}
		//时间搜索
//		StringBuffer searchDateOff = searchDate(map);
//		sbf.append(searchDateOff);
		
		sbf.append("AND so.uuid = '"+companyUuid+"' ");
		sbf.append("ORDER BY CONVERT(productName USING gbk) ASC LIMIT 5) ");
		//订单类型筛选
		if(map.get("orderType") != null && StringUtils.isNotBlank(map.get("orderType").toString())){
			if(map.get("orderType").toString().equals("0") || map.get("orderType").toString().equals("6")){
				//获取签证类下架的产品
				sbf.append("UNION ALL ");
				sbf.append("(SELECT ");
				sbf.append("tr.productName AS productName ");
				sbf.append("FROM ");
				sbf.append("visa_products tr ");
				sbf.append("LEFT JOIN sys_office so ON tr.proCompanyId = so.id ");
				sbf.append("WHERE tr.delFlag = '"+Context.DEL_FLAG_NORMAL+"' ");
				if(StringUtils.isNotBlank(visaId)){
					sbf.append("AND tr.id not IN("+visaId+") ");
				}
				sbf.append("AND tr.productStatus = '"+Context.PRODUCT_OFFLINE_STATUS+"' ");
				sbf.append("AND so.uuid = '"+companyUuid+"' ");
				//时间搜索
//				StringBuffer searchDateVisaOff = searchDate(map);
//				sbf.append(searchDateVisaOff);
				sbf.append("ORDER BY CONVERT(productName USING gbk) ASC LIMIT 5) ");
			}
		}
		sbf.append("LIMIT 0,"+size+" ");
	
		List<Map<String, Object>> findBySql = this.findBySql(sbf.toString(), Map.class);
		return findBySql;
	}
	//从getListForProductId(Map<String, Object> map, String productId, String visaId, int size)方法中提炼出的方法。 
	private StringBuffer searchDate(Map<String, Object> map){
		StringBuffer sbf = new StringBuffer();
		if(map.get("searchDate") != null && StringUtils.isNotBlank(map.get("searchDate").toString())){
			//1：今日 2：本周 3：本月 4：今年  5：全部
			String searchDate = map.get("searchDate").toString();
			String  searchDateSimple = map.get("searchDateSimple").toString();
			if(searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
				sbf.append("AND DATE_FORMAT(tr.createDate, '%Y-%m-%d') ='").append(searchDateSimple).append("' ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
				sbf.append("AND DATE_FORMAT(tr.createDate,'%x%v') = DATE_FORMAT('").append(searchDateSimple).append("','%x%v') ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
				sbf.append("AND DATE_FORMAT(tr.createDate, '%Y-%m') = '").append(searchDateSimple).append("' ");
			}else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
				sbf.append("AND DATE_FORMAT(tr.createDate, '%Y') = '").append(searchDateSimple).append("' ");
			}
		}
		//自定义时间搜索
		if(map.get("startDate") != null && StringUtils.isNotBlank(map.get("startDate").toString())){
			sbf.append("AND DATE_FORMAT(tr.createDate, '%Y-%m-%d') >= '").append(map.get("startDate").toString()).append("' ");
		}
		if(map.get("endDate") != null && StringUtils.isNotBlank(map.get("endDate").toString())){
			sbf.append("AND DATE_FORMAT(tr.createDate, '%Y-%m-%d') <= '").append(map.get("endDate").toString()).append("' ");
		}
		
		return sbf;
	}
	
	
	
	
	
	
}
