/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.input;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class CruiseshipInfoInput  extends BaseInput {
	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 1L;
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"游轮名称"
	private java.lang.String name;
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
	//columns END
	private CruiseshipInfo dataObj ;
	
	public CruiseshipInfoInput(){
	}
	//数据库映射bean转换成表单提交bean
	public CruiseshipInfoInput(CruiseshipInfo obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public CruiseshipInfo getCruiseshipInfo() {
		dataObj = new CruiseshipInfo();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
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
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
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

