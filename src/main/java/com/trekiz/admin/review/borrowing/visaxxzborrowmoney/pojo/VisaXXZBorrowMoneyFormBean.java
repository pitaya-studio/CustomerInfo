package com.trekiz.admin.review.borrowing.visaxxzborrowmoney.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 签证借款列表条件类
 * @author majiancheng
 *
 */
public class VisaXXZBorrowMoneyFormBean {
	private String searchLike;//模糊查询
	private String agent;//渠道商
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyDateFrom;//申请开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyDateTo;//申请结束日期
	private String applyPerson;//审批发起人
	private String saler;//销售
	private Integer reviewStatus;//审批状态
	private Integer cashConfirm;//出纳确认
	private Integer printStatus;//打印状态
	private Double minBorrowMoney;//最低借款金额
	private Double maxBorrowMoney;//最高借款金额
	private Integer tabStatus;//审核选择状态
	private String orderBy;//排序字段
	private String ascOrDesc;//升序或降序
	public String getSearchLike() {
		return searchLike;
	}
	public void setSearchLike(String searchLike) {
		this.searchLike = searchLike;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Date getApplyDateFrom() {
		return applyDateFrom;
	}
	public void setApplyDateFrom(Date applyDateFrom) {
		this.applyDateFrom = applyDateFrom;
	}
	public Date getApplyDateTo() {
		return applyDateTo;
	}
	public void setApplyDateTo(Date applyDateTo) {
		this.applyDateTo = applyDateTo;
	}
	public String getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}
	public Integer getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Integer getCashConfirm() {
		return cashConfirm;
	}
	public void setCashConfirm(Integer cashConfirm) {
		this.cashConfirm = cashConfirm;
	}
	public Integer getPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(Integer printStatus) {
		this.printStatus = printStatus;
	}
	public Double getMinBorrowMoney() {
		return minBorrowMoney;
	}
	public void setMinBorrowMoney(Double minBorrowMoney) {
		this.minBorrowMoney = minBorrowMoney;
	}
	public Double getMaxBorrowMoney() {
		return maxBorrowMoney;
	}
	public void setMaxBorrowMoney(Double maxBorrowMoney) {
		this.maxBorrowMoney = maxBorrowMoney;
	}
	public Integer getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(Integer tabStatus) {
		this.tabStatus = tabStatus;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	
	
}
