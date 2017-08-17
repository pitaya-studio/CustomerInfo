package com.trekiz.admin.modules.statistics.product.bean;

/**
 * 所有供应商的产品信息，只针对C355需求
 * @author shijun.liu   
 *
 */
public class ProductInfo {
	private String companyName;				//所属公司名称
	private String productName;				//产品名称
	private String operator;				//计调
	private String departureCity;			//出发城市
	private String arrivedCity;				//到达城市
	private String visa;					//签证
	private String groupCode;				//团号
	private String groupOpenDate;			//出团日期
	private String groupCloseDate;			//截团日期
	private String visaCountry;				//签证国家
	private String visaType;				//签证类型
	private String visaArea;				//签证领取
	private String costMoney;				//成本价格
	private String receiveMoney;			//应收价格
	private String createDate;				//创建日期
	private String infoDate;				//资料截止日期
	private String settlementAdultPrice;	//成人同行价
	private String settlementChildPrice;	//儿童同行价
	private String settlementSpecialPrice;	//特殊人群同行价
	private String suggestAdultPrice;		//成人直客价
	private String suggestChildPrice;		//儿童直客价
	private String suggestSpecialPrice;		//特殊人群直客价
	private String payDeposit;				//定金
	private String singleDiff;				//单房差
	private String planPosition;			//预收
	private String freePosition;			//余位
	private String payReservePosition;		//切位	
	private String productCode;				//产品编号
	private String airType;					//机票类型
	private String taxamt;					//税费
	private String depositTime;				//定金时限
	private String cancelTimeLimit;			//取消时限
	private String outTicketTime;			//出票日期
	private String airCompany;				//航空公司
	private String airSpace;				//仓位
	private String leaveAirport;			//出发机场
	private String arrivedAirport;			//到达机场
	private String startTime;				//起飞时间
	private String arrivalTime;				//到达时间
	private String orderType;				//订单类型
	private String productId;				//产品ID
	private String version;					//所属版本
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getDepartureCity() {
		return departureCity;
	}
	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}
	public String getArrivedCity() {
		return arrivedCity;
	}
	public void setArrivedCity(String arrivedCity) {
		this.arrivedCity = arrivedCity;
	}
	public String getVisa() {
		return visa;
	}
	public void setVisa(String visa) {
		this.visa = visa;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupOpenDate() {
		return groupOpenDate;
	}
	public void setGroupOpenDate(String groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	public String getGroupCloseDate() {
		return groupCloseDate;
	}
	public void setGroupCloseDate(String groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}
	public String getVisaCountry() {
		return visaCountry;
	}
	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}
	public String getVisaType() {
		return visaType;
	}
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	public String getVisaArea() {
		return visaArea;
	}
	public void setVisaArea(String visaArea) {
		this.visaArea = visaArea;
	}
	public String getCostMoney() {
		return costMoney;
	}
	public void setCostMoney(String costMoney) {
		this.costMoney = costMoney;
	}
	public String getReceiveMoney() {
		return receiveMoney;
	}
	public void setReceiveMoney(String receiveMoney) {
		this.receiveMoney = receiveMoney;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getInfoDate() {
		return infoDate;
	}
	public void setInfoDate(String infoDate) {
		this.infoDate = infoDate;
	}
	public String getSettlementAdultPrice() {
		return settlementAdultPrice;
	}
	public void setSettlementAdultPrice(String settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}
	public String getSettlementChildPrice() {
		return settlementChildPrice;
	}
	public void setSettlementChildPrice(String settlementChildPrice) {
		this.settlementChildPrice = settlementChildPrice;
	}
	public String getSettlementSpecialPrice() {
		return settlementSpecialPrice;
	}
	public void setSettlementSpecialPrice(String settlementSpecialPrice) {
		this.settlementSpecialPrice = settlementSpecialPrice;
	}
	public String getSuggestAdultPrice() {
		return suggestAdultPrice;
	}
	public void setSuggestAdultPrice(String suggestAdultPrice) {
		this.suggestAdultPrice = suggestAdultPrice;
	}
	public String getSuggestChildPrice() {
		return suggestChildPrice;
	}
	public void setSuggestChildPrice(String suggestChildPrice) {
		this.suggestChildPrice = suggestChildPrice;
	}
	public String getSuggestSpecialPrice() {
		return suggestSpecialPrice;
	}
	public void setSuggestSpecialPrice(String suggestSpecialPrice) {
		this.suggestSpecialPrice = suggestSpecialPrice;
	}
	public String getPayDeposit() {
		return payDeposit;
	}
	public void setPayDeposit(String payDeposit) {
		this.payDeposit = payDeposit;
	}
	public String getSingleDiff() {
		return singleDiff;
	}
	public void setSingleDiff(String singleDiff) {
		this.singleDiff = singleDiff;
	}
	public String getPlanPosition() {
		return planPosition;
	}
	public void setPlanPosition(String planPosition) {
		this.planPosition = planPosition;
	}
	public String getFreePosition() {
		return freePosition;
	}
	public void setFreePosition(String freePosition) {
		this.freePosition = freePosition;
	}
	public String getPayReservePosition() {
		return payReservePosition;
	}
	public void setPayReservePosition(String payReservePosition) {
		this.payReservePosition = payReservePosition;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getAirType() {
		return airType;
	}
	public void setAirType(String airType) {
		this.airType = airType;
	}
	public String getTaxamt() {
		return taxamt;
	}
	public void setTaxamt(String taxamt) {
		this.taxamt = taxamt;
	}
	public String getDepositTime() {
		return depositTime;
	}
	public void setDepositTime(String depositTime) {
		this.depositTime = depositTime;
	}
	public String getCancelTimeLimit() {
		return cancelTimeLimit;
	}
	public void setCancelTimeLimit(String cancelTimeLimit) {
		this.cancelTimeLimit = cancelTimeLimit;
	}
	public String getOutTicketTime() {
		return outTicketTime;
	}
	public void setOutTicketTime(String outTicketTime) {
		this.outTicketTime = outTicketTime;
	}
	public String getAirCompany() {
		return airCompany;
	}
	public void setAirCompany(String airCompany) {
		this.airCompany = airCompany;
	}
	public String getAirSpace() {
		return airSpace;
	}
	public void setAirSpace(String airSpace) {
		this.airSpace = airSpace;
	}
	public String getLeaveAirport() {
		return leaveAirport;
	}
	public void setLeaveAirport(String leaveAirport) {
		this.leaveAirport = leaveAirport;
	}
	public String getArrivedAirport() {
		return arrivedAirport;
	}
	public void setArrivedAirport(String arrivedAirport) {
		this.arrivedAirport = arrivedAirport;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
