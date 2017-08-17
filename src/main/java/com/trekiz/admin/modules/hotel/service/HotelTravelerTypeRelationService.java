/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;
import com.trekiz.admin.modules.hotel.input.HotelTravelerTypeRelationInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerTypeRelationQuery;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelTravelerTypeRelationService{
	
	public void save (HotelTravelerTypeRelation hotelTravelerTypeRelation);
	
	public void save (HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput);
	
	public void update (HotelTravelerTypeRelation hotelTravelerTypeRelation);
	
	public HotelTravelerTypeRelation getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public void removeByHotelUuid(String hotelUuid);
	
	public Page<HotelTravelerTypeRelation> find(Page<HotelTravelerTypeRelation> page, HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery);
	
	public List<HotelTravelerTypeRelation> find( HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery);
	
	public HotelTravelerTypeRelation getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取游客类型
	*<p>Title: getTravelerTypesByHotelUuid</p>
	* @return List<TravelerType> 返回类型
	* @author majiancheng
	* @date 2015-8-17 上午10:09:35
	* @throws
	 */
	public List<TravelerType> getTravelerTypesByHotelUuid(String hotelUuid);
}
