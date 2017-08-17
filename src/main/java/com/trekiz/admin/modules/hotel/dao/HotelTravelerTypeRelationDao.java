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
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelTravelerTypeRelationDao  extends BaseDao<HotelTravelerTypeRelation> {
	
	public HotelTravelerTypeRelation getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取所有的酒店游客类型关联信息集合
	*<p>Title: getByHotelUuid</p>
	* @return List<HotelTravelerTypeRelation> 返回类型
	* @author majiancheng
	* @date 2015-8-13 下午7:58:04
	* @throws
	 */
	public List<HotelTravelerTypeRelation> getByHotelUuid(String hotelUuid);
	
	public void deleteByHotelUuid(String hotelUuid);
	
	/**
	 * 根据酒店uuid获取游客类型
	*<p>Title: getTravelerTypesByHotelUuid</p>
	* @return List<TravelerType> 返回类型
	* @author majiancheng
	* @date 2015-8-17 上午10:09:35
	* @throws
	 */
	public List<TravelerType> getTravelerTypesByHotelUuid(String hotelUuid);
}
