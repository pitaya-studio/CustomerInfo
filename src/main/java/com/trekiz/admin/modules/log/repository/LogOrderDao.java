package com.trekiz.admin.modules.log.repository;


import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.log.entity.LogOrder;
import org.springframework.data.repository.CrudRepository;
interface LogOrderDaoCustom extends BaseDao<LogOrder> {
}

public interface LogOrderDao extends LogOrderDaoCustom, CrudRepository<LogOrder, Long> {

}

class LogOrderDaoImpl extends BaseDaoImpl<LogOrder> implements LogOrderDaoCustom {
	
}