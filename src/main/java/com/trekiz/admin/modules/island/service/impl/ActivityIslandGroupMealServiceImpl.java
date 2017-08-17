/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealRiseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupMealInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupMealQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupMealServiceImpl  extends BaseService implements ActivityIslandGroupMealService{
	@Autowired
	private ActivityIslandGroupMealDao activityIslandGroupMealDao;
	@Autowired
	private ActivityIslandGroupMealRiseDao activityIslandGroupMealRiseDao;

	public void save (ActivityIslandGroupMeal activityIslandGroupMeal){
		super.setOptInfo(activityIslandGroupMeal, BaseService.OPERATION_ADD);
		activityIslandGroupMealDao.saveObj(activityIslandGroupMeal);
	}
	
	public void save (ActivityIslandGroupMealInput activityIslandGroupMealInput){
		ActivityIslandGroupMeal activityIslandGroupMeal = activityIslandGroupMealInput.getActivityIslandGroupMeal();
		super.setOptInfo(activityIslandGroupMeal, BaseService.OPERATION_ADD);
		activityIslandGroupMealDao.saveObj(activityIslandGroupMeal);
	}
	
	public void update (ActivityIslandGroupMeal activityIslandGroupMeal){
		super.setOptInfo(activityIslandGroupMeal, BaseService.OPERATION_UPDATE);
		activityIslandGroupMealDao.updateObj(activityIslandGroupMeal);
	}
	
	public ActivityIslandGroupMeal getById(java.lang.Integer value) {
		return activityIslandGroupMealDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupMeal obj = activityIslandGroupMealDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupMeal> find(Page<ActivityIslandGroupMeal> page, ActivityIslandGroupMealQuery activityIslandGroupMealQuery) {
		DetachedCriteria dc = activityIslandGroupMealDao.createDetachedCriteria();
		
	   	if(activityIslandGroupMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupMealQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupMealQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandGroupRoomUuid())){
			dc.add(Restrictions.eq("activityIslandGroupRoomUuid", activityIslandGroupMealQuery.getActivityIslandGroupRoomUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getHotelRoomMealUuid())){
			dc.add(Restrictions.eq("hotelRoomMealUuid", activityIslandGroupMealQuery.getHotelRoomMealUuid()));
		}
	   	if(activityIslandGroupMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupMealQuery.getCreateBy()));
	   	}
		if(activityIslandGroupMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupMealQuery.getCreateDate()));
		}
	   	if(activityIslandGroupMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupMealQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupMealDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupMeal> find( ActivityIslandGroupMealQuery activityIslandGroupMealQuery) {
		DetachedCriteria dc = activityIslandGroupMealDao.createDetachedCriteria();
		
	   	if(activityIslandGroupMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupMealQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupMealQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getActivityIslandGroupRoomUuid())){
			dc.add(Restrictions.eq("activityIslandGroupRoomUuid", activityIslandGroupMealQuery.getActivityIslandGroupRoomUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getHotelRoomMealUuid())){
			dc.add(Restrictions.eq("hotelRoomMealUuid", activityIslandGroupMealQuery.getHotelRoomMealUuid()));
		}
	   	if(activityIslandGroupMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupMealQuery.getCreateBy()));
	   	}
		if(activityIslandGroupMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupMealQuery.getCreateDate()));
		}
	   	if(activityIslandGroupMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupMealQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupMealDao.find(dc);
	}
	
	public ActivityIslandGroupMeal getByUuid(String uuid) {
		return activityIslandGroupMealDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupMeal activityIslandGroupMeal = getByUuid(uuid);
		activityIslandGroupMeal.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupMeal);
	}
	public List<ActivityIslandGroupMeal> getByactivityIslandGroupUuid(String activityIslandGroupUuid){
		List<ActivityIslandGroupMeal> groupMeals = activityIslandGroupMealDao.find("from ActivityIslandGroupMeal where activityIslandGroupRoomUuid=? and delFlag=?", activityIslandGroupUuid,BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityIslandGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityIslandGroupMealRiseList(activityIslandGroupMealRiseDao.getbyGroupMealUuid(groupMeal.getUuid()));
			}
		}
		
		return groupMeals;
	}
	
	public List<ActivityIslandGroupMeal> getByactivityIslandGroupRoomUuid(String roomUuid) {
		List<ActivityIslandGroupMeal> groupMeals = activityIslandGroupMealDao.getByactivityIslandGroupRoomUuid(roomUuid);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityIslandGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityIslandGroupMealRiseList(activityIslandGroupMealRiseDao.getbyGroupMealUuid(groupMeal.getUuid()));
			}
		}
		return groupMeals;
	}
	
	@Override
	public List<ActivityIslandGroupMeal> getMealListByGroupUuid(String uuid) {
		List<ActivityIslandGroupMeal> groupMeals = activityIslandGroupMealDao.getMealListByGroupUuid(uuid);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityIslandGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityIslandGroupMealRiseList(activityIslandGroupMealRiseDao.getMealRiseByMealUuid(groupMeal.getUuid()));
			}
		}
		return groupMeals;
	}
}
