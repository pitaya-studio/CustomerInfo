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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupRoomDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupRoomDaoImpl extends BaseDaoImpl<ActivityIslandGroupRoom>  implements ActivityIslandGroupRoomDao{
	@Override
	public ActivityIslandGroupRoom getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroupRoom activityIslandGroupRoom where activityIslandGroupRoom.uuid=? and activityIslandGroupRoom.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroupRoom)entity;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ActivityIslandGroupRoom> getRoomListByGroupUuid(String groupUuid) {
		return super.createQuery("from ActivityIslandGroupRoom where activityIslandGroupUuid=? and delFlag=?", groupUuid, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	public List<ActivityIslandGroupRoom> getRoomListByActivityIslandGroupUuid(String groupUuid) {
		return super.find("from ActivityIslandGroupRoom where activityIslandGroupUuid=? and delFlag=?", groupUuid, BaseEntity.DEL_FLAG_NORMAL);
	}

	@Override
	public int updateByActivityIslandGroupUUid(String islandGroupUuid) {
		return super.updateBySql("update activity_island_group_room set delFlag=? where activity_island_group_uuid=? ", BaseEntity.DEL_FLAG_DELETE, islandGroupUuid);
	}
	
}
