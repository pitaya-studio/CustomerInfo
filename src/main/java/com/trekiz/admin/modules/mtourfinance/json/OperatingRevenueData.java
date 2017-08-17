package com.trekiz.admin.modules.mtourfinance.json;

import com.trekiz.admin.common.utils.MoneyNumberFormat;

import java.math.BigDecimal;

/**
 * Created by quauq on 2016/6/21.
 */
public class OperatingRevenueData {

    private Integer salerId;
    private String salerName;
    private String groupCode;
    private Object totalMoney;
    private Object cost;
    private Object profit;
    private Object receivable;

    public OperatingRevenueData(Integer salerId, String salerName, String groupCode,
                                Object totalMoney, Object cost, Object profit, Object receivable) {
        this.salerId = salerId;
        this.salerName = salerName;
        this.groupCode = groupCode;
        this.totalMoney = totalMoney;
        this.cost = cost;
        this.profit = profit;
        this.receivable = receivable;
    }

    public OperatingRevenueData() {
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Object getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Object totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Object getCost() {
        return cost;
    }

    public void setCost(Object cost) {
        this.cost = cost;
    }

    public Object getProfit() {
        return profit;
    }

    public void setProfit(Object profit) {
        this.profit = profit;
    }

    public Object getReceivable() {
        return receivable;
    }

    public void setReceivable(Object receivable) {
        this.receivable = receivable;
    }

    public void convertToMoneyNumberFormat(){
        totalMoney = MoneyNumberFormat.getRoundMoney((BigDecimal)totalMoney,2,BigDecimal.ROUND_HALF_UP);
        cost = MoneyNumberFormat.getRoundMoney((BigDecimal)cost,2,BigDecimal.ROUND_HALF_UP);
        profit = MoneyNumberFormat.getRoundMoney((BigDecimal)profit,2,BigDecimal.ROUND_HALF_UP);
        receivable = MoneyNumberFormat.getRoundMoney((BigDecimal)receivable,2,BigDecimal.ROUND_HALF_UP);
    }
}
