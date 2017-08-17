package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceAdmitRequirementsDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceAdmitRequirementsService;

@Service("estimatePriceAdmitRequirementsService")
@Transactional(readOnly = true)
public class EstimatePriceAdmitRequirementsServiceImpl implements EstimatePriceAdmitRequirementsService {
	
	@Autowired
	private EstimatePriceAdmitRequirementsDao estimatePriceAdmitRequirementsDao;

	public void save(EstimatePriceAdmitRequirements epar) {
		estimatePriceAdmitRequirementsDao.save(epar);
		estimatePriceAdmitRequirementsDao.getSession().flush();
		
	}

	public EstimatePriceAdmitRequirements findById(Long id) {
		if(id==null){
			return null;
		}
		return estimatePriceAdmitRequirementsDao.findById(id);
	}

	public void delById(Long id) {
		if(id==null){
			return ;
		}
		EstimatePriceAdmitRequirements epar = new EstimatePriceAdmitRequirements();
		
		epar.setId(id);
		epar.setStatus(EstimatePriceAdmitRequirements.STATUS_DEL);
		epar.setModifyTime(new Date());
		
		this.save(epar);
		
	}

	@Override
	public void update(EstimatePriceAdmitRequirements epar) {
		// TODO Auto-generated method stub
		estimatePriceAdmitRequirementsDao.getSession().update(epar);
		
	}

	@Override
	public List<EstimatePriceAdmitRequirements> findByPid(Long pid) {
		// TODO Auto-generated method stub
		List<EstimatePriceAdmitRequirements> findByPid = estimatePriceAdmitRequirementsDao.findByPid(pid);
		return findByPid;
	}

}
