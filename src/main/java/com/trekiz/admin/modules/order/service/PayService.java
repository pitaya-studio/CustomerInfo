package com.trekiz.admin.modules.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.Pay;
import com.trekiz.admin.modules.order.repository.PayDao;

@Service
public class PayService {
	@Autowired
	private PayDao payDao;

	public void save(Pay pay) {
		payDao.save(pay);
	}
}
