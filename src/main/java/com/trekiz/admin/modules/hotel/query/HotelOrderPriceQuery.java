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

public class HotelOrderPriceQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"订单uuid"
	private java.lang.String orderUuid;
	//"价格类型：1 团期价格类型；2 返佣类型；3 优惠类型 4 其他类型"
	private java.lang.Integer priceType;
	//"金额名称"
	private java.lang.String priceName;
	//"酒店产品团期价格表UUID"
	private java.lang.String activityHotelGroupPriceUuid;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double price;
	//"个数"
	private java.lang.Integer num;
	//"备注"
	private java.lang.String remark;
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
	public void setOrderUuid(java.lang.String value) {
		this.orderUuid = value;
	}
	public java.lang.String getOrderUuid() {
		return this.orderUuid;
	}
	public void setPriceType(java.lang.Integer value) {
		this.priceType = value;
	}
	public java.lang.Integer getPriceType() {
		return this.priceType;
	}
	public void setPriceName(java.lang.String value) {
		this.priceName = value;
	}
	public java.lang.String getPriceName() {
		return this.priceName;
	}
	public void setActivityHotelGroupPriceUuid(java.lang.String value) {
		this.activityHotelGroupPriceUuid = value;
	}
	public java.lang.String getActivityHotelGroupPriceUuid() {
		return this.activityHotelGroupPriceUuid;
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
	public void setNum(java.lang.Integer value) {
		this.num = value;
	}
	public java.lang.Integer getNum() {
		return this.num;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
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

