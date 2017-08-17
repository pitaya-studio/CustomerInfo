package com.trekiz.admin.modules.cost.entity;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 作为参数传递的对象
 * @author shijun.liu
 * @date 2015年11月12日
 */
public class ParamBean {

	private String groupCode;		//团号
	private String orderNum;		//订单号
	private Integer orderType;		//订单类型
	private String orderBeginDate;	//下单时间
	private String orderEndDate;	//下单时间
	private Long orderedId ;		//下单人
	private String orderedName;		//下单人名称
	private Long agentId;			//渠道ID
	private String agentName;		//渠道名称
	private Long salerId;			//销售ID
	private String salerName;		//销售名称
	private Long deptId;			//部门ID
	private String deptName;		//部门名称
	private Long operatorId;		//计调ID
	private String operatorName;	//计调
	private String badAccount;		//是否是坏账
	private String paymentType;     // 渠道结款方式
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getOrderBeginDate() {
		return orderBeginDate;
	}
	public void setOrderBeginDate(String orderBeginDate) {
		this.orderBeginDate = orderBeginDate;
	}
	public String getOrderEndDate() {
		return orderEndDate;
	}
	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}
	public Long getOrderedId() {
		return orderedId;
	}
	public void setOrderedId(Long orderedId) {
		this.orderedId = orderedId;
	}
	public String getOrderedName() {
		return orderedName;
	}
	public void setOrderedName(String orderedName) {
		this.orderedName = orderedName;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public Long getSalerId() {
		return salerId;
	}
	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getBadAccount() {
		return badAccount;
	}
	public void setBadAccount(String badAccount) {
		this.badAccount = badAccount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
}
