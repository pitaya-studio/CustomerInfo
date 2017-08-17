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
@Table(name = "cost_record_hotel")
public class CostRecordHotel  {
	
	private Long id;

	private String uuid;
	
    //海岛或酒店团期 uuid
    private String activityUuid;

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
    
	private Integer payNowLevel;
	private Integer payReview;
    private Long payReviewCompanyId;
    private String bankName;
    private String bankAccount;
    private BigDecimal rate;
    //格式化之后的汇率，保留三位小数
    private String formatRate;
    private Integer reviewType;


    private String reviewStatus;
    private Long reviewId;
    
    private String payedMoney;
    private String confirmMoney;
    private Integer payUpdateBy;
	private Date payUpdateDate; 
	
	private User createBy; // 创建者
	private Date createDate;// 创建日期
	private Long updateBy; // 更新者
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	private Date payApplyDate; //付款审核的申请时间
	
    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
     * 该条成本数据审核通过后是否计入到预报单
     * */

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

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="activity_uuid")
	public String getActivityUuid() {
		return activityUuid;
	}

	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
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
	
	@Column(name="pay_apply_date",unique=false,nullable=true)
	public Date getPayApplyDate() {
		return payApplyDate;
	}

	public void setPayApplyDate(Date payApplyDate) {
		this.payApplyDate = payApplyDate;
	}
		
}
