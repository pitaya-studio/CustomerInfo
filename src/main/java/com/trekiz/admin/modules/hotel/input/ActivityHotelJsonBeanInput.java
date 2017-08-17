package com.trekiz.admin.modules.hotel.input;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;

public class ActivityHotelJsonBeanInput {
	
	private String hotelGroupUuid;//酒店产品团期表uuid
	private String no;
	private Date date;
	private List<HouseTypesInput> houseTypes;
	private List<TrafficWaysInput> trafficWays;
	private String airlines;
	private List<TradePricesInput> tradePrices;
	private String ctrlRoom;
	private List<CtrlRoomsInput> ctrlRooms;
	private String unCtrlRoom;
	private String ctrlRoomPriority;// 系统常量：1控票数2非控票数
	private String comment;// 备注
	private PriceDifferenceInput  priceDiff;
	private DepositInput  deposit;
	private String hotelUuid;//酒店产品表uuid
	
	public PriceDifferenceInput getPriceDiff() {
		return priceDiff;
	}
	public void setPriceDiff(PriceDifferenceInput priceDiff) {
		this.priceDiff = priceDiff;
	}
	public DepositInput getDeposit() {
		return deposit;
	}
	public void setDeposit(DepositInput deposit) {
		this.deposit = deposit;
	}
	public List<TrafficWaysInput> getTrafficWays() {
		return trafficWays;
	}
	public void setTrafficWays(List<TrafficWaysInput> trafficWays) {
		this.trafficWays = trafficWays;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<HouseTypesInput> getHouseTypes() {
		return houseTypes;
	}
	public void setHouseTypes(List<HouseTypesInput> houseTypes) {
		this.houseTypes = houseTypes;
	}
	public String getAirlines() {
		return airlines;
	}
	public void setAirlines(String airlines) {
		this.airlines = airlines;
	}
	public List<TradePricesInput> getTradePrices() {
		return tradePrices;
	}
	public void setTradePrices(List<TradePricesInput> tradePrices) {
		this.tradePrices = tradePrices;
	}
	public String getCtrlRoom() {
		return ctrlRoom;
	}
	public void setCtrlRoom(String ctrlRoom) {
		this.ctrlRoom = ctrlRoom;
	}
	public String getUnCtrlRoom() {
		return unCtrlRoom;
	}
	public void setUnCtrlRoom(String unCtrlRoom) {
		this.unCtrlRoom = unCtrlRoom;
	}
	public String getCtrlRoomPriority() {
		return ctrlRoomPriority;
	}
	public void setCtrlRoomPriority(String ctrlRoomPriority) {
		this.ctrlRoomPriority = ctrlRoomPriority;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getHotelGroupUuid() {
		return hotelGroupUuid;
	}
	public void setHotelGroupUuid(String hotelGroupUuid) {
		this.hotelGroupUuid = hotelGroupUuid;
	}
    public List<CtrlRoomsInput> getCtrlRooms() {
		return ctrlRooms;
	}
	public void setCtrlRooms(List<CtrlRoomsInput> ctrlRooms) {
		this.ctrlRooms = ctrlRooms;
	}
	public String getHotelUuid() {
		return hotelUuid;
	}
	public void setHotelUuid(String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}
	//返回上岛方式字符串，多个值用‘；’分隔
	public String getIslandWay(List<TrafficWaysInput> list){
		String islandWay = "";
		if(CollectionUtils.isNotEmpty(list)){
			for(TrafficWaysInput way :list){
				islandWay +=way.getTrafficWaysval()+";";
			}
		}
		return islandWay.substring(0, islandWay.lastIndexOf(";"));
	}
	//返回activity_hotel_group_price类数据
	public List<ActivityHotelGroupPrice> getHotelGroupPrice(List<TradePricesInput> list){
		List<ActivityHotelGroupPrice> hotelGtoupPrice = new ArrayList<ActivityHotelGroupPrice>();
		for(TradePricesInput tradePrice : list){
			ActivityHotelGroupPrice price = new ActivityHotelGroupPrice();
			if(StringUtils.isNotEmpty(tradePrice.getGroupPriceUuid())){
				price.setUuid(tradePrice.getGroupPriceUuid());
			}
			price.setType(tradePrice.getUuid());
			if(StringUtils.isNotEmpty(tradePrice.getPrice())){
			price.setPrice(Double.valueOf(tradePrice.getPrice()));
			}
			if(StringUtils.isNotEmpty(tradePrice.getCurrency().getValue())){
			price.setCurrencyId(Integer.valueOf(tradePrice.getCurrency().getValue()));
			}
			hotelGtoupPrice.add(price);
		}
		return hotelGtoupPrice;
	}
	//返回activity_hotel_group_room类数据
	public List<ActivityHotelGroupRoom> getHotelGroupRoom(List<HouseTypesInput> list){
		List<ActivityHotelGroupRoom> hotelGroupRoom = new ArrayList<ActivityHotelGroupRoom>();
		for(HouseTypesInput houseType : list){
			ActivityHotelGroupRoom roomType = new ActivityHotelGroupRoom();
			if(StringUtils.isNotEmpty(houseType.getGroupRoomUuid())){
				roomType.setHotelRoomUuid(houseType.getGroupRoomUuid());
			}
			if(StringUtils.isNotEmpty(houseType.getHouseType().getValue())){
				roomType.setHotelRoomUuid(houseType.getHouseType().getValue());
			}
			if(StringUtils.isNotEmpty(houseType.getNight())){
				roomType.setNights(Integer.valueOf(houseType.getNight()));
			}
			if(CollectionUtils.isNotEmpty(houseType.getBaseMealTypes())){
				roomType.setActivityHotelGroupMealList(houseType.getHotelGroupMeal(houseType.getBaseMealTypes()));
			}
			hotelGroupRoom.add(roomType);
		}
		return hotelGroupRoom;
	}
	//返回activity_hotel_group_controlDetail类数据
	private List<ActivityHotelGroupControlDetail> getControlDetail(List<CtrlRoomsInput> list) {
		List<ActivityHotelGroupControlDetail> hotelControlDetails = new ArrayList<ActivityHotelGroupControlDetail>();
		for(CtrlRoomsInput ctrRooms : list){
			ActivityHotelGroupControlDetail hotelControlDetail = new ActivityHotelGroupControlDetail();
			if(StringUtils.isNotEmpty(ctrRooms.getControlDetailUuid())){
				hotelControlDetail.setUuid(ctrRooms.getControlDetailUuid());
			}
			hotelControlDetail.setHotelControlDetailUuid(ctrRooms.getUuid());
			hotelControlDetail.setNum(Integer.valueOf(ctrRooms.getNum()));
			hotelControlDetails.add(hotelControlDetail);
		}
		return hotelControlDetails;
	}

	public ActivityHotelGroup transfer2ActivityHotelGroup(){
		ActivityHotelGroup activityHotelGroup = new ActivityHotelGroup();
		
		if(StringUtils.isNotEmpty(hotelGroupUuid)){
			activityHotelGroup.setUuid(hotelGroupUuid);
		}
		if(StringUtils.isNotEmpty(hotelUuid)){
			activityHotelGroup.setActivityHotelUuid(hotelUuid);
		}
		if(StringUtils.isNotEmpty(no)){
			activityHotelGroup.setGroupCode(no);
		}
		if(date!=null){
			activityHotelGroup.setGroupOpenDate(date);
		}
		if(StringUtils.isNotEmpty(airlines)){
			activityHotelGroup.setAirline(airlines);
		}
		if(StringUtils.isNotEmpty(ctrlRoom)&&StringUtils.isNotEmpty(unCtrlRoom)){
			activityHotelGroup.setRemNumber(Integer.valueOf(ctrlRoom)+Integer.valueOf(unCtrlRoom));
		}
		if(StringUtils.isNotEmpty(ctrlRoom)){
			activityHotelGroup.setControlNum(Integer.valueOf(ctrlRoom));
		}
		if(StringUtils.isNotEmpty(unCtrlRoom)){
			activityHotelGroup.setUncontrolNum(Integer.valueOf(unCtrlRoom));
		}
		if(StringUtils.isNotEmpty(ctrlRoomPriority)){
			activityHotelGroup.setPriorityDeduction(Integer.valueOf((ctrlRoomPriority.equals("true")?1:2)));
		}
		if(StringUtils.isNotEmpty(comment)){
			activityHotelGroup.setMemo(comment);
		}
		if(priceDiff != null){
			activityHotelGroup.setCurrencyId(Integer.valueOf(priceDiff.getCurrency().getValue()));
			Integer singlePriceUnit = null;
			if(StringUtils.isNotEmpty(priceDiff.getUnit().getValue())){
				switch (priceDiff.getUnit().getValue()) {  
				case "/人":  
					singlePriceUnit = 1;break;  
				case "/间":  
					singlePriceUnit = 2;break;  
				case "/晚":  
					singlePriceUnit = 3;break;  
				}  
			}
			activityHotelGroup.setSinglePriceUnit(singlePriceUnit);		
			if(StringUtils.isNotEmpty(priceDiff.getPrice())){
			activityHotelGroup.setSinglePrice(Double.valueOf(priceDiff.getPrice()));
			}
		}
		if(deposit != null){
			if(StringUtils.isNotEmpty(deposit.getCurrency().getValue())){
			activityHotelGroup.setFrontMoneyCurrencyId(Integer.valueOf(deposit.getCurrency().getValue()));
			}
			if(StringUtils.isNotEmpty(deposit.getPrice())){
			activityHotelGroup.setFrontMoney(Double.valueOf(deposit.getPrice()));
			}
		}
		if(CollectionUtils.isNotEmpty(trafficWays)){
			activityHotelGroup.setIslandWay(getIslandWay(trafficWays));
		}
		if(CollectionUtils.isNotEmpty(tradePrices)){
			activityHotelGroup.setActivityHotelGroupPriceList(getHotelGroupPrice(tradePrices));
		}
		if(CollectionUtils.isNotEmpty(houseTypes)){
			activityHotelGroup.setActivityHotelGroupRoomList(getHotelGroupRoom(houseTypes));
		}
		if(CollectionUtils.isNotEmpty(ctrlRooms)){
			activityHotelGroup.setActivityHotelGroupControlDetail(getControlDetail(ctrlRooms));
		}
		return activityHotelGroup;
	}
	
	public void initActivityHotelJsonInput(ActivityHotelGroup activityHotelGroup){
		this.setHotelUuid(activityHotelGroup.getActivityHotelUuid());
		this.setHotelGroupUuid(activityHotelGroup.getUuid());
		this.setNo(activityHotelGroup.getGroupCode());
		this.setDate(activityHotelGroup.getGroupOpenDate());
		this.setAirlines(activityHotelGroup.getAirline());
		if(activityHotelGroup.getControlNum()!=null){
		this.setCtrlRoom(activityHotelGroup.getControlNum().toString());
		}
		if(activityHotelGroup.getUncontrolNum()!=null){
			this.setUnCtrlRoom(activityHotelGroup.getUncontrolNum().toString());
		}
		if(activityHotelGroup.getPriorityDeduction()!=null){
			this.setCtrlRoomPriority(activityHotelGroup.getPriorityDeduction().toString());
		}
		this.setComment(activityHotelGroup.getMemo());
		this.setPriceDiff(tansfer2PriceDifferenceInput(activityHotelGroup));
		this.setDeposit(tansfer2DepositInput(activityHotelGroup));
		if(CollectionUtils.isNotEmpty(activityHotelGroup.getActivityHotelGroupControlDetail())){
		this.setCtrlRooms(tansfer2CtrlRoomsInput(activityHotelGroup.getActivityHotelGroupControlDetail()));
		}
		if(activityHotelGroup.getIslandWay()!=null){
		this.setTrafficWays(tansfer2TrafficWaysInput(activityHotelGroup.getIslandWay()));
		}
		if(CollectionUtils.isNotEmpty(activityHotelGroup.getActivityHotelGroupPriceList())){
		this.setTradePrices(tansfer2TradePricesInput(activityHotelGroup.getActivityHotelGroupPriceList()));
		}
		if(CollectionUtils.isNotEmpty(activityHotelGroup.getActivityHotelGroupRoomList())){
		this.setHouseTypes(tansfer2HouseTypesInput(activityHotelGroup.getActivityHotelGroupRoomList()));
		}
	}
	private List<HouseTypesInput> tansfer2HouseTypesInput(List<ActivityHotelGroupRoom> activityHotelGroupRoomList) {
		List<HouseTypesInput> inputList = new ArrayList<HouseTypesInput>();
		for(ActivityHotelGroupRoom groupRoom : activityHotelGroupRoomList){
			HouseTypesInput input = new HouseTypesInput();
			input.setGroupRoomUuid(groupRoom.getUuid());
			if(groupRoom.getNights()!=null){
				input.setNight(groupRoom.getNights().toString());
			}
			input.setHouseType(tansfer2HouseTypeInput(groupRoom.getHotelRoomUuid()));
			if(CollectionUtils.isNotEmpty(groupRoom.getActivityHotelGroupMealList())){
			input.setBaseMealTypes(tansfer2BaseMealTypesInput(groupRoom.getActivityHotelGroupMealList()));
			}
			inputList.add(input);
		}
		return inputList;
	}
	private List<BaseMealTypesInput> tansfer2BaseMealTypesInput(List<ActivityHotelGroupMeal> activityHotelGroupMealList) {
		List<BaseMealTypesInput> inputList = new ArrayList<BaseMealTypesInput>();
		for(ActivityHotelGroupMeal groupMeal : activityHotelGroupMealList){
			BaseMealTypesInput input = new BaseMealTypesInput();
			input.setGroupMealUuid(groupMeal.getUuid());
			input.setMealType(tansfer2MealTypeInput(groupMeal.getHotelMealUuid()));
			if(CollectionUtils.isNotEmpty(groupMeal.getActivityHotelGroupMealsRiseList())){
			input.setUpMealTypes(tansfer2UpMealTypesInput(groupMeal.getActivityHotelGroupMealsRiseList()));
			}
			inputList.add(input);
		}
		return inputList;
	}
	private List<UpMealTypesInput> tansfer2UpMealTypesInput(List<ActivityHotelGroupMealRise> activityHotelGroupMealsRiseList) {
		List<UpMealTypesInput> inputList = new ArrayList<UpMealTypesInput>();
		for(ActivityHotelGroupMealRise groupMealRise : activityHotelGroupMealsRiseList){
			UpMealTypesInput input = new UpMealTypesInput();
			input.setGroupMealRiseUuid(groupMealRise.getUuid());
			if(groupMealRise.getPrice()!= null){
			input.setPrice(groupMealRise.getPrice().toString());
			}
			input.setMealType(tansfer2MealTypeInput(groupMealRise.getHotelMealUuid()));
			if(groupMealRise.getCurrencyId()!= null){
				input.setCurrency(tansfer2CurrencyInput(groupMealRise.getCurrencyId().toString()));
			}
			inputList.add(input);
		}
		return inputList;
	}
	private MealTypeInput tansfer2MealTypeInput(String hotelMealUuid) {
		MealTypeInput input = new MealTypeInput();
		input.setValue(hotelMealUuid);
		return input;
	}
	private HouseTypeInput tansfer2HouseTypeInput(String hotelRoomUuid) {
		HouseTypeInput input = new HouseTypeInput();
		input.setValue(hotelRoomUuid);
		return input;
	}
	private List<TradePricesInput> tansfer2TradePricesInput(List<ActivityHotelGroupPrice> activityHotelGroupPriceList) {
		List<TradePricesInput> inputList = new ArrayList<TradePricesInput>();
		for(ActivityHotelGroupPrice groupPrice :activityHotelGroupPriceList){
			TradePricesInput input = new TradePricesInput();
			if(groupPrice.getCurrencyId()!=null){
			input.setCurrency(tansfer2CurrencyInput(groupPrice.getCurrencyId().toString()));
			}
			input.setGroupPriceUuid(groupPrice.getUuid());
			if(groupPrice.getPrice()!=null){
			input.setPrice(groupPrice.getPrice().toString());
			}
			input.setUuid(groupPrice.getType());
			inputList.add(input);
		}
		return inputList;
	}
	private List<TrafficWaysInput> tansfer2TrafficWaysInput(String islandWay) {
		List<TrafficWaysInput> inputList = new ArrayList<TrafficWaysInput>();
		if(islandWay.contains(";")){
			String[] islandWayArray = islandWay.split(";");
			for(String islandwayuuid : islandWayArray){
			TrafficWaysInput input = new TrafficWaysInput();
			input.setTrafficWaysval(islandwayuuid);
			inputList.add(input);
			}
		}else{
			TrafficWaysInput input = new TrafficWaysInput();
			input.setTrafficWaysval(islandWay);
			inputList.add(input);
		}
		return inputList;
	}
	private List<CtrlRoomsInput> tansfer2CtrlRoomsInput(List<ActivityHotelGroupControlDetail> activityHotelGroupControlDetail) {
		List<CtrlRoomsInput> inputList = new ArrayList<CtrlRoomsInput>();
		for(ActivityHotelGroupControlDetail controlDetail :activityHotelGroupControlDetail){
			CtrlRoomsInput input = new CtrlRoomsInput();
			input.setControlDetailUuid(controlDetail.getUuid());
			if(controlDetail.getNum() !=null){
			input.setNum(controlDetail.getNum().toString());
			}
			input.setUuid(controlDetail.getHotelControlDetailUuid());
			inputList.add(input);
		}
		return inputList;
	}
	private DepositInput tansfer2DepositInput(ActivityHotelGroup activityHotelGroup) {
		DepositInput input = new DepositInput();
		if(activityHotelGroup.getFrontMoney()!=null){
		input.setPrice(activityHotelGroup.getFrontMoney().toString());
		}
		if(activityHotelGroup.getFrontMoneyCurrencyId()!=null){
		input.setCurrency(tansfer2CurrencyInput(activityHotelGroup.getFrontMoneyCurrencyId().toString()));
		}
		return input;
	}
	private CurrencyInput tansfer2CurrencyInput(String uuid) {
		CurrencyInput input = new CurrencyInput();
		input.setValue(uuid);
		return input;
	}
	public PriceDifferenceInput tansfer2PriceDifferenceInput(ActivityHotelGroup activityHotelGroup){
		PriceDifferenceInput input = new PriceDifferenceInput();
		if(activityHotelGroup.getSinglePrice()!=null){
		    input.setPrice(activityHotelGroup.getSinglePrice().toString());
		}
		if(activityHotelGroup.getSinglePriceUnit()!=null){
		    input.setUnit(tansfer2UnitInput(activityHotelGroup.getSinglePriceUnit().toString()));
		}
		if(activityHotelGroup.getCurrencyId()!=null){
			input.setCurrency(tansfer2CurrencyInput(activityHotelGroup.getCurrencyId().toString()));
		}
		return input;
	}
	public UnitInput tansfer2UnitInput(String uuid){
		UnitInput input = new UnitInput();
		input.setValue(uuid);
		return input;
	}
	
	
	/*public static void main(String[] args){ 
		  String json = "{\"NO\" : \"T001\",\"date\" : \"2015-06-16\",\"houseTypes\" : [ {\"houseType\" : \"水上屋(2B+1C)\",\"houseTypeText\" : \"水上屋(2B+1C)\",\"night\" : \"1\"	}, {\"houseType\" : \"沙滩屋(2B+2C)\",\"houseTypeText\" : \"沙滩屋(2B+2C)\",\"night\" : \"3\"	} ],\"mealTypes\" : [ {	\"mealType\" : \"BB\",\"mealTypeText\" : \"BB\",\"upMealTypes\" : [ {	\"upMealType\" : \"BB\",\"upMealTypeText\" : \"BB\",\"currency\" : \"23\",\"currencyText\" : \"$\",\"price\" : \"50\"}, {\"upMealType\" : \"HB\",\"upMealTypeText\" : \"HB\",\"currency\" : \"22\",\"currencyText\" : \"$\",\"price\" : \"100\"} ]}, {\"mealType\" : \"HB\",\"mealTypeText\" : \"HB\",\"upMealTypes\" : []} ],\"trafficWays\" : [ \"水飞\", \"内飞\" ],	\"airlines\" : \"GH001\",	\"tradePrices\" : [ {	\"type\" : \"成人\",\"currency\" : \"33\",\"currencyText\" : \"￥\",\"price\" : \"100\"	}, {\"type\" : \" 第三人 \",\"currency\" : \"55\",\"currencyText\" : \"￥\",\"price\" : \"50\"}, {\"type\" : \" 儿童 \",\"currency\" : \"66\",\"currencyText\" : \"￥\",\"price\" : \"30\"	}, {\"type\" : \" 婴儿 \",\"currency\" : \"77\",\"currencyText\" : \"￥\",\"price\" : \"20\"} ],\"usedRoom\" : 0,	\"ctrlRoom\" : 10,\"unCtrlRoom\" : 30,\"ctrlRoomPriority\" : 1,	\"priceDifference\" : {	\"currency\" : \"12\",\"currencyText\" : \"$\",\"price\" : \"50\",\"unit\" : \"1\",\"unitText\" : \"/间\"},\"deposit\" : {\"currency\" : \"99\",\"currencyText\" : \"￥\",\"price\" : \"200\"},\"comment\" : \"测试一\"}";
		  String jsonTwo ="{\"NO\":\"123\",\"date\":\"2015-06-23\",\"houseTypes\":[{\"houseType\":{\"value\":\"222\",\"text\":\"水上屋(2B+1C)\"},\"night\":\"2\",\"baseMealTypes\":[{\"mealType\":{\"value\":\"33\",\"text\":\"BB\"},\"upMealTypes\":[{\"mealType\":{\"value\":\"33\",\"text\":\"BB\"},\"currency\":{\"value\":\"1\",\"text\":\"$\"},\"price\":\"23\"}]}]},{\"houseType\":{\"value\":\"44\",\"text\":\"水上屋(2B+1C)\"},\"night\":\"2\",\"baseMealTypes\":[{\"mealType\":{\"value\":\"55\",\"text\":\"BB\"},\"upMealTypes\":[{\"mealType\":{\"value\":\"55\",\"text\":\"BB\"},\"currency\":{\"value\":\"5\",\"text\":\"$\"},\"price\":\"323\"}]}]}],\"trafficWays\":[{\"trafficWaystext\":\"水飞\",\"trafficWaysval\":\"00\"},{\"trafficWaystext\":\"内飞\",\"trafficWaysval\":\"01\"}],\"airlines\":\"23232,34343\",\"tradePrices\":[{\"type\":\"成人\",\"currency\":{\"value\":\"￥\",\"text\":\"￥\"},\"price\":\"3\"},{\"type\":\" 第三人 \",\"currency\":{\"value\":\"￥\",\"text\":\"￥\"},\"price\":\"3\"},{\"type\":\" 儿童 \",\"currency\":{\"value\":\"￥\",\"text\":\"￥\"},\"price\":\"3\"},{\"type\":\" 婴儿 \",\"currency\":{\"value\":\"￥\",\"text\":\"￥\"},\"price\":\"3\"}],\"ctrlRoom\":3,\"unCtrlRoom\":33,\"ctrlRoomPriority\":0,\"priceDiff\":{\"currency\":{\"value\":\"$\",\"text\":\"$\"},\"price\":\"3\",\"unit\":{\"value\":\"/人\",\"text\":\"/人\"}},\"deposit\":{\"currency\":{\"value\":\"￥\",\"text\":\"￥\"},\"price\":\"3\"},\"comment\":\"333\"}";
		  ActivityHotelJsonBeanInput info = JSON.parseObject(jsonTwo,ActivityHotelJsonBeanInput.class);
		  info.transfer2ActivityHotelGroup();
		  System.out.println(info);
		 } */
	
}
