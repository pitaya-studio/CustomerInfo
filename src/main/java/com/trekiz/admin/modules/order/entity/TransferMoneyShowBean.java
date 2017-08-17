package com.trekiz.admin.modules.order.entity;

import com.trekiz.admin.modules.activity.entity.ActivityGroup;

/**
 * 用于转团转款 展示
 * @author yue.wang
 *
 */
public class TransferMoneyShowBean   { 
	
//	public static String KEY_TRAVELID = "TRAVELID";
//	public static String KEY_CHANGEGROUPID = "CHANGEGROUPID";
//	public static String KEY_NEWORDERID = "NEWORDERID";
	
	private ProductOrderCommon productOrderCommon;
	
    private ActivityGroup activitygroup;
    
    /*
     * 订单总金额 多币种
     */
    private String orderTotalMoney;
    
    /*
     * 订单达账金额 多币种
     */
    private String orderAccountedMoney;
	
	public ProductOrderCommon getProductOrderCommon() {
		return productOrderCommon;
	}

	public void setProductOrderCommon(ProductOrderCommon productOrderCommon) {
		this.productOrderCommon = productOrderCommon;
	}

	public ActivityGroup getActivitygroup() {
		return activitygroup;
	}

	public void setActivitygroup(ActivityGroup activitygroup) {
		this.activitygroup = activitygroup;
	}

	public String getOrderTotalMoney() {
		return orderTotalMoney;
	}

	public void setOrderTotalMoney(String orderTotalMoney) {
		this.orderTotalMoney = orderTotalMoney;
	}

	public String getOrderAccountedMoney() {
		return orderAccountedMoney;
	}

	public void setOrderAccountedMoney(String orderAccountedMoney) {
		this.orderAccountedMoney = orderAccountedMoney;
	}

	
	
	
	
	
}


