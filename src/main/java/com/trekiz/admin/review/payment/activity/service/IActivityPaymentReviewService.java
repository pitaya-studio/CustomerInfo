package com.trekiz.admin.review.payment.activity.service;


/**
 * 单团类产品付款申请的Service
 * @author shijun.liu
 *
 */
public interface IActivityPaymentReviewService {

	/**
	 * 单团类产品付款申请功能
	 * @param item	需要提交付款申请的成本项，格式如下：activityGroupId,orderType,costrecorduuid
	 * @return
	 * @author shijun.liu
	 */
	public void apply(String item);
}
