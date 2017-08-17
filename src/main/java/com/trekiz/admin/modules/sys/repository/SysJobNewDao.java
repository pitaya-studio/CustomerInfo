package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysJobNew;

interface SysJobNewDaoCustom extends BaseDao<SysJobNew> {
}

public interface SysJobNewDao extends SysJobNewDaoCustom, CrudRepository<SysJobNew, Long>{
	@Query("from SysJobNew where companyUuid = ?1 and delFlag = '0' order by id desc ")
	public List<SysJobNew> findByCompanyUuid(String companyUuid);

	@Query("from SysJobNew where id=?1 and delFlag=0")
	SysJobNew findById(Long id);
}

class SysJobNewDaoImpl extends BaseDaoImpl<SysJobNew> implements SysJobNewDaoCustom {
	
}