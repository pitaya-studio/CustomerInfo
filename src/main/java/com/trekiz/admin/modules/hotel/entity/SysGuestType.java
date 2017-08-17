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
@Table(name = "sys_guest_type")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SysGuestType   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "SysGuestType";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_NAME = "住客类型名称";
	public static final String ALIAS_VALUE = "属性值（对应的是游客人数）";
	public static final String ALIAS_TYPE = "类型（0：共；1：增。即0共几人，1第几人）";
	public static final String ALIAS_PERSON_TYPE="人员类型";
	public static final String ALIAS_DESCRIPTION = "描述";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	
	/**类型 共（共几人）*/
	public static final Integer SYS_GUEST_TYPE_TYPE_0 = 0;
	/**类型 增（第几人）*/
	public static final Integer SYS_GUEST_TYPE_TYPE_1 = 1;
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"住客类型名称"
	private java.lang.String name;
	//"属性值（对应的是游客人数）"
	private java.lang.Integer value;
	//"类型（0：共；1：增。即0共几人，1第几人）"
	private java.lang.Integer type;
	//"描述"
	private java.lang.String description;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除状态"
	private java.lang.String delFlag;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	private java.lang.Integer personType;
	
	//批发商的游客类型UUID
	private String hotelGuestTypeUuid;
	
	//columns END
	private java.util.List<SysGuestTravelerTypeRel> travelerTypeRelList; 
	
	private java.lang.String hotelRoomUuid;//报价器查询类型绑定的房型UUID使用
	
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
	@Column(name="person_type")
	public java.lang.Integer getPersonType() {
		return personType;
	}

	public void setPersonType(java.lang.Integer personType) {
		this.personType = personType;
	}

	public SysGuestType(){
	}

	public SysGuestType(
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
	
		
	public void setValue(java.lang.Integer value) {
		this.value = value;
	}
	@Column(name="value")
	public java.lang.Integer getValue() {
		return this.value;
	}
	
		
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.Integer getType() {
		return this.type;
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
	@Transient	
	public java.lang.String getHotelRoomUuid() {
		return hotelRoomUuid;
	}

	public void setHotelRoomUuid(java.lang.String hotelRoomUuid) {
		this.hotelRoomUuid = hotelRoomUuid;
	}

	@Transient	
	public java.util.List<SysGuestTravelerTypeRel> getTravelerTypeRelList() {
		return travelerTypeRelList;
	}

	public void setTravelerTypeRelList(
			java.util.List<SysGuestTravelerTypeRel> travelerTypeRelList) {
		this.travelerTypeRelList = travelerTypeRelList;
	}
	@Transient	
	public String getHotelGuestTypeUuid() {
		return hotelGuestTypeUuid;
	}

	public void setHotelGuestTypeUuid(String hotelGuestTypeUuid) {
		this.hotelGuestTypeUuid = hotelGuestTypeUuid;
	}

}

