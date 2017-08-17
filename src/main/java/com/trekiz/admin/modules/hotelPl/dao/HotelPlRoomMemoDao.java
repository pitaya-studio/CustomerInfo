/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelPl.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlRoomMemoDao  extends BaseDao<HotelPlRoomMemo> {
	
	public HotelPlRoomMemo getByUuid(String uuid);
	
	/**
	 *  根据酒店价单uuid获取房型备注信息
	*<p>Title: getPlRoomMemoByHotelPlUuid</p>
	* @return HotelPlRoomMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:15:59
	* @throws
	 */
	public List<HotelPlRoomMemo> findPlRoomMemosByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据酒店价单uuid删除酒店价格备注
	*<p>Title: deleteHotelPlMemoByHotelPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:14:17
	* @throws
	 */
	public int deleteHotelPlRoomMemoByHotelPlUuid(String hotelPlUuid);
}
