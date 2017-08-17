package com.trekiz.admin.modules.reviewflow.entity;

import java.util.Date;
/**
 * 团队退款审核列表页面  查询 实体类
 * @author ruyi.chen
 * create date 2014-11-07
 *
 */
public class GroupRefund {

	private String groupId;//团号
	private Date orderStartTime;//下单时间
	private Date orderEndTime;
	private String channel;//渠道
	private String saleMan;//销售
	private String meter;//计调
	private Integer groupType;//团队类型
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Date getOrderStartTime() {
		return orderStartTime;
	}
	public void setOrderStartTime(Date orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public Date getOrderEndTime() {
		return orderEndTime;
	}
	public void setOrderEndTime(Date orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSaleMan() {
		return saleMan;
	}
	public void setSaleMan(String saleMan) {
		this.saleMan = saleMan;
	}
	public String getMeter() {
		return meter;
	}
	public void setMeter(String meter) {
		this.meter = meter;
	}
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	
}
