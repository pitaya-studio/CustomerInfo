/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;

/**
 * @author junhao.zhao
 * 2017年1月5日  下午8:53:32
 */
public interface OrderDateSaveOrUpdateDao extends BaseDao<OrderDataStatistics>{
	
	/**为了向表order_data_statistics中添加数据，需要根据productOrderId从四张表查询UUID()、product_name、company_uuid、amount.
	 * @param productOrderId
	 * @return
	 */
	public List<Map<String, Object>> getAddDate(Long productOrderId);
	
	
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对productorder
	 * @param productOrderId
	 * @return
	 */
	public List<Map<String, Object>> getOrderDataStatisticsId(Long productOrderId);
	
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对visa_order
	 * @param visaOrderId
	 * @param orderType
	 * @return
	 */
	public List<Map<String, Object>> getOrderDataStaIdForVisa(Long visaOrderId, Integer orderType);
	
	
	/**表productorder订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param id
	 * @param orderPersonNum
	 * @param amount
	 * @param orderStatus 
	 */
	public int updateForProductorder(Long id, Integer orderPersonNum, BigDecimal amount, Integer orderStatus);

	/**取消订单，将表order_data_statistics中的order_status改为99(针对productOrder表中的取消)
	 * @param productOrderId
	 * @param orderType
	 * @return
	 */
	public int updateOrderStatus(Long productOrderId, String orderType);

	/**激活或删除订单，表order_data_statistics中order_status改为激活或删除(针对productOrder表中的删除与激活)
	 * @param id
	 * @param payStatus
	 */
	public int updateOrderStatusInvoke(Long id, Integer payStatus);

	/**为了向表order_data_statistics中添加数据，需要根据visaOrderId从四张表查询UUID()、product_name、company_uuid、amount
	 * @param visaOrderId
	 * @return
	 */
	public List<Map<String, Object>> getVisaAddDate(Long visaOrderId);
	
	/**为了向表order_data_statistics中添加数据，需要根据airticket_order从四张表查询UUID()、product_name、company_uuid、amount
	 * @param airticketOrderId
	 * @return
	 */
	public List<Map<String, Object>> getAirticketAddDate(Long airticketOrderId);

	/**取消订单，将表order_data_statistics中的order_status改为取消、删除、激活(针对visa_order表的取消、激活与airticket_order表中的取消、删除、激活)
	 * @param orderId
	 * @param orderStatus
	 * @param orderType
	 * @return
	 */
	public int updateOrderStatus(Long orderId, Integer orderStatus, Integer orderType);

	/**visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
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

	/**airticket_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param totalAmount
	 * @param personNum
	 * @param orderId
	 * @param orderStatusAirTicket
	 * @return
	 */
	public int updateForAirticket(BigDecimal totalAmount, int personNum, Long orderId, String orderStatusAirTicket);



	/**通过传来的参数更改表order_data_statistics订单金额与金额uuid
	 * @param id
	 * @param orderTypeQz
	 * @param visaPay
	 * @param payPriceSerialNum
	 */
	public int updateMoneyAndUuid(Long id, Integer orderTypeQz, BigDecimal visaPay, String payPriceSerialNum);



	/**visa_order订单删除之后，修改表order_data_statistics中对应的订单状态
	 * @param id
	 * @param orderTypeQz
	 */
	public void updateDelFlag(Long id, Integer orderTypeQz);

}
