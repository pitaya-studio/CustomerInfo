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

public class HotelPlMealriseQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店餐型uuid（数据源是从酒店房型餐型关联表中获得，保存时映射到酒店餐型表中即：hotel_meal表中的uuid）"
	private java.lang.String hotelMealUuid;
	//"酒店餐型uuid（hotel_meal表中的uuid）"
	private java.lang.String hotelMealRiseUuid;
	//"起始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"游客类型uuid"
	private java.lang.String travelerTypeUuid;
	//"币种id（为了后期支持多币种所有的金额都有对应的币种ID）。"
	private java.lang.Integer currencyId;
	//"金额"
	private Double amount;
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
	public void setHotelMealUuid(java.lang.String value) {
		this.hotelMealUuid = value;
	}
	public java.lang.String getHotelMealUuid() {
		return this.hotelMealUuid;
	}
	public void setHotelMealRiseUuid(java.lang.String value) {
		this.hotelMealRiseUuid = value;
	}
	public java.lang.String getHotelMealRiseUuid() {
		return this.hotelMealRiseUuid;
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
	public void setTravelerTypeUuid(java.lang.String value) {
		this.travelerTypeUuid = value;
	}
	public java.lang.String getTravelerTypeUuid() {
		return this.travelerTypeUuid;
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

