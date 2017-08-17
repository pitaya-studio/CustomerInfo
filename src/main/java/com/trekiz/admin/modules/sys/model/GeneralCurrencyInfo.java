package com.trekiz.admin.modules.sys.model;

import java.util.List;
//序号		币种名称		发行国家		主币中文大写		辅币中文大写			辅币进位制
	//1			人民币		中国			元					角、分			1元=10角=100分
/**
 * 常用币种进位信息
 * @author majiancheng
 *
 */
public class GeneralCurrencyInfo {
	private String currencyName;//币种名称
	private String country;//发行国家
	private String currencyCapital;//主币中文大写	
	private String fractionalCurrency;//辅币中文大写
	private List<FractionalCurrency> fractionalCurrencys;//辅币进位制
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCurrencyCapital() {
		return currencyCapital;
	}
	public void setCurrencyCapital(String currencyCapital) {
		this.currencyCapital = currencyCapital;
	}
	public String getFractionalCurrency() {
		return fractionalCurrency;
	}
	public void setFractionalCurrency(String fractionalCurrency) {
		this.fractionalCurrency = fractionalCurrency;
	}
	public List<FractionalCurrency> getFractionalCurrencys() {
		return fractionalCurrencys;
	}
	public void setFractionalCurrencys(List<FractionalCurrency> fractionalCurrencys) {
		this.fractionalCurrencys = fractionalCurrencys;
	}
	
}
