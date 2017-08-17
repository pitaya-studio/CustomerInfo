package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;

public interface IActivityUpPricesService {
	/**
	 * 查询该订单下的游客列表
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryTravelerList(String orderId);
	
	/**
	 * 查询订单定金
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryFrontMoneyList(String orderId);
	/**
	 * 查询订单的当前应收金额
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryOrderReceivable(String orderId);
	/**
	 *  验证游客的改价申请流程是否有申请记录
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param currencyid 
	 * @param travelerId
	 * @return 是/否 (false/true)
	 */
	public boolean verifyTravelerFlowsign(String orderId,String productType,String flowType,String currencyid,String travelerId);
	/**
	 *  验证定金的改价申请流程是否有申请记录
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param currencyid 
	 * @return 是/否 (false/true)
	 */
	public boolean verifyfrontMoneyFlowsign(String orderId,String productType,String flowType,String currencyid);
	/**
	 * 
	 * queryTravelerUpPrices	查询游客列表
	 * @param orderId
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	public List<Map<String, Object>> queryTravelerUpPrices(String orderId);
	
	public List<Map<String, Object>> queryHotelTravelerList(String orderUuid);
	public List<Map<String, Object>> queryIslandTravelerList(String orderUuid);
	
}
