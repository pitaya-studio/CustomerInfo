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

public interface HotelPlPreferentialRelHotelService{
	
	public void save (HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel);
	
	public void save (HotelPlPreferentialRelHotelInput hotelPlPreferentialRelHotelInput);
	
	public void update (HotelPlPreferentialRelHotel hotelPlPreferentialRelHotel);
	
	public HotelPlPreferentialRelHotel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialRelHotel> find(Page<HotelPlPreferentialRelHotel> page, HotelPlPreferentialRelHotelQuery hotelPlPreferentialRelHotelQuery);
	
	public List<HotelPlPreferentialRelHotel> find( HotelPlPreferentialRelHotelQuery hotelPlPreferentialRelHotelQuery);
	
	public HotelPlPreferentialRelHotel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取优惠关联酒店信息
	*<p>Title: getRelHotelByPreferentialUuid</p>
	* @return HotelPlPreferentialRelHotel 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:41:13
	* @throws
	 */
	public HotelPlPreferentialRelHotel getRelHotelByPreferentialUuid(String preferentialUuid);
}
