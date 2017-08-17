package com.trekiz.admin.modules.airticket.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;

@Entity
@Table(name = "activityreserveorder")
@DynamicInsert
public class AirTicketReserveOrder implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; // 编号	
	private String orderNum;	
//	private Long srcActivityId;	
	private Long activityGroupId;	
	private Long agentId;
	private Long saleId;
	private Integer payType;//支付方式
	private String reservation;//预订人
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private Integer orderStatus;
	private BigDecimal orderMoney;
	private BigDecimal payMoney;
	private Integer Confirm;	
	private Integer reserveType;
	private Integer moneyType;
	private Integer payReservePosition;
	private String remark;
    private ActivityAirTicket activityAirTicket;
    
    
    
    
    
    @OneToOne
    @JoinColumn(name="srcActivityId")
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
	public ActivityAirTicket getActivityAirTicket() {
		return activityAirTicket;
	}

	public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
		this.activityAirTicket = activityAirTicket;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	} 
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "orderNum", unique = false, nullable = true)
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	 @Transient
		public String getAgentName() {
			return AgentInfoUtils.getAgentName(this.agentId);
		}
	
	@Column(name = "activityGroupId", unique = false, nullable = false)

	public Long getActivityGroupId() {
		return activityGroupId;
	}
	public void setActivityGroupId(Long activityGroupId) {
		this.activityGroupId = activityGroupId;
	}
	
	@Column(name = "agentId", unique = false, nullable = true)
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	@Column(name = "saleId", unique = false, nullable = true)
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	
	@Column(name = "createDate", unique = false, nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "startDate", unique = false, nullable = true)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "endDate", unique = false, nullable = true)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "orderStatus", unique = false, nullable = true)
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Column(name = "orderMoney", unique = false, nullable = true)
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	
	@Column(name = "payMoney", unique = false, nullable = true)
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	
	@Column(name = "confirm", unique = false, nullable = true)
	public Integer getConfirm() {
		return Confirm;
	}
	public void setConfirm(Integer confirm) {
		Confirm = confirm;
	}
	
	@Column(name = "reserveType", unique = false, nullable = true)
	public Integer getReserveType() {
		return reserveType;
	}
	public void setReserveType(Integer reserveType) {
		this.reserveType = reserveType;
	}
	@Column(name = "moneyType", unique = false, nullable = true)
	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}
	@Column(name = "payReservePosition", unique = false, nullable = true)
	public Integer getPayReservePosition() {
		return payReservePosition;
	}

	public void setPayReservePosition(Integer payReservePosition) {
		this.payReservePosition = payReservePosition;
	}

	@Transient
	public String getPayTypeLabel() {
		if(payType==null)return "";
		return DictUtils.getDictLabel(String.valueOf(payType), "offlineorder_pay_type", "");
	}
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getReservation() {
		return reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
}
