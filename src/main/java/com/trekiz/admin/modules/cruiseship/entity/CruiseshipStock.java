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
@Table(name = "cruiseship_stock")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CruiseshipStock   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "CruiseshipStock";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_CRUISESHIP_INFO_UUID = "游轮UUID";
	public static final String ALIAS_SHIP_DATE = "船期";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
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
	//"游轮UUID"
	private java.lang.String cruiseshipInfoUuid;
	//"游轮名称"
	private java.lang.String cruiseshipInfoName;
	//"船期"
	private java.util.Date shipDate;
	//"批发商id"
	private java.lang.Integer wholesalerId;
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
	
	//自定义属性 add by majiancheng 2016-02-02
	private String cruiseshipStockUuid;//库存UUID，同当前对象uuid
	private List<CruiseshipStockDetail> cruiseshipCabinList;
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
	public CruiseshipStock(){
	}

	public CruiseshipStock(
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

	@Transient
	public List<CruiseshipStockDetail> getCruiseshipCabinList() {
		return cruiseshipCabinList;
	}

	public void setCruiseshipCabinList(List<CruiseshipStockDetail> cruiseshipCabinList) {
		this.cruiseshipCabinList = cruiseshipCabinList;
	}

	@Transient
	public String getCruiseshipStockUuid() {
		return cruiseshipStockUuid;
	}

	public void setCruiseshipStockUuid(String cruiseshipStockUuid) {
		this.cruiseshipStockUuid = cruiseshipStockUuid;
	}

	@Transient
	public String getCruiseshipInfoName() {
		return cruiseshipInfoName;
	}

	public void setCruiseshipInfoName(String cruiseshipInfoName) {
		this.cruiseshipInfoName = cruiseshipInfoName;
	}
	
}

