package com.trekiz.admin.modules.airticketorder.entity;

import java.io.Serializable;

public class OrderAgentAjax implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String airticketId;
	private String flightRemark;
	private String agentId;
	private String agentName;
	private String agentContact;
	private String agentTel;
	private String agentAddress;
	private String agentFax;
	private String agentQQ;
	private String agentEmail;
	private String agentRemarks;
	private String orderAllP;//订单应收价
	
	
	public String getOrderAllP() {
		return orderAllP;
	}
	public void setOrderAllP(String orderAllP) {
		this.orderAllP = orderAllP;
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
	public String getFlightRemark() {
		return flightRemark;
	}
	public void setFlightRemark(String flightRemark) {
		this.flightRemark = flightRemark;
	}
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentContact() {
		return agentContact;
	}
	public void setAgentContact(String agentContact) {
		this.agentContact = agentContact;
	}
	public String getAgentTel() {
		return agentTel;
	}
	public void setAgentTel(String agentTel) {
		this.agentTel = agentTel;
	}
	public String getAgentAddress() {
		return agentAddress;
	}
	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}
	public String getAgentFax() {
		return agentFax;
	}
	public void setAgentFax(String agentFax) {
		this.agentFax = agentFax;
	}
	public String getAgentQQ() {
		return agentQQ;
	}
	public void setAgentQQ(String agentQQ) {
		this.agentQQ = agentQQ;
	}
	public String getAgentEmail() {
		return agentEmail;
	}
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}
	public String getAgentRemarks() {
		return agentRemarks;
	}
	public void setAgentRemarks(String agentRemarks) {
		this.agentRemarks = agentRemarks;
	}

}
