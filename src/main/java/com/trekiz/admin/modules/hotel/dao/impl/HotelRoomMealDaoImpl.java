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
import com.trekiz.admin.modules.hotel.dao.HotelRoomMealDao;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelRoomMealDaoImpl extends BaseDaoImpl<HotelRoomMeal>  implements HotelRoomMealDao{
	@Override
	public HotelRoomMeal getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelRoomMeal hotelRoomMeal where hotelRoomMeal.uuid=? and hotelRoomMeal.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelRoomMeal)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelRoomMeal> getByRoomUuid(String roomUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT roomMeal.hotel_uuid AS hotelUuid, roomMeal.hotel_room_uuid AS hotelRoomUuid,roomMeal.hotel_meal_uuid AS hotelMealUuid,hotelMeal.meal_name AS hotelMealName FROM hotel_room_meal roomMeal ");
		sb.append("LEFT JOIN hotel_meal hotelMeal ON roomMeal.hotel_meal_uuid = hotelMeal.uuid ");
		sb.append("WHERE roomMeal.hotel_room_uuid = ? AND roomMeal.delFlag=?");
		return (List<HotelRoomMeal>) super.findCustomObjBySql(sb.toString(), HotelRoomMeal.class, roomUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<HotelMeal> findByHotelRoomUUid(String hotelRoomUuid){
		String sqlString = "SELECT hm.id,hm.uuid,hm.hotel_uuid,hm.meal_name,hm.meal_type,hm.suitable_num,hm.price,hm.sort,hm.meal_description,hm.createBy,hm.createDate,hm.updateBy,hm.updateDate,hm.delFlag,hm.wholesaler_id,hm.meal_name from hotel_room_meal base LEFT JOIN hotel_meal hm on base.hotel_meal_uuid = hm.uuid where base.hotel_room_uuid = ? and base.delFlag=?";
		List<HotelMeal> list = super.findBySql(sqlString, HotelMeal.class, hotelRoomUuid,BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
	

	@SuppressWarnings("unchecked")
	public List<HotelRoomMeal> findByHotelUuid(String hotelUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT roomMeal.hotel_uuid AS hotelUuid, roomMeal.hotel_room_uuid AS hotelRoomUuid,roomMeal.hotel_meal_uuid AS hotelMealUuid,hotelMeal.meal_name AS hotelMealName FROM hotel_room_meal roomMeal ");
		sb.append("LEFT JOIN hotel_meal hotelMeal ON roomMeal.hotel_meal_uuid = hotelMeal.uuid ");
		sb.append("WHERE roomMeal.hotel_uuid = ? AND roomMeal.delFlag=?");
		return (List<HotelRoomMeal>) super.findCustomObjBySql(sb.toString(), HotelRoomMeal.class, hotelUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	
}
