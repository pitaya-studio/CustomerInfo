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
import com.trekiz.admin.modules.island.dao.ActivityIslandShareDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandShareDaoImpl extends BaseDaoImpl<ActivityIslandShare>  implements ActivityIslandShareDao{
	@Override
	public ActivityIslandShare getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandShare activityIslandShare where activityIslandShare.uuid=? and activityIslandShare.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandShare)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivityIslandShare> findShareUserByIsland(String activityIslandUuid) {
		return super.createQuery("from ActivityIslandShare activityIslandShare where activityIslandShare.activityIslandUuid=? and activityIslandShare.delFlag=?", activityIslandUuid, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	
	public int deleteShareDataByIslandUuid(String islandUuid) {
		return super.createSqlQuery("update activity_island_share set delFlag = ? where activity_island_uuid=?", BaseEntity.DEL_FLAG_DELETE, islandUuid).executeUpdate();
	}
	
}
