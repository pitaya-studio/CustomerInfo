/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.input.HotelOrderPriceInput;
import com.trekiz.admin.modules.hotel.query.HotelOrderPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelOrderPriceService{
	
	public void save (HotelOrderPrice hotelOrderPrice);
	
	public void save (HotelOrderPriceInput hotelOrderPriceInput);
	
	public void update (HotelOrderPrice hotelOrderPrice);
	
	public HotelOrderPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelOrderPrice> find(Page<HotelOrderPrice> page, HotelOrderPriceQuery hotelOrderPriceQuery);
	
	public List<HotelOrderPrice> find( HotelOrderPriceQuery hotelOrderPriceQuery);
	
	public HotelOrderPrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public HotelOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid);
	
	public List<HotelOrderPrice> getByOrderUuid(String orderUuid);
}
