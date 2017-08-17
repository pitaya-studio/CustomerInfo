package com.trekiz.admin.modules.island.query;
/**
 * 转团申请表单
 * @author gao
 *  2015年6月15日
 */
public class ActivityIslandGroupChangeQuery {
	// 游客Uuid
	private String[] travelUuid;
	// 原订单Uuid
	private String orderUuid;
	// 原产品Uuid
	private String activityUuid;
	// 新团期code
	private String groupCode;
	
	public String[] getTravelUuid() {
		return travelUuid;
	}
	public void setTravelUuid(String[] travelUuid) {
		this.travelUuid = travelUuid;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getActivityUuid() {
		return activityUuid;
	}
	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
}
