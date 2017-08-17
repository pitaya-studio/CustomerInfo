package com.trekiz.admin.modules.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单进度跟踪表
 * @author yakun.bai
 * @Date 2016-8-12
 */
@Entity
@Table(name = "order_progress_tracking")
public class OrderProgressTracking {

	private Long id;
	/** 询单号 */
	private String askNum;
	/** 询单时间 */
	private Date askTime;
	/** 询单用户ID */
	private Long askUserId;
	/** 询单渠道ID */
	private Long askAgentId;
	/** 询单销售ID */
	private Long askSalerId;
	/** 被询问公司ID */
	private Long companyId;
	/** 被询问产品ID */
	private Long activityId;
	/** 被询问团期ID */
	private Long groupId;
	/** 订单ID */
	private Long orderId;
	/** 订单编号 */
	private String orderNum;
	/** 销售操作人：订单创建人 */
	private String orderCreateName;
	/** 计调操作人：产品创建人 */
	private String activityCreateName;
	/** 订单创建时间 */
	private Date orderCreateTime;
	/** 上传确认单销售ID */
	private Long confirmationFileSalerId;
	/** 销售上传确认单时间 */
	private Date confirmationFileSalerTime;
	/** 财务初次收款时间 */
	private Date firstOrderPayTime;
	/** 财务最终确认所有款项达帐 */
	private Date lastOrderPayTime;
	/** 更新时间*/
	private Date updateDate;
	/** 更新人ID */
	private Long updateById;
	/** 订单来源：1 询批发商；2 预报名 */
	private Integer orderType = 1;
	/** 预报名订单ID */
	private Long preOrderId;
	/** 订单状态: 0 正常；1 删除；2 正常取消；3 驳回取消；4 预报名取消 */
	private Integer orderStatus = 0;
	/** 询单记录来源   0:T1,1:微信  	默认 为0 **/
	private Integer t1Flag = 0;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="ask_num")
	public String getAskNum() {
		return askNum;
	}
	public void setAskNum(String askNum) {
		this.askNum = askNum;
	}
	@Column(name="ask_time")
	public Date getAskTime() {
		return askTime;
	}
	public void setAskTime(Date askTime) {
		this.askTime = askTime;
	}
	@Column(name="ask_user_id")
	public Long getAskUserId() {
		return askUserId;
	}
	public void setAskUserId(Long askUserId) {
		this.askUserId = askUserId;
	}
	@Column(name="ask_agent_id")
	public Long getAskAgentId() {
		return askAgentId;
	}
	public void setAskAgentId(Long askAgentId) {
		this.askAgentId = askAgentId;
	}
	@Column(name="company_id")
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	@Column(name="activity_id")
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	@Column(name="group_id")
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	@Column(name="order_id")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	@Column(name="order_num")
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	@Column(name="order_create_name")
	public String getOrderCreateName() {
		return orderCreateName;
	}
	public void setOrderCreateName(String orderCreateName) {
		this.orderCreateName = orderCreateName;
	}
	@Column(name="activity_create_name")
	public String getActivityCreateName() {
		return activityCreateName;
	}
	public void setActivityCreateName(String activityCreateName) {
		this.activityCreateName = activityCreateName;
	}
	@Column(name="order_create_time")
	public Date getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(Date orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	@Column(name="confirmation_file_saler_id")
	public Long getConfirmationFileSalerId() {
		return confirmationFileSalerId;
	}
	public void setConfirmationFileSalerId(Long confirmationFileSalerId) {
		this.confirmationFileSalerId = confirmationFileSalerId;
	}
	@Column(name="confirmation_file_saler_time")
	public Date getConfirmationFileSalerTime() {
		return confirmationFileSalerTime;
	}
	public void setConfirmationFileSalerTime(Date confirmationFileSalerTime) {
		this.confirmationFileSalerTime = confirmationFileSalerTime;
	}
	@Column(name="first_order_pay_time")
	public Date getFirstOrderPayTime() {
		return firstOrderPayTime;
	}
	public void setFirstOrderPayTime(Date firstOrderPayTime) {
		this.firstOrderPayTime = firstOrderPayTime;
	}
	@Column(name="last_order_pay_time")
	public Date getLastOrderPayTime() {
		return lastOrderPayTime;
	}
	public void setLastOrderPayTime(Date lastOrderPayTime) {
		this.lastOrderPayTime = lastOrderPayTime;
	}
	@Column(name="updateDate")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name="updateById")
	public Long getUpdateById() {
		return updateById;
	}
	public void setUpdateById(Long updateById) {
		this.updateById = updateById;
	}
	@Column(name="orderType")
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	@Column(name="preOrderId")
	public Long getPreOrderId() {
		return preOrderId;
	}
	public void setPreOrderId(Long preOrderId) {
		this.preOrderId = preOrderId;
	}
	@Column(name="orderStatus")
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Column(name="ask_saler_id")
	public Long getAskSalerId() {
		return askSalerId;
	}
	public void setAskSalerId(Long askSalerId) {
		this.askSalerId = askSalerId;
	}
	@Column(name="t1_flag")
	public Integer getT1Flag() {
		return t1Flag;
	}
	public void setT1Flag(Integer t1Flag) {
		this.t1Flag = t1Flag;
	}
	
}