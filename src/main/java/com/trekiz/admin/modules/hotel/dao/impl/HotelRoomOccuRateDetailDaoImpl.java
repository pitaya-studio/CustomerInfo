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
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.hotel.entity.HotelRoomOccuRateDetail;
import com.trekiz.admin.modules.hotel.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelRoomOccuRateDetailDaoImpl extends BaseDaoImpl<HotelRoomOccuRateDetail>  implements HotelRoomOccuRateDetailDao{
	@Override
	public HotelRoomOccuRateDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelRoomOccuRateDetail hotelRoomOccuRateDetail where hotelRoomOccuRateDetail.uuid=? ", uuid).uniqueResult();
		if(entity != null) {
			return (HotelRoomOccuRateDetail)entity;
		}
		return null;
	}
	
	public HotelRoomOccuRateDetail getByRoomOccuRateUuidAndTravelerTypeUuid(String roomOccuRateUuid, String travelerTypeUuid) {
		Object entity = super.createQuery("from HotelRoomOccuRateDetail hotelRoomOccuRateDetail where hotelRoomOccuRateDetail.hotelRoomOccuRateUuid=? and hotelRoomOccuRateDetail.travelerTypeUuid=?", roomOccuRateUuid, travelerTypeUuid).uniqueResult();
		if(entity != null) {
			return (HotelRoomOccuRateDetail)entity;
		}
		return null;
	}
	
	public int removeByRoomOccuRateUuid(String roomOccuRateUuid) {
		return super.createSqlQuery("delete from hotel_room_occuRate_detail where hotel_room_occuRate_uuid = ?", roomOccuRateUuid).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDetailUuidsByRoomUuid(String hotelRoomUuid) {
		return super.createSqlQuery("select uuid from hotel_room_occuRate_detail where hotel_room_uuid = ?", hotelRoomUuid).list();
	}
	
	public int removeByOccuRateDetailUuids(List<String> toDelOccuRateDetailUuids) {
		StringBuffer sqlUuids = new StringBuffer();
		if(CollectionUtils.isEmpty(toDelOccuRateDetailUuids)) {
			return 0;
		}
		for(String uuid : toDelOccuRateDetailUuids) {
			sqlUuids.append("'");
			sqlUuids.append(uuid);
			sqlUuids.append("',");
		}
		sqlUuids.deleteCharAt(sqlUuids.length()-1);
		
		return super.createSqlQuery("delete from hotel_room_occuRate_detail where uuid in ("+ sqlUuids +")").executeUpdate();
	}
	
}
