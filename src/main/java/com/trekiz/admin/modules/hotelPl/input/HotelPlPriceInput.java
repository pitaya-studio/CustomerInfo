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
import com.trekiz.admin.modules.hotelPl.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelPlPriceInput  extends BaseInput {
	private static final long serialVersionUID = -4123163584909046266L;
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	
	/** 价单价格新增状态 */
	public final static int STATUS_ADD = 1;
	/** 价单价格修改状态 */
	public final static int STATUS_UPDATE = 2;
	
	//columns START
	//"UUID"
	private String[] hotelPlPrice_uuid;
	//"酒店价单UUID"
	private String[] hotelPlPrice_hotelPlUuid;
	//"岛屿UUID"
	private String[] hotelPlPrice_islandUuid;
	//"酒店UUID"
	private String[] hotelPlPrice_hotelUuid;
	//"房型UUID"
	private String[] hotelPlPrice_hotelRoomUuid;
	//"酒店餐型uuid（数据源是从酒店房型餐型关联表中获得，保存时映射到酒店餐型表中）多个餐型用“;”分隔，便于修改时分组"
	private String[] hotelPlPrice_hotelMealUuids;
	//"起始日期"
	private String[] hotelPlPrice_startDate;
	//"结束日期"
	private String[] hotelPlPrice_endDate;
	//"酒店住客类型uuid"
	private String[] hotelPlPrice_hotelGuestTypeUuid;
	//"币种id（为了后期支持多币种所有的金额都有对应的币种ID）。"
	private String[] hotelPlPrice_currencyId;
	//"金额"
	private String[] hotelPlPrice_amount;
	//"价格类型（0、普通价；1、同行价）"
	private String[] hotelPlPrice_priceType;
	//columns END
	private HotelPl dataObj ;
	
	//自定义属性
	private Integer status;
	private String hotelPlUuid;
	private Integer mixliveCurrencyId;
	private Double mixliveAmount; 
	//自定义属性
	
	public HotelPlPriceInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelPlPriceInput(HotelPlPrice obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelPl getHotelPl() throws Exception {
		dataObj = new HotelPl();
		dataObj.setUuid(this.getHotelPlUuid());
		dataObj.setMixliveCurrencyId(this.getMixliveCurrencyId());
		dataObj.setMixliveAmount(this.getMixliveAmount());
		
		dataObj.setHotelPlPriceList(super.transfer2Object(HotelPlPrice.class, this));
		
		return dataObj;
	}
	public String[] getHotelPlPrice_uuid() {
		return hotelPlPrice_uuid;
	}
	public void setHotelPlPrice_uuid(String[] hotelPlPrice_uuid) {
		this.hotelPlPrice_uuid = hotelPlPrice_uuid;
	}
	public String[] getHotelPlPrice_hotelPlUuid() {
		return hotelPlPrice_hotelPlUuid;
	}
	public void setHotelPlPrice_hotelPlUuid(String[] hotelPlPrice_hotelPlUuid) {
		this.hotelPlPrice_hotelPlUuid = hotelPlPrice_hotelPlUuid;
	}
	public String[] getHotelPlPrice_islandUuid() {
		return hotelPlPrice_islandUuid;
	}
	public void setHotelPlPrice_islandUuid(String[] hotelPlPrice_islandUuid) {
		this.hotelPlPrice_islandUuid = hotelPlPrice_islandUuid;
	}
	public String[] getHotelPlPrice_hotelUuid() {
		return hotelPlPrice_hotelUuid;
	}
	public void setHotelPlPrice_hotelUuid(String[] hotelPlPrice_hotelUuid) {
		this.hotelPlPrice_hotelUuid = hotelPlPrice_hotelUuid;
	}
	public String[] getHotelPlPrice_hotelRoomUuid() {
		return hotelPlPrice_hotelRoomUuid;
	}
	public void setHotelPlPrice_hotelRoomUuid(String[] hotelPlPrice_hotelRoomUuid) {
		this.hotelPlPrice_hotelRoomUuid = hotelPlPrice_hotelRoomUuid;
	}
	public String[] getHotelPlPrice_hotelMealUuids() {
		return hotelPlPrice_hotelMealUuids;
	}
	public void setHotelPlPrice_hotelMealUuids(String[] hotelPlPrice_hotelMealUuids) {
		this.hotelPlPrice_hotelMealUuids = hotelPlPrice_hotelMealUuids;
	}
	public String[] getHotelPlPrice_startDate() {
		return hotelPlPrice_startDate;
	}
	public void setHotelPlPrice_startDate(String[] hotelPlPrice_startDate) {
		this.hotelPlPrice_startDate = hotelPlPrice_startDate;
	}
	public String[] getHotelPlPrice_endDate() {
		return hotelPlPrice_endDate;
	}
	public void setHotelPlPrice_endDate(String[] hotelPlPrice_endDate) {
		this.hotelPlPrice_endDate = hotelPlPrice_endDate;
	}
	public String[] getHotelPlPrice_hotelGuestTypeUuid() {
		return hotelPlPrice_hotelGuestTypeUuid;
	}
	public void setHotelPlPrice_hotelGuestTypeUuid(
			String[] hotelPlPrice_hotelGuestTypeUuid) {
		this.hotelPlPrice_hotelGuestTypeUuid = hotelPlPrice_hotelGuestTypeUuid;
	}
	public String[] getHotelPlPrice_currencyId() {
		return hotelPlPrice_currencyId;
	}
	public void setHotelPlPrice_currencyId(String[] hotelPlPrice_currencyId) {
		this.hotelPlPrice_currencyId = hotelPlPrice_currencyId;
	}
	public String[] getHotelPlPrice_amount() {
		return hotelPlPrice_amount;
	}
	public void setHotelPlPrice_amount(String[] hotelPlPrice_amount) {
		this.hotelPlPrice_amount = hotelPlPrice_amount;
	}
	public String[] getHotelPlPrice_priceType() {
		return hotelPlPrice_priceType;
	}
	public void setHotelPlPrice_priceType(String[] hotelPlPrice_priceType) {
		this.hotelPlPrice_priceType = hotelPlPrice_priceType;
	}
	public HotelPl getDataObj() {
		return dataObj;
	}
	public void setDataObj(HotelPl dataObj) {
		this.dataObj = dataObj;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getHotelPlUuid() {
		return hotelPlUuid;
	}
	public void setHotelPlUuid(String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}
	public Integer getMixliveCurrencyId() {
		return mixliveCurrencyId;
	}
	public void setMixliveCurrencyId(Integer mixliveCurrencyId) {
		this.mixliveCurrencyId = mixliveCurrencyId;
	}
	public Double getMixliveAmount() {
		return mixliveAmount;
	}
	public void setMixliveAmount(Double mixliveAmount) {
		this.mixliveAmount = mixliveAmount;
	}
	
	
}

