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

public class ActivityHotelShareQuery  {
	
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activityHotelUuid;
	private java.lang.Long shareUser;
	private java.lang.Long acceptShareUser;
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
	public void setShareUser(java.lang.Long value) {
		this.shareUser = value;
	}
	public java.lang.Long getShareUser() {
		return this.shareUser;
	}
	public void setAcceptShareUser(java.lang.Long value) {
		this.acceptShareUser = value;
	}
	public java.lang.Long getAcceptShareUser() {
		return this.acceptShareUser;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

