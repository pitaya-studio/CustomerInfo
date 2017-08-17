/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "cruiseship_annex")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CruiseshipAnnex   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "CruiseshipAnnex";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_MAIN_UUID = "主表ID";
	public static final String ALIAS_DOC_ID = "附件表ID";
	public static final String ALIAS_TYPE = "类型。1:游轮；2：游轮库存；";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_DOC_NAME = "文件的名称";
	public static final String ALIAS_DOC_PATH = "存放文件的路径";
	public static final String ALIAS_DOC_TYPE = "上传的文件类型";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
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
	//columns END
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public CruiseshipAnnex(){
	}

	public CruiseshipAnnex(
		java.lang.Integer id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setMainUuid(java.lang.String value) {
		this.mainUuid = value;
	}
	@Column(name="main_uuid")
	public java.lang.String getMainUuid() {
		return this.mainUuid;
	}
	
		
	public void setDocId(java.lang.Integer value) {
		this.docId = value;
	}
	@Column(name="doc_id")
	public java.lang.Integer getDocId() {
		return this.docId;
	}
	
		
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.Integer getType() {
		return this.type;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
		
	public void setDocName(java.lang.String value) {
		this.docName = value;
	}
	@Column(name="doc_name")
	public java.lang.String getDocName() {
		return this.docName;
	}
	
		
	public void setDocPath(java.lang.String value) {
		this.docPath = value;
	}
	@Column(name="doc_path")
	public java.lang.String getDocPath() {
		return this.docPath;
	}
	
		
	public void setDocType(java.lang.Integer value) {
		this.docType = value;
	}
	@Column(name="doc_type")
	public java.lang.Integer getDocType() {
		return this.docType;
	}
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	@Transient	
	public String getCreateDateString() {
		if(getCreateDate() != null) {
			return this.date2String(getCreateDate(), FORMAT_CREATE_DATE);
		} else {
			return null;
		}
	}
	public void setCreateDateString(String value) {
		setCreateDate(this.string2Date(value, FORMAT_CREATE_DATE));
	}
	
		
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	@Column(name="createDate")
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	@Transient	
	public String getUpdateDateString() {
		if(getUpdateDate() != null) {
			return this.date2String(getUpdateDate(), FORMAT_UPDATE_DATE);
		} else {
			return null;
		}
	}
	public void setUpdateDateString(String value) {
		setUpdateDate(this.string2Date(value, FORMAT_UPDATE_DATE));
	}
	
		
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	@Column(name="updateDate")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

