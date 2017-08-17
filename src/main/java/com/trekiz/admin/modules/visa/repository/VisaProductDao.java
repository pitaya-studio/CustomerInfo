package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaProducts;

/**
 * 重复的Dao，与visaProductsDao重复，请使用后者。
 */
public interface VisaProductDao extends VisaProductDaoCustom, CrudRepository<VisaProducts, Long>{

	
}
interface VisaProductDaoCustom extends BaseDao<VisaProducts>{}

@Repository
class VisaProductDaoImpl extends BaseDaoImpl<VisaProducts> implements VisaProductDaoCustom {
	
}
