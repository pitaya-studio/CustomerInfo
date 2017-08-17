package com.trekiz.admin.modules.sys.entity;

import com.trekiz.admin.review.configuration.config.ReviewContext;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 * 
 * 公司与流程类型映射实体
 * 
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
@Entity
@Table(name = "sys_office_process_type")
public class SysOfficeProcessType implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	/**
	 * 公司id
	 */
	@Column(name = "company_id", nullable = false, length = 36)
	private String companyId;
	/**
	 * 流程类型
	 */
	@Column(name = "process_type", nullable = false)
	private Integer processType;
	/**
	 * 创建者
	 */
	@Column(name = "create_by")
	private String createBy;
	/**
	 * 创建时间
	 */
	@Column(name = "create_date")
	private Date createDate;
	/**
	 * 更新者
	 */
	@Column(name = "update_by")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@Column(name = "update_date")
	private Date updateDate;
	/**
	 * 删除标识
	 */
	@Column(name = "del_flag",nullable=false)
	private Integer delFlag;

	public SysOfficeProcessType() {
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

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	@Transient
	public String getProcessName(){
		return ReviewContext.reviewFlowTypeMap.get(processType);
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
