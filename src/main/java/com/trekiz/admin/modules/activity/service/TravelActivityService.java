package com.trekiz.admin.modules.activity.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * 旅游产品信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class TravelActivityService extends BaseService implements ITravelActivityService{

	@Autowired
	private ActivityGroupReserveDao activityGroupReserveDao;
	@Autowired
	private TravelActivityDao travelActivityDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SystemService systemService;
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#save(com.trekiz.admin.modules.activity.entity.TravelActivity)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public TravelActivity save(TravelActivity travelActivity){
		return travelActivityDao.save(travelActivity);
	}	
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findById(java.lang.Long)
	 */
	@Override
	public TravelActivity findById(Long id){
		
		return travelActivityDao.findOne(id);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#delActivity(com.trekiz.admin.modules.activity.entity.TravelActivity)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivity(TravelActivity activity){
		activity.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		travelActivityDao.save(activity);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#batchDelActivity(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void batchDelActivity(List<Long> ids){
		travelActivityDao.batchDelActivity(ids);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#batchOffActivity(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void batchOnOrOffActivity(List<Long> ids, Integer status){
		travelActivityDao.batchOnOrOffActivity(ids, status);
	}
	
	public Page<Object[]> findActivityByStatusAndCompanyId(Page<Object[]> page,Integer status,String companyIds,String targetAreaIdList) {
		StringBuffer sql = new StringBuffer("select  id,activitySerNum,acitivityName,activityTypeId,activityTypeName,travelTypeId,travelTypeName,activityLevelId,activityLevelName,payMode,remainDays,fromArea,targetArea,activityDuration,trafficMode,trafficName,flightInfo,groupOpenDate,groupCloseDate,settlementAdultPrice,suggestAdultPrice,createDate,activityStatus,recentUpdateTime,wholeSalerId,createBy,updateBy,updateDate,delFlag,proCompany,proCompanyName,remarks,overseasFlag,is_sync_success,is_after_supplement,payMode_full,payMode_deposit,payMode_advance,remainDays_advance,remainDays_deposit,payMode_data,payMode_guarantee,payMode_express,remainDays_data,remainDays_guarantee,remainDays_express,time_slice_hour_begin,time_slice_hour_end,time_slice_minute_begin,time_slice_minute_end,outArea,activity_kind,currency_type,activity_kind_name,airticket_id,airticketNum,estimate_price_id,group_lead,special_remark,singleDiff_unit,groupOpenCode,groupCloseCode,deptId,opUserId,backArea,payMode_op,maxPeopleCount from travelactivity where delFlag='"+TravelActivity.DEL_FLAG_NORMAL+"' AND proCompanyName NOT LIKE '%TEST_%'");
		if(status==0||status==null) {
			if(StringUtils.isNotBlank(companyIds)) {
				sql.append(" and proCompany in (" + companyIds + ") ");
			}
		}else{
			if(StringUtils.isEmpty(companyIds)) {
				sql.append(" and activityStatus = " + status);
			}else{
				sql.append(" and proCompany in (" + companyIds + ")");
				sql.append(" and activityStatus = " + status);
			}
		}
		if(StringUtils.isNotBlank(targetAreaIdList)) {
			sql.append(" and id in (select srcActivityId from activitytargetarea where targetAreaId in ("+targetAreaIdList+"))");
		}
		
		sql.append(" order by proCompany, createDate desc ");
		return travelActivityDao.findBySql(page,sql.toString());
	}
	
	public String getTargetAreaName(String activityId) {
		String sql = "select targetAreaId from activitytargetarea where srcActivityId="+activityId;
		List<Object> targetAreaIds = travelActivityDao.findBySql(sql);
		StringBuffer targetAreaName = new StringBuffer("");
		if(!targetAreaIds.isEmpty()) {
			for(Object o:targetAreaIds) {
				targetAreaName.append(areaService.get(StringUtils.toLong(o.toString())).getName()+",");
			}
			targetAreaName.deleteCharAt(targetAreaName.length()-1);
		}
		return targetAreaName.toString();
	}
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findAreaIds(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> findAreaIds(Long companyId){
		
		String sql = "select sa.id from sys_area sa inner join userdefinedict u  on sa.id = u.dictId where  u.companyId='"+companyId+"' and u.type='area'";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	//-----------t1t2需求-----------s--//
	@Override
	public List<Map<String, Object>> findAreaIds4T1(Long companyId){
		
		String sql = "select sa.id from sys_area sa where sa.delFlag = 0 and parentId <> 0 ORDER BY sa.code";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	
	
	
	@Override
	public List<Map<String, Object>> findAreaIdsEndCountry(Long companyId) {
		String sql = "select sa.id from sys_area sa inner join userdefinedict u  on sa.id = u.dictId where  u.companyId='"+companyId+"' and u.type='area' and sa.type < 3";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	//-----------t1t2需求-----------s--//
	@Override
	public List<Map<String, Object>> findAreaIdsEndCountry4T1(Long companyId) {
		String sql = "select sa.id from sys_area sa where  sa.type < 3 and parentId <> 0 and sa.delFlag = 0";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	
	
	/* (non-Javadoc)
	 * travelActivity  产品实体类
	 * settlementAdultPriceStart  查询条件  最低同行价
	 * settlementAdultPriceEnd    查询条件  最高同行价
	 * common   查询条件   根据权限不同 产品的查询范围不同
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findTravelActivity(com.trekiz.admin.common.persistence.Page, com.trekiz.admin.modules.activity.entity.TravelActivity, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page, TravelActivity travelActivity, String settlementAdultPriceStart,
			String settlementAdultPriceEnd, DepartmentCommon common) {
		
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		
		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		if(travelActivity.getTravelTypeId()!=null){
			dc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		if(travelActivity.getIsAfterSupplement()!=null){
			dc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		//产品种类
		if(travelActivity.getActivityKind()!=null){
			dc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
		}
		//货币种类
		if(travelActivity.getCurrencyType()!=null){
			//下面一行代码之前被注释，修复bug15047时打开,如果是单团类产品
			if(travelActivity.getActivityKind().equals(Context.PRODUCT_TYPE_DAN_TUAN)) {
				dc.add(Restrictions.eq("currencyType", travelActivity.getCurrencyType()));//以前用这个
			}
		}
		if(travelActivity.getRemainDays()!=null){
			dc.add(Restrictions.eq("remainDays", travelActivity.getRemainDays()));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)){
			dc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (" + targetAreaIds + ") )"));
		}
		if(StringUtils.isNotBlank(travelActivity.getActivitySerNum())){
			dc.add(Restrictions.like("activitySerNum", "%"+travelActivity.getActivitySerNum()+"%"));
		}
		if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
			dc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		if(travelActivity.getActivityLevelId()!=null){
			dc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if(travelActivity.getActivityTypeId()!=null){
			dc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		if(travelActivity.getActivityDuration()!=null){
			dc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}

//		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
//			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName().trim()+"%"));
//		}
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
			String acitivityName = travelActivity.getAcitivityName();
			acitivityName = acitivityName.replace("'", "''");
			acitivityName = acitivityName.replace("%", "\\%");
			acitivityName = acitivityName.replace("_", "\\_");
			dc.add(Restrictions.or(
					Restrictions.like("acitivityName", "%"+acitivityName.trim()+"%"),
					Restrictions.like("activitySerNum", "%"+acitivityName.trim()+"%"),
//					Restrictions.sqlRestriction("{alias}.acitivityName in (select acitivityName from travelactivity where acitivityName like '%"+acitivityName.trim()+"%')"),
					Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitygroup where groupcode like '%"+acitivityName.trim()+"%')")));
		}

		//领队
		if(StringUtils.isNotBlank(travelActivity.getGroupLead())) {
			dc.add(Restrictions.eq("groupLead", travelActivity.getGroupLead()));
		}
		//计调(创建人)
		if(travelActivity.getCreateBy() != null) {
			List<User> userList = UserUtils.getUserListByName(travelActivity.getCreateBy().getName());
			for(User u : userList) {
				dc.add(Restrictions.eq("createBy", u));
			}
		}
		//询价销售
		if(travelActivity.getEstimatePriceRecord() != null && StringUtils.isNotBlank(travelActivity.getEstimatePriceRecord().getUserName())) {
			dc.add(Restrictions.sqlRestriction("{alias}.estimate_price_id in (select id from estimate_price_record where user_name = '" + travelActivity.getEstimatePriceRecord().getUserName() + "')"));
		}
//		if(travelActivity.getActivityKind() != null){
//			dc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
//		}
		if(travelActivity.getFromArea()!=null){
		    dc.add(Restrictions.eq("fromArea", travelActivity.getFromArea()));
		}
		
		if(travelActivity.getActivityStatus()!=null){
			dc.add(Restrictions.eq("activityStatus", travelActivity.getActivityStatus()));
		}
		
		//机票产品航空公司
		if(null != travelActivity.getActivityAirTicket()) {
			String airlines = travelActivity.getActivityAirTicket().getAirlines();
			if(StringUtils.isNotBlank(airlines)) {
				dc.add(Restrictions.sqlRestriction("{alias}.airticket_id in (select airticketId from activity_flight_info where airlines like '%" + airlines + "%')"));
			}
		}
		
		//游轮产品返回城市
		if(null != travelActivity.getBackArea()) {
			dc.add(Restrictions.eq("backArea", travelActivity.getBackArea()));
		}
		
		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());
		
		boolean initflag = false;
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		//筛选没有删除的团期
		subgroups.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));

		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			initflag = true;
			subgroups.add(Restrictions.like("currencyType", travelActivity.getCurrencyType() + "%"));//根据选择的币种筛选
			subgroups.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if(StringUtils.isNotBlank(settlementAdultPriceEnd)){
			initflag = true;
			subgroups.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null){
			initflag = true;
			subgroups.add(Restrictions.between("groupOpenDate",travelActivity.getGroupOpenDate(),travelActivity.getGroupCloseDate()));
		}
		//------------考虑出团日期部分有值的情况-s---//
		   if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()==null){
			   initflag = true;
			   subgroups.add(Restrictions.ge("groupOpenDate",travelActivity.getGroupOpenDate() ));
		   }
		   if(travelActivity.getGroupOpenDate()==null && travelActivity.getGroupCloseDate()!=null){
			   initflag = true;
			   subgroups.add(Restrictions.le("groupCloseDate",travelActivity.getGroupCloseDate() ));
		   }
		//------------考虑出团日期部分有值的情况-e---//
		if(initflag){
			subgroups.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(subgroups));
		}
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.desc("groupOpenDate"));
		}
	
		return travelActivityDao.find(page, dc);
	}


	/* (non-Javadoc)
	 * @param travelActivity  产品实体类
	 * @param settlementAdultPriceStart  查询条件  最低同行价
	 * @param settlementAdultPriceEnd    查询条件  最高同行价
	 * @param common   查询条件   根据权限不同 产品的查询范围不同
	 * @param groundingStatus  查询条件  T1上架状态
	 */
	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page, TravelActivity travelActivity, String settlementAdultPriceStart,
												   String settlementAdultPriceEnd, DepartmentCommon common, String groundingStatus) {

		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();

		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		if(travelActivity.getTravelTypeId()!=null){
			dc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		if(travelActivity.getIsAfterSupplement()!=null){
			dc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		//产品种类
		if(travelActivity.getActivityKind()!=null){
			dc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
		}
		//货币种类
		if(travelActivity.getCurrencyType()!=null){
			//下面一行代码之前被注释，修复bug15047时打开,如果是单团类产品
			if(travelActivity.getActivityKind().equals(Context.PRODUCT_TYPE_DAN_TUAN)) {
				dc.add(Restrictions.eq("currencyType", travelActivity.getCurrencyType()));//以前用这个
			}
		}
		if(travelActivity.getRemainDays()!=null){
			dc.add(Restrictions.eq("remainDays", travelActivity.getRemainDays()));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)){
			dc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (" + targetAreaIds + ") )"));
		}
		if(StringUtils.isNotBlank(travelActivity.getActivitySerNum())){
			dc.add(Restrictions.like("activitySerNum", "%"+travelActivity.getActivitySerNum()+"%"));
		}
		if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
			dc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		if(travelActivity.getActivityLevelId()!=null){
			dc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if(travelActivity.getActivityTypeId()!=null){
			dc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		if(travelActivity.getActivityDuration()!=null){
			dc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}

//		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
//			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName().trim()+"%"));
//		}

		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
			String acitivityName = travelActivity.getAcitivityName();
			acitivityName = acitivityName.replace("'", "''");
			acitivityName = acitivityName.replace("%", "\\%");
			acitivityName = acitivityName.replace("_", "\\_");
			dc.add(Restrictions.or(Restrictions.like("acitivityName", "%"+acitivityName.trim()+"%"),
					Restrictions.like("activitySerNum", "%"+acitivityName.trim()+"%"),
					Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitygroup where groupcode like '%"+acitivityName.trim()+"%')")));
		}

		//518需求，上架状态查询
		if ("1".equals(groundingStatus)){  //未上架
			dc.add(Restrictions.sqlRestriction("{alias}.id in (SELECT srcActivityId FROM activitygroup WHERE is_t1 = '0' AND (quauqAdultPrice IS NULL AND quauqChildPrice IS NULL AND quauqSpecialPrice IS NULL) AND delFlag = 0)"));
		}else if("2".equals(groundingStatus)){  //已上架
			dc.add(Restrictions.sqlRestriction("{alias}.id in (SELECT srcActivityId FROM activitygroup WHERE is_t1 = '1' AND delFlag = 0)"));
		}else if ("3".equals(groundingStatus)){   //已下架
			dc.add(Restrictions.sqlRestriction("{alias}.id in (SELECT srcActivityId FROM activitygroup WHERE is_t1 = '0' AND (quauqAdultPrice IS NOT NULL OR quauqChildPrice IS NOT NULL OR quauqSpecialPrice IS NOT NULL) AND delFlag = 0)"));
		}

		//领队
		if(StringUtils.isNotBlank(travelActivity.getGroupLead())) {
			dc.add(Restrictions.eq("groupLead", travelActivity.getGroupLead()));
		}
		//计调(创建人)
		if(travelActivity.getCreateBy() != null) {
			List<User> userList = UserUtils.getUserListByName(travelActivity.getCreateBy().getName());
			for(User u : userList) {
				dc.add(Restrictions.eq("createBy", u));
			}
		}
		//询价销售
		if(travelActivity.getEstimatePriceRecord() != null && StringUtils.isNotBlank(travelActivity.getEstimatePriceRecord().getUserName())) {
			dc.add(Restrictions.sqlRestriction("{alias}.estimate_price_id in (select id from estimate_price_record where user_name = '" + travelActivity.getEstimatePriceRecord().getUserName() + "')"));
		}
//		if(travelActivity.getActivityKind() != null){
//			dc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
//		}
		if(travelActivity.getFromArea()!=null){
			dc.add(Restrictions.eq("fromArea", travelActivity.getFromArea()));
		}

		if(travelActivity.getActivityStatus()!=null){
			dc.add(Restrictions.eq("activityStatus", travelActivity.getActivityStatus()));
		}

		//机票产品航空公司
		if(null != travelActivity.getActivityAirTicket()) {
			String airlines = travelActivity.getActivityAirTicket().getAirlines();
			if(StringUtils.isNotBlank(airlines)) {
				dc.add(Restrictions.sqlRestriction("{alias}.airticket_id in (select airticketId from activity_flight_info where airlines like '%" + airlines + "%')"));
			}
		}

		//游轮产品返回城市
		if(null != travelActivity.getBackArea()) {
			dc.add(Restrictions.eq("backArea", travelActivity.getBackArea()));
		}

		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());

		boolean initflag = false;
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		//筛选没有删除的团期
		subgroups.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));

		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			initflag = true;
			subgroups.add(Restrictions.like("currencyType", travelActivity.getCurrencyType() + "%"));//根据选择的币种筛选
			subgroups.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if(StringUtils.isNotBlank(settlementAdultPriceEnd)){
			initflag = true;
			subgroups.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null){
			initflag = true;
			subgroups.add(Restrictions.between("groupOpenDate",travelActivity.getGroupOpenDate(),travelActivity.getGroupCloseDate()));
		}
		//------------考虑出团日期部分有值的情况-s---//
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()==null){
			initflag = true;
			subgroups.add(Restrictions.ge("groupOpenDate",travelActivity.getGroupOpenDate() ));
		}
		if(travelActivity.getGroupOpenDate()==null && travelActivity.getGroupCloseDate()!=null){
			initflag = true;
			subgroups.add(Restrictions.le("groupCloseDate",travelActivity.getGroupCloseDate() ));
		}
		//518需求，处理上架状态搜索
		/*if("1".equals(groundingStatus)){   //未上架
			initflag = true;
			subgroups.add(Restrictions.eq("isT1",0));
			subgroups.add(Restrictions.isNull("quauqAdultPrice"));
			subgroups.add(Restrictions.isNull("quauqChildPrice"));
			subgroups.add(Restrictions.isNull("quauqSpecialPrice"));

		}else if ("2".equals(groundingStatus)){  //已上架
			initflag = true;
			subgroups.add(Restrictions.eq("isT1",1));
		}else if ("3".equals(groundingStatus)){   //已下架
			initflag = true;
			subgroups.add(Restrictions.eq("isT1",0));
			subgroups.add(Restrictions.isNotNull("quauqAdultPrice"));
			subgroups.add(Restrictions.isNotNull("quauqChildPrice"));
			subgroups.add(Restrictions.isNotNull("quauqSpecialPrice"));
		}*/


		//------------考虑出团日期部分有值的情况-e---//
		if(initflag){
			subgroups.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(subgroups));
		}
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.desc("groupOpenDate"));
			page.setOrderBy("groupOpenDate DESC");
		}

		return travelActivityDao.find(page, dc);
	}

	/*
	 * (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findTravelActivity(com.trekiz.admin.common.persistence.Page, com.trekiz.admin.modules.activity.entity.TravelActivity, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page, TravelActivity travelActivity,String settlementAdultPriceStart,
			String settlementAdultPriceEnd,Long agentId, DepartmentCommon common) {
		
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		
		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null) {
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		if(travelActivity.getTravelTypeId()!=null) {
			dc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)) {
			dc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (?) )",targetAreaIds,StringType.INSTANCE));
		}
		if(StringUtils.isNotBlank(travelActivity.getTrafficName())) {
			dc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		if(travelActivity.getIsAfterSupplement()!=null) {
			dc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		if(travelActivity.getActivityLevelId()!=null) {
			dc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if(travelActivity.getActivityTypeId()!=null) {
			dc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		if(travelActivity.getActivityDuration()!=null) {
			dc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())) {
			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"));
		}
		
		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());
		
		boolean initflag = false;
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		//筛选没有删除的团期
		subgroups.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));
		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			initflag = true;
			subgroups.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if(StringUtils.isNotBlank(settlementAdultPriceEnd)){
			initflag = true;
			subgroups.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null) {
			initflag = true;
			subgroups.add(Restrictions.between("groupOpenDate",travelActivity.getGroupOpenDate(),travelActivity.getGroupCloseDate()));
		}
		if(initflag) {
			subgroups.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(subgroups));
		}
		if(agentId!=null) {
			DetachedCriteria reserves = DetachedCriteria.forClass(ActivityGroupReserve.class, "reserves");
			reserves.add(Restrictions.eq("agentId", agentId));
			reserves.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(reserves));
		}
		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}
		return travelActivityDao.find(page, dc);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findTravelActivitysByCode(java.lang.String)
	 */
	@Override
	public Long findTravelActivitysByCode(String groupCode){
		
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		subgroups.add(Restrictions.eq("groupCode", groupCode));
		subgroups.setProjection(Property.forName("srcActivityId"));
		dc.add(Property.forName("id").in(subgroups));
		return travelActivityDao.count(dc);
	}
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#findActivity(java.lang.String, java.lang.Long)
	 */
	@Override
	public List<TravelActivity> findActivity(String activitySerNum,Long proId) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		return travelActivityDao.findActivity(companyId,activitySerNum,proId);
	}

	@Override
	public List<TravelActivity> findActivityByCompany(Long companyId, boolean lazy) {
		List<TravelActivity> list = travelActivityDao.findActivityByCompany(companyId);
		if(!lazy) {
			if(list != null && list.size() > 0) {
				for(TravelActivity activity : list) {
					Hibernate.initialize(activity.getActivityGroups());
					Hibernate.initialize(activity.getTargetAreaList());
				}
			}
		} 
		return list;
	}
	
	@Override
	public List<TravelActivity> findActivityByCompanyIgnoreDeleteFlag(Long companyId) {
		return travelActivityDao.findActivityByCompanyIgnoreDeleteFlag(companyId);
	}
	
	@Override
	public Page<TravelActivity> findActivityByCompany(
			Page<TravelActivity> page, TravelActivity travelActivity) {
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		Long companyId = travelActivity.getProCompany();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		Integer isSuccess = travelActivity.getIsSyncSuccess();
		if(isSuccess != 3 && isSuccess != -3){
			dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		}
		if(isSuccess != null){
			dc.add(Restrictions.eq("isSyncSuccess", isSuccess));
		}
		return travelActivityDao.find(page, dc);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
    public Boolean updateTravelActivityWithSyncStatus(Long activityId,
            Integer status) {
		int result = travelActivityDao.updateBySql("update travelactivity set is_sync_success=? where id=?", status, activityId);
	    if(result == 1){
	    	return Boolean.TRUE;
	    }else{
	    	return Boolean.FALSE;
	    }
    }

	@Override
	public String modSave(String groupdata,
			TravelActivity travelActivity, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(TravelActivity travelActivity, String groupOpenDateBegin,
			String groupCloseDateEnd, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes, boolean is_after_supplement) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#batchOnActivityTmp(java.util.List)
	 */
	@Override
    public void batchOnActivityTmp(List<Long> ids) {
		
		this.travelActivityDao.batchOnOrOffActivity(ids, Integer.valueOf(Context.PRODUCT_ONLINE_STATUS));
    }

	@Override
    public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page,
            TravelActivity travelActivity, String settlementAdultPriceStart,
            String settlementAdultPriceEnd, List<Integer> state) {

//		DetachedCriteria dc = DetachedCriteria.forClass(TravelActivity.class);
		
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		
//		DetachedCriteria child = dc.createCriteria("activityGroups", Criteria.LEFT_JOIN);
		
		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		if(travelActivity.getTravelTypeId()!=null){
			dc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)){
			dc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (?) )",targetAreaIds,StringType.INSTANCE));
		}
		if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
			dc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		if(travelActivity.getIsAfterSupplement()!=null){
			dc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		if(travelActivity.getActivityLevelId()!=null){
			dc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if(travelActivity.getActivityTypeId()!=null){
			dc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		if(travelActivity.getActivityDuration()!=null){
			dc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}
		if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
			dc.add(Restrictions.like("acitivityName", "%"+travelActivity.getAcitivityName()+"%"));
		}
		boolean initflag = false;
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		subgroups.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		if(state!=null){
			subgroups.add(Restrictions.in("costStatus", state));
            initflag = true;
		}
		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			initflag = true;
			subgroups.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if(StringUtils.isNotBlank(settlementAdultPriceEnd)){
			initflag = true;
			subgroups.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null){
			initflag = true;
			subgroups.add(Restrictions.between("groupOpenDate",travelActivity.getGroupOpenDate(),travelActivity.getGroupCloseDate()));
		}
		if(initflag){
			subgroups.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(subgroups));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.desc("id"));
		}
		return travelActivityDao.find(page, dc);
    }

	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page, TravelActivity travelActivity, 
			DepartmentCommon common, Map<String, String> map) {
		
		DetachedCriteria dc = travelActivityDao.createDetachedCriteria();
		//没有删除的数据
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		String wholeSalerKey = map.get("wholeSalerKey");
		if(StringUtils.isNotBlank(wholeSalerKey)){
			dc.add(Restrictions.or(Restrictions.like("acitivityName", "%" + wholeSalerKey + "%"),
					Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitygroup where groupcode like '%" + wholeSalerKey + "%')")));
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != null){
			dc.add(Restrictions.eq("proCompany", companyId));
		}
		//旅游类型

		if(travelActivity.getTravelTypeId()!=null){
			dc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		if(travelActivity.getIsAfterSupplement()!=null){
			dc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		//产品种类
		if(travelActivity.getActivityKind()!=null){
			dc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
		}
		//货币种类
		if(travelActivity.getCurrencyType()!=null){
			dc.add(Restrictions.eq("currencyType", travelActivity.getCurrencyType()));
		}
		// 保留天数
		if (StringUtils.isNoneBlank(map.get("remainDays"))) {
			dc.add(Restrictions.eq("remainDays", map.get("remainDays")));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if(StringUtils.isNotBlank(targetAreaIds)){
			dc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (" + targetAreaIds + ") )"));
		}
		if(StringUtils.isNotBlank(travelActivity.getActivitySerNum())){
			dc.add(Restrictions.like("activitySerNum", "%"+travelActivity.getActivitySerNum()+"%"));
		}
		if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
			dc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		if(travelActivity.getActivityLevelId()!=null){
			dc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if(travelActivity.getActivityTypeId()!=null){
			dc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		//行程天数
		if(travelActivity.getActivityDuration()!=null){
			dc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}
		//出发城市
		if(travelActivity.getFromArea()!=null){
		    dc.add(Restrictions.eq("fromArea", travelActivity.getFromArea()));
		}
		
		if(travelActivity.getActivityStatus()!=null){
			dc.add(Restrictions.eq("activityStatus", travelActivity.getActivityStatus()));
		}
		//操作人（计调、产品发布人）
		String activityCreate = map.get("activityCreate");
		if(StringUtils.isNotBlank(activityCreate) && !"-99999".equals(activityCreate)){
			dc.add(Restrictions.eq("createBy.id", Long.valueOf(activityCreate)));
		}
		//操作人（计调、产品发布人） Calendar
		String activityCreateCalendar = map.get("activityCreateCalendar");
		if(StringUtils.isNotBlank(activityCreateCalendar) && !"-99999".equals(activityCreateCalendar)){
			dc.add(Restrictions.eq("createBy.id", Long.valueOf(activityCreateCalendar)));
		}
		
		systemService.getDepartmentSql("activity", dc, null, common, travelActivity.getActivityKind());
		
		boolean initflag = false;
		DetachedCriteria subgroups = DetachedCriteria.forClass(ActivityGroup.class, "groups");
		//筛选没有删除的团期
		subgroups.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));
		String settlementAdultPriceStart = map.get("settlementAdultPriceStart");
		String settlementAdultPriceEnd = map.get("settlementAdultPriceEnd");
		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			initflag = true;
			subgroups.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			initflag = true;
			subgroups.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		//出团日期开始时间
		if (travelActivity.getGroupOpenDate() != null) {
			initflag = true;
			subgroups.add(Restrictions.ge("groupOpenDate", travelActivity.getGroupOpenDate()));
		}
		//出团日期结束时间
		if (travelActivity.getGroupCloseDate() != null) {
			initflag = true;
			subgroups.add(Restrictions.le("groupOpenDate", travelActivity.getGroupCloseDate()));
		}
		//舱型
		String spaceType = map.get("spaceType");
		if(StringUtils.isNotBlank(spaceType)){
			initflag = true;
			subgroups.add(Restrictions.eq("spaceType", Long.valueOf(spaceType)));
		}
		//是否还有余位
		String haveYw = map.get("haveYw");
		if (StringUtils.isNotBlank(haveYw)) {
			initflag = true;
			if ("1".equals(haveYw)) {
				subgroups.add(Restrictions.and(Restrictions.ne("freePosition", 0), Restrictions.isNotNull("freePosition")));
			} else {
				subgroups.add(Restrictions.or(Restrictions.eq("freePosition", 0), Restrictions.isNull("freePosition")));
			}
			
		}
		//是否还有切位
		String haveQw = map.get("haveQw");
		if (StringUtils.isNotBlank(haveQw)) {
			initflag = true;
			if ("1".equals(haveQw)) {
				subgroups.add(Restrictions.and(Restrictions.ne("payReservePosition", 0), Restrictions.isNotNull("payReservePosition")));
			} else {
				subgroups.add(Restrictions.or(Restrictions.eq("payReservePosition", 0), Restrictions.isNull("payReservePosition")));
			}
			
		}
		if (StringUtils.isNotBlank(spaceType)) {
			initflag = true;
			subgroups.add(Restrictions.eq("spaceType", Long.valueOf(spaceType)));
		}
		if(initflag){
			subgroups.setProjection(Property.forName("srcActivityId"));
			dc.add(Property.forName("id").in(subgroups));
		}
		
		// 产品系列取值（267需求）
		if (Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid()) && map.get("productType") == null) {
			List<TravelActivity> list = travelActivityDao.find(dc);
			Set<Integer> productType = Sets.newHashSet();
			for (TravelActivity activity : list) {
				if (activity.getActivityLevelId() != null) {
					productType.add(activity.getActivityLevelId());
				}
			}
			StringBuffer typeStr = new StringBuffer("");
			for (Integer type : productType) {
				typeStr.append(type.toString() + ",");
			}
			map.put("productType", typeStr.toString());
		}
		
		return travelActivityDao.find(page, dc);
	}
	
	public Page<ActivityGroup> findActivityGroup(
			Page<ActivityGroup> page, TravelActivity travelActivity, DepartmentCommon common, Map<String,String> map) {
		
		DetachedCriteria dc = activityGroupDao.createDetachedCriteria();
		
		//筛选没有删除的团期
		dc.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));
		String settlementAdultPriceStart = map.get("settlementAdultPriceStart");
		String settlementAdultPriceEnd = map.get("settlementAdultPriceEnd");
		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			dc.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			dc.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		//出团日期开始时间
		if (travelActivity.getGroupOpenDate() != null) {
			dc.add(Restrictions.ge("groupOpenDate", travelActivity.getGroupOpenDate()));
		}
		//出团日期结束时间
		if (travelActivity.getGroupCloseDate() != null) {
			dc.add(Restrictions.le("groupOpenDate", travelActivity.getGroupCloseDate()));
		}
		//舱型
		String spaceType = map.get("spaceType");
		if(StringUtils.isNotBlank(spaceType)){
			dc.add(Restrictions.eq("spaceType", Long.valueOf(spaceType)));
		}
		//是否还有余位
		String haveYw = map.get("haveYw");
		if (StringUtils.isNotBlank(haveYw)) {
			if ("1".equals(haveYw)) {
				dc.add(Restrictions.and(Restrictions.ne("freePosition", 0), Restrictions.isNotNull("freePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("freePosition", 0), Restrictions.isNull("freePosition")));
			}
			
		}
		//是否还有切位
		String haveQw = map.get("haveQw");
		if (StringUtils.isNotBlank(haveQw)) {
			if ("1".equals(haveQw)) {
				dc.add(Restrictions.and(Restrictions.ne("payReservePosition", 0), Restrictions.isNotNull("payReservePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("payReservePosition", 0), Restrictions.isNull("payReservePosition")));
			}
			
		}
		if (StringUtils.isNotBlank(spaceType)) {
			dc.add(Restrictions.eq("spaceType", Long.valueOf(spaceType)));
		}
		
		DetachedCriteria productDc = DetachedCriteria.forClass(TravelActivity.class);
		
		//没有删除的数据
		productDc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		String wholeSalerKey = map.get("wholeSalerKey");
		if (StringUtils.isNotBlank(wholeSalerKey)) {
			dc.add(Restrictions.or(Restrictions.like("groupCode", "%" + wholeSalerKey + "%"),
				Restrictions.sqlRestriction("{alias}.srcActivityId in (select id from travelactivity where acitivityName like '%" + wholeSalerKey + "%')")));
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (companyId != null) {
			productDc.add(Restrictions.eq("proCompany", companyId));
		}
		// 旅游类型
		if (travelActivity.getTravelTypeId() != null) {
			productDc.add(Restrictions.eq("travelTypeId", travelActivity.getTravelTypeId()));
		}
		if (travelActivity.getIsAfterSupplement() != null) {
			productDc.add(Restrictions.eq("isAfterSupplement", travelActivity.getIsAfterSupplement()));
		}
		// 产品种类
		if (travelActivity.getActivityKind() != null) {
			productDc.add(Restrictions.eq("activityKind", travelActivity.getActivityKind()));
		}
		// 货币种类
		if (travelActivity.getCurrencyType() != null) {
			productDc.add(Restrictions.eq("currencyType", travelActivity.getCurrencyType()));
		}
		// 保留天数
		if (StringUtils.isNoneBlank(map.get("remainDays"))) {
			productDc.add(Restrictions.eq("remainDays", map.get("remainDays")));
		}
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if (StringUtils.isNotBlank(targetAreaIds)) {
			productDc.add(Restrictions.sqlRestriction("{alias}.id in (select srcActivityId from activitytargetarea where targetAreaId in (" + targetAreaIds + ") )"));
		}
		if (StringUtils.isNotBlank(travelActivity.getActivitySerNum())) {
			productDc.add(Restrictions.like("activitySerNum", "%"+travelActivity.getActivitySerNum()+"%"));
		}
		if (StringUtils.isNotBlank(travelActivity.getTrafficName())) {
			productDc.add(Restrictions.eq("trafficName", travelActivity.getTrafficName()));
		}
		// 产品系列
		if (travelActivity.getActivityLevelId() != null) {
			productDc.add(Restrictions.eq("activityLevelId", travelActivity.getActivityLevelId()));
		}
		if (travelActivity.getActivityTypeId() != null) {
			productDc.add(Restrictions.eq("activityTypeId", travelActivity.getActivityTypeId()));
		}
		// 行程天数
		if (travelActivity.getActivityDuration() != null) {
			productDc.add(Restrictions.eq("activityDuration", travelActivity.getActivityDuration()));
		}
		// 出发城市
		if (travelActivity.getFromArea() != null) {
			productDc.add(Restrictions.eq("fromArea", travelActivity.getFromArea()));
		}
		if (travelActivity.getActivityStatus() != null) {
			productDc.add(Restrictions.eq("activityStatus", travelActivity.getActivityStatus()));
		}
		// 操作人（计调、产品发布人）
		String activityCreate = map.get("activityCreate");
		if (StringUtils.isNotBlank(activityCreate) && !"-99999".equals(activityCreate)) {
			productDc.add(Restrictions.eq("createBy.id", Long.valueOf(activityCreate)));
		}
		//操作人（计调、产品发布人） Calendar
		String activityCreateCalendar = map.get("activityCreateCalendar");
		if (StringUtils.isNotBlank(activityCreateCalendar) && !"-99999".equals(activityCreateCalendar)) {
			productDc.add(Restrictions.eq("createBy.id", Long.valueOf(activityCreateCalendar)));
		}
		systemService.getDepartmentSql("activity", productDc, null, common, travelActivity.getActivityKind());
		
		productDc.setProjection(Property.forName("id"));
		dc.add(Property.forName("srcActivityId").in(productDc));
		
		// 产品系列取值（267需求）
		if (Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid()) && map.get("productType") == null) {
			List<ActivityGroup> list = activityGroupDao.find(dc);
			Set<Integer> productType = Sets.newHashSet();
			for (ActivityGroup group : list) {
				if (group.getTravelActivity().getActivityLevelId() != null) {
					productType.add(group.getTravelActivity().getActivityLevelId());
				}
			}
			StringBuffer typeStr = new StringBuffer("");
			for (Integer type : productType) {
				typeStr.append(type.toString() + ",");
			}
			map.put("productType", typeStr.toString());
		}
		
		return activityGroupDao.find(page, dc);
	}
	
	@Override
	public List<ActivityGroup> findGroupsByActivityId(TravelActivity travelActivity, Map<String,String> map) {
		
		DetachedCriteria dc = activityGroupDao.createDetachedCriteria();
		dc.add(Restrictions.eq("srcActivityId", travelActivity.getId().intValue()));
		//筛选没有删除的团期
		dc.add(Restrictions.eq("delFlag", TravelActivity.DEL_FLAG_NORMAL));
		String settlementAdultPriceStart = map.get("settlementAdultPriceStart");
		String settlementAdultPriceEnd = map.get("settlementAdultPriceEnd");
		if (StringUtils.isNotBlank(settlementAdultPriceStart)){
			dc.add(Restrictions.ge("settlementAdultPrice", new BigDecimal(settlementAdultPriceStart)));
		}
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			dc.add(Restrictions.le("settlementAdultPrice",new BigDecimal(settlementAdultPriceEnd)));
		}
		//出团日期开始时间
		if (travelActivity.getGroupOpenDate() != null) {
			dc.add(Restrictions.ge("groupOpenDate", travelActivity.getGroupOpenDate()));
		}
		//出团日期结束时间
		if (travelActivity.getGroupCloseDate() != null) {
			dc.add(Restrictions.le("groupOpenDate", travelActivity.getGroupCloseDate()));
		}
		//舱型
		String spaceType = map.get("spaceType");
		if(StringUtils.isNotBlank(spaceType)){
			dc.add(Restrictions.eq("spaceType", Long.valueOf(spaceType)));
		}
		//是否还有余位
		String haveYw = map.get("haveYw");
		if (StringUtils.isNotBlank(haveYw)) {
			if ("1".equals(haveYw)) {
				dc.add(Restrictions.and(Restrictions.ne("freePosition", 0), Restrictions.isNotNull("freePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("freePosition", 0), Restrictions.isNull("freePosition")));
			}
			
		}
		//是否还有切位
		String haveQw = map.get("haveQw");
		if (StringUtils.isNotBlank(haveQw)) {
			if ("1".equals(haveQw)) {
				dc.add(Restrictions.and(Restrictions.ne("payReservePosition", 0), Restrictions.isNotNull("payReservePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("payReservePosition", 0), Restrictions.isNull("payReservePosition")));
			}
			
		}
		//按出团日期升序排列
		dc.addOrder(Order.asc("groupOpenDate"));
		return activityGroupDao.find(dc);
	}

	@Override
	public List<Map<String, Object>> findCountryAreaIds(Long companyId) {
		// TODO Auto-generated method stub
		String sql = "select sa.id from sys_area sa inner join userdefinedict u  on sa.id = u.dictId where sa.type='2' and   u.companyId='"+companyId+"' and u.type='area'";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	
	/**
	 * 查找团号生成规则(新行者需求)
	 * @author jiachen
	 * @DateTime 2015年3月5日 下午12:02:37
	 * @return List<Object[]>
	 */
	@Override
	public List<Object[]> findGroupCodeRule() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ActivityGroup> getYwByGroupIds(List<Long> groupIdList) {
		return null;
	}

	/* 
	 * add by yunpeng.zhang
	 * 通过团期 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#getAllLeftpayReservePosition(java.lang.Long)
	 */
	@Override
	public Integer getAllLeftpayReservePosition(Long activityGroupId, Long activityId) {
		List<ActivityGroupReserve> activityGroupReserveList = activityGroupReserveDao.findByActivityGroupIdAndSrcActivityId(activityGroupId, activityId);
		Integer totalNum = 0;
		for (ActivityGroupReserve activityGroupReserve : activityGroupReserveList) {
			Integer leftpayReservePosition = activityGroupReserve.getLeftpayReservePosition();
			totalNum += leftpayReservePosition;
		}
		return totalNum;
	}

	@Override
	public void delCruiseshipStockGrooupRelByActivityIds(List<Long> idlist) {
		for (Long id : idlist) {
			try {
				travelActivityDao.updateBySql("update cruiseship_stock_group_rel set STATUS=1 where activity_id=?",id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
    /**
      * c460--下架取消关联状态
      * 通过团期id更新表cruiseship_stock_group_rel的关联状态
     */
	@Override
	public void updateCruiseshipRelStatusByActivityId(List<Long> idlist) {
		for (Long id : idlist) {
			try {
				travelActivityDao.updateBySql("update cruiseship_stock_group_rel csgr set csgr.STATUS=1 where csgr.activity_id="+id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//-----------t1t2需求-----------s--//
	@Override
	public Page<TravelActivity> findActivityGroupInfos(Page<TravelActivity> page, TravelActivity travelActivity,
			String keywordSearching, String fromArea, String targetAreaIdList, String supplier, String groupOpenDateBegin, String groupOpenDateEnd, String activityDurationFrom, String activityDurationTo,
			String pricePara, String freePositionFrom, String freePostionTo,String countryPara,String pageNo,String pageSize,String orderBy, String type) {
		//当前日期
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String curDate=sdf.format(new Date());
		StringBuffer sql=new StringBuffer();
		sql.append("  SELECT  ")
		   .append("  t.id AS 'travelactivity_id',") //产品主键id
		   .append("  a.id AS 'activitygroup_id', ")//团期主键id
		   .append("  a.groupCode, ")//团号
		   .append("  a.groupOpenDate AS 'groupOpenDate', ")//团期的出团日期(一个产品可以有多个团期)
		   .append(" t.activityDuration AS 'activityDuration', ") //行程天数
		   .append(" t.acitivityName AS 'activityName', ") //产品名称
		   .append(" a.freePosition AS 'freePosition', ")  //余位
		   .append(" a.currency_type AS 'currencyids', ") //包含表中所有币种的id,逗号分隔
		   .append(" a.quauqAdultPrice AS 'quauqPrice', ")   //quauq价(仅仅是数额)
		   .append(" a.suggestAdultPrice AS 'suggestAdultPrice', ") //建议直客价
			.append(" a.settlementAdultPrice,")//同行价
			.append(" t.fromArea AS 'fromArea', ")   //出发城市,从发布来看,出发城市只有一个
		   .append(" ata.sys_area_id AS 'targetArea', ")//到达城市ids,逗号分隔
		  // .append(" a.createBy   AS  'sys_user_id', ")  //供应商对应的主键id
		   .append(" sup.sys_office_id AS 'sys_office_id', ")//供应商对应的主键id
		   .append(" sup.supplierName AS 'supplierName', ") //供应商名称
				.append("sup.charge_rate chargeRate,")
				.append("a.quauqAdultPrice/a.settlementAdultPrice as defaultSort ")
				.append(" FROM activitygroup a ")
		   .append(" LEFT JOIN travelactivity t ")
		   .append(" ON a.srcActivityId=t.id ")
		   .append(" LEFT JOIN  ")
		   .append("  ( ")
		   .append(" SELECT  ")
		   .append(" su.id AS 'createBy', ")
		   .append(" so.id AS 'sys_office_id', ")//供应商的id
		   .append(" so.name AS 'supplierName', ")
		   .append(" so.shelfRightsStatus AS 'shelfRightsStatus', ")//批发商的上架状态
		   .append(" su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")//用户的报名权限
				.append(" so.charge_rate ")
				.append(" FROM sys_user su ")
		   .append(" LEFT JOIN sys_office so ")
		   .append(" ON su.companyId=so.id WHERE so.delFlag=0 ")
		   .append(" )sup ")
		   .append(" ON sup.createBy=a.createBy ")
		   .append(" LEFT JOIN ")//将产品和目的表进行组装
		   .append(" (SELECT ")
		   .append(" a.srcActivityId AS 'travelactivity_id', ")
		   .append(" GROUP_CONCAT(a.targetAreaId) AS 'sys_area_id', ")
				.append(" GROUP_CONCAT(a.name) name, ")
				.append(" GROUP_CONCAT(sa.parentId) parentId, ")
				.append(" GROUP_CONCAT(a.parentName) parentName, ")
				.append(" GROUP_CONCAT(sa.parentIds) parentIds ")
//				.append(" FROM activitytargetarea_view a ")
				.append(" FROM (").append(getLooseActivityTargetAreaSql(curDate)).append(") a ")
				.append(" LEFT JOIN sys_area sa on a.targetAreaId = sa.id ");

		if(StringUtils.isNotBlank(targetAreaIdList) || StringUtils.isNotBlank(keywordSearching) || StringUtils.isNotBlank(countryPara)) {
			sql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(targetAreaIdList)){
				sql.append("  and a.targetAreaId IN ("+targetAreaIdList.substring(0, targetAreaIdList.length()-1)+") ");
			}
			if ("产品名称 / 供应商 / 团号 / 目的地".equals(keywordSearching)) {
				keywordSearching = "";
			}
//			if(StringUtils.isNotBlank(keywordSearching)) {
//				if(areaService.isExist(keywordSearching)) {
//					sql.append("  and (FIND_IN_SET('"+keywordSearching+"',a.name) or FIND_IN_SET('"+keywordSearching+"',a.parentName)) ");
//				}
//			}
			if(StringUtils.isNotBlank(countryPara)) {
				sql.append("  and (");
				String[] countryArr = countryPara.split(",");
				for (int i = 0; i < countryArr.length; i++) {
					String country = countryArr[i];
					if (i != countryArr.length - 1) {
						sql.append(" FIND_IN_SET('"+country+"',parentId) OR FIND_IN_SET('"+country+"', targetAreaId) or ");
					} else {
						sql.append(" FIND_IN_SET('"+country+"',parentId) OR FIND_IN_SET('"+country+"', targetAreaId) ");
					}
				}
				sql.append(" ) ");
			}
		}

		   sql.append(" GROUP BY a.srcActivityId) ata ")
		   .append(" ON t.id = ata.travelactivity_id ")
		   .append(" WHERE t.activity_kind='2' AND t.delFlag = 0 AND t.activityStatus = 2 AND a.delFlag = 0 ");

		if (type == null || "100000".equals(type)) {
			sql.append(" and FIND_IN_SET(100000, ata.parentIds) ");
		} else {
			sql.append(" and FIND_IN_SET(200000, ata.parentIds) ");
		}
		   //1.批发商上架权限(0:上架;1:下架);2.用户具有报名权限(0:无报名;1:有报名);3.批发商在价格策略中,且该策略的启用状态为启用(1:启用;2:禁用).
		    sql.append(" AND sup.shelfRightsStatus=0 ")//AND sup.quauqBookOrderPermission=1
			//.append(" AND ps.id IS NOT NULL AND a.is_t1 = 1");
		    .append(" AND a.is_t1 = 1");
//					.append(" and (a.quauqAdultPrice is not null or a.quauqChildPrice is not null or a.quauqSpecialPrice is not null) ");
		   //如果出发城市不为空的话或者不为默认的全选时,则拼接上出发城市的条件
		   if(StringUtils.isNotBlank(fromArea)){
			  sql.append(" AND t.fromArea in (" + fromArea.substring(0, fromArea.length()-1) + ")");
		   }
		   //当出发地不为空的时候,除了拼接上出发地的id,还需要去除出发地为空值的记录
		   if(StringUtils.isNotBlank(targetAreaIdList)){
			   sql.append(" AND ata.sys_area_id in (" + targetAreaIdList.substring(0, targetAreaIdList.length()-1) + ")");
		   }
		   //当供应商搜索条件不为空的时候,拼接上供应商的id
		   if(StringUtils.isNotBlank(supplier)){
			   sql.append(" AND sup.sys_office_id in ("+supplier.substring(0, supplier.length()-1)+") ");
		   }
		   //当出团日期筛选条件不为空时,拼接上出团日期
//		   if(StringUtils.isNotBlank(groupOpenDateBegin)){ //出团日期起始时间不为空的时候
//			   sql.append(" AND a.groupOpenDate>='"+groupOpenDateBegin+"' ");
//		   }
//		   if(StringUtils.isNotBlank(groupOpenDateEnd)){ //出团日期结束时间不为空的时候
//			   sql.append(" AND a.groupOpenDate<='"+groupOpenDateEnd+"' ");
//		   }
		//出团日期
		if (StringUtils.isNotBlank(groupOpenDateBegin)) {
			sql.append(" AND ");
			String groupDateStr = "";
			if (groupOpenDateBegin.charAt(groupOpenDateBegin.length() - 1) == '-') {
				groupDateStr = " a.groupOpenDate >= '" + groupOpenDateBegin.substring(0, groupOpenDateBegin.length()-1) + "'";
			} else if (groupOpenDateBegin.charAt(0) == '-') {
				groupDateStr = " a.groupOpenDate <= '" + groupOpenDateBegin.substring(1) + "'";
			} else {
				groupDateStr = " (a.groupOpenDate >= '" + groupOpenDateBegin.substring(0, 10)
				+ "' and a.groupOpenDate <= '" + groupOpenDateBegin.substring(11) + "')";
			}
			sql.append(groupDateStr);
		}
		if (StringUtils.isNotBlank(countryPara)) {
			sql.append(" and (");
			String[] cs = countryPara.split(",");
			for (int i = 0; i < cs.length; i++) {
				String c = cs[i];
				if (i != cs.length - 1) {
					sql.append(" FIND_IN_SET("+c+",ata.parentId) OR FIND_IN_SET("+c+", ata.sys_area_id) or ");
				} else {
					sql.append(" FIND_IN_SET("+c+",ata.parentId) OR FIND_IN_SET("+c+", ata.sys_area_id) ");
				}
			}
			sql.append(" ) ");
		}
//		   //当行程天数筛选条件不为空的时候,拼接上行程天数
//		   if(StringUtils.isNotBlank(activityDurationFrom)){ //行程起始天数
//			   sql.append(" AND t.activityDuration>="+Integer.valueOf(activityDurationFrom)+"  ");
//		   }
//		   if(StringUtils.isNotBlank(activityDurationTo)){ //行程结束天数
//			   sql.append(" AND t.activityDuration<="+Integer.valueOf(activityDurationTo)+"  ");
//		   }
		//行程天数
		if (StringUtils.isNotBlank(activityDurationFrom)) {
			sql.append(" AND ");
			String[] days = activityDurationFrom.split(",");
			for (int i = 0; i < days.length; i++) {
				String day = days[i];
				switch (day) {
					case "d1":
						days[i] = " activityDuration = 1 ";
						break;
					case "d2":
						days[i] = " activityDuration = 2 ";
						break;
					case "d3":
						days[i] = " activityDuration = 3 ";
						break;
					case "d4":
						days[i] = " activityDuration = 4 ";
						break;
					case "d5":
						days[i] = " activityDuration = 5 ";
						break;
					case "d6":
						days[i] = " activityDuration = 6 ";
						break;
					case "d7":
						days[i] = " activityDuration = 7 ";
						break;
					case "d8":
						days[i] = " activityDuration = 8 ";
						break;
					case "d9":
						days[i] = " activityDuration = 9 ";
						break;
					case "d0":
						days[i] = " activityDuration >= 10 ";
						break;
					default:
						days[i] = days[i].replaceAll("天", "");
						int pos = returnPos(days[i]);
						String[] dayInterval = days[i].split("-");
						if (pos == 1) {
							days[i] = " (activityDuration >= " + dayInterval[0] + ") ";
						} else if(pos == -1) {
							days[i] = " (activityDuration <= " + dayInterval[1] + ") ";
						} else {
							days[i] = " (activityDuration >= " + dayInterval[0] + " AND activityDuration <= " + dayInterval[1] + ") ";
						}
						break;
				}
			}
			StringBuffer daysPara = new StringBuffer("");
			for (int i = 0; i < days.length; i++) {
				if(i != days.length -1) {
					daysPara.append(days[i] + " OR ");
				} else {
					daysPara.append(days[i]);
				}
			}
			sql.append(daysPara.insert(0, "(").append(")"));
		}

		//价格区间
		if (StringUtils.isNotBlank(pricePara)) {
			sql.append(" AND ");
			String[] prices = pricePara.split(",");
			for (int i = 0; i < prices.length; i++) {
				String price = prices[i];
				switch (price) {
					case "p0":
						prices[i] = " (a.quauqAdultPrice < 3000/(1+sup.charge_rate)) ";
						break;
					case "p1":
						prices[i] = " (a.quauqAdultPrice >= 3000/(1+sup.charge_rate) AND a.quauqAdultPrice <= 4999/(1+sup.charge_rate)) ";
						break;
					case "p2":
						prices[i] = " (a.quauqAdultPrice >= 5000/(1+sup.charge_rate) AND a.quauqAdultPrice <= 7999/(1+sup.charge_rate)) ";
						break;
					case "p3":
						prices[i] = " (a.quauqAdultPrice >= 8000/(1+sup.charge_rate) AND a.quauqAdultPrice <= 9999/(1+sup.charge_rate)) ";
						break;
					case "p4":
						prices[i] = " (a.quauqAdultPrice >= 10000/(1+sup.charge_rate)) ";
						break;
					default:
						prices[i] = prices[i].replaceAll("元", "");
						int pos = returnPos(prices[i]);
						String[] priceInterval = prices[i].split("-");
						if (pos == 1) {
							prices[i] = " (a.quauqAdultPrice >= " + priceInterval[0] + "/(1+sup.charge_rate)) ";
						} else if(pos == -1) {
							prices[i] = " (a.quauqAdultPrice <= " + priceInterval[1] + "/(1+sup.charge_rate)) ";
						} else {
							prices[i] = " (a.quauqAdultPrice >= " + priceInterval[0] + "/(1+sup.charge_rate) AND a.quauqAdultPrice <= " + priceInterval[1] + "/(1+sup.charge_rate)) ";
						}
						break;

				}
			}
			String priceStr = "";
			for (int i = 0; i < prices.length; i++) {
				if(i != prices.length -1) {
					priceStr += prices[i] + " OR ";
				} else {
					priceStr += prices[i];
				}
			}
			sql.append(priceStr);
		}

		//余位
		if (StringUtils.isNotBlank(freePositionFrom)) {
			sql.append(" AND ");
			String[] frees = freePositionFrom.split(",");
			for (int i = 0; i < frees.length; i++) {
				String free = frees[i];
				switch (free) {
					case "f0":
						frees[i] = " (a.freePosition < 10) ";
						break;
					case "f1":
						frees[i] = " (a.freePosition >= 10 AND a.freePosition <= 19) ";
						break;
					case "f2":
						frees[i] = " (a.freePosition >= 20 AND a.freePosition <= 29) ";
						break;
					case "f3":
						frees[i] = " (a.freePosition >= 30) ";
						break;
					default:
						int pos = returnPos(frees[i]);
//						frees[i] = frees[i].replaceAll("元", "");
						String[] freeInterval = frees[i].split("-");
						if (pos == 1) {
							frees[i] = " (a.freePosition >= " + freeInterval[0] + ") ";
						} else if(pos == -1) {
							frees[i] = " (a.freePosition <= " + freeInterval[1] + ") ";
						} else {
							frees[i] = " (a.freePosition >= " + freeInterval[0] + " AND a.freePosition <= " + freeInterval[1] + ") ";
						}
						break;

				}
			}
			String freeStr = "";
			for (int i = 0; i < frees.length; i++) {
				if(i != frees.length -1) {
					freeStr += frees[i] + " OR ";
				} else {
					freeStr += frees[i];
				}
			}
			sql.append(freeStr);
		}
//		   //当余位筛选条件不为空的时候,拼接上余位
//		   if(StringUtils.isNotBlank(freePositionFrom)){ //起始余位
//			   sql.append(" AND a.freePosition>="+Integer.valueOf(freePositionFrom)+" ");
//		   }
//		   if(StringUtils.isNotBlank(freePostionTo)){ //结束余位
//			   sql.append(" AND a.freePosition<="+Integer.valueOf(freePostionTo)+" ");
//		   }
		   //当搜索关键字不为空的时候,拼接上关键字条件:产品名称,供应商,团号
		   if(StringUtils.isNotBlank(keywordSearching) && !"产品名称 / 供应商 / 团号 / 目的地".equals(keywordSearching)){
			   keywordSearching=StringUtils.trim(keywordSearching);
			   keywordSearching = keywordSearching.replace("'", "''");
			   keywordSearching = keywordSearching.replace("%", "\\%");
			   keywordSearching = keywordSearching.replace("_", "\\_");
			   keywordSearching = keywordSearching.replace(" ", "%");
			   if (type == null || "100000".equals(type)) {
				   sql.append(" AND (( t.acitivityName LIKE '%"+keywordSearching+"%') OR  (sup.supplierName LIKE '%"+keywordSearching+"%') OR (a.groupCode LIKE '%"+keywordSearching+"%') OR FIND_IN_SET('"+keywordSearching+"', ata.parentName) OR FIND_IN_SET('"+keywordSearching+"',ata.name) )  ");
			   } else {
				   sql.append(" AND (( t.acitivityName LIKE '%"+keywordSearching+"%')OR  (sup.supplierName LIKE '%"+keywordSearching+"%') OR(a.groupCode LIKE '%"+keywordSearching+"%') OR FIND_IN_SET('"+keywordSearching+"',ata.name))  ");
			   }
		   }
		   sql.append(" AND a.groupOpenDate>='"+curDate+"' ");

		if(StringUtils.isBlank(orderBy)) {
			orderBy = "defaultSort ASC";
		}
		page.setOrderBy(orderBy);

		if(StringUtils.isNotBlank(pageNo)){
			page.setPageNo(Integer.valueOf(pageNo));
		}
		if(StringUtils.isNotBlank(pageSize)){
			page.setPageSize(Integer.valueOf(pageSize));
		}
		
		Page<TravelActivity> returnPage = travelActivityDao.findBySql(page, sql.toString(),Map.class);
		return returnPage;
	}
	
	/**
	 * 获取散拼产品目的地sql：sql优化，替换请小心 
	 * @author yakun.bai
	 * @Date 2016-9-12
	 */
	private StringBuffer getLooseActivityTargetAreaSql(String curDate) {
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT t1.id AS id, t1.srcActivityId AS srcActivityId, t1.targetAreaId AS targetAreaId, s1.name AS name, ")
					.append("(SELECT sa.name FROM sys_area sa WHERE sa.id = s1.parentId) AS parentName FROM ")
					.append("activitytargetarea t1, sys_area s1, travelactivity t, activitygroup a ")
					.append("WHERE t1.targetAreaId = s1.id AND a.srcActivityId = t.id AND t.id = t1.srcActivityId AND ")
					.append("t.activity_kind = '2' AND a.groupOpenDate >= '" + curDate + "' ");
		return sb;
	}


	/**
	 * “-”位于最左侧返回-1，位于中间返回0，位于末尾返回1
	 * @param price
	 * @return
	 */
	private int returnPos(String price) {
		if (price.charAt(price.length() - 1) == '-') {
			return 1;
		} else if(price.charAt(0) == '-') {
			return -1;
		} else {
			return 0;
		}
	}
	@Override
	public List<Map<String, String>> getFromAreas(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select fromArea, label, count(fromArea) sumArea from activitygroup c LEFT JOIN travelactivity a ON c.srcActivityId = a.id LEFT JOIN(select id, label, value from sys_dict where type = 'from_area' and delFlag = 0) b on a.fromArea = b.`value` where a.activity_kind = "+activityKind+" and a.proCompany= "+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.delFlag = 0 GROUP BY fromArea ORDER BY sumArea DESC";
		}else{
			sql = "select fromArea, label, count(fromArea) sumArea from activitygroup c LEFT JOIN travelactivity a ON c.srcActivityId = a.id LEFT JOIN(select id, label, value from sys_dict where type = 'from_area' and delFlag = 0) b on a.fromArea = b.`value` where a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.delFlag = 0 GROUP BY fromArea ORDER BY sumArea DESC";
		}
		return travelActivityDao.findBySql(sql, Map.class);
	}

	@Override
	public Object getActivityKind(String activityKind, String companyId) {
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select DISTINCT a.activity_kind activityKind from travelactivity a where a.proCompany ="+companyId+"  and a.delFlag = 0";
		}else{
			sql = "select DISTINCT a.activity_kind activityKind from travelactivity a where  a.delFlag = 0";;
		}
		return travelActivityDao.findBySql(sql, Map.class);
	}

	@Override
	public List<Map<String, String>> getTargetAreas(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select targetAreaId,c.name, count(targetAreaId) sumArea  FROM activitygroup d LEFT JOIN  travelactivity a ON d.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany= "+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and d.delFlag = 0 GROUP BY targetAreaId ORDER BY sumArea DESC";
		}else{
			sql = "select targetAreaId,c.name, count(targetAreaId) sumArea  FROM activitygroup d LEFT JOIN  travelactivity a ON d.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and  a.delFlag = 0 and a.activityStatus = 2 and d.delFlag = 0 GROUP BY targetAreaId ORDER BY sumArea DESC";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getTravelTypes(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select traveltypeid, traveltypename FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where traveltypeid is not null and traveltypename is not null and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 GROUP BY traveltypeid";
		}else{
			sql =  "select traveltypeid, traveltypename FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where traveltypeid is not null and traveltypename is not null and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 GROUP BY traveltypeid";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getActivityTypes(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select activitytypeid,activitytypename  FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activitytypeid is not null and activitytypename is not null  and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activitytypeid";
		}else{
			sql = "select activitytypeid,activitytypename  FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activitytypeid is not null and activitytypename is not null  and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activitytypeid";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getActivityLevels(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select activityLevelId,activityLevelName FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activityLevelId is not null and activityLevelName is not null and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activityLevelId";
		}else{
			sql = "select activityLevelId,activityLevelName FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activityLevelId is not null and activityLevelName is not null and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activityLevelId";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public Object getDestinations(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = 	"select id,name, sum(sumArea) sumArea1 from( "+
					"select d.id,d.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select c.id,c.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select e.id,e.name, count(targetAreaId) sumArea  FROM activitygroup  f LEFT JOIN travelactivity a ON f.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d, sys_area e where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 3 and d.parentId = e.id and e. type = 2 and f.delFlag = 0 GROUP BY targetAreaId ) e group by id ORDER BY sumArea1 DESC";
		}else{
			sql = "select id,name, sum(sumArea) sumArea1 from( "+
					"select d.id,d.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select c.id,c.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select e.id,e.name, count(targetAreaId) sumArea  FROM activitygroup  f LEFT JOIN travelactivity a ON f.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d, sys_area e where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 3 and d.parentId = e.id and e. type = 2 and f.delFlag = 0 GROUP BY targetAreaId ) e group by id ORDER BY sumArea1 DESC";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}
	
	@Override
	public Page<Map<Object, Object>> searchSPActivityList(
			Page<Map<Object, Object>> page, HttpServletRequest request,Model model, String companyId, String requestType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Map<Object, Object>> exportData() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 通过groupCode查看详情
     * @param groupCode
     */
    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> getDetail(Long activityId, String groupCode) {
        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT ")
//				.append("g.id groupId, ")
//				.append("g.groupOpenDate, ")
//                .append("g.freePosition, ")
//                .append("t.acitivityName activityName, ")
//                .append("g.groupCode, ")
//                .append("t.fromArea, ")
//                .append("t.activityDuration, ")
//                .append("t.trafficMode, ")
//                .append("g.settlementAdultPrice, ")
//                .append("g.settlementcChildPrice, ")
//                .append("g.settlementSpecialPrice, ")
//                .append("g.suggestAdultPrice, ")
//                .append("g.suggestChildPrice, ")
//                .append("g.suggestSpecialPrice ")
//                .append("FROM ")
//                .append("travelactivity t, ")
//                .append("activitygroup g ")
//                .append("WHERE ")
//                .append("t.id = g.srcActivityId ")
//                .append("AND g.groupCode = ?");
        Map < String, Object > map = (Map<String, Object>) travelActivityDao.findBySql(sb.toString(), Map.class, groupCode);
        return map;
    }

	@Override
	public int getCount(boolean b) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getChangedCount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hasChanged(String groupId, String srcId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> searchChangedList(String groupId,
			String srcId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFreeLog(TravelActivity source, TravelActivity activity){
		// TODO Auto-generated method stub
		return ;
	}

	public void setFreeLog(TravelActivity activity, ActivityGroup sourceGroup,Map<String,String> map){
		return;
	}

	@Override
	public Page<Map<Object, Object>> searchProductAndActivityList(
			Page<Map<Object, Object>> page, HttpServletRequest request,
			Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findProductAndActivityList(
			String officeId, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getGroupAndOfficeT1PermissionStatus(
			String groupId) {
		return null;
	}

	/**
	 * 根据产品类型获取所有的产品的目的地,做数据初始化用。
	 * @param orderType
	 * @return
	 * @author yudong.xu 2016.10.20
	 */
	@Override
	public List<Map<String,Object>> getAllActivityAreaIdsByType(Integer orderType){
		String sql = "SELECT GROUP_CONCAT(m.targetAreaId) AS areaIds,p.id AS activityId,p.acitivityName AS activityName " +
			"FROM travelactivity p,activitytargetarea m WHERE p.id=m.srcActivityId AND p.activity_kind=? GROUP BY p.id";
		List<Map<String,Object>> list =  travelActivityDao.findBySql(sql,Map.class,orderType);
		return list;
	}

	/**
	 * 更新产品中的线路玩法
	 * @param touristLineId
	 * @param activityId
	 * @author yudong.xu 2016.10.20
     */
	@Transactional(readOnly = false)
	@Override
	public void updateTouristLine(Long touristLineId, Long activityId){
		travelActivityDao.updateTouristLine(touristLineId,activityId);
	}

	/**
	 * 通过产品id获取目的地id
	 * @param activityId
	 * @return
     */
	public List<Integer> getTargetAreaByActivityId(Long activityId) {
		String sql = "SELECT a.targetAreaId FROM activitytargetarea a WHERE a.srcActivityId = ?";
		List<Integer> targetAreaIds = travelActivityDao.findBySql(sql, activityId);
		return targetAreaIds;
	}

}
