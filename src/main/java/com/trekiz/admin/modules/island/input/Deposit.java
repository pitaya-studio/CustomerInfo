package com.trekiz.admin.modules.island.input;
/**
 * 需交订金
 * @author wangXK
 *
 */
public class Deposit {
	
	private Currency currency;
	private String price;
	
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
