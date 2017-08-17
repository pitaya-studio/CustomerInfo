/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "pay_banktransfer")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PayBanktransfer   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "PayBanktransfer";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_PAY_BANK_NAME = "汇款行名称";
	public static final String ALIAS_PAY_ACCOUNT = "汇款账户";
	public static final String ALIAS_RECEIVE_COMPANY_NAME = "收款单位";
	public static final String ALIAS_RECEIVE_BANK_NAME = "收款行名称";
	public static final String ALIAS_RECEIVE_ACCOUNT = "收款账户";
	public static final String ALIAS_CREATE_BY = "创建者";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_UPDATE_BY = "更新者";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标记";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"汇款行名称"
	private java.lang.String payBankName;
	//"汇款账户"
	private java.lang.String payAccount;
	//"收款单位"
	private java.lang.String receiveCompanyName;
	//"收款行名称"
	private java.lang.String receiveBankName;
	//"收款账户"
	private java.lang.String receiveAccount;
	//"创建者"
	private java.lang.Integer createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新者"
	private java.lang.Integer updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标记"
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
	public PayBanktransfer(){
	}

	public PayBanktransfer(
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
	
		
	public void setPayBankName(java.lang.String value) {
		this.payBankName = value;
	}
	@Column(name="pay_bank_name")
	public java.lang.String getPayBankName() {
		return this.payBankName;
	}
	
		
	public void setPayAccount(java.lang.String value) {
		this.payAccount = value;
	}
	@Column(name="pay_account")
	public java.lang.String getPayAccount() {
		return this.payAccount;
	}
	
		
	public void setReceiveCompanyName(java.lang.String value) {
		this.receiveCompanyName = value;
	}
	@Column(name="receive_company_name")
	public java.lang.String getReceiveCompanyName() {
		return this.receiveCompanyName;
	}
	
		
	public void setReceiveBankName(java.lang.String value) {
		this.receiveBankName = value;
	}
	@Column(name="receive_bank_name")
	public java.lang.String getReceiveBankName() {
		return this.receiveBankName;
	}
	
		
	public void setReceiveAccount(java.lang.String value) {
		this.receiveAccount = value;
	}
	@Column(name="receive_account")
	public java.lang.String getReceiveAccount() {
		return this.receiveAccount;
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

