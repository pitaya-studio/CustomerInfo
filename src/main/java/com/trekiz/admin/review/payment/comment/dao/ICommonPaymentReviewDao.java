package com.trekiz.admin.review.payment.comment.dao;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.review.payment.comment.entity.PaymentParam;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批模块，成本付款审批对应的Dao接口
 * @author shijun.liu
 * @date 2015年11月17日
 */
public interface ICommonPaymentReviewDao {

	/**
	 * 查询成本付款审核列表数据
	 * @param page			分页对象
	 * @param paymentParam  参数对象
	 * @return
	 * @author shijun.liu
	 * @date 2015年11月17日
	 */
	public Page<Map<String, Object>> getPaymentReviewList(Page<Map<String, Object>> page, PaymentParam paymentParam);
}
