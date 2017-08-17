package com.trekiz.admin.review.common.service;

import java.util.Date;
import java.util.List;

import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 新审批公共Service接口
 * @author shijun.liu
 * @date 2015年11月30日
 */
public interface ICommonReviewService {

	/**
	 * 新审批确认付款，取消付款公共方法
	 * @param reviewId		新审批ID(review_new 表的ID)
	 * @param status		0：未付，1：已付
	 * @param payConfirmDate 出纳确认时间，如果为空，就设为当前时间。yudong.xu
	 * @author shijun.liu
	 * @date  2015.11.30
	 */
	public void confimOrCancelPay(String reviewId, String status, Date payConfirmDate);
	
	/**
	 * 判断当前订单是否锁定结算单 add by zhanghao
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @return true未锁定，可以发起申请；false锁定，不能发起申请。
	 */
	public boolean checkApplyStart(String orderId,Integer productType,Integer flowType);
	
	/**
	 * 返佣或退款审核通过或驳回之后对成本操作
	 * @param 新审核类实体
	 * @param 审核状态
	 * @author yakun.bai
	 * @Date 2015-12-12
	 */
	public void updateCostRecordStatus(ReviewNew review, Integer result);
	
	/**
	 * 查询新审批预算成本的审核日志
	 * @param activityId	产品或者团期ID
	 * @param orderType		订单类型
	 * @param costId		成本项ID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	public List<CostPaymentReviewNewLog> getBudgetReviewLog(Long activityId, Integer orderType, Long costId);
	
	/**
	 * 查询新审批实际成本的审核日志
	 * @param activityId	产品或者团期ID
	 * @param orderType		订单类型
	 * @param costId		成本项ID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	public List<CostPaymentReviewNewLog> getActualReviewLog(Long activityId, Integer orderType, Long costId);
	
	/**
	 * 查询新审批付款审批的审核日志
	 * @param activityId	产品或者团期ID
	 * @param orderType		订单类型
	 * @param costId		成本项ID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	public List<CostPaymentReviewNewLog> getPaymentReviewLog(Long activityId, Integer orderType, Long costId);
	/**
	 * 针对奢华之旅   成本付款和退款付款的发票状态的修改
	 * @param reviewId   审批的ID
	 * @param status     发票的状态
	 * @author jinxin.gao
	 * @date 2016.3.25
	 */
	public void confimOrCancelInvoice(String reviewId, String status);
	/**
	 * 针对奢华之旅   成本付款和退款付款的发票状态的批量修改
	 * @param reviewId   审批的ID
	 * @author jinxin.gao
	 * @date 2016.3.25
	 */
	public void confimOrCancelInvoiceAll(String reviewId);
	
}
