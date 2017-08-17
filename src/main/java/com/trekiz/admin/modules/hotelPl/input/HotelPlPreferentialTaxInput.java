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
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class HotelPlPreferentialTaxInput  extends BaseInput {
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
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"税费类型（1、房费；2、餐费；3、交通费）"
	private java.lang.Integer type;
	//"游客类型uuid"
	private java.lang.String travelerTypeUuid;
	//"优惠方式（1、合计；2、打折；3、减金额；4、减最低）"
	private java.lang.Integer preferentialType;
	//"币种id（为了后期支持多币种所有的金额都有对应的币种ID）"
	private java.lang.Integer currencyId;
	//"优惠金额"
	private Double preferentialAmount;
	//"收费类型（1、%；2、￥）"
	private java.lang.Integer chargeType;
	//"加税种类(1、政府税；2、服务费；3、床税；4、其他)多个用“;”分隔"
	private java.lang.Integer istax;
	//"餐型uuids（数据源是酒店房型餐型关联表读取多个用；分隔）"
	private java.lang.String hotelMealUuids;
	//"交通方式uuids（数据源是海岛游上岛方式表读取，多个用；分隔）"
	private java.lang.String islandWayUuids;
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
	private HotelPlPreferentialTax dataObj ;
	
	public HotelPlPreferentialTaxInput(){
	}
	//数据库映射bean转换成表单提交bean
	public HotelPlPreferentialTaxInput(HotelPlPreferentialTax obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public HotelPlPreferentialTax getHotelPlPreferentialTax() {
		dataObj = new HotelPlPreferentialTax();
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
	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}
	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	public java.lang.Integer getType() {
		return this.type;
	}
	public void setTravelerTypeUuid(java.lang.String value) {
		this.travelerTypeUuid = value;
	}
	public java.lang.String getTravelerTypeUuid() {
		return this.travelerTypeUuid;
	}
	public void setPreferentialType(java.lang.Integer value) {
		this.preferentialType = value;
	}
	public java.lang.Integer getPreferentialType() {
		return this.preferentialType;
	}
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	public void setPreferentialAmount(Double value) {
		this.preferentialAmount = value;
	}
	public Double getPreferentialAmount() {
		return this.preferentialAmount;
	}
	public void setChargeType(java.lang.Integer value) {
		this.chargeType = value;
	}
	public java.lang.Integer getChargeType() {
		return this.chargeType;
	}
	public void setIstax(java.lang.Integer value) {
		this.istax = value;
	}
	public java.lang.Integer getIstax() {
		return this.istax;
	}
	public void setHotelMealUuids(java.lang.String value) {
		this.hotelMealUuids = value;
	}
	public java.lang.String getHotelMealUuids() {
		return this.hotelMealUuids;
	}
	public void setIslandWayUuids(java.lang.String value) {
		this.islandWayUuids = value;
	}
	public java.lang.String getIslandWayUuids() {
		return this.islandWayUuids;
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

