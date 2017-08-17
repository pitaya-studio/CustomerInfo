package com.trekiz.admin.modules.order.pojo;

import java.util.Date;
import java.util.List;

import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.sys.entity.DocInfo;

public class PayInfoDetail {
	/** 主键ID */
	private String id;
	/** 支付方式（现金、支票、汇款等） */
	private Integer payType;
	/** 支付方式（现金、支票、汇款等） */
	private String payTypeName;
	/** 支付方式表ID */
	private String payTypeId;
	/** 付款金额UUID */
	private String moneySerialNum;
	/** 拼接后的金额 */
	private String moneyDispStyle;
	/** 是否合并支付（0：不合并;1：合并） */
	private String mergePayFlag;
	/** 款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款） */
	private Integer moneyType;
	/** 记录状态 */
	private String status;
	/** 业务表ID */
	private Long recordId;
	/** 合并后的总金额UUID */
	private String mergeMoneySerialNum;
	/** 支付凭证附件id */
	private String payVoucher;
	private List<DocInfo> docInfoList;
	/** 备注信息 */
	private String remarks;
	/** 创建时间（付款的创建时间） */
	private Date createDate;

	// //////////////////////////////////////////////////////////////
	/** 付款单位 */
	private String payerName;
	/** 支票号 */
	private String checkNumber;
	/** 开票日期 */
	private Date invoiceDate;

	// //////////////////////////////////////////////////////////////
	/** 开户行名称 */
	private String bankName;
	/** 开户行账户 */
	private String bankAccount;
	/** 转入行名称 */
	private String tobankName;
	/** 转入行账户 */
	private String tobankAccount;
	/**出票人名称*/
	private String drawerName;
    /**汇票到期日*/
	private String draftAccountedDate;
	/**出票人账号*/
	private String drawerAccount;
	/**付款行全称*/
	private String payBankName;
	/**确认付款时间*/
	private Date updateDate;
	/** 付款手续费集合 */
	private List<PayFee> payFees;
	/** 已付金额显示（不包含手续费） */
	private String refundDispStyle;
	/** 已付金额转为RMB显示*/
	private String refundRMBDispStyle;

	// /////////////////////////////// 支付宝结算字段 /////////////////////////////////
	// 来款支付宝名称
	private String fromAlipayName;
	// 来款支付宝账号
	private String fromAlipayAccount;
	// 收款单位
	private String receiveCompanyName;
	// 收款支付宝名称
	private String toAlipayName;
	// 收款支付宝账号
	private String toAlipayAccount;
	
	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public String getDraftAccountedDate() {
		return draftAccountedDate;
	}

	public void setDraftAccountedDate(String draftAccountedDate) {
		this.draftAccountedDate = draftAccountedDate;
	}

	public String getDrawerAccount() {
		return drawerAccount;
	}

	public void setDrawerAccount(String drawerAccount) {
		this.drawerAccount = drawerAccount;
	}

	public String getPayBankName() {
		return payBankName;
	}

	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getTobankName() {
		return tobankName;
	}

	public void setTobankName(String tobankName) {
		this.tobankName = tobankName;
	}

	public String getTobankAccount() {
		return tobankAccount;
	}

	public void setTobankAccount(String tobankAccount) {
		this.tobankAccount = tobankAccount;
	}

	public String getMoneyDispStyle() {
		return moneyDispStyle;
	}

	public void setMoneyDispStyle(String moneyDispStyle) {
		this.moneyDispStyle = moneyDispStyle;
	}

	public List<DocInfo> getDocInfoList() {
		return docInfoList;
	}

	public void setDocInfoList(List<DocInfo> docInfoList) {
		this.docInfoList = docInfoList;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMergePayFlag() {
		return mergePayFlag;
	}

	public void setMergePayFlag(String mergePayFlag) {
		this.mergePayFlag = mergePayFlag;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getMergeMoneySerialNum() {
		return mergeMoneySerialNum;
	}

	public void setMergeMoneySerialNum(String mergeMoneySerialNum) {
		this.mergeMoneySerialNum = mergeMoneySerialNum;
	}

	@Override
	public String toString() {
		return "PayInfoDetail [id=" + id + ", payType=" + payType
				+ ", payTypeName=" + payTypeName + ", payTypeId=" + payTypeId
				+ ", moneySerialNum=" + moneySerialNum + ", moneyDispStyle="
				+ moneyDispStyle + ", payVoucher=" + payVoucher
				+ ", docInfoList=" + docInfoList + ", remarks=" + remarks
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + ", payerName=" + payerName
				+ ", checkNumber=" + checkNumber + ", invoiceDate="
				+ invoiceDate + ", bankName=" + bankName + ", bankAccount="
				+ bankAccount + ", tobankName=" + tobankName
				+ ", tobankAccount=" + tobankAccount + "]";
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<PayFee> getPayFees() {
		return payFees;
	}

	public void setPayFees(List<PayFee> payFees) {
		this.payFees = payFees;
	}

	public String getRefundDispStyle() {
		return refundDispStyle;
	}

	public void setRefundDispStyle(String refundDispStyle) {
		this.refundDispStyle = refundDispStyle;
	}

	public String getRefundRMBDispStyle() {
		return refundRMBDispStyle;
	}

	public void setRefundRMBDispStyle(String refundRMBDispStyle) {
		this.refundRMBDispStyle = refundRMBDispStyle;
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

	public String getReceiveCompanyName() {
		return receiveCompanyName;
	}

	public void setReceiveCompanyName(String receiveCompanyName) {
		this.receiveCompanyName = receiveCompanyName;
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
}
