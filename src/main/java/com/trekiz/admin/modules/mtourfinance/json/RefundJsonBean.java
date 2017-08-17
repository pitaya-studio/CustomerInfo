package com.trekiz.admin.modules.mtourfinance.json;

import java.util.Date;
import java.util.List;

/**
 * 付款jsonBean类封装
     * Title: RefundJsonBean.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-21 上午11:49:21
 */
public class RefundJsonBean {
	
	private String orderUuid;//订单Uuid
	private String paymentUuid;//付款Uuid
	private String paymentDetailUuid;//'付款记录Uuid'
	private String fundsType;//款项类型 '（1、借款；2、退款；3、追加成本；4、成本；）'
	private String fundsName;//款项名称
	private String paymentMethodCode;//'付款方式code'
	private String payTypeId;//付款方式Id
	private Integer currencyUuid;//币种
	private Double paymentAmount;//付款金额
	private Double exchangeRate;//汇率
	private String receiveCompany;//'收款单位',
	private String checkNo;//支票号',//只有付款方式是支票时才有效
	private Date checkIssueDate;//'开票日期'//只有付款方式是支票时才有效
	private String paymentBank;//'付款行',//只有付款方式是汇款时才有效
	private String paymentAccount;//'付款账号'.//只有付款方式是汇款时才有效
	private String receiveBank;//'收款行',//只有付款方式是汇款时才有效
	private String receiveAccount;//'收款账户',//只有付款方式是汇款时才有效
	private String memo;//'备注'
	private Date paymentDate;//付款日期
	private String mergePayFlag;//合并支付
	private Integer projectId;//业务表ID、其中包括(review表id、cost_record表id、airticket_order_moneyAmount表id)
	private Integer orderType;//订单类型
	private List<DocInfoJsonBean> attachments;//付款附件
	
	//-----批量付款所用字段信息
	private String paymentObjectUuid;
	private List<PaymentJsonBean> payments;
	//-----批量付款所用字段信息
	
	public String getOrderUuid() {
		return this.orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getPaymentUuid() {
		return this.paymentUuid;
	}
	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}
	public String getPaymentDetailUuid() {
		return this.paymentDetailUuid;
	}
	public void setPaymentDetailUuid(String paymentDetailUuid) {
		this.paymentDetailUuid = paymentDetailUuid;
	}
	public String getFundsType() {
		return this.fundsType;
	}
	public void setFundsType(String fundsType) {
		this.fundsType = fundsType;
	}
	public String getFundsName() {
		return this.fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public String getPaymentMethodCode() {
		return this.paymentMethodCode;
	}
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}
	public String getPayTypeId(){
		return this.payTypeId;
	}
	public void setPayTypeId(String payTypeId){
		this.payTypeId = payTypeId;
	}
	public Integer getCurrencyUuid() {
		return this.currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public Double getPaymentAmount() {
		return this.paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getReceiveCompany() {
		return this.receiveCompany;
	}
	public void setReceiveCompany(String receiveCompany) {
		this.receiveCompany = receiveCompany;
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
	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getPaymentDate() {
		return this.paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getMergePayFlag() {
		return this.mergePayFlag;
	}
	public void setMergePayFlag(String mergePayFlag) {
		this.mergePayFlag = mergePayFlag;
	}
	public Integer getProjectId() {
		return this.projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getOrderType() {
		return this.orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public List<DocInfoJsonBean> getAttachments() {
		return this.attachments;
	}
	public void setAttachments(List<DocInfoJsonBean> attachments) {
		this.attachments = attachments;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getPaymentObjectUuid() {
		return paymentObjectUuid;
	}
	public void setPaymentObjectUuid(String paymentObjectUuid) {
		this.paymentObjectUuid = paymentObjectUuid;
	}
	public List<PaymentJsonBean> getPayments() {
		return payments;
	}
	public void setPayments(List<PaymentJsonBean> payments) {
		this.payments = payments;
	}
	
	
}
