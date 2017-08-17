package com.trekiz.admin.modules.visa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;

@Service
@Transactional(readOnly = true)
public class BatchTravelerRelationService extends BaseService {

	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	
	public void save(BatchTravelerRelation relation){
		
		batchTravelerRelationDao.getSession().save(relation);
	}
}
