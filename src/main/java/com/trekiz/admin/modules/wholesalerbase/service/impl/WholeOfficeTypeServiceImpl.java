package com.trekiz.admin.modules.wholesalerbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.wholesalerbase.entity.WholeOfficeType;
import com.trekiz.admin.modules.wholesalerbase.repository.WholeOfficeTypeDao;
import com.trekiz.admin.modules.wholesalerbase.service.WholeOfficeTypeService;
@Service
@Transactional(readOnly = true)
public class WholeOfficeTypeServiceImpl implements WholeOfficeTypeService{

	@Autowired
	private WholeOfficeTypeDao wholeOfficeTypeDao;
	
	@Override
	public List<WholeOfficeType> findByCompanyID(String companyID) {
		List<WholeOfficeType> wholeOfficeTypeList =  wholeOfficeTypeDao.findByCompanyUUID(Long.valueOf(companyID));
		if(wholeOfficeTypeList!=null && !wholeOfficeTypeList.isEmpty()){
			return wholeOfficeTypeList;
		}
		return null;
	}

	@Override
	public WholeOfficeType findBySysdefinedictUUID(String sysdefinedictUUID,Long companyId) {
		WholeOfficeType wholeOfficeType =  wholeOfficeTypeDao.findBySysdefinedictUUID(sysdefinedictUUID, companyId);
		if(wholeOfficeType!=null ){
			return wholeOfficeType;
		}
		return null;
	}

	@Override
	public WholeOfficeType save(WholeOfficeType wholeOfficeType) {
		WholeOfficeType back = wholeOfficeTypeDao.save(wholeOfficeType);
		if(back!=null){
			return back;
		}
		return null;
	}
}
