package com.trekiz.admin.modules.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.repository.PayRemittanceDao;

/**
 * 支付-汇款Service 
 */
@Service 
class PayRemittanceService {
	@Autowired
	private PayRemittanceDao payRemittanceDao;

	/**
	 * 保存支付-汇款信息
	 * @param payRemittance
	 */
	public void save(PayRemittance payRemittance) {
		payRemittanceDao.save(payRemittance);
	}
	
	/**
	 * 由ID取得支付-汇款信息
	 * @param id
	 * @return
	 */
	PayRemittance findPayRemittanceInfoById(String id){
		return payRemittanceDao.findPayRemittanceInfoById(id);
	}
}
