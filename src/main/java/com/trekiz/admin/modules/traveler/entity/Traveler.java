/**
 *
 */
package com.trekiz.admin.modules.traveler.entity;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 文件名: Traveler.java 功能: 游客信息pojo
 * 
 * 修改记录:
 * 
 * @author xuziqian
 * @DateTime 2014-1-16 上午10:52:14
 * @version 1.0
 */
@Entity
@Table(name = "traveler")
public class Traveler extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int APPLY_ORDER_TYPE = 0;
	private Long id; // 编号

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**  */
	private Long orderId;
	/**  */
	private String nameSpell;
	/**  */
	private Integer hotelDemand;
	/**  */
	private BigDecimal singleDiff;

	private Currency singleDiffCurrency;
	/*已退票标志 0代表已退票*/
	private String isAirticketFlag;
	/*原始结算价UUID*/
	private String originalPayPriceSerialNum;
	/**游客已付款流水号*/
	private String payedMoneySerialNum;
	/**游客成本价流水号*/
	private String costPriceSerialNum;
	/** 游客结算价流水号*/
	private String payPriceSerialNum;
	/**游客返佣费用流水号*/
	private String rebatesMoneySerialNum;
	//wxw added 返佣币种ID 展示用
	private String rebatesCurrencyID;
	private String rebatesAmount;
	
	//骡子假期 544需求 添加“签发地”“签发日期” 字段
	private String issuePlace1;
	private Date issueDate;
	private String hometown;
	
	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getIssuePlace1() {
		return issuePlace1;
	}

	public void setIssuePlace1(String issuePlace1) {
		this.issuePlace1 = issuePlace1;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**游客借款uuid*/
	private String jkSerialNum;
	
	/**达账金额 UUID*/
	private String accountedMoney;
	//wxw added 20150302
	/* 主订单ID */
	private Long mainOrderId;
	/* 主订单游客ID，当此订单的游客作为子订单的游客时，
	 * 修改游客信息时主订单也要同步修改，
	 * 通过该字段进行关联查找 */
	private Long mainOrderTravelerId;
	
	/**
	 * 申请额外优惠 applied_discount_price 
	 */
	private BigDecimal appliedDiscountPrice;
	/**
	 * 额内优惠 fixed_discount_price
	 */
	private BigDecimal fixedDiscountPrice = new BigDecimal(0.00);
	/**
	 * 累计审批通过的优惠金额 reviewed_discount_price
	 */
	private BigDecimal reviewedDiscountPrice;
	/**
	 * 原始额定优惠：报名时的团期优惠 org_discount_price
	 */
	private BigDecimal orgDiscountPrice = new BigDecimal(0.00);
	/**
	 * 是否允许手动修改优惠。只要该游客所在的订单有一条正在审批，或者该游客的优惠申请被审批通过，都不允许再修改。
	 */
	private Boolean isAllowModifyDiscount;
	/** 批发商ID */
	private Long companyId;
	
	private Integer paymentType;
	@Column(name="payment_type")
	public Integer getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	@Column(name="rebates_moneySerialNum")
	public String getRebatesMoneySerialNum() {
		return rebatesMoneySerialNum;
	}
	private List<Currency> currencies;
	
	@Transient
	public List<Currency> getCurrencies()
	{
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies)
	{
		this.currencies = currencies;
	}
	
	
	public void setRebatesMoneySerialNum(String rebatesMoneySerialNum) {
		this.rebatesMoneySerialNum = rebatesMoneySerialNum;
	}

	@Column(name="costPriceSerialNum")
	public String getCostPriceSerialNum() {
		return costPriceSerialNum;
	}

	public void setCostPriceSerialNum(String costPriceSerialNum) {
		this.costPriceSerialNum = costPriceSerialNum;
	}

	@Column(name="original_payPriceSerialNum")
	public String getOriginalPayPriceSerialNum() {
		return originalPayPriceSerialNum;
	}

	public void setOriginalPayPriceSerialNum(String originalPayPriceSerialNum) {
		this.originalPayPriceSerialNum = originalPayPriceSerialNum;
	}

	public String getIsAirticketFlag() {
		return isAirticketFlag;
	}

	public void setIsAirticketFlag(String isAirticketFlag) {
		this.isAirticketFlag = isAirticketFlag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "singleDiffCurrency", referencedColumnName = "currency_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Currency getSingleDiffCurrency() {
		return singleDiffCurrency;
	}

	public void setSingleDiffCurrency(Currency singleDiffCurrency) {
		this.singleDiffCurrency = singleDiffCurrency;
	}

	/**  */
	private String papersType;
	/**  */
	private String idCard;
	/**  */
	private Integer nationality;
	/** 中文职位名称 */
	private String positionCn;
	/** 英文职位名称*/
	private String positionEn;
	/** 护照签发地**/
	private String passportPlace;
	/**  */
	private Integer sex;
	/**  */
	private Date birthDay;
	/**  */
	private Date issuePlace;
	/**  */
	private Date validityDate;
	/**  */
	private String telephone;
	/** 备注  */
	private String remark;
	/** 价格备注  需求299  */
	private String priceRemark;
	/** 单价 */
	private BigDecimal srcPrice;

	private Currency srcPriceCurrency;
	
	/**
	 * 结算价
	 */
	private BigDecimal jsPrice;
	
	@Transient
	public BigDecimal getJsPrice() {
		return jsPrice;
	}

	public void setJsPrice(BigDecimal jsPrice) {
		this.jsPrice = jsPrice;
	}

	/**
	 * 结算价
	 */
	
	private List<String> costsettlementPrice;
	
	@Transient
	public List<String> getCostsettlementPrice() {
		return costsettlementPrice;
	}

	public void setCostsettlementPrice(List<String> costsettlementPrice) {
		this.costsettlementPrice = costsettlementPrice;
	}

	/** 签证 */
	private Visa visa;
	
	/** 其他费用*/
	private List<Costchange> costChange;
	
	private String  borrowMoney;
	private String  borrowMoneyCheckStatus;
	private String totalRebateJe;
	/*批量借护照批次号*/
	private String borrowPassportBatchNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "srcPriceCurrency", referencedColumnName = "currency_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Currency getSrcPriceCurrency() {
		return srcPriceCurrency;
	}

	public void setSrcPriceCurrency(Currency srcPriceCurrency) {
		this.srcPriceCurrency = srcPriceCurrency;
	}

	private Integer personType;
	/** 护照号码 */
	private String passportCode;
	/** 护照有效期 */
	private Date passportValidity;
	/** 护照种类 1：因公护照 2：因私护照 */
	private String passportType;

	public String getPassportType() {
		return passportType;
	}

	public void setPassportType(String passportType) {
		this.passportType = passportType;
	}

	/** 是否需要联运 0：不需要，1：需要 */
	private Integer intermodalType;
	/** 产品联运信息表主键 */
	private Long intermodalId;
	/** 护照状态 1:借出;2:归还客户;3:未签收;3:已签收*/
	private Integer passportStatus;
	private Integer singleDiffNight;
	private Integer orderType;
	
	/**
	 * 删除标记 0:正常 1：删除 2:退团审核中 3：已退团 4：转团审核中 5：已转团 update by ruyi.chen 2014-11-13
	 */
	private Integer delFlag;
	
	
	/** 是否为参团游客  0：单办签游客，1：参团游客 */
	private Integer isjoingroup = 0;

	private String reviewUuid;

	@Column(name="review_uuid")
	public String getReviewUuid() {
		return reviewUuid;
	}

	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}

	public Integer getIsjoingroup() {
		return isjoingroup;
	}

	public void setIsjoingroup(Integer isjoingroup) {
		this.isjoingroup = isjoingroup;
	}

	private String subtractMoneySerialNum;	//扣减金额
	
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getPersonType() {
		return personType;
	}

	public void setPersonType(Integer personType) {
		this.personType = personType;
	}

	public Traveler() {
		super();
		this.delFlag = 0;
	}

	public Traveler(Long id) {
		this();
		this.id = id;
	}

	public BigDecimal getSrcPrice() {
		return srcPrice;
	}

	public void setSrcPrice(BigDecimal srcPrice) {
		this.srcPrice = srcPrice;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setNameSpell(String nameSpell) {
		this.nameSpell = nameSpell;
	}

	@Length(min = 0, max = 100)
	public String getNameSpell() {
		return this.nameSpell;
	}

	public void setHotelDemand(Integer hotelDemand) {
		this.hotelDemand = hotelDemand;
	}

	public Integer getHotelDemand() {
		return this.hotelDemand;
	}

	public void setSingleDiff(BigDecimal singleDiff) {
		this.singleDiff = singleDiff;
	}

	public BigDecimal getSingleDiff() {
		return this.singleDiff;
	}

	public void setPapersType(String papersType) {
		this.papersType = papersType;
	}

	public String getPapersType() {
		return this.papersType;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIdCard() {
		return this.idCard;
	}

	public void setNationality(Integer nationality) {
		this.nationality = nationality;
	}

	public Integer getNationality() {
		return this.nationality;
	}

	public String getPositionEn() {
		return positionEn;
	}

	public void setPositionEn(String positionEn) {
		this.positionEn = positionEn;
	}

	public String getPositionCn() {
		return positionCn;
	}

	public void setPositionCn(String positionCn) {
		this.positionCn = positionCn;
	}

	public String getPassportPlace() {
		return passportPlace;
	}

	public void setPassportPlace(String passportPlace) {
		this.passportPlace = passportPlace;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSex() {
		return this.sex;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Date getBirthDay() {
		return this.birthDay;
	}

	public void setIssuePlace(Date issuePlace) {
		this.issuePlace = issuePlace;
	}

	public Date getIssuePlace() {
		return this.issuePlace;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Date getValidityDate() {
		return this.validityDate;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Length(min = 0, max = 50)
	public String getTelephone() {
		return this.telephone;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Length(min = 0, max = 65535)
	public String getRemark() {
		return this.remark;
	}
	
	@Column(name="price_remark")
	public String getPriceRemark() {
		return priceRemark;
	}

	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}

	public String getPassportCode() {
		return passportCode;
	}

	public void setPassportCode(String passportCode) {
		this.passportCode = passportCode;
	}

	public Date getPassportValidity() {
		return passportValidity;
	}

	public void setPassportValidity(Date passportValidity) {
		this.passportValidity = passportValidity;
	}

	public Integer getIntermodalType() {
		return intermodalType;
	}

	public void setIntermodalType(Integer intermodalType) {
		this.intermodalType = intermodalType;
	}

	public Integer getSingleDiffNight() {
		return singleDiffNight;
	}

	public void setSingleDiffNight(Integer singleDiffNight) {
		this.singleDiffNight = singleDiffNight;
	}

	public Long getIntermodalId() {
		return intermodalId;
	}

	public void setIntermodalId(Long intermodalId) {
		this.intermodalId = intermodalId;
	}

	@Column(name = "order_type")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "payPriceSerialNum")
	public String getPayPriceSerialNum() {
		return payPriceSerialNum;
	}

	public void setPayPriceSerialNum(String payPriceSerialNum) {
		this.payPriceSerialNum = payPriceSerialNum;
	}

	@Transient
	public Visa getVisa() {
		return visa;
	}

	public void setVisa(Visa visa) {
		this.visa = visa;
	}

	@Transient
	public List<Costchange> getCostChange() {
		return costChange;
	}

	public void setCostChange(List<Costchange> costChange) {
		this.costChange = costChange;
	}

	public Integer getPassportStatus() {
		return passportStatus;
	}

	public void setPassportStatus(Integer passportStatus) {
		this.passportStatus = passportStatus;
	}
    
	@Transient
	public String getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(String borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	@Transient
	public String getPayPriceSerialNumInfo() {
		return payPriceSerialNumInfo;
	}

	public void setPayPriceSerialNumInfo(String payPriceSerialNumInfo) {
		this.payPriceSerialNumInfo = payPriceSerialNumInfo;
	}
	/**
	 * 转团页面中，求取应收结算价专用
	 */
	private String payPriceSerialNumInfo;

	@Transient
	public String getTotalRebateJe() {
		return totalRebateJe;
	}

	public void setTotalRebateJe(String totalRebateJe) {
		this.totalRebateJe = totalRebateJe;
	}

	@Transient
	public String getBorrowMoneyCheckStatus() {
		return borrowMoneyCheckStatus;
	}

	public void setBorrowMoneyCheckStatus(String borrowMoneyCheckStatus) {
		this.borrowMoneyCheckStatus = borrowMoneyCheckStatus;
	}

	@Column(name = "payed_moneySerialNum")
	public String getPayedMoneySerialNum() {
		return payedMoneySerialNum;
	}

	public void setPayedMoneySerialNum(String payedMoneySerialNum) {
		this.payedMoneySerialNum = payedMoneySerialNum;
	}
	
	@Column(name = "main_order_id")
	public Long getMainOrderId() {
		return mainOrderId;
	}

	public void setMainOrderId(Long mainOrderId) {
		this.mainOrderId = mainOrderId;
	}
	
	@Column(name = "main_order_travelerid")
	public Long getMainOrderTravelerId() {
		return mainOrderTravelerId;
	}

	public void setMainOrderTravelerId(Long mainOrderTravelerId) {
		this.mainOrderTravelerId = mainOrderTravelerId;
	}

	@Column(name="accounted_money")
	public String getAccountedMoney() {
		return accountedMoney;
	}

	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Column(name="borrow_passport_batch_no")
	public String getBorrowPassportBatchNo() {
		return borrowPassportBatchNo;
	}

	public void setBorrowPassportBatchNo(String borrowPassportBatchNo) {
		this.borrowPassportBatchNo = borrowPassportBatchNo;
	}

	@Column(name="jkSerialNum")
	public String getJkSerialNum() {
		return jkSerialNum;
	}

	public void setJkSerialNum(String jkSerialNum) {
		this.jkSerialNum = jkSerialNum;
	}

	@Column(name="subtract_moneySerialNum")
	public String getSubtractMoneySerialNum() {
		return subtractMoneySerialNum;
	}

	public void setSubtractMoneySerialNum(String subtractMoneySerialNum) {
		this.subtractMoneySerialNum = subtractMoneySerialNum;
	}

	@Transient
	public String getRebatesCurrencyID() {
		return rebatesCurrencyID;
	}

	public void setRebatesCurrencyID(String rebatesCurrencyID) {
		this.rebatesCurrencyID = rebatesCurrencyID;
	}

	@Transient
	public String getRebatesAmount() {
		return rebatesAmount;
	}

	public void setRebatesAmount(String rebatesAmount) {
		this.rebatesAmount = rebatesAmount;
	}

	@Column(name="applied_discount_price")
	public BigDecimal getAppliedDiscountPrice() {
		return appliedDiscountPrice;
	}

	public void setAppliedDiscountPrice(BigDecimal appliedDiscountPrice) {
		this.appliedDiscountPrice = appliedDiscountPrice;
	}

	@Column(name="fixed_discount_price")
	public BigDecimal getFixedDiscountPrice() {
		return fixedDiscountPrice;
	}

	public void setFixedDiscountPrice(BigDecimal fixedDiscountPrice) {
		this.fixedDiscountPrice = fixedDiscountPrice;
	}		
	
	@Column(name="reviewed_discount_price")
	public BigDecimal getReviewedDiscountPrice() {
		return reviewedDiscountPrice;
	}

	public void setReviewedDiscountPrice(BigDecimal reviewedDiscountPrice) {
		this.reviewedDiscountPrice = reviewedDiscountPrice;
	}

	@Column(name="org_discount_price")
	public BigDecimal getOrgDiscountPrice() {
		return orgDiscountPrice;
	}
	
	public void setOrgDiscountPrice(BigDecimal orgDiscountPrice) {
		this.orgDiscountPrice = orgDiscountPrice;
	}

	@Column(name="companyId")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Transient
	public Boolean getIsAllowModifyDiscount() {
		return isAllowModifyDiscount;
	}

	public void setIsAllowModifyDiscount(Boolean isAllowModifyDiscount) {
		this.isAllowModifyDiscount = isAllowModifyDiscount;
	}
	
	@PrePersist
	public void prePersist() {
		if (this.companyId == null) {
			this.companyId = UserUtils.getUser().getCompany().getId();
		}
	}
}
