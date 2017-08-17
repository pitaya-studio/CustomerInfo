/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.entity;

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
@Table(name = "sys_geography")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SysGeography   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	//date formats
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private Integer id;
	private java.lang.String uuid;
	private java.lang.Integer parentId;
	private java.lang.String parentUuid;
	private java.lang.Integer level;
	private java.lang.Integer sort;
	private java.lang.Integer hot;
	private java.lang.String nameCn;
	private java.lang.String nameShortCn;
	private java.lang.String nameEn;
	private java.lang.String nameShortEn;
	private java.lang.String namePinyin;
	private java.lang.String nameShortPinyin;
	private java.lang.String crossSection;
	private java.lang.String description;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.Integer status;
	private java.lang.String delFlag;
	private java.lang.Integer position;
	//添加父级uuids字段
	private java.lang.String parentUuids;
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
	public SysGeography(){
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
	
		
	public void setParentId(java.lang.Integer value) {
		this.parentId = value;
	}
	@Column(name="parent_id")
	public java.lang.Integer getParentId() {
		return this.parentId;
	}
	
		
	public void setParentUuid(java.lang.String value) {
		this.parentUuid = value;
	}
	@Column(name="parent_uuid")
	public java.lang.String getParentUuid() {
		return this.parentUuid;
	}
	
		
	public void setLevel(java.lang.Integer value) {
		this.level = value;
	}
	@Column(name="level")
	public java.lang.Integer getLevel() {
		return this.level;
	}
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
	}
	
	
	public void setHot(java.lang.Integer value) {
		this.hot = value;
	}
	@Column(name="hot")
	public java.lang.Integer getHot() {
		return this.hot;
	}
	
		
	public void setNameCn(java.lang.String value) {
		this.nameCn = value;
	}
	@Column(name="name_cn")
	public java.lang.String getNameCn() {
		return this.nameCn;
	}
	
		
	public void setNameShortCn(java.lang.String value) {
		this.nameShortCn = value;
	}
	@Column(name="name_short_cn")
	public java.lang.String getNameShortCn() {
		return this.nameShortCn;
	}
	
		
	public void setNameEn(java.lang.String value) {
		this.nameEn = value;
	}
	@Column(name="name_en")
	public java.lang.String getNameEn() {
		return this.nameEn;
	}
	
		
	public void setNameShortEn(java.lang.String value) {
		this.nameShortEn = value;
	}
	@Column(name="name_short_en")
	public java.lang.String getNameShortEn() {
		return this.nameShortEn;
	}
	
		
	public void setNamePinyin(java.lang.String value) {
		this.namePinyin = value;
	}
	@Column(name="name_pinyin")
	public java.lang.String getNamePinyin() {
		return this.namePinyin;
	}
	
		
	public void setNameShortPinyin(java.lang.String value) {
		this.nameShortPinyin = value;
	}
	@Column(name="name_short_pinyin")
	public java.lang.String getNameShortPinyin() {
		return this.nameShortPinyin;
	}
	
		
	public void setCrossSection(java.lang.String value) {
		this.crossSection = value;
	}
	@Column(name="cross_section")
	public java.lang.String getCrossSection() {
		return this.crossSection;
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
   
	public void setPosition(java.lang.Integer value) {
		this.position = value;
	}
	@Column(name="position")
	public java.lang.Integer getPosition() {
		return this.position;
	}
	//添加父级uuids字段 parentUuids
	public void setParentUuids(java.lang.String value) {
		this.parentUuids = value;
	}
	@Column(name="parent_uuids")
	public java.lang.String getParentUuids() {
		return this.parentUuids;
	}
}

