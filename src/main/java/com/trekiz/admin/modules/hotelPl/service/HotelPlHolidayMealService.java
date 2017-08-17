/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;

import com.trekiz.admin.modules.hotelPl.entity.*;
import com.trekiz.admin.modules.hotelPl.input.*;
import com.trekiz.admin.modules.hotelPl.query.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlHolidayMealService{
	
	public void save (HotelPlHolidayMeal hotelPlHolidayMeal);
	
	public void save (HotelPlHolidayMealInput hotelPlHolidayMealInput);
	
	public void update (HotelPlHolidayMeal hotelPlHolidayMeal);
	
	public HotelPlHolidayMeal getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlHolidayMeal> find(Page<HotelPlHolidayMeal> page, HotelPlHolidayMealQuery hotelPlHolidayMealQuery);
	
	public List<HotelPlHolidayMeal> find( HotelPlHolidayMealQuery hotelPlHolidayMealQuery);
	
	public HotelPlHolidayMeal getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单节日餐集合
	*<p>Title: getPlHolidayMealsByHotelPlUuid</p>
	* @return List<HotelPlHolidayMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:59:31
	* @throws
	 */
	public List<HotelPlHolidayMeal> findPlHolidayMealsByHotelPlUuid(String hotelPlUuid);
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的强制餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlHolidayMeal> getHotelPlHolidayMeal4AutoQuotedPrice(HotelPlHolidayMealQuery hotelPlHolidayMealQuery);
	
}
