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

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "activity_island")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityIsland   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivityIsland";
	public static final String ALIAS_ID = "唯一主键,自主递增";
	public static final String ALIAS_UUID = "唯一性标识uuid";
	public static final String ALIAS_ACTIVITY_SER_NUM = "产品编号,如SG0001";
	public static final String ALIAS_ACITIVITY_NAME = "海岛游产品的名称";
	public static final String ALIAS_COUNTRY = "国家";
	public static final String ALIAS_ISLAND_UUID = "海岛";
	public static final String ALIAS_HOTEL_UUID = "酒店";
	public static final String ALIAS_CURRENCY_ID = "币种";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_WHOLESALER_ID = "产品所属批发商的ID";
	public static final String ALIAS_DEPT_ID = "产品所属部门(创建者部门)";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "更新人";
	public static final String ALIAS_UPDATE_DATE = "更新时间";
	public static final String ALIAS_DEL_FLAG = "0：正常；1：删除";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activitySerNum;
	private java.lang.String activityName;
	private java.lang.String country;
	private java.lang.String islandUuid;
	private java.lang.String hotelUuid;
	private java.lang.Integer currencyId;
	private java.lang.String memo;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer deptId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	
	private List<ActivityIslandGroup> activityIslandGroupList;  //海岛游产品团期表
	private List<ActivityIslandVisaFile> activityIslandVisaFile; // 海岛游产品上传签证资料
	@Transient
	public List<ActivityIslandVisaFile> getActivityIslandVisaFile() {
		return activityIslandVisaFile;
	}

	public void setActivityIslandVisaFile(
			List<ActivityIslandVisaFile> activityIslandVisaFile) {
		this.activityIslandVisaFile = activityIslandVisaFile;
	}

	@Transient
	public List<ActivityIslandGroup> getActivityIslandGroupList() {
		return activityIslandGroupList;
	}

	public void setActivityIslandGroupList(
			List<ActivityIslandGroup> activityIslandGroupList) {
		this.activityIslandGroupList = activityIslandGroupList;
	}


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
	public ActivityIsland(){
	}

	public ActivityIsland(
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
	
		
	public void setActivitySerNum(java.lang.String value) {
		this.activitySerNum = value;
	}
	@Column(name="activitySerNum")
	public java.lang.String getActivitySerNum() {
		return this.activitySerNum;
	}
	
	@Column(name="activityName")
	public java.lang.String getActivityName() {
		return activityName;
	}

	public void setActivityName(java.lang.String activityName) {
		this.activityName = activityName;
	}	
		
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	@Column(name="country")
	public java.lang.String getCountry() {
		return this.country;
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
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
		
	public void setDeptId(java.lang.Integer value) {
		this.deptId = value;
	}
	@Column(name="deptId")
	public java.lang.Integer getDeptId() {
		return this.deptId;
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

