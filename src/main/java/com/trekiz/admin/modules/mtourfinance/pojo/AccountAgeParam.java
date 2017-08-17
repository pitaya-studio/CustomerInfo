package com.trekiz.admin.modules.mtourfinance.pojo;

/**
 * 账龄查询需要的参数
 * @author shijun.liu
 *
 */
public class AccountAgeParam {
	private String searchType;		//搜索类型
	private String searchKey;		//搜索关键字
	private String accountAgeStatus;//账龄状态
	private String channelId;		//渠道*ID
	private String salerId;			//销售ID
	private Integer pageNow;		//当前页
	private Integer pageCount;		//每页显示数目
	private String orderBy;			//排序字段		字段名称 desc / 字段名称   asc
	private String channelType;		//渠道类型， 1:签约渠道，2:非签约渠道
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
	public String getAccountAgeStatus() {
		return accountAgeStatus;
	}
	public void setAccountAgeStatus(String accountAgeStatus) {
		this.accountAgeStatus = accountAgeStatus;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public Integer getPageNow() {
		return pageNow;
	}
	public void setPageNow(Integer pageNow) {
		this.pageNow = pageNow;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

}
