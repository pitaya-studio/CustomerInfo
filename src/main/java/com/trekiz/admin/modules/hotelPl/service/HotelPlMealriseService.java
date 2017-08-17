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

public interface HotelPlMealriseService{
	
	public void save (HotelPlMealrise hotelPlMealrise);
	
	public void save (HotelPlMealriseInput hotelPlMealriseInput);
	
	public void update (HotelPlMealrise hotelPlMealrise);
	
	public HotelPlMealrise getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlMealrise> find(Page<HotelPlMealrise> page, HotelPlMealriseQuery hotelPlMealriseQuery);
	
	public List<HotelPlMealrise> find( HotelPlMealriseQuery hotelPlMealriseQuery);
	
	public HotelPlMealrise getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取酒店简单升餐表集合(使用餐型进行分组)
	*<p>Title: findPlMealRisesByHotelPlUuid</p>
	* @return Map<String, List<HotelPlMealrise>> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:56:25
	* @throws
	 */
	public Map<String, Map<String, List<HotelPlMealrise>>> findPlMealRisesByHotelPlUuid(String hotelPlUuid);
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的生餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlMealrise> getHotelPlMealrise4AutoQuotedPrice(HotelPlMealriseQuery hotelPlMealriseQuery);
	
}
