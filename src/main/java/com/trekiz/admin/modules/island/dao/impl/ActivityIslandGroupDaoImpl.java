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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupDaoImpl extends BaseDaoImpl<ActivityIslandGroup>  implements ActivityIslandGroupDao{
	@Override
	public ActivityIslandGroup getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandGroup activityIslandGroup where activityIslandGroup.uuid=? and activityIslandGroup.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandGroup)entity;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ActivityIslandGroup> findGroupByActivityIslandUuid(String activityIslandUuid) {
		return super.createQuery("from ActivityIslandGroup activityIslandGroup where activityIslandGroup.activityIslandUuid=? and activityIslandGroup.delFlag=?", activityIslandUuid, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	
	public int batchUpdateStatusByIslandUuidArr(String[] islandUuidArray, String status) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE activity_island_group SET status = ? WHERE activity_island_uuid IN (");
		if(islandUuidArray != null && islandUuidArray.length > 0) {
			for(String islandUuid : islandUuidArray) {
				sb.append("'");
				sb.append(islandUuid);
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(") AND delFlag = ?");
		return super.createSqlQuery(sb.toString(), status, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
	}
	
	public int batchUpdateStatusByGroupUuidArr(String[] uuidArray, String status) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE activity_island_group SET status = ? WHERE uuid IN (");
		if(uuidArray != null && uuidArray.length > 0) {
			for(String islandUuid : uuidArray) {
				sb.append("'");
				sb.append(islandUuid);
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(") AND delFlag = ?");
		return super.createSqlQuery(sb.toString(), status, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
	}

	@Override
	public int updateIscommission(Integer id, int iscommission) {
		StringBuffer sb = new StringBuffer("update activity_island_group set iscommission = ? where id = ?");
		return createSqlQuery(sb.toString(), iscommission,id).executeUpdate();

	}
}
