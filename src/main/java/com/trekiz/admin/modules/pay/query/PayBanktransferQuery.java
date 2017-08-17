/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class PayBanktransferQuery  {
	
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String payBankName;
	private java.lang.String payAccount;
	private java.lang.String receiveCompanyName;
	private java.lang.String receiveBankName;
	private java.lang.String receiveAccount;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	public java.lang.String getUuid() {
		return this.uuid;
	}
	public void setPayBankName(java.lang.String value) {
		this.payBankName = value;
	}
	public java.lang.String getPayBankName() {
		return this.payBankName;
	}
	public void setPayAccount(java.lang.String value) {
		this.payAccount = value;
	}
	public java.lang.String getPayAccount() {
		return this.payAccount;
	}
	public void setReceiveCompanyName(java.lang.String value) {
		this.receiveCompanyName = value;
	}
	public java.lang.String getReceiveCompanyName() {
		return this.receiveCompanyName;
	}
	public void setReceiveBankName(java.lang.String value) {
		this.receiveBankName = value;
	}
	public java.lang.String getReceiveBankName() {
		return this.receiveBankName;
	}
	public void setReceiveAccount(java.lang.String value) {
		this.receiveAccount = value;
	}
	public java.lang.String getReceiveAccount() {
		return this.receiveAccount;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

