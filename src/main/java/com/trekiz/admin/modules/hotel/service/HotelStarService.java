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

public interface HotelStarService{
	
	public void save (HotelStar hotelStar);
	
	public void update (HotelStar hotelStar);
	
	public HotelStar getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<HotelStar> find(Page<HotelStar> page, HotelStar hotelStar);
	
	public List<HotelStar> find( HotelStar hotelStar);
	
	/**
	* 查询酒店星级中名称是否存在(名称存在返回true，反之返回false)
	* @param id 字典ID
	* @param label 字典Label值
	* @param wholesalerId 供应商ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public boolean findIsExist(String uuid, String label, Long wholesalerId);
	
	/**
	* 根据uuid查询酒店星级信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public HotelStar getByUuid(String uuid);
	
	/**
	* 根据uuid删除酒店星级信息（逻辑删除）
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-8
	*/
	public void removeByUuid(String uuid);
	
}
