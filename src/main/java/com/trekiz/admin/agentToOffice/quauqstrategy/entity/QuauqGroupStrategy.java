package com.trekiz.admin.agentToOffice.quauqstrategy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 团期策略
 * @time 2016/8/10
 * @author chao.zhang@quauq.com
 */
@Entity
@Table(name = "quauq_group_strategy")
public class QuauqGroupStrategy extends DataEntity {
	//id
	private Integer id;
	//uuid
	private String uuid;
	//产品批发商id
	private String companyUuid;
	//所属产品或团期id
	private Integer activityId;
	//产品类型
	private Integer productType;
	//渠道id（客户：门市、总社、集团客户id）
	private Integer agentId;
	//quauq费率符号取值(0:百分比 1：定额)
	private Integer quauqRateType=0;
	//quauq费率
	private Double quauqRate=0.01;
	//quauq其他费率符号取值
	private Integer quauqOtherRateType=0;
	//quauq其他费率
	private Double quauqOtherRate=0d;
	//渠道费率符号取值
	private Integer agentRateType=0;
	//渠道费率
	private Double agentRate=0d;
	//渠道其他费率符号取值
	private Integer agentOtherRateType=0;
	//渠道其他费率
	private Double agentOtherRate=0d;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="uuid",unique=true,nullable=false)
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="company_uuid",unique=false,nullable=false)
	public String getCompanyUuid() {
		return companyUuid;
	}
	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	
	@Column(name="product_type",unique=false,nullable=false)
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	@Column(name="agent_id",unique=false,nullable=false)
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	@Column(name="quauq_rate_type")
	public Integer getQuauqRateType() {
		return quauqRateType;
	}
	public void setQuauqRateType(Integer quauqRateType) {
		this.quauqRateType = quauqRateType;
	}
	@Column(name="quauq_rate")
	public Double getQuauqRate() {
		return quauqRate;
	}
	public void setQuauqRate(Double quauqRate) {
		this.quauqRate = quauqRate;
	}
	@Column(name="quauq_other_rate_type")
	public Integer getQuauqOtherRateType() {
		return quauqOtherRateType;
	}
	public void setQuauqOtherRateType(Integer quauqOtherRateType) {
		this.quauqOtherRateType = quauqOtherRateType;
	}
	@Column(name="quauq_other_rate")
	public Double getQuauqOtherRate() {
		return quauqOtherRate;
	}
	public void setQuauqOtherRate(Double quauqOtherRate) {
		this.quauqOtherRate = quauqOtherRate;
	}
	@Column(name="agent_rate_type")
	public Integer getAgentRateType() {
		return agentRateType;
	}
	public void setAgentRateType(Integer agentRateType) {
		this.agentRateType = agentRateType;
	}
	@Column(name="agent_rate")
	public Double getAgentRate() {
		return agentRate;
	}
	public void setAgentRate(Double agentRate) {
		this.agentRate = agentRate;
	}
	@Column(name="agent_other_rate_type")
	public Integer getAgentOtherRateType() {
		return agentOtherRateType;
	}
	public void setAgentOtherRateType(Integer agentOtherRateType) {
		this.agentOtherRateType = agentOtherRateType;
	}
	@Column(name="agent_other_rate")
	public Double getAgentOtherRate() {
		return agentOtherRate;
	}
	public void setAgentOtherRate(Double agentOtherRate) {
		this.agentOtherRate = agentOtherRate;
	}
	@Column(name="activity_id",unique=false,nullable=false)
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	@Override
	public String toString() {
		return "QuauqGroupStrategy [id=" + id + ", uuid=" + uuid
				+ ", companyUuid=" + companyUuid + ", activityId=" + activityId
				+ ", productType=" + productType + ", agentId=" + agentId
				+ ", quauqRateType=" + quauqRateType + ", quauqRate="
				+ quauqRate + ", quauqOtherRateType=" + quauqOtherRateType
				+ ", quauqOtherRate=" + quauqOtherRate + ", agentRateType="
				+ agentRateType + ", agentRate=" + agentRate
				+ ", agentOtherRateType=" + agentOtherRateType
				+ ", agentOtherRate=" + agentOtherRate + "]";
	}
}
