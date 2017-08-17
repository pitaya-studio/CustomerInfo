/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityHotelGroupMealDao  extends BaseDao<ActivityHotelGroupMeal> {
	
	public ActivityHotelGroupMeal getByUuid(String uuid);

	public List<ActivityHotelGroupMeal> getMealListByGroupUuid(String uuid);
	
	/**
	 * 根据房型uuid获取餐型集合
	*<p>Title: getMealListByRoomUuid</p>
	* @return List<ActivityHotelGroupMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-25 上午11:18:42
	* @throws
	 */
	public List<ActivityHotelGroupMeal> getMealListByRoomUuid(String roomUuid);
	
	/**
	 * 根据团期房型uuid获取团期餐型集合
	*<p>Title: getByactivityIslandGroupRoomUuid</p>
	* @return List<ActivityIslandGroupMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-25 上午10:05:44
	* @throws
	 */
	public List<ActivityHotelGroupMeal> getByactivityHotelGroupRoomUuid(String roomUuid);
}
