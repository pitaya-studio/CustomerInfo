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

public interface HotelPlIslandwayMemoService{
	
	public void save (HotelPlIslandwayMemo hotelPlIslandwayMemo);
	
	public void save (HotelPlIslandwayMemoInput hotelPlIslandwayMemoInput);
	
	public void update (HotelPlIslandwayMemo hotelPlIslandwayMemo);
	
	public HotelPlIslandwayMemo getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlIslandwayMemo> find(Page<HotelPlIslandwayMemo> page, HotelPlIslandwayMemoQuery hotelPlIslandwayMemoQuery);
	
	public List<HotelPlIslandwayMemo> find( HotelPlIslandwayMemoQuery hotelPlIslandwayMemoQuery);
	
	public HotelPlIslandwayMemo getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取上岛方式备注
	*<p>Title: getPlIslandwayMemoByHotelPlUuid</p>
	* @return HotelPlIslandwayMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:18:53
	* @throws
	 */
	public List<HotelPlIslandwayMemo> findPlIslandwayMemosByHotelPlUuid(String hotelPlUuid);
}
