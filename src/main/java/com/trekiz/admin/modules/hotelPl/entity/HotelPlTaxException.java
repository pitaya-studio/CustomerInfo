/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.entity;

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
@Table(name = "hotel_pl_taxException")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelPlTaxException   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	/**默认的分隔符 ；*/
	public static final String REGEX = ";";
	//alias
	public static final String TABLE_ALIAS = "HotelPlTaxException";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_EXCEPTION_TYPE = "例外类型(1、房型；2、餐型；3、交通)";
	public static final String ALIAS_EXCEPTION_NAME = "例外类型名称(1、房型；2、餐型；3、交通)显示时使用";
	public static final String ALIAS_START_DATE = "起始日期";
	public static final String ALIAS_END_DATE = "结束日期";
	public static final String ALIAS_TAX_TYPE = "税费类型(1、政府税；2、服务费；3、床税；多个用“;”分隔)";
	public static final String ALIAS_TRAVEL_TYPE = "游客类型uuid多个用“;”分隔";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_START_DATE = "yyyy-MM-dd";
	public static final String FORMAT_END_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	
	/**房型*/
	public static final Integer EXCEPTION_TYPE_ROOM = 1;
	/**餐型*/
	public static final Integer EXCEPTION_TYPE_MEAL = 2;
	/**交通*/
	public static final Integer EXCEPTION_TYPE_ISLAND = 3;
	
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"例外类型(1、房型；2、餐型；3、交通)"
	private java.lang.Integer exceptionType;
	//"例外类型名称(1、房型；2、餐型；3、交通)显示时使用"
	private java.lang.String exceptionName;
	//"起始日期"
	private java.util.Date startDate;
	//"结束日期"
	private java.util.Date endDate;
	//"税费类型(1、政府税；2、服务费；3、床税；4、其他)"
	private java.lang.String taxType;
	//"游客类型uuid多个用“;”分隔"
	private java.lang.String travelType;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	//columns END
	//自定义属性
	private String taxTypeText;
	private String travelTypeText;
	//自定义属性
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
	public HotelPlTaxException(){
	}

	public HotelPlTaxException(
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
	
		
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	@Column(name="hotel_pl_uuid")
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
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
	
		
	public void setExceptionType(java.lang.Integer value) {
		this.exceptionType = value;
	}
	@Column(name="exception_type")
	public java.lang.Integer getExceptionType() {
		return this.exceptionType;
	}
	
		
	public void setExceptionName(java.lang.String value) {
		this.exceptionName = value;
	}
	@Column(name="exception_name")
	public java.lang.String getExceptionName() {
		return this.exceptionName;
	}
	@Transient	
	public String getStartDateString() {
		if(getStartDate() != null) {
			return this.date2String(getStartDate(), FORMAT_START_DATE);
		} else {
			return null;
		}
	}
	public void setStartDateString(String value) {
		setStartDate(this.string2Date(value, FORMAT_START_DATE));
	}
	
		
	public void setStartDate(java.util.Date value) {
		this.startDate = value;
	}
	@Column(name="start_date")
	public java.util.Date getStartDate() {
		return this.startDate;
	}
	@Transient	
	public String getEndDateString() {
		if(getEndDate() != null) {
			return this.date2String(getEndDate(), FORMAT_END_DATE);
		} else {
			return null;
		}
	}
	public void setEndDateString(String value) {
		setEndDate(this.string2Date(value, FORMAT_END_DATE));
	}
	
		
	public void setEndDate(java.util.Date value) {
		this.endDate = value;
	}
	@Column(name="end_date")
	public java.util.Date getEndDate() {
		return this.endDate;
	}
	
		
	public void setTaxType(java.lang.String value) {
		this.taxType = value;
	}
	@Column(name="tax_type")
	public java.lang.String getTaxType() {
		return this.taxType;
	}
	
		
	public void setTravelType(java.lang.String value) {
		this.travelType = value;
	}
	@Column(name="travel_type")
	public java.lang.String getTravelType() {
		return this.travelType;
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
	@Transient
	public String getTaxTypeText() {
		return taxTypeText;
	}

	public void setTaxTypeText(String taxTypeText) {
		this.taxTypeText = taxTypeText;
	}

	@Transient
	public String getTravelTypeText() {
		return travelTypeText;
	}

	public void setTravelTypeText(String travelTypeText) {
		this.travelTypeText = travelTypeText;
	}

	
}

