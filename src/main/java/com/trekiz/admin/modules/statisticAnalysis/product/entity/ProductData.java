package com.trekiz.admin.modules.statisticAnalysis.product.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 预统计分析表
 *
 * @author mbmr
 * @date 2016-12-22
 */
public class ProductData implements Serializable {


    /**
     * 产品id
     */
    private int productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品类型
     */
    private int orderType;
    /**
     * 收款金额
     */
    private long orderMoney;
    /**
     * 订单数
     */
    private int orderNum;
    /**
     * 收客人数
     */
    private int orderPersonNum;
    /**
     * 创建时间
     */
    private Date createDate;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(long orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderPersonNum() {
        return orderPersonNum;
    }

    public void setOrderPersonNum(int orderPersonNum) {
        this.orderPersonNum = orderPersonNum;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
