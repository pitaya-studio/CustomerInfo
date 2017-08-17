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
@Table(name = "cruiseship_stock_order")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CruiseshipStockOrder   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "CruiseshipStockOrder";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_ACTIVITY_ID = "产品表ID";
	public static final String ALIAS_ACTIVITY_TYPE = "产品团期表类型（1：activitygroup表；）";
	public static final String ALIAS_ACTIVITY_NAME = "产品名称";
	public static final String ALIAS_DEPARTURE_CITY_ID = "产品出发地ID";
	public static final String ALIAS_DEPARTURE_CITY_NAME = "产品出发地名称";
	public static final String ALIAS_CRUISESHIP_STOCK_UUID = "库存UUID";
	public static final String ALIAS_CRUISESHIP_STOCK_DETAIL_UUID = "库存明细UUID";
	public static final String ALIAS_CRUISESHIP_CABIN_NAME = "舱位名称";
	public static final String ALIAS_SEX = "性别（女：F，男：M）";
	public static final String ALIAS_FNUM = "女人数";
	public static final String ALIAS_MNUM = "男人数";
	public static final String ALIAS_FPIECE = "女拼（拼：0；不拼：1；）";
	public static final String ALIAS_MPIECE = "男拼（拼：0；不拼：1；）";
	public static final String ALIAS_ALL_NUM = "总人数";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
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
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"产品表ID"
	private java.lang.Integer activityId;
	//"产品团期表类型（1：activitygroup表；）"
	private java.lang.Integer activityType;
	//"产品名称"
	private java.lang.String activityName;
	//"产品出发地ID"
	private java.lang.Integer departureCityId;
	//"产品出发地名称"
	private java.lang.String departureCityName;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"库存明细UUID"
	private java.lang.String cruiseshipStockDetailUuid;
	//"舱位名称"
	private java.lang.String cruiseshipCabinName;
	//"性别（女：F，男：M）"
	private java.lang.String sex;
	//"女人数"
	private java.lang.Integer fnum;
	//"男人数"
	private java.lang.Integer mnum;
	//"女拼（拼：0；不拼：1；）"
	private java.lang.Integer fpiece;
	//"男拼（拼：0；不拼：1；）"
	private java.lang.Integer mpiece;
	//"总人数"
	private java.lang.Integer allNum;
	//"批发商id"
	private java.lang.Integer wholesalerId;
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
	
	/** 产品团期表类型-单团类 */
	public static final int ACTIVITY_TYPE_SINGLE = 1;
	
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
	public CruiseshipStockOrder(){
	}

	public CruiseshipStockOrder(
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
	
		
	public void setActivityId(java.lang.Integer value) {
		this.activityId = value;
	}
	@Column(name="activity_id")
	public java.lang.Integer getActivityId() {
		return this.activityId;
	}
	
		
	public void setActivityType(java.lang.Integer value) {
		this.activityType = value;
	}
	@Column(name="activity_type")
	public java.lang.Integer getActivityType() {
		return this.activityType;
	}
	
		
	public void setActivityName(java.lang.String value) {
		this.activityName = value;
	}
	@Column(name="activity_name")
	public java.lang.String getActivityName() {
		return this.activityName;
	}
	
		
	public void setDepartureCityId(java.lang.Integer value) {
		this.departureCityId = value;
	}
	@Column(name="departureCityId")
	public java.lang.Integer getDepartureCityId() {
		return this.departureCityId;
	}
	
		
	public void setDepartureCityName(java.lang.String value) {
		this.departureCityName = value;
	}
	@Column(name="departureCityName")
	public java.lang.String getDepartureCityName() {
		return this.departureCityName;
	}
	
		
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	@Column(name="cruiseship_stock_uuid")
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
	}
	
		
	public void setCruiseshipStockDetailUuid(java.lang.String value) {
		this.cruiseshipStockDetailUuid = value;
	}
	@Column(name="cruiseship_stock_detail_uuid")
	public java.lang.String getCruiseshipStockDetailUuid() {
		return this.cruiseshipStockDetailUuid;
	}
	
		
	public void setCruiseshipCabinName(java.lang.String value) {
		this.cruiseshipCabinName = value;
	}
	@Column(name="cruiseship_cabin_name")
	public java.lang.String getCruiseshipCabinName() {
		return this.cruiseshipCabinName;
	}
	
		
	public void setSex(java.lang.String value) {
		this.sex = value;
	}
	@Column(name="sex")
	public java.lang.String getSex() {
		return this.sex;
	}
	
		
	public void setFnum(java.lang.Integer value) {
		this.fnum = value;
	}
	@Column(name="f_num")
	public java.lang.Integer getFnum() {
		return this.fnum;
	}
	
		
	public void setMnum(java.lang.Integer value) {
		this.mnum = value;
	}
	@Column(name="m_num")
	public java.lang.Integer getMnum() {
		return this.mnum;
	}
	
		
	public void setFpiece(java.lang.Integer value) {
		this.fpiece = value;
	}
	@Column(name="f_piece")
	public java.lang.Integer getFpiece() {
		return this.fpiece;
	}
	
		
	public void setMpiece(java.lang.Integer value) {
		this.mpiece = value;
	}
	@Column(name="m_piece")
	public java.lang.Integer getMpiece() {
		return this.mpiece;
	}
	
		
	public void setAllNum(java.lang.Integer value) {
		this.allNum = value;
	}
	@Column(name="all_num")
	public java.lang.Integer getAllNum() {
		return this.allNum;
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

