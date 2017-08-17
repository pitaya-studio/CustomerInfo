package com.trekiz.admin.modules.statisticAnalysis.sale.json;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 获取前几名销售数据的json返回对象。
 */
public class SaleTopJsonBean {

    private List<Map<String,Object>> saleData; // 销售数据
    private String otherData; // 其他销售数据
    private Integer saleSum; // 总的销售数

    private String serverTime; // 系统时间

    public SaleTopJsonBean() { }

    public SaleTopJsonBean(List<Map<String, Object>> saleData, String otherData, Integer saleSum, String serverTime) {
        this.saleData = saleData;
        this.otherData = otherData;
        this.saleSum = saleSum;
        this.serverTime = serverTime;
    }

    public List<Map<String, Object>> getSaleData() {
        return saleData;
    }

    public void setSaleData(List<Map<String, Object>> saleData) {
        this.saleData = saleData;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public Integer getSaleSum() {
        return saleSum;
    }

    public void setSaleSum(Integer saleSum) {
        this.saleSum = saleSum;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
