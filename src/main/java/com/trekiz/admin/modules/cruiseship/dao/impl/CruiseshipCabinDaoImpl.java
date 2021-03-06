/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipCabinDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipCabinDaoImpl extends BaseDaoImpl<CruiseshipCabin>  implements CruiseshipCabinDao{
	@Override
	public CruiseshipCabin getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipCabin cruiseshipCabin where cruiseshipCabin.uuid=? and cruiseshipCabin.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipCabin)entity;
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
		int count = super.createSqlQuery("update cruiseship_cabin set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
}
