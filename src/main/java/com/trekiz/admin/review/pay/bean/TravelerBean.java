package com.trekiz.admin.review.pay.bean;

import java.util.Map;

/**
 * 
 * @author shijun.liu
 * @date   2016.01.20
 *  游客列表导出实体类
 */
public class TravelerBean {

	private String createDate;		//日期
	private String groupCode;		//团号
	private String traveler;		//游客
	private String salerName;		//销售
	private Map<String, String> map;//销售和游客之和
	private String personNum;		//人数
	private String price;			//借款单价
	private String visaCountry;		//签证国家
	private Integer personCount;	//总人数
	private String borrowSumMoney;	//借款总额
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getTraveler() {
		return traveler;
	}
	public void setTraveler(String traveler) {
		this.traveler = traveler;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getPersonNum() {
		return personNum;
	}
	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVisaCountry() {
		return visaCountry;
	}
	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}
	public Integer getPersonCount() {
		return personCount;
	}
	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}
	public String getBorrowSumMoney() {
		return borrowSumMoney;
	}
	public void setBorrowSumMoney(String borrowSumMoney) {
		this.borrowSumMoney = borrowSumMoney;
	}
	
}
