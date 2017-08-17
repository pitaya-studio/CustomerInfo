package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

import com.trekiz.admin.modules.mtourfinance.entity.PriceAmount;

/**
 * 订单明细
 * @author gao
 * @date 2015年10月23日
 */
public class OrderDetailJsonBean {

	private String orderUuid;//订单id'
	private String orderNum;//订单号
	private String groupNo;//'团号'
	private String productName;//产品名称'
	private String salesName;//销售'
	private List<String> operator;//计调',可以是多个，update by yudong.xu 2016.6.21
	private String orderer;//下单人'
	private String orderTime;//'下单时间'
	private String peopleCount;//人数'
	private String departureDate;//'出团日期'
	private String orderAmountUuid;//外报总额uuid
	private String receivedAmountUuid;//已收金额uuid'
	private String arrivedAmountUuid;//到账金额uuid'
	private List<PriceAmount> orderAmount;//外报总额
	private List<PriceAmount> receivedAmount;//已收金额
	private List<PriceAmount> arrivedAmount;//到账金额
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public List<String> getOperator() {
		return operator;
	}

	public void setOperator(List<String> operator) {
		this.operator = operator;
	}

	public String getOrderer() {
		return orderer;
	}
	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getReceivedAmountUuid() {
		return receivedAmountUuid;
	}
	public void setReceivedAmountUuid(String receivedAmountUuid) {
		this.receivedAmountUuid = receivedAmountUuid;
	}
	public String getArrivedAmountUuid() {
		return arrivedAmountUuid;
	}
	public void setArrivedAmountUuid(String arrivedAmountUuid) {
		this.arrivedAmountUuid = arrivedAmountUuid;
	}
	public List<PriceAmount> getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(List<PriceAmount> receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public List<PriceAmount> getArrivedAmount() {
		return arrivedAmount;
	}
	public void setArrivedAmount(List<PriceAmount> arrivedAmount) {
		this.arrivedAmount = arrivedAmount;
	}
	public String getOrderAmountUuid() {
		return orderAmountUuid;
	}
	public void setOrderAmountUuid(String orderAmountUuid) {
		this.orderAmountUuid = orderAmountUuid;
	}
	public List<PriceAmount> getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(List<PriceAmount> orderAmount) {
		this.orderAmount = orderAmount;
	}
}
