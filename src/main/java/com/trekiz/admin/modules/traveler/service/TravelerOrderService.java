package com.trekiz.admin.modules.traveler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.traveler.entity.TravelerOrder;
import com.trekiz.admin.modules.traveler.repository.TravelerOrderDao;


/**
 * 
 *  文件名: TravelerVisaService.java
 *  功能:游客订单关系Service
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-11-13 上午10:25:38
 *  @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class TravelerOrderService extends BaseService{
	
	@Autowired
	private TravelerOrderDao travelerOrderDao;
	
	public TravelerOrder save(TravelerOrder travelerOrder){
		return travelerOrderDao.save(travelerOrder);
	}
}
