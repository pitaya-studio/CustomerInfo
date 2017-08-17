package com.trekiz.admin.modules.review.changeprice.service;

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
public interface IChangePriceReviewService {

	Page<Map<String, Object>> queryRefundReviewList(HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> queryGrouporderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaorderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询机票订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType);
	
	/**
	 * 根据订单id查询散拼订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId);
	
	/**
	 * 进行改价业务数据处理
	 */
	public boolean doChangePrice(Map<String, String> params);
	
	/**
	 *  供其它地方使用的更改业务数据的接口
	 */
	public boolean doChangePrice(Review review);
	public boolean doHotelChangePrice(Map<String, String> params);
	public boolean doIslandChangePrice(Map<String, String> params);
	
}
