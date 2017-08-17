package com.trekiz.admin.modules.island.input;

public class IslandPrice {

	private Currency currency;
	private String price;
	private String airlineuuid;
	private String typeUuid;
	
	public String getAirlineuuid() {
		return airlineuuid;
	}
	public void setAirlineuuid(String airlineuuid) {
		this.airlineuuid = airlineuuid;
	}
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
	public String getTypeUuid() {
		return typeUuid;
	}
	public void setTypeUuid(String typeUuid) {
		this.typeUuid = typeUuid;
	}
	
}
