package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaProductOther;

public interface VisaProductOtherDao extends VisaProductOtherDaoCustom, CrudRepository<VisaProductOther, Long>{

}

interface VisaProductOtherDaoCustom extends BaseDao<VisaProductOther>{}

@Repository
class VisaProductOtherDaoImpl extends BaseDaoImpl<VisaProductOther> implements VisaProductOtherDaoCustom {
	
}