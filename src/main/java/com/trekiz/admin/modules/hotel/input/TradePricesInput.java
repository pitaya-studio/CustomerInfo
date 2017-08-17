package com.trekiz.admin.modules.hotel.input;

/**
 * 同行价格类
 * 
 * @author 6CR428W6Y2
 */
public class TradePricesInput {
	private String groupPriceUuid;//团期价格表uuid
	private String uuid; //游客类型uuid
	private String type;//name
	private CurrencyInput currency;
	private String price;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*private String currency;// UUID
	private String currencyText;// 名称
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
*/
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGroupPriceUuid() {
		return groupPriceUuid;
	}

	public void setGroupPriceUuid(String groupPriceUuid) {
		this.groupPriceUuid = groupPriceUuid;
	}

	public CurrencyInput getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyInput currency) {
		this.currency = currency;
	}
	
	
}
