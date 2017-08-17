/**
 *
 */
package com.trekiz.admin.modules.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 预报名订单实体类：单团、散拼、游学、大客户、自由行
 * @author baiyakun
 *
 */
@Entity
@Table(name = "preproductorder")
public class PreProductOrderCommon extends DataEntity {
	

	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	                                              
    /** 产品id */
    private Long productId;
    /** 产品出团信息表id */
    private Long productGroupId;
    /** 订单单号 */
    private String orderNum;
    /** 订单状态 */
    private Integer orderType;
    /** 预订单位 */
    private Long orderCompany;
    /** 预订单位名称 */
    private String orderCompanyName;
    /** 预订人 */
    private User orderSaler;
	/** 预订联系人名称 */
    private String orderPersonName;
    /** 预订联系人电话 */
    private String orderPersonPhoneNum;
    /** 预订日期 */
    private Date orderTime;
    /** 预订人数 */
    private Integer orderPersonNum;
    /** 订单种类 */
    private Integer orderStatus;
    
    /** 成人预订人数 */
    private Integer orderPersonNumAdult;
    /** 儿童预订人数 */
    private Integer orderPersonNumChild;
    /** 特殊预订人数 */
    private Integer orderPersonNumSpecial;
	/** 订金金额 */
    private String frontMoney;
    /** 支付状态 */
    private Integer payStatus;
    /** 订单原价总额 */
    private String totalMoney;
    /** 已支付金额 */
    private String payedMoney;
    




	/** 支付方式 */
    private Integer payType;
    /** 是否已开发票 */
    private Integer isAlreadyInvoice;
    /** 退换类型 */
    private Integer groupChangeType;
    /** 当前退换记录Id*/
    private Long changeGroupId;
    
    /** 总达账金额*/
    private String accountedMoney;
    /** 达账状态*/
    private Integer asAcountType;
    /** 占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位*/
    private Integer placeHolderType;
    
    /** 下订单时成人的产品结算价 */
    private String settlementAdultPrice;
    /** 下订单时儿童的产品结算价 */
    private String settlementcChildPrice;
    /** 下订单时特殊的产品结算价 */
    private String settlementSpecialPrice;
    /** 下订单时产品的预收订金 */
    private String payDeposit;
    /** 下订单时的单房差 */
    private String singleDiff;
    /**付款方式*/
    private String payMode;
    /**保留天数*/
    private Double remainDays;
    /** 激活日期 */
    private Date activationDate;
    
	private String signKey;
    
    private String cancelDescription;

    private Long intermodalType;

    private String specialDemand;
    
    /** 销售Id */
    private Integer salerId;
    
    /** 销售姓名 */
    private String salerName;
    
    /*原始应收价*/
    private String originalTotalMoney;
    
    @Column(name="original_total_money")
	public String getOriginalTotalMoney() {
		return originalTotalMoney;
	}

	public void setOriginalTotalMoney(String originalTotalMoney) {
		this.originalTotalMoney = originalTotalMoney;
	}
    
    @Column(name="front_money")
	public String getFrontMoney() {
		return frontMoney;
	}

	public void setFrontMoney(String frontMoney) {
		this.frontMoney = frontMoney;
	}
	
	@Column(name="total_money")
	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	
	
