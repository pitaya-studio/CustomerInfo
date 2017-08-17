package com.trekiz.admin.modules.eprice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.eprice.entity.EstimateMoneyView;
import com.trekiz.admin.modules.eprice.repository.EstimateMoneyViewDao;
import com.trekiz.admin.modules.eprice.service.EstimateMoneyViewService;

public class EstimateMoneyViewServiceImpl extends BaseService  implements EstimateMoneyViewService {
	@Autowired
	private EstimateMoneyViewDao estimateMoneyViewDao;
	@Override
	public List<EstimateMoneyView> findView(Long replyId) {
		List<EstimateMoneyView> list =  estimateMoneyViewDao.findByReplyId(replyId);
		return list;
	}
}
