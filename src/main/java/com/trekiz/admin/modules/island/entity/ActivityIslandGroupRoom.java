/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.entity;

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
@Table(name = "activity_island_group_room")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityIslandGroupRoom   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//date formats
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activityIslandUuid;
	private java.lang.String activityIslandGroupUuid;
	private java.lang.String hotelRoomUuid;
	private java.lang.Integer nights;
	private java.lang.Integer sort;
	private java.lang.Long createBy;
	private java.lang.Long updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private java.util.Date createDate;
	
	private java.lang.String roomName;//房间名称
	private java.lang.String occupancyRate;//容住率
	//房型下总的升餐个数
	private java.lang.Integer roomMealRiseRowspan;
	private List<ActivityIslandGroupMeal> activityIslandGroupMealList; //海岛游产品团期基础餐型表
	
	
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
	public ActivityIslandGroupRoom(){
	}

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Transient
	public List<ActivityIslandGroupMeal> getActivityIslandGroupMealList() {
		return activityIslandGroupMealList;
	}

	public void setActivityIslandGroupMealList(
			List<ActivityIslandGroupMeal> activityIslandGroupMealList) {
		this.activityIslandGroupMealList = activityIslandGroupMealList;
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
	
		
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	@Column(name="activity_island_uuid")
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	
		
	public void setActivityIslandGroupUuid(java.lang.String value) {
		this.activityIslandGroupUuid = value;
	}
	@Column(name="activity_island_group_uuid")
	public java.lang.String getActivityIslandGroupUuid() {
		return this.activityIslandGroupUuid;
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
	
	@Transient	
	public java.lang.String getRoomName() {
		return roomName;
	}

	public void setRoomName(java.lang.String roomName) {
		this.roomName = roomName;
	}
	@Transient	
	public java.lang.String getOccupancyRate() {
		return occupancyRate;
	}

	public void setOccupancyRate(java.lang.String occupancyRate) {
		this.occupancyRate = occupancyRate;
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
	@Transient
	public java.lang.Integer getRoomMealRiseRowspan() {
		return roomMealRiseRowspan;
	}

	public void setRoomMealRiseRowspan(java.lang.Integer roomMealRiseRowspan) {
		this.roomMealRiseRowspan = roomMealRiseRowspan;
	}


	
}

