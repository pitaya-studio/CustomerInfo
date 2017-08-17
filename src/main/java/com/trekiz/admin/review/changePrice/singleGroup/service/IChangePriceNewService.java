package com.trekiz.admin.review.changePrice.singleGroup.service;

import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.traveler.entity.Traveler;

import java.util.List;
import java.util.Map;

public interface IChangePriceNewService {
	
	/**
	 * @Description 获取游客列表：只查询正常游客（包括退团审核中和转团审核中游客）
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	List<Traveler> queryTravelerList(Long orderId, Integer orderType);
	
	/**
	 * @Description 游客改价申请
	 * @param param 申请参数
	 * @param orderId 订单ID
	 * @param productType 产品/订单类型
	 * @author yakun.bai
	 * @Date 2015-12-15
	 */
	Map<String, String>  travelerForApply(Map<String, Object> param, Long orderId, Integer productType) throws Exception;

	Map<String, String>  travelerForApply(String travelerArr, Long orderId, Integer productType) throws Exception;

	List<Map<String, Object>> getReviewList(Long orderId, Integer productType);

	Map<String, Object> getReviewDetail(String reviewUuid);

	void handlerMap(Map<String, Object> map, ProductOrderCommon productOrder);

}
