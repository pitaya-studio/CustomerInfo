package com.trekiz.admin.modules.finance.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 返还差额
 * @author chao.zhang
 *@time 2016/10/13
 */
@Table(name = "return_difference")
@Entity
public class ReturnDifference {
	//id
	private Integer id;
	//uuid
	private String uuid;
	//批发商id
	private Integer companyId;
	//订单id
	private Integer orderId;
	//订单号
	private String orderNum;
	//订单类型
	private Integer orderType;
	//返还差额
	private BigDecimal returnPrice;
	//币种id
	private Integer currencyId;
	//是否达账 0:未达账(已撤销) 1：已达账 2：已驳回
	private Integer toAccountType;
	//达账时间
	private Date toAccountDate;
	private Integer createBy;
	private Date createDate;
	private Integer updateBy;
	private Date updateDate;
	private Integer delFlag;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="company_id")
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="orderNum")
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Column(name="orderType")
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	@Column(name="return_price")
	public BigDecimal getReturnPrice() {
		return returnPrice;
	}
	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}
	
	@Column(name="currency_id")
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
		
	@Column(name="toAccountType")
	public Integer getToAccountType() {
		return toAccountType;
	}
	public void setToAccountType(Integer toAccountType) {
		this.toAccountType = toAccountType;
	}
	
	@Column(name="createBy")
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	
	@Column(name="createDate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="updateBy")
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	
	@Column(name="updateDate")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name="delFlag")
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	@Column(name="toAccountDate")
	public Date getToAccountDate() {
		return toAccountDate;
	}
	public void setToAccountDate(Date toAccountDate) {
		this.toAccountDate = toAccountDate;
	}
	
	
}
