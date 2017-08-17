package com.trekiz.admin.modules.traveler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;


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
public class TravelerVisaService extends BaseService{
	
	@Autowired
	private TravelerVisaDao travelerVisaDao;
	
	public List<TravelerVisa> findVisaListByPid(Long srcTravelerId){
		return travelerVisaDao.findVisaListByPid(srcTravelerId);
	}

	TravelerVisa save(TravelerVisa travelerVisa){
		return travelerVisaDao.save(travelerVisa);
	}
	
	void delete(Long travelerVisaId){
		travelerVisaDao.delTravelerVisaByTravelerId(travelerVisaId);
	}
	
	public void deleteByOrderId(Long Id){
		String sql = "delete from travelervisa where travelerid in (select id from traveler where orderid = "+Id+")";
		travelerVisaDao.getSession().createSQLQuery(sql).executeUpdate();
	}
}
