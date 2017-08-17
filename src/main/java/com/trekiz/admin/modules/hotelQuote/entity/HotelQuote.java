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
@Table(name = "hotel_quote")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuote   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuote";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_USER_ID = "销售人员ID ";
	public static final String ALIAS_QUOTE_TYPE = "报价类型（1、直客；2、同行；3、其他）";
	public static final String ALIAS_QUOTE_OBJECT = "报价对象（报价类型quote_type等于2时对应渠道ID，等于3时对应客户姓名）";
	public static final String ALIAS_LINK_NAME = "联系人名称 ";
	public static final String ALIAS_LINK_PHONE = "联系电话";
	public static final String ALIAS_CURRENCY_ID = "币种id";
	public static final String ALIAS_MEMO = "备注";
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
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"销售人员ID "
	private java.lang.Integer userId;
	//"报价类型（1、直客；2、同行；3、其他）"
	private java.lang.Integer quoteType;
	//"报价对象（报价类型quote_type等于2时对应渠道ID，等于3时对应客户姓名）"
	private java.lang.String quoteObject;
	//"联系人名称 "
	private java.lang.String linkName;
	//"联系电话"
	private java.lang.String linkPhone;
	//"币种id"
	private java.lang.Integer currencyId;
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
	public HotelQuote(){
	}

	public HotelQuote(
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
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
		
	public void setUserId(java.lang.Integer value) {
		this.userId = value;
	}
	@Column(name="user_id")
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	
		
	public void setQuoteType(java.lang.Integer value) {
		this.quoteType = value;
	}
	@Column(name="quote_type")
	public java.lang.Integer getQuoteType() {
		return this.quoteType;
	}
	
		
	public void setQuoteObject(java.lang.String value) {
		this.quoteObject = value;
	}
	@Column(name="quote_object")
	public java.lang.String getQuoteObject() {
		return this.quoteObject;
	}
	
		
	public void setLinkName(java.lang.String value) {
		this.linkName = value;
	}
	@Column(name="link_name")
	public java.lang.String getLinkName() {
		return this.linkName;
	}
	
		
	public void setLinkPhone(java.lang.String value) {
		this.linkPhone = value;
	}
	@Column(name="link_phone")
	public java.lang.String getLinkPhone() {
		return this.linkPhone;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
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

