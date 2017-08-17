package com.trekiz.admin.modules.airticketorder.entity;

import java.io.Serializable;

public class OrderTravelAjax implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String airticketId;
	private String travelId;
	private String travelName;
	private String travelNamePinyin;
	private String travelerSex;
	private String nationality;
	private String birthDay;
	private String telephone;
	private String passportCode;
	private String passportValidity;
	private String idCard;
	private String remarks;
	private String personType;
	private String passportType;
	private String intermodalId;
	private String intermodalType;
	private String currencyId;
	private String payPriceSerialNum;
	private String payPrice;//结算价
	
	public String getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}
	public String getPayPriceSerialNum() {
		return payPriceSerialNum;
	}
	public void setPayPriceSerialNum(String payPriceSerialNum) {
		this.payPriceSerialNum = payPriceSerialNum;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAirticketId() {
		return airticketId;
	}
	public void setAirticketId(String airticketId) {
		this.airticketId = airticketId;
	}
	public String getTravelId() {
		return travelId;
	}
	public void setTravelId(String travelId) {
		this.travelId = travelId;
	}
	public String getTravelName() {
		return travelName;
	}
	public void setTravelName(String travelName) {
		this.travelName = travelName;
	}
	public String getTravelNamePinyin() {
		return travelNamePinyin;
	}
	public void setTravelNamePinyin(String travelNamePinyin) {
		this.travelNamePinyin = travelNamePinyin;
	}
	public String getTravelerSex() {
		return travelerSex;
	}
	public void setTravelerSex(String travelerSex) {
		this.travelerSex = travelerSex;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPassportCode() {
		return passportCode;
	}
	public void setPassportCode(String passportCode) {
		this.passportCode = passportCode;
	}
	public String getPassportValidity() {
		return passportValidity;
	}
	public void setPassportValidity(String passportValidity) {
		this.passportValidity = passportValidity;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}
	public String getPassportType() {
		return passportType;
	}
	public void setPassportType(String passportType) {
		this.passportType = passportType;
	}
	public String getIntermodalId() {
		return intermodalId;
	}
	public void setIntermodalId(String intermodalId) {
		this.intermodalId = intermodalId;
	}
	public String getIntermodalType() {
		return intermodalType;
	}
	public void setIntermodalType(String intermodalType) {
		this.intermodalType = intermodalType;
	}
	
}
