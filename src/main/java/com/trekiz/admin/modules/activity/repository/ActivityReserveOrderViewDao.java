package com.trekiz.admin.modules.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrderView;

interface ActivityReserveOrderViewDaoCustom extends BaseDao<ActivityReserveOrderView> {
}
public interface ActivityReserveOrderViewDao extends ActivityReserveOrderViewDaoCustom, CrudRepository<ActivityReserveOrderView, Long> {
   @Query("from ActivityReserveOrderView where aid=?1  order by id")
   public  List<ActivityReserveOrderView> findActivityGroupView( Integer aid);
}
@Repository
class ActivityReserveOrderViewDaoImpl extends BaseDaoImpl<ActivityReserveOrderView> implements ActivityReserveOrderViewDaoCustom {
	
}