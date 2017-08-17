package com.trekiz.admin.modules.island.dao;

import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.HotelRebates;

public interface HotelRebatesDao extends BaseDao<HotelRebates>{

	public List<HotelRebates>findHotelRebatesList(Long orderId, Integer orderType);
	public List<HotelRebates> findHotelRebatesByTravelerAndStatus(Long travelerId);
}
