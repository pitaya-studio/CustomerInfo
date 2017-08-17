/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.visa.entity.VisaOrder;

/**
 * @author junhao.zhao
 * 2017年1月9日  上午10:12:32
 */
public interface OrderDateSaveOrUpdateService {

	/**为了向表order_data_statistics中添加数据，需要根据visaOrderId从四张表查询UUID()、product_name、company_uuid、amount
	 * @param visaOrder
	 * @return
	 */
	public void insertVisaOrder(VisaOrder visaOrder);
	
	/**visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额、支付状态修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public int updatePeopleAndMoney(Long orderId, Integer orderType);
	
	/**productorder订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public int updatePeopleAndMoneyPro(Long orderId, Integer orderType);
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对visa_order
	 * @param visaOrderId
	 * @param orderType
	 * @return
	 */
	public List<Map<String, Object>> getOrderDataStaIdForVisa(Long visaOrderId, Integer orderType);

	/**通过传来的参数更改表order_data_statistics订单金额与金额uuid
	 * @param id
	 * @param orderTypeQz
	 * @param visaPay
	 * @param payPriceSerialNum
	 */
	public int updateMoneyAndUuid(Long id, Integer orderTypeQz, BigDecimal visaPay, String payPriceSerialNum);
	
	
	/**向表order_data_statistics中添加数据,或修改数据
	 * @param productOrder
	 */
	public void addOrderDataStatistics(ProductOrderCommon productOrder);

	/**visa_order订单删除之后，修改表order_data_statistics中对应的订单状态
	 * @param id
	 * @param orderTypeQz
	 */
	public void updateDelFlag(Long id, Integer orderTypeQz);
	
	
	/**判断是否向表order_data_statistics中加数据，或修改数据
	 * @param id
	 * @param orderTypeQz
	 */
	public boolean whetherAddOrUpdate(ProductOrderCommon order);
}
