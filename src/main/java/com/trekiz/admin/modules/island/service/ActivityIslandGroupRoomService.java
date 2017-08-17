/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupRoomInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupRoomQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupRoomService{
	
	public void save (ActivityIslandGroupRoom activityIslandGroupRoom);
	
	public void save (ActivityIslandGroupRoomInput activityIslandGroupRoomInput);
	
	public void update (ActivityIslandGroupRoom activityIslandGroupRoom);
	
	public ActivityIslandGroupRoom getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupRoom> find(Page<ActivityIslandGroupRoom> page, ActivityIslandGroupRoomQuery activityIslandGroupRoomQuery);
	
	public List<ActivityIslandGroupRoom> find( ActivityIslandGroupRoomQuery activityIslandGroupRoomQuery);
	
	public ActivityIslandGroupRoom getByUuid(String uuid);
	
	public List<ActivityIslandGroupRoom> getByactivityIslandGroupUuid(String activityIslandGroupUuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据团期uuid获取团期房型集合
	*<p>Title: getRoomListByGroupUuid</p>
	* @return List<ActivityIslandGroupRoom> 返回类型
	* @author majiancheng
	* @date 2015-5-27 下午6:19:24
	* @throws
	 */
	public List<ActivityIslandGroupRoom> getRoomListByGroupUuid(String mealUuid);
	
	public List<Map<String,Object>> getRoomListByactivityIslandGroupUuid(String activityIslandGroupUuid);
}
