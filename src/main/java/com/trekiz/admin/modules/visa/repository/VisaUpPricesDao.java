package com.trekiz.admin.modules.visa.repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * 签证 申请改价
 * @author HPT
 *
 */
public interface VisaUpPricesDao {
	/**
	 *  查询流程表里的签证记录
	 * @param flowId
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> queryVisaUpPricesList(String flowId,String orderId);
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
	 * 查询团队签证费
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> queryVisaMoney(String orderId);
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
