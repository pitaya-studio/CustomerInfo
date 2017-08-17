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

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupAirlineDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupAirlineDaoImpl extends BaseDaoImpl<ActivityIslandGroupAirline>  implements ActivityIslandGroupAirlineDao{
	@Override
	public ActivityIslandGroupAirline getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroupAirline activityIslandGroupAirline where activityIslandGroupAirline.uuid=? and activityIslandGroupAirline.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroupAirline)entity;
		}
		return null;
	}
	@Override
	public int updateByActivityIslandGroupUUid(String islandGroupUuid){
		super.updateBySql("update activity_island_group_airline set delFlag=? where activity_island_group_uuid=? ", BaseEntity.DEL_FLAG_DELETE,islandGroupUuid);
		return 1;
	}
	
	public List<ActivityIslandGroupAirline> getByActivityIslandGroupUuid(String activityIslandGroupUuid){
		return super.find("from ActivityIslandGroupAirline where activityIslandGroupUuid=? and delFlag=?", activityIslandGroupUuid,Context.DEL_FLAG_NORMAL);
	}
}
