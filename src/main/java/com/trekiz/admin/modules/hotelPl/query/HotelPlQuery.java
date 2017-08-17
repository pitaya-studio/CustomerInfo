/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.query;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class HotelPlQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"价单名称"
	private java.lang.String name;
	//"地接供应商（批发商维护的地接社supplier_info表中的id）"
	private java.lang.Integer supplierInfoId;
	//"币种ID"
	private java.lang.Integer currencyId;
	//"位置（1、境内；2、境外）"
	private java.lang.Integer position;
	//"国家"
	private java.lang.String country;
	//"类型（1、内陆；2、海岛）"
	private java.lang.Integer areaType;
	//"采购类型（0内采1外采）"
	private java.lang.Integer purchaseType;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"混住费币种ID，为了后期支持多币种所有的金额都有对应的币种ID。V1默认都存储默认的币种，即：价单的币种"
	private java.lang.Integer mixliveCurrencyId;
	//"混住费金额"
	private Double mixliveAmount;
	//"税金算法（1、连乘；2、分乘）"
	private java.lang.Integer taxArithmetic;
	//"节日餐备注"
	private java.lang.String galamealMemo;
	//"备注"
	private java.lang.String memo;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"开始创建时间"
	private java.util.Date startCreateDate;
	//"结束创建时间"
	private java.util.Date endCreateDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	
	//"酒店集团"
	private String hotelGroup;
	//"排序字段"
	private String orderBy;
	//"升序或降序"
	private String ascOrDesc;
	

	//"开始创建时间字符"
	private java.lang.String startCreateDateStr;
	//"结束创建时间字符"
	private java.lang.String endCreateDateStr;
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	public java.lang.String getUuid() {
		return this.uuid;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public void setSupplierInfoId(java.lang.Integer value) {
		this.supplierInfoId = value;
	}
	public java.lang.Integer getSupplierInfoId() {
		return this.supplierInfoId;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setPosition(java.lang.Integer value) {
		this.position = value;
	}
	public java.lang.Integer getPosition() {
		return this.position;
	}
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	public java.lang.String getCountry() {
		return this.country;
	}
	public void setAreaType(java.lang.Integer value) {
		this.areaType = value;
	}
	public java.lang.Integer getAreaType() {
		return this.areaType;
	}
	public void setPurchaseType(java.lang.Integer value) {
		this.purchaseType = value;
	}
	public java.lang.Integer getPurchaseType() {
		return this.purchaseType;
	}
	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}
	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	public void setMixliveCurrencyId(java.lang.Integer value) {
		this.mixliveCurrencyId = value;
	}
	public java.lang.Integer getMixliveCurrencyId() {
		return this.mixliveCurrencyId;
	}
	public void setMixliveAmount(Double value) {
		this.mixliveAmount = value;
	}
	public Double getMixliveAmount() {
		return this.mixliveAmount;
	}
	public void setTaxArithmetic(java.lang.Integer value) {
		this.taxArithmetic = value;
	}
	public java.lang.Integer getTaxArithmetic() {
		return this.taxArithmetic;
	}
	public void setGalamealMemo(java.lang.String value) {
		this.galamealMemo = value;
	}
	public java.lang.String getGalamealMemo() {
		return this.galamealMemo;
	}
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	public java.lang.String getMemo() {
		return this.memo;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public java.util.Date getStartCreateDate() {
		return startCreateDate;
	}
	public void setStartCreateDate(java.util.Date startCreateDate) {
		this.startCreateDate = startCreateDate;
	}
	public java.util.Date getEndCreateDate() {
		return endCreateDate;
	}
	public void setEndCreateDate(java.util.Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	public String getHotelGroup() {
		return hotelGroup;
	}
	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	public java.lang.String getStartCreateDateStr() {
		return startCreateDateStr;
	}
	public void setStartCreateDateStr(java.lang.String startCreateDateStr) {
		this.startCreateDateStr = startCreateDateStr;
	}
	public java.lang.String getEndCreateDateStr() {
		return endCreateDateStr;
	}
	public void setEndCreateDateStr(java.lang.String endCreateDateStr) {
		this.endCreateDateStr = endCreateDateStr;
	}
	
}

