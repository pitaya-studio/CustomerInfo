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
@Table(name = "airticketreservefile_temp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AirticketreservefileTemp   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "AirticketreservefileTemp";
	public static final String ALIAS_ID = "编号";
	public static final String ALIAS_AIRTICKET_ACTIVITY_ID = "机票产品表ID外键";
	public static final String ALIAS_AGENT_ID = "渠道商基本信息表id";
	public static final String ALIAS_SRC_DOC_ID = "附件表id";
	public static final String ALIAS_FILE_NAME = "文件名称";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_UPDATE_BY = "更新人";
	public static final String ALIAS_DEL_FLAG = "删除标志";
	public static final String ALIAS_RESERVE_ORDER_ID = "reserveOrderId";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"编号"
	private java.lang.Integer id;
	//"机票产品表ID外键"
	private java.lang.Integer airticketActivityId;
	//"渠道商基本信息表id"
	private java.lang.Integer agentId;
	//"附件表id"
	private java.lang.Integer srcDocId;
	//"文件名称"
	private java.lang.String fileName;
	//切位草稿箱表uuid
	private java.lang.String reserveTempUuid;
	//"创建日期"
	private java.util.Date createDate;
	//"创建人"
	private java.lang.Integer createBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"更新人"
	private java.lang.Integer updateBy;
	//"删除标志"
	private java.lang.String delFlag;
	//"reserveOrderId"
	private java.lang.Integer reserveOrderId;
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
	public AirticketreservefileTemp(){
	}

	public AirticketreservefileTemp(
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
	
		
	public void setAirticketActivityId(java.lang.Integer value) {
		this.airticketActivityId = value;
	}
	@Column(name="airticketActivityId")
	public java.lang.Integer getAirticketActivityId() {
		return this.airticketActivityId;
	}
	
		
	public void setAgentId(java.lang.Integer value) {
		this.agentId = value;
	}
	@Column(name="agentId")
	public java.lang.Integer getAgentId() {
		return this.agentId;
	}
	
		
	public void setSrcDocId(java.lang.Integer value) {
		this.srcDocId = value;
	}
	@Column(name="srcDocId")
	public java.lang.Integer getSrcDocId() {
		return this.srcDocId;
	}
	
		
	public void setFileName(java.lang.String value) {
		this.fileName = value;
	}
	@Column(name="fileName")
	public java.lang.String getFileName() {
		return this.fileName;
	}
	
	@Column(name="reserve_temp_uuid")
	public java.lang.String getReserveTempUuid() {
		return reserveTempUuid;
	}

	public void setReserveTempUuid(java.lang.String reserveTempUuid) {
		this.reserveTempUuid = reserveTempUuid;
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
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
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
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	
		
	public void setReserveOrderId(java.lang.Integer value) {
		this.reserveOrderId = value;
	}
	@Column(name="reserveOrderId")
	public java.lang.Integer getReserveOrderId() {
		return this.reserveOrderId;
	}


	
}

