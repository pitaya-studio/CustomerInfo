/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.input.IslandTravelerInput;
import com.trekiz.admin.modules.island.query.IslandTravelerQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandTravelerService{
	
	public void save (IslandTraveler islandTraveler);
	
	public void save (IslandTravelerInput islandTravelerInput);
	
	public void update (IslandTraveler islandTraveler);
	
	public IslandTraveler getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<IslandTraveler> find(Page<IslandTraveler> page, IslandTravelerQuery islandTravelerQuery);
	
	public List<IslandTraveler> find( IslandTravelerQuery islandTravelerQuery);
	
	public IslandTraveler getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	public List<IslandTraveler> findTravelerByOrderUuid(String orderUuid);
	
}
