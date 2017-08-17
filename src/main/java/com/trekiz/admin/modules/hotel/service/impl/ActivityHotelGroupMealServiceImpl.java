/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealRiseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupMealInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupMealQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupMealServiceImpl  extends BaseService implements ActivityHotelGroupMealService{
	@Autowired
	private ActivityHotelGroupMealDao activityHotelGroupMealDao;
	@Autowired
	private ActivityHotelGroupMealRiseDao activityHotelGroupMealRiseDao;

	public void save (ActivityHotelGroupMeal activityHotelGroupMeal){
		super.setOptInfo(activityHotelGroupMeal, BaseService.OPERATION_ADD);
		activityHotelGroupMealDao.saveObj(activityHotelGroupMeal);
	}
	
	public void save (ActivityHotelGroupMealInput activityHotelGroupMealInput){
		ActivityHotelGroupMeal activityHotelGroupMeal = activityHotelGroupMealInput.getActivityHotelGroupMeal();
		super.setOptInfo(activityHotelGroupMeal, BaseService.OPERATION_ADD);
		activityHotelGroupMealDao.saveObj(activityHotelGroupMeal);
	}
	
	public void update (ActivityHotelGroupMeal activityHotelGroupMeal){
		super.setOptInfo(activityHotelGroupMeal, BaseService.OPERATION_UPDATE);
		activityHotelGroupMealDao.updateObj(activityHotelGroupMeal);
	}
	
	public ActivityHotelGroupMeal getById(java.lang.Integer value) {
		return activityHotelGroupMealDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupMeal obj = activityHotelGroupMealDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupMeal> find(Page<ActivityHotelGroupMeal> page, ActivityHotelGroupMealQuery activityHotelGroupMealQuery) {
		DetachedCriteria dc = activityHotelGroupMealDao.createDetachedCriteria();
		
	   	if(activityHotelGroupMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupMealQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupMealQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityHotelGroupMealQuery.getHotelMealUuid()));
		}
	   	if(activityHotelGroupMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupMealQuery.getCreateBy()));
	   	}
		if(activityHotelGroupMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupMealQuery.getCreateDate()));
		}
	   	if(activityHotelGroupMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupMealQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupMealDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupMeal> find( ActivityHotelGroupMealQuery activityHotelGroupMealQuery) {
		DetachedCriteria dc = activityHotelGroupMealDao.createDetachedCriteria();
		
	   	if(activityHotelGroupMealQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupMealQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupMealQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupMealQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupMealQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getHotelMealUuid())){
			dc.add(Restrictions.eq("hotelMealUuid", activityHotelGroupMealQuery.getHotelMealUuid()));
		}
	   	if(activityHotelGroupMealQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupMealQuery.getCreateBy()));
	   	}
		if(activityHotelGroupMealQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupMealQuery.getCreateDate()));
		}
	   	if(activityHotelGroupMealQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupMealQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupMealQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupMealQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupMealQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupMealQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupMealDao.find(dc);
	}
	
	public ActivityHotelGroupMeal getByUuid(String uuid) {
		return activityHotelGroupMealDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupMeal activityHotelGroupMeal = getByUuid(uuid);
		activityHotelGroupMeal.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupMeal);
	}

	@Override
	public List<ActivityHotelGroupMeal> getMealListByGroupUuid(String uuid) {
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealDao.getMealListByGroupUuid(uuid);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityHotelGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityHotelGroupMealsRiseList(activityHotelGroupMealRiseDao.getMealRiseByMealUuid(groupMeal.getUuid()));
			}
		}
		
		return groupMeals;
	}
	
	public List<ActivityHotelGroupMeal> getMealListByRoomUuid(String roomUuid) {
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealDao.getMealListByRoomUuid(roomUuid);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityHotelGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityHotelGroupMealsRiseList(activityHotelGroupMealRiseDao.getMealRiseByMealUuid(groupMeal.getUuid()));
			}
		}
		return groupMeals;
	}
	
	public List<ActivityHotelGroupMeal> getByactivityHotelGroupRoomUuid(String roomUuid) {
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealDao.getByactivityHotelGroupRoomUuid(roomUuid);
		if(CollectionUtils.isNotEmpty(groupMeals)) {
			for(ActivityHotelGroupMeal groupMeal : groupMeals) {
				groupMeal.setActivityHotelGroupMealsRiseList(activityHotelGroupMealRiseDao.getbyGroupMealUuid(groupMeal.getUuid()));
			}			  
		}
		return groupMeals;
	}
	
}
