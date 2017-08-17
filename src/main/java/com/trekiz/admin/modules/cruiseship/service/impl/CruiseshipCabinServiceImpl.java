/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipCabinDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipCabinInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipCabinQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipCabinService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipCabinServiceImpl  extends BaseService implements CruiseshipCabinService{
	@Autowired
	private CruiseshipCabinDao cruiseshipCabinDao;

	public void save (CruiseshipCabin cruiseshipCabin){
		super.setOptInfo(cruiseshipCabin, BaseService.OPERATION_ADD);
		cruiseshipCabinDao.saveObj(cruiseshipCabin);
	}
	
	public void save (CruiseshipCabinInput cruiseshipCabinInput) {
			CruiseshipCabin cruiseshipCabin = cruiseshipCabinInput.getCruiseshipCabin();
			super.setOptInfo(cruiseshipCabin, BaseService.OPERATION_ADD);
			cruiseshipCabinDao.saveObj(cruiseshipCabin);
	}
	
	public void update (CruiseshipCabin cruiseshipCabin){
		super.setOptInfo(cruiseshipCabin, BaseService.OPERATION_UPDATE);
		cruiseshipCabinDao.updateObj(cruiseshipCabin);
	}
	
	public CruiseshipCabin getById(java.lang.Integer value) {
		return cruiseshipCabinDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipCabin obj = cruiseshipCabinDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipCabin> find(Page<CruiseshipCabin> page, CruiseshipCabinQuery cruiseshipCabinQuery) {
		DetachedCriteria dc = cruiseshipCabinDao.createDetachedCriteria();
		
	   	if(cruiseshipCabinQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipCabinQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipCabinQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipCabinQuery.getCruiseshipInfoUuid()));
		}
	   	if(cruiseshipCabinQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipCabinQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getName())){
			dc.add(Restrictions.eq("name", cruiseshipCabinQuery.getName()));
		}
	   	if(cruiseshipCabinQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipCabinQuery.getCreateBy()));
	   	}
		if(cruiseshipCabinQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipCabinQuery.getCreateDate()));
		}
	   	if(cruiseshipCabinQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipCabinQuery.getUpdateBy()));
	   	}
		if(cruiseshipCabinQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipCabinQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipCabinQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipCabinDao.find(page, dc);
	}
	
	public List<CruiseshipCabin> find( CruiseshipCabinQuery cruiseshipCabinQuery) {
		DetachedCriteria dc = cruiseshipCabinDao.createDetachedCriteria();
		
	   	if(cruiseshipCabinQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipCabinQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipCabinQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipCabinQuery.getCruiseshipInfoUuid()));
		}
	   	if(cruiseshipCabinQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipCabinQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getName())){
			dc.add(Restrictions.eq("name", cruiseshipCabinQuery.getName()));
		}
	   	if(cruiseshipCabinQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipCabinQuery.getCreateBy()));
	   	}
		if(cruiseshipCabinQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipCabinQuery.getCreateDate()));
		}
	   	if(cruiseshipCabinQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipCabinQuery.getUpdateBy()));
	   	}
		if(cruiseshipCabinQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipCabinQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipCabinQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipCabinQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipCabinDao.find(dc);
	}
	
	public CruiseshipCabin getByUuid(String uuid) {
		return cruiseshipCabinDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipCabin cruiseshipCabin = getByUuid(uuid);
		cruiseshipCabin.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipCabin);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipCabinDao.batchDelete(uuids);
	}
	
}
