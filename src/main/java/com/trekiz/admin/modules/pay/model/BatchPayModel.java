package com.trekiz.admin.modules.pay.model;

import java.util.List;

/**
 * 批量支付使用的实体类
* <p>description: TODO</p>
* <p>Date: 2015-8-26 下午4:36:18</p>
* <p>modify：</p>
* @author: jangta
* @version: 3.0
* </p>Company: jangt.com</p>
 */
public class BatchPayModel {
	private Integer travelerid;
	private Integer orderid;
	private List<CurrencyAmount> currencyAmounts;
	public Integer getTravelerid() {
		return travelerid;
	}
	public void setTravelerid(Integer travelerid) {
		this.travelerid = travelerid;
	}
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	public List<CurrencyAmount> getCurrencyAmounts() {
		return currencyAmounts;
	}
	public void setCurrencyAmounts(List<CurrencyAmount> currencyAmounts) {
		this.currencyAmounts = currencyAmounts;
	}
	
	
}
