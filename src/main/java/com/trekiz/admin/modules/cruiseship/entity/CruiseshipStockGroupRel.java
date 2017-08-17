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
import java.util.List;
import java.util.Map;

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
@Table(name = "cruiseship_stock_group_rel")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CruiseshipStockGroupRel   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "CruiseshipStockGroupRel";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_ACTIVITY_ID = "产品ID";
	public static final String ALIAS_ACTIVITYGROUP_ID = "产品团期表ID";
	public static final String ALIAS_ACTIVITY_TYPE = "产品团期表类型（1：activitygroup表；）";
	public static final String ALIAS_CRUISESHIP_STOCK_UUID = "库存UUID";
	public static final String ALIAS_STATUS = "关联状态（0：已关联；1：未关联）";
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
	//"产品ID"
	private java.lang.Integer activityId;
	//"产品团期表ID"
	private java.lang.Integer activitygroupId;
	//"产品团期表类型（1：activitygroup表；）"
	private java.lang.Integer activityType;
	//"库存UUID"
	private java.lang.String cruiseshipStockUuid;
	//"关联状态（0：已关联；1：未关联）"
	private java.lang.Integer status;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"批发商id"
	private java.lang.Integer cruiseshipStockDetailId;
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

	
	private java.lang.String fromArea;
	
	
	//自定义属性 add by majiancheng 2016-02-02
	private String activityName;//产品名称
	//自定义属性 add by majiancheng 2016-02-02
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
	public CruiseshipStockGroupRel(){
	}

	public CruiseshipStockGroupRel(
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
	
		
	public void setActivitygroupId(java.lang.Integer value) {
		this.activitygroupId = value;
	}
	@Column(name="activitygroup_id")
	public java.lang.Integer getActivitygroupId() {
		return this.activitygroupId;
	}
	
		
	public void setActivityType(java.lang.Integer value) {
		this.activityType = value;
	}
	@Column(name="activity_type")
	public java.lang.Integer getActivityType() {
		return this.activityType;
	}
	
		
	public void setCruiseshipStockUuid(java.lang.String value) {
		this.cruiseshipStockUuid = value;
	}
	@Column(name="cruiseship_stock_uuid")
	public java.lang.String getCruiseshipStockUuid() {
		return this.cruiseshipStockUuid;
	}
	
		
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	@Column(name="STATUS")
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
	
	@Column(name="cruiseship_stock_detail_id")	
	public java.lang.Integer getCruiseshipStockDetailId() {
		return cruiseshipStockDetailId;
	}

	public void setCruiseshipStockDetailId(java.lang.Integer cruiseshipStockDetailId) {
		this.cruiseshipStockDetailId = cruiseshipStockDetailId;
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
	public java.lang.String getFromArea() {
		return fromArea;
	}

	public void setFromArea(java.lang.String fromArea) {
		this.fromArea = fromArea;
	}

	@Transient
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
 
}

