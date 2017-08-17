package com.trekiz.admin.modules.mtourfinance.pojo;

import java.util.List;
/**
 * 付款余额POJO
 * @author ZHAOHAIMING
 * */
public class PayedMoneyPojo {
	List<MoneyAmountVO> payableAmount;//应付金额
	List<MoneyAmountVO> payingAmount;//未付金额
	String receiveCompanyUuid=""; //付款对象UUid 付款对象为地接社或者渠道商
	String receiveCompany="";//付款对象Name 付款对象为地接社或者渠道商
	String receiveBank = "";//收款行名称
	String receiveAccount = "";//收款账户
	String tourOperatorChannelCategoryCode="";//用于区分地接社和渠道商，地接社：1；渠道商：2；
	
	public String getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}
	public void setTourOperatorChannelCategoryCode(
			String tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}
	public List<MoneyAmountVO> getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(List<MoneyAmountVO> payableAmount) {
		this.payableAmount = payableAmount;
	}
	public List<MoneyAmountVO> getPayingAmount() {
		return payingAmount;
	}
	public void setPayingAmount(List<MoneyAmountVO> payingAmount) {
		this.payingAmount = payingAmount;
	}
	public String getReceiveCompanyUuid() {
		return receiveCompanyUuid;
	}
	public void setReceiveCompanyUuid(String receiveCompanyUuid) {
		this.receiveCompanyUuid = receiveCompanyUuid;
	}
	
	public String getReceiveCompany() {
		return receiveCompany;
	}
	public void setReceiveCompany(String receiveCompany) {
		this.receiveCompany = receiveCompany;
	}
	public String getReceiveBank() {
		return receiveBank;
	}
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	
	
	
}
