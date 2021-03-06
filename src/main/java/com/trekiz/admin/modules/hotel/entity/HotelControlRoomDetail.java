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
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_control_room_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelControlRoomDetail   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelControlRoomDetail";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_CONTROL_UUID = "hotel_control表uuid";
	public static final String ALIAS_HOTEL_CONTROL_DETAIL_UUID = "hotel_control_detail表uuid";
	public static final String ALIAS_ROOM_UUID = "hotel_room表uuid";
	public static final String ALIAS_NIGHT = "晚数";
	public static final String ALIAS_HOTEL_MEALS = "餐型uuids(使用;进行分隔)";
	public static final String ALIAS_SORT = "排序";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String hotelControlUuid;
	private java.lang.String hotelControlDetailUuid;
	private java.lang.String roomUuid;
	private java.lang.String roomName;
	private java.lang.Integer night;
	private java.lang.Integer sort;
	private java.lang.String hotelMeals;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
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
	public HotelControlRoomDetail(){
	}

	public HotelControlRoomDetail(
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
	
		
	public void setHotelControlUuid(java.lang.String value) {
		this.hotelControlUuid = value;
	}
	@Column(name="hotel_control_uuid")
	public java.lang.String getHotelControlUuid() {
		return this.hotelControlUuid;
	}
	
		
	public void setHotelControlDetailUuid(java.lang.String value) {
		this.hotelControlDetailUuid = value;
	}
	@Column(name="hotel_control_detail_uuid")
	public java.lang.String getHotelControlDetailUuid() {
		return this.hotelControlDetailUuid;
	}
	
		
	public void setRoomUuid(java.lang.String value) {
		this.roomUuid = value;
	}
	@Column(name="room_uuid")
	public java.lang.String getRoomUuid() {
		return this.roomUuid;
	}

	@Transient
	public java.lang.String getRoomName() {
		return roomName;
	}

	public void setRoomName(java.lang.String roomName) {
		this.roomName = roomName;
	}
		
	public void setNight(java.lang.Integer value) {
		this.night = value;
	}
	@Column(name="night")
	public java.lang.Integer getNight() {
		return this.night;
	}
	
	
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
	}


	public void setHotelMeals(java.lang.String hotelMeals) {
		this.hotelMeals = hotelMeals;
	}
	@Column(name="hotel_meals")
	public java.lang.String getHotelMeals() {
		return hotelMeals;
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

