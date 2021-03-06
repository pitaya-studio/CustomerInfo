/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.pay.entity.PayDraft;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PayDraftInput  extends BaseInput {
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
	//"UUID"
	private java.lang.String uuid;
	//"出票人名称"
	private java.lang.String drawerName;
	//"出票人帐号"
	private java.lang.String drawerAccount;
	//"付款行全称"
	private java.lang.String payBankName;
	//"汇票到期日"
	private java.util.Date draftAccountedDate;
	//"收款单位"
	private java.lang.String receiveCompanyName;
	//"收款行名称"
	private java.lang.String receiveBankName;
	//"收款账户"
	private java.lang.String receiveAccount;
	//"创建者"
	private java.lang.Integer createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新者"
	private java.lang.Integer updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标记"
	private java.lang.String delFlag;
	//columns END
	private PayDraft dataObj ;
	
	public PayDraftInput(){
	}
	//数据库映射bean转换成表单提交bean
	public PayDraftInput(PayDraft obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public PayDraft getPayDraft() {
		dataObj = new PayDraft();
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
	public void setDrawerName(java.lang.String value) {
		this.drawerName = value;
	}
	public java.lang.String getDrawerName() {
		return this.drawerName;
	}
	public void setDrawerAccount(java.lang.String value) {
		this.drawerAccount = value;
	}
	public java.lang.String getDrawerAccount() {
		return this.drawerAccount;
	}
	public void setPayBankName(java.lang.String value) {
		this.payBankName = value;
	}
	public java.lang.String getPayBankName() {
		return this.payBankName;
	}
	public void setDraftAccountedDate(java.util.Date value) {
		this.draftAccountedDate = value;
	}
	public java.util.Date getDraftAccountedDate() {
		return this.draftAccountedDate;
	}
	public void setReceiveCompanyName(java.lang.String value) {
		this.receiveCompanyName = value;
	}
	public java.lang.String getReceiveCompanyName() {
		return this.receiveCompanyName;
	}
	public void setReceiveBankName(java.lang.String value) {
		this.receiveBankName = value;
	}
	public java.lang.String getReceiveBankName() {
		return this.receiveBankName;
	}
	public void setReceiveAccount(java.lang.String value) {
		this.receiveAccount = value;
	}
	public java.lang.String getReceiveAccount() {
		return this.receiveAccount;
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

