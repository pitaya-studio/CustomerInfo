package com.trekiz.admin.modules.order.entity;

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
import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: Orderpay
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-01-20
 *  @version 1.0
 */
@Entity
@Table(name = "orderpay")
@DynamicUpdate
@DynamicInsert
public class Orderpay extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	/**编号*/
	private Long id;                                       
    /** 支付方式 */
    private Integer payType;
    /** 支付方式名称 */
    private String payTypeName;
    /** 付款单位 */
    private String payerName;
    /** 支票号 */
    private String checkNumber;
    /** 开票日期 */
    private Date invoiceDate;
    /** 支付凭证附件id */
    private String payVoucher;
    /** 备注信息 */
    private String remarks;
    /** POS单号 */
    private String posNo;
    /** POS机终端号 */
    private String posTagEendNo;
    /** POS机所属银行 */
    private String posBank;
    /** 开户行名称 */
    private String bankName;
    /** 转入行名称 */
    private String toBankNname;
    /** 开户行账户 */
    private String bankAccount;
    /** 转入行账号 */
    private String toBankAccount;
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
    /** 支付金额 */
    private String payPrice;
    /** 支付金额back */
    private String payPriceBack;
	/** 支付款类型（全款、订金、尾款） */
    private Integer payPriceType;
    /** 订单号ID*/
    private Long orderId;
    /** 是否达账*/
    private Integer isAsAccount;
    /**改价之前的原金额*/
    private String oldPayPrice;
    /**快速支付中的支付方式*/
    private String fastPayType;
    /**订单号*/
    private String orderNum;
	/**订单类型*/
    private Integer orderType;
    /**金额流水号*/
    private String moneySerialNum;
    
    /**支付金额，多币种金额由moneyAmount表查出*/
    private String moneyAmount;
    
    /**结算方式 */
    private Integer paymentStatus;
    /** 游客ID*/
    private Long travelerId;
    
    private Integer printFlag = 0;
    
    private Date printTime;

    /**驳回备注*/
    private String rejectReason;
    
    /**银行到账日期*/
    private Date accountDate;
    /**收款确认日期*/
    private Date receiptConfirmationDate;
    /**订单支付批量号*/
    private String orderPaySerialNum;
    /** 收款人数 */
    private Integer receivePeopleCount;
    /**支付宝账户（来款）*/
    private String fromAlipayName;
    /**支付宝账号（来款）*/
    private String fromAlipayAccount;
    /**支付宝账户（收款）*/
    private String toAlipayName;
    /**支付宝账号（收款）*/
    private String toAlipayAccount;
    /**收款单位*/
    private String comeOfficeName;
    /**return_difference的uuid 538需求*/
    private String differenceUuid;
	//    /** 再次提交验证*/
