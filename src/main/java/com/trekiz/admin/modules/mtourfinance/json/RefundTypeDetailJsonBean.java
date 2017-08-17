package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

/**
 * 付款类型jsonBean，用于美途付款-款项明细-（借款、退款、追加成本）接口
 * Title: RefundTypeDetailJsonBean.java
 * Description:
 * @author majiancheng
 * @created 2015-10-24 下午3:46:06
 */
public class RefundTypeDetailJsonBean {
	private String fundsName;//'款项'
	private Integer currencyUuid;//'借款币种Uuid'
	private Double amount;//'借款金额'
	private String channelName;//'渠道商Name'
	private String applicant;//'申请人'
	private String memo;//'备注'
	private List<AmountJsonBean> totalAmounts;//累计借款金额

	public String getFundsName() {
		return fundsName;
	}

	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}

	public Integer getCurrencyUuid() {
		return currencyUuid;
	}

	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<AmountJsonBean> getTotalAmounts() {
		return totalAmounts;
	}

	public void setTotalAmounts(List<AmountJsonBean> totalAmounts) {
		this.totalAmounts = totalAmounts;
	}
}
