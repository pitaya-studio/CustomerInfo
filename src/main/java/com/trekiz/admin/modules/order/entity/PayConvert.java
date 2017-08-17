package com.trekiz.admin.modules.order.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "pay_convert")
public class PayConvert extends DataEntityTTS {
	private static final long serialVersionUID = 4088573883400090962L;
	/** 编号 */
	private Long id;
	/** 流水号 */
	private String serialNum;
	/** 币种ID */
	private Integer currencyId;
	/** 换汇汇率 */
	private BigDecimal convertLowest;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "serialNum")
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@Column(name = "currencyId")
	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "convert_lowest")
	public BigDecimal getConvertLowest() {
		return convertLowest;
	}

	public void setConvertLowest(BigDecimal convertLowest) {
		this.convertLowest = convertLowest;
	}
}
