package com.trekiz.admin.modules.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;

/**
 * 切位信息DAO接口
 * @author zj
 *
 */
public interface ActivityGroupReserveDao extends ActivityGroupReserveDaoCustom,CrudRepository<ActivityGroupReserve, Long>{

	public List<ActivityGroupReserve> findByAgentIdAndSrcActivityId(Long agentId,Long srcActivityId);
	
	public ActivityGroupReserve findByAgentIdAndSrcActivityIdAndActivityGroupId(Long agentId,Long srcActivityId,Long activityGroupId);
	
	@Query("SELECT t2.orderCompanyName,t1.payReservePosition,t1.leftpayReservePosition,t2.orderPersonName,t2.orderNum,t2.totalMoney,t2.payedMoney,t2.orderPersonNum," +
			"t2.payStatus,t1.remark,t2.orderCompany "
			+"FROM ActivityGroupReserve t1,ProductOrderCommon t2 where t1.agentId = t2.orderCompany and t1.activityGroupId = t2.productGroupId and t2.placeHolderType=1 " +
			"and t1.activityGroupId = ?1 and t2.payStatus != 99")
	public List<Object[]> findSoldNopayPosition(Long activityGroupId);
	
	@Query("FROM ActivityGroupReserve WHERE srcActivityId=?1 and agentId=?2")
	public List<ActivityGroupReserve> findPayReservePositionByActivityId(Long srcActivityId,Long agentId);
	
	public ActivityGroupReserve findByActivityGroupIdAndAgentId(Long groupId, Long agentId);
	
	@Query("FROM ActivityGroupReserve WHERE activityGroupId=?1")
	public List<ActivityGroupReserve> findPayReserveByGroupId(Long groupId);
	
	@Query("from ProductOrderCommon where productGroupId = (select id from ActivityGroup WHERE groupCode=?1 and srcActivityId=?2)")
	public List<ProductOrderCommon> findProductorderByGroupCode(String groupCode,Integer srcActivityId);

	@Query("from TravelActivity where id = ?1 ")
	public List<TravelActivity> findTravelActivityByGroupCode(Long srcActivityId);
	
	@Query("from ActivityGroup where groupCode=?1 and srcActivityId=?2")
	public List<ActivityGroup> findActivityGroupByGroupCode(String groupCode,Integer srcActivityId);

	public List<ActivityGroupReserve> findByActivityGroupIdAndSrcActivityId(Long activityGroupId, Long srcActivityId);
	
	/**
	 * 根据团期id和渠道id获取切位信息集合
	 * @Description: 
	 * @param @param activityGroupId
	 * @param @param agentId
	 * @param @return   
	 * @return List<ActivityGroupReserve>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-3 下午1:54:45
	 */
	@Query("FROM ActivityGroupReserve WHERE activityGroupId=?1 and agentId=?2 and delFlag='"+BaseEntity.DEL_FLAG_NORMAL+"'")
	public List<ActivityGroupReserve> findReserveByActivityGroupId(Long activityGroupId,Long agentId);
}

/**
 * 自定义DAO接口
 * @author liangjingming
 *
 */
interface ActivityGroupReserveDaoCustom extends BaseDao<ActivityGroupReserve>{
	
}

/**
 * 自定义DAO接口实现
 * @author liangjingming
 *
 */
@Repository
class ActivityGroupReserveDaoImpl extends BaseDaoImpl<ActivityGroupReserve> implements ActivityGroupReserveDaoCustom{
	
}