/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupMealInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupMealQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupMealService {
	
	public void save (ActivityIslandGroupMeal activityIslandGroupMeal);
	
	public void save (ActivityIslandGroupMealInput activityIslandGroupMealInput);
	
	public void update (ActivityIslandGroupMeal activityIslandGroupMeal);
	
	public ActivityIslandGroupMeal getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupMeal> find(Page<ActivityIslandGroupMeal> page, ActivityIslandGroupMealQuery activityIslandGroupMealQuery);
	
	public List<ActivityIslandGroupMeal> find( ActivityIslandGroupMealQuery activityIslandGroupMealQuery);
	
	public ActivityIslandGroupMeal getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<ActivityIslandGroupMeal> getByactivityIslandGroupUuid(String activityIslandGroupUuid);
	
	public List<ActivityIslandGroupMeal> getMealListByGroupUuid(String uuid);
	
	/**
	 * 根据团期房型uuid获取团期餐型集合
	*<p>Title: getByactivityIslandGroupRoomUuid</p>
	* @return List<ActivityIslandGroupMeal> 返回类型
	* @author majiancheng
	* @date 2015-6-25 上午10:05:44
	* @throws
	 */
	public List<ActivityIslandGroupMeal> getByactivityIslandGroupRoomUuid(String roomUuid);

}
