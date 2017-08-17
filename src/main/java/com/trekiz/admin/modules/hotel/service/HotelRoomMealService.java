/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.input.HotelRoomMealInput;
import com.trekiz.admin.modules.hotel.query.HotelRoomMealQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelRoomMealService{
	
	public void save (HotelRoomMeal hotelRoomMeal);
	
	public void save (HotelRoomMealInput hotelRoomMealInput);
	
	public void update (HotelRoomMeal hotelRoomMeal);
	
	public HotelRoomMeal getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelRoomMeal> find(Page<HotelRoomMeal> page, HotelRoomMealQuery hotelRoomMealQuery);
	
	public List<HotelRoomMeal> find( HotelRoomMealQuery hotelRoomMealQuery);
	
	public HotelRoomMeal getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**根据酒店房型的UUid查询出房型下的基础餐型
	 * @param uuid
	 * @return
	 */
	public List<HotelMeal> findByHotelRoomUUid(String hotelRoomUuid);
	
	/**
	 * 根据酒店uuid获取所有的房型集合
	*<p>Title: findByHotelUuid</p>
	* @return List<HotelMeal> 返回类型
	* @author majiancheng
	* @date 2015-8-6 下午4:30:13
	* @throws
	 */
	public List<HotelRoomMeal> findByHotelUuid(String hotelUuid);
}
