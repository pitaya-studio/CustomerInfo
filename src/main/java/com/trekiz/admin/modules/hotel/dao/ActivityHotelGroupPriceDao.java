/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityHotelGroupPriceDao  extends BaseDao<ActivityHotelGroupPrice> {
	
	public ActivityHotelGroupPrice getByUuid(String uuid);

	public List<ActivityHotelGroupPrice> getPriceListByGroupUuid(String uuid);
	
	/**
	 * 根据团期uuid和游客类型获取酒店团期价格
	*<p>Title: getGroupPriceByGroupInfo</p>
	* @return ActivityHotelGroupPrice 返回类型
	* @author majiancheng
	* @date 2015-6-30 下午8:06:14
	* @throws
	 */
	public ActivityHotelGroupPrice getGroupPriceByGroupInfo(String groupUuid, String type);

}
