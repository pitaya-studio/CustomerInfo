package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;
/**
 * 订单游客关联表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "order_traveler_pay_relation")
public class OrderTravelerPayRelation extends DataEntity {
	

	/** 主键ID */
	private String  id;
	
	/**
	 * 订单支付关联表主键id
	 */
	private String orderPayRelationId;
	
	
	/**
	 * 游客id
	 */
	private String travelerId;
	
	
	/**
	 * 是否确认标示位(0:未确认;1:已确认),默认为0'
	 */
	private String confirmFlag;

	@Id
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name="order_pay_relation_id")
	public String getOrderPayRelationId() {
		return orderPayRelationId;
	}


	public void setOrderPayRelationId(String orderPayRelationId) {
		this.orderPayRelationId = orderPayRelationId;
	}

	@Column(name="traveler_id")
	public String getTravelerId() {
		return travelerId;
	}


	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	@Column(name="confirm_flag")
	public String getConfirmFlag() {
		return confirmFlag;
	}


	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	
	
	
	
	
	
	
	

	
}
