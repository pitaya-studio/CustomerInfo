package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 文件名: ZhifubaokInfo 功能: 支付宝实体 
 */
@Entity
@Table(name = "zhifubao_info")
public class ZhifubaoInfo extends DataEntity {

	private static final long serialVersionUID = 1L;
	/** 批发商类型 */
	public static final Integer PLAT_TYPE_WHO = 0;
	/** 地接社类型 */
	public static final Integer PLAT_YPE_TRA = 1;
	/** 渠道商类型 */
	public static final Integer PLAT_TYPE_GIN = 2;
	/** 供应商类型 */
	public static final Integer PLAT_TYPE_SUP = 3;

	private Long id; // 编号

	/** 支付宝名称 */
	private String name;
	/** 支付宝 账户*/
	private String account;
	/** 备注 */
	private String remarks;
	/** 默认账户标志(0 是 1 否) */
	private String defaultFlag;
	/** 所属公司类型 */
	private Integer platType;
	/** 所属公司id */
	private Long companyId;
	/** 支付宝uuid */
	private String uuid;
	
	public ZhifubaoInfo(){
		super();
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	@Column(name="plat_type")
	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType = platType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
