/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.input.IslandOrderPriceInput;
import com.trekiz.admin.modules.island.query.IslandOrderPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandOrderPriceService{
	
	public void save (IslandOrderPrice islandOrderPrice);
	
	public void save (IslandOrderPriceInput islandOrderPriceInput);
	
	public void update (IslandOrderPrice islandOrderPrice);
	
	public IslandOrderPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<IslandOrderPrice> find(Page<IslandOrderPrice> page, IslandOrderPriceQuery islandOrderPriceQuery);
	
	public List<IslandOrderPrice> find( IslandOrderPriceQuery islandOrderPriceQuery);
	
	public IslandOrderPrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public IslandOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid);
	
	public List<IslandOrderPrice> getByOrderUuid(String orderUuid);
	/**
	 * 根据订单uuid获取订单价格信息
	 * @param groupUuid
	 * @return
	 */
	public List<IslandOrderPrice> getOrderPriceByOrderUuid(String orderUuid);
}
