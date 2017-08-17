package com.trekiz.admin.modules.review.airticketreturn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.review.airticketreturn.repository.IAirticketReturnDao;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

@Service
public class AirTicketReturnServiceImpl implements IAirTicketReturnService {

	@Autowired
	private IAirticketReturnDao airticketReturnDao;
	
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
	 * 查询退票审核列表 add by chy2014年12月1日16:23:15
	 */
	@Override
	public Page<Map<String, Object>> queryAirticketReturnReviewInfo(HttpServletRequest request, HttpServletResponse response) {
		
		String groupCode = request.getParameter("groupCode");
		if(groupCode == null || "".equals(groupCode.trim())){
			groupCode = null;
		}
		String startTime = request.getParameter("startTime");
		if(startTime == null || "".equals(startTime.trim())){
			startTime = null;
		}
		String endtime = request.getParameter("endTime");
		if(endtime == null || "".equals(endtime.trim())){
			endtime = null;
		}
		String agent = request.getParameter("channel");
		if(agent == null || "".equals(agent.trim())){
			agent = null;
		}
		String saler = request.getParameter("saler");
		if(saler == null || "".equals(saler.trim())){
			saler = null;
		}
		String jdsaler = request.getParameter("meter");
		if(jdsaler == null || "".equals(jdsaler.trim())){
			jdsaler = null;
		}
		String status = request.getParameter("statusChoose");
		if(status == null){
			status = "1";
		}
		if(status != null && "".equals(status.trim())){
			status = null;
		}
		String orderId = request.getParameter("orderId");
		if(orderId == null || "".equals(orderId.trim())){
			orderId = null;
		}
		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(userJobs.size()-1);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);
		if(levels == null || levels.size() == 0){
			return null;
		}
		
		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = airticketReturnDao
				.queryAirticketRetturnReviewList(request, response, groupCode,
						startTime, endtime, agent, saler, jdsaler, status, levels, cOrderBy, uOrderBy, orderId, userJob, reviewCompanyId, subIds);
		// 2 调用review接口查询审核信息
		List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_JP, Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN,
				false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
			String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}
		}
		reviewPage.setList(reviewList);
		return reviewPage;
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
        
        setOrderNum(airticketOrder, traveler);
		activityAirTicketDao.save(activityAirTicket);
		if(activityReserve != null){
			activityReserveDao.save(activityReserve);
		}
		//更改游客的退票标示 0 代表已退票
		traveler.setIsAirticketFlag("0");
		travelerDao.save(traveler);
		return 0;
	}
	
	/**
	 * @Description 扣减订单人数和对应游客类型人数
	 * @author yakun.bai
	 * @Date 2015-12-3
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void setOrderNum(AirticketOrder airticketOrder, Traveler traveler) {
		 /** 扣减订单人数 */
        int personType = traveler.getPersonType();
        switch(personType){
        	case Context.PERSON_TYPE_ADULT:
        		airticketOrder.setAdultNum(airticketOrder.getAdultNum() - 1);
        	break;
        	case Context.PERSON_TYPE_CHILD:
        		airticketOrder.setChildNum(airticketOrder.getChildNum() - 1);
        	break;
        	case Context.PERSON_TYPE_SPECIAL:
        		airticketOrder.setSpecialNum(airticketOrder.getSpecialNum() - 1);
        	break;
        	default:
        		airticketOrder.setAdultNum(airticketOrder.getAdultNum() - 1);
            break;
        }
        airticketOrder.setPersonNum(airticketOrder.getPersonNum() - 1);
        airticketPreOrderDao.save(airticketOrder);
	}
}
