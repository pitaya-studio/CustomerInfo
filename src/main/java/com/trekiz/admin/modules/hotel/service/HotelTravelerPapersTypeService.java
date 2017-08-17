/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerPapersType;
import com.trekiz.admin.modules.hotel.input.HotelTravelerPapersTypeInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerPapersTypeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelTravelerPapersTypeService{
	
	public void save (HotelTravelerPapersType hotelTravelerPapersType);
	
	public void save (HotelTravelerPapersTypeInput hotelTravelerPapersTypeInput);
	
	public void update (HotelTravelerPapersType hotelTravelerPapersType);
	
	public HotelTravelerPapersType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelTravelerPapersType> find(Page<HotelTravelerPapersType> page, HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery);
	
	public List<HotelTravelerPapersType> find( HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery);
	
	public HotelTravelerPapersType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
