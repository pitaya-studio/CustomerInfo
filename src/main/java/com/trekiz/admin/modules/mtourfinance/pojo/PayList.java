package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;

/**
 * 付款列表vo
 * */
public class PayList {

	
	public String orderUuid;//'订单Uuid',
    public String paymentUuid;//'付款Uuid',
    public String approvalDate;//'报批日期',
    public String productNo;//'产品号',
    public String applicantName;//申请人姓名

    //只有成本付款的需要pnr
    public String invoiceOriginalTypeCode;//'PNR或者地接社的的代码',//大编号(票源类型)
    public String PNR;//'PNR编号',//大编号为PNR的时候才有意义
    public String tourOperatorUuid;//'地接社Uuid',//大编号为地接社的时候才有意义
    public String tourOperatorName;//'地接社Name',//大编号为地接社的时候才有意义
    public String groupNo;//'团号',
    public String productName;//'产品名称',
    public String departureDate;//'出团日期',
    public String fundsName;//'款项名称',
    public String fundsType;//'款项类型',
    public String tourOperatorOrChannelName;//'付款对象名称',
    public String applicant;//'申请人姓名',
    public List<MoneyAmountVO> payableAmount;//[//应付金额'
    public List<MoneyAmountVO> paidAmount;//[//已付金额
    public String paymentStatus;//'付款状态'
    public String modifiedDateTime;//最后修改时间
    public String orderNum;
    public ChannelType channelType; //渠道
    
    public List<MoneyAmountVO> payAmount;//成本单价
    
    
    
	public List<MoneyAmountVO> getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(List<MoneyAmountVO> payAmount) {
		this.payAmount = payAmount;
	}
	public ChannelType getChannelType() {
		return channelType;
	}
	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(String modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
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
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getInvoiceOriginalTypeCode() {
		return invoiceOriginalTypeCode;
	}
	public void setInvoiceOriginalTypeCode(String invoiceOriginalTypeCode) {
		this.invoiceOriginalTypeCode = invoiceOriginalTypeCode;
	}
	public String getPNR() {
		return PNR;
	}
	public void setPNR(String pNR) {
		PNR = pNR;
	}
	public String getTourOperatorUuid() {
		return tourOperatorUuid;
	}
	public void setTourOperatorUuid(String tourOperatorUuid) {
		this.tourOperatorUuid = tourOperatorUuid;
	}
	public String getTourOperatorName() {
		return tourOperatorName;
	}
	public void setTourOperatorName(String tourOperatorName) {
		this.tourOperatorName = tourOperatorName;
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
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getFundsName() {
		return fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public String getFundsType() {
		return fundsType;
	}
	public void setFundsType(String fundsType) {
		this.fundsType = fundsType;
	}
	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}
	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public List<MoneyAmountVO> getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(List<MoneyAmountVO> payableAmount) {
		this.payableAmount = payableAmount;
	}
	public List<MoneyAmountVO> getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(List<MoneyAmountVO> paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
    
    
    
}
