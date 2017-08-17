package com.trekiz.admin.modules.island.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.island.dao.IslandRebatesDao;
import com.trekiz.admin.modules.island.entity.IslandRebates;
@Service
@Transactional(readOnly = true)
public class IslandRebatesDaoImpl extends BaseDaoImpl<IslandRebates>  implements IslandRebatesDao{

	@Override
	public List<IslandRebates> findRebatesList(Long orderId, Integer orderType) {
		List<IslandRebates> rebatesList = Lists.newArrayList();
		rebatesList = super.find("from IslandRebates where orderId = ? and orderType = 12",orderId);
		return rebatesList;
	}

	@Override
	public List<IslandRebates> findRebatesByTravelerAndStatus(Long travelerId) {
		List<IslandRebates> rebatesList = Lists.newArrayList();
		rebatesList = super.find("select rebates from IslandRebates rebates,Review review where rebates.review.id = review.id and rebates.review.status=2 and rebates.orderType =12 and rebates.travelerId = ? order by rebates.id desc",travelerId);
		return rebatesList;
	}	

}
