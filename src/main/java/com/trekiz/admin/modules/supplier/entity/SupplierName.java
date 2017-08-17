package com.trekiz.admin.modules.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "suppliername_view")
public class SupplierName  {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;	

	// 地接社类型
	private Integer supplyType;
	// 地接社ID
	private Long supplierId;
	// 地接社名称
	private String supplierName;
	// 所属公司ID
	private Long companyId;

	@Column(name="supplyType",unique=false,nullable=true)
	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="supplierId",unique=false,nullable=true)
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name="supplierName",unique=false,nullable=true)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
