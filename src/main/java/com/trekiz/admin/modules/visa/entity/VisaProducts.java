package com.trekiz.admin.modules.visa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

@Entity
@Table(name = "visa_products")
public class VisaProducts extends DataEntity {
	private static final long serialVersionUID = 1L;
	
	/** 签证产品主键ID */
	private Long id;
	
	/** 产品名称 */
	private String productName;
	
	/** 产品编号 */
	private String productCode;
	
	/**	产品虚拟团号*/
	private String groupCode;

	/** 签证国家ID（关联国家表sys_country） */
	private Integer sysCountryId;
	
	/** 签证类型 */
	private Integer visaType;
	
	/**
	 * 领区联系人
	 */
	
	private String contactPerson;
	
	@Column(name = "contactPerson")
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * 预订方式
	 */
	private String reserveMethod;

	/** 是否需要面试 */
	@Column(name = "needSpotAudition")
	private boolean isNeedSpotAudition;
	/** 币种ID（关联币种表 currency） */
	private Integer currencyId;
	/** 成本价 */
	private BigDecimal visaPrice;
	/** 应收价格 */
	private BigDecimal visaPay;
	
	/** 发票税-0258-qyl*/
	private BigDecimal invoiceQZ;

	/** 产品状态 */
	private Integer productStatus;
	/** 产品发布商ID 未知作用!!!!!*/
	private Integer wholeSalerId;
	/** 产品机构ID */
	private Long  proCompanyId;
	/** 预计工作日 */
	private Integer forecastWorkingTime;
	/** 领区 */
	private String collarZoning;
	/** 入境次数 */
	private Integer enterNum;
	/** 停留时间 */
	private Integer stayTime;
	/** 停留有效时间单位 */
	private String valid_period_unit;
	/** 有效期 */
	private String valid_period;

	private String stayTimeUnit;
	
	/**	产品所属部门id*/
	private Long deptId;

	/** 备注 */
	private String remark;
	/** 其他费用 **/
	private BigDecimal otherCost;
	
	/** 产品分类表ID **/
	private Integer product_type_id; 
	/*预计成本*/
	private String cost;
	/*预计收入*/
	private String income;
	
	/** 原件项目类型 **/
    private String original_Project_Type; 
    /** 填的原件项目类型名字 **/
	private String  original_Project_Name;
	/** 复印项目类型 **/
	private String copy_Project_Type;
	/** 复印项项目类型名字 **/
	private String  copy_Project_Name;
	/**	可见范围*/
	private int activityScope;
	
	/**成本录入审核:0未通过,1待审核,2已通过 4待录入*/
	private Integer review;
	
	/**成本审核当前层级*/
	private Integer nowLevel;
	
	/**成本录入锁定：0:没锁定，1：锁定*/
	private Integer lockStatus;
	/**预报单锁*/
	private String forcastStatus;
	/**应付账期*/
	private Date payableDate;
	/**提成状态 0为计算提成，1已计算提成*/
	private int iscommission;
	
	@Column(name = "iscommission", unique = false, nullable = false)
	public int getIscommission() {
		return iscommission;
	}

	public void setIscommission(int iscommission) {
		this.iscommission = iscommission;
	}

	public String getForcastStatus() {
		return forcastStatus;
	}

	public void setForcastStatus(String forcastStatus) {
		this.forcastStatus = forcastStatus;
	}

	@Column(name = "activity_scope")
	public int getActivityScope() {
		return activityScope;
	}

	public void setActivityScope(int activityScope) {
		this.activityScope = activityScope;
	}

	public String getOriginal_Project_Type() {
		return original_Project_Type;
	}

	public void setOriginal_Project_Type(String originalProjectType) {
		original_Project_Type = originalProjectType;
	}

	public String getCopy_Project_Type() {
		return copy_Project_Type;
	}

	public void setCopy_Project_Type(String copyProjectType) {
		copy_Project_Type = copyProjectType;
	}

	public String getOriginal_Project_Name() {
		return original_Project_Name;
	}

	public void setOriginal_Project_Name(String originalProjectName) {
		original_Project_Name = originalProjectName;
	}


	public String getCopy_Project_Name() {
		return copy_Project_Name;
	}

	public void setCopy_Project_Name(String copyProjectName) {
		copy_Project_Name = copyProjectName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStayTimeUnit() {
		return stayTimeUnit;
	}

	public void setStayTimeUnit(String stayTimeUnit) {
		this.stayTimeUnit = stayTimeUnit;
	}

	public String getValid_period_unit() {
		return valid_period_unit;
	}

	public void setValid_period_unit(String validPeriodUnit) {
		valid_period_unit = validPeriodUnit;
	}

	public String getValid_period() {
		return valid_period;
	}

	public void setValid_period(String validPeriod) {
		valid_period = validPeriod;
	}

	public Integer getProduct_type_id() {
		return product_type_id;
	}

	public void setProduct_type_id(Integer product_type_id) {
		this.product_type_id = product_type_id;
	}

	public BigDecimal getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}

	public String getReserveMethod() {
		return reserveMethod;
	}

	public void setReserveMethod(String reserveMethod) {
		this.reserveMethod = reserveMethod;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public Integer getSysCountryId() {
		return sysCountryId;
	}

	public void setSysCountryId(Integer sysCountryId) {
		this.sysCountryId = sysCountryId;
	}

	public Integer getVisaType() {
		return visaType;
	}

	public void setVisaType(Integer visaType) {
		this.visaType = visaType;
	}

	public boolean isNeedSpotAudition() {
		return isNeedSpotAudition;
	}

	public void setNeedSpotAudition(boolean isNeedSpotAudition) {
		this.isNeedSpotAudition = isNeedSpotAudition;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getVisaPrice() {
		return visaPrice;
	}

	public void setVisaPrice(BigDecimal visaPrice) {
		this.visaPrice = visaPrice;
	}

	public BigDecimal getVisaPay() {
		return visaPay;
	}

	public void setVisaPay(BigDecimal visaPay) {
		this.visaPay = visaPay;
	}
    @Column(name="invoice_qz")
	public BigDecimal getInvoiceQZ() {
		return invoiceQZ;
	}

	public void setInvoiceQZ(BigDecimal invoiceQZ) {
		this.invoiceQZ = invoiceQZ;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public Integer getWholeSalerId() {
		return wholeSalerId;
	}

	public void setWholeSalerId(Integer wholeSalerId) {
		this.wholeSalerId = wholeSalerId;
	}

	public Long getProCompanyId() {
		return proCompanyId;
	}

	public void setProCompanyId(Long proCompanyId) {
		this.proCompanyId = proCompanyId;
	}

	public Integer getForecastWorkingTime() {
		return forecastWorkingTime;
	}

	public void setForecastWorkingTime(Integer forecastWorkingTime) {
		this.forecastWorkingTime = forecastWorkingTime;
	}
	//@Column(name = "collarZoning")
	public String getCollarZoning() {
		return collarZoning;
	}

	public void setCollarZoning(String collarZoning) {
		this.collarZoning = collarZoning;
	}

	public Integer getEnterNum() {
		return enterNum;
	}

	public void setEnterNum(Integer enterNum) {
		this.enterNum = enterNum;
	}

	public Integer getStayTime() {
		return stayTime;
	}

	public void setStayTime(Integer stayTime) {
		this.stayTime = stayTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}	

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Date getPayableDate() {
		return payableDate;
	}

	public void setPayableDate(Date payableDate) {
		this.payableDate = payableDate;
	}
}
