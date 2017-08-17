package com.trekiz.admin.review.depositrefund.service;

import java.util.Map;

import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.persistence.Page;

/**
 * 退款审批处理service
 * 
 * @author chy
 * 
 */
public interface IDepositeRefundReviewNewService {

	Page<Map<String, Object>> queryCostRefundReviewList(Map<String, String> params, Page<Map<String, Object>> page);
	
	Page<Map<String, Object>> queryRefundReviewList(Map<String, Object> prams);

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
	void doDepositeRefund(ReviewNew review);
}
