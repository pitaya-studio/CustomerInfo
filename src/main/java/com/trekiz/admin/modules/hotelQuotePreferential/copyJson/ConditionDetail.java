package com.trekiz.admin.modules.hotelQuotePreferential.copyJson;

import java.util.List;

//每个报价条件的详细信息
public class ConditionDetail {
   private String inDate;
   private String roomType;
   private String mealType;
   private String islandWay;
   private List<GuestPrice> GuestPrices;
   //private String thirdPriceCurrencyText;//第三人
   //private Double thirdPrice;//
   
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public String getIslandWay() {
		return islandWay;
	}
	public void setIslandWay(String islandWay) {
		this.islandWay = islandWay;
	}
	public List<GuestPrice> getGuestPrices() {
		return GuestPrices;
	}
	public void setGuestPrices(List<GuestPrice> guestPrices) {
		GuestPrices = guestPrices;
	}
	/*public String getThirdPriceCurrencyText() {
		return thirdPriceCurrencyText;
	}
	public void setThirdPriceCurrencyText(String thirdPriceCurrencyText) {
		this.thirdPriceCurrencyText = thirdPriceCurrencyText;
	}
	public Double getThirdPrice() {
		return thirdPrice;
	}
	public void setThirdPrice(Double thirdPrice) {
		this.thirdPrice = thirdPrice;
	}*/
   
}
