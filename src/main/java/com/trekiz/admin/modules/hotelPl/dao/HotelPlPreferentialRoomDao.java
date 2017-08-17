/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import java.util.*;

import com.trekiz.admin.modules.hotelPl.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlPreferentialRoomDao  extends BaseDao<HotelPlPreferentialRoom> {
	
	public HotelPlPreferentialRoom getByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取关联酒店房型表
	*<p>Title: findRoomsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialRoom> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:32:32
	* @throws
	 */
	public List<HotelPlPreferentialRoom> findRoomsByPreferentialUuid(String preferentialUuid);
	
	/**
	 * 根据优惠uuid集合删除关联酒店房型信息
	*<p>Title: deleteByPlPreferentialUuids</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-16 上午10:50:17
	* @throws
	 */
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids);
}
