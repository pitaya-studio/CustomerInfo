package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;

/**
 * 付款支付记录pojo
 * @author zhaohaiming
 * */
public class RefundRecord {

	public String orderUuid;//订单UUid
	public String paymentUuid;//付款UUid
	public String paymentDetailUuid;//付款记录UUID
	public String paymentMethod;//付款方式
	public Double paymentAmount;//付款金额
	public Integer currencyUuid;//币种Id
	public Double exchangeRate;//汇率
	public String paymentDate;//付款日期
	public String financePaymentRecordStatusCode;//付款状态 edit by majiancheng(2015-11-6)
	public List<Dict> attachments;//付款附件
	public String memo;//备注
	
	
	
	public List<Dict> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Dict> attachments) {
		this.attachments = attachments;
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
	public String getPaymentUuid() {
		return paymentUuid;
	}
	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}
	public String getPaymentDetailUuid() {
		return paymentDetailUuid;
	}
	public void setPaymentDetailUuid(String paymentDetailUuid) {
		this.paymentDetailUuid = paymentDetailUuid;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Integer getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getFinancePaymentRecordStatusCode() {
		return financePaymentRecordStatusCode;
	}
	public void setFinancePaymentRecordStatusCode(String financePaymentRecordStatusCode) {
		this.financePaymentRecordStatusCode = financePaymentRecordStatusCode;
	}
	
	
}
