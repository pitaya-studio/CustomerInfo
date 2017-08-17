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
@Table(name = "hotel_room")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelRoom   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelRoom";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_ROOM_NAME = "房型名称";
	public static final String ALIAS_SHOW_NAME = "展示名称";
	public static final String ALIAS_OCCUPANCY_RATE = "容住率";
	public static final String ALIAS_BED = "床型名称(字典)";
	public static final String ALIAS_FLOOR = "楼层(字典)";
	public static final String ALIAS_EXTRA_BED_NUM = "加床数量";
	public static final String ALIAS_EXTRA_BED_COST = "加床费用";
	public static final String ALIAS_EXTRA_BED_CUSTOMER = "加床顾客类型";
	public static final String ALIAS_ROOM_AREA = "房间面积";
	public static final String ALIAS_ROOM_FEATURES = "房型特色（字典）";
	public static final String ALIAS_SORT = "排序";
	public static final String ALIAS_IN_DATE = "入住时间";
	public static final String ALIAS_OUT_DATE = "离店时间";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_REMARK="酒店房型备注";
	public static final String ALIAS_ROOM_NUMB="房间数";
	//date formats
	public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_OUT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String hotelUuid;
	private java.lang.String roomName;
	private java.lang.String showName;
	private java.lang.String occupancyRate;
	private java.lang.String bed;
	private java.lang.String floor;
	private java.lang.Integer extraBedNum;
	private java.lang.Double extraBedCost;
	private java.lang.String extraBedCustomer;
	private Double roomArea;
	private java.lang.String roomFeatures;
	private java.lang.Integer sort;
	private java.util.Date inDate;
	private java.util.Date outDate;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private java.lang.Integer wholesalerId;
    private java.lang.String remark;
    private java.lang.String roomNumb;
	//columns END
	//自定义属性
	/** 房型绑定的基础餐型  */
	private List<HotelRoomMeal> hotelRoomMeals;
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
	public HotelRoom(){
	}

	public HotelRoom(
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
	
		
	public void setRoomName(java.lang.String value) {
		this.roomName = value;
	}
	@Column(name="room_name")
	public java.lang.String getRoomName() {
		return this.roomName;
	}
	
	public void setShowName(java.lang.String value) {
		this.showName = value;
	}
	@Column(name="show_name")
	public java.lang.String getShowName() {
		return this.showName;
	}
	
	public void setOccupancyRate(java.lang.String value) {
		this.occupancyRate = value;
	}
	@Column(name="occupancy_rate")
	public java.lang.String getOccupancyRate() {
		return this.occupancyRate;
	}
	
		
	public void setBed(java.lang.String value) {
		this.bed = value;
	}
	@Column(name="bed")
	public java.lang.String getBed() {
		return this.bed;
	}
	
		
	public void setFloor(java.lang.String value) {
		this.floor = value;
	}
	@Column(name="floor")
	public java.lang.String getFloor() {
		return this.floor;
	}
	
		
	public void setExtraBedNum(java.lang.Integer value) {
		this.extraBedNum = value;
	}
	@Column(name="extra_bed_num")
	public java.lang.Integer getExtraBedNum() {
		return this.extraBedNum;
	}
	
		
	public void setExtraBedCost(java.lang.Double value) {
		this.extraBedCost = value;
	}
	@Column(name="extra_bed_cost")
	public java.lang.Double getExtraBedCost() {
		return this.extraBedCost;
	}
	
		
	public void setExtraBedCustomer(java.lang.String value) {
		this.extraBedCustomer = value;
	}
	@Column(name="extra_bed_customer")
	public java.lang.String getExtraBedCustomer() {
		return this.extraBedCustomer;
	}
	
		
	public void setRoomArea(Double value) {
		this.roomArea = value;
	}
	@Column(name="room_area")
	public Double getRoomArea() {
		return this.roomArea;
	}
	
		
	public void setRoomFeatures(java.lang.String value) {
		this.roomFeatures = value;
	}
	@Column(name="room_features")
	public java.lang.String getRoomFeatures() {
		return this.roomFeatures;
	}
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
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
	
	@Column(name="remark")
	public java.lang.String getRemark() {
		return remark;
	}
	
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	@Column(name="room_numb")
	public java.lang.String getRoomNumb() {
		return roomNumb;
	}

	public void setRoomNumb(java.lang.String roomNumb) {
		this.roomNumb = roomNumb;
	}

	@Transient
	public List<HotelRoomMeal> getHotelRoomMeals() {
		return hotelRoomMeals;
	}

	public void setHotelRoomMeals(List<HotelRoomMeal> hotelRoomMeals) {
		this.hotelRoomMeals = hotelRoomMeals;
	}


	
}