//    private Integer repeatSubmit;
    //付款状态（99：默认值 0 ：已撤销  1：已达账 2：已驳回）
    /** 默认值 */
    public static final int ISASACCOUNT_DEFAULT = 99;
    
    public String getOrderPaySerialNum() {
		return orderPaySerialNum;
	}

	public void setOrderPaySerialNum(String orderPaySerialNum) {
		this.orderPaySerialNum = orderPaySerialNum;
	}

	
	public Date getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(Date accountDate) {
		this.accountDate = accountDate;
	}

	public String getMoneySerialNum() {
		return moneySerialNum;
	}

	public void setMoneySerialNum(String moneySerialNum) {
		this.moneySerialNum = moneySerialNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getFastPayType() {
		return fastPayType;
	}

	public void setFastPayType(String fastPayType) {
		this.fastPayType = fastPayType;
	}


    public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}



	public Integer getIsAsAccount() {
        return isAsAccount;
    }
    
    public void setIsAsAccount(Integer isAsAccount) {
        this.isAsAccount = isAsAccount;
    }

	public Orderpay() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	    public void setPayType(Integer payType ){
        this.payType = payType ;
    }

    public Integer getPayType(){
        return this.payType;
    }

    public void setPayTypeName(String payTypeName ){
        this.payTypeName = payTypeName ;
    }

    @Length(min=0, max=50)
    public String getPayTypeName(){
        return this.payTypeName;
    }

    public void setPayerName(String payerName ){
        this.payerName = payerName ;
    }

    @Length(min=0, max=100)
    public String getPayerName(){
        return this.payerName;
    }

    public void setCheckNumber(String checkNumber ){
        this.checkNumber = checkNumber ;
    }

    @Length(min=0, max=50)
    public String getCheckNumber(){
        return this.checkNumber;
    }

    public void setInvoiceDate(Date invoiceDate ){
        this.invoiceDate = invoiceDate ;
    }

    public Date getInvoiceDate(){
        return this.invoiceDate;
    }

    public void setPayVoucher(String payVoucher ){
        this.payVoucher = payVoucher ;
    }

    public String getPayVoucher(){
        return this.payVoucher;
    }

    public void setRemarks(String remarks ){
        this.remarks = remarks ;
    }

    public String getRemarks(){
        return this.remarks;
    }

    public void setPosNo(String posNo ){
        this.posNo = posNo ;
    }

    @Length(min=0, max=50)
    public String getPosNo(){
        return this.posNo;
    }

    public void setPosTagEendNo(String posTagEendNo ){
        this.posTagEendNo = posTagEendNo ;
    }

    @Length(min=0, max=50)
    public String getPosTagEendNo(){
        return this.posTagEendNo;
    }

    public void setPosBank(String posBank ){
        this.posBank = posBank ;
    }

    @Length(min=0, max=100)
    public String getPosBank(){
        return this.posBank;
    }

    public void setBankName(String bankName ){
        this.bankName = bankName ;
    }

    @Length(min=0, max=100)
    public String getBankName(){
        return this.bankName;
    }

    public void setToBankNname(String toBankNname ){
        this.toBankNname = toBankNname ;
    }

    @Length(min=0, max=100)
    public String getToBankNname(){
        return this.toBankNname;
    }

    public void setBankAccount(String bankAccount ){
        this.bankAccount = bankAccount ;
    }

    @Length(min=0, max=50)
    public String getBankAccount(){
        return this.bankAccount;
    }

    public void setToBankAccount(String toBankAccount ){
        this.toBankAccount = toBankAccount ;
    }

    @Length(min=0, max=50)
    public String getToBankAccount(){
        return this.toBankAccount;
    }
    
	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getPayPriceBack() {
		return payPriceBack;
	}

	public void setPayPriceBack(String payPriceBack) {
		this.payPriceBack = payPriceBack;
	}

	public String getOldPayPrice() {
		return oldPayPrice;
	}

	public void setOldPayPrice(String oldPayPrice) {
		this.oldPayPrice = oldPayPrice;
	}

	public void setPayPriceType(Integer payPriceType ){
        this.payPriceType = payPriceType ;
    }

    public Integer getPayPriceType(){
        return this.payPriceType;
    }

    public void setOrderId(Long orderId ){
        this.orderId = orderId ;
    }

    public Long getOrderId(){
        return this.orderId;
    }

    @Transient
	public String getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(String moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(name="traveler_id")
	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}

	public Integer getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(Integer printFlag) {
		this.printFlag = printFlag;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
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
		
	public void setDraftAccountedDate(java.util.Date value) {
		this.draftAccountedDate = value;
	}
	@Column(name="draft_accounted_date")
	public java.util.Date getDraftAccountedDate() {
		return this.draftAccountedDate;
	}

	@Column(name="receivePeopleCount")
	public Integer getReceivePeopleCount() {
		return this.receivePeopleCount;
	}

	public void setReceivePeopleCount(Integer receivePeopleCount) {
		this.receivePeopleCount = receivePeopleCount;
	}

      @Column(name="reject_reason")
      public String getRejectReason() {
          return rejectReason;
      }

      public void setRejectReason(String rejectReason) {
          this.rejectReason = rejectReason;
      }
      
    @Column(name="receiptConfirmationDate")
	public Date getReceiptConfirmationDate() {
		return receiptConfirmationDate;
	}

	public void setReceiptConfirmationDate(Date receiptConfirmationDate) {
		this.receiptConfirmationDate = receiptConfirmationDate;
	}
	@Column(name="from_alipay_name")
	public String getFromAlipayName() {
		return fromAlipayName;
	}

	public void setFromAlipayName(String fromAlipayName) {
		this.fromAlipayName = fromAlipayName;
	}
	@Column(name="from_alipay_account")
	public String getFromAlipayAccount() {
		return fromAlipayAccount;
	}

	public void setFromAlipayAccount(String fromAlipayAccount) {
		this.fromAlipayAccount = fromAlipayAccount;
	}
	@Column(name="to_alipay_name")
	public String getToAlipayName() {
		return toAlipayName;
	}

	public void setToAlipayName(String toAlipayName) {
		this.toAlipayName = toAlipayName;
	}
	@Column(name="to_alipay_account")
	public String getToAlipayAccount() {
		return toAlipayAccount;
	}

	public void setToAlipayAccount(String toAlipayAccount) {
		this.toAlipayAccount = toAlipayAccount;
	}
	@Column(name="come_office_name")
	public String getComeOfficeName() {
		return comeOfficeName;
	}

	public void setComeOfficeName(String comeOfficeName) {
		this.comeOfficeName = comeOfficeName;
	}
//	@Column(name="repeatSubmit")
//	public Integer getRepeatSubmit() {
//		return repeatSubmit;
//	}
//
//	public void setRepeatSubmit(Integer repeatSubmit) {
//		this.repeatSubmit = repeatSubmit;
//	}
	@Column(name="differenceUuid")
	public String getDifferenceUuid() {
		return differenceUuid;
	}

	public void setDifferenceUuid(String differenceUuid) {
		this.differenceUuid = differenceUuid;
	}
  }