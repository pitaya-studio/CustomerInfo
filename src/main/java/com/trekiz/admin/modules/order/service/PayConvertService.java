package com.trekiz.admin.modules.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.PayConvert;
import com.trekiz.admin.modules.order.repository.PayConvertDao;

/**
 * 支付-支票Service 
 */
@Service 
class PayConvertService {
	@Autowired
	private PayConvertDao payConvertDao;

	/**
	 * 保存信息
	 * @param payCheck
	 */
	void save(PayConvert payConvert) {
		payConvertDao.save(payConvert);
	}
	
	List<PayConvert> findInfoBySerialNum(String serialNum){
		return payConvertDao.findInfoBySerialNum(serialNum);
	}

//	/**
//	 * 由ID取得支付-支票信息
//	 * @param id
//	 * @return
//	 */
//	public PayCheck findPayCheckInfoById(String id) {
//		return payCheckDao.findPayCheckInfoById(id);
//	}
}
