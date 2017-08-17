package com.trekiz.admin.review.payment.airticket.service;


/**
 * 机票产品付款申请的Service
 * @author shijun.liu
 *
 */
public interface IAirticketPaymentReviewService {

	/**
	 * 机票产品付款申请功能
	 * @param item	需要提交付款申请的成本项，格式如下：productId,orderType,costrecorduuid
	 * @return
	 * @author shijun.liu
	 */
	public void apply(String item);
}
