package com.trekiz.admin.modules.airticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;


/***
 * 

 * @Description:DAO接口实现

 * @author:midas

 * @time:2014-9-19 上午10:22:13
 */
public interface ActivityAirTicketDao extends ActivityAirTicketDaoCustom,
		CrudRepository<ActivityAirTicket, Long> {
	
	@Modifying
	@Query("update ActivityAirTicket set productStatus = ?2 where id in ?1")
	public void batchOnOrOffActivity(List<Long> ids, Integer product_status);

	//提交成本审核
	@Modifying
	@Query("update ActivityAirTicket set nowLevel=1,review = ?2 where id = ?1 ")
	public void submitReview(Long id, Integer review);	
	
	//成本审核
	@Modifying
	@Query("update ActivityAirTicket set review = ?2,nowLevel=?3 where id = ?1")
	public void updateReview(Long id, Integer review,Integer nowLevel);	
	
	
	@Modifying
	@Query("update ActivityAirTicket set delFlag = '"+ActivityAirTicket.DEL_FLAG_DELETE+"' where id in ?1")
	public void batchDelActivity(List<Long> ids);
	
	@Query("from ActivityAirTicket where delFlag = '"+ActivityAirTicket.DEL_FLAG_NORMAL+"' and productCode = ?1")
	public ActivityAirTicket findByProductCode(String productCode);
	
	//修改机票产品余位、各渠道总的占位人数、已支付占位人数
	@Modifying
	@Query("update ActivityAirTicket set freePosition = ?2,nopayReservePosition=?3,soldNopayPosition=?4 where id = ?1")
	public void updateAirTicketFreePosition(Long id, Integer freePosition,Integer nopayReservePosition, Integer soldNopayPosition);
	
	@Query("from ActivityAirTicket where groupCode = ?1 and proCompany = ?2")
	public List<ActivityAirTicket> findGroupCodeByGroupCode(String groupCode,Long companyId);
	
	/**
	 * 更新计算成本状态
	 * @author zhaohaiming
	 * */
	@Modifying
	@Query("update ActivityAirTicket set iscommission =?2 where id=?1")
	public void updateIsCommission(Long id,Integer iscommission);

	@Query("from ActivityAirTicket where  proCompany = ?1")
	public List<ActivityAirTicket> findByCompany(Long id);
}



