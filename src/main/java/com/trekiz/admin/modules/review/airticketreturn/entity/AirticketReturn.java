package com.trekiz.admin.modules.review.airticketreturn.entity;

import java.util.Date;

/**
 * 机票退票审核虚拟实体类
 * 
 * @author chy
 */
public class AirticketReturn {

//	private static final long serialVersionUID = 1L;

	private Long id;
	/*产品id*/
	private Long cpid;
	/*审核的最高层级*/
	private Integer topLevel;
	/*审核的当前层级*/
	private Integer nowLevel;
	/*产品类型*/
	private Integer productType;
	/*订单id*/
	private Long orderId;
	/*审核流程类型*/
	private Integer flowType;
	/*游客id*/
	private Long travelerId;
	/*审核原因*/
	private String createReason;
	/*驳回原因*/
	private String denyReason;
	/*创建人*/
	private Long createBy;
	/*更新人*/
	private Long updateBy;
	/*创建日期*/
	private Date createDate;
	/*更新日期*/
	private Date updateDate;
	/*更新人姓名*/
	private String updateByName;
	/*审核状态*/
	private Integer status;
	/*有效标志*/
	private Integer active;
	/* 下单日期 */
	private Date orderCreateDate;
	/* 应付金额 */
	private String payPrice;
	/* 当前审核人 */
	private String curReviewer;
	/* 审核状态描述 */
	private String statusDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpid() {
		return cpid;
	}

	public void setCpid(Long cpid) {
		this.cpid = cpid;
	}

	public Integer getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(Integer topLevel) {
		this.topLevel = topLevel;
	}

	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getFlowType() {
		return flowType;
	}

	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}

	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}

	public String getCreateReason() {
		return createReason;
	}

	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}

	public String getDenyReason() {
		return denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Date getOrderCreateDate() {
		return orderCreateDate;
	}

	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getCurReviewer() {
		return curReviewer;
	}

	public void setCurReviewer(String curReviewer) {
		this.curReviewer = curReviewer;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
