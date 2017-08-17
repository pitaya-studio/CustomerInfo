package com.trekiz.admin.modules.hotelPl.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;

public class HotelPlTaxInput extends BaseInput {
	private static final long serialVersionUID = 6707581287624380399L;

	//状态（1、新增，2、修改）
	private Integer status;
	
	//酒店价单uuid
	private String hotelPlUuid;
	private HotelPl dataObj ;

	//酒店价单税金价格信息
	//uuid
	private String[] hotelPlTaxPrice_uuid;
	//税费类型(1、政府税；2、服务费；3、床税；4、其他)
	private String[] hotelPlTaxPrice_taxType;
	//税费名称（1、政府税；2、服务费；3、床税；4、其他税费时会手动输入税费名称）显示时使用
	private String[] hotelPlTaxPrice_taxName;
	//起始日期
	private String[] hotelPlTaxPrice_startDate;
	//结束日期
	private String[] hotelPlTaxPrice_endDate;
	//币种id（为了后期支持多币种所有的金额都有对应的币种ID）
	private String[] hotelPlTaxPrice_currencyId;
	//金额(1：百分比收税、2：固定税收金额)
	private String[] hotelPlTaxPrice_amount;
	//收费类型（1、%；2、￥）
	private String[] hotelPlTaxPrice_chargeType;
	//酒店价单税金价格信息
	
	
	//酒店价单税金例外信息
	//uuid
	private String[] hotelPlTaxException_uuid;
	//例外类型(1、房型；2、餐型；3、交通)
	private String[] hotelPlTaxException_exceptionType;
	//例外类型名称(1、房型；2、餐型；3、交通)显示时使用
	private String[] hotelPlTaxException_exceptionName;
	//起始日期
	private String[] hotelPlTaxException_startDate;
	//结束日期
	private String[] hotelPlTaxException_endDate;
	//税费类型(1、政府税；2、服务费；3、床税；4、其他)
	private String[] hotelPlTaxException_taxType;
	//游客类型uuid多个用“;”分隔
	private String[] hotelPlTaxException_travelType;
	//酒店价单税金例外信息

	/** 新增状态 */
	public static final int STATUS_ADD = 1;
	/** 修改状态 */
	public static final int STATUS_UPDATE = 2;
	
	
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	
	public HotelPlTaxInput(){
	}
	
	//表单提交的bean转换成数据库映射的bean
	public HotelPl getHotelPl() throws Exception {
		dataObj = new HotelPl();
		dataObj.setUuid(this.getHotelPlUuid());
		
		//酒店税金价格
		dataObj.setHotelPlTaxPrices(super.transfer2Object(HotelPlTaxPrice.class, this));
		//酒店税金例外
		dataObj.setHotelPlTaxExceptions(super.transfer2Object(HotelPlTaxException.class, this));
		
		return dataObj;
	}
	
	public List<HotelPlTaxPrice> getHotelPlTaxPrices() {
		return null;
	}
	
	public List<HotelPlTaxException> getHotelPlTaxExceptions() {
		return null;
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
	public String[] getHotelPlTaxPrice_uuid() {
		return hotelPlTaxPrice_uuid;
	}
	public void setHotelPlTaxPrice_uuid(String[] hotelPlTaxPrice_uuid) {
		this.hotelPlTaxPrice_uuid = hotelPlTaxPrice_uuid;
	}
	public String[] getHotelPlTaxPrice_taxType() {
		return hotelPlTaxPrice_taxType;
	}
	public void setHotelPlTaxPrice_taxType(String[] hotelPlTaxPrice_taxType) {
		this.hotelPlTaxPrice_taxType = hotelPlTaxPrice_taxType;
	}
	public String[] getHotelPlTaxPrice_taxName() {
		return hotelPlTaxPrice_taxName;
	}
	public void setHotelPlTaxPrice_taxName(String[] hotelPlTaxPrice_taxName) {
		this.hotelPlTaxPrice_taxName = hotelPlTaxPrice_taxName;
	}
	public String[] getHotelPlTaxPrice_startDate() {
		return hotelPlTaxPrice_startDate;
	}
	public void setHotelPlTaxPrice_startDate(String[] hotelPlTaxPrice_startDate) {
		this.hotelPlTaxPrice_startDate = hotelPlTaxPrice_startDate;
	}
	public String[] getHotelPlTaxPrice_endDate() {
		return hotelPlTaxPrice_endDate;
	}
	public void setHotelPlTaxPrice_endDate(String[] hotelPlTaxPrice_endDate) {
		this.hotelPlTaxPrice_endDate = hotelPlTaxPrice_endDate;
	}
	public String[] getHotelPlTaxPrice_currencyId() {
		return hotelPlTaxPrice_currencyId;
	}
	public void setHotelPlTaxPrice_currencyId(String[] hotelPlTaxPrice_currencyId) {
		this.hotelPlTaxPrice_currencyId = hotelPlTaxPrice_currencyId;
	}
	public String[] getHotelPlTaxPrice_amount() {
		return hotelPlTaxPrice_amount;
	}
	public void setHotelPlTaxPrice_amount(String[] hotelPlTaxPrice_amount) {
		this.hotelPlTaxPrice_amount = hotelPlTaxPrice_amount;
	}
	public String[] getHotelPlTaxPrice_chargeType() {
		return hotelPlTaxPrice_chargeType;
	}
	public void setHotelPlTaxPrice_chargeType(String[] hotelPlTaxPrice_chargeType) {
		this.hotelPlTaxPrice_chargeType = hotelPlTaxPrice_chargeType;
	}
	public String[] getHotelPlTaxException_uuid() {
		return hotelPlTaxException_uuid;
	}
	public void setHotelPlTaxException_uuid(String[] hotelPlTaxException_uuid) {
		this.hotelPlTaxException_uuid = hotelPlTaxException_uuid;
	}
	public String[] getHotelPlTaxException_exceptionType() {
		return hotelPlTaxException_exceptionType;
	}
	public void setHotelPlTaxException_exceptionType(
			String[] hotelPlTaxException_exceptionType) {
		this.hotelPlTaxException_exceptionType = hotelPlTaxException_exceptionType;
	}
	public String[] getHotelPlTaxException_exceptionName() {
		return hotelPlTaxException_exceptionName;
	}
	public void setHotelPlTaxException_exceptionName(
			String[] hotelPlTaxException_exceptionName) {
		this.hotelPlTaxException_exceptionName = hotelPlTaxException_exceptionName;
	}
	public String[] getHotelPlTaxException_startDate() {
		return hotelPlTaxException_startDate;
	}
	public void setHotelPlTaxException_startDate(
			String[] hotelPlTaxException_startDate) {
		this.hotelPlTaxException_startDate = hotelPlTaxException_startDate;
	}
	public String[] getHotelPlTaxException_endDate() {
		return hotelPlTaxException_endDate;
	}
	public void setHotelPlTaxException_endDate(
			String[] hotelPlTaxException_endDate) {
		this.hotelPlTaxException_endDate = hotelPlTaxException_endDate;
	}
	public String[] getHotelPlTaxException_taxType() {
		return hotelPlTaxException_taxType;
	}
	public void setHotelPlTaxException_taxType(
			String[] hotelPlTaxException_taxType) {
		this.hotelPlTaxException_taxType = hotelPlTaxException_taxType;
	}
	public String[] getHotelPlTaxException_travelType() {
		return hotelPlTaxException_travelType;
	}
	public void setHotelPlTaxException_travelType(
			String[] hotelPlTaxException_travelType) {
		this.hotelPlTaxException_travelType = hotelPlTaxException_travelType;
	}
	
	

}
