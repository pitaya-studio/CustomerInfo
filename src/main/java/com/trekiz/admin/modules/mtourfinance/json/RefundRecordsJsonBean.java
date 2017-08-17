package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

/**
 * 批量付款信息集合JsonBean
 * ClassName: RefundRecordsJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-1-21
 */
public class RefundRecordsJsonBean {
	private String groupNo;//团号
	private Integer paymentObjectUuid;//付款对象Uuid
	private String paymentObjectName;//付款对象Name
	private String paymentTotalAmount;//付款总额
	private String payableTotalAmount;//应付金额-该付款对象所涉及的所有付款数据的应付金额之和
	private String paidTotalAmount;//已付金额-该付款对象所涉及的所有付款数据的已付金额之和
	private List<RefundRecordJsonBean> results;//付款记录集合
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public Integer getPaymentObjectUuid() {
		return paymentObjectUuid;
	}
	public void setPaymentObjectUuid(Integer paymentObjectUuid) {
		this.paymentObjectUuid = paymentObjectUuid;
	}
	public String getPaymentObjectName() {
		return paymentObjectName;
	}
	public void setPaymentObjectName(String paymentObjectName) {
		this.paymentObjectName = paymentObjectName;
	}
	public String getPaymentTotalAmount() {
		return paymentTotalAmount;
	}
	public void setPaymentTotalAmount(String paymentTotalAmount) {
		this.paymentTotalAmount = paymentTotalAmount;
	}
	public List<RefundRecordJsonBean> getResults() {
		return results;
	}
	public void setResults(List<RefundRecordJsonBean> results) {
		this.results = results;
	}
	public String getPayableTotalAmount() {
		return payableTotalAmount;
	}
	public void setPayableTotalAmount(String payableTotalAmount) {
		this.payableTotalAmount = payableTotalAmount;
	}
	public String getPaidTotalAmount() {
		return paidTotalAmount;
	}
	public void setPaidTotalAmount(String paidTotalAmount) {
		this.paidTotalAmount = paidTotalAmount;
	}
}
