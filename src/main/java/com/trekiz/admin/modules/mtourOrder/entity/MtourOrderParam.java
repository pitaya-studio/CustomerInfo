package com.trekiz.admin.modules.mtourOrder.entity;


public class MtourOrderParam {

	private String searchType; // 搜索类型
	private String searchKey; // 搜索输入的文本
	private String[] channelUuid; // ['渠道Uuid'](逗号分开)
	private String[] ordererId;  // ['下单人Id'](逗号分开)
	private String[] orderStatusCode; // ['订单状态Code']（逗号分开）
	private String[] receiveStatusCode;// ['收款状态Code'](逗号分开)
	private String currentIndex; // 当前页码'
	private String rowCount; // 每页总行数'
	private String sortKey;//排序项
	private boolean dec; //是否倒序
	private String createDateStart;//下单日期开始
	private String createDateEnd;//下单日期结束
	
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
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String[] getChannelUuid() {
		return channelUuid;
	}
	public void setChannelUuid(String[] channelUuid) {
		this.channelUuid = channelUuid;
	}
	public String[] getOrdererId() {
		return ordererId;
	}
	public void setOrdererId(String[] ordererId) {
		this.ordererId = ordererId;
	}
	public String[] getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String[] orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public String[] getReceiveStatusCode() {
		return receiveStatusCode;
	}
	public void setReceiveStatusCode(String[] receiveStatusCode) {
		this.receiveStatusCode = receiveStatusCode;
	}
	public String getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(String currentIndex) {
		this.currentIndex = currentIndex;
	}
	public String getRowCount() {
		return rowCount;
	}
	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}
	public String getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	
}
