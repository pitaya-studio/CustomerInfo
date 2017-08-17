package com.trekiz.admin.modules.finance.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
*
* Copyright 2016 QUAUQ Technology Co. Ltd.
*
* 财务模块，预报单境外预计付款表的实体类
* @author yudong.xu
* @date 2016年05月05日
*/
@Table(name = "finance_forecast_out")
@Entity
public class ForeCastOut {
	private Long id;					//主键id

	private String uuid;                //唯一标识符uuid
	private String forecastUuid;        //对应预报单的uuid

	private String agentName;			//客户单位(地接社/渠道商)
	private String currencyName;		//币种
	private String rate;		        //汇率
	private String price;         		//外币，业务取值为：币种符号+单价*数量。
	private String amount;				//金额
	private Integer quantity = 0;		//数量（人数）
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

	@Column(name = "forecast_uuid")
	public String getForecastUuid() {
		return forecastUuid;
	}

	public void setForecastUuid(String forecastUuid) {
		this.forecastUuid = forecastUuid;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
