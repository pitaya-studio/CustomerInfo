package com.trekiz.admin.modules.finance.param;

/**
 * Created by yudong.xu on 2016/8/24.
 * 发票模块的请求参数bean。包括发票记录，待审核发票，已审核发票，开票页面。
 * 也包括收据管理模块的请求参数bean，发票和收据的请求参数相同，所以使用相同的参数bean。
 */
public class InvoiceParam {

    private String invoiceNum; // 发票(收据)号
    private String invoiceHead; // 发票(收据)抬头

    private String orderNum; // 订单号,预定单号
    private String groupCode; // 团号

    private String invoiceType; // 开票(收据)类型
    private String invoiceMode; // 开票(收据)方式
    private String invoiceCustomer; // 开票(收据)客户
    private String invoiceSubject; // 开票(收据)项目
    private String createStatus; // 开票(收据)状态

    private String verifyStatus; // 审核状态

    private String invoiceTimeBegin; // 开票(收据)日期开始
    private String invoiceTimeEnd; // 开票(收据)日期结束

    private String invoiceMoneyBegin; // 发票(收据)金额开始
    private String invoiceMoneyEnd; // 发票(收据)金额结束

    private String createName; // 申请人
    private String applyInvoiceType; // 申请类型
    private String applyInvoiceBegin; // 申请日期开始
    private String applyInvoiceEnd; // 申请日期结束

    private String orderTimeBegin; // 下单日期开始
    private String orderTimeEnd; // 下单日期结束

    private String invoiceComingUnit; // 来款单位(虽然单位翻译错误)

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceMode() {
        return invoiceMode;
    }

    public void setInvoiceMode(String invoiceMode) {
        this.invoiceMode = invoiceMode;
    }

    public String getInvoiceCustomer() {
        return invoiceCustomer;
    }

    public void setInvoiceCustomer(String invoiceCustomer) {
        this.invoiceCustomer = invoiceCustomer;
    }

    public String getInvoiceSubject() {
        return invoiceSubject;
    }

    public void setInvoiceSubject(String invoiceSubject) {
        this.invoiceSubject = invoiceSubject;
    }

    public String getCreateStatus() {
        return createStatus;
    }

    public void setCreateStatus(String createStatus) {
        this.createStatus = createStatus;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getInvoiceTimeBegin() {
        return invoiceTimeBegin;
    }

    public void setInvoiceTimeBegin(String invoiceTimeBegin) {
        this.invoiceTimeBegin = invoiceTimeBegin;
    }

    public String getInvoiceTimeEnd() {
        return invoiceTimeEnd;
    }

    public void setInvoiceTimeEnd(String invoiceTimeEnd) {
        this.invoiceTimeEnd = invoiceTimeEnd;
    }

    public String getInvoiceMoneyBegin() {
        return invoiceMoneyBegin;
    }

    public void setInvoiceMoneyBegin(String invoiceMoneyBegin) {
        this.invoiceMoneyBegin = invoiceMoneyBegin;
    }

    public String getInvoiceMoneyEnd() {
        return invoiceMoneyEnd;
    }

    public void setInvoiceMoneyEnd(String invoiceMoneyEnd) {
        this.invoiceMoneyEnd = invoiceMoneyEnd;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getApplyInvoiceType() {
        return applyInvoiceType;
    }

    public void setApplyInvoiceType(String applyInvoiceType) {
        this.applyInvoiceType = applyInvoiceType;
    }

    public String getApplyInvoiceBegin() {
        return applyInvoiceBegin;
    }

    public void setApplyInvoiceBegin(String applyInvoiceBegin) {
        this.applyInvoiceBegin = applyInvoiceBegin;
    }

    public String getApplyInvoiceEnd() {
        return applyInvoiceEnd;
    }

    public void setApplyInvoiceEnd(String applyInvoiceEnd) {
        this.applyInvoiceEnd = applyInvoiceEnd;
    }

    public String getOrderTimeBegin() {
        return orderTimeBegin;
    }

    public void setOrderTimeBegin(String orderTimeBegin) {
        this.orderTimeBegin = orderTimeBegin;
    }

    public String getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(String orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getInvoiceComingUnit() {
        return invoiceComingUnit;
    }

    public void setInvoiceComingUnit(String invoiceComingUnit) {
        this.invoiceComingUnit = invoiceComingUnit;
    }
}
