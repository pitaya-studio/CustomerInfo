package com.trekiz.admin.modules.eprice.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  多币种、整体报价、细分报价 和 金额表 联查的视图
 * @author gao
 *  2015年5月8日
 */
@Entity
@Table(name = "estimate_money_view")
public class EstimateMoneyView {
	private Long msgId;
	// 多币种、整体报价、细分报价 表 ID
	private Long estimatePriceReplyId;
	// 1：整体报价 2：细分报价
	private Integer priceType;
	// 1：成人类型；2：儿童类型；3：特殊人群
	private Integer personType;
	// 人数
	private Integer personNum;
	// 多币种记录ID
	private Long moneyAmountID;
	/** 记录人ID */
	private Long createdBy;
	/** 生成时间 */
	private Date createTime;
	
	/** 流水号 */
	private String serialNum;
	/** 币种ID */
	private Integer currencyId;
	/** 金额 */
	private BigDecimal amount;
	/** 汇率 */
	private BigDecimal exchangerate;
	/** 订单ID或游客ID */
	private Long uid;
	/** 款项类型 */
	private Integer moneyType;
	/** 产品类型 */
	private Integer orderType;
	/** 业务类型 */
	private Integer businessType;
	

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Long getEstimatePriceReplyId() {
		return estimatePriceReplyId;
	}

	public void setEstimatePriceReplyId(Long estimatePriceReplyId) {
		this.estimatePriceReplyId = estimatePriceReplyId;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Integer getPersonType() {
		return personType;
	}

	public void setPersonType(Integer personType) {
		this.personType = personType;
	}

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="moneyAmountId",unique=true,nullable=false)
	public Long getMoneyAmountID() {
		return moneyAmountID;
	}

	public void setMoneyAmountID(Long moneyAmountID) {
		this.moneyAmountID = moneyAmountID;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(BigDecimal exchangerate) {
		this.exchangerate = exchangerate;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}


	
}
