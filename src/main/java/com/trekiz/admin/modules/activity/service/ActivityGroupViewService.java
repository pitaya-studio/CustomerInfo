package com.trekiz.admin.modules.activity.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroupView;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrderView;
import com.trekiz.admin.modules.activity.entity.TargetArea;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupViewDao;
import com.trekiz.admin.modules.activity.repository.ActivityReserveOrderDao;
import com.trekiz.admin.modules.activity.repository.ActivityReserveOrderViewDao;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 产品出团信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class ActivityGroupViewService extends BaseService implements IActivityGroupViewService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ActivityGroupViewDao activityGroupViewDao;
	@Autowired
	private ActivityReserveOrderViewDao activityReserveOrderViewDao;
	@Autowired
	private ActivityReserveOrderDao activityReserveOrderDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public  Page<Map<String, Object>> findActivityGroupCostView(Page<Map<String, Object>> page,TravelActivity travelActivity,
						DepartmentCommon common, Map<String, Object> params) {
		Object groupCode = params.get("groupCode");
		Object commitType = params.get("commitType");
		Object orderType = params.get("orderType");
		Object supplierId = params.get("supplierId");
		Object operator = params.get("operator");
		Object isReject = params.get("isReject");

		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT t.id, groupCode, srcActivityId, acitivityName, activitySerNum, fromArea, targetArea, activityStatus,")
		  .append(" groupOpenDate, groupCloseDate, payReservePosition, nopayReservePosition,soldPayPosition, freePosition, planPosition, t.review,")
		  .append(" t.nowLevel, settlementAdultPrice, productId, activityKind, proCompany, t.createDate, ")
		  .append(" t.updateDate, suggestAdultPrice, t.createBy, t.operator, (SELECT '1' FROM cost_record cr LEFT JOIN ")
		  .append(" review_new rn ON cr.reviewUuid = rn.id LEFT JOIN review_new rp  ON cr.pay_review_uuid = rp.id ")
		  .append(" WHERE cr.orderType = t.activityKind AND cr.activityId = t.id AND cr.reviewType IN (0, 2) ")
		  .append(" AND cr.is_new = 2 AND cr.delFlag = '0' AND cr.visaId IS NULL AND ( rn.`status` = 0 OR rp.`status` = 0 ) ")
		  .append(" LIMIT 1) AS isReject FROM ")
		  .append(" (SELECT agp.id, agp.groupCode, agp.srcActivityId, activity.acitivityName, activity.activitySerNum, activity.fromArea, activity.targetArea, ")
		  .append("	activity.activityStatus,agp.groupOpenDate, agp.groupCloseDate, agp.payReservePosition, agp.nopayReservePosition, ")
		  .append(" agp.soldPayPosition, agp.freePosition, agp.planPosition, agp.review,agp.nowLevel, activity.settlementAdultPrice, activity.id AS productId, ")
		  .append(" activity.activity_kind AS activityKind, activity.proCompany, activity.createDate, activity.updateDate,")
		  .append(" activity.suggestAdultPrice, activity.createBy, (SELECT name FROM sys_user su WHERE su.id = agp.createBy) AS operator ")
		  .append(" FROM activitygroup agp, travelactivity activity ")
		  .append(" WHERE agp.srcActivityId = activity.id AND activity.delFlag = '0' AND activity.activityStatus = 2 AND agp.delFlag = '0'")
		  .append(" UNION ")
		  .append(" SELECT agp.id, agp.groupCode, agp.srcActivityId, activity.acitivityName, activity.activitySerNum, activity.fromArea, activity.targetArea, ")
		  .append(" activity.activityStatus,agp.groupOpenDate, agp.groupCloseDate, agp.payReservePosition, agp.nopayReservePosition, agp.soldPayPosition, ")
		  .append(" agp.freePosition, agp.planPosition, agp.review,")
		  .append(" agp.nowLevel, activity.settlementAdultPrice, activity.id AS productId, activity.activity_kind AS activityKind, ")
		  .append(" activity.proCompany, activity.createDate, activity.updateDate,")
		  .append(" activity.suggestAdultPrice, activity.createBy, ")
		  .append(" (SELECT name FROM sys_user su WHERE su.id = agp.createBy) AS operator ")
		  .append(" FROM activitygroup agp, travelactivity activity, productorder orders")
		  .append(" WHERE agp.srcActivityId = activity.id AND activity.id = orders.productId AND agp.id = orders.productGroupId ")
		  .append("  AND (activity.delFlag = '1' OR activity.activityStatus = 3)) t ");
		if(null != supplierId && StringUtils.isNotEmpty(String.valueOf(supplierId))) {
			sb.append(" LEFT JOIN cost_record cr on cr.activityId = t.id ");
		}
		sb.append(" WHERE 1=1 ");
		if(null != supplierId && StringUtils.isNotEmpty(String.valueOf(supplierId))) {
			sb.append(" AND (cr.orderType <6 OR cr.orderType = 10) AND cr.supplyType = 0 ")
			  .append(" AND cr.supplyId = ").append(supplierId);
		}
		//未提交  0416   update by shijun.liu   2016.04.29
		// 存在实际成本审批通过 并且有未提交付款审批的数据 或者提交付款审批并且被驳回，或者已取消的数据
		if("1".equals(commitType)){//
			sb.append(" AND EXISTS (SELECT c.id FROM cost_record c WHERE c.activityId = t.id AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append(ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.budgetType = 1 AND c.reviewType = 0 AND c.review = 2 AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		//已提交  0416   update by shijun.liu   2016.04.29
		//已通过成本审批，并且已提交付款审批(all)
		// 首先必须有实际成本，并且所有的实际成本都进行了付款审批的提交
		if("2".equals(commitType)){
			sb.append(" AND NOT EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append( ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ")
			  .append(" AND EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}

		// 查询团期内存在有被驳回的审批。 add by yudong.xu 2017.3.22
		if (null != isReject && "1".equals(isReject)) {
			sb.append(" AND EXISTS (SELECT 'x' FROM cost_record cr LEFT JOIN review_new rn ON cr.reviewUuid = rn.id ")
			  .append(" LEFT JOIN review_new rp ON cr.pay_review_uuid = rp.id WHERE cr.orderType = t.activityKind ")
			  .append(" AND cr.activityId = t.id AND cr.reviewType IN (0, 2) AND cr.is_new = 2 AND cr.delFlag = '0' ")
			  .append(" AND cr.visaId IS NULL AND ( rn.`status` = 0 OR rp.`status` = 0 ))");
		}

		if(null != groupCode && StringUtils.isNotEmpty(String.valueOf(groupCode))) {
			sb.append(" AND groupCode like '%").append(String.valueOf(groupCode)).append("%' ");
		}
		if(null != orderType && StringUtils.isNotEmpty(String.valueOf(orderType))) {
			sb.append(" AND activityKind = ").append(Integer.parseInt(String.valueOf(orderType)));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(travelActivity.getGroupOpenDate() != null) {
			String groupOpenDate = sdf.format(travelActivity.getGroupOpenDate());
			sb.append(" AND groupOpenDate >= '").append(groupOpenDate).append("' ");
		}
		if(travelActivity.getGroupCloseDate() != null) {
			String groupCloseDate = sdf.format(travelActivity.getGroupCloseDate());
			sb.append(" AND groupOpenDate <= '").append(groupCloseDate).append("' ");
		}
		if(null != operator && StringUtils.isNotEmpty(String.valueOf(operator))){
			sb.append(" AND t.operator like '%").append(String.valueOf(operator)).append("%' ");
		}
		
		String departmentId = common.getDepartmentId();
		String s = "";
		if(departmentId!=null && !"".equals(departmentId)) {
			List<User> userList= userDao.getUserByDepartment(companyId, Long.parseLong(departmentId));
			List<Department> depts = departmentDao.findByParentIdsLike("%,"+ departmentId + ",%");
			for(Department dept : depts) {
				userList.addAll(userDao.getUserByDepartment(companyId, dept.getId()));
			}
			for(User user : userList) {
				s += user.getId() + ",";
			}
		}
		if(s.length() > 0) {
			s = s.substring(0, s.length() - 1);
			sb.append(" and (t.createBy in (").append(s).append(")");
		}else {
			sb.append(" and (t.createBy in ('')");
		}
		String activityIds = "";
		List<Integer> activityIdList = departmentDao.findActivityIdsByOpUserId(UserUtils.getUser().getId());
		for(Integer activityId : activityIdList) {
			activityIds += activityId + ",";
		}
		if(activityIds.length() > 0) {
			activityIds = activityIds.substring(0, activityIds.length() - 1);
			sb.append(" or t.srcActivityId in (").append(activityIds).append(") ");
		}
		sb.append(" ) ");
		String order = page.getOrderBy();
        if(StringUtils.isBlank(order)){
            page.setOrderBy("createDate DESC ");
        }
		Page<Map<String, Object>> groupPage = (Page<Map<String, Object>>) activityGroupViewDao.findBySql(page, sb.toString(), Map.class);
		return groupPage;
	}
	
	
	@Override
	public  Page<ActivityGroupView> findActivityGroupReview(Page<ActivityGroupView> page,TravelActivity travelActivity, String settlementAdultPriceStart,
			String settlementAdultPriceEnd,Long agentId,Integer activityKind,String review,Long companyId, DepartmentCommon common) {		
	
		DetachedCriteria dc = activityGroupViewDao.createDetachedCriteria();		
			
		dc.add(Restrictions.eq("activityKind",  activityKind));
		dc.add(Restrictions.eq("proCompany",  companyId));
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)) {			
			dc.add(Restrictions.sqlRestriction("{alias}.productId in (select srcActivityId from activitytargetarea where targetAreaId in (?) )",targetAreaIds,StringType.INSTANCE));
		}
		if(agentId != null) {			
		    dc.add(Restrictions.sqlRestriction("{alias}.id in (select activityGroupId from activitygroupreserve where agentId =(?) )",agentId.toString(), StringType.INSTANCE));
		}	
		   
		if (StringUtils.isNotBlank(review)) {			
			dc.add(Restrictions.eq("review", new Integer(review)));
		}		
		//20151013散拼产品的库存切位，默认搜索条件需添加团号
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())) {
			//dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"));
			dc.add(Restrictions.or(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"),
					Restrictions.like("groupCode", "%"+travelActivity.getAcitivityName()+"%")));
		}				
		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());
		
				
		if (StringUtils.isNotBlank(settlementAdultPriceStart)) {
			dc.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
					
		}
		
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			dc.add(Restrictions.le("settlementAdultPrice", new BigDecimal(settlementAdultPriceEnd)));
		}
		
		if(travelActivity.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",travelActivity.getGroupOpenDate()));
		}
		
		if (travelActivity.getGroupCloseDate()!=null) {
			dc.add(Restrictions.le("groupOpenDate",travelActivity.getGroupCloseDate()));
		}	
		if (StringUtils.isBlank(page.getOrderBy())) {
			if(Context.SUPPLIER_UUID_DYGL.equals(UserUtils.getUser().getCompany().getUuid())){
				dc.addOrder(Order.desc("groupOpenDate"));
			}else{
				dc.addOrder(Order.desc("createDate"));
			}
		}
	    return activityGroupViewDao.find(page, dc);
	}
	
	@Override
	public  Page<ActivityGroupView> findActivityGroupReviewC325(Page<ActivityGroupView> page,TravelActivity travelActivity,Map<String,String> map,Long agentId,Integer activityKind, DepartmentCommon common) {		

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = activityGroupViewDao.createDetachedCriteria();		
		dc.add(Restrictions.eq("activityKind",  activityKind));
		dc.add(Restrictions.eq("proCompany",  UserUtils.getUser().getCompany().getId()));
		//截团日期之前的数据，过期的数据不予显示
		dc.add(Restrictions.ge("groupCloseDate",new Date()));
		String targetAreaIds = travelActivity.getTargetAreaIds();//目的地
		if(StringUtils.isNotBlank(targetAreaIds)) {			
			dc.add(Restrictions.sqlRestriction("{alias}.productId in (select srcActivityId from activitytargetarea where targetAreaId in (?) )",targetAreaIds,StringType.INSTANCE));
		}
		if(agentId != null) {			
		    dc.add(Restrictions.sqlRestriction("{alias}.id in (select activityGroupId from activitygroupreserve where agentId =(?) )",agentId.toString(), StringType.INSTANCE));
		}
		//散拼产品的库存切位，默认搜索条件需添加团号
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())) {
			dc.add(Restrictions.or(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"),
					Restrictions.like("groupCode", "%"+travelActivity.getAcitivityName()+"%")));
		}				
		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());
		//价格范围		
		if (StringUtils.isNotBlank(map.get("settlementAdultPriceStart"))) {
			dc.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(map.get("settlementAdultPriceStart"))));
					
		}
		if (StringUtils.isNotBlank(map.get("settlementAdultPriceEnd"))) {
			dc.add(Restrictions.le("settlementAdultPrice", new BigDecimal(map.get("settlementAdultPriceEnd"))));
		}
		//出团日期
		try {
			if(StringUtils.isNotBlank(map.get("groupOpenDateStart"))) {
				dc.add(Restrictions.ge("groupOpenDate",df.parse(map.get("groupOpenDateStart"))));
			}
			if (StringUtils.isNotBlank(map.get("groupOpenDateEnd"))) {
				dc.add(Restrictions.le("groupOpenDate",df.parse(map.get("groupOpenDateEnd"))));
			}
		} catch (ParseException e) {
			logger.error("C325 需求解析出团日期的开始时候或者截止时间出错！");
			e.printStackTrace();
		}
		//截团日期groupCloseDate  
		try{
			if(StringUtils.isNotBlank(map.get("groupCloseDateStart"))) {
				dc.add(Restrictions.ge("groupCloseDate",df.parse(map.get("groupCloseDateStart"))));
			}
			
			if (StringUtils.isNotBlank(map.get("groupCloseDateEnd"))) {
				dc.add(Restrictions.le("groupCloseDate",df.parse(map.get("groupCloseDateEnd"))));
			}
		}catch (ParseException e) {
			logger.error("C325 需求解析出团日期的开始时候或者截止时间出错！");
			e.printStackTrace();
		}
		//切位数范围 freePosition
		if(map.get("source")==null || map.get("source").equals("isReserve")) {
			try{
				if(StringUtils.isNotBlank(map.get("freePositionStart"))) {
					dc.add(Restrictions.ge("freePosition",Integer.valueOf(map.get("freePositionStart"))));
				}
				if (StringUtils.isNotBlank(map.get("freePositionEnd"))) {
					dc.add(Restrictions.le("freePosition",Integer.valueOf(map.get("freePositionEnd"))));
				}
			}catch (Exception e) {
				logger.error("C325 需求解析切位范围的数据出错！");
				e.printStackTrace();
			}
		}
		if (StringUtils.isBlank(page.getOrderBy())) {
			dc.addOrder(Order.desc("groupOpenDate"));
			page.setOrderBy("groupOpenDate");
		}
		
		//update by zhanghao 判断调用接口的来源 如果是批量切位操作中的添加团期默认判断余位数大于0，如果是批量归还切位操作中的添加团期默认判断切位数大于0
		if(map.containsKey("source")&&map.get("source")!=null){
			String source = map.get("source");
			if(source.equals("isReserve")){
				dc.add(Restrictions.gt("freePosition",Integer.valueOf(0)));
			}else if(source.equals("isReturn")){
				dc.add(Restrictions.gt("payReservePosition",Integer.valueOf(0)));
			}
		}

	    return activityGroupViewDao.find(page, dc);
	}

	/**
	 * add by ruyi.chen
	 * add date 2014-12-22
	 * 分页获取散拼切位订单信息列表(在散拼库存列表查询基础上修改，视图展示)
	 */
	@Override
	public Page<ActivityReserveOrderView> findReserveOrderList(
			Page<ActivityReserveOrderView> page, TravelActivity travelActivity,
			String settlementAdultPriceStart, String settlementAdultPriceEnd,
			Long agentId,String paymentType, DepartmentCommon common) {
		DetachedCriteria dc = activityReserveOrderViewDao.createDetachedCriteria();
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)) {
			dc.add(Restrictions.sqlRestriction("{alias}.srcActivityId in (select srcActivityId from activitytargetarea where targetAreaId in ("+targetAreaIds+") )"));
			
		}
		
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())) {
			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"));
		} 
		
		
		if (StringUtils.isNotBlank(settlementAdultPriceStart)) {
			dc.add(Restrictions.ge("payMoney", StringNumFormat.getBigDecimalValue(settlementAdultPriceStart)));
		}
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			dc.add(Restrictions.le("payMoney", StringNumFormat.getBigDecimalValue(settlementAdultPriceEnd)));
		}
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null) {
			dc.add(Restrictions.between("groupOpenDate",travelActivity.getGroupOpenDate(),travelActivity.getGroupCloseDate()));
		}
		if(agentId!=null) {
			dc.add(Restrictions.eq("agentId", agentId.intValue()));
		}
		if(StringUtils.isNotBlank(paymentType)){
			dc.add(Restrictions.sqlRestriction("{alias}.agentId in (select id from agentinfo where paymentType = "+paymentType+" )"));
		}
	
		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));			
		}
		
		dc.add(Restrictions.isNotNull("id"));
		dc.add(Restrictions.eq("reserveType", 0));
		
		dc.add(Restrictions.eq("companyId", companyId.intValue()));
		return activityReserveOrderViewDao.find(page, dc);
	}
	
	public ActivityReserveOrderView findReserveOrderInfo(Integer aid){
		
		List<ActivityReserveOrderView> av=activityReserveOrderViewDao.findActivityGroupView(aid);
		if(av.size()>0){
			return av.get(0);
		}
		return null;
	}
	
	 public  List<TargetArea> findTargetArea() {
		 List<TargetArea> targetAreaList = Lists.newArrayList();
		 String Sql = "SELECT id,srcActivityId,targetAreaId,name FROM activitytargetarea_view";
		 List<Map<String, Object>> list = activityGroupViewDao.findBySql(Sql, Map.class);
		 if (CollectionUtils.isNotEmpty(list)) {
			 TargetArea targetArea;
 			for (Map<String, Object> map : list) {
 				targetArea = new TargetArea();
 				try {
 					targetArea.setId(Long.parseLong(map.get("id").toString()));
 					targetArea.setName(map.get("name").toString());
 					targetArea.setSrcActivityId(Long.parseLong(map.get("srcActivityId").toString()));
 					targetArea.setTargetAreaId(Long.parseLong(map.get("targetAreaId").toString()));
 					targetAreaList.add(targetArea);
					} catch (Exception e) {
						e.printStackTrace();
					}
 				
 			}
		 }
		 return targetAreaList;
	 }
}
