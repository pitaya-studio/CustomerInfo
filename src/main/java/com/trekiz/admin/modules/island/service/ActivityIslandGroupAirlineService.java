/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupAirlineInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupAirlineQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupAirlineService{
	
	public void save (ActivityIslandGroupAirline activityIslandGroupAirline);
	
	public void save (ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput);
	
	public void update (ActivityIslandGroupAirline activityIslandGroupAirline);
	
	public ActivityIslandGroupAirline getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupAirline> find(Page<ActivityIslandGroupAirline> page, ActivityIslandGroupAirlineQuery activityIslandGroupAirlineQuery);
	
	public List<ActivityIslandGroupAirline> find( ActivityIslandGroupAirlineQuery activityIslandGroupAirlineQuery);
	
	public ActivityIslandGroupAirline getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<ActivityIslandGroupAirline> getByactivityIslandGroup(String activityIslandGroupUuid);
	
	public List<ActivityIslandGroupAirline> getByactivityIsland(String activityIslandUuid);
	/**
	 * 根据团期uuid获取航班信息
	 * @param groupUuid
	 * @return
	 */
	public List<ActivityIslandGroupAirline> getAirlineByGroupUuid(String groupUuid);

}
