package com.trekiz.admin.modules.hotel.input;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;

/**
 * 房型类
 * @author wangxv
 *
 */
public class HouseTypesInput {
	
	private String groupRoomUuid;// 房型表uuid
	private String night;
	private HouseTypeInput houseType;
	private List<BaseMealTypesInput> baseMealTypes;
	
	public String getNight() {
		return night;
	}
	public void setNight(String night) {
		this.night = night;
	}
	public String getGroupRoomUuid() {
		return groupRoomUuid;
	}
	public void setGroupRoomUuid(String groupRoomUuid) {
		this.groupRoomUuid = groupRoomUuid;
	}
	public HouseTypeInput getHouseType() {
		return houseType;
	}
	public void setHouseType(HouseTypeInput houseType) {
		this.houseType = houseType;
	}
	public List<BaseMealTypesInput> getBaseMealTypes() {
		return baseMealTypes;
	}
	public void setBaseMealTypes(List<BaseMealTypesInput> baseMealTypes) {
		this.baseMealTypes = baseMealTypes;
	}
	
	
	//返回activity_hotel_group_meal类数据
	public List<ActivityHotelGroupMeal> getHotelGroupMeal(List<BaseMealTypesInput> list){
		List<ActivityHotelGroupMeal> hotelGroupMeal = new ArrayList<ActivityHotelGroupMeal>();
		for(BaseMealTypesInput mealTypes : list){
			ActivityHotelGroupMeal mealType = new ActivityHotelGroupMeal();
			if(StringUtils.isNotEmpty(mealTypes.getGroupMealUuid())){
				mealType.setUuid(mealTypes.getGroupMealUuid());
			}
			if(StringUtils.isNotEmpty(mealTypes.getMealType().getValue())){
				mealType.setHotelMealUuid(mealTypes.getMealType().getValue());
			}
			mealType.setActivityHotelGroupMealsRiseList(mealTypes.getHotelGroupMealRise(mealTypes.getUpMealTypes()));
			hotelGroupMeal.add(mealType);
		}
		return hotelGroupMeal;
	}
}
