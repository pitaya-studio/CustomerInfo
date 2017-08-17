package com.trekiz.admin.modules.sys.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "currency")
public class Currency extends DataEntityTTS {
	private static final long serialVersionUID = 1L;
	
	/** 币种ID */
	private Long id;
	
	/** 币种名称 */
	private String currencyName;
	
	/** 币种标识，例如：美元：$ */
	private String currencyMark;
	
	/** 币种样式标识 */
	private String currencyStyle;
	
	/** 汇率 */
	private BigDecimal currencyExchangerate;
	
	/** 换汇汇率-现金收款 */
	private BigDecimal convertCash;
	
	/** 换汇汇率-对公收款 */
	private BigDecimal convertForeign;
	
	/** 换汇汇率-中行折算价 */
	private BigDecimal convertAbc;
	
	/** 换汇汇率-公司最低汇率标准 */
	private BigDecimal convertLowest;
	
	/** 添加公司ID */
	private Long createCompanyId;
	
	/** 备注 */
	private String remark;
	
	/** 是否在前台页面显示 */
	private String displayFlag;
	
	/** 排序字段,默认50*/
	private Integer sort = 50;
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Currency() {
		super();
		this.displayFlag = SHOW;
	}
	
	public Currency(Long currency_id) {
		this();
		this.id = currency_id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="currency_id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="currency_name")
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	@Column(name="currency_mark")
	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	
	@Column(name="currency_style")
	public String getCurrencyStyle() {
		return currencyStyle;
	}

	public void setCurrencyStyle(String currencyStyle) {
		this.currencyStyle = currencyStyle;
	}
	
	@Column(name="currency_exchangerate")
	public BigDecimal getCurrencyExchangerate() {
		return currencyExchangerate;
	}
	public void setCurrencyExchangerate(BigDecimal currencyExchangerate) {
		this.currencyExchangerate = currencyExchangerate;
	}
	
	@Column(name="convert_cash")
	public BigDecimal getConvertCash() {
		return convertCash;
	}

	public void setConvertCash(BigDecimal convertCash) {
		this.convertCash = convertCash;
	}

	@Column(name="convert_foreign")
	public BigDecimal getConvertForeign() {
		return convertForeign;
	}

	public void setConvertForeign(BigDecimal convertForeign) {
		this.convertForeign = convertForeign;
	}

	@Column(name="convert_abc")
	public BigDecimal getConvertAbc() {
		return convertAbc;
	}

	public void setConvertAbc(BigDecimal convertAbc) {
		this.convertAbc = convertAbc;
	}

	@Column(name="convert_lowest")
	public BigDecimal getConvertLowest() {
		return convertLowest;
	}

	public void setConvertLowest(BigDecimal convertLowest) {
		this.convertLowest = convertLowest;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="create_company_id")
	public Long getCreateCompanyId() {
		return createCompanyId;
	}
	public void setCreateCompanyId(Long createCompanyId) {
		this.createCompanyId = createCompanyId;
	}
	
	@Column(name="display_flag")
	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
}
