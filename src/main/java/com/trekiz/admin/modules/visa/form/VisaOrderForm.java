package com.trekiz.admin.modules.visa.form;

/***
 * 签务和销售身份管理订单功能
 * 页面查询条件类
 * wenjianye
 * 2014-12-03
 * 
 * */
public class VisaOrderForm {

	/****签证身份管理订单的查询条件****/
	private String travelName;
	//private String  orderBy;
	//订单编号/产品编号 的接收字段,进入action后,根据规则把值存到 产品编号 或是订单中
	private String commonCode;
	//订单号
	private String orderNo;
	//签证国家
	private String sysCountryId;
	//AA码
	private String AACode;
	//产品编号
	private String visaProductId;
	//下单人
	private String createBy;
	//签证类型
	private String visaType;
	//付款状态
	private String orderPayStatus;
	//参团类型
	private String orderType;
	//签证状态
	private String visaStatus;
	//主订单id
	private String main_orderId;
	//护照状态
	private String passportStatus;
	//渠道结款方式
	private String paymentType;
	
	
	
	public String getMain_orderId() {
		return main_orderId;
	}
	public void setMain_orderId(String main_orderId) {
		this.main_orderId = main_orderId;
	}
	//制表人
	//private String makeTable;
	//渠道选择
	private String agentinfoId;
	//借款状态
	private String jiekuanStatus;
	//预计出团时间-开始时间
	private String forecastStartOutStart;
	//预计出团时间-结束时间
	private String forecastStartOutEnd;
	//预计约签时间-开始时间
	private String forecastContractStart;
	//预计约签时间-结束时间
	private String forecastContractEnd;
	//实际出团时间-开始时间
	private String startOutStart;
	//实际出团时间-结束时间
	private String startOutEnd;
	//实际签约时间-开始时间
	private String contractStart;
	//实际签约时间-结束时间
	private String contractEnd;
	//借款申请日期-开始时间
	private String jiekuanTimeStart;
	//借款申请日期-结束时间
	private String jiekuanTimeEnd;
	
	//说明会时间-开始时间
	private String explanationMeetingStart;
	//说明会时间-结束时间
	private String explanationMeetingEnd;
	
	//发票状态
	private String invoiceStatus;
	//收据状态
	private String receiptStatus;
	//销售ID
	private String salerId;
	
	/**签务身份订单  批量操作页面搜索条件**/
	//批次编号
	private String batchNo;
	//操作人
	private String txnPerson;
	//申请时间-开始时间
	private String createTimeStart;
	//申请时间-结束时间
	private String createTimeEnd;
	
	/****销售身份管理订单的查询条件 开始****/
	
	//页面加载完成后,显示哪个标签的标示位 dingdan:显示订单选项;tuanhao:显示团旗列表
	private String  showList;
	//下单日期-开始时间
	private String createDateStart;
	//下单日期-结束时间
	private String createDateEnd;
	
	//是否被查看
	private String isRead;
	// 显示Tab页面的类型 ()
	private String showType;
		
		
	/*****销售管理订单添加   结束*****/
	
	public String getIsRead()
	{
		return isRead;
	}
	public void setIsRead(String isRead)
	{
		this.isRead = isRead;
	}
	
