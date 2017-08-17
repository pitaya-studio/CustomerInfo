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
import com.trekiz.admin.modules.island.dao.IslandTravelerPapersTypeDao;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.input.IslandTravelerPapersTypeInput;
import com.trekiz.admin.modules.island.query.IslandTravelerPapersTypeQuery;
import com.trekiz.admin.modules.island.service.IslandTravelerPapersTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandTravelerPapersTypeServiceImpl  extends BaseService implements IslandTravelerPapersTypeService{
	@Autowired
	private IslandTravelerPapersTypeDao islandTravelerPapersTypeDao;

	public void save (IslandTravelerPapersType islandTravelerPapersType){
		super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_ADD);
		islandTravelerPapersTypeDao.saveObj(islandTravelerPapersType);
	}
	
	public void save (IslandTravelerPapersTypeInput islandTravelerPapersTypeInput){
		IslandTravelerPapersType islandTravelerPapersType = islandTravelerPapersTypeInput.getIslandTravelerPapersType();
		super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_ADD);
		islandTravelerPapersTypeDao.saveObj(islandTravelerPapersType);
	}
	
	public void update (IslandTravelerPapersType islandTravelerPapersType){
		super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_UPDATE);
		islandTravelerPapersTypeDao.updateObj(islandTravelerPapersType);
	}
	
	public IslandTravelerPapersType getById(java.lang.Integer value) {
		return islandTravelerPapersTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		IslandTravelerPapersType obj = islandTravelerPapersTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandTravelerPapersType> find(Page<IslandTravelerPapersType> page, IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery) {
		DetachedCriteria dc = islandTravelerPapersTypeDao.createDetachedCriteria();
		
	   	if(islandTravelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelerPapersTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIslandTravelerUuid())){
			dc.add(Restrictions.eq("islandTravelerUuid", islandTravelerPapersTypeQuery.getIslandTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandTravelerPapersTypeQuery.getOrderUuid()));
		}
	   	if(islandTravelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", islandTravelerPapersTypeQuery.getPapersType()));
	   	}
		if(islandTravelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", islandTravelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", islandTravelerPapersTypeQuery.getIdCard()));
		}
		if(islandTravelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", islandTravelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", islandTravelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(islandTravelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelerPapersTypeQuery.getCreateBy()));
	   	}
		if(islandTravelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelerPapersTypeQuery.getCreateDate()));
		}
	   	if(islandTravelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(islandTravelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandTravelerPapersTypeDao.find(page, dc);
	}
	
	public List<IslandTravelerPapersType> find( IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery) {
		DetachedCriteria dc = islandTravelerPapersTypeDao.createDetachedCriteria();
		
	   	if(islandTravelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelerPapersTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIslandTravelerUuid())){
			dc.add(Restrictions.eq("islandTravelerUuid", islandTravelerPapersTypeQuery.getIslandTravelerUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandTravelerPapersTypeQuery.getOrderUuid()));
		}
	   	if(islandTravelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", islandTravelerPapersTypeQuery.getPapersType()));
	   	}
		if(islandTravelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", islandTravelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", islandTravelerPapersTypeQuery.getIdCard()));
		}
		if(islandTravelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", islandTravelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", islandTravelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(islandTravelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelerPapersTypeQuery.getCreateBy()));
	   	}
		if(islandTravelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelerPapersTypeQuery.getCreateDate()));
		}
	   	if(islandTravelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(islandTravelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandTravelerPapersTypeDao.find(dc);
	}
	
	public IslandTravelerPapersType getByUuid(String uuid) {
		return islandTravelerPapersTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandTravelerPapersType islandTravelerPapersType = getByUuid(uuid);
		islandTravelerPapersType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandTravelerPapersType);
	}
}
