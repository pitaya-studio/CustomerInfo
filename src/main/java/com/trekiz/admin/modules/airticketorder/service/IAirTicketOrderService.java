package com.trekiz.admin.modules.airticketorder.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import org.springframework.ui.ModelMap;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.OrderAgentAjax;
import com.trekiz.admin.modules.airticketorder.entity.OrderTravelAjax;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 机票订单处理service
 * @author wl
 *
 */
public interface IAirTicketOrderService {
    
    /**
     * 取消订单
     * @throws Exception 
     */
	String cancelOrder(Long orderId, String description) throws Exception;
	
	/**
	 * @Description 计调撤销订单占位
	 * @author yakun.bai
	 * @Date 2015-11-16
	 */
	String revokeOrder(Long orderId) throws Exception;
	
    /**
     * 删除订单
     */
	String deleteOrder(Long orderId);

	/**
	 * 确认占位
	 * @param orderId
	 * @param request
	 * @return
	 */
	public String confirmOrder(Long orderId, HttpServletRequest request)  throws Exception;
	
    /**
     * 激活订单
     */
    String invokeOrder(Long orderId)  throws Exception;
    /**
     * 锁定订单
     */
    void lockOrder(Long orderId);
    /**
     * 解锁订单
     */
    void unLockOrder(Long orderId);
    
    /**
     * 保存订单
     */
    void saveOrder(AirticketOrder order);
    
	/**
	 * 查询orderpay信息
	 */
    Orderpay getOrderpay(Long payId);
    /**
     * 查询机票订单信息
     */
    AirticketOrder getAirticketorderById(Long orderId);
    /**
     * 清除session中Orderpay的缓存
     * @param object
     * @return
     */
    Object clearObject(Object object);
    
	/**
	 * 根据查询条件查询机票订单列表
	 * @param request
	 * @param response
	 * @param userInfo
	 * @param paramsMap
	 * @param inMap
	 * @return
	 */
	Page<Map<String,Object>> queryAirticketOrderListByCond(HttpServletRequest request, HttpServletResponse response, User userInfo, Map<String,Object> paramsMap,Map<String,Object> inMap);
	
	/**
	 * 根据id查询机票订单的详情
	 * @param orderId
	 * @return
	 */
	Map<String,Object> queryAirticketOrderDetailInfoById(String orderId);
	
	
	/**
	 * 根据id查询机票订单的详情
	 * @param orderId
	 * @return
	 */
	Map<String,Object> queryAirticketOrderDetailInfoByIdRefund(String orderId);
	
	/**
	 * 根据id查询机票订单的详情
	 * by sy 20150908
	 */
	
	Map<String,Object> queryAirticketOrderDetailInfoByIdAddcontacts(String orderId);
	
	/**
	 * 根据订单的id和产品类型查询对应的支付记录信息
	 * @param orderId
	 * @param travelId
	 * @return
	 */
	List<Orderpay> queryOrderpayInfo(Long orderId, Integer prdType);
	
	Map<String,Object> queryAirticketOrderDetailInfoById(String orderId,String travelId);
	
	/**
	 * 查询最大游客id
	 * @param orderTravelAjax
	 * @return
	 */
	String queryAirticketOrderTravelMaxId(OrderTravelAjax orderTravelAjax);
	
	/**
	 * 保存机票订单游客
	 * @param orderTravelAjax
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	boolean saveAirticketOrderTravel(OrderTravelAjax orderTravelAjax) throws NumberFormatException, ParseException;
	
	/**
	 * 更改机票订单游客
	 * @param orderTravelAjax
	 * @return
	 */
	boolean updateAirticketOrderTravel(OrderTravelAjax orderTravelAjax);
	
	/**
	 * 查询机票订单渠道信息
	 * @param agentId
	 * @return
	 */
	Map<String,Object> queryAirticketOrderAgent(String agentId);
	
