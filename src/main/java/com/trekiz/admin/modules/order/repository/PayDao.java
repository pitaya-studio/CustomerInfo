/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.Pay;

public interface PayDao extends PayCustom, CrudRepository<Pay, Long> {
}

interface PayCustom extends BaseDao<Pay> {

}

@Repository
class PayDaoImpl extends BaseDaoImpl<Pay> implements PayCustom {

}
