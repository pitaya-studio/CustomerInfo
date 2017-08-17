package com.trekiz.admin.modules.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 其他收入收款 结算单
 * @author chao.zhang
 *
 */
@Table(name = "finance_settle_other")
@Entity
public class SettleOther {
	private Long id;	 				//主键id

	private String uuid;                //唯一标识符uuid
	private String settleUuid;       	//对应预报单的uuid

	private String name;            //项目名称
	private String agentName;           //客户单位(地接社/渠道商)
	private String totalMoney;               //金额
	private String comment;				//备注
	
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
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
