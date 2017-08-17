package com.trekiz.admin.modules.order.entity;


import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.traveler.entity.Traveler;

public class TransferMoneyAppShow  {
	
	private Review review;
	
	private Traveler traveler;
	
	private ProductOrderCommon newOrder;
	
	private ProductOrderCommon oldOrder;
	
	
	//转款金额
	private String transMoney;
	
	//订单金额
	private String orderTotalMoney;

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	public ProductOrderCommon getNewOrder() {
		return newOrder;
	}

	public void setNewOrder(ProductOrderCommon newOrder) {
		this.newOrder = newOrder;
	}

	public ProductOrderCommon getOldOrder() {
		return oldOrder;
	}

	public void setOldOrder(ProductOrderCommon oldOrder) {
		this.oldOrder = oldOrder;
	}

	public String getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(String transMoney) {
		this.transMoney = transMoney;
	}

	public String getOrderTotalMoney() {
		return orderTotalMoney;
	}

	public void setOrderTotalMoney(String orderTotalMoney) {
		this.orderTotalMoney = orderTotalMoney;
	}
	
	
	
	

}
