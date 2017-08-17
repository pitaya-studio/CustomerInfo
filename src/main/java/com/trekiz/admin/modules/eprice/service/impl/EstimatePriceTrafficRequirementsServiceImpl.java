package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceTrafficRequirementsDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficRequirementsService;

@Service("estimatePriceTrafficRequirementsService")
@Transactional(readOnly = true)
public class EstimatePriceTrafficRequirementsServiceImpl implements EstimatePriceTrafficRequirementsService {

	@Autowired
	private EstimatePriceTrafficRequirementsDao estimatePriceTrafficRequirementsDao;
	
	public void save(EstimatePriceTrafficRequirements eptr) {
		
		estimatePriceTrafficRequirementsDao.save(eptr);
		estimatePriceTrafficRequirementsDao.getSession().flush();
	}

	public EstimatePriceTrafficRequirements findById(Long id) {
		
		return estimatePriceTrafficRequirementsDao.findById(id);
	}

	public void delById(Long id) {
		EstimatePriceTrafficRequirements eptr = new EstimatePriceTrafficRequirements();
		eptr.setId(id);
		eptr.setStatus(EstimatePriceTrafficRequirements.STATUS_DEL);
		eptr.setModifyTime(new Date());
		
		this.save(eptr);
		
	}

}
