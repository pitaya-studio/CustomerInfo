/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class ActivityIslandGroupMealRiseQuery  {
	
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activityIslandUuid;
	private java.lang.String activityIslandGroupUuid;
	private java.lang.String activityIslandGroupMealUuid;
	
	private java.lang.String hotelMealUuid;
	private java.lang.Integer currencyId;
	private Double price;
	private java.lang.Long createBy;
	private java.util.Date createDate;
	private java.lang.Long updateBy;
	private java.util.Date updateDate;
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
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	public void setActivityIslandGroupUuid(java.lang.String value) {
		this.activityIslandGroupUuid = value;
	}
	public java.lang.String getActivityIslandGroupUuid() {
		return this.activityIslandGroupUuid;
	}
	public void setHotelMealUuid(java.lang.String value) {
		this.hotelMealUuid = value;
	}
	public java.lang.String getHotelMealUuid() {
		return this.hotelMealUuid;
	}
	public java.lang.String getActivityIslandGroupMealUuid() {
		return activityIslandGroupMealUuid;
	}
	public void setActivityIslandGroupMealUuid(
			java.lang.String activityIslandGroupMealUuid) {
		this.activityIslandGroupMealUuid = activityIslandGroupMealUuid;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setPrice(Double value) {
		this.price = value;
	}
	public Double getPrice() {
		return this.price;
	}
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	public java.lang.Long getCreateBy() {
		return this.createBy;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	public java.lang.Long getUpdateBy() {
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

