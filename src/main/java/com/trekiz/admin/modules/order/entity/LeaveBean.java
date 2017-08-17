package com.trekiz.admin.modules.order.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;
/**
 * 
 * @author ruyi.chen
 * add date 2014-12-04
 * describe 退团实体bean(包括单团，散拼，自由行，大客户，游学等)
 *
 */
public class LeaveBean {

	/**
	 * 游客Id
	 */
	private static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客名称
	 */
	private static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 申请日期
	 */
	private static final String KEY_APPLYDATE = "applyDate";
	/**
	 * 退团说明
	 */
	private static final String KEY_REMARK = "remark";
	/**
	 * 申请状态
	 */
	private static final String KEY_STATUS = "status";
	
	private static final String KEY_REVIEWID = "id";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static Map<String, String> statusMap = new HashMap<String, String>();

	static {
		statusMap.put("0", "已驳回");
		statusMap.put("1", "审核中");
		statusMap.put("2", "审核成功");
		statusMap.put("3", "已退款");

	}
	/**
	 * 游客Id
	 */
	private String travelerId;
	/**
	 * 游客名称
	 */
	private String travelerName;
	
	public String getStatusDesc() {
		return statusMap.get(String.valueOf(status));
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
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 退团说明
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
	
	public LeaveBean(){
		
	}
	public LeaveBean(Map<String, String> reviewDetailMap){
		this.travelerId = reviewDetailMap.get(KEY_TRAVELERID);
		this.travelerName = reviewDetailMap.get(KEY_TRAVELERNAME);
		this.applyDate = reviewDetailMap.get(KEY_APPLYDATE) == null ? null
				: DateUtils.dateFormat(reviewDetailMap.get(KEY_APPLYDATE));
		this.remark = reviewDetailMap.get(KEY_REMARK);
		this.status = Integer.valueOf(reviewDetailMap.get(KEY_STATUS));
		this.reviewId = reviewDetailMap.get(KEY_REVIEWID);
	}
	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_TRAVELERID, travelerId);
		reviewDetailMap.put(KEY_TRAVELERNAME, travelerName);
		reviewDetailMap.put(KEY_APPLYDATE, sdf.format(getApplyDate()));
		reviewDetailMap.put(KEY_REMARK, remark);
		return reviewDetailMap;
	}
}
