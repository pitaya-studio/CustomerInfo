package com.trekiz.admin.modules.mtourOrder.jsonbean;

public class OrderSortInfo {
	private String sortKey;//'排序项',//下单时间:orderDateTime,出团日期:departureDate
	private boolean dec;//'是否倒序'
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public boolean getDec() {
		return dec;
	}
	public void setDec(boolean dec) {
		this.dec = dec;
	}
	

}
