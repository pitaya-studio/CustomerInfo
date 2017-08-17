/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.SysdefinedictDao;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SysdefinedictDaoImpl extends BaseDaoImpl<Sysdefinedict>  implements SysdefinedictDao{
	
	
	
	public List<Sysdefinedict> findAll() {
		return super.find("from Sysdefinedict");
	}
	
	public void updateSysdefinedicts(List<Sysdefinedict> sysdefinedicts) {
		if(sysdefinedicts != null && !sysdefinedicts.isEmpty()) {
			for(Sysdefinedict sysdefinedict : sysdefinedicts) {
				getSession().update(sysdefinedict);
			}
			getSession().flush();
		}
	}
	
	@Override
	public Sysdefinedict getByUuid(String uuid)  {
		Object entity = super.createQuery("from Sysdefinedict sd where sd.uuid=? and sd.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (Sysdefinedict)entity;
		}
		return null;
	}
	
	public List<Sysdefinedict> findByCompanyIdAndType(Long companyId,String type){
		@SuppressWarnings("unchecked")
		List<Sysdefinedict> list = super.createQuery("from Sysdefinedict sd where sd.companyId = ? and sd.type=? and sd.delFlag=?",Integer.valueOf(companyId.toString()),type,BaseEntity.DEL_FLAG_NORMAL).list();
		return list;
	}
}
