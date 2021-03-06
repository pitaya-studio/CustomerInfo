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

public class HotelPlPreferentialRequireQuery  {
	
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
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"起订晚数"
	private java.lang.Integer bookingNights;
	//"起订间数"
	private java.lang.Integer bookingNumbers;
	//"不适用日期多个日期用“;”分隔"
	private java.lang.String notApplicableDate;
	//"不适用房型uuid多个房型uuid用“;”分隔"
	private java.lang.String notApplicableRoom;
	//"适用第三人（0适用，1不适用）"
	private java.lang.Integer applicableThirdPerson;
	//"是否可以叠加（0可以，1不可以）"
	private java.lang.Integer isSuperposition;
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
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	public void setBookingNights(java.lang.Integer value) {
		this.bookingNights = value;
	}
	public java.lang.Integer getBookingNights() {
		return this.bookingNights;
	}
	public void setBookingNumbers(java.lang.Integer value) {
		this.bookingNumbers = value;
	}
	public java.lang.Integer getBookingNumbers() {
		return this.bookingNumbers;
	}
	public void setNotApplicableDate(java.lang.String value) {
		this.notApplicableDate = value;
	}
	public java.lang.String getNotApplicableDate() {
		return this.notApplicableDate;
	}
	public void setNotApplicableRoom(java.lang.String value) {
		this.notApplicableRoom = value;
	}
	public java.lang.String getNotApplicableRoom() {
		return this.notApplicableRoom;
	}
	public void setApplicableThirdPerson(java.lang.Integer value) {
		this.applicableThirdPerson = value;
	}
	public java.lang.Integer getApplicableThirdPerson() {
		return this.applicableThirdPerson;
	}
	public void setIsSuperposition(java.lang.Integer value) {
		this.isSuperposition = value;
	}
	public java.lang.Integer getIsSuperposition() {
		return this.isSuperposition;
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

