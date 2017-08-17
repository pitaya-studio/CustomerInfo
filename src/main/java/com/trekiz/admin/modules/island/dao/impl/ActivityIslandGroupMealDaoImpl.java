/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupMealDaoImpl extends BaseDaoImpl<ActivityIslandGroupMeal>  implements ActivityIslandGroupMealDao{
	@Override
	public ActivityIslandGroupMeal getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroupMeal activityIslandGroupMeal where activityIslandGroupMeal.uuid=? and activityIslandGroupMeal.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroupMeal)entity;
		}
		return null;
	}
	
	
	public List<ActivityIslandGroupMeal> getByactivityIslandGroupRoomUuid(String roomUuid) {
		return super.find("from ActivityIslandGroupMeal activityIslandGroupMeal where activityIslandGroupMeal.activityIslandGroupRoomUuid=? and activityIslandGroupMeal.delFlag=?", roomUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public int updateByActivityIslandGroupUUid(String islandGroupUuid){
		return super.updateBySql("update activity_island_group_meal set delFlag=? where activity_island_group_uuid=? ", BaseEntity.DEL_FLAG_DELETE, islandGroupUuid);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityIslandGroupMeal> getMealListByGroupUuid(String uuid) {
		Object obj=super.createQuery("from ActivityIslandGroupMeal activityIslandGroupMeal where activityIslandGroupMeal.activityIslandGroupUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		if(obj!=null){
			return (List<ActivityIslandGroupMeal>)obj;
		}
		return null;
	}
	
}
