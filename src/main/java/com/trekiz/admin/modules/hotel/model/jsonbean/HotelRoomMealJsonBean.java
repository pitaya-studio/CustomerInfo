package com.trekiz.admin.modules.hotel.model.jsonbean;

import java.io.Serializable;
import java.util.Map;

/**
 * 级联需要的数据结构
 * @author police
 *
 */
public class HotelRoomMealJsonBean implements Serializable {
	private String uuid;
	private String name ;
	private Map<String,HotelMealJsonBean> upMealTypes;
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
	public Map<String, HotelMealJsonBean> getUpMealTypes() {
		return upMealTypes;
	}
	public void setUpMealTypes(Map<String, HotelMealJsonBean> upMealTypes) {
		this.upMealTypes = upMealTypes;
	}
}
