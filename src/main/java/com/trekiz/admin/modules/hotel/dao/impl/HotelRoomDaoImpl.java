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
import com.trekiz.admin.modules.hotel.entity.HotelRoom;

import com.trekiz.admin.modules.hotel.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelRoomDaoImpl extends BaseDaoImpl<HotelRoom>  implements HotelRoomDao{
	
	@Override
	public HotelRoom getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelRoom hotelRoom where hotelRoom.uuid=? and hotelRoom.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelRoom)entity;
		}
		return null;
	}
	
	public List<HotelRoom> findHotelRoomsByHotelUuid(String hotelUuid) {
		return super.find("from HotelRoom hotelRoom where hotelRoom.hotelUuid=? and hotelRoom.delFlag=?", hotelUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
