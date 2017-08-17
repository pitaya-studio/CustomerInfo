package com.trekiz.admin.modules.mtourOrder.jsonbean;

/**
 * 分页搜索信息
 * ClassName: OrderPageParam
 * @Description: 
 * @author majiancheng
 * @date 2016-1-25
 */
public class OrderPageParam {
	private Integer currentIndex;
	private Integer rowCount;
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
