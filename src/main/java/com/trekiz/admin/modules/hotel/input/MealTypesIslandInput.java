package com.trekiz.admin.modules.hotel.input;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;

public class MealTypesIslandInput {
	private String mealType;// UUID
	private String mealTypeText;// 名称
	List<UpMealTypesInput> upMealTypes;
	
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public String getMealTypeText() {
		return mealTypeText;
	}
	public void setMealTypeText(String mealTypeText) {
		this.mealTypeText = mealTypeText;
	}
	public List<UpMealTypesInput> getUpMealTypes() {
		return upMealTypes;
	}
	public void setUpMealTypes(List<UpMealTypesInput> upMealTypes) {
		this.upMealTypes = upMealTypes;
	}
	public List<ActivityIslandGroupMealRise> getIslandGroupMealRise (List<UpMealTypesInput> list){
		List<ActivityIslandGroupMealRise> groupMealRiseList = new ArrayList<ActivityIslandGroupMealRise>();
		if(CollectionUtils.isNotEmpty(list)){
			ActivityIslandGroupMealRise groupMealRise = new ActivityIslandGroupMealRise();
			for(UpMealTypesInput upMealType : list){
				groupMealRise = new ActivityIslandGroupMealRise();
//				groupMealRise.setCurrencyId(Integer.valueOf(upMealType.getCurrency()));
				groupMealRise.setPrice(Double.valueOf(upMealType.getPrice()));
//				groupMealRise.setHotelMealUuid(upMealType.getUpMealType());
			}
		}
		return groupMealRiseList;
	}
}