	/**
	 * 更新机票订单渠道
	 * @param orderAgentAjax
	 * @return
	 */
	boolean updateAirticketOrderAgent(OrderAgentAjax orderAgentAjax);
	
	/**
	 * 查询游客
	 * @param orderId
	 * @return
	 */
	List<Map<String, Object>> queryAirticketOrderTravels(String orderId);
	
	/**
	 * 根据产品id查询机票订单信息
	 * @param productId
	 * @return
	 */
	List<Map<String,Object>> queryAirticketOrdersByProductId(String productId);
	
	List<Map<String,Object>> findSoldNopayPosition(String productId);
	/**
	 * 更新机票订单航班备注
	 * @param orderAgentAjax
	 * @return
	 */
	boolean updateAirticketOrderFlightRemark(OrderAgentAjax orderAgentAjax);
	
	/**
	 * 查询last_insert_id
	 * @return
	 */
	String queryLastInsertId();
	
	/**
	 * 保存上传文件的路径到数据库，
	 * 保存文件和订单的关系
	 * @param srcFileName
	 * @param relativePath
	 * @return
	 */
	boolean saveFilePathAndRelation(String orderId,String srcFileName, String relativePath);
	
	
	/**
	 * 根据机票产品编码查询
	 * @param airticketId
	 * @return
	 */
	List<Map<String, Object>> queryAirtickeByProcode(String procode);
	
	

	/**
	 *机票改签代办
	 * @param airticketId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    Page<Map<String, Object>> queryAirtickeToDo(HttpServletRequest request,HttpServletResponse response,Map condition);
	
	
	void planeReview(Long orderId,Long reviewId,Long tralverId)throws Exception;
	
	@SuppressWarnings("rawtypes")
    public Page<Map<String, Object>> airticketApprovalHistoryList(HttpServletRequest request,HttpServletResponse response,Map condition);
	
	
	
	Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId,String orderId);
	
	/**
	 * 保存备注
	 * @param orderId
	 * @param remark
	 * @return
	 */
	public boolean saveRemark(Long orderId, String remark);
	
	public Map<String, Object> updatepayVoucherFile(
			ArrayList<DocInfo> docInfoList, Orderpay orderPay, String orderId,
			ModelMap mode, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException,
			Exception;
	
	
	
	public List<Map<String, Object>> areaGaiQianCheck(@SuppressWarnings("rawtypes") Map map) ;

	void changeOrderStatus();
	
	/**
	 * 修改未查看订单状态
	 * @param notSeenOrderIdList
	 * @return
	 */
	public Integer changeNotSeenOrderFlag(Set<Long> notSeenOrderIdList);
	
	/**
     * 保存或更新机票订单
     * @author hhx
     * @param airticketOrder
     */
	public void saveOrUpdateAirticketOrder(AirticketOrder airticketOrder);
	
	/**
	 * 更新机票订单
	 * @Description: 
	 * @param @param airticketOrder   
	 * @return void  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-12 下午2:30:22
	 */
	public void updateAirticketOrder(AirticketOrder airticketOrder);

	/**
	 * 机票订单修改，核心业务处理
	 */
	void modifyAirticketOrder(Map<String, String> parameters, String travelers, List<OrderContacts> orderContactsList, List<MoneyAmount>
			orderTotalClearPriceList) throws Exception;

	/**
	 * 机票余位校验
     */
	void validateFreePosition(Map<String, String> parameters) throws Exception;

	/**
	 * 更改机票已确认占位状态
     */
	void handleConfirmStatus(AirticketOrder airticketOrder);

	/**
	 * 根据渠道id查找机票订单信息，并且排除99：已取消， 111：已删除的订单
	 */
	public List<AirticketOrder> findByAgentId(Long agentId);
	
	/*
	 * 根据产品id获取该产品的所有订单id和订单No
	 * @param activityAirticketId  机票产品id
	 * @return 订单id和No的Map列表
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> findOrderIdAndNoByActivityId(Long activityAirticketId);
}
