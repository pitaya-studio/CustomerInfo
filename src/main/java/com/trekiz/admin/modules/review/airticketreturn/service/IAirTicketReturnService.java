package com.trekiz.admin.modules.review.airticketreturn.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.entity.Review;

/**
 * 机票退票处理service
 * 
 * @author chy
 * 
 */
public interface IAirTicketReturnService {

	Map<String, Object> queryAirTicketReturnInfoById(String orderId);

	Page<Map<String, Object>> queryAirticketReturnReviewInfo(HttpServletRequest request, HttpServletResponse response);
	
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
