package com.trekiz.admin.modules.activity.repository;

import java.util.List;
import java.util.Map;
/**
 * 单团、散拼订单 改价
 * @author HPT
 *
 */
public interface IActivityUpPricesDao {
	
	/**
	 *  查询流程表里的单团、散拼订单改价记录
	 * @param flowId
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> queryAirTicketUpPricesList(String flowId,String orderId);
	/**
	 * 查询该订单下的游客列表
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryTravelerUpPrices(String orderId);
	/**
	 * 查询该订单下的游客币种信息
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryTravelerCurrency(String orderId);
	/**
	 * 查询订单定金
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryFrontMoney(String orderId);
	/**
	 * 查询原始应收价
	 * @param orderId
	 * @param serialNum
	 * @return
	 */
	public List<Map<String,Object>> queryOriginalMoney(String orderId,String serialNum);
	/**
	 * 查询订单的当前应收金额
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryOrderReceivable(String orderId);
	/**
	 * 查询改价申请的流程
	 */
	@SuppressWarnings("rawtypes")
    public List<Map> queryReviewDetials(String orderId, String productType,String flowType);
	public List<Map<String, Object>> queryHotelTravelerUpPrices(String orderUuid);
	public List<Map<String, Object>> queryHotelTravelerCurrency(String orderUuid);
	public List<Map<String,Object>> queryHotelOriginalMoney(String orderUuid,String serialNum);
	
	public List<Map<String, Object>> queryIslandTravelerUpPrices(String orderUuid);
	public List<Map<String, Object>> queryIslandTravelerCurrency(String orderUuid);
	public List<Map<String,Object>> queryIslandOriginalMoney(String orderUuid,String serialNum);
}
