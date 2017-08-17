package com.trekiz.admin.modules.visa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeAddress;
import com.trekiz.admin.modules.visa.repository.VisaInterviewNoticeAddressDao;

@Service
@Transactional(readOnly = true)
public class VisaInterviewNoticeAddressService  extends BaseService {
	
	@Autowired
	private VisaInterviewNoticeAddressDao visaInterviewNoticeAddressDao;
	
	public List<Map<Object, Object>> list(Long companyId){
		return visaInterviewNoticeAddressDao.list(companyId);
	}
	
	public List<VisaInterviewNoticeAddress> findByCountryIdAndArea(Long countryId,String area,Long companyId){
		return visaInterviewNoticeAddressDao.findByCountryIdAndArea(countryId,area,companyId);
	}
	
	public int deleteByCountryIdAndArea(Long countryId,String area,Long companyId){
		return visaInterviewNoticeAddressDao.deleteByCountryIdAndArea(countryId,area,companyId);
	}
	
	public boolean batchDelete(String listIds){
		return visaInterviewNoticeAddressDao.batchDelete(listIds);
	}
}
