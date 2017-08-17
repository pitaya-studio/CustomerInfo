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
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class AirticketOrderPnrAirlinePriceInput  extends BaseInput {
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
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"Pnr表UUID"
	private java.lang.String airticketOrderPnrUuid;
	//"航段表UUID"
	private java.lang.String airticketOrderPnrAirlineUuid;
	//"价格类型（0：成本价，1：外报价）"
	private java.lang.Integer priceType;
	//"币种"
	private java.lang.Integer currencyId;
	//"人数"
	private java.lang.Integer personNum;
	//"价格"
	private Double price;
	//"定金"
	private Double frontMoney;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//columns END
	private AirticketOrderPnrAirlinePrice dataObj ;
	
	public AirticketOrderPnrAirlinePriceInput(){
	}
	//数据库映射bean转换成表单提交bean
	public AirticketOrderPnrAirlinePriceInput(AirticketOrderPnrAirlinePrice obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public AirticketOrderPnrAirlinePrice getAirticketOrderPnrAirlinePrice() {
		dataObj = new AirticketOrderPnrAirlinePrice();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 32) {
			return false;
		}
		if(this.getAirticketOrderPnrUuid().length() > 32) {
			return false;
		}
		if(this.getAirticketOrderPnrAirlineUuid().length() > 32) {
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
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	public void setAirticketOrderPnrUuid(java.lang.String value) {
		this.airticketOrderPnrUuid = value;
	}
	public java.lang.String getAirticketOrderPnrUuid() {
		return this.airticketOrderPnrUuid;
	}
	public void setAirticketOrderPnrAirlineUuid(java.lang.String value) {
		this.airticketOrderPnrAirlineUuid = value;
	}
	public java.lang.String getAirticketOrderPnrAirlineUuid() {
		return this.airticketOrderPnrAirlineUuid;
	}
	public void setPriceType(java.lang.Integer value) {
		this.priceType = value;
	}
	public java.lang.Integer getPriceType() {
		return this.priceType;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setPersonNum(java.lang.Integer value) {
		this.personNum = value;
	}
	public java.lang.Integer getPersonNum() {
		return this.personNum;
	}
	public void setPrice(Double value) {
		this.price = value;
	}
	public Double getPrice() {
		return this.price;
	}
	public void setFrontMoney(Double value) {
		this.frontMoney = value;
	}
	public Double getFrontMoney() {
		return this.frontMoney;
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

