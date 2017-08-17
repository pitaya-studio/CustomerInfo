package com.trekiz.admin.modules.order.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;

public class TransFerGroup {

	/**
	 * 游客Id
	 */
	public static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客转团发起订单orderId
	 */
	private static final String KEY_OLD_ORDERID = "oldOrderId";
	/**
	 * 游客转入新团団期唯一主键
	 */
	public static final String KEY_NEW_GROUPCODE = "newGroupCode";
	/**
	 * 游客转入新团生成新订单orderId
	 */
	public static final String KEY_NEW_ORDERID = "newOrderId";
	/**
	 * 游客名称
	 */
	private static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 老订单创建日期
	 */
	private static final String KEY_CREATEDATE = "createDate";
	/**
	 * 申请日期
	 */
	private static final String KEY_APPLYDATE = "applyDate";
	/**
	 * 转团说明
	 */
	private static final String KEY_REMARK = "remark";
	/**
	 * 申请状态
	 */
	private static final String KEY_STATUS = "status";
	
	private static final String KEY_REVIEWID = "id";
	/**
	 * 应付金额
	 */
	private static final String KEY_MONEY = "money";
	/**
	 * 扣减金额
	 */
	private static final String KEY_SUBTRACT_MONEY= "subtractMoney";
	/**
	 * 订单支付方式
	 */
	private static final String PAY_TYPE = "payType";
	/**
	 * 保留天数
	 */
	private static final String REMAIN_DAYS = "remainDays";

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
	/**
	 * 老订单创建日期
	 */
	private Date createDate;
	/**
	 * 申请日期
	 */
	private Date applyDate;
	/**
	 * 转团说明
	 */
	private String remark;
	/**
	 * 申请状态
	 */
	private int status;
	private String statusCn;
	/**
	 * 应付金额
	 */
	private String money;
	/**
	 * 转团后应付
	 */
	private String subtractMoney;
	
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
	 * 游客转入新团団期团号
	 */
	private  String newGroupCode;
	/**
	 * 订单支付方式
	 */
	private String payType;
	/**
	 * 占位保留天数
	 */
	private String remainDays;
	
	public String getNewGroupCode() {
		return newGroupCode;
	}

	public void setNewGroupCode(String newGroupCode) {
		this.newGroupCode = newGroupCode;
	}

	public TransFerGroup(){}
	
	public TransFerGroup(Map<String, String> reviewDetailMap){
		this.travelerId = reviewDetailMap.get(KEY_TRAVELERID);
		this.travelerName = reviewDetailMap.get(KEY_TRAVELERNAME);
		this.createDate = reviewDetailMap.get(KEY_CREATEDATE)==null?null
				:DateUtils.dateFormat(reviewDetailMap.get(KEY_CREATEDATE));
		this.applyDate = reviewDetailMap.get(KEY_APPLYDATE) == null ? null
				: DateUtils.dateFormat(reviewDetailMap.get(KEY_APPLYDATE));
		this.remark = reviewDetailMap.get(KEY_REMARK);
		this.status = Integer.valueOf(reviewDetailMap.get(KEY_STATUS));
		this.reviewId = reviewDetailMap.get(KEY_REVIEWID);
		this.oldOrderId=reviewDetailMap.get(KEY_OLD_ORDERID);
		this.newOrderId=reviewDetailMap.get(KEY_NEW_ORDERID);
		this.newGroupCode=reviewDetailMap.get(KEY_NEW_GROUPCODE);
	}
	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_TRAVELERID, travelerId);
		reviewDetailMap.put(KEY_TRAVELERNAME, travelerName);
		reviewDetailMap.put(KEY_APPLYDATE, sdf.format(getApplyDate()));
		reviewDetailMap.put(KEY_REMARK, remark);
		reviewDetailMap.put(KEY_OLD_ORDERID, oldOrderId);
		reviewDetailMap.put(KEY_NEW_ORDERID, newOrderId);
		reviewDetailMap.put(KEY_CREATEDATE , sdf.format(getCreateDate()));
		reviewDetailMap.put(KEY_NEW_GROUPCODE , newGroupCode);
		reviewDetailMap.put(KEY_MONEY, money);
		reviewDetailMap.put(KEY_SUBTRACT_MONEY, subtractMoney);
		reviewDetailMap.put(PAY_TYPE, payType);
		reviewDetailMap.put(REMAIN_DAYS, remainDays);
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}

	public String getSubtractMoney() {
		return subtractMoney;
	}

	public void setSubtractMoney(String subtractMoney) {
		this.subtractMoney = subtractMoney;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}
}
