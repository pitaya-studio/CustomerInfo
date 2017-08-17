package com.trekiz.admin.modules.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCompare;

public interface ActivityGroupCompareDao extends ActivityGroupCompareDaoCustom,CrudRepository<ActivityGroupCompare, Long> {
	@Query("from ActivityGroupCompare where operatorId=?1 and activityGroupId=?2")
	public ActivityGroupCompare findByProperties(Long operatorId, Long activityGroupId);
	
	@Query("from ActivityGroupCompare where operatorId=?1")
	public List<ActivityGroupCompare> findByPropertie(Long operatorId);
	
	@Query("select groups from ActivityGroupCompare groupCom, ActivityGroup groups " +
			"where groupCom.activityGroupId = groups.id and groups.delFlag = '0' and groupCom.operatorId = ?1")
	public List<ActivityGroup> findCompareGroup(Long operatorId);
	
	@Modifying
	@Query("update ActivityGroupCompare set sortName = ?2 where operatorId = ?1")
	public int saveCompareSortName(Long userId, String sortName);
}

/**
 * 自定义接口
 * @author zhangyp
 *
 */
interface ActivityGroupCompareDaoCustom extends BaseDao<ActivityGroupCompare>{
	
}

/**
 * 自定义接口实现
 * @author zhangyp
 *
 */
@Repository
class ActivityGroupCompareDaoImpl extends BaseDaoImpl<ActivityGroupCompare> implements ActivityGroupCompareDaoCustom{
	
}
