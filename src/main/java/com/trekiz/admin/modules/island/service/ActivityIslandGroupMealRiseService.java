/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupMealRiseInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupMealRiseQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupMealRiseService{
	
	public void save (ActivityIslandGroupMealRise activityIslandGroupMealRise);
	
	public void save (ActivityIslandGroupMealRiseInput activityIslandGroupMealRiseInput);
	
	public void update (ActivityIslandGroupMealRise activityIslandGroupMealRise);
	
	public ActivityIslandGroupMealRise getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupMealRise> find(Page<ActivityIslandGroupMealRise> page, ActivityIslandGroupMealRiseQuery activityIslandGroupMealRiseQuery);
	
	public List<ActivityIslandGroupMealRise> find( ActivityIslandGroupMealRiseQuery activityIslandGroupMealRiseQuery);
	
	public ActivityIslandGroupMealRise getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<ActivityIslandGroupMealRise> getbyGroupMealUuid(String mealuuid);

	public List<ActivityIslandGroupMealRise> getMealRiseListByGroupUuid(
			String uuid);
}
