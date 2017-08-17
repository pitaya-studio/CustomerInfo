/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelPlInput  extends BaseInput {
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"地接供应商（批发商维护的地接社supplier_info表中的id）"
	private java.lang.Integer supplierInfoId;
	//"价单名称"
	private java.lang.String name;
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
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//columns END
	private HotelPl dataObj ;
	
	public HotelPlInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelPlInput(HotelPl obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelPl getHotelPl() {
		dataObj = new HotelPl();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	
	
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
	public void setSupplierInfoId(java.lang.Integer value) {
		this.supplierInfoId = value;
	}
	public java.lang.Integer getSupplierInfoId() {
		return this.supplierInfoId;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
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


	
}

