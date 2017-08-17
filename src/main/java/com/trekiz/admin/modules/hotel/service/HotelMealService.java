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

public interface HotelMealService{
	
	public void save (HotelMeal hotelMeal);
	
	public void update (HotelMeal hotelMeal);
	
	public HotelMeal getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelMeal> find(Page<HotelMeal> page, HotelMeal hotelMeal);
	
	public List<HotelMeal> find( HotelMeal hotelMeal);
	
	public HotelMeal getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	* 查询酒店餐型中名称是否存在(名称存在返回true，反之返回false)
	* @param hotelMeal 酒店餐型
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public boolean findIsExist(HotelMeal hotelMeal);
	/**
	 * 通过hotelUuid查找餐型列表
	 * @param hotelUuid
	 * @return
	 */
	public List<HotelMeal> getMealListByUuid(String hotelUuid);
	
	/**
	 * 根据uuid集合获取多个餐型
	*<p>Title: getMealListByUuids</p>
	* @return List<HotelMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-23 下午9:19:12
	* @throws
	 */
	public List<HotelMeal> getMealListByUuids(List<String> mealUuids);
	
	/**
	 * 通过hotelRoomUuid查找餐型uuid
	 * @param uuid
	 * @return
	 */
	public List<HotelRoomMeal> getMealListByRoomUuid(String uuid);
	
}
