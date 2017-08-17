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
@Table(name = "hotel_star")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelStar   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelStar";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_DICT_UUID = "字典表酒店星级UUID";
	public static final String ALIAS_LABEL = "星级名称";
	public static final String ALIAS_VALUE = "星级值(取对应字典表里的value)";
	public static final String ALIAS_SORT = "排序";
	public static final String ALIAS_DESCRIPTION = "描述";
	public static final String ALIAS_WHOLESALER_ID = "批发商ID";
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
	private java.lang.Long id;
	private java.lang.String uuid;
	private java.lang.String dictUuid;
	private java.lang.String label;
	private java.lang.Integer value;
	private java.lang.Integer sort;
	private java.lang.String description;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	//columns END
	
	private SysCompanyDictView sysCompanyDictView;
	
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
	public HotelStar(){
	}

	public HotelStar(
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
	
	public void setDictUuid(java.lang.String dictUuid) {
		this.dictUuid = dictUuid;
	}
	
	@Column(name="dict_uuid")
	public String getDictUuid() {
		return dictUuid;
	}
		
	public void setSysCompanyDictView(SysCompanyDictView sysCompanyDictView) {
		this.sysCompanyDictView = sysCompanyDictView;
	}
	
	@Transient
	public SysCompanyDictView getSysCompanyDictView() {
		return this.sysCompanyDictView;
	}
	
		
	public void setLabel(java.lang.String value) {
		this.label = value;
	}
	@Column(name="label")
	public java.lang.String getLabel() {
		return this.label;
	}
	
	public void setValue(java.lang.Integer value) {
		this.value = value;
	}
	public java.lang.Integer getValue() {
		return this.value;
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
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
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

