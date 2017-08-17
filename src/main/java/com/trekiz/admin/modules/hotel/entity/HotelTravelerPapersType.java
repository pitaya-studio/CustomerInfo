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
@Table(name = "hotel_traveler_papers_type")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelTravelerPapersType   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelTravelerPapersType";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_TRAVELER_UUID = "酒店游客uuid";
	public static final String ALIAS_ORDER_UUID = "订单uuid";
	public static final String ALIAS_PAPERS_TYPE = "证件类型1、身份证2、护照3、警官证4、军官证5、其他";
	public static final String ALIAS_VALIDITY_DATE = "有效期";
	public static final String ALIAS_ID_CARD = "证件号";
	public static final String ALIAS_ISSUE_DATE = "发证时间";
	public static final String ALIAS_ISSUE_PLACE = "发证地点";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_VALIDITY_DATE = "yyyy-MM-dd";
	public static final String FORMAT_ISSUE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店游客uuid"
	private java.lang.String hotelTravelerUuid;
	//"订单uuid"
	private java.lang.String orderUuid;
	//"证件类型1、身份证2、护照3、警官证4、军官证5、其他"
	private java.lang.Integer papersType;
	//"有效期"
	private java.util.Date validityDate;
	//"证件号"
	private java.lang.String idCard;
	//"发证时间"
	private java.util.Date issueDate;
	//"发证地点"
	private java.lang.String issuePlace;
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
	public HotelTravelerPapersType(){
	}

	public HotelTravelerPapersType(
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
	
		
	public void setHotelTravelerUuid(java.lang.String value) {
		this.hotelTravelerUuid = value;
	}
	@Column(name="hotel_traveler_uuid")
	public java.lang.String getHotelTravelerUuid() {
		return this.hotelTravelerUuid;
	}
	
		
	public void setOrderUuid(java.lang.String value) {
		this.orderUuid = value;
	}
	@Column(name="order_uuid")
	public java.lang.String getOrderUuid() {
		return this.orderUuid;
	}
	
		
	public void setPapersType(java.lang.Integer value) {
		this.papersType = value;
	}
	@Column(name="papers_type")
	public java.lang.Integer getPapersType() {
		return this.papersType;
	}
	@Transient	
	public String getValidityDateString() {
		if(getValidityDate() != null) {
			return this.date2String(getValidityDate(), FORMAT_VALIDITY_DATE);
		} else {
			return null;
		}
	}
	public void setValidityDateString(String value) {
		setValidityDate(this.string2Date(value, FORMAT_VALIDITY_DATE));
	}
	
		
	public void setValidityDate(java.util.Date value) {
		this.validityDate = value;
	}
	@Column(name="validityDate")
	public java.util.Date getValidityDate() {
		return this.validityDate;
	}
	
		
	public void setIdCard(java.lang.String value) {
		this.idCard = value;
	}
	@Column(name="idCard")
	public java.lang.String getIdCard() {
		return this.idCard;
	}
	@Transient	
	public String getIssueDateString() {
		if(getIssueDate() != null) {
			return this.date2String(getIssueDate(), FORMAT_ISSUE_DATE);
		} else {
			return null;
		}
	}
	public void setIssueDateString(String value) {
		setIssueDate(this.string2Date(value, FORMAT_ISSUE_DATE));
	}
	
		
	public void setIssueDate(java.util.Date value) {
		this.issueDate = value;
	}
	@Column(name="issueDate")
	public java.util.Date getIssueDate() {
		return this.issueDate;
	}
	
		
	public void setIssuePlace(java.lang.String value) {
		this.issuePlace = value;
	}
	@Column(name="issuePlace")
	public java.lang.String getIssuePlace() {
		return this.issuePlace;
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

