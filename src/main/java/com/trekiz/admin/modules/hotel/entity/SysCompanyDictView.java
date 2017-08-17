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
@Table(name = "sys_company_dict_view")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SysCompanyDictView   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "SysCompanyDictView";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_LABEL = "label";
	public static final String ALIAS_VALUE = "value";
	public static final String ALIAS_TYPE = "type";
	public static final String ALIAS_DEFAULT_FLAG = "defaultFlag";
	public static final String ALIAS_SORT = "sort";
	public static final String ALIAS_DESCRIPTION = "description";
	public static final String ALIAS_COMPANY_ID = "companyId";
	public static final String ALIAS_CREATE_BY = "createBy";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_BY = "updateBy";
	public static final String ALIAS_UPDATE_DATE = "updateDate";
	public static final String ALIAS_REMARKS = "remarks";
	public static final String ALIAS_DEL_FLAG = "delFlag";
	public static final String ALIAS_UUID = "uuid";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String label;
	private java.lang.String value;
	private java.lang.String type;
	private java.lang.String defaultFlag;
	private java.lang.Integer sort;
	private java.lang.String description;
	private java.lang.Long companyId;
	private java.lang.Long createBy;
	private java.util.Date createDate;
	private java.lang.Long updateBy;
	private java.util.Date updateDate;
	private java.lang.String remarks;
	private java.lang.String delFlag;
	private java.lang.String uuid;
	
	private HotelTopic hotelTopic;
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
	public SysCompanyDictView(){
	}
		
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Column(name="id")
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setLabel(java.lang.String value) {
		this.label = value;
	}
	@Column(name="label")
	public java.lang.String getLabel() {
		return this.label;
	}
	
		
	public void setValue(java.lang.String value) {
		this.value = value;
	}
	@Column(name="value")
	public java.lang.String getValue() {
		return this.value;
	}
	
		
	public void setType(java.lang.String value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.String getType() {
		return this.type;
	}
	
		
	public void setDefaultFlag(java.lang.String value) {
		this.defaultFlag = value;
	}
	@Column(name="defaultFlag")
	public java.lang.String getDefaultFlag() {
		return this.defaultFlag;
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
	
		
	public void setCompanyId(java.lang.Long value) {
		this.companyId = value;
	}
	@Column(name="companyId")
	public java.lang.Long getCompanyId() {
		return this.companyId;
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
	
		
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}
	@Column(name="remarks")
	public java.lang.String getRemarks() {
		return this.remarks;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	
	@Id
	@Column(name = "uuid", unique = true, nullable = false)
	public java.lang.String getUuid() {
		return this.uuid;
	}

	@Transient
	public HotelTopic getHotelTopic() {
		return this.hotelTopic;
	}
	
	public void setHotelTopic(HotelTopic hotelTopic) {
		this.hotelTopic = hotelTopic;
	}
	
}

