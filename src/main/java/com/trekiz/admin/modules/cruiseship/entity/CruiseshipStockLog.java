/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.entity;

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
@Table(name = "cruiseship_stock_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CruiseshipStockLog   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "CruiseshipStockLog";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_CRUISESHIP_STOCK_UUID = "库存UUID";
	public static final String ALIAS_CRUISESHIP_INFO_UUID = "游轮UUID";
	public static final String ALIAS_SHIP_DATE = "船期";
	public static final String ALIAS_CRUISESHIP_CABIN_UUID = "舱型UUID";
	public static final String ALIAS_STOCK_AMOUNT = "修改前库存";
	public static final String ALIAS_FREE_POSITION = "修改前余位";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_OPERATE_SOURCE = "操作源。1：库存；2：余位；";
	public static final String ALIAS_OPERATE_TYPE = "操作类型。1：增加；2：减少；";
	public static final String ALIAS_OPERATE_NUM = "操作数量。";
	public static final String ALIAS_STOCK_AMOUNT_AFTER = "修改后库存";
	public static final String ALIAS_FREE_POSITION_AFTER = "修改后余位";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_SHIP_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"游轮UUID"
	private java.lang.String cruiseshipInfoUuid;
	//"船期"
	private java.util.Date shipDate;
	//"舱型UUID"
	private java.lang.String cruiseshipCabinUuid;
	//"修改前库存"
	private java.lang.Integer stockAmount;
	//"修改前余位"
	private java.lang.Integer freePosition;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"操作源。1：库存；2：余位；"
	private java.lang.Integer operateSource;
	//"操作类型。1：增加；2：减少；"
	private java.lang.Integer operateType;
	//"操作数量。"
	private java.lang.Integer operateNum;
	//"修改后库存"
	private java.lang.Integer stockAmountAfter;
	//"修改后余位"
	private java.lang.Integer freePositionAfter;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除状态"
	private java.lang.String delFlag;
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
	public CruiseshipStockLog(){
	}

	public CruiseshipStockLog(
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
	@Column(name="UUID")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	@Column(name="cruiseship_stock_uuid")
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
	}
	
		
	public void setCruiseshipInfoUuid(java.lang.String value) {
		this.cruiseshipInfoUuid = value;
	}
	@Column(name="cruiseship_info_uuid")
	public java.lang.String getCruiseshipInfoUuid() {
		return this.cruiseshipInfoUuid;
	}
	@Transient	
	public String getShipDateString() {
		if(getShipDate() != null) {
			return this.date2String(getShipDate(), FORMAT_SHIP_DATE);
		} else {
			return null;
		}
	}
	public void setShipDateString(String value) {
		setShipDate(this.string2Date(value, FORMAT_SHIP_DATE));
	}
	
		
	public void setShipDate(java.util.Date value) {
		this.shipDate = value;
	}
	@Column(name="ship_date")
	public java.util.Date getShipDate() {
		return this.shipDate;
	}
	
		
	public void setCruiseshipCabinUuid(java.lang.String value) {
		this.cruiseshipCabinUuid = value;
	}
	@Column(name="cruiseship_cabin_uuid")
	public java.lang.String getCruiseshipCabinUuid() {
		return this.cruiseshipCabinUuid;
	}
	
		
	public void setStockAmount(java.lang.Integer value) {
		this.stockAmount = value;
	}
	@Column(name="stock_amount")
	public java.lang.Integer getStockAmount() {
		return this.stockAmount;
	}
	
		
	public void setFreePosition(java.lang.Integer value) {
		this.freePosition = value;
	}
	@Column(name="free_position")
	public java.lang.Integer getFreePosition() {
		return this.freePosition;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
		
	public void setOperateSource(java.lang.Integer value) {
		this.operateSource = value;
	}
	@Column(name="operate_source")
	public java.lang.Integer getOperateSource() {
		return this.operateSource;
	}
	
		
	public void setOperateType(java.lang.Integer value) {
		this.operateType = value;
	}
	@Column(name="operate_type")
	public java.lang.Integer getOperateType() {
		return this.operateType;
	}
	
		
	public void setOperateNum(java.lang.Integer value) {
		this.operateNum = value;
	}
	@Column(name="operate_num")
	public java.lang.Integer getOperateNum() {
		return this.operateNum;
	}
	
		
	public void setStockAmountAfter(java.lang.Integer value) {
		this.stockAmountAfter = value;
	}
	@Column(name="stock_amount_after")
	public java.lang.Integer getStockAmountAfter() {
		return this.stockAmountAfter;
	}
	
		
	public void setFreePositionAfter(java.lang.Integer value) {
		this.freePositionAfter = value;
	}
	@Column(name="free_position_after")
	public java.lang.Integer getFreePositionAfter() {
		return this.freePositionAfter;
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

