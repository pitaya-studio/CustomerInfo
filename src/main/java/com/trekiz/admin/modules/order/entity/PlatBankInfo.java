package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 文件名: PlatBankInfo 功能: 平台账户信息实体 修改记录:
 * 
 * @author chy
 * @DateTime 2014年11月4日15:14:06
 * @version 1.0
 */
@Entity
@Table(name = "plat_bank_info")
public class PlatBankInfo extends DataEntity {

	private static final long serialVersionUID = 1L;
	/** 批发商类型 */
	public static final Integer PLAT_BANK_TYPE_WHO = 0;
	/** 地接社类型 */
	public static final Integer PLAT_BANK_TYPE_TRA = 1;
	/** 渠道商类型 */
	public static final Integer PLAT_BANK_TYPE_GIN = 2;
	/** 供应商类型 */
	public static final Integer PLAT_BANK_TYPE_SUP = 3;
	/** 银行类型 ： 境内*/
	public static final Long BELONG_TYPE_DOMESTIC=1L;
	/** 银行类型 ： 境外*/
	public static final Long BELONG_TYPE_OVERSEAS=2L;

	private Long id; // 编号

	/** 账户名 */
	private String accountName;
	/** 默认账户标志(0 是 1 否) */
	private String defaultFlag="1";
	/** 银行id */
	private Long bankId;
	/** 开户行code */
	private String bankCode;
	/** 开户行名称 */
	private String bankName;
	/** 开户行地址 */
	private String bankAddr;
	/** 银行账户 */
	private String bankAccountCode;
	/** 所属平台类型 */
	private Integer platType;
	/** 所属平台id */
	private Long beLongPlatId;
	/** 所属的总平台id */
	private Long belongParentPlatId;
	/** 所属类型: 1 境内 2 境外 */
	private Long belongType;
	/** 备注 */
	private String remarks;
	
	private String rounting;
	private String swiftNum;
	private String phoneNum;
	
	/**538需求 账户支付类型 1：微信 2：支付宝 3：银行卡*/
	private Integer accountPayType;
	
	
	
	public String getRounting() {
		return rounting;
	}

	public void setRounting(String rounting) {
		this.rounting = rounting;
	}

	public String getSwiftNum() {
		return swiftNum;
	}

	public void setSwiftNum(String swiftNum) {
		this.swiftNum = swiftNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public PlatBankInfo(){
		super();
	}
	
	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBeLongPlatId() {
		return beLongPlatId;
	}

	public void setBeLongPlatId(Long beLongPlatId) {
		this.beLongPlatId = beLongPlatId;
	}

	public String getBankAccountCode() {
		return bankAccountCode;
	}

	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
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

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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

	@Column(name="platType")
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

	public Long getBelongType() {
		return belongType;
	}

	public void setBelongType(Long belongType) {
		this.belongType = belongType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="accountPayType")
	public Integer getAccountPayType() {
		return accountPayType;
	}

	public void setAccountPayType(Integer accountPayType) {
		this.accountPayType = accountPayType;
	}
}
