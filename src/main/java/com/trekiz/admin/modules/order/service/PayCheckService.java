package com.trekiz.admin.modules.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.PayCheck;
import com.trekiz.admin.modules.order.repository.PayCheckDao;

/**
 * 支付-支票Service 
 */
@Service
public class PayCheckService {
	@Autowired
	private PayCheckDao payCheckDao;

	/**
	 * 保存支付-支票信息
	 * @param payCheck
	 */
	public void save(PayCheck payCheck) {
		payCheckDao.save(payCheck);
	}

	/**
	 * 由ID取得支付-支票信息
	 * @param id
	 * @return
	 */
	public PayCheck findPayCheckInfoById(String id) {
		return payCheckDao.findPayCheckInfoById(id);
	}
}
