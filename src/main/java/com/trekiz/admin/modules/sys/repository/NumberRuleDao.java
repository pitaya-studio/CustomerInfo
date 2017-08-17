package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.NumberRule;


public interface NumberRuleDao extends NumberRuleDaoCustom, CrudRepository<NumberRule, Long>{

	@Query(value="select count(numberType) from NumberRule n where n.numberType = ?1")
	public long checkIsExist(String  numberType);
	
	@Query(value="select numberValue from NumberRule n where n.markName = ?1")
	public String queryNumberByMarkname(String markName);
}

interface NumberRuleDaoCustom extends BaseDao<NumberRule>{}

@Repository
class NumberRuleDaoImpl extends BaseDaoImpl<NumberRule> implements NumberRuleDaoCustom {
	
}
