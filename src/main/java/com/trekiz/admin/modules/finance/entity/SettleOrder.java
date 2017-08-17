package com.trekiz.admin.modules.finance.entity;

import javax.persistence.*;

/**
 * Created by quauq on 2016/5/10.
 */

@Table(name = "finance_settle_order")
@Entity
public class SettleOrder {
    private Long id;					//主键id

    private String uuid;                //唯一标识符uuid
    private String settleUuid;			//对应的结算单uuid

    private String saler;					//销售
    private String agentName;	    		//客户单位
    private Integer orderPersonNum;         //订单人数
    private String totalMoney;				//收款
    private String backMoney;				//退款
    private String accountedMoney;			//实际收款
    private String notAccountedMoney;		//未收团款
    
    private Integer personCount;//退团人数（骡子假期）
    private Integer zGPersonCount;//转团人数（骡子假期）
    
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

    public String getSaler() {
        return saler;
    }

    public void setSaler(String saler) {
        this.saler = saler;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getOrderPersonNum() {
        return orderPersonNum;
    }

    public void setOrderPersonNum(Integer orderPersonNum) {
        this.orderPersonNum = orderPersonNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(String backMoney) {
        this.backMoney = backMoney;
    }

    public String getAccountedMoney() {
        return accountedMoney;
    }

    public void setAccountedMoney(String accountedMoney) {
        this.accountedMoney = accountedMoney;
    }

    public String getNotAccountedMoney() {
        return notAccountedMoney;
    }

    public void setNotAccountedMoney(String notAccountedMoney) {
        this.notAccountedMoney = notAccountedMoney;
    }

	public Integer getPersonCount() {
		return personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public Integer getzGPersonCount() {
		return zGPersonCount;
	}

	public void setzGPersonCount(Integer zGPersonCount) {
		this.zGPersonCount = zGPersonCount;
	}

}
