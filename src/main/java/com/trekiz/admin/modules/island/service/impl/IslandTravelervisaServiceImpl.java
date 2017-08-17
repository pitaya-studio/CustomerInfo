/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

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
import com.trekiz.admin.modules.island.dao.IslandTravelervisaDao;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;
import com.trekiz.admin.modules.island.input.IslandTravelervisaInput;
import com.trekiz.admin.modules.island.query.IslandTravelervisaQuery;
import com.trekiz.admin.modules.island.service.IslandTravelervisaService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandTravelervisaServiceImpl  extends BaseService implements IslandTravelervisaService{
	@Autowired
	private IslandTravelervisaDao islandTravelervisaDao;

	public void save (IslandTravelervisa islandTravelervisa){
		super.setOptInfo(islandTravelervisa, BaseService.OPERATION_ADD);
		islandTravelervisaDao.saveObj(islandTravelervisa);
	}
	
	public void save (IslandTravelervisaInput islandTravelervisaInput){
		IslandTravelervisa islandTravelervisa = islandTravelervisaInput.getIslandTravelervisa();
		super.setOptInfo(islandTravelervisa, BaseService.OPERATION_ADD);
		islandTravelervisaDao.saveObj(islandTravelervisa);
	}
	
	public void update (IslandTravelervisa islandTravelervisa){
		super.setOptInfo(islandTravelervisa, BaseService.OPERATION_UPDATE);
		islandTravelervisaDao.updateObj(islandTravelervisa);
	}
	
	public IslandTravelervisa getById(java.lang.Long value) {
		return islandTravelervisaDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		IslandTravelervisa obj = islandTravelervisaDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandTravelervisa> find(Page<IslandTravelervisa> page, IslandTravelervisaQuery islandTravelervisaQuery) {
		DetachedCriteria dc = islandTravelervisaDao.createDetachedCriteria();
		
	   	if(islandTravelervisaQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelervisaQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelervisaQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getIslandOrderUuid())){
			dc.add(Restrictions.eq("islandOrderUuid", islandTravelervisaQuery.getIslandOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getIslandTravelerUuid())){
			dc.add(Restrictions.eq("islandTravelerUuid", islandTravelervisaQuery.getIslandTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getCountry())){
			dc.add(Restrictions.eq("country", islandTravelervisaQuery.getCountry()));
		}
	   	if(islandTravelervisaQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", islandTravelervisaQuery.getVisaTypeId()));
	   	}
	   	if(islandTravelervisaQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelervisaQuery.getCreateBy()));
	   	}
		if(islandTravelervisaQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelervisaQuery.getCreateDate()));
		}
	   	if(islandTravelervisaQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelervisaQuery.getUpdateBy()));
	   	}
		if(islandTravelervisaQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelervisaQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelervisaQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandTravelervisaDao.find(page, dc);
	}
	
	public List<IslandTravelervisa> find( IslandTravelervisaQuery islandTravelervisaQuery) {
		DetachedCriteria dc = islandTravelervisaDao.createDetachedCriteria();
		
	   	if(islandTravelervisaQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelervisaQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelervisaQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getIslandOrderUuid())){
			dc.add(Restrictions.eq("islandOrderUuid", islandTravelervisaQuery.getIslandOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getIslandTravelerUuid())){
			dc.add(Restrictions.eq("islandTravelerUuid", islandTravelervisaQuery.getIslandTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getCountry())){
			dc.add(Restrictions.eq("country", islandTravelervisaQuery.getCountry()));
		}
	   	if(islandTravelervisaQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", islandTravelervisaQuery.getVisaTypeId()));
	   	}
	   	if(islandTravelervisaQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelervisaQuery.getCreateBy()));
	   	}
		if(islandTravelervisaQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelervisaQuery.getCreateDate()));
		}
	   	if(islandTravelervisaQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelervisaQuery.getUpdateBy()));
	   	}
		if(islandTravelervisaQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelervisaQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelervisaQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelervisaQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandTravelervisaDao.find(dc);
	}
	
	public IslandTravelervisa getByUuid(String uuid) {
		return islandTravelervisaDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandTravelervisa islandTravelervisa = getByUuid(uuid);
		islandTravelervisa.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandTravelervisa);
	}
}
