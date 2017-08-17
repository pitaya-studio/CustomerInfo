/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockGroupRelDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockGroupRelInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockGroupRelQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockGroupRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipStockGroupRelServiceImpl  extends BaseService implements CruiseshipStockGroupRelService{
	@Autowired
	private CruiseshipStockGroupRelDao cruiseshipStockGroupRelDao;

	public void save (CruiseshipStockGroupRel cruiseshipStockGroupRel){
		super.setOptInfo(cruiseshipStockGroupRel, BaseService.OPERATION_ADD);
		cruiseshipStockGroupRelDao.saveObj(cruiseshipStockGroupRel);
	}
	
	public void save (CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput){
		CruiseshipStockGroupRel cruiseshipStockGroupRel = cruiseshipStockGroupRelInput.getCruiseshipStockGroupRel();
		super.setOptInfo(cruiseshipStockGroupRel, BaseService.OPERATION_ADD);
		cruiseshipStockGroupRelDao.saveObj(cruiseshipStockGroupRel);
	}
	
	public void update (CruiseshipStockGroupRel cruiseshipStockGroupRel){
		super.setOptInfo(cruiseshipStockGroupRel, BaseService.OPERATION_UPDATE);
		cruiseshipStockGroupRelDao.updateObj(cruiseshipStockGroupRel);
	}
	
	public CruiseshipStockGroupRel getById(java.lang.Integer value) {
		return cruiseshipStockGroupRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipStockGroupRel obj = cruiseshipStockGroupRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipStockGroupRel> find(Page<CruiseshipStockGroupRel> page, CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery) {
		DetachedCriteria dc = cruiseshipStockGroupRelDao.createDetachedCriteria();
		
	   	if(cruiseshipStockGroupRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockGroupRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockGroupRelQuery.getUuid()));
		}
	   	if(cruiseshipStockGroupRelQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", cruiseshipStockGroupRelQuery.getActivityId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getActivitygroupId()!=null){
	   		dc.add(Restrictions.eq("activitygroupId", cruiseshipStockGroupRelQuery.getActivitygroupId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getActivityType()!=null){
	   		dc.add(Restrictions.eq("activityType", cruiseshipStockGroupRelQuery.getActivityType()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockGroupRelQuery.getCruiseshipStockUuid()));
		}
	   	if(cruiseshipStockGroupRelQuery.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", cruiseshipStockGroupRelQuery.getStatus()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockGroupRelQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockGroupRelQuery.getCreateBy()));
	   	}
		if(cruiseshipStockGroupRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockGroupRelQuery.getCreateDate()));
		}
	   	if(cruiseshipStockGroupRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockGroupRelQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockGroupRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockGroupRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockGroupRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockGroupRelDao.find(page, dc);
	}
	
	public List<CruiseshipStockGroupRel> find( CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery) {
		DetachedCriteria dc = cruiseshipStockGroupRelDao.createDetachedCriteria();
		
	   	if(cruiseshipStockGroupRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockGroupRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockGroupRelQuery.getUuid()));
		}
	   	if(cruiseshipStockGroupRelQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", cruiseshipStockGroupRelQuery.getActivityId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getActivitygroupId()!=null){
	   		dc.add(Restrictions.eq("activitygroupId", cruiseshipStockGroupRelQuery.getActivitygroupId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getActivityType()!=null){
	   		dc.add(Restrictions.eq("activityType", cruiseshipStockGroupRelQuery.getActivityType()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockGroupRelQuery.getCruiseshipStockUuid()));
		}
	   	if(cruiseshipStockGroupRelQuery.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", cruiseshipStockGroupRelQuery.getStatus()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockGroupRelQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockGroupRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockGroupRelQuery.getCreateBy()));
	   	}
		if(cruiseshipStockGroupRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockGroupRelQuery.getCreateDate()));
		}
	   	if(cruiseshipStockGroupRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockGroupRelQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockGroupRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockGroupRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockGroupRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockGroupRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockGroupRelDao.find(dc);
	}
	
	public CruiseshipStockGroupRel getByUuid(String uuid) {
		return cruiseshipStockGroupRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipStockGroupRel cruiseshipStockGroupRel = getByUuid(uuid);
		cruiseshipStockGroupRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipStockGroupRel);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipStockGroupRelDao.batchDelete(uuids);
	}
    /**
     * 223:tgy
     * 根据团期id查询表cruiseship_group_rel和activitygroup表,获得关联状态,cruiseship_stock_detail表的id,关联日期和操作人
     */
	@Override
	public List<Map<String, Object>> getRelInfo(String agId) {
		return cruiseshipStockGroupRelDao.getRelInfo(agId);
	}
	
}
