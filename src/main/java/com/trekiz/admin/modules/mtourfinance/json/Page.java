package com.trekiz.admin.modules.mtourfinance.json;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台分页使用
 * @author shijun.liu
 */
public class Page {
	
    private List results = new ArrayList();
	private PageBean page;
	public List getResults() {
		return results;
	}
	public void setResults(List results) {
		this.results = results;
	}
	public PageBean getPage() {
		return page;
	}
	public void setPage(PageBean page) {
		this.page = page;
	}
	
}
