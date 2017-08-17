/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.query;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class ActivityIslandQuery   extends BaseEntity {
	
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activitySerNum;//产品编号
	private java.lang.String country;
	private java.lang.String memo;
	private java.lang.String status;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer deptId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	private java.lang.String groupCode;//团号
	private java.lang.String activityName;//产品名称
	private java.lang.String islandUuid;//岛屿
	private String island;//岛屿名称
	private String islandway;//上岛方式
	private String hotel;//酒店名称
	private String roomtype;//房型
	private String hotelstar;//酒店星级
	private String foodtype;//餐型
	private String airline;//航空公司
	private String flightnum;//航班号
	private String spacelevel;//舱位级别
	
	private java.lang.String hotelUuid;//酒店
	private java.lang.Integer startPrice;//价格
	private java.lang.Integer endPrice;//价格
	private java.lang.Integer currencyId;//币种ID
	private java.lang.String startGroupDate;//团期
	private java.lang.String endGroupDate;//团期
	
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
	public void setActivitySerNum(java.lang.String value) {
		this.activitySerNum = value;
	}
	public java.lang.String getActivitySerNum() {
		return this.activitySerNum;
	}
	public void setActivityName(java.lang.String value) {
		this.activityName = value;
	}
	public java.lang.String getActivityName() {
		return this.activityName;
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
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	public java.lang.String getMemo() {
		return this.memo;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	public java.lang.String getStatus() {
		return this.status;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	public void setDeptId(java.lang.Integer value) {
		this.deptId = value;
	}
	public java.lang.Integer getDeptId() {
		return this.deptId;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public java.lang.String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(java.lang.String groupCode) {
		this.groupCode = groupCode;
	}
	public java.lang.Integer getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(java.lang.Integer startPrice) {
		this.startPrice = startPrice;
	}
	public java.lang.Integer getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(java.lang.Integer endPrice) {
		this.endPrice = endPrice;
	}
	public java.lang.String getStartGroupDate() {
		return startGroupDate;
	}
	public void setStartGroupDate(java.lang.String startGroupDate) {
		this.startGroupDate = startGroupDate;
	}
	public java.lang.String getEndGroupDate() {
		return endGroupDate;
	}
	public void setEndGroupDate(java.lang.String endGroupDate) {
		this.endGroupDate = endGroupDate;
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
	public String getIsland() {
		return island;
	}
	public void setIsland(String island) {
		this.island = island;
	}
	public String getIslandway() {
		return islandway;
	}
	public void setIslandway(String islandway) {
		this.islandway = islandway;
	}
	public String getHotel() {
		return hotel;
	}
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getHotelstar() {
		return hotelstar;
	}
	public void setHotelstar(String hotelstar) {
		this.hotelstar = hotelstar;
	}
	public String getFoodtype() {
		return foodtype;
	}
	public void setFoodtype(String foodtype) {
		this.foodtype = foodtype;
	}
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getFlightnum() {
		return flightnum;
	}
	public void setFlightnum(String flightnum) {
		this.flightnum = flightnum;
	}
	public String getSpacelevel() {
		return spacelevel;
	}
	public void setSpacelevel(String spacelevel) {
		this.spacelevel = spacelevel;
	}
	
}

