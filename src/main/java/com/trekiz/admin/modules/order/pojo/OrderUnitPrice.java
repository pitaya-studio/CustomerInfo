package com.trekiz.admin.modules.order.pojo;

import java.math.BigDecimal;

import com.trekiz.admin.agentToOffice.T2.entity.Rate;

/**
 * 订单成人、儿童、特殊人群币种及金额
 * @author yakun.bai
 * @Date 2016-9-20
 */
public class OrderUnitPrice {
	
	/** 成人币种 */
	private Long adultCurrencyId;
	/** 儿童币种 */
	private Long childCurrencyId;
	/** 特殊人群币种 */
	private Long specialCurrencyId;
	/** 成人单价 */
	private BigDecimal adultPrice;
	/** 儿童单价 */
	private BigDecimal childPrice;
	/** 特殊人群单价 */
	private BigDecimal specialPrice;
	/** 费率对应费率 */
	private Rate rate;
	/** 成人：零售价是否大于同行价 */
	private boolean adultOverFlag = false;
	/** 儿童：零售价是否大于同行价 */
	private boolean childOverFlag = false;
	/** 特殊人群：零售价是否大于同行价 */
	private boolean specialOverFlag = false;
	/** 成人：同行价减去quauq价差额 */
	private BigDecimal adultDifferencePrice;
	/** 儿童：同行价减去quauq价差额 */
	private BigDecimal childDifferencePrice;
	/** 特殊人群：同行价减去quauq价差额 */
	private BigDecimal specialDifferencePrice;
	
	
	public Long getAdultCurrencyId() {
		return adultCurrencyId;
	}
	public void setAdultCurrencyId(Long adultCurrencyId) {
		this.adultCurrencyId = adultCurrencyId;
	}
	public Long getChildCurrencyId() {
		return childCurrencyId;
	}
	public void setChildCurrencyId(Long childCurrencyId) {
		this.childCurrencyId = childCurrencyId;
	}
	public Long getSpecialCurrencyId() {
		return specialCurrencyId;
	}
	public void setSpecialCurrencyId(Long specialCurrencyId) {
		this.specialCurrencyId = specialCurrencyId;
	}
	public BigDecimal getAdultPrice() {
		return adultPrice;
	}
	public void setAdultPrice(BigDecimal adultPrice) {
		this.adultPrice = adultPrice;
	}
	public BigDecimal getChildPrice() {
		return childPrice;
	}
	public void setChildPrice(BigDecimal childPrice) {
		this.childPrice = childPrice;
	}
	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}
	public Rate getRate() {
		return rate;
	}
	public void setRate(Rate rate) {
		this.rate = rate;
	}
	public boolean isAdultOverFlag() {
		return adultOverFlag;
	}
	public void setAdultOverFlag(boolean adultOverFlag) {
		this.adultOverFlag = adultOverFlag;
	}
	public boolean isChildOverFlag() {
		return childOverFlag;
	}
	public void setChildOverFlag(boolean childOverFlag) {
		this.childOverFlag = childOverFlag;
	}
	public boolean isSpecialOverFlag() {
		return specialOverFlag;
	}
	public void setSpecialOverFlag(boolean specialOverFlag) {
		this.specialOverFlag = specialOverFlag;
	}
	public BigDecimal getAdultDifferencePrice() {
		return adultDifferencePrice;
	}
	public void setAdultDifferencePrice(BigDecimal adultDifferencePrice) {
		this.adultDifferencePrice = adultDifferencePrice;
	}
	public BigDecimal getChildDifferencePrice() {
		return childDifferencePrice;
	}
	public void setChildDifferencePrice(BigDecimal childDifferencePrice) {
		this.childDifferencePrice = childDifferencePrice;
	}
	public BigDecimal getSpecialDifferencePrice() {
		return specialDifferencePrice;
	}
	public void setSpecialDifferencePrice(BigDecimal specialDifferencePrice) {
		this.specialDifferencePrice = specialDifferencePrice;
	}
}
