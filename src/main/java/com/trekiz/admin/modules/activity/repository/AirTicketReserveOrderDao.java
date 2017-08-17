package com.trekiz.admin.modules.activity.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.airticket.entity.AirTicketReserveOrder;

interface AirTicketReserveOrderDaoCustom extends BaseDao<AirTicketReserveOrder> {
}
public interface AirTicketReserveOrderDao extends AirTicketReserveOrderDaoCustom, CrudRepository<AirTicketReserveOrder, Long> {

	
}
@Repository
class AirTicketReserveOrderDaoImpl extends BaseDaoImpl<AirTicketReserveOrder> implements AirTicketReserveOrderDaoCustom {
	
}
