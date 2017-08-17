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
@Table(name = "hotel_room_occuRate_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelRoomOccuRateDetail   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelRoomOccuRateDetail";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_ROOM_UUID = "酒店房型UUID";
	public static final String ALIAS_HOTEL_ROOM_OCCU_RATE_UUID = "酒店容住率总表UUID";
	public static final String ALIAS_TRAVELER_TYPE_UUID = "游客类型UUID";
	public static final String ALIAS_COUNT = "游客类型人数";
	public static final String ALIAS_SHORT_NAME = "游客类型简称";
	
	//date formats
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String hotelUuid;
	private java.lang.String hotelRoomUuid;
	private java.lang.String hotelRoomOccuRateUuid;
	private java.lang.String travelerTypeUuid;
	private java.lang.Integer count;
	private java.lang.String shortName;
	//columns END
	@SuppressWarnings("unused")
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	@SuppressWarnings("unused")
	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public HotelRoomOccuRateDetail(){
	}

	public HotelRoomOccuRateDetail(
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
	
		
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	@Column(name="hotel_uuid")
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	
		
	public void setHotelRoomUuid(java.lang.String value) {
		this.hotelRoomUuid = value;
	}
	@Column(name="hotel_room_uuid")
	public java.lang.String getHotelRoomUuid() {
		return this.hotelRoomUuid;
	}
	
		
	public void setHotelRoomOccuRateUuid(java.lang.String value) {
		this.hotelRoomOccuRateUuid = value;
	}
	@Column(name="hotel_room_occuRate_uuid")
	public java.lang.String getHotelRoomOccuRateUuid() {
		return this.hotelRoomOccuRateUuid;
	}
	
		
	public void setTravelerTypeUuid(java.lang.String value) {
		this.travelerTypeUuid = value;
	}
	@Column(name="traveler_type_uuid")
	public java.lang.String getTravelerTypeUuid() {
		return this.travelerTypeUuid;
	}
	
		
	public void setCount(java.lang.Integer value) {
		this.count = value;
	}
	@Column(name="count")
	public java.lang.Integer getCount() {
		return this.count;
	}
	
		
	public void setShortName(java.lang.String value) {
		this.shortName = value;
	}
	@Column(name="short_name")
	public java.lang.String getShortName() {
		return this.shortName;
	}


	
}

