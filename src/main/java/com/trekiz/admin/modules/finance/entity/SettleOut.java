package com.trekiz.admin.modules.finance.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by quauq on 2016/5/10.
 */

@Table(name = "finance_settle_out")
@Entity
public class SettleOut {
    private Long id;					//主键id

    private String uuid;                //唯一标识符uuid
    private String settleUuid;         	//对应的结算单uuid

    private String agentName;			//客户单位(地接社/渠道商)
    private String currencyName;		//币种
    private String rate;		        //汇率
    private String price;         		//外币，业务取值为：币种符号+单价*数量。
    private String amount;				//金额

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "settle_uuid")
    public String getSettleUuid() {
        return settleUuid;
    }

    public void setSettleUuid(String settleUuid) {
        this.settleUuid = settleUuid;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
