package com.trekiz.admin.review.changePrice.visa.service;

import java.util.List;

public interface INewVisaChangePriceService {
	/**
	 * 根据订单id查询产品所属部门id
	 * @param orderId
	 * @return
	 */
	List<Object> getDeptIdByOrderId(String orderId);
}
