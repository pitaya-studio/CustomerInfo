package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.RuleParameter;


public interface RuleParameterDao extends RuleParameterDaoCustom, CrudRepository<RuleParameter, Long>{

	
}

interface RuleParameterDaoCustom extends BaseDao<RuleParameter>{}

@Repository
class RuleParameterDaoImpl extends BaseDaoImpl<RuleParameter> implements RuleParameterDaoCustom {
	
}
