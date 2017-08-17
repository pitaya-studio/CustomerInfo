package com.trekiz.admin.modules.order.entity;

import java.math.BigDecimal;
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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 订单通用实体类：单团、散拼、游学、大客户、自由行
 * @author baiyakun
 *
 */
@Entity
@Table(name = "productorder")
@DynamicInsert
public class ProductOrderCommon extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	                                              
    /** 产品id */
    private Long productId;
    /** 产品出团信息表id */
    private Long productGroupId;
    /** 订单单号 */
    private String orderNum;
    /** 已占位订单的确认状态   0:未确认   1:已确认*/
    private Integer seizedConfirmationStatus;
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
    /***/
    private String hasSeen = "1";
    
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
    
    /** 总达账金额*/
    private String accountedMoney;
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
    
    private String cancelDescription;
    
    /**是否是补单订单：0为否，1为是*/
    private Integer isAfterSupplement;
    
    /**是否已查看过此订单：0为否，1为是*/
    private Integer seenFlag;

    /** 联运类型ID */
    private Long intermodalType;
    /** 订单锁定状态  0：正常，1：锁定   默认为0 */
    private Integer lockStatus;
    /** 确认单文件ID */
    private String confirmationFileId;
    /** 出团通知文件ID */
    private String openNoticeFileId;
    
    private String specialDemand;

    private String specialDemandFileIds;

    /** 原始应收价 */
    private String originalTotalMoney;
    
    /** 原始订金价格 */
    private String originalFrontMoney;
    
    /** 结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4 */
    private Integer paymentType = 1;
    
    /** 订单成本价 */
    private String costMoney;
    
    /** 游轮总间数 */
    private Integer roomNumber;
	
	/** 订单预定返佣金额uuid */
	private String scheduleBackUuid;
	
	/** 销售id*/
	private Integer salerId;
	
	/** 销售名称*/
	private String salerName;
	
	/** 订单锁定后退款与返佣录入成本值为1*/
