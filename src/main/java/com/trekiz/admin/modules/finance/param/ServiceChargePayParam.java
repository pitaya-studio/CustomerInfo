package com.trekiz.admin.modules.finance.param;

/**
 * 服务费付款页面，查询参数封装bean。
 * yudong.xu 2016.9.2
 */
public class ServiceChargePayParam {

    private String groupCode; // 团号
    private Integer jd; // 计调
    private Integer salerId; // 销售
    private Integer orderType; // 团队类型

    private Integer settleAgentId; // 结算方
    private String payStatus; // 付款状态
    private String printStatus; // 打印状态

    private String payMoneyBegin; // 付款金额开始
    private String payMoneyEnd; // 付款金额结束

    private String cashierConfirmDateBegin; // 出纳确认时间开始
    private String cashierConfirmDateEnd; // 出纳确认时间结束

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getJd() {
        return jd;
    }

    public void setJd(Integer jd) {
        this.jd = jd;
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getSettleAgentId() {
        return settleAgentId;
    }

    public void setSettleAgentId(Integer settleAgentId) {
        this.settleAgentId = settleAgentId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getPayMoneyBegin() {
        return payMoneyBegin;
    }

    public void setPayMoneyBegin(String payMoneyBegin) {
        this.payMoneyBegin = payMoneyBegin;
    }

    public String getPayMoneyEnd() {
        return payMoneyEnd;
    }

    public void setPayMoneyEnd(String payMoneyEnd) {
        this.payMoneyEnd = payMoneyEnd;
    }

    public String getCashierConfirmDateBegin() {
        return cashierConfirmDateBegin;
    }

    public void setCashierConfirmDateBegin(String cashierConfirmDateBegin) {
        this.cashierConfirmDateBegin = cashierConfirmDateBegin;
    }

    public String getCashierConfirmDateEnd() {
        return cashierConfirmDateEnd;
    }

    public void setCashierConfirmDateEnd(String cashierConfirmDateEnd) {
        this.cashierConfirmDateEnd = cashierConfirmDateEnd;
    }
}
