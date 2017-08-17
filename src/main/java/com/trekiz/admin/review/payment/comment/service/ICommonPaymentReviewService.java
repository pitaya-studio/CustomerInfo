package com.trekiz.admin.review.payment.comment.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.review.payment.comment.entity.PaymentParam;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批模块，成本付款审批对应的Service接口
 * @author shijun.liu
 * @date 2015年11月17日
 */
public interface ICommonPaymentReviewService {

	/**
	 * 查询成本付款审批列表数据
	 * @param page			分页对象
	 * @param paymentParam  参数对象
	 * @return
	 * @author shijun.liu
	 * @date 2015年11月17日
	 */
	public Page<Map<String, Object>> getPaymentReviewList(Page<Map<String, Object>> page, PaymentParam paymentParam);
	
	/**
	 * 审批通过接口
	 * @param reviewId			付款审批UUID
	 * @param comment			审批备注
	 * @author shijun.liu
	 */
	public void batchApprove(String reviewId, String comment);
	
	/**
	 * 审批驳回接口
	 * @param reviewId			付款审批UUID
	 * @param comment			驳回备注
	 * @author shijun.liu
	 */
	public void batchReject(String reviewId, String comment);
	
	/**
	 * 撤销审批流程
	 * @param reviewId	新审批表的ID
	 * @param comment	撤销备注
	 * @author shijun.liu
	 */
	public void backReview(String reviewId, String comment);
	
	/**
	 * 取消审批流程
	 * @param reviewId	新审批表的ID
	 * @param comment	撤销备注
	 * @author shijun.liu
	 */
	public void cancelReview(String reviewId, String comment);
}
