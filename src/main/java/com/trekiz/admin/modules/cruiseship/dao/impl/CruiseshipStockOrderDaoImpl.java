/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockOrderDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.jsonbean.CruiseshipOrderQueryJsonBean;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipStockOrderDaoImpl extends BaseDaoImpl<CruiseshipStockOrder>  implements CruiseshipStockOrderDao{
	@Override
	public CruiseshipStockOrder getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipStockOrder cruiseshipStockOrder where cruiseshipStockOrder.uuid=? and cruiseshipStockOrder.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipStockOrder)entity;
		}
		return null;
	}
	
	public boolean batchDelete(String[] uuids) {
		if(ArrayUtils.isEmpty(uuids)) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		for(String uuid : uuids) {
			sb.append("'");
			sb.append(uuid);
			sb.append("'");
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		int count = super.createSqlQuery("update cruiseship_stock_order set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据库存uuid获取创建用户信息集合
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @return   
	 * @return List<CruiseshipStockOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryCreateUsersByStockUuid(String cruiseshipStockUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT stockOrder.createBy, ");
		sql.append(" sysUser.name AS createByName ");
		sql.append(" FROM cruiseship_stock_order stockOrder ");
		sql.append(" LEFT JOIN sys_user sysUser ON sysUser.id = stockOrder.createBy ");
		sql.append(" WHERE stockOrder.cruiseship_stock_uuid=? AND stockOrder.delFlag=?");
		List<Map<String,Object>> list=super.findBySql(sql.toString(), Map.class, cruiseshipStockUuid, BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
	
	/**
	 * 根据游轮库存jsonBean获取游轮库存订单集合
	 * @Description: 
	 * @param @param jsonBean
	 * @param @return   
	 * @return List<CruiseshipStockOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockOrder> getStockOrdersByOrderQueryJsonBean(CruiseshipOrderQueryJsonBean jsonBean) {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isEmpty(jsonBean.getCruiseshipStockUuid())) {
			return null;
		}
		
		StringBuffer querySql = new StringBuffer();
		if(CollectionUtils.isNotEmpty(jsonBean.getActivityList())) {
			querySql.append(" ( ");
			for(CruiseshipStockOrder stockOrder : jsonBean.getActivityList()) {
				querySql.append(" ( activity_id="+stockOrder.getActivityId()+" AND activity_type="+stockOrder.getActivityType()+") ");
				querySql.append("OR");
			}
			querySql.delete(querySql.length()-2, querySql.length());
			querySql.append(" ) ");
		}
		
		sql.append("SELECT * FROM cruiseship_stock_order stockOrder ");
		sql.append(" WHERE stockOrder.cruiseship_stock_uuid=? AND stockOrder.delFlag=?");
		if(querySql.length() != 0) {
			sql.append(" AND ");
			sql.append(querySql);
		}
		
		return super.findBySql(sql.toString(), CruiseshipStockOrder.class, jsonBean.getCruiseshipStockUuid(), BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
