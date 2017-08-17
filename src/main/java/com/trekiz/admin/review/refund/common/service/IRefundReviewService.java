package com.trekiz.admin.review.refund.common.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;

@Service
public interface IRefundReviewService {

	/**
	 * 查询退款审批列表
	 * @param params
	 * @return
	 */
	Page<Map<String, Object>> queryRefundReviewListNew(Map<String, Object> params);
	
	/**
	 * 根据订单id查询机票订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType);
	
	/**
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaorderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询散拼订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId);
	
	/**
	 * 根据订单id 查询单团类订单详情
	 * @param prdOrderId
	 * @return
	 */
	Map<String, Object> queryGrouporderDeatail(String prdOrderId);
}
