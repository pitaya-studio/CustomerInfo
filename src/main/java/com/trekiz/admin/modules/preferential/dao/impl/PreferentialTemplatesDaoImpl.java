/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;

import java.util.*;

import com.trekiz.admin.modules.preferential.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PreferentialTemplatesDaoImpl extends BaseDaoImpl<PreferentialTemplates>  implements PreferentialTemplatesDao{
	@Override
	public PreferentialTemplates getByUuid(String uuid) {
		Object entity = super.createQuery("from PreferentialTemplates preferentialTemplates where preferentialTemplates.uuid=? and preferentialTemplates.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PreferentialTemplates)entity;
		}
		return null;
	}
	
	public List<PreferentialTemplates> getAllTemplates() {
		return super.find("from PreferentialTemplates preferentialTemplates where preferentialTemplates.delFlag=?", BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
