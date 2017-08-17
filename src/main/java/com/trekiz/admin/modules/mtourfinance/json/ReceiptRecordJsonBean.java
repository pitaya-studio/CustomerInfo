package com.trekiz.admin.modules.mtourfinance.json;

import java.util.List;
import java.util.Map;

/**
 * 收款记录列表
 * @author xu.wang
 *
 */
public class ReceiptRecordJsonBean {
	
	List<Map<String, Object>> results;//表格的输出结果
	Map<String, String> page;//分页信息
	
	public List<Map<String, Object>> getResults() {
		return results;
	}
	public void setResults(List<Map<String, Object>> results) {
		this.results = results;
	}
	public Map<String, String> getPage() {
		return page;
	}
	public void setPage(Map<String, String> page) {
		this.page = page;
	}
	
}
