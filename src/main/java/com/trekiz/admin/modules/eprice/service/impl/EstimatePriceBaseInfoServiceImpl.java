package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceBaseInfoDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceBaseInfoService;

@Service("estimatePriceBaseInfoService")
@Transactional(readOnly = true)
public class EstimatePriceBaseInfoServiceImpl implements EstimatePriceBaseInfoService {

	@Autowired
	private EstimatePriceBaseInfoDao estimatePriceBaseInfoDao;
	
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public EstimatePriceBaseInfo save(EstimatePriceBaseInfo epbi) {
		
		EstimatePriceBaseInfo info =estimatePriceBaseInfoDao.save(epbi);
		return info;
	}
	
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void update(EstimatePriceBaseInfo epbi) {
		
		estimatePriceBaseInfoDao.update(epbi);
	}

	public EstimatePriceBaseInfo findById(Long id) {
		
		return estimatePriceBaseInfoDao.findById(id);
	}

	public void delById(Long id) {
		EstimatePriceBaseInfo epbi = new EstimatePriceBaseInfo();
		epbi.setId(id);
		epbi.setStatus(EstimatePriceBaseInfo.STATUS_DEL);
		epbi.setModifyTime(new Date());
		
		this.save(epbi);
	}

	@Override
	public List<EstimatePriceBaseInfo> findByPid(Long pid) {
		// TODO Auto-generated method stub
		List<EstimatePriceBaseInfo> findByPid = estimatePriceBaseInfoDao.findByPid(pid);
		return findByPid;
	}

}
