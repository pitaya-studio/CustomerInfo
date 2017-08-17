package com.trekiz.admin.modules.finance.param;

/**
 * Created by yudong.xu on 2016/8/24.
 * 付款模块的请求参数bean。包含成本付款，退款付款，返佣付款，借款付款页面。
 */
public class PaymentParam {

    private String groupCode; // 团号
    private String orderType; // 团队类型

    private String supplier; // 地接社
    private String agentId; // 渠道
    private String jd; // 计调
    private String creator; // 下单人
    private String saler; // 销售

    private String payStatus; // 付款状态
    private String printStatus; // 打印状态

    private String payMode; // 支付方式
    private String fromBank; // 来款银行

    private String payMoneyBegin; // 付款金额开始
    private String payMoneyEnd; // 付款金额结束

    private String createTimeBegin; // 申请日期开始
    private String createTimeEnd; // 申请日期结束

    private String cashierConfirmDateBegin; // 出纳确认时间开始
    private String cashierConfirmDateEnd; // 出纳确认时间结束

    private String payItem; // 款项(退款付款)

    private String traveler; // 游客(返佣付款)
    private String channelPayMode; // 渠道结算方式(返佣付款)

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSaler() {
        return saler;
    }

    public void setSaler(String saler) {
        this.saler = saler;
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

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getFromBank() {
        return fromBank;
    }

    public void setFromBank(String fromBank) {
        this.fromBank = fromBank;
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

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
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

    public String getPayItem() {
        return payItem;
    }

    public void setPayItem(String payItem) {
        this.payItem = payItem;
    }

    public String getTraveler() {
        return traveler;
    }

    public void setTraveler(String traveler) {
        this.traveler = traveler;
    }

    public String getChannelPayMode() {
        return channelPayMode;
    }

    public void setChannelPayMode(String channelPayMode) {
        this.channelPayMode = channelPayMode;
    }
}
