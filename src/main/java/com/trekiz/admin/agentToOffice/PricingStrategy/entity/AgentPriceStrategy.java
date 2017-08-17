package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 价格策略实体类
 * @author yakun.bai
 * @Date 2016-4-28
 */
@Entity
@Table(name = "agent_price_strategy")
@DynamicInsert @DynamicUpdate
public class AgentPriceStrategy extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	/** 价格策略ID */
	private Long priceStrategyId;
	/** 渠道类型IDS */
	private String agentTypeIds;
	/** 渠道类型 */
	private String agentTypeNames;
	/** 渠道等级IDS */
	private String agentLevelIds;
	/** 渠道等级 */
	private String agentLevelNames;
	/** 成人价格方案 */
	private String adultPriceStrategy;
	/** 儿童价格方案 */
	private String childrenPriceStrategy;
	/** 特殊人群价格方案 */
	private String specialPriceStrategy;
	/** 优惠IDS */
	private String discountIds;
	/** 优惠 */
	private String discountNames;
	/**价格策略*/
	private String priceStrategyDesc;
	/**价格策略描述*/
	
	private PriceStrategy priceStrategy;
	
	
	public String getPriceStrategyDesc() {
		return priceStrategyDesc;
	}
	public void setPriceStrategyDesc(String priceStrategyDesc) {
		this.priceStrategyDesc = priceStrategyDesc;
	}
//	@ContainedIn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priceStrategyId", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
//	@NotNull
	public PriceStrategy getPriceStrategy() {
		return priceStrategy;
	}
	public void setPriceStrategy(PriceStrategy priceStrategy) {
		this.priceStrategy = priceStrategy;
	}
	
	@Id 
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAgentTypeIds() {
		return agentTypeIds;
	}
	public void setAgentTypeIds(String agentTypeIds) {
		this.agentTypeIds = agentTypeIds;
	}
	public String getAgentTypeNames() {
		return agentTypeNames;
	}
	public void setAgentTypeNames(String agentTypeNames) {
		this.agentTypeNames = agentTypeNames;
	}
	public String getAgentLevelIds() {
		return agentLevelIds;
	}
	public void setAgentLevelIds(String agentLevelIds) {
		this.agentLevelIds = agentLevelIds;
	}
	public String getAgentLevelNames() {
		return agentLevelNames;
	}
	public void setAgentLevelNames(String agentLevelNames) {
		this.agentLevelNames = agentLevelNames;
	}
	public String getAdultPriceStrategy() {
		return adultPriceStrategy;
	}
	public void setAdultPriceStrategy(String adultPriceStrategy) {
		this.adultPriceStrategy = adultPriceStrategy;
	}
	public String getChildrenPriceStrategy() {
		return childrenPriceStrategy;
	}
	public void setChildrenPriceStrategy(String childrenPriceStrategy) {
		this.childrenPriceStrategy = childrenPriceStrategy;
	}
	public String getSpecialPriceStrategy() {
		return specialPriceStrategy;
	}
	public void setSpecialPriceStrategy(String specialPriceStrategy) {
		this.specialPriceStrategy = specialPriceStrategy;
	}
	public String getDiscountIds() {
		return discountIds;
	}
	public void setDiscountIds(String discountIds) {
		this.discountIds = discountIds;
	}
	public String getDiscountNames() {
		return discountNames;
	}
	public void setDiscountNames(String discountNames) {
		this.discountNames = discountNames;
	}
	@Transient
	public Long getPriceStrategyId() {
		return priceStrategyId;
	}
	public void setPriceStrategyId(Long priceStrategyId) {
		this.priceStrategyId = priceStrategyId;
	}
	
}