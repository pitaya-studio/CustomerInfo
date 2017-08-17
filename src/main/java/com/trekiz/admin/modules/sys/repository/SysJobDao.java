package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.repository.CrudRepository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysJob;

interface SysJobDaoCustom extends BaseDao<SysJob> {
}

public interface SysJobDao extends SysJobDaoCustom, CrudRepository<SysJob, Long> {	   
	  	   

}

class SysJobDaoImpl extends BaseDaoImpl<SysJob> implements SysJobDaoCustom {
	
}