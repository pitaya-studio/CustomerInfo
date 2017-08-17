package com.trekiz.admin.modules.mtourfinance.json;

/**
 * 结算单追加成本或者退款对象
 * @author 6CR433WKMN
 *
 */
public class AdditionCostRefundJsonBean {

	//追加成本金额或者退款金额
	private String amount;
	//币种
	private String currencyUuid;
	//币种符号
	private String currencyMark;
	//汇率
	private String exchangeRate;
	//转换人民币金额
	private String rmb;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
