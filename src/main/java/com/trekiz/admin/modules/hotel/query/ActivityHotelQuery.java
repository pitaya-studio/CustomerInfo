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

public class ActivityHotelQuery  {
	
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activitySerNum;
	private java.lang.String activityName;
	private java.lang.String country;
	private java.lang.String islandUuid;
	private java.lang.String hotelUuid;
	private java.lang.Integer currencyId;
	private java.lang.String memo;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer deptId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	private String island;//岛屿名称
	private String islandway;//上岛方式
	private String hotel;//酒店名称
	private String roomtype;//房型
	private String hotelstar;//酒店星级
	private String foodtype;//餐型
	private Integer startPrice;//价格
	private Integer endPrice;//价格
	private String startGroupDate;//团期
	private String endGroupDate;//团期
	private String status;//团期状态
	private String groupCode;//团期编号
	
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
	public Integer getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(Integer startPrice) {
		this.startPrice = startPrice;
	}
	public Integer getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(Integer endPrice) {
		this.endPrice = endPrice;
	}
	public String getStartGroupDate() {
		return startGroupDate;
	}
	public void setStartGroupDate(String startGroupDate) {
		this.startGroupDate = startGroupDate;
	}
	public String getEndGroupDate() {
		return endGroupDate;
	}
	public void setEndGroupDate(String endGroupDate) {
		this.endGroupDate = endGroupDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
}

