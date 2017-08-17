/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "island")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Island   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "Island";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_ISLAND_NAME = "岛屿名称";
	public static final String ALIAS_SHORT_NAME = "名称缩写";
	public static final String ALIAS_SPELLING = "全拼";
	public static final String ALIAS_SHORT_SPELLING = "全拼缩写";
	public static final String ALIAS_ISLAND_NAME_EN = "英文名称";
	public static final String ALIAS_SHORT_NAME_EN = "英文缩写";
	public static final String ALIAS_POSITION = "位置（1：境内，2：境外）";
	public static final String ALIAS_COUNTRY = "地理国家";
	public static final String ALIAS_PROVINCE = "地理省";
	public static final String ALIAS_CITY = "地理市";
	public static final String ALIAS_DISTRICT = "地理区";
	public static final String ALIAS_REGION = "地理行政区";
	public static final String ALIAS_SHORT_ADDRESS = "地址后缀";
	public static final String ALIAS_TYPE = "岛屿类型（字典）";
	public static final String ALIAS_TOPIC = "岛屿主题（字典）";
	public static final String ALIAS_ISLAND_WAY = "上岛方式（字典）";
	public static final String ALIAS_SORT = "排序";
	public static final String ALIAS_DESCRIPTION = "描述";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_LONGITUDE = "经度";
	public static final String ALIAS_LATITUDE = "纬度";
	public static final String ALIAS_ISLAND_ADDRESS = "岛屿地址";
	public static final String ALIAS_ISLAND_COUNTRY = "国家";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String islandName;
	private java.lang.String shortName;
	private java.lang.String spelling;
	private java.lang.String shortSpelling;
	private java.lang.String islandNameEn;
	private java.lang.String shortNameEn;
	private java.lang.Integer position;
	private java.lang.String country;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String district;
	private java.lang.String region;
	private java.lang.String shortAddress;
	private java.lang.String type;
	private java.lang.String topic;
	private java.lang.String islandWay;
	private java.lang.Integer sort;
	private java.lang.String description;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.Integer status;
	private java.lang.String delFlag;
	private java.lang.Integer wholesalerId;
	private java.lang.String longitude;
	private java.lang.String latitude;
	private java.lang.String islandAddress;
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
	public Island(){
	}

	public Island(
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
	
		
	public void setIslandName(java.lang.String value) {
		this.islandName = value;
	}
	@Column(name="island_name")
	public java.lang.String getIslandName() {
		return this.islandName;
	}
	
		
	public void setShortName(java.lang.String value) {
		this.shortName = value;
	}
	@Column(name="short_name")
	public java.lang.String getShortName() {
		return this.shortName;
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
	
		
	public void setIslandNameEn(java.lang.String value) {
		this.islandNameEn = value;
	}
	@Column(name="island_name_en")
	public java.lang.String getIslandNameEn() {
		return this.islandNameEn;
	}
	
		
	public void setShortNameEn(java.lang.String value) {
		this.shortNameEn = value;
	}
	@Column(name="short_name_en")
	public java.lang.String getShortNameEn() {
		return this.shortNameEn;
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
	
		
	public void setType(java.lang.String value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.String getType() {
		return this.type;
	}
	
		
	public void setTopic(java.lang.String value) {
		this.topic = value;
	}
	@Column(name="topic")
	public java.lang.String getTopic() {
		return this.topic;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
	}
	
		
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	@Column(name="description")
	public java.lang.String getDescription() {
		return this.description;
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
	
		
	public void setIslandAddress(java.lang.String value) {
		this.islandAddress = value;
	}
	@Column(name="islandAddress")
	public java.lang.String getIslandAddress() {
		return this.islandAddress;
	}

	
}

