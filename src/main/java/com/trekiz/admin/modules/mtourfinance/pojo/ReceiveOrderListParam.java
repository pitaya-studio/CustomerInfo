package com.trekiz.admin.modules.mtourfinance.pojo;

/**
 * 财务中心-收款-订单列表参数
 * @author shijun.liu
 */
public class ReceiveOrderListParam {
    private String searchType;		//搜索类型
    private String searchKey;		//搜索关键字
    private String orderDateTime;   //下单日期
    private String departureDate;   //出团日期
    private String orderedId;		//下单人
    private String receiveStatus;  //订单收款状态
    private Integer pageNow;		//当前页
    private Integer pageCount;		//每页显示数目
    private String orderBy;			//排序字段		字段名称 desc / 字段名称 asc

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
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

    public String getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(String orderedId) {
        this.orderedId = orderedId;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Integer getPageNow() {
        return pageNow;
    }

    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
