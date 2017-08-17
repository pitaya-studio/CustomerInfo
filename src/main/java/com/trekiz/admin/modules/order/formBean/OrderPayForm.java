package com.trekiz.admin.modules.order.formBean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.trekiz.admin.modules.order.pojo.OrderPayInput;

public class OrderPayForm {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 调用模块传入的参数部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** json串 */
	private String orderPayInputJson;
	private OrderPayInput orderPayInput;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 页面部分输入部分值
	 */
	//131需求新加款项 
	private Integer payPriceType;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	private Integer payType;/* 支付类型，快速支付，支票，现金支付，汇款 */
	private String payTypeName;/* 支付类型名称 */
	private Integer paymentStatus = 1;/* 结算(1 即时结算 2 按月结算 3 担保结算 4 后续费) */
	private String mergePayFlag;
	private String[] currencyIdPrice;
	private String[] dqzfprice;

	private String[] convertLowest;
	private String mergeCurrencyId;
	private String mergeCurrencyPrice;
	private String remarks;
	private Long[] DocInfoIds;
	// 汇款时需要的信息
	private String bankName;/* 开户行bankId */
	private String realBankName;/* 开户行bankId */
	private String bankAccount;/* 开户行账户 */
	private String realBankAccount;/* 开户行账户 */
	private String toBankNname;/* 转入行bankId */
	private String toBankAccount;/* 转入行bankId */
	// 快速支付时需要填写的支付方式
	private String fastPayType;
	// 支票支付需要的信息
	private String payerName; /* 付款单位 */
	private String checkNumber;/* 支票号 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date invoiceDate;/* 开票日期 */
	
	//俄风行收款和支付时需要的信息（多个收款方式会共用同一个属性）
	private String fromCompanyName;/* 来款单位 */
	private String fromBankName;/* 来款行名称 */
	private String fromAccount;/* 来款账户 */
	private String receiveBankName;/* 收款行名称 */
	private String receiveAccount;/* 收款账户 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date draftAccountedDate;/* 汇票到期日 */
	private String payBankName;/* 汇款行名称 */
	private String payAccount;/* 汇款账户 */
	private String receiveCompanyName;/* 收款单位 */
	private java.lang.String drawerName;/* 出票人名称 */
	private java.lang.String drawerAccount;/* 出票人帐号 */
	
	private java.lang.String[] feeName;//手续费名称
	private java.lang.String[] feeCurrencyId;//手续费id
	private java.lang.String[] feeAmount;//手续费金额
	
	private String relationInvoiceIds; // 0444关联发票的ids
	
	/** 收款单位 */
	private String payee;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 页面输出项目
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// 页面显示的消息提示
	private String msg;
	private String finalForwardSuccessPage;
	private String finalForwardErrorPage;
	
	//224需求增加支付宝名称（来款）、支付宝账号（来款）、支付宝名称（收款）、支付宝账号（收款）、收款单位
	   /**支付宝账户（来款）*/
    private String fromAlipayName;
    /**支付宝账号（来款）*/
    private String fromAlipayAccount;
    /**支付宝账户（收款）*/
    private String toAlipayName;
    /**支付宝账号（收款）*/
    private String toAlipayAccount;
    /**收款单位*/
    private String comeOfficeName;
    /**支付宝账户（收款）*/
    private String toAlipayName1;
    /**支付宝账号（收款）*/
    private String toAlipayAccount1;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	public String getToAlipayName1() {
		return toAlipayName1;
	}

	public void setToAlipayName1(String toAlipayName1) {
		this.toAlipayName1 = toAlipayName1;
	}

	public String getToAlipayAccount1() {
		return toAlipayAccount1;
	}

	public void setToAlipayAccount1(String toAlipayAccount1) {
		this.toAlipayAccount1 = toAlipayAccount1;
	}

	public String getFromAlipayName() {
		return fromAlipayName;
	}

	public void setFromAlipayName(String fromAlipayName) {
		this.fromAlipayName = fromAlipayName;
	}

	public String getFromAlipayAccount() {
		return fromAlipayAccount;
	}

	public void setFromAlipayAccount(String fromAlipayAccount) {
		this.fromAlipayAccount = fromAlipayAccount;
	}

	public String getToAlipayName() {
		return toAlipayName;
	}

	public void setToAlipayName(String toAlipayName) {
		this.toAlipayName = toAlipayName;
	}

	public String getToAlipayAccount() {
		return toAlipayAccount;
	}

	public void setToAlipayAccount(String toAlipayAccount) {
		this.toAlipayAccount = toAlipayAccount;
	}

	public String getComeOfficeName() {
		return comeOfficeName;
	}

