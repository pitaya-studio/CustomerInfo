package com.trekiz.admin.modules.order.entity;

/**
 * add by ruyi.chen
 * add date 2014-12-09
 * describe 订单退团审核列表查询实体
 *
 */
public class OrderExitGroupReviewVO{

	/**订单编号*/
	private String orderNo;
	/**团号*/
	private String groupCode;
	/**团队类型*/
	private String orderType;
	/**下单日期起始*/
	private String orderTimeBegin;
	/**下单日期截止*/
	private String orderTimeEnd;
	/**渠道*/
	private String channel;
	/**销售*/
	private String saler;
//	/**下单人*/
//	private String createBy;
	/**计调*/
	private String meter;
	/**当前用户审核层级*/
	private int userLevel;
	/**当前查询审核状态*/
	private int reviewStatus=1;
	private String paymentType;
	
	/**
	 * 新当前查询审核状态
	 * add sy 二〇一五年十二月九日 16:04:44
	 */
	
	private String newReviewStatus;
	
	/**
	 * select审核状态
	 */
	private String selectReviewStatus;
	/**当前用户审核角色唯一标识*/
	private long rid=0;
	/**订单号*/
	private String orderNum;
	/**报批日期起始*/
	private String createTimeBegin;
	/**报批日期截止*/
	private String createTimeEnd;
	/**返佣差额起*/
	private String rebatesDiffBegin;
	/**返佣差额至*/
	private String rebatesDiffEnd;
	/**打印状态*/
	private String printFlag;
	/**游客姓名*/
	private String travelerName;
	/**下单人 */
	private String picker;
	/**
	 * 支付状态
	 */
	private String payStatus;
	/**
	 * 产品类型
	 */
	private String productType;
	/**
	 * 金额起始
	 */
	private String lendMoneyFrom;
	/**
	 * 金额截止
	 */
	private String lendMoneyTo;
	
	/**
	 * 申请日期开始
	 * @return
	 */
	
	private String applyDateFrom;
	
	
	/**
	 * 申请日期结束
	 * @return
	 */
	
	private String applyDateTo;
	
	/**
	 * 审批发起人
	 * @return
	 */
	
	private String applyPerson;
	
	/**
	 * 出纳确认
	 * @return
	 */
	private String cashConfirm;
	
	/**
	 * 团号  订单编号 产品名称
	 * @return
	 */
	private String searchGOP;
	private String orderBy;//排序字段
	private String ascOrDesc;//升序或降序
	
	
	
	
	public String getNewReviewStatus() {
		return newReviewStatus;
	}
	public void setNewReviewStatus(String newReviewStatus) {
		this.newReviewStatus = newReviewStatus;
	}
	public String getSearchGOP() {
		return searchGOP;
	}
	public void setSearchGOP(String searchGOP) {
		this.searchGOP = searchGOP;
	}
	public String getCashConfirm() {
		return cashConfirm;
	}
	public void setCashConfirm(String cashConfirm) {
		this.cashConfirm = cashConfirm;
	}
	public String getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}
	public String getApplyDateFrom() {
		return applyDateFrom;
	}
	public void setApplyDateFrom(String applyDateFrom) {
		this.applyDateFrom = applyDateFrom;
	}
	public String getApplyDateTo() {
		return applyDateTo;
	}
	public void setApplyDateTo(String applyDateTo) {
		this.applyDateTo = applyDateTo;
	}
	public String getSelectReviewStatus() {
		return selectReviewStatus;
	}
	public void setSelectReviewStatus(String selectReviewStatus) {
		this.selectReviewStatus = selectReviewStatus;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getLendMoneyFrom() {
		return lendMoneyFrom;
	}
	public void setLendMoneyFrom(String lendMoneyFrom) {
		this.lendMoneyFrom = lendMoneyFrom;
	}
	public String getLendMoneyTo() {
		return lendMoneyTo;
	}
	public void setLendMoneyTo(String lendMoneyTo) {
		this.lendMoneyTo = lendMoneyTo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getRebatesDiffBegin() {
		return rebatesDiffBegin;
	}
	public void setRebatesDiffBegin(String rebatesDiffBegin) {
		this.rebatesDiffBegin = rebatesDiffBegin;
	}
	public String getRebatesDiffEnd() {
		return rebatesDiffEnd;
	}
	public void setRebatesDiffEnd(String rebatesDiffEnd) {
		this.rebatesDiffEnd = rebatesDiffEnd;
	}
	public String getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public long getRid() {
		return rid;
	}
	public void setRid(long rid) {
		this.rid = rid;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public int getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderTimeBegin() {
		return orderTimeBegin;
	}
	public void setOrderTimeBegin(String orderTimeBegin) {
		this.orderTimeBegin = orderTimeBegin;
	}
	public String getOrderTimeEnd() {
		return orderTimeEnd;
	}
	public void setOrderTimeEnd(String orderTimeEnd) {
		this.orderTimeEnd = orderTimeEnd;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}	
//	public String getCreateBy() {
//		return createBy;
//	}
//	public void setCreateBy(String createBy) {
//		this.createBy = createBy;
//	}
	public String getMeter() {
		return meter;
	}
	public void setMeter(String meter) {
		this.meter = meter;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCreateTimeBegin() {
		return createTimeBegin;
	}
	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public String getTravelerName() {
		return travelerName;
	}
	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}
	public String getPicker() {
		return picker;
	}
	public void setPicker(String picker) {
		this.picker = picker;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	
}
