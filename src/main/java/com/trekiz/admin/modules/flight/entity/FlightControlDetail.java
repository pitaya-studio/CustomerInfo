/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.entity;

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
@Table(name = "flight_control_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
public class FlightControlDetail   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	// 提交状态（0：已提交；1：已保存草稿；2：已删除；）
	public static final int STATUS_SUBMIT_FLAG = 0;
	public static final int STATUS_DEL_FLAG = 2;
	
	//date formats
	private static final String FORMAT_DEPARTURE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String flightControlUuid;
	private java.util.Date departureDate;
	private java.lang.String spaceGradeType;
	private java.lang.String flightNumber;
	private java.lang.Integer priceCurrencyId;
	private Double price;
	private java.lang.Integer taxesCurrencyId;
	private Double taxesPrice;
	private java.lang.Integer stock;
	private java.lang.Integer sellStock;
	private java.lang.Integer preStock;
	private java.lang.String memo;
	private java.lang.Integer status;
	private java.lang.String validateFlag;
	private java.lang.Integer wholesalerId;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	private List<FlightControlHotelDetail> flightControlHotelDetails ;
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
	public FlightControlDetail(){
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
	
		
	public void setFlightControlUuid(java.lang.String value) {
		this.flightControlUuid = value;
	}
	@Column(name="flight_control_uuid")
	public java.lang.String getFlightControlUuid() {
		return this.flightControlUuid;
	}
	@Transient	
	public String getDepartureDateString() {
		if(getDepartureDate() != null) {
			return this.date2String(getDepartureDate(), FORMAT_DEPARTURE_DATE);
		} else {
			return null;
		}
	}
	public void setDepartureDateString(String value) {
		setDepartureDate(this.string2Date(value, FORMAT_DEPARTURE_DATE));
	}
	
		
	public void setDepartureDate(java.util.Date value) {
		this.departureDate = value;
	}
	@Column(name="departure_date")
	public java.util.Date getDepartureDate() {
		return this.departureDate;
	}
	
		
	public void setSpaceGradeType(java.lang.String value) {
		this.spaceGradeType = value;
	}
	@Column(name="spaceGrade_type")
	public java.lang.String getSpaceGradeType() {
		return this.spaceGradeType;
	}
	

	public void setFlightNumber(java.lang.String value) {
		this.flightNumber = value;
	}
	@Column(name="flight_number")
	public java.lang.String getFlightNumber() {
		return this.flightNumber;
	}
		
	public void setPriceCurrencyId(java.lang.Integer value) {
		this.priceCurrencyId = value;
	}
	@Column(name="price_currency_id")
	public java.lang.Integer getPriceCurrencyId() {
		return this.priceCurrencyId;
	}
	
		
	public void setPrice(Double value) {
		this.price = value;
	}
	@Column(name="price")
	public Double getPrice() {
		return this.price;
	}
	
		
	public void setTaxesCurrencyId(java.lang.Integer value) {
		this.taxesCurrencyId = value;
	}
	@Column(name="taxes_currency_id")
	public java.lang.Integer getTaxesCurrencyId() {
		return this.taxesCurrencyId;
	}
	
		
	public void setTaxesPrice(Double value) {
		this.taxesPrice = value;
	}
	@Column(name="taxes_price")
	public Double getTaxesPrice() {
		return this.taxesPrice;
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
	
		
	public void setValidateFlag(java.lang.String value) {
		this.validateFlag = value;
	}
	@Column(name="validate_flag")
	public java.lang.String getValidateFlag() {
		return this.validateFlag;
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

	@Transient
	public List<FlightControlHotelDetail> getFlightControlHotelDetails() {
		return flightControlHotelDetails;
	}

	public void setFlightControlHotelDetails(
			List<FlightControlHotelDetail> flightControlHotelDetails) {
		this.flightControlHotelDetails = flightControlHotelDetails;
	}


	
}

