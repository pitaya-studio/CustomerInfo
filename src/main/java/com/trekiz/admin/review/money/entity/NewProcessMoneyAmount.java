package com.trekiz.admin.review.money.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.quauq.review.core.utils.IdGenerator;

@Entity
@Table(name = "review_process_money_amount")
public class NewProcessMoneyAmount {
	
	/** 流水号 */
	private String id ;
	/** 币种ID */
	private Integer currencyId;
	/** 金额 */
	private BigDecimal amount;
	/** 汇率 */
	private BigDecimal exchangerate;
	/** 订单ID或游客ID或报价ID */
	private Long uid;
	/** 款项类型 */
	private Integer moneyType;
	/** 产品类型 */
	private Integer orderType;
	/** 业务类型 */
	private Integer busindessType;
	/** 记录人ID */
	private Long createdBy;
	/** 生成时间 */
	private Date createTime;
	/** 删除标志 */
	private String delFlag;
	/** 审核流程表主ID */
	private String reviewId;
	/** 业务金额流水号 */
	private String serialNum;
	/**
	 * companyID
	 * 
	 */
	private String companyId;
	
	public NewProcessMoneyAmount(){
		super();
		this.id = IdGenerator.generateUuid();
	}
	
	@Id
	@Column(name="id",unique=true,nullable=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "currencyId")
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Column(name = "exchangerate")
	public BigDecimal getExchangerate() {
		return exchangerate;
	}
	public void setExchangerate(BigDecimal exchangerate) {
		this.exchangerate = exchangerate;
	}
	@Column(name = "uid")
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	@Column(name = "moneyType")
	public Integer getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}
	@Column(name = "orderType")
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	@Column(name = "businessType")
	public Integer getBusindessType() {
		return busindessType;
	}
	public void setBusindessType(Integer busindessType) {
		this.busindessType = busindessType;
	}
	@Column(name = "createdBy")
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "delFlag")
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "reviewId")
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	@Column(name = "company_id")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Column(name = "serial_number")
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	
	
}
