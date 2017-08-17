/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class AirticketOrderPnrQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"大编号类型(0:PNR或者1:地接社)"
	private java.lang.Integer codeType;
	//"大编号输入（PNR或者地接社ID）"
	private java.lang.String flightPnr;
	//"关联航空公司（航空公司信息表sys_airline_info的二字码）"
	private java.lang.String airline;
	//"出票期限"
	private java.util.Date ticketDeadline;
	//"定金期限"
	private java.util.Date frontDeadline;
	//"名单期限"
	private java.util.Date listDeadline;
	//"改名期限"
	private java.util.Date renameDeadline;
	//"机票订单id"
	private java.util.Date cancelDeadline;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
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
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	public void setCodeType(java.lang.Integer value) {
		this.codeType = value;
	}
	public java.lang.Integer getCodeType() {
		return this.codeType;
	}
	public void setFlightPnr(java.lang.String value) {
		this.flightPnr = value;
	}
	public java.lang.String getFlightPnr() {
		return this.flightPnr;
	}
	public void setAirline(java.lang.String value) {
		this.airline = value;
	}
	public java.lang.String getAirline() {
		return this.airline;
	}
	public void setTicketDeadline(java.util.Date value) {
		this.ticketDeadline = value;
	}
	public java.util.Date getTicketDeadline() {
		return this.ticketDeadline;
	}
	public void setFrontDeadline(java.util.Date value) {
		this.frontDeadline = value;
	}
	public java.util.Date getFrontDeadline() {
		return this.frontDeadline;
	}
	public void setListDeadline(java.util.Date value) {
		this.listDeadline = value;
	}
	public java.util.Date getListDeadline() {
		return this.listDeadline;
	}
	public void setRenameDeadline(java.util.Date value) {
		this.renameDeadline = value;
	}
	public java.util.Date getRenameDeadline() {
		return this.renameDeadline;
	}
	public void setCancelDeadline(java.util.Date value) {
		this.cancelDeadline = value;
	}
	public java.util.Date getCancelDeadline() {
		return this.cancelDeadline;
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

