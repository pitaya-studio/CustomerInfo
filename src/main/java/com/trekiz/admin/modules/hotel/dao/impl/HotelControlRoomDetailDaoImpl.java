/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

import java.util.*;

import com.trekiz.admin.modules.hotel.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelControlRoomDetailDaoImpl extends BaseDaoImpl<HotelControlRoomDetail>  implements HotelControlRoomDetailDao{
	@Override
	public HotelControlRoomDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelControlRoomDetail hotelControlRoomDetail where hotelControlRoomDetail.uuid=? and hotelControlRoomDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelControlRoomDetail)entity;
		}
		return null;
	}
	
	/**
	 * 根据控房明细UUID查询所有的房型信息 add by zhanghao
	 * @param detailUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotelControlRoomDetail> getListByDetailUuid(String detailUuid){
		org.hibernate.Query query = super.createQuery("from HotelControlRoomDetail hotelControlRoomDetail where hotelControlRoomDetail.hotelControlDetailUuid=? and hotelControlRoomDetail.delFlag=?", detailUuid, BaseEntity.DEL_FLAG_NORMAL);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelControlRoomDetail> getNamesByDetailUuid(String detailUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT room_uuid AS roomUuid, ");
		sb.append("night     AS night,");
		sb.append("room_name AS roomName ");
		sb.append("FROM   hotel_control_room_detail detail ");
		sb.append("LEFT JOIN hotel_room room ");
		sb.append("ON detail.room_uuid = room.uuid ");
		sb.append("where detail.hotel_control_detail_uuid=? and detail.delFlag=?");
		return (List<HotelControlRoomDetail>) super.findCustomObjBySql(sb.toString(), HotelControlRoomDetail.class, detailUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<HotelControlRoomDetail> getByHotelControlUuid(String hotelControlUuid) {
		return super.find("from HotelControlRoomDetail hotelControlRoomDetail where hotelControlRoomDetail.hotelControlUuid=? and hotelControlRoomDetail.delFlag=?", hotelControlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
