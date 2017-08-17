/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupLowpriceInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupLowpriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelGroupLowpriceService{
	
	public void save (ActivityHotelGroupLowprice activityHotelGroupLowprice);
	
	public void save (ActivityHotelGroupLowpriceInput activityHotelGroupLowpriceInput);
	
	public void update (ActivityHotelGroupLowprice activityHotelGroupLowprice);
	
	public ActivityHotelGroupLowprice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelGroupLowprice> find(Page<ActivityHotelGroupLowprice> page, ActivityHotelGroupLowpriceQuery activityHotelGroupLowpriceQuery);
	
	public List<ActivityHotelGroupLowprice> find( ActivityHotelGroupLowpriceQuery activityHotelGroupLowpriceQuery);
	
	public ActivityHotelGroupLowprice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<ActivityHotelGroupLowprice> getLowPriceListByGroupUuid(
			String uuid);
	public void getPriceList(String uuid);
	
	public List<ActivityHotelGroupLowprice> getLowprice(String uuid);
}
