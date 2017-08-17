package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.List;
import java.util.Map;

public class AirticketOrderPNRListJsonBean {
	//订单id
	private String orderUuid;
	//'大编号组Uuid'
	private String invoiceOriginalGroupUuid;
	//'出票人数'
	private Integer drawerCount;
	//'预定人数'
	private Integer reserveCount;
	
	private Integer lockStatus;
	
	//invoiceOriginals
	private List<Map<String,Object>> invoiceOriginals;
	
	public String getInvoiceOriginalGroupUuid() {
		return invoiceOriginalGroupUuid;
	}

	public void setInvoiceOriginalGroupUuid(String invoiceOriginalGroupUuid) {
		this.invoiceOriginalGroupUuid = invoiceOriginalGroupUuid;
	}

	public Integer getDrawerCount() {
		return drawerCount;
	}

	public void setDrawerCount(Integer drawerCount) {
		this.drawerCount = drawerCount;
	}

	public Integer getReserveCount() {
		return reserveCount;
	}

	public void setReserveCount(Integer reserveCount) {
		this.reserveCount = reserveCount;
	}

	public List<Map<String,Object>> getInvoiceOriginals() {
		return invoiceOriginals;
	}

	public void setInvoiceOriginals(List<Map<String,Object>> invoiceOriginals) {
		this.invoiceOriginals = invoiceOriginals;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

}
