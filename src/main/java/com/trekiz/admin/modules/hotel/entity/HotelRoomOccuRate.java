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
@Table(name = "hotel_room_occuRate")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelRoomOccuRate   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelRoomOccuRate";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_ROOM_UUID = "酒店房型UUID";
	public static final String ALIAS_OCCUPANCY = "入住人数";
	public static final String ALIAS_OCCUPANCY_RATE = "容住率";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_REMARK ="容住率备注";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	public static final String SPLIT_FLAG = "、";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String hotelUuid;
	private java.lang.String hotelRoomUuid;
	private java.lang.Integer occupancy;
	private java.lang.String occupancyRate;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private java.lang.String remark;
	
	private List<HotelRoomOccuRateDetail> hotelRoomOccuRateDetails;
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
	public HotelRoomOccuRate(){
	}

	public HotelRoomOccuRate(
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
	
		
	public void setOccupancy(java.lang.Integer value) {
		this.occupancy = value;
	}
	@Column(name="occupancy")
	public java.lang.Integer getOccupancy() {
		return this.occupancy;
	}
	
		
	public void setOccupancyRate(java.lang.String value) {
		this.occupancyRate = value;
	}
	@Column(name="occupancy_rate")
	public java.lang.String getOccupancyRate() {
		return this.occupancyRate;
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
	
	@Column(name="remark")
	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public void setHotelRoomOccuRateDetails(List<HotelRoomOccuRateDetail> hotelRoomOccuRateDetails) {
		this.hotelRoomOccuRateDetails = hotelRoomOccuRateDetails;
	}
	@Transient
	public List<HotelRoomOccuRateDetail> getHotelRoomOccuRateDetails() {
		return this.hotelRoomOccuRateDetails;
	}


	
}

