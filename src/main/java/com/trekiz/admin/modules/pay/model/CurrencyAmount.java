package com.trekiz.admin.modules.pay.model;

/**
 * 批量支付使用的金额类，里面包含币种还有价格
* <p>description: TODO</p>
* <p>Date: 2015-8-26 下午4:37:23</p>
* <p>modify：</p>
* @author: jangta
* @version: 3.0
* </p>Company: jangt.com</p>
 */
public class CurrencyAmount {
	private Integer currencyid;
	private Double price;
	
	public Integer getCurrencyid() {
		return currencyid;
	}
	public void setCurrencyid(Integer currencyid) {
		this.currencyid = currencyid;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
