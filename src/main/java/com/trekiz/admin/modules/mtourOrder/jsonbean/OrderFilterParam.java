package com.trekiz.admin.modules.mtourOrder.jsonbean;


public class OrderFilterParam {
	private String orderDateTime;//下单日期
	private String departureDate;//出团日期
	private String ordererId;//下单人Id
	private String payment_orderPaymentStatusCode;//['订单付款状态']//1-已付全款，0-未付全款
	private String orderReceiveStatusCodeStatusCode;//['订单收款状态']//0-待收款,1-部分定金，2-已收定金,3-已收全款
	private String pnrValue;
	
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getOrdererId() {
		return ordererId;
	}
	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}
	public String getPayment_orderPaymentStatusCode() {
		return payment_orderPaymentStatusCode;
	}
	public void setPayment_orderPaymentStatusCode(
			String payment_orderPaymentStatusCode) {
		this.payment_orderPaymentStatusCode = payment_orderPaymentStatusCode;
	}
	public String getOrderReceiveStatusCodeStatusCode() {
		return orderReceiveStatusCodeStatusCode;
	}
	public void setOrderReceiveStatusCodeStatusCode(
			String orderReceiveStatusCodeStatusCode) {
		this.orderReceiveStatusCodeStatusCode = orderReceiveStatusCodeStatusCode;
	}
	public String getPnrValue() {
		return pnrValue;
	}
	public void setPnrValue(String pnrValue) {
		this.pnrValue = pnrValue;
	}
	
}
