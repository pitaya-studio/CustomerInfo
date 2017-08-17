package com.trekiz.admin.review.payment.comment.entity;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批模块，付款审批的参数对象
 * @author shijun.liu
 * @date 2015年11月18日
 */
public class PaymentParam {

	private String groupCodeProductName;		//团号产品名称
	private Integer orderType;					//订单类型
	private Integer supplyId;					//地接社
	private String applyBeginDate;				//申请日期
	private String applyEndDate;				//申请日期
	private Long reviewerId;					//审批发起人
	private String payMoneyBegin;				//付款金额
	private String payMoneyEnd;					//付款金额
	private String groupOpenDateBegin;			//出团日期
	private String groupOpenDateEnd;			//出团日期
	private Integer reviewStatus;				//审批状态
	private Integer payStatus;					//出纳确认
	private String tabStatus;					//选择tab签，包括：全部，待本人审批，本人已审批，非本人审批
	private Integer agentId;					//渠道ID
	private String paymentType;
	public String getGroupCodeProductName() {
		return groupCodeProductName;
	}
	public void setGroupCodeProductName(String groupCodeProductName) {
		this.groupCodeProductName = groupCodeProductName;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}
	public String getApplyBeginDate() {
		return applyBeginDate;
	}
	public void setApplyBeginDate(String applyBeginDate) {
		this.applyBeginDate = applyBeginDate;
	}
	public String getApplyEndDate() {
		return applyEndDate;
	}
	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}
	public Long getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}
	public String getPayMoneyBegin() {
		return payMoneyBegin;
	}
	public void setPayMoneyBegin(String payMoneyBegin) {
		this.payMoneyBegin = payMoneyBegin;
	}
	public String getPayMoneyEnd() {
		return payMoneyEnd;
	}
	public void setPayMoneyEnd(String payMoneyEnd) {
		this.payMoneyEnd = payMoneyEnd;
	}
	public String getGroupOpenDateBegin() {
		return groupOpenDateBegin;
	}
	public void setGroupOpenDateBegin(String groupOpenDateBegin) {
		this.groupOpenDateBegin = groupOpenDateBegin;
	}
	public String getGroupOpenDateEnd() {
		return groupOpenDateEnd;
	}
	public void setGroupOpenDateEnd(String groupOpenDateEnd) {
		this.groupOpenDateEnd = groupOpenDateEnd;
	}
	public Integer getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
}
