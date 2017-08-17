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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealRiseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupMealRiseInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupMealRiseQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealRiseService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupMealRiseServiceImpl  extends BaseService implements ActivityIslandGroupMealRiseService{
	@Autowired
	private ActivityIslandGroupMealRiseDao activityIslandGroupMealRiseDao;

	public void save (ActivityIslandGroupMealRise activityIslandGroupMealRise){
		super.setOptInfo(activityIslandGroupMealRise, BaseService.OPERATION_ADD);
		activityIslandGroupMealRiseDao.saveObj(activityIslandGroupMealRise);
	}
	
	public void save (ActivityIslandGroupMealRiseInput activityIslandGroupMealRiseInput){
		ActivityIslandGroupMealRise activityIslandGroupMealRise = activityIslandGroupMealRiseInput.getActivityIslandGroupMealRise();
		super.setOptInfo(activityIslandGroupMealRise, BaseService.OPERATION_ADD);
		activityIslandGroupMealRiseDao.saveObj(activityIslandGroupMealRise);
	}
	
	public void update (ActivityIslandGroupMealRise activityIslandGroupMealRise){
		super.setOptInfo(activityIslandGroupMealRise, BaseService.OPERATION_UPDATE);
		activityIslandGroupMealRiseDao.updateObj(activityIslandGroupMealRise);
	}
	
	public ActivityIslandGroupMealRise getById(java.lang.Integer value) {
		return activityIslandGroupMealRiseDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupMealRise obj = activityIslandGroupMealRiseDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupMealRise> find(Page<ActivityIslandGroupMealRise> page, ActivityIslandGroupMealRiseQuery activityIslandGroupMealRiseQuery) {
		DetachedCriteria dc = activityIslandGroupMealRiseDao.createDetachedCriteria();
		
	   	if(activityIslandGroupMealRiseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupMealRiseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupMealRiseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupMealRiseQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupMealRiseQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityIslandGroupMealRiseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandGroupMealUuid())){
			dc.add(Restrictions.eq("activityIslandGroupMealUuid", activityIslandGroupMealRiseQuery.getActivityIslandGroupMealUuid()));
		}
	   	if(activityIslandGroupMealRiseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupMealRiseQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupMealRiseQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupMealRiseQuery.getPrice()));
	   	}
	   	if(activityIslandGroupMealRiseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupMealRiseQuery.getCreateBy()));
	   	}
		if(activityIslandGroupMealRiseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupMealRiseQuery.getCreateDate()));
		}
	   	if(activityIslandGroupMealRiseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupMealRiseQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupMealRiseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupMealRiseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupMealRiseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupMealRiseDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupMealRise> find( ActivityIslandGroupMealRiseQuery activityIslandGroupMealRiseQuery) {
		DetachedCriteria dc = activityIslandGroupMealRiseDao.createDetachedCriteria();
		
	   	if(activityIslandGroupMealRiseQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupMealRiseQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupMealRiseQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupMealRiseQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupMealRiseQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityIslandGroupMealRiseQuery.getHotelMealUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getActivityIslandGroupMealUuid())){
			dc.add(Restrictions.eq("activityIslandGroupMealUuid", activityIslandGroupMealRiseQuery.getActivityIslandGroupMealUuid()));
		}
	   	if(activityIslandGroupMealRiseQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupMealRiseQuery.getCurrencyId()));
	   	}
	   	if(activityIslandGroupMealRiseQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", activityIslandGroupMealRiseQuery.getPrice()));
	   	}
	   	if(activityIslandGroupMealRiseQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupMealRiseQuery.getCreateBy()));
	   	}
		if(activityIslandGroupMealRiseQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupMealRiseQuery.getCreateDate()));
		}
	   	if(activityIslandGroupMealRiseQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupMealRiseQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupMealRiseQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupMealRiseQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealRiseQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupMealRiseQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupMealRiseDao.find(dc);
	}
	
	public ActivityIslandGroupMealRise getByUuid(String uuid) {
		return activityIslandGroupMealRiseDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupMealRise activityIslandGroupMealRise = getByUuid(uuid);
		activityIslandGroupMealRise.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupMealRise);
	}
	
	public List<ActivityIslandGroupMealRise> getbyGroupMealUuid(String mealuuid){
		return activityIslandGroupMealRiseDao.getbyGroupMealUuid(mealuuid);
	}

	@Override
	public List<ActivityIslandGroupMealRise> getMealRiseListByGroupUuid(
			String uuid) {
		
		return activityIslandGroupMealRiseDao.getMealRiseListByGroupUuid(uuid);
	}
}
