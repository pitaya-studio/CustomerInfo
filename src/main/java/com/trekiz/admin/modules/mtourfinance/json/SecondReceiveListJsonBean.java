package com.trekiz.admin.modules.mtourfinance.json;

import java.io.Serializable;
import java.util.List;

import com.trekiz.admin.modules.mtourfinance.pojo.MoneyAmountVO;
import com.trekiz.admin.modules.mtourfinance.pojo.ReceiveFundsTypeVO;


/**
 * 订单列表-二级列表-收款子列表jsonBean
 * ClassName: SecondReceiveListJsonBean
 * @Description: 用于封装收款二级列表的jsonBean
 * @author wangyang
 * @date 2016-3-9
 */
public class SecondReceiveListJsonBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String receiveUuid;	//收款Uuid
	private ReceiveFundsTypeVO receiveFundsType;	//收款类型 订单/其他收入
	
	private String receiveDate;	//收款日期
	private String arrivalBankDate;	//银行到账日期
	
	private String orderUuid;	//订单Uuid
	private String fundsName;	//款项名称
	
	private String receiveTypeName;	//收款类别Name
	private String receiveTypeCode;	//收款类别Code
	
	private String tourOperatorOrChannelName;	//地接社/渠道商名称
	private String paymentCompany;	//付款单位
	private String receiver;	//收款人
	private String receiveStatusCode;	//收款状态Code
	
	private List<MoneyAmountVO> receivedAmount; //已收金额
	private List<MoneyAmountVO> arrivedAmount;	//到账金额
	
	
	public String getReceiveUuid() {
		return receiveUuid;
	}
	public void setReceiveUuid(String receiveUuid) {
		this.receiveUuid = receiveUuid;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getArrivalBankDate() {
		return arrivalBankDate;
	}
	public void setArrivalBankDate(String arrivalBankDate) {
		this.arrivalBankDate = arrivalBankDate;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getFundsName() {
		return fundsName;
	}
	public void setFundsName(String fundsName) {
		this.fundsName = fundsName;
	}
	public String getReceiveTypeName() {
		return receiveTypeName;
	}
	public void setReceiveTypeName(String receiveTypeName) {
		this.receiveTypeName = receiveTypeName;
	}
	public String getReceiveTypeCode() {
		return receiveTypeCode;
	}
	public void setReceiveTypeCode(String receiveTypeCode) {
		this.receiveTypeCode = receiveTypeCode;
	}
	public String getTourOperatorOrChannelName() {
		return tourOperatorOrChannelName;
	}
	public void setTourOperatorOrChannelName(String tourOperatorOrChannelName) {
		this.tourOperatorOrChannelName = tourOperatorOrChannelName;
	}
	public String getPaymentCompany() {
		return paymentCompany;
	}
	public void setPaymentCompany(String paymentCompany) {
		this.paymentCompany = paymentCompany;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiveStatusCode() {
		return receiveStatusCode;
	}
	public void setReceiveStatusCode(String receiveStatusCode) {
		this.receiveStatusCode = receiveStatusCode;
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
	public ReceiveFundsTypeVO getReceiveFundsType() {
		return receiveFundsType;
	}
	public void setReceiveFundsType(ReceiveFundsTypeVO receiveFundsType) {
		this.receiveFundsType = receiveFundsType;
	}


	
	
}
