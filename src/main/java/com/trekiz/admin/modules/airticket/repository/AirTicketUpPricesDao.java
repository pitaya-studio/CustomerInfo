package com.trekiz.admin.modules.airticket.repository;

import java.util.List;
import java.util.Map;
/**
 * 飞机票 改价
 * @author HPT
 *
 */
public interface AirTicketUpPricesDao {
	
	/**
	 *  查询流程表里的机票改价记录
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
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public List<Map> queryReviewDetials (String orderId,String productType,String flowType);
}
