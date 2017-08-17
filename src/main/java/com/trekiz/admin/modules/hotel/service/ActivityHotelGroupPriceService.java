/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupPriceInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelGroupPriceService{
	
	public void save (ActivityHotelGroupPrice activityHotelGroupPrice);
	
	public void save (ActivityHotelGroupPriceInput activityHotelGroupPriceInput);
	
	public void update (ActivityHotelGroupPrice activityHotelGroupPrice);
	
	public ActivityHotelGroupPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelGroupPrice> find(Page<ActivityHotelGroupPrice> page, ActivityHotelGroupPriceQuery activityHotelGroupPriceQuery);
	
	public List<ActivityHotelGroupPrice> find( ActivityHotelGroupPriceQuery activityHotelGroupPriceQuery);
	
	public ActivityHotelGroupPrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<ActivityHotelGroupPrice> getPriceListByGroupUuid(String uuid);
	
	/**
	 * 根据团期uuid,酒店uuid查询ActivityHotelGroupPrice
	 * @author star
	 * @param groupUuid
	 * @param hotelUuid
	 * @return
	 */
	public List<ActivityHotelGroupPrice> getPriceFilterTravel(String groupUuid,String hotelUuid);
}
