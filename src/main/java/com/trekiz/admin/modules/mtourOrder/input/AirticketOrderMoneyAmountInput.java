/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class AirticketOrderMoneyAmountInput  extends BaseInput {
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
	//"流水号UUID"
	private java.lang.String serialNum;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double amount;
	//"汇率"
	private Double exchangerate;
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"款项类型（借款：1，退款：2，追加成本：3）"
	private java.lang.Integer moneyType;
	//"款项名称"
	private java.lang.String fundsName;
	//"备注"
	private java.lang.String memo;
	//"状态(默认是1，撤消是0)"
	private java.lang.Integer status;
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
	private AirticketOrderMoneyAmount dataObj ;
	
	public AirticketOrderMoneyAmountInput(){
	}
	//数据库映射bean转换成表单提交bean
	public AirticketOrderMoneyAmountInput(AirticketOrderMoneyAmount obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public AirticketOrderMoneyAmount getAirticketOrderMoneyAmount() {
		dataObj = new AirticketOrderMoneyAmount();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 50) {
			return false;
		}
		if(this.getSerialNum().length() > 48) {
			return false;
		}
		if(this.getFundsName().length() > 100) {
			return false;
		}
		if(this.getMemo().length() > 65535) {
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
	public void setSerialNum(java.lang.String value) {
		this.serialNum = value;
	}
	public java.lang.String getSerialNum() {
		return this.serialNum;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setAmount(Double value) {
		this.amount = value;
	}
	public Double getAmount() {
		return this.amount;
	}
	public void setExchangerate(Double value) {
		this.exchangerate = value;
	}
	public Double getExchangerate() {
		return this.exchangerate;
	}
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	public void setMoneyType(java.lang.Integer value) {
		this.moneyType = value;
	}
	public java.lang.Integer getMoneyType() {
		return this.moneyType;
	}
	public void setFundsName(java.lang.String value) {
		this.fundsName = value;
	}
	public java.lang.String getFundsName() {
		return this.fundsName;
	}
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	public java.lang.String getMemo() {
		return this.memo;
	}
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	public java.lang.Integer getStatus() {
		return this.status;
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

