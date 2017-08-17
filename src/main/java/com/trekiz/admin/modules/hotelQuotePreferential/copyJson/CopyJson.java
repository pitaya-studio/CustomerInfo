package com.trekiz.admin.modules.hotelQuotePreferential.copyJson;

import java.util.Date;
import java.util.List;

public class CopyJson {
   private String islandText;//岛屿名称
   private List<ConditionDetail> conditionDetails;//报价的详情
   private Date beginDate;//合计开始日期
   private Date endDate;//合计结束日期
   private List<RoomNight> roomNights;//合计的房型和晚数
   private String meals;//餐型(HB,FB,BB)
   private String islandWays;//上岛方式(快艇，水飞)
   private TotalPrice totalPrice;//优惠前价格明细
   private TotalPrice preferentialTotalPrice;//优惠后价格明细
   
	public String getIslandText() {
		return islandText;
	}
	public void setIslandText(String islandText) {
		this.islandText = islandText;
	}
	public List<ConditionDetail> getConditionDetails() {
		return conditionDetails;
	}
	public void setConditionDetails(List<ConditionDetail> conditionDetails) {
		this.conditionDetails = conditionDetails;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<RoomNight> getRoomNights() {
		return roomNights;
	}
	public void setRoomNights(List<RoomNight> roomNights) {
		this.roomNights = roomNights;
	}
	public String getMeals() {
		return meals;
	}
	public void setMeals(String meals) {
		this.meals = meals;
	}
	public String getIslandWays() {
		return islandWays;
	}
	public void setIslandWays(String islandWays) {
		this.islandWays = islandWays;
	}
	public TotalPrice getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(TotalPrice totalPrice) {
		this.totalPrice = totalPrice;
	}
	public TotalPrice getPreferentialTotalPrice() {
		return preferentialTotalPrice;
	}
	public void setPreferentialTotalPrice(TotalPrice preferentialTotalPrice) {
		this.preferentialTotalPrice = preferentialTotalPrice;
	}
   
}
