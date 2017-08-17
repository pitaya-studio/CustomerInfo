package com.trekiz.admin.modules.cruiseship.jsonbean;

import java.util.List;

import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;

/**
 * 库存订单库存明细查询接口查询jsonbean
 * ClassName: CruiseshipOrderQueryJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-2-2
 */
public class CruiseshipOrderQueryJsonBean {
	private String cruiseshipStockUuid;
	private List<CruiseshipStockOrder> activityList;
	
	public String getCruiseshipStockUuid() {
		return cruiseshipStockUuid;
	}
	public void setCruiseshipStockUuid(String cruiseshipStockUuid) {
		this.cruiseshipStockUuid = cruiseshipStockUuid;
	}
	public List<CruiseshipStockOrder> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<CruiseshipStockOrder> activityList) {
		this.activityList = activityList;
	}

}
