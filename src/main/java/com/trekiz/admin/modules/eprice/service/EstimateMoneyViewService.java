package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import com.trekiz.admin.modules.eprice.entity.EstimateMoneyView;

public interface EstimateMoneyViewService {

	/**
	 * 根据报价回复ID，查询报价结果LIst
	 * @author gao
	 * @param replyId
	 * @return
	 */
	public List<EstimateMoneyView> findView(Long replyId);
}
