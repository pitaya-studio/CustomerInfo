package com.trekiz.admin.modules.order.airticket.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;

public interface AirticketPreOrderDao extends IAirticketPreOrderDao,
		CrudRepository<AirticketOrder, Long> {
	
	/**
	 * 根据主订单ID查询机票子订单
	 * @param sqlString
	 * @return
	 */
	@Query("from AirticketOrder where mainOrderId = ?1")
	public AirticketOrder findByProductOrderId(Long mainOrderId);
	
	@Modifying
    @Query("update AirticketOrder set lockStatus=?2 where id=?1")
	public void updateOrderLockStatus(Long orderId, int status);
	
	@Modifying
    @Query("update AirticketOrder set remaindDays=?2,orderState=?3,activationDate=?4 where id=?1")
    public int invokeOrder(Long id, Integer remainDays, Integer payStatus, Date activationDate );
	
	//修改未查看订单状态
	@Modifying
	@Query("update AirticketOrder set seenFlag = 1 where id in ?1")
    public int changeNotSeenOrderFlag(Set<Long> notSeenOrderIdList);
	
	@Query("FROM ActivityAirTicket WHERE id in ?1")
    public List<ActivityAirTicket> findYwByActivityIds(List<Long> groupIds);
	
	@Query("select id FROM ActivityAirTicket WHERE currency_id = ?1")
	public List<Integer> findIdByCurrencyId(Long currencyId);
}

