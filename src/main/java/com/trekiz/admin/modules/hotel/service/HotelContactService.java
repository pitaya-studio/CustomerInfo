/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelContact;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelContactService{
	
	public void save (HotelContact hotelContact);
	
	public void update (HotelContact hotelContact);
	
	public HotelContact getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<HotelContact> find(Page<HotelContact> page, HotelContact hotelContact);
	
	public List<HotelContact> find( HotelContact hotelContact);
}
