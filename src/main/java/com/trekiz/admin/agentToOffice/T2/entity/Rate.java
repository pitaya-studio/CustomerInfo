package com.trekiz.admin.agentToOffice.T2.entity;

import java.math.BigDecimal;

/**
 * 给下订单或者需要显示供应价的返回费率对象
 * @author shijun.liu
 * @date   2016.08.10
 */
public class Rate {

    private Integer agentType;          // 渠道类型，1:门店，2:总社，3:集团客户
    private Integer quauqRateType;       //费率类型，0:百分比，1:金额
    private BigDecimal quauqRate;            //Quauq服务费
    private Integer quauqOtherRateType;  //费率类型，0:百分比，1:金额
    private BigDecimal quauqOtherRate;       //Quauq其他费用服务费
    private Integer agentRateType;        //费率类型，0:百分比，1:金额
    private BigDecimal agentRate;            //渠道服务费
    private Integer agentOtherRateType;  //费率类型，0:百分比，1:金额
    private BigDecimal agentOtherRate;       //渠道其他费用服务费
    
    //----560需求抽成服务费
    private Integer chouchengRateType;//抽成费率类型 0百分比，1 金额
    private BigDecimal chouchengRate;//抽成费率
    

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    public Integer getQuauqRateType() {
        return quauqRateType;
    }

    public void setQuauqRateType(Integer quauqRateType) {
        this.quauqRateType = quauqRateType;
    }

    public BigDecimal getQuauqRate() {
        return quauqRate;
    }

    public void setQuauqRate(BigDecimal quauqRate) {
        this.quauqRate = quauqRate;
    }

    public Integer getQuauqOtherRateType() {
        return quauqOtherRateType;
    }

    public void setQuauqOtherRateType(Integer quauqOtherRateType) {
        this.quauqOtherRateType = quauqOtherRateType;
    }

    public BigDecimal getQuauqOtherRate() {
        return quauqOtherRate;
    }

    public void setQuauqOtherRate(BigDecimal quauqOtherRate) {
        this.quauqOtherRate = quauqOtherRate;
    }

    public Integer getAgentRateType() {
        return agentRateType;
    }

    public void setAgentRateType(Integer agentRateType) {
        this.agentRateType = agentRateType;
    }

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public Integer getAgentOtherRateType() {
        return agentOtherRateType;
    }

    public void setAgentOtherRateType(Integer agentOtherRateType) {
        this.agentOtherRateType = agentOtherRateType;
    }

    public BigDecimal getAgentOtherRate() {
        return agentOtherRate;
    }

    public void setAgentOtherRate(BigDecimal agentOtherRate) {
        this.agentOtherRate = agentOtherRate;
    }
    
    public String getRateDesc(String companyUuid,String activityId,String productType,String agentId){
    	return "QuauqGroupStrategy [ companyUuid=" + companyUuid + ", activityId=" + activityId
				+ ", productType=" + productType + ", agentId=" + agentId
				+ ", quauqRateType=" + this.quauqRateType + ", quauqRate="
				+ this.quauqRate + ", quauqOtherRateType=" + this.quauqOtherRateType
				+ ", quauqOtherRate=" + this.quauqOtherRate + ", agentRateType="
				+ this.agentRateType + ", agentRate=" + this.agentRate
				+ ", agentOtherRateType=" + this.agentOtherRateType
				+ ", agentOtherRate=" + this.agentOtherRate + "]";
    }

	public Integer getChouchengRateType() {
		return chouchengRateType;
	}

	public void setChouchengRateType(Integer chouchengRateType) {
		this.chouchengRateType = chouchengRateType;
	}

	public BigDecimal getChouchengRate() {
		return chouchengRate;
	}

	public void setChouchengRate(BigDecimal chouchengRate) {
		this.chouchengRate = chouchengRate;
	}
}
