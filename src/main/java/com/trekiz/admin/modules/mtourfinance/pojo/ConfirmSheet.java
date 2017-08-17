package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;



public class ConfirmSheet {
   
	//批发商名称
	public String companyName;
    //批发商英文名称
	public String companyName_EN;
	//批发商公司地址
	public String  addressee;
	//批发商公司英文地址
	public String addressee_EN;
	//批发商电话
	public String tel;
	//批发商传真
	public String tax;
	//渠道名称(收件人)
	public String channelName;
	//渠道电话（收件人电话）
	public String channelTel;
	//下单人（发件人）
	public String orderer;
	//下单人电话（发件人电话）
	public String ordererTel;
	//生成订单的日期
	public String orderDate;
	//团号
	public String groupNo;
	//
	public OverseasAccount overseasAccount;
	public Account account;
	//产品名称
	public String productName;
	//线路国家
	public String routerInfo;
	//订单备注
	public String memo;
	//总计公式
	public String total;
	//总计结果
	public String totalResult;
	//航班备注
	public String airticketRemark;
	// 航班信息
	public List<Flights> flights;
	//电子章路径
	public String ElectronicChapterUrl;
	
	
	
	
	public String getElectronicChapterUrl() {
		return ElectronicChapterUrl;
	}
	public void setElectronicChapterUrl(String electronicChapterUrl) {
		ElectronicChapterUrl = electronicChapterUrl;
	}
	public List<Flights> getFlights() {
		return flights;
	}
	public void setFlights(List<Flights> flights) {
		this.flights = flights;
	}
	public String getAirticketRemark() {
			return airticketRemark;
		}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyName_EN() {
		return companyName_EN;
	}
	public void setCompanyName_EN(String companyName_EN) {
		this.companyName_EN = companyName_EN;
	}
	public void setAirticketRemark(String airticketRemark) {
		this.airticketRemark = airticketRemark;
	}
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelTel() {
		return channelTel;
	}
	public void setChannelTel(String channelTel) {
		this.channelTel = channelTel;
	}
	
	public String getOrderer() {
		return orderer;
	}
	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}
	public String getOrdererTel() {
		return ordererTel;
	}
	public void setOrdererTel(String ordererTel) {
		this.ordererTel = ordererTel;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getRouterInfo() {
		return routerInfo;
	}
	public void setRouterInfo(String routerInfo) {
		this.routerInfo = routerInfo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	
    public OverseasAccount getOverseasAccount() {
		return overseasAccount;
	}
	public void setOverseasAccount(OverseasAccount overseasAccount) {
		this.overseasAccount = overseasAccount;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	//	public String getOutAccountName() {
//		return outAccountName;
//	}
//	public void setOutAccountName(String outAccountName) {
//		this.outAccountName = outAccountName;
//	}
//	public String getRounting() {
//		return rounting;
//	}
//	public void setRounting(String rounting) {
//		this.rounting = rounting;
//	}
//	public String getOutBankAccountCode() {
//		return outBankAccountCode;
//	}
//	public void setOutBankAccountCode(String outBankAccountCode) {
//		this.outBankAccountCode = outBankAccountCode;
//	}
//	public String getSwiftNum() {
//		return swiftNum;
//	}
//	public void setSwiftNum(String swiftNum) {
//		this.swiftNum = swiftNum;
//	}
//	public String getOutBankAddr() {
//		return outBankAddr;
//	}
//	public void setOutBankAddr(String outBankAddr) {
//		this.outBankAddr = outBankAddr;
//	}
//	public String getPhoneNum() {
//		return phoneNum;
//	}
//	public void setPhoneNum(String phoneNum) {
//		this.phoneNum = phoneNum;
//	}
//	public String getInAccountName() {
//		return inAccountName;
//	}
//	public void setInAccountName(String inAccountName) {
//		this.inAccountName = inAccountName;
//	}
//	public String getInBankName() {
//		return inBankName;
//	}
//	public void setInBankName(String inBankName) {
//		this.inBankName = inBankName;
//	}
//	public String getInBankAccountCode() {
//		return inBankAccountCode;
//	}
//	public void setInBankAccountCode(String inBankAccountCode) {
//		this.inBankAccountCode = inBankAccountCode;
//	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(String totalResult) {
		this.totalResult = totalResult;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getAddressee_EN() {
		return addressee_EN;
	}
	public void setAddressee_EN(String addressee_EN) {
		this.addressee_EN = addressee_EN;
	}
	
	
	
}
