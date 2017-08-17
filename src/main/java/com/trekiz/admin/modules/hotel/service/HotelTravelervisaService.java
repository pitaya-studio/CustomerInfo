/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelTravelervisa;
import com.trekiz.admin.modules.hotel.input.HotelTravelervisaInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelervisaQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelTravelervisaService{
	
	public void save (HotelTravelervisa hotelTravelervisa);
	
	public void save (HotelTravelervisaInput hotelTravelervisaInput);
	
	public void update (HotelTravelervisa hotelTravelervisa);
	
	public HotelTravelervisa getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<HotelTravelervisa> find(Page<HotelTravelervisa> page, HotelTravelervisaQuery hotelTravelervisaQuery);
	
	public List<HotelTravelervisa> find( HotelTravelervisaQuery hotelTravelervisaQuery);
	
	public HotelTravelervisa getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
