package com.trekiz.admin.modules.hotelPl.module.bean;

import java.io.Serializable;

public class HotelPlRoom implements Serializable{
	private static final long serialVersionUID = 3626228665785256313L;
	
	private String hotelUuid;
	private String hotelPlUuid;
	private String hotelRoomUuid;
	private String roomName;
	public String getHotelUuid() {
		return hotelUuid;
	}
	public void setHotelUuid(String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}
	public String getHotelPlUuid() {
		return hotelPlUuid;
	}
	public void setHotelPlUuid(String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}
	public String getHotelRoomUuid() {
		return hotelRoomUuid;
	}
	public void setHotelRoomUuid(String hotelRoomUuid) {
		this.hotelRoomUuid = hotelRoomUuid;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
}
