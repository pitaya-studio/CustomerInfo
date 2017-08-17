package com.trekiz.admin.review.airticketChange.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface INewAirticketChangeService {

	//检查能否改签
	public List<Map<String, Object>> newAreaGaiQianCheck(@SuppressWarnings("rawtypes") Map map) ;
	
	/**
	 * by sy 二〇一五年十二月五日 15:35:57
	 * 审批流程优化审批通过
	 * @param reviewId
	 * @throws Exception
	 */
	void planeReviewNew(String reviewId)throws Exception;
	
	/**
	 * @Description 退票和改签审批通过后共同调用方法
	 * @author yakun.bai
	 * @Date 2016-2-18
	 */
	Integer changeOrExit(String reviewId, String orderId, String travelId) throws Exception;


	/**
	 * 审批页面游客信息组装
	 * by songyang 二〇一五年十二月八日 17:46:00
	 * @param request
	 * @param response
	 * @param reviewId
	 * @param orderId
	 * @return
	 */
	
	Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId,String orderId);
}
