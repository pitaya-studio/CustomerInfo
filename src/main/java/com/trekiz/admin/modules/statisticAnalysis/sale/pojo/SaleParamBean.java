package com.trekiz.admin.modules.statisticAnalysis.sale.pojo;

import java.math.BigDecimal;

/**
 * 封装统计分析模块销售方面的请求参数。
 */
public class SaleParamBean {

    private Integer orderType; // 订单类型，和系统定义的一致
    private String searchDate; // 分析的时间范围，1今日，2本周，3本月，4本年，5全部
    private String startDate; // 用户自定义时间范围的开始时间
    private String endDate; // 用户自定义时间范围的结束时间
    private Integer analysisType; // 分析类型，1订单数，2收客人数，3订单金额

    private String searchValue; // 销售名称
    private Integer orderNumBegin; // 订单数开始
    private Integer orderNumEnd; // 订单数结束
    private Integer orderPersonNumBegin; // 收客人数开始
    private Integer orderPersonNumEnd; // 收客人数结束
    private BigDecimal orderMoneyBegin; // 订单金额开始
    private BigDecimal orderMoneyEnd; // 订单金额结束
    private Integer orderBy; // 1：订单数倒序   2：订单数正序  3：收客人数倒序 4：收客人数正序 5订单金额倒序 6：订单金额正序

    private Integer pageNo; // 当前页码
    private Integer pageSize; // 每页显示多少条记录

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(Integer analysisType) {
        this.analysisType = analysisType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Integer getOrderNumBegin() {
        return orderNumBegin;
    }

    public void setOrderNumBegin(Integer orderNumBegin) {
        this.orderNumBegin = orderNumBegin;
    }

    public Integer getOrderNumEnd() {
        return orderNumEnd;
    }

    public void setOrderNumEnd(Integer orderNumEnd) {
        this.orderNumEnd = orderNumEnd;
    }

    public Integer getOrderPersonNumBegin() {
        return orderPersonNumBegin;
    }

    public void setOrderPersonNumBegin(Integer orderPersonNumBegin) {
        this.orderPersonNumBegin = orderPersonNumBegin;
    }

    public Integer getOrderPersonNumEnd() {
        return orderPersonNumEnd;
    }

    public void setOrderPersonNumEnd(Integer orderPersonNumEnd) {
        this.orderPersonNumEnd = orderPersonNumEnd;
    }

    public BigDecimal getOrderMoneyBegin() {
        return orderMoneyBegin;
    }

    public void setOrderMoneyBegin(BigDecimal orderMoneyBegin) {
        this.orderMoneyBegin = orderMoneyBegin;
    }

    public BigDecimal getOrderMoneyEnd() {
        return orderMoneyEnd;
    }

    public void setOrderMoneyEnd(BigDecimal orderMoneyEnd) {
        this.orderMoneyEnd = orderMoneyEnd;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
