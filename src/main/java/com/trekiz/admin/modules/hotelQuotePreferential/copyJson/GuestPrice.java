package com.trekiz.admin.modules.hotelQuotePreferential.copyJson;

public class GuestPrice {
	private String travelerType;//游客类型
	private String travelerTypeText;//游客类型页面输出
	private String currencyId;//币种id
	private String currencyText;//币种输出符号
	private Double amount;//金额
	
	public String getTravelerType() {
		return travelerType;
	}
	public void setTravelerType(String travelerType) {
		this.travelerType = travelerType;
	}
	public String getTravelerTypeText() {
		return travelerTypeText;
	}
	public void setTravelerTypeText(String travelerTypeText) {
		this.travelerTypeText = travelerTypeText;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyText() {
		return currencyText;
	}
	public void setCurrencyText(String currencyText) {
		this.currencyText = currencyText;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
