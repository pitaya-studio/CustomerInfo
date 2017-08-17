package com.trekiz.admin.modules.statisticAnalysis.sale.pojo;

/**
 * Created by yudong.xu on 2017/3/8.
 */
public class SaleTopParamDTO {

    private String overView; // 查看类型 dd:询单总览 xd:订单总览
    private String searchDate; // 查询时间：1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部
    private String startDate; // 自定义开始时间
    private String endDate; // 自定义结束时间
    private String analysisType; // 分析类型，1订单数，2收客人数，3订单金额

    public SaleTopParamDTO() { }

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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }
}
