package com.trekiz.admin.modules.mtourfinance.entity;

import java.math.BigDecimal;

/**
 * 订单明细:金额类
 * @author gao
 * @date 2015年10月23日
 */
public class PriceAmount {

	private Integer currencyUuid; //币种Uuid',
	private BigDecimal amount; //金额'
	public Integer getCurrencyUuid() {
		return currencyUuid;
	}
	public void setCurrencyUuid(Integer currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
