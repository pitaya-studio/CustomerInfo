/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PayCheck;

public interface PayCheckDao extends PayCheckCustom, CrudRepository<PayCheck, Long> {
	@Query("from PayCheck where id = ?1 and delFlag = '0'")
	public PayCheck findPayCheckInfoById(String id);
}


interface PayCheckCustom extends BaseDao<PayCheck> {

}

@Repository
class PayCheckDaoImpl extends BaseDaoImpl<PayCheck> implements PayCheckCustom {

}
