/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelGuestTypeDaoImpl extends BaseDaoImpl<HotelGuestType>  implements HotelGuestTypeDao{
	@Override
	public HotelGuestType getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelGuestType hotelGuestType where hotelGuestType.uuid=? and hotelGuestType.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelGuestType)entity;
		}
		return null;
	}
	
	public List<HotelGuestType> findByWholesalerId(Integer wholesalerId) {
		return super.find("from HotelGuestType hotelGuestType where hotelGuestType.wholesalerId=? and hotelGuestType.status=? and hotelGuestType.delFlag=? order by hotelGuestType.sort", wholesalerId, HotelGuestType.STATUS_ON, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	
	/**
	 * 查询批发商下 第N人类型 的游客类型简写和批发商住客类型UUID的集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<HotelGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，简写来源系统游客类型的简写
	 * 			
	 */
	public List<HotelGuestType> findShortNameAndGuestTypeUuidList(Integer wholesalerId,List<String> shortNameParams){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT hotelGuest.name as name,traveler.short_name as shortName, hotelGuest.uuid as uuid");
		sb.append(" FROM sys_guest_type guest, sys_traveler_type traveler, sys_guest_traveler_type_rel rel, hotel_guest_type hotelGuest");
		sb.append(" WHERE guest.uuid = rel.sys_guest_type_uuid AND traveler.uuid = rel.sys_traveler_type_uuid AND guest.uuid = hotelGuest.sys_guest_type");
		sb.append(" AND guest.delFlag = 0 AND traveler.delFlag = 0 AND hotelGuest.delFlag = 0 AND rel.delFlag = 0");
		sb.append(" AND hotelGuest.wholesaler_id = ? AND guest.type = 1");
		if(CollectionUtils.isNotEmpty(shortNameParams)){
			sb.append(" AND traveler.short_name in(");
			for(String shortName:shortNameParams){
				sb.append("'"+shortName+"'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(")");
		}
		List<? extends Serializable> list = super.findCustomObjBySql(sb.toString(), HotelGuestType.class,  wholesalerId);
		return (List<HotelGuestType>) list;
	}
	
	/**
	 * 根据批发商id获取所有的酒店住客类型
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<HotelGuestType>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午12:04:20
	 */
	public List<HotelGuestType> findAllByWholesalerId(Integer wholesalerId) {
		return super.find("from HotelGuestType hotelGuestType where hotelGuestType.wholesalerId=? and hotelGuestType.delFlag=? order by hotelGuestType.sort asc, hotelGuestType.updateDate desc", wholesalerId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
