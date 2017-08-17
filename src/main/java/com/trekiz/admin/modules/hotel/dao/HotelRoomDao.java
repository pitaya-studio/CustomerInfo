/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelRoomDao  extends BaseDao<HotelRoom> {
	/**
	* 根据uuid查询酒店房型信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public HotelRoom getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取酒店房型集合
	*<p>Title: findHotelRoomsByHotelUuid</p>
	* @return List<HotelRoom> 返回类型
	* @author majiancheng
	* @date 2015-7-2 下午9:14:24
	* @throws
	 */
	public List<HotelRoom> findHotelRoomsByHotelUuid(String hotelUuid);
}
