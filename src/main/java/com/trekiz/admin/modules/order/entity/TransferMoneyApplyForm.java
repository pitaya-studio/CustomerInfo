package com.trekiz.admin.modules.order.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransferMoneyApplyForm {
	
	
	//前台向后台传值
    private String orderId;
	private String[] transferMoney;
	private Integer[] travelorId;
	private String[] travelorName;
	private Long[] inOrderId;
	private String transferGroupReviewId;
	private String[] remarks;
	private Long[] newTravellerIds;
	

	/**
	 * 游客Id
	 */
	public static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客转团发起订单orderId
	 */
	public static final String KEY_OLD_ORDERID = "oldOrderId";
	/**
	 * 游客转入新团団期唯一主键
	 */
	private static final String KEY_NEW_GROUPID = "newGroupId";
	/**
	 * 游客转入新团生成新订单orderId
	 */
	public static final String KEY_NEW_ORDERID = "newOrderId";
	/**
	 * 转团转款金额对应的uuid
	 */
	public static final String KEY_REFUND_MONEY = "refundMoney";
	/**
	 * 游客名称
	 */
	public static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 申请日期
	 */
	private static final String KEY_APPLYDATE = "applyDate";
	/**
	 * 转团转款说明
	 */
	private static final String KEY_REMARK = "remark";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static Map<String, String> statusMap = new HashMap<String, String>();

	static {
		statusMap.put("0", "已驳回");
		statusMap.put("1", "审核中");
		statusMap.put("2", "审核成功");
		statusMap.put("3", "已退款");
		statusMap.put("4", "取消申请");

	}
	
	
	/**
	 * 游客Id
	 */
	private String travelerId;
	/**
	 * 游客名称
	 */
	private String travelerName;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 转款说明
	 */
	private String remark;
	/**
	 * 申请状态
	 */
	private int status;
	
	/**
	 * 审核主键
	 */
	private String reviewId;
	/**
	 * 游客转团发起订单orderId
	 */
	private String oldOrderId;
	
	/**
	 * 游客转入新团生成新订单orderId
	 */
	private String newOrderId;
	/**
	 * 游客转入新团団期唯一主键
	 */
	private  String newGroupId;
	/**
	 * 转团转款金额对应的uuid
	 */
	private String refundMoney;
	
	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getNewGroupId() {
		return newGroupId;
	}

	public void setNewGroupId(String newGroupId) {
		this.newGroupId = newGroupId;
	}

	public TransferMoneyApplyForm(){}
	
	
	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_TRAVELERID, travelerId);
		reviewDetailMap.put(KEY_TRAVELERNAME, travelerName);
		reviewDetailMap.put(KEY_APPLYDATE, sdf.format(getApplyDate()));
		reviewDetailMap.put(KEY_REMARK, remark);
		reviewDetailMap.put(KEY_OLD_ORDERID, oldOrderId);
		reviewDetailMap.put(KEY_NEW_ORDERID, newOrderId);
		reviewDetailMap.put(KEY_NEW_GROUPID, newGroupId);
		reviewDetailMap.put(KEY_REFUND_MONEY, refundMoney);
		return reviewDetailMap;
	}
	public String getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	public String getTravelerName() {
		return travelerName;
	}

	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public String getNewOrderId() {
		return newOrderId;
	}

	public void setNewOrderId(String newOrderId) {
		this.newOrderId = newOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String[] getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(String[] transferMoney) {
		this.transferMoney = transferMoney;
	}

	public Integer[] getTravelorId() {
		return travelorId;
	}

	public void setTravelorId(Integer[] travelorId) {
		this.travelorId = travelorId;
	}

	public Long[] getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(Long[] inOrderId) {
		this.inOrderId = inOrderId;
	}

	public String[] getTravelorName() {
		return travelorName;
	}

	public void setTravelorName(String[] travelorName) {
		this.travelorName = travelorName;
	}

	public String getTransferGroupReviewId() {
		return transferGroupReviewId;
	}

	public void setTransferGroupReviewId(String transferGroupReviewId) {
		this.transferGroupReviewId = transferGroupReviewId;
	}

	public String[] getRemarks() {
		return remarks;
	}

	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}

	public Long[] getNewTravellerIds() {
		return newTravellerIds;
	}

	public void setNewTravellerIds(Long[] newTravellerIds) {
		this.newTravellerIds = newTravellerIds;
	}
	
	
	
}
