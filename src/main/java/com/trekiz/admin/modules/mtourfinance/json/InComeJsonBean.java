package com.trekiz.admin.modules.mtourfinance.json;

/**
 * 结算单收入项类
 * @author shijun.liu
 *
 */
public class InComeJsonBean{
	//收款日期
	private String receiveDate;
	//客户名称
	private String customer;
	//人数
	private String peopleCount;
	//定金
	private String deposit;
	//尾款(全款)
	private String balancePayment;
	//总计
	private String totalAmount;
	//币种
	private String currencyUuid;
	//币种符号
	private String currencyMark;
	//汇率
	private String exchangeRate;
	//折合人民币
	private String rmb;
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getBalancePayment() {
		return balancePayment;
	}
	public void setBalancePayment(String balancePayment) {
		this.balancePayment = balancePayment;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(String currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getRmb() {
		return rmb;
	}
	public void setRmb(String rmb) {
		this.rmb = rmb;
	}
	
}