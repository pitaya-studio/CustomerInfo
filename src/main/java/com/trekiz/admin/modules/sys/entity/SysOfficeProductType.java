package com.trekiz.admin.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trekiz.admin.review.configuration.config.ReviewContext;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 * 
 * 公司与产品类型映射实体
 * 
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
@Entity
@Table(name = "sys_office_product_type")
public class SysOfficeProductType implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@Id
	@Column(name="id",unique=true,nullable=false,length=32)
	private String id;
	/**
	 * 公司id
	 */
	@Column(name="company_id",nullable=false,length=36)
	private String companyId;
	/**
	 * 产品类型
	 */
	@Column(name="product_type",nullable=false)
	private Integer productType;
	/**
	 * 创建者
	 */
	@Column(name="create_by")
	private String createBy;
	/**
	 * 创建时间
	 */
	@Column(name="create_date")
	private Date createDate;
	/**
	 * 更新者
	 */
	@Column(name="update_by")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@Column(name="update_date")
	private Date updateDate;
	/**
	 * 删除标识
	 */
	@Column(name="del_flag",nullable=false)
	private Integer delFlag;

	public SysOfficeProductType() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	@Transient
	public String getProductName(){
		return ReviewContext.productTypeMap.get(productType);
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
