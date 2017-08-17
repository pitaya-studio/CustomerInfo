/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;



/**
 * 可选择的优惠信息 
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PreferentialJsonBean  {

	private List<HotelPlPreferential> preferentialList;//优惠名称
	/** 优惠后报价结果 */
	private List<GuestPriceJsonBean> guestPriceList;//动态游客类型价格 key游客类型的uuid
	
	private String mixlivePriceCurrencyId;//混住费用币种id
	private String mixlivePriceCurrencyText;//混住费用币种输出符号
	private Double mixlivePrice;//混住费用
	
	private String totalPriceCurrencyId;//打包的整体价格币种id
	private String totalPriceCurrencyText;//打包的整体价格币种输出符号
	private Double totalPrice;//打包的整体价格
	
	public List<HotelPlPreferential> getPreferentialList() {
		return preferentialList;
	}
	public void setPreferentialList(List<HotelPlPreferential> preferentialList) {
		this.preferentialList = preferentialList;
	}
	public List<GuestPriceJsonBean> getGuestPriceList() {
		return guestPriceList;
	}
	public void setGuestPriceList(List<GuestPriceJsonBean> guestPriceList) {
		this.guestPriceList = guestPriceList;
	}
	
	public String getMixlivePriceCurrencyId() {
		return mixlivePriceCurrencyId;
	}
	public void setMixlivePriceCurrencyId(String mixlivePriceCurrencyId) {
		this.mixlivePriceCurrencyId = mixlivePriceCurrencyId;
	}
	public String getMixlivePriceCurrencyText() {
		return mixlivePriceCurrencyText;
	}
	public void setMixlivePriceCurrencyText(String mixlivePriceCurrencyText) {
		this.mixlivePriceCurrencyText = mixlivePriceCurrencyText;
	}
	public Double getMixlivePrice() {
		return mixlivePrice;
	}
	public void setMixlivePrice(Double mixlivePrice) {
		this.mixlivePrice = mixlivePrice;
	}
	
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String getTotalPriceCurrencyId() {
		return totalPriceCurrencyId;
	}
	public void setTotalPriceCurrencyId(String totalPriceCurrencyId) {
		this.totalPriceCurrencyId = totalPriceCurrencyId;
	}
	public String getTotalPriceCurrencyText() {
		return totalPriceCurrencyText;
	}
	public void setTotalPriceCurrencyText(String totalPriceCurrencyText) {
		this.totalPriceCurrencyText = totalPriceCurrencyText;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initPreferentialJsonBean();
	}
	
	
	public static List<PreferentialJsonBean> initPreferentialJsonBean(){

		List<PreferentialJsonBean> beanList = new ArrayList<PreferentialJsonBean>();
		PreferentialJsonBean bean = new PreferentialJsonBean();
		beanList.add(bean);
		
		
		bean.setMixlivePrice(50d);
		bean.setMixlivePriceCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		bean.setMixlivePriceCurrencyText("$");
		
		bean.setTotalPrice(12000d);
		bean.setTotalPriceCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		bean.setTotalPriceCurrencyText("$");
		
		List<HotelPlPreferential> preferentialList = new ArrayList<HotelPlPreferential>();
		HotelPlPreferential hpp1 = new HotelPlPreferential();
		hpp1.setUuid("优惠1uuid");
		hpp1.setPreferentialName("住七付五房费九折优惠");
		hpp1.setBookingCode("2BPV2OWPV001");
		hpp1.setDescription("2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受住七付五，房费九折，含HB餐免二人交通优惠。");
		
		hpp1.setInDate(DateUtils.string2Date("2015.01.01","yyyy.MM.dd"));
		hpp1.setOutDate(DateUtils.string2Date("2015.09.30","yyyy.MM.dd"));
		hpp1.setBookingStartDate(DateUtils.string2Date("2014.09.01","yyyy.MM.dd"));
		hpp1.setBookingEndDate(DateUtils.string2Date("2015.12.31","yyyy.MM.dd"));
		
		List<HotelPlPreferentialRoom> preferentialRoomList1 = new ArrayList<HotelPlPreferentialRoom> ();
		
		HotelPlPreferentialRoom room1 = new HotelPlPreferentialRoom();
		room1.setHotelRoomText("Beachfront Villa");
		room1.setNights(2);
		room1.setRoomOccupancyRate("2A+2C");
		room1.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList1 = new ArrayList<HotelMeal>();
		HotelMeal hm11 = new HotelMeal();
		hm11.setMealName("BB");
		HotelMeal hm12 = new HotelMeal();
		hm12.setMealName("HB");
		hotelMealList1.add(hm11);
		hotelMealList1.add(hm12);
		room1.setHotelMealList(hotelMealList1);
		
		HotelPlPreferentialRoom room2 = new HotelPlPreferentialRoom();
		room2.setHotelRoomText("Water Bungalow");
		room2.setNights(1);
		room2.setRoomOccupancyRate("2A+2C");
		room2.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList2 = new ArrayList<HotelMeal>();
		HotelMeal hm21 = new HotelMeal();
		hm21.setMealName("BB");
		HotelMeal hm22 = new HotelMeal();
		hm22.setMealName("HB");
		hotelMealList2.add(hm21);
		hotelMealList2.add(hm22);
		room2.setHotelMealList(hotelMealList2);
		
		HotelPlPreferentialRoom room3 = new HotelPlPreferentialRoom();
		room3.setHotelRoomText("Water Bungalow");
		room3.setNights(1);
		room3.setRoomOccupancyRate("2A+2C");
		room3.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList3 = new ArrayList<HotelMeal>();
		HotelMeal hm31 = new HotelMeal();
		hm31.setMealName("BB");
		HotelMeal hm32 = new HotelMeal();
		hm32.setMealName("HB");
		hotelMealList3.add(hm31);
		hotelMealList3.add(hm32);
		room3.setHotelMealList(hotelMealList3);
		
		preferentialRoomList1.add(room1);
		preferentialRoomList1.add(room2);
		preferentialRoomList1.add(room3);
		hpp1.setPreferentialRoomList(preferentialRoomList1);
		
		List<SysCompanyDictView> islandWayList1 = new ArrayList<SysCompanyDictView>();
		SysCompanyDictView sdv11 = new SysCompanyDictView();
		sdv11.setLabel("水飞");
		SysCompanyDictView sdv12 = new SysCompanyDictView();
		sdv12.setLabel("内飞");
		islandWayList1.add(sdv11);
		islandWayList1.add(sdv12);
		hpp1.setIslandWayList(islandWayList1);
		
		
		HotelPlPreferentialRequire require1 = new HotelPlPreferentialRequire ();
		require1.setBookingNights(5);
		require1.setBookingNumbers(10);
		require1.setNotApplicableDate("2015.01.01");
		require1.setNotApplicableRoomName("Beach Villa");
		require1.setApplicableThirdPersonText("是");
		require1.setIsSuperpositionText("允许");
		require1.setMemo("XXXXXXXXXXXXXXXXXXXXXXXX");
		hpp1.setRequire(require1);
		
//		private String travelerTypeText;//游客类型 输出文本
//		private String preferentialTypeText;//优惠方式（1、合计；2、打折；3、减金额；4、减最低）输出文本
//		private String chargeTypeText;//收费类型（1、%；2、￥） 输出文本
//		private String islandWayText;//上岛方式集合
//		private String hotelMealText;//基础餐型
//		private String istaxText;//加税种类(1、政府税；2、服务费；3、床税；4、其他   输出文本
		HotelPlPreferentialMatter matter1 = new HotelPlPreferentialMatter();
		matter1.setPreferentialTemplatesText("住宿优惠");
		matter1.setPreferentialTemplatesDetailText("住： 5  晚  免：1  晚");
		Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap1 = new HashMap<String,List<HotelPlPreferentialTax>>();
		List<HotelPlPreferentialTax> lpt11= new ArrayList<HotelPlPreferentialTax>();
		HotelPlPreferentialTax hpt111 = new HotelPlPreferentialTax();
		hpt111.setTravelerTypeText("成人");
		hpt111.setPreferentialTypeText("打折");
		hpt111.setPreferentialAmount(50d);
		hpt111.setChargeTypeText("%");
		hpt111.setIslandWayText("");
		hpt111.setHotelMealText("");
		hpt111.setIstaxText("政府税，服务税，床税");
		
		HotelPlPreferentialTax hpt222 = new HotelPlPreferentialTax();
		hpt222.setTravelerTypeText("儿童");
		hpt222.setPreferentialTypeText("减金额");
		hpt222.setPreferentialAmount(200d);
		hpt222.setChargeTypeText("$");
		hpt222.setIslandWayText("");
		hpt222.setHotelMealText("");
		hpt222.setIstaxText("服务税，床税");
		
		HotelPlPreferentialTax hpt333 = new HotelPlPreferentialTax();
		hpt333.setTravelerTypeText("婴儿");
		hpt333.setPreferentialTypeText("减最低");
		hpt333.setPreferentialAmount(20d);
		hpt333.setChargeTypeText("$");
		hpt333.setIslandWayText("");
		hpt333.setHotelMealText("");
		hpt333.setIstaxText("床税");
		
		lpt11.add(hpt111);
		lpt11.add(hpt222);
		lpt11.add(hpt333);
		
		preferentialTaxMap1.put("1", lpt11);
		
		matter1.setPreferentialTaxMap(preferentialTaxMap1);
		matter1.setMemo("XXXXXXXXXXXXXXXXXXX");
		hpp1.setMatter(matter1);
		
		preferentialList.add(hpp1);
		
		
		
		HotelPlPreferential hpp2 = new HotelPlPreferential();
		hpp2.setUuid("优惠2uuid");
		hpp2.setPreferentialName("交通优惠");
		hpp2.setBookingCode("2BPV2OWPV002");
		hpp2.setDescription("2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受水上飞机9折优");
		
		hpp2.setInDate(DateUtils.string2Date("2015.01.01","yyyy.MM.dd"));
		hpp2.setOutDate(DateUtils.string2Date("2015.09.30","yyyy.MM.dd"));
		hpp2.setBookingStartDate(DateUtils.string2Date("2014.09.01","yyyy.MM.dd"));
		hpp2.setBookingEndDate(DateUtils.string2Date("2015.12.31","yyyy.MM.dd"));
		
		List<HotelPlPreferentialRoom> preferentialRoomList2 = new ArrayList<HotelPlPreferentialRoom> ();
		
		HotelPlPreferentialRoom room21 = new HotelPlPreferentialRoom();
		room21.setHotelRoomText("Beachfront Villa");
		room21.setNights(2);
		room21.setRoomOccupancyRate("2A+2C");
		room21.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList21 = new ArrayList<HotelMeal>();
		HotelMeal hm211 = new HotelMeal();
		hm211.setMealName("BB");
		HotelMeal hm212 = new HotelMeal();
		hm212.setMealName("HB");
		hotelMealList21.add(hm211);
		hotelMealList21.add(hm212);
		room21.setHotelMealList(hotelMealList21);
		
		HotelPlPreferentialRoom room22 = new HotelPlPreferentialRoom();
		room22.setHotelRoomText("Water Bungalow");
		room22.setNights(1);
		room22.setRoomOccupancyRate("2A+2C");
		room22.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList22 = new ArrayList<HotelMeal>();
		HotelMeal hm221 = new HotelMeal();
		hm221.setMealName("BB");
		HotelMeal hm222 = new HotelMeal();
		hm222.setMealName("HB");
		hotelMealList22.add(hm221);
		hotelMealList22.add(hm222);
		room22.setHotelMealList(hotelMealList22);
		
		HotelPlPreferentialRoom room23 = new HotelPlPreferentialRoom();
		room23.setHotelRoomText("Water Bungalow");
		room23.setNights(1);
		room23.setRoomOccupancyRate("2A+2C");
		room23.setRelHotelName("关联酒店名称");
		List<HotelMeal> hotelMealList23 = new ArrayList<HotelMeal>();
		HotelMeal hm231 = new HotelMeal();
		hm231.setMealName("BB");
		HotelMeal hm232 = new HotelMeal();
		hm232.setMealName("HB");
		hotelMealList23.add(hm231);
		hotelMealList23.add(hm232);
		room23.setHotelMealList(hotelMealList23);
		
		preferentialRoomList2.add(room21);
		preferentialRoomList2.add(room22);
		preferentialRoomList2.add(room23);
		hpp2.setPreferentialRoomList(preferentialRoomList2);
		
		
		List<SysCompanyDictView> islandWayList2 = new ArrayList<SysCompanyDictView>();
		SysCompanyDictView sdv21 = new SysCompanyDictView();
		sdv21.setLabel("水飞");
		islandWayList2.add(sdv21);
		hpp1.setIslandWayList(islandWayList2);
		
		HotelPlPreferentialRequire require2 = new HotelPlPreferentialRequire ();
		require2.setBookingNights(5);
		require2.setBookingNumbers(10);
		require2.setNotApplicableDate("2015.01.01");
		require2.setNotApplicableRoomName("Beach Villa");
		require2.setApplicableThirdPersonText("是");
		require2.setIsSuperpositionText("允许");
		require2.setMemo("XXXXXXXXXXXXXXXXXXXXXXXX");
		hpp2.setRequire(require2);
		
		
		HotelPlPreferentialMatter matter2 = new HotelPlPreferentialMatter();
		matter2.setPreferentialTemplatesText("住宿优惠");
		matter2.setPreferentialTemplatesDetailText("住： 5  晚  免：1  晚");
		Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap2 = new HashMap<String,List<HotelPlPreferentialTax>>();
		List<HotelPlPreferentialTax> lpt21= new ArrayList<HotelPlPreferentialTax>();
		HotelPlPreferentialTax hpt211 = new HotelPlPreferentialTax();
		hpt211.setTravelerTypeText("成人");
		hpt211.setPreferentialTypeText("打折");
		hpt211.setPreferentialAmount(50d);
		hpt211.setChargeTypeText("%");
		hpt211.setIslandWayText("水飞,内飞");
		hpt211.setHotelMealText("");
		hpt211.setIstaxText("政府税，服务税，床税");
		
		HotelPlPreferentialTax hpt212 = new HotelPlPreferentialTax();
		hpt212.setTravelerTypeText("儿童");
		hpt212.setPreferentialTypeText("减金额");
		hpt212.setPreferentialAmount(200d);
		hpt212.setChargeTypeText("$");
		hpt212.setIslandWayText("水飞");
		hpt212.setHotelMealText("");
		hpt212.setIstaxText("服务税，床税");
		
		HotelPlPreferentialTax hpt213 = new HotelPlPreferentialTax();
		hpt213.setTravelerTypeText("婴儿");
		hpt213.setPreferentialTypeText("减最低");
		hpt213.setPreferentialAmount(20d);
		hpt213.setChargeTypeText("$");
		hpt213.setIslandWayText("内飞");
		hpt213.setHotelMealText("");
		hpt213.setIstaxText("床税");
		
		lpt21.add(hpt211);
		lpt21.add(hpt212);
		lpt21.add(hpt213);
		
		preferentialTaxMap2.put("2", lpt21);
		
		
		List<HotelPlPreferentialTax> lpt31= new ArrayList<HotelPlPreferentialTax>();
		HotelPlPreferentialTax hpt311 = new HotelPlPreferentialTax();
		hpt311.setTravelerTypeText("成人");
		hpt311.setPreferentialTypeText("打折");
		hpt311.setPreferentialAmount(50d);
		hpt311.setChargeTypeText("%");
		hpt311.setIslandWayText("");
		hpt311.setHotelMealText("BB,HB,AI");
		hpt311.setIstaxText("政府税，服务税，床税");
		
		HotelPlPreferentialTax hpt312 = new HotelPlPreferentialTax();
		hpt312.setTravelerTypeText("儿童");
		hpt312.setPreferentialTypeText("减金额");
		hpt312.setPreferentialAmount(200d);
		hpt312.setChargeTypeText("$");
		hpt312.setIslandWayText("");
		hpt312.setHotelMealText("BB,HB,AI");
		hpt312.setIstaxText("服务税，床税");
		
		HotelPlPreferentialTax hpt313 = new HotelPlPreferentialTax();
		hpt313.setTravelerTypeText("婴儿");
		hpt313.setPreferentialTypeText("减最低");
		hpt313.setPreferentialAmount(20d);
		hpt313.setChargeTypeText("$");
		hpt313.setIslandWayText("");
		hpt313.setHotelMealText("BB,HB,AI");
		hpt313.setIstaxText("床税");
		
		lpt31.add(hpt311);
		lpt31.add(hpt312);
		lpt31.add(hpt313);
		
		preferentialTaxMap2.put("3", lpt31);
		
		
		List<HotelPlPreferentialTax> lpt41= new ArrayList<HotelPlPreferentialTax>();
		HotelPlPreferentialTax hpt411 = new HotelPlPreferentialTax();
		hpt411.setIstaxText("政府税，服务税，床税");
		lpt41.add(hpt411);
		
		preferentialTaxMap2.put("4", lpt41);
		
		matter2.setPreferentialTaxMap(preferentialTaxMap2);
		matter2.setMemo("XXXXXXXXXXXXXXXXXXX");
		hpp2.setMatter(matter2);
		
		preferentialList.add(hpp2);
		
		
		List<GuestPriceJsonBean> guestPriceList = new ArrayList<GuestPriceJsonBean> ();
		
		GuestPriceJsonBean gusetBean11 = new GuestPriceJsonBean();
		gusetBean11.setTravelerType("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean11.setTravelerTypeText("成人");
		gusetBean11.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean11.setCurrencyText("$");
		gusetBean11.setAmount(1200d);
		gusetBean11.setPreferAmount(200d);
		gusetBean11.setIsThirdPerson(0);
		guestPriceList.add(gusetBean11);
		
		GuestPriceJsonBean gusetBean22 = new GuestPriceJsonBean();
		gusetBean22.setTravelerType("0d72dcad18d849549dee4589f50bdc9e");
		gusetBean22.setTravelerTypeText("婴儿");
		gusetBean22.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean22.setCurrencyText("$");
		gusetBean22.setAmount(500d);
		gusetBean22.setPreferAmount(50d);
		gusetBean22.setIsThirdPerson(0);
		guestPriceList.add(gusetBean22);
		
		GuestPriceJsonBean gusetBean33 = new GuestPriceJsonBean();
		gusetBean33.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean33.setTravelerTypeText("儿童");
		gusetBean33.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean33.setCurrencyText("$");
		gusetBean33.setAmount(800d);
		gusetBean33.setPreferAmount(100d);
		gusetBean33.setIsThirdPerson(0);
		guestPriceList.add(gusetBean33);
		
		GuestPriceJsonBean gusetBean44 = new GuestPriceJsonBean();
		gusetBean44.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean44.setTravelerTypeText("第三人");
		gusetBean44.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean44.setCurrencyText("$");
		gusetBean44.setAmount(200d);
		gusetBean44.setPreferAmount(20d);
		gusetBean44.setIsThirdPerson(1);
		guestPriceList.add(gusetBean44);
		
		GuestPriceJsonBean gusetBean55 = new GuestPriceJsonBean();
		gusetBean55.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean55.setTravelerTypeText("第四人");
		gusetBean55.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean55.setCurrencyText("$");
		gusetBean55.setAmount(200d);
		gusetBean55.setPreferAmount(80d);
		gusetBean55.setIsThirdPerson(1);
		guestPriceList.add(gusetBean55);
		
		bean.setPreferentialList(preferentialList);
		bean.setGuestPriceList(guestPriceList);
		//System.out.println(JSON.toJSONStringWithDateFormat(beanList, "yyyy.MM.dd"));
		return beanList;
	}
	
}

