/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupRoomDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupRoomInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupRoomQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupRoomServiceImpl  extends BaseService implements ActivityIslandGroupRoomService{
	@Autowired
	private ActivityIslandGroupRoomDao activityIslandGroupRoomDao;
	@Autowired
	private ActivityIslandGroupMealService activityIslandGroupMealService;

	public void save (ActivityIslandGroupRoom activityIslandGroupRoom){
		super.setOptInfo(activityIslandGroupRoom, BaseService.OPERATION_ADD);
		activityIslandGroupRoomDao.saveObj(activityIslandGroupRoom);
	}
	
	public void save (ActivityIslandGroupRoomInput activityIslandGroupRoomInput){
		ActivityIslandGroupRoom activityIslandGroupRoom = activityIslandGroupRoomInput.getActivityIslandGroupRoom();
		super.setOptInfo(activityIslandGroupRoom, BaseService.OPERATION_ADD);
		activityIslandGroupRoomDao.saveObj(activityIslandGroupRoom);
	}
	
	public void update (ActivityIslandGroupRoom activityIslandGroupRoom){
		super.setOptInfo(activityIslandGroupRoom, BaseService.OPERATION_UPDATE);
		activityIslandGroupRoomDao.updateObj(activityIslandGroupRoom);
	}
	
	public ActivityIslandGroupRoom getById(java.lang.Integer value) {
		return activityIslandGroupRoomDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroupRoom obj = activityIslandGroupRoomDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroupRoom> find(Page<ActivityIslandGroupRoom> page, ActivityIslandGroupRoomQuery activityIslandGroupRoomQuery) {
		DetachedCriteria dc = activityIslandGroupRoomDao.createDetachedCriteria();
		
	   	if(activityIslandGroupRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupRoomQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupRoomQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", activityIslandGroupRoomQuery.getHotelRoomUuid()));
		}
	   	if(activityIslandGroupRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", activityIslandGroupRoomQuery.getNights()));
	   	}
	   	if(activityIslandGroupRoomQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupRoomQuery.getSort()));
	   	}
	   	if(activityIslandGroupRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupRoomQuery.getCreateBy()));
	   	}
	   	if(activityIslandGroupRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupRoomQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupRoomQuery.getDelFlag()));
		}
		if(activityIslandGroupRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupRoomQuery.getCreateDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupRoomDao.find(page, dc);
	}
	
	public List<ActivityIslandGroupRoom> find( ActivityIslandGroupRoomQuery activityIslandGroupRoomQuery) {
		DetachedCriteria dc = activityIslandGroupRoomDao.createDetachedCriteria();
		
	   	if(activityIslandGroupRoomQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupRoomQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupRoomQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupRoomQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", activityIslandGroupRoomQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getHotelRoomUuid())){
			dc.add(Restrictions.eq("hotelRoomUuid", activityIslandGroupRoomQuery.getHotelRoomUuid()));
		}
	   	if(activityIslandGroupRoomQuery.getNights()!=null){
	   		dc.add(Restrictions.eq("nights", activityIslandGroupRoomQuery.getNights()));
	   	}
	   	if(activityIslandGroupRoomQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", activityIslandGroupRoomQuery.getSort()));
	   	}
	   	if(activityIslandGroupRoomQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupRoomQuery.getCreateBy()));
	   	}
	   	if(activityIslandGroupRoomQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupRoomQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupRoomQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupRoomQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupRoomQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupRoomQuery.getDelFlag()));
		}
		if(activityIslandGroupRoomQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupRoomQuery.getCreateDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupRoomDao.find(dc);
	}
	
	public ActivityIslandGroupRoom getByUuid(String uuid) {
		return activityIslandGroupRoomDao.getByUuid(uuid);
	}
	
	public List<ActivityIslandGroupRoom> getByactivityIslandGroupUuid(String activityIslandGroupUuid){
		List<ActivityIslandGroupRoom> roomList = activityIslandGroupRoomDao.getRoomListByGroupUuid(activityIslandGroupUuid);
		if(CollectionUtils.isNotEmpty(roomList)) {
			for(ActivityIslandGroupRoom groupRoom : roomList) {
				int roomMealRiseRowspan = 0;
				List<ActivityIslandGroupMeal> mealList = activityIslandGroupMealService.getByactivityIslandGroupRoomUuid(groupRoom.getUuid());
				if(CollectionUtils.isNotEmpty(mealList)) {
					groupRoom.setActivityIslandGroupMealList(mealList);
					
					//统计房型下所有升餐的长度
					for(ActivityIslandGroupMeal groupMeal : mealList) {
						if(CollectionUtils.isNotEmpty(groupMeal.getActivityIslandGroupMealRiseList())) {
							roomMealRiseRowspan += groupMeal.getActivityIslandGroupMealRiseList().size();
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
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroupRoom activityIslandGroupRoom = getByUuid(uuid);
		activityIslandGroupRoom.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroupRoom);
	}
	
	public List<ActivityIslandGroupRoom> getRoomListByGroupUuid(String groupUuid) {
		return activityIslandGroupRoomDao.getRoomListByGroupUuid(groupUuid);
	}
	
	@Override
	public List<Map<String, Object>> getRoomListByactivityIslandGroupUuid(String activityIslandGroupUuid) {
		List<Map<String, Object>> roomList = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer("select ar.uuid,ar.hotel_room_uuid,ar.nights,r.room_name,r.occupancy_rate from activity_island_group_room ar,hotel_room r where "
				+ "ar.hotel_room_uuid=r.uuid and ar.activity_island_group_uuid='" + activityIslandGroupUuid + "'"
				);
		roomList = activityIslandGroupRoomDao.findBySql(sql.toString(), Map.class);
		return roomList;
	}
}
