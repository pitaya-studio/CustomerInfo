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

public class HotelQuoteQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"分公司（部门）不与数据表中字段只是查询条件"
	private java.lang.String wholesalerId;
	//"销售人员ID "
	private java.lang.Integer userId;
	//"报价类型（1、直客；2、同行；3、其他）"
	private java.lang.Integer quoteType;
	//"报价对象（报价类型quote_type等于2时对应渠道ID，等于3时对应客户姓名）"
	private java.lang.String quoteObject;
	//"联系人名称 "
	private java.lang.String linkName;
	//"联系电话"
	private java.lang.String linkPhone;
	//"币种id"
	private java.lang.Integer currencyId;
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
	//开始日期
	private java.lang.String fromDate;
	//结束日期
	private java.lang.String endDate;
	//岛屿名称uuid
	private java.lang.String islandUuid;
	//酒店名称uuid
	private java.lang.String hotel;
	//酒店集团uuid
	private java.lang.String hotelGroupUuid;
	//排序字段
	private java.lang.String orderBy;
	
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
	public void setWholesalerId(java.lang.String value) {
		this.wholesalerId = value;
	}
	public java.lang.String getWholesalerId() {
		return this.wholesalerId;
	}
	public void setUserId(java.lang.Integer value) {
		this.userId = value;
	}
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setQuoteType(java.lang.Integer value) {
		this.quoteType = value;
	}
	public java.lang.Integer getQuoteType() {
		return this.quoteType;
	}
	public void setQuoteObject(java.lang.String value) {
		this.quoteObject = value;
	}
	public java.lang.String getQuoteObject() {
		return this.quoteObject;
	}
	public void setLinkName(java.lang.String value) {
		this.linkName = value;
	}
	public java.lang.String getLinkName() {
		return this.linkName;
	}
	public void setLinkPhone(java.lang.String value) {
		this.linkPhone = value;
	}
	public java.lang.String getLinkPhone() {
		return this.linkPhone;
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
	public java.lang.String getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.lang.String fromDate) {
		this.fromDate = fromDate;
	}
	public java.lang.String getEndDate() {
		return endDate;
	}
	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}
	public java.lang.String getIslandUuid() {
		return islandUuid;
	}
	public void setIslandUuid(java.lang.String islandUuid) {
		this.islandUuid = islandUuid;
	}
	public java.lang.String getHotelGroupUuid() {
		return hotelGroupUuid;
	}
	public void setHotelGroupUuid(java.lang.String hotelGroupUuid) {
		this.hotelGroupUuid = hotelGroupUuid;
	}
	public java.lang.String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(java.lang.String orderBy) {
		this.orderBy = orderBy;
	}
	public java.lang.String getHotel() {
		return hotel;
	}
	public void setHotel(java.lang.String hotel) {
		this.hotel = hotel;
	}
	
}

