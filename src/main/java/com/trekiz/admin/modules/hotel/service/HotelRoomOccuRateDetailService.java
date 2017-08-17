/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelRoomOccuRateDetailService{
	
	public void save (HotelRoomOccuRateDetail hotelRoomOccuRateDetail);
	
	public void update (HotelRoomOccuRateDetail hotelRoomOccuRateDetail);
	
	public HotelRoomOccuRateDetail getById(java.lang.Integer value);
	
	public Page<HotelRoomOccuRateDetail> find(Page<HotelRoomOccuRateDetail> page, HotelRoomOccuRateDetail hotelRoomOccuRateDetail);
	
	public List<HotelRoomOccuRateDetail> find( HotelRoomOccuRateDetail hotelRoomOccuRateDetail);
	
	public HotelRoomOccuRateDetail getByUuid(String uuid);
	
	/**
	 * 根据酒店容住率uuid获取酒店容住率明细
		* 
		* @param hotelRoomUuid 酒店容住率uuid
		* @return List<HotelRoomOccuRateDetail>
		* @author majiancheng
		* @Time 2015-5-4
	 */
	public List<HotelRoomOccuRateDetail> findOccuRateDetailByRoomOccuRate(String roomOccuRateUuid);
}
