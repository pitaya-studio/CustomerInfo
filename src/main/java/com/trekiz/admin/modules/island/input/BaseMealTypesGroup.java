package com.trekiz.admin.modules.island.input;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.input.UpMealTypesInput;

public class BaseMealTypesGroup {
   private String uuid;//uuid
   private MealType mealType;
   private List<UpMealTypeGroup> upMealTypes;
	   
		
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


	public List<UpMealTypeGroup> getUpMealTypes() {
		return upMealTypes;
	}


	public void setUpMealTypes(List<UpMealTypeGroup> upMealTypes) {
		this.upMealTypes = upMealTypes;
	}


		//需要重写
	public List<ActivityHotelGroupMealRise> getHotelGroupMealRise(List<UpMealTypesInput> list){
			List<ActivityHotelGroupMealRise> hotelGroupMealRise = new ArrayList<ActivityHotelGroupMealRise>();
			for(UpMealTypesInput mealTypes : list){
				ActivityHotelGroupMealRise riseMealType = new ActivityHotelGroupMealRise();
				if(StringUtils.isNotEmpty(mealTypes.getGroupMealRiseUuid())){
					riseMealType.setUuid(mealTypes.getGroupMealRiseUuid());
				}
				riseMealType.setHotelMealUuid(mealTypes.getMealType().getValue());
				riseMealType.setCurrencyId(Integer.valueOf(mealTypes.getCurrency().getValue()));
				riseMealType.setPrice(Double.valueOf(mealTypes.getPrice()));
				hotelGroupMealRise.add(riseMealType);
			}
			return hotelGroupMealRise;
	}
}
