/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotelPl.entity.HotelPl;

import java.util.*;

import com.trekiz.admin.modules.hotelPl.dao.*;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlDaoImpl extends BaseDaoImpl<HotelPl>  implements HotelPlDao{
	@Override
	public HotelPl getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPl hotelPl where hotelPl.uuid=? and hotelPl.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPl)entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelPlRoom> getDistinctHotelPlRoomsByUuid(String hotelPlUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT hotelPl.uuid AS hotelPlUuid,hotelPl.hotel_uuid AS hotelUuid,hotelPlPrice.hotel_room_uuid AS hotelRoomUuid,hotelRoom.room_name AS roomName ");
		sb.append("FROM hotel_pl hotelPl "); 
		sb.append("LEFT JOIN hotel_pl_price hotelPlPrice ON hotelPl.uuid = hotelPlPrice.hotel_pl_uuid "); 
		sb.append("LEFT JOIN hotel_room hotelRoom ON hotelPlPrice.hotel_room_uuid = hotelRoom.uuid ");
		sb.append("WHERE hotelPl.uuid = ? and hotelPl.delFlag = ?");
		sb.append("GROUP BY hotelPl.hotel_uuid ");
		
		return (List<HotelPlRoom>) super.findCustomObjBySql(sb.toString(), HotelPlRoom.class, hotelPlUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public boolean findIsExist(String hotelUuid, int purchaseType, int supplierInfoId) {
		List<HotelPl> hotelPlList = super.find("from HotelPl hotelPl where hotelPl.hotelUuid=? and hotelPl.purchaseType=? and hotelPl.supplierInfoId=? and hotelPl.delFlag=?", hotelUuid, purchaseType, supplierInfoId, BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(hotelPlList)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 批量删除价单接口
	     * @discription 
	     * @author majiancheng       
	     * @created 2015-10-23 下午3:23:56      
	     * @param uuids
	     * @return     
	     * @see com.trekiz.admin.modules.hotelPl.dao.HotelPlDao#batchDelete(java.lang.String[])
	 */
	public boolean batchDelete(String[] uuids) {
		if(ArrayUtils.isEmpty(uuids)) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		for(String uuid : uuids) {
			sb.append("'");
			sb.append(uuid);
			sb.append("'");
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		int count = super.createSqlQuery("update hotel_pl set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
}
