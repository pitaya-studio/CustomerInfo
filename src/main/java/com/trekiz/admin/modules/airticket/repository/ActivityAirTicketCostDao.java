package com.trekiz.admin.modules.airticket.repository;




import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicketCost;

interface ActivityAirTicketCostDaoCustom extends BaseDao<ActivityAirTicketCost> {
}

public interface ActivityAirTicketCostDao extends ActivityAirTicketCostDaoCustom, CrudRepository<ActivityAirTicketCost, Long> {
		

}

@Repository
class ActivityAirTicketCostDaoImpl extends BaseDaoImpl<ActivityAirTicketCost> implements ActivityAirTicketCostDaoCustom {
	
}