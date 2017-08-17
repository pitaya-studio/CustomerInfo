package com.trekiz.admin.modules.review.depositereview.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.entity.Review;

/**
 * 退款审批处理service
 * 
 * @author chy
 * 
 */
public interface IDepositeRefundReviewService {

	Page<Map<String, Object>> queryCostRefundReviewList(Map<String, String> params, Page<Map<String, Object>> page);
	
	Page<Map<String, Object>> queryRefundReviewList(HttpServletRequest request,
			HttpServletResponse response);

//	Map<String, Object> queryGrouporderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaorderDeatail(String prdOrderId);
	
	/**
	 * 出纳确认
	 */
	void cashConfirm(String travelerId);
	
	/**
	 * 退签证押金 公共接口
	 */
	void doDepositeRefund(Review review);
}
