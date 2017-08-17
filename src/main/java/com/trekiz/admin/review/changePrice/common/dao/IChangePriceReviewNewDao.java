package com.trekiz.admin.review.changePrice.common.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;

public interface IChangePriceReviewNewDao extends BaseDao<Map<String, Object>> {

	/**
	 * @Description 查询游客对应币种信息
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	List<Map<String,Object>> queryTravelerCurrency(Long orderId, Integer orderType);
	
	Map<String, Object> queryAirticketReviewOrderDetail(String prdOrderId, String prdType);

	Map<String, Object> queryGroupReviewOrderDetail(String prdOrderId);

	Map<String, Object> queryVisaReviewOrderDetail(String prdOrderId);

	Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId);

}
