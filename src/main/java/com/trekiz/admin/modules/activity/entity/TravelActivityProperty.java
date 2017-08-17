package com.trekiz.admin.modules.activity.entity;

public class TravelActivityProperty {
	
	/**
	 * 团期id
	 */
	private String id;
	/**
	 * 团号
	 */
	private String value;
	/**
	 *  产品id
	 */
	private String productId;
	/**
	 *产品类型 
	 */
	private String productType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public TravelActivityProperty() {
		super();
	}
}