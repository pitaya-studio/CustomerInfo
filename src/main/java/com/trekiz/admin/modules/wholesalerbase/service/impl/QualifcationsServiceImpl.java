package com.trekiz.admin.modules.wholesalerbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.wholesalerbase.entity.Qualifications;
import com.trekiz.admin.modules.wholesalerbase.repository.QualificationsDao;
import com.trekiz.admin.modules.wholesalerbase.service.QualificationsService;
/**
 * 批发商资质附件关联
 * @author gao
 *  2015年4月14日
 */
@Service
@Transactional(readOnly = true)
public class QualifcationsServiceImpl implements QualificationsService {

	@Autowired
	private QualificationsDao qualificationsDao;
	@Transactional(readOnly = false)
	@Override
	public Qualifications save(Qualifications qualifications) {
		Qualifications back = qualificationsDao.save(qualifications);
		return back;
	}

	@Override
	public List<Qualifications> getQualificationsByCompanyId(Long companyId) {
		List<Qualifications> back =qualificationsDao.getQualificationsByCompanyId(companyId);
		return back;
	}

	@Override
	public Qualifications getQualificationsByUUID(String UUID) {
		Qualifications back = qualificationsDao.getQualificationsByUUID(UUID);
		return back;
	}

	@Override
	public List<Qualifications> getQualificationsByCompanyIdWithOutOther(
			Long companyId) {
		List<Qualifications> back =qualificationsDao.getQualificationsByCompanyIdWithOutOther(companyId);
		return back;
	}

	@Override
	public List<Qualifications> getQualificationsByCompanyIdOther(Long companyId) {
		List<Qualifications> back =qualificationsDao.getQualificationsByCompanyIdOther(companyId);
		return back;
	}
	
	@Override
	public Qualifications getBySalerTripFileId(Long salerTripFileId) {
		Qualifications qualifications = qualificationsDao.getQualificationsBySalerTripFileId(salerTripFileId);
		return qualifications;
	}
}
