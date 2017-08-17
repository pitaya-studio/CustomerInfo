/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.input.ActivityHotelShareInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelShareQuery;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelShareService{
	
	public void save (ActivityHotelShare activityHotelShare);
	
	public void save (ActivityHotelShareInput activityHotelShareInput);
	
	public void update (ActivityHotelShare activityHotelShare);
	
	public ActivityHotelShare getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelShare> find(Page<ActivityHotelShare> page, ActivityHotelShareQuery activityHotelShareQuery);
	
	public List<ActivityHotelShare> find( ActivityHotelShareQuery activityHotelShareQuery);
	
	public ActivityHotelShare getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	/**
	 * 通过酒店产品uuid查找分享信息
	 * @param uuid
	 * @return
	 */
	public List<ActivityHotelShare> findByActivityHotelUuid(String uuid);

	public List<User> findUserByActivityHotelUuid(String hotelUuid);
}
