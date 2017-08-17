package com.trekiz.admin.modules.hotel.model.jsonbean;

import java.io.Serializable;
import java.util.Map;

/**
 * 级联需要的数据结构
 * @author police
 *
 */
public class HotelRoomJsonBean implements Serializable {
	private String uuid;
	private String name ;
	private String capacity;
	private String capacityNote;
	private Map<String,HotelRoomMealJsonBean> baseMealTypes;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public Map<String, HotelRoomMealJsonBean> getBaseMealTypes() {
		return baseMealTypes;
	}
	public void setBaseMealTypes(Map<String, HotelRoomMealJsonBean> baseMealTypes) {
		this.baseMealTypes = baseMealTypes;
	}
	public String getCapacityNote() {
		return capacityNote;
	}
	public void setCapacityNote(String capacityNote) {
		this.capacityNote = capacityNote;
	}
	
}
