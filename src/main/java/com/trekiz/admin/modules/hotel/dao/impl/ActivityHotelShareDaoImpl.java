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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelShareDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelShareDaoImpl extends BaseDaoImpl<ActivityHotelShare>  implements ActivityHotelShareDao{
	@Override
	public ActivityHotelShare getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelShare activityHotelShare where activityHotelShare.uuid=? and activityHotelShare.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelShare)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelShare> findByActivityHotelUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelShare activityHotelShare where activityHotelShare.activityHotelUuid=? and activityHotelShare.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).list();
		if(entity != null) {
			return (List<ActivityHotelShare>)entity;
		}
		return null;
	}
	
}
