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

public class HotelPlTaxPriceQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"地接供应商（批发商维护的地接社supplier_info表中的id）"
	private java.lang.Integer supplierInfoId;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"税费类型(1、政府税；2、服务费；3、床税；4、其他)"
	private java.lang.Integer taxType;
	//"税费名称（1、政府税；2、服务费；3、床税；4、其他税费时会手动输入税费名称）显示时使用"
	private java.lang.String taxName;
	//"起始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"币种id（为了后期支持多币种所有的金额都有对应的币种ID）。"
	private java.lang.Integer currencyId;
	//"金额(1：百分比收税、2：固定税收金额)"
	private Double amount;
	//"收费类型（1、%；2、￥）"
	private java.lang.Integer chargeType;
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
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
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
	public void setTaxType(java.lang.Integer value) {
		this.taxType = value;
	}
	public java.lang.Integer getTaxType() {
		return this.taxType;
	}
	public void setTaxName(java.lang.String value) {
		this.taxName = value;
	}
	public java.lang.String getTaxName() {
		return this.taxName;
	}
	public void setStartDate(java.util.Date value) {
		this.startDate = value;
	}
	public java.util.Date getStartDate() {
		return this.startDate;
	}
	public void setEndDate(java.util.Date value) {
		this.endDate = value;
	}
	public java.util.Date getEndDate() {
		return this.endDate;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setAmount(Double value) {
		this.amount = value;
	}
	public Double getAmount() {
		return this.amount;
	}
	public void setChargeType(java.lang.Integer value) {
		this.chargeType = value;
	}
	public java.lang.Integer getChargeType() {
		return this.chargeType;
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

