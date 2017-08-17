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
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotel.entity.HotelMeal;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_pl_preferential_room")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelPlPreferentialRoom   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelPlPreferentialRoom";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_HOTEL_PL_PREFERENTIAL_UUID = "酒店价单优惠信息uuid";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID（当前价单的酒店或者关联的批发商下的酒店）";
	public static final String ALIAS_HOTEL_ROOM_UUID = "酒店房型UUID";
	public static final String ALIAS_NIGHTS = "晚数";
	public static final String ALIAS_HOTEL_MEAL_UUIDS = "酒店餐型uuid（数据源是酒店房型餐型关联表读取多个用；分隔）";
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
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"酒店UUID（当前价单的酒店或者关联的批发商下的酒店）"
	private java.lang.String hotelUuid;
	//"酒店房型UUID"
	private java.lang.String hotelRoomUuid;
	//"晚数"
	private java.lang.Integer nights;
	//"酒店餐型uuid（数据源是酒店房型餐型关联表读取多个用；分隔）"
	private java.lang.String hotelMealUuids;
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
	
	//start 自动报价 带出 供选择的优惠信息 结构 add by zhanghao
	private String hotelRoomText;//房型名称 输出
	private String roomOccupancyRate;//房型容住率
	private String relHotelName;//关联的酒店名称
	private List<HotelMeal> hotelMealList;//基础餐型
	private String hotelText;//酒店显示文本
	//end 自动报价 带出 供选择的优惠信息 结构 add by zhanghao
	
	
	
	/*private String date2String(Date date,String format){
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
	}*/
	public HotelPlPreferentialRoom(){
	}

	public HotelPlPreferentialRoom(
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
	
		
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	@Column(name="hotel_pl_preferential_uuid")
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
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
	
		
	public void setNights(java.lang.Integer value) {
		this.nights = value;
	}
	@Column(name="nights")
	public java.lang.Integer getNights() {
		return this.nights;
	}
	
		
	public void setHotelMealUuids(java.lang.String value) {
		this.hotelMealUuids = value;
	}
	@Column(name="hotel_meal_uuids")
	public java.lang.String getHotelMealUuids() {
		return this.hotelMealUuids;
	}
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	/*@Transient	
	public String getStringreateDate() {
		if(getCreateDate() != null) {
			return this.date2String(getCreateDate(), FORMAT_CREATE_DATE);
		} else {
			return null;
		}
	}
	public void setStringreateDate(String value) {
		setCreateDate(this.string2Date(value, FORMAT_CREATE_DATE));
	}*/
	
		
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
	/*@Transient	
	public String getStringpdateDate() {
		if(getUpdateDate() != null) {
			return this.date2String(getUpdateDate(), FORMAT_UPDATE_DATE);
		} else {
			return null;
		}
	}
	public void setStringpdateDate(String value) {
		setUpdateDate(this.string2Date(value, FORMAT_UPDATE_DATE));
	}
	*/
		
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
	public String getRoomOccupancyRate() {
		return roomOccupancyRate;
	}

	public void setRoomOccupancyRate(String roomOccupancyRate) {
		this.roomOccupancyRate = roomOccupancyRate;
	}
	@Transient	
	public String getRelHotelName() {
		return relHotelName;
	}

	public void setRelHotelName(String relHotelName) {
		this.relHotelName = relHotelName;
	}
	@Transient	
	public List<HotelMeal> getHotelMealList() {
		return hotelMealList;
	}

	public void setHotelMealList(List<HotelMeal> hotelMealList) {
		this.hotelMealList = hotelMealList;
	}

	@Transient	
	public String getHotelRoomText() {
		return hotelRoomText;
	}

	public void setHotelRoomText(String hotelRoomText) {
		this.hotelRoomText = hotelRoomText;
	}


	@Transient	
	public String getHotelText() {
		return hotelText;
	}

	public void setHotelText(String hotelText) {
		this.hotelText = hotelText;
	}
}

