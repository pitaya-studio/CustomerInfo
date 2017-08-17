package com.trekiz.admin.modules.mtourOrder.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单相关：款项类
 * @author gao
 * @date 2015年10月21日
 */
public class MtourOrderMoney implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer currencyUuid;	// 币种uuid
	private BigDecimal exchangeRate;	// 汇率
	private BigDecimal amount;	// 金额
	public Integer getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
