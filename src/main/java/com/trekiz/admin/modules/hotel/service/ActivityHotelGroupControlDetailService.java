/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupControlDetailInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupControlDetailQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelGroupControlDetailService{
	
	public void save (ActivityHotelGroupControlDetail activityHotelGroupControlDetail);
	
	public void save (ActivityHotelGroupControlDetailInput activityHotelGroupControlDetailInput);
	
	public void update (ActivityHotelGroupControlDetail activityHotelGroupControlDetail);
	
	public ActivityHotelGroupControlDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelGroupControlDetail> find(Page<ActivityHotelGroupControlDetail> page, ActivityHotelGroupControlDetailQuery activityHotelGroupControlDetailQuery);
	
	public List<ActivityHotelGroupControlDetail> find( ActivityHotelGroupControlDetailQuery activityHotelGroupControlDetailQuery);
	
	public ActivityHotelGroupControlDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<ActivityHotelGroupControlDetail> getDetailListByGroupUuid(String uuid);
}
