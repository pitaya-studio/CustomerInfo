package com.trekiz.admin.modules.order.entity;

import java.math.BigDecimal;

public class OrderInvoiceVO {

	
	/** 开票方式*/
    private Integer invoiceMode;
    /** 开票类型*/
    private Integer invoiceType;
    /** 开票客户*/
    private String invoiceCustomer;
    /** 开票项目*/
    private Integer invoiceSubject;
    /** 发票抬头*/
	private String invoiceHead;
	/** 来款单位  0414 增加  update By pengfei.shang */
	private String invoiceComingUnit;
	/** 开票原因*/
	private String remarks;
    /**
     * 开票付款人
     */
    private String invoicePayMan;
    /**
     * 订单号
     */
    private Long[] orderId;
    
    /**
     * 支付记录标识  暂只针对订单开发票，不传递支付记录标识
     */
    
    private Long[] payId;
    /**团号*/
    private String[] groupCode;
    /** 每个支付记录的开票金额*/
    private BigDecimal[] orderInvoiceAmount;
    
    /** 订单单号 */
    private String[] orderNum;
    private String orderType;

    /** 订单类型数组 */
    private Integer[] orderTypes;
    /** 申请方式  0444*/
    private int applyInvoiceWay;
    /** 开票单位  	568 仅针对鼎鸿假期**/
    private int invoiceComeFromCompany;
    
    public int getInvoiceComeFromCompany() {
		return invoiceComeFromCompany;
	}
	public void setInvoiceComeFromCompany(int invoiceComeFromCompany) {
		this.invoiceComeFromCompany = invoiceComeFromCompany;
	}
	public int getApplyInvoiceWay() {
		return applyInvoiceWay;
	}
	public void setApplyInvoiceWay(int applyInvoiceWay) {
		this.applyInvoiceWay = applyInvoiceWay;
	}
	
	public Integer[] getOrderTypes() {
		return orderTypes;
	}
	public void setOrderTypes(Integer[] orderTypes) {
		this.orderTypes = orderTypes;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String[] getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String[] groupCode) {
		this.groupCode = groupCode;
	}
	public String[] getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String[] orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getInvoiceMode() {
		return invoiceMode;
	}
	public void setInvoiceMode(Integer invoiceMode) {
		this.invoiceMode = invoiceMode;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceCustomer() {
		return invoiceCustomer;
	}
	public void setInvoiceCustomer(String invoiceCustomer) {
		this.invoiceCustomer = invoiceCustomer;
	}
	public Integer getInvoiceSubject() {
		return invoiceSubject;
	}
	public void setInvoiceSubject(Integer invoiceSubject) {
		this.invoiceSubject = invoiceSubject;
	}
	public String getInvoiceHead() {
		return invoiceHead;
	}
	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getInvoicePayMan() {
		return invoicePayMan;
	}
	public void setInvoicePayMan(String invoicePayMan) {
		this.invoicePayMan = invoicePayMan;
	}
	public Long[] getOrderId() {
		return orderId;
	}
	public void setOrderId(Long[] orderId) {
		this.orderId = orderId;
	}
	public Long[] getPayId() {
		return payId;
	}
	public void setPayId(Long[] payId) {
		this.payId = payId;
	}
	public BigDecimal[] getOrderInvoiceAmount() {
		return orderInvoiceAmount;
	}
	public void setOrderInvoiceAmount(BigDecimal[] orderInvoiceAmount) {
		this.orderInvoiceAmount = orderInvoiceAmount;
	}
	//0414 新增，update by pengfei.shang
	public String getInvoiceComingUnit() {
		return invoiceComingUnit;
	}
	public void setInvoiceComingUnit(String invoiceComingUnit) {
		this.invoiceComingUnit = invoiceComingUnit;
	}
}
