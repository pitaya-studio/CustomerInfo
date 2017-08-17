package com.trekiz.admin.modules.island.input;

/**
 * 升餐型对应的表
 * @author Wangxk
 *
 */
public class UpMealTypeGroup {

	private String uuid;// 升餐型的UUID
	private MealType mealType;
	private Currency currency;
	private String price;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public MealType getMealType() {
		return mealType;
	}
	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
