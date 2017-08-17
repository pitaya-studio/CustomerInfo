package com.trekiz.admin.modules.order.entity;

/**
 * 订单详细信息，主要用于订单数据的统计
 * @author shijun.liu
 *
 */
public class OrderDetail {

	private String supplierName;		//供应商
	private String agentName;           //渠道
	private String orderNum;            //订单编号
	private String orderPersonName;     //下单人名称
	private String orderTime;           //下单时间
	private String productName;         //产品名称
	private String groupCode;           //团号
	private String orderPersonCount;    //订单人数
	private String orderStatus;         //订单状态
	private String totalMoney;          //订单总金额
	private String payedMoney;          //应收金额
	private String accountedMoney;      //已付金额
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderPersonName() {
		return orderPersonName;
	}
	public void setOrderPersonName(String orderPersonName) {
		this.orderPersonName = orderPersonName;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getOrderPersonCount() {
		return orderPersonCount;
	}
	public void setOrderPersonCount(String orderPersonCount) {
		this.orderPersonCount = orderPersonCount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getPayedMoney() {
		return payedMoney;
	}
	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}
	public String getAccountedMoney() {
		return accountedMoney;
	}
	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}
	
}
