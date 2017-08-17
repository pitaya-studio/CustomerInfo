/**
 *
 */
package com.trekiz.admin.modules.receipt.entity;

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
@Table(name = "orderreceipt")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderReceipt extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	/*唯一标示UUID 多个订单合开发票时 共享这一个UUID*/
	private String uuid;                                              
    /** 订单ID */
    private Integer orderId;
    /** 订单单号 */
    private String orderNum;
    /** 收据号*/
    private String invoiceNum;
    /** 团号 */
    private String groupCode;
    /** 开收据状态*/
	private Integer createStatus;
    /** 收据审核状态 */
    private Integer verifyStatus;
    /** 开收据金额*/
    private BigDecimal invoiceAmount;
    /** 开收据方式*/
    private Integer invoiceMode;
    /** 开收据类型*/
    private Integer invoiceType;
    /** 开收据客户*/
    private String invoiceCustomer;
    /** 开收据项目*/
    private Integer invoiceSubject;
    /** 发收据机构ID*/
    private Long invoiceCompany;
    /** 发收据机构名称*/
    private String invoiceCompanyName;
	/** 发收据抬头*/
	private String invoiceHead;
	/** 开收据原因*/
	private String remarks;
	/** 审核收据金额*/
	private BigDecimal checkAmount;
	/** 领取状态*/
	private Integer receiveStatus;
	/** 订单状态*/
	private Integer orderType;
	
//=======================================================
                                              


	public OrderReceipt() {
		super();
	}

	public OrderReceipt(Long id){
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	
	
//=======================================================

	
}


