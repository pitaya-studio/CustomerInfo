package com.trekiz.admin.modules.wholesalerbase.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 新增供应商第三步，账户信息
 * @author gao
 *  2015年4月13日
 */
public class WholeOfficeThreeForm {
	
	private Long id;
	/** 默认账户标志(0 是 1 否) */
	private String defaultFlag;
	/** 开户行名称 */
	@NotBlank(message="开户行名称不能为空")
	@Size(min=0, max=40) 
	private String bankName;
	/** 开户行地址 */
	@NotBlank(message="开户行地址不能为空")
	@Size(min=0, max=50) 
	private String bankAddr;
	/** 银行账户 */
	@NotBlank(message="银行账户不能为空")
	@Size(min=0, max=30) 
	private String bankAccountCode;
	/** 账户名 */
	private String accountName;
	/** 备注 */
	private String remarks;
	/** 所属平台类型 区分 0:批发商（供应商），1:地接社（供货商） */
	private Integer platType;
	/** 批发商ID */
	@NotNull(message="没有找到指定批发商")
	private Long belongParentPlatId;
	
	
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBankAddr() {
		return bankAddr;
	}
	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}
	
	public String getBankAccountCode() {
		return bankAccountCode;
	}
	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getPlatType() {
		this.platType = 0;
		return platType;
	}
	public void setPlatType(Integer platType) {
		this.platType = platType;
	}
	
	public Long getBelongParentPlatId() {
		return belongParentPlatId;
	}
	public void setBelongParentPlatId(Long belongParentPlatId) {
		this.belongParentPlatId = belongParentPlatId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
