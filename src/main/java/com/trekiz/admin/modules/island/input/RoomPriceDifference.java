package com.trekiz.admin.modules.island.input;

/**
 * 单房差
 * @author WangXK
 *
 */
public class RoomPriceDifference {
	private String price;
	private UnitRoomPrice unit;
	private Currency currency;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public UnitRoomPrice getUnit() {
		return unit;
	}
	public void setUnit(UnitRoomPrice unit) {
		this.unit = unit;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
