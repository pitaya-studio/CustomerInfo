package com.trekiz.admin.modules.mtourOrder.jsonbean;

/**
 * 财务付款-订单模块查询条件封装
 * ClassName: MtourOrderSearchJsonBean
 * @Description: 
 * @author majiancheng
 * @date 2016-1-25
 */
public class MtourOrderSearchJsonBean {
	private OrderSearchParam searchParam;//模糊查询参数
	private OrderFilterParam filterParam;//查询条件
	private OrderSortInfo sortInfo;//排序条件
	private OrderPageParam pageParam;//分页搜索信息
	public OrderSearchParam getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(OrderSearchParam searchParam) {
		this.searchParam = searchParam;
	}
	public OrderFilterParam getFilterParam() {
		return filterParam;
	}
	public void setFilterParam(OrderFilterParam filterParam) {
		this.filterParam = filterParam;
	}
	public OrderSortInfo getSortInfo() {
		return sortInfo;
	}
	public void setSortInfo(OrderSortInfo sortInfo) {
		this.sortInfo = sortInfo;
	}
	public OrderPageParam getPageParam() {
		return pageParam;
	}
	public void setPageParam(OrderPageParam pageParam) {
		this.pageParam = pageParam;
	}

}
