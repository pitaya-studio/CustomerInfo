package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;

public class AirticketOrderDetailJsonBean {
	
	private Map<String, String> baseInfo;
	private Map<String, Object> reservations;
	private Map<String, Object> fee;
	private List<Map<String,String>> flights;
	private List<Map<String,String>> attachment;
	private List<Map<String,Object>> travelers;
	private String memo;

	public Map<String, String> getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(Map<String, String> baseInfo) {
		this.baseInfo = baseInfo;
	}

	public Map<String, Object> getReservations() {
		return reservations;
	}

	public void setReservations(Map<String, Object> reservations) {
		this.reservations = reservations;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<Map<String, String>> getFlights() {
		return flights;
	}

	public void setFlights(List<Map<String, String>> flights) {
		this.flights = flights;
	}

	public List<Map<String, String>> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Map<String, String>> attachment) {
		this.attachment = attachment;
	}

	public List<Map<String, Object>> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<Map<String, Object>> travelers) {
		this.travelers = travelers;
	}

	public Map<String, Object> getFee() {
		return fee;
	}

	public void setFee(Map<String, Object> fee) {
		this.fee = fee;
	}

	public static void main(String[] args) {
		AirticketOrderDetailJsonBean json = new AirticketOrderDetailJsonBean();
		Map<String, String> baseInfoMap = new  TreeMap<String, String>();
		baseInfoMap.put("orderUuid", "dsahfdah56f465");
		baseInfoMap.put("productName", "AirticketProduct");
		json.setBaseInfo(baseInfoMap);
		Map<String, Object> reservationsMap = new TreeMap<String, Object>();
		List<Map<String, String>> contactsList = new ArrayList<Map<String, String>>();
		reservationsMap.put("channelTypeUuid", "jkgkfhfj");
		reservationsMap.put("channelUuid", "channelA");
		Map<String, String> contacts1= new TreeMap<String, String>();
		contacts1.put("name","姓名1");
		contacts1.put("phone","电话号码1");
		Map<String, String> contacts2= new TreeMap<String, String>();
		contacts2.put("name","姓名2");
		contacts2.put("phone","电话号码2");
		Map<String, String> contacts3= new TreeMap<String, String>();
		contacts3.put("name","姓名3");
		contacts3.put("phone","电话号码3");
		contactsList.add(contacts1);
		contactsList.add(contacts2);
		contactsList.add(contacts3);
		reservationsMap.put("contacts", contactsList);
		json.setReservations(reservationsMap);
		json.setMemo("wozaiceshi");
		System.out.println(JSON.toJSONString(json));
		System.out.println(System.getProperty("user.dir"));
	}
}
