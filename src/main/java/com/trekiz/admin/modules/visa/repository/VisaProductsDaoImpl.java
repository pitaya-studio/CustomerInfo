package com.trekiz.admin.modules.visa.repository;



import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaProducts;

/**
 * 自定义DAO接口实现
 * 
 * 
 * 
 */
@Repository
class VisaProductsDaoImpl extends BaseDaoImpl<VisaProducts> implements
		VisaProductsDaoCustom {

}