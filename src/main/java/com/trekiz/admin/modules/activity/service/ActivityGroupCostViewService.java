package com.trekiz.admin.modules.activity.service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCostView;
import com.trekiz.admin.modules.activity.entity.HotelGroupCostView;
import com.trekiz.admin.modules.activity.entity.IslandGroupCostView;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.*;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品出团信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class ActivityGroupCostViewService extends BaseService implements IActivityGroupCostViewService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ActivityGroupViewDao activityGroupViewDao;
	@Autowired
	private ActivityGroupCostViewDao activityGroupCostViewDao;
	
	@Autowired
	private HotelGroupCostViewDao hotelGroupCostViewDao;
	@Autowired
	private IslandGroupCostViewDao islandGroupCostViewDao;
	@Autowired
	private ActivityReserveOrderViewDao activityReserveOrderViewDao;
	@Autowired
	private ActivityReserveOrderDao activityReserveOrderDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserDao userDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
	private ActivityHotelGroupDao hotelGroupDao;
    @Autowired
    private ActivityIslandGroupDao islandGroupDao;
    @Autowired
    private VisaProductsDao visaProductsDao;
    @Autowired
    private ActivityAirTicketDao airTicketDao;
    @Autowired
    private ActivityGroupDao activityGroupDao;
	@Override
	public Page<ActivityGroupCostView> findGroupCostReview(Page<ActivityGroupCostView> page,TravelActivity travelActivity,
			String groupCode,Long supplierId,Long agentId,Integer activityKind,String review,Integer nowLevel, Long companyId,
			Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName) {		
	
		DetachedCriteria dcUser = userDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(createByName)) {
			dcUser.add(Restrictions.like("name", "%" + createByName + "%"));
		}

		List<User> list = userDao.find(dcUser);
		User[] createBy = new User[list.size()];
		for (int i = 0; i < list.size(); i++) {
			createBy[i] = list.get(i);
		}
		
		DetachedCriteria dc = activityGroupCostViewDao.createDetachedCriteria();			
		
		dc.add(Restrictions.eq("activityKind",  activityKind));
		dc.add(Restrictions.eq("proCompany",  companyId));	
		
		if (flowType==15) {
			dc.add(Restrictions.lt("review", 4));	
			dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId)); 
		}else{
			if (!StringUtils.isNotBlank(review)){
				dc.add(Restrictions.lt("payReview", 4));
			} 			
			dc.add(Restrictions.eq("budgetType", 1));
			dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId)); 
		}
		
		if (flowType==15) {
			if(StringUtils.isBlank(review)){	//待当前层级审核 			
				dc.add(Restrictions.eq("nowLevel", nowLevel)); 
				dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中 	
				dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
			    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 			    
			}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
			}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
			}
		}
		if (flowType==18) {
			if(StringUtils.isBlank(review)){	//待当前层级审核 			
				dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
				dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中	
				dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
			    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
			}else if(review.equals("22")){//本人审核通过			   
			    dc.add(Restrictions.gt("payNowLevel", nowLevel));
			}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
			}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
			}
		}
		
		if(StringUtils.isNotBlank(groupCode)) {			
			dc.add(Restrictions.like("groupCode", "%"+ groupCode.trim()+"%"));
		}			
		
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())) {
			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"));
		}			
			
		if(travelActivity.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",travelActivity.getGroupOpenDate()));
		}
		
		if (travelActivity.getGroupCloseDate()!=null) {
			dc.add(Restrictions.le("groupOpenDate",travelActivity.getGroupCloseDate()));
		}
		
		if(supplierId != null) {			
		    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and orderType<6 and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
			dc.add(Restrictions.eq("supplyId", supplierId)); 
			dc.add(Restrictions.eq("supplyType", 0)); 
		}	
		
		if(agentId != null) {			
		    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and orderType<6 and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
			dc.add(Restrictions.eq("supplyId", agentId)); 
			dc.add(Restrictions.eq("supplyType", 1)); 
		}
		
		if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
			dc.add(Restrictions.in("createBy", createBy));
		} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
			return page;
		}
		if (StringUtils.isBlank(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}
		
		return activityGroupCostViewDao.find(page, dc);
	}
	

	//海岛成本审核列表
	@Override
	public Page<IslandGroupCostView> findIslandCostReview(Page<IslandGroupCostView> page,ActivityIslandGroup activityIslandGroup, 
			Long supplierId,Long  agentId,String review,Integer nowLevel, Long companyId,
			Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName) {		
	
		DetachedCriteria dcUser = userDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(createByName)) {
			//dcUser.add(Restrictions.like("name", "%" + createByName + "%"));
		}

		List<User> list = userDao.find(dcUser);
		User[] createBy = new User[list.size()];
		for (int i = 0; i < list.size(); i++) {
			createBy[i] = list.get(i);
		}
		
		DetachedCriteria dc = islandGroupCostViewDao.createDetachedCriteria();			
		
		dc.add(Restrictions.eq("wholesalerId",  companyId));		
		if (flowType==15) {
			dc.add(Restrictions.lt("review", 4));	
			dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId)); 
		}
		else{
			if ( ! StringUtils.isNotBlank(review)){
				dc.add(Restrictions.lt("payReview", 4));
			} 			
			dc.add(Restrictions.eq("budgetType", 1));
			dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId)); 
		}
		
		
		if (flowType==15) {
			 if(StringUtils.isBlank(review)){	//待当前层级审核 			
				dc.add(Restrictions.eq("nowLevel", nowLevel)); 
				dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中 	
				dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
			    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 			    
			}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
			}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
			 }
			}
		if (flowType==18) {
			if(StringUtils.isBlank(review)){	//待当前层级审核 			
				dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
				dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中	
				dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
			}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
			    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
			}else if(review.equals("22")){//本人审核通过			   
			    dc.add(Restrictions.gt("payNowLevel", nowLevel));
			}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
			}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
			 }
			}
		
		
		
		if(StringUtils.isNotBlank(activityIslandGroup.getGroupCode())) {			
		 dc.add(Restrictions.like("groupCode", activityIslandGroup.getGroupCode().trim()+"%"));
		}			
		/*
		if(StringUtils.isNotBlank(travelIsland.getAcitivityName())) {
			dc.add(Restrictions.like("acitivityName", "%"+travelIsland.getAcitivityName()+"%"));
		}			
		*/
		
		if(activityIslandGroup.getGroupOpenDate()!=null ) {
		dc.add(Restrictions.ge("groupOpenDate",activityIslandGroup.getGroupOpenDate()));
		}
		/*
		if (activityIslandGroup.getGroupCloseDate()!=null) {
			dc.add(Restrictions.le("groupOpenDate",activityIslandGroup.getGroupCloseDate()));
		}
       */
		
		if(supplierId != null) {			
		    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
			dc.add(Restrictions.eq("supplyId", supplierId)); 
			dc.add(Restrictions.eq("supplyType", 0)); 
		}	
		
		if(agentId != null) {			
		    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
			dc.add(Restrictions.eq("supplyId", agentId)); 
			dc.add(Restrictions.eq("supplyType", 1)); 
		}
		/*
		if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
			dc.add(Restrictions.in("createBy", createBy));
		} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
			return page;
		} */
		
		return islandGroupCostViewDao.find(page, dc);
	}

	
	//海岛付款审核列表
		@Override
		public Page<IslandGroupCostView> findIslandPayReview(Page<IslandGroupCostView> page,ActivityIslandGroup activityIslandGroup, 
				String activityName,String hotelName,String islandName,String currencyId,String startCurrency,String endCurrency,Long supplierId,Long  agentId,String review,Integer nowLevel, Long companyId,
				Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName) {		
			
			DetachedCriteria dc = islandGroupCostViewDao.createDetachedCriteria();		
			
			dc.add(Restrictions.eq("wholesalerId",  companyId));	
			
			if (StringUtils.isNotBlank(createByName)) {
				//dcUser.add(Restrictions.like("name", "%" + createByName + "%"));
			}
			if(StringUtils.isNotBlank(activityName)) {			
				 dc.add(Restrictions.like("activityName", activityName.trim()+"%"));
				}
			
			if(StringUtils.isNotBlank(islandName)) {			
				 dc.add(Restrictions.like("islandName", islandName.trim()+"%"));
				}
			
			if(StringUtils.isNotBlank(createByName)) {			
				 dc.add(Restrictions.like("createByName",createByName.trim()+"%"));
				}	
			
			if(StringUtils.isNotBlank(currencyId)) {
				 dc.add(Restrictions.eq("currencyId", Integer.valueOf(currencyId)));
				if(StringUtils.isNotBlank(startCurrency)) {
					BigDecimal bd = new BigDecimal(startCurrency);
					 dc.add(Restrictions.gt("totalPrice",bd));
				}
              if(StringUtils.isNotBlank(endCurrency)) {
           	   BigDecimal bd = new BigDecimal(endCurrency);
           	   dc.add(Restrictions.lt("totalPrice",bd));
				}
			}	
				
			if (flowType==15) {
				dc.add(Restrictions.lt("review", 4));	
				dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId)); 
			}
			else{
				if ( ! StringUtils.isNotBlank(review)){
					dc.add(Restrictions.lt("payReview", 4));
				} 			
				dc.add(Restrictions.eq("budgetType", 1));
				dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId)); 
			}
			
			
			if (flowType==15) {
				 if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("nowLevel", nowLevel)); 
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中 	
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 			    
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
				 }
				}
			if (flowType==18) {
				if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中	
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
				}else if(review.equals("22")){//本人审核通过			   
				    dc.add(Restrictions.gt("payNowLevel", nowLevel));
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
				 }
				}
			
			
			
			if(StringUtils.isNotBlank(activityIslandGroup.getGroupCode())) {			
			 dc.add(Restrictions.like("groupCode", "%" +activityIslandGroup.getGroupCode().trim()+"%"));
			}			
			/*
			if(StringUtils.isNotBlank(travelIsland.getAcitivityName())) {
				dc.add(Restrictions.like("acitivityName", "%"+travelIsland.getAcitivityName()+"%"));
			}			
			*/
			
			if(activityIslandGroup.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",activityIslandGroup.getGroupOpenDate()));
			}
			/*
			if (activityIslandGroup.getGroupCloseDate()!=null) {
				dc.add(Restrictions.le("groupOpenDate",activityIslandGroup.getGroupCloseDate()));
			}
	       */
			
			if(supplierId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", supplierId)); 
				dc.add(Restrictions.eq("supplyType", 0)); 
			}	
			
			if(agentId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", agentId)); 
				dc.add(Restrictions.eq("supplyType", 1)); 
			}
			/*
			if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
				dc.add(Restrictions.in("createBy", createBy));
			} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
				return page;
			} */
			
			return islandGroupCostViewDao.find(page, dc);
		}
	
	
	//酒店成本审核列表
		@Override
		public Page<HotelGroupCostView> findHotelCostReview(Page<HotelGroupCostView> page,ActivityHotelGroup activityHotelGroup, 
				Long supplierId,Long  agentId,String review,Integer nowLevel, Long companyId,
				Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName) {		
		
			
			DetachedCriteria dc = hotelGroupCostViewDao.createDetachedCriteria();
			
			dc.add(Restrictions.eq("wholesalerId",  companyId));
			
			if(StringUtils.isNotBlank(activityHotelGroup.getGroupCode())) {			
				 dc.add(Restrictions.like("groupCode", activityHotelGroup.getGroupCode().trim()+"%"));
				}			
			
			if (flowType==15) {
				dc.add(Restrictions.lt("review", 4));	
				dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId)); 
			}
			else{
				if ( ! StringUtils.isNotBlank(review)){
					dc.add(Restrictions.lt("payReview", 4));
				} 			
				dc.add(Restrictions.eq("budgetType", 1));
				dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId)); 
			}
			
			
			if (flowType==15) {
				 if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("nowLevel", nowLevel)); 
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中 	
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 			    
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
				 }
				}
			if (flowType==18) {
				if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中	
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
				}else if(review.equals("22")){//本人审核通过			   
				    dc.add(Restrictions.gt("payNowLevel", nowLevel));
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
				 }
				}
				
			
					
			/*
			if(StringUtils.isNotBlank(travelHotel.getAcitivityName())) {
				dc.add(Restrictions.like("acitivityName", "%"+travelHotel.getAcitivityName()+"%"));
			}			
			*/
				
				if(activityHotelGroup.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",activityHotelGroup.getGroupOpenDate()));
			}
			/*
			if (activityIslandGroup.getGroupCloseDate()!=null) {
				dc.add(Restrictions.le("groupOpenDate",activityIslandGroup.getGroupCloseDate()));
			}
	       */
			
			if(supplierId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", supplierId)); 
				dc.add(Restrictions.eq("supplyType", 0)); 
			}	
			
			if(agentId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", agentId)); 
				dc.add(Restrictions.eq("supplyType", 1)); 
			}
			
			if(StringUtils.isNotBlank(createByName)) {			
				 dc.add(Restrictions.like("createByName",createByName.trim()+"%"));
				}	
			/*
			List<User> list = userDao.find(dcUser);
			User[] createBy = new User[list.size()];
			for (int i = 0; i < list.size(); i++) {
				createBy[i] = list.get(i);
			}
		
			
			if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
				dc.add(Restrictions.in("createBy", createBy));
			} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
				return page;
			} 	*/	
			return hotelGroupCostViewDao.find(page, dc);
		}
		
		//酒店付款审核列表
		@Override
		public Page<HotelGroupCostView> findHotelPayReview(Page<HotelGroupCostView> page,ActivityHotelGroup activityHotelGroup, 
				String activityName,String hotelName,String islandName,String currencyId,String startCurrency,String endCurrency,Long supplierId,Long  agentId,String review,Integer nowLevel, Long companyId,
				Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName) {		
		
			
			DetachedCriteria dc = hotelGroupCostViewDao.createDetachedCriteria();
			
			dc.add(Restrictions.eq("wholesalerId",  companyId));
			
			if(StringUtils.isNotBlank(activityHotelGroup.getGroupCode())) {			
				 dc.add(Restrictions.like("groupCode", "%" + activityHotelGroup.getGroupCode().trim()+"%"));
				}	
			if(StringUtils.isNotBlank(activityName)) {			
				 dc.add(Restrictions.like("activityName", activityName.trim()+"%"));
				}
			
			if(StringUtils.isNotBlank(islandName)) {			
				 dc.add(Restrictions.like("islandName", islandName.trim()+"%"));
				}
			
			if(StringUtils.isNotBlank(currencyId)) {
				 dc.add(Restrictions.eq("currencyId", Integer.valueOf(currencyId)));
				if(StringUtils.isNotBlank(startCurrency)) {
					BigDecimal bd = new BigDecimal(startCurrency);
					 dc.add(Restrictions.gt("totalPrice",bd));
				}
               if(StringUtils.isNotBlank(endCurrency)) {
            	   BigDecimal bd = new BigDecimal(endCurrency);
            	   dc.add(Restrictions.lt("totalPrice",bd));
				}
			}
			
				
			if (flowType==15) {
				dc.add(Restrictions.lt("review", 4));	
				dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId)); 
			}
			else{
				if ( ! StringUtils.isNotBlank(review)){
					dc.add(Restrictions.lt("payReview", 4));
				} 			
				dc.add(Restrictions.eq("budgetType", 1));
				dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId)); 
			}
			
				
			if (flowType==15) {
				 if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("nowLevel", nowLevel)); 
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中 	
					dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS)); 			    
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL)); 
				 }
				}
			if (flowType==18) {
				if(StringUtils.isBlank(review)){	//待当前层级审核 			
					dc.add(Restrictions.eq("payNowLevel", nowLevel)); 
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_WAIT.toString())){	//审核中	
					dc.add(Restrictions.eq("payReview",Context.REVIEW_COST_WAIT)); 
				}else if(review.equals(Context.REVIEW_COST_PASS.toString())){//已经通过			   
				    dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
				}else if(review.equals("22")){//本人审核通过			   
				    dc.add(Restrictions.gt("payNowLevel", nowLevel));
				}else if (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL)); 
				}else if (review.equals(Context.REVIEW_COST_CANCEL.toString())){//已取消				
					dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL)); 
				 }
				}
			 	
			
					
			/*
			if(StringUtils.isNotBlank(travelHotel.getAcitivityName())) {
				dc.add(Restrictions.like("acitivityName", "%"+travelHotel.getAcitivityName()+"%"));
			}			
			*/
				
				if(activityHotelGroup.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",activityHotelGroup.getGroupOpenDate()));
			}
			/*
			if (activityIslandGroup.getGroupCloseDate()!=null) {
				dc.add(Restrictions.le("groupOpenDate",activityIslandGroup.getGroupCloseDate()));
			}
	       */
			
			if(supplierId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", supplierId)); 
				dc.add(Restrictions.eq("supplyType", 0)); 
			}	
			
			if(agentId != null) {			
			    //dc.add(Restrictions.sqlRestriction("{alias}.agpId in (select activityId  from cost_record where supplyId =(?) and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
				dc.add(Restrictions.eq("supplyId", agentId)); 
				dc.add(Restrictions.eq("supplyType", 1)); 
			}
			
			if(StringUtils.isNotBlank(createByName)) {			
				 dc.add(Restrictions.like("createByName",createByName.trim()+"%"));
				}	
		
			return hotelGroupCostViewDao.find(page, dc);
		}
		
	/**
	 * 团队管理生成应收总金额，到账金额，已付金额的SQL
	 * @param moneyType          金额类型，13：totlaMoney ,5： payedMoney ,4：accountedMoney
	 * @param entity
	 * @param orderType	团队类型
	 * @param companyId	供应商ID
	 * @return	String
	 * @author zhaohaiming
	 */
	public String createMoneySql(int moneyType,GroupManagerEntity entity,Integer orderType,Long companyId){
		StringBuffer sql = new StringBuffer("SELECT currencyId, sum(amount) amount FROM (");
		String flag = "";
		String where = getWhereSQL(entity,orderType);
		if(Context.MONEY_TYPE_YS == moneyType){
			flag = "o.payed_money";
		}else if(Context.MONEY_TYPE_DZ == moneyType){
			flag = "o.accounted_money";
		}else if(Context.MONEY_TYPE_YSH == moneyType){
			flag = "o.total_money";
		}
		if(Context.ORDER_TYPE_ALL == orderType){ // 未分类（全部订单）
			sql.append("SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" g.createBy AS operatorId,")
				.append(" p.acitivityName productName,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ) salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" (ma.amount * ma.exchangerate-IFNULL(m.amount* m.exchangerate,0)) amount,")
				.append(" p.activity_kind orderType,")
				.append(" g.groupOpenDate, p.deptId")
				.append(" FROM")
				.append(" productorder o LEFT JOIN money_amount m ON m.serialNum = o.differenceMoney,")
				.append(" activitygroup g,")
				.append(" travelactivity p,")
				.append(" money_amount ma")
				.append(" WHERE")
				.append(" o.productId = p.id")
				.append(" AND o.productGroupId = g.id")
				.append(" AND p.id = g.srcActivityId")
				.append(" AND ma.serialNum = ").append(flag)
				.append(" AND o.payStatus IN (1, 2, 3, 4, 5)")
				.append(" AND o.delFlag = '0'")
				.append(" AND g.delFlag = 0")
				.append(" AND p.activityStatus = 2")
				.append(" AND p.delFlag = 0")
				.append(" AND p.proCompany = ").append(companyId)
				.append(" AND p.activity_kind in (1, 2, 3, 4, 5, 10)")
				.append(" AND ma.moneyType = ").append(moneyType)
				.append(" UNION ALL ")
				.append(" SELECT")
				.append(" p.iscommission,")
				.append(" p.group_code AS groupCode,")
				.append(" p.createBy AS operatorId,")
				.append(" ' ' AS productName,")
				.append(" ( SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, airticket_order o WHERE su.id = o.salerId AND o.airticket_id = p.id ) salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" 7 AS orderType,")
				.append(" p.outTicketTime groupOpenDate, p.deptId")
				.append(" FROM")
				.append(" activity_airticket p,")
				.append(" airticket_order o,")
				.append(" money_amount ma")
				.append(" WHERE  o.airticket_id = p.id")
				.append(" AND ma.serialNum = ").append(flag)
				.append(" AND p.productStatus = 2")
				.append(" AND p.proCompany = ").append(companyId)
				.append(" AND p.delFlag = 0")
				.append(" AND o.del_flag = '0'")
				.append(" AND o.order_state NOT IN (99, 111)")
				.append(" AND ma.moneyType = ").append(moneyType)
				.append(" AND ma.orderType = 7")
				.append(" UNION ALL ")
				.append(" SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" g.createBy operatorId,")
				.append(" p.activityName productName,")
				.append(" o.salerId salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" 12 orderType,")
				.append(" '' groupOpenDate, '' deptId")
				.append(" FROM")
				.append(" island_order o,")
				.append(" activity_island_group g,")
				.append(" activity_island p,")
				.append(" island_money_amount ma")
				.append(" WHERE")
				.append(" o.activity_island_group_uuid = g.uuid")
				.append(" AND g.activity_island_uuid = p.uuid")
				.append(" AND  ma.serialNum =").append(flag)
				.append(" AND o.delFlag = 0")
				.append(" AND (o.orderStatus <> 3 OR o.payStatus <> 99) ")
				.append(" AND g.delFlag = 0")
				.append(" AND g.`status` = 1")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
				.append(" AND ma.businessType = 1")
				.append(" AND ma.moneyType = ").append(moneyType)
				.append(" UNION ALL ")
				.append(" SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" g.createBy operatorId,")
				.append(" p.activityName productName,")
				.append(" o.salerId salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" 11 orderType,")
				.append(" '' groupOpenDate, '' deptId")
				.append(" FROM")
				.append(" hotel_order o,")
				.append(" activity_hotel_group g,")
				.append(" activity_hotel p,")
				.append(" hotel_money_amount ma")
				.append(" WHERE")
				.append(" o.activity_hotel_group_uuid = g.uuid")
				.append(" AND g.activity_hotel_uuid = p.uuid")
				.append(" AND  ma.serialNum =").append(flag)
				.append(" AND o.delFlag = 0")
				.append(" AND (o.orderStatus <> 3 OR o.payStatus <> 99) ")
				.append(" AND g.delFlag = 0")
				.append(" AND g.`status` = 1")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
				.append(" AND ma.businessType = 1")
				.append(" AND ma.moneyType = ").append(moneyType);
		}else if(Context.ORDER_TYPE_JP == orderType){ // 机票
			sql.append("SELECT")
				.append(" p.iscommission,")
				.append(" p.group_code AS groupCode,")
				.append(" p.createBy AS operatorId,")
				.append(" ' ' AS productName,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, airticket_order o WHERE su.id = o.salerId AND o.airticket_id = p.id ) salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" p.outTicketTime groupOpenDate, p.deptId")
				.append(" FROM")
				.append(" activity_airticket p,")
				.append(" airticket_order o,")
				.append(" money_amount ma")
				.append(" WHERE ")
				.append(" o.airticket_id = p.id")
				.append(" AND ma.serialNum = ").append(flag)
				.append(" AND p.productStatus = 2")
				.append(" AND p.proCompany = ").append(companyId)
				.append(" AND p.delFlag = 0")
				.append(" AND o.del_flag = '0'")
				.append(" AND o.order_state NOT IN (99, 111)")
				.append(" AND ma.moneyType = ").append(moneyType)
				.append(" AND ma.orderType = ").append(orderType);
		}else if(Context.ORDER_TYPE_QZ == orderType){ // 签证
			sql.append("SELECT")
				.append(" p.iscommission,")
				.append(" p.groupCode,")
				.append(" p.createBy AS operatorId,")
				.append(" p.productName,")
				.append(" o.salerId AS salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" p.createDate groupOpenDate, p.deptId")
				.append(" FROM")
				.append(" visa_products p,")
				.append(" visa_order o,")
				.append(" money_amount ma")
				.append(" WHERE o.visa_product_id = p.id ")
				.append(" AND ma.serialNum = ").append(flag)
				.append(" AND productStatus = 2")
				.append(" AND p.proCompanyId = ").append(companyId)
				.append(" AND p.delFlag = 0")
				.append(" AND o.del_flag = '0'")
				.append(" AND o.payStatus NOT IN (99, 111)")
				.append(" AND o.visa_order_status <> 100")
				.append(" AND ma.moneyType = ").append(moneyType)
				.append(" AND ma.orderType = ").append(orderType);
		}else if(Context.ORDER_TYPE_HOTEL == orderType){ // 酒店
			sql.append(" SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" g.createBy operatorId,")
				.append(" p.activityName productName,")
				.append(" o.salerId salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" 11 orderType")
				.append(" FROM")
				.append(" hotel_order o,")
				.append(" activity_hotel_group g,")
				.append(" activity_hotel p,")
				.append(" hotel_money_amount ma")
				.append(" WHERE")
				.append(" o.activity_hotel_group_uuid = g.uuid")
				.append(" AND g.activity_hotel_uuid = p.uuid")
				.append(" AND  ma.serialNum =").append(flag)
				.append(" AND o.delFlag = 0")
				.append(" AND (")
				.append(" o.orderStatus <> 3")
				.append(" OR o.payStatus <> 99")
				.append(" )")
				.append(" AND g.delFlag = 0")
				.append(" AND g.`status` = 1")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
				.append(" AND ma.businessType = 1")
				.append(" AND ma.moneyType = ")
				.append(moneyType);
		}else if(Context.ORDER_TYPE_ISLAND == orderType){ // 海岛游
			sql.append(" SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" g.createBy operatorId,")
				.append(" p.activityName productName,")
				.append(" o.salerId salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate amount,")
				.append(" 12 orderType")
				.append(" FROM")
				.append(" island_order o,")
				.append(" activity_island_group g,")
				.append(" activity_island p,")
				.append(" island_money_amount ma")
				.append(" WHERE")
				.append(" o.activity_island_group_uuid = g.uuid")
				.append(" AND g.activity_island_uuid = p.uuid")
				.append(" AND  ma.serialNum =").append(flag)
				.append(" AND o.delFlag = 0")
				.append(" AND (")
				.append(" o.orderStatus <> 3")
				.append(" OR o.payStatus <> 99")
				.append(" )")
				.append(" AND g.delFlag = 0")
				.append(" AND g.`status` = 1")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
				.append(" AND ma.businessType = 1")
				.append(" AND ma.moneyType = ")
				.append(moneyType);
		}else{ // 团期类
			sql.append("SELECT")
				.append(" g.iscommission,")
				.append(" g.groupCode,")
				.append(" p.acitivityName productName,")
				.append(" g.createBy AS operatorId,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ) salerId,")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" ma.amount * ma.exchangerate- IFNULL(m.amount,0)* IFNULL(m.exchangerate,0) amount,")
				.append(" p.activity_kind orderType,")
				.append(" g.groupOpenDate, p.deptId")
				.append(" FROM")
				.append(" productorder o LEFT JOIN money_amount m ON o.differenceMoney = m.serialNum,")
				.append(" activitygroup g,")
				.append(" travelactivity p,")
				.append(" money_amount ma")
				.append(" WHERE")
				.append(" o.productId = p.id")
				.append(" AND o.productGroupId = g.id")
				.append(" AND	p.id = g.srcActivityId")
				.append(" AND ma.serialNum = ")
				.append(flag)
				.append(" AND o.payStatus IN (1, 2, 3, 4, 5)")
				.append(" AND o.delFlag = '0'")
				.append(" AND g.delFlag = 0")
				.append(" AND p.activityStatus = 2")
				.append(" AND p.delFlag = 0")
				.append(" AND p.proCompany = ").append(companyId)
				.append(" AND p.activity_kind = ").append(orderType)
				.append(" AND ma.moneyType = ").append(moneyType);
		}
		sql.append(" ) a WHERE 1 = 1").append(where).append(" GROUP BY currencyId ");
		return sql.toString();
	}

	/**
	 * 团队管理列表
	 */
	@Override
	public Page<Map<String, Object>> findGroup(Page<Map<String, Object>> page, GroupManagerEntity entity,
			Integer orderType, Long companyId) {
		StringBuffer strs = new StringBuffer();
		String where = getWhereSQL(entity, orderType);
		if(0 == orderType && !"散拼".equals(entity.getSaler())){
			strs.append(getAllSql(companyId, where));
		}else if(Context.ORDER_TYPE_QZ == orderType){
			strs.append("SELECT iscommission, productId, groupCode, productName, operatorName, operatorId, saler,")
			    .append(" salerId, createDate, updateDate, lockStatus, forcastStatus, orderType,")
			    .append(" DATE_FORMAT(groupOpenDate,'%Y-%m-%d') groupOpenDate, personNum, deptId FROM ")
			    .append(" (SELECT p.iscommission, p.id AS productId, p.groupCode, p.productName, ")
			    .append(" (SELECT name FROM sys_user su WHERE su.id = p.createBy) AS operatorName, p.createBy AS operatorId, ")
			    .append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, visa_order o WHERE su.id = o.salerId AND o.visa_product_id = p.id")
			    .append(" AND o.payStatus NOT IN (99,111) AND o.visa_order_status<>100 AND o.del_flag = '0' ) as saler,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, visa_order o WHERE su.id = o.salerId AND o.visa_product_id = p.id")
				.append(" AND o.payStatus NOT IN (99,111) AND o.visa_order_status<>100 AND o.del_flag = '0' ) as salerId,")
			    .append(" p.createDate, p.updateDate, p.lockStatus, p.forcastStatus, 6 orderType, p.createDate groupOpenDate,")
			    .append(" (SELECT SUM(vo.travel_num) from visa_order vo where vo.visa_product_id=p.id and vo.del_flag='0' ")
				.append(" AND vo.payStatus NOT IN (99,111) AND vo.visa_order_status<>100 GROUP BY p.id ) personNum, p.deptId")
			    .append(" FROM visa_products p WHERE p.productStatus = 2")
			    .append(" AND p.proCompanyId = ").append(companyId).append(" AND p.delFlag = 0 ) A ")
			    .append(" WHERE 1=1 ").append(where);
		}else if(Context.ORDER_TYPE_JP == orderType){
			strs.append("SELECT iscommission, productId, groupCode, productName, operatorName, operatorId, saler,")
				.append(" salerId, createDate, updateDate, lockStatus, forcastStatus, orderType,")
				.append(" DATE_FORMAT(groupOpenDate,'%Y-%m-%d') groupOpenDate, personNum, deptId FROM ")
			    .append(" (SELECT t.iscommission, t.productId, t.groupCode, CONCAT(departureCity, '-', arrivedCity, ':', airType) AS productName,")
			    .append(" operatorName, operatorId, saler, salerId, createDate, updateDate, lockStatus, forcastStatus, orderType, ")
				.append(" groupOpenDate, personNum, deptId FROM ")
				.append(" (SELECT p.iscommission, p.id AS productId, p.group_code AS groupCode, ")
			    .append(" CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END AS airType,")
			    .append(" IFNULL((SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = p.departureCity),' ') AS departureCity,")
			    .append(" IFNULL((SELECT name FROM sys_area area WHERE delFlag = '0' AND area.id = p.arrivedCity),' ') AS arrivedCity,")
			    .append(" (SELECT name FROM sys_user su WHERE su.id = p.createBy) AS operatorName, p.createBy AS operatorId,")
			    .append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, airticket_order o where su.id = o.salerId AND o.airticket_id = p.id ")
				.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS saler,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, airticket_order o where su.id = o.salerId AND o.airticket_id = p.id ")
				.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS salerId,")
			    .append(" p.createDate, p.updateDate, p.lockStatus, p.forcastStatus, 7 orderType, outTicketTime AS groupOpenDate, ")
			    .append(" (SELECT SUM(o.person_num) FROM airticket_order o WHERE o.airticket_id = p.id AND o.del_flag=0 ")
				.append(" AND o.order_state NOT IN (99, 111)) personNum, deptId ")
				.append(" FROM activity_airticket p WHERE p.productStatus = 2 ")
			    .append(" AND p.proCompany = ").append(companyId).append(" AND p.delFlag = 0 ) t ")
			    .append(" ) A ").append(" where 1=1 ").append(where);
		}else if(Context.ORDER_TYPE_ISLAND == orderType){
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId, a.saler,")
				.append(" a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType, ")
				.append(" a.groupOpenDate, a.personNum, a.deptId, a.productUuid, a.uuid activityUuid FROM ")
				.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
				.append(" (SELECT NAME FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, island_order o WHERE su.id = o.salerId AND o.activity_island_group_uuid = g.uuid")
				.append(" AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, island_order o WHERE su.id = o.salerId AND o.activity_island_group_uuid = g.uuid")
				.append(" AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
				.append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, 12 as orderType, g.groupOpenDate,")
				.append(" (SELECT SUM(o.orderPersonNum) FROM island_order o WHERE g.uuid = o.activity_island_group_uuid AND o.delFlag='0' ")
				.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid ")
				.append(" FROM activity_island_group g,	activity_island p WHERE	p.uuid = g.activity_island_uuid AND p.wholesaler_id = ")
				.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		}else if(Context.ORDER_TYPE_HOTEL == orderType){
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
				.append(" a.saler, a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType, ")
				.append(" a.groupOpenDate, a.personNum, a.deptId, a.productUuid, a.uuid activityUuid FROM ")
				.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
				.append(" (SELECT name FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
				.append("  AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
				.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
				.append(" AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
				.append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, 11 AS orderType, g.groupOpenDate, ")
				.append(" (SELECT SUM(o.orderPersonNum) FROM hotel_order o WHERE g.uuid = o.activity_hotel_group_uuid AND o.delFlag='0' ")
				.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid ")
				.append(" FROM activity_hotel_group g, activity_hotel p WHERE p.uuid = g.activity_hotel_uuid AND p.wholesaler_id = ")
				.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		}else{
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
				.append(" a.saler, a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType,")
				.append(" a.groupOpenDate, a.personNum, a.deptId FROM ")
			    .append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.acitivityName productName, p.id AS productId, ")
			    .append(" (SELECT NAME FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy as operatorId,")
			    .append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
				.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') saler, ")
				.append("(SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
				.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') salerId,")
			    .append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, p.activity_kind orderType, g.groupOpenDate,")
			    .append(" (SELECT SUM(o.orderPersonNum) FROM productorder o WHERE g.id = o.productGroupId AND o.delFlag=0 ")
				.append(" AND o.payStatus NOT IN (99,111)) AS personNum, p.deptId FROM activitygroup g, travelactivity p ")
			    .append(" WHERE p.id = g.srcActivityId AND p.activityStatus = 2 AND p.delFlag = 0 AND g.delFlag = 0 ")
				.append(" AND p.proCompany = ").append(companyId);
			    if(orderType != 0) {
				    strs.append(" AND p.activity_kind = ").append(orderType);
			    }
			    strs.append(" ) a ").append(" WHERE 1=1 ").append(where);
		}
		String order = page.getOrderBy();
		if(StringUtils.isBlank(order)){
			page.setOrderBy("createDate DESC ");
		}
		return activityGroupViewDao.findBySql(page, strs.toString(), Map.class);
	}

	@Override
	public Integer getTotalPerson(GroupManagerEntity entity, String orderS,
			Long companyId) {
		Integer orderType = null;
		if(orderS != null) {
			orderType = Integer.parseInt(orderS);
		}
		StringBuffer str = new StringBuffer();
		String where = getWhereSQL(entity, orderType);
		if(StringUtils.isNotBlank(entity.getIscommission())){
			 where = getWhereSQL(entity, orderType);
		}
		if(0 == orderType && !"散拼".equals(entity.getSaler())){
			str.append(getAllPerson(companyId, where));
		}else if(Context.ORDER_TYPE_QZ == orderType){
			str.append("select sum(totalPerson) totalPerson,groupCode,productName,operatorId,salerId,iscommission,deptId,groupOpenDate")
				.append(" from (")
				.append("select o.travel_num totalPerson, v.groupCode groupCode, v.productName, v.createBy operatorId,")
				.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,visa_order o WHERE su.id = o.salerId) salerId,")
				.append(" v.iscommission,v.deptId,v.createDate groupOpenDate from visa_products v, visa_order o")
				.append(" where v.id=o.visa_product_id and v.productStatus = 2 and v.delFlag=0 and o.del_flag=0 and o.payStatus NOT IN (99, 111) AND o.visa_order_status <> 100")
				.append(" and v.proCompanyId=").append(companyId).append(") t where 1=1");
			str.append(where);
		}else if(Context.ORDER_TYPE_JP == orderType){
			str.append("select sum(totalPerson) totalPerson,groupCode,productName,operatorId,salerId,iscommission,deptId,groupOpenDate")
				.append(" from (")
				.append("select o.person_num totalPerson, a.group_code groupCode, ")
				.append(" CONCAT(ifnull((SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = a.departureCity), ' '), ")
				.append(" '-', ")
				.append(" ifnull((SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = a.arrivedCity), ' '), ")
				.append(" ':', ")
				.append(" CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END) AS productName, ")
				.append(" a.createBy operatorId,")
				.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,airticket_order o WHERE su.id = o.salerId) salerId,")
				.append(" a.iscommission,a.deptId,a.outTicketTime groupOpenDate from activity_airticket a, airticket_order o")
				.append(" where a.id=o.airticket_id and a.productStatus = 2 and a.delFlag=0 and o.del_flag=0 and o.order_state not in (99,111)")
				.append(" and a.proCompany=").append(companyId).append(") t where 1=1");
			str.append(where);
		}else{
			str.append("select sum(totalPerson) totalPerson,groupCode,productName,operatorId,salerId,iscommission,deptId,groupOpenDate")
				.append(" from (")
				.append("select o.orderPersonNum totalPerson, g.groupCode, p.acitivityName productName, g.createBy operatorId,")
				.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,productorder o WHERE su.id = o.salerId	AND o.productGroupId = g.id) salerId,")
				.append(" g.iscommission,p.deptId,g.groupOpenDate from travelactivity p, activitygroup g, productorder o")
				.append(" where p.id = g.srcActivityId and g.id=o.productGroupId and p.activityStatus = 2 and p.delFlag=0 and g.delFlag=0 and o.delFlag=0 and o.payStatus not in (99,111)")
				.append(" and p.proCompany=").append(companyId);
			if(orderType != 0) {
				str.append(" and p.activity_kind = ").append(orderType);
			}
			str.append(") t where 1=1");
			str.append(where);
		}
		
		List<Map<String, Object>> totalPerson = activityGroupViewDao.findBySql(str.toString(), Map.class);
		if(totalPerson != null && totalPerson.get(0).get("totalPerson") != null) {
			return Integer.parseInt(totalPerson.get(0).get("totalPerson").toString());
		}else{
			return 0;
		}
		
	}
	
	/**
	 * 团队管理--全部
	 * @param companyId
	 * @param where
	 * @return
	 */
	private StringBuffer getAllSql(Long companyId, String where) {
		StringBuffer strs = new StringBuffer();
		//单团类
		strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
			.append(" a.saler, a.salerId, a.createDate, a.updateDate, a.orderType, a.lockStatus, a.forcastStatus,")
			.append(" a.groupOpenDate, a.personNum, a.deptId, ' ' productUuid, ' ' activityUuid FROM ")
			.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode,	p.acitivityName productName, p.id AS productId,")
			.append(" (SELECT name FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
			.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') saler, ")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
			.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') salerId,")
			.append(" p.createDate, p.updateDate, p.activity_kind AS orderType, g.lockStatus, g.forcastStatus, g.groupOpenDate, ")
			.append(" (SELECT SUM(o.orderPersonNum) FROM productorder o WHERE g.id = o.productGroupId AND o.delFlag='0' ")
			.append(" AND o.payStatus NOT IN (99, 111)) AS personNum, p.deptId FROM activitygroup g, travelactivity p ")
			.append(" WHERE	p.id = g.srcActivityId AND p.activityStatus = 2	AND p.proCompany = ").append(companyId)
			.append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		strs.append(" UNION ");
		//机票
		strs.append("SELECT iscommission, -1 AS groupId, groupCode, productName, productId, operatorName, operatorId, ")
			.append(" saler, salerId, createDate, updateDate, orderType, lockStatus, forcastStatus,")
			.append(" groupOpenDate, personNum, deptId, ' ' productUuid, ' ' activityUuid FROM ")
		    .append(" (SELECT iscommission, groupCode, CONCAT(departureCity, '-', arrivedCity, ':', airType) AS productName,")
		    .append(" productId, operatorName, operatorId, saler, salerId, createDate, updateDate, lockStatus, forcastStatus, ")
			.append(" orderType, DATE_FORMAT(groupOpenDate,'%Y-%m-%d') groupOpenDate, personNum, deptId FROM ")
			.append(" (SELECT p.iscommission, p.id AS productId, p.group_code AS groupCode, ")
		    .append("  CASE p.airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END AS airType,")
		    .append(" IFNULL((SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = p.departureCity),' ') AS departureCity,")
		    .append(" IFNULL((SELECT name FROM sys_area area WHERE delFlag = '0' AND area.id = p.arrivedCity),' ') AS arrivedCity,")
		    .append(" (SELECT name FROM sys_user su WHERE su.id = p.createBy) AS operatorName, p.createBy AS operatorId,")
		    .append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, airticket_order o WHERE su.id = o.salerId AND o.airticket_id = p.id ")
			.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS saler,")
			.append("(SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, airticket_order o WHERE su.id = o.salerId AND o.airticket_id = p.id ")
			.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS salerId,")
			.append(" p.createDate, p.updateDate, '7' as orderType, p.lockStatus, p.forcastStatus, p.outTicketTime groupOpenDate, ")
			.append(" (SELECT SUM(o.person_num) FROM airticket_order o WHERE o.airticket_id = p.id AND o.del_flag=0 ")
			.append(" AND o.order_state NOT IN (99, 111)) personNum, p.deptId FROM ")
			.append(" activity_airticket p WHERE p.productStatus = 2 AND p.delFlag = 0")
		    .append(" and p.proCompany = ").append(companyId).append(" ) t ) A ")
		    .append(" where 1=1 ").append(where);
		strs.append(" UNION ");
		//酒店
		strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
			.append("  a.saler, a.salerId, a.createDate, a.updateDate, orderType, a.lockStatus, a.forcastStatus,")
			.append(" a.groupOpenDate, personNum, a.deptId, a.productUuid, activityUuid FROM ")
			.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
			.append(" (SELECT name FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
			.append(" AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
			.append(" AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
			.append(" p.createDate, p.updateDate, 11 AS orderType, g.lockStatus, g.forcastStatus, g.groupOpenDate, ")
			.append("(SELECT SUM(o.orderPersonNum) FROM hotel_order o WHERE g.uuid = o.activity_hotel_group_uuid AND o.delFlag='0' ")
			.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid AS activityUuid FROM ")
			.append(" activity_hotel_group g, activity_hotel p WHERE p.uuid = g.activity_hotel_uuid AND p.wholesaler_id=")
			.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		strs.append(" UNION ");
		//海岛游
		strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
			.append("  a.saler, a.salerId, a.createDate, a.updateDate, orderType, a.lockStatus, a.forcastStatus,")
			.append(" a.groupOpenDate, personNum, a.deptId, a.productUuid, activityUuid FROM ")
			.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
			.append(" (SELECT name FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, island_order o WHERE su.id = o.salerId ")
			.append(" AND o.activity_island_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
			.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, island_order o WHERE su.id = o.salerId ")
			.append(" AND o.activity_island_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
			.append(" p.createDate, p.updateDate, 12 AS orderType, g.lockStatus, g.forcastStatus, g.groupOpenDate,")
			.append(" (SELECT SUM(o.orderPersonNum) FROM island_order o WHERE g.uuid = o.activity_island_group_uuid AND o.delFlag='0'")
			.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid AS activityUuid FROM ")
			.append(" activity_island_group g, activity_island p WHERE p.uuid = g.activity_island_uuid AND p.wholesaler_id=")
			.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		return strs;
	}
	
	private StringBuffer getAllPerson(Long companyId, String where) {
		StringBuffer str = new StringBuffer();

		str.append("select sum(totalPerson) totalPerson,groupCode,productName,operatorId,salerId,iscommission,deptId,groupOpenDate")
			.append(" from (")
			.append(" select o.orderPersonNum totalPerson, g.groupCode, p.acitivityName productName, g.createBy operatorId,")
			.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,productorder o WHERE su.id = o.salerId	AND o.productGroupId = g.id) salerId,")
			.append(" g.iscommission,p.deptId,g.groupOpenDate from travelactivity p, activitygroup g, productorder o")
			.append(" where p.id = g.srcActivityId and g.id=o.productGroupId and p.activityStatus = 2 and p.delFlag=0 and g.delFlag=0 and o.delFlag=0 and o.payStatus not in (99,111)")
			.append(" and p.proCompany=").append(companyId)
			.append(" UNION all")
//			.append(" select o.travel_num totalPerson, v.groupCode groupCode, v.productName, v.createBy operatorId,")
//			.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,visa_order o WHERE su.id = o.salerId) salerId,")
//			.append(" v.iscommission,v.deptId,v.createDate groupOpenDate from visa_products v, visa_order o")
//			.append(" where v.id=o.visa_product_id and v.productStatus = 2 and v.delFlag=0 and o.del_flag=0 and o.payStatus NOT IN (99, 111) AND o.visa_order_status <> 100")
//			.append(" and v.proCompanyId=").append(companyId)
//			.append(" UNION")
			.append(" select o.person_num totalPerson, a.group_code groupCode, ")
			.append(" CONCAT(ifnull((SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = a.departureCity), ' '), ")
			.append(" '-', ")
			.append(" ifnull((SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = a.arrivedCity), ' '), ")
			.append(" ':', ")
			.append(" CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END) AS productName, ")
			.append(" a.createBy operatorId,")
			.append(" (SELECT	GROUP_CONCAT(DISTINCT su.id) FROM	sys_user su,airticket_order o WHERE su.id = o.salerId) salerId,")
			.append(" a.iscommission,a.deptId,a.outTicketTime groupOpenDate from activity_airticket a, airticket_order o")
			.append(" where a.id=o.airticket_id and a.productStatus = 2 and a.delFlag=0 and o.del_flag=0 and o.order_state not in (99,111)")
			.append(" and a.proCompany=").append(companyId).append(") t where 1=1").append(where);
	
		return str;
	}
	
	@Override
	public Page<Map<String, Object>> findGroupDetail(Page<Map<String, Object>> page, GroupManagerEntity entity, 
			Integer orderType, Long companyId) {
		StringBuffer str = new StringBuffer();
		if(orderType == Context.ORDER_TYPE_ISLAND) { // 海岛游
			str.append("SELECT groupCode, supplyName, NAME,	quantity, currencyName,	currencyMark, COMMENT, money, orderType, activity_uuid, supplyId, supplyType")
				.append(" FROM (SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME,	quantity, ")
				.append(" (SELECT currency_name	FROM currency cny WHERE	cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyName,")
				.append(" orderType, activity_uuid, supplyId, supplyType, COMMENT, ")
				.append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyMark,")
				.append(" FORMAT(ifnull(price, 0) * quantity, 2) AS money FROM cost_record_island c	WHERE review = 2 AND budgetType = 1	AND delFlag = '0'")
				.append(" UNION	SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME, quantity, ")
				.append(" (SELECT currency_name	FROM currency cny WHERE	cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyName,")
				.append(" orderType, activity_uuid, supplyId, supplyType, COMMENT, FORMAT(ifnull(price, 0) * quantity, 2) AS money, ")
				.append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyMark ")
				.append(" FROM cost_record_island c	WHERE budgetType = 1 AND delFlag = '0' AND reviewStatus NOT IN ('已取消', '已驳回')) tt ")
				.append(" WHERE activity_uuid = '").append(entity.getActivityUuid()).append("' AND orderType = ").append(orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) { // 酒店
			str.append("SELECT groupCode, supplyName, NAME,	quantity, currencyName,	currencyMark, COMMENT, money, orderType, activity_uuid, supplyId, supplyType")
				.append(" FROM (SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME, quantity, ")
				.append(" (SELECT currency_name	FROM currency cny WHERE	cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyName,")
				.append(" orderType, activity_uuid, supplyId, supplyType, COMMENT,")
				.append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyMark,")
				.append(" FORMAT(ifnull(price, 0) * quantity, 2) AS money FROM cost_record_hotel c	WHERE review = 2 AND budgetType = 1	AND delFlag = '0'")
				.append(" UNION	SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME, quantity, ")
				.append(" (SELECT currency_name	FROM currency cny WHERE	cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyName,")
				.append(" orderType, activity_uuid, supplyId, supplyType, COMMENT, FORMAT(ifnull(price, 0) * quantity, 2) AS money, ")
				.append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ").append(companyId).append(") AS currencyMark ")
				.append(" FROM cost_record_hotel c	WHERE budgetType = 1 AND delFlag = '0' AND reviewStatus NOT IN ('已取消', '已驳回')) tt ")
				.append(" WHERE activity_uuid = '").append(entity.getActivityUuid()).append("' AND orderType = ").append(orderType);
		} else { // 除酒店和海岛游以外的其他订单
			str.append("SELECT groupCode,supplyName,NAME,quantity,currencyName,currencyMark,comment,money,orderType,activityId,supplyId,supplyType FROM (")
			   .append("SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME, quantity,")
			   .append(" (SELECT currency_name FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ")
			   .append(companyId).append(" ) AS currencyName,orderType,activityId,supplyId,supplyType,")
			   .append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ")
			   .append(companyId).append(" ) AS currencyMark,comment, FORMAT(ifnull(price, 0) * quantity, 2) as money ")
			   .append(" FROM cost_record c WHERE review = 2 and budgetType = 1 and delFlag= '0' ")
			   .append(" UNION ")
			   .append(" SELECT '").append(entity.getGroupCode()).append("' AS groupCode, supplyName, NAME, quantity,")
			   .append(" (SELECT currency_name FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ")
			   .append(companyId).append(" ) AS currencyName,orderType,activityId,supplyId,supplyType, ")
			   .append(" (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId AND cny.create_company_id = ")
			   .append(companyId).append(" ) AS currencyMark, comment, ")
			   .append(" FORMAT(ifnull(price, 0) * quantity, 2) as money FROM cost_record c WHERE budgetType = 1 and delFlag= '0' ")
			   .append(" AND reviewStatus not in ('已取消','已驳回')) tt ");
			str.append(" WHERE activityId = ").append(entity.getProductId()).append(" and orderType = ").append(orderType);
		}
		
		if(null != entity.getSupplyId()){
			str.append(" AND supplyId =").append(entity.getSupplyId())
			   .append(" AND supplyType = 0");
		}
		if(null != entity.getAgentId()){
			str.append(" AND supplyId =").append(entity.getAgentId())
			   .append(" AND supplyType = 1");
		}
		
		return activityGroupViewDao.findBySql(page, str.toString(), Map.class);
	}
	
	private String getWhereSQL(GroupManagerEntity entity, Integer orderType){
		StringBuffer where = new StringBuffer();
		String groupCode = entity.getGroupCode();
		String productName = entity.getProductName();
		Integer operatorId = entity.getOperatorId();
		Integer salerId = entity.getSalerId();
		String iscommission = entity.getIscommission();
		Integer deptId = entity.getDeptId();
		String groupOpenDate = entity.getGroupOpenDate();
		String groupCloseDate = entity.getGroupCloseDate();

		if(StringUtils.isNotBlank(groupCode)){
			where.append(" and groupCode like '%").append(groupCode).append("%'");
		}
		if(StringUtils.isNotBlank(productName)){
			where.append(" and productName like '%").append(productName).append("%'");	
		}
		if(null != operatorId){
			where.append(" and operatorId = ").append(operatorId);	
		}
		if(null != salerId) {
			where.append(" and salerId like '%").append(salerId).append("%'");
		}
		if(StringUtils.isNotBlank(iscommission)){
			where.append(" and iscommission = ").append(iscommission);
		}
		if(null != deptId) {
			where.append(" and deptId =").append(deptId);
		}
		if(StringUtils.isNotBlank(groupOpenDate)){
			where.append(" and groupOpenDate >= '").append(groupOpenDate).append("'");
		}
		if(StringUtils.isNotBlank(groupCloseDate)){
			where.append(" and groupOpenDate <= '").append(groupCloseDate).append("'");
		}
		return where.toString();
	}


	@Override
	public List<Map<String, Object>> getMoneySum(int moneyType,GroupManagerEntity entity,Integer orderType,Long companyId) {
		String sql = createMoneySql(moneyType, entity, orderType, companyId);
		return activityGroupViewDao.findBySql(sql,Map.class);
	}


	@Override
	public String getMoneySumStr(List<Map<String,Object>> list) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		StringBuffer s = new StringBuffer();
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Map<String,Object> map = list.get(i);
				Currency c = currencyDao.findID(Long.valueOf(map.get("currencyId").toString()),companyId);
				BigDecimal num = (BigDecimal) map.get("amount");
				
				int flag = num.compareTo(new BigDecimal(0)); //和零比较大小-1表示小于,0是等于,1是大于.
				String amount = MoneyNumberFormat.getThousandsMoney(Double.valueOf(num.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO);
				if(i==size-1){
					if(c != null && StringUtils.isNotBlank(c.getCurrencyMark())){
						if(amount != null){
							s.append(c.getCurrencyMark()).append(amount);
						}
					}else{
						if(amount != null){
							if(flag == -1){
								sb.append(amount);
							}else if(flag == 1){
								s.append(amount);
							}
						}
					}
				}else{
					if(c != null && StringUtils.isNotBlank(c.getCurrencyMark())){
						if(amount != null){
							s.append(c.getCurrencyMark()).append(amount).append("+");
						}
					}else{
						if(amount != null){
							if(flag== -1){
								sb.append(amount);
							}else if(flag ==1){
								s.append(amount).append("+");
							}
						}
					}
				}
			}
		}
		String moneyStr =  s.append(sb).toString();
		
		return moneyStr;
	}

	/**
	 * 团队管理--应付总额
	 * @param entity
	 * @param orderType
	 * @param companyId
	 * @return
	 */
	public String createCostTotal(GroupManagerEntity entity, Integer orderType, Long companyId){
		StringBuffer sb = new StringBuffer();
		StringBuffer pro = new StringBuffer();
		if(orderType == Context.ORDER_TYPE_ALL){
			String productName = entity.getProductName();
			String groupCode = entity.getGroupCode();
			Integer operatorId = entity.getOperatorId();
			String iscommission = entity.getIscommission();
			StringBuffer jp = new StringBuffer();
			StringBuffer dt = new StringBuffer();
			StringBuffer hotel = new StringBuffer();
			StringBuffer island = new StringBuffer();
			Integer salerId = entity.getSalerId();
			if(salerId != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
				jp.append("(select DISTINCT p.id from (SELECT")
				   .append(" pro.id,")
				   .append(" pro.iscommission,")
				   .append(" pro.group_code AS groupCode,")
				   .append(" pro.createBy,")
				   .append(" CONCAT( ifnull(( SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = pro.departureCity ), ' ' ), '-', ifnull(( SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = pro.arrivedCity ), ' ' ), ':', CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END ) AS productName,")
				   .append(" o.salerId as create_by ,")
				   .append(" pro.outTicketTime,pro.deptId")
				   .append(" FROM")
				   .append(" activity_airticket pro,")
				   .append(" airticket_order o")
				   .append(" WHERE")
				   .append(" o.airticket_id = pro.id")
				   .append(" AND pro.productStatus = 2")
				   .append(" AND pro.proCompany = ").append(companyId)
				   .append(" AND pro.delFlag = 0")
				   .append(" AND o.del_flag = '0'")
				   .append(" AND o.order_state NOT IN (99, 111)) p")  
				   .append(" where p.create_by=").append(salerId);
			}else{
				jp.append("(select DISTINCT p.id from (SELECT")
					.append(" pro.id,")
					.append(" pro.iscommission,")
					.append(" pro.group_code AS groupCode,")
					.append(" pro.createBy,")
					.append(" CONCAT( ifnull(( SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = pro.departureCity ), ' ' ), '-', ifnull(( SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = pro.arrivedCity ), ' ' ), ':', CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END ) AS productName,")
					.append(" pro.outTicketTime,pro.deptId")
					.append(" FROM")
					.append(" activity_airticket pro")
					.append(" WHERE")
					.append(" pro.productStatus = 2")
					.append(" AND pro.proCompany = ").append(companyId)
					.append(" AND pro.delFlag = 0 ) p where 1=1");
			}
			
			if(StringUtils.isNotBlank(productName)){
				jp.append(" AND p.productName like '%").append(productName).append("%'");
			}
			if(StringUtils.isNotBlank(groupCode)){
				jp.append(" AND p.groupCode like '%").append(groupCode).append("%'");
			}
			if(operatorId != null){//计调
				jp.append(" AND p.createBy =").append(operatorId);
			}
			if(StringUtils.isNotBlank(iscommission)){
				jp.append(" AND p.iscommission = ").append(iscommission);
			}
			String groupOpenDate = entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				jp.append(" AND p.outTicketTime >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				jp.append(" AND p.outTicketTime <='").append(groupCloseDate).append("'");
			}
			Integer deptId = entity.getDeptId();
			if(deptId != null) {
				jp.append(" AND p.deptId =").append(deptId);
			}
			jp.append(") cp");
			
			
			dt.append("(SELECT DISTINCT g.id FROM");
			if(entity.getSalerId() != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
				dt.append(" productorder o,")
					.append(" activitygroup g,")
					.append(" travelactivity p")
					.append(" WHERE")
					.append(" o.productGroupId = g.id")
					.append(" and g.srcActivityId =	p.id")
					.append(" AND p.activityStatus = 2")
					.append(" AND p.delFlag = 0")
					.append(" AND p.proCompany = ")
					.append(companyId)
					.append(" AND o.salerId =")
					.append(entity.getSalerId());
			}else{
				dt.append(" activitygroup g,")
				.append(" travelactivity p")
				.append(" WHERE")
				.append(" g.srcActivityId =	p.id")
				.append(" AND p.activityStatus = 2")
				.append(" AND p.delFlag = 0")
				.append(" AND p.proCompany = ")
				.append(companyId);
			}
			
			if(StringUtils.isNotBlank(productName)){
				dt.append(" AND p.acitivityName like '%").append(productName).append("%'");
			}
			if(StringUtils.isNotBlank(groupCode)){
				dt.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			if(operatorId != null){//计调
				dt.append(" AND g.createBy =").append(operatorId);
			}
			
			if(StringUtils.isNotBlank(iscommission)){
				dt.append(" AND g.iscommission = ").append(iscommission);
			}
//			String groupOpenDate = entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				dt.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
//			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				dt.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
//			Integer deptId = entity.getDeptId();
			if(deptId != null) {
				dt.append(" AND p.deptId =").append(deptId);
			}
			dt.append(") pro");
			
			hotel.append(" SELECT DISTINCT g.uuid FROM")
				.append(" hotel_order o,")
				.append(" activity_hotel_group g,")
				.append(" activity_hotel p")
				.append(" WHERE")
				.append(" 	o.activity_hotel_group_uuid = g.uuid")
				.append(" AND g.activity_hotel_uuid = p.uuid")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId);
			if(StringUtils.isNotBlank(productName)){
				hotel.append(" AND p.activityName like '%").append(productName).append("%'");
			}
			if(StringUtils.isNotBlank(groupCode)){
				hotel.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			if(operatorId != null){//计调
				hotel.append(" AND g.createBy =").append(operatorId);
			}
			if(salerId != null){
				hotel.append(" AND o.salerId =").append(salerId);
			}
			if(StringUtils.isNotBlank(groupOpenDate)) {
				hotel.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
			if(StringUtils.isNotBlank(groupCloseDate)) {
				hotel.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
			if(StringUtils.isNotBlank(iscommission)){
				hotel.append(" AND g.iscommission = ").append(iscommission);
			}
			hotel.append(") pro");
			
			island.append(" SELECT DISTINCT g.uuid FROM")
				.append(" island_order o,")
				.append(" activity_island_group g,")
				.append(" activity_island p")
				.append(" WHERE")
				.append(" 	o.activity_island_group_uuid = g.uuid")
				.append(" AND g.activity_island_uuid = p.uuid")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId);
			if(StringUtils.isNotBlank(productName)){
				island.append(" AND p.activityName like '%").append(productName).append("%'");
			}
			if(StringUtils.isNotBlank(groupCode)){
				island.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			if(operatorId != null){//计调
				island.append(" AND g.createBy =").append(operatorId);
			}
			if(salerId != null){
				island.append(" AND o.salerId =").append(salerId);
			}
			
			if(StringUtils.isNotBlank(groupOpenDate)) {
				island.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
			if(StringUtils.isNotBlank(groupCloseDate)) {
				island.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
			if(StringUtils.isNotBlank(iscommission)){
				island.append(" AND g.iscommission = ").append(iscommission);
			}
			island.append(") pro");
			
			sb.append("select currencyId, amount from ( select currencyId,sum(amount) amount from (select ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" from  cost_record c,")
//			sb.append("select currencyId,sum(amount) amount from (select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(jp)
				.append(" where c.activityId = cp.id ")
				.append(" AND c.budgetType = 0 AND c.orderType=7 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag = '0'")
				.append(" union all")
				.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(jp)
				.append(" where c.activityId = cp.id")
				.append(" AND c.budgetType = 0 AND c.orderType=7 AND c.reviewType = 2 AND c.reviewStatus <> '已取消' AND c.reviewStatus <> '已驳回' AND c.delFlag = '0'");
			sb.append(" union all");
			sb.append(" select ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" from  cost_record c,")
//			sb.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(dt)
				.append(" where c.activityId = pro.id ")
				.append(" AND c.orderType in (1,2,3,4,5,10) AND c.budgetType = 0 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag = '0'")
				.append(" union all")
				.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(dt)
				.append(" where c.activityId = pro.id")
				.append(" AND c.orderType in (1,2,3,4,5,10) AND c.budgetType = 1 AND c.reviewType = 2 AND c.reviewStatus <> '已取消' AND c.reviewStatus <> '已驳回' AND c.delFlag = '0') aa group by currencyId");
			sb.append(" union all");
			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" FROM cost_record_hotel c, (")
//			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_hotel c, (")
				.append(hotel)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 11 AND budgetType = 0 AND reviewType = 0 AND review = 2 AND delFlag = '0'")
				.append(" union all")
				.append(" SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_hotel c, (")
				.append(hotel)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 11 AND budgetType = 1 AND reviewType = 2 AND reviewStatus <> '已取消' AND reviewStatus <> '已驳回' AND delFlag = '0') aa group by currencyId");
			sb.append(" union all");
			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" FROM cost_record_island c, (")
//			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_island c, (")
				.append(island)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 12 AND budgetType = 0 AND reviewType = 0 AND review = 2 AND delFlag = '0'")
				.append(" union all")
				.append(" SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_island c, (")
				.append(island)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 12 AND budgetType = 1 AND reviewType = 2 AND reviewStatus <> '已取消' AND reviewStatus <> '已驳回' AND delFlag = '0') aa group by currencyId) total group by currencyId");
		
		}else if(orderType == Context.ORDER_TYPE_JP){
			Integer salerId = entity.getSalerId();
			if(salerId != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
				pro.append("(select DISTINCT p.id from (SELECT")
				   .append(" pro.id,")
				   .append(" pro.iscommission,")
				   .append(" pro.group_code AS groupCode,")
				   .append(" pro.createBy,")
				   .append(" CONCAT( ifnull(( SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = pro.departureCity ), ' ' ), '-', ifnull(( SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = pro.arrivedCity ), ' ' ), ':', CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END ) AS productName,")
				   .append(" o.salerId as create_by,")
				   .append(" pro.outTicketTime,pro.deptId")
				   .append(" FROM")
				   .append(" activity_airticket pro,")
				   .append(" airticket_order o")
				   .append(" WHERE")
				   .append(" o.airticket_id = pro.id")
				   .append(" AND pro.productStatus = 2")
				   .append(" AND pro.proCompany = ").append(companyId)
				   .append(" AND pro.delFlag = 0")
				   .append(" AND o.del_flag = '0'")
				   .append(" AND o.order_state NOT IN (99, 111)) p")  
				   .append(" where p.create_by=").append(salerId);
			}else{
				pro.append("(select DISTINCT p.id from (SELECT")
					.append(" pro.id,")
					.append(" pro.iscommission,")
					.append(" pro.group_code AS groupCode,")
					.append(" pro.createBy,")
					.append(" CONCAT( ifnull(( SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = pro.departureCity ), ' ' ), '-', ifnull(( SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = pro.arrivedCity ), ' ' ), ':', CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END ) AS productName,")
					.append(" pro.outTicketTime, pro.deptId")
					.append(" FROM")
					.append(" activity_airticket pro")
					.append(" WHERE")
					.append(" pro.productStatus = 2")
					.append(" AND pro.proCompany = ").append(companyId)
					.append(" AND pro.delFlag = 0 ) p where 1=1");
			}
			String productName = entity.getProductName();
			if(StringUtils.isNotBlank(productName)){
				pro.append(" AND p.productName like '%").append(productName).append("%'");
			}
			String groupCode = entity.getGroupCode();
			if(StringUtils.isNotBlank(groupCode)){
				pro.append(" AND p.groupCode like '%").append(groupCode).append("%'");
			}
			Integer operatorId = entity.getOperatorId();
			if(operatorId != null){//计调
				pro.append(" AND p.createBy =").append(operatorId);
			}
			String iscommission = entity.getIscommission();
			if(StringUtils.isNotBlank(iscommission)){
				pro.append(" AND p.iscommission = ").append(iscommission);
			}
			String groupOpenDate = entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				pro.append(" AND p.outTicketTime >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				pro.append(" AND p.outTicketTime <='").append(groupCloseDate).append("'");
			}
			Integer deptId = entity.getDeptId();
			if(deptId != null) {
				pro.append(" AND p.deptId =").append(deptId);
			}
			pro.append(") cp");
			sb.append("select currencyId,sum(amount) amount from (select ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" from  cost_record c,")
//			sb.append("select currencyId,sum(amount) amount from (select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = cp.id AND c.ordertype = ")
				.append(orderType)
				.append(" AND c.budgetType = 0 AND c.orderType=7 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag = '0'")
				.append(" union all")
				.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = cp.id AND c.ordertype = ")
				.append(orderType)
				.append(" AND c.budgetType = 0 AND c.orderType=7 AND  c.reviewType = 2 AND c.reviewStatus <> '已取消' AND c.reviewStatus <> '已驳回' AND c.delFlag = '0') aa group by currencyId");
		}else if(orderType == Context.ORDER_TYPE_QZ){
			pro.append(" (SELECT DISTINCT p.id FROM");
			if(entity.getSalerId() != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
				pro.append(" visa_products p,")
				   .append(" visa_order o")
				   .append(" WHERE")
				   .append(" p.id = o.visa_product_id")
				   .append(" AND productStatus = 2")
				   .append(" AND p.proCompanyId = ")
				   .append(companyId)
				   .append(" AND p.delFlag = 0")
				   .append(" AND o.del_flag = 0")
				   .append(" AND o.payStatus NOT IN (99, 111)")
				   .append(" AND o.visa_order_status <> 100")
				   .append(" AND o.salerId=")
				   .append(entity.getSalerId());
				
			}else{
				pro.append(" visa_products p")
					.append(" WHERE")
					.append(" productStatus = 2")
					.append(" AND p.proCompanyId = ")
					.append(companyId)
					.append(" AND p.delFlag = 0");
			}
			String productName = entity.getProductName();
			if(StringUtils.isNotBlank(productName)){
				pro.append(" AND p.productName like '%").append(productName).append("%'");
			}
			String groupCode = entity.getGroupCode();
			if(StringUtils.isNotBlank(groupCode)){
				pro.append(" AND p.groupCode like '%").append(groupCode).append("%'");
			}
			Integer operatorId = entity.getOperatorId();
			if(operatorId != null){//计调
				pro.append(" AND p.createBy =").append(operatorId);
			}
			String iscommission = entity.getIscommission();
			if(StringUtils.isNotBlank(iscommission)){
				pro.append(" AND p.iscommission = ").append(iscommission);
			}
			String groupOpenDate = entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				pro.append(" AND p.createDate >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				pro.append(" AND p.createDate <='").append(groupCloseDate).append("'");
			}
			Integer deptId = entity.getDeptId();
			if(deptId != null) {
				pro.append(" AND p.deptId =").append(deptId);
			}
			pro.append(") pro");
			
			sb.append("select currencyId,sum(amount) amount from (select ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" from  cost_record c,")
//			sb.append("select currencyId,sum(amount) amount from (select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = pro.id AND c.ordertype = ")
				.append(orderType)
				.append(" AND c.budgetType = 0 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag = '0'")
				.append(" union all")
				.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = pro.id AND c.ordertype = ")
				.append(orderType)
				.append(" AND c.budgetType = 0 AND c.reviewType = 2 AND c.reviewStatus <> '已取消' AND c.reviewStatus <> '已驳回' AND c.delFlag = '0') aa group by currencyId");
		}else if(orderType == Context.ORDER_TYPE_HOTEL){
			if(entity.getSalerId() != null){
			//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
			pro.append(" SELECT DISTINCT g.uuid FROM")
				.append(" hotel_order o,")
				.append(" activity_hotel_group g,")
				.append(" activity_hotel p")
				.append(" WHERE")
				.append(" 	o.activity_hotel_group_uuid = g.uuid")
				.append(" AND g.activity_hotel_uuid = p.uuid")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
				.append(" AND o.salerId = ").append(entity.getSalerId());
			}else{
				pro.append(" SELECT DISTINCT g.uuid FROM")
					.append(" activity_hotel_group g,")
					.append(" activity_hotel p")
					.append(" WHERE")
					.append(" g.activity_hotel_uuid = p.uuid")
					.append(" AND p.delFlag = 0")
					.append(" AND p.wholesaler_id = ").append(companyId);
			}
			String productName = entity.getProductName();
			if(StringUtils.isNotBlank(productName)){
				pro.append(" AND p.acitivityName like '%").append(productName).append("%'");
			}
			String groupCode = entity.getGroupCode();
			if(StringUtils.isNotBlank(groupCode)){
				pro.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			Integer operatorId = entity.getOperatorId();
			if(operatorId != null){//计调
				pro.append(" AND g.createBy =").append(operatorId);
			}
			String groupOpenDate =  entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				pro.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				pro.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
			String iscommission = entity.getIscommission();
			if(StringUtils.isNotBlank(iscommission)){
				pro.append(" AND g.iscommission = ").append(iscommission);
			}
			pro.append(") pro");
			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" FROM cost_record_hotel c, (")
//			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_hotel c, (")
				.append(pro)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 1 AND budgetType = 0 AND reviewType = 0 AND review = 2 AND delFlag = '0'")
				.append(" union all")
				.append(" SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_hotel c, (")
				.append(pro)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 1 AND budgetType = 1 AND reviewType = 2 AND reviewStatus <> '已取消' AND reviewStatus <> '已驳回' AND delFlag = '0') aa group by currencyId");
		}else if(orderType == Context.ORDER_TYPE_ISLAND){
			if(entity.getSalerId() != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
			pro.append(" SELECT DISTINCT g.uuid FROM")
			   .append(" island_order o,")
			   .append(" activity_island_group g,")
				.append(" activity_island p")
				.append(" WHERE")
				.append(" 	o.activity_island_group_uuid = g.uuid")
				.append(" AND g.activity_island_uuid = p.uuid")
				.append(" AND p.delFlag = 0")
				.append(" AND p.wholesaler_id = ").append(companyId)
			    .append(" AND o.salerId = ").append(entity.getSalerId());
			}else{
				pro.append(" SELECT DISTINCT g.uuid FROM")
					.append(" activity_island_group g,")
					.append(" activity_island p")
					.append(" WHERE")
					.append(" g.activity_island_uuid = p.uuid")
					.append(" AND p.delFlag = 0")
					.append(" AND p.wholesaler_id = ").append(companyId);
			}
			String productName = entity.getProductName();
			if(StringUtils.isNotBlank(productName)){
				pro.append(" AND p.acitivityName like '%").append(productName).append("%'");
			}
			String groupCode = entity.getGroupCode();
			if(StringUtils.isNotBlank(groupCode)){
				pro.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			Integer operatorId = entity.getOperatorId();
			if(operatorId != null){//计调
				pro.append(" AND g.createBy =").append(operatorId);
			}
			String groupOpenDate =  entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				pro.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				pro.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
			String iscommission = entity.getIscommission();
			if(StringUtils.isNotBlank(iscommission)){
				pro.append(" AND g.iscommission = ").append(iscommission);
			}
			pro.append(") pro");
			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" FROM cost_record_island c, (")
//			sb.append(" SELECT currencyId, sum(amount) amount FROM ( SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_island c, (")
				.append(pro)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 1 AND budgetType = 0 AND reviewType = 0 AND review = 2 AND delFlag = '0'")
				.append(" union all")
				.append(" SELECT c.currencyId, c.price * c.quantity amount FROM cost_record_island c, (")
				.append(pro)
				.append(" WHERE c.activity_uuid = pro.uuid AND ordertype = 1 AND budgetType = 1 AND reviewType = 2 AND reviewStatus <> '已取消' AND reviewStatus <> '已驳回' AND delFlag = '0') aa group by currencyId");
		}else{
			pro.append("(SELECT DISTINCT g.id FROM");
			if(entity.getSalerId() != null){
				//如果销售查询条件不为空，先找这个销售的订单，然后根据订单中的产品编号或团期id,去找产品或团期信息
				pro.append(" productorder o,")
					.append(" activitygroup g,")
					.append(" travelactivity p")
					.append(" WHERE")
					.append(" o.productGroupId = g.id")
					.append(" and g.srcActivityId =	p.id")
					.append(" AND p.activityStatus = 2")
					.append(" AND p.delFlag = 0")
					.append(" AND p.proCompany = ")
					.append(companyId)
					.append(" AND p.activity_kind = ")
					.append(orderType)
					.append(" AND o.salerId =")
					.append(entity.getSalerId());
			}else{
				pro.append(" activitygroup g,")
				.append(" travelactivity p")
				.append(" WHERE")
				.append("  g.srcActivityId =p.id")
				.append(" AND p.activityStatus = 2")
				.append(" AND p.delFlag = 0")
				.append(" AND p.proCompany = ")
				.append(companyId)
				.append(" AND p.activity_kind = ")
				.append(orderType);
			}
			String productName = entity.getProductName();
			if(StringUtils.isNotBlank(productName)){
				pro.append(" AND p.acitivityName like '%").append(productName).append("%'");
			}
			String groupCode = entity.getGroupCode();
			if(StringUtils.isNotBlank(groupCode)){
				pro.append(" AND g.groupCode like '%").append(groupCode).append("%'");
			}
			Integer operatorId = entity.getOperatorId();
			if(operatorId != null){//计调
				pro.append(" AND g.createBy =").append(operatorId);
			}
			String iscommission = entity.getIscommission();
			if(StringUtils.isNotBlank(iscommission)){
				pro.append(" AND g.iscommission = ").append(iscommission);
			}
			String groupOpenDate = entity.getGroupOpenDate();
			if(StringUtils.isNotBlank(groupOpenDate)) {
				pro.append(" AND g.groupOpenDate >='").append(groupOpenDate).append("'");
			}
			String groupCloseDate = entity.getGroupCloseDate();
			if(StringUtils.isNotBlank(groupCloseDate)) {
				pro.append(" AND g.groupOpenDate <='").append(groupCloseDate).append("'");
			}
			Integer deptId = entity.getDeptId();
			if(deptId != null) {
				pro.append(" AND p.deptId =").append(deptId);
			}
			
			pro.append(") pro");
			
			sb.append("select currencyId,sum(amount) amount from (select ")
				.append(" (select c.currency_id from currency c where c.create_company_id = " + companyId + " and c.currency_name='人民币' and c.del_flag=0) currencyId,")
				.append(" c.priceAfter amount")
				.append(" from  cost_record c,")
//			sb.append("select currencyId,sum(amount) amount from (select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = pro.id AND ordertype = ")
				.append(orderType)
				.append(" AND c.orderType in(1,2,3,4,5,10) AND c.budgetType = 0 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag = '0'")
				.append(" union all")
				.append(" select c.currencyId,c.price*c.quantity amount from  cost_record c,")
				.append(pro)
				.append(" where c.activityId = pro.id AND ordertype = ")
				.append(orderType)
				.append(" AND c.orderType in(1,2,3,4,5,10) AND c.budgetType = 0 AND c.reviewType = 2 AND c.reviewStatus <> '已取消' AND c.reviewStatus <> '已驳回' AND c.delFlag = '0') aa group by currencyId");
		}
		return sb.toString();
	}

	@Override
	public List<Map<String, Object>> getCostTotal(GroupManagerEntity entity,
			Integer orderType, Long companyId) {
		String sql = createCostTotal(entity, orderType, companyId);
		
		return activityGroupViewDao.findBySql(sql, Map.class);
	}

	@Override
	public String getProfitTotal(int moneyType, GroupManagerEntity entity,
			Integer orderType, Long companyId) {
 		List<Map<String,Object>> tmp = new ArrayList<Map<String,Object>>();
		String str ="";
		//获取达账金额
		List<Map<String,Object>> accountList = getMoneySum(Context.MONEY_TYPE_DZ, entity, orderType, companyId);
		//获取应付金额
		List<Map<String,Object>> costList = getCostTotal(entity, orderType, companyId);
		//利润=达账-应付
		//判断达账是否为空

		if(CollectionUtils.isNotEmpty(accountList)){
			//达账不为空，判断应付是否为空
			if(CollectionUtils.isNotEmpty(costList)){
				int alsize = accountList.size();
				int clsize = costList.size();
				//遍历两个list，币种一样相减，相减的结果放到新的list里，并从原来的list移除这两个相减的元素
				//accountList集合里币种没有相同的，costList集合里币种也没有相同的，查询这两个集合的时候按币种goup by ,
				for(int al=0;al<alsize;){
					boolean flag = false;
					Map<String,Object> almap = accountList.get(al);
					for(int cl = 0;cl<clsize;){
						Map<String,Object> clmap = costList.get(cl);
						Map<String,Object> tmpmap = new HashMap<String,Object>();
						if(almap.get("currencyId").equals(clmap.get("currencyId"))){
							BigDecimal ao = (BigDecimal) almap.get("amount");
							BigDecimal co = (BigDecimal) clmap.get("amount");
							//金额相减
							BigDecimal sub = ao.subtract(co);
							//金额相减的差值和0作比较，如果不等于0的话放进tmpmap 集合
							int a = sub.compareTo(new BigDecimal(0));//和零比较大小-1表示小于,0是等于,1是大于.
							if(a != 0){
								tmpmap.put("currencyId", almap.get("currencyId"));
								tmpmap.put("amount", ao.subtract(co));
							}
							tmp.add(tmpmap);
							costList.remove(cl);
							clsize=clsize-1;
							flag=true;
						}else{
							cl++;
						}
					}
					if(flag){
						  accountList.remove(al);
						  alsize = alsize-1;
					}else{
						al++;
					}
					
				}
				//相减完，并移除了元素，再次判断这个list是否为空，
				if(costList.size()>0){
					//应付金额list不为空的话,金额取负数，放进tmp
					int csize = costList.size();
					for(int i = 0;i<csize;i++){
						Map<String,Object> cmap = costList.get(i);
						Map<String,Object> newmap = new HashMap<String,Object>();
						BigDecimal b = (BigDecimal) cmap.get("amount");
						String currencyId = cmap.get("currencyId").toString();
						newmap.put("currencyId",currencyId);
						newmap.put("amount", b.multiply(new BigDecimal(-1)));
						tmp.add(newmap);
					}
				}
				if(accountList.size()>0){
					//达账金额list不为空的话，放进tmp
					int asize = accountList.size();
					for(int in =0;in<asize;in++){
						tmp.add(accountList.get(in));
					}
				}
				str = getMoneySumStr(tmp);
			}else{
				//应付为空的，利润=达账
				str = getMoneySumStr(accountList);
			}
		}else{
			//达账金额为空,判断应付是否为空
			if(CollectionUtils.isNotEmpty(costList)){
				//应付不为空，利润=（-应付）
				int clsize = costList.size();
				for(int cl=0;cl<clsize;cl++){
					Map<String,Object> map = costList.get(cl);
					Map<String,Object> tmpmap = new HashMap<String,Object>();
					Object o = map.get("amount");
					if(o != null && !"0".equals(o.toString())){
						tmpmap.put("currencyId", map.get("currencyId"));
						BigDecimal bd = (BigDecimal) o;
						tmpmap.put("amount", bd.multiply(new BigDecimal(-1)));
						tmp.add(tmpmap);
					}
					
				}
				str = getMoneySumStr(tmp);
			}
		}
		return str;
	}
	
	
	/**
     * 团队管理-利润总额(应收总额-应付总额)
     * @param totalMoneyList    应收总额币种和金额列表
	 * @param costTotalList		应付总和币种和金额列表
	 * @return	String
	 * @author xianglei.dong
	 * **/
	@Override
    public String getProfitTotal(List<Map<String,Object>> totalMoneyList, List<Map<String,Object>> costTotalList) {
		List<Map<String,Object>> tmp = new ArrayList<Map<String,Object>>();
		String str ="";
		//判断应收是否为空
		if(CollectionUtils.isNotEmpty(totalMoneyList)){
			//应收不为空，判断应付是否为空
			if(CollectionUtils.isNotEmpty(costTotalList)){
				int alsize = totalMoneyList.size();
				int clsize = costTotalList.size();
				//遍历两个list，币种一样相减，相减的结果放到新的list里，并从原来的list移除这两个相减的元素
				//totalMoneyList集合里币种没有相同的，costTotalList集合里币种也没有相同的，查询这两个集合的时候按币种goup by ,
				for(int al=0;al<alsize;){
					boolean flag = false;
					Map<String,Object> almap = totalMoneyList.get(al);
					for(int cl = 0;cl<clsize;){
						Map<String,Object> clmap = costTotalList.get(cl);
						Map<String,Object> tmpmap = new HashMap<String,Object>();
						if(almap.get("currencyId").equals(clmap.get("currencyId"))){
							BigDecimal ao = (BigDecimal) almap.get("amount");
							BigDecimal co = (BigDecimal) clmap.get("amount");
							//金额相减
							BigDecimal sub = ao.subtract(co);
							//金额相减的差值和0作比较，如果不等于0的话放进tmpmap 集合
							int a = sub.compareTo(new BigDecimal(0));//和零比较大小-1表示小于,0是等于,1是大于.
							if(a != 0){
								tmpmap.put("currencyId", almap.get("currencyId"));
								tmpmap.put("amount", ao.subtract(co));
							}
							tmp.add(tmpmap);
							costTotalList.remove(cl);
							clsize=clsize-1;
							flag=true;
						}else{
							cl++;
						}
					}
					if(flag){
						totalMoneyList.remove(al);
						  alsize = alsize-1;
					}else{
						al++;
					}
					
				}
				//相减完，并移除了元素，再次判断这个list是否为空，
				if(costTotalList.size()>0){
					//应付金额list不为空的话,金额取负数，放进tmp
					int csize = costTotalList.size();
					for(int i = 0;i<csize;i++){
						Map<String,Object> cmap = costTotalList.get(i);
						Map<String,Object> newmap = new HashMap<String,Object>();
						BigDecimal b = (BigDecimal) cmap.get("amount");
						String currencyId = cmap.get("currencyId").toString();
						newmap.put("currencyId",currencyId);
						newmap.put("amount", b.multiply(new BigDecimal(-1)));
						tmp.add(newmap);
					}
				}
				if(totalMoneyList.size()>0){
					//应收金额list不为空的话，放进tmp
					int asize = totalMoneyList.size();
					for(int in =0;in<asize;in++){
						tmp.add(totalMoneyList.get(in));
					}
				}
				str = getMoneySumStr(tmp);
			}else{
				//应付为空的，利润=应收
				str = getMoneySumStr(totalMoneyList);
			}
		}else{
			//应收金额为空,判断应付是否为空
			if(CollectionUtils.isNotEmpty(costTotalList)){
				//应付不为空，利润=（-应付）
				int clsize = costTotalList.size();
				for(int cl=0;cl<clsize;cl++){
					Map<String,Object> map = costTotalList.get(cl);
					Map<String,Object> tmpmap = new HashMap<String,Object>();
					Object o = map.get("amount");
					if(o != null && !"0".equals(o.toString())){
						tmpmap.put("currencyId", map.get("currencyId"));
						BigDecimal bd = (BigDecimal) o;
						tmpmap.put("amount", bd.multiply(new BigDecimal(-1)));
						tmp.add(tmpmap);
					}
					
				}
				str = getMoneySumStr(tmp);
			}
		}
		return str;
	}
	
	@Override
	public boolean updateIscommissionStatus(HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		String groupId = request.getParameter("groupId");
		String status = request.getParameter("status");
		String orderType = request.getParameter("orderType");
		try{
			if(StringUtils.isNotBlank(orderType)){
				Integer type = Integer.valueOf(orderType);
				if(Context.ORDER_TYPE_QZ == type){
					visaProductsDao.updateIsCommission(Long.valueOf(groupId), Integer.valueOf(status));
				}else if(Context.ORDER_TYPE_JP == type){
					airTicketDao.updateIsCommission(Long.valueOf(groupId), Integer.valueOf(status));
				}else if(Context.ORDER_TYPE_HOTEL == type){
					hotelGroupDao.updateIscommission(Integer.valueOf(groupId), Integer.valueOf(status));
				}else if(Context.ORDER_TYPE_ISLAND == type){
					islandGroupDao.updateIscommission(Integer.valueOf(groupId), Integer.valueOf(status));
				}else{
					activityGroupDao.updateIsCommission(Long.valueOf(groupId), Integer.valueOf(status));
				}
			}
			flag = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}


	@Override
	public List<Map<String, Object>> getAllInfoMap(GroupManagerEntity entity,
			Integer orderType, Long companyId) {
		StringBuffer strs = new StringBuffer();
		String where = getWhereSQL(entity, orderType);
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where = getWhereSQL(entity, orderType);
		}
		if(0 == orderType && !"散拼".equals(entity.getSaler())){
			strs.append(getAllSql(companyId, where));
		}else if(Context.ORDER_TYPE_QZ == orderType){
			strs.append("SELECT iscommission, productId, groupCode, productName, operatorName, operatorId, saler,")
					.append(" salerId, createDate, updateDate, lockStatus, forcastStatus, orderType,")
					.append(" DATE_FORMAT(groupOpenDate,'%Y-%m-%d') groupOpenDate, personNum, deptId FROM ")
					.append(" (SELECT p.iscommission, p.id AS productId, p.groupCode, p.productName, ")
					.append(" (SELECT name FROM sys_user su WHERE su.id = p.createBy) AS operatorName, p.createBy AS operatorId, ")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, visa_order o WHERE su.id = o.salerId AND o.visa_product_id = p.id")
					.append(" AND o.payStatus NOT IN (99,111) AND o.visa_order_status<>100 AND o.del_flag = '0' ) as saler,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, visa_order o WHERE su.id = o.salerId AND o.visa_product_id = p.id")
					.append(" AND o.payStatus NOT IN (99,111) AND o.visa_order_status<>100 AND o.del_flag = '0' ) as salerId,")
					.append(" p.createDate, p.updateDate, p.lockStatus, p.forcastStatus, 6 orderType, p.createDate groupOpenDate,")
					.append(" (SELECT SUM(vo.travel_num) from visa_order vo where vo.visa_product_id=p.id and vo.del_flag='0' ")
					.append(" AND vo.payStatus NOT IN (99,111) AND vo.visa_order_status<>100 GROUP BY p.id ) personNum, p.deptId")
					.append(" FROM visa_products p WHERE p.productStatus = 2")
					.append(" AND p.proCompanyId = ").append(companyId).append(" AND p.delFlag = 0 ) A ")
					.append(" WHERE 1=1 ").append(where);
		}else if(Context.ORDER_TYPE_JP == orderType){
			strs.append("SELECT iscommission, productId, groupCode, productName, operatorName, operatorId, saler,")
					.append(" salerId, createDate, updateDate, lockStatus, forcastStatus, orderType,")
					.append(" DATE_FORMAT(groupOpenDate,'%Y-%m-%d') groupOpenDate, personNum, deptId FROM ")
					.append(" (SELECT t.iscommission, t.productId, t.groupCode, CONCAT(departureCity, '-', arrivedCity, ':', airType) AS productName,")
					.append(" operatorName, operatorId, saler, salerId, createDate, updateDate, lockStatus, forcastStatus, orderType, ")
					.append(" groupOpenDate, personNum, deptId FROM ")
					.append(" (SELECT p.iscommission, p.id AS productId, p.group_code AS groupCode, ")
					.append(" CASE airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 THEN '单程' ELSE '其他' END AS airType,")
					.append(" IFNULL((SELECT label FROM sys_dict WHERE type = 'from_area' AND delFlag = '0' AND VALUE = p.departureCity),' ') AS departureCity,")
					.append(" IFNULL((SELECT name FROM sys_area area WHERE delFlag = '0' AND area.id = p.arrivedCity),' ') AS arrivedCity,")
					.append(" (SELECT name FROM sys_user su WHERE su.id = p.createBy) AS operatorName, p.createBy AS operatorId,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, airticket_order o where su.id = o.salerId AND o.airticket_id = p.id ")
					.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS saler,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, airticket_order o where su.id = o.salerId AND o.airticket_id = p.id ")
					.append(" AND o.del_flag = '0' AND o.order_state NOT IN (99,111)) AS salerId,")
					.append(" p.createDate, p.updateDate, p.lockStatus, p.forcastStatus, 7 orderType, outTicketTime AS groupOpenDate, ")
					.append(" (SELECT SUM(o.person_num) FROM airticket_order o WHERE o.airticket_id = p.id AND o.del_flag=0 ")
					.append(" AND o.order_state NOT IN (99, 111)) personNum, deptId ")
					.append(" FROM activity_airticket p WHERE p.productStatus = 2 ")
					.append(" AND p.proCompany = ").append(companyId).append(" AND p.delFlag = 0 ) t ")
					.append(" ) A ").append(" where 1=1 ").append(where);
		}else if(Context.ORDER_TYPE_ISLAND == orderType){
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId, a.saler,")
					.append(" a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType, ")
					.append(" a.groupOpenDate, a.personNum, a.deptId, a.productUuid, a.uuid activityUuid FROM ")
					.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
					.append(" (SELECT NAME FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, island_order o WHERE su.id = o.salerId AND o.activity_island_group_uuid = g.uuid")
					.append(" AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, island_order o WHERE su.id = o.salerId AND o.activity_island_group_uuid = g.uuid")
					.append(" AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
					.append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, 12 as orderType, g.groupOpenDate,")
					.append(" (SELECT SUM(o.orderPersonNum) FROM island_order o WHERE g.uuid = o.activity_island_group_uuid AND o.delFlag='0' ")
					.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid ")
					.append(" FROM activity_island_group g,	activity_island p WHERE	p.uuid = g.activity_island_uuid AND p.wholesaler_id = ")
					.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		}else if(Context.ORDER_TYPE_HOTEL == orderType){
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
					.append(" a.saler, a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType, ")
					.append(" a.groupOpenDate, a.personNum, a.deptId, a.productUuid, a.uuid activityUuid FROM ")
					.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.activityName productName, p.id AS productId,")
					.append(" (SELECT name FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy AS operatorId,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
					.append("  AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') saler,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, hotel_order o WHERE su.id = o.salerId ")
					.append(" AND o.activity_hotel_group_uuid = g.uuid AND o.orderStatus <> 3 AND o.delFlag = '0') salerId,")
					.append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, 11 AS orderType, g.groupOpenDate, ")
					.append(" (SELECT SUM(o.orderPersonNum) FROM hotel_order o WHERE g.uuid = o.activity_hotel_group_uuid AND o.delFlag='0' ")
					.append(" AND o.orderStatus <> 3) AS personNum, p.deptId, p.uuid productUuid, g.uuid ")
					.append(" FROM activity_hotel_group g, activity_hotel p WHERE p.uuid = g.activity_hotel_uuid AND p.wholesaler_id = ")
					.append(companyId).append(" AND p.delFlag = 0 AND g.delFlag = 0) a WHERE 1 = 1 ").append(where);
		}else{
			strs.append("SELECT a.iscommission, a.groupId, a.groupCode, a.productName, a.productId, a.operatorName, a.operatorId,")
					.append(" a.saler, a.salerId, a.createDate, a.updateDate, a.lockStatus, a.forcastStatus, a.orderType,")
					.append(" a.groupOpenDate, a.personNum, a.deptId FROM ")
					.append(" (SELECT g.iscommission, g.id AS groupId, g.groupCode, p.acitivityName productName, p.id AS productId, ")
					.append(" (SELECT NAME FROM sys_user su WHERE su.id = g.createBy) AS operatorName, g.createBy as operatorId,")
					.append(" (SELECT GROUP_CONCAT(DISTINCT su.name) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
					.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') saler, ")
					.append("(SELECT GROUP_CONCAT(DISTINCT su.id) FROM sys_user su, productorder o WHERE su.id = o.salerId AND o.productGroupId = g.id ")
					.append(" AND o.payStatus NOT IN (99, 111) AND o.delFlag = '0') salerId,")
					.append(" p.createDate, p.updateDate, g.lockStatus, g.forcastStatus, p.activity_kind orderType, g.groupOpenDate,")
					.append(" (SELECT SUM(o.orderPersonNum) FROM productorder o WHERE g.id = o.productGroupId AND o.delFlag=0 ")
					.append(" AND o.payStatus NOT IN (99,111)) AS personNum, p.deptId FROM activitygroup g, travelactivity p ")
					.append(" WHERE p.id = g.srcActivityId AND p.activityStatus = 2 AND p.delFlag = 0 AND g.delFlag = 0 ")
					.append(" AND p.proCompany = ").append(companyId);
			if(orderType != 0) {
				strs.append(" AND p.activity_kind = ").append(orderType);
			}
			strs.append(" ) a ").append(" WHERE 1=1 ").append(where);
		}
		List<Map<String,Object>> list = activityGroupViewDao.findBySql(strs.toString(),Map.class );
		return list;
	}


}
