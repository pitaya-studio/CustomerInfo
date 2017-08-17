package com.trekiz.admin.modules.finance.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by quauq on 2016/5/10.
 */

@Table(name = "finance_settle_in")
@Entity
public class SettleIn {
    private Long id;					//主键id

    private String uuid;                //唯一标识符uuid
    private String settleUuid;       	//对应的结算单uuid

    private String costName;            //成本名称
    private String agentName;           //客户单位(地接社/渠道商)
    private String price;               //单价
    private Integer quantity;           //数量
    private String amount;              //金额
    private String comment;             //备注

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

    public String getCostName() {
        return costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
