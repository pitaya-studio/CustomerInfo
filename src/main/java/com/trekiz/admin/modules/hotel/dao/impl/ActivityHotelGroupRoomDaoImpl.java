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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupRoomDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupRoomDaoImpl extends BaseDaoImpl<ActivityHotelGroupRoom>  implements ActivityHotelGroupRoomDao{
	@Override
	public ActivityHotelGroupRoom getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityHotelGroupRoom activityHotelGroupRoom where activityHotelGroupRoom.uuid=? and activityHotelGroupRoom.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityHotelGroupRoom)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityHotelGroupRoom> getRoomListByGroupUuid(String uuid) {
		Object obj=super.createQuery("from ActivityHotelGroupRoom activityHotelGroupRoom where activityHotelGroupRoom.activityHotelGroupUuid=? and delFlag=?", uuid,BaseEntity.DEL_FLAG_NORMAL).list();
		if(obj!=null){
			return (List<ActivityHotelGroupRoom>)obj;
		}
		return null;
	}
	
}
