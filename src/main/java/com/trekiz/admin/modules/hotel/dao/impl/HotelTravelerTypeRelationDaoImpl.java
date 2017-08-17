/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;

import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelTravelerTypeRelationDaoImpl extends BaseDaoImpl<HotelTravelerTypeRelation>  implements HotelTravelerTypeRelationDao{
	@Override
	public HotelTravelerTypeRelation getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelTravelerTypeRelation hotelTravelerTypeRelation where hotelTravelerTypeRelation.hotelUuid=? and hotelTravelerTypeRelation.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelTravelerTypeRelation)entity;
		}
		return null;
	}
	
	public List<HotelTravelerTypeRelation> getByHotelUuid(String hotelUuid) {
		return super.find("from HotelTravelerTypeRelation hotelTravelerTypeRelation where hotelTravelerTypeRelation.hotelUuid=? and hotelTravelerTypeRelation.delFlag=?", hotelUuid, BaseEntity.DEL_FLAG_NORMAL);
	}

	@Override
	public void deleteByHotelUuid(String hotelUuid) {
		// TODO Auto-generated method stub
//		List<String> list = new  ArrayList<String>();
		String sql = "delete from hotel_traveler_type_relation  where hotel_uuid = '"+hotelUuid+"' ";
//		list.add(hotelUuid);
		this.updateBySql(sql);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<TravelerType> getTravelerTypesByHotelUuid(String hotelUuid) {
		SQLQuery sqlQuery = super.createSqlQuery("select * from traveler_type travelerType where travelerType.uuid in(select traveler_type_uuid from hotel_traveler_type_relation rel where rel.hotel_uuid = ?) and travelerType.status = ? and travelerType.delFlag = ? order by travelerType.sort asc", hotelUuid, TravelerType.STATUS_ON, BaseEntity.DEL_FLAG_NORMAL).addEntity(TravelerType.class);
		return (List<TravelerType>)sqlQuery.list();
	}
	
}
