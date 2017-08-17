package com.trekiz.admin.modules.order.pojo;

import java.util.Arrays;
import java.util.List;

public class OrderPayInput {
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 调用支付信息保存的service方法前和后要执行的类和方法
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 调用模块的service类名（包含包名） */
	private String serviceClassName;
	/** 调用模块的service前置方法名 */
	private String serviceBeforeMethodName;
	/** 调用模块的service后置方法名 */
	private String serviceAfterMethodName;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 调用支付功能和数据相关的参数
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 1：收款（***->批发商） 、操作orderpay表 2：付款<包含退款、返佣等>（环球行->***） 、操作refund表 3:团期付款,操作pay_group表*/
	private String payType = "1";
	/** 渠道商类型 0: 地接社, 1:渠道商 */
	private Integer supplyType = 1;
	/** 渠道商、批发商、地接社ID */
	private Integer agentId;

	/** 支付详细信息 */
	private List<OrderPayDetail> orderPayDetailList;
	/** 总金额的币种ID */
	private String[] totalCurrencyId;
	/** 总金额的金额 */
	private String[] totalCurrencyPrice;

	/** 支付页面上方的导航图片类型（最开始的图片导航为0，现在不显示为1，以备将来修改用） */
	private Integer navigationImgFlag = 1;
	/** 订单总额显示状态(true:显示;false:不显示) */
	private boolean totalCurrencyFlag = false;
	/** 月结/后付费的状态(0:月结/后续费可操作;1:月结/后续费不可操作-disbled状态) */
	private Integer paymentTypeRadioFlag = 0;
	/** input金额状态(0:不显示；1：显示、可操作；2：显示、只读) */
	private Integer moneyFlag = 2;

	/** input订单类型（1、单团；2、散拼；3、游学；4、大客户；5、自由行；6、签证；7、机票；8、套餐；9、其他；10、游轮；11、酒店；12、海岛游、） */
	private Integer orderType;

	/** 用户自定义金额流水号（目前签证批量付款时使用） */
	private String moneySerialNum;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 支付成功页面的，我的订单、订单详情的URL及其他参数
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 我的订单、订单列表的URL */
	private String orderListUrl;
	/** 订单详情的URL */
	private String orderDetailUrl;
	/** 付款时的款项描述 */
	private String refundMoneyTypeDesc;
	/** 付款列表 */
	private String paymentListUrl;
	/** 进入订单的URL */
	private String entryOrderUrl;
	
	/** 订单中是否有预开发票  0444需求*/
	private Boolean hasPreOpeninvoice;
	/** 付款类型,目前只对签证付款设置为"1",则程序中可在model中添加hasPreOpeninvoice(针对运控成本录入，返佣付款等不用显示关联发票)  0444需求*/
	private String comingPay;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 支付功能处理后返回保存的值
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	private String payTypeDesc = "收款";

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	//结算方 用于代收服务费付款 add by zhangchao 2016-09-02
	private String settleName;
	
	public String getComingPay() {
		return comingPay;
	}

	public void setComingPay(String comingPay) {
		this.comingPay = comingPay;
	}
	
	public Boolean getHasPreOpeninvoice() {
		return hasPreOpeninvoice;
	}

	public void setHasPreOpeninvoice(Boolean hasPreOpeninvoice) {
		this.hasPreOpeninvoice = hasPreOpeninvoice;
	}
	
