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
@Table(name = "activity_hotel_group_room")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityHotelGroupRoom   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivityHotelGroupRoom";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "唯一uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_UUID = "酒店产品uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_GROUP_UUID = "酒店产品团期uuid";
	public static final String ALIAS_HOTEL_ROOM_UUID = "酒店房型uuid";
	public static final String ALIAS_NIGHTS = "晚数";
	public static final String ALIAS_SORT = "排序";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_UPDATE_BY = "更新人";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标志（0:正常，1:删除）";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"酒店产品uuid"
	private java.lang.String activityHotelUuid;
	//"酒店产品团期uuid"
	private java.lang.String activityHotelGroupUuid;
	//"酒店房型uuid"
	private java.lang.String hotelRoomUuid;
	//"晚数"
	private java.lang.Integer nights;
	//"排序"
	private java.lang.Integer sort;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//columns END
	//房型下总的升餐个数
	private java.lang.Integer roomMealRiseRowspan;
	//酒店产品团期基础餐型明细
	private List<ActivityHotelGroupMeal> activityHotelGroupMealList;
		
		
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
	public ActivityHotelGroupRoom(){
	}

	public ActivityHotelGroupRoom(
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
	@Transient
	public java.lang.Integer getRoomMealRiseRowspan() {
		return roomMealRiseRowspan;
	}

	public void setRoomMealRiseRowspan(java.lang.Integer roomMealRiseRowspan) {
		this.roomMealRiseRowspan = roomMealRiseRowspan;
	}
		
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	@Column(name="activity_hotel_uuid")
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}
	
		
	public void setActivityHotelGroupUuid(java.lang.String value) {
		this.activityHotelGroupUuid = value;
	}
	@Column(name="activity_hotel_group_uuid")
	public java.lang.String getActivityHotelGroupUuid() {
		return this.activityHotelGroupUuid;
	}
	
		
	public void setHotelRoomUuid(java.lang.String value) {
		this.hotelRoomUuid = value;
	}
	@Column(name="hotel_room_uuid")
	public java.lang.String getHotelRoomUuid() {
		return this.hotelRoomUuid;
	}
	
		
	public void setNights(java.lang.Integer value) {
		this.nights = value;
	}
	@Column(name="nights")
	public java.lang.Integer getNights() {
		return this.nights;
	}
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
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
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	@Transient	
	public List<ActivityHotelGroupMeal> getActivityHotelGroupMealList() {
		return activityHotelGroupMealList;
	}

	public void setActivityHotelGroupMealList(
			List<ActivityHotelGroupMeal> activityHotelGroupMealList) {
		this.activityHotelGroupMealList = activityHotelGroupMealList;
	}


	
}

