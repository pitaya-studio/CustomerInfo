package com.trekiz.admin.modules.order.entity;


/**
 * 单团类 销售明细
 * @author gao
 * @date 2015 11 12
 */
public class ActivityInfo {

	private String orderNo; // 订单号码
	private String shell;	// 销售人
	private String orderUser; // 下单人
	private String reserveDate; // 预订时间
	private Integer personNum; // 订单人数
	private String orderStatus;// 订单状态
	private String MainOrderStatus;// 主订单状态
	private String totalAmount; // 订单总额
	private String totalAmountUuid; // 订单总额Uuid
	private String payedAmount; //已付金额
	private String payedAmountUuid; //已付金额Uuid
	private String accountedAmount;// 达帐金额
	private String accountedAmountUuid;// 达帐金额Uuid
	private String agentName; // 渠道名称
	
	private String typeId;	 // 参团类型ID
	private String typeName; // 参团类型名称
	private Integer airTypeID; //机票类型ID 1-多段 2-往返 3-单程
	private String airTypeName; // 机票类型名称
	
	public Integer getAirTypeID() {
		return airTypeID;
	}
	public void setAirTypeID(Integer airTypeID) {
		this.airTypeID = airTypeID;
	}
	public String getAirTypeName() {
		return airTypeName;
	}
	public void setAirTypeName(String airTypeName) {
		this.airTypeName = airTypeName;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getShell() {
		return shell;
	}
	public void setShell(String shell) {
		this.shell = shell;
	}
	public String getOrderUser() {
		return orderUser;
	}
	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
	}
	public String getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	public Integer getPersonNum() {
		return personNum;
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPayedAmount() {
		return payedAmount;
	}
	public void setPayedAmount(String payedAmount) {
		this.payedAmount = payedAmount;
	}
	public String getAccountedAmount() {
		return accountedAmount;
	}
	public void setAccountedAmount(String accountedAmount) {
		this.accountedAmount = accountedAmount;
	}
	public String getTotalAmountUuid() {
		return totalAmountUuid;
	}
	public void setTotalAmountUuid(String totalAmountUuid) {
		this.totalAmountUuid = totalAmountUuid;
	}
	public String getPayedAmountUuid() {
		return payedAmountUuid;
	}
	public void setPayedAmountUuid(String payedAmountUuid) {
		this.payedAmountUuid = payedAmountUuid;
	}
	public String getAccountedAmountUuid() {
		return accountedAmountUuid;
	}
	public void setAccountedAmountUuid(String accountedAmountUuid) {
		this.accountedAmountUuid = accountedAmountUuid;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getMainOrderStatus() {
		return MainOrderStatus;
	}
	public void setMainOrderStatus(String mainOrderStatus) {
		MainOrderStatus = mainOrderStatus;
	}
}
