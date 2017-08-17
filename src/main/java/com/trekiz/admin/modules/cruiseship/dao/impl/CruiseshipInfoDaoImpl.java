/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipInfoDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipInfoDaoImpl extends BaseDaoImpl<CruiseshipInfo>  implements CruiseshipInfoDao{
	@Override
	public CruiseshipInfo getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipInfo cruiseshipInfo where cruiseshipInfo.uuid=? and cruiseshipInfo.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipInfo)entity;
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
		int count = super.createSqlQuery("update cruiseship_info set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据批发商id获取该批发商下所有的游轮基础信息
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<CruiseshipInfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	@SuppressWarnings("unchecked")
	public List<CruiseshipInfo> findByWholesalerId(Long wholesalerId) {
		StringBuffer sb = new StringBuffer();
//		sb.append(" SELECT info.uuid AS UUID,info.name AS NAME FROM cruiseship_info info WHERE info.wholesaler_id = ? AND info.delFlag=? ");
		sb.append("SELECT  ");
		sb.append(" distinct info.`name` as name,  ");
		sb.append("info.uuid AS UUID ");
		sb.append("FROM ");
		sb.append("cruiseship_info info ");
		sb.append("LEFT JOIN cruiseship_stock stock ON info.uuid = stock.cruiseship_info_uuid LEFT JOIN cruiseship_stock_group_rel rel on stock.uuid = rel.cruiseship_stock_uuid ");
		sb.append("WHERE ");
		sb.append("info.wholesaler_id = ? ");
		sb.append("AND info.delFlag =  ? ");
		sb.append("AND rel.`STATUS` in (1,0)  ");
		return (List<CruiseshipInfo>)super.findCustomObjBySql(sb.toString(), CruiseshipInfo.class, wholesalerId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
