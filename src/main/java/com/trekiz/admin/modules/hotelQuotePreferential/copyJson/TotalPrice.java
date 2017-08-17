package com.trekiz.admin.modules.hotelQuotePreferential.copyJson;

import java.util.List;

public class TotalPrice {
	private String mixlivePriceCurrencyId;//混住费用币种id
	private String mixlivePriceCurrencyText;//混住费用币种输出符号
	private Double mixlivePrice;//混住费用
	private String totalPriceCurrencyId;//打包的整体价格币种id
	private String totalPriceCurrencyText;//打包的整体价格币种输出符号
	private Double totalPrice;//打包的整体价格
	//private String thirdPriceCurrencyId;//第三人
	//private String thirdPriceCurrencyText;//
	//private Double thirdPrice;//
	private List<GuestPrice> GuestPrices;//各游客类型的价格
	
	public String getMixlivePriceCurrencyId() {
		return mixlivePriceCurrencyId;
	}
	public void setMixlivePriceCurrencyId(String mixlivePriceCurrencyId) {
		this.mixlivePriceCurrencyId = mixlivePriceCurrencyId;
	}
	public String getMixlivePriceCurrencyText() {
		return mixlivePriceCurrencyText;
	}
	public void setMixlivePriceCurrencyText(String mixlivePriceCurrencyText) {
		this.mixlivePriceCurrencyText = mixlivePriceCurrencyText;
	}
	public Double getMixlivePrice() {
		return mixlivePrice;
	}
	public void setMixlivePrice(Double mixlivePrice) {
		this.mixlivePrice = mixlivePrice;
	}
	public List<GuestPrice> getGuestPrices() {
		return GuestPrices;
	}
	public void setGuestPrices(List<GuestPrice> guestPrices) {
		GuestPrices = guestPrices;
	}
	public String getTotalPriceCurrencyId() {
		return totalPriceCurrencyId;
	}
	public void setTotalPriceCurrencyId(String totalPriceCurrencyId) {
		this.totalPriceCurrencyId = totalPriceCurrencyId;
	}
	public String getTotalPriceCurrencyText() {
		return totalPriceCurrencyText;
	}
	public void setTotalPriceCurrencyText(String totalPriceCurrencyText) {
		this.totalPriceCurrencyText = totalPriceCurrencyText;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	/*public String getThirdPriceCurrencyId() {
		return thirdPriceCurrencyId;
	}
	public void setThirdPriceCurrencyId(String thirdPriceCurrencyId) {
		this.thirdPriceCurrencyId = thirdPriceCurrencyId;
	}
	public String getThirdPriceCurrencyText() {
		return thirdPriceCurrencyText;
	}
	public void setThirdPriceCurrencyText(String thirdPriceCurrencyText) {
		this.thirdPriceCurrencyText = thirdPriceCurrencyText;
	}
	public Double getThirdPrice() {
		return thirdPrice;
	}
	public void setThirdPrice(Double thirdPrice) {
		this.thirdPrice = thirdPrice;
	}*/
	
}
