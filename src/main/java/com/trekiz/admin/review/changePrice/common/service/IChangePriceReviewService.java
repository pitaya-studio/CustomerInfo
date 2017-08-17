package com.trekiz.admin.review.changePrice.common.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.review.changePrice.singleGroup.service.IChangePriceNewService;

public interface IChangePriceReviewService {
	
	Page<Map<String, Object>> queryChangePriceReviewList(Map<String, Object> params,IChangePriceNewService changePriceService);

	Map<String, Object> queryAirticketorderDeatail(String orderId, String prdType);

	Map<String, Object> queryVisaorderDeatail(String orderId);

	Map<String, Object> querySanPinReviewOrderDetail(String orderId);

	Map<String, Object> queryGrouporderDeatail(String orderId);

	boolean doChangePrice(Map<String, String> params);

	boolean doChangePrice(Review review);

}
