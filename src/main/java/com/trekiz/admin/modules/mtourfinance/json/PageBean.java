package com.trekiz.admin.modules.mtourfinance.json;

public class PageBean {

	 private String totalRowCount;	//总行数
     private String currentIndex;	//当前页
     private String rowCount;		//每页显示行数
	public String getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(String totalRowCount) {
		this.totalRowCount = totalRowCount;
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
}
