package com.trekiz.admin.modules.order.entity;

import java.util.List;

import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.traveler.entity.Traveler;

public class TravelerShowBean  {
	
	private Traveler traveler;
	
//	public TravelerShowBean(Traveler t){
//		try {
//			ConvertUtils.register(new DateConverter(null), java.util.Date.class); 
//			BeanUtils.copyProperties(this, t);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	private Traveler newTraveler;  //新订单中游客的新entity
	private String groupNo;
	
	private Long inOrderId;
	
	private String remark;
	
	//存在的币种
	private List<Currency>  currencyList;
	//转出订单游客结算价
	private String oldPayPriceMoney;	
	//转入订单游客结算价
	private String newPayPriceMoney;	
	//转入订单 金额
	private String orderMoney;
	//转款金额
	private String transferMoney;
	//旧审核 游客结算价
	private String payPriceMoney;
	//转入订单
	private ProductOrderCommon newOrder;
	
	
	public String getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(String transferMoney) {
		this.transferMoney = transferMoney;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	 
	public Long getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(Long inOrderId) {
		this.inOrderId = inOrderId;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}	

	public String getOldPayPriceMoney() {
		return oldPayPriceMoney;
	}

	public void setOldPayPriceMoney(String oldPayPriceMoney) {
		this.oldPayPriceMoney = oldPayPriceMoney;
	}

	public String getNewPayPriceMoney() {
		return newPayPriceMoney;
	}

	public void setNewPayPriceMoney(String newPayPriceMoney) {
		this.newPayPriceMoney = newPayPriceMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Traveler getNewTraveler() {
		return newTraveler;
	}

	public void setNewTraveler(Traveler newTraveler) {
		this.newTraveler = newTraveler;
	}

	public String getPayPriceMoney() {
		return payPriceMoney;
	}

	public void setPayPriceMoney(String payPriceMoney) {
		this.payPriceMoney = payPriceMoney;
	}

	public ProductOrderCommon getNewOrder() {
		return newOrder;
	}

	public void setNewOrder(ProductOrderCommon newOrder) {
		this.newOrder = newOrder;
	}	
	
	

}
