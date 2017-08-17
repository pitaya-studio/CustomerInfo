/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@Table(name = "hotel_quote_result")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuoteResult   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuoteResult";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_QUOTE_UUID = "报价表UUID";
	public static final String ALIAS_HOTEL_QUOTE_CONDITION_UUID = "绑定的报价条件UUID";
	public static final String ALIAS_PRICE = "优惠前费用";
	public static final String ALIAS_PREFERENTIAL_PRICE = "优惠后费用";
	public static final String ALIAS_PRICE_TYPE = "合计的价格类型（1、各项游客类型的费用；2、第三人费用； 3、混住费；）";
	public static final String ALIAS_TYPE_UUID = "关联的类型UUID（price_type为1时此字段存游客类型的UUID）";
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
	//"uuid"
	private java.lang.String uuid;
	//"报价表UUID"
	private java.lang.String hotelQuoteUuid;
	//"绑定的报价条件UUID"
	private java.lang.String hotelQuoteConditionUuid;
	//"优惠前费用"
	private java.lang.Double price;
	//"优惠后费用"
	private java.lang.Double preferentialPrice;
	//"合计的价格类型（1、各项游客类型的费用；2、第三人费用； 3、混住费；）"
	private java.lang.Integer priceType;
	//"关联的类型UUID（price_type为1时此字段存游客类型的UUID）"
	private java.lang.String typeUuid;
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
	//酒店价单UUID
	private java.lang.String hotelPlUuid;
	//columns END
	
	/** 价格类型（游客） */
	public static final int PRICE_TYPE_TRAVELER = 1;
	/** 价格类型（第三人） */
	public static final int PRICE_TYPE_THIRD_PEOPLE = 2;
	/** 价格类型（混住费） */
	public static final int PRICE_TYPE_MIX = 3;
	/** 价格类型（打包费） */
	public static final int PRICE_TYPE_TOTAL = 4;
	
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
	public HotelQuoteResult(){
	}

	public HotelQuoteResult(
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
	
		
	public void setHotelQuoteUuid(java.lang.String value) {
		this.hotelQuoteUuid = value;
	}
	@Column(name="hotel_quote_uuid")
	public java.lang.String getHotelQuoteUuid() {
		return this.hotelQuoteUuid;
	}
	
		
	public void setHotelQuoteConditionUuid(java.lang.String value) {
		this.hotelQuoteConditionUuid = value;
	}
	@Column(name="hotel_quote_condition_uuid")
	public java.lang.String getHotelQuoteConditionUuid() {
		return this.hotelQuoteConditionUuid;
	}
	
		
	public void setPrice(java.lang.Double value) {
		this.price = value;
	}
	@Column(name="price")
	public java.lang.Double getPrice() {
		return this.price;
	}
	
		
	public void setPreferentialPrice(java.lang.Double value) {
		this.preferentialPrice = value;
	}
	@Column(name="preferential_price")
	public java.lang.Double getPreferentialPrice() {
		return this.preferentialPrice;
	}
	
		
	public void setPriceType(java.lang.Integer value) {
		this.priceType = value;
	}
	@Column(name="price_type")
	public java.lang.Integer getPriceType() {
		return this.priceType;
	}
	
		
	public void setTypeUuid(java.lang.String value) {
		this.typeUuid = value;
	}
	@Column(name="type_uuid")
	public java.lang.String getTypeUuid() {
		return this.typeUuid;
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
	
	@Column(name="hotel_pl_uuid")
	public java.lang.String getHotelPlUuid() {
		return hotelPlUuid;
	}

	public void setHotelPlUuid(java.lang.String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}

	
}

