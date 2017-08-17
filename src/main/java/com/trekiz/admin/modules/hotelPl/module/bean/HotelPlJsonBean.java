package com.trekiz.admin.modules.hotelPl.module.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlHolidayMeal;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandwayMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlMealrise;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlRisemealMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlRoomMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

public class HotelPlJsonBean {

	public static void main(String[] args) {
		//基本信息json对象生成
		//initBaseInfoJson();
		
		//酒店税金 json对象生成
//		initTaxPriceJson();
		
		//酒店房型价格 json对象生成
//		initHotelPlPriceJson();
		
		//交通费用json对象生成
//		initHotelPlIslandwayJson();
		
		//酒店价单升餐json对象生成
//		initHotelPlMealriseJson();
		
		//强制性节日餐 json对象生成
		initHolidayMealJson();
		
		//生成价单酒店房型价格 基础数据
//		initHotelPriceBaseInfo();
		
		//酒店价单备注json对象生成
//		initHotelPlMemoJson();
		
		//交通费用基础数据封装
//		initIslandWayBaseInfoJson();
		//升餐费用基础数据封装
//		initHotelMealBaseInfoJson();
		
	}
	
	public static void initBaseInfoJson(){
		HotelPl hotelPl = new HotelPl();
		hotelPl.setPurchaseType(1);
		hotelPl.setUuid("酒店价单uuid");
		hotelPl.setName("酒店价单名称");
		hotelPl.setSupplierInfoId(2);
		hotelPl.setSupplierInfoText("地接社供应商显示文本");
		hotelPl.setCurrencyId(2);
		hotelPl.setCurrencyMark("币种显示符号");
		hotelPl.setCurrencyText("币种符号显示文本");
		hotelPl.setPosition(1);
		hotelPl.setCountry("国家uuid");
		hotelPl.setCountryText("国家显示文本");
		hotelPl.setAreaType(1);
		hotelPl.setIslandUuid("岛屿uuid");
		hotelPl.setIslandText("岛屿名称显示文本");
		hotelPl.setHotelUuid("酒店UUID");
		hotelPl.setHotelText("酒店名称显示文本");
		hotelPl.setHotelGroup("酒店集团");
		hotelPl.setHotelStar("酒店星级");
		hotelPl.setHotelAddress("酒店地址");
		hotelPl.setContactPhone("联系电话");
		hotelPl.setMixliveCurrencyId(2);
		hotelPl.setMixliveCurrencyText("混住费用币种符号显示文本");
		hotelPl.setMixliveAmount(66.0);
		hotelPl.setGalamealMemo("节日餐备注");
		hotelPl.setMemo("酒店备注");
		hotelPl.setUpdateByText("更改人显示文本");
		hotelPl.setUpdateDate(new Date());
		hotelPl.setCreateByText("创建人文本显示");
		

		System.out.println(JSON.toJSONStringWithDateFormat(hotelPl, "yyyy-MM-dd"));
	}
	
