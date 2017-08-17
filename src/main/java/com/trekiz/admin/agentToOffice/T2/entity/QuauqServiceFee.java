package com.trekiz.admin.agentToOffice.T2.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * QUAUQ服务费实体类
 * @author ayong.bao
 * @Date 2016-6-15
 */
@Entity
public class QuauqServiceFee {

	/** 供应商id */
	@Id
	private Long officeId;
	/** 订单数 */
	private Long orderCount;
	/** 人数 */
	private Long personCount;
	/** 供应商姓名 */
	private String officeName;
	/** 交易服务费总额 */
	private String quauqServiceFeeTotalCount;
	/** 交易服务费结清额 */
	private String quauqServiceFeeSettled;
	/** 交易服务费未结额 */
	private String quauqServiceFeeUnsettled;
	/** 渠道服务费总额 */
	private String agentChargeTotal;
	
	public String getAgentChargeTotal() {
		return agentChargeTotal;
	}
	public void setAgentChargeTotal(String agentChargeTotal) {
		this.agentChargeTotal = agentChargeTotal;
	}
	public Long getOfficeId() {
		return officeId;
	}
	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}
	public Long getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}
	public Long getPersonCount() {
		return personCount;
	}
	public void setPersonCount(Long personCount) {
		this.personCount = personCount;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getQuauqServiceFeeTotalCount() {
		return quauqServiceFeeTotalCount;
	}
	public void setQuauqServiceFeeTotalCount(String quauqServiceFeeTotalCount) {
		this.quauqServiceFeeTotalCount = quauqServiceFeeTotalCount;
	}
	public String getQuauqServiceFeeSettled() {
		return quauqServiceFeeSettled;
	}
	public void setQuauqServiceFeeSettled(String quauqServiceFeeSettled) {
		this.quauqServiceFeeSettled = quauqServiceFeeSettled;
	}
	public String getQuauqServiceFeeUnsettled() {
		return quauqServiceFeeUnsettled;
	}
	public void setQuauqServiceFeeUnsettled(String quauqServiceFeeUnsettled) {
		this.quauqServiceFeeUnsettled = quauqServiceFeeUnsettled;
	}
	
}
