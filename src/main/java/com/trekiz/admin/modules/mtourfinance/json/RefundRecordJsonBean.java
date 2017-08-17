package com.trekiz.admin.modules.mtourfinance.json;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 批量付款信息JsonBean
 * ClassName: RefundRecordJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-1-21
 */

public class RefundRecordJsonBean {
	//add by sy start 2016年1月27日15:32:34
	private String paymentStatusCode; //付款状态
	private String paymentStatusName;//付款名称
	private String fundsTypeName ; //款项类型名字
	private String fundsName; //款项名称
	private int peopleCount; //人数 
	private Integer currencyId;//币种ID
	@JSONField(name="PNR")
	private String pnr;
	private String tourOperatorUuid ; //地接社UUid大编号为地接社的时候才有意义
	private String tourOperatorName ; //地接社Name大编号为地接社的时候才有意义
	private String costUnitPrice ;//成本单价
	private String convertedTotalAmount ;//转换后总价
	private String receiveBank;//收款行名称款项为成本时才有意义
	private String receiveAccountNo ;//收款行账户款项为成本时才有意义
	private String approvalDateTime ;//报批日期
	private String approvalDateStr;//报批日期没有时分秒
	//款项-追加成本
	private String totalAddAmount; //累计追加金额
	private String currentAddAmount;//本次追加金额
	//款项-退款
	private String totalRefundAmount;//累计退款金额
	private String currentRefundAmount;//本次退款金额
	//款项-借款
	private String totalBorrowAmount;//累计借款金额
	private String currentBorrowAmount;//本次借款金额
	private String memo ; //备注
	//add by sy end
	private Long paymentUuid;//付款记录Uuid
	private Date approvalDate;//报批日期
	private String fundsType;//款项类型//成本:1,退款:2,借款:3,追加成本:4
	private AmountJsonBean payableAmount;//应付金额
	private AmountJsonBean paidAmount;//已付金额
	private String strPayableAmount;//应付金额(拼装好的)
	private String strPayableAmount_amount;//应付金额不要币种符号的
	private String strPaidAmount;//已付金额(拼装好的)
	private Boolean changeabled;//汇率是否可以修改'//值为true或者false
	private BigDecimal exchangeRate;//'汇率'
	private String applicant;// 申请人
	
	

	public String getApprovalDateStr() {
		return approvalDateStr;
	}
	public void setApprovalDateStr(String approvalDateStr) {
		this.approvalDateStr = approvalDateStr;
	}
	public String getStrPayableAmount_amount() {
		return strPayableAmount_amount;
	}
	public void setStrPayableAmount_amount(String strPayableAmount_amount) {
		this.strPayableAmount_amount = strPayableAmount_amount;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getPnr() {
		return pnr;
	}
	public void setPnr(String pnr) {
		this.pnr = pnr;
	}
	public String getPaymentStatusName() {
		return paymentStatusName;
	}
	public void setPaymentStatusName(String paymentStatusName) {
		this.paymentStatusName = paymentStatusName;
	}
	public String getApprovalDateTime() {
		return approvalDateTime;
	}
	public void setApprovalDateTime(String approvalDateTime) {
		this.approvalDateTime = approvalDateTime;
	}
	public Long getPaymentUuid() {
		return paymentUuid;
	}
	public void setPaymentUuid(Long paymentUuid) {
		this.paymentUuid = paymentUuid;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getFundsType() {
		return fundsType;
	}
	public void setFundsType(String fundsType) {
		this.fundsType = fundsType;
	}
	public AmountJsonBean getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(AmountJsonBean payableAmount) {
		this.payableAmount = payableAmount;
	}
	public AmountJsonBean getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(AmountJsonBean paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Boolean getChangeabled() {
		return changeabled;
	}
	public void setChangeabled(Boolean changeabled) {
		this.changeabled = changeabled;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getPaymentStatusCode() {
		return paymentStatusCode;
	}
	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}
	public String getFundsTypeName() {
		return fundsTypeName;
	}
	public void setFundsTypeName(String fundsTypeName) {
		this.fundsTypeName = fundsTypeName;
	}
	public String getFundsName() {
		return fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public int getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(int peopleCount) {
		this.peopleCount = peopleCount;
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
	public String getCostUnitPrice() {
		return costUnitPrice;
	}
	public void setCostUnitPrice(String costUnitPrice) {
		this.costUnitPrice = costUnitPrice;
	}
	public String getConvertedTotalAmount() {
		return convertedTotalAmount;
	}
	public void setConvertedTotalAmount(String convertedTotalAmount) {
		this.convertedTotalAmount = convertedTotalAmount;
	}
	public String getReceiveBank() {
		return receiveBank;
	}
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}
	public String getReceiveAccountNo() {
		return receiveAccountNo;
	}
	public void setReceiveAccountNo(String receiveAccountNo) {
		this.receiveAccountNo = receiveAccountNo;
	}
	public String getTotalAddAmount() {
		return totalAddAmount;
	}
	public void setTotalAddAmount(String totalAddAmount) {
		this.totalAddAmount = totalAddAmount;
	}
	public String getCurrentAddAmount() {
		return currentAddAmount;
	}
	public void setCurrentAddAmount(String currentAddAmount) {
		this.currentAddAmount = currentAddAmount;
	}
	public String getTotalRefundAmount() {
		return totalRefundAmount;
	}
	public void setTotalRefundAmount(String totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
	public String getCurrentRefundAmount() {
		return currentRefundAmount;
	}
	public void setCurrentRefundAmount(String currentRefundAmount) {
		this.currentRefundAmount = currentRefundAmount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStrPayableAmount() {
		return strPayableAmount;
	}
	public void setStrPayableAmount(String strPayableAmount) {
		this.strPayableAmount = strPayableAmount;
	}
	public String getStrPaidAmount() {
		return strPaidAmount;
	}
	public void setStrPaidAmount(String strPaidAmount) {
		this.strPaidAmount = strPaidAmount;
	}
	public String getTotalBorrowAmount() {
		return totalBorrowAmount;
	}
	public void setTotalBorrowAmount(String totalBorrowAmount) {
		this.totalBorrowAmount = totalBorrowAmount;
	}
	public String getCurrentBorrowAmount() {
		return currentBorrowAmount;
	}
	public void setCurrentBorrowAmount(String currentBorrowAmount) {
		this.currentBorrowAmount = currentBorrowAmount;
	}
	
	
}
