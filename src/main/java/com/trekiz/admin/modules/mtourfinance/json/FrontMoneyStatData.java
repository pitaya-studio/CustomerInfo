package com.trekiz.admin.modules.mtourfinance.json;

import java.io.Serializable;

/**
 * 用于订金统计数据中的jsonBean
 * @author wangyang
 * @date 2016.6.20
 * */
public class FrontMoneyStatData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//团号
	private String orderNum;
	//机票全款
	private String totalMoney;
	//未收全款=全款-已到账金额
	private String notAccountedTotalMoney;
	//订金
	private String frontMoney;
	//未收订金=订金-已收订金
	private String notAccountFrontMoney;
	//销售人员ID
	private String salerId;
	//销售人员名称
	private String salerName;
	
	
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getNotAccountedTotalMoney() {
		return notAccountedTotalMoney;
	}
	public void setNotAccountedTotalMoney(String notAccountedTotalMoney) {
		this.notAccountedTotalMoney = notAccountedTotalMoney;
	}
	public String getFrontMoney() {
		return frontMoney;
	}
	public void setFrontMoney(String frontMoney) {
		this.frontMoney = frontMoney;
	}
	public String getNotAccountFrontMoney() {
		return notAccountFrontMoney;
	}
	public void setNotAccountFrontMoney(String notAccountFrontMoney) {
		this.notAccountFrontMoney = notAccountFrontMoney;
	}
	
	
}
