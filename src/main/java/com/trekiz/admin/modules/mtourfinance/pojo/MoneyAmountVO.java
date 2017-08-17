package com.trekiz.admin.modules.mtourfinance.pojo;

import java.io.Serializable;

public class MoneyAmountVO implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	
	private Integer currencyUuid;//'币种Uuid',
    private Double amount;//'金额',
    private Double exchangeRate;//汇率
    private Boolean changeable;	//汇率是否可修改。true可修改，false不可修改
	public Integer getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Boolean getChangeable() {
		return changeable;
	}
	public void setChangeable(Boolean changeable) {
		this.changeable = changeable;
	}
	
	@Override
	public MoneyAmountVO clone() {
		MoneyAmountVO mao = null;
		try {
			mao = (MoneyAmountVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return mao;
	}
}
