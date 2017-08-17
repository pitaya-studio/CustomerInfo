package com.trekiz.admin.modules.mtourfinance.json;


public class CostRecordJsonBean extends BigCode {
	private String saveType; //保存类型
	private String orderUuid; //订单id
	private String costUuid; //成本记录id
	private String otherRevenueUuid; //其他收入录入id
	private String receiveUuid;//'收款记录'//91需求中其他收入记录的收入单中的uuid和财务收款列表的收入记录数据要保持一致
	private String fundsName; //款项名称
	private String tourOperatorChannelCategoryCode; //区分 地接社/渠道商
	private String tourOperatorOrChannelTypeCode; //地接社/渠道商的类型Code
	private String tourOperatorOrChannelName; //地接社/渠道商 的名称
	private String tourOperatorOrChannelUuid; //地接社/渠道商 的id
	
	private String peopleCount; //人数
	private String currencyUuid; //转换前币种id
	private String amount; //转换前金额
	private String exchangeRate; //汇率
	private String convertedCurrencyUuid; //转换后币种id
	private String convertedAmount; //转换后金额
	private String inputPerson; //录入人
	private String userName; //录入人名称
	private String paymentBank; //付款行名称
	private String paymentAccount; //付款账号
	private String state; //成本记录状态
	private String stateCode; //状态
	private String memo; //备注
	private String lockStatus; //结算单锁定状态

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public String getCostUuid() {
		return costUuid;
	}

	public void setCostUuid(String costUuid) {
		this.costUuid = costUuid;
	}

	public String getOtherRevenueUuid() {
		return otherRevenueUuid;
	}

	public void setOtherRevenueUuid(String otherRevenueUuid) {
		this.otherRevenueUuid = otherRevenueUuid;
	}

	public String getFundsName() {
		return fundsName;
	}

	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}

	public String getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}

	public void setTourOperatorChannelCategoryCode(
			String tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}

	public String getTourOperatorOrChannelTypeCode() {
		return tourOperatorOrChannelTypeCode;
	}

	public void setTourOperatorOrChannelTypeCode(
			String tourOperatorOrChannelTypeCode) {
		this.tourOperatorOrChannelTypeCode = tourOperatorOrChannelTypeCode;
	}

	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}

	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}

	public String getTourOperatorOrChannelUuid() {
		return tourOperatorOrChannelUuid;
	}

	public void setTourOperatorOrChannelUuid(String tourOperatorOrChannelUuid) {
		this.tourOperatorOrChannelUuid = tourOperatorOrChannelUuid;
	}

	

	public String getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}

	public String getCurrencyUuid() {
		return currencyUuid;
	}

	public void setCurrencyUuid(String currencyUuid) {
		this.currencyUuid = currencyUuid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getConvertedCurrencyUuid() {
		return convertedCurrencyUuid;
	}

	public void setConvertedCurrencyUuid(String convertedCurrencyUuid) {
		this.convertedCurrencyUuid = convertedCurrencyUuid;
	}

	public String getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(String convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public String getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(String inputPerson) {
		this.inputPerson = inputPerson;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getReceiveUuid() {
		return receiveUuid;
	}

	public void setReceiveUuid(String receiveUuid) {
		this.receiveUuid = receiveUuid;
	}

}
