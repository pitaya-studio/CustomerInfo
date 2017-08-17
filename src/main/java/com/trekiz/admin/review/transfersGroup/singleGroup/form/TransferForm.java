package com.trekiz.admin.review.transfersGroup.singleGroup.form;
/**
 * 审核查询条件
 * @author gao
 * @date 2015-12-1
 */
public class TransferForm {

	// 模糊搜索：团号/产品名称/订单号
	private String selectInfo; 
	// 产品类型
	private String productType;
	// 渠道商ID
	private String agnetID;
	// 申请日期（起）
	private String startDate;
	// 申请日期（止）
	private String endDate;
	// 审批发起人
	private String transferUserID;
	// 游客
	private String travelerID;
	// 审批状态
	private Integer transferStatus;
	// 创建时间（排序）
	private String createDate;
	// 更新时间（排序）
	private String updateDate;
	// 审核分类（1、全部；2、待本人审核；3、本人已审核；4、非本人审核）
	private Integer selfStatus;
	public String getSelectInfo() {
		return selectInfo;
	}
	public void setSelectInfo(String selectInfo) {
		this.selectInfo = selectInfo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getAgnetID() {
		return agnetID;
	}
	public void setAgnetID(String agnetID) {
		this.agnetID = agnetID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTransferUserID() {
		return transferUserID;
	}
	public void setTransferUserID(String transferUserID) {
		this.transferUserID = transferUserID;
	}
	public String getTravelerID() {
		return travelerID;
	}
	public void setTravelerID(String travelerID) {
		this.travelerID = travelerID;
	}
	public Integer getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(Integer transferStatus) {
		this.transferStatus = transferStatus;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getSelfStatus() {
		return selfStatus;
	}
	public void setSelfStatus(Integer selfStatus) {
		this.selfStatus = selfStatus;
	}
	
}
