/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.input;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityIslandGroupPriceInput {
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"海岛游产品团期uuid"
	private java.lang.String activityIslandGroupUuid;
	//"价格类型"
	private java.lang.String type;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double price;
	//"排序"
	private java.lang.Integer sort;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//columns END
	private ActivityIslandGroupPrice dataObj ;
	
	public ActivityIslandGroupPriceInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityIslandGroupPriceInput(ActivityIslandGroupPrice obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityIslandGroupPrice getActivityIslandGroupPrice() {
		dataObj = new ActivityIslandGroupPrice();
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
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	public void setActivityIslandGroupUuid(java.lang.String value) {
		this.activityIslandGroupUuid = value;
	}
	public java.lang.String getActivityIslandGroupUuid() {
		return this.activityIslandGroupUuid;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setPrice(Double value) {
		this.price = value;
	}
	public Double getPrice() {
		return this.price;
	}
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	public java.lang.Integer getSort() {
		return this.sort;
	}
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	public java.lang.Long getCreateBy() {
		return this.createBy;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	public java.lang.Long getUpdateBy() {
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

