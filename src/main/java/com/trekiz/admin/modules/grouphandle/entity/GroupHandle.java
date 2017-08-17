package com.trekiz.admin.modules.grouphandle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

/**  
 * @Title: GroupControll.java
 * @Package com.trekiz.admin.modules.groupcontroll.entity
 * @Description: 团控实体类
 * @author xinwei.wang  
 * @date 2016-2016年1月25日 下午3:41:36
 * @version V1.0  
 */
@Entity
@Table(name = "group_control")
public class GroupHandle extends DataEntity {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer orderId; //订单id
	private String orderNum; //订单Num
	
	private Integer salerId; //销售
	private String salerName; //销售姓名
	
	private Integer opId; //计调
	private String  opName; //计调姓名
	
	private Integer agentinfoId; //渠道
	private String agentinfoName; //渠道姓名
	
	private Integer activityProductId; //产品id
	private String activityProductName; //产品名称
	private Integer activityProductKind; //团队（产品）类型
	
	private Integer activityGroupId; //团期id
	private String activityGroupCode; //团号
	
	/** 主键ID */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="order_num")
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Column(name="saler_id")
	public Integer getSalerId() {
		return salerId;
	}
	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}
	
	@Column(name="saler_name")
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	
	@Column(name="op_id")
	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	
	@Column(name="op_name")
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	
	@Column(name="agentinfo_id")
	public Integer getAgentinfoId() {
		return agentinfoId;
	}
	public void setAgentinfoId(Integer agentinfoId) {
		this.agentinfoId = agentinfoId;
	}
	
	@Column(name="agentinfo_name")
	public String getAgentinfoName() {
		return agentinfoName;
	}
	public void setAgentinfoName(String agentinfoName) {
		this.agentinfoName = agentinfoName;
	}
	
	@Column(name="activity_product_id")
	public Integer getActivityProductId() {
		return activityProductId;
	}
	public void setActivityProductId(Integer activityProductId) {
		this.activityProductId = activityProductId;
	}
	
	@Column(name="activity_product_name")
	public String getActivityProductName() {
		return activityProductName;
	}
	public void setActivityProductName(String activityProductName) {
		this.activityProductName = activityProductName;
	}
	
	@Column(name="activity_product_kind")
	public Integer getActivityProductKind() {
		return activityProductKind;
	}
	public void setActivityProductKind(Integer activityProductKind) {
		this.activityProductKind = activityProductKind;
	}
	
	@Column(name="activity_group_id")
	public Integer getActivityGroupId() {
		return activityGroupId;
	}
	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}
	
	@Column(name="activity_group_code")
	public String getActivityGroupCode() {
		return activityGroupCode;
	}
	public void setActivityGroupCode(String activityGroupCode) {
		this.activityGroupCode = activityGroupCode;
	}
	
	
	
}
