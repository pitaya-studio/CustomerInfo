package com.trekiz.admin.agentToOffice.T1.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 预报名实体类
 * @author yakun.bai
 * @Date 2016-10-12
 */
@Entity
@Table(name = "t1_pre_order")
@DynamicInsert
public class T1PreOrder extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
    /** 产品id */
    private Long productId;
    /** 产品出团信息表id */
    private Long productGroupId;
    /** 渠道id */
    private Long agentId;
    /** T2订单id */
    private Long orderId;
    /** T2订单预订日期 */
    private Date orderTimeForT2;
    /** 订单单号 */
    private String orderNum;
    /** 预订日期 */
    private Date orderTime;
    /** 预订人数 */
    private Integer orderPersonNum;
    /** 成人预订人数 */
    private Integer orderPersonNumAdult;
    /** 儿童预订人数 */
    private Integer orderPersonNumChild;
    /** 特殊预订人数 */
    private Integer orderPersonNumSpecial;
    /** 下订单时成人的产品结算价 */
    private String adultPrice;
    /** 下订单时儿童的产品结算价 */
    private String childPrice;
    /** 下订单时特殊的产品结算价 */
    private String specialPrice;
    /** 下订单时成人的产品结算价 */
    private String companyAdultPrice;
    /** 下订单时儿童的产品结算价 */
    private String companyChildPrice;
    /** 下订单时特殊的产品结算价 */
    private String companySpecialPrice;
    /** quauq产品费率类型 */
	private Integer quauqProductChargeType;
	/** quauq产品费率 */
	private BigDecimal quauqProductChargeRate;
	/** quauq其他费用费率类型 */
	private Integer quauqOtherChargeType;
	/** quauq其他费用费率 */
	private BigDecimal quauqOtherChargeRate;
	/** 总社或集团产品费率类型 */
	private Integer partnerProductChargeType;
	/** 总社或集团产品费率 */
	private BigDecimal partnerProductChargeRate;
	/** 总社或集团其他费用费率类型 */
	private Integer partnerOtherChargeType;
	/** 总社或集团其他费用费率 */
	private BigDecimal partnerOtherChargeRate;
	/** 抽成费用费率类型 */
	private Integer cutChargeType;
	/** 抽成费用费率 */
	private BigDecimal cutChargeRate;
    /** 订单状态：1 待处理；2 已下单；3 已取消；4 已删除 */
    private Integer orderStatus;
	/** 销售id*/
	private Long salerId;
	/** 销售名称*/
	private String salerName;
    /** 实际结算价UUID */
    private String totalMoney;
    /** 系统结算价UUID */
    private String companyMoney;
    /** 门店结算价返还差额UUID */
    private String differenceMoney;
    /** 支付信息ID */
    private Long payId;
    /** 是否已被查看 */
    private Integer hasSeen = 0;
    /** 备注 */
    private String remark;
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="productId")
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	@Column(name="productGroupId")
	public Long getProductGroupId() {
		return productGroupId;
	}
	public void setProductGroupId(Long productGroupId) {
		this.productGroupId = productGroupId;
	}
	@Column(name="agentId")
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	@Column(name="orderId")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	@Column(name="orderTimeForT2")
	public Date getOrderTimeForT2() {
		return orderTimeForT2;
	}
	public void setOrderTimeForT2(Date orderTimeForT2) {
		this.orderTimeForT2 = orderTimeForT2;
	}
	@Column(name="orderNum")
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	@Column(name="orderTime")
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	@Column(name="orderPersonNum")
	public Integer getOrderPersonNum() {
		return orderPersonNum;
	}
	public void setOrderPersonNum(Integer orderPersonNum) {
		this.orderPersonNum = orderPersonNum;
	}
	@Column(name="orderPersonNumAdult")
	public Integer getOrderPersonNumAdult() {
		return orderPersonNumAdult;
	}
	public void setOrderPersonNumAdult(Integer orderPersonNumAdult) {
		this.orderPersonNumAdult = orderPersonNumAdult;
	}
	@Column(name="orderPersonNumChild")
	public Integer getOrderPersonNumChild() {
		return orderPersonNumChild;
	}
	public void setOrderPersonNumChild(Integer orderPersonNumChild) {
		this.orderPersonNumChild = orderPersonNumChild;
	}
	@Column(name="orderPersonNumSpecial")
	public Integer getOrderPersonNumSpecial() {
		return orderPersonNumSpecial;
	}
	public void setOrderPersonNumSpecial(Integer orderPersonNumSpecial) {
		this.orderPersonNumSpecial = orderPersonNumSpecial;
	}
	@Column(name="adultPrice")
	public String getAdultPrice() {
		return adultPrice;
	}
	public void setAdultPrice(String adultPrice) {
		this.adultPrice = adultPrice;
	}
	@Column(name="childPrice")
	public String getChildPrice() {
		return childPrice;
	}
	public void setChildPrice(String childPrice) {
		this.childPrice = childPrice;
	}
	@Column(name="specialPrice")
	public String getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}
	@Column(name="companyAdultPrice")
	public String getCompanyAdultPrice() {
		return companyAdultPrice;
	}
	public void setCompanyAdultPrice(String companyAdultPrice) {
		this.companyAdultPrice = companyAdultPrice;
	}
	@Column(name="companyChildPrice")
	public String getCompanyChildPrice() {
		return companyChildPrice;
	}
	public void setCompanyChildPrice(String companyChildPrice) {
		this.companyChildPrice = companyChildPrice;
	}
	@Column(name="companySpecialPrice")
	public String getCompanySpecialPrice() {
		return companySpecialPrice;
	}
	public void setCompanySpecialPrice(String companySpecialPrice) {
		this.companySpecialPrice = companySpecialPrice;
	}
	@Column(name="quauq_product_charge_type")
	public Integer getQuauqProductChargeType() {
		return quauqProductChargeType;
	}
	public void setQuauqProductChargeType(Integer quauqProductChargeType) {
		this.quauqProductChargeType = quauqProductChargeType;
	}
	@Column(name="quauq_product_charge_rate")
	public BigDecimal getQuauqProductChargeRate() {
		return quauqProductChargeRate;
	}
	public void setQuauqProductChargeRate(BigDecimal quauqProductChargeRate) {
		this.quauqProductChargeRate = quauqProductChargeRate;
	}
	@Column(name="quauq_other_charge_type")
	public Integer getQuauqOtherChargeType() {
		return quauqOtherChargeType;
	}
	public void setQuauqOtherChargeType(Integer quauqOtherChargeType) {
		this.quauqOtherChargeType = quauqOtherChargeType;
	}
	@Column(name="quauq_other_charge_rate")
	public BigDecimal getQuauqOtherChargeRate() {
		return quauqOtherChargeRate;
	}
	public void setQuauqOtherChargeRate(BigDecimal quauqOtherChargeRate) {
		this.quauqOtherChargeRate = quauqOtherChargeRate;
	}
	@Column(name="partner_product_charge_type")
	public Integer getPartnerProductChargeType() {
		return partnerProductChargeType;
	}
	public void setPartnerProductChargeType(Integer partnerProductChargeType) {
		this.partnerProductChargeType = partnerProductChargeType;
	}
	@Column(name="partner_product_charge_rate")
	public BigDecimal getPartnerProductChargeRate() {
		return partnerProductChargeRate;
	}
	public void setPartnerProductChargeRate(BigDecimal partnerProductChargeRate) {
		this.partnerProductChargeRate = partnerProductChargeRate;
	}
	@Column(name="partner_other_charge_type")
	public Integer getPartnerOtherChargeType() {
		return partnerOtherChargeType;
	}
	public void setPartnerOtherChargeType(Integer partnerOtherChargeType) {
		this.partnerOtherChargeType = partnerOtherChargeType;
	}
	@Column(name="partner_other_charge_rate")
	public BigDecimal getPartnerOtherChargeRate() {
		return partnerOtherChargeRate;
	}
	public void setPartnerOtherChargeRate(BigDecimal partnerOtherChargeRate) {
		this.partnerOtherChargeRate = partnerOtherChargeRate;
	}
	@Column(name="cut_charge_type")
	public Integer getCutChargeType() {
		return cutChargeType;
	}
	public void setCutChargeType(Integer cutChargeType) {
		this.cutChargeType = cutChargeType;
	}
	@Column(name="cut_charge_rate")
	public BigDecimal getCutChargeRate() {
		return cutChargeRate;
	}
	public void setCutChargeRate(BigDecimal cutChargeRate) {
		this.cutChargeRate = cutChargeRate;
	}
	@Column(name="orderStatus")
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Column(name="salerId")
	public Long getSalerId() {
		return salerId;
	}
	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}
	@Column(name="salerName")
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	@Column(name="total_money")
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	@Column(name="company_money")
	public String getCompanyMoney() {
		return companyMoney;
	}
	public void setCompanyMoney(String companyMoney) {
		this.companyMoney = companyMoney;
	}
	@Column(name="difference_money")
	public String getDifferenceMoney() {
		return differenceMoney;
	}
	public void setDifferenceMoney(String differenceMoney) {
		this.differenceMoney = differenceMoney;
	}
	@Column(name="payId")
	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
	@Column(name="hasSeen")
	public Integer getHasSeen() {
		return hasSeen;
	}
	public void setHasSeen(Integer hasSeen) {
		this.hasSeen = hasSeen;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}