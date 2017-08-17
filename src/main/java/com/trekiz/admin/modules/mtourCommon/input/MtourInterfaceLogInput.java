/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.mtourCommon.entity.MtourInterfaceLog;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class MtourInterfaceLogInput  extends BaseInput {
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
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
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//columns END
	private MtourInterfaceLog dataObj ;
	
	public MtourInterfaceLogInput(){
	}
	//数据库映射bean转换成表单提交bean
	public MtourInterfaceLogInput(MtourInterfaceLog obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public MtourInterfaceLog getMtourInterfaceLog() {
		dataObj = new MtourInterfaceLog();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 36) {
			return false;
		}
		if(this.getRemoteAddr().length() > 255) {
			return false;
		}
		if(this.getUserAgent().length() > 255) {
			return false;
		}
		if(this.getRequestUri().length() > 255) {
			return false;
		}
		if(this.getMethod().length() > 5) {
			return false;
		}
		if(this.getParams().length() > 65535) {
			return false;
		}
		if(this.getResponse().length() > 65535) {
			return false;
		}
		if(this.getException().length() > 65535) {
			return false;
		}
		if(this.getDelFlag().length() > 1) {
			return false;
		}
		return true;
	}
	
	
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	public java.lang.Long getId() {
		return this.id;
	}
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	public java.lang.String getUuid() {
		return this.uuid;
	}
	public void setRemoteAddr(java.lang.String value) {
		this.remoteAddr = value;
	}
	public java.lang.String getRemoteAddr() {
		return this.remoteAddr;
	}
	public void setUserAgent(java.lang.String value) {
		this.userAgent = value;
	}
	public java.lang.String getUserAgent() {
		return this.userAgent;
	}
	public void setRequestUri(java.lang.String value) {
		this.requestUri = value;
	}
	public java.lang.String getRequestUri() {
		return this.requestUri;
	}
	public void setMethod(java.lang.String value) {
		this.method = value;
	}
	public java.lang.String getMethod() {
		return this.method;
	}
	public void setParams(java.lang.String value) {
		this.params = value;
	}
	public java.lang.String getParams() {
		return this.params;
	}
	public void setResponse(java.lang.String value) {
		this.response = value;
	}
	public java.lang.String getResponse() {
		return this.response;
	}
	public void setException(java.lang.String value) {
		this.exception = value;
	}
	public java.lang.String getException() {
		return this.exception;
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

