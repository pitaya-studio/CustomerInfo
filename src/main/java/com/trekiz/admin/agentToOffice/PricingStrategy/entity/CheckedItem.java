package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

public class CheckedItem {
	private String itemName;
	private String itemValue;
	private boolean checkedFlag;
	
	public CheckedItem(String value, boolean checkedFlag) {
		this.itemName = value;
		this.checkedFlag = checkedFlag;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public boolean isCheckedFlag() {
		return checkedFlag;
	}
	public void setCheckedFlag(boolean checkedFlag) {
		this.checkedFlag = checkedFlag;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public CheckedItem(String itemName, String itemValue, boolean checkedFlag) {
		super();
		this.itemName = itemName;
		this.itemValue = itemValue;
		this.checkedFlag = checkedFlag;
	}
	
}
