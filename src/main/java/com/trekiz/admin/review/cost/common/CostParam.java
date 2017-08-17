package com.trekiz.admin.review.cost.common;

public class CostParam {

	private String groupCode; // 团号
	private Integer productType; // 产品类型
	private Integer supplyId; // 地接社id
	private Integer agentId; // 渠道商id
	private String createDateStart; // 申请日期--开始
	private String createDateEnd; // 申请日期--结束
	private Integer createBy; // 审批发起人
	private Integer status; // 审批状态
	private String priceStart; // 成本金额--开始
	private String priceEnd; // 成本金额--结束
	private String groupOpenDateStart; // 出团日期--开始
	private String groupOpenDateEnd; // 出团日期--结束
	private Integer reviewer; // 审批人
	private String paymentType;//渠道结算方式

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(String priceStart) {
		this.priceStart = priceStart;
	}

	public String getPriceEnd() {
		return priceEnd;
	}

	public void setPriceEnd(String priceEnd) {
		this.priceEnd = priceEnd;
	}

	public String getGroupOpenDateStart() {
		return groupOpenDateStart;
	}

	public void setGroupOpenDateStart(String groupOpenDateStart) {
		this.groupOpenDateStart = groupOpenDateStart;
	}

	public String getGroupOpenDateEnd() {
		return groupOpenDateEnd;
	}

	public void setGroupOpenDateEnd(String groupOpenDateEnd) {
		this.groupOpenDateEnd = groupOpenDateEnd;
	}

	public Integer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Integer reviewer) {
		this.reviewer = reviewer;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	
}
