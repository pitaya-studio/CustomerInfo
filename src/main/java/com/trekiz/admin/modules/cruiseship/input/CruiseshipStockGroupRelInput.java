/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.input;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class CruiseshipStockGroupRelInput  extends BaseInput {
	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 1L;
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"产品ID"
	private java.lang.Integer activityId;
	//"产品团期表ID"
	private java.lang.Integer activitygroupId;
	//"产品团期表类型（1：activitygroup表；）"
	private java.lang.Integer activityType;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"关联状态（0：已关联；1：未关联）"
	private java.lang.Integer status;
	//"批发商id"
	private java.lang.Integer wholesalerId;
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
	//columns END
	private CruiseshipStockGroupRel dataObj ;
	
	public CruiseshipStockGroupRelInput(){
	}
	//数据库映射bean转换成表单提交bean
	public CruiseshipStockGroupRelInput(CruiseshipStockGroupRel obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public CruiseshipStockGroupRel getCruiseshipStockGroupRel() {
		dataObj = new CruiseshipStockGroupRel();
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
	public void setActivityId(java.lang.Integer value) {
		this.activityId = value;
	}
	public java.lang.Integer getActivityId() {
		return this.activityId;
	}
	public void setActivitygroupId(java.lang.Integer value) {
		this.activitygroupId = value;
	}
	public java.lang.Integer getActivitygroupId() {
		return this.activitygroupId;
	}
	public void setActivityType(java.lang.Integer value) {
		this.activityType = value;
	}
	public java.lang.Integer getActivityType() {
		return this.activityType;
	}
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
	}
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	public java.lang.Integer getStatus() {
		return this.status;
	}
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
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

