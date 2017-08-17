/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class HotelQuoteConditionQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"报价表UUID"
	private java.lang.String hotelQuoteUuid;
	//"位置（1、境内；2、境外）"
	private java.lang.Integer position;
	//"国家"
	private java.lang.String country;
	//"岛屿"
	private java.lang.String islandUuid;
	//"酒店"
	private java.lang.String hotelUuid;
	//"地接供应商"
	private java.lang.Integer supplierInfoId;
	//"采购类型"
	private java.lang.Integer purchaseType;
	//"去程交通"
	private java.lang.String departureIslandWay;
	//"去程交通"
	private java.lang.String arrivalIslandWay;
	//"房间数  "
	private java.lang.Integer roomNum;
	//"混住次数"
	private java.lang.Integer mixliveNum;
	//"排序（报价的次数）"
	private java.lang.Integer sort;
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
	public void setHotelQuoteUuid(java.lang.String value) {
		this.hotelQuoteUuid = value;
	}
	public java.lang.String getHotelQuoteUuid() {
		return this.hotelQuoteUuid;
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
	public void setSupplierInfoId(java.lang.Integer value) {
		this.supplierInfoId = value;
	}
	public java.lang.Integer getSupplierInfoId() {
		return this.supplierInfoId;
	}
	public void setPurchaseType(java.lang.Integer value) {
		this.purchaseType = value;
	}
	public java.lang.Integer getPurchaseType() {
		return this.purchaseType;
	}
	public void setDepartureIslandWay(java.lang.String value) {
		this.departureIslandWay = value;
	}
	public java.lang.String getDepartureIslandWay() {
		return this.departureIslandWay;
	}
	public void setArrivalIslandWay(java.lang.String value) {
		this.arrivalIslandWay = value;
	}
	public java.lang.String getArrivalIslandWay() {
		return this.arrivalIslandWay;
	}
	public void setRoomNum(java.lang.Integer value) {
		this.roomNum = value;
	}
	public java.lang.Integer getRoomNum() {
		return this.roomNum;
	}
	public void setMixliveNum(java.lang.Integer value) {
		this.mixliveNum = value;
	}
	public java.lang.Integer getMixliveNum() {
		return this.mixliveNum;
	}
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	public java.lang.Integer getSort() {
		return this.sort;
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

