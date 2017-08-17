package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

import com.trekiz.admin.modules.mtourfinance.pojo.MoneyAmountVO;

/**
 * 账龄查询的结果数据对象
 * @author shijun.liu
 *
 */
public class AccountAgeJsonResult {

	private String channelUuid;			//渠道ID
	private String channelName;			//渠道名称
	private String saleId;				//销售ID
	private String salesName;			//销售名称
	private String totalTravelerCount;	//人数
	private List<MoneyAmountVO> receivableAmount;	//应收总金额
	private List<MoneyAmountVO> receivedAmount;		//已收金额
	private List<MoneyAmountVO> arrivedAmount;		//达帐金额
	private List<MoneyAmountVO> unreceiveAmount;	//未收金额
	private String totalMoney;						//应收总金额  格式 : 币种Id币种符号金额+币种Id币种符号金额...
	private String payedMoney;						//已收金额      格式 : 币种Id币种符号金额+币种Id币种符号金额...
	private String accountedMoney;					//达帐金额     格式 : 币种Id币种符号金额+币种Id币种符号金额...
	private String notReceiviedMoney;				//未收金额     格式 : 币种Id币种符号金额+币种Id币种符号金额...
	public String getChannelUuid() {
		return channelUuid;
	}
	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getTotalTravelerCount() {
		return totalTravelerCount;
	}
	public void setTotalTravelerCount(String totalTravelerCount) {
		this.totalTravelerCount = totalTravelerCount;
	}
	public List<MoneyAmountVO> getReceivableAmount() {
		return receivableAmount;
	}
	public void setReceivableAmount(List<MoneyAmountVO> receivableAmount) {
		this.receivableAmount = receivableAmount;
	}
	public List<MoneyAmountVO> getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(List<MoneyAmountVO> receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public List<MoneyAmountVO> getArrivedAmount() {
		return arrivedAmount;
	}
	public void setArrivedAmount(List<MoneyAmountVO> arrivedAmount) {
		this.arrivedAmount = arrivedAmount;
	}
	public List<MoneyAmountVO> getUnreceiveAmount() {
		return unreceiveAmount;
	}
	public void setUnreceiveAmount(List<MoneyAmountVO> unreceiveAmount) {
		this.unreceiveAmount = unreceiveAmount;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getPayedMoney() {
		return payedMoney;
	}
	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}
	public String getAccountedMoney() {
		return accountedMoney;
	}
	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}
	public String getNotReceiviedMoney() {
		return notReceiviedMoney;
	}
	public void setNotReceiviedMoney(String notReceiviedMoney) {
		this.notReceiviedMoney = notReceiviedMoney;
	}
	
}
