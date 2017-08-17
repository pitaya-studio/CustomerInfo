package com.trekiz.admin.modules.visa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;
/**
 * 订单支付关联表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "order_pay_relation")
public class OrderPayRelation extends DataEntity {
	

	
	/** 主键ID */
	private String  id;
	
	/**
	 * 订单id
	 */
	private String orderId;

	/**
	 * 支付表pay的主键id
	 */
	private String payId;
	
	/**
	 * 是否确认标示位(0:未确认;1:已确认),默认为0'
	 */
	private String confirmFlag;
	
	/**
	 * 结算方式（0表示即时结算;1表示按月结算;2表示担保结算;3表示后续费）
	 */
	private Integer paymentStatus;
	
	/**
	 * 确认时间
	 */
	private Date confirmTime;
	
	
	
	/**
	 *确认人
	 */
	private String confirmBy;
	
	
	

	@Id
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name="order_id")
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name="pay_id")
	public String getPayId() {
		return payId;
	}


	public void setPayId(String payId) {
		this.payId = payId;
	}

	@Column(name="confirm_flag")
	public String getConfirmFlag() {
		return confirmFlag;
	}


	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	@Column(name="payment_status")
	public Integer getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(name="confirm_time")
	public Date getConfirmTime() {
		return confirmTime;
	}


	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	@Column(name="confirm_by")
	public String getConfirmBy() {
		return confirmBy;
	}


	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}
	
	
	
	
	
	
}
