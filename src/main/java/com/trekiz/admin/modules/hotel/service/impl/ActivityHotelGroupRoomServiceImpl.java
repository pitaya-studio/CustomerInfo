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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupRoomDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupRoomInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupRoomQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupRoomServiceImpl  extends BaseService implements ActivityHotelGroupRoomService{
	@Autowired
	private ActivityHotelGroupRoomDao activityHotelGroupRoomDao;
	@Autowired
	private ActivityHotelGroupMealService activityHotelGroupMealService;

	public void save (ActivityHotelGroupRoom activityHotelGroupRoom){
		super.setOptInfo(activityHotelGroupRoom, BaseService.OPERATION_ADD);
		activityHotelGroupRoomDao.saveObj(activityHotelGroupRoom);
	}
	
	public void save (ActivityHotelGroupRoomInput activityHotelGroupRoomInput){
		ActivityHotelGroupRoom activityHotelGroupRoom = activityHotelGroupRoomInput.getActivityHotelGroupRoom();
		super.setOptInfo(activityHotelGroupRoom, BaseService.OPERATION_ADD);
		activityHotelGroupRoomDao.saveObj(activityHotelGroupRoom);
	}
	
	public void update (ActivityHotelGroupRoom activityHotelGroupRoom){
		super.setOptInfo(activityHotelGroupRoom, BaseService.OPERATION_UPDATE);
		activityHotelGroupRoomDao.updateObj(activityHotelGroupRoom);
	}
	
	public ActivityHotelGroupRoom getById(java.lang.Integer value) {
		return activityHotelGroupRoomDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupRoom obj = activityHotelGroupRoomDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupRoom> find(Page<ActivityHotelGroupRoom> page, ActivityHotelGroupRoomQuery activityHotelGroupRoomQuery) {
		DetachedCriteria dc = activityHotelGroupRoomDao.createDetachedCriteria();
		
	   	if(activityHotelGroupRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupRoomQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupRoomQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", activityHotelGroupRoomQuery.getHotelRoomUuid()));
		}
	   	if(activityHotelGroupRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", activityHotelGroupRoomQuery.getNights()));
	   	}
	   	if(activityHotelGroupRoomQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityHotelGroupRoomQuery.getSort()));
	   	}
	   	if(activityHotelGroupRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupRoomQuery.getCreateBy()));
	   	}
		if(activityHotelGroupRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupRoomQuery.getCreateDate()));
		}
	   	if(activityHotelGroupRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupRoomQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupRoomDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupRoom> find( ActivityHotelGroupRoomQuery activityHotelGroupRoomQuery) {
		DetachedCriteria dc = activityHotelGroupRoomDao.createDetachedCriteria();
		
	   	if(activityHotelGroupRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupRoomQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupRoomQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", activityHotelGroupRoomQuery.getHotelRoomUuid()));
		}
	   	if(activityHotelGroupRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", activityHotelGroupRoomQuery.getNights()));
	   	}
	   	if(activityHotelGroupRoomQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityHotelGroupRoomQuery.getSort()));
	   	}
	   	if(activityHotelGroupRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupRoomQuery.getCreateBy()));
	   	}
		if(activityHotelGroupRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupRoomQuery.getCreateDate()));
		}
	   	if(activityHotelGroupRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupRoomQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupRoomQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupRoomDao.find(dc);
	}
	
	public ActivityHotelGroupRoom getByUuid(String uuid) {
		return activityHotelGroupRoomDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupRoom activityHotelGroupRoom = getByUuid(uuid);
		activityHotelGroupRoom.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupRoom);
	}

	@Override
	public List<ActivityHotelGroupRoom> getRoomListByGroupUuid(String uuid) {
		List<ActivityHotelGroupRoom> roomList = activityHotelGroupRoomDao.getRoomListByGroupUuid(uuid);
		if(CollectionUtils.isNotEmpty(roomList)) {
			for(ActivityHotelGroupRoom groupRoom : roomList) {
				int roomMealRiseRowspan = 0;
				List<ActivityHotelGroupMeal> mealList = activityHotelGroupMealService.getMealListByRoomUuid(groupRoom.getUuid());
				if(CollectionUtils.isNotEmpty(mealList)) {
					groupRoom.setActivityHotelGroupMealList(mealList);
					
					//统计房型下所有升餐的长度
					for(ActivityHotelGroupMeal groupMeal : mealList) {
						if(CollectionUtils.isNotEmpty(groupMeal.getActivityHotelGroupMealsRiseList())) {
							roomMealRiseRowspan += groupMeal.getActivityHotelGroupMealsRiseList().size();
						} else {
							roomMealRiseRowspan += 1;
						}
					}
				}
				groupRoom.setRoomMealRiseRowspan(roomMealRiseRowspan);
			}
		}
		return roomList;
	}
	
	public List<ActivityHotelGroupRoom> getByactivityHotelGroupUuid(String activityHotelGroupUuid){
		List<ActivityHotelGroupRoom> rooms = activityHotelGroupRoomDao.getRoomListByGroupUuid(activityHotelGroupUuid);
		if(CollectionUtils.isNotEmpty(rooms)) {
			for(ActivityHotelGroupRoom room : rooms) {
				room.setActivityHotelGroupMealList(activityHotelGroupMealService.getByactivityHotelGroupRoomUuid(room.getUuid()));
			}
		}
		return rooms;
	}
	
}
