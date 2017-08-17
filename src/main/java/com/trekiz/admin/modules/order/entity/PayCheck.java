package com.trekiz.admin.modules.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "pay_check")
public class PayCheck extends DataEntityTTS {
	private static final long serialVersionUID = -8130688058044010794L;
	/** 主键ID */
	private String id;
	/** 付款单位 */
	private String payerName;
	/** 支票号 */
	private String checkNumber;
	/** 开票日期 */
	private Date invoiceDate;

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "payer_name")
	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	@Column(name = "check_number")
	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Column(name = "invoice_date")
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
}
