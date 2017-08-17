package com.trekiz.admin.modules.cost.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.modules.sys.entity.User;

@Entity
@Table(name = "cost_record")
public class CostRecord{

	private Long id;
	
	private String uuid;
    //团期,签证,机票 id
    private Long activityId;

    //订单类型 2:散拼;6签证;7机票
    private Integer orderType; 
    
    private Integer quantity;
    
    private Integer currencyId;
    
    private Integer currencyAfter;
    
    private Integer overseas;
    
    private String name;
    
    private BigDecimal price;
    
	private String formatPrice;
    
	private String comment;
    //渠道商类型 
    private Integer supplyType;
    //地接社类型
    private Integer supplierType;
    //渠道商 Id
    private Integer supplyId;  
      
    private String supplyName;
    
    private Integer budgetType;  
       
    private Integer payStatus;
    
    private Integer printFlag = 0;
    
    private Integer invoiceStatus = 0;
    
    private Date printTime;
    
    private String serialNum;
    
    private BigDecimal priceAfter;
    private String formatPriceAfter;
    
	private Integer nowLevel;
	private Integer review;
    private Long reviewCompanyId;
    private String reviewComment;
    
	private Integer payNowLevel;
	private Integer payReview;
	private String payReviewComment;
    private Long payReviewCompanyId;
    private String bankName;
    private String bankAccount;
    private BigDecimal rate;
    //格式化之后的汇率，保留三位小数
    private String formatRate;
    private Integer reviewType;
    private Long orderId;

    private String reviewStatus;
    private Long reviewId;
    private String reviewUuid;
    private String payReviewUuid;
  
	private String payedMoney;
    private String confirmMoney;
    /**
     * 该条成本数据审核通过后是否计入到预报单
     * */
    private Integer isJoin;
    private Integer payUpdateBy;
	private Date payUpdateDate; 
	
	private User createBy; // 创建者
	private Date createDate;// 创建日期
	private Long updateBy; // 更新者
	private Date updateDate;// 更新日期
	private String delFlag="0"; // 删除标记（0：正常；1：删除；2：审核）
	private Date payApplyDate; //付款审核的申请时间
	private Long visaId; //签证ID
	private String bigCode;
	private String pnrUuid;	//美途国际 airticket_order_pnr表的uuid字段
	private Integer isNew;  //是否是新审批
	private Integer kb; //是否为kb款
	private String forecastLockedIn;	//只针对拉美途，预报单锁定之后录入的成本，退款，返佣，其他收入数据该值为1
	private String settleLockedIn;	//只针对拉美途，结算单锁定之后录入的成本，退款，返佣，其他收入数据该值为1
	private String airline; //航空公司二字码
	private String airlineUuid; //航段表Uuid
	private String airlineName; //航段名称
	private BigDecimal costTotalDeposit; //定金总额
	private String costVoucher; // 附件
	private Long rebatesId;//rebatesId
	
	
	@Column(name="is_new")
	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	//payStatus '付款状态:0 没付款, 1:已付款, 2:待提交, 3:已提交,4:撤回',
	/** 付款状态（0 ：没付款） */
	public final static int PAY_STATUS_PENDING = 0;
	/** 付款状态（1:已付款） */
	public final static int PAY_STATUS_ALREADY = 1;
	/** 付款状态（2:待提交） */
	public final static int PAY_STATUS_TOBESUBMIT = 2;
	/** 付款状态（3:已提交） */
	public final static int PAY_STATUS_SUBMIT = 3;
	/** 付款状态（4:撤回） */
	public final static int PAY_STATUS_CANCEL = 4;

    
	@Transient
    public String getFormatPrice() {
		return formatPrice;
	}

	public void setFormatPrice(String formatPrice) {
		this.formatPrice = formatPrice;
	}
	
	@Transient
	public String getFormatPriceAfter() {
		return formatPriceAfter;
	}

