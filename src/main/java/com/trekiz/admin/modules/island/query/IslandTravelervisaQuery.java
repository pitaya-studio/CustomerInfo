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

public class IslandTravelervisaQuery  {
	
	//columns START
	private java.lang.Long id;
	private java.lang.String uuid;
	private java.lang.String islandOrderUuid;
	private java.lang.String islandTravelerUuid;
	private java.lang.String country;
	private java.lang.Integer visaTypeId;
	private java.lang.Long createBy;
	private java.util.Date createDate;
	private java.lang.Long updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	public java.lang.Long getId() {
		return this.id;
	}
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	public java.lang.String getUuid() {
		return this.uuid;
	}
	public void setIslandOrderUuid(java.lang.String value) {
		this.islandOrderUuid = value;
	}
	public java.lang.String getIslandOrderUuid() {
		return this.islandOrderUuid;
	}
	public void setIslandTravelerUuid(java.lang.String value) {
		this.islandTravelerUuid = value;
	}
	public java.lang.String getIslandTravelerUuid() {
		return this.islandTravelerUuid;
	}
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	public java.lang.String getCountry() {
		return this.country;
	}
	public void setVisaTypeId(java.lang.Integer value) {
		this.visaTypeId = value;
	}
	public java.lang.Integer getVisaTypeId() {
		return this.visaTypeId;
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

