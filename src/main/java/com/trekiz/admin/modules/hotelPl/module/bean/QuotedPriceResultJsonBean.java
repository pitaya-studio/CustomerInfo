/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;


/**
 * 报价结果展示
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class QuotedPriceResultJsonBean  {

	private String status;//报价结果状态
	private String message;//返回信息
	
	private List<QuotedPriceJsonBean> quotedPriceJsonList;

	private List<GuestPriceJsonBean> travelerTypesQuotedList;//动态第N人的列表title
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public List<QuotedPriceJsonBean> getQuotedPriceJsonList() {
		return quotedPriceJsonList;
	}

	public void setQuotedPriceJsonList(List<QuotedPriceJsonBean> quotedPriceJsonList) {
		this.quotedPriceJsonList = quotedPriceJsonList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
		list.add(gusetBean1);
		
		GuestPriceJsonBean gusetBean2 = new GuestPriceJsonBean();
		gusetBean2.setTravelerType("0d72dcad18d849549dee4589f50bdc9e");
		gusetBean2.setTravelerTypeText("婴儿");
		gusetBean2.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean2.setCurrencyText("$");
		gusetBean2.setAmount(400d);
		list.add(gusetBean2);
		
		GuestPriceJsonBean gusetBean3 = new GuestPriceJsonBean();
		gusetBean3.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean3.setTravelerTypeText("儿童");
		gusetBean3.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean3.setCurrencyText("$");
		gusetBean3.setAmount(450d);
		list.add(gusetBean3);
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
		guestPriceList.add(gusetBean11);
		
		GuestPriceJsonBean gusetBean22 = new GuestPriceJsonBean();
		gusetBean22.setTravelerType("0d72dcad18d849549dee4589f50bdc9e");
		gusetBean22.setTravelerTypeText("婴儿");
		gusetBean22.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean22.setCurrencyText("$");
		gusetBean22.setAmount(200d);
		guestPriceList.add(gusetBean22);
		
		GuestPriceJsonBean gusetBean33 = new GuestPriceJsonBean();
		gusetBean33.setTravelerType("5750998f7ad044f89fd8a7e8966130e1");
		gusetBean33.setTravelerTypeText("儿童");
		gusetBean33.setCurrencyId("c89e0a6661b64d1e809d8873cf85bc80");
		gusetBean33.setCurrencyText("$");
		gusetBean33.setAmount(150d);
		guestPriceList.add(gusetBean33);
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
		
		QuotedPriceResultJsonBean result = new QuotedPriceResultJsonBean();
		result.setStatus("success或者fail");
		result.setMessage("fail时的提示信息");
		List<QuotedPriceJsonBean> quotedPriceJsonList  = new ArrayList<QuotedPriceJsonBean>();
		quotedPriceJsonList.add(bean);
		result.setQuotedPriceJsonList(quotedPriceJsonList);
		
		System.out.println(JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd"));
		
	}

	public List<GuestPriceJsonBean> getTravelerTypesQuotedList() {
		return travelerTypesQuotedList;
	}

	public void setTravelerTypesQuotedList(
			List<GuestPriceJsonBean> travelerTypesQuotedList) {
		this.travelerTypesQuotedList = travelerTypesQuotedList;
	}
	
}

