package com.trekiz.admin.modules.money.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @ClassName: MoneyAmount
 * @Description: TODO(流水实体)
 * @author kai.xiao
 * @date 2014年12月1日 下午9:30:03
 *
 */
@Entity
@Table(name = "money_amount")
public class MoneyAmount {
	
	@Override
	public String toString() {
		return this.getId() + this.getSerialNum() + this.getBusindessType() + this.getCreatedBy() + this.getUid() + this.getSerialNum();
	}

	public static final Integer BUSINDESSTYPE_DD=1;//订单
	public static final Integer BUSINDESSTYPE_YK=2;//游客

	/** 编号 */
	private Long id;
	/** 流水号 */
	private String serialNum;
	/** 币种ID */
	private Integer currencyId;
	/** 金额 */
	private BigDecimal amount;
	/** 汇率 */
	private BigDecimal exchangerate;
	/** 订单ID或游客ID或报价ID（20150511新增报价ID） */
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
	
	private String delFlag;

	/** 审核ID*/
	private Long reviewId;
	
	/**
	 * 订单支付批量号
	 */
	private String orderPaySerialNum;
	
	/**
	 * 新审批 review_new 表的id
	 */
	private String reviewUuid;

	/**
	 * 已付金额和达帐金额对应的订单收款的收款记录UUID，目前只适用于美途国际
	 */
	private String payedAccountedUUID;

	@Column(name = "payed_accounted_uuid")
	public String getPayedAccountedUUID() {
		return payedAccountedUUID;
	}

	public void setPayedAccountedUUID(String payedAccountedUUID) {
		this.payedAccountedUUID = payedAccountedUUID;
	}

	@Column(name = "orderPaySerialNum")
	public String getOrderPaySerialNum() {
		return orderPaySerialNum;
	}

	public void setOrderPaySerialNum(String orderPaySerialNum) {
		this.orderPaySerialNum = orderPaySerialNum;
	}
	
	@Column(name = "review_uuid")
	public String getReviewUuid() {
		return reviewUuid;
	}

	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}

	public MoneyAmount() {
	}

	/**
	 * 构造方法
	 * @param serialNum UUID
	 * @param currencyId 币种ID
	 * @param amount 金额
	 * @param uid 订单ID或游客ID
	 * @param moneyType 款项类型
	 * @param orderType 产品类型
	 * @param busindessType 业务类型
	 * @param createdBy 创建者
	 */
	public MoneyAmount(String serialNum, Integer currencyId, BigDecimal amount,
			Long uid, Integer moneyType, Integer orderType,
			Integer busindessType, Long createdBy) {
		super();
		this.serialNum = serialNum;
		this.currencyId = currencyId;
		this.amount = amount;
		this.uid = uid;
		this.moneyType = moneyType;
		this.orderType = orderType;
		this.busindessType = busindessType;
		this.createdBy = createdBy;
	}
	
	
	public MoneyAmount(String serialNum, Integer currencyId, BigDecimal amount,
			 Integer moneyType, Integer orderType,
			Integer busindessType, Long createdBy , BigDecimal exchangerate) {
		super();
		this.serialNum = serialNum;
		this.currencyId = currencyId;
		this.amount = amount;
		this.moneyType = moneyType;
		this.orderType = orderType;
		this.busindessType = busindessType;
		this.createdBy = createdBy;
		this.exchangerate = exchangerate;
	}
	
	public MoneyAmount(MoneyAmount moneyAmount) {
		super();
		this.serialNum = moneyAmount.getSerialNum();
		this.currencyId = moneyAmount.getCurrencyId();
		this.amount = moneyAmount.getAmount();
		this.exchangerate = moneyAmount.getExchangerate();
		this.uid = moneyAmount.getUid();
		this.moneyType = moneyAmount.getMoneyType();
		this.orderType = moneyAmount.getOrderType();
		this.busindessType = moneyAmount.getBusindessType();
		this.createdBy = moneyAmount.getCreatedBy();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "serialNum", unique = true, nullable = false, length = 32)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@Column(name = "currencyId", unique = false, nullable = false)
	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "amount", unique = false, nullable = false)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "uid", unique = false, nullable = false)
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	@Column(name = "moneyType", unique = false, nullable = false)
	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	@Column(name = "orderType", unique = false, nullable = false)
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "businessType", unique = false, nullable = false)
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

	@Column(name = "createTime", nullable = true, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Column(name = "exchangerate")
	public BigDecimal getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(BigDecimal exchangerate) {
		this.exchangerate = exchangerate;
	}

	@Column(name = "reviewId")
	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}	
	
	
}
