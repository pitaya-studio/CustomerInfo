package com.trekiz.admin.review.transfersGroup.singleGroup.entity;

public class TransferInfo {

	// 订单号
	private String orderNo;
	// 团号
	private String groupCode;
	// 产品名称
	private String productName;
	// 产品类型
	private String productType;
	// 申请时间
	private String createDate;
	// 审批发起人
	private String createBy;
	// 渠道商
	private String agentName;
	// 游客
	private String travelerName;
	// 原应收金额
	private String oldAmountMoney;
	// 转团后应收金额
	private String newAmountMoney;
	// 上一环节审批人
	private String lastReviewer;
	// 审批状态
	private String status;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getTravelerName() {
		return travelerName;
	}
	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}
	public String getOldAmountMoney() {
		return oldAmountMoney;
	}
	public void setOldAmountMoney(String oldAmountMoney) {
		this.oldAmountMoney = oldAmountMoney;
	}
	public String getNewAmountMoney() {
		return newAmountMoney;
	}
	public void setNewAmountMoney(String newAmountMoney) {
		this.newAmountMoney = newAmountMoney;
	}
	public String getLastReviewer() {
		return lastReviewer;
	}
	public void setLastReviewer(String lastReviewer) {
		this.lastReviewer = lastReviewer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
