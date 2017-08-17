package com.trekiz.admin.modules.airticketorder.repository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.OrderAgentAjax;
import com.trekiz.admin.modules.airticketorder.entity.OrderTravelAjax;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.traveler.entity.Traveler;

public interface IAirticketOrderDao extends BaseDao<Map<String,Object>> {
	
	/**
	 * 根据查询条件查询机票订单列表
	 * @param request
	 * @param response
	 * @param whereSql
	 * @param condMap
	 * @return
	 */
	Page<Map<String,Object>> queryAirticketOrderListByCond(HttpServletRequest request, HttpServletResponse response,String whereSql,Map<String,Object> condMap);
	
	/**
	 * 根据订单id查询机票订单详情
	 * @param id
	 * @return
	 */
	Map<String,Object> queryAirticketOrderDetailInfoById(String id);
	
	/**
	 * 根据订单id查询机票订单详情(新返佣)
	 * @param id
	 * @return
	 */
	Map<String, Object> queryAirticketOrderDetailInfoByIdNew(String orderId);
	
	/**
	 * 根据订单id查询机票订单详情(新返佣)
	 * @param id
	 * @return
	 */
	Map<String, Object> queryAirticketOrderDetailInfoByIdNewRefund(String orderId);
	
	/**
	 * 根据订单id查询机票订单详情
	 * @param id
	 * @return
	 */
	Map<String,Object> queryAirticketOrderDetailInfoByIdAddcontact(String id);
	
	
	Map<String,Object> queryAirticketOrderDetailInfoById(String id,String travelId);
	
	
	/**
	 * 
	 * 查询所有可用订单列表
	 */
	
	List<AirticketOrder> allAirticketOrderList();
	
	/**
	 * 根据订单id查询该订单最大游客id
	 * @param orderTravelAjax
	 * @return
	 */
	List<Map<String,Object>> queryAirticketOrderTravelMaxId(OrderTravelAjax orderTravelAjax);
	
	/**
	 * 保存机票订单游客
	 * @param orderTravelAjax
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	Integer saveAirticketOrderTravel(OrderTravelAjax orderTravelAjax) throws NumberFormatException, ParseException;
	
	/**
	 * 更新机票订单游客
	 * @param orderTravelAjax
	 * @return
	 */
	Integer updateAirticketOrderTravel(OrderTravelAjax orderTravelAjax);
	
	/**
	 * 根据渠道id查询机票订单渠道
	 * @param agentId
	 * @return
	 */
	List<Map<String,Object>> queryAirticketOrderAgent(String agentId);
	
	/**
	 * 更新机票订单中的航班备注
	 * @param orderAgentAjax
	 * @return
	 */
	boolean updateAirticketOrderFlightRemark(OrderAgentAjax orderAgentAjax);
	
	/**
	 * 更新机票订单中的渠道id
	 * @param orderAgentAjax
	 * @return
	 */
	boolean updateAirticketOrderAgentId(OrderAgentAjax orderAgentAjax);
	
	/**
	 * 更新机票订单中的渠道信息
	 * @param orderAgentAjax
	 * @return
	 */
	boolean updateAirticketOrderAgentInfo(OrderAgentAjax orderAgentAjax);
	
	/**
	 * 根据订单id查询机票订单中的所有游客信息
	 * @param orderId
	 * @return
	 */
	List<Map<String, Object>> queryAirticketOrderTravel(String orderId);
	
	/**
	 * 根据产品id查询机票订单信息
	 * @param productId
	 * @return
	 */
	List<Map<String,Object>> queryAirticketOrdersByProductId(String productId);
	
	List<Map<String,Object>> findSoldNopayPosition(String productId);
	/**
	 * 根据机票产品id查询机票产品中的航段信息
	 * @param airticketId
	 * @return
	 */
	List<Map<String,Object>> queryAirticketOrderFlights(Integer airticketId);
	
	/**
	 * 查询last_insert_id
	 * @return
	 */
	List<Object> queryLastInsertId();
	
	
	/**
	 * 根据ID查询机票订单
	 * @param id
	 * @return
	 */
	public AirticketOrder getAirticketOrderById(Long id);
	
	/**
	 * 保存或更新机票订单
	 * @param id
	 * @return
	 */
	public void saveOrUpdateAirticketOrderById(AirticketOrder airticketOrder);
	
	/**
	 * 保存文件和订单的关系
	 * @param orderId
	 * @param lastInsertId
	 * @return
	 */
	boolean saveDocinfoAndOrderRelation(Long orderId, Long docInfoId);
	
	/**
	 * 根据机票订单id查询订单的附件
	 * @param airticketId
	 * @return
	 */
	List<Map<String, Object>> queryAirticketOrderAttachment(Integer airticketId);
	
	
	/**
	 * 根据机票产品编码查询
	 * @param airticketId
	 * @return
	 */
	List<Map<String, Object>> queryAirtickeByProcode(String procode);
	
	
	Page<Map<String, Object>> queryAirtickeToDo(HttpServletRequest request,HttpServletResponse response,@SuppressWarnings("rawtypes") Map condition);
	
	
	Review queryOneRview(Long reviewId);

	
	ActivityAirTicket queryOneActivityAirTicket(Long id);
	
	Traveler queryoneTravler(Long id);
	
	AirticketOrder queryoneAirticketOrder(Long id);
	
	public Agentinfo queryAgentinfo(Long id);
	
	public List<Map<String, Object>> queryGaiQianRoles(Long companyId) ;
	
	@SuppressWarnings("rawtypes")
    public Page<Map<String, Object>> airticketApprovalHistoryList(HttpServletRequest request,HttpServletResponse response,Map condition);
	
	public void updateairticketorder(String sql);
	
	public Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId);
	
	public boolean updateRemark(Long id, String remark);
	
	public List<Map<String, Object>> areaGaiQianCheck(@SuppressWarnings("rawtypes") Map map) ;
	
	public List<Map<String, Object>> queryAirticketOrderRebates(String orderNo);

	public List<Map<String, Object>> queryAirticketOrderRebatesInf(String orderNo);
	
	/**
	 * 根据订单id获取收入单
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-11 上午11:28:22
	 */
	public Map<String, Object> findMeituIncomeInfoByOrderId(Long orderId);

	public List<Map<String, Object>> queryAirticketOrderNewRebatesInf(String orderNo);
	
	/**
	 * 根据创建人获取该创建人下的所有订单
	 * @Description: 
	 * @param @param createBys
	 * @param @return   
	 * @return List<AirticketOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<AirticketOrder> getAirticketOrderByCreateBys(List<Long> createBys);

	/**
	 * 根据机票产品编号查询该机票产品下的所有机票订单
     */
	List<AirticketOrder> findByAirticketActivityCode(Long activityAirticketId);
	
	/*
	 * 根据产品id获取该产品的所有订单id和订单No
	 * @param activityAirticketId  机票产品id
	 * @return 订单id和No的Map 列表
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> findOrderIdAndNoByActivityId(Long activityAirticketId);
}
