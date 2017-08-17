package com.trekiz.admin.modules.order.formBean;

import java.util.ArrayList;
import java.util.List;

import com.trekiz.admin.modules.sys.entity.Currency;

public class OrderPaySuccessBean {
	private String orderNum;
	private String orderType;
	private String payTypeName;
	private Integer[] currencyIdPrice;
	private List<Currency> curlist=new ArrayList<Currency>();
	private List<String> dqzfprice;
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public Integer[] getCurrencyIdPrice() {
		return currencyIdPrice;
	}
	public void setCurrencyIdPrice(Integer[] currencyIdPrice) {
		this.currencyIdPrice = currencyIdPrice;
	}
	

	public List<Currency> getCurlist() {
		return curlist;
	}
	public void setCurlist(List<Currency> curlist) {
		this.curlist = curlist;
	}
	public List<String> getDqzfprice() {
		return dqzfprice;
	}
	public void setDqzfprice(List<String> dqzfprice) {
		this.dqzfprice = dqzfprice;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
}