	/****销售身份管理订单的查询条件  结束****/
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSysCountryId() {
		return sysCountryId;
	}
	public String getTravelName() {
		return travelName;
	}
	public void setTravelName(String travelName) {
		this.travelName = travelName;
	}
	public void setSysCountryId(String sysCountryId) {
		this.sysCountryId = sysCountryId;
	}
	public String getAACode() {
		return AACode;
	}
	public void setAACode(String aACode) {
		AACode = aACode;
	}
	public String getVisaProductId() {
		return visaProductId;
	}
	public void setVisaProductId(String visaProductId) {
		this.visaProductId = visaProductId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getVisaType() {
		return visaType;
	}
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	public String getOrderPayStatus() {
		return orderPayStatus;
	}
	public void setOrderPayStatus(String orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getVisaStatus() {
		return visaStatus;
	}
	public void setVisaStatus(String visaStatus) {
		this.visaStatus = visaStatus;
	}
//	public String getMakeTable() {
//		return makeTable;
//	}
//	public void setMakeTable(String makeTable) {
//		this.makeTable = makeTable;
//	}
	public String getAgentinfoId() {
		return agentinfoId;
	}
	public void setAgentinfoId(String agentinfoId) {
		this.agentinfoId = agentinfoId;
	}
//	public String getJiekuanStatus() {
//		return jiekuanStatus;
//	}
//	public void setJiekuanStatus(String jiekuanStatus) {
//		this.jiekuanStatus = jiekuanStatus;
//	}
	public String getForecastStartOutStart() {
		return forecastStartOutStart;
	}
	public void setForecastStartOutStart(String forecastStartOutStart) {
		this.forecastStartOutStart = forecastStartOutStart;
	}
	public String getForecastStartOutEnd() {
		return forecastStartOutEnd;
	}
	public void setForecastStartOutEnd(String forecastStartOutEnd) {
		this.forecastStartOutEnd = forecastStartOutEnd;
	}
	public String getForecastContractStart() {
		return forecastContractStart;
	}
	public void setForecastContractStart(String forecastContractStart) {
		this.forecastContractStart = forecastContractStart;
	}
	public String getForecastContractEnd() {
		return forecastContractEnd;
	}
	public void setForecastContractEnd(String forecastContractEnd) {
		this.forecastContractEnd = forecastContractEnd;
	}
	public String getStartOutStart() {
		return startOutStart;
	}
	public void setStartOutStart(String startOutStart) {
		this.startOutStart = startOutStart;
	}
	public String getStartOutEnd() {
		return startOutEnd;
	}
	public void setStartOutEnd(String startOutEnd) {
		this.startOutEnd = startOutEnd;
	}
	public String getContractStart() {
		return contractStart;
	}
	public void setContractStart(String contractStart) {
		this.contractStart = contractStart;
	}
	public String getContractEnd() {
		return contractEnd;
	}
	public void setContractEnd(String contractEnd) {
		this.contractEnd = contractEnd;
	}
	public String getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public String getCommonCode() {
		return commonCode;
	}
	public void setCommonCode(String commonCode) {
		this.commonCode = commonCode;
	}
//	public String getOrderBy() {
//		return orderBy;
//	}
//	public void setOrderBy(String orderBy) {
//		this.orderBy = orderBy;
//	}
	public String getShowList() {
		return showList;
	}
	public String getJiekuanTimeStart() {
		return jiekuanTimeStart;
	}
	public String getJiekuanStatus() {
		return jiekuanStatus;
	}
	public void setJiekuanStatus(String jiekuanStatus) {
		this.jiekuanStatus = jiekuanStatus;
	}
	public void setJiekuanTimeStart(String jiekuanTimeStart) {
		this.jiekuanTimeStart = jiekuanTimeStart;
	}
	public String getJiekuanTimeEnd() {
		return jiekuanTimeEnd;
	}
	public void setJiekuanTimeEnd(String jiekuanTimeEnd) {
		this.jiekuanTimeEnd = jiekuanTimeEnd;
	}
	public void setShowList(String showList) {
		this.showList = showList;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTxnPerson() {
		return txnPerson;
	}
	public void setTxnPerson(String txnPerson) {
		this.txnPerson = txnPerson;
	}
	public String getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public String getPassportStatus() {
		return passportStatus;
	}
	public void setPassportStatus(String passportStatus) {
		this.passportStatus = passportStatus;
	}
	public String getExplanationMeetingStart() {
		return explanationMeetingStart;
	}
	public void setExplanationMeetingStart(String explanationMeetingStart) {
		this.explanationMeetingStart = explanationMeetingStart;
	}
	public String getExplanationMeetingEnd() {
		return explanationMeetingEnd;
	}
	public void setExplanationMeetingEnd(String explanationMeetingEnd) {
		this.explanationMeetingEnd = explanationMeetingEnd;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	
}
