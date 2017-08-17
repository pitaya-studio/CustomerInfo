/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.async.parameter.AsyncSysParameterAdapter;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "mtour_interface_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MtourInterfaceLog  extends AsyncSysParameterAdapter {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "MtourInterfaceLog";
	public static final String ALIAS_ID = "编号";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_REMOTE_ADDR = "操作IP地址";
	public static final String ALIAS_USER_AGENT = "用户代理";
	public static final String ALIAS_REQUEST_URI = "请求URI";
	public static final String ALIAS_METHOD = "操作方式";
	public static final String ALIAS_PARAMS = "操作提交的数据";
	public static final String ALIAS_RESPONSE = "操作返回的数据";
	public static final String ALIAS_EXCEPTION = "异常信息";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "创建时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"编号"
	private java.lang.Long id;
	//"uuid"
	private java.lang.String uuid;
	//"操作IP地址"
	private java.lang.String remoteAddr;
	//"用户代理"
	private java.lang.String userAgent;
	//"请求URI"
	private java.lang.String requestUri;
	//"操作方式"
	private java.lang.String method;
	//"操作提交的数据"
	private java.lang.String params;
	//"操作返回的数据"
	private java.lang.String response;
	//"异常信息"
	private java.lang.String exception;
	//"创建人"
	private java.lang.Long createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
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
	public MtourInterfaceLog(){
	}

	public MtourInterfaceLog(
		java.lang.Long id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Long getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setRemoteAddr(java.lang.String value) {
		this.remoteAddr = value;
	}
	@Column(name="remoteAddr")
	public java.lang.String getRemoteAddr() {
		return this.remoteAddr;
	}
	
		
	public void setUserAgent(java.lang.String value) {
		this.userAgent = value;
	}
	@Column(name="userAgent")
	public java.lang.String getUserAgent() {
		return this.userAgent;
	}
	
		
	public void setRequestUri(java.lang.String value) {
		this.requestUri = value;
	}
	@Column(name="requestUri")
	public java.lang.String getRequestUri() {
		return this.requestUri;
	}
	
		
	public void setMethod(java.lang.String value) {
		this.method = value;
	}
	@Column(name="method")
	public java.lang.String getMethod() {
		return this.method;
	}
	
		
	public void setParams(java.lang.String value) {
		this.params = value;
	}
	@Column(name="params")
	public java.lang.String getParams() {
		return this.params;
	}
	
		
	public void setResponse(java.lang.String value) {
		this.response = value;
	}
	@Column(name="response")
	public java.lang.String getResponse() {
		return this.response;
	}
	
		
	public void setException(java.lang.String value) {
		this.exception = value;
	}
	@Column(name="exception")
	public java.lang.String getException() {
		return this.exception;
	}
	
		
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Long getCreateBy() {
		return this.createBy;
	}
		
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	
	@Column(name="createDate")
	@Temporal(TemporalType.TIMESTAMP)
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
		
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	@Column(name="updateDate")
	@Temporal(TemporalType.TIMESTAMP )
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

