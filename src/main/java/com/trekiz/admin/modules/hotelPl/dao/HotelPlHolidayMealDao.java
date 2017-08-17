/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlHolidayMeal;
import com.trekiz.admin.modules.hotelPl.query.HotelPlHolidayMealQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlHolidayMealDao  extends BaseDao<HotelPlHolidayMeal> {
	
	public HotelPlHolidayMeal getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单节日餐集合
	*<p>Title: getPlHolidayMealsByHotelPlUuid</p>
	* @return List<HotelPlHolidayMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:59:31
	* @throws
	 */
	public List<HotelPlHolidayMeal> findPlHolidayMealsByHotelPlUuid(String hotelPlUuid);
	
	public Map<String, List<HotelPlHolidayMeal>> findPlHolidayMealMapByHotelPlUuid(String hotelPlUuid) ;
	
	/**
	 * 自动报价 根据条件筛选 符合条件的强制餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlHolidayMeal> getHotelPlHolidayMeal4AutoQuotedPrice(HotelPlHolidayMealQuery hotelPlHolidayMealQuery);
	
	/**
	 * 根据价单uuid删除节日餐集合
	*<p>Title: deletePlHolidayMealByPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:24:54
	* @throws
	 */
	public int deletePlHolidayMealByPlUuid(String hotelPlUuid);
}
