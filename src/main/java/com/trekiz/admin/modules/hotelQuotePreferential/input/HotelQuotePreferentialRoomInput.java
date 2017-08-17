/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelQuotePreferentialRoomInput  extends BaseInput {
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
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelQuotePreferentialUuid;
	//"酒店UUID（当前价单的酒店或者关联的批发商下的酒店）"
	private java.lang.String hotelUuid;
	//"酒店房型UUID"
	private java.lang.String hotelRoomUuid;
	//"晚数"
	private java.lang.Integer nights;
	//"酒店餐型uuids（数据源是酒店房型餐型关联表读取多个用；分隔）"
	private java.lang.String hotelMealUuids;
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
	private HotelQuotePreferentialRoom dataObj ;
	
	public HotelQuotePreferentialRoomInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelQuotePreferentialRoomInput(HotelQuotePreferentialRoom obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelQuotePreferentialRoom getHotelQuotePreferentialRoom() {
		dataObj = new HotelQuotePreferentialRoom();
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
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
	}
	public void setHotelQuotePreferentialUuid(java.lang.String value) {
		this.hotelQuotePreferentialUuid = value;
	}
	public java.lang.String getHotelQuotePreferentialUuid() {
		return this.hotelQuotePreferentialUuid;
	}
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	public void setHotelRoomUuid(java.lang.String value) {
		this.hotelRoomUuid = value;
	}
	public java.lang.String getHotelRoomUuid() {
		return this.hotelRoomUuid;
	}
	public void setNights(java.lang.Integer value) {
		this.nights = value;
	}
	public java.lang.Integer getNights() {
		return this.nights;
	}
	public void setHotelMealUuids(java.lang.String value) {
		this.hotelMealUuids = value;
	}
	public java.lang.String getHotelMealUuids() {
		return this.hotelMealUuids;
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

