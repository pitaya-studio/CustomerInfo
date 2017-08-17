package com.trekiz.admin.review.prdreturn.airticket.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.prdreturn.airticket.dao.IAirticketReturnNewDao;
import com.trekiz.admin.review.prdreturn.airticket.service.IAirTicketReturnNewService;

@Service
public class AirTicketReturnNewServiceImpl implements IAirTicketReturnNewService {

	@Autowired
	private IAirticketReturnNewDao airticketReturnDao;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private AirticketActivityReserveDao activityReserveDao;
	
	@Autowired
	private AirticketPreOrderDao airticketPreOrderDao;
	
	@Autowired
	private TravelerDao travelerDao;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;

	/**
	 * 根据订单id查询订单详情内容
	 */
	@Override
	public Map<String, Object> queryAirTicketReturnInfoById(String id) {
		Map<String, Object> orderDetailInfoMap = airticketReturnDao
				.queryAirTicketReturnDetailInfoById(id);
		return orderDetailInfoMap;
	}

	/**
	 * 退票公共方法
	 */
	@Override
	public boolean doReturnPosition(Review review){
		String orderId = review.getOrderId();
		String travelId = review.getTravelerId().toString();
		returnPosition(orderId, travelId);
		return true;
	}
	/**
	 * 还余位
	 */
	@Override
	public int returnPosition(String orderId, String travelId) {
		AirticketOrder airticketOrder = airticketPreOrderDao.findOne(Long.parseLong(orderId));
		ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
		AirticketActivityReserve activityReserve = activityReserveDao.findAgentReserve(airticketOrder.getAirticketId(), airticketOrder.getAgentinfoId());
		Traveler traveler = travelerDao.findOne(Long.parseLong(travelId));
		
        Map<String,String> rMap = orderStatisticsService.getPlaceHolderInfo(airticketOrder.getId(), Context.ORDER_STATUS_AIR_TICKET);
        try {
        	if (null != rMap && null != rMap.get(Context.RESULT)) {
            	String resultP = rMap.get(Context.RESULT);
            	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
            		//当订单占位时归还余位
            		Map<String,String> pMap = orderStatisticsService.saveAirTicketActivityPlaceHolderChange(
            				airticketOrder.getId(), Context.ORDER_STATUS_AIR_TICKET, 1, activityAirTicket);
            		//余位处理失败
            		if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
            			throw new Exception(Context.MESSAGE);
            		}
            		//余位处理成功
            		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
            			
            		} else {
            			throw new Exception("归还余位失败！");
            		}
            	} else if (Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)) {
            		throw new Exception(rMap.get(Context.MESSAGE));
            	}
            } else {
            	throw new Exception("游客退票失败！");
            }
        } catch (Exception e) {
        	
        }
        airticketOrder.setPersonNum(airticketOrder.getPersonNum() - 1);
        airticketPreOrderDao.save(airticketOrder);
		activityAirTicketDao.save(activityAirTicket);
		if(activityReserve != null){
			activityReserveDao.save(activityReserve);
		}
		//更改游客的退票标示 0 代表已退票
		traveler.setIsAirticketFlag("0");
		travelerDao.save(traveler);
		return 0;
	}
}