	@Column(name="payed_money")
	public String getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}

	@Column(name="accounted_money")
	public String getAccountedMoney() {
		return accountedMoney;
	}

	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}

	public String getSettlementAdultPrice() {
		return settlementAdultPrice;
	}

	public void setSettlementAdultPrice(String settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}

	public String getSettlementcChildPrice() {
		return settlementcChildPrice;
	}

	public void setSettlementcChildPrice(String settlementcChildPrice) {
		this.settlementcChildPrice = settlementcChildPrice;
	}

	public String getSettlementSpecialPrice() {
		return settlementSpecialPrice;
	}

	public void setSettlementSpecialPrice(String settlementSpecialPrice) {
		this.settlementSpecialPrice = settlementSpecialPrice;
	}

	public String getPayDeposit() {
		return payDeposit;
	}

	public void setPayDeposit(String payDeposit) {
		this.payDeposit = payDeposit;
	}

	public String getSingleDiff() {
		return singleDiff;
	}

	public void setSingleDiff(String singleDiff) {
		this.singleDiff = singleDiff;
	}

    public Integer getOrderPersonNumAdult() {
        return orderPersonNumAdult;
    }

    public void setOrderPersonNumAdult(Integer orderPersonNumAdult) {
        this.orderPersonNumAdult = orderPersonNumAdult;
    }

    public Integer getOrderPersonNumChild() {
        return orderPersonNumChild;
    }

    public void setOrderPersonNumChild(Integer orderPersonNumChild) {
        this.orderPersonNumChild = orderPersonNumChild;
    }
    
    public Integer getOrderPersonNumSpecial() {
		return orderPersonNumSpecial;
	}

	public void setOrderPersonNumSpecial(Integer orderPersonNumSpecial) {
		this.orderPersonNumSpecial = orderPersonNumSpecial;
	}

    
    public Integer getPlaceHolderType() {
        return placeHolderType;
    }

    public void setPlaceHolderType(Integer placeHolderType) {
        this.placeHolderType = placeHolderType;
    }

    


    public Integer getAsAcountType() {
        return asAcountType;
    }

    public void setAsAcountType(Integer asAcountType) {
        this.asAcountType = asAcountType;
    }

    public Integer getGroupChangeType() {
        return groupChangeType;
    }

    public void setGroupChangeType(Integer groupChangeType) {
        this.groupChangeType = groupChangeType;
    }

    public Long getChangeGroupId() {
        return changeGroupId;
    }

    public void setChangeGroupId(Long changeGroupId) {
        this.changeGroupId = changeGroupId;
    }

    public PreProductOrderCommon() {
		super();
	}

	public PreProductOrderCommon(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_productorder")
	//@SequenceGenerator(name = "seq_order_productorder", sequenceName = "seq_order_productorder")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProductId(Long productId ){
        this.productId = productId ;
    }

    public Long getProductId(){
        return this.productId;
    }

    public void setProductGroupId(Long productGroupId ){
        this.productGroupId = productGroupId ;
    }

    public Long getProductGroupId(){
        return this.productGroupId;
    }

    public void setOrderNum(String orderNum ){
        this.orderNum = orderNum ;
    }

    @Length(min=0, max=50)
    public String getOrderNum(){
        return this.orderNum;
    }

    public void setOrderType(Integer orderType ){
        this.orderType = orderType ;
    }

    public Integer getOrderType(){
        return this.orderType;
    }
    
    public void setOrderStatus(Integer orderStatus ){
        this.orderStatus = orderStatus ;
    }

    public Integer getOrderStatus(){
        return this.orderStatus;
    }

    public void setOrderCompany(Long orderCompany ){
        this.orderCompany = orderCompany ;
    }

    public Long getOrderCompany(){
        return this.orderCompany;
    }

    public void setOrderCompanyName(String orderCompanyName ){
        this.orderCompanyName = orderCompanyName ;
    }

    @Length(min=0, max=100)
    public String getOrderCompanyName(){
        return this.orderCompanyName;
    }

    public void setOrderPersonName(String orderPersonName ){
        this.orderPersonName = orderPersonName ;
    }
    
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="orderSalerId")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
    public User getOrderSaler() {
		return orderSaler;
	}

	public void setOrderSaler(User orderSaler) {
		this.orderSaler = orderSaler;
	}

	@Length(min=0, max=50)
    public String getOrderPersonName(){
        return this.orderPersonName;
    }

    public void setOrderPersonPhoneNum(String orderPersonPhoneNum ){
        this.orderPersonPhoneNum = orderPersonPhoneNum ;
    }

    @Length(min=0, max=50)
    public String getOrderPersonPhoneNum(){
        return this.orderPersonPhoneNum;
    }

    public void setOrderTime(Date orderTime ){
        this.orderTime = orderTime ;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getOrderTime(){
        return this.orderTime;
    }

    public void setOrderPersonNum(Integer orderPersonNum ){
        this.orderPersonNum = orderPersonNum ;
    }

    public Integer getOrderPersonNum(){
        return this.orderPersonNum;
    }


  

    public void setPayStatus(Integer payStatus ){
        this.payStatus = payStatus ;
    }

    @Column(name="payStatus",unique=false,nullable=false)
    public Integer getPayStatus(){
        return this.payStatus;
    }

    

    public void setPayType(Integer payType ){
        this.payType = payType ;
    }

    public Integer getPayType(){
        return this.payType;
    }

    public void setIsAlreadyInvoice(Integer isAlreadyInvoice ){
        this.isAlreadyInvoice = isAlreadyInvoice ;
    }

    public Integer getIsAlreadyInvoice(){
        return this.isAlreadyInvoice;
    }
    @Column(name="cancel_description", nullable=true, unique=false)
    public String getCancelDescription() {
    	return cancelDescription;
    }
    
    public void setCancelDescription(String cancelDescription) {
    	this.cancelDescription = cancelDescription;
    }
    @Column(name="payMode",unique=false,nullable=false)
	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	@Column(name="remainDays",unique=false,nullable=false)
	public Double getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(Double remainDays) {
		this.remainDays = remainDays;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	
	@PrePersist
	public void preSaveActivationDate(){
		this.activationDate = this.orderTime;
	}

    @Column(name="intermodal_type",unique=false,nullable=true)
    public Long getIntermodalType() {
      return intermodalType;
    }

    public void setIntermodalType(Long intermodalType) {
      this.intermodalType = intermodalType;
    }

	public String getSpecialDemand() {
		return specialDemand;
	}

	public void setSpecialDemand(String specialDemand) {
		this.specialDemand = specialDemand;
	}

	public Integer getSalerId() {
		return salerId;
	}

	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	
  }