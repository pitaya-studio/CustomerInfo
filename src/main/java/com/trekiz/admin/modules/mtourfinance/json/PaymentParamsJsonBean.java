package com.trekiz.admin.modules.mtourfinance.json;

import com.alibaba.fastjson.annotation.JSONType;
import java.util.List;

/**
 * 创建下载支出单时请求参数JsonBean。 yudong.xu 2016.7.7
 */
@JSONType(ignores = {"groupNo","paymentObjectName"})
public class PaymentParamsJsonBean {

    private String orderUuid;
    private String groupNo;
    private String paymentObjectUuid;
    private String paymentObjectName;
    private List<PaymentParamsItem> fundsTypePayList;

    public PaymentParamsJsonBean() { }

    public PaymentParamsJsonBean(String orderUuid, String groupNo, String paymentObjectUuid, String paymentObjectName,
                                 List<PaymentParamsItem> fundsTypePayList) {
        this.orderUuid = orderUuid;
        this.groupNo = groupNo;
        this.paymentObjectUuid = paymentObjectUuid;
        this.paymentObjectName = paymentObjectName;
        this.fundsTypePayList = fundsTypePayList;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
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

    public List<PaymentParamsItem> getFundsTypePayList() {
        return fundsTypePayList;
    }

    public void setFundsTypePayList(List<PaymentParamsItem> fundsTypePayList) {
        this.fundsTypePayList = fundsTypePayList;
    }
}
