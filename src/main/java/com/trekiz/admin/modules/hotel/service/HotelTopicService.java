/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelTopicService{
	
	public void save (HotelTopic hotelTopic);
	
	public void update (HotelTopic hotelTopic);
	
	public HotelTopic getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelTopic> find(Page<HotelTopic> page, HotelTopic hotelTopic);
	
	public List<HotelTopic> find( HotelTopic hotelTopic);
	
	/**
	 * 根据uuid查询酒店主题信息
		* 
		* @param uuid
		* @return HotelTopic
		* @author majiancheng
		* @Time 2015-4-13
	 */
	public HotelTopic getByUuid(String uuid);
	
	/**
	 * 根据字典视图UUID和公司ID查询酒店主题信息
		* 
		* @param 
		* @return HotelTopic
		* @author majiancheng
		* @Time 2015-4-13
	 */
	public HotelTopic findByViewUuidAndCompany(String uuid, Integer companyId);
}
