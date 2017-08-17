package com.trekiz.admin.modules.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PayRemittance;

public interface PayRemittanceDao extends PayRemittanceCustom,
		CrudRepository<PayRemittance, Long> {
	@Query("from PayRemittance where id = ?1 and delFlag = '0'")
	public PayRemittance findPayRemittanceInfoById(String id);
}

interface PayRemittanceCustom extends BaseDao<PayRemittance> {

}

@Repository
class PayRemittanceDaoImpl extends BaseDaoImpl<PayRemittance> implements
		PayRemittanceCustom {

}
