package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

/**
 * 美途国际结算单对应的json对象
 * @author shijun.liu
 *
 */
public class SettlementJsonBean {

	//订单ID
	private String orderUuid;
	//团号
	private String groupNo;
	//订单编号
	private String orderNo;
	//人数
	private String peopleCount;
	//出票日期
	private String invoiceDate;
	//行程
	private String itinerary;
	//行程时间段
	private String travelPeriod;
	//航空公司
	private String airlineCompany;
	//供应商名称
	private String supplierName;
	//机票操作员
	private String ticketName;
	//销售人员
	private String orderer;
	//结算单锁定状态
	private String lockStatus;
	//收入项
	private List<InComeJsonBean> incomes;
	//成本项
	private List<CostJsonBean> costs;
	//追加成本
	private List<AdditionCostRefundJsonBean> additionalCosts;
	//退款
	private List<AdditionCostRefundJsonBean> refunds;
	//毛利
	private String grossProfit;
	//毛利率
	private String grossProfitRate;
	//收入项--收入合计
	private String inComeSum;
	//收入项--收入合计(折合RMB)
	private String inComeSumRMB;
	//追加成本总计
	private String additionalCostSum;
	//追加成本总计--折合人民币
	private String additionalCostSumRMB;
	//成本总计
	private String costSum;
	//成本总计--折合人民币
	private String costSumRMB;
	//退款总计
	private String refundSum;
	//退款总计--折合人民币
	private String refundSumRMB;
	// 制表人
	private String lister;
	// 制表日期
	private String listerDate;
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String OrderUuid) {
		this.orderUuid = OrderUuid;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getItinerary() {
		return itinerary;
	}
	public void setItinerary(String itinerary) {
		this.itinerary = itinerary;
	}
	public String getTravelPeriod() {
		return travelPeriod;
	}
	public void setTravelPeriod(String travelPeriod) {
		this.travelPeriod = travelPeriod;
	}
	public String getAirlineCompany() {
		return airlineCompany;
	}
	public void setAirlineCompany(String airlineCompany) {
		this.airlineCompany = airlineCompany;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public String getOrderer() {
		return orderer;
	}
	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}
	public List<InComeJsonBean> getIncomes() {
		return incomes;
	}
	public void setIncomes(List<InComeJsonBean> incomes) {
		this.incomes = incomes;
	}
	public List<CostJsonBean> getCosts() {
		return costs;
	}
	public void setCosts(List<CostJsonBean> costs) {
		this.costs = costs;
	}
	public List<AdditionCostRefundJsonBean> getAdditionalCosts() {
		return additionalCosts;
	}
	public void setAdditionalCosts(List<AdditionCostRefundJsonBean> additionalCosts) {
		this.additionalCosts = additionalCosts;
	}
	public List<AdditionCostRefundJsonBean> getRefunds() {
		return refunds;
	}
	public void setRefunds(List<AdditionCostRefundJsonBean> refunds) {
		this.refunds = refunds;
	}
	public String getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(String grossProfit) {
		this.grossProfit = grossProfit;
	}
	public String getGrossProfitRate() {
		return grossProfitRate;
	}
	public void setGrossProfitRate(String grossProfitRate) {
		this.grossProfitRate = grossProfitRate;
	}
	public String getInComeSum() {
		return inComeSum;
	}
	public void setInComeSum(String inComeSum) {
		this.inComeSum = inComeSum;
	}
	public String getInComeSumRMB() {
		return inComeSumRMB;
	}
	public void setInComeSumRMB(String inComeSumRMB) {
		this.inComeSumRMB = inComeSumRMB;
	}
	public String getAdditionalCostSum() {
		return additionalCostSum;
	}
	public void setAdditionalCostSum(String additionalCostSum) {
		this.additionalCostSum = additionalCostSum;
	}
	public String getAdditionalCostSumRMB() {
		return additionalCostSumRMB;
	}
	public void setAdditionalCostSumRMB(String additionalCostSumRMB) {
		this.additionalCostSumRMB = additionalCostSumRMB;
	}
	public String getCostSum() {
		return costSum;
	}
	public void setCostSum(String costSum) {
		this.costSum = costSum;
	}
	public String getCostSumRMB() {
		return costSumRMB;
	}
	public void setCostSumRMB(String costSumRMB) {
		this.costSumRMB = costSumRMB;
	}
	public String getRefundSum() {
		return refundSum;
	}
	public void setRefundSum(String refundSum) {
		this.refundSum = refundSum;
	}
	public String getRefundSumRMB() {
		return refundSumRMB;
	}
	public void setRefundSumRMB(String refundSumRMB) {
		this.refundSumRMB = refundSumRMB;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getListerDate() {
		return listerDate;
	}

	public void setListerDate(String listerDate) {
		this.listerDate = listerDate;
	}

	public String getLister() {
		return lister;
	}

	public void setLister(String lister) {
		this.lister = lister;
	}
	
}