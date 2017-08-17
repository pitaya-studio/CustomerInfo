package com.trekiz.admin.modules.activity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityGroupView;
import com.trekiz.admin.modules.activity.entity.TargetArea;

interface ActivityGroupViewDaoCustom extends BaseDao<ActivityGroupView> {
}

public interface ActivityGroupViewDao extends ActivityGroupViewDaoCustom, CrudRepository<ActivityGroupView, Long> {
	@Query("from TargetArea where srcActivityId=?1")
	public  List<TargetArea> findTargetAreaById(Long id);	
}

@Repository
class ActivityGroupViewDaoImpl extends BaseDaoImpl<ActivityGroupView> implements ActivityGroupViewDaoCustom {
	
}