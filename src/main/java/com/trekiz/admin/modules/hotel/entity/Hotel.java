/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.entity;

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
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Hotel   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "Hotel";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_NAME_CN = "酒店名称";
	public static final String ALIAS_SHORT_NAME_CN = "中文缩写";
	public static final String ALIAS_NAME_EN = "英文名称";
	public static final String ALIAS_SHORT_NAME_EN = "英文缩写";
	public static final String ALIAS_SPELLING = "全拼";
	public static final String ALIAS_SHORT_SPELLING = "全拼缩写";
	public static final String ALIAS_POSITION = "位置（1：境内，2：境外）";
	public static final String ALIAS_COUNTRY = "国家";
	public static final String ALIAS_PROVINCE = "省";
	public static final String ALIAS_CITY = "市";
	public static final String ALIAS_DISTRICT = "区";
	public static final String ALIAS_REGION = "行政区";
	public static final String ALIAS_SHORT_ADDRESS = "地址后缀";
	public static final String ALIAS_AREA_TYPE = "区域类型（1：内陆，2：海岛）";
	public static final String ALIAS_TYPE = "酒店类型（字典）";
	public static final String ALIAS_BRAND = "酒店品牌";
	public static final String ALIAS_HOTEL_GROUP = "酒店品牌";
	public static final String ALIAS_STAR = "酒店星级（字典）";
	public static final String ALIAS_FLOOR = "层高（字典）";
	public static final String ALIAS_OPENING_DATE = "开业日期";
	public static final String ALIAS_LAST_DECO_DATE = "最后装修日期";
	public static final String ALIAS_TOPIC = "酒店主题（字典）";
	public static final String ALIAS_FACILITIES = "酒店设施（字典）";
	public static final String ALIAS_FEATURE = "酒店特色（字典）";
	public static final String ALIAS_ADDRESS = "地址";
	public static final String ALIAS_WEBSITE = "网址";
	public static final String ALIAS_TELEPHONE = "电话";
	public static final String ALIAS_FAX = "传真";
	public static final String ALIAS_CHARGE_PERSON = "负责人";
	public static final String ALIAS_CHARGE_MOBILE = "手机";
	public static final String ALIAS_CHARGE_TELEPHONE = "电话";
	public static final String ALIAS_CHARGE_FAX = "传真";
	public static final String ALIAS_CHARGE_EMAIL = "邮箱";
	public static final String ALIAS_REMARK = "摘要";
	public static final String ALIAS_DESCRIPTION = "描述";
	public static final String ALIAS_IN_DATE = "入住日期";
	public static final String ALIAS_OUT_DATE = "离店日期";
	public static final String ALIAS_ISLAND_UUID = "岛屿";
	public static final String ALIAS_ISLAND_WAY = "上岛方式（字典）";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_LONGITUDE = "经度";
	public static final String ALIAS_LATITUDE = "纬度";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_OPENING_DATE = "yyyy-MM-dd";
	public static final String FORMAT_LAST_DECO_DATE = "yyyy-MM-dd";
	public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_OUT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String nameCn;
	private java.lang.String shortNameCn;
	private java.lang.String nameEn;
	private java.lang.String shortNameEn;
	private java.lang.String spelling;
	private java.lang.String shortSpelling;
	private java.lang.Integer position;
	private java.lang.String country;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String district;
	private java.lang.String region;
	private java.lang.String shortAddress;
	private java.lang.Integer areaType;
	private java.lang.String type;
	private java.lang.String brand;
	private java.lang.String hotelGroup;
	private java.lang.String star;
	private java.lang.String floor;
	private java.util.Date openingDate;
	private java.util.Date lastDecoDate;
	private java.lang.String topic;
	private java.lang.String facilities;
	private java.lang.String feature;
	private java.lang.String address;
	private java.lang.String website;
	private java.lang.String telephone;
	private java.lang.String fax;
	private java.lang.String chargePerson;
	private java.lang.String chargeMobile;
	private java.lang.String chargeTelephone;
	private java.lang.String chargeFax;
	private java.lang.String chargeEmail;
	private java.lang.String remark;
	private java.lang.String description;
	private java.util.Date inDate;
	private java.util.Date outDate;
	private java.lang.String islandUuid;
	private java.lang.String islandWay;
	private java.lang.Integer wholesalerId;
	private java.lang.String longitude;
	private java.lang.String latitude;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.Integer status;
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
	public Hotel(){
	}

	public Hotel(
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
	
		
	public void setNameCn(java.lang.String value) {
		this.nameCn = value;
	}
	@Column(name="name_cn")
	public java.lang.String getNameCn() {
		return this.nameCn;
	}
	
		
	public void setShortNameCn(java.lang.String value) {
		this.shortNameCn = value;
	}
	@Column(name="short_name_cn")
	public java.lang.String getShortNameCn() {
		return this.shortNameCn;
	}
	
		
	public void setNameEn(java.lang.String value) {
		this.nameEn = value;
	}
	@Column(name="name_en")
	public java.lang.String getNameEn() {
		return this.nameEn;
	}
	
		
	public void setShortNameEn(java.lang.String value) {
		this.shortNameEn = value;
	}
	@Column(name="short_name_en")
	public java.lang.String getShortNameEn() {
		return this.shortNameEn;
	}
	
		
	public void setSpelling(java.lang.String value) {
		this.spelling = value;
	}
	@Column(name="spelling")
	public java.lang.String getSpelling() {
		return this.spelling;
	}
	
		
	public void setShortSpelling(java.lang.String value) {
		this.shortSpelling = value;
	}
	@Column(name="short_spelling")
	public java.lang.String getShortSpelling() {
		return this.shortSpelling;
	}
	
		
	public void setPosition(java.lang.Integer value) {
		this.position = value;
	}
	@Column(name="position")
	public java.lang.Integer getPosition() {
		return this.position;
	}
	
		
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	@Column(name="country")
	public java.lang.String getCountry() {
		return this.country;
	}
	
		
	public void setProvince(java.lang.String value) {
		this.province = value;
	}
	@Column(name="province")
	public java.lang.String getProvince() {
		return this.province;
	}
	
		
	public void setCity(java.lang.String value) {
		this.city = value;
	}
	@Column(name="city")
	public java.lang.String getCity() {
		return this.city;
	}
	
		
	public void setDistrict(java.lang.String value) {
		this.district = value;
	}
	@Column(name="district")
	public java.lang.String getDistrict() {
		return this.district;
	}
	
		
	public void setRegion(java.lang.String value) {
		this.region = value;
	}
	@Column(name="region")
	public java.lang.String getRegion() {
		return this.region;
	}
	
		
	public void setShortAddress(java.lang.String value) {
		this.shortAddress = value;
	}
	@Column(name="short_address")
	public java.lang.String getShortAddress() {
		return this.shortAddress;
	}
	
		
	public void setAreaType(java.lang.Integer value) {
		this.areaType = value;
	}
	@Column(name="area_type")
	public java.lang.Integer getAreaType() {
		return this.areaType;
	}
	
		
	public void setType(java.lang.String value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.String getType() {
		return this.type;
	}
	
		
	public void setBrand(java.lang.String value) {
		this.brand = value;
	}
	@Column(name="brand")
	public java.lang.String getBrand() {
		return this.brand;
	}
	
	@Column(name="hotel_group")
	public java.lang.String getHotelGroup() {
		return hotelGroup;
	}
	public void setHotelGroup(java.lang.String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public void setStar(java.lang.String value) {
		this.star = value;
	}
	@Column(name="star")
	public java.lang.String getStar() {
		return this.star;
	}
	
		
	public void setFloor(java.lang.String value) {
		this.floor = value;
	}
	@Column(name="floor")
	public java.lang.String getFloor() {
		return this.floor;
	}
	@Transient	
	public String getOpeningDateString() {
		if(getOpeningDate() != null) {
			return this.date2String(getOpeningDate(), FORMAT_OPENING_DATE);
		} else {
			return null;
		}
	}
	public void setOpeningDateString(String value) {
		setOpeningDate(this.string2Date(value, FORMAT_OPENING_DATE));
	}
	
		
	public void setOpeningDate(java.util.Date value) {
		this.openingDate = value;
	}
	@Column(name="opening_date")
	public java.util.Date getOpeningDate() {
		return this.openingDate;
	}
	@Transient	
	public String getLastDecoDateString() {
		if(getLastDecoDate() != null) {
			return this.date2String(getLastDecoDate(), FORMAT_LAST_DECO_DATE);
		} else {
			return null;
		}
	}
	public void setLastDecoDateString(String value) {
		setLastDecoDate(this.string2Date(value, FORMAT_LAST_DECO_DATE));
	}
	
		
	public void setLastDecoDate(java.util.Date value) {
		this.lastDecoDate = value;
	}
	@Column(name="last_deco_date")
	public java.util.Date getLastDecoDate() {
		return this.lastDecoDate;
	}
	
		
	public void setTopic(java.lang.String value) {
		this.topic = value;
	}
	@Column(name="topic")
	public java.lang.String getTopic() {
		return this.topic;
	}
	
		
	public void setFacilities(java.lang.String value) {
		this.facilities = value;
	}
	@Column(name="facilities")
	public java.lang.String getFacilities() {
		return this.facilities;
	}
	
		
	public void setFeature(java.lang.String value) {
		this.feature = value;
	}
	@Column(name="feature")
	public java.lang.String getFeature() {
		return this.feature;
	}
	
		
	public void setAddress(java.lang.String value) {
		this.address = value;
	}
	@Column(name="address")
	public java.lang.String getAddress() {
		return this.address;
	}
	
		
	public void setWebsite(java.lang.String value) {
		this.website = value;
	}
	@Column(name="website")
	public java.lang.String getWebsite() {
		return this.website;
	}
	
		
	public void setTelephone(java.lang.String value) {
		this.telephone = value;
	}
	@Column(name="telephone")
	public java.lang.String getTelephone() {
		return this.telephone;
	}
	
		
	public void setFax(java.lang.String value) {
		this.fax = value;
	}
	@Column(name="fax")
	public java.lang.String getFax() {
		return this.fax;
	}
	
		
	public void setChargePerson(java.lang.String value) {
		this.chargePerson = value;
	}
	@Column(name="charge_person")
	public java.lang.String getChargePerson() {
		return this.chargePerson;
	}
	
		
	public void setChargeMobile(java.lang.String value) {
		this.chargeMobile = value;
	}
	@Column(name="charge_mobile")
	public java.lang.String getChargeMobile() {
		return this.chargeMobile;
	}
	
		
	public void setChargeTelephone(java.lang.String value) {
		this.chargeTelephone = value;
	}
	@Column(name="charge_telephone")
	public java.lang.String getChargeTelephone() {
		return this.chargeTelephone;
	}
	
		
	public void setChargeFax(java.lang.String value) {
		this.chargeFax = value;
	}
	@Column(name="charge_fax")
	public java.lang.String getChargeFax() {
		return this.chargeFax;
	}
	
		
	public void setChargeEmail(java.lang.String value) {
		this.chargeEmail = value;
	}
	@Column(name="charge_email")
	public java.lang.String getChargeEmail() {
		return this.chargeEmail;
	}
	
		
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	@Column(name="remark")
	public java.lang.String getRemark() {
		return this.remark;
	}
	
		
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	@Column(name="description")
	public java.lang.String getDescription() {
		return this.description;
	}
	@Transient	
	public String getInDateString() {
		if(getInDate() != null) {
			return this.date2String(getInDate(), FORMAT_IN_DATE);
		} else {
			return null;
		}
	}
	public void setInDateString(String value) {
		setInDate(this.string2Date(value, FORMAT_IN_DATE));
	}
	
		
	public void setInDate(java.util.Date value) {
		this.inDate = value;
	}
	@Column(name="in_date")
	public java.util.Date getInDate() {
		return this.inDate;
	}
	@Transient	
	public String getOutDateString() {
		if(getOutDate() != null) {
			return this.date2String(getOutDate(), FORMAT_OUT_DATE);
		} else {
			return null;
		}
	}
	public void setOutDateString(String value) {
		setOutDate(this.string2Date(value, FORMAT_OUT_DATE));
	}
	
		
	public void setOutDate(java.util.Date value) {
		this.outDate = value;
	}
	@Column(name="out_date")
	public java.util.Date getOutDate() {
		return this.outDate;
	}
	
		
	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}
	@Column(name="island_uuid")
	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
		
	public void setLongitude(java.lang.String value) {
		this.longitude = value;
	}
	@Column(name="longitude")
	public java.lang.String getLongitude() {
		return this.longitude;
	}
	
		
	public void setLatitude(java.lang.String value) {
		this.latitude = value;
	}
	@Column(name="latitude")
	public java.lang.String getLatitude() {
		return this.latitude;
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
	
		
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.Integer getStatus() {
		return this.status;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}




	
}

