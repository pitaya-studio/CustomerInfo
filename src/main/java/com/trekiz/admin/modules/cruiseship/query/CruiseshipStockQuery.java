/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class CruiseshipStockQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"游轮UUID"
	private java.lang.String cruiseshipInfoUuid;
	//"船期"
	private java.util.Date shipDate;
	//"批发商id"
	private java.lang.Integer wholesalerId;
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
	public void setCruiseshipInfoUuid(java.lang.String value) {
		this.cruiseshipInfoUuid = value;
	}
	public java.lang.String getCruiseshipInfoUuid() {
		return this.cruiseshipInfoUuid;
	}
	public void setShipDate(java.util.Date value) {
		this.shipDate = value;
	}
	public java.util.Date getShipDate() {
		return this.shipDate;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
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


	
}
