/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class HotelQuotePreferentialQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"优惠信息名称"
	private java.lang.String preferentialName;
	//"下单代码"
	private java.lang.String bookingCode;
	//"入住日期"
	private java.util.Date inDate;
	//"离店日期"
	private java.util.Date outDate;
	//"预订起始日期"
	private java.util.Date bookingStartDate;
	//"预订结束日期"
	private java.util.Date bookingEndDate;
	//"交通（上岛方式字典UUID，多个用；分隔）"
	private java.lang.String islandWay;
	//"是否关联酒店（0不关联，1关联。关联酒店的信息存储在hotel_pl_preferential_relHotel表中）"
	private java.lang.Integer isRelation;
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
	public void setPreferentialName(java.lang.String value) {
		this.preferentialName = value;
	}
	public java.lang.String getPreferentialName() {
		return this.preferentialName;
	}
	public void setBookingCode(java.lang.String value) {
		this.bookingCode = value;
	}
	public java.lang.String getBookingCode() {
		return this.bookingCode;
	}
	public void setInDate(java.util.Date value) {
		this.inDate = value;
	}
	public java.util.Date getInDate() {
		return this.inDate;
	}
	public void setOutDate(java.util.Date value) {
		this.outDate = value;
	}
	public java.util.Date getOutDate() {
		return this.outDate;
	}
	public void setBookingStartDate(java.util.Date value) {
		this.bookingStartDate = value;
	}
	public java.util.Date getBookingStartDate() {
		return this.bookingStartDate;
	}
	public void setBookingEndDate(java.util.Date value) {
		this.bookingEndDate = value;
	}
	public java.util.Date getBookingEndDate() {
		return this.bookingEndDate;
	}
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	public void setIsRelation(java.lang.Integer value) {
		this.isRelation = value;
	}
	public java.lang.Integer getIsRelation() {
		return this.isRelation;
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

