/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotelPl.entity.*;
import com.trekiz.admin.modules.hotelPl.input.*;
import com.trekiz.admin.modules.hotelPl.query.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlPreferentialRoomService{
	
	public void save (HotelPlPreferentialRoom hotelPlPreferentialRoom);
	
	public void save (HotelPlPreferentialRoomInput hotelPlPreferentialRoomInput);
	
	public void update (HotelPlPreferentialRoom hotelPlPreferentialRoom);
	
	public HotelPlPreferentialRoom getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialRoom> find(Page<HotelPlPreferentialRoom> page, HotelPlPreferentialRoomQuery hotelPlPreferentialRoomQuery);
	
	public List<HotelPlPreferentialRoom> find( HotelPlPreferentialRoomQuery hotelPlPreferentialRoomQuery);
	
	public HotelPlPreferentialRoom getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取关联酒店房型表
	*<p>Title: findRoomsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialRoom> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:32:32
	* @throws
	 */
	public List<HotelPlPreferentialRoom> findRoomsByPreferentialUuid(String preferentialUuid);
}
