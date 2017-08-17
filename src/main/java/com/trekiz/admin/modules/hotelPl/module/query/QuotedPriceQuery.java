/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;


/**
 * 报价条件映射类
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class QuotedPriceQuery  {

	private int position;//位置（1、境内；2、境外）
	private String country;//国家
	private String countryText;//国家文本
	private String islandUuid;//岛屿
	private String islandText;//岛屿文本
	private String hotelUuid;//酒店
	private String hotelText;//酒店文本
	private String hotelGroup;//酒店集团uuid
	private String hotelGroupText;//酒店集团名称
	private Integer supplierInfoId;//地接供应商
	private String supplierInfoText;//地接供应商文本
	private Integer purchaseType;//采购类型
	private String purchaseTypeText;//采购类型文本
	
	private String departureIslandWay;//去程交通
	private String departureIslandWayText;//去程交通文本
	private String arrivalIslandWay;//返程交通
	private String arrivalIslandWayText;//返程交通文本
	private Integer roomNum;//间数
	private String[] personNum;//人数数组
	private Integer mixliveNum;//混住次数
	
	//入住房型信息
	private List<QuotedPriceRoomQuery> quotedPriceRoomList;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIslandUuid() {
		return islandUuid;
	}

	public void setIslandUuid(String islandUuid) {
		this.islandUuid = islandUuid;
	}

	public String getHotelUuid() {
		return hotelUuid;
	}

	public void setHotelUuid(String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}

	

	public Integer getSupplierInfoId() {
		return supplierInfoId;
	}
	
	public Integer getPurchaseType() {
		return purchaseType;
	}

	public String getDepartureIslandWay() {
		return departureIslandWay;
	}

	public void setDepartureIslandWay(String departureIslandWay) {
		this.departureIslandWay = departureIslandWay;
	}

	public String getArrivalIslandWay() {
		return arrivalIslandWay;
	}

	public void setArrivalIslandWay(String arrivalIslandWay) {
		this.arrivalIslandWay = arrivalIslandWay;
	}

	public Integer getRoomNum() {
		return roomNum;
	}

	public String[] getPersonNum() {
		return personNum;
	}

	public void setPersonNum(String[] personNum) {
		this.personNum = personNum;
	}

	public void setMixliveNum(Integer mixliveNum) {
		this.mixliveNum = mixliveNum;
	}

	public Integer getMixliveNum() {
		return mixliveNum;
	}

	public List<QuotedPriceRoomQuery> getQuotedPriceRoomList() {
		return quotedPriceRoomList;
	}

	public void setQuotedPriceRoomList(
			List<QuotedPriceRoomQuery> quotedPriceRoomList) {
		this.quotedPriceRoomList = quotedPriceRoomList;
	}
	
	public String getCountryText() {
		return countryText;
	}

	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}

	public String getIslandText() {
		return islandText;
	}

	public void setIslandText(String islandText) {
		this.islandText = islandText;
	}

	public String getHotelText() {
		return hotelText;
	}

	public void setHotelText(String hotelText) {
		this.hotelText = hotelText;
	}

	public String getSupplierInfoText() {
		return supplierInfoText;
	}

	public void setSupplierInfoText(String supplierInfoText) {
		this.supplierInfoText = supplierInfoText;
	}

	public String getPurchaseTypeText() {
		return purchaseTypeText;
	}

	public void setPurchaseTypeText(String purchaseTypeText) {
		this.purchaseTypeText = purchaseTypeText;
	}

	public String getDepartureIslandWayText() {
		return departureIslandWayText;
	}

	public void setDepartureIslandWayText(String departureIslandWayText) {
		this.departureIslandWayText = departureIslandWayText;
	}

	public String getArrivalIslandWayText() {
		return arrivalIslandWayText;
	}

	public void setArrivalIslandWayText(String arrivalIslandWayText) {
		this.arrivalIslandWayText = arrivalIslandWayText;
	}

	public void setSupplierInfoId(Integer supplierInfoId) {
		this.supplierInfoId = supplierInfoId;
	}

	public void setPurchaseType(Integer purchaseType) {
		this.purchaseType = purchaseType;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

	public String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public String getHotelGroupText() {
		return hotelGroupText;
	}

	public void setHotelGroupText(String hotelGroupText) {
		this.hotelGroupText = hotelGroupText;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QuotedPriceQuery query = new QuotedPriceQuery();
		query.setPosition(1);
		query.setCountry("c89e0a6661b64d1e809d8873cf85bc80");
		query.setCountryText("马尔代夫");
		query.setIslandUuid("c89e0a6661b64d1e809d8873cf85bc80");
		query.setIslandText("太阳岛");
		query.setHotelUuid("c89e0a6661b64d1e809d8873cf85bc80");
		query.setHotelText("万豪大酒店");
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
		
		List<QuotedPriceRoomQuery> list = new ArrayList<QuotedPriceRoomQuery>();
		
		QuotedPriceRoomQuery roomQuery1 = new QuotedPriceRoomQuery();
		roomQuery1.setInDate(new Date());
		roomQuery1.setHotelRoomUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery1.setHotelRoomText("水屋");
		roomQuery1.setHotelMealUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery1.setHotelMealText("BB");
		roomQuery1.setHotelMealRiseUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery1.setHotelMealRiseText("FB");
		list.add(roomQuery1);
		
		QuotedPriceRoomQuery roomQuery2 = new QuotedPriceRoomQuery();
		roomQuery2.setInDate(new Date());
		roomQuery2.setHotelRoomUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery2.setHotelRoomText("沙屋");
		roomQuery2.setHotelMealUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery2.setHotelMealText("FB");
		roomQuery2.setHotelMealRiseUuid("c89e0a6661b64d1e809d8873cf85bc80");
		roomQuery2.setHotelMealRiseText("AI");
		list.add(roomQuery2);
		
		query.setQuotedPriceRoomList(list);
		
		System.out.println(JSON.toJSONStringWithDateFormat(query, "yyyy-MM-dd"));
	}

}

