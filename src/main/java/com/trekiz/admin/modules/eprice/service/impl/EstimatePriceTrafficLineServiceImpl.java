package com.trekiz.admin.modules.eprice.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceTrafficLineDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficLineService;

@Service("estimatePriceTrafficLineService")
@Transactional(readOnly = true)
public class EstimatePriceTrafficLineServiceImpl implements EstimatePriceTrafficLineService {

	@Autowired
	private EstimatePriceTrafficLineDao estimatePriceTrafficLineDao;
	
	public void save(EstimatePriceTrafficLine eptl) {
		
		estimatePriceTrafficLineDao.save(eptl);
	}

	public EstimatePriceTrafficLine findById(Long id) {
		
		return estimatePriceTrafficLineDao.findById(id);
	}

	public void delById(Long id) {
		EstimatePriceTrafficLine eptl = new EstimatePriceTrafficLine();
		eptl.setId(id);
		eptl.setStatus(EstimatePriceTrafficLine.STATUS_DEL);
		eptl.setModifyTime(new Date());
		
		this.save(eptl);
	}


	public void save(List<EstimatePriceTrafficLine> eptls) {
		
		for(EstimatePriceTrafficLine eptl : eptls){
			estimatePriceTrafficLineDao.save(eptl);
		}
		
		estimatePriceTrafficLineDao.getSession().flush();
	}

	
	public List<EstimatePriceTrafficLine> findByPfid(Long pfid) {
		
		return estimatePriceTrafficLineDao.findByPfid(pfid);
	}

}
