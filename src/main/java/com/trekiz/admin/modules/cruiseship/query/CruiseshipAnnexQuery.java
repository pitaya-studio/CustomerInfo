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

public class CruiseshipAnnexQuery  {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"主表ID"
	private java.lang.String mainUuid;
	//"附件表ID"
	private java.lang.Integer docId;
	//"类型。1:游轮；2：游轮库存；"
	private java.lang.Integer type;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"文件的名称"
	private java.lang.String docName;
	//"存放文件的路径"
	private java.lang.String docPath;
	//"上传的文件类型"
	private java.lang.Integer docType;
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
	public void setMainUuid(java.lang.String value) {
		this.mainUuid = value;
	}
	public java.lang.String getMainUuid() {
		return this.mainUuid;
	}
	public void setDocId(java.lang.Integer value) {
		this.docId = value;
	}
	public java.lang.Integer getDocId() {
		return this.docId;
	}
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	public java.lang.Integer getType() {
		return this.type;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	public void setDocName(java.lang.String value) {
		this.docName = value;
	}
	public java.lang.String getDocName() {
		return this.docName;
	}
	public void setDocPath(java.lang.String value) {
		this.docPath = value;
	}
	public java.lang.String getDocPath() {
		return this.docPath;
	}
	public void setDocType(java.lang.Integer value) {
		this.docType = value;
	}
	public java.lang.Integer getDocType() {
		return this.docType;
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

