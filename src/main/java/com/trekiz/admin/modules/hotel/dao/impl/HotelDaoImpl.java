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
import com.trekiz.admin.modules.hotel.dao.HotelDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelDaoImpl extends BaseDaoImpl<Hotel>  implements HotelDao{
	
	public Hotel getByUuid(String value){
		
		Hotel h = (Hotel)super.getSession().createQuery("from Hotel hotel where hotel.uuid='"+value+"'").uniqueResult();
		return h;
	}
	

	/**
	 * 根据岛屿和公司Id查询酒店集合
	 * @param islandUuid	岛屿uuid
	 * @param companyId		公司id
	 * @return
	 */
	public List<Hotel> findByIslandUuidAndCompany(String islandUuid, int companyId) {
		List<Hotel> hotels = super.find("from Hotel hotel where hotel.islandUuid = ? and hotel.wholesalerId = ? and hotel.delFlag = ?", islandUuid, companyId, BaseEntity.DEL_FLAG_NORMAL);
		return hotels;
	}
	
	public String getHotelGroupByUuid(String hotelUuid) {
		Object hotelGroup = (Object)super.createSqlQuery("SELECT label FROM sys_company_dict_view view WHERE view.uuid = (SELECT hotel_group FROM hotel hotel WHERE hotel.uuid = ?)", hotelUuid).uniqueResult();
		if(hotelGroup != null) {
			return (String)hotelGroup;
		}
		return "";
	}
	
	public List<Hotel> findHotelsByCompanyId(Integer companyId) {
		return super.find("from Hotel hotel where hotel.wholesalerId = ? and hotel.delFlag = ?", companyId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<Hotel> findHotelsByGroupAndCompany(String hotelGroup, Integer companyId) {
		return super.find("from Hotel hotel where hotel.hotelGroup=? and hotel.wholesalerId = ? and hotel.delFlag = ?", hotelGroup, companyId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public List<Hotel> findHotelsByCompanyIdAndCountry(Integer companyId, String country) {
		return super.find("from Hotel hotel where hotel.wholesalerId = ? and hotel.country=? and hotel.delFlag = ?", companyId, country, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	
}
