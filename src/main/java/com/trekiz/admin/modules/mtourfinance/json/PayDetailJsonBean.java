package com.trekiz.admin.modules.mtourfinance.json;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 用来封装财务--付款--订单列表--展开子列表--付款记录--详情的返回值
 * @author zhangchao
 * @time 2016-1-28
 */
public class PayDetailJsonBean {
	private String paymentMethodCode;//付款方式code',
	private String paymentMethodName;//付款方式name
	private Date paymentDate;//付款日期
	private List<Map<String,Object>> recordList;
	/*recordList:[{
	 * paymentUuid:'付款记录Uuid'
    approvalDate:'报批日期'
    fundsType:'款项类型//借款:1,退款:2,追加成本:3,成本:4
    fundsTypeName
    applicant:'申请人'
    exchangeRate:'汇率'

    payableAmount:'应付金额',
    paidAmount:'已付金额'
    paymentAmount:'本次付款金额'
    paymentStatusCode:'付款状态',//0-已付款 1-已撤销
    paymentStatusName:'付款状态',//0-已付款 1-已撤销
    }]
*/	
	private String paymentTotalAmount;//付款总额
	private String convertedTotalAmount;//转换后总额
	private String receiveCompany;//收款单位
	private String checkNo;//'支票号',只有付款方式是支票时才有效
	private Date checkIssueDate;//'开票日期'//只有付款方式是支票时才有效
	private String paymentBank;//'付款行',//只有付款方式是汇款时才有效
	private String paymentAccount;//'付款账号'.//只有付款方式是汇款时才有效
	private String receiveBank;//'收款行',//只有付款方式是汇款时才有效
	private String receiveAccount;//'收款账户',//只有付款方式是汇款时才有效
	private List<Map<String,Object>> attachments;//付款附件
	private String memo;//'备注'
	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}
	public String getPaymentMethodName() {
		return paymentMethodName;
	}
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public List<Map<String, Object>> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Map<String, Object>> recordList) {
		this.recordList = recordList;
	}
	
	public String getPaymentTotalAmount() {
		return paymentTotalAmount;
	}
	public void setPaymentTotalAmount(String paymentTotalAmount) {
		this.paymentTotalAmount = paymentTotalAmount;
	}
	public String getConvertedTotalAmount() {
		return convertedTotalAmount;
	}
	public void setConvertedTotalAmount(String convertedTotalAmount) {
		this.convertedTotalAmount = convertedTotalAmount;
	}
	public String getReceiveCompany() {
		return receiveCompany;
	}
	public void setReceiveCompany(String receiveCompany) {
		this.receiveCompany = receiveCompany;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public Date getCheckIssueDate() {
		return checkIssueDate;
	}
	public void setCheckIssueDate(Date checkIssueDate) {
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
	public List<Map<String, Object>> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Map<String, Object>> attachments) {
		this.attachments = attachments;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
