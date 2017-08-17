/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.entity;

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
@Table(name = "activitygroupreserve_temp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivitygroupreserveTemp   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivitygroupreserveTemp";
	public static final String ALIAS_ID = "编号";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_SRC_ACTIVITY_ID = "产品信息表ID外键";
	public static final String ALIAS_ACTIVITY_GROUP_ID = "产品团期表ID";
	public static final String ALIAS_AGENT_ID = "渠道商基本信息表id";
	public static final String ALIAS_RESERVE_TYPE = "0,定金占位；1,全款占位";
	public static final String ALIAS_PAY_RESERVE_POSITION = "切位人数";
	public static final String ALIAS_SOLD_PAY_POSITION = "已售出切位";
	public static final String ALIAS_FRONT_MONEY = "定金金额";
	public static final String ALIAS_LEFTPAY_RESERVE_POSITION = "剩余的切位人数";
	public static final String ALIAS_LEFT_FONT_MONEY = "剩余的定金金额";
	public static final String ALIAS_RESERVATION = "预订人";
	public static final String ALIAS_PAY_TYPE = "支付方式";
	public static final String ALIAS_REMARK = "切位备注";
	public static final String ALIAS_RETURN_REMARK = "还位备注";
	public static final String ALIAS_CREATE_BY = "createBy";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_BY = "updateBy";
	public static final String ALIAS_UPDATE_DATE = "updateDate";
	public static final String ALIAS_DEL_FLAG = "delFlag";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"编号"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"产品信息表ID外键"
	private java.lang.Integer srcActivityId;
	//"产品团期表ID"
	private java.lang.Integer activityGroupId;
	//"渠道商基本信息表id"
	private java.lang.Integer agentId;
	//"0,定金占位；1,全款占位"
	private Integer reserveType;
	//"切位人数"
	private java.lang.Integer payReservePosition;
	//"已售出切位"
	private java.lang.Integer soldPayPosition;
	//"定金金额"
	private Double frontMoney;
	//"剩余的切位人数"
	private java.lang.Integer leftpayReservePosition;
	//"剩余的定金金额"
	private Double leftFontMoney;
	//"预订人"
	private java.lang.String reservation;
	//"支付方式"
	private Integer payType;
	//"切位备注"
	private java.lang.String remark;
	//"还位备注"
	private java.lang.String returnRemark;
	//"createBy"
	private java.lang.Long createBy;
	//"createDate"
	private java.util.Date createDate;
	//"updateBy"
	private java.lang.Long updateBy;
	//"updateDate"
	private java.util.Date updateDate;
	//"delFlag"
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
	public ActivitygroupreserveTemp(){
	}

	public ActivitygroupreserveTemp(
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
	
		
	public void setSrcActivityId(java.lang.Integer value) {
		this.srcActivityId = value;
	}
	@Column(name="srcActivityId")
	public java.lang.Integer getSrcActivityId() {
		return this.srcActivityId;
	}
	
		
	public void setActivityGroupId(java.lang.Integer value) {
		this.activityGroupId = value;
	}
	@Column(name="activityGroupId")
	public java.lang.Integer getActivityGroupId() {
		return this.activityGroupId;
	}
	
		
	public void setAgentId(java.lang.Integer value) {
		this.agentId = value;
	}
	@Column(name="agentId")
	public java.lang.Integer getAgentId() {
		return this.agentId;
	}
	
		
	public void setReserveType(Integer value) {
		this.reserveType = value;
	}
	@Column(name="reserveType")
	public Integer getReserveType() {
		return this.reserveType;
	}
	
		
	public void setPayReservePosition(java.lang.Integer value) {
		this.payReservePosition = value;
	}
	@Column(name="payReservePosition")
	public java.lang.Integer getPayReservePosition() {
		return this.payReservePosition;
	}
	
		
	public void setSoldPayPosition(java.lang.Integer value) {
		this.soldPayPosition = value;
	}
	@Column(name="soldPayPosition")
	public java.lang.Integer getSoldPayPosition() {
		return this.soldPayPosition;
	}
	
		
	public void setFrontMoney(Double value) {
		this.frontMoney = value;
	}
	@Column(name="frontMoney")
	public Double getFrontMoney() {
		return this.frontMoney;
	}
	
		
	public void setLeftpayReservePosition(java.lang.Integer value) {
		this.leftpayReservePosition = value;
	}
	@Column(name="leftpayReservePosition")
	public java.lang.Integer getLeftpayReservePosition() {
		return this.leftpayReservePosition;
	}
	
		
	public void setLeftFontMoney(Double value) {
		this.leftFontMoney = value;
	}
	@Column(name="leftFontMoney")
	public Double getLeftFontMoney() {
		return this.leftFontMoney;
	}
	
		
	public void setReservation(java.lang.String value) {
		this.reservation = value;
	}
	@Column(name="reservation")
	public java.lang.String getReservation() {
		return this.reservation;
	}
	
		
	public void setPayType(Integer value) {
		this.payType = value;
	}
	@Column(name="payType")
	public Integer getPayType() {
		return this.payType;
	}
	
		
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	@Column(name="remark")
	public java.lang.String getRemark() {
		return this.remark;
	}
	
		
	public void setReturnRemark(java.lang.String value) {
		this.returnRemark = value;
	}
	@Column(name="returnRemark")
	public java.lang.String getReturnRemark() {
		return this.returnRemark;
	}
	
		
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Long getCreateBy() {
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
	
		
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Long getUpdateBy() {
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