	public void setComeOfficeName(String comeOfficeName) {
		this.comeOfficeName = comeOfficeName;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPaymentStatus() {
		if (paymentStatus == null) {
			paymentStatus = 1;
		}
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	

	public String[] getCurrencyIdPrice() {
		return currencyIdPrice;
	}

	public void setCurrencyIdPrice(String[] currencyIdPrice) {
		this.currencyIdPrice = currencyIdPrice;
	}
	
	public String getRelationInvoiceIds() {
		return relationInvoiceIds;
	}

	public void setRelationInvoiceIds(String relationInvoiceIds) {
		this.relationInvoiceIds = relationInvoiceIds;
	}

	public String[] getDqzfprice() {
		return dqzfprice;
	}

	public void setDqzfprice(String[] dqzfprice) {
		this.dqzfprice = dqzfprice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long[] getDocInfoIds() {
		return DocInfoIds;
	}

	public void setDocInfoIds(Long[] docInfoIds) {
		DocInfoIds = docInfoIds;
	}

	public String getFastPayType() {
		return fastPayType;
	}

	public void setFastPayType(String fastPayType) {
		this.fastPayType = fastPayType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getToBankNname() {
		return toBankNname;
	}

	public void setToBankNname(String toBankNname) {
		this.toBankNname = toBankNname;
	}

	public String getToBankAccount() {
		return toBankAccount;
	}

	public void setToBankAccount(String toBankAccount) {
		this.toBankAccount = toBankAccount;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getOrderPayInputJson() {
		return orderPayInputJson;
	}

	public void setOrderPayInputJson(String orderPayInputJson) {
		this.orderPayInputJson = orderPayInputJson;
	}

	public OrderPayInput getOrderPayInput() {
		return orderPayInput;
	}

	public void setOrderPayInput(OrderPayInput orderPayInput) {
		this.orderPayInput = orderPayInput;
	}

	public String getFinalForwardSuccessPage() {
		return finalForwardSuccessPage;
	}

	public void setFinalForwardSuccessPage(String finalForwardSuccessPage) {
		this.finalForwardSuccessPage = finalForwardSuccessPage;
	}

	public String getFinalForwardErrorPage() {
		return finalForwardErrorPage;
	}

	public void setFinalForwardErrorPage(String finalForwardErrorPage) {
		this.finalForwardErrorPage = finalForwardErrorPage;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMergePayFlag() {
		return mergePayFlag;
	}

	public void setMergePayFlag(String mergePayFlag) {
		this.mergePayFlag = mergePayFlag;
	}

	public String[] getConvertLowest() {
		return convertLowest;
	}

	public void setConvertLowest(String[] convertLowest) {
		this.convertLowest = convertLowest;
	}

	public String getMergeCurrencyId() {
		return mergeCurrencyId;
	}

	public void setMergeCurrencyId(String mergeCurrencyId) {
		this.mergeCurrencyId = mergeCurrencyId;
	}

	public String getMergeCurrencyPrice() {
		return mergeCurrencyPrice;
	}

	public void setMergeCurrencyPrice(String mergeCurrencyPrice) {
		this.mergeCurrencyPrice = mergeCurrencyPrice;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	public String getFromCompanyName() {
		return fromCompanyName;
	}

	public void setFromCompanyName(String fromCompanyName) {
		this.fromCompanyName = fromCompanyName;
	}

	public String getFromBankName() {
		return fromBankName;
	}

	public void setFromBankName(String fromBankName) {
		this.fromBankName = fromBankName;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getReceiveBankName() {
		return receiveBankName;
	}

	public void setReceiveBankName(String receiveBankName) {
		this.receiveBankName = receiveBankName;
	}

	public String getReceiveAccount() {
		return receiveAccount;
	}

	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	public Date getDraftAccountedDate() {
		return draftAccountedDate;
	}

	public void setDraftAccountedDate(Date draftAccountedDate) {
		this.draftAccountedDate = draftAccountedDate;
	}
	

	public String getPayBankName() {
		return payBankName;
	}

	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getReceiveCompanyName() {
		return receiveCompanyName;
	}

	public void setReceiveCompanyName(String receiveCompanyName) {
		this.receiveCompanyName = receiveCompanyName;
	}

	public java.lang.String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(java.lang.String drawerName) {
		this.drawerName = drawerName;
	}

	public java.lang.String getDrawerAccount() {
		return drawerAccount;
	}

	public void setDrawerAccount(java.lang.String drawerAccount) {
		this.drawerAccount = drawerAccount;
	}

	public java.lang.String[] getFeeName() {
		return feeName;
	}

	public void setFeeName(java.lang.String[] feeName) {
		this.feeName = feeName;
	}

	public java.lang.String[] getFeeCurrencyId() {
		return feeCurrencyId;
	}

	public void setFeeCurrencyId(java.lang.String[] feeCurrencyId) {
		this.feeCurrencyId = feeCurrencyId;
	}

	public java.lang.String[] getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(java.lang.String[] feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Integer getPayPriceType() {
		return payPriceType;
	}

	public void setPayPriceType(Integer payPriceType) {
		this.payPriceType = payPriceType;
	}

	public String getRealBankName() {
		return realBankName;
	}

	public void setRealBankName(String realBankName) {
		this.realBankName = realBankName;
	}

	public String getRealBankAccount() {
		return realBankAccount;
	}

	public void setRealBankAccount(String realBankAccount) {
		this.realBankAccount = realBankAccount;
	}
	
}
