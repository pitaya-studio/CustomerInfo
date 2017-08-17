package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.List;
import java.util.Set;

/**
 * 封装一个订单下所有的支出单，和对应的支出对象的Jsonbean. yudong.xu 2016.7.5
 */
public class OrderPaymentJsonBean {

    private Integer orderUuid; //订单id
    private String groupNo;  //团号
    private Set<PaymentObjJsonBean> payObj; //该订单下支付对象列表
    private List<PaymentItemData> paymentObject; //该订单下支付信息列表

    public OrderPaymentJsonBean() { }

    public Integer getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(Integer orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public Set<PaymentObjJsonBean> getPayObj() {
        return payObj;
    }

    public void setPayObj(Set<PaymentObjJsonBean> payObj) {
        this.payObj = payObj;
    }

    public List<PaymentItemData> getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(List<PaymentItemData> paymentObject) {
        this.paymentObject = paymentObject;
    }
}
