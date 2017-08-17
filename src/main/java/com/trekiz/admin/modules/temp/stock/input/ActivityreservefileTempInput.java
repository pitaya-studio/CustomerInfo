/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityreservefileTempInput  extends BaseInput {
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
	private java.lang.Integer id;
	//"产品信息表ID外键"
	private java.lang.Integer srcActivityId;
	//"产品团期表ID"
	private java.lang.Integer activityGroupId;
	//"渠道商基本信息表id"
	private java.lang.Integer agentId;
	//"附件表id"
	private java.lang.Integer srcDocId;
	//"文件名称"
	private java.lang.String fileName;
	//"创建日期"
	private java.util.Date createDate;
	//"创建人"
	private java.lang.Integer createBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"更新人"
	private java.lang.Integer updateBy;
	//"删除标志"
	private java.lang.String delFlag;
	//"reserveOrderId"
	private java.lang.Integer reserveOrderId;
	//columns END
	private ActivityreservefileTemp dataObj ;
	
	public ActivityreservefileTempInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityreservefileTempInput(ActivityreservefileTemp obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityreservefileTemp getActivityreservefileTemp() {
		dataObj = new ActivityreservefileTemp();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getFileName().length() > 50) {
			return false;
		}
		if(this.getDelFlag().length() > 1) {
			return false;
		}
		return true;
	}
	
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setSrcActivityId(java.lang.Integer value) {
		this.srcActivityId = value;
	}
	public java.lang.Integer getSrcActivityId() {
		return this.srcActivityId;
	}
	public void setActivityGroupId(java.lang.Integer value) {
		this.activityGroupId = value;
	}
	public java.lang.Integer getActivityGroupId() {
		return this.activityGroupId;
	}
	public void setAgentId(java.lang.Integer value) {
		this.agentId = value;
	}
	public java.lang.Integer getAgentId() {
		return this.agentId;
	}
	public void setSrcDocId(java.lang.Integer value) {
		this.srcDocId = value;
	}
	public java.lang.Integer getSrcDocId() {
		return this.srcDocId;
	}
	public void setFileName(java.lang.String value) {
		this.fileName = value;
	}
	public java.lang.String getFileName() {
		return this.fileName;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	public void setReserveOrderId(java.lang.Integer value) {
		this.reserveOrderId = value;
	}
	public java.lang.Integer getReserveOrderId() {
		return this.reserveOrderId;
	}


	
}

