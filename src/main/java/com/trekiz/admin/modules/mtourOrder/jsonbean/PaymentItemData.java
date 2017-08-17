package com.trekiz.admin.modules.mtourOrder.jsonbean;

/**
 * 批量查询支出单时，封装支出记录item的jsonbean. yudong.xu 2016.7.5
 */
public class PaymentItemData {
    private Integer paymentUuid; //支付信息id
    private String approvalDate; //报批日期
    private String paymentObjectUuid; //支付对象uuid
    private String paymentObjectName; //支付对象名称
    private String fundsType; //款项类型
    private String fundsTypeName; //款项类型名称
    private String fundsName; //款项名称
    private String fundsPNR; //PNR
    private String fundsCost; //应付金额
    private Integer currencyId; //币种id

    public PaymentItemData() { }

    public Integer getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(Integer paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getPaymentObjectUuid() {
        return paymentObjectUuid;
    }

    public void setPaymentObjectUuid(String paymentObjectUuid) {
        this.paymentObjectUuid = paymentObjectUuid;
    }

    public String getPaymentObjectName() {
        return paymentObjectName;
    }

    public void setPaymentObjectName(String paymentObjectName) {
        this.paymentObjectName = paymentObjectName;
    }

    public String getFundsType() {
        return fundsType;
    }

    public void setFundsType(String fundsType) {
        this.fundsType = fundsType;
    }

    public String getFundsTypeName() {
        return fundsTypeName;
    }

    public void setFundsTypeName(String fundsTypeName) {
        this.fundsTypeName = fundsTypeName;
    }

    public String getFundsName() {
        return fundsName;
    }

    public void setFundsName(String fundsName) {
        this.fundsName = fundsName;
    }

    public String getFundsPNR() {
        return fundsPNR;
    }

    public void setFundsPNR(String fundsPNR) {
        this.fundsPNR = fundsPNR;
    }

    public String getFundsCost() {
        return fundsCost;
    }

    public void setFundsCost(String fundsCost) {
        this.fundsCost = fundsCost;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
