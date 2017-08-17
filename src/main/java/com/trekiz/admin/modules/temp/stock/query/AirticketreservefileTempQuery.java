/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class AirticketreservefileTempQuery  {
	
	//columns START
	//"编号"
	private java.lang.Integer id;
	//"机票产品表ID外键"
	private java.lang.Integer airticketActivityId;
	//"渠道商基本信息表id"
	private java.lang.Integer agentId;
	//"附件表id"
	private java.lang.Integer srcDocId;
	//"文件名称"
	private java.lang.String fileName;
	//"创建日期"
	private java.util.Date createDate;
	//"创建人"
	private java.lang.Integer createBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"更新人"
	private java.lang.Integer updateBy;
	//"删除标志"
	private java.lang.String delFlag;
	//"reserveOrderId"
	private java.lang.Integer reserveOrderId;
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setAirticketActivityId(java.lang.Integer value) {
		this.airticketActivityId = value;
	}
	public java.lang.Integer getAirticketActivityId() {
		return this.airticketActivityId;
	}
	public void setAgentId(java.lang.Integer value) {
		this.agentId = value;
	}
	public java.lang.Integer getAgentId() {
		return this.agentId;
	}
	public void setSrcDocId(java.lang.Integer value) {
		this.srcDocId = value;
	}
	public java.lang.Integer getSrcDocId() {
		return this.srcDocId;
	}
	public void setFileName(java.lang.String value) {
		this.fileName = value;
	}
	public java.lang.String getFileName() {
		return this.fileName;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	public void setReserveOrderId(java.lang.Integer value) {
		this.reserveOrderId = value;
	}
	public java.lang.Integer getReserveOrderId() {
		return this.reserveOrderId;
	}


	
}

