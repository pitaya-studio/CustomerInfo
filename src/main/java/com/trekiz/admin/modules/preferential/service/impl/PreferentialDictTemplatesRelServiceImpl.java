/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.entity.*;
import com.trekiz.admin.modules.preferential.dao.*;
import com.trekiz.admin.modules.preferential.service.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialDictTemplatesRelServiceImpl  extends BaseService implements PreferentialDictTemplatesRelService{
	@Autowired
	private PreferentialDictTemplatesRelDao preferentialDictTemplatesRelDao;

	public void save (PreferentialDictTemplatesRel preferentialDictTemplatesRel){
		super.setOptInfo(preferentialDictTemplatesRel, BaseService.OPERATION_ADD);
		preferentialDictTemplatesRelDao.saveObj(preferentialDictTemplatesRel);
	}
	
	public void update (PreferentialDictTemplatesRel preferentialDictTemplatesRel){
		super.setOptInfo(preferentialDictTemplatesRel, BaseService.OPERATION_UPDATE);
		preferentialDictTemplatesRelDao.updateObj(preferentialDictTemplatesRel);
	}
	
	public PreferentialDictTemplatesRel getById(java.lang.Integer value) {
		return preferentialDictTemplatesRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialDictTemplatesRel obj = preferentialDictTemplatesRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PreferentialDictTemplatesRel> find(Page<PreferentialDictTemplatesRel> page, PreferentialDictTemplatesRel preferentialDictTemplatesRel) {
		DetachedCriteria dc = preferentialDictTemplatesRelDao.createDetachedCriteria();
		
	   	if(preferentialDictTemplatesRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDictTemplatesRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDictTemplatesRel.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getTemplatesUuid())){
			dc.add(Restrictions.eq("templatesUuid", preferentialDictTemplatesRel.getTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getUnitUuid())){
			dc.add(Restrictions.eq("unitUuid", preferentialDictTemplatesRel.getUnitUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getDictUuid())){
			dc.add(Restrictions.eq("dictUuid", preferentialDictTemplatesRel.getDictUuid()));
		}
	   	if(preferentialDictTemplatesRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDictTemplatesRel.getCreateBy()));
	   	}
		if(preferentialDictTemplatesRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDictTemplatesRel.getCreateDate()));
		}
	   	if(preferentialDictTemplatesRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDictTemplatesRel.getUpdateBy()));
	   	}
		if(preferentialDictTemplatesRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDictTemplatesRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDictTemplatesRel.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictTemplatesRelDao.find(page, dc);
	}
	
	public List<PreferentialDictTemplatesRel> find( PreferentialDictTemplatesRel preferentialDictTemplatesRel) {
		DetachedCriteria dc = preferentialDictTemplatesRelDao.createDetachedCriteria();
		
	   	if(preferentialDictTemplatesRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDictTemplatesRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDictTemplatesRel.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getTemplatesUuid())){
			dc.add(Restrictions.eq("templatesUuid", preferentialDictTemplatesRel.getTemplatesUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getUnitUuid())){
			dc.add(Restrictions.eq("unitUuid", preferentialDictTemplatesRel.getUnitUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getDictUuid())){
			dc.add(Restrictions.eq("dictUuid", preferentialDictTemplatesRel.getDictUuid()));
		}
	   	if(preferentialDictTemplatesRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDictTemplatesRel.getCreateBy()));
	   	}
		if(preferentialDictTemplatesRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDictTemplatesRel.getCreateDate()));
		}
	   	if(preferentialDictTemplatesRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDictTemplatesRel.getUpdateBy()));
	   	}
		if(preferentialDictTemplatesRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDictTemplatesRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDictTemplatesRel.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDictTemplatesRel.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictTemplatesRelDao.find(dc);
	}
	
	public PreferentialDictTemplatesRel getByUuid(String uuid) {
		return preferentialDictTemplatesRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialDictTemplatesRel preferentialDictTemplatesRel = getByUuid(uuid);
		preferentialDictTemplatesRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialDictTemplatesRel);
	}
	
	public List<Map<String, String>> findDictUuidAndDictNameByType(Integer type) {
		return preferentialDictTemplatesRelDao.findDictUuidAndDictNameByType(type);
	}
}
