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
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class AirticketactivityreserveTempInput  extends BaseInput {
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
	//"uuid"
	private java.lang.String uuid;
	//"机票产品信息表ID外键"
	private java.lang.Integer activityId;
	//"渠道商基本信息表id"
	private java.lang.Integer agentId;
	//"0,定金占位；1,全款占位"
	private Integer reserveType;
	//"切位人数"
	private java.lang.Integer payReservePosition;
	//"售出切位人数"
	private java.lang.Integer soldPayPosition;
	//"定金金额"
	private java.lang.Integer frontMoney;
	//"剩余的切位人数"
	private java.lang.Integer leftpayReservePosition;
	//"剩余的定金金额"
	private java.lang.Integer leftFontMoney;
	//"预订人"
	private java.lang.String reservation;
	//"支付方式"
	private java.lang.Integer payType;
	//"切位备注"
	private java.lang.String remark;
	//"还位备注"
	private java.lang.String returnRemark;
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
	private AirticketactivityreserveTemp dataObj ;
	
	public AirticketactivityreserveTempInput(){
	}
	//数据库映射bean转换成表单提交bean
	public AirticketactivityreserveTempInput(AirticketactivityreserveTemp obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public AirticketactivityreserveTemp getAirticketactivityreserveTemp() {
		dataObj = new AirticketactivityreserveTemp();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 32) {
			return false;
		}
		if(this.getReservation().length() > 10) {
			return false;
		}
		if(this.getRemark().length() > 200) {
			return false;
		}
		if(this.getReturnRemark().length() > 200) {
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
	public void setAgentId(java.lang.Integer value) {
		this.agentId = value;
	}
	public java.lang.Integer getAgentId() {
		return this.agentId;
	}
	public void setReserveType(Integer value) {
		this.reserveType = value;
	}
	public Integer getReserveType() {
		return this.reserveType;
	}
	public void setPayReservePosition(java.lang.Integer value) {
		this.payReservePosition = value;
	}
	public java.lang.Integer getPayReservePosition() {
		return this.payReservePosition;
	}
	public void setSoldPayPosition(java.lang.Integer value) {
		this.soldPayPosition = value;
	}
	public java.lang.Integer getSoldPayPosition() {
		return this.soldPayPosition;
	}
	public void setFrontMoney(java.lang.Integer value) {
		this.frontMoney = value;
	}
	public java.lang.Integer getFrontMoney() {
		return this.frontMoney;
	}
	public void setLeftpayReservePosition(java.lang.Integer value) {
		this.leftpayReservePosition = value;
	}
	public java.lang.Integer getLeftpayReservePosition() {
		return this.leftpayReservePosition;
	}
	public void setLeftFontMoney(java.lang.Integer value) {
		this.leftFontMoney = value;
	}
	public java.lang.Integer getLeftFontMoney() {
		return this.leftFontMoney;
	}
	public void setReservation(java.lang.String value) {
		this.reservation = value;
	}
	public java.lang.String getReservation() {
		return this.reservation;
	}
	public void setPayType(java.lang.Integer value) {
		this.payType = value;
	}
	public java.lang.Integer getPayType() {
		return this.payType;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setReturnRemark(java.lang.String value) {
		this.returnRemark = value;
	}
	public java.lang.String getReturnRemark() {
		return this.returnRemark;
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

