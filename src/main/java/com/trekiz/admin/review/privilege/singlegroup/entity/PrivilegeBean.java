package com.trekiz.admin.review.privilege.singlegroup.entity;

import java.util.HashMap;
import java.util.Map;

public class PrivilegeBean {

	/**
	 * 货币类型Id
	 */
	private static final String KEY_CURRENCYID = "currencyId";
	
	public PrivilegeBean(Map<String,String> reviewDetailMap){
		
		this.currencyId = reviewDetailMap.get(KEY_CURRENCYID);
		
	}
	
	public Map<String, String> getReviewDetailMap() {
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put(KEY_CURRENCYID, currencyId);
		return reviewDetailMap;
	}
	
	private String currencyId;

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	
	
}
