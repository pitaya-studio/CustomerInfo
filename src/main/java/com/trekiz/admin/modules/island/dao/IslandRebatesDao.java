package com.trekiz.admin.modules.island.dao;

import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.IslandRebates;

public interface IslandRebatesDao extends BaseDao<IslandRebates>{

	public List<IslandRebates>findRebatesList(Long orderId, Integer orderType);
	public List<IslandRebates> findRebatesByTravelerAndStatus(Long travelerId);
	
}
