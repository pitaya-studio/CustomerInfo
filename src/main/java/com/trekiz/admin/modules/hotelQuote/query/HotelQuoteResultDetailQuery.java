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

public class HotelQuoteResultDetailQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"报价表UUID"
	private java.lang.String hotelQuoteUuid;
	//"报价结果表UUID"
	private java.lang.String hotelQuoteResultUuid;
	//"费用"
	private java.lang.Integer price;
	//"合计的价格类型（1、各项游客类型的费用；2、第三人费用；）"
	private java.lang.Integer priceType;
	//"关联的类型UUID（price_type为1时此字段存游客类型的UUID）"
	private java.lang.String typeUuid;
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
	public void setHotelQuoteResultUuid(java.lang.String value) {
		this.hotelQuoteResultUuid = value;
	}
	public java.lang.String getHotelQuoteResultUuid() {
		return this.hotelQuoteResultUuid;
	}
	public void setPrice(java.lang.Integer value) {
		this.price = value;
	}
	public java.lang.Integer getPrice() {
		return this.price;
	}
	public void setPriceType(java.lang.Integer value) {
		this.priceType = value;
	}
	public java.lang.Integer getPriceType() {
		return this.priceType;
	}
	public void setTypeUuid(java.lang.String value) {
		this.typeUuid = value;
	}
	public java.lang.String getTypeUuid() {
		return this.typeUuid;
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

