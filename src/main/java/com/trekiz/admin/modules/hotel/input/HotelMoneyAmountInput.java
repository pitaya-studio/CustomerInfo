/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelMoneyAmountInput  extends BaseInput {
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
	private java.lang.Long id;
	//"uuid"
	private java.lang.String uuid;
	//"币种ID"
	private Integer currencyId;
	//"金额"
	private Double amount;
	//"汇率"
	private Double exchangerate;
	//"订单UUID或游客UUID"
	private java.lang.String businessUuid;
	//"款项类型"
	private Integer moneyType;
	//"业务类型(1表示订单，2表示游客,3 表示询价报价4表示团期)"
	private Integer businessType;
	//"审核review表主键id"
	private java.lang.Integer reviewId;
	//"流水号UUID"
	private java.lang.String serialNum;
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
	private HotelMoneyAmount dataObj ;
	
	public HotelMoneyAmountInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelMoneyAmountInput(HotelMoneyAmount obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelMoneyAmount getHotelMoneyAmount() {
		dataObj = new HotelMoneyAmount();
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
	public void setCurrencyId(Integer value) {
		this.currencyId = value;
	}
	public Integer getCurrencyId() {
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
	public void setBusinessUuid(java.lang.String value) {
		this.businessUuid = value;
	}
	public java.lang.String getBusinessUuid() {
		return this.businessUuid;
	}
	public void setMoneyType(Integer value) {
		this.moneyType = value;
	}
	public Integer getMoneyType() {
		return this.moneyType;
	}
	public void setBusinessType(Integer value) {
		this.businessType = value;
	}
	public Integer getBusinessType() {
		return this.businessType;
	}
	public void setReviewId(java.lang.Integer value) {
		this.reviewId = value;
	}
	public java.lang.Integer getReviewId() {
		return this.reviewId;
	}
	public void setSerialNum(java.lang.String value) {
		this.serialNum = value;
	}
	public java.lang.String getSerialNum() {
		return this.serialNum;
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

