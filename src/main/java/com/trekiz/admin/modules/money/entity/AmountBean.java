package com.trekiz.admin.modules.money.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.trekiz.admin.modules.sys.entity.Currency;

/**
 * @ClassName: AmountBean
 * @Description: 金额bean，用以组织特定场合下“金额和币种”相关信息 TODO 待后续开发补充
 * @author yang.jiang
 * @date 2016-2-16 19:44:47
 *
 */
public class AmountBean {
	
	/** 金额 */
	private BigDecimal amount;
	/** 币种ID */
	private Integer currencyId;
	/** 币种名称 */
	private String currencyName;
	/** 币种符号 */
	private String currencyMark;
	/** 业务id */
	private Long busindessId;
	/** 业务类型 TODO 待后续开发补充 */
	private Integer busindessType;
	

	public AmountBean() {
	}

	/**
	 * 构造方法
	 * @param amount 金额
	 * @param currencyId 币种ID
	 * @param currencyName 币种名称
	 * @param currencyMark 币种符号
	 * @param busindessId 业务id
	 * @param busindessType 业务类型
	 */
	public AmountBean(BigDecimal amount, Integer currencyId,
			String currencyName, String currencyMark, Long busindessId,
			Integer busindessType) {
		super();
		this.amount = amount;
		this.currencyId = currencyId;
		this.currencyName = currencyName;
		this.currencyMark = currencyMark;
		this.busindessId = busindessId;
		this.busindessType = busindessType;
	}
	
	/**
	 * 构造方法(指定币种)
	 * @param amount 金额
	 * @param currencyId 币种ID
	 * @param currencyName 币种名称
	 * @param currencyMark 币种符号
	 * @param busindessId 业务id
	 * @param busindessType 业务类型
	 */
	public AmountBean(BigDecimal amount, Currency currency) {
		super();
		this.amount = amount;
		this.currencyId = Integer.parseInt(currency.getId().toString());
		this.currencyName = currency.getCurrencyName();
		this.currencyMark = currency.getCurrencyMark();
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}

	public Long getBusindessId() {
		return busindessId;
	}
	public void setBusindessId(Long busindessId) {
		this.busindessId = busindessId;
	}

	public Integer getBusindessType() {
		return busindessType;
	}
	public void setBusindessType(Integer busindessType) {
		this.busindessType = busindessType;
	}
	
}
