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

public class HotelGuestTypeRelationQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店住客类型uuid"
	private java.lang.String hotelGuestTypeUuid;
	//"住客类型名称"
	private java.lang.String hotelGuestTypeName;
	//"酒店uuid"
	private java.lang.String hotelUuid;
	//"酒店房型uuid"
	private java.lang.String hotelRoomUuid;
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
	public void setHotelGuestTypeUuid(java.lang.String value) {
		this.hotelGuestTypeUuid = value;
	}
	public java.lang.String getHotelGuestTypeUuid() {
		return this.hotelGuestTypeUuid;
	}
	public void setHotelGuestTypeName(java.lang.String value) {
		this.hotelGuestTypeName = value;
	}
	public java.lang.String getHotelGuestTypeName() {
		return this.hotelGuestTypeName;
	}
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	public void setHotelRoomUuid(java.lang.String value) {
		this.hotelRoomUuid = value;
	}
	public java.lang.String getHotelRoomUuid() {
		return this.hotelRoomUuid;
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

