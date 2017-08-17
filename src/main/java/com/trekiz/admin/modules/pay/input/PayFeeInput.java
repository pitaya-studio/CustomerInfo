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
import com.trekiz.admin.modules.pay.entity.PayFee;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PayFeeInput  extends BaseInput {
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
	//"uuid"
	private java.lang.String uuid;
	//"支付表id（对应refund的id属性）"
	private java.lang.String refundId;
	//"手续费名称"
	private java.lang.String feeName;
	//"手续费币种"
	private java.lang.Integer feeCurrencyId;
	//"手续费金额"
	private Double feeAmount;
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
	private PayFee dataObj ;
	
	public PayFeeInput(){
	}
	//数据库映射bean转换成表单提交bean
	public PayFeeInput(PayFee obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public PayFee getPayFee() {
		dataObj = new PayFee();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 32) {
			return false;
		}
		if(this.getRefundId().length() > 32) {
			return false;
		}
		if(this.getFeeName().length() > 50) {
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
	public void setRefundId(java.lang.String value) {
		this.refundId = value;
	}
	public java.lang.String getRefundId() {
		return this.refundId;
	}
	public void setFeeName(java.lang.String value) {
		this.feeName = value;
	}
	public java.lang.String getFeeName() {
		return this.feeName;
	}
	public void setFeeCurrencyId(java.lang.Integer value) {
		this.feeCurrencyId = value;
	}
	public java.lang.Integer getFeeCurrencyId() {
		return this.feeCurrencyId;
	}
	public void setFeeAmount(Double value) {
		this.feeAmount = value;
	}
	public Double getFeeAmount() {
		return this.feeAmount;
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

