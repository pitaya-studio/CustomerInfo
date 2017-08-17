package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.List;

import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderDetail;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderPage;


/**
 * 订单列表返回结构
 * @author gao
 * @date 2015年10月21日
 */
public class MtourOrderJsonBean {

	private List<MtourOrderDetail> results; //订单列表
	private MtourOrderPage page;//  分页相关
	public List<MtourOrderDetail> getResults() {
		return results;
	}
	public void setResults(List<MtourOrderDetail> results) {
		this.results = results;
	}
	public MtourOrderPage getPage() {
		return page;
	}
	public void setPage(MtourOrderPage page) {
		this.page = page;
	}
}
