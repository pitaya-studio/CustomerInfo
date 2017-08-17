package com.trekiz.admin.review.rebates.common.input;

/**
 * 审核列表查询实体
 * @author 6CR428W6Y2
 */
public class FormInput {

	private String wholeSalerKey ;//团号or产品名称or订单号
	private String productType ;//产品类型
	private String agent ;//渠道商
	private String payStatus ;//出纳确认
	private String createBy ;//审批发起人
	private String status ;//审批状态
	private String rebateAmountFrom ;//返佣金额min
	private String rebateAmountTo ;//返佣金额max
	private String approveDateFrom ;//申请日期min
	private String approveDateTo ;//申请日期max
	private String operator ;//计调
	private String isPrinted ;//打印状态
	private String orderBy;//排序
	private String tabStatus;//0全部；1待本人审批；2本人已审批；3非本人审批
	
	public String getWholeSalerKey() {
		return wholeSalerKey;
	}
	public void setWholeSalerKey(String key) {
		this.wholeSalerKey = key;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRebateAmountFrom() {
		return rebateAmountFrom;
	}
	public void setRebateAmountFrom(String rebateAmountFrom) {
		this.rebateAmountFrom = rebateAmountFrom;
	}
	public String getRebateAmountTo() {
		return rebateAmountTo;
	}
	public void setRebateAmountTo(String rebateAmountTo) {
		this.rebateAmountTo = rebateAmountTo;
	}
	public String getApproveDateFrom() {
		return approveDateFrom;
	}
	public void setApproveDateFrom(String approveDateFrom) {
		this.approveDateFrom = approveDateFrom;
	}
	public String getApproveDateTo() {
		return approveDateTo;
	}
	public void setApproveDateTo(String approveDateTo) {
		this.approveDateTo = approveDateTo;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getIsPrinted() {
		return isPrinted;
	}
	public void setIsPrinted(String isPrinted) {
		this.isPrinted = isPrinted;
	}
	
}
