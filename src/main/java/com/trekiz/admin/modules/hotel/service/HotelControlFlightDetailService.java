/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelControlFlightDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelControlFlightDetailService{
	
	public void save (HotelControlFlightDetail hotelControlFlightDetail);
	
	public void update (HotelControlFlightDetail hotelControlFlightDetail);
	
	public HotelControlFlightDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelControlFlightDetail> find(Page<HotelControlFlightDetail> page, HotelControlFlightDetail hotelControlFlightDetail);
	
	public List<HotelControlFlightDetail> find( HotelControlFlightDetail hotelControlFlightDetail);
	
	public HotelControlFlightDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<HotelControlFlightDetail> getByHotelControlDetailUuid(String hotelControlDetailUuid);
}
