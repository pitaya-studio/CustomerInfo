package com.trekiz.admin.modules.finance.param;

/**
 * 收款确认筛选项集合对象
 * @author wangyang
 * @date 2016.8.24
 * */
public class ReceiveConfirmParams extends ParamEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 银行到账日期开始*/
	private String accountDateBegin;
	/** 银行到账日期结束*/
	private String accountDateEnd;
	/** 下单人*/
	private String creator;
	/** 渠道选择*/
	private String agentId;
	/** 是否到账*/
	private String isAccounted;
	/** 收款日期开始*/
	private String createDateBegin;
	/** 收款日期结束*/
	private String createDateEnd;
	/** 来款单位*/
	private String payerName;
	/** 收款银行*/
	private String toBankNname;
	/** 收款金额开始*/
	private String payAmountStart;
	/** 收款金额结束*/
	private String payAmountEnd;
	/** 打印状态*/
	private String printFlag;
	/** 收款方式*/
	private String payType;
	/** 出团日期开始*/
	private String groupOpenDateBegin;
	/** 出团日期结束*/
	private String groupOpenDateEnd;
	/** 确认收款日期开始*/
	private String receiptConfirmationDateBegin;
	/** 确认收款日期结束*/
	private String receiptConfirmationDateEnd;
	/** 游客（签证订单收款）*/
	private String travellerName;
	/** 地接社（其他收入收款）*/
	private String supplierInfo;
	/** 收款金额币种（其他收入收款）*/
	private String currency;
	
	
	public String getAccountDateBegin() {
		return accountDateBegin;
	}
	public void setAccountDateBegin(String accountDateBegin) {
		this.accountDateBegin = accountDateBegin;
	}
	public String getAccountDateEnd() {
		return accountDateEnd;
	}
	public void setAccountDateEnd(String accountDateEnd) {
		this.accountDateEnd = accountDateEnd;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getIsAccounted() {
		return isAccounted;
	}
	public void setIsAccounted(String isAccounted) {
		this.isAccounted = isAccounted;
	}
	public String getCreateDateBegin() {
		return createDateBegin;
	}
	public void setCreateDateBegin(String createDateBegin) {
		this.createDateBegin = createDateBegin;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getToBankNname() {
		return toBankNname;
	}
	public void setToBankNname(String toBankNname) {
		this.toBankNname = toBankNname;
	}
	public String getPayAmountStart() {
		return payAmountStart;
	}
	public void setPayAmountStart(String payAmountStart) {
		this.payAmountStart = payAmountStart;
	}
	public String getPayAmountEnd() {
		return payAmountEnd;
	}
	public void setPayAmountEnd(String payAmountEnd) {
		this.payAmountEnd = payAmountEnd;
	}
	public String getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getGroupOpenDateBegin() {
		return groupOpenDateBegin;
	}
	public void setGroupOpenDateBegin(String groupOpenDateBegin) {
		this.groupOpenDateBegin = groupOpenDateBegin;
	}
	public String getGroupOpenDateEnd() {
		return groupOpenDateEnd;
	}
	public void setGroupOpenDateEnd(String groupOpenDateEnd) {
		this.groupOpenDateEnd = groupOpenDateEnd;
	}
	public String getReceiptConfirmationDateBegin() {
		return receiptConfirmationDateBegin;
	}
	public void setReceiptConfirmationDateBegin(String receiptConfirmationDateBegin) {
		this.receiptConfirmationDateBegin = receiptConfirmationDateBegin;
	}
	public String getReceiptConfirmationDateEnd() {
		return receiptConfirmationDateEnd;
	}
	public void setReceiptConfirmationDateEnd(String receiptConfirmationDateEnd) {
		this.receiptConfirmationDateEnd = receiptConfirmationDateEnd;
	}
	public String getTravellerName() {
		return travellerName;
	}
	public void setTravellerName(String travellerName) {
		this.travellerName = travellerName;
	}
	public String getSupplierInfo() {
		return supplierInfo;
	}
	public void setSupplierInfo(String supplierInfo) {
		this.supplierInfo = supplierInfo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
}
