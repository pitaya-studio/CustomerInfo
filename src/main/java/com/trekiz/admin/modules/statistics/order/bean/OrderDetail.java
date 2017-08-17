package com.trekiz.admin.modules.statistics.order.bean;

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
	private String airticketName;		//美途国际机票产品名称
	private String groupCode;           //团号
	private String orderPersonCount;    //订单人数
	private String orderStatus;         //订单状态
	private String totalMoney;          //订单总金额
	private String payedMoney;          //应收金额
	private String accountedMoney;      //已付金额
	private String productType;			//产品类型，1：单团，2：散拼。。。6：签证，7：机票，11:酒店，12：海岛游
	private String salerName;			//订单销售
	private String country;				//国家(机票产品只针对美途，华尔)，海岛，酒店产品，签证产品取签证国家
	private String departureAddress;	//出发地，只针对机票产品(美途国际除外)和团期类产品
	private String arriviedAddress;		//目的地，只针对机票产品(美途国际除外)和团期类产品
	private String area;				//区域	目前为空
	private String agentType; //渠道类型，是否是实时连通渠道
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
	public String getAirticketName() {
		return airticketName;
	}
	public void setAirticketName(String airticketName) {
		this.airticketName = airticketName;
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
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDepartureAddress() {
		return departureAddress;
	}
	public void setDepartureAddress(String departureAddress) {
		this.departureAddress = departureAddress;
	}
	public String getArriviedAddress() {
		return arriviedAddress;
	}
	public void setArriviedAddress(String arriviedAddress) {
		this.arriviedAddress = arriviedAddress;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	
}
