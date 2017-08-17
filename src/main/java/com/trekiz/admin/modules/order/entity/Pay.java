package com.trekiz.admin.modules.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "pay")
public class Pay extends DataEntityTTS {
	private static final long serialVersionUID = 6889036336229097632L;
	/** 主键ID */
	private String id;
	/** 支付方式（现金、支票、汇款等） */
	private Integer payType;
	/** 支付方式表ID */
	private String payTypeId;
	/** 付款金额UUID */
	private String moneySerialNum;
	/** 支付凭证附件id */
	private String payVoucher;
	/** 备注信息 */
	private String remarks;

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "pay_type_Id")
	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	@Column(name = "money_serial_num")
	public String getMoneySerialNum() {
		return moneySerialNum;
	}

	public void setMoneySerialNum(String moneySerialNum) {
		this.moneySerialNum = moneySerialNum;
	}

	@Column(name = "pay_voucher")
	public String getPayVoucher() {
		return payVoucher;
	}

	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
