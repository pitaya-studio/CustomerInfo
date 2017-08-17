package com.trekiz.admin.modules.order.airticket.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.traveler.entity.Traveler;

public interface IAirticketPreOrderService {

	/**
	 * 保存机票订单
	 * 
	 * @param order
	 * @return
	 */
	AirticketOrder saveAirticketOrder(ActivityAirTicket activity,
			AirticketOrder order, List<OrderContacts> contactsList,
			List<Traveler> travelerList,List<MoneyAmount> moneyAmounts, JSONArray rebatesObjects ,String reviewId) throws Exception;

	
	/**
	 * 驳回
	 * 
	 * @param orderid,orderpayid
	 * @return
	 */
	
	boolean airticketRejected(Long orderid,Long orderPayId,Boolean flag,String reason);
	
	/**
	 * 撤销
	 * 
	 * @param orderid,orderpayid
	 * @return
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 */
    boolean airticketOrderCancel(Long orderid, Long orderPayId) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception ;
	
	
	/**
	 * 获取产品、渠道切位信息
	 * 
	 * @param activityId
	 * @param agentId
	 * @return
	 */
	AirticketActivityReserve queryAirticketActivityReserve(Long activityId, Long agentId);
	
	/**
	 * 删除游客信息
	 * @param travelerId
	 */
	void deleteTraveler(Long travelerId);
	
	/**
	 * 计划任务
	 */
	void scheduledAirticketOrderService();

	AirticketOrder createAirticketOrder(Long airticketId,ProductOrderCommon po) throws Exception;

	/**
	 * 返还机票子订单余位
	 * productOrderId 主的id
	 * */
	boolean returnFreePosionByProductOrderId(Long productOrderId,Integer personNum);
	

	public List<ActivityAirTicket> findYwByActivityIds(List<Long> activityIdList);
	
	/**
	 * 传入产品编号或订单编号
	 * @author hhx
	 * @return
	 */
	public String genOrderNo(Integer serinum);
	
	/**
	 * 根据机票订单自动生成团号
	 * @Description: 
	 * @param @param order
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-20 下午2:37:02
	 */
	public String genGroupCode(AirticketOrder order);
}
