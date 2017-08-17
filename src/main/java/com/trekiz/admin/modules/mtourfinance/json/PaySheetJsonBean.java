package com.trekiz.admin.modules.mtourfinance.json;

import com.alibaba.fastjson.annotation.JSONField;


public class PaySheetJsonBean {
	private String paymentUuid;  // '付款Uuid'
	private String printNo; //订单流水号
	private String applicantDate;  // '日期',
	private String groupNo;  // '团号',
	private String invoiceOriginalTypeCode;  // 'PNR或者地接社的的代码',//大编号(票源类型)------------
	@JSONField(name="PNR")
	private String pnr;  // 'PNR编号',//大编号为PNR的时候才有意义---------------
	private String tourOperatorName; // '地接社Name'//大编号为地接社的时候才有意义---------------
	private String tourOperatorOrChannelName; // /'支付对象名称',
	private String purpose; // '用途',
	private String totalRMB; // '计人民币',
	private String totalRMB_CN; // '计人民币(大写)',
	private String totalOther; //'外币'
	private String totalOther_CN; //'外币(大写)'
	private String applicant; // '申请人'
	private String paymentPeople;//付款人

	public String getPaymentUuid() {
		return paymentUuid;
	}

	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}
	
	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public String getApplicantDate() {
		return applicantDate;
	}

	public void setApplicantDate(String applicantDate) {
		this.applicantDate = applicantDate;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getInvoiceOriginalTypeCode() {
		return invoiceOriginalTypeCode;
	}

	public void setInvoiceOriginalTypeCode(String invoiceOriginalTypeCode) {
		this.invoiceOriginalTypeCode = invoiceOriginalTypeCode;
	}

	public String getTourOperatorName() {
		return tourOperatorName;
	}

	public void setTourOperatorName(String tourOperatorName) {
		this.tourOperatorName = tourOperatorName;
	}


	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}
	
	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}

	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getTotalRMB() {
		return totalRMB;
	}

	public void setTotalRMB(String totalRMB) {
		this.totalRMB = totalRMB;
	}

	public String getTotalRMB_CN() {
		return totalRMB_CN;
	}

	public void setTotalRMB_CN(String totalRMB_CN) {
		this.totalRMB_CN = totalRMB_CN;
	}

	public String getTotalOther() {
		return totalOther;
	}

	public void setTotalOther(String totalOther) {
		this.totalOther = totalOther;
	}

	public String getTotalOther_CN() {
		return totalOther_CN;
	}

	public void setTotalOther_CN(String totalOther_CN) {
		this.totalOther_CN = totalOther_CN;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getPaymentPeople() {
		return paymentPeople;
	}

	public void setPaymentPeople(String paymentPeople) {
		this.paymentPeople = paymentPeople;
	}

}
