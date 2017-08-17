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

public interface HotelRoomService{
	
	public void save (HotelRoom hotelRoom);
	
	public void update (HotelRoom hotelRoom);
	
	public HotelRoom getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<HotelRoom> find(Page<HotelRoom> page, HotelRoom hotelRoom);
	
	public List<HotelRoom> find( HotelRoom hotelRoom);
	
	/**
	* 根据uuid查询酒店房型信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public HotelRoom getByUuid(String uuid);
	
	/**
	* 根据uuid删除酒店房型信息（逻辑删除）
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-8
	*/
	public void removeByUuid(String uuid);
	
	/**
	* 查询酒店房型中名称是否存在(名称存在返回true，反之返回false)
	* @param id 字典ID
	* @param roomName 房型名称
	* @param companyId 公司ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public boolean findIsExist(String hotelUuid, String uuid, String roomName, Long companyId);
	
	/**
	 * 保存酒店房型和容住率表数据
		* 
		* @param hotelRoom 酒店房型
		* @param occupancyRates 容住率
		* @param occupancys 可住人数
		* @return void
		* @author majiancheng
		* @Time 2015-5-4
	 */
	public void saveHotelRoom (HotelRoom hotelRoom, String occupancyRates);
	
	/**
	 * 更新酒店房型和容住率表数据
		* 
		* @param hotelRoom 酒店房型
		* @param occupancyRates 酒店容住率拼接字符串
		* @param roomOccuRateUuids 酒店容住率uuids
		* @return void
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public void updateHotelRoom(HotelRoom hotelRoom, String occupancyRates, String roomOccuRateUuids);
}
