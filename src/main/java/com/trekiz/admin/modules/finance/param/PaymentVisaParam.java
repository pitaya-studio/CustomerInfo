package com.trekiz.admin.modules.finance.param;

/**
 * Created by yudong.xu on 2016/8/24.
 * 签证批量借款付款页面的请求参数bean。单独的bean，避免冗余的属性。
 */
public class PaymentVisaParam {

    private String groupCode; // 团号

    private String visaType; // 签证类型
    private String visaCountry; // 签证国家

    private String saler; // 销售
    private String jd; // 计调

    private String payStatus; // 付款状态
    private String printStatus; // 打印状态

    private String reportDateBegin; // 报批日期开始
    private String reportDateEnd; // 报批日期结束

    private String createTimeBegin; // 申请日期开始
    private String createTimeEnd; // 申请日期结束

    private String payMoneyBegin; // 付款金额开始
    private String payMoneyEnd; // 付款金额结束

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public String getVisaCountry() {
        return visaCountry;
    }

    public void setVisaCountry(String visaCountry) {
        this.visaCountry = visaCountry;
    }

    public String getSaler() {
        return saler;
    }

    public void setSaler(String saler) {
        this.saler = saler;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
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

    public String getReportDateBegin() {
        return reportDateBegin;
    }

    public void setReportDateBegin(String reportDateBegin) {
        this.reportDateBegin = reportDateBegin;
    }

    public String getReportDateEnd() {
        return reportDateEnd;
    }

    public void setReportDateEnd(String reportDateEnd) {
        this.reportDateEnd = reportDateEnd;
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
}
