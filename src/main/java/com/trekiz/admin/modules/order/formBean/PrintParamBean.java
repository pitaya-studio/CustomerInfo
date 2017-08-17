package com.trekiz.admin.modules.order.formBean;

/**
 * 统一打印页面的请求参数
 */
public class PrintParamBean {

    private Long orderId;
    private Integer orderType;
    private String reviewId;

    private String isShowPrint;
    private Integer reviewFlag;
    private String payId;
    private String option;

    private String batchNo;
    private String isPrintFlag;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getIsShowPrint() {
        return isShowPrint;
    }

    public void setIsShowPrint(String isShowPrint) {
        this.isShowPrint = isShowPrint;
    }

    public Integer getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(Integer reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIsPrintFlag() {
        return isPrintFlag;
    }

    public void setIsPrintFlag(String isPrintFlag) {
        this.isPrintFlag = isPrintFlag;
    }
}
