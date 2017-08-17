package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 价格策略实体类
 * @author yakun.bai
 * @Date 2016-4-28
 */
@Entity
@Table(name = "price_strategy")
@DynamicInsert @DynamicUpdate
public class PriceStrategy extends DataEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	/** 渠道ID */
	private Long agentId;
	/** 出发地IDS */
	private String fromAreaIds;
	/** 出发地 */
	private String fromAreaNames;
	/** 目的地IDS */
	private String targetAreaIds;
	/** 目的地 */
	private String targetAreaNames;
	/** 旅游类型IDS */
	private String travelTypeIds;
	/** 旅游类型 */
	private String travelTypeNames;
	/** 产品类型IDS */
	private String activityTypeIds;
	/** 产品类型 */
	private String activityTypeIdNames;
	/** 产品系列IDS */
	private String productLevelIds;
	/** 产品系列*/
	private String productLevelNames;
	/** 批发商ID */
	private Long supplyId;
	/** 批发商名称 */
	private String supplyName;
	/** 备注 */
	private String remarks;
	/** 价格策略状态 ：1 启用 2 禁用*/
	private Integer state;
	
	
    public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	private Set<AgentPriceStrategy> agentPriceStrategySet;
//=======================================================
	@Id 
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	/** 价格策略*/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "priceStrategy", targetEntity = AgentPriceStrategy.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy(value = "createDate")

	public Set<AgentPriceStrategy> getAgentPriceStrategySet() {
		return agentPriceStrategySet;
	}
	public void setAgentPriceStrategySet(
			Set<AgentPriceStrategy> agentPriceStrategySet) {
		this.agentPriceStrategySet = agentPriceStrategySet;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public String getFromAreaIds() {
		return fromAreaIds;
	}
	public void setFromAreaIds(String fromAreaIds) {
		this.fromAreaIds = fromAreaIds;
	}
	public String getFromAreaNames() {
		return fromAreaNames;
	}
	public void setFromAreaNames(String fromAreaNames) {
		this.fromAreaNames = fromAreaNames;
	}
	public String getTargetAreaIds() {
		return targetAreaIds;
	}
	public void setTargetAreaIds(String targetAreaIds) {
		this.targetAreaIds = targetAreaIds;
	}
	public String getTargetAreaNames() {
		return targetAreaNames;
	}
	public void setTargetAreaNames(String targetAreaNames) {
		this.targetAreaNames = targetAreaNames;
	}
	public String getTravelTypeIds() {
		return travelTypeIds;
	}
	public void setTravelTypeIds(String travelTypeIds) {
		this.travelTypeIds = travelTypeIds;
	}
	public String getTravelTypeNames() {
		return travelTypeNames;
	}
	public void setTravelTypeNames(String travelTypeNames) {
		this.travelTypeNames = travelTypeNames;
	}
	public String getActivityTypeIds() {
		return activityTypeIds;
	}
	public void setActivityTypeIds(String activityTypeIds) {
		this.activityTypeIds = activityTypeIds;
	}
	public String getActivityTypeIdNames() {
		return activityTypeIdNames;
	}
	public void setActivityTypeIdNames(String activityTypeIdNames) {
		this.activityTypeIdNames = activityTypeIdNames;
	}
	public String getProductLevelIds() {
		return productLevelIds;
	}
	public void setProductLevelIds(String productLevelIds) {
		this.productLevelIds = productLevelIds;
	}
	public String getProductLevelNames() {
		return productLevelNames;
	}
	public void setProductLevelNames(String productLevelNames) {
		this.productLevelNames = productLevelNames;
	}
	public Long getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(Long supplyId) {
		this.supplyId = supplyId;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}