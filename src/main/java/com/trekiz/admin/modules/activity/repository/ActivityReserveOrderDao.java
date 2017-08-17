package com.trekiz.admin.modules.activity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;

interface ActivityReserveOrderDaoCustom extends BaseDao<ActivityReserveOrder> {
}

public interface ActivityReserveOrderDao extends ActivityReserveOrderDaoCustom, CrudRepository<ActivityReserveOrder, Long> {
	   //散拼库存切位的业务类型=1
	
	   @Query("from ActivityReserveOrder where orderNum=?1  order by id")
	    public  List<ActivityReserveOrder> findActivityReserveOrder(String orderNum);	   
}

@Repository
class ActivityReserveOrderDaoImpl extends BaseDaoImpl<ActivityReserveOrder> implements ActivityReserveOrderDaoCustom {
	
}