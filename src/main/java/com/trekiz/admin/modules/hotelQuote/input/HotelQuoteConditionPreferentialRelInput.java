/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteConditionPreferentialRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelQuoteConditionPreferentialRelInput  extends BaseInput {
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
	//"报价表UUID"
	private java.lang.String hotelQuoteUuid;
	//"报价条件表UUID"
	private java.lang.String hotelQuoteConditionUuid;
	//"关联原酒店优惠信息的UUID"
	private java.lang.String hotelPlPreferentialUuid;
	//"关联新报价优惠信息UUID"
	private java.lang.String hotelQuotePreferentialUuid;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//columns END
	private HotelQuoteConditionPreferentialRel dataObj ;
	
	public HotelQuoteConditionPreferentialRelInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelQuoteConditionPreferentialRelInput(HotelQuoteConditionPreferentialRel obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelQuoteConditionPreferentialRel getHotelQuoteConditionPreferentialRel() {
		dataObj = new HotelQuoteConditionPreferentialRel();
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
	public void setHotelQuoteUuid(java.lang.String value) {
		this.hotelQuoteUuid = value;
	}
	public java.lang.String getHotelQuoteUuid() {
		return this.hotelQuoteUuid;
	}
	public void setHotelQuoteConditionUuid(java.lang.String value) {
		this.hotelQuoteConditionUuid = value;
	}
	public java.lang.String getHotelQuoteConditionUuid() {
		return this.hotelQuoteConditionUuid;
	}
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	public void setHotelQuotePreferentialUuid(java.lang.String value) {
		this.hotelQuotePreferentialUuid = value;
	}
	public java.lang.String getHotelQuotePreferentialUuid() {
		return this.hotelQuotePreferentialUuid;
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

