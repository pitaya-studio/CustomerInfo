package com.trekiz.admin.modules.eprice.entity;


public class EstimatePrice {
	private Long moneyAmountId; // 流水ID
	private Long replyId; // 报价ID
	private String amount;// 单价
	private Integer personNum;// 人数
	private String sumPrice;// 单价*人数
	private Long currencyId;// 币种类
//	private String mark;// 币种符号
//	private String name;// 币种名称
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Integer getPersonNum() {
		return personNum;
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
	public String getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(String sumPrice) {
		this.sumPrice = sumPrice;
	}
	public Long getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
	public Long getMoneyAmountId() {
		return moneyAmountId;
	}
	public void setMoneyAmountId(Long moneyAmountId) {
		this.moneyAmountId = moneyAmountId;
	}
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
}
