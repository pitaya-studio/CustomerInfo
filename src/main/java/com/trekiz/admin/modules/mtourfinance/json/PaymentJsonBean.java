package com.trekiz.admin.modules.mtourfinance.json;

import java.math.BigDecimal;

/**
 * 批量付款金额信息jsonBean
 * ClassName: PaymentJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-1-21
 */
public class PaymentJsonBean {
	private String paymentUuid;//付款Uuid
	private Integer currencyUuid;//币种id
	private BigDecimal paymentAmount;//付款金额
	private BigDecimal exchangeRate;//汇率
	private String fundsType;//'款项类型//成本:1,退款:2,借款:3,追加成本:4
	private String tourOperatorChannelCategoryCode;//地接社/渠道商' 1-地接社 2-渠道商
	public String getPaymentUuid() {
		return paymentUuid;
	}
	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}
	public Integer getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getFundsType() {
		return fundsType;
	}
	public void setFundsType(String fundsType) {
		this.fundsType = fundsType;
	}
	public String getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}
	public void setTourOperatorChannelCategoryCode(
			String tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}

}
