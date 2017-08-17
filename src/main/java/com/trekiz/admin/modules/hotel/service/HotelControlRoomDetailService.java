/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelControlRoomDetailService{
	
	public void save (HotelControlRoomDetail hotelControlRoomDetail);
	
	public void update (HotelControlRoomDetail hotelControlRoomDetail);
	
	public HotelControlRoomDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelControlRoomDetail> find(Page<HotelControlRoomDetail> page, HotelControlRoomDetail hotelControlRoomDetail);
	
	public List<HotelControlRoomDetail> find( HotelControlRoomDetail hotelControlRoomDetail);
	
	public HotelControlRoomDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<HotelControlRoomDetail> getByHotelControlDetailUuid(String hotelControlDetailUuid);
	
}
