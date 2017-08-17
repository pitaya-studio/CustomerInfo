package com.trekiz.admin.modules.mtourOrder.entity;
/**
 * 订单列表排序项
 * @author gao
 *
 */
public class MtourOrderSortInfo {

	/**创建时间*/
	public static String SORTKEY_ORDERDATETIME = "orderDateTime"; 
	/**更新时间*/
	public static String SORTKEY_MODIFIEDDATETIME = "modifiedDateTime"; 
	/**出团日期*/
	public static String SORTKEY_DEPARTUREDATETIME = "departureDate"; 
	
	// 创建时间:orderDateTime,更新时间:modifiedDateTime,出团日期:departureDate
	private String sortKey;
	// '是否倒序'
	private boolean dec;
	
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
