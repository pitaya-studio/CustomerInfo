package com.trekiz.admin.modules.island.input;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;

public class ActivityIslandJsonBeanInput { 
	private String islandUUid;//activityIslandUuid 
	private String uuid;//activityIslandGroupUuid 
	private String no;//团号
	private String date;//出团日期
	private List<IslandRoomGroup> houseTypes;//房型
	private List<TrafficWay> trafficWays;//字符串以逗号间隔，上岛方式
	private IslandAirline airline;
	private String ctrlTicketPriority;//优先扣减
	private String predictCount;//预收人数
	private RoomPriceDifference priceDiff;//单房差
	private Deposit deposit;//需交订金
	private String comment;//备注
	private String status;//状态  这个在json串里面没有，需要新加
	
	//返回上岛方式字符串，多个值可以用';'分割
	public String getIslandWay(List<TrafficWay> trafficWays){
		String islandWay = "";
		if(CollectionUtils.isNotEmpty(trafficWays)){
			for(TrafficWay way :trafficWays){
				if(way!=null){
					islandWay +=way.getValue()+";";
				}
			}
		}
		return islandWay.substring(0, islandWay.lastIndexOf(";"));
	}
	public void initActivityIslandJsonInput(ActivityIslandGroup activityIslandGroup){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.setIslandUUid(activityIslandGroup.getActivityIslandUuid());
		this.setUuid(activityIslandGroup.getUuid());
		this.setNo(activityIslandGroup.getGroupCode());
		this.setDate(sdf.format(activityIslandGroup.getGroupOpenDate()).length()>10?sdf.format(activityIslandGroup.getGroupOpenDate()).substring(0,10):sdf.format(activityIslandGroup.getGroupOpenDate()));
		this.setHouseTypes(transfer2HouseTypes(activityIslandGroup.getActivityIslandGroupRoomList()));
		this.setTrafficWays(transfer2TrafficWay(activityIslandGroup.getIslandWay()));
		this.setAirline(transfer2IslandAirline(activityIslandGroup.getActivityIslandGroupAirlineList()));
		this.setCtrlTicketPriority(activityIslandGroup.getPriorityDeduction().toString());
		this.setPredictCount(activityIslandGroup.getAdvNumber()==null?null:activityIslandGroup.getAdvNumber().toString());
		
		this.setPriceDiff(getRoomPriceDifference(activityIslandGroup));
		this.setDeposit(getDeposit(activityIslandGroup));
		this.setComment(activityIslandGroup.getMemo());
	}
	
	public Deposit getDeposit(ActivityIslandGroup activityIslandGroup){
		Deposit deposit = new Deposit();
		Currency currency = new Currency();
		currency.setValue(activityIslandGroup.getFrontMoneyCurrencyId().toString());
		deposit.setPrice(activityIslandGroup.getFrontMoney()==null?null:activityIslandGroup.getFrontMoney().toString());
		deposit.setCurrency(currency);
		
		return deposit;
	}
	public RoomPriceDifference getRoomPriceDifference(ActivityIslandGroup activityIslandGroup){
		RoomPriceDifference pricediff = new RoomPriceDifference();
		UnitRoomPrice unit = new UnitRoomPrice();
		unit.setValue(activityIslandGroup.getSinglePriceUnit().toString());
		Currency currency = new Currency();
		currency.setValue(activityIslandGroup.getCurrencyId().toString());
		pricediff.setCurrency(currency);
		pricediff.setPrice(activityIslandGroup.getSinglePrice()==null?null:activityIslandGroup.getSinglePrice().toString());
		pricediff.setUnit(unit);
		
		return pricediff;
	}
	public List<TrafficWay> transfer2TrafficWay(String islandWay){
		List<TrafficWay> waylist =  new ArrayList<TrafficWay>();
		TrafficWay way = new TrafficWay();
		if(islandWay!=null && !"".equals(islandWay)){
			String[] wayarr = islandWay.split(";");
			for(int i=0;i<wayarr.length;i++){
				way = new TrafficWay();
				way.setValue(wayarr[i]);
				waylist.add(way);
			}
		}
		
		return waylist;
	}
	
