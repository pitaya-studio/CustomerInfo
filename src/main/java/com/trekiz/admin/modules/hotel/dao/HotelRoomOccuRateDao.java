/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import java.util.*;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelRoomOccuRateDao  extends BaseDao<HotelRoomOccuRate> {
	
	public HotelRoomOccuRate getByUuid(String uuid);
	
	/**
	 * 根据hotelRoomUuid获取酒店容住率表数据
		* 
		* @param hotelRoomUuid 酒店房型UUID
		* @return List<HotelRoomOccuRate>
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public List<HotelRoomOccuRate> getByHotelRoomUuid(String hotelRoomUuid);
	
	
}
