/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.entity;

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
@Table(name = "sys_region")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SysRegion   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "SysRegion";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_NAME = "区域名称";
	public static final String ALIAS_STATUS = "状态0启用1停用";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_IS_HOME = "国内国际标记1国内2国际";
	public static final String ALIAS_CREATE_DATE = "签证创建时间";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_LEVEL = "级别";
	public static final String ALIAS_DESCRIPTION = "描述";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String name;
	private java.lang.String status;
	private java.lang.String delFlag;
	private java.lang.String isHome;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private java.lang.Long createBy;
	private java.lang.Long updateBy;
	private java.lang.Integer level;
	private java.lang.String description;
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
	public SysRegion(){
	}

	public SysRegion(
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
	
		
	public void setName(java.lang.String value) {
		this.name = value;
	}
	@Column(name="name")
	public java.lang.String getName() {
		return this.name;
	}
	
		
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.String getStatus() {
		return this.status;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	
		
	public void setIsHome(java.lang.String value) {
		this.isHome = value;
	}
	@Column(name="isHome")
	public java.lang.String getIsHome() {
		return this.isHome;
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
	@Column(name="create_date")
	public java.util.Date getCreateDate() {
		return this.createDate;
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
	@Column(name="update_date")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	
		
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	@Column(name="create_by")
	public java.lang.Long getCreateBy() {
		return this.createBy;
	}
	
		
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	@Column(name="update_by")
	public java.lang.Long getUpdateBy() {
		return this.updateBy;
	}
	
		
	public void setLevel(java.lang.Integer value) {
		this.level = value;
	}
	@Column(name="level")
	public java.lang.Integer getLevel() {
		return this.level;
	}
	
		
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	@Column(name="description")
	public java.lang.String getDescription() {
		return this.description;
	}


	
}

