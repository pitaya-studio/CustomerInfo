package com.trekiz.admin.modules.traveler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.traveler.entity.TravelerVisaNew;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaNewDao;


/**
 * 
 *  文件名: TravelerVisaService.java
 *  功能:游客签证信息
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-10-28 上午10:25:38
 *  @version 1.0
 */
@Service
@Transactional(readOnly = true) 
class TravelerVisaNewService extends BaseService{
	
	@Autowired
	private TravelerVisaNewDao travelerVisaDao;
	
	List<TravelerVisaNew> findVisaListByPid(Long srcTravelerId){
		return travelerVisaDao.findVisaListByPid(srcTravelerId);
	}
	
	/**
	 * 获取一个自备签
	 * @param id
	 * @return
     */
	TravelerVisaNew getById(Long id){
		Assert.notNull(id,"id should not null!");
		return travelerVisaDao.getById(id);
	}
	
	/**
	 * 批量保存自备签
	 * @param 
	 * @return
     */
	List<TravelerVisaNew> batchSaveReturn(List<TravelerVisaNew> travelerVisas){
		List<TravelerVisaNew> resultVisas = new ArrayList<>();
		Assert.notNull(travelerVisas,"travelerId should not null!");
		for (TravelerVisaNew travelerVisa : travelerVisas) {
			resultVisas.add(travelerVisaDao.save(travelerVisa));
		}
		return resultVisas;
	}
	
	/**
	 * 批量更新
	 * @param travelerVisas
	 */
	void batchUpdate(List<TravelerVisaNew> travelerVisas){
		travelerVisaDao.batchUpdate(travelerVisas);
	}
}
