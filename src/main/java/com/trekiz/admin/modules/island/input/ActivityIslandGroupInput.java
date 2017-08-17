/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityIslandGroupInput  extends BaseInput {

	private static final long serialVersionUID = 1L;
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"团期编号"
	private java.lang.String groupCode;
	//"出团日期"
	private java.util.Date groupOpenDate;
	//"结束时期"
	private java.util.Date groupEndDate;
	//"上岛方式（字典，多个用“；”分隔）"
	private java.lang.String islandWay;
	//"预收人数"
	private java.lang.Integer advNumber;
	//"余位数"
	private java.lang.Integer remNumber;
	//"单房差"
	private Double singlePrice;
	//"单房差币种"
	private java.lang.Integer currencyId;
	//"1：上架；2：下架；3：草稿；4：已删除"
	private java.lang.String status;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期 "
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//"frontMoney"
	private Double frontMoney;
	//"frontMoneyCurrencyId"
	private java.lang.Integer frontMoneyCurrencyId;
	//columns END
	private ActivityIslandGroup dataObj ;
	
	public ActivityIslandGroupInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityIslandGroupInput(ActivityIslandGroup obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityIslandGroup getActivityIslandGroup() {
		dataObj = new ActivityIslandGroup();
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
	public void setGroupCode(java.lang.String value) {
		this.groupCode = value;
	}
	public java.lang.String getGroupCode() {
		return this.groupCode;
	}
	public void setGroupOpenDate(java.util.Date value) {
		this.groupOpenDate = value;
	}
	public java.util.Date getGroupOpenDate() {
		return this.groupOpenDate;
	}
	public void setGroupEndDate(java.util.Date value) {
		this.groupEndDate = value;
	}
	public java.util.Date getGroupEndDate() {
		return this.groupEndDate;
	}
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	public void setAdvNumber(java.lang.Integer value) {
		this.advNumber = value;
	}
	public java.lang.Integer getAdvNumber() {
		return this.advNumber;
	}
	public void setRemNumber(java.lang.Integer value) {
		this.remNumber = value;
	}
	public java.lang.Integer getRemNumber() {
		return this.remNumber;
	}
	public void setSinglePrice(Double value) {
		this.singlePrice = value;
	}
	public Double getSinglePrice() {
		return this.singlePrice;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	public java.lang.String getStatus() {
		return this.status;
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
	public void setFrontMoney(Double value) {
		this.frontMoney = value;
	}
	public Double getFrontMoney() {
		return this.frontMoney;
	}
	public void setFrontMoneyCurrencyId(java.lang.Integer value) {
		this.frontMoneyCurrencyId = value;
	}
	public java.lang.Integer getFrontMoneyCurrencyId() {
		return this.frontMoneyCurrencyId;
	}


	
}

