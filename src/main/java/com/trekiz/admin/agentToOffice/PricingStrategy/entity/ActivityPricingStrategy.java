package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 价格策略实体类
 * @author yakun.bai
 * @Date 2016-4-28
 */
@Entity
@Table(name = "activity_pricingStrategy")
@DynamicInsert @DynamicUpdate
@IdClass(com.trekiz.admin.agentToOffice.PricingStrategy.entity.APpk.class)
public class ActivityPricingStrategy extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	private Long travelactivityId;
	private Long activitygroupId;
	private Long pricingStrategyId;
	private Integer usageState;//0 使用 1 废弃
	public Long getTravelactivityId() {
		return travelactivityId;
	}
	public void setTravelactivityId(Long travelactivityId) {
		this.travelactivityId = travelactivityId;
	}
	@Id
	public Long getActivitygroupId() {
		return activitygroupId;
	}
	
	public void setActivitygroupId(Long activitygroupId) {
		this.activitygroupId = activitygroupId;
	}
	@Id
	public Long getPricingStrategyId() {
		return pricingStrategyId;
	}
	
	public void setPricingStrategyId(Long pricingStrategyId) {
		this.pricingStrategyId = pricingStrategyId;
	}
	@Id
	public Integer getUsageState() {
		return usageState;
	}
	public void setUsageState(Integer usageState) {
		this.usageState = usageState;
	}
	
	@Override
	public void prePersist() {
		this.usageState = 0;
		super.prePersist();
	}
	@Override
	public void preUpdate() {
		this.usageState = 1;
		super.preUpdate();
	}
	public ActivityPricingStrategy(Long travelactivityId, Long activitygroupId,
			Long pricingStrategyId) {
		super();
		this.travelactivityId = travelactivityId;
		this.activitygroupId = activitygroupId;
		this.pricingStrategyId = pricingStrategyId;
	}
	
}