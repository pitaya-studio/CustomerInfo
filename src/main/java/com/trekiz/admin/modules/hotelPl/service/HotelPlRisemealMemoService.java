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

public interface HotelPlRisemealMemoService{
	
	public void save (HotelPlRisemealMemo hotelPlRisemealMemo);
	
	public void save (HotelPlRisemealMemoInput hotelPlRisemealMemoInput);
	
	public void update (HotelPlRisemealMemo hotelPlRisemealMemo);
	
	public HotelPlRisemealMemo getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlRisemealMemo> find(Page<HotelPlRisemealMemo> page, HotelPlRisemealMemoQuery hotelPlRisemealMemoQuery);
	
	public List<HotelPlRisemealMemo> find( HotelPlRisemealMemoQuery hotelPlRisemealMemoQuery);
	
	public HotelPlRisemealMemo getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取升餐备注信息
	*<p>Title: findPlRisemealMemoByHotelPlUuid</p>
	* @return HotelPlRisemealMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:23:04
	* @throws
	 */
	public List<HotelPlRisemealMemo> findPlRisemealMemosByHotelPlUuid(String hotelPlUuid);
}
