package com.trekiz.admin.modules.wholesalerbase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.wholesalerbase.entity.WholeOfficeType;

public interface WholeOfficeTypeDao extends WholeOfficeTypeDaoCustom, CrudRepository<WholeOfficeType, Long>{

	@Query("from WholeOfficeType w where w.companyID = ?1")
	public List<WholeOfficeType> findByCompanyUUID(Long companyUUID);
	
	@Query("from WholeOfficeType w where w.sysdefinedictUUID = ?1 and w.companyID = ?2")
	public WholeOfficeType findBySysdefinedictUUID(String sysdefinedictUUID,Long companyId);
}
interface WholeOfficeTypeDaoCustom extends BaseDao< WholeOfficeType>{
	
}
@Repository
class WholeOfficeTypeDaoImpl extends BaseDaoImpl<WholeOfficeType> implements WholeOfficeTypeDaoCustom{
	
}