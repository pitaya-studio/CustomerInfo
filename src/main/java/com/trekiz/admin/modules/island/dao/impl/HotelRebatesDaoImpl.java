package com.trekiz.admin.modules.island.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.island.dao.HotelRebatesDao;
import com.trekiz.admin.modules.island.entity.HotelRebates;
@Service
@Transactional(readOnly = true)
public class HotelRebatesDaoImpl  extends BaseDaoImpl<HotelRebates>  implements HotelRebatesDao{

	@Override
	public List<HotelRebates> findHotelRebatesList(Long orderId,
			Integer orderType) {
		List<HotelRebates> rebatesList = Lists.newArrayList();
		rebatesList = super.find("from HotelRebates where orderId = ? and orderType = 11",orderId);
		return rebatesList;
	}

	@Override
	public List<HotelRebates> findHotelRebatesByTravelerAndStatus(
			Long travelerId) {
		List<HotelRebates> rebatesList = Lists.newArrayList();
		rebatesList = super.find("select rebates from HotelRebates rebates,Review review where rebates.review.id = review.id and rebates.review.status=2 and rebates.orderType =11 and rebates.travelerId = ? order by rebates.id desc",travelerId);
		return rebatesList;
	}
}
