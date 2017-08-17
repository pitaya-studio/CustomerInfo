package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;

/**
 * 账龄查询的Json对象类
 * @author shijun.liu
 *
 */
public class AccountAgeJsonBean {

	private List<AccountAgeJsonResult> results;		//账龄查询结果
	private Page page;								//分页对象
	public List<AccountAgeJsonResult> getResults() {
		return results;
	}
	public void setResults(List<AccountAgeJsonResult> results) {
		this.results = results;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
