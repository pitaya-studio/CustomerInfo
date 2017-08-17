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
@Table(name = "pay_group")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PayGroup   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "PayGroup";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_PAY_TYPE = "支付方式";
	public static final String ALIAS_PAY_TYPE_NAME = "支付方式名称";
	public static final String ALIAS_PAYER_NAME = "付款单位";
	public static final String ALIAS_CHECK_NUMBER = "支票号";
	public static final String ALIAS_INVOICE_DATE = "开票日期";
	public static final String ALIAS_PAY_VOUCHER = "支付凭证附件id 关联docinfo表id";
	public static final String ALIAS_REMARKS = "备注信息";
	public static final String ALIAS_POS_NO = "POS单号";
	public static final String ALIAS_POS_TAG_EEND_NO = "POS机终端号";
	public static final String ALIAS_POS_BANK = "POS机所属银行";
	public static final String ALIAS_BANK_NAME = "开户行名称";
	public static final String ALIAS_TO_BANK_NNAME = "转入行名称";
	public static final String ALIAS_BANK_ACCOUNT = "开户行账户";
	public static final String ALIAS_TO_BANK_ACCOUNT = "转入行账号";
	public static final String ALIAS_FROM_COMPANY_NAME = "来款单位";
	public static final String ALIAS_FROM_BANK_NAME = "来款行名称";
	public static final String ALIAS_FROM_ACCOUNT = "来款账户";
	public static final String ALIAS_RECEIVE_BANK_NAME = "收款行名称";
	public static final String ALIAS_RECEIVE_ACCOUNT = "收款账户";
	public static final String ALIAS_DRAFT_ACCOUNTED_DATE = "汇票到期日";
	public static final String ALIAS_PAY_PRICE = "支付金额";
	public static final String ALIAS_PAY_PRICE_TYPE = "支付款类型（全款、定金、尾款）";
	public static final String ALIAS_GROUP_ID = "团期id（目前单团存团期id，机票、签证存产品id）";
	public static final String ALIAS_CREATE_BY = "创建者";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_UPDATE_BY = "更新者";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标记";
	public static final String ALIAS_PAY_PRICE_BACK = "支付金额back";
	public static final String ALIAS_IS_AS_ACCOUNT = "是否达账";
	public static final String ALIAS_OLD_PAY_PRICE = "改价之前的支付金额";
	public static final String ALIAS_ORDER_TYPE = "订单类型";
	public static final String ALIAS_FAST_PAY_TYPE = "快速支付的付款类型";
	public static final String ALIAS_PRINT_TIME = "打印时间";
	public static final String ALIAS_PRINT_FLAG = "0表示未打印；1表示已打印";
	public static final String ALIAS_PAYMENT_STATUS = "0表示支付操作；1表示月结/后付费;";
	public static final String ALIAS_ACCOUNT_DATE = "银行到账日期";
	public static final String ALIAS_COST_RECORD_ID = "团期成本ID";
	
	//date formats
	public static final String FORMAT_INVOICE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DRAFT_ACCOUNTED_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_PRINT_TIME = "yyyy-MM-dd";
	public static final String FORMAT_ACCOUNT_DATE = "yyyy-MM-dd";
	
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private Integer id;
	//"UUID"
	private String uuid;
	//"支付方式"
	private Integer payType;
	//"支付方式名称"
	private String payTypeName;
	//"付款单位"
	private String payerName;
	//"支票号"
	private String checkNumber;
	//"开票日期"
	private Date invoiceDate;
	//"支付凭证附件id 关联docinfo表id"
	private String payVoucher;
	//"备注信息"
	private String remarks;
	//"POS单号"
	private String posNo;
	//"POS机终端号"
	private String posTagEendNo;
	//"POS机所属银行"
	private String posBank;
	//"开户行名称"
	private String bankName;
	//"转入行名称"
	private String toBankNname;
	//"开户行账户"
	private String bankAccount;
	//"转入行账号"
	private String toBankAccount;
	//"来款单位"
	private String fromCompanyName;
	//"来款行名称"
	private String fromBankName;
	//"来款账户"
	private String fromAccount;
	//"收款行名称"
	private String receiveBankName;
	//"收款账户"
	private String receiveAccount;
	//"汇票到期日"
	private Date draftAccountedDate;
	//"支付金额"
	private String payPrice;
	//"支付款类型（全款、定金、尾款）"
	private Integer payPriceType;
	//"团期id（目前单团存团期id，机票、签证存产品id）"
	private Integer groupId;
	//"创建者"
	private Integer createBy;
	//"创建日期"
	private Date createDate;
	//"更新者"
	private Integer updateBy;
	//"更新日期"
	private Date updateDate;
	//"删除标记"
	private String delFlag;
	//"支付金额back"
	private String payPriceBack;
	//"是否达账"
	private Integer isAsAccount;
	//"改价之前的支付金额"
	private String oldPayPrice;
	//"订单类型"
	private Integer orderType;
	//"快速支付的付款类型"
	private String fastPayType;
	//"打印时间"
	private Date printTime;
	//"0表示未打印；1表示已打印"
	private Integer printFlag;
	//"0表示支付操作；1表示月结/后付费;"
	private Integer paymentStatus;
	//"银行到账日期"
	private Date accountDate;
	//收款确认日期
	private Date receiptConfirmationDate;
	//"团期成本ID"
	private Integer costRecordId;
	//驳回备注
	private String rejectReason;
	//支付宝名称（来款）
	private String fromAlipayName;
	//支付宝账号（来款）
	private String fromAlipayAccount;
	//支付宝名称（收款）
	private String toAlipayName;
	//支付宝账号（收款）
	private String toAlipayAccount;
	//收款单位
	private String comeOfficeName;
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
	public PayGroup(){
	}

	public PayGroup(
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
	
		
	public void setPayType(java.lang.Integer value) {
		this.payType = value;
	}
	@Column(name="payType")
	public java.lang.Integer getPayType() {
		return this.payType;
	}
	
		
	public void setPayTypeName(java.lang.String value) {
		this.payTypeName = value;
	}
	@Column(name="payTypeName")
	public java.lang.String getPayTypeName() {
		return this.payTypeName;
	}
	
		
	public void setPayerName(java.lang.String value) {
		this.payerName = value;
	}
	@Column(name="payerName")
	public java.lang.String getPayerName() {
		return this.payerName;
	}
	
		
	public void setCheckNumber(java.lang.String value) {
		this.checkNumber = value;
	}
	@Column(name="checkNumber")
	public java.lang.String getCheckNumber() {
		return this.checkNumber;
	}
	@Transient	
	public String getInvoiceDateString() {
		if(getInvoiceDate() != null) {
			return this.date2String(getInvoiceDate(), FORMAT_INVOICE_DATE);
		} else {
			return null;
		}
	}
	public void setInvoiceDateString(String value) {
		setInvoiceDate(this.string2Date(value, FORMAT_INVOICE_DATE));
	}
	
		
	public void setInvoiceDate(java.util.Date value) {
		this.invoiceDate = value;
	}
	@Column(name="invoiceDate")
	public java.util.Date getInvoiceDate() {
		return this.invoiceDate;
	}
	
		
	public void setPayVoucher(java.lang.String value) {
		this.payVoucher = value;
	}
	@Column(name="payVoucher")
	public java.lang.String getPayVoucher() {
		return this.payVoucher;
	}
	
		
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}
	@Column(name="remarks")
	public java.lang.String getRemarks() {
		return this.remarks;
	}
	
		
	public void setPosNo(java.lang.String value) {
		this.posNo = value;
	}
	@Column(name="posNo")
	public java.lang.String getPosNo() {
		return this.posNo;
	}
	
		
	public void setPosTagEendNo(java.lang.String value) {
		this.posTagEendNo = value;
	}
	@Column(name="posTagEendNo")
	public java.lang.String getPosTagEendNo() {
		return this.posTagEendNo;
	}
	
		
	public void setPosBank(java.lang.String value) {
		this.posBank = value;
	}
	@Column(name="posBank")
	public java.lang.String getPosBank() {
		return this.posBank;
	}
	
		
	public void setBankName(java.lang.String value) {
		this.bankName = value;
	}
	@Column(name="bankName")
	public java.lang.String getBankName() {
		return this.bankName;
	}
	
		
	public void setToBankNname(java.lang.String value) {
		this.toBankNname = value;
	}
	@Column(name="toBankNname")
	public java.lang.String getToBankNname() {
		return this.toBankNname;
	}
	
		
	public void setBankAccount(java.lang.String value) {
		this.bankAccount = value;
	}
	@Column(name="bankAccount")
	public java.lang.String getBankAccount() {
		return this.bankAccount;
	}
	
		
	public void setToBankAccount(java.lang.String value) {
		this.toBankAccount = value;
	}
	@Column(name="toBankAccount")
	public java.lang.String getToBankAccount() {
		return this.toBankAccount;
	}
	
		
	public void setFromCompanyName(java.lang.String value) {
		this.fromCompanyName = value;
	}
	@Column(name="from_company_name")
	public java.lang.String getFromCompanyName() {
		return this.fromCompanyName;
	}
	
		
	public void setFromBankName(java.lang.String value) {
		this.fromBankName = value;
	}
	@Column(name="from_bank_name")
	public java.lang.String getFromBankName() {
		return this.fromBankName;
	}
	
		
	public void setFromAccount(java.lang.String value) {
		this.fromAccount = value;
	}
	@Column(name="from_account")
	public java.lang.String getFromAccount() {
		return this.fromAccount;
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
	@Transient	
	public String getDraftAccountedDateString() {
		if(getDraftAccountedDate() != null) {
			return this.date2String(getDraftAccountedDate(), FORMAT_DRAFT_ACCOUNTED_DATE);
		} else {
			return null;
		}
	}
	public void setDraftAccountedDateString(String value) {
		setDraftAccountedDate(this.string2Date(value, FORMAT_DRAFT_ACCOUNTED_DATE));
	}
	
		
	public void setDraftAccountedDate(java.util.Date value) {
		this.draftAccountedDate = value;
	}
	@Column(name="draft_accounted_date")
	public java.util.Date getDraftAccountedDate() {
		return this.draftAccountedDate;
	}
	
		
	public void setPayPrice(java.lang.String value) {
		this.payPrice = value;
	}
	@Column(name="payPrice")
	public java.lang.String getPayPrice() {
		return this.payPrice;
	}
	
		
	public void setPayPriceType(Integer value) {
		this.payPriceType = value;
	}
	@Column(name="payPriceType")
	public Integer getPayPriceType() {
		return this.payPriceType;
	}
	
		
	public void setGroupId(java.lang.Integer value) {
		this.groupId = value;
	}
	@Column(name="groupId")
	public java.lang.Integer getGroupId() {
		return this.groupId;
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
	
		
	public void setPayPriceBack(java.lang.String value) {
		this.payPriceBack = value;
	}
	@Column(name="payPriceBack")
	public java.lang.String getPayPriceBack() {
		return this.payPriceBack;
	}
	
		
	public void setIsAsAccount(Integer value) {
		this.isAsAccount = value;
	}
	@Column(name="isAsAccount")
	public Integer getIsAsAccount() {
		return this.isAsAccount;
	}
	
		
	public void setOldPayPrice(java.lang.String value) {
		this.oldPayPrice = value;
	}
	@Column(name="oldPayPrice")
	public java.lang.String getOldPayPrice() {
		return this.oldPayPrice;
	}
	
		
	public void setOrderType(java.lang.Integer value) {
		this.orderType = value;
	}
	@Column(name="orderType")
	public java.lang.Integer getOrderType() {
		return this.orderType;
	}
	
		
	public void setFastPayType(java.lang.String value) {
		this.fastPayType = value;
	}
	@Column(name="fastPayType")
	public java.lang.String getFastPayType() {
		return this.fastPayType;
	}
	@Transient	
	public String getPrintTimeString() {
		if(getPrintTime() != null) {
			return this.date2String(getPrintTime(), FORMAT_PRINT_TIME);
		} else {
			return null;
		}
	}
	public void setPrintTimeString(String value) {
		setPrintTime(this.string2Date(value, FORMAT_PRINT_TIME));
	}
	
		
	public void setPrintTime(java.util.Date value) {
		this.printTime = value;
	}
	@Column(name="printTime")
	public java.util.Date getPrintTime() {
		return this.printTime;
	}
	
		
	public void setPrintFlag(Integer value) {
		this.printFlag = value;
	}
	@Column(name="printFlag")
	public Integer getPrintFlag() {
		return this.printFlag;
	}
	
		
	public void setPaymentStatus(Integer value) {
		this.paymentStatus = value;
	}
	@Column(name="paymentStatus")
	public Integer getPaymentStatus() {
		return this.paymentStatus;
	}
	@Transient	
	public String getAccountDateString() {
		if(getAccountDate() != null) {
			return this.date2String(getAccountDate(), FORMAT_ACCOUNT_DATE);
		} else {
			return null;
		}
	}
	public void setAccountDateString(String value) {
		setAccountDate(this.string2Date(value, FORMAT_ACCOUNT_DATE));
	}
	
		
	public void setAccountDate(java.util.Date value) {
		this.accountDate = value;
	}
	@Column(name="accountDate")
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}

	public void setReceiptConfirmationDate(Date receiptConfirmationDate) {
		this.receiptConfirmationDate = receiptConfirmationDate;
	}
	@Column(name="receiptConfirmationDate")
	public Date getReceiptConfirmationDate() {
		return this.receiptConfirmationDate;
	}

	@Transient
	public String getReceiptConfirmationDateString() {
		if(getReceiptConfirmationDate() != null) {
			return this.date2String(getReceiptConfirmationDate(), FORMAT_ACCOUNT_DATE);
		} else {
			return null;
		}
	}
	public void setReceiptConfirmationDateString(String value) {
		setReceiptConfirmationDate(this.string2Date(value, FORMAT_ACCOUNT_DATE));
	}
		
	public void setCostRecordId(java.lang.Integer value) {
		this.costRecordId = value;
	}
	@Column(name="cost_record_id")
	public java.lang.Integer getCostRecordId() {
		return this.costRecordId;
	}

	@Column(name="reject_reason")
	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	@Column(name = "from_alipay_name")
	public String getFromAlipayName() {
		return fromAlipayName;
	}

	public void setFromAlipayName(String fromAlipayName) {
		this.fromAlipayName = fromAlipayName;
	}

	@Column(name = "from_alipay_account")
	public String getFromAlipayAccount() {
		return fromAlipayAccount;
	}

	public void setFromAlipayAccount(String fromAlipayAccount) {
		this.fromAlipayAccount = fromAlipayAccount;
	}

	@Column(name = "to_alipay_name")
	public String getToAlipayName() {
		return toAlipayName;
	}

	public void setToAlipayName(String toAlipayName) {
		this.toAlipayName = toAlipayName;
	}

	@Column(name = "to_alipay_account")
	public String getToAlipayAccount() {
		return toAlipayAccount;
	}

	public void setToAlipayAccount(String toAlipayAccount) {
		this.toAlipayAccount = toAlipayAccount;
	}

	@Column(name = "come_office_name")
	public String getComeOfficeName() {
		return comeOfficeName;
	}

	public void setComeOfficeName(String comeOfficeName) {
		this.comeOfficeName = comeOfficeName;
	}
	
}

