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

public interface HotelRoomOccuRateService{
	
	public void save (HotelRoomOccuRate hotelRoomOccuRate);
	
	public void update (HotelRoomOccuRate hotelRoomOccuRate);
	
	public HotelRoomOccuRate getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelRoomOccuRate> find(Page<HotelRoomOccuRate> page, HotelRoomOccuRate hotelRoomOccuRate);
	
	public List<HotelRoomOccuRate> find( HotelRoomOccuRate hotelRoomOccuRate);
	
	public HotelRoomOccuRate getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店房型uuid获取酒店容住率表信息
		* 
		* @param hotelRoomUuid 酒店房型uuid
		* @return List<HotelRoomOccuRate>
		* @author majiancheng
		* @Time 2015-5-4
	 */
	public List<HotelRoomOccuRate> findRoomOccuRateByHotelRoom(String hotelRoomUuid);
}
