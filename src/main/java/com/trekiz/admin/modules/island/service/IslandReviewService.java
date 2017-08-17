package com.trekiz.admin.modules.island.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandRebates;

public interface IslandReviewService {

	public String saveBorrowing(HttpServletRequest request);
	public List<Map<String, Object>> getBorrowingTravelerByOrderUuid(String orderUuid);
	public List<IslandRebates>findRebatesList(Long orderId, Integer orderType);
	public List<IslandRebates> findRebatesByTravelerAndStatus(Long travelerId);
	public List<Map<String, Object>> getHotelBorrowingTravelerByOrderUuid(String orderUuid);
	public String saveHotelBorrowing(HttpServletRequest request);
	public List<HotelRebates>findHotelRebatesList(Long orderId, Integer orderType);
	public List<HotelRebates> findHotelRebatesByTravelerAndStatus(Long travelerId);
	public Map<String,Object> getIslandAllTypeReviewByOrderUuid(String orderUuid);
}
