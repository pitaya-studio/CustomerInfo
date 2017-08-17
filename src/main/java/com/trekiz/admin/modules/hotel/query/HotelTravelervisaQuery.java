/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class HotelTravelervisaQuery  {
	
	//columns START
	//"id"
	private java.lang.Long id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店订单UUID"
	private java.lang.String hotelOrderUuid;
	//"酒店订单游客UUID"
	private java.lang.String hotelTravelerUuid;
	//"申请国家UUID"
	private java.lang.String country;
	//"签证类型(字典表type:new_visa_type)"
	private java.lang.Integer visaTypeId;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除状态"
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
	public void setHotelOrderUuid(java.lang.String value) {
		this.hotelOrderUuid = value;
	}
	public java.lang.String getHotelOrderUuid() {
		return this.hotelOrderUuid;
	}
	public void setHotelTravelerUuid(java.lang.String value) {
		this.hotelTravelerUuid = value;
	}
	public java.lang.String getHotelTravelerUuid() {
		return this.hotelTravelerUuid;
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

