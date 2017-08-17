package com.trekiz.admin.modules.statisticAnalysis.sale.pojo;

/**
 * 销售统计详情页(带询单)参数
 * @author gaoyang
 * @Time 2017-3-8 下午2:57:36
 */
public class SaleAnalysisParam {

    private String overView; // dd：订单总览  xd：询单总览
    private String searchDate; // 时间 1：今日-1：昨日3：本月 -3：上月4：本年-4：去年 5：全部
    private String startDate; // 自定义开始时间
    private String endDate; // 自定义结束时间
    private String searchValue; // 搜索框值	
    private String orderBy; // 排序  1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
    private String orderType; // 订单类型    		
    private String pageNo; // 当前页
    private String pageSize; // 每页多少条
    private String count; // 列表总条数
    private String pageTab; // 详情页类型  1：渠道  2：销售  3：产品
    private String startRealDate; // 全部开始时间(第一次产生询单或者订单时间)
    private String endRealDate; // 全部结束时间(最后一次产生询单或者订单时间)
    private String analysisType; // 分析类型，-1：代表询单1：订单数2：收客人数3：订单金额
    
	public String getOverView() {
		return overView;
	}
	public void setOverView(String overView) {
		this.overView = overView;
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
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPageTab() {
		return pageTab;
	}
	public void setPageTab(String pageTab) {
		this.pageTab = pageTab;
	}
	public String getStartRealDate() {
		return startRealDate;
	}
	public void setStartRealDate(String startRealDate) {
		this.startRealDate = startRealDate;
	}
	public String getEndRealDate() {
		return endRealDate;
	}
	public void setEndRealDate(String endRealDate) {
		this.endRealDate = endRealDate;
	}
	public String getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
   
}
