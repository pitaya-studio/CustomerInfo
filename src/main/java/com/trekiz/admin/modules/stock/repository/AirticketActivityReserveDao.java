package com.trekiz.admin.modules.stock.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;

/**
 * Created by ZhengZiyu on 2014/11/4.
 */
public interface AirticketActivityReserveDao  extends AirticketActivityReserveDaoCustom,CrudRepository<AirticketActivityReserve, Long> {

	@Query("from AirticketActivityReserve where  agentId=?1 and activityId =?2 ")
    public List<AirticketActivityReserve> findByAgentIdAndActivityId(Long agentId,Long activityId);

	@Query("from AirticketActivityReserve where  activityId =?1 ")
    public List<AirticketActivityReserve> findByActivityId(Long activityId);

    @Query("from AirticketActivityReserve where  activityId =?1 and agentId=?2")
    public AirticketActivityReserve findAgentReserve(Long activityId, Long agentId);

    @Query(value = "SELECT t3.agentName,t1.payReservePosition,t1.leftpayReservePosition,u.name as orderPersonName,t2.order_no as orderNum,t2.total_money as totalMoney,t2.payed_money as payed_money,t2.person_num as orderPersonNum," +
            "t2.order_state as payStatus,t1.remark,t2.airticket_id as orderCompany "
            +"FROM airticketactivityreserve t1,airticket_order t2, agentinfo t3, sys_user u where u.id=t2.create_by and t1.agentId = t2.agentinfo_id and t1.agentId=t3.id and t1.activityId = t2.airticket_id and t2.place_holder_type=1 " +
            "and t1.activityId = ?1 and t2.order_state != 99", nativeQuery = true)
    public List<Map<String, Object>> findSoldNopayPosition(Long activityGroupId);

   // @Query(value = "SELECT t3.agentName,t1.id,t1.payReservePosition,t1.leftpayReservePosition,u.name as orderPersonName,t2.order_no as orderNum,t2.total_money as totalMoney,t2.payed_money as payed_money,t2.person_num as orderPersonNum," +
    //        "t2.order_state as payStatus,t1.remark,t2.airticket_id as orderCompany "
    //        +"FROM airticketactivityreserve t1,airticket_order t2, agentinfo t3, sys_user u where u.id=t2.create_by and t1.agentId = t2.agentinfo_id and t1.agentId=t3.id and t1.activityId = t2.airticket_id and t2.place_holder_type=1 " +
     //       "and t1.activityId = ?1 and t1.agentId=?2 and t2.order_state != 99", nativeQuery = true)
    @Query(value = "SELECT t1.id,t1.payReservePosition,t1.leftpayReservePosition, " +
            "t1.remark FROM airticketactivityreserve t1 where  " +
            " t1.activityId = ?1 and t1.agentId=?2", nativeQuery = true)
    public List<Map<String, Object>> findSoldNopayPosition(Long activityGroupId, Long agentId);

    @Query("FROM AirticketActivityReserve WHERE activityId=?1 and agentId=?2")
    public List<AirticketActivityReserve> findPayReservePositionByActivityId(Long activityId,Long agentId);
    
    /**
     * 根据机票id集合和渠道id信息
     * @Description: 
     * @param @param airTicketIds
     * @param @param agentId
     * @param @return   
     * @return List<AirticketActivityReserve>  
     * @throws
     * @author majiancheng
     * @date 2016-1-9
     */
    @Query("FROM AirticketActivityReserve WHERE activityId in (?1) and agentId=?2")
    public List<AirticketActivityReserve> getReservesByAirTicketIds(List<Long> airTicketIds, Long agentId);
    
//    @Query("FROM AirticketActivityReserve WHERE activityGroupId=?1")
//    public List<AirticketActivityReserve> findPayReserveByGroupId(Long groupId);

//    @Query("from ProductOrderCommon where productGroupId = (select id from ActivityGroup WHERE groupCode=?1 and srcActivityId=?2)")
//    public List<ProductOrderCommon> findProductorderByGroupCode(String groupCode,Integer srcActivityId);

//    @Query("from TravelActivity where id = ?1 ")
//    public List<TravelActivity> findTravelActivityByGroupCode(Long srcActivityId);

//    @Query("from ActivityGroup where groupCode=?1 and srcActivityId=?2")
//    public List<ActivityGroup> findActivityGroupByGroupCode(String groupCode,Integer srcActivityId);
}

interface AirticketActivityReserveDaoCustom extends BaseDao<AirticketActivityReserve> {
}
@Repository
class AirticketActivityReserveDaoImpl extends BaseDaoImpl<AirticketActivityReserve> implements AirticketActivityReserveDaoCustom {

}