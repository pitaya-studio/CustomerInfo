/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotelPl.entity.HotelPlRoomMemo;

import com.trekiz.admin.modules.hotelPl.dao.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlRoomMemoDaoImpl extends BaseDaoImpl<HotelPlRoomMemo>  implements HotelPlRoomMemoDao{
	@Override
	public HotelPlRoomMemo getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlRoomMemo hotelPlRoomMemo where hotelPlRoomMemo.uuid=? and hotelPlRoomMemo.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlRoomMemo)entity;
		}
		return null;
	}
	
	public List<HotelPlRoomMemo> findPlRoomMemosByHotelPlUuid(String hotelPlUuid) {
		return super.find("from HotelPlRoomMemo hotelPlRoomMemo where hotelPlRoomMemo.hotelPlUuid=? and hotelPlRoomMemo.delFlag=?", hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public int deleteHotelPlRoomMemoByHotelPlUuid(String hotelPlUuid) {
		return super.createSqlQuery("DELETE FROM hotel_pl_room_memo WHERE hotel_pl_uuid = ?", hotelPlUuid).executeUpdate();
	}
}
