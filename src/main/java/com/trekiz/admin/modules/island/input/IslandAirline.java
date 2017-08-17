package com.trekiz.admin.modules.island.input;

import java.util.List;

public class IslandAirline {
	private String value;//存放的是二字码
	private String text;//存放的是航空公司的名字
	private IslandFlight flight;
	private List<SpaceGrade> spaceGrade;
	private List<IslandPrices> prices;//根据舱位等级划分的价格
	private List<TouristType> touristType;//存放的是游客类型的list
	private List<String> ctrlTickets;//控票数
	private List<String> unCtrlTickets;//非控票数
	private List<String> remainTickets;//余位
	private Amount amount;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public IslandFlight getFlight() {
		return flight;
	}
	public void setFlight(IslandFlight flight) {
		this.flight = flight;
	}
	public List<SpaceGrade> getSpaceGrade() {
		return spaceGrade;
	}
	public void setSpaceGrade(List<SpaceGrade> spaceGrade) {
		this.spaceGrade = spaceGrade;
	}
	public List<IslandPrices> getPrices() {
		return prices;
	}
	public void setPrices(List<IslandPrices> prices) {
		this.prices = prices;
	}
	
	public List<TouristType> getTouristType() {
		return touristType;
	}
	public void setTouristType(List<TouristType> touristType) {
		this.touristType = touristType;
	}
	public List<String> getCtrlTickets() {
		return ctrlTickets;
	}
	public void setCtrlTickets(List<String> ctrlTickets) {
		this.ctrlTickets = ctrlTickets;
	}
	public List<String> getUnCtrlTickets() {
		return unCtrlTickets;
	}
	public void setUnCtrlTickets(List<String> unCtrlTickets) {
		this.unCtrlTickets = unCtrlTickets;
	}
	public List<String> getRemainTickets() {
		return remainTickets;
	}
	public void setRemainTickets(List<String> remainTickets) {
		this.remainTickets = remainTickets;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
}
