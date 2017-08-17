package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "pay_remittance")
public class PayRemittance extends DataEntityTTS {
	private static final long serialVersionUID = -1824032986660465142L;
	/** 主键ID */
	private String id;
	/** 开户行名称 */
	private String bankName;
	/** 开户行账户 */
	private String bankAccount;
	/** 转入行名称 */
	private String tobankName;
	/** 转入行账户 */
	private String tobankAccount;

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "bank_name")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "bank_account")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Column(name = "tobank_name")
	public String getTobankName() {
		return tobankName;
	}

	public void setTobankName(String tobankName) {
		this.tobankName = tobankName;
	}

	@Column(name = "tobank_account")
	public String getTobankAccount() {
		return tobankAccount;
	}

	public void setTobankAccount(String tobankAccount) {
		this.tobankAccount = tobankAccount;
	}
}
