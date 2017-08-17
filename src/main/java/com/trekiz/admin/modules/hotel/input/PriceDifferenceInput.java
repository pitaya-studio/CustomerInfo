package com.trekiz.admin.modules.hotel.input;
/**
 * 单房差类
 * @author wangxv
 *
 */
public class PriceDifferenceInput {
	
	
	private String price;
	private UnitInput unit;
	private CurrencyInput currency;
	
	/*private String currency;// UUID
	private String currencyText;// 币种符号
	private String unit;// 单房差单位id
	private String unitText;// 名称
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyText() {
		return currencyText;
	}
	public void setCurrencyText(String currencyText) {
		this.currencyText = currencyText;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnitText() {
		return unitText;
	}
	public void setUnitText(String unitText) {
		this.unitText = unitText;
	}*/
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public UnitInput getUnit() {
		return unit;
	}
	public void setUnit(UnitInput unit) {
		this.unit = unit;
	}
	public CurrencyInput getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyInput currency) {
		this.currency = currency;
	}
	
}
