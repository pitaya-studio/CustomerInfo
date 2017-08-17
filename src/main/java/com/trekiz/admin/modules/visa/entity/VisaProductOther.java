package com.trekiz.admin.modules.visa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;
@Entity
@Table(name = "visa_product_other")
public class VisaProductOther extends DataEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 产品其他费用主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JoinColumn(name="visa_po_id")
	private Integer visaPoId;
	/** 关联产品ID */
	@JoinColumn(name="product_id")
	private Integer productId;
	/** 其他费用名称 */
	@JoinColumn(name="visa_po_name")
	private String visaPoName;
	/** 币种ID（关联currency 币种表） */
	@JoinColumn(name="currency_id")
	private Integer currencyId;
	/** 费用值 */
	@JoinColumn(name="visa_po_pay")
	private BigDecimal visaPoPay;
	
	public Integer getVisaPoId() {
		return visaPoId;
	}
	public void setVisaPoId(Integer visaPoId) {
		this.visaPoId = visaPoId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getVisaPoName() {
		return visaPoName;
	}
	public void setVisaPoName(String visaPoName) {
		this.visaPoName = visaPoName;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public BigDecimal getVisaPoPay() {
		return visaPoPay;
	}
	public void setVisaPoPay(BigDecimal visaPoPay) {
		this.visaPoPay = visaPoPay;
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

	
	
}
