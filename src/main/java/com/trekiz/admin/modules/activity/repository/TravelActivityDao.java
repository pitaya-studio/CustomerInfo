package com.trekiz.admin.modules.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;

/**
 * 旅游产品信息DAO接口
 * @author liangjingming
 *
 */
public interface TravelActivityDao extends TravelActivityDaoCustom,CrudRepository<TravelActivity, Long> {
	
	@Modifying
	@Query("update TravelActivity set delFlag = '"+TravelActivity.DEL_FLAG_DELETE+"' where id in ?1")
	public void batchDelActivity(List<Long> ids);
		
	@Modifying
	@Query("update TravelActivity set activityStatus = ?2 where id in ?1")
	public void batchOnOrOffActivity(List<Long> ids, Integer status);
	
	@Query("from TravelActivity where proCompany=?1 and delFlag='"+TravelActivity.DEL_FLAG_NORMAL+"' and activitySerNum=?2 and id<>?3")
	public List<TravelActivity> findActivity(Long proCompany,String activitySerNum,Long proId);
	
	@Query("from TravelActivity where proCompany=?1 and delFlag='"+TravelActivity.DEL_FLAG_NORMAL+"'")
	public List<TravelActivity> findActivityByCompany(Long proCompany);
	
	@Query("from TravelActivity where proCompany=?1")
	public List<TravelActivity> findActivityByCompanyIgnoreDeleteFlag(Long proCompany);
	
	@Query("FROM ActivityGroup WHERE id in ?1")
    public List<ActivityGroup> findYwByGroupIds(List<Long> groupIds);


	@Query("FROM TravelActivity WHERE activityKind = ?1 and delFlag = ?2")
	List<TravelActivity> findIdByActivityKindAndDelFlag(Integer activityKind, String delFlag);

	@Query("FROM TravelActivity WHERE activityKind = ?1 and proCompany=?2")
	public List<TravelActivity> findAllIdsByCompanyAndType(Integer activityType, Long companyId);

	@Query("from TravelActivity where touristLineId = ?1 and delFlag = '0'")
	public List<TravelActivity> isUsed4TouristLine(Long touristLineId);

	@Modifying
	@Query("update TravelActivity set touristLineId = ?1 where id = ?2")
	public void updateTouristLine(Long touristLineId, Long activityId);
} 


/**
 * 自定义DAO接口
 * @author liangjingming
 *
 */
interface TravelActivityDaoCustom extends BaseDao<TravelActivity> {
	
}

/**
 * 自定义DAO接口实现
 * @author liangjingming
 *
 */
@Repository
class TravelActivityDaoImpl extends BaseDaoImpl<TravelActivity> implements TravelActivityDaoCustom{
	
}