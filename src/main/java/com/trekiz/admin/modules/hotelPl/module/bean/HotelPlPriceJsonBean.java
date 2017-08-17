package com.trekiz.admin.modules.hotelPl.module.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class HotelPlPriceJsonBean implements Serializable{
	private static final long serialVersionUID = -8323180644792170454L;
	
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"房型UUID"
	private java.lang.String hotelRoomUuid;
	//"酒店餐型uuid（数据源是从酒店房型餐型关联表中获得，保存时映射到酒店餐型表中）多个餐型用“;”分隔，便于修改时分组"
	private java.lang.String hotelMealUuids;
	//"起始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"酒店住客类型uuid"
	private java.lang.String hotelGuestTypeUuid;
	//"币种id（为了后期支持多币种所有的金额都有对应的币种ID）。"
	private java.lang.Integer currencyId;
	//"金额"
	private BigDecimal amount;
	//"价格类型（0、普通价；1、同行价）"
	private java.lang.Integer priceType;
	//币种符号
	private String currencyMark;
	//入住率
	private String occupancyRate;
	//酒店房型显示文本
	private String hotelRoomText;
	public java.lang.String getUuid() {
		return uuid;
	}
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}
	public java.lang.String getHotelPlUuid() {
		return hotelPlUuid;
	}
	public void setHotelPlUuid(java.lang.String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}
	public java.lang.String getHotelRoomUuid() {
		return hotelRoomUuid;
	}
	public void setHotelRoomUuid(java.lang.String hotelRoomUuid) {
		this.hotelRoomUuid = hotelRoomUuid;
	}
	public java.lang.String getHotelMealUuids() {
		return hotelMealUuids;
	}
	public void setHotelMealUuids(java.lang.String hotelMealUuids) {
		this.hotelMealUuids = hotelMealUuids;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.lang.String getHotelGuestTypeUuid() {
		return hotelGuestTypeUuid;
	}
	public void setHotelGuestTypeUuid(java.lang.String hotelGuestTypeUuid) {
		this.hotelGuestTypeUuid = hotelGuestTypeUuid;
	}
	public java.lang.Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(java.lang.Integer currencyId) {
		this.currencyId = currencyId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public java.lang.Integer getPriceType() {
		return priceType;
	}
	public void setPriceType(java.lang.Integer priceType) {
		this.priceType = priceType;
	}
	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	public String getOccupancyRate() {
		return occupancyRate;
	}
	public void setOccupancyRate(String occupancyRate) {
		this.occupancyRate = occupancyRate;
	}
	public String getHotelRoomText() {
		return hotelRoomText;
	}
	public void setHotelRoomText(String hotelRoomText) {
		this.hotelRoomText = hotelRoomText;
	}
	
	

}
