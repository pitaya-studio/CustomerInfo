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
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityIslandVisaFileInput  extends BaseInput {
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
	//"国家"
	private java.lang.String country;
	//"签证类型(字典表type:new_visa_type)"
	private java.lang.Integer visaTypeId;
	//"创建人"
	private java.lang.Integer createBy;
	//"更新人"
	private java.lang.Integer updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"创建时间"
	private java.util.Date createDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//columns END
	private ActivityIslandVisaFile dataObj ;
	
	public ActivityIslandVisaFileInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityIslandVisaFileInput(ActivityIslandVisaFile obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityIslandVisaFile getActivityIslandVisaFile() {
		dataObj = new ActivityIslandVisaFile();
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
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
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
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

