package com.trekiz.admin.agentToOffice.T1.money.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * t1金额实体类
 * @author yakun.bai
 * @Date 2016-10-12
 */
@Entity
@Table(name = "t1_money_amount")
public class T1MoneyAmount {
	
	/** 编号 */
	private Long id;
	/** 流水号 */
	private String serialNum;
	/** 币种ID */
	private Integer currencyId;
	/** 金额 */
	private BigDecimal amount;
	/** 记录人ID */
	private Long createdBy;
	/** 生成时间 */
	private Date createTime;
	/** 删除标识 */
	private String delFlag;

	public T1MoneyAmount() {
		
	}

	public T1MoneyAmount(String serialNum, Integer currencyId, BigDecimal amount, Long createdBy) {
		super();
		this.serialNum = serialNum;
		this.currencyId = currencyId;
		this.amount = amount;
		this.createdBy = createdBy;
		this.createTime = new Date();
		this.delFlag = "0";
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
}
