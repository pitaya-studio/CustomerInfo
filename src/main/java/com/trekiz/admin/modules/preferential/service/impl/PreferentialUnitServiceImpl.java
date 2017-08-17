/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.dao.PreferentialUnitDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialUnit;
import com.trekiz.admin.modules.preferential.service.PreferentialUnitService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialUnitServiceImpl  extends BaseService implements PreferentialUnitService{
	@Autowired
	private PreferentialUnitDao preferentialUnitDao;

	public void save (PreferentialUnit preferentialUnit){
		super.setOptInfo(preferentialUnit, BaseService.OPERATION_ADD);
		preferentialUnitDao.saveObj(preferentialUnit);
	}
	
	public void update (PreferentialUnit preferentialUnit){
		PreferentialUnit oldObj = preferentialUnitDao.getByUuid(preferentialUnit.getUuid());
		BeanUtil.copySimpleProperties(oldObj, preferentialUnit,true);
		super.setOptInfo(oldObj, BaseService.OPERATION_UPDATE);
		preferentialUnitDao.updateObj(oldObj);
	}
	
	public PreferentialUnit getById(java.lang.Integer value) {
		return preferentialUnitDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialUnit obj = preferentialUnitDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PreferentialUnit> find(Page<PreferentialUnit> page, PreferentialUnit preferentialUnit) {
		DetachedCriteria dc = preferentialUnitDao.createDetachedCriteria();
		
	   	if(preferentialUnit.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialUnit.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialUnit.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialUnit.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialUnit.getName())){
			dc.add(Restrictions.eq("name", preferentialUnit.getName()));
		}
	   	if(preferentialUnit.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", preferentialUnit.getSort()));
	   	}
		if (StringUtils.isNotEmpty(preferentialUnit.getDescription())){
			dc.add(Restrictions.eq("description", preferentialUnit.getDescription()));
		}
	   	if(preferentialUnit.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialUnit.getCreateBy()));
	   	}
		if(preferentialUnit.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialUnit.getCreateDate()));
		}
	   	if(preferentialUnit.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialUnit.getUpdateBy()));
	   	}
		if(preferentialUnit.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialUnit.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialUnit.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialUnit.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialUnitDao.find(page, dc);
	}
	
	public List<PreferentialUnit> find( PreferentialUnit preferentialUnit) {
		DetachedCriteria dc = preferentialUnitDao.createDetachedCriteria();
		
	   	if(preferentialUnit.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialUnit.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialUnit.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialUnit.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialUnit.getName())){
			dc.add(Restrictions.eq("name", preferentialUnit.getName()));
		}
	   	if(preferentialUnit.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", preferentialUnit.getSort()));
	   	}
		if (StringUtils.isNotEmpty(preferentialUnit.getDescription())){
			dc.add(Restrictions.eq("description", preferentialUnit.getDescription()));
		}
	   	if(preferentialUnit.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialUnit.getCreateBy()));
	   	}
		if(preferentialUnit.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialUnit.getCreateDate()));
		}
	   	if(preferentialUnit.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialUnit.getUpdateBy()));
	   	}
		if(preferentialUnit.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialUnit.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialUnit.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialUnit.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialUnitDao.find(dc);
	}
	
	public PreferentialUnit getByUuid(String uuid) {
		return preferentialUnitDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialUnit preferentialUnit = getByUuid(uuid);
		preferentialUnit.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialUnit);
	}
}