	public String getServiceClassName() {
		return serviceClassName;
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	public String getServiceBeforeMethodName() {
		return serviceBeforeMethodName;
	}

	public void setServiceBeforeMethodName(String serviceBeforeMethodName) {
		this.serviceBeforeMethodName = serviceBeforeMethodName;
	}

	public String getServiceAfterMethodName() {
		return serviceAfterMethodName;
	}

	public void setServiceAfterMethodName(String serviceAfterMethodName) {
		this.serviceAfterMethodName = serviceAfterMethodName;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public List<OrderPayDetail> getOrderPayDetailList() {
		return orderPayDetailList;
	}

	public void setOrderPayDetailList(List<OrderPayDetail> orderPayDetailList) {
		this.orderPayDetailList = orderPayDetailList;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderListUrl() {
		return orderListUrl;
	}

	public void setOrderListUrl(String orderListUrl) {
		this.orderListUrl = orderListUrl;
	}

	public String getOrderDetailUrl() {
		return orderDetailUrl;
	}

	public void setOrderDetailUrl(String orderDetailUrl) {
		this.orderDetailUrl = orderDetailUrl;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Integer getNavigationImgFlag() {
		return navigationImgFlag;
	}

	public void setNavigationImgFlag(Integer navigationImgFlag) {
		this.navigationImgFlag = navigationImgFlag;
	}

	public Integer getPaymentTypeRadioFlag() {
		return paymentTypeRadioFlag;
	}

	public void setPaymentTypeRadioFlag(Integer paymentTypeRadioFlag) {
		this.paymentTypeRadioFlag = paymentTypeRadioFlag;
	}

	public Integer getMoneyFlag() {
		return moneyFlag;
	}

	public void setMoneyFlag(Integer moneyFlag) {
		this.moneyFlag = moneyFlag;
	}

	public String getMoneySerialNum() {
		return moneySerialNum;
	}

	public void setMoneySerialNum(String moneySerialNum) {
		this.moneySerialNum = moneySerialNum;
	}
	
	public String[] getTotalCurrencyId() {
		return totalCurrencyId;
	}

	public void setTotalCurrencyId(String[] totalCurrencyId) {
		this.totalCurrencyId = totalCurrencyId;
	}

	public String[] getTotalCurrencyPrice() {
		return totalCurrencyPrice;
	}

	public void setTotalCurrencyPrice(String[] totalCurrencyPrice) {
		this.totalCurrencyPrice = totalCurrencyPrice;
	}

	public String getPayTypeDesc() {
		return payTypeDesc;
	}

	public void setPayTypeDesc(String payTypeDesc) {
		this.payTypeDesc = payTypeDesc;
	}

	public boolean isTotalCurrencyFlag() {
		return totalCurrencyFlag;
	}

	public void setTotalCurrencyFlag(boolean totalCurrencyFlag) {
		this.totalCurrencyFlag = totalCurrencyFlag;
	}

	public String getRefundMoneyTypeDesc() {
		return refundMoneyTypeDesc;
	}

	public void setRefundMoneyTypeDesc(String refundMoneyTypeDesc) {
		this.refundMoneyTypeDesc = refundMoneyTypeDesc;
	}
	
	public String getPaymentListUrl() {
		return paymentListUrl;
	}

	public void setPaymentListUrl(String paymentListUrl) {
		this.paymentListUrl = paymentListUrl;
	}

	public String getEntryOrderUrl() {
		return entryOrderUrl;
	}

	public void setEntryOrderUrl(String entryOrderUrl) {
		this.entryOrderUrl = entryOrderUrl;
	}
	
	@Override
	public String toString() {
		return "OrderPayInput [serviceClassName=" + serviceClassName
				+ ", serviceBeforeMethodName=" + serviceBeforeMethodName
				+ ", serviceAfterMethodName=" + serviceAfterMethodName
				+ ", payType=" + payType + ", supplyType=" + supplyType
				+ ", agentId=" + agentId + ", orderPayDetailList="
				+ orderPayDetailList + ", totalCurrencyId="
				+ Arrays.toString(totalCurrencyId) + ", totalCurrencyPrice="
				+ Arrays.toString(totalCurrencyPrice) + ", navigationImgFlag="
				+ navigationImgFlag + ", paymentTypeRadioFlag="
				+ paymentTypeRadioFlag + ", moneyFlag=" + moneyFlag
				+ ", orderListUrl=" + orderListUrl + ", orderDetailUrl="
				+ orderDetailUrl + ", payTypeDesc=" + payTypeDesc + "]";
	}

	public String getSettleName() {
		return settleName;
	}

	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}

}
