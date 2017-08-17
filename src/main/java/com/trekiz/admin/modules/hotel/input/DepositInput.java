package com.trekiz.admin.modules.hotel.input;
/**
 * 定金类
 * @author wangxv
 *
 */
public class DepositInput {
	
    private String price;
	
	private CurrencyInput currency;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public CurrencyInput getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyInput currency) {
		this.currency = currency;
	}
	
	
}
