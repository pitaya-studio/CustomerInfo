package com.trekiz.admin.modules.mtourfinance.pojo;

/**
 * 财务中心-收款-订单列表数据对象
 * @author shijun.liu
 */
public class ReceiveOrderListData {
    private String orderUuid;                        // 订单ID
    private String groupNo;                          // 团号
    private String orderDateTime;                    // 下单时间
    private String departureDate;                    // 出团日期
    private String orderer;                          // 下单人姓名
    private String totalArriveAmount;                // 累计到账金额
    private String orderAmount;                      // 订单总额
    private String orderReceiveStatusCodeStatusCode; // 订单收款状态 ,//0-待收款,1-部分定金，2-已收定金,3-已收全款

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

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getOrderer() {
        return orderer;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public String getTotalArriveAmount() {
        return totalArriveAmount;
    }

    public void setTotalArriveAmount(String totalArriveAmount) {
        this.totalArriveAmount = totalArriveAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderReceiveStatusCodeStatusCode() {
        return orderReceiveStatusCodeStatusCode;
    }

    public void setOrderReceiveStatusCodeStatusCode(String orderReceiveStatusCodeStatusCode) {
        this.orderReceiveStatusCodeStatusCode = orderReceiveStatusCodeStatusCode;
    }
}
