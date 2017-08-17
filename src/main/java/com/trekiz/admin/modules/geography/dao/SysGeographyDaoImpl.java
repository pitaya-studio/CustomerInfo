/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.dao;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.geography.entity.SysGeography;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SysGeographyDaoImpl extends BaseDaoImpl<SysGeography>  implements SysGeographyDao{
	
	public void save (SysGeography sysGeography){
		getSession().save(sysGeography);
		getSession().flush();
	}
	
	public void update (SysGeography sysGeography){
		getSession().update(sysGeography);
		getSession().flush();
	}
	public SysGeography getById(java.lang.Integer value) {
		return (SysGeography)getSession().get(SysGeography.class, value);
	}	
	public void removeById(java.lang.Integer value){
		SysGeography obj = getById(value);
		obj.setDelFlag("1");
		getSession().update(obj);
		getSession().flush();
	}
	
	public void updateGeographys(List<SysGeography> sysGeographys) {
		if(sysGeographys == null || sysGeographys.isEmpty()) {
			return ;
		}
		
		for(SysGeography sysGeography : sysGeographys) {
			getSession().update(sysGeography);
		}
		getSession().flush();
	}
	
	public SysGeography getByUuid(String uuid) {
		Object entity = super.createQuery("from SysGeography sysGeography where sysGeography.uuid=? and sysGeography.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (SysGeography)entity;
		}
		return null;
	}

}
