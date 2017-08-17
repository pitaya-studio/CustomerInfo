/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_quote_preferential_tax")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuotePreferentialTax   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuotePreferentialTax";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_QUOTE_PREFERENTIAL_UUID = "酒店报价优惠信息uuid";
	public static final String ALIAS_TYPE = "税费类型（1、房费；2、餐费；3、交通费）";
	public static final String ALIAS_TRAVELER_TYPE_UUID = "游客类型uuid";
	public static final String ALIAS_PREFERENTIAL_TYPE = "优惠方式（1、合计；2、打折；3、减金额；4、减最低）";
	public static final String ALIAS_CURRENCY_ID = "币种id（为了后期支持多币种所有的金额都有对应的币种ID）";
	public static final String ALIAS_PREFERENTIAL_AMOUNT = "优惠金额";
	public static final String ALIAS_CHARGE_TYPE = "收费类型（1、%；2、￥）";
	public static final String ALIAS_ISTAX = "加税种类(1、政府税；2、服务费；3、床税；4、其他)多个用“;”分隔";
	public static final String ALIAS_HOTEL_MEAL_UUIDS = "餐型uuids（数据源是酒店房型餐型关联表读取多个用；分隔）";
	public static final String ALIAS_ISLAND_WAY_UUIDS = "交通方式uuids（数据源是海岛游上岛方式表读取，多个用；分隔）";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店报价优惠信息uuid"
	private java.lang.String hotelQuotePreferentialUuid;
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
	private java.lang.String istax;
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
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public HotelQuotePreferentialTax(){
	}

	public HotelQuotePreferentialTax(
		java.lang.Integer id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	@Column(name="hotel_pl_uuid")
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
	}
	
		
	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}
	@Column(name="island_uuid")
	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}
	
		
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	@Column(name="hotel_uuid")
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	
		
	public void setHotelQuotePreferentialUuid(java.lang.String value) {
		this.hotelQuotePreferentialUuid = value;
	}
	@Column(name="hotel_quote_preferential_uuid")
	public java.lang.String getHotelQuotePreferentialUuid() {
		return this.hotelQuotePreferentialUuid;
	}
	
		
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.Integer getType() {
		return this.type;
	}
	
		
	public void setTravelerTypeUuid(java.lang.String value) {
		this.travelerTypeUuid = value;
	}
	@Column(name="traveler_type_uuid")
	public java.lang.String getTravelerTypeUuid() {
		return this.travelerTypeUuid;
	}
	
		
	public void setPreferentialType(java.lang.Integer value) {
		this.preferentialType = value;
	}
	@Column(name="preferential_type")
	public java.lang.Integer getPreferentialType() {
		return this.preferentialType;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setPreferentialAmount(Double value) {
		this.preferentialAmount = value;
	}
	@Column(name="preferential_amount")
	public Double getPreferentialAmount() {
		return this.preferentialAmount;
	}
	
		
	public void setChargeType(java.lang.Integer value) {
		this.chargeType = value;
	}
	@Column(name="charge_type")
	public java.lang.Integer getChargeType() {
		return this.chargeType;
	}
	
		
	public void setIstax(java.lang.String value) {
		this.istax = value;
	}
	@Column(name="istax")
	public java.lang.String getIstax() {
		return this.istax;
	}
	
		
	public void setHotelMealUuids(java.lang.String value) {
		this.hotelMealUuids = value;
	}
	@Column(name="hotel_meal_uuids")
	public java.lang.String getHotelMealUuids() {
		return this.hotelMealUuids;
	}
	
		
	public void setIslandWayUuids(java.lang.String value) {
		this.islandWayUuids = value;
	}
	@Column(name="island_way_uuids")
	public java.lang.String getIslandWayUuids() {
		return this.islandWayUuids;
	}
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	@Transient	
	public String getCreateDateString() {
		if(getCreateDate() != null) {
			return this.date2String(getCreateDate(), FORMAT_CREATE_DATE);
		} else {
			return null;
		}
	}
	public void setCreateDateString(String value) {
		setCreateDate(this.string2Date(value, FORMAT_CREATE_DATE));
	}
	
		
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	@Column(name="createDate")
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	@Transient	
	public String getUpdateDateString() {
		if(getUpdateDate() != null) {
			return this.date2String(getUpdateDate(), FORMAT_UPDATE_DATE);
		} else {
			return null;
		}
	}
	public void setUpdateDateString(String value) {
		setUpdateDate(this.string2Date(value, FORMAT_UPDATE_DATE));
	}
	
		
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	@Column(name="updateDate")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

