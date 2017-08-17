package com.trekiz.admin.review.prdreturn.airticket.service;

import java.util.Map;

import com.trekiz.admin.modules.reviewflow.entity.Review;

/**
 * 机票退票处理service
 * 
 * @author chy
 * 
 */
public interface IAirTicketReturnNewService {

	Map<String, Object> queryAirTicketReturnInfoById(String orderId);

	/**
	 * 还余位 by chy 2014年12月3日19:51:34
	 * @param request
	 * @param response
	 * @return
	 */
	int returnPosition(String orderId, String travelId);
	
	/**
	 * 退票公共业务方法
	 */
	boolean doReturnPosition(Review review);
}
