/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequireNotAppDate;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelPlPreferentialRequireNotAppDateInput  extends BaseInput {
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
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"酒店价单要求UUID"
	private java.lang.String hotelPlPreferentialRequireUuid;
	//"开始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"间隔天数"
	private java.lang.Integer dayNum;
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
	private HotelPlPreferentialRequireNotAppDate dataObj ;
	
	public HotelPlPreferentialRequireNotAppDateInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelPlPreferentialRequireNotAppDateInput(HotelPlPreferentialRequireNotAppDate obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelPlPreferentialRequireNotAppDate getHotelPlPreferentialRequireNotAppDate() {
		dataObj = new HotelPlPreferentialRequireNotAppDate();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 32) {
			return false;
		}
		if(this.getHotelPlUuid().length() > 32) {
			return false;
		}
		if(this.getHotelPlPreferentialUuid().length() > 32) {
			return false;
		}
		if(this.getHotelPlPreferentialRequireUuid().length() > 32) {
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
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
	}
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	public void setHotelPlPreferentialRequireUuid(java.lang.String value) {
		this.hotelPlPreferentialRequireUuid = value;
	}
	public java.lang.String getHotelPlPreferentialRequireUuid() {
		return this.hotelPlPreferentialRequireUuid;
	}
	public void setStartDate(java.util.Date value) {
		this.startDate = value;
	}
	public java.util.Date getStartDate() {
		return this.startDate;
	}
	public void setEndDate(java.util.Date value) {
		this.endDate = value;
	}
	public java.util.Date getEndDate() {
		return this.endDate;
	}
	public void setDayNum(java.lang.Integer value) {
		this.dayNum = value;
	}
	public java.lang.Integer getDayNum() {
		return this.dayNum;
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

