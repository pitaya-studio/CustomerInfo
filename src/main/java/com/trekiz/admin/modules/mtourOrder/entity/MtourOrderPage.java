package com.trekiz.admin.modules.mtourOrder.entity;

/**
 * 订单列表分页类
 * @author gao
 * @Date 2015-10-30
 */
public class MtourOrderPage {

	private Integer totalRowCount; // 总行数
	private Integer currentIndex; // 当前页码'
	private Integer rowCount; // 每页总行数'
	public Integer getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	public Integer getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
}
