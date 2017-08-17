/**
 *
 */
package com.trekiz.admin.modules.invoice.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: Orderinvoice
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author liangjingming
 *  @DateTime 2014-01-20
 *  @version 1.0
 */
@Entity
@Table(name = "orderinvoice")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Orderinvoice extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 订单ID */
    private Integer orderId;
    /** 订单单号 */
    private String orderNum;
    /** 发票号*/
    private String invoiceNum;
    /** 支付ID*/
    private Integer payId;
    /** 团号 */
    private String groupCode;
    /** 开票状态*/
	private Integer createStatus;
    /** 发票审核状态 */
    private Integer verifyStatus;
    /** 开票金额*/
    private BigDecimal invoiceAmount;
    /** 开票方式*/
    private Integer invoiceMode;
    /** 开票类型*/
    private Integer invoiceType;
    /** 开票客户*/
    private String invoiceCustomer;
    /** 开票项目*/
    private Integer invoiceSubject;
    
    // 0444需求
    /** 申请方式*/
    private Integer applyInvoiceWay;
    /** 回款状态*/
    private Integer receivedPayStatus;
    // 0444需求
    
    /** 发票机构ID*/
    private Long invoiceCompany;
    /** 发票机构名称*/
    private String invoiceCompanyName;
	/** 发票抬头*/
	private String invoiceHead;
	/** 来款单位  0414 增加来款单位 */
	private String invoiceComingUnit;
	/** 开票原因*/
	private String remarks;
	/** 审核发票金额*/
	private BigDecimal checkAmount;
	/** 领取状态*/
	private Integer receiveStatus;
	/** 订单类型*/
	private Integer orderType;
	/** uuid*/
	private String uuid;
	/** 开票备注*/
	private String invoiceRemark;
	/** 审核人备注 */
	private String reviewRemark;
	/** 出票单位  	--568 仅针对鼎鸿假期  		2 : 北京鼎鸿假期旅行社有限公司 	3 :北京鼎鸿假日国际旅行社有限公司(默认选择)*/
	private Integer invoiceComeFromCompany; 
	
//=======================================================

	public Orderinvoice() {
		super();
	}

	public Integer getInvoiceComeFromCompany() {
		return invoiceComeFromCompany;
	}

	public void setInvoiceComeFromCompany(Integer invoiceComeFromCompany) {
		this.invoiceComeFromCompany = invoiceComeFromCompany;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name="applyInvoiceWay",unique=false,nullable=true)
	public Integer getApplyInvoiceWay() {
		return applyInvoiceWay;
	}

	public void setApplyInvoiceWay(Integer applyInvoiceWay) {
		this.applyInvoiceWay = applyInvoiceWay;
	}

	@Column(name="receivedPayStatus",unique=false,nullable=true)
	public Integer getReceivedPayStatus() {
		return receivedPayStatus;
	}

	public void setReceivedPayStatus(Integer receivedPayStatus) {
		this.receivedPayStatus = receivedPayStatus;
	}

	public Orderinvoice(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orderinvoice")
	//@SequenceGenerator(name = "seq_orderinvoice", sequenceName = "seq_orderinvoice")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderId(Integer orderId ){
        this.orderId = orderId ;
    }

	@Column(name="orderId",unique=false,nullable=true)
    public Integer getOrderId(){
        return this.orderId;
    }

    public void setOrderNum(String orderNum ){
        this.orderNum = orderNum ;
    }

    @Length(min=0, max=50)
    @Column(name="orderNum",unique=false,nullable=true)
    public String getOrderNum(){
        return this.orderNum;
    }
    
    @Column(name="invoiceNum",unique=false,nullable=true)
    public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public void setPayId(Integer payId ){
        this.payId = payId ;
    }

    @Column(name="payId",unique=false,nullable=true)
    public Integer getPayId(){
        return this.payId;
    }

    public void setGroupCode(String groupCode ){
        this.groupCode = groupCode ;
    }

    @Length(min=0, max=500)
    @Column(name="groupCode",unique=false,nullable=true)
    public String getGroupCode(){
        return this.groupCode;
    }

    public void setVerifyStatus(Integer verifyStatus ){
        this.verifyStatus = verifyStatus ;
    }

    @Column(name="verifyStatus")
    public Integer getVerifyStatus(){
        return this.verifyStatus;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount ){
        this.invoiceAmount = invoiceAmount ;
    }

    @Column(name="invoiceAmount",unique=false,nullable=true)
    public BigDecimal getInvoiceAmount(){
        return this.invoiceAmount;
    }

    public void setInvoiceMode(Integer invoiceMode ){
        this.invoiceMode = invoiceMode ;
    }
    
    @Column(name="invoiceMode",unique=false,nullable=true)
    public Integer getInvoiceMode(){
        return this.invoiceMode;
    }

    public void setInvoiceType(Integer invoiceType ){
        this.invoiceType = invoiceType ;
    }

    @Column(name="invoiceType",unique=false,nullable=true)
    public Integer getInvoiceType(){
        return this.invoiceType;
    }

    public void setInvoiceCustomer(String invoiceCustomer ){
        this.invoiceCustomer = invoiceCustomer ;
    }

    @Length(min=0, max=100)
    @Column(name="invoiceCustomer",unique=false,nullable=true)
    public String getInvoiceCustomer(){
        return this.invoiceCustomer;
    }

    public void setInvoiceSubject(Integer invoiceSubject ){
        this.invoiceSubject = invoiceSubject ;
    }

    @Column(name="invoiceSubject",unique=false,nullable=true)
    public Integer getInvoiceSubject(){
        return this.invoiceSubject;
    }

    @Column(name="invoiceCompany",unique=false,nullable=true)
	public Long getInvoiceCompany() {
		return invoiceCompany;
	}

	public void setInvoiceCompany(Long invoiceCompany) {
		this.invoiceCompany = invoiceCompany;
	}

	@Column(name="invoiceCompanyName",unique=false,nullable=true)
	public String getInvoiceCompanyName() {
		return invoiceCompanyName;
	}

	public void setInvoiceCompanyName(String invoiceCompanyName) {
		this.invoiceCompanyName = invoiceCompanyName;
	}
	
	@Column(name="createStatus")
	public Integer getCreateStatus() {
		return createStatus;
	}

	public void setCreateStatus(Integer createStatus) {
		this.createStatus = createStatus;
	}
	
	@Column(name="invoiceHead")
	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name="checkAmount")
	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}

	@Column(name="receiveStatus")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Column(name="invoiceRemark")
	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}
	
	@Column(name="reviewRemark")
	public String getReviewRemark() {
		return reviewRemark;
	}

	public void setReviewRemark(String reviewRemark) {
		this.reviewRemark = reviewRemark;
	}

	@Column(name="invoiceComingUnit")
	public String getInvoiceComingUnit() {
		return invoiceComingUnit;
	}

	public void setInvoiceComingUnit(String invoiceComingUnit) {
		this.invoiceComingUnit = invoiceComingUnit;
	}


	
//=======================================================

	
}


