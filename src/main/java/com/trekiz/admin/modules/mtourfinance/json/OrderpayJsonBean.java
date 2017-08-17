package com.trekiz.admin.modules.mtourfinance.json;

import java.util.Date;
import java.util.List;

/**
 * 订单支付类封装
     * Title: OrderpayJsonBean.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-21 上午11:48:38
 */
public class OrderpayJsonBean {
	private String orderUuid;//订单Uuid
	private String receiveUuid;//收款Uuid
	private String receiveMethodCode;//收款方式
	private String speedyClearance;//'即时结算',//'0':否,'1':是
	private String receiveType;//'收款类别'
	private Double receiveAmount;//收款金额
	private Double exchangeRate;//'汇率'
	private String currencyUuid;//收款币种Uuid
	private String paymentMethodCode;//'付款方式code'
	private String payer;//付款单位
	private String checkNo;//'支票号',//收款方式为支票时有效
	private Date checkIssueDate;//'开票日期',//收款方式为支票时有效
	private String paymentBank;//'付款行Uuid',//收款方式为汇款时有效
	private String paymentAccount;//'付款账号'.//收款方式为汇款时有效
	private String receiveBank;//'收款行Uuid',//收款方式为汇款时有效
	private String receiveAccount;//'收款账户',//收款方式为汇款时有效
	private Date arrivalBankDate;//银行到账日期
	private Integer receivePeopleCount;//收款人数
	private Integer tourOperatorChannelCategoryCode;//区分 地接社/渠道商
	private Integer tourOperatorOrChannelTypeCode;//地接社/渠道商的类型Code
	private String tourOperatorOrChannelName;//地接社/渠道商 的名称
	private String tourOperatorOrChannelUuid;//地接社/渠道商 的Uuid
	private String memo;//'备注'
	private List<DocInfoJsonBean> attachments;//收款凭证
	
	public String getOrderUuid() {
		return this.orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getReceiveUuid() {
		return this.receiveUuid;
	}
	public void setReceiveUuid(String receiveUuid) {
		this.receiveUuid = receiveUuid;
	}
	public String getReceiveMethodCode() {
		return this.receiveMethodCode;
	}
	public void setReceiveMethodCode(String receiveMethodCode) {
		this.receiveMethodCode = receiveMethodCode;
	}
	public String getSpeedyClearance() {
		return this.speedyClearance;
	}
	public void setSpeedyClearance(String speedyClearance) {
		this.speedyClearance = speedyClearance;
	}
	public String getReceiveType() {
		return this.receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	public Double getReceiveAmount() {
		return this.receiveAmount;
	}
	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	public Double getExchangeRate() {
		return this.exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getCurrencyUuid() {
		return this.currencyUuid;
	}
	public void setCurrencyUuid(String currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public String getPaymentMethodCode() {
		return this.paymentMethodCode;
	}
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}
	public String getPayer() {
		return this.payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getCheckNo() {
		return this.checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public Date getCheckIssueDate() {
		return this.checkIssueDate;
	}
	public void setCheckIssueDate(Date checkIssueDate) {
		this.checkIssueDate = checkIssueDate;
	}
	public String getPaymentBank() {
		return this.paymentBank;
	}
	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}
	public String getPaymentAccount() {
		return this.paymentAccount;
	}
	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
	public String getReceiveBank() {
		return this.receiveBank;
	}
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}
	public String getReceiveAccount() {
		return this.receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	public Date getArrivalBankDate() {
		return this.arrivalBankDate;
	}
	public void setArrivalBankDate(Date arrivalBankDate) {
		this.arrivalBankDate = arrivalBankDate;
	}
	public Integer getReceivePeopleCount() {
		return this.receivePeopleCount;
	}
	public void setReceivePeopleCount(Integer receivePeopleCount) {
		this.receivePeopleCount = receivePeopleCount;
	}
	public Integer getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}
	public void setTourOperatorChannelCategoryCode(
			Integer tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}
	public Integer getTourOperatorOrChannelTypeCode() {
		return tourOperatorOrChannelTypeCode;
	}
	public void setTourOperatorOrChannelTypeCode(
			Integer tourOperatorOrChannelTypeCode) {
		this.tourOperatorOrChannelTypeCode = tourOperatorOrChannelTypeCode;
	}
	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}
	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}
	public String getTourOperatorOrChannelUuid() {
		return tourOperatorOrChannelUuid;
	}
	public void setTourOperatorOrChannelUuid(String tourOperatorOrChannelUuid) {
		this.tourOperatorOrChannelUuid = tourOperatorOrChannelUuid;
	}
	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<DocInfoJsonBean> getAttachments() {
		return this.attachments;
	}
	public void setAttachments(List<DocInfoJsonBean> attachments) {
		this.attachments = attachments;
	}
	
	
	
}
