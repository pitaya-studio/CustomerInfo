/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.input;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class CruiseshipStockOrderInput  extends BaseInput {
	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 1L;
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"产品表ID"
	private java.lang.Integer activityId;
	//"产品团期表类型（1：activitygroup表；）"
	private java.lang.Integer activityType;
	//"产品名称"
	private java.lang.String activityName;
	//"产品出发地ID"
	private java.lang.Integer departureCityId;
	//"产品出发地名称"
	private java.lang.String departureCityName;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"库存明细UUID"
	private java.lang.String cruiseshipStockDetailUuid;
	//"舱位名称"
	private java.lang.String cruiseshipCabinName;
	//"性别（女：F，男：M）"
	private java.lang.String sex;
	//"女人数"
	private java.lang.Integer fnum;
	//"男人数"
	private java.lang.Integer mnum;
	//"女拼（拼：0；不拼：1；）"
	private java.lang.Integer fpiece;
	//"男拼（拼：0；不拼：1；）"
	private java.lang.Integer mpiece;
	//"总人数"
	private java.lang.Integer allNum;
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
	private CruiseshipStockOrder dataObj ;
	
	public CruiseshipStockOrderInput(){
	}
	//数据库映射bean转换成表单提交bean
	public CruiseshipStockOrderInput(CruiseshipStockOrder obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public CruiseshipStockOrder getCruiseshipStockOrder() {
		dataObj = new CruiseshipStockOrder();
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
	public void setActivityType(java.lang.Integer value) {
		this.activityType = value;
	}
	public java.lang.Integer getActivityType() {
		return this.activityType;
	}
	public void setActivityName(java.lang.String value) {
		this.activityName = value;
	}
	public java.lang.String getActivityName() {
		return this.activityName;
	}
	public void setDepartureCityId(java.lang.Integer value) {
		this.departureCityId = value;
	}
	public java.lang.Integer getDepartureCityId() {
		return this.departureCityId;
	}
	public void setDepartureCityName(java.lang.String value) {
		this.departureCityName = value;
	}
	public java.lang.String getDepartureCityName() {
		return this.departureCityName;
	}
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
	}
	public void setCruiseshipStockDetailUuid(java.lang.String value) {
		this.cruiseshipStockDetailUuid = value;
	}
	public java.lang.String getCruiseshipStockDetailUuid() {
		return this.cruiseshipStockDetailUuid;
	}
	public void setCruiseshipCabinName(java.lang.String value) {
		this.cruiseshipCabinName = value;
	}
	public java.lang.String getCruiseshipCabinName() {
		return this.cruiseshipCabinName;
	}
	public void setSex(java.lang.String value) {
		this.sex = value;
	}
	public java.lang.String getSex() {
		return this.sex;
	}
	public void setFnum(java.lang.Integer value) {
		this.fnum = value;
	}
	public java.lang.Integer getFnum() {
		return this.fnum;
	}
	public void setMnum(java.lang.Integer value) {
		this.mnum = value;
	}
	public java.lang.Integer getMnum() {
		return this.mnum;
	}
	public void setFpiece(java.lang.Integer value) {
		this.fpiece = value;
	}
	public java.lang.Integer getFpiece() {
		return this.fpiece;
	}
	public void setMpiece(java.lang.Integer value) {
		this.mpiece = value;
	}
	public java.lang.Integer getMpiece() {
		return this.mpiece;
	}
	public void setAllNum(java.lang.Integer value) {
		this.allNum = value;
	}
	public java.lang.Integer getAllNum() {
		return this.allNum;
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

