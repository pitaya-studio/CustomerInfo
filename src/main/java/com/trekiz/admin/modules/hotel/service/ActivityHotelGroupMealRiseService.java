/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupMealRiseInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupMealRiseQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelGroupMealRiseService{
	
	public void save (ActivityHotelGroupMealRise activityHotelGroupMealRise);
	
	public void save (ActivityHotelGroupMealRiseInput activityHotelGroupMealRiseInput);
	
	public void update (ActivityHotelGroupMealRise activityHotelGroupMealRise);
	
	public ActivityHotelGroupMealRise getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelGroupMealRise> find(Page<ActivityHotelGroupMealRise> page, ActivityHotelGroupMealRiseQuery activityHotelGroupMealRiseQuery);
	
	public List<ActivityHotelGroupMealRise> find( ActivityHotelGroupMealRiseQuery activityHotelGroupMealRiseQuery);
	
	public ActivityHotelGroupMealRise getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<ActivityHotelGroupMealRise> getMealRiseByMealUuid(String uuid);
}
