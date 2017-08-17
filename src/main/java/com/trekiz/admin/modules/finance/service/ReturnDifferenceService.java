package com.trekiz.admin.modules.finance.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;

public interface ReturnDifferenceService {
	/**
	 * 保存差额
	 * @param returnDifference
	 */
	public void saveDifference(ReturnDifference returnDifference);
	/**
	 * 根据uuid查询returnDifference
	 * @param uuid
	 * @return
	 */
	public ReturnDifference getReturnDifferenceByUuid(String uuid);
	
	/**
	 * 更改达账状态
	 * @param returnDifference
	 */
	public void updateToAccountType(ReturnDifference returnDifference);
	
	/**
	 * 查询差额明细列表
	 * @param page
	 * @param input
	 * @param agentId
	 * @param agentContactId
	 * @param payType
	 * @return
	 */
	public Page<Map<String,Object>> getReturnPriceDetail(Page<Map<String,Object>> page,String input,String agentId,String agentContactId,String payType);
	
	/**
	 * 更改差额状态
	 * @param productOrderCommon
	 */
	public void updateDifferencePayStatus(ProductOrderCommon productOrderCommon);
	
	/**
	 * 根据订单id获得差额
	 * @param orderId
	 * @return
	 */
	public ReturnDifference getDifferenceByOrderId(Integer orderId);
	
	/**
	 * 根据订单id获得差额(达账)
	 * @param orderId
	 * @return
	 */
	public ReturnDifference getDifferenceSumAccountByOrderId(Integer orderId);
	
	/**
	 * 根据订单id获得订单总额(减去差额)
	 * @param orderId
	 * @return
	 */
	public String getOrderTotalMoneyStrByOrderId(Long orderId);
	
	/**
	 * 根据订单id获得订单各种金额(减去差额)
	 * @param orderId
	 * @return
	 */
	public Map<String,Object> getOrderMoneyByOrderId(Map<String,Object> refundMap);
	
	/**
	 * 根据orderpay 的serialNum的获得差额
	 * @param serialNum
	 * @return
	 */
	public Map<String,Object> getReturnDifferenceBySerialNum(String serialNum);
}
