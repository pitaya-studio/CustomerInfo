/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;
import com.trekiz.admin.modules.island.input.IslandTravelervisaInput;
import com.trekiz.admin.modules.island.query.IslandTravelervisaQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandTravelervisaService{
	
	public void save (IslandTravelervisa islandTravelervisa);
	
	public void save (IslandTravelervisaInput islandTravelervisaInput);
	
	public void update (IslandTravelervisa islandTravelervisa);
	
	public IslandTravelervisa getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<IslandTravelervisa> find(Page<IslandTravelervisa> page, IslandTravelervisaQuery islandTravelervisaQuery);
	
	public List<IslandTravelervisa> find( IslandTravelervisaQuery islandTravelervisaQuery);
	
	public IslandTravelervisa getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
