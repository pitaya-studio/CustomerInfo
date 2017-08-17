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

public interface HotelPlRoomMemoService{
	
	public void save (HotelPlRoomMemo hotelPlRoomMemo);
	
	public void save (HotelPlRoomMemoInput hotelPlRoomMemoInput);
	
	public void update (HotelPlRoomMemo hotelPlRoomMemo);
	
	public HotelPlRoomMemo getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlRoomMemo> find(Page<HotelPlRoomMemo> page, HotelPlRoomMemoQuery hotelPlRoomMemoQuery);
	
	public List<HotelPlRoomMemo> find( HotelPlRoomMemoQuery hotelPlRoomMemoQuery);
	
	public HotelPlRoomMemo getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 *  根据酒店价单uuid获取房型备注信息
	*<p>Title: getPlRoomMemoByHotelPlUuid</p>
	* @return HotelPlRoomMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:15:59
	* @throws
	 */
	public List<HotelPlRoomMemo> findPlRoomMemosByHotelPlUuid(String hotelPlUuid);
}
