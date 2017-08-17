/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.entity;

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
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_quote_preferential")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuotePreferential   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuotePreferential";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_PREFERENTIAL_NAME = "优惠信息名称";
	public static final String ALIAS_BOOKING_CODE = "下单代码";
	public static final String ALIAS_IN_DATE = "入住日期";
	public static final String ALIAS_OUT_DATE = "离店日期";
	public static final String ALIAS_BOOKING_START_DATE = "预订起始日期";
	public static final String ALIAS_BOOKING_END_DATE = "预订结束日期";
	public static final String ALIAS_ISLAND_WAY = "交通（上岛方式字典UUID，多个用；分隔）";
	public static final String ALIAS_IS_RELATION = "是否关联酒店（0不关联，1关联。关联酒店的信息存储在hotel_pl_preferential_relHotel表中）";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_OUT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_BOOKING_START_DATE = "yyyy-MM-dd";
	public static final String FORMAT_BOOKING_END_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"优惠信息名称"
	private java.lang.String preferentialName;
	//"下单代码"
	private java.lang.String bookingCode;
	//"入住日期"
	private java.util.Date inDate;
	//"离店日期"
	private java.util.Date outDate;
	//"预订起始日期"
	private java.util.Date bookingStartDate;
	//"预订结束日期"
	private java.util.Date bookingEndDate;
	//"交通（上岛方式字典UUID，多个用；分隔）"
	private java.lang.String islandWay;
	//"是否关联酒店（0不关联，1关联。关联酒店的信息存储在hotel_pl_preferential_relHotel表中）"
	private java.lang.Integer isRelation;
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
	//优惠描述
	private String description;
	
	private List<HotelQuotePreferentialRoom> roomList;
	private List<HotelQuotePreferentialRequire> requireList;
	private List<HotelQuoteResult> resultList;
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
	public HotelQuotePreferential(){
	}

	public HotelQuotePreferential(
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
	
		
	public void setPreferentialName(java.lang.String value) {
		this.preferentialName = value;
	}
	@Column(name="preferential_name")
	public java.lang.String getPreferentialName() {
		return this.preferentialName;
	}
	
		
	public void setBookingCode(java.lang.String value) {
		this.bookingCode = value;
	}
	@Column(name="booking_code")
	public java.lang.String getBookingCode() {
		return this.bookingCode;
	}
	@Transient	
	public String getInDateString() {
		if(getInDate() != null) {
			return this.date2String(getInDate(), FORMAT_IN_DATE);
		} else {
			return null;
		}
	}
	public void setInDateString(String value) {
		setInDate(this.string2Date(value, FORMAT_IN_DATE));
	}
	
		
	public void setInDate(java.util.Date value) {
		this.inDate = value;
	}
	@Column(name="in_date")
	public java.util.Date getInDate() {
		return this.inDate;
	}
	@Transient	
	public String getOutDateString() {
		if(getOutDate() != null) {
			return this.date2String(getOutDate(), FORMAT_OUT_DATE);
		} else {
			return null;
		}
	}
	public void setOutDateString(String value) {
		setOutDate(this.string2Date(value, FORMAT_OUT_DATE));
	}
	
		
	public void setOutDate(java.util.Date value) {
		this.outDate = value;
	}
	@Column(name="out_date")
	public java.util.Date getOutDate() {
		return this.outDate;
	}
	@Transient	
	public String getBookingStartDateString() {
		if(getBookingStartDate() != null) {
			return this.date2String(getBookingStartDate(), FORMAT_BOOKING_START_DATE);
		} else {
			return null;
		}
	}
	public void setBookingStartDateString(String value) {
		setBookingStartDate(this.string2Date(value, FORMAT_BOOKING_START_DATE));
	}
	
		
	public void setBookingStartDate(java.util.Date value) {
		this.bookingStartDate = value;
	}
	@Column(name="booking_start_date")
	public java.util.Date getBookingStartDate() {
		return this.bookingStartDate;
	}
	@Transient	
	public String getBookingEndDateString() {
		if(getBookingEndDate() != null) {
			return this.date2String(getBookingEndDate(), FORMAT_BOOKING_END_DATE);
		} else {
			return null;
		}
	}
	public void setBookingEndDateString(String value) {
		setBookingEndDate(this.string2Date(value, FORMAT_BOOKING_END_DATE));
	}
	
		
	public void setBookingEndDate(java.util.Date value) {
		this.bookingEndDate = value;
	}
	@Column(name="booking_end_date")
	public java.util.Date getBookingEndDate() {
		return this.bookingEndDate;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	
		
	public void setIsRelation(java.lang.Integer value) {
		this.isRelation = value;
	}
	@Column(name="isRelation")
	public java.lang.Integer getIsRelation() {
		return this.isRelation;
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
	public List<HotelQuotePreferentialRoom> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<HotelQuotePreferentialRoom> roomList) {
		this.roomList = roomList;
	}
	@Transient
	public List<HotelQuotePreferentialRequire> getRequireList() {
		return requireList;
	}

	public void setRequireList(List<HotelQuotePreferentialRequire> requireList) {
		this.requireList = requireList;
	}
	@Transient
	public List<HotelQuoteResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<HotelQuoteResult> resultList) {
		this.resultList = resultList;
	}
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

