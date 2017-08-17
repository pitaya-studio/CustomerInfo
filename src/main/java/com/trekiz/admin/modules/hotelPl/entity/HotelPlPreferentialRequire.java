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

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_pl_preferential_require")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelPlPreferentialRequire   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelPlPreferentialRequire";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_PL_PREFERENTIAL_UUID = "酒店价单优惠信息uuid";
	public static final String ALIAS_BOOKING_NIGHTS = "起订晚数";
	public static final String ALIAS_BOOKING_NUMBERS = "起订间数";
	public static final String ALIAS_NOT_APPLICABLE_DATE = "不适用日期多个日期用“;”分隔";
	public static final String ALIAS_NOT_APPLICABLE_ROOM = "不适用房型uuid多个房型uuid用“;”分隔";
	public static final String ALIAS_APPLICABLE_THIRD_PERSON = "适用第三人（0适用，1不适用）";
	public static final String ALIAS_IS_SUPERPOSITION = "是否可以叠加（0可以，1不可以）";
	public static final String ALIAS_MEMO = "备注";
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
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"起订晚数"
	private java.lang.Integer bookingNights;
	//"起订间数"
	private java.lang.Integer bookingNumbers;
	//"不适用日期多个日期用“;”分隔"
	private java.lang.String notApplicableDate;
	//"不适用房型uuid多个房型uuid用“;”分隔"
	private java.lang.String notApplicableRoom;
	//"适用第三人（0适用，1不适用）"
	private java.lang.Integer applicableThirdPerson;
	//"是否可以叠加（0可以，1不可以）"
	private java.lang.Integer isSuperposition;
	//"备注"
	private java.lang.String memo;
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
	private String notApplicableRoomName;//不适用房型名称
	private String applicableThirdPersonText;//是否适用第三人 文本
	private String isSuperpositionText;//是否可以叠加  文本
	//end 自动报价 带出 供选择的优惠信息 结构 add by zhanghao
	
	/** 是否可以叠加（可以） */
	public static final int IS_SUPERPOSITION_YES = 0;
	/** 是否可以叠加（不可以） */
	public static final int IS_SUPERPOSITION_NO = 1;
	
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
	public HotelPlPreferentialRequire(){
	}

	public HotelPlPreferentialRequire(
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
	
		
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	@Column(name="hotel_pl_preferential_uuid")
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	
		
	public void setBookingNights(java.lang.Integer value) {
		this.bookingNights = value;
	}
	@Column(name="bookingNights")
	public java.lang.Integer getBookingNights() {
		return this.bookingNights;
	}
	
		
	public void setBookingNumbers(java.lang.Integer value) {
		this.bookingNumbers = value;
	}
	@Column(name="bookingNumbers")
	public java.lang.Integer getBookingNumbers() {
		return this.bookingNumbers;
	}
	
		
	public void setNotApplicableDate(java.lang.String value) {
		this.notApplicableDate = value;
	}
	@Column(name="notApplicable_date")
	public java.lang.String getNotApplicableDate() {
		return this.notApplicableDate;
	}
	
		
	public void setNotApplicableRoom(java.lang.String value) {
		this.notApplicableRoom = value;
	}
	@Column(name="notApplicable_room")
	public java.lang.String getNotApplicableRoom() {
		return this.notApplicableRoom;
	}
	
		
	public void setApplicableThirdPerson(java.lang.Integer value) {
		this.applicableThirdPerson = value;
	}
	@Column(name="applicable_thirdPerson")
	public java.lang.Integer getApplicableThirdPerson() {
		return this.applicableThirdPerson;
	}
	
		
	public void setIsSuperposition(java.lang.Integer value) {
		this.isSuperposition = value;
	}
	@Column(name="isSuperposition")
	public java.lang.Integer getIsSuperposition() {
		return this.isSuperposition;
	}
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
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
	public String getNotApplicableRoomName() {
		return notApplicableRoomName;
	}

	public void setNotApplicableRoomName(String notApplicableRoomName) {
		this.notApplicableRoomName = notApplicableRoomName;
	}
	@Transient	
	public String getApplicableThirdPersonText() {
		if(applicableThirdPerson!=null){
			if(applicableThirdPerson==0){
				return "适用";
			}else if(applicableThirdPerson==1){
				return "不适用";
			}
		}
		return "";
	}

	public void setApplicableThirdPersonText(String applicableThirdPersonText) {
		this.applicableThirdPersonText = applicableThirdPersonText;
	}
	@Transient	
	public String getIsSuperpositionText() {
		if(isSuperposition!=null){
			if(isSuperposition==0){
				return "可以";
			}else if(isSuperposition==1){
				return "不可以";
			}
		}
		return "";
	}

	public void setIsSuperpositionText(String isSuperpositionText) {
		this.isSuperpositionText = isSuperpositionText;
	}


	
}

