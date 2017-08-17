package com.trekiz.admin.agentToOffice.T2.pojo;

import java.math.BigDecimal;

/**
 * Created by quauq on 2016/8/15.
 * 用来临时保存前端传来的默认费率设置的项，对应一个input元素。 yudong.xu
 */
public class CompanyRateItem {

    private String agentName;
    private String rateName;
    private Integer rateType;
    private BigDecimal rate;

    public CompanyRateItem() { }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
