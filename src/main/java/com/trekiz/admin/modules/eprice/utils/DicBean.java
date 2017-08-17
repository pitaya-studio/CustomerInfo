package com.trekiz.admin.modules.eprice.utils;

public class DicBean{
	
	public DicBean(String key1  ,String val){
		this.key = key1;
		this.value = val;
	}
	
	public DicBean(  Integer intk ,String val){
		this.value = val;
		this.intKey = intk;
	}
	
	private String key;
	private Integer intKey;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getIntKey() {
		return intKey;
	}
	public void setIntKey(Integer intKey) {
		this.intKey = intKey;
	}
	
	
}