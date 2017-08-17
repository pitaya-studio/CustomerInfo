package com.trekiz.admin.modules.hotel.input;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;

public class BaseMealTypesInput {
	
   private String groupMealUuid;
   private MealTypeInput mealType;
   private List<UpMealTypesInput> upMealTypes;
   
	public MealTypeInput getMealType() {
		return mealType;
	}
	public void setMealType(MealTypeInput mealType) {
		this.mealType = mealType;
	}
	public List<UpMealTypesInput> getUpMealTypes() {
		return upMealTypes;
	}
	public void setUpMealTypes(List<UpMealTypesInput> upMealTypes) {
		this.upMealTypes = upMealTypes;
	}
	public String getGroupMealUuid() {
		return groupMealUuid;
	}
	public void setGroupMealUuid(String groupMealUuid) {
		this.groupMealUuid = groupMealUuid;
	}
	public List<ActivityHotelGroupMealRise> getHotelGroupMealRise(List<UpMealTypesInput> list){
		List<ActivityHotelGroupMealRise> hotelGroupMealRise = new ArrayList<ActivityHotelGroupMealRise>();
		for(UpMealTypesInput mealTypes : list){
			ActivityHotelGroupMealRise riseMealType = new ActivityHotelGroupMealRise();
			if(StringUtils.isNotEmpty(mealTypes.getGroupMealRiseUuid())){
				riseMealType.setUuid(mealTypes.getGroupMealRiseUuid());
			}
			if(StringUtils.isNotEmpty(mealTypes.getMealType().getValue())){
				riseMealType.setHotelMealUuid(mealTypes.getMealType().getValue());
			}
			if(StringUtils.isNotEmpty(mealTypes.getCurrency().getValue())){
				riseMealType.setCurrencyId(Integer.valueOf(mealTypes.getCurrency().getValue()));
			}
			if(StringUtils.isNotEmpty(mealTypes.getPrice())){
				riseMealType.setPrice(Double.valueOf(mealTypes.getPrice()));
			}
			hotelGroupMealRise.add(riseMealType);
		}
		return hotelGroupMealRise;
	}
}
