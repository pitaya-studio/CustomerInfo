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
@Table(name = "hotel_control_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelControlDetail   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	// 提交状态（0：已提交；1：已保存草稿；2：已删除；）
	public static final int STATUS_SUBMIT_FLAG = 0;
	public static final int STATUS_DRAFT_FLAG = 1;
	public static final int STATUS_DEL_FLAG = 2;
	public static final int STATUS_STOP = 4;
	
	//alias
	public static final String TABLE_ALIAS = "HotelControlDetail";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_CONTROL_UUID = "hotel_control表uuid";
	public static final String ALIAS_IN_DATE = "入住日期";
	public static final String ALIAS_HOTEL_MEAL = "餐型";
	public static final String ALIAS_ISLAND_WAY = "上岛方式：字典中维护";
	public static final String ALIAS_TOTAL_PRICE = "总价";
	public static final String ALIAS_CURRENCY_ID = "币种id";
	public static final String ALIAS_STOCK = "库存（间）";
	public static final String ALIAS_SELL_STOCK = "已售（间）";
	public static final String ALIAS_PRE_STOCK = "预占位（间）";
	public static final String ALIAS_VALIDATE_FLAG = "控房规则的唯一标示（md5加密后入库）";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_STATUS = "0：已保存草稿；1：已提交；2：已删除；";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String hotelControlUuid;
	private java.util.Date inDate;
	private java.lang.String hotelMeal;
	private java.lang.String islandWay;
	private java.lang.String totalPrice;
	private java.lang.Integer currencyId;
	private java.lang.Integer stock;
	private java.lang.Integer sellStock;
	private java.lang.Integer preStock;
	private java.lang.String validateFlag;
	private java.lang.String memo;
	private java.lang.Integer status;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	//columns END
	
	//明细表关联的房型记录和航空记录
	private List<HotelControlFlightDetail> flightList;
	private List<HotelControlRoomDetail> roomList;
	
	
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
	public HotelControlDetail(){
	}

	public HotelControlDetail(
		java.lang.Integer id
	){
		this.id = id;
	}

	
	@Transient		
	public List<HotelControlFlightDetail> getFlightList() {
		return flightList;
	}

	public void setFlightList(List<HotelControlFlightDetail> flightList) {
		this.flightList = flightList;
	}
	@Transient	
	public List<HotelControlRoomDetail> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<HotelControlRoomDetail> roomList) {
		this.roomList = roomList;
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
	
		
	public void setHotelMeal(java.lang.String value) {
		this.hotelMeal = value;
	}
	@Column(name="hotel_meal")
	public java.lang.String getHotelMeal() {
		return this.hotelMeal;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	
		
	public void setTotalPrice(java.lang.String value) {
		this.totalPrice = value;
	}
	@Column(name="total_price")
	public java.lang.String getTotalPrice() {
		return this.totalPrice;
	}

	
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
		
	public void setStock(java.lang.Integer value) {
		this.stock = value;
	}
	@Column(name="stock")
	public java.lang.Integer getStock() {
		return this.stock;
	}
	
		
	public void setSellStock(java.lang.Integer value) {
		this.sellStock = value;
	}
	@Column(name="sell_stock")
	public java.lang.Integer getSellStock() {
		return this.sellStock;
	}
	
		
	public void setPreStock(java.lang.Integer value) {
		this.preStock = value;
	}
	@Column(name="pre_stock")
	public java.lang.Integer getPreStock() {
		return this.preStock;
	}
	
		
	public void setValidateFlag(java.lang.String value) {
		this.validateFlag = value;
	}
	@Column(name="validate_flag")
	public java.lang.String getValidateFlag() {
		return this.validateFlag;
	}
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
	}
	
		
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.Integer getStatus() {
		return this.status;
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

