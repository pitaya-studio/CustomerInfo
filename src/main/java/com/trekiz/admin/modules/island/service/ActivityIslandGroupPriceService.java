/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupPriceInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupPriceService{
	
	public void save (ActivityIslandGroupPrice activityIslandGroupPrice);
	
	public void save (ActivityIslandGroupPriceInput activityIslandGroupPriceInput);
	
	public void update (ActivityIslandGroupPrice activityIslandGroupPrice);
	
	public ActivityIslandGroupPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroupPrice> find(Page<ActivityIslandGroupPrice> page, ActivityIslandGroupPriceQuery activityIslandGroupPriceQuery);
	
	public List<ActivityIslandGroupPrice> find( ActivityIslandGroupPriceQuery activityIslandGroupPriceQuery);
	
	public ActivityIslandGroupPrice getByUuid(String uuid);
	
	public List<ActivityIslandGroupPrice> getByactivityIslandGroupUuid(String activityIslandGroupUuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据团期uuid获取团期价格集合
	*<p>Title: getPriceListByGroupUuid</p>
	* @return List<ActivityIslandGroupPrice> 返回类型
	* @author majiancheng
	* @date 2015-5-27 下午6:25:08
	* @throws
	 */
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid);
	public List<ActivityIslandGroupPrice> getPriceListByGroupUuid(String groupUuid,String hotelUuid);
	/**
	 * 根据团期uuid,酒店uuid查询ActivityIslandGroupPrice
	 * @author star
	 * @param groupUuid
	 * @param hotelUuid
	 * @return
	 */
	public List<ActivityIslandGroupPrice> getPriceFilterTravel(String groupUuid,String hotelUuid);
}