	public static void initTaxPriceJson() {
		//酒店价单税金价格信息
		List<HotelPlTaxPrice> hotelPlTaxPriceList = new ArrayList<HotelPlTaxPrice>();
		HotelPlTaxPrice hotelPlTaxPrice = new HotelPlTaxPrice();
		hotelPlTaxPrice.setHotelPlUuid("酒店价单uuid");
		hotelPlTaxPrice.setTaxType(1);
		hotelPlTaxPrice.setTaxName("政府税");
		hotelPlTaxPrice.setStartDate(new Date());
		hotelPlTaxPrice.setEndDate(new Date());
		hotelPlTaxPrice.setCurrencyId(2);
		hotelPlTaxPrice.setChargeTypeText("收费类型显示文本");
		hotelPlTaxPrice.setAmount(66.0);
		hotelPlTaxPrice.setChargeType(1);
		hotelPlTaxPrice.setUuid("酒店税金uuid");
		hotelPlTaxPriceList.add(hotelPlTaxPrice);
		

		HotelPlTaxPrice hotelPlTaxPrice1 = new HotelPlTaxPrice();
		hotelPlTaxPrice1.setHotelPlUuid("酒店价单uuid");
		hotelPlTaxPrice1.setTaxType(1);
		hotelPlTaxPrice1.setTaxName("服务税");
		hotelPlTaxPrice1.setStartDate(new Date());
		hotelPlTaxPrice1.setEndDate(new Date());
		hotelPlTaxPrice1.setCurrencyId(2);
		hotelPlTaxPrice1.setChargeTypeText("收费类型显示文本");
		hotelPlTaxPrice1.setAmount(66.0);
		hotelPlTaxPrice1.setChargeType(1);
		hotelPlTaxPrice1.setUuid("酒店税金uuid");
		hotelPlTaxPriceList.add(hotelPlTaxPrice1);
		
		//酒店价单税金例外信息
		List<HotelPlTaxException> hotelPlTaxExceptionList = new ArrayList<HotelPlTaxException>();
		HotelPlTaxException hotelPlTaxException1 = new HotelPlTaxException();
		hotelPlTaxException1.setUuid("税金例外uuid");
		hotelPlTaxException1.setHotelPlUuid("酒店价单uuid");
		hotelPlTaxException1.setExceptionType(1);
		hotelPlTaxException1.setExceptionName("例外类型名称(1、房型；2、餐型；3、交通)");
		hotelPlTaxException1.setStartDate(new Date());
		hotelPlTaxException1.setEndDate(new Date());
		hotelPlTaxException1.setTaxType("1");
		hotelPlTaxException1.setTaxTypeText("税费类型显示文本");
		hotelPlTaxException1.setTravelType("游客类型uuid多个用“;”分隔");
		hotelPlTaxException1.setTravelTypeText("游客类型显示文本");
		
		HotelPlTaxException hotelPlTaxException2 = new HotelPlTaxException();
		hotelPlTaxException2.setUuid("税金例外uuid");
		hotelPlTaxException2.setHotelPlUuid("酒店价单uuid");
		hotelPlTaxException2.setExceptionType(2);
		hotelPlTaxException2.setExceptionName("例外类型名称(房型；餐型；交通)");
		hotelPlTaxException2.setStartDate(new Date());
		hotelPlTaxException2.setEndDate(new Date());
		hotelPlTaxException2.setTaxType("1");
		hotelPlTaxException2.setTaxTypeText("税费类型显示文本");
		hotelPlTaxException2.setTravelType("游客类型uuid多个用“;”分隔");
		hotelPlTaxException2.setTravelTypeText("游客类型显示文本");
		
		
		hotelPlTaxExceptionList.add(hotelPlTaxException1);
		hotelPlTaxExceptionList.add(hotelPlTaxException2);
		
		
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlTaxPriceList, "yyyy-MM-dd"));
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlTaxExceptionList, "yyyy-MM-dd"));
	}
	
	public static void initHolidayMealJson() {
		Map<String, List<HotelPlHolidayMeal>> hotelPlHolidayMealMap = new HashMap<String, List<HotelPlHolidayMeal>>();
		
		List<HotelPlHolidayMeal> hotelPlHolidayMealList1 = new ArrayList<HotelPlHolidayMeal>();
		HotelPlHolidayMeal hotelPlHolidayMeal1 = new HotelPlHolidayMeal();
		hotelPlHolidayMeal1.setUuid("酒店价单节日餐uuid");
		hotelPlHolidayMeal1.setHotelPlUuid("酒店价单uuid");
		hotelPlHolidayMeal1.setHolidayMealName("节日餐名称");
		hotelPlHolidayMeal1.setHolidayMealUuid("节日餐uuid");
		hotelPlHolidayMeal1.setHotelMealUuid("酒店餐型uuid");
		hotelPlHolidayMeal1.setHotelMealText("酒店餐型显示文本");
		hotelPlHolidayMeal1.setStartDate(new Date());
		hotelPlHolidayMeal1.setEndDate(new Date());
		hotelPlHolidayMeal1.setTravelerTypeUuid("游客类型uuid");
		hotelPlHolidayMeal1.setCurrencyId(2);
		hotelPlHolidayMeal1.setCurrencyMark("币种符号");
		hotelPlHolidayMeal1.setAmount(66.0);
		
		hotelPlHolidayMealList1.add(hotelPlHolidayMeal1);

		HotelPlHolidayMeal hotelPlHolidayMeal2 = new HotelPlHolidayMeal();
		hotelPlHolidayMeal2.setUuid("酒店价单节日餐uuid");
		hotelPlHolidayMeal2.setHotelPlUuid("酒店价单uuid");
		hotelPlHolidayMeal2.setHolidayMealName("节日餐名称");
		hotelPlHolidayMeal2.setHolidayMealUuid("节日餐uuid");
		hotelPlHolidayMeal2.setHotelMealUuid("酒店餐型uuid");
		hotelPlHolidayMeal2.setHotelMealText("酒店餐型显示文本");
		hotelPlHolidayMeal2.setStartDate(new Date());
		hotelPlHolidayMeal2.setEndDate(new Date());
		hotelPlHolidayMeal2.setTravelerTypeUuid("游客类型uuid");
		hotelPlHolidayMeal2.setCurrencyId(2);
		hotelPlHolidayMeal2.setCurrencyMark("币种符号");
		hotelPlHolidayMeal2.setAmount(66.0);
		
		hotelPlHolidayMealList1.add(hotelPlHolidayMeal2);
		
		hotelPlHolidayMealMap.put("节日餐名称1|节日餐uuid1", hotelPlHolidayMealList1);

		List<HotelPlHolidayMeal> hotelPlHolidayMealList2 = new ArrayList<HotelPlHolidayMeal>();
		HotelPlHolidayMeal hotelPlHolidayMeal3 = new HotelPlHolidayMeal();
		hotelPlHolidayMeal3.setUuid("酒店价单节日餐uuid");
		hotelPlHolidayMeal3.setHotelPlUuid("酒店价单uuid");
		hotelPlHolidayMeal3.setHolidayMealName("节日餐名称");
		hotelPlHolidayMeal3.setHolidayMealUuid("节日餐uuid");
		hotelPlHolidayMeal3.setHotelMealUuid("酒店餐型uuid");
		hotelPlHolidayMeal3.setHotelMealText("酒店餐型显示文本");
		hotelPlHolidayMeal3.setStartDate(new Date());
		hotelPlHolidayMeal3.setEndDate(new Date());
		hotelPlHolidayMeal3.setTravelerTypeUuid("游客类型uuid");
		hotelPlHolidayMeal3.setCurrencyId(2);
		hotelPlHolidayMeal3.setCurrencyMark("币种符号");
		hotelPlHolidayMeal3.setAmount(66.0);
		
		hotelPlHolidayMealList2.add(hotelPlHolidayMeal3);

		HotelPlHolidayMeal hotelPlHolidayMeal4 = new HotelPlHolidayMeal();
		hotelPlHolidayMeal4.setUuid("酒店价单节日餐uuid");
		hotelPlHolidayMeal4.setHotelPlUuid("酒店价单uuid");
		hotelPlHolidayMeal4.setHolidayMealName("节日餐名称");
		hotelPlHolidayMeal4.setHolidayMealUuid("节日餐uuid");
		hotelPlHolidayMeal4.setHotelMealUuid("酒店餐型uuid");
		hotelPlHolidayMeal4.setHotelMealText("酒店餐型显示文本");
		hotelPlHolidayMeal4.setStartDate(new Date());
		hotelPlHolidayMeal4.setEndDate(new Date());
		hotelPlHolidayMeal4.setTravelerTypeUuid("游客类型uuid");
		hotelPlHolidayMeal4.setCurrencyId(2);
		hotelPlHolidayMeal4.setCurrencyMark("币种符号");
		hotelPlHolidayMeal4.setAmount(66.0);
		
		hotelPlHolidayMealList2.add(hotelPlHolidayMeal4);

		hotelPlHolidayMealMap.put("节日餐名称2|节日餐uuid2", hotelPlHolidayMealList2);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlHolidayMealMap, "yyyy-MM-dd"));
	}
	
	public static void initHotelPlPriceJson() {
		Map<String, List<HotelPlPrice>> map = new LinkedHashMap<String, List<HotelPlPrice>>();
		List<HotelPlPrice> hotelPlPriceList1 = new ArrayList<HotelPlPrice>();
		HotelPlPrice hotelPlPrice1 = new HotelPlPrice();
		hotelPlPrice1.setHotelPlUuid("酒店价单uuid");
		hotelPlPrice1.setHotelRoomUuid("酒店房型uuid");
		hotelPlPrice1.setHotelRoomText("酒店房型显示文本");
		hotelPlPrice1.setHotelMealUuids("酒店餐型uuids");
		hotelPlPrice1.setStartDate(new Date());
		hotelPlPrice1.setEndDate(new Date());
		hotelPlPrice1.setHotelGuestTypeUuid("酒店住客类型uuid");
		hotelPlPrice1.setCurrencyId(2);
		hotelPlPrice1.setCurrencyMark("币种符号");
		hotelPlPrice1.setAmount(66.0);
		hotelPlPrice1.setPriceType(0);
		hotelPlPrice1.setUuid("酒店价单价格uuid");
		hotelPlPrice1.setOccupancyRate("酒店容住率");
		hotelPlPriceList1.add(hotelPlPrice1);

		HotelPlPrice hotelPlPrice2 = new HotelPlPrice();
		hotelPlPrice2.setHotelPlUuid("酒店价单uuid");
		hotelPlPrice2.setHotelRoomUuid("酒店房型uuid");
		hotelPlPrice2.setHotelRoomText("酒店房型显示文本");
		hotelPlPrice2.setHotelMealUuids("酒店餐型uuids");
		hotelPlPrice2.setStartDate(new Date());
		hotelPlPrice2.setEndDate(new Date());
		hotelPlPrice2.setHotelGuestTypeUuid("酒店住客类型uuid");
		hotelPlPrice2.setCurrencyId(2);
		hotelPlPrice2.setCurrencyMark("币种符号");
		hotelPlPrice2.setAmount(66.0);
		hotelPlPrice2.setPriceType(0);
		hotelPlPrice2.setUuid("酒店价单价格uuid");
		hotelPlPrice2.setOccupancyRate("酒店容住率");
		hotelPlPriceList1.add(hotelPlPrice1);
		

		List<HotelPlPrice> hotelPlPriceList2 = new ArrayList<HotelPlPrice>();
		
		HotelPlPrice hotelPlPrice3 = new HotelPlPrice();
		hotelPlPrice3.setHotelPlUuid("酒店价单uuid");
		hotelPlPrice3.setHotelRoomUuid("酒店房型uuid");
		hotelPlPrice3.setHotelRoomText("酒店房型显示文本");
		hotelPlPrice3.setHotelMealUuids("酒店餐型uuids");
		hotelPlPrice3.setStartDate(new Date());
		hotelPlPrice3.setEndDate(new Date());
		hotelPlPrice3.setHotelGuestTypeUuid("酒店住客类型uuid");
		hotelPlPrice3.setCurrencyId(2);
		hotelPlPrice3.setCurrencyMark("币种符号");
		hotelPlPrice3.setAmount(66.0);
		hotelPlPrice3.setPriceType(0);
		hotelPlPrice3.setUuid("酒店价单价格uuid");
		hotelPlPrice3.setOccupancyRate("酒店容住率");
		hotelPlPriceList2.add(hotelPlPrice3);

		HotelPlPrice hotelPlPrice4 = new HotelPlPrice();
		hotelPlPrice4.setHotelPlUuid("酒店价单uuid");
		hotelPlPrice4.setHotelRoomUuid("酒店房型uuid");
		hotelPlPrice4.setHotelRoomText("酒店房型显示文本");
		hotelPlPrice4.setHotelMealUuids("酒店餐型uuids");
		hotelPlPrice4.setStartDate(new Date());
		hotelPlPrice4.setEndDate(new Date());
		hotelPlPrice4.setHotelGuestTypeUuid("酒店住客类型uuid");
		hotelPlPrice4.setCurrencyId(2);
		hotelPlPrice4.setCurrencyMark("币种符号");
		hotelPlPrice4.setAmount(66.0);
		hotelPlPrice4.setPriceType(0);
		hotelPlPrice4.setUuid("酒店价单价格uuid");
		hotelPlPrice4.setOccupancyRate("酒店容住率");
		hotelPlPriceList2.add(hotelPlPrice4);
		
		hotelPlPriceList1.add(hotelPlPrice2);
		map.put("房型uuid1", hotelPlPriceList1);
		map.put("房型uuid2", hotelPlPriceList2);
		
		System.out.println(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd"));
	}
	
	public static void initHotelPlIslandwayJson() {
		Map<String, List<HotelPlIslandway>> map = new LinkedHashMap<String, List<HotelPlIslandway>>();
		List<HotelPlIslandway> hotelPlIslandwayList1 = new ArrayList<HotelPlIslandway>();
		HotelPlIslandway hotelPlIslandway1 = new HotelPlIslandway();
		hotelPlIslandway1.setUuid("交通费用uuid");
		hotelPlIslandway1.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandway1.setIslandWay("上岛方式uuid");
		hotelPlIslandway1.setStartDate(new Date());
		hotelPlIslandway1.setEndDate(new Date());
		hotelPlIslandway1.setTravelerTypeUuid("游客类型uuid");
		hotelPlIslandway1.setCurrencyId(2);
		hotelPlIslandway1.setCurrencyMark("币种符号：￥、$");
		hotelPlIslandway1.setAmount(66.0);
		
		HotelPlIslandway hotelPlIslandway2 = new HotelPlIslandway();
		hotelPlIslandway2.setUuid("交通费用uuid");
		hotelPlIslandway2.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandway2.setIslandWay("上岛方式uuid");
		hotelPlIslandway2.setStartDate(new Date());
		hotelPlIslandway2.setEndDate(new Date());
		hotelPlIslandway2.setTravelerTypeUuid("游客类型uuid");
		hotelPlIslandway2.setCurrencyId(2);
		hotelPlIslandway2.setCurrencyMark("币种符号：￥、$");

		hotelPlIslandwayList1.add(hotelPlIslandway1);
		hotelPlIslandwayList1.add(hotelPlIslandway2);
		List<HotelPlIslandway> hotelPlIslandwayList2 = new ArrayList<HotelPlIslandway>();

		HotelPlIslandway hotelPlIslandway3 = new HotelPlIslandway();
		hotelPlIslandway3.setUuid("交通费用uuid");
		hotelPlIslandway3.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandway3.setIslandWay("上岛方式uuid");
		hotelPlIslandway3.setStartDate(new Date());
		hotelPlIslandway3.setEndDate(new Date());
		hotelPlIslandway3.setTravelerTypeUuid("游客类型uuid");
		hotelPlIslandway3.setCurrencyId(2);
		hotelPlIslandway3.setCurrencyMark("币种符号：￥、$");
		
		HotelPlIslandway hotelPlIslandway4 = new HotelPlIslandway();
		hotelPlIslandway4.setUuid("交通费用uuid");
		hotelPlIslandway4.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandway4.setIslandWay("上岛方式uuid");
		hotelPlIslandway4.setStartDate(new Date());
		hotelPlIslandway4.setEndDate(new Date());
		hotelPlIslandway4.setTravelerTypeUuid("游客类型uuid");
		hotelPlIslandway4.setCurrencyId(2);
		hotelPlIslandway4.setCurrencyMark("币种符号：￥、$");
		
		hotelPlIslandwayList2.add(hotelPlIslandway3);
		hotelPlIslandwayList2.add(hotelPlIslandway4);

		map.put("交通方式uuid1", hotelPlIslandwayList1);
		map.put("交通方式uuid2", hotelPlIslandwayList2);
		System.out.println(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd"));
	}
	
	public static void initHotelPlMealriseJson() {
		Map<String, List<HotelPlMealrise>> map = new LinkedHashMap<String, List<HotelPlMealrise>>();
		List<HotelPlMealrise> hotelPlMealriseList1 = new ArrayList<HotelPlMealrise>();
		HotelPlMealrise hotelPlMealrise1 = new HotelPlMealrise();
		hotelPlMealrise1.setUuid("酒店价单升餐uuid");
		hotelPlMealrise1.setHotelMealUuid("酒店餐型uuid");
		hotelPlMealrise1.setHotelMealRiseUuid("酒店升级餐型uuid");
		hotelPlMealrise1.setHotelMealRiseText("酒店升级餐型显示文本");
		hotelPlMealrise1.setHotelRoomUuid("酒店房型uuid");
		hotelPlMealrise1.setHotelRoomText("酒店房型显示文本");
		hotelPlMealrise1.setStartDate(new Date());
		hotelPlMealrise1.setEndDate(new Date());
		hotelPlMealrise1.setRequest(1);
		hotelPlMealrise1.setRequestText("升餐要求显示文本");
		hotelPlMealrise1.setRequestCondition(1);
		hotelPlMealrise1.setRequestConditionText("升餐要求条件显示文本");
		hotelPlMealrise1.setRequestNight(1);
		hotelPlMealrise1.setTravelerTypeUuid("游客类型uuid");
		hotelPlMealrise1.setCurrencyId(2);
		hotelPlMealrise1.setCurrencyMark("币种显示符号");
		hotelPlMealrise1.setAmount(66.0);
		hotelPlMealrise1.setHotelPlUuid("酒店价单uuid");
		
		HotelPlMealrise hotelPlMealrise2 = new HotelPlMealrise();
		hotelPlMealrise2.setUuid("酒店价单升餐uuid");
		hotelPlMealrise2.setHotelMealUuid("酒店餐型uuid");
		hotelPlMealrise2.setHotelMealRiseUuid("酒店升级餐型uuid");
		hotelPlMealrise2.setHotelMealRiseText("酒店升级餐型显示文本");
		hotelPlMealrise2.setHotelRoomUuid("酒店房型uuid");
		hotelPlMealrise2.setHotelRoomText("酒店房型显示文本");
		hotelPlMealrise2.setStartDate(new Date());
		hotelPlMealrise2.setEndDate(new Date());
		hotelPlMealrise2.setRequest(1);
		hotelPlMealrise2.setRequestText("升餐要求显示文本");
		hotelPlMealrise2.setRequestCondition(1);
		hotelPlMealrise2.setRequestConditionText("升餐要求条件显示文本");
		hotelPlMealrise2.setRequestNight(1);
		hotelPlMealrise2.setTravelerTypeUuid("游客类型uuid");
		hotelPlMealrise2.setCurrencyId(2);
		hotelPlMealrise2.setCurrencyMark("币种显示符号");
		hotelPlMealrise2.setAmount(66.0);
		hotelPlMealrise2.setHotelPlUuid("酒店价单uuid");
		
		hotelPlMealriseList1.add(hotelPlMealrise1);
		hotelPlMealriseList1.add(hotelPlMealrise2);
		List<HotelPlMealrise> hotelPlMealriseList2 = new ArrayList<HotelPlMealrise>();
		HotelPlMealrise hotelPlMealrise3 = new HotelPlMealrise();

		hotelPlMealrise3.setUuid("酒店价单升餐uuid");
		hotelPlMealrise3.setHotelMealUuid("酒店餐型uuid");
		hotelPlMealrise3.setHotelMealRiseUuid("酒店升级餐型uuid");
		hotelPlMealrise3.setHotelMealRiseText("酒店升级餐型显示文本");
		hotelPlMealrise3.setHotelRoomUuid("酒店房型uuid");
		hotelPlMealrise3.setHotelRoomText("酒店房型显示文本");
		hotelPlMealrise3.setStartDate(new Date());
		hotelPlMealrise3.setEndDate(new Date());
		hotelPlMealrise3.setRequest(1);
		hotelPlMealrise3.setRequestText("升餐要求显示文本");
		hotelPlMealrise3.setRequestCondition(1);
		hotelPlMealrise3.setRequestConditionText("升餐要求条件显示文本");
		hotelPlMealrise3.setRequestNight(1);
		hotelPlMealrise3.setTravelerTypeUuid("游客类型uuid");
		hotelPlMealrise3.setCurrencyId(2);
		hotelPlMealrise3.setCurrencyMark("币种显示符号");
		hotelPlMealrise3.setAmount(66.0);
		hotelPlMealrise3.setHotelPlUuid("酒店价单uuid");
		
		HotelPlMealrise hotelPlMealrise4 = new HotelPlMealrise();

		hotelPlMealrise4.setUuid("酒店价单升餐uuid");
		hotelPlMealrise4.setHotelMealUuid("酒店餐型uuid");
		hotelPlMealrise4.setHotelMealRiseUuid("酒店升级餐型uuid");
		hotelPlMealrise4.setHotelMealRiseText("酒店升级餐型显示文本");
		hotelPlMealrise4.setHotelRoomUuid("酒店房型uuid");
		hotelPlMealrise4.setHotelRoomText("酒店房型显示文本");
		hotelPlMealrise4.setStartDate(new Date());
		hotelPlMealrise4.setEndDate(new Date());
		hotelPlMealrise4.setRequest(1);
		hotelPlMealrise4.setRequestText("升餐要求显示文本");
		hotelPlMealrise4.setRequestCondition(1);
		hotelPlMealrise4.setRequestConditionText("升餐要求条件显示文本");
		hotelPlMealrise4.setRequestNight(1);
		hotelPlMealrise4.setTravelerTypeUuid("游客类型uuid");
		hotelPlMealrise4.setCurrencyId(2);
		hotelPlMealrise4.setCurrencyMark("币种显示符号");
		hotelPlMealrise4.setAmount(66.0);
		hotelPlMealrise4.setHotelPlUuid("酒店价单uuid");
		
		hotelPlMealriseList2.add(hotelPlMealrise3);
		hotelPlMealriseList2.add(hotelPlMealrise4);
		map.put("酒店升餐uuid1", hotelPlMealriseList1);
		map.put("酒店升餐uuid2", hotelPlMealriseList2);
		System.out.println(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd"));
		
		List<HotelMeal> hotelMealList = new ArrayList<HotelMeal>();
		HotelMeal hotelMeal1 = new HotelMeal();
		hotelMeal1.setUuid("酒店餐型uuid1");
		hotelMeal1.setMealName("酒店餐型名称1");
		
		HotelMeal hotelMeal2 = new HotelMeal();
		hotelMeal2.setUuid("酒店餐型uuid1");
		hotelMeal2.setMealName("酒店餐型名称1");
		
		HotelMeal hotelMeal3 = new HotelMeal();
		hotelMeal3.setUuid("酒店餐型uuid1");
		hotelMeal3.setMealName("酒店餐型名称1");
		
		HotelMeal hotelMeal4 = new HotelMeal();
		hotelMeal4.setUuid("酒店餐型uuid1");
		hotelMeal4.setMealName("酒店餐型名称1");
		
		HotelMeal hotelMeal5 = new HotelMeal();
		hotelMeal5.setUuid("酒店餐型uuid1");
		hotelMeal5.setMealName("酒店餐型名称1");

		hotelMealList.add(hotelMeal1);
		hotelMealList.add(hotelMeal2);
		hotelMealList.add(hotelMeal3);
		hotelMealList.add(hotelMeal4);
		hotelMealList.add(hotelMeal5);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelMealList, "yyyy-MM-dd"));
	}
	
	public static void initHotelPriceBaseInfo() {
		//酒店房型基础数据json封装(容住率和餐型)
		List<Map<String, Object>> hotelRoomList = new ArrayList<Map<String, Object>>();
		Map<String, Object> hotelRoomMap1 = new HashMap<String, Object>();
		hotelRoomMap1.put("hotelRoomUuid", "酒店房型uuid1");
		hotelRoomMap1.put("hotelRoomText", "酒店房型显示文本1");
		hotelRoomMap1.put("occupancyRate", "酒店房型容住率1");
		
		Map<String, String> hotelMealMap1 = new LinkedHashMap<String, String>();
		hotelMealMap1.put("餐型uuid1","餐型展示文本1");
		hotelMealMap1.put("餐型uuid2","餐型展示文本2");
		hotelMealMap1.put("餐型uuid3","餐型展示文本3");
		hotelMealMap1.put("餐型uuid4","餐型展示文本4");
		hotelRoomMap1.put("hotelMealMap1", hotelMealMap1);
		
		List<HotelGuestTypeRelation> hotelGuestTypeList = new ArrayList<HotelGuestTypeRelation>();
		HotelGuestTypeRelation relation1 = new HotelGuestTypeRelation();
		relation1.setHotelGuestTypeUuid("住客类型uuid1");
		relation1.setHotelGuestTypeName("住客类型名称1");
		relation1.setHotelRoomUuid("酒店房型uuid1");
		
		HotelGuestTypeRelation relation2 = new HotelGuestTypeRelation();
		relation2.setHotelGuestTypeUuid("住客类型uuid2");
		relation2.setHotelGuestTypeName("住客类型名称2");
		relation2.setHotelRoomUuid("酒店房型uuid2");
		
		HotelGuestTypeRelation relation3 = new HotelGuestTypeRelation();
		relation3.setHotelGuestTypeUuid("住客类型uuid3");
		relation3.setHotelGuestTypeName("住客类型名称3");
		relation3.setHotelRoomUuid("酒店房型uuid3");
		
		hotelGuestTypeList.add(relation1);
		hotelGuestTypeList.add(relation2);
		hotelGuestTypeList.add(relation3);
		
		hotelRoomMap1.put("hotelGuestTypeList", hotelGuestTypeList);
		
		

		Map<String, Object> hotelRoomMap2 = new HashMap<String, Object>();
		hotelRoomMap2.put("hotelRoomUuid", "酒店房型uuid2");
		hotelRoomMap2.put("hotelRoomText", "酒店房型显示文本2");
		hotelRoomMap2.put("occupancyRate", "酒店房型容住率2");
		Map<String, String> hotelMealMap2 = new LinkedHashMap<String, String>();
		hotelMealMap2.put("餐型uuid1","餐型展示文本1");
		hotelMealMap2.put("餐型uuid2","餐型展示文本2");
		hotelMealMap2.put("餐型uuid3","餐型展示文本3");
		hotelMealMap2.put("餐型uuid4","餐型展示文本4");
		hotelRoomMap2.put("hotelMealMap2", hotelMealMap2);
		
		List<HotelGuestTypeRelation> hotelGuestTypeList1 = new ArrayList<HotelGuestTypeRelation>();
		HotelGuestTypeRelation relation4 = new HotelGuestTypeRelation();
		relation4.setHotelGuestTypeUuid("住客类型uuid1");
		relation4.setHotelGuestTypeName("住客类型名称1");
		relation4.setHotelRoomUuid("酒店房型uuid1");
		
		HotelGuestTypeRelation relation5 = new HotelGuestTypeRelation();
		relation5.setHotelGuestTypeUuid("住客类型uuid2");
		relation5.setHotelGuestTypeName("住客类型名称2");
		relation5.setHotelRoomUuid("酒店房型uuid2");
		
		HotelGuestTypeRelation relation6 = new HotelGuestTypeRelation();
		relation6.setHotelGuestTypeUuid("住客类型uuid3");
		relation6.setHotelGuestTypeName("住客类型名称3");
		relation6.setHotelRoomUuid("酒店房型uuid3");
		
		hotelGuestTypeList1.add(relation4);
		hotelGuestTypeList1.add(relation5);
		hotelGuestTypeList1.add(relation6);
		
		hotelRoomMap2.put("hotelGuestTypeList", hotelGuestTypeList1);
		


		Map<String, Object> hotelRoomMap3 = new HashMap<String, Object>();
		hotelRoomMap3.put("hotelRoomUuid", "酒店房型uuid2");
		hotelRoomMap3.put("hotelRoomText", "酒店房型显示文本2");
		hotelRoomMap3.put("occupancyRate", "酒店房型容住率2");
		Map<String, String> hotelMealMap3 = new LinkedHashMap<String, String>();
		hotelMealMap3.put("餐型uuid1","餐型展示文本1");
		hotelMealMap3.put("餐型uuid2","餐型展示文本2");
		hotelMealMap3.put("餐型uuid3","餐型展示文本3");
		hotelMealMap3.put("餐型uuid4","餐型展示文本4");
		hotelRoomMap3.put("hotelMealMap3", hotelMealMap3);
		
		List<HotelGuestTypeRelation> hotelGuestTypeList2 = new ArrayList<HotelGuestTypeRelation>();
		HotelGuestTypeRelation relation7 = new HotelGuestTypeRelation();
		relation7.setHotelGuestTypeUuid("住客类型uuid1");
		relation7.setHotelGuestTypeName("住客类型名称1");
		relation7.setHotelRoomUuid("酒店房型uuid1");
		
		HotelGuestTypeRelation relation8 = new HotelGuestTypeRelation();
		relation8.setHotelGuestTypeUuid("住客类型uuid2");
		relation8.setHotelGuestTypeName("住客类型名称2");
		relation8.setHotelRoomUuid("酒店房型uuid2");
		
		HotelGuestTypeRelation relation9 = new HotelGuestTypeRelation();
		relation9.setHotelGuestTypeUuid("住客类型uuid3");
		relation9.setHotelGuestTypeName("住客类型名称3");
		relation9.setHotelRoomUuid("酒店房型uuid3");
		
		hotelGuestTypeList2.add(relation7);
		hotelGuestTypeList2.add(relation8);
		hotelGuestTypeList2.add(relation9);
		
		hotelRoomMap3.put("hotelGuestTypeList", hotelGuestTypeList2);

		hotelRoomList.add(hotelRoomMap1);
		hotelRoomList.add(hotelRoomMap2);
		hotelRoomList.add(hotelRoomMap3);
		
		System.out.println(JSON.toJSONStringWithDateFormat(hotelRoomList, "yyyy-MM-dd"));
		
		//酒店房型基础数据json封装(住客类型关联信息)
		List<HotelGuestTypeRelation> hotelGuestTypeRelList = new ArrayList<HotelGuestTypeRelation>();
		Map<String, List<HotelGuestTypeRelation>> hotelGuestTypeRelMap = new LinkedHashMap<String, List<HotelGuestTypeRelation>>();
		
		HotelGuestTypeRelation HotelGuestType1 = new HotelGuestTypeRelation();
		HotelGuestType1.setHotelGuestTypeUuid("住客类型uuid1");
		HotelGuestType1.setHotelGuestTypeName("住客类型名称1");
		HotelGuestType1.setHotelRoomUuid("酒店房型uuid1");
		HotelGuestTypeRelation HotelGuestType2 = new HotelGuestTypeRelation();
		HotelGuestType2.setHotelGuestTypeUuid("住客类型uuid2");
		HotelGuestType2.setHotelGuestTypeName("住客类型名称2");
		HotelGuestType2.setHotelRoomUuid("酒店房型uuid2");
		HotelGuestTypeRelation HotelGuestType3 = new HotelGuestTypeRelation();
		HotelGuestType3.setHotelGuestTypeUuid("住客类型uuid3");
		HotelGuestType3.setHotelGuestTypeName("住客类型名称3");
		HotelGuestType3.setHotelRoomUuid("酒店房型uuid3");
		HotelGuestTypeRelation HotelGuestType4 = new HotelGuestTypeRelation();
		HotelGuestType4.setHotelGuestTypeUuid("住客类型uuid4");
		HotelGuestType4.setHotelGuestTypeName("住客类型名称4");
		HotelGuestType4.setHotelRoomUuid("酒店房型uuid4");
		
		hotelGuestTypeRelList.add(HotelGuestType1);
		hotelGuestTypeRelList.add(HotelGuestType2);
		hotelGuestTypeRelList.add(HotelGuestType3);
		hotelGuestTypeRelList.add(HotelGuestType4);
		
		if(CollectionUtils.isNotEmpty(hotelGuestTypeRelList)) {
			for(HotelGuestTypeRelation relation : hotelGuestTypeRelList) {
				if(hotelGuestTypeRelMap.get(relation.getHotelRoomUuid()) == null) {
					List<HotelGuestTypeRelation> newRelationList = new ArrayList<HotelGuestTypeRelation>();
					newRelationList.add(relation);
					
					hotelGuestTypeRelMap.put(relation.getHotelRoomUuid(), newRelationList);
				} else {
					hotelGuestTypeRelMap.get(relation.getHotelRoomUuid()).add(relation);
				}
			}
		}
		
		
		System.out.println(JSON.toJSONStringWithDateFormat(hotelGuestTypeRelMap, "yyyy-MM-dd"));
		
		List<TravelerType> travelerTypeList = new ArrayList<TravelerType>();
		TravelerType travelerType1 = new TravelerType();
		travelerType1.setUuid("游客类型uuid1");
		travelerType1.setName("游客类型名称1");
		
		TravelerType travelerType2 = new TravelerType();
		travelerType2.setUuid("游客类型uuid2");
		travelerType2.setName("游客类型名称2");
		
		TravelerType travelerType3 = new TravelerType();
		travelerType3.setUuid("游客类型uuid3");
		travelerType3.setName("游客类型名称3");
		
		TravelerType travelerType4 = new TravelerType();
		travelerType4.setUuid("游客类型uuid4");
		travelerType4.setName("游客类型名称5");
		
		travelerTypeList.add(travelerType1);
		travelerTypeList.add(travelerType2);
		travelerTypeList.add(travelerType3);
		travelerTypeList.add(travelerType4);
		System.out.println(JSON.toJSONStringWithDateFormat(travelerTypeList, "yyyy-MM-dd"));
		
	}
	
	public static void initHotelPlMemoJson() {
		List<HotelPlRoomMemo> hotelPlRoomMemoList = new ArrayList<HotelPlRoomMemo>();
		HotelPlRoomMemo hotelPlRoomMemo1 = new HotelPlRoomMemo();
		hotelPlRoomMemo1.setUuid("酒店房型价格备注uuid");
		hotelPlRoomMemo1.setHotelPlUuid("酒店价单uuid");
		hotelPlRoomMemo1.setHotelRoomId("酒店价单房型uuid");
		hotelPlRoomMemo1.setMemo("酒店价单房型价格 备注");
		HotelPlRoomMemo hotelPlRoomMemo2 = new HotelPlRoomMemo();
		hotelPlRoomMemo2.setUuid("酒店房型价格备注uuid");
		hotelPlRoomMemo2.setHotelPlUuid("酒店价单uuid");
		hotelPlRoomMemo2.setHotelRoomId("酒店价单房型uuid");
		hotelPlRoomMemo2.setMemo("酒店价单房型价格 备注");

		hotelPlRoomMemoList.add(hotelPlRoomMemo1);
		hotelPlRoomMemoList.add(hotelPlRoomMemo2);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlRoomMemoList, "yyyy-MM-dd"));
		
		List<HotelPlIslandwayMemo> hotelPlIslandwayMemoList = new ArrayList<HotelPlIslandwayMemo>();
		HotelPlIslandwayMemo hotelPlIslandwayMemo1 = new HotelPlIslandwayMemo();
		hotelPlIslandwayMemo1.setUuid("酒店上岛方式备注uuid");
		hotelPlIslandwayMemo1.setIslandWay("酒店上岛方式uuid");
		hotelPlIslandwayMemo1.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandwayMemo1.setMemo("酒店价单交通费用备注");
		
		HotelPlIslandwayMemo hotelPlIslandwayMemo2 = new HotelPlIslandwayMemo();
		hotelPlIslandwayMemo2.setUuid("酒店上岛方式备注uuid");
		hotelPlIslandwayMemo2.setIslandWay("酒店上岛方式uuid");
		hotelPlIslandwayMemo2.setHotelPlUuid("酒店价单uuid");
		hotelPlIslandwayMemo2.setMemo("酒店价单交通费用备注");

		hotelPlIslandwayMemoList.add(hotelPlIslandwayMemo1);
		hotelPlIslandwayMemoList.add(hotelPlIslandwayMemo2);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlIslandwayMemoList, "yyyy-MM-dd"));
		
		List<HotelPlRisemealMemo> hotelPlRisemealMemoList = new ArrayList<HotelPlRisemealMemo>();
		HotelPlRisemealMemo hotelPlRisemealMemo1 = new HotelPlRisemealMemo();
		hotelPlRisemealMemo1.setUuid("酒店价单升餐备注uuid");
		hotelPlRisemealMemo1.setHotelMealUuid("酒店餐型uuid");
		hotelPlRisemealMemo1.setHotelPlUuid("酒店价单uuid");
		hotelPlRisemealMemo1.setMemo("酒店价单升餐备注");
		
		HotelPlRisemealMemo hotelPlRisemealMemo2 = new HotelPlRisemealMemo();
		hotelPlRisemealMemo2.setUuid("酒店价单升餐备注uuid");
		hotelPlRisemealMemo2.setHotelMealUuid("酒店餐型uuid");
		hotelPlRisemealMemo2.setHotelPlUuid("酒店价单uuid");
		hotelPlRisemealMemo2.setMemo("酒店价单升餐备注");
		
		hotelPlRisemealMemoList.add(hotelPlRisemealMemo1);
		hotelPlRisemealMemoList.add(hotelPlRisemealMemo2);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelPlRisemealMemoList, "yyyy-MM-dd"));
		
	}
	
	//交通费用基础数据封装
	public static void initIslandWayBaseInfoJson() {
		List<SysCompanyDictView> islandWays = new ArrayList<SysCompanyDictView>();
		SysCompanyDictView sysCompanyDictView1 = new SysCompanyDictView();
		sysCompanyDictView1.setUuid("上岛方式uuid1");
		sysCompanyDictView1.setLabel("上岛方式显示文本1");

		SysCompanyDictView sysCompanyDictView2 = new SysCompanyDictView();
		sysCompanyDictView2.setUuid("上岛方式uuid2");
		sysCompanyDictView2.setLabel("上岛方式显示文本2");

		SysCompanyDictView sysCompanyDictView3 = new SysCompanyDictView();
		sysCompanyDictView3.setUuid("上岛方式uuid3");
		sysCompanyDictView3.setLabel("上岛方式显示文本3");

		SysCompanyDictView sysCompanyDictView4 = new SysCompanyDictView();
		sysCompanyDictView4.setUuid("上岛方式uuid4");
		sysCompanyDictView4.setLabel("上岛方式显示文本4");
		
		islandWays.add(sysCompanyDictView1);
		islandWays.add(sysCompanyDictView2);
		islandWays.add(sysCompanyDictView3);
		islandWays.add(sysCompanyDictView4);
		System.out.println(JSON.toJSONStringWithDateFormat(islandWays, "yyyy-MM-dd"));
	}
	//升餐费用基础数据封装
	public static void initHotelMealBaseInfoJson() {
		List<HotelMeal> hotelMeals = new ArrayList<HotelMeal>();
		HotelMeal hotelMeal1 = new HotelMeal();
		hotelMeal1.setUuid("酒店餐型uuid1");
		hotelMeal1.setMealName("酒店餐型名称1");

		HotelMeal hotelMeal2 = new HotelMeal();
		hotelMeal2.setUuid("酒店餐型uuid2");
		hotelMeal2.setMealName("酒店餐型名称2");

		HotelMeal hotelMeal3 = new HotelMeal();
		hotelMeal3.setUuid("酒店餐型uuid3");
		hotelMeal3.setMealName("酒店餐型名称3");

		HotelMeal hotelMeal4 = new HotelMeal();
		hotelMeal4.setUuid("酒店餐型uuid4");
		hotelMeal4.setMealName("酒店餐型名称4");

		hotelMeals.add(hotelMeal1);
		hotelMeals.add(hotelMeal2);
		hotelMeals.add(hotelMeal3);
		hotelMeals.add(hotelMeal4);
		System.out.println(JSON.toJSONStringWithDateFormat(hotelMeals, "yyyy-MM-dd"));
	}

}
