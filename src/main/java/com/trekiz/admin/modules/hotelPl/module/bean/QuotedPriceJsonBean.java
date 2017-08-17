/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;


/**
 * 报价结果展示
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class QuotedPriceJsonBean  {

	/** 优惠后报价结果  */
	
	private String mixlivePriceCurrencyId;//混住费用币种id
	private String mixlivePriceCurrencyText;//混住费用币种输出符号
	private Double mixlivePrice;//混住费用
	
	private List<GuestPriceJsonBean> guestPriceList;//动态游客类型价格
	
	
	private List<QuotedPriceDetailJsonBean> detailList; //明细价格集合
	
	private String memo;//明细备注
	
	private QuotedPriceQuery quotedPriceQuery;
	
	private String preferentialUuids;//选择的优惠uuids多个用，分隔
	
	private List<PreferentialJsonBean> preferentialTotal;//可以使用的优惠集合
	
	private List<HotelPlPreferential> preferentialList4hotelPl;//价单的所有优惠集合
	
	private String hotelPlUuid;
	
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
	public String getMixlivePriceString() {
		return BeanUtil.numberFormat2String(mixlivePrice);
	}
	
	public void setMixlivePrice(Double mixlivePrice) {
		this.mixlivePrice = mixlivePrice;
	}

	public List<QuotedPriceDetailJsonBean> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<QuotedPriceDetailJsonBean> detailList) {
		this.detailList = detailList;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public QuotedPriceQuery getQuotedPriceQuery() {
		return quotedPriceQuery;
	}

	public void setQuotedPriceQuery(QuotedPriceQuery quotedPriceQuery) {
		this.quotedPriceQuery = quotedPriceQuery;
	}

	public String getPreferentialUuids() {
		return preferentialUuids;
	}

	public void setPreferentialUuids(String preferentialUuids) {
		this.preferentialUuids = preferentialUuids;
	}

	public List<PreferentialJsonBean> getPreferentialTotal() {
		return preferentialTotal;
	}

	public void setPreferentialTotal(List<PreferentialJsonBean> preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}

	public String getHotelPlUuid() {
		return hotelPlUuid;
	}

	public void setHotelPlUuid(String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}

	public List<HotelPlPreferential> getPreferentialList4hotelPl() {
		return preferentialList4hotelPl;
	}

	public void setPreferentialList4hotelPl(
			List<HotelPlPreferential> preferentialList4hotelPl) {
		this.preferentialList4hotelPl = preferentialList4hotelPl;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QuotedPriceJsonBean bean = new QuotedPriceJsonBean();
		
		bean.setMixlivePrice(50d);
		bean.setMixlivePriceCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		bean.setMixlivePriceCurrencyText("$");
		bean.setMemo("1、以下价格为每间每晚的报价，已含服务费，但不含政府税，不含床税；从2014.11.01开始，政府税将由8%涨至12%，从2014.12.01开始取消$8床税；2、全年3晚起订；12.28-01.05期间4晚起订，不能在12.30或12.31退房；2014.12.25-2015.01.08期间不接受混住；其它时间若混住的话，每种房型2晚起订；");
		
		List<GuestPriceJsonBean> list = new ArrayList<GuestPriceJsonBean> ();
		
		GuestPriceJsonBean gusetBean1 = new GuestPriceJsonBean();
		gusetBean1.setTravelerType("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean1.setTravelerTypeText("成人");
		gusetBean1.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean1.setCurrencyText("$");
		gusetBean1.setAmount(500d);
		gusetBean1.setIsThirdPerson(0);
		list.add(gusetBean1);
		
		GuestPriceJsonBean gusetBean2 = new GuestPriceJsonBean();
		gusetBean2.setTravelerType("0d72dcad18d849549dee4589f50bdc9e");
		gusetBean2.setTravelerTypeText("婴儿");
		gusetBean2.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean2.setCurrencyText("$");
		gusetBean2.setAmount(400d);
		gusetBean2.setIsThirdPerson(0);
		list.add(gusetBean2);
		
		GuestPriceJsonBean gusetBean3 = new GuestPriceJsonBean();
		gusetBean3.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean3.setTravelerTypeText("儿童");
		gusetBean3.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean3.setCurrencyText("$");
		gusetBean3.setAmount(450d);
		gusetBean3.setIsThirdPerson(0);
		list.add(gusetBean3);
		
		GuestPriceJsonBean gusetBean4 = new GuestPriceJsonBean();
		gusetBean4.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean4.setTravelerTypeText("第三人");
		gusetBean4.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean4.setCurrencyText("$");
		gusetBean4.setAmount(220d);
		gusetBean4.setIsThirdPerson(1);
		list.add(gusetBean4);
		
		GuestPriceJsonBean gusetBean5 = new GuestPriceJsonBean();
		gusetBean5.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean5.setTravelerTypeText("第四人");
		gusetBean5.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean5.setCurrencyText("$");
		gusetBean5.setAmount(280d);
		gusetBean5.setIsThirdPerson(1);
		list.add(gusetBean5);
		
		bean.setGuestPriceList(list);
		
		List<QuotedPriceDetailJsonBean> detailList = new ArrayList<QuotedPriceDetailJsonBean> ();
		QuotedPriceDetailJsonBean detailBean1 = new QuotedPriceDetailJsonBean();
		detailBean1.setInDate(DateUtils.date2String(new Date()));
		detailBean1.setHotelRoomUuid("108328bd4db141c99ff73559f1122e8b");
		detailBean1.setHotelRoomName("Water Villa");
		detailBean1.setHotelRoomOccupancyRate("2A+2C/3A+1C");
		detailBean1.setHotelMealUuid("a8f70bba75174c3cb820848d4a157b0b");
		detailBean1.setHotelMealText("BB");
		List<GuestPriceJsonBean> guestPriceList = new ArrayList<GuestPriceJsonBean> ();
		
		GuestPriceJsonBean gusetBean11 = new GuestPriceJsonBean();
		gusetBean11.setTravelerType("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean11.setTravelerTypeText("成人");
		gusetBean11.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean11.setCurrencyText("$");
		gusetBean11.setAmount(300d);
		gusetBean11.setIsThirdPerson(0);
		guestPriceList.add(gusetBean11);
		
		GuestPriceJsonBean gusetBean22 = new GuestPriceJsonBean();
		gusetBean22.setTravelerType("0d72dcad18d849549dee4589f50bdc9e");
		gusetBean22.setTravelerTypeText("婴儿");
		gusetBean22.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean22.setCurrencyText("$");
		gusetBean22.setAmount(200d);
		gusetBean22.setIsThirdPerson(0);
		guestPriceList.add(gusetBean22);
		
		GuestPriceJsonBean gusetBean33 = new GuestPriceJsonBean();
		gusetBean33.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean33.setTravelerTypeText("儿童");
		gusetBean33.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean33.setCurrencyText("$");
		gusetBean33.setAmount(150d);
		gusetBean33.setIsThirdPerson(0);
		guestPriceList.add(gusetBean33);
		
		GuestPriceJsonBean gusetBean44 = new GuestPriceJsonBean();
		gusetBean44.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean44.setTravelerTypeText("第三人");
		gusetBean44.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean44.setCurrencyText("$");
		gusetBean44.setAmount(120d);
		gusetBean44.setIsThirdPerson(1);
		guestPriceList.add(gusetBean44);
		
		GuestPriceJsonBean gusetBean55 = new GuestPriceJsonBean();
		gusetBean55.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean55.setTravelerTypeText("第四人");
		gusetBean55.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean55.setCurrencyText("$");
		gusetBean55.setAmount(160d);
		gusetBean55.setIsThirdPerson(1);
		guestPriceList.add(gusetBean55);
		
		detailBean1.setGuestPriceList(guestPriceList);
		detailList.add(detailBean1);
		bean.setDetailList(detailList);
		
		
		QuotedPriceQuery query = new QuotedPriceQuery();
		query.setPosition(1);
		query.setCountry("c89e0a6661b64d1e809d8873cf85bc80");
		query.setCountryText("马尔代夫");
		query.setIslandUuid("c89e0a6661b64d1e809d8873cf85bc80");
		query.setIslandText("太阳岛");
		query.setHotelUuid("c89e0a6661b64d1e809d8873cf85bc80");
		query.setHotelText("万豪大酒店");
		query.setHotelGroupText("万豪集团");
		query.setSupplierInfoId(2);
		query.setSupplierInfoText("天马");
		query.setPurchaseType(1);
		query.setPurchaseTypeText("内采");
		query.setDepartureIslandWay("c89e0a6661b64d1e809d8873cf85bc80");
		query.setDepartureIslandWayText("水飞");
		query.setArrivalIslandWay("c89e0a6661b64d1e809d8873cf85bc80");
		query.setArrivalIslandWayText("内飞");
		query.setRoomNum(1);
		query.setPersonNum(new String[]{"4","0","0"});
		
		query.setMixliveNum(2);
		
//		List<QuotedPriceRoomQuery> roomlist = new ArrayList<QuotedPriceRoomQuery>();
//		
//		QuotedPriceRoomQuery roomQuery1 = new QuotedPriceRoomQuery();
//		roomQuery1.setInDate(new Date());
//		roomQuery1.setHotelRoomUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery1.setHotelRoomText("水屋");
//		roomQuery1.setHotelMealUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery1.setHotelMealText("BB");
//		roomQuery1.setHotelMealRiseUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery1.setHotelMealRiseText("FB");
//		roomlist.add(roomQuery1);
//		
//		QuotedPriceRoomQuery roomQuery2 = new QuotedPriceRoomQuery();
//		roomQuery2.setInDate(new Date());
//		roomQuery2.setHotelRoomUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery2.setHotelRoomText("沙屋");
//		roomQuery2.setHotelMealUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery2.setHotelMealText("FB");
//		roomQuery2.setHotelMealRiseUuid("c89e0a6661b64d1e809d8873cf85bc80");
//		roomQuery2.setHotelMealRiseText("AI");
//		roomlist.add(roomQuery2);
//		
//		query.setQuotedPriceRoomList(roomlist);
		
		bean.setQuotedPriceQuery(query);
		
		bean.setPreferentialUuids("选择的优惠UUID1,选择的优惠UUID2");
		
		List<PreferentialJsonBean> preferentialList = PreferentialJsonBean.initPreferentialJsonBean();
		bean.setPreferentialTotal(preferentialList);
		
		List<HotelPlPreferential> preferentialList4hotelPl = new ArrayList<HotelPlPreferential>();
		HotelPlPreferential hpprefer1 = new HotelPlPreferential();
		hpprefer1.setUuid("优惠1uuid");
		hpprefer1.setPreferentialName("住七付五房费九折优惠");
		hpprefer1.setBookingCode("2BPV2OWPV001");
		hpprefer1.setDescription("2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受住七付五，房费九折，含HB餐免二人交通优惠。");
		preferentialList4hotelPl.add(hpprefer1);
		
		HotelPlPreferential hpprefer2 = new HotelPlPreferential();
		hpprefer2.setUuid("优惠2uuid");
		hpprefer2.setPreferentialName("交通优惠");
		hpprefer2.setBookingCode("2BPV2OWPV002");
		hpprefer2.setDescription("2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受水上飞机9折优惠。");
		preferentialList4hotelPl.add(hpprefer2);
		
		HotelPlPreferential hpprefer3 = new HotelPlPreferential();
		hpprefer3.setUuid("优惠3uuid");
		hpprefer3.setPreferentialName("蜜月优惠");
		hpprefer3.setBookingCode("2BPV2OWPV003");
		hpprefer3.setDescription("赠送水果篮，1瓶香槟；入住期间参加自费项目可优惠$100，；赠送烛光晚餐，不含饮料。");
		preferentialList4hotelPl.add(hpprefer3);
		
		HotelPlPreferential hpprefer4 = new HotelPlPreferential();
		hpprefer4.setUuid("优惠4uuid");
		hpprefer4.setPreferentialName("打包优惠");
		hpprefer4.setBookingCode("2BPV2OWPV004");
		hpprefer4.setDescription("3N Deluxe Beach Villa+1N Water Villa 2015.10.6-2015.12.18期间打包价为$780/间");
		preferentialList4hotelPl.add(hpprefer4);
		
		bean.setPreferentialList4hotelPl(preferentialList4hotelPl);
		
		System.out.println(JSON.toJSONStringWithDateFormat(bean,"yyyy-MM-dd"));
		
//		QuotedPriceJsonBean testbean = JSON.parseObject(JSON.toJSONStringWithDateFormat(bean,"yyyy-MM-dd"), QuotedPriceJsonBean.class);
//		System.out.println(testbean.getGuestPriceList().get(0).getAmount());
		
		List<Map<String,String>> travelerTypeList = new ArrayList<Map<String,String>>();
		Map<String,String> travelerMap = new HashMap<String,String>();
    	travelerMap.put("guestTypeText", "第三人");
    	travelerMap.put("guestType", "c89e0a6661b64d1e809d8873cf85bc80");
    	travelerTypeList.add(travelerMap);
    	
    	Map<String,String> travelerMap2 = new HashMap<String,String>();
    	travelerMap2.put("guestTypeText", "第四人");
    	travelerMap2.put("guestType", "32ds0a6661b64d1e809d8873cf85bc80");
    	travelerTypeList.add(travelerMap2);
    	System.out.println(JSON.toJSONString(travelerTypeList));	
	}
}

