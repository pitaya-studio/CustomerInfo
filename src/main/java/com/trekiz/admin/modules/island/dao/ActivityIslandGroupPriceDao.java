/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityIslandGroupPriceDao  extends BaseDao<ActivityIslandGroupPrice> {
	
	public ActivityIslandGroupPrice getByUuid(String uuid);
	
	/**
	 * 根据团期uuid获取团期价格集合
	*<p>Title: getPriceListByGroupUuid</p>
	* @return List<ActivityIslandGroupPrice> 返回类型
	* @author majiancheng
	* @date 2015-5-27 下午6:25:08
	* @throws
	 */
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid);

	public List<ActivityIslandGroupPrice> getLowPriceListByUuid(
			String activityIslandUuid);

	public int updateByActivityIslandGroupUUid(String islandGroupUuid);
	
	/**
	 * 根据团期字段信息（1、海岛游团期uuid,2、游客类型uuid，3、舱位等级）
	*<p>Title: getGroupPriceByGroupInfo</p>
	* @return ActivityIslandGroupPrice 返回类型
	* @author majiancheng
	* @date 2015-6-30 下午6:20:11
	* @throws
	 */
	public ActivityIslandGroupPrice getGroupPriceByGroupInfo(String activityIslandGroupUuid, String travelerTypeUuid, String spaceLevel);
	
	public List<ActivityIslandGroupPrice> getGroupPriceByAirlineUuid(String activityIslandGroupAirlineUuid);
}
