package com.trekiz.admin.review.changePrice.airticket.bean;

public class AirticketChangePriceInput {
	
	
	private String orderId;// 订单编号
	private String productType;//产品类型
	private String flowType;// 流程类型
	private String reviewId;//申请ID
	private String[] plusysTrue;// 游客差价
	private String[] travelerids;// 游客编号 所有游客的数组
	private String[] gaijiaCurency;// 游客币种
	private String[] travelerremark;// 游客备注
	private String[] travelerId;// 游客编号 checkbox选中的游客数组
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	
	public String[] getTravelerids() {
		return travelerids;
	}
	public void setTravelerids(String[] travelerids) {
		this.travelerids = travelerids;
	}
	
	public String[] getPlusysTrue() {
		return plusysTrue;
	}
	public void setPlusysTrue(String[] plusysTrue) {
		this.plusysTrue = plusysTrue;
	}
	public String[] getGaijiaCurency() {
		return gaijiaCurency;
	}
	public void setGaijiaCurency(String[] gaijiaCurency) {
		this.gaijiaCurency = gaijiaCurency;
	}
	public String[] getTravelerremark() {
		return travelerremark;
	}
	public void setTravelerremark(String[] travelerremark) {
		this.travelerremark = travelerremark;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String[] getTravelerId() {
		return travelerId;
	}
	public void setTravelerId(String[] travelerId) {
		this.travelerId = travelerId;
	}
	
	
	
	
	
}
