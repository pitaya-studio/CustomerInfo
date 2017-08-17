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
import java.util.List;

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
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "activity_island_visa_file")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityIslandVisaFile   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//date formats
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activityIslandUuid;
	private java.lang.String country;
	private java.lang.Integer visaTypeId;
	private java.lang.Integer createBy;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.util.Date createDate;
	private java.lang.String delFlag;
	private List<HotelAnnex> hotelAnnexList; // 海岛游产品上传签证资料
	//columns END
	
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	@Transient
	public List<HotelAnnex> getHotelAnnexList() {
		return hotelAnnexList;
	}

	public void setHotelAnnexList(List<HotelAnnex> hotelAnnexList) {
		this.hotelAnnexList = hotelAnnexList;
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public ActivityIslandVisaFile(){
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
	
		
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	@Column(name="activity_island_uuid")
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
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
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
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
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

