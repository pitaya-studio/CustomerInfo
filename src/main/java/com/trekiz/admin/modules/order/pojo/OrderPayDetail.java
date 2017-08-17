package com.trekiz.admin.modules.order.pojo;

public class OrderPayDetail {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 调用模块传入的参数部分（必须提供部分）
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 应付的币种ID（币种间用逗号分隔） 例:1,2 */
	private String payCurrencyId;
	/** 应付的支付金额（币种间用逗号分隔）例：100,200 */
	private String payCurrencyPrice;
	/** 总金额的币种ID（币种间用逗号分隔） */
	private String totalCurrencyId;
	/** 总金额的支付金额（币种间用逗号分隔） */
	private String totalCurrencyPrice;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 由调用模块传入的参数部分（各模块可能用到的参数，非必须提供）
	 */
	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 订单号ID */
	private Long orderId;
	/** 订单号Uuid */
	private String orderUuid;
	/** 审批表ID */
	private Long reviewId;
	/** 游客ID */
	private Long travelerId;
	/** 项目编号（成本记录ID） */
	private Long projectId;
	/** 团期ID(单团使用团期ID，机票、签证暂用产品ID) */
	private Integer groupId;
	/** 款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款） */
	private Integer refundMoneyType;
	/** 订单号 */
	private String orderNum;
	/** 订单类型 */
	private Integer orderType;
	/** 付款金额（目前主要用于团期支付） */
	private String payPrice;
	/** 付款方式类型 */
	private Integer payPriceType;
	/** 业务类型(1表示订单，2表示游客,3表示表示询价报价，4表示团期)---money_amount表中使用 */
	private Integer busindessType;
	/** 团期成本ID */
	private Integer costRecordId;
	/** 游客uuid */
	private String travelerUuid;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 支付模块操作后保存的参数部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 支付ID(orderpay表中的ID) */
	private Long orderPayId;
	/** 支付ID(pay表中的ID) */
	private String payId;
	/** 支付ID(refund表中的ID) */
	private String refundId;
	/** 支付ID(pay_group表中的ID) */
	private String payGroupUuid;
	/** 支付类型UUID */
	private String payTypeId;
	/** 支付类型名称 */
	private String payTypeName;
	/** 付款金额UUID */
	private String moneySerialNum;
	/** 支付凭证附件id */
	private String payVoucher;

	/** 支付表uuid(支付（产品）表中的uuid) */
	private String payProductOrderUuid;

	/** 订单支付批量号 
	 * add by jiachen*/
	private String orderPaySerialNum;


	public Long getOrderPayId() {
		return orderPayId;
	}

	public void setOrderPayId(Long orderPayId) {
		this.orderPayId = orderPayId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	
	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getPayCurrencyId() {
		return payCurrencyId;
	}

	public void setPayCurrencyId(String payCurrencyId) {
		this.payCurrencyId = payCurrencyId;
	}

	public String getPayCurrencyPrice() {
		return payCurrencyPrice;
	}

	public void setPayCurrencyPrice(String payCurrencyPrice) {
		this.payCurrencyPrice = payCurrencyPrice;
	}

	public String getTotalCurrencyId() {
		return totalCurrencyId;
	}

	public void setTotalCurrencyId(String totalCurrencyId) {
		this.totalCurrencyId = totalCurrencyId;
	}

	public String getTotalCurrencyPrice() {
		return totalCurrencyPrice;
	}

	public void setTotalCurrencyPrice(String totalCurrencyPrice) {
		this.totalCurrencyPrice = totalCurrencyPrice;
	}

	public String getMoneySerialNum() {
		return moneySerialNum;
	}

	public void setMoneySerialNum(String moneySerialNum) {
		this.moneySerialNum = moneySerialNum;
	}

	public String getPayVoucher() {
		return payVoucher;
	}

	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	public String getPayGroupUuid(){
		return payGroupUuid;
	}
	
	public void setPayGroupUuid(String payGroupUuid) {
		this.payGroupUuid = payGroupUuid;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public Integer getGroupId() {
		return this.groupId;
	}
	
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getPayPriceType() {
		return payPriceType;
	}

	public void setPayPriceType(Integer payPriceType) {
		this.payPriceType = payPriceType;
	}

	public Integer getBusindessType() {
		return busindessType;
	}

	public void setBusindessType(Integer busindessType) {
		this.busindessType = busindessType;
	}
	
	public Integer getCostRecordId() {
		return costRecordId;
	}

	public void setCostRecordId(Integer costRecordId) {
		this.costRecordId = costRecordId;
	}
	
	public String getTravelerUuid() {
		return this.travelerUuid;
	}
	
	public void setTravelerUuid(String travelerUuid) {
		this.travelerUuid = travelerUuid;
	}

	public Integer getRefundMoneyType() {
		return refundMoneyType;
	}

	public void setRefundMoneyType(Integer refundMoneyType) {
		this.refundMoneyType = refundMoneyType;
	}
	
	public String getOrderPaySerialNum() {
		return orderPaySerialNum;
	}


	public String getPayProductOrderUuid() {
		return payProductOrderUuid;
	}

	public void setPayProductOrderUuid(String payProductOrderUuid) {
		this.payProductOrderUuid = payProductOrderUuid;
	}


	public void setOrderPaySerialNum(String orderPaySerialNum) {
		this.orderPaySerialNum = orderPaySerialNum;
	}


	@Override
	public String toString() {
		return "OrderPayDetail [payCurrencyId=" + payCurrencyId
				+ ", payCurrencyPrice=" + payCurrencyPrice
				+ ", totalCurrencyId=" + totalCurrencyId
				+ ", totalCurrencyPrice=" + totalCurrencyPrice + ", orderId="
				+ orderId + ", reviewId=" + reviewId + ", travelerId="
				+ travelerId + ", projectId=" + projectId + ", orderNum="
				+ orderNum + ", orderType=" + orderType + ", orderPayId="
				+ orderPayId + ", payId=" + payId + ", refundId=" + refundId
				+ ", payTypeId=" + payTypeId + ", payTypeName=" + payTypeName
				+ ", moneySerialNum=" + moneySerialNum + ", payVoucher="
				+ payVoucher + "]";
	}

}
