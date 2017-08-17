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
@Table(name = "island_travelervisa")
@DynamicInsert(true)
@DynamicUpdate(true)
public class IslandTravelervisa   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "IslandTravelervisa";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_ISLAND_ORDER_UUID = "海岛游订单UUID";
	public static final String ALIAS_ISLAND_TRAVELER_UUID = "海岛游订单游客UUID";
	public static final String ALIAS_COUNTRY = "申请国家UUID";
	public static final String ALIAS_VISA_TYPE_ID = "签证类型(字典表type:new_visa_type)";
	public static final String ALIAS_CREATE_BY = "createBy";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_BY = "updateBy";
	public static final String ALIAS_UPDATE_DATE = "updateDate";
	public static final String ALIAS_DEL_FLAG = "delFlag";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Long id;
	private java.lang.String uuid;
	private java.lang.String islandOrderUuid;
	private java.lang.String islandTravelerUuid;
	private java.lang.String country;
	private java.lang.Integer visaTypeId;
	private java.lang.Long createBy;
	private java.util.Date createDate;
	private java.lang.Long updateBy;
	private java.util.Date updateDate;
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
	public IslandTravelervisa(){
	}

	public IslandTravelervisa(
		java.lang.Long id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Long getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setIslandOrderUuid(java.lang.String value) {
		this.islandOrderUuid = value;
	}
	@Column(name="island_order_uuid")
	public java.lang.String getIslandOrderUuid() {
		return this.islandOrderUuid;
	}
	
		
	public void setIslandTravelerUuid(java.lang.String value) {
		this.islandTravelerUuid = value;
	}
	@Column(name="island_traveler_uuid")
	public java.lang.String getIslandTravelerUuid() {
		return this.islandTravelerUuid;
	}
	
		
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	@Column(name="country")
	public java.lang.String getCountry() {
		return this.country;
	}
	
		
	public void setVisaTypeId(java.lang.Integer value) {
		this.visaTypeId = value;
	}
	@Column(name="visa_type_id")
	public java.lang.Integer getVisaTypeId() {
		return this.visaTypeId;
	}
	
		
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Long getCreateBy() {
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
	
		
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Long getUpdateBy() {
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

