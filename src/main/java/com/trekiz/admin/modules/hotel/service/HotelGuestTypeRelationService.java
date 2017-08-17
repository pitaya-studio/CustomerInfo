/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;
import com.trekiz.admin.modules.hotel.input.HotelGuestTypeRelationInput;
import com.trekiz.admin.modules.hotel.query.HotelGuestTypeRelationQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelGuestTypeRelationService{
	
	public void save (HotelGuestTypeRelation hotelGuestTypeRelation);
	
	public void save (HotelGuestTypeRelationInput hotelGuestTypeRelationInput);
	
	public void update (HotelGuestTypeRelation hotelGuestTypeRelation);
	
	public HotelGuestTypeRelation getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelGuestTypeRelation> find(Page<HotelGuestTypeRelation> page, HotelGuestTypeRelationQuery hotelGuestTypeRelationQuery);
	
	public List<HotelGuestTypeRelation> find( HotelGuestTypeRelationQuery hotelGuestTypeRelationQuery);
	
	public HotelGuestTypeRelation getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
