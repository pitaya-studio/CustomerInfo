package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;

/***
 * 收款列表vo
 * @author zhaohaiming
 * */
public class OrderlistPoJO  extends Object{
    
	public String orderUuid;//'订单Uuid',
	public String orderNum;//订单编号
	public String receiveUuid;//'收款Uuid'
	public String receiveDate;//'收款日期'
	public String arrivalBankDate;//'银行到账日期'
	public String fundsName;//'款项名称'
	public String fundsType;//'款项类型',
	public String  groupNo;//'团号'
	public String productName;//'产品名称'
	public String departureDate;//'出团日期'
	public String receiveTypeCode;
	public String receiveTypeName;//'收款类别'
	public String tourOperatorOrChannelName;//'地接社名称'，
	public String paymentCompany;//'付款单位'
	public String receiver;//'收款人'
	public String receiveStatusCode;//收款状态
	public List<MoneyAmountVO> orderAmount;//订单总额
	public List<MoneyAmountVO> totalArrivedAmount;//累计到账金额'
	public List<MoneyAmountVO> receivedAmount;//已收金额
	public List<MoneyAmountVO> arrivedAmount;//'到账金额'
	private ReceiveFundsTypeVO receiveFundsType;//收款类型
	public String modifiedDateTime;
	
	
	
	
	
	
	public String getReceiveTypeCode() {
		return receiveTypeCode;
	}
	public void setReceiveTypeCode(String receiveTypeCode) {
		this.receiveTypeCode = receiveTypeCode;
	}
	public String getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(String modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public ReceiveFundsTypeVO getReceiveFundsType() {
		return receiveFundsType;
	}
	public void setReceiveFundsType(ReceiveFundsTypeVO receiveFundsType) {
		this.receiveFundsType = receiveFundsType;
	}
	public String getReceiveStatusCode() {
		return receiveStatusCode;
	}
	public void setReceiveStatusCode(String receiveStatusCode) {
		this.receiveStatusCode = receiveStatusCode;
	}
	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}
	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}
	public String getReceiveTypeName() {
		return receiveTypeName;
	}
	public void setReceiveTypeName(String receiveTypeName) {
		this.receiveTypeName = receiveTypeName;
	}
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
	public String getReceiveUuid() {
		return receiveUuid;
	}
	public void setReceiveUuid(String receiveUuid) {
		this.receiveUuid = receiveUuid;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getArrivalBankDate() {
		return arrivalBankDate;
	}
	public void setArrivalBankDate(String arrivalBankDate) {
		this.arrivalBankDate = arrivalBankDate;
	}
	public String getFundsName() {
		return fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public String getFundsType() {
		return fundsType;
	}
	public void setFundsType(String fundsType) {
		this.fundsType = fundsType;
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
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
	public String getPaymentCompany() {
		return paymentCompany;
	}
	public void setPaymentCompany(String paymentCompany) {
		this.paymentCompany = paymentCompany;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public List<MoneyAmountVO> getTotalArrivedAmount() {
		return totalArrivedAmount;
	}
	public void setTotalArrivedAmount(List<MoneyAmountVO> totalArrivedAmount) {
		this.totalArrivedAmount = totalArrivedAmount;
	}
	public List<MoneyAmountVO> getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(List<MoneyAmountVO> receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public List<MoneyAmountVO> getArrivedAmount() {
		return arrivedAmount;
	}
	public void setArrivedAmount(List<MoneyAmountVO> arrivedAmount) {
		this.arrivedAmount = arrivedAmount;
	}
	public List<MoneyAmountVO> getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(List<MoneyAmountVO> orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	
	
	
}
