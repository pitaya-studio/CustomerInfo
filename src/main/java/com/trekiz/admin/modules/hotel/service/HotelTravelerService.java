/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.input.HotelTravelerInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelTravelerService{
	
	public void save (HotelTraveler hotelTraveler);
	
	public void save (HotelTravelerInput hotelTravelerInput);
	
	public void update (HotelTraveler hotelTraveler);
	
	public HotelTraveler getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelTraveler> find(Page<HotelTraveler> page, HotelTravelerQuery hotelTravelerQuery);
	
	public List<HotelTraveler> find( HotelTravelerQuery hotelTravelerQuery);
	
	public HotelTraveler getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * @Description 查询游客
	 * @param isNormal 是否要查询正常游客（不包括已退团和已转团游客）
	 * @author yakun.bai
	 * @Date 2015-11-18
	 */
	public List<HotelTraveler> findTravelerByOrderUuid(String orderUuid, boolean isNormal);
}