	public void setFormatPriceAfter(String formatPriceAfter) {
		this.formatPriceAfter = formatPriceAfter;
	}
   	
	@Transient
	public String getFormatRate() {
		return formatRate;
	}

	public void setFormatRate(String formatRate) {
		this.formatRate = formatRate;
	}
	
	public Integer getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	@Column(name="name",unique=false,nullable=true)
	public String getName() {
		return name;
	}

	@Column(name="price",unique=false,nullable=true)
	public BigDecimal getPrice() {
		return price;
	}

	@Column(name="comment",unique=false,nullable=true)
	public String getComment() {
		return comment;
	}

	@Column(name="activityId",unique=false,nullable=true)
	public Long getActivityId() {
		return activityId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	@Column(name="quantity",unique=false,nullable=true)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name="supplyId",unique=false,nullable=true)
	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}

	@Column(name="supplyName",unique=false,nullable=true)
	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	@Column(name="currencyId",unique=false,nullable=true)
	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	@Column(name="overseas",unique=false,nullable=true)
	public Integer getOverseas() {
		return overseas;
	}
	public void setOverseas(Integer overseas) {
		this.overseas = overseas;
	}
	@Column(name="supplierType",unique=false,nullable=true)
	public Integer getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Integer supplierType) {
		this.supplierType = supplierType;
	}
	@Column(name="supplyType",unique=false,nullable=true)
	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}
	@Column(name="budgetType",unique=false,nullable=true)
	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}
	
	@Column(name="orderType",unique=false,nullable=true)
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	@Column(name="review",unique=false,nullable=true)
	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	@Column(name="payStatus",unique=false,nullable=true)
	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	} 

	public Integer getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(Integer printFlag) {
		this.printFlag = printFlag;
	}
	
	@Column(name = "invoiceStatus", unique = false, nullable = false)
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	
	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	@Column(name="priceAfter",unique=false,nullable=true)
	public BigDecimal getPriceAfter() {
		return priceAfter;
	}

	public void setPriceAfter(BigDecimal priceAfter) {
		this.priceAfter = priceAfter;
	}
	@Column(name="nowLevel",unique=false,nullable=true)
	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}
	@Column(name="reviewCompanyId",unique=false,nullable=true)
	public Long getReviewCompanyId() {
		return reviewCompanyId;
	}

	public void setReviewCompanyId(Long reviewCompanyId) {
		this.reviewCompanyId = reviewCompanyId;
	}
	
	@Column(name="reviewComment",unique=false,nullable=true)
	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}
	
	@Column(name="bankName",unique=false,nullable=true)
	public String getBankName() {
		return bankName;
	}
	
	public Integer getPayNowLevel() {
		return payNowLevel;
	}

	public void setPayNowLevel(Integer payNowLevel) {
		this.payNowLevel = payNowLevel;
	}

	public Integer getPayReview() {
		return payReview;
	}

	public void setPayReview(Integer payReview) {
		this.payReview = payReview;
	}

	public Long getPayReviewCompanyId() {
		return payReviewCompanyId;
	}

	public void setPayReviewCompanyId(Long payReviewCompanyId) {
		this.payReviewCompanyId = payReviewCompanyId;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Column(name="bankAccount",unique=false,nullable=true)
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	@Column(name="currencyAfter",unique=false,nullable=true)
	public Integer getCurrencyAfter() {
		return currencyAfter;
	}

	public void setCurrencyAfter(Integer currencyAfter) {
		this.currencyAfter = currencyAfter;
	}
	@Column(name="rate",unique=false,nullable=true)
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	@Column(name="reviewType",unique=false,nullable=true)
	public Integer getReviewType() {
		return reviewType;
	}

	public void setReviewType(Integer reviewType) {
		this.reviewType = reviewType;
	}
	@Column(name="orderId",unique=false,nullable=true)
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="reviewStatus",unique=false,nullable=true)
	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	@Column(name="reviewId",unique=false,nullable=true)
	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	
	@Transient
	public String getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}

	@Transient
	public String getConfirmMoney() {
		return confirmMoney;
	}

	public void setConfirmMoney(String confirmMoney) {
		this.confirmMoney = confirmMoney;
	}

	/**
	 * 获取成本总价格，单价*数量
	 * */
	@Transient
	public BigDecimal getTotal(){
		BigDecimal totalMoney= new BigDecimal(0);
		if(this.price != null && this.quantity != null){ 
			totalMoney = price.multiply(new BigDecimal(this.quantity));
		}
		return totalMoney;
	}

	public Integer getPayUpdateBy() {
		return payUpdateBy;
	}

	public void setPayUpdateBy(Integer payUpdateBy) {
		this.payUpdateBy = payUpdateBy;
	}

	public Date getPayUpdateDate() {
		return payUpdateDate;
	}

	public void setPayUpdateDate(Date payUpdateDate) {
		this.payUpdateDate = payUpdateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createBy")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	
	@Column(name="payReviewComment",unique=false,nullable=true)
	public String getPayReviewComment() {
		return payReviewComment;
	}

	public void setPayReviewComment(String payReviewComment) {
		this.payReviewComment = payReviewComment;
	}
	  
    public String getReviewUuid() {
		return reviewUuid;
	}
    @Column(name="reviewUuid")
	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}
    
    @Column(name="pay_review_uuid")
    public String getPayReviewUuid() {
		return payReviewUuid;
	}
    
	public void setPayReviewUuid(String payReviewUuid) {
		this.payReviewUuid = payReviewUuid;
	}
    

	@Column(name="pay_apply_date",unique=false,nullable=true)
	public Date getPayApplyDate() {
		return payApplyDate;
	}
	
	public void setPayApplyDate(Date payApplyDate) {
		this.payApplyDate = payApplyDate;
	}

	public Long getVisaId() {
		return visaId;
	}

	public void setVisaId(Long visaId) {
		this.visaId = visaId;
	}

	@Column(name="bigCode",unique=false,nullable=true)
	public String getBigCode() {
		return bigCode;
	}

	public void setBigCode(String bigCode) {
		this.bigCode = bigCode;
	}
	
	@Column(name="pnr_uuid",unique=false,nullable=true)
	public String getPnrUuid() {
		return pnrUuid;
	}

	public void setPnrUuid(String pnrUuid) {
		this.pnrUuid = pnrUuid;
	}
	
	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getKb() {
		return kb;
	}

	public void setKb(Integer kb) {
		this.kb = kb;
	}
	
	@Column(name="forecast_locked_in")
	public String getForecastLockedIn() {
		return forecastLockedIn;
	}
	
	public void setForecastLockedIn(String forecastLockedIn) {
		this.forecastLockedIn = forecastLockedIn;
	}
	
	@Column(name="settle_locked_in")
	public String getSettleLockedIn() {
		return settleLockedIn;
	}
	
	public void setSettleLockedIn(String settleLockedIn) {
		this.settleLockedIn = settleLockedIn;
	}

	@Column(name="airline")
	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	@Column(name="airline_uuid")
	public String getAirlineUuid() {
		return airlineUuid;
	}

	public void setAirlineUuid(String airlineUuid) {
		this.airlineUuid = airlineUuid;
	}

	@Column(name="airline_name")
	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	@Column(name="cost_total_deposit")
	public BigDecimal getCostTotalDeposit() {
		return costTotalDeposit;
	}

	public void setCostTotalDeposit(BigDecimal costTotalDeposit) {
		this.costTotalDeposit = costTotalDeposit;
	}


	public String getCostVoucher() {
		return costVoucher;
	}

	public void setCostVoucher(String costVoucher) {
		this.costVoucher = costVoucher;
	}

	public Long getRebatesId() {
		return rebatesId;
	}

	public void setRebatesId(Long rebatesId) {
		this.rebatesId = rebatesId;
	}
}
