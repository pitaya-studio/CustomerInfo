/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.query;

public class IslandOrderQuery  {
	
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.String activityIslandUuid;
	private java.lang.String activityIslandGroupUuid;
	private java.lang.String orderNum;
	private java.lang.Integer orderSalerId;
	private java.lang.String orderPersonPhoneNum;
	private java.util.Date orderTime;
	private java.lang.Integer orderPersonNum;
	private java.lang.String frontMoney;
	private Integer payStatus;
	private java.lang.String payedMoney;
	private Integer payType;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private java.lang.Integer changeGroupId;
	private java.lang.Integer groupChangeType;
	private java.lang.String costMoney;
	private Integer asAcountType;
	private java.lang.String accountedMoney;
	private java.lang.String payDeposit;
	private Integer placeHolderType;
	private java.lang.String singleDiff;
	private java.lang.String cancelDescription;
	private Integer isPayment;
	private java.lang.String payMode;
	private java.lang.Integer remainDays;
	private java.util.Date activationDate;
	private Integer lockStatus;
	private java.lang.String specialDemand;
	private java.lang.String totalMoney;
	private java.lang.String fileIds;
	private java.lang.String originalTotalMoney;
	private java.lang.Boolean isAfterSupplement;
	private java.lang.String originalFrontMoney;
	private java.lang.Integer paymentType;
	private java.lang.Integer forecaseReportRoomNum;
	private java.lang.Integer forecaseReportTicketNum;
	private java.lang.Integer subControlNum;
	private java.lang.Integer subUnControlNum;
	private java.lang.String remark;
	private java.lang.String bookingStatus;
	
