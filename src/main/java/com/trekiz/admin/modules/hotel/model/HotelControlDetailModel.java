package com.trekiz.admin.modules.hotel.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

/**
 * 控房集合（扣减间数处使用）
* <p>description: TODO</p>
* <p>Date: 2015-6-11 下午10:17:49</p>
* <p>modify：</p>
* @author: majiancheng
* @version: 3.0
 */
public class HotelControlDetailModel implements Serializable {
	private static final long serialVersionUID = 8624356377811251795L;
	
	private String hotelControlUuid;
	private String hotelControlDetailUuid;
	private Date inDate;
	private String hotelMeal;
	private String hotelMealName;
	private String islandWay;
	private String islandWayName;
	private Integer stock;
	private Integer sellStock;
	private Integer preStock;
	private Integer groundSupplier;
	private Integer purchaseType;
	private String islandOrderControlDetailUuid;
	private Integer islandOrderControlDetailNumber;
	private List<HotelControlRoomDetail> rooms;
	
	public String getHotelControlUuid() {
		return hotelControlUuid;
	}
	public void setHotelControlUuid(String hotelControlUuid) {
		this.hotelControlUuid = hotelControlUuid;
	}
	public String getHotelControlDetailUuid() {
		return hotelControlDetailUuid;
	}
	public void setHotelControlDetailUuid(String hotelControlDetailUuid) {
		this.hotelControlDetailUuid = hotelControlDetailUuid;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getHotelMeal() {
		return hotelMeal;
	}
	public void setHotelMeal(String hotelMeal) {
		this.hotelMeal = hotelMeal;
	}
	public String getIslandWay() {
		return islandWay;
	}
	public void setIslandWay(String islandWay) {
		this.islandWay = islandWay;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getSellStock() {
		return sellStock;
	}
	public void setSellStock(Integer sellStock) {
		this.sellStock = sellStock;
	}
	public Integer getPreStock() {
		return preStock;
	}
	public void setPreStock(Integer preStock) {
		this.preStock = preStock;
	}
	
	public Integer getGroundSupplier() {
		return groundSupplier;
	}
	public void setGroundSupplier(Integer groundSupplier) {
		this.groundSupplier = groundSupplier;
	}
	public Integer getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(Integer purchaseType) {
		this.purchaseType = purchaseType;
	}
	public List<HotelControlRoomDetail> getRooms() {
		return rooms;
	}
	public void setRooms(List<HotelControlRoomDetail> rooms) {
		this.rooms = rooms;
	}
	public String getHotelMealName() {
		return hotelMealName;
	}
	public void setHotelMealName(String hotelMealName) {
		this.hotelMealName = hotelMealName;
	}
	public String getIslandWayName() {
		return islandWayName;
	}
	public void setIslandWayName(String islandWayName) {
		this.islandWayName = islandWayName;
	}
	public String getIslandOrderControlDetailUuid() {
		return islandOrderControlDetailUuid;
	}
	public void setIslandOrderControlDetailUuid(String islandOrderControlDetailUuid) {
		this.islandOrderControlDetailUuid = islandOrderControlDetailUuid;
	}
	public Integer getIslandOrderControlDetailNumber() {
		return islandOrderControlDetailNumber;
	}
	public void setIslandOrderControlDetailNumber(
			Integer islandOrderControlDetailNumber) {
		this.islandOrderControlDetailNumber = islandOrderControlDetailNumber;
	}

	
}
