package com.trekiz.admin.modules.order.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;

public interface OrderProgressTrackingDao extends OrderProgressTrackingDaoCustom, CrudRepository<OrderProgressTracking, Long> {

	@Query("FROM OrderProgressTracking WHERE askUserId = ?1 AND activityId = ?2 AND orderType = ?3 AND askSalerId = ?4 AND t1Flag = ?5  AND orderId is null and TIMESTAMPDIFF(SECOND,askTime, NOW()) <60 ")
	public List<OrderProgressTracking> findByAskUserIdAndProductIdAndSalerIdAndAskTime(Long askUserId, Long activityId, Integer orderType,Long askSalerId,Integer t1Flag);
	
	@Query("FROM OrderProgressTracking WHERE askUserId = ?1 AND activityId = ?2 AND orderType = ?3 AND askSalerId = ?4 AND t1Flag = 0  AND orderId is null")
	public List<OrderProgressTracking> findByAskUserIdAndProductIdAndSalerId(Long askUserId, Long activityId, Integer orderType,Long askSalerId);
	
	@Query("FROM OrderProgressTracking WHERE preOrderId = ?1")
	public OrderProgressTracking findByPreOrderId(Long preOrderId);
	
	@Query("FROM OrderProgressTracking WHERE askAgentId = ?1 AND activityId = ?2 AND orderType = ?3 AND askSalerId = ?4 " +
			"AND orderId is null AND DATEDIFF(?5 , askTime) <= 30 ORDER BY askTime DESC")
	public List<OrderProgressTracking> findByAgentIdAndProductIdAndSalerId(Long askAgentId, Long productId, Integer orderType, 
			Long askSalerId, Date orderTime);
	
	@Query("FROM OrderProgressTracking WHERE askAgentId = ?1 AND activityId = ?2 AND orderType = ?3 AND askSalerId = ?4 " +
			"AND orderId is null AND DATEDIFF(?5 , askTime) >= 0 AND DATEDIFF(?5 , askTime) <= 30 ORDER BY askTime DESC")
	public List<OrderProgressTracking> findEntityForBD(Long askAgentId, Long productId, Integer orderType, 
			Long askSalerId, Date orderTime);
	
	@Query("FROM OrderProgressTracking WHERE orderId = ?1")
	public OrderProgressTracking findByOrderId(Long orderId);
	
	@Query("FROM OrderProgressTracking WHERE company_id = ?1 AND ask_num IS NOT NULL AND ask_time IS NOT NULL ORDER BY ask_time ASC")
	public List<OrderProgressTracking> getEarliestAskTime(Long companyId);
}


interface OrderProgressTrackingDaoCustom extends BaseDao<OrderProgressTracking> {

}

@Repository
class OrderProgressTrackingDaoImpl extends BaseDaoImpl<OrderProgressTracking> implements OrderProgressTrackingDaoCustom {}
