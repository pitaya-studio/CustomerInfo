package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;

/**
 * 收款详情POJO
 * @author 赵海明
 * */
public class PayedDetail {
	public String orderUuid;//'订单Uuid',
	public String receiveUuid;//'收款Uuid'
	public String receiveType;//'收款类别',
	public String receiveMethod;//'收款方式',
	public String receiveAmount;//'收款金额',
	public String currencyUuid;//'收款币种uuid',
	public String payer;//'付款单位',
    public String checkNo;//'支票号',//只有付款方式是支票时才有效
	public String checkIssueDate;//'开票日期'//只有付款方式是支票时才有效
	public String paymentBank;//'付款行',//只有付款方式是汇款时才有效
	public String paymentAccount;//'付款账号'.//只有付款方式是汇款时才有效
	public String receiveBank;//'收款行',//只有付款方式是汇款时才有效
	public String receiveAccount;//'收款账户',//只有付款方式是汇款时才有效
    public List<Dict>attachments;//收款凭证
    public String memo;
    public Integer receivePeopleCount;//人数
    public double exchangeRate;//汇率
    
    
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Integer getReceivePeopleCount() {
		return receivePeopleCount;
	}
	public void setReceivePeopleCount(Integer receivePeopleCount) {
		this.receivePeopleCount = receivePeopleCount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getReceiveUuid() {
		return receiveUuid;
	}
	public void setReceiveUuid(String receiveUuid) {
		this.receiveUuid = receiveUuid;
	}
	public String getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	public String getReceiveMethod() {
		return receiveMethod;
	}
	public void setReceiveMethod(String receiveMethod) {
		this.receiveMethod = receiveMethod;
	}
	public String getReceiveAmount() {
		return receiveAmount;
	}
	public void setReceiveAmount(String receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	public String getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(String currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getCheckIssueDate() {
		return checkIssueDate;
	}
	public void setCheckIssueDate(String checkIssueDate) {
		this.checkIssueDate = checkIssueDate;
	}
	public String getPaymentBank() {
		return paymentBank;
	}
	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}
	public String getPaymentAccount() {
		return paymentAccount;
	}
	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
	public String getReceiveBank() {
		return receiveBank;
	}
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	public List<Dict> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Dict> attachments) {
		this.attachments = attachments;
	}
	
}
