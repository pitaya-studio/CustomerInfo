package com.trekiz.admin.modules.stock.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;


public interface IStockDao {

	List<Map<String, Object>> findSoldNopayPosition(String productId);
	
	List<Map<String, Object>> findAirSoldNopayPosition(String productId);
	
	List<Map<String, Object>> findProductGroupOrders(Long id, String status);

	List<Map<String, Object>> findReserveOrder(Long productId, Long agentId);
	
	List<Map<String, Object>> findAirReserveOrder(Long productId, Long agentId);

	List<Map<String, Object>> findCostRecordLog(Long activityId, Integer orderType);
	
	/**
	 * 根据切位类型和对应的 散拼团期ID/机票产品ID 查询切位订单数据（散拼或者机票）
	 * 
	 * @param sourceId 散拼团期ID/机票产品ID
	 * @param reserveType 散拼0,机票1
	 * @return 
	 */
	public List<Map<String,Object>> queryReserveList4ReserveType(Long sourceId,Integer reserveType) ;
	
	/**
	 * 根据团期id集合和批发商id获取切位集合
	 * @Description: 
	 * @param @param activityGroupIds
	 * @param @param agentId
	 * @param @return   
	 * @return List<ActivityGroupReserve>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-9
	 */
	public List<ActivityGroupReserve> getReservesByGroupIds(List<String> activityGroupIds, Long agentId);
}
