package com.trekiz.admin.modules.mtourfinance.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单列表-二级列表-付款记录jsonBean
 * ClassName: SecondRefundRecordsJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-1-29
 */
public class SecondRefundRecordsJsonBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String orderUuid;//'订单Uuid'
	private String paymentObj_paymentUuid;//'按付款对象付款的付款Uuid'
	private String paymentObjPaymentUuid;//'按付款对象付款的付款Uuid'(对应数据库的属性)
	private String paymentObjectUuid;//'付款对象Uuid'
	private Date paymentDate;//'付款日期'
	private String paymentTotalAmount;//'付款总额'
	private String convertedTotalAmount;//'转换后金额',
	private Integer paymentMethodCode;//'付款方式code'
	private String paymentMethodName;//'付款方式Name'
	private String paymentStatusCode;//'付款状态',//0-已付款 1-已撤销
	private String paymentStatusName;//'付款状态',//0-已付款 1-已撤销
	private String tourOperatorChannelCategoryCode;//'地接社/渠道商' 1-地接社 2-渠道商'
	private String payVoucher;//'支付凭证附件id
	private List<DocInfoJsonBean> attachments;//付款附件
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getPaymentObj_paymentUuid() {
		return paymentObj_paymentUuid;
	}
	public void setPaymentObj_paymentUuid(String paymentObj_paymentUuid) {
		this.paymentObj_paymentUuid = paymentObj_paymentUuid;
	}
	public String getPaymentObjectUuid() {
		return paymentObjectUuid;
	}
	public void setPaymentObjectUuid(String paymentObjectUuid) {
		this.paymentObjectUuid = paymentObjectUuid;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentTotalAmount() {
		return paymentTotalAmount;
	}
	public void setPaymentTotalAmount(String paymentTotalAmount) {
		this.paymentTotalAmount = paymentTotalAmount;
	}
	public String getConvertedTotalAmount() {
		return convertedTotalAmount;
	}
	public void setConvertedTotalAmount(String convertedTotalAmount) {
		this.convertedTotalAmount = convertedTotalAmount;
	}
	public Integer getPaymentMethodCode() {
		return paymentMethodCode;
	}
	public void setPaymentMethodCode(Integer paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}
	public String getPaymentMethodName() {
		return paymentMethodName;
	}
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}
	public String getPaymentStatusCode() {
		return paymentStatusCode;
	}
	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}
	public String getPaymentStatusName() {
		return paymentStatusName;
	}
	public void setPaymentStatusName(String paymentStatusName) {
		this.paymentStatusName = paymentStatusName;
	}
	public List<DocInfoJsonBean> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<DocInfoJsonBean> attachments) {
		this.attachments = attachments;
	}
	public String getPayVoucher() {
		return payVoucher;
	}
	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}
	public String getPaymentObjPaymentUuid() {
		return paymentObjPaymentUuid;
	}
	public void setPaymentObjPaymentUuid(String paymentObjPaymentUuid) {
		this.paymentObjPaymentUuid = paymentObjPaymentUuid;
	}
	public String getTourOperatorChannelCategoryCode() {
		return tourOperatorChannelCategoryCode;
	}
	public void setTourOperatorChannelCategoryCode(
			String tourOperatorChannelCategoryCode) {
		this.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
	}
	
	

}
