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
import java.util.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_control")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelControl   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelControl";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_NAME = "控房单名称";
	public static final String ALIAS_COUNTRY = "国家：地理信息UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿表UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店表UUID";
	public static final String ALIAS_HOTEL_GROUP = "酒店集团字典表";
	public static final String ALIAS_GROUND_SUPPLIER = "地接供应商";
	public static final String ALIAS_PURCHASE_TYPE = "采购类型（0内采1外采）";
	public static final String ALIAS_CURRENCY_ID = "币种id";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String name;
	private java.lang.String country;
	private java.lang.String islandUuid;
	private java.lang.String hotelUuid;
	private java.lang.String hotelGroup;
	private java.lang.Integer groundSupplier;
	private java.lang.Integer purchaseType;
	private java.lang.Integer currencyId;
	private java.lang.Integer wholesalerId;
	private java.lang.String memo;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	//columns END
	
	//控房记录明细表集合
	private List<HotelControlDetail> detailList;
	
	
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
	public HotelControl(){
	}

	public HotelControl(
		java.lang.Integer id
	){
		this.id = id;
	}

	
	@Transient	
	public List<HotelControlDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<HotelControlDetail> detailList) {
		this.detailList = detailList;
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
	
	
	@Column(name="name")	
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}

	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	@Column(name="country")
	public java.lang.String getCountry() {
		return this.country;
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
	
		
	public void setHotelGroup(java.lang.String value) {
		this.hotelGroup = value;
	}
	@Column(name="hotel_group")
	public java.lang.String getHotelGroup() {
		return this.hotelGroup;
	}

	public void setGroundSupplier(java.lang.Integer value) {
		this.groundSupplier = value;
	}
	@Column(name="ground_supplier")
	public java.lang.Integer getGroundSupplier() {
		return this.groundSupplier;
	}
	
		
	public void setPurchaseType(java.lang.Integer value) {
		this.purchaseType = value;
	}
	@Column(name="purchase_type")
	public java.lang.Integer getPurchaseType() {
		return this.purchaseType;
	}
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
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


	
}

