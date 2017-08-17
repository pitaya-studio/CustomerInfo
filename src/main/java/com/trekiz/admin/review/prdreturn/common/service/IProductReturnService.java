package com.trekiz.admin.review.prdreturn.common.service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;


public interface IProductReturnService {

	Page<Map<String, Object>> queryReturnReviewList(Map<String, Object> params);

	/**
	 * 还余位 by chy 2015年11月16日10:08:59
	 * @param request
	 * @param response
	 * @return
	 */
	int returnPosition(String orderId, String travelId);

	/**
	 * 查询机票订单详情
	 * @param orderId
	 * @return
	 */
	Map<String, Object> queryAirTicketReturnInfoById(String orderId);

	/**
	 * 退团审核通过之后，更改游客状态，金钱、订单和余位
	 *
	 * @throws Exception
	 * @author yunpeng.zhang
	 * @createDate 2015年11月25日
	 */
	void handleFreePositionAndTraveler(String travelerIdStr, ProductOrderCommon productOrder, String reviewId, HttpServletRequest request) throws Exception;
	
	/**
	 * 退团驳回后，更改游客状态
	 * 
	 * @param travelerDelflagNormal
	 * @param parseLong
	 * @throws Exception
	 * @author yunpeng.zhang
	 * @createDate 2015年12月23日10:22:27
	 */
	void updateTravelerStatus(Integer status, long travelerId) throws Exception;

}
