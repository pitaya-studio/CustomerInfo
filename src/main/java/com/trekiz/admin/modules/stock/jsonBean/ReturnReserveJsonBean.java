/**
 *
 */
package com.trekiz.admin.modules.stock.jsonBean;


  /**
   * 批量归回切位jsonbean
   * @author police
   */

public class ReturnReserveJsonBean {
	
	private Long agentId;//渠道ID
	
	private Long productId;//散拼团期ID或者机票产品ID
	
	private Integer reserveBackAmount;//归还切位的数量
	
	private String returnRemark;//归还切位的备注

	

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getReserveBackAmount() {
		return reserveBackAmount;
	}

	public void setReserveBackAmount(Integer reserveBackAmount) {
		this.reserveBackAmount = reserveBackAmount;
	}

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
	
	
	
	
}


