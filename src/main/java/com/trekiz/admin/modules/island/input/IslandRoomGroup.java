package com.trekiz.admin.modules.island.input;

import java.util.List;

public class IslandRoomGroup {
	
	private HouseType houseType;
	private String night;
	private List<BaseMealTypesGroup> baseMealTypes;
	
	public HouseType getHouseType() {
		return houseType;
	}
	public void setHouseType(HouseType houseType) {
		this.houseType = houseType;
	}
	public String getNight() {
		return night;
	}
	public void setNight(String night) {
		this.night = night;
	}
	public List<BaseMealTypesGroup> getBaseMealTypes() {
		return baseMealTypes;
	}
	public void setBaseMealTypes(List<BaseMealTypesGroup> baseMealTypes) {
		this.baseMealTypes = baseMealTypes;
	}
	
}