	/**
	 * 保存房型、基础餐型、升级餐型的相关列表
	 * @param islandRoomList
	 * @return
	 */
	private List<IslandRoomGroup> transfer2HouseTypes(List<ActivityIslandGroupRoom> islandRoomList){
		List<IslandRoomGroup> Roomlist = new ArrayList<IslandRoomGroup>();
		List<BaseMealTypesGroup> baseMealTypes = new ArrayList<BaseMealTypesGroup>();
		List<UpMealTypeGroup> upMealTypes = new ArrayList<UpMealTypeGroup>();
		IslandRoomGroup room = new IslandRoomGroup();
		BaseMealTypesGroup baseMeal = new BaseMealTypesGroup();//存放的是页面上传输的实体
		HouseType houseType = new HouseType();
		MealType mealType = new MealType();
		UpMealTypeGroup upMeal = new UpMealTypeGroup();
		Currency currency = new Currency();
		
		ActivityIslandGroupRoom islandRoom = new ActivityIslandGroupRoom();
		ActivityIslandGroupMeal islandMeal = new ActivityIslandGroupMeal();
		ActivityIslandGroupMealRise islandRise = new ActivityIslandGroupMealRise();
		
		List<ActivityIslandGroupMeal> meallist = new ArrayList<ActivityIslandGroupMeal>();
		List<ActivityIslandGroupMealRise> riselist = new ArrayList<ActivityIslandGroupMealRise>();
		if(islandRoomList!=null && islandRoomList.size()>0){
			for(int i=0;i<islandRoomList.size();i++){
				islandRoom = islandRoomList.get(i);
				
				room = new IslandRoomGroup();
				baseMealTypes = new ArrayList<BaseMealTypesGroup>();
				
				houseType = new HouseType();
				houseType.setValue(islandRoom.getHotelRoomUuid());
				room.setHouseType(houseType);
				room.setNight(islandRoom.getNights()==null?null:islandRoom.getNights().toString());
				
				meallist = islandRoom.getActivityIslandGroupMealList();
				if(meallist!=null && meallist.size()>0){
					for(int j=0;j<meallist.size();j++){
						islandMeal = meallist.get(j);
						baseMeal = new BaseMealTypesGroup();
						upMealTypes = new ArrayList<UpMealTypeGroup>();
						
						mealType = new MealType();
						mealType.setValue(islandMeal.getHotelMealUuid());
						baseMeal.setMealType(mealType);
						riselist = islandMeal.getActivityIslandGroupMealRiseList();
						if(riselist!=null && riselist.size() > 0){
							for(int z=0;z<riselist.size();z++){
								islandRise = riselist.get(z);
								upMeal = new UpMealTypeGroup();
								currency = new Currency();
								mealType = new MealType();
								
								currency.setValue(islandRise.getCurrencyId().toString());
								mealType.setValue(islandRise.getHotelMealUuid());
								upMeal.setCurrency(currency);
								upMeal.setMealType(mealType);
								upMeal.setPrice(islandRise.getPrice()==null?"":islandRise.getPrice().toString());
								upMealTypes.add(upMeal);
							}
						}
						baseMeal.setUpMealTypes(upMealTypes);
						baseMealTypes.add(baseMeal);
					}
					
				}
				room.setBaseMealTypes(baseMealTypes);
				
				Roomlist.add(room);
			}
		}
		
		
		return Roomlist;
	}
	private IslandAirline transfer2IslandAirline(List<ActivityIslandGroupAirline> airlineList){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		IslandAirline islandAirline = new IslandAirline();
		
		IslandFlight flight = new IslandFlight();
		List<String> ctrlTickets = new ArrayList<String>();
		List<String> unCtrlTickets = new ArrayList<String>();
		List<String> remainTickets = new ArrayList<String>();
		List<SpaceGrade> spaceGradeList = new ArrayList<SpaceGrade>();
		List<TouristType> touristTypeList = new ArrayList<TouristType>();
		SpaceGrade spaceGrade = new SpaceGrade();//舱位等级
		TouristType touristType = new TouristType();//游客类型
		Integer remainNum = 0;
		Integer ticketNum = 0;
		List<ActivityIslandGroupPrice> pricelist = new ArrayList<ActivityIslandGroupPrice>();
		List<ActivityIslandGroupPrice> allpricelist = new ArrayList<ActivityIslandGroupPrice>();
		ActivityIslandGroupAirline airline = new ActivityIslandGroupAirline();
		ActivityIslandGroupPrice price = new ActivityIslandGroupPrice();
		
		if(airlineList != null && airlineList.size()>0){
			airline = airlineList.get(0);
			islandAirline.setValue(airline.getAirline());
			flight.setValue(airline.getFlightNumber());
			flight.setDays(airline.getDayNum()==null?"":airline.getDayNum().toString());
			flight.setEnd(airline.getArriveTime()==null?"":sdf.format(airline.getArriveTime()));
			flight.setStart(airline.getDepartureTime()==null?"":sdf.format(airline.getDepartureTime()));
			//flight.setText(text);
			islandAirline.setFlight(flight);
			
			for(int i=0;i<airlineList.size();i++){
				airline = airlineList.get(i);
				pricelist =  new ArrayList<ActivityIslandGroupPrice>();
				pricelist = airline.getActivityIslandGroupPriceList();//根据舱位等级取的价格表
				
				if(i==0){//不同的舱位等级，游客类型是一样的，只取一次即可
					if(pricelist!=null && pricelist.size()>0){
						for(int j=0;j<pricelist.size();j++){
							price = pricelist.get(j);
							touristType = new TouristType(); 
							touristType.setValue(price.getType());
							touristTypeList.add(touristType);
						}
						islandAirline.setTouristType(touristTypeList);
					 }
				 }
				
				spaceGrade = new SpaceGrade();
				ctrlTickets.add(airline.getControlNum()==null?"":airline.getControlNum().toString());
				unCtrlTickets.add(airline.getUncontrolNum()==null?"":airline.getUncontrolNum().toString());
				remainTickets.add(airline.getRemNumber()==null?"":airline.getRemNumber().toString());
				spaceGrade.setValue(airline.getSpaceLevel());//舱位等级
				spaceGradeList.add(spaceGrade);
				ticketNum += (airline.getControlNum()==null?0:airline.getControlNum()) + (airline.getUncontrolNum()==null?0:airline.getUncontrolNum());
				remainNum += airline.getRemNumber()==null?0:airline.getRemNumber();
				allpricelist.addAll(pricelist);
			}
			
			//根据游客类型进行存价格,里面是舱位等级
			
			Map<Object, List<IslandPrice>> islandpriceMap = new LinkedHashMap<Object, List<IslandPrice>>();
			Map<Object, List<ActivityIslandGroupPrice>> travelerTypeMap = BeanUtil.groupByKeyString("type", allpricelist);//游客类型进行分
			
			Set<Entry<Object, List<ActivityIslandGroupPrice>>> set = travelerTypeMap.entrySet();
			for(Entry<Object, List<ActivityIslandGroupPrice>> entry : set){
				List<ActivityIslandGroupPrice> entrylist = entry.getValue();
				List<IslandPrice> ilpList = new ArrayList<IslandPrice>();
				for(ActivityIslandGroupPrice aisgp:entrylist){
					IslandPrice aigpobj = new IslandPrice();
					aigpobj.setPrice(aisgp.getPrice()==null?"":aisgp.getPrice().toString());
					aigpobj.setAirlineuuid(aisgp.getActivityIslandGroupAirlineUuid());
					Currency currencytest = new Currency();
					currencytest.setText("");
					currencytest.setValue(aisgp.getCurrencyId().toString());
					aigpobj.setCurrency(currencytest);
					aigpobj.setTypeUuid(aisgp.getType());
					ilpList.add(aigpobj);	
				}
				islandpriceMap.put(entry.getKey(), ilpList);
			}
			
			List<IslandPrices> ilpssTest = new ArrayList<IslandPrices>();
			
			Set<Object> keyset = islandpriceMap.keySet();
			for(Object obj:keyset){
				IslandPrices istest = new IslandPrices();
				istest.setIslandprice(islandpriceMap.get(obj));
				ilpssTest.add(istest);
			}
			
			
			islandAirline.setTouristType(touristTypeList);
			islandAirline.setPrices(ilpssTest);
			islandAirline.setCtrlTickets(ctrlTickets);
			islandAirline.setUnCtrlTickets(unCtrlTickets);
			islandAirline.setRemainTickets(remainTickets);
			islandAirline.setSpaceGrade(spaceGradeList);
			
			Amount amount = new Amount();
			amount.setTicket(ticketNum.toString());
			amount.setRemainTicket(remainNum.toString());
			islandAirline.setAmount(amount);
		}
		
		return islandAirline;
	}
	/**
	 * 生成对应的二级表
	 * 将json串转化到对应的对象上
	 * @return
	 */
	public ActivityIslandGroup getActivityIslandGroup(){
		ActivityIslandGroup islandGroup = new ActivityIslandGroup();
		if(StringUtils.isNotEmpty(islandUUid)){//产品的UUId
			islandGroup.setActivityIslandUuid(islandUUid);
		}
		if(StringUtils.isNotEmpty(uuid)){//团期的UUId
			islandGroup.setUuid(uuid);
		}
		if(StringUtils.isNotEmpty(no)){//团号
			islandGroup.setGroupCode(no);
		}
		if(StringUtils.isNotEmpty(date)){//团期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				islandGroup.setGroupOpenDate(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(trafficWays!=null&&trafficWays.size() >0){
			//this.getIslandWay(trafficWays);
			islandGroup.setIslandWay(this.getIslandWay(trafficWays));
		}
		if(StringUtils.isNotEmpty(predictCount)){
			if(predictCount.indexOf("/")>0){
				islandGroup.setAdvNumber(Integer.valueOf(predictCount.substring(0, predictCount.indexOf("/")).trim()));
			}else if(predictCount.indexOf("/")==0){
				islandGroup.setAdvNumber(0);
			}else{
				islandGroup.setAdvNumber(Integer.valueOf(predictCount.trim()));
			}
		}
		if(priceDiff!=null){
			if(priceDiff.getPrice()!=null&&!"".equals(priceDiff.getPrice())){
				islandGroup.setSinglePrice(Double.valueOf(priceDiff.getPrice()));
			}
			Currency currency = priceDiff.getCurrency();
			if(currency!=null&&currency.getValue()!=null){
				islandGroup.setCurrencyId(Integer.valueOf(currency.getValue().trim()));
			}
			UnitRoomPrice unit = priceDiff.getUnit();
			if(unit!=null&&unit.getValue()!=null){
				islandGroup.setSinglePriceUnit(Integer.valueOf(unit.getValue().trim()));
			}
		}
		if(StringUtils.isNotEmpty(ctrlTicketPriority)){
			islandGroup.setPriorityDeduction(Integer.valueOf(ctrlTicketPriority.trim()));
		}
		if(deposit!=null){
			Currency currency = deposit.getCurrency();
			if(StringUtils.isNotEmpty(deposit.getPrice())){
				islandGroup.setFrontMoney(Double.valueOf(deposit.getPrice()));
			}
			if(currency!=null && StringUtils.isNotEmpty(currency.getValue())){
				islandGroup.setFrontMoneyCurrencyId(Integer.valueOf(currency.getValue().trim()));
			}
		}
		
		if(StringUtils.isNotEmpty(comment)){
			islandGroup.setMemo(comment);
		}
		islandGroup.setStatus(status);
		islandGroup.setLockStatus(0);
		islandGroup.setForcastStatus("00");
		islandGroup.setActivityIslandGroupAirlineList(getActivityIslandGroupAirline(airline));
		islandGroup.setJsonActivityIslandGroupPriceList(getActivityIslandGroupPrice(airline));
		islandGroup.setActivityIslandGroupRoomList(getActivityIslandGroupRoom(houseTypes));
		
		return islandGroup;
	}
	/**
	 * 返回三级表  activity_Island_group_room类数据
	 * @param roomlist
	 * @return
	 */
	public List<ActivityIslandGroupRoom> getActivityIslandGroupRoom(List<IslandRoomGroup> roomlist){
		List<ActivityIslandGroupRoom> groupRoomList  = new ArrayList<ActivityIslandGroupRoom>();
		ActivityIslandGroupRoom groupRoom = new ActivityIslandGroupRoom();
		if(roomlist!=null&&roomlist.size()>0){
			for(IslandRoomGroup room : roomlist){
				groupRoom = new ActivityIslandGroupRoom();
				if(room!=null){
					HouseType houseType = room.getHouseType();
					if(houseType!=null){
						groupRoom.setHotelRoomUuid(room.getHouseType().getValue());
					}
					
					String night = room.getNight();
					if(night!=null&&!"".equals(night.trim())){
						groupRoom.setNights(Integer.valueOf(night.trim()));
					}
					groupRoom.setActivityIslandGroupMealList(getIslandGroupMeal(room.getBaseMealTypes()));
				}
				
				groupRoomList.add(groupRoom);
			}
		}
		
		return groupRoomList;
	}
	/**返回room下的四级表  
	 * 返回activity_Island_group_meal类数据
	 * @param list
	 * @return
	 */
	public List<ActivityIslandGroupMeal> getIslandGroupMeal(List<BaseMealTypesGroup> baseMealList){
		List<ActivityIslandGroupMeal> groupMealList = new ArrayList<ActivityIslandGroupMeal>();
		ActivityIslandGroupMeal baseMeal = new ActivityIslandGroupMeal();
		if(baseMealList!=null && baseMealList.size()>0){
			for(BaseMealTypesGroup mealTypes : baseMealList){
				baseMeal = new ActivityIslandGroupMeal();
				
				if(mealTypes!=null){
					MealType meal= mealTypes.getMealType();
					if(meal!=null){
						baseMeal.setHotelMealUuid(meal.getValue());
					}
					baseMeal.setActivityIslandGroupMealRiseList(getIslandGroupMealRise(mealTypes.getUpMealTypes()));
				}
				
				groupMealList.add(baseMeal);
			}
		}
		
		return groupMealList;
	}
	public List<ActivityIslandGroupMealRise> getIslandGroupMealRise(List<UpMealTypeGroup> upMealTypes){
		List<ActivityIslandGroupMealRise> groupRiseList = new ArrayList<ActivityIslandGroupMealRise>();
		ActivityIslandGroupMealRise rise = new ActivityIslandGroupMealRise();
		if(upMealTypes!=null && upMealTypes.size()>0){
			for(UpMealTypeGroup upMeal:upMealTypes){
				rise = new ActivityIslandGroupMealRise();
				if(upMeal!=null){
					MealType meal = upMeal.getMealType();
					if(meal!=null){
						rise.setHotelMealUuid(meal.getValue());
					}
					Currency currency = upMeal.getCurrency();
					if(currency!=null){
						if(currency.getValue()!=null){
							rise.setCurrencyId(Integer.valueOf(currency.getValue().trim()));
						}
						
					}
					
					String price = upMeal.getPrice();
					if(StringUtils.isNotBlank(price)){
						rise.setPrice(Double.valueOf(price));
					}
				}
				
				groupRiseList.add(rise);
			}
		}
		
		return groupRiseList;
	}
	/**
	 * 返回三级表  activity_Island_group_price类数据
	 * 该数据是一个二维的数组，外围根据游客类型进行划分，里面是根据舱位等级进行划分，
	 * 为了在后面将舱位等级的UUid赋值，该数组赋值之后还是一个二维的数组，不能用一维数组表示，否则后面没办法将UUid赋值进来
	 * @param airline
	 * @return
	 */
	public List<List<ActivityIslandGroupPrice>> getActivityIslandGroupPrice(IslandAirline airline){
		List<List<ActivityIslandGroupPrice>> tourlist = new ArrayList<List<ActivityIslandGroupPrice>>();
		List<ActivityIslandGroupPrice> spaceLevelList = new ArrayList<ActivityIslandGroupPrice>();
		ActivityIslandGroupPrice groupPrice = new ActivityIslandGroupPrice();
		IslandPrice islandPrice = new IslandPrice();
		if(airline!=null){
			List<TouristType> touristTypes = airline.getTouristType();//游客类型
			List<IslandPrices> prices = airline.getPrices();//根据舱位等级划分的，存放的价格的数组
			
			if(prices!=null && prices.size()>0){
				for(int i=0;i<prices.size();i++){//存放的是根据游客类型生成的不同舱位等级的价格不同的数组
					String touristType = null;
					List<IslandPrice> pricelist = new ArrayList<IslandPrice>();
					spaceLevelList = new ArrayList<ActivityIslandGroupPrice>();//每次循环一次就初始化一次
					
					IslandPrices islandPrices = prices.get(i);
					if(islandPrices!=null){
						pricelist = islandPrices.getIslandprice();
						for(int j=0;j<pricelist.size();j++){//这个是根据舱位等级的数组，里边存放的是该游客类型的所有舱位等级的价格
							
							islandPrice = pricelist.get(j);
							groupPrice = new ActivityIslandGroupPrice();
							if(islandPrice!=null){
								Currency currency = islandPrice.getCurrency();
								if(currency!=null){
									if(currency.getValue()!=null){
										groupPrice.setCurrencyId(Integer.valueOf(currency.getValue().trim()));
									}
								}
								
								if(islandPrice.getPrice()!=null&&StringUtils.isNotEmpty(islandPrice.getPrice())){
									groupPrice.setPrice(Double.valueOf(islandPrice.getPrice()));
								}
								
							}
							touristType = touristTypes.get(i).getValue();
							groupPrice.setType(touristType);
							spaceLevelList.add(groupPrice);
						}
					}
					//将一个舱位等级的作为一个数组list放到以游客类型为标志的外围的list中
					tourlist.add(spaceLevelList);
					
				}
			}
		}
		
		
		return tourlist;
	}
	/**
	 * 返回三级表
	 * 返回的是航班舱位等级的个数在数据库中保存
	 * @return
	 */
	public List<ActivityIslandGroupAirline> getActivityIslandGroupAirline(IslandAirline airline){
		List<ActivityIslandGroupAirline> airlineList = new ArrayList<ActivityIslandGroupAirline>();
		ActivityIslandGroupAirline airlinegroup = new ActivityIslandGroupAirline();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		if(airline!=null){
			List<SpaceGrade> gradelist = airline.getSpaceGrade();//舱位等级
			String springlevel = "";
			if(gradelist!=null&&gradelist.size()>0){//舱位等级
				for(int i=0;i<gradelist.size();i++){
					airlinegroup = new ActivityIslandGroupAirline();
					airlinegroup.setAirline(airline.getValue());
					IslandFlight flight = airline.getFlight();
					if(flight!=null){
						airlinegroup.setFlightNumber(flight.getValue());
						if(flight.getStart()!=null&&!"".equals(flight.getStart())){
							try {
								airlinegroup.setDepartureTime(sdf.parse(flight.getStart()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						if(flight.getEnd()!=null&&!"".equals(flight.getEnd())){
							try {
								airlinegroup.setArriveTime(sdf.parse(flight.getEnd()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						if(flight.getDays()!=null&&!"".equals(flight.getDays())){
							airlinegroup.setDayNum(Integer.valueOf(flight.getDays().trim()));
						}
						
					}
					if(gradelist.get(i)!=null){
						springlevel = gradelist.get(i).getValue();
					}
					airlinegroup.setSpaceLevel(springlevel);
					//update by WangXK 20151020 添加空指针的判断
					if(airline.getCtrlTickets()!=null &&airline.getCtrlTickets().get(i)!=null&&!"".equals(airline.getCtrlTickets().get(i))){
						airlinegroup.setControlNum(Integer.valueOf(airline.getCtrlTickets().get(i).trim()));
					}
					if(airline.getUnCtrlTickets()!=null&&airline.getUnCtrlTickets().get(i)!=null&&!"".equals(airline.getUnCtrlTickets().get(i))){
						airlinegroup.setUncontrolNum(Integer.valueOf(airline.getUnCtrlTickets().get(i).trim()));
					}
					if(airline.getRemainTickets()!=null&&airline.getRemainTickets().get(i)!=null&&!"".equals(airline.getRemainTickets().get(i))){
						airlinegroup.setRemNumber(Integer.valueOf(airline.getRemainTickets().get(i).trim()));
					}
					
					airlineList.add(airlinegroup);
				}
			}else{//没有舱位等级，则只生成一条数据，而不是List

				airlinegroup = new ActivityIslandGroupAirline();
				airlinegroup.setAirline(airline.getValue());
				//update by WangXK 20151020 添加空指针的判断
				if(airline.getCtrlTickets()!=null &&airline.getCtrlTickets().get(0)!=null&&!"".equals(airline.getCtrlTickets().get(0))){
					airlinegroup.setControlNum(Integer.valueOf(airline.getCtrlTickets().get(0).trim()));
				}
				if(airline.getUnCtrlTickets()!=null&&airline.getUnCtrlTickets().get(0)!=null&&!"".equals(airline.getUnCtrlTickets().get(0))){
					airlinegroup.setUncontrolNum(Integer.valueOf(airline.getUnCtrlTickets().get(0).trim()));
				}
				if(airline.getRemainTickets()!=null&&airline.getRemainTickets().get(0)!=null&&!"".equals(airline.getRemainTickets().get(0))){
					airlinegroup.setRemNumber(Integer.valueOf(airline.getRemainTickets().get(0).trim()));
				}
				airlineList.add(airlinegroup);
			}
			
		}
		
		return airlineList;
	}
	public String getIslandUUid() {
		return islandUUid;
	}

	public void setIslandUUid(String islandUUid) {
		this.islandUUid = islandUUid;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<IslandRoomGroup> getHouseTypes() {
		return houseTypes;
	}
	public void setHouseTypes(List<IslandRoomGroup> houseTypes) {
		this.houseTypes = houseTypes;
	}
	public List<TrafficWay> getTrafficWays() {
		return trafficWays;
	}
	public void setTrafficWays(List<TrafficWay> trafficWays) {
		this.trafficWays = trafficWays;
	}
	public IslandAirline getAirline() {
		return airline;
	}
	public void setAirline(IslandAirline airline) {
		this.airline = airline;
	}
	public String getCtrlTicketPriority() {
		return ctrlTicketPriority;
	}
	public void setCtrlTicketPriority(String ctrlTicketPriority) {
		this.ctrlTicketPriority = ctrlTicketPriority;
	}
	public String getPredictCount() {
		return predictCount;
	}
	public void setPredictCount(String predictCount) {
		this.predictCount = predictCount;
	}
	public RoomPriceDifference getPriceDiff() {
		return priceDiff;
	}
	public void setPriceDiff(RoomPriceDifference priceDiff) {
		this.priceDiff = priceDiff;
	}
	public Deposit getDeposit() {
		return deposit;
	}
	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}