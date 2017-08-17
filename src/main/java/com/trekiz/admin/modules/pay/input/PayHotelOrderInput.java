/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PayHotelOrderInput  extends BaseInput {
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"支付方式"
	private java.lang.Integer payType;
	//"支付方式名称"
	private java.lang.String payTypeName;
	//"付款单位"
	private java.lang.String payerName;
	//"支票号"
	private java.lang.String checkNumber;
	//"开票日期"
	private java.util.Date invoiceDate;
	//"支付凭证附件id 关联docinfo表id"
	private java.lang.String payVoucher;
	//"备注信息"
	private java.lang.String remarks;
	//"POS单号"
	private java.lang.String posNo;
	//"POS机终端号"
	private java.lang.String posTagEendNo;
	//"POS机所属银行"
	private java.lang.String posBank;
	//"开户行名称"
	private java.lang.String bankName;
	//"转入行名称"
	private java.lang.String toBankNname;
	//"开户行账户"
	private java.lang.String bankAccount;
	//"转入行账号"
	private java.lang.String toBankAccount;
	//"来款单位"
	private java.lang.String fromCompanyName;
	//"来款行名称"
	private java.lang.String fromBankName;
	//"来款账户"
	private java.lang.String fromAccount;
	//"收款行名称"
	private java.lang.String receiveBankName;
	//"收款账户"
	private java.lang.String receiveAccount;
	//"汇票到期日"
	private java.util.Date draftAccountedDate;
	//"支付金额"
	private java.lang.String payPrice;
	//"支付款类型（全款、定金、尾款）"
	private Integer payPriceType;
	//"订单uuid"
	private java.lang.String orderUuid;
	//"支付金额back"
	private java.lang.String payPriceBack;
	//"isAsAccount"
	private Integer isAsAccount;
	//"改价之前的支付金额"
	private java.lang.String oldPayPrice;
	//"快速支付的付款类型"
	private java.lang.String fastPayType;
	//"订单号"
	private java.lang.String orderNum;
	//"订单流水号"
	private java.lang.String moneySerialNum;
	//"打印时间"
	private java.util.Date printTime;
	//"0表示未打印；1表示已打印"
	private Integer printFlag;
	//"0表示支付操作；1表示月结/后付费;"
	private Integer paymentStatus;
	//"游客uuid"
	private java.lang.String travelerUuid;
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
	//"达帐日期"
	private java.util.Date accountDate;
	//columns END
	private PayHotelOrder dataObj ;
	
	public PayHotelOrderInput(){
	}
	//数据库映射bean转换成表单提交bean
	public PayHotelOrderInput(PayHotelOrder obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public PayHotelOrder getPayHotelOrder() {
		dataObj = new PayHotelOrder();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	
	
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
	public void setPayType(java.lang.Integer value) {
		this.payType = value;
	}
	public java.lang.Integer getPayType() {
		return this.payType;
	}
	public void setPayTypeName(java.lang.String value) {
		this.payTypeName = value;
	}
	public java.lang.String getPayTypeName() {
		return this.payTypeName;
	}
	public void setPayerName(java.lang.String value) {
		this.payerName = value;
	}
	public java.lang.String getPayerName() {
		return this.payerName;
	}
	public void setCheckNumber(java.lang.String value) {
		this.checkNumber = value;
	}
	public java.lang.String getCheckNumber() {
		return this.checkNumber;
	}
	public void setInvoiceDate(java.util.Date value) {
		this.invoiceDate = value;
	}
	public java.util.Date getInvoiceDate() {
		return this.invoiceDate;
	}
	public void setPayVoucher(java.lang.String value) {
		this.payVoucher = value;
	}
	public java.lang.String getPayVoucher() {
		return this.payVoucher;
	}
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}
	public java.lang.String getRemarks() {
		return this.remarks;
	}
	public void setPosNo(java.lang.String value) {
		this.posNo = value;
	}
	public java.lang.String getPosNo() {
		return this.posNo;
	}
	public void setPosTagEendNo(java.lang.String value) {
		this.posTagEendNo = value;
	}
	public java.lang.String getPosTagEendNo() {
		return this.posTagEendNo;
	}
	public void setPosBank(java.lang.String value) {
		this.posBank = value;
	}
	public java.lang.String getPosBank() {
		return this.posBank;
	}
	public void setBankName(java.lang.String value) {
		this.bankName = value;
	}
	public java.lang.String getBankName() {
		return this.bankName;
	}
	public void setToBankNname(java.lang.String value) {
		this.toBankNname = value;
	}
	public java.lang.String getToBankNname() {
		return this.toBankNname;
	}
	public void setBankAccount(java.lang.String value) {
		this.bankAccount = value;
	}
	public java.lang.String getBankAccount() {
		return this.bankAccount;
	}
	public void setToBankAccount(java.lang.String value) {
		this.toBankAccount = value;
	}
	public java.lang.String getToBankAccount() {
		return this.toBankAccount;
	}
	public void setFromCompanyName(java.lang.String value) {
		this.fromCompanyName = value;
	}
	public java.lang.String getFromCompanyName() {
		return this.fromCompanyName;
	}
	public void setFromBankName(java.lang.String value) {
		this.fromBankName = value;
	}
	public java.lang.String getFromBankName() {
		return this.fromBankName;
	}
	public void setFromAccount(java.lang.String value) {
		this.fromAccount = value;
	}
	public java.lang.String getFromAccount() {
		return this.fromAccount;
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
	public void setDraftAccountedDate(java.util.Date value) {
		this.draftAccountedDate = value;
	}
	public java.util.Date getDraftAccountedDate() {
		return this.draftAccountedDate;
	}
	public void setPayPrice(java.lang.String value) {
		this.payPrice = value;
	}
	public java.lang.String getPayPrice() {
		return this.payPrice;
	}
	public void setPayPriceType(Integer value) {
		this.payPriceType = value;
	}
	public Integer getPayPriceType() {
		return this.payPriceType;
	}
	public void setOrderUuid(java.lang.String value) {
		this.orderUuid = value;
	}
	public java.lang.String getOrderUuid() {
		return this.orderUuid;
	}
	public void setPayPriceBack(java.lang.String value) {
		this.payPriceBack = value;
	}
	public java.lang.String getPayPriceBack() {
		return this.payPriceBack;
	}
	public void setIsAsAccount(Integer value) {
		this.isAsAccount = value;
	}
	public Integer getIsAsAccount() {
		return this.isAsAccount;
	}
	public void setOldPayPrice(java.lang.String value) {
		this.oldPayPrice = value;
	}
	public java.lang.String getOldPayPrice() {
		return this.oldPayPrice;
	}
	public void setFastPayType(java.lang.String value) {
		this.fastPayType = value;
	}
	public java.lang.String getFastPayType() {
		return this.fastPayType;
	}
	public void setOrderNum(java.lang.String value) {
		this.orderNum = value;
	}
	public java.lang.String getOrderNum() {
		return this.orderNum;
	}
	public void setMoneySerialNum(java.lang.String value) {
		this.moneySerialNum = value;
	}
	public java.lang.String getMoneySerialNum() {
		return this.moneySerialNum;
	}
	public void setPrintTime(java.util.Date value) {
		this.printTime = value;
	}
	public java.util.Date getPrintTime() {
		return this.printTime;
	}
	public void setPrintFlag(Integer value) {
		this.printFlag = value;
	}
	public Integer getPrintFlag() {
		return this.printFlag;
	}
	public void setPaymentStatus(Integer value) {
		this.paymentStatus = value;
	}
	public Integer getPaymentStatus() {
		return this.paymentStatus;
	}
	public void setTravelerUuid(java.lang.String value) {
		this.travelerUuid = value;
	}
	public java.lang.String getTravelerUuid() {
		return this.travelerUuid;
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
	public void setAccountDate(java.util.Date value) {
		this.accountDate = value;
	}
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}


	
}

