package com.trekiz.admin.modules.mtourOrder.entity;

import java.util.List;
/**
 * 订单列表
 * @author gao
 * @date 2015年10月21日
 */
public class MtourOrderDetail {
	private String groupNo; // 团号
	private String productId; // 产品ID
	private String productName; // 产品名称
	private String orderNum; // 订单no
	private String orderUuid; // 订单Uuid
	private String channelName; // 渠道名称
	private String orderer;		// 下单人
	private String orderStatus; // 订单状态
	private String orderStatusCode;// 订单状态Code
	private String receiveStatus; // 收款状态
	private String receiveStatusCode;//收款状态Code
	
	private String depositUuid; // 定金Uuid
	private String fullPaymentUuid; // 全款Uuid
	private String receivedAmountUuid; // 已收金额Uuid
	private String arrivedAmountUuid; // 到帐金额Uuid
	
	private String orderDateTime; // 下单时间
	private String modifiedDateTime; // 最后修改时间
	private String departureDate; // 出团日期
	private String lockStatus; // 产品表中的成本录入锁（ 0没有锁定, 1锁定）或付款-订单列表中的结算单锁定状态
	private String financePayOrderListPayStatusCode;//订单付款状态code
	private String financePayOrderListPayStatusName;//订单付款状态name
	
	private List<MtourOrderMoney> deposit; // 定金
	private List<MtourOrderMoney> fullPayment; // 全款
	private List<MtourOrderMoney> balancePayment; // 尾款
	private List<MtourOrderMoney> receivedAmount; // 已收金额
	private List<MtourOrderMoney> arrivedAmount; // 到帐金额
	private List<MtourOrderMoney> payableAmount;//应付金额'
	private List<MtourOrderMoney> paidAmount;//已付金额
	private List<MtourOrderAttachment> attachment; // 附件
	
	private MtourOrderSortInfo sortInfo; // 排序相关
	
	public MtourOrderSortInfo getSortInfo() {
		return sortInfo;
	}
	public void setSortInfo(MtourOrderSortInfo sortInfo) {
		this.sortInfo = sortInfo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public List<MtourOrderAttachment> getAttachment() {
		return attachment;
	}
	public void setAttachment(List<MtourOrderAttachment> attachment) {
		this.attachment = attachment;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getOrderer() {
		return orderer;
	}
	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public String getReceiveStatusCode() {
		return receiveStatusCode;
	}
	public void setReceiveStatusCode(String receiveStatusCode) {
		this.receiveStatusCode = receiveStatusCode;
	}
	public List<MtourOrderMoney> getDeposit() {
		return deposit;
	}
	public void setDeposit(List<MtourOrderMoney> deposit) {
		this.deposit = deposit;
	}
	public List<MtourOrderMoney> getFullPayment() {
		return fullPayment;
	}
	public void setFullPayment(List<MtourOrderMoney> fullPayment) {
		this.fullPayment = fullPayment;
	}
	public List<MtourOrderMoney> getBalancePayment() {
		return balancePayment;
	}
	public void setBalancePayment(List<MtourOrderMoney> balancePayment) {
		this.balancePayment = balancePayment;
	}
	public List<MtourOrderMoney> getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(List<MtourOrderMoney> receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public List<MtourOrderMoney> getArrivedAmount() {
		return arrivedAmount;
	}
	public void setArrivedAmount(List<MtourOrderMoney> arrivedAmount) {
		this.arrivedAmount = arrivedAmount;
	}
	public String getDepositUuid() {
		return depositUuid;
	}
	public void setDepositUuid(String depositUuid) {
		this.depositUuid = depositUuid;
	}
	public String getFullPaymentUuid() {
		return fullPaymentUuid;
	}
	public void setFullPaymentUuid(String fullPaymentUuid) {
		this.fullPaymentUuid = fullPaymentUuid;
	}
	public String getReceivedAmountUuid() {
		return receivedAmountUuid;
	}
	public void setReceivedAmountUuid(String receivedAmountUuid) {
		this.receivedAmountUuid = receivedAmountUuid;
	}
	public String getArrivedAmountUuid() {
		return arrivedAmountUuid;
	}
	public void setArrivedAmountUuid(String arrivedAmountUuid) {
		this.arrivedAmountUuid = arrivedAmountUuid;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public String getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(String modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public String getFinancePayOrderListPayStatusCode() {
		return financePayOrderListPayStatusCode;
	}
	public void setFinancePayOrderListPayStatusCode(
			String financePayOrderListPayStatusCode) {
		this.financePayOrderListPayStatusCode = financePayOrderListPayStatusCode;
	}
	public String getFinancePayOrderListPayStatusName() {
		return financePayOrderListPayStatusName;
	}
	public void setFinancePayOrderListPayStatusName(
			String financePayOrderListPayStatusName) {
		this.financePayOrderListPayStatusName = financePayOrderListPayStatusName;
	}
	public List<MtourOrderMoney> getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(List<MtourOrderMoney> payableAmount) {
		this.payableAmount = payableAmount;
	}
	public List<MtourOrderMoney> getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(List<MtourOrderMoney> paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	
}
