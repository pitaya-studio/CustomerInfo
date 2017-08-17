package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaProductsCostView;


interface VisaProductsCostViewDaoCustom extends BaseDao<VisaProductsCostView> {
}

public interface VisaProductsCostViewDao extends VisaProductsCostViewDaoCustom, CrudRepository<VisaProductsCostView, Long> {
		

}

@Repository
class VisaProductsCostViewDaoImpl extends BaseDaoImpl<VisaProductsCostView> implements VisaProductsCostViewDaoCustom {
	
}
