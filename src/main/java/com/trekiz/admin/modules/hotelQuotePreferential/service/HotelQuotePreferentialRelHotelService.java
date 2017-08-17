/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRelHotel;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRelHotelInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRelHotelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuotePreferentialRelHotelService{
	
	public void save (HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel);
	
	public void save (HotelQuotePreferentialRelHotelInput hotelQuotePreferentialRelHotelInput);
	
	public void update (HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel);
	
	public HotelQuotePreferentialRelHotel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuotePreferentialRelHotel> find(Page<HotelQuotePreferentialRelHotel> page, HotelQuotePreferentialRelHotelQuery hotelQuotePreferentialRelHotelQuery);
	
	public List<HotelQuotePreferentialRelHotel> find( HotelQuotePreferentialRelHotelQuery hotelQuotePreferentialRelHotelQuery);
	
	public HotelQuotePreferentialRelHotel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
