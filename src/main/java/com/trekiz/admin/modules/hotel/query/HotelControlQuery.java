package com.trekiz.admin.modules.hotel.query;

/**
 * 酒店控房的查询条件类
 * 
 * @author wang
 * 
 */
public class HotelControlQuery {
	
	private String country;//国家
	private String groupOpenDate;//开始日期
	private String groupCloseDate;//结束日期
	private String island;//岛屿名称
	private String islandway;//上岛方式
	private String flights;//航班
	private String roomtype;//房型
	private String roomnum;//现有库存
	private String publishperson;//发布人
	private String hotel;//酒店名称
	private String hoteljituan;//酒店集团
	private String foodtype;//餐型
	private String controlname;//控房名称
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getGroupOpenDate() {
		return groupOpenDate;
	}
	public void setGroupOpenDate(String groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	public String getGroupCloseDate() {
		return groupCloseDate;
	}
	public void setGroupCloseDate(String groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}
	public String getIsland() {
		return island;
	}
	public void setIsland(String island) {
		this.island = island;
	}
	public String getIslandway() {
		return islandway;
	}
	public void setIslandway(String islandway) {
		this.islandway = islandway;
	}
	public String getFlights() {
		return flights;
	}
	public void setFlights(String flights) {
		this.flights = flights;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getRoomnum() {
		return roomnum;
	}
	public void setRoomnum(String roomnum) {
		this.roomnum = roomnum;
	}
	public String getPublishperson() {
		return publishperson;
	}
	public void setPublishperson(String publishperson) {
		this.publishperson = publishperson;
	}
	public String getHotel() {
		return hotel;
	}
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	public String getHoteljituan() {
		return hoteljituan;
	}
	public void setHoteljituan(String hoteljituan) {
		this.hoteljituan = hoteljituan;
	}
	public String getFoodtype() {
		return foodtype;
	}
	public void setFoodtype(String foodtype) {
		this.foodtype = foodtype;
	}
	public String getControlname() {
		return controlname;
	}
	public void setControlname(String controlname) {
		this.controlname = controlname;
	}

	
}
