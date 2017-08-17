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

public interface HotelGuestTypeService{
	
	public void save (HotelGuestType hotelGuestType);
	
	public void update (HotelGuestType hotelGuestType);
	
	public HotelGuestType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelGuestType> find(Page<HotelGuestType> page, HotelGuestType hotelGuestType);
	
	public List<HotelGuestType> find( HotelGuestType hotelGuestType);
	
	public HotelGuestType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	* 查询住客类型中名称是否存在(名称存在返回true，反之返回false)
	* @param id 字典ID
	* @param name 住客类型名称
	* @param wholesalerId 供应商ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public boolean findIsExist(String uuid, String name, Long wholesalerId);
	/**
	 * 获取批发商下所有的住客类型
	*<p>Title: findByWholesalerId</p>
	* @return List<HotelGuestType> 返回类型
	* @author majiancheng
	* @date 2015-7-3 下午6:20:55
	* @throws
	 */
	public List<HotelGuestType> findByWholesalerId(Integer wholesalerId);
	/**
	* 查询住客类型中名称是否存在(名称存在返回true，反之返回false)
	* @param uuid 
	* @param sysGuestType
	* @param wholesalerId 供应商ID
	* @return 
	* @author wangXK
	* @Time 2015-8-17
	*/
	public boolean findIsExistBySysGuestType(String uuid, String sysGuestType, int companyId); 
	
	/**
	 * 查询批发商下 第N人类型 的游客类型简写和批发商住客类型UUID的集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @param shortNameParams
	 * @return List<HotelGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，简写来源系统游客类型的简写
	 * 			
	 */
	public List<HotelGuestType> findShortNameAndGuestTypeUuidList(Integer wholesalerId,List<String> shortNameParams);
	
	/**
	 * 根据批发商将酒店住客类型sort进行累加操作
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午12:02:17
	 */
	public boolean cumulationSortByWholesalerId(Integer wholesalerId);
	
}
