package com.trekiz.admin.review.cost.common;

import java.math.BigDecimal;

public class CostRecordModel {
	private Long costId; //成本id
	private Long activityId; //产品id
	private Long groupId; //团期id
	private Integer typeId; //订单类型
	private Long deptId; //部门
	private Integer review; //
	private Integer overseas; //境内、境外
	private String itemname; //项目名称
	private Integer quantity; //数量
	private Integer currencyId; //转换前币种
	private BigDecimal price; //转换前单价
	private Integer supplyType; //客户类别
	private Integer agentId; //渠道商
	private Integer first; //地接社类型
	private Integer supplier; //地接社
	private Integer budgetType; //预算、实际、其他收入
	private Long bank; //银行
	private String bankname; //银行名称
	private String account; //银行账号
	private BigDecimal rate; //汇率
	private Integer currencyAfter; //转换后币种
	private BigDecimal priceAfter; //转换后总价
	private String comment; //备注
	private Integer payReview;
	private Integer kb; //是否为kb款

	public Long getCostId() {
		return costId;
	}

	public void setCostId(Long costId) {
		this.costId = costId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public Integer getOverseas() {
		return overseas;
	}

	public void setOverseas(Integer overseas) {
		this.overseas = overseas;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getSupplier() {
		return supplier;
	}

	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}

	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}

	public Long getBank() {
		return bank;
	}

	public void setBank(Long bank) {
		this.bank = bank;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getCurrencyAfter() {
		return currencyAfter;
	}

	public void setCurrencyAfter(Integer currencyAfter) {
		this.currencyAfter = currencyAfter;
	}

	public BigDecimal getPriceAfter() {
		return priceAfter;
	}

	public void setPriceAfter(BigDecimal priceAfter) {
		this.priceAfter = priceAfter;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPayReview() {
		return payReview;
	}

	public void setPayReview(Integer payReview) {
		this.payReview = payReview;
	}

	public Integer getKb() {
		return kb;
	}

	public void setKb(Integer kb) {
		this.kb = kb;
	}
}
