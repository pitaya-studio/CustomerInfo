/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelGuestTypeRelationDao  extends BaseDao<HotelGuestTypeRelation> {
	
	public HotelGuestTypeRelation getByUuid(String uuid);
	
	/**
	 * 根据酒店房型uuid获取所有的住客类型关联信息
	*<p>Title: getByHotelRoomUuid</p>
	* @return List<HotelGuestTypeRelation> 返回类型
	* @author majiancheng
	* @date 2015-8-12 下午2:31:19
	* @throws
	 */
	public List<HotelGuestTypeRelation> getByHotelRoomUuid(String roomUuid);
	
	/**
	 * 根据酒店房型uuid获取住客类型集合
	*<p>Title: getHotelGuestTypesByRoomUuid</p>
	* @return List<HotelGuestType> 返回类型
	* @author majiancheng
	* @date 2015-8-18 下午5:00:18
	* @throws
	 */
	public List<HotelGuestType> getHotelGuestTypesByRoomUuid(String roomUuid);
}