//	private String isLockedIn;
	
	/** 报名时价格类型 */
	private Integer priceType = 0;
	
	/** 预报单锁定后生成的订单数据对应此字段值为1(目前只针对拉美途) */
	private Integer forecastLockedIn;
	
	/** 结算单锁定后生成的订单数据对应此字段值为1(目前只针对拉美途) */
	private Integer settleLockedIn;
	/** quauq服务费UUID */
	private String quauqServiceCharge;
	/** 'T2平台是否已向QUAUQ结清服务费  0 未结清 1 已结清' */
	private Integer isPayedCharge = 0;
	
	private String downloadFileIds;
	
	private Boolean confirmFlag;
	
	
	/** 整体服务费UUID */
	private String serviceCharge;
	/** 总社或集团服务费UUID */
	private String partnerServiceCharge;
	/** 抽成服务费UUID */
	private String cutServiceCharge;
	/** quauq产品费率类型 */
	private Integer quauqProductChargeType;
	/** quauq产品费率 */
	private BigDecimal quauqProductChargeRate;
	/** quauq其他费用费率类型 */
	private Integer quauqOtherChargeType;
	/** quauq其他费用费率 */
	private BigDecimal quauqOtherChargeRate;
	/** 总社或集团产品费率类型 */
	private Integer partnerProductChargeType;
	/** 总社或集团产品费率 */
	private BigDecimal partnerProductChargeRate;
	/** 总社或集团其他费用费率类型 */
	private Integer partnerOtherChargeType;
	/** 总社或集团其他费用费率 */
	private BigDecimal partnerOtherChargeRate;
	/** 抽成费用费率类型 */
	private Integer cutChargeType;
	/** 抽成费用费率 */
	private BigDecimal cutChargeRate;
	/** 预报名订单ID */
	private Long preOrderId;
	/** 是否有差额 */
	private Integer differenceFlag = 0;
	/** 差额UUID */
	private String differenceMoney;
	/** 同行价大于T1零售价金额：以分号分割 */
	private String overMoney;
	
	/** 渠道类型：0 普通渠道；1 门店；2 总社；3 集团客户 */
	private Integer agentType = -1;
	
	/**订单差额支付状态：0 未支付 1：已支付*/
	private Integer differencePayStatus=0;
    @Column(name="cost_money")
    public String getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(String costMoney) {
		this.costMoney = costMoney;
	}

	@Column(name="original_total_money")
	public String getOriginalTotalMoney() {
		return originalTotalMoney;
	}

	public void setOriginalTotalMoney(String originalTotalMoney) {
		this.originalTotalMoney = originalTotalMoney;
	}
	
	@Column(name="original_front_money")
	public String getOriginalFrontMoney() {
		return originalFrontMoney;
	}

	public void setOriginalFrontMoney(String originalFrontMoney) {
		this.originalFrontMoney = originalFrontMoney;
	}
	
	@Column(name="paymentType")
	public Integer getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
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

    public ProductOrderCommon() {
		super();
	}

	public ProductOrderCommon(Long id){
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

    @Column(name="seized_confirmation_status")
    public Integer getSeizedConfirmationStatus() {
		return seizedConfirmationStatus;
	}

	public void setSeizedConfirmationStatus(Integer seizedConfirmationStatus) {
		this.seizedConfirmationStatus = seizedConfirmationStatus;
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
	
	@Column(name="confirmationFileId")
	public String getConfirmationFileId() {
		return confirmationFileId;
	}

	public void setConfirmationFileId(String confirmationFileId) {
		this.confirmationFileId = confirmationFileId;
	}
	
	@Column(name="openNoticeFileId")
	public String getOpenNoticeFileId() {
		return openNoticeFileId;
	}

	public void setOpenNoticeFileId(String openNoticeFileId) {
		this.openNoticeFileId = openNoticeFileId;
	}

	@Column(name = "is_after_supplement", columnDefinition = "tinyint default 0")
	public Integer getIsAfterSupplement() {
		return isAfterSupplement;
	}

	public void setIsAfterSupplement(Integer isAfterSupplement) {
		this.isAfterSupplement = isAfterSupplement;
	}
	
	@Column(name = "seen_flag", columnDefinition = "int default 0")
	public Integer getSeenFlag() {
		return seenFlag;
	}

	public void setSeenFlag(Integer seenFlag) {
		this.seenFlag = seenFlag;
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

    public String getSpecialDemandFileIds() {
        return specialDemandFileIds;
    }

    public void setSpecialDemandFileIds(String specialDemandFileIds) {
        this.specialDemandFileIds = specialDemandFileIds;
    }

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Column(name="schedule_back_uuid",unique=false,nullable=true)
	public String getScheduleBackUuid() {
		return scheduleBackUuid;
	}

	public void setScheduleBackUuid(String scheduleBackUuid) {
		this.scheduleBackUuid = scheduleBackUuid;
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
	
//	@Column(name="is_locked_in",unique=false,nullable=true)
//	public String getIsLockedIn() {
//		return isLockedIn;
//	}
//
//	public void setIsLockedIn(String isLockedIn) {
//		this.isLockedIn = isLockedIn;
//	}

	@Column(name="priceType",unique=false,nullable=true)
	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	@Column(name="forecast_locked_in",unique=false,nullable=true)
	public Integer getForecastLockedIn() {
		return forecastLockedIn;
	}

	public void setForecastLockedIn(Integer forecastLockedIn) {
		this.forecastLockedIn = forecastLockedIn;
	}

	@Column(name="settle_locked_in",unique=false,nullable=true)
	public Integer getSettleLockedIn() {
		return settleLockedIn;
	}

	public void setSettleLockedIn(Integer settleLockedIn) {
		this.settleLockedIn = settleLockedIn;
	}

	public String getDownloadFileIds() {
		return downloadFileIds;
	}

	public void setDownloadFileIds(String downloadFileIds) {
		this.downloadFileIds = downloadFileIds;
	}

	public Boolean getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

//	@PrePersist
	@Override
	public void prePersist() {
		if(StringUtils.isBlank(this.confirmationFileId) && StringUtils.isBlank(this.downloadFileIds)){
			this.confirmFlag = false;
		}else if(!StringUtils.isBlank(this.confirmationFileId) && !StringUtils.isBlank(this.downloadFileIds) && this.confirmationFileId.equals(this.downloadFileIds)){
			this.confirmFlag = false;
		}else{
			this.confirmFlag = true;
		}
		super.prePersist();
	}

//	@PreUpdate
	@Override
	public void preUpdate() {
		if(StringUtils.isBlank(this.confirmationFileId) && StringUtils.isBlank(this.downloadFileIds)){
			this.confirmFlag = false;
		}else if(!StringUtils.isBlank(this.confirmationFileId) && !StringUtils.isBlank(this.downloadFileIds) && this.confirmationFileId.equals(this.downloadFileIds)){
			this.confirmFlag = false;
		}else{
			this.confirmFlag = true;
		}
		super.preUpdate();
	}
	
	@Column(name="quauq_service_charge",unique=false,nullable=true)
	public String getQuauqServiceCharge() {
		return quauqServiceCharge;
	}

	public void setQuauqServiceCharge(String quauqServiceCharge) {
		this.quauqServiceCharge = quauqServiceCharge;
	}

	public String getHasSeen() {
		return hasSeen;
	}

	public void setHasSeen(String hasSeen) {
		this.hasSeen = hasSeen;
	}

	@Column(name="isPayedCharge")
	public Integer getIsPayedCharge() {
		return isPayedCharge;
	}

	public void setIsPayedCharge(Integer isPayedCharge) {
		this.isPayedCharge = isPayedCharge;
	}

	@Column(name="partner_service_charge")
	public String getPartnerServiceCharge() {
		return partnerServiceCharge;
	}

	public void setPartnerServiceCharge(String partnerServiceCharge) {
		this.partnerServiceCharge = partnerServiceCharge;
	}

	@Column(name="cut_service_charge")
	public String getCutServiceCharge() {
		return cutServiceCharge;
	}

	public void setCutServiceCharge(String cutServiceCharge) {
		this.cutServiceCharge = cutServiceCharge;
	}

	@Column(name="quauq_product_charge_rate")
	public BigDecimal getQuauqProductChargeRate() {
		return quauqProductChargeRate;
	}

	public void setQuauqProductChargeRate(BigDecimal quauqProductChargeRate) {
		this.quauqProductChargeRate = quauqProductChargeRate;
	}

	@Column(name="quauq_other_charge_rate")
	public BigDecimal getQuauqOtherChargeRate() {
		return quauqOtherChargeRate;
	}

	public void setQuauqOtherChargeRate(BigDecimal quauqOtherChargeRate) {
		this.quauqOtherChargeRate = quauqOtherChargeRate;
	}

	@Column(name="partner_product_charge_rate")
	public BigDecimal getPartnerProductChargeRate() {
		return partnerProductChargeRate;
	}

	public void setPartnerProductChargeRate(BigDecimal partnerProductChargeRate) {
		this.partnerProductChargeRate = partnerProductChargeRate;
	}

	@Column(name="partner_other_charge_rate")
	public BigDecimal getPartnerOtherChargeRate() {
		return partnerOtherChargeRate;
	}

	public void setPartnerOtherChargeRate(BigDecimal partnerOtherChargeRate) {
		this.partnerOtherChargeRate = partnerOtherChargeRate;
	}

	@Column(name="cut_charge_type")
	public Integer getCutChargeType() {
		return cutChargeType;
	}

	public void setCutChargeType(Integer cutChargeType) {
		this.cutChargeType = cutChargeType;
	}

	@Column(name="cut_charge_rate")
	public BigDecimal getCutChargeRate() {
		return cutChargeRate;
	}

	public void setCutChargeRate(BigDecimal cutChargeRate) {
		this.cutChargeRate = cutChargeRate;
	}

	@Column(name="service_charge")
	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	@Column(name="quauq_product_charge_type")
	public Integer getQuauqProductChargeType() {
		return quauqProductChargeType;
	}

	public void setQuauqProductChargeType(Integer quauqProductChargeType) {
		this.quauqProductChargeType = quauqProductChargeType;
	}

	@Column(name="quauq_other_charge_type")
	public Integer getQuauqOtherChargeType() {
		return quauqOtherChargeType;
	}

	public void setQuauqOtherChargeType(Integer quauqOtherChargeType) {
		this.quauqOtherChargeType = quauqOtherChargeType;
	}

	@Column(name="partner_product_charge_type")
	public Integer getPartnerProductChargeType() {
		return partnerProductChargeType;
	}

	public void setPartnerProductChargeType(Integer partnerProductChargeType) {
		this.partnerProductChargeType = partnerProductChargeType;
	}

	@Column(name="partner_other_charge_type")
	public Integer getPartnerOtherChargeType() {
		return partnerOtherChargeType;
	}

	public void setPartnerOtherChargeType(Integer partnerOtherChargeType) {
		this.partnerOtherChargeType = partnerOtherChargeType;
	}

	@Column(name="agent_type")
	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}

	@Column(name="preOrderId")
	public Long getPreOrderId() {
		return preOrderId;
	}

	public void setPreOrderId(Long preOrderId) {
		this.preOrderId = preOrderId;
	}

	@Column(name = "differenceFlag")
	public Integer getDifferenceFlag() {
		return differenceFlag;
	}

	public void setDifferenceFlag(Integer differenceFlag) {
		this.differenceFlag = differenceFlag;
	}

	@Column(name = "differenceMoney")
	public String getDifferenceMoney() {
		return differenceMoney;
	}

	public void setDifferenceMoney(String differenceMoney) {
		this.differenceMoney = differenceMoney;
	}
	
	@Column(name = "differencePayStatus")
	public Integer getDifferencePayStatus() {
		return differencePayStatus;
	}

	public void setDifferencePayStatus(Integer differencePayStatus) {
		this.differencePayStatus = differencePayStatus;
	}

	@Column(name = "overMoney")
	public String getOverMoney() {
		return overMoney;
	}

	public void setOverMoney(String overMoney) {
		this.overMoney = overMoney;
	}
  }