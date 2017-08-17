package com.trekiz.admin.modules.mtourOrder.jsonbean;

/**
 * 支出单，批量查询时，支付对象封装bean。 yudong.xu 2016.7.5
 */
public class PaymentObjJsonBean {
    private String paymentObjectUuid; //支付对象的id 或者 uuid。
    private String paymentObjectName; //支付对象的名称。

    public PaymentObjJsonBean() { }

    public PaymentObjJsonBean(String paymentObjectUuid, String paymentObjectName) {
        this.paymentObjectUuid = paymentObjectUuid;
        this.paymentObjectName = paymentObjectName;
    }

    public String getPaymentObjectName() {
        return paymentObjectName;
    }

    public void setPaymentObjectName(String paymentObjectName) {
        this.paymentObjectName = paymentObjectName;
    }

    public String getPaymentObjectUuid() {
        return paymentObjectUuid;
    }

    public void setPaymentObjectUuid(String paymentObjectUuid) {
        this.paymentObjectUuid = paymentObjectUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentObjJsonBean that = (PaymentObjJsonBean) o;

        return !(paymentObjectUuid != null ? !paymentObjectUuid.equals(that.paymentObjectUuid) : that.paymentObjectUuid != null);

    }

    @Override
    public int hashCode() {
        return paymentObjectUuid != null ? paymentObjectUuid.hashCode() : 0;
    }
}
