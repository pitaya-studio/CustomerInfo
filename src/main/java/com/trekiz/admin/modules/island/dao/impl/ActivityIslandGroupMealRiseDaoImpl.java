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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealRiseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupMealRiseDaoImpl extends BaseDaoImpl<ActivityIslandGroupMealRise>  implements ActivityIslandGroupMealRiseDao{
	@Override
	public ActivityIslandGroupMealRise getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroupMealRise activityIslandGroupMealRise where activityIslandGroupMealRise.uuid=? and activityIslandGroupMealRise.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroupMealRise)entity;
		}
		return null;
	}
	
	public List<ActivityIslandGroupMealRise> getbyGroupMealUuid(String mealuuid) {
		return super.find("from ActivityIslandGroupMealRise where activityIslandGroupMealUuid=? and delFlag=?", mealuuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public int updateByActivityIslandGroupUUid(String islandGroupUuid){
		int num = super.updateBySql("update activity_island_group_meal_rise set delFlag=? where activity_island_group_uuid=?",BaseEntity.DEL_FLAG_DELETE, islandGroupUuid);
		return num;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityIslandGroupMealRise> getMealRiseListByGroupUuid(
			String uuid) {
		List<ActivityIslandGroupMealRise> groupMealRises=super.createQuery("from ActivityIslandGroupMealRise where activityIslandGroupUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		return groupMealRises;
	}
	
	@Override
	public List<ActivityIslandGroupMealRise> getMealRiseByMealUuid(String uuid) {
		return super.find("from ActivityIslandGroupMealRise activityIslandGroupMealRise where activityIslandGroupMealRise.activityIslandGroupMealUuid=? and activityIslandGroupMealRise.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL);
	}
}
