package com.trekiz.admin.modules.hotel.input;


/**
 * 餐型升餐类
 * @author wangxv
 *
 */
public class UpMealTypesInput {
	
	private String groupMealRiseUuid;// UUID
	private MealTypeInput mealType;
	private CurrencyInput currency;
	private String price;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getGroupMealRiseUuid() {
		return groupMealRiseUuid;
	}
	public void setGroupMealRiseUuid(String groupMealRiseUuid) {
		this.groupMealRiseUuid = groupMealRiseUuid;
	}
	public MealTypeInput getMealType() {
		return mealType;
	}
	public void setMealType(MealTypeInput mealType) {
		this.mealType = mealType;
	}
	public CurrencyInput getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyInput currency) {
		this.currency = currency;
	}
	
	
}
