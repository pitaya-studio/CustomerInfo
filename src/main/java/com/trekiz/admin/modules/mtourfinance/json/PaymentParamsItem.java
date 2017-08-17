package com.trekiz.admin.modules.mtourfinance.json;

/**
 * 请求下载支出单参数的对象内部JsonBean. yudong.xu 2016.7.7
 */
public class PaymentParamsItem {
    private String paymentUuid; //支付条目item的id.
    private String fundsType; //支付条目的类型。

    public PaymentParamsItem() { }

    public PaymentParamsItem(String paymentUuid, String fundsType) {
        this.paymentUuid = paymentUuid;
        this.fundsType = fundsType;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public String getFundsType() {
        return fundsType;
    }

    public void setFundsType(String fundsType) {
        this.fundsType = fundsType;
    }
}
