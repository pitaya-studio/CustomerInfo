package com.trekiz.admin.modules.mtourfinance.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quauq on 2016/6/21.
 */
public class OperatingRevenueJsonBean {

    private Integer salerId;
    private String salerName;
    private List<OperatingRevenueData> orders = new ArrayList<>();

    public OperatingRevenueJsonBean(Integer salerId, String salerName) {
        this.salerId = salerId;
        this.salerName = salerName;
    }

    public OperatingRevenueJsonBean() {
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

    public List<OperatingRevenueData> getOrders() {
        return orders;
    }

    public void setOrders(List<OperatingRevenueData> orders) {
        this.orders = orders;
    }
}