	/** 订单状态：默认为团期查询 */
	private java.lang.Boolean isOrder = false;
	/** 订单状态：默认查询全部 */
	private java.lang.Integer orderStatus = 0;
	/** 渠道ID */
	private java.lang.Integer orderCompany;
	/** 渠道名称 */
	private java.lang.String orderCompanyName;
	/** 订单编号或团期编号 */
	private java.lang.String orderNumOrGroupCode;
	/** 订单联系人 */
	private java.lang.String orderPersonName;
	/** 计调 */
	private java.lang.Integer activityCreateBy;
	/** 团期起始开始时间 */
	private java.lang.String groupOpenDateBegin;
	/** 团期起始结束时间 */
	private java.lang.String groupOpenDateEnd;
	/** 订单下单开始时间 */
	private java.lang.String orderTimeBegin;
	/** 订单下单结束时间 */
	private java.lang.String orderTimeEnd;
	/** 产品名称 */
	private java.lang.String activityName;
	/** 排序方式：默认为出团日期降序排列 */
	private java.lang.String orderBy = "groupOpenDate DESC";
	/** 订单查询sql */
	private java.lang.String orderSql;
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getUuid() {
		return uuid;
	}
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}
	public java.lang.String getActivityIslandUuid() {
		return activityIslandUuid;
	}
	public void setActivityIslandUuid(java.lang.String activityIslandUuid) {
		this.activityIslandUuid = activityIslandUuid;
	}
	public java.lang.String getActivityIslandGroupUuid() {
		return activityIslandGroupUuid;
	}
	public void setActivityIslandGroupUuid(java.lang.String activityIslandGroupUuid) {
		this.activityIslandGroupUuid = activityIslandGroupUuid;
	}
	public java.lang.String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(java.lang.String orderNum) {
		this.orderNum = orderNum;
	}
	public java.lang.Integer getOrderSalerId() {
		return orderSalerId;
	}
	public void setOrderSalerId(java.lang.Integer orderSalerId) {
		this.orderSalerId = orderSalerId;
	}
	public java.lang.String getOrderPersonPhoneNum() {
		return orderPersonPhoneNum;
	}
	public void setOrderPersonPhoneNum(java.lang.String orderPersonPhoneNum) {
		this.orderPersonPhoneNum = orderPersonPhoneNum;
	}
	public java.util.Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(java.util.Date orderTime) {
		this.orderTime = orderTime;
	}
	public java.lang.Integer getOrderPersonNum() {
		return orderPersonNum;
	}
	public void setOrderPersonNum(java.lang.Integer orderPersonNum) {
		this.orderPersonNum = orderPersonNum;
	}
	public java.lang.String getFrontMoney() {
		return frontMoney;
	}
	public void setFrontMoney(java.lang.String frontMoney) {
		this.frontMoney = frontMoney;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public java.lang.String getPayedMoney() {
		return payedMoney;
	}
	public void setPayedMoney(java.lang.String payedMoney) {
		this.payedMoney = payedMoney;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public java.lang.Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.Integer createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.Integer updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public java.lang.String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(java.lang.String delFlag) {
		this.delFlag = delFlag;
	}
	public java.lang.Integer getChangeGroupId() {
		return changeGroupId;
	}
	public void setChangeGroupId(java.lang.Integer changeGroupId) {
		this.changeGroupId = changeGroupId;
	}
	public java.lang.Integer getGroupChangeType() {
		return groupChangeType;
	}
	public void setGroupChangeType(java.lang.Integer groupChangeType) {
		this.groupChangeType = groupChangeType;
	}
	public java.lang.String getCostMoney() {
		return costMoney;
	}
	public void setCostMoney(java.lang.String costMoney) {
		this.costMoney = costMoney;
	}
	public Integer getAsAcountType() {
		return asAcountType;
	}
	public void setAsAcountType(Integer asAcountType) {
		this.asAcountType = asAcountType;
	}
	public java.lang.String getAccountedMoney() {
		return accountedMoney;
	}
	public void setAccountedMoney(java.lang.String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}
	public java.lang.String getPayDeposit() {
		return payDeposit;
	}
	public void setPayDeposit(java.lang.String payDeposit) {
		this.payDeposit = payDeposit;
	}
	public Integer getPlaceHolderType() {
		return placeHolderType;
	}
	public void setPlaceHolderType(Integer placeHolderType) {
		this.placeHolderType = placeHolderType;
	}
	public java.lang.String getSingleDiff() {
		return singleDiff;
	}
	public void setSingleDiff(java.lang.String singleDiff) {
		this.singleDiff = singleDiff;
	}
	public java.lang.String getCancelDescription() {
		return cancelDescription;
	}
	public void setCancelDescription(java.lang.String cancelDescription) {
		this.cancelDescription = cancelDescription;
	}
	public Integer getIsPayment() {
		return isPayment;
	}
	public void setIsPayment(Integer isPayment) {
		this.isPayment = isPayment;
	}
	public java.lang.String getPayMode() {
		return payMode;
	}
	public void setPayMode(java.lang.String payMode) {
		this.payMode = payMode;
	}
	public java.lang.Integer getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(java.lang.Integer remainDays) {
		this.remainDays = remainDays;
	}
	public java.util.Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(java.util.Date activationDate) {
		this.activationDate = activationDate;
	}
	public Integer getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}
	public java.lang.String getSpecialDemand() {
		return specialDemand;
	}
	public void setSpecialDemand(java.lang.String specialDemand) {
		this.specialDemand = specialDemand;
	}
	public java.lang.String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(java.lang.String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public java.lang.String getFileIds() {
		return fileIds;
	}
	public void setFileIds(java.lang.String fileIds) {
		this.fileIds = fileIds;
	}
	public java.lang.String getOriginalTotalMoney() {
		return originalTotalMoney;
	}
	public void setOriginalTotalMoney(java.lang.String originalTotalMoney) {
		this.originalTotalMoney = originalTotalMoney;
	}
	public java.lang.Boolean getIsAfterSupplement() {
		return isAfterSupplement;
	}
	public void setIsAfterSupplement(java.lang.Boolean isAfterSupplement) {
		this.isAfterSupplement = isAfterSupplement;
	}
	public java.lang.String getOriginalFrontMoney() {
		return originalFrontMoney;
	}
	public void setOriginalFrontMoney(java.lang.String originalFrontMoney) {
		this.originalFrontMoney = originalFrontMoney;
	}
	public java.lang.Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(java.lang.Integer paymentType) {
		this.paymentType = paymentType;
	}
	public void setForecaseReportRoomNum(java.lang.Integer value) {
		this.forecaseReportRoomNum = value;
	}
	public java.lang.Integer getForecaseReportRoomNum() {
		return this.forecaseReportRoomNum;
	}
	public void setForecaseReportTicketNum(java.lang.Integer value) {
		this.forecaseReportTicketNum = value;
	}
	public java.lang.Integer getForecaseReportTicketNum() {
		return this.forecaseReportTicketNum;
	}
	public java.lang.Integer getSubControlNum() {
		return subControlNum;
	}
	public void setSubControlNum(java.lang.Integer subControlNum) {
		this.subControlNum = subControlNum;
	}
	public java.lang.Integer getSubUnControlNum() {
		return subUnControlNum;
	}
	public void setSubUnControlNum(java.lang.Integer subUnControlNum) {
		this.subUnControlNum = subUnControlNum;
	}
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	public java.lang.String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(java.lang.String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public java.lang.Boolean getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(java.lang.Boolean isOrder) {
		this.isOrder = isOrder;
	}
	public java.lang.Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(java.lang.Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public java.lang.Integer getOrderCompany() {
		return orderCompany;
	}
	public void setOrderCompany(java.lang.Integer orderCompany) {
		this.orderCompany = orderCompany;
	}
	public java.lang.String getOrderCompanyName() {
		return orderCompanyName;
	}
	public void setOrderCompanyName(java.lang.String orderCompanyName) {
		this.orderCompanyName = orderCompanyName;
	}
	public java.lang.String getOrderNumOrGroupCode() {
		return orderNumOrGroupCode;
	}
	public void setOrderNumOrGroupCode(java.lang.String orderNumOrGroupCode) {
		this.orderNumOrGroupCode = orderNumOrGroupCode;
	}
	public java.lang.String getOrderPersonName() {
		return orderPersonName;
	}
	public void setOrderPersonName(java.lang.String orderPersonName) {
		this.orderPersonName = orderPersonName;
	}
	public java.lang.Integer getActivityCreateBy() {
		return activityCreateBy;
	}
	public void setActivityCreateBy(java.lang.Integer activityCreateBy) {
		this.activityCreateBy = activityCreateBy;
	}
	public java.lang.String getGroupOpenDateBegin() {
		return groupOpenDateBegin;
	}
	public void setGroupOpenDateBegin(java.lang.String groupOpenDateBegin) {
		this.groupOpenDateBegin = groupOpenDateBegin;
	}
	public java.lang.String getGroupOpenDateEnd() {
		return groupOpenDateEnd;
	}
	public void setGroupOpenDateEnd(java.lang.String groupOpenDateEnd) {
		this.groupOpenDateEnd = groupOpenDateEnd;
	}
	public java.lang.String getOrderTimeBegin() {
		return orderTimeBegin;
	}
	public void setOrderTimeBegin(java.lang.String orderTimeBegin) {
		this.orderTimeBegin = orderTimeBegin;
	}
	public java.lang.String getOrderTimeEnd() {
		return orderTimeEnd;
	}
	public void setOrderTimeEnd(java.lang.String orderTimeEnd) {
		this.orderTimeEnd = orderTimeEnd;
	}
	public java.lang.String getActivityName() {
		return activityName;
	}
	public void setActivityName(java.lang.String activityName) {
		this.activityName = activityName;
	}
	public java.lang.String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(java.lang.String orderBy) {
		this.orderBy = orderBy;
	}
	public java.lang.String getOrderSql() {
		return orderSql;
	}
	public void setOrderSql(java.lang.String orderSql) {
		this.orderSql = orderSql;
	}
}