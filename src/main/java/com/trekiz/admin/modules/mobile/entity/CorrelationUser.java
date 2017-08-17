package com.trekiz.admin.modules.mobile.entity;

import java.io.Serializable;

public class CorrelationUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mobileUserId;//微信用户id
	private String userId;//t2用户id
	private String agentId;//渠道id
	private String agentContactMobile;//手机号
	private String agentContact;//姓名
	private String agentName;//渠道名称
	private String wxCode;//微信号
	private String leftAgentContactTel;//左边-区号
	private String rightAgentContactTel;//右边-座机号
	private String agentBrand;//渠道品牌
	private String agentAddress;
	private Long agentAddressProvince;
	private Long agentAddressCity;
	private String agentAddressStreet;


	public String getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentContactMobile() {
		return agentContactMobile;
	}

	public void setAgentContactMobile(String agentContactMobile) {
		this.agentContactMobile = agentContactMobile;
	}

	public String getAgentContact() {
		return agentContact;
	}

	public void setAgentContact(String agentContact) {
		this.agentContact = agentContact;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getWxCode() {
		return wxCode;
	}

	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}

	public String getLeftAgentContactTel() {
		return leftAgentContactTel;
	}

	public void setLeftAgentContactTel(String leftAgentContactTel) {
		this.leftAgentContactTel = leftAgentContactTel;
	}

	public String getRightAgentContactTel() {
		return rightAgentContactTel;
	}

	public void setRightAgentContactTel(String rightAgentContactTel) {
		this.rightAgentContactTel = rightAgentContactTel;
	}

	public String getAgentBrand() {
		return agentBrand;
	}

	public void setAgentBrand(String agentBrand) {
		this.agentBrand = agentBrand;
	}

	public String getAgentAddress() {
		return agentAddress;
	}

	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}

	public Long getAgentAddressProvince() {
		return agentAddressProvince;
	}

	public void setAgentAddressProvince(Long agentAddressProvince) {
		this.agentAddressProvince = agentAddressProvince;
	}

	public Long getAgentAddressCity() {
		return agentAddressCity;
	}

	public void setAgentAddressCity(Long agentAddressCity) {
		this.agentAddressCity = agentAddressCity;
	}

	public String getAgentAddressStreet() {
		return agentAddressStreet;
	}

	public void setAgentAddressStreet(String agentAddressStreet) {
		this.agentAddressStreet = agentAddressStreet;
	}
}
