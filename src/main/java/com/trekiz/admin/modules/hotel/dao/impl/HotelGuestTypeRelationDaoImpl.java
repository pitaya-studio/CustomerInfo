/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;

import java.util.*;

import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelGuestTypeRelationDaoImpl extends BaseDaoImpl<HotelGuestTypeRelation>  implements HotelGuestTypeRelationDao{
	@Override
	public HotelGuestTypeRelation getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelGuestTypeRelation hotelGuestTypeRelation where hotelGuestTypeRelation.uuid=? and hotelGuestTypeRelation.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelGuestTypeRelation)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelGuestTypeRelation> getByHotelRoomUuid(String roomUuid) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT rel.id,rel.uuid,rel.hotel_guest_type_uuid,guestType.name AS hotel_guest_type_name,rel.hotel_uuid,rel.hotel_room_uuid,rel.createBy,rel.createDate,rel.updateBy,rel.updateDate,rel.delFlag FROM hotel_guest_type_relation rel ");
		sb.append("LEFT JOIN hotel_guest_type guestType ON guestType.uuid = rel.hotel_guest_type_uuid  ");
		sb.append("WHERE rel.hotel_room_uuid = ? AND rel.delFlag = ? AND guestType.delFlag = ? AND guestType.status=? AND guestType.wholesaler_id=? order by guestType.sort");
		SQLQuery sqlQuery = super.createSqlQuery(sb.toString(), roomUuid, BaseEntity.DEL_FLAG_NORMAL, BaseEntity.DEL_FLAG_NORMAL, HotelGuestType.STATUS_ON, companyId.intValue()).addEntity(HotelGuestTypeRelation.class);
		return sqlQuery.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelGuestType> getHotelGuestTypesByRoomUuid(String roomUuid) {
		SQLQuery sqlQuery = super.createSqlQuery("select * from hotel_guest_type hotelGuestType where hotelGuestType.uuid in(select hotel_guest_type_uuid from hotel_guest_type_relation rel where rel.hotel_room_uuid = ?) and hotelGuestType.status = ? and hotelGuestType.delFlag = ? order by hotelGuestType.sort asc", roomUuid, HotelGuestType.STATUS_ON, BaseEntity.DEL_FLAG_NORMAL).addEntity(HotelGuestType.class);
		return (List<HotelGuestType>)sqlQuery.list();
	}
	
}
