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

public class HotelPlTaxExceptionQuery  {
	
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
	//"例外类型(1、房型；2、餐型；3、交通)"
	private java.lang.Integer exceptionType;
	//"例外类型名称(1、房型；2、餐型；3、交通)显示时使用"
	private java.lang.String exceptionName;
	//"起始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"税费类型(1、政府税；2、服务费；3、床税；4、其他)"
	private java.lang.String taxType;
	//"游客类型uuid多个用“;”分隔"
	private java.lang.String travelType;
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
	public void setExceptionType(java.lang.Integer value) {
		this.exceptionType = value;
	}
	public java.lang.Integer getExceptionType() {
		return this.exceptionType;
	}
	public void setExceptionName(java.lang.String value) {
		this.exceptionName = value;
	}
	public java.lang.String getExceptionName() {
		return this.exceptionName;
	}
	public void setStartDate(java.util.Date value) {
		this.startDate = value;
	}
	public java.util.Date getStartDate() {
		return this.startDate;
	}
	public void setEndDate(java.util.Date value) {
		this.endDate = value;
	}
	public java.util.Date getEndDate() {
		return this.endDate;
	}
	public void setTaxType(java.lang.String value) {
		this.taxType = value;
	}
	public java.lang.String getTaxType() {
		return this.taxType;
	}
	public void setTravelType(java.lang.String value) {
		this.travelType = value;
	}
	public java.lang.String getTravelType() {
		return this.travelType;
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

