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

public class AirticketOrderOperateRecordQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"修改目标类型（0:修改PNR组）"
	private java.lang.Integer targetType;
	//"目标UUID或ID"
	private java.lang.String targetUuid;
	//"修改内容（按指定模板拼接成字符串储存）;美途人数修改模板“大编号KB5555、KB5656的总XXX(出票或预订)人数由XXX修改为XXX”"
	private java.lang.String content;
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
	public void setTargetType(java.lang.Integer value) {
		this.targetType = value;
	}
	public java.lang.Integer getTargetType() {
		return this.targetType;
	}
	public void setTargetUuid(java.lang.String value) {
		this.targetUuid = value;
	}
	public java.lang.String getTargetUuid() {
		return this.targetUuid;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
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

