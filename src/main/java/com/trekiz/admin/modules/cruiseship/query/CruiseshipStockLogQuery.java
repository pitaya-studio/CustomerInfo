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

public class CruiseshipStockLogQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"游轮UUID"
	private java.lang.String cruiseshipInfoUuid;
	//"船期"
	private java.util.Date shipDate;
	//"舱型UUID"
	private java.lang.String cruiseshipCabinUuid;
	//"修改前库存"
	private java.lang.Integer stockAmount;
	//"修改前余位"
	private java.lang.Integer freePosition;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"操作源。1：库存；2：余位；"
	private java.lang.Integer operateSource;
	//"操作类型。1：增加；2：减少；"
	private java.lang.Integer operateType;
	//"操作数量。"
	private java.lang.Integer operateNum;
	//"修改后库存"
	private java.lang.Integer stockAmountAfter;
	//"修改后余位"
	private java.lang.Integer freePositionAfter;
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
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
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
	public void setCruiseshipCabinUuid(java.lang.String value) {
		this.cruiseshipCabinUuid = value;
	}
	public java.lang.String getCruiseshipCabinUuid() {
		return this.cruiseshipCabinUuid;
	}
	public void setStockAmount(java.lang.Integer value) {
		this.stockAmount = value;
	}
	public java.lang.Integer getStockAmount() {
		return this.stockAmount;
	}
	public void setFreePosition(java.lang.Integer value) {
		this.freePosition = value;
	}
	public java.lang.Integer getFreePosition() {
		return this.freePosition;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	public void setOperateSource(java.lang.Integer value) {
		this.operateSource = value;
	}
	public java.lang.Integer getOperateSource() {
		return this.operateSource;
	}
	public void setOperateType(java.lang.Integer value) {
		this.operateType = value;
	}
	public java.lang.Integer getOperateType() {
		return this.operateType;
	}
	public void setOperateNum(java.lang.Integer value) {
		this.operateNum = value;
	}
	public java.lang.Integer getOperateNum() {
		return this.operateNum;
	}
	public void setStockAmountAfter(java.lang.Integer value) {
		this.stockAmountAfter = value;
	}
	public java.lang.Integer getStockAmountAfter() {
		return this.stockAmountAfter;
	}
	public void setFreePositionAfter(java.lang.Integer value) {
		this.freePositionAfter = value;
	}
	public java.lang.Integer getFreePositionAfter() {
		return this.freePositionAfter;
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

