package com.trekiz.admin.modules.hotel.model.jsonbean;

import java.io.Serializable;

/**
 * 级联需要的数据结构
 * @author police
 *
 */
public class HotelMealJsonBean implements Serializable {
	private String uuid;
	private String name ;
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
	
}
