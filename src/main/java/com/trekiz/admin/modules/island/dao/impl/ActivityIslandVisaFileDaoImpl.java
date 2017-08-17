/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.ActivityIslandVisaFileDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandVisaFileDaoImpl extends BaseDaoImpl<ActivityIslandVisaFile>  implements ActivityIslandVisaFileDao{
	@Override
	public ActivityIslandVisaFile getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityIslandVisaFile activityIslandVisaFile where activityIslandVisaFile.uuid=? and activityIslandVisaFile.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityIslandVisaFile)entity;
		}
		return null;
	}
	
	public int updateByActivityIslandUUid(String ActivityIslandUuid){
		return super.updateBySql("update activity_island_visa_file set delFlag=? where activity_island_uuid=? ", BaseEntity.DEL_FLAG_DELETE, ActivityIslandUuid);
	}
	
}
