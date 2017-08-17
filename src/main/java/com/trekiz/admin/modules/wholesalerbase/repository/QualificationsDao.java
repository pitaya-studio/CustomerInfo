package com.trekiz.admin.modules.wholesalerbase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.wholesalerbase.entity.Qualifications;

/**
 * 批发商资质附件关联表
 * @author gao
 *  2015年4月14日
 */
public interface QualificationsDao extends QualificationsDaoCustom,CrudRepository<Qualifications, Long> {

	@Query("from Qualifications q where q.companyId=?1")
	public List<Qualifications> getQualificationsByCompanyId(Long companyId);
	/** 查询除“其他文件”外的上传文件 */
	@Query("from Qualifications q where q.companyId=?1  and q.titleType in (1,2,3,4,5,6,7)")
	public List<Qualifications> getQualificationsByCompanyIdWithOutOther(Long companyId);
	/** 查询“其他文件” */
	@Query("from Qualifications q where q.companyId=?1 and q.titleType in (8)")
	public List<Qualifications> getQualificationsByCompanyIdOther(Long companyId);
	
	@Query("from Qualifications q where q.uuid = ?1")
	public Qualifications getQualificationsByUUID(String UUID);
	
	@Query("from Qualifications q where q.docInfoId = ?1")
	public Qualifications getQualificationsBySalerTripFileId(Long salerTripFileId);
}
interface QualificationsDaoCustom extends BaseDao<Qualifications> {
	
}

@Repository
class QualificationsDaoImpl extends BaseDaoImpl<Qualifications> implements QualificationsDaoCustom{
	
}