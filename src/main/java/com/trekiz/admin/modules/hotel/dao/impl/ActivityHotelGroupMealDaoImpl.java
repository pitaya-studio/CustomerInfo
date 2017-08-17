/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupMealDaoImpl extends BaseDaoImpl<ActivityHotelGroupMeal>  implements ActivityHotelGroupMealDao{
	@Override
	public ActivityHotelGroupMeal getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupMeal activityHotelGroupMeal where activityHotelGroupMeal.uuid=? and activityHotelGroupMeal.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupMeal)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupMeal> getMealListByGroupUuid(String uuid) {
		Object obj=super.createQuery("from ActivityHotelGroupMeal activityHotelGroupMeal where activityHotelGroupMeal.activityHotelGroupUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		if(obj!=null){
			return (List<ActivityHotelGroupMeal>)obj;
		}
		return null;
	}
	
	public List<ActivityHotelGroupMeal> getMealListByRoomUuid(String roomUuid) {
		return super.find("from ActivityHotelGroupMeal activityHotelGroupMeal where activityHotelGroupMeal.activityHotelGroupRoomUuid=? and delFlag=?", roomUuid,BaseEntity.DEL_FLAG_NORMAL);
	}

	public List<ActivityHotelGroupMeal> getByactivityHotelGroupRoomUuid(String roomUuid) {
		return super.find("from ActivityHotelGroupMeal activityHotelGroupMeal where activityHotelGroupMeal.activityHotelGroupRoomUuid=? and activityHotelGroupMeal.delFlag=?", roomUuid, BaseEntity.DEL_FLAG_NORMAL);
	}

}
