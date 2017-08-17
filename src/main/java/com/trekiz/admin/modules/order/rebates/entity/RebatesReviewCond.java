package com.trekiz.admin.modules.order.rebates.entity;

/**
 * add by ruyi.chen
 * add date 2014-12-09
 * describe 订单退团审核列表查询实体
 *
 */
public class RebatesReviewCond{

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
	/**计调*/
	private String meter;
	/**当前用户审核层级*/
	private int userLevel;
	/**当前查询审核状态*/
	private int reviewStatus=0;
	/**当前用户审核角色唯一标识*/
	private long rid=0;
	
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
	public String getMeter() {
		return meter;
	}
	public void setMeter(String meter) {
		this.meter = meter;
	}
	
}
