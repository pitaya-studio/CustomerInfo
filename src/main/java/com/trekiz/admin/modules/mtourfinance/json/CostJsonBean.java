package com.trekiz.admin.modules.mtourfinance.json;

/**
 * 结算单成本项类
 * @author shijun.liu
 *
 */
public class CostJsonBean {

	//项目
	private String fundsName;
	//PNR或者地接社的的代码
	private String invoiceOriginalTypeCode;
	//PNR编号
	private String PNR;
	//地接社Name
	private String tourOperatorName;
	//航空公司
	private String airlineCompany;
	//人数
	private String peopleCount;
	//成本
	private String price;
	//总计
	private String totalAmount;
	//币种ID
	private String currencyUuid;
	//币种符号
	private String currencyMark;
	//汇率
	private String exchangeRate;
	//折合人民币
	private String rmb;
	public String getFundsName() {
		return fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public String getInvoiceOriginalTypeCode() {
		return invoiceOriginalTypeCode;
	}
	public void setInvoiceOriginalTypeCode(String invoiceOriginalTypeCode) {
		this.invoiceOriginalTypeCode = invoiceOriginalTypeCode;
	}
	public String getPNR() {
		return PNR;
	}
	public void setPNR(String pNR) {
		this.PNR = pNR;
	}
	public String getTourOperatorName() {
		return tourOperatorName;
	}
	public void setTourOperatorName(String tourOperatorName) {
		this.tourOperatorName = tourOperatorName;
	}
	public String getAirlineCompany() {
		return airlineCompany;
	}
	public void setAirlineCompany(String airlineCompany) {
		this.airlineCompany = airlineCompany;
	}
	public String getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
