package com.trekiz.admin.modules.mtourfinance.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订金统计-订金统计的jsonBean
 * @author wangyang
 * @date 2016.6.20
 * */
public class FrontMoneyStatJsonBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//销售id
	private String salerId;
	//销售名称
	private String salerName;
	//订金数据列表
	private List<FrontMoneyStatData> orders = new ArrayList<FrontMoneyStatData>();
	
	
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
//	public List<FrontMoneyStatData> getDatalist() {
//		return orders;
//	}
//	public void setDatalist(List<FrontMoneyStatData> orders) {
//		this.orders = orders;
//	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public List<FrontMoneyStatData> getOrders() {
		return orders;
	}
	public void setOrders(List<FrontMoneyStatData> orders) {
		this.orders = orders;
	}
	
	
}
