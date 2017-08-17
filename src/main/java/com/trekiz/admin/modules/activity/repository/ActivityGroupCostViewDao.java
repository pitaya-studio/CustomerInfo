package com.trekiz.admin.modules.activity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCostView;

interface ActivityGroupCostViewDaoCustom extends BaseDao<ActivityGroupCostView> {
}

public interface ActivityGroupCostViewDao extends ActivityGroupCostViewDaoCustom, CrudRepository<ActivityGroupCostView, Long> {

}

@Repository
class ActivityGroupCostViewDaoImpl extends BaseDaoImpl<ActivityGroupCostView> implements ActivityGroupCostViewDaoCustom {
	
}