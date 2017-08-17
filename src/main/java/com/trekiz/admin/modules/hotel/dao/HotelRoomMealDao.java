/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelRoomMealDao  extends BaseDao<HotelRoomMeal> {
	
	public HotelRoomMeal getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取酒店房型餐型关联集合
	*<p>Title: getByHotelUuid</p>
	* @return List<HotelRoomMeal> 返回类型
	* @author majiancheng
	* @date 2015-7-2 下午9:27:08
	* @throws
	 */
	public List<HotelRoomMeal> getByRoomUuid(String hotelUuid);
	
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
