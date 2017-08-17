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

public class ActivityHotelGroupQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"酒店产品uuid"
	private java.lang.String activityHotelUuid;
	//"团期编号"
	private java.lang.String groupCode;
	//"出团日期"
	private java.util.Date groupOpenDate;
	//"结束时期（俄风行隐藏）"
	private java.util.Date groupEndDate;
	//"上岛方式（字典，多个用“；”分隔）"
	private java.lang.String islandWay;
	//"单房差"
	private Double singlePrice;
	//"单房差币种"
	private java.lang.Integer currencyId;
	//"单房差单位（系统常量：1人2间3晚）"
	private java.lang.Integer singlePriceUnit;
	//"控房间数(选择关联控房单中的库存数量)"
	private java.lang.Integer controlNum;
	//"非控房间数"
	private java.lang.Integer uncontrolNum;
	//"余位数"
	private java.lang.Integer remNumber;
	//"参考航班（文本输入，不和航班有关联）"
	private java.lang.String airline;
	//"优先扣减（系统常量：1控票数2非控票数）"
	private java.lang.Integer priorityDeduction;
	//"定金"
	private Double frontMoney;
	//"定金币种"
	private java.lang.Integer frontMoneyCurrencyId;
	//"团期备注"
	private java.lang.String memo;
	//"1：上架；2：下架；3：草稿；4：已删除"
	private java.lang.String status;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
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
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}
	public void setGroupCode(java.lang.String value) {
		this.groupCode = value;
	}
	public java.lang.String getGroupCode() {
		return this.groupCode;
	}
	public void setGroupOpenDate(java.util.Date value) {
		this.groupOpenDate = value;
	}
	public java.util.Date getGroupOpenDate() {
		return this.groupOpenDate;
	}
	public void setGroupEndDate(java.util.Date value) {
		this.groupEndDate = value;
	}
	public java.util.Date getGroupEndDate() {
		return this.groupEndDate;
	}
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	public void setSinglePrice(Double value) {
		this.singlePrice = value;
	}
	public Double getSinglePrice() {
		return this.singlePrice;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setSinglePriceUnit(java.lang.Integer value) {
		this.singlePriceUnit = value;
	}
	public java.lang.Integer getSinglePriceUnit() {
		return this.singlePriceUnit;
	}
	public void setControlNum(java.lang.Integer value) {
		this.controlNum = value;
	}
	public java.lang.Integer getControlNum() {
		return this.controlNum;
	}
	public void setUncontrolNum(java.lang.Integer value) {
		this.uncontrolNum = value;
	}
	public java.lang.Integer getUncontrolNum() {
		return this.uncontrolNum;
	}
	public void setRemNumber(java.lang.Integer value) {
		this.remNumber = value;
	}
	public java.lang.Integer getRemNumber() {
		return this.remNumber;
	}
	public void setAirline(java.lang.String value) {
		this.airline = value;
	}
	public java.lang.String getAirline() {
		return this.airline;
	}
	public void setPriorityDeduction(java.lang.Integer value) {
		this.priorityDeduction = value;
	}
	public java.lang.Integer getPriorityDeduction() {
		return this.priorityDeduction;
	}
	public void setFrontMoney(Double value) {
		this.frontMoney = value;
	}
	public Double getFrontMoney() {
		return this.frontMoney;
	}
	public void setFrontMoneyCurrencyId(java.lang.Integer value) {
		this.frontMoneyCurrencyId = value;
	}
	public java.lang.Integer getFrontMoneyCurrencyId() {
		return this.frontMoneyCurrencyId;
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

