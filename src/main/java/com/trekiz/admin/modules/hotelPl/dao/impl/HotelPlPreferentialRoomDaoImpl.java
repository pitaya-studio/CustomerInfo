/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRoomDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialRoomDaoImpl extends BaseDaoImpl<HotelPlPreferentialRoom>  implements HotelPlPreferentialRoomDao{
	@Autowired
	private HotelMealService hotelMealService;
	@Override
	public HotelPlPreferentialRoom getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferentialRoom hotelPlPreferentialRoom where hotelPlPreferentialRoom.uuid=? and hotelPlPreferentialRoom.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferentialRoom)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelPlPreferentialRoom> findRoomsByPreferentialUuid(String preferentialUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT preferentialRoom.uuid,preferentialRoom.hotel_pl_uuid AS hotelPlUuid,preferentialRoom.hotel_pl_preferential_uuid AS hotelPlPreferentialUuid,");
		sb.append("preferentialRoom.hotel_uuid AS hotelUuid,preferentialRoom.hotel_room_uuid AS hotelRoomUuid,preferentialRoom.nights,preferentialRoom.hotel_meal_uuids AS hotelMealUuids, ");
		sb.append("hotelRoom.room_name AS hotelRoomText,hotelRoom.occupancy_rate AS roomOccupancyRate,hotel.name_cn AS relHotelName ");
		sb.append("FROM hotel_pl_preferential_room preferentialRoom ");
		sb.append("LEFT JOIN hotel_room hotelRoom ON preferentialRoom.hotel_room_uuid = hotelRoom.uuid ");
		sb.append("LEFT JOIN hotel hotel ON preferentialRoom.hotel_uuid = hotel.uuid ");
		sb.append("WHERE preferentialRoom.hotel_pl_preferential_uuid=? and preferentialRoom.delFlag=?");
		
		List<HotelPlPreferentialRoom> preferentialRooms = (List<HotelPlPreferentialRoom>) super.findCustomObjBySql(sb.toString(), HotelPlPreferentialRoom.class, preferentialUuid, BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(preferentialRooms)) {
			for(HotelPlPreferentialRoom preferentialRoom : preferentialRooms) {
				String hotelMealUuids = preferentialRoom.getHotelMealUuids();
				if(StringUtils.isNotEmpty(hotelMealUuids)) {
					List<HotelMeal> hotelMealList = hotelMealService.getMealListByUuids(Arrays.asList(hotelMealUuids.split(";")));
					
					//复制酒店餐型集合-------------------------
					List<HotelMeal> hotelMeals = new ArrayList<HotelMeal>();
					if(CollectionUtils.isNotEmpty(hotelMealList)) {
						for(HotelMeal src : hotelMealList){
							HotelMeal dest = new HotelMeal();
							BeanUtil.copySimpleProperties(dest, src);
							hotelMeals.add(dest);
						}
					}
					//复制酒店餐型集合-------------------------
					
					preferentialRoom.setHotelMealList(hotelMeals);
				}
			}
		}
		
		return preferentialRooms;
	}
	
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids) {
		if(CollectionUtils.isEmpty(preferentialUuids)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential_room WHERE hotel_pl_preferential_uuid IN ");
		sb.append("(");
		for(String preferentialUuid : preferentialUuids) {
			sb.append("'" + preferentialUuid + "',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return super.createSqlQuery(sb.toString()).executeUpdate();
	}
	
}
