package com.trekiz.admin.modules.finance.entity;

import javax.persistence.*;

/**
*
* 预报单结算单的订单收款item的属性。用于保存数据。
*
* @author yudong.xu
* @date 2016年05月05日
*/
@Table(name = "finance_forecast_order")
@Entity
public class ForeCastOrder {
	private Long id;						//主键id

	private String uuid;					//唯一标识符uuid
	private String forecastUuid;			//对应预报单的uuid

	private String saler;					//销售
	private String agentName;	    		//客户单位(地接社/渠道商)
	private Integer orderPersonNum = 0;		//订单人数
	private String totalMoney;				//预计收款
	private String backMoney;				//预计退款
	private String accountedMoney;			//实际收款
	private String notAccountedMoney;		//未收款项

	// 0546 骡子假期 modify by 2016.11.11
	private String price;					//单价（骡子假期）
	private Integer personNum = 0;			//人数（骡子假期）
	private String totalPrice;				//金额（骡子假期）= 单价x人数
	private String remark;					// 备注
	
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
