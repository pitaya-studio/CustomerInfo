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
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class IslandTravelervisaInput  extends BaseInput {
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"id"
	private java.lang.Long id;
	//"uuid"
	private java.lang.String uuid;
	//"海岛游订单UUID"
	private java.lang.String islandOrderUuid;
	//"海岛游订单游客UUID"
	private java.lang.String islandTravelerUuid;
	//"申请国家UUID"
	private java.lang.String country;
	//"签证类型(字典表type:new_visa_type)"
	private java.lang.Integer visaTypeId;
	//"createBy"
	private java.lang.Long createBy;
	//"createDate"
	private java.util.Date createDate;
	//"updateBy"
	private java.lang.Long updateBy;
	//"updateDate"
	private java.util.Date updateDate;
	//"delFlag"
	private java.lang.String delFlag;
	//columns END
	private IslandTravelervisa dataObj ;
	
	public IslandTravelervisaInput(){
	}
	//数据库映射bean转换成表单提交bean
	public IslandTravelervisaInput(IslandTravelervisa obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public IslandTravelervisa getIslandTravelervisa() {
		dataObj = new IslandTravelervisa();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
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
	public void setIslandOrderUuid(java.lang.String value) {
		this.islandOrderUuid = value;
	}
	public java.lang.String getIslandOrderUuid() {
		return this.islandOrderUuid;
	}
	public void setIslandTravelerUuid(java.lang.String value) {
		this.islandTravelerUuid = value;
	}
	public java.lang.String getIslandTravelerUuid() {
		return this.islandTravelerUuid;
	}
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	public java.lang.String getCountry() {
		return this.country;
	}
	public void setVisaTypeId(java.lang.Integer value) {
		this.visaTypeId = value;
	}
	public java.lang.Integer getVisaTypeId() {
		return this.visaTypeId;
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

