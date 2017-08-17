package com.trekiz.admin.modules.airticketorder.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;

public class RefundBean {
	/**
	 * 游客Id
	 */
	private static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客名称
	 */
	private static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 退款款项
	 */
	private static final String KEY_REFUNDNAME = "refundName";
	/**
	 * 申请日期
	 */
	private static final String KEY_APPLYDATE = "applyDate";
	/**
	 * 货币类型Id
	 */
	private static final String KEY_CURRENCYID = "currencyId";
	/**
	 * 货币名称
	 */
	private static final String KEY_CURRENCYNAME = "currencyName";
	/**
	 * 货币标示
	 */
	private static final String KEY_CURRENCYMARK = "currencyMark";
	/**
	 * 应付款
	 */
	private static final String KEY_PAYPRICE = "payPrice";
	/**
	 * 退款
	 */
	private static final String KEY_REFUNDPRICE = "refundPrice";
	/**
	 * 退款说明
	 */
	private static final String KEY_REMARK = "remark";
	/**
	 * 申请状态
	 */
	private static final String KEY_STATUS = "status";
	/**
	 * 申请人
	 */
	private static final String KEY_CREATEBY = "createBy";
	
	private static final String KEY_REVIEWID = "id";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static Map<String, String> statusMap = new HashMap<String, String>();
	
	private static Map<String, String> statusClassMap = new HashMap<String, String>();
	
	static {
		statusMap.put("0", "已驳回");
		statusMap.put("1", "审核中");
		statusMap.put("2", "已通过");
		statusMap.put("3", "已退款");
		
		statusClassMap.put("0", "invoice_back");
		statusClassMap.put("1", "invoice_no");
		statusClassMap.put("2", "invoice_yes");
		statusClassMap.put("3", "invoice_yes");

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
	 * 退款款项
	 */
	private String refundName;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 货币类型Id
	 */
	private String currencyId;
	/**
	 * 货币名称
	 */
	private String currencyName;
	/**
	 * 货币标示
	 */
	private String currencyMark;
	/**
	 * 应付款
	 */
	private String payPrice;
	/**
	 * 退款
	 */
	private String refundPrice;
	/**
	 * 退款说明
	 */
	private String remark;
	/**
	 * 申请状态
	 */
	private int status;
	
	/**
	 * 评审Id
	 */
	private String reviewId;
	
	/**
	 * 评审Id
	 */
	private String createBy;


	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getStatusDesc() {
		return statusMap.get(String.valueOf(status));
	}
	
	public String getStatusClassDesc(){
	    return statusClassMap.get(String.valueOf(status));
	}

	public RefundBean() {

	}

	public RefundBean(Map<String, String> reviewDetailMap) {
		this.travelerId = reviewDetailMap.get(KEY_TRAVELERID);
		this.travelerName = reviewDetailMap.get(KEY_TRAVELERNAME);
		this.refundName = reviewDetailMap.get(KEY_REFUNDNAME);
		this.applyDate = reviewDetailMap.get(KEY_APPLYDATE) == null ? null
				: DateUtils.dateFormat(reviewDetailMap.get(KEY_APPLYDATE));
		this.currencyId = reviewDetailMap.get(KEY_CURRENCYID);
		this.currencyName = reviewDetailMap.get(KEY_CURRENCYNAME);
		this.currencyMark = reviewDetailMap.get(KEY_CURRENCYMARK);
		this.payPrice = reviewDetailMap.get(KEY_PAYPRICE);
		this.refundPrice = reviewDetailMap.get(KEY_REFUNDPRICE);
		this.remark = reviewDetailMap.get(KEY_REMARK);
		this.status = Integer.valueOf(reviewDetailMap.get(KEY_STATUS));
		this.reviewId = reviewDetailMap.get(KEY_REVIEWID);
		this.createBy = reviewDetailMap.get(KEY_CREATEBY);
	}

	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_TRAVELERID, travelerId);
		reviewDetailMap.put(KEY_TRAVELERNAME, travelerName);
		reviewDetailMap.put(KEY_REFUNDNAME, refundName);
		reviewDetailMap.put(KEY_APPLYDATE, sdf.format(getApplyDate()));
		reviewDetailMap.put(KEY_CURRENCYID, currencyId);
		reviewDetailMap.put(KEY_CURRENCYNAME, currencyName);
		reviewDetailMap.put(KEY_CURRENCYMARK, currencyMark);
		reviewDetailMap.put(KEY_PAYPRICE, payPrice);
		reviewDetailMap.put(KEY_REFUNDPRICE, refundPrice);
		reviewDetailMap.put(KEY_REMARK, remark);
		reviewDetailMap.put(KEY_CREATEBY, createBy);
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

	public String getRefundName() {
		return refundName;
	}

	public void setRefundName(String refundName) {
		this.refundName = refundName;
	}

	public Date getApplyDate() {
		if (null == applyDate) {
			applyDate = new Date();
		}
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyMark() {
		return currencyMark;
	}

	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
}
