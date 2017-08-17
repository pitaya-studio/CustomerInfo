package com.trekiz.admin.modules.airticket.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.repository.ActivityFileDao;
import com.trekiz.admin.modules.activity.repository.IntermodalStrategyDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicketCost;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketCostDao;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.repository.IAirticketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

/**
 * 
 * 
 * @Description:TODO
 * 
 * @author:midas
 * 
 * @time:2014-9-19 上午10:22:57
 */
@Service
@Transactional(readOnly = true)
public class ActivityAirTicketServiceImpl extends BaseService implements
		IActivityAirTicketService {

	protected Logger logger = LoggerFactory
			.getLogger(ActivityAirTicketServiceImpl.class);

	@Autowired
	private AirticketActivityReserveDao airticketActivityReserveDao;

	@Autowired
	private ActivityFileDao activityFileDao;

	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private ActivityAirTicketCostDao ActivityAirTicketCostDao;
	@Autowired
	private CostRecordLogDao costRecordLogDao;

	@Autowired
	private IAirticketDao airticketDao;

	@Autowired
	private AirportInfoService airportInfoService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private UserDao userDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private IntermodalStrategyDao intermodalStrategyDao;

	@Override
	public ActivityAirTicket getActivityAirTicketById(Long airTicketId) {
		// TODO Auto-generated method stub
		return activityAirTicketDao.findOne(airTicketId);
	}

	/**
	 * 分页列表查询
	 * 
	 * @Description:TODO
	 * @param page
	 * @param travelActivity
	 * @param settlementAdultPriceStart
	 * @param settlementAdultPriceEnd
	 * @param agentId
	 * @return Page<ActivityAirTicket>
	 * @exception:
	 * @author: midas
	 * @time:2014-9-19 下午12:28:47
	 */
	@Override
	public Page<ActivityAirTicket> findActivityAirTicketPage(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId, DepartmentCommon common) {
		// TODO Auto-generated method stub

		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(airTicket.getProductCode())) {
			dc.add(Restrictions.eq("productCode", airTicket.getProductCode()));
		}
		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));
		// 团号搜索
		if (StringUtils.isNotBlank(airTicket.getGroupCode())) {
			String groupCode = airTicket.getGroupCode();
			groupCode = groupCode.replace("'", "''");
			groupCode = groupCode.replace("%", "\\%");
			groupCode = groupCode.replace("_", "\\_");
			dc.add(Restrictions.like("groupCode", "%" + groupCode.trim() + "%"));
		}
		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}
		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.between("startingDate", startDate, endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		systemService.getDepartmentSql("activity", dc, null, common, null);

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
			page.setOrderBy("id DESC");
		}

		return activityAirTicketDao.find(page, dc);
	}

	@Override
	public Page<Map<String, Object>> findActivityAirTicketReviewPage(
			Page<Map<String, Object>> page, ActivityAirTicket airTicket, Map<String, Object> params) {
		Object departureCityPara = params.get("departureCityPara");
		Object arrivedCityPara = params.get("arrivedCityPara");
		Object airType = params.get("airType");
		Object commitType = params.get("commitType");
		Object operator = params.get("operator");
		Object groupOpenDate = params.get("groupOpenDate");
		Object groupCloseDate = params.get("groupCloseDate");
		Object isReject = params.get("isReject"); // 540 增加驳回筛选项 王洋 2017.3.22
		Long companyId = UserUtils.getUser().getCompany().getId();
		/**
		StringBuffer column = new StringBuffer();
		column.append("airticket.id,airType,activity_airticket_name,country,operator,startingDate,returnDate,")
			  .append("departureCity,arrivedCity,airticket.currency_id,airticket.createDate,airticket.updateDate,")
			  .append("airticket.createBy,airticket.updateBy,airticket.delflag,product_status,proCompany,")
			  .append("airticket.remark,depositTime,airticket.ticket_area_type,cancelTimeLimit,airticket.product_type_id,")
			  .append("product_code,airticket.istax,limitTime,airticket.taxamt,depositamt,freePosition,")
			  .append("airticket.settlementAdultPrice,airticket.settlementcChildPrice,airticket.settlementSpecialPrice,isSection,")
			  .append("specialremark,activity_scope,reservationsNum,nopayReservePosition,payReservePosition,soldNopayPosition,")
			  .append("soldPayPosition,productStatus,airticket.airlines,whetherTrip,isRecord,payMode_full,payMode_deposit,")
			  .append("payMode_advance,remainDays_deposit,remainDays_advance,outTicketTime,")
			  .append("intermodalType,outArea,recordId,airticket.review,airticket.nowLevel,airticket.lockStatus,")
			  .append("income,cost,airticket.deptId,forcastStatus,airticket.group_code,payMode_op,maxPeopleCount,journey,")
			  .append("payMode_cw,remainDays_advance_fen,remainDays_advance_hour,remainDays_deposit_fen,remainDays_deposit_hour,iscommission,")
			  .append("payableDate,invoice_tax");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT ").append(column.toString())
				.append(" FROM (")
				.append(" SELECT ").append(column.toString())
				.append(" FROM activity_airticket airticket, activity_flight_info flight ")
				.append(" WHERE airticket.id = flight.airticketId  AND airticket.delflag = 0 AND airticket.productStatus = 2 AND airticket.proCompany = ")
				.append(companyId)
				.append(" UNION ")
				.append(" SELECT ").append(column.toString())
				.append(" FROM activity_airticket airticket, activity_flight_info flight, airticket_order orders")
				.append(" WHERE airticket.id = flight.airticketId  AND airticket.id = orders.airticket_id AND airticket.proCompany = ")
				.append(companyId)
				.append(" AND (airticket.productStatus = 3 OR airticket.delflag = 1)) airticket WHERE 1=1 ");
		//未提交  0416   update by shijun.liu   2016.04.29
		// 存在审批通过 并且有未提交付款审批的数据
		if("1".equals(commitType)){//
			sb.append(" AND EXISTS (SELECT c.id FROM cost_record c WHERE c.activityId = airticket.id AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview = ").append( ReviewConstant.REVIEW_STATUS_REJECTED).append(")) ")
			  .append(" AND c.reviewType = 0 AND c.review = 2 AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		//已提交  0416   update by shijun.liu   2016.04.29
		//已通过成本审批，并且已提交付款审批(all)
		// 首先必须有成本，并且所有的成本都进行了付款审批的提交
		if("2".equals(commitType)){
			sb.append(" AND NOT EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = airticket.id AND c.reviewType = 0 ")
			  .append(" AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview = ").append( ReviewConstant.REVIEW_STATUS_REJECTED).append(")) ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ")
			  .append(" AND EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = airticket.id AND c.reviewType = 0 ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		if (null != departureCityPara && StringUtils.isNotEmpty(String.valueOf(departureCityPara))) {
			sb.append(" AND departureCity = '").append(String.valueOf(departureCityPara)).append("'");
		}
		if (null != arrivedCityPara && StringUtils.isNotEmpty(String.valueOf(arrivedCityPara))) {
			sb.append(" AND arrivedCity = '").append(String.valueOf(arrivedCityPara)).append("'");
		}
		if (null != airType && StringUtils.isNotEmpty(String.valueOf(airType))) {
			sb.append(" AND airType = ").append(Integer.parseInt(String.valueOf(airType)));
		}
		if (StringUtils.isNotBlank(airTicket.getAirlines())) {
			sb.append(" AND airlines like '%").append(airTicket.getAirlines()).append("%'");
		}
		if (null != groupOpenDate && StringUtils.isNotEmpty(String.valueOf(groupOpenDate))) {
			sb.append(" AND startingDate >= '").append(String.valueOf(groupOpenDate)).append("'");
		}
		if (null != groupCloseDate && StringUtils.isNotEmpty(String.valueOf(groupCloseDate))) {
			sb.append(" AND startingDate <= '").append(String.valueOf(groupCloseDate)).append("'");
		}
		String order = page.getOrderBy();
		if (StringUtils.isBlank(order)) {
			page.setOrderBy("createDate DESC");
		}*/
		StringBuffer column = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		column.append(" t.id, t.airlines, t.airType, t.operator, t.settlementAdultPrice, t.reservationsNum,t.freePosition,")
			  .append(" t.payReservePosition, t.createDate, t.updateDate, t.currencyId, IFNULL(COUNT(t_rn.id), 0) AS rejectCount ");
		//公共条件
		StringBuffer innerCondition = new StringBuffer();
		if (null != departureCityPara && StringUtils.isNotEmpty(String.valueOf(departureCityPara))) {
			innerCondition.append(" AND p.departureCity = '").append(String.valueOf(departureCityPara)).append("'");
		}
		if (null != arrivedCityPara && StringUtils.isNotEmpty(String.valueOf(arrivedCityPara))) {
			innerCondition.append(" AND p.arrivedCity = '").append(String.valueOf(arrivedCityPara)).append("'");
		}
		if (null != groupOpenDate && StringUtils.isNotEmpty(String.valueOf(groupOpenDate))) {
			innerCondition.append(" AND af.startTime >= '").append(String.valueOf(groupOpenDate)).append("'");
		}
		if (null != groupCloseDate && StringUtils.isNotEmpty(String.valueOf(groupCloseDate))) {
			innerCondition.append(" AND af.startTime <= '").append(String.valueOf(groupCloseDate)).append("'");
		}
		sb.append("SELECT ").append(column).append(" FROM (")
		  .append(" SELECT p.id, p.airlines, p.airType, (SELECT NAME FROM sys_user WHERE id = p.createBy) AS operator, ")
		  .append(" p.settlementAdultPrice, p.reservationsNum, p.freePosition, p.payReservePosition, p.createDate, ")
		  .append(" p.updateDate, p.currency_id AS currencyId FROM activity_airticket p LEFT JOIN activity_flight_info af ")
		  .append(" ON p.id = af.airticketId WHERE p.delflag = 0 AND p.productStatus = 2 AND p.proCompany = ").append(companyId)
		  .append(innerCondition)
		  .append(" UNION ")
		  .append(" SELECT p.id, p.airlines, p.airType, (SELECT NAME FROM sys_user WHERE id = p.createBy) AS operator, ")
		  .append(" p.settlementAdultPrice, p.reservationsNum, p.freePosition, p.payReservePosition, p.createDate, ")
		  .append(" p.updateDate, p.currency_id AS currencyId FROM activity_airticket p LEFT JOIN activity_flight_info af ")
		  .append(" ON p.id = af.airticketId, airticket_order o WHERE p.id = o.airticket_id ")
		  .append(" AND (p.productStatus = 3 OR p.delflag = 1) AND p.proCompany = ").append(companyId)
		  .append(innerCondition).append(" ) t LEFT JOIN ( SELECT rn.id, cost.activityId ")
		  // ----- 540需求，运控机票页面添加驳回标识   王洋 2017.3.22
		  .append(" FROM review_new rn INNER JOIN cost_record cost ON cost.pay_review_uuid = rn.id ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--start
		  .append(" AND cost.orderType = rn.product_type AND cost.delFlag=0 WHERE rn.del_flag = '0' AND rn.process_type = 18 ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--end
		  .append(" AND rn.need_no_review_flag = 0 AND rn.product_type = 7 AND rn.`status` = 0 ")
		  .append(" AND rn.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ")
		  .append(" UNION SELECT rn.id, cost.activityId FROM ")
		  .append(" review_new rn INNER JOIN cost_record cost ON cost.reviewUuid = rn.id ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--start
		  .append(" AND cost.orderType = rn.product_type AND cost.delFlag=0 WHERE rn.del_flag = '0' ")
		  // 540需求 bug17745 修正 追加---cost.delFlag=0--条件--高阳 2017-04-12--end
		  .append(" AND rn.process_type IN (15, 17) AND rn.need_no_review_flag = 0 ")
		  .append(" AND rn.product_type = 7 AND rn.`status` = 0 ")
		  .append(" AND rn.company_id = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ")
		  .append(" ) t_rn ON t_rn.activityId = t.id WHERE 1=1 ");
		if (null != airType && StringUtils.isNotEmpty(String.valueOf(airType))) {
			sb.append(" AND t.airType = ").append(Integer.parseInt(String.valueOf(airType)));
		}
		if (StringUtils.isNotBlank(airTicket.getAirlines())) {
			sb.append(" AND t.airlines like '%").append(airTicket.getAirlines()).append("%'");
		}
		if (null != operator && StringUtils.isNotEmpty(String.valueOf(operator))) {
			sb.append(" AND t.operator like '%").append(String.valueOf(operator)).append("%'");
		}
		//未提交  0416   update by shijun.liu   2016.04.29
		// 存在审批通过 并且有未提交付款审批的数据,或者提交审批，被驳回或者已取消的数据
		if("1".equals(commitType)){//
			sb.append(" AND EXISTS (SELECT c.id FROM cost_record c WHERE c.activityId = t.id AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append( ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.reviewType = 0 AND c.review = 2 AND c.budgetType = 1 AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		//已提交  0416   update by shijun.liu   2016.04.29
		//已通过成本审批，并且已提交付款审批(all)
		// 首先必须有成本，并且所有的成本都进行了付款审批的提交
		if("2".equals(commitType)){
			sb.append(" AND NOT EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND (c.pay_review_uuid IS NULL ")
			  .append(" OR (c.pay_review_uuid IS NOT NULL AND c.payReview IN (5,").append( ReviewConstant.REVIEW_STATUS_REJECTED).append("))) ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ")
			  .append(" AND EXISTS (SELECT c.activityId FROM cost_record c WHERE c.activityId = t.id AND c.reviewType = 0 AND c.budgetType = 1 ")
			  .append(" AND c.orderType = ").append(Context.ORDER_TYPE_JP)
			  .append(" AND c.delFlag= ").append(Context.DEL_FLAG_NORMAL).append(" ) ");
		}
		// 540添加以id为分组用于计算是否存在驳回标识 王洋  2017.3.22
		sb.append(" GROUP BY t.id ");
		if ("1".equals(isReject)) {
			sb.append(" HAVING IFNULL(COUNT(t_rn.id), 0) > 0 ");
		}
		String order = page.getOrderBy();
		if (StringUtils.isBlank(order)) {
			page.setOrderBy("createDate DESC");
		}
		return activityAirTicketDao.findBySql(page, sb.toString(), Map.class);
	}

	@Override
	public Page<ActivityAirTicket> findAirTicketReviewPage(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, String review, Integer nowLevel, Long companyId,
			String orderBy) {
		// TODO Auto-generated method stub

		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();

		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));

		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}
		if (StringUtils.isNotBlank(review)) {
			if (review.equals(Context.REVIEW_COST_NEW.toString())) { // 待录入
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_NEW));
			} else if (review.equals(Context.REVIEW_COST_WAIT.toString())) { // 待当前层级审核
				dc.add(Restrictions.eq("nowLevel", nowLevel));
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_WAIT));
			} else if (review.equals(Context.REVIEW_COST_PASS.toString())) {// (当前层级)已经通过,
																			// 并没有被其他层级驳回
				dc.add(Restrictions.gt("nowLevel", nowLevel));
				dc.add(Restrictions.ne("review", Context.REVIEW_COST_FAIL));
			} else if (review.equals(Context.REVIEW_COST_FAIL.toString())) {// 所有层级已经驳回
				// dc.add(Restrictions.eq("nowLevel", nowLevel));
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL));
			}
		} else if (nowLevel > 1) {
			/*
			 * 经理审核nowLevel=2 时，全部数据列表需要过滤掉 待财务审核 的记录=待录入记录 +
			 * nowLevel大于1(财务审核)的记录
			 */
			dc.add(Restrictions.or(
					Restrictions.eq("review", Context.REVIEW_COST_NEW),
					Restrictions.gt("nowLevel", (Integer) 1)));
		}

		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			dc.add(Restrictions.ge("startingDate", startDate));
		}

		if (StringUtils.isNotBlank(endTime)) {
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.le("startingDate", endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}

		return activityAirTicketDao.find(page, dc);
	}

	@Override
	public Page<ActivityAirTicketCost> findAirCostReviewPage(
			Page<ActivityAirTicketCost> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, String review, Integer nowLevel, Long companyId,
			Long reviewCompanyId, Integer flowType, String orderBy,
			String createByName) {
		// TODO Auto-generated method stub
		DetachedCriteria dcUser = userDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(createByName)) {
			dcUser.add(Restrictions.like("name", "%" + createByName + "%"));
		}

		List<User> list = userDao.find(dcUser);
		User[] createBy = new User[list.size()];
		for (int i = 0; i < list.size(); i++) {
			createBy[i] = list.get(i);
		}

		DetachedCriteria dc = ActivityAirTicketCostDao.createDetachedCriteria();

		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));

		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}

		/*
		 * if (StringUtils.isNotBlank(review)) {
		 * if(review.equals(Context.REVIEW_COST_NEW.toString())){ //待录入
		 * dc.add(Restrictions.eq("review", Context.REVIEW_COST_NEW)); }else
		 * if(review.equals(Context.REVIEW_COST_WAIT.toString())){ //待当前层级审核
		 * dc.add(Restrictions.eq("nowLevel", nowLevel));
		 * dc.add(Restrictions.eq("review",Context.REVIEW_COST_WAIT)); }else
		 * if(review.equals(Context.REVIEW_COST_PASS.toString())){//(当前层级)已经通过,
		 * 并没有被其他层级驳回 dc.add(Restrictions.gt("nowLevel", nowLevel));
		 * dc.add(Restrictions.ne("review", Context.REVIEW_COST_FAIL)); }else if
		 * (review.equals(Context.REVIEW_COST_FAIL.toString())){//所有层级已经驳回
		 * //dc.add(Restrictions.eq("nowLevel", nowLevel));
		 * dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL)); } }else
		 * if(nowLevel >1 ){ //经理审核nowLevel=2 时，全部数据列表需要过滤掉 待财务审核 的记录=待录入记录 +
		 * nowLevel大于1(财务审核)的记录 dc.add(Restrictions.or(Restrictions.eq("review",
		 * Context.REVIEW_COST_NEW),Restrictions.gt("nowLevel", (Integer)1) ));
		 * }
		 */

		if (flowType == 15) {
			dc.add(Restrictions.lt("review", 4));
			dc.add(Restrictions.eq("reviewCompanyId", reviewCompanyId));
		} else {
			if (!StringUtils.isNotBlank(review)) {
				dc.add(Restrictions.lt("payReview", 4));
			}
			dc.add(Restrictions.eq("budgetType", 1));
			dc.add(Restrictions.eq("payReviewCompanyId", reviewCompanyId));
		}

		if (flowType == 15) {
			if (StringUtils.isBlank(review)) { // 待本人审核
				dc.add(Restrictions.eq("nowLevel", nowLevel));
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_WAIT));
			} else if (review.equals(Context.REVIEW_COST_WAIT.toString())) { // 审核中
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_WAIT));
			} else if (review.equals(Context.REVIEW_COST_PASS.toString())) {// (已经通过
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_PASS));
			} else if (review.equals(Context.REVIEW_COST_FAIL.toString())) {// 已经驳回
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_FAIL));
			} else if (review.equals(Context.REVIEW_COST_CANCEL.toString())) {// 已取消
				dc.add(Restrictions.eq("review", Context.REVIEW_COST_CANCEL));
			}
		}
		if (flowType == 18) {
			if (StringUtils.isBlank(review)) { // 待本人审核
				dc.add(Restrictions.eq("payNowLevel", nowLevel));
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_WAIT));
			} else if (review.equals(Context.REVIEW_COST_WAIT.toString())) { // 审核中
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_WAIT));
			} else if (review.equals(Context.REVIEW_COST_PASS.toString())) {// (已经通过
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_PASS));
			} else if (review.equals("22")) {// 本人审核通过
				dc.add(Restrictions.gt("payNowLevel", nowLevel));
			} else if (review.equals(Context.REVIEW_COST_FAIL.toString())) {// 已经驳回
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_FAIL));
			} else if (review.equals(Context.REVIEW_COST_CANCEL.toString())) {// 已取消
				dc.add(Restrictions.eq("payReview", Context.REVIEW_COST_CANCEL));
			}
		}

		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			dc.add(Restrictions.ge("startingDate", startDate));
		}

		if (StringUtils.isNotBlank(endTime)) {
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.le("startingDate", endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}

		if (StringUtils.isNotBlank(createByName) && list.size() > 0) {
			dc.add(Restrictions.in("createBy", createBy));
		} else if (StringUtils.isNotBlank(createByName) && list.size() == 0) {
			return page;
		}

		return ActivityAirTicketCostDao.find(page, dc);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void batchDelActivityAirTicket(List<Long> ids) {
		if(ids == null || ids.isEmpty()){
			throw new IllegalArgumentException("产品ids为空");
		}
		for(Long id : ids){
			ActivityAirTicket ticket = findById(id);
			try {
				this.delActivity(ticket);
			} catch (Exception e) {
				throw new RuntimeException("删除产品失败");
			}
		}
		// TODO Auto-generated method stub
		//this.activityAirTicketDao.batchDelActivity(ids);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivity(ActivityAirTicket ticket){
		ticket.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		activityAirTicketDao.save(ticket);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public List<Traveler> getAllTraveler(AirticketOrder airticketOrder) {
		List<Traveler> travelers = null;
		if(airticketOrder != null) {
			travelers = travelerDao.findTravelerByOrderIdAndOrderTypeExt(airticketOrder.getId(),
					airticketOrder.getProductTypeId().intValue());
		}
		return travelers;
	}

	@Override
	public void batchOnActivityAirTicketTmp(List<Long> ids) {
		// TODO Auto-generated method stub
		this.activityAirTicketDao.batchOnOrOffActivity(ids,
				StringUtils.toInteger(Context.PRODUCT_ONLINE_STATUS));

	}

	@Override
	public void batchOnOrOffActivityAirTicket(List<Long> ids,
			Integer productStatus) {
		// TODO Auto-generated method stub
		activityAirTicketDao.batchOnOrOffActivity(ids, productStatus);
	}

	@Override
	public void delActivityAirTicket(ActivityAirTicket activityAirTicket) {
		// TODO Auto-generated method stub
		this.activityAirTicketDao.delete(activityAirTicket);
	}

	// 成本审核
	public void submitReview(Long id, Integer review) {
		this.activityAirTicketDao.submitReview(id, review);

	}

	public void updateReview(Long id, Integer review, Integer nowLevel,
			CostRecordLog costRecordLog) {

		this.activityAirTicketDao.updateReview(id, review, nowLevel);
		this.costRecordLogDao.save(costRecordLog);
	}

	@Override
	public String modSave(MultipartFile introduction,
			MultipartFile costagreement, MultipartFile otheragreement,
			List<MultipartFile> otherfile, List<MultipartFile> signmaterial,
			String groupdata, ActivityAirTicket activityAirTicket,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(MultipartFile introduction, MultipartFile costagreement,
			MultipartFile otheragreement, List<MultipartFile> otherfile,
			List<MultipartFile> signmaterial,
			ActivityAirTicket activityAirTicket, String groupOpenDateBegin,
			String groupCloseDateEnd, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ActivityAirTicket save(ActivityAirTicket activityAirTicket) {
		// TODO Auto-generated method stub
		return this.activityAirTicketDao.save(activityAirTicket);
	}

	@Override
	public ActivityAirTicket findById(Long id) {
		return this.activityAirTicketDao.findOne(id);
	}

	@Override
	public ActivityAirTicket flushSave(ActivityAirTicket activityAirTicket) {
		activityAirTicket = this.activityAirTicketDao.save(activityAirTicket);
		activityAirTicketDao.flush();
		return activityAirTicket;
	}

	@Override
	public void deleteById(Long id) {
		this.activityAirTicketDao.delete(id);
	}

	@Override
	public List<Map<String, Object>> findAreaIds(Long companyId) {
		/*
		 * // TODO Auto-generated method stub //return
		 * activityAirTicketDao.findAreaIds(companyId);
		 */
		return null;
	}

	/**
	 * 保存页面传递文件
	 * 
	 * @param activity
	 * @param introduction
	 * @param costagreement
	 * @param otheragreement
	 * @param otherfile
	 * @param request
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void saveFile(ActivityAirTicket airTicket,
			MultipartFile introduction, MultipartFile costagreement,
			MultipartFile otheragreement, List<MultipartFile> otherfile,
			HttpServletRequest request) throws IOException {
		// 产品行程介绍
		Set<ActivityFile> files = airTicket.getActivityFiles();
		DocInfo docInfo;
		ActivityFile activityFile;
		String introduction_name = "";
		String introduction_id = "";
		String costagreement_name = "";
		String costagreement_id = "";
		String otheragreement_name = "";
		String otheragreement_id = "";

		// 行程介绍
		if (!introduction.isEmpty()) {
			introduction_name = request.getParameter("introduction_name");
			introduction_id = request.getParameter("introduction_id");
			if (StringUtils.isNotBlank(introduction_id)) {
				ActivityFile tmp = activityFileDao.findOne(Long
						.parseLong(introduction_id));
				airTicket.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
			docInfo = new DocInfo();
			docInfo.setDocName(introduction.getOriginalFilename());
			docInfo.setDocPath(FileUtils.uploadFile(
					introduction.getInputStream(),
					introduction.getOriginalFilename()));
			activityFile = new ActivityFile();
			activityFile.setFileType(ActivityFile.INTRODUCTION_TYPE);
			activityFile.setFileName(introduction_name);
			activityFile.setDocInfo(docInfo);
			activityFile.setActivityAirTicket(airTicket);
			airTicket.getActivityFiles().add(activityFile);
		}

		// 自费补充协议
		if (!costagreement.isEmpty()) {
			costagreement_name = request.getParameter("costagreement_name");
			costagreement_id = request.getParameter("costagreement_id");
			if (StringUtils.isNotBlank(costagreement_id)) {
				ActivityFile tmp = activityFileDao.findOne(Long
						.parseLong(costagreement_id));
				airTicket.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
			docInfo = new DocInfo();
			docInfo.setDocName(costagreement.getOriginalFilename());
			docInfo.setDocPath(FileUtils.uploadFile(
					costagreement.getInputStream(),
					costagreement.getOriginalFilename()));
			activityFile = new ActivityFile();
			activityFile.setFileType(ActivityFile.COSTAGREEMENT_TYPE);
			activityFile.setFileName(costagreement_name);
			activityFile.setDocInfo(docInfo);
			activityFile.setActivityAirTicket(airTicket);
			airTicket.getActivityFiles().add(activityFile);
		}

		// 其他补充协议
		if (!otheragreement.isEmpty()) {
			otheragreement_name = request.getParameter("otheragreement_name");
			otheragreement_id = request.getParameter("otheragreement_id");
			if (StringUtils.isNotBlank(otheragreement_id)) {

				ActivityFile tmp = activityFileDao.findOne(Long
						.parseLong(otheragreement_id));
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
			docInfo = new DocInfo();
			docInfo.setDocName(otheragreement.getOriginalFilename());
			docInfo.setDocPath(FileUtils.uploadFile(
					otheragreement.getInputStream(),
					otheragreement.getOriginalFilename()));
			activityFile = new ActivityFile();
			activityFile.setFileType(ActivityFile.OTHERAGREEMENT_TYPE);
			activityFile.setFileName(otheragreement_name);
			activityFile.setDocInfo(docInfo);
			activityFile.setActivityAirTicket(airTicket);
			airTicket.getActivityFiles().add(activityFile);
		}

		String otherfile_ids[] = request.getParameterValues("otherfile_id");
		int other_len = 0;
		int otherfile_len = 0;
		if (otherfile_ids != null)
			other_len = otherfile_ids.length;
		// 其他文件
		if (!otherfile.isEmpty()) {
			otherfile_len = otherfile.size();
			// 对现有其他文件的修改
			for (int i = 0; i < other_len; i++) {
				MultipartFile file = otherfile.get(i);
				if (!file.isEmpty() && file.getSize() != 0) {
					ActivityFile tmp = activityFileDao.findOne(Long
							.parseLong(otherfile_ids[i]));
					activityFileDao.delActivityFileById(tmp.getId());
					docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
					docInfo = new DocInfo();
					docInfo.setDocName(file.getOriginalFilename());
					docInfo.setDocPath(FileUtils.uploadFile(
							file.getInputStream(), file.getOriginalFilename()));
					activityFile = new ActivityFile();
					activityFile.setFileType(ActivityFile.OTHER_TYPE);
					activityFile.setFileName("其他文件");
					activityFile.setDocInfo(docInfo);
					activityFile.setActivityAirTicket(airTicket);
					airTicket.getActivityFiles().add(activityFile);
				}
			}
			if ((otherfile_len - other_len) != 0) {
				for (int i = other_len; i < otherfile_len; i++) {
					MultipartFile file = otherfile.get(i);
					if (file.getSize() != 0) {
						docInfo = new DocInfo();
						docInfo.setDocName(file.getOriginalFilename());
						docInfo.setDocPath(FileUtils.uploadFile(
								file.getInputStream(),
								file.getOriginalFilename()));
						activityFile = new ActivityFile();
						activityFile.setFileType(ActivityFile.OTHER_TYPE);
						activityFile.setFileName("其他文件");
						activityFile.setDocInfo(docInfo);
						activityFile.setActivityAirTicket(airTicket);
						files.add(activityFile);
					}
				}
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findActivityByPorCode(String productCode) {
		// TODO Auto-generated method stub
		if (productCode != null) {
			// 组织好的json容器
			List flightList = null;
			ActivityAirTicket activityAirTicket = activityAirTicketDao
					.findByProductCode(productCode);
			if (activityAirTicket != null) {
				flightList = new ArrayList();
				// 用来存贮机票产品的区域类型
				Map<String, String> airticketAreaTypeMap = new HashMap<String, String>();

				airticketAreaTypeMap.put("airticketAreaType",
						activityAirTicket.getTicket_area_type());
				airticketAreaTypeMap.put("airType",
						activityAirTicket.getAirType());
				airticketAreaTypeMap.put("airticketId", activityAirTicket
						.getId().toString());
				// 出发城市
				airticketAreaTypeMap.put("leaveCountry", DictUtils
						.getDictLabel(activityAirTicket.getDepartureCity(),
								"from_area", ""));
				// 到达城市
				airticketAreaTypeMap.put("destination", AreaUtil
						.findAreaNameById(StringUtils.toLong(activityAirTicket
								.getArrivedCity())));
				// 联运类型
				airticketAreaTypeMap
						.put("intermodalType",
								activityAirTicket.getIntermodalType() == 0 ? "无联运"
										: activityAirTicket.getIntermodalType() == 1 ? "全国联运"
												: "分区联运");
				// 预收人数
				airticketAreaTypeMap.put("reservationsNum",
						String.valueOf(activityAirTicket.getReservationsNum()));
				// 出票日期
				airticketAreaTypeMap.put("outTicketTime", DateUtils
						.formatCustomDate(activityAirTicket.getOutTicketTime(),
								"yyyy-MM-dd"));
				// 定金占位保留天数
				airticketAreaTypeMap.put("remainDays_deposit",
						activityAirTicket.getRemainDays_deposit().toString());
				// 与占位保留天数
				airticketAreaTypeMap.put("remainDays_advance",
						activityAirTicket.getRemainDays_advance().toString());
				flightList.add(airticketAreaTypeMap);
				for (FlightInfo info : activityAirTicket.getFlightInfos()) {
					// 用来存贮航段信息，封装用到的属性
					Map<String, String> attrMap = new HashMap<String, String>();
					// 航空公司
					if (StringUtils.isNotBlank(info.getAirlines())
							&& !"-1".equals(info.getAirlines())) {
						attrMap.put("airlines",
								DictUtils.getLabelDesMap(Context.TRAFFIC_NAME)
										.get(info.getAirlines()));
					} else {
						attrMap.put("airlines", "不限");
					}
					// 航班号
					attrMap.put(
							"flightNumber",
							info.getFlightNumber() == null ? "" : info
									.getFlightNumber());
					// 出发城市机场
					attrMap.put(
							"leaveAirport",
							airportInfoService.getAirportInfo(
									StringUtils.toLong(info.getLeaveAirport()))
									.getAirportName());
					// 出发日期
					attrMap.put("startTime", DateUtils.formatCustomDate(
							info.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					// 舱位等级
					if (StringUtils.isNotBlank(info.getAirspace())
							&& !"-1".equals(info.getAirspace())) {
						attrMap.put(
								"spaceGrade",
								DictUtils.getKeyIntMap("airspace_Type").get(
										StringUtils.toInteger(info
												.getAirspace())));
					} else {
						attrMap.put("spaceGrade", "不限");
					}
					// 到达城市机场
					attrMap.put(
							"destinationAirpost",
							airportInfoService.getAirportInfo(
									StringUtils.toLong(info
											.getDestinationAirpost()))
									.getAirportName());
					// 到达时间
					attrMap.put("arrivalTime", DateUtils.formatCustomDate(
							info.getArrivalTime(), "yyyy-MM-dd HH:mm:ss"));
					// 舱位
					if (StringUtils.isNotBlank(info.getSpaceGrade())
							&& !"-1".equals(info.getSpaceGrade())) {
						attrMap.put(
								"airspace",
								DictUtils.getKeyIntMap("spaceGrade_Type").get(
										StringUtils.toInteger(info
												.getSpaceGrade())));
					} else {
						attrMap.put("airspace", "不限");
					}
					// 机票区域类型
					attrMap.put("ticketAreaType",
							Integer.toString(info.getTicket_area_type()));
					// 航段序号
					attrMap.put("number", String.valueOf(info.getNumber()));

					flightList.add(attrMap);
				}
				return flightList;
			} else {
				return new ArrayList();
			}
		} else
			return new ArrayList();
	}

	/**
	 * 获取查询条件
	 * 
	 * @param page
	 * @param airTicket
	 * @param orderBy
	 * @return
	 */
	private DetachedCriteria getDetachedCriteria(Page<ActivityAirTicket> page,
			ActivityAirTicket airTicket, String orderBy, User user,
			String haveYw, String haveQw, String groupCodeOrActSer) {
		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("productStatus",
				Integer.valueOf(Context.PRODUCT_ONLINE_STATUS)));
		if (StringUtils.isNotBlank(airTicket.getProductCode())) {
			dc.add(Restrictions.eq("productCode", airTicket.getProductCode()));
		}

		// 是否还有余位
		if (StringUtils.isNotBlank(haveYw)) {
			if ("1".equals(haveYw)) {
				dc.add(Restrictions.and(Restrictions.ne("freePosition", 0),
						Restrictions.isNotNull("freePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("freePosition", 0),
						Restrictions.isNull("freePosition")));
			}

		}
		// 是否还有切位
		if (StringUtils.isNotBlank(haveQw)) {
			if ("1".equals(haveQw)) {
				dc.add(Restrictions.and(
						Restrictions.ne("payReservePosition", 0),
						Restrictions.isNotNull("payReservePosition")));
			} else {
				dc.add(Restrictions.or(
						Restrictions.eq("payReservePosition", 0),
						Restrictions.isNull("payReservePosition")));
			}

		}

		if (StringUtils.isNotBlank(groupCodeOrActSer)) {
			dc.add(Restrictions.or(
					Restrictions.like("productCode", "%" + groupCodeOrActSer
							+ "%"),
					Restrictions.like("groupCode", "%" + groupCodeOrActSer
							+ "%")));
		}

		Disjunction ordj = Restrictions.or(Restrictions.eq("activityScope",
				Context.ACTIVITY_SCOPE_ALL));
		ordj.add(Restrictions.and(Restrictions.eq("activityScope",
				Context.ACTIVITY_SCOPE_COMPANY), Restrictions.eq("proCompany",
				user.getCompany().getId())));

		if (airTicket.getCreateBy() != null) {
			if (airTicket.getCreateBy().getCompany().getId() != null) {
				dc.add(Restrictions
						.sqlRestriction(
								" EXISTS(select 1 from sys_user a where a.id = {alias}.createBy and a.companyId = ?)",
								airTicket.getCreateBy().getCompany().getId(),
								new LongType()));
			}
		}

		if (user.getAgentId() != null) {

			ordj.add(Restrictions.and(
					Restrictions.eq("activityScope",
							Context.ACTIVITY_SCOPE_AGENT),
					Restrictions
							.sqlRestriction(
									" EXISTS(select 1 from sys_user a left join agentinfo b on a.agentId = b.id  where a.id = {alias}.createBy and b.id = ?)",
									user.getAgentId(), new LongType())));
		}
		dc.add(ordj);

		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (StringUtils.isNotBlank(airTicket.getTicket_area_type())) {
			appendSql += " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null && airTicket.getSpaceGrade() != 0) {
			appendSql += " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql += " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		if (StringUtils.isNotBlank(airTicket.getAirType())) {
			dc.add(Restrictions.eq("airType", airTicket.getAirType()));
		}

		if (StringUtils.isNotBlank(airTicket.getArrivedCity())
				&& !"0".equals(airTicket.getArrivedCity())) {
			dc.add(Restrictions.eq("arrivedCity", airTicket.getArrivedCity()));
		}
		if (StringUtils.isNotBlank(airTicket.getDepartureCity())
				&& !"0".equals(airTicket.getDepartureCity())) {
			dc.add(Restrictions.eq("departureCity",
					airTicket.getDepartureCity()));
		}

		if (airTicket.getStartingDate() != null) {
			dc.add(Restrictions.ge("startingDate", airTicket.getStartingDate()));
		}

		if (airTicket.getReturnDate() != null) {
			dc.add(Restrictions.le("returnDate", airTicket.getReturnDate()));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("startingDate"));
		}

		if (StringUtils.isNotEmpty(airTicket.getReserveNumber())) {
			dc.add(Restrictions.eq("reservationsNum",
					Integer.valueOf(airTicket.getReserveNumber())));
		}

		return dc;
	}

	@Override
	public Page<ActivityAirTicket> findActivityAirTicketPageByOrder(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String orderBy, User user, String haveYw, String haveQw,
			String groupCodeOrActSer) {
		DetachedCriteria dc = getDetachedCriteria(page, airTicket, orderBy,
				user, haveYw, haveQw, groupCodeOrActSer);
		Page<ActivityAirTicket> returnPage = activityAirTicketDao
				.find(page, dc);
		if (returnPage.getCount() == 0) {
			return returnPage;
		}
		List<ActivityAirTicket> activityAirTicketList = returnPage.getList();
		// 更新切位、订单数属性
		updateOtherPropertys(activityAirTicketList);
		return returnPage;
	}

	/**
	 * 获取机票产品Id
	 * 
	 * @param activityAirTicketList
	 * @return
	 */
	private List<Long> getActivityIdList(
			List<ActivityAirTicket> activityAirTicketList) {
		List<Long> idList = new ArrayList<Long>();
		for (ActivityAirTicket each : activityAirTicketList) {
			idList.add(each.getId());
		}
		return idList;
	}

	/**
	 * 更新其他属性
	 * 
	 * @param activityAirTicketList
	 */
	private void updateOtherPropertys(
			List<ActivityAirTicket> activityAirTicketList) {
		List<Long> idList = getActivityIdList(activityAirTicketList);
		Map<String, String> reserveNumMap = queryAirticketReserveNum(idList);
		Map<String, String> orderNumMap = queryAirticketOrderNum(idList);
		String key = null;
		String value = null;
		for (ActivityAirTicket each : activityAirTicketList) {
			key = String.valueOf(each.getId());
			value = reserveNumMap.get(key);
			if (value != null) {
				each.setPayReserveNumber(Integer.valueOf(value));
			}

			value = orderNumMap.get(key);
			if (value != null) {
				each.setOrderNumber(Integer.valueOf(value));
			}
		}

	}

	private String getSqlParam(List<Long> idList) {
		StringBuffer buffer = new StringBuffer();
		for (@SuppressWarnings("unused")
		Long id : idList) {
			buffer.append("?,");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();

	}

	/**
	 * 获取机票产品切位数
	 * 
	 * @param idList
	 * @return
	 */
	private Map<String, String> queryAirticketReserveNum(List<Long> idList) {
		String sqlString = "select a.activityId, "
				+ " sum(a.payReservePosition) payReservePosition "
				+ " from airticketactivityreserve a "
				+ " where a.activityId in (" + getSqlParam(idList) + ") "
				+ " and a.delFlag = 0" + " group by a.activityId";

		List<Map<String, String>> reserveNumMapList = this.activityAirTicketDao
				.findBySql(sqlString, Map.class, idList.toArray());
		Map<String, String> reserveNumMap = new HashMap<String, String>();
		if (null != reserveNumMapList && reserveNumMapList.size() > 0) {
			for (Map<String, String> each : reserveNumMapList) {
				reserveNumMap.put(String.valueOf(each.get("activityId")),
						String.valueOf(each.get("payReservePosition")));
			}
		}

		return reserveNumMap;
	}

	/**
	 * 获取机票产品订单数
	 * 
	 * @param idList
	 * @return
	 */
	private Map<String, String> queryAirticketOrderNum(List<Long> idList) {
		String sqlString = "select t.airticket_id,"
				+ " SUM(t.person_num) person_num " + " from airticket_order t "
				+ " where t.airticket_id in (" + getSqlParam(idList) + ")"
				+ " and t.del_flag = 0 " + " group by t.airticket_id";

		List<Map<String, String>> reserveNumMapList = this.activityAirTicketDao
				.findBySql(sqlString, Map.class, idList.toArray());
		Map<String, String> personNumMap = new HashMap<String, String>();
		if (null != reserveNumMapList && reserveNumMapList.size() > 0) {
			for (Map<String, String> each : reserveNumMapList) {
				personNumMap.put(String.valueOf(each.get("airticket_id")),
						String.valueOf(each.get("person_num")));
			}
		}

		return personNumMap;
	}

	@Override
	public List<AirlineInfo> findAirlineByComid(Long companyId) {
		List<Map<String, String>> mapList = airticketDao
				.findAirlineByComid(companyId);
		List<AirlineInfo> list = new ArrayList<AirlineInfo>();
		AirlineInfo entity = null;
		for (Map<String, String> map : mapList) {
			entity = new AirlineInfo();
			entity.setAirlineCode(map.get("airline_code"));
			entity.setAirlineName(map.get("airline_name"));
			list.add(entity);
		}
		return list;
	}

	/**
	 * 获取仓位
	 * 
	 * @param companyId
	 * @param airlineCode
	 * @param spaceLevel
	 * @return
	 */
	@Override
	public List<Map<String, String>> findAirlSpaceList(Long companyId,
			String airlineCode, String spaceLevel) {
		List<Map<String, String>> list = this.airticketDao
				.findAirline_spaceName(companyId, airlineCode, spaceLevel);
		return list;
	}

	/**
	 * 获取仓位等级
	 * 
	 * @param companyId
	 * @param airlineCode
	 * @return
	 */
	@Override
	public List<Map<String, String>> findSpaceLevelList(Long companyId,
			String airlineCode) {
		List<Map<String, String>> list = this.airticketDao
				.findAirline_spacelevel(companyId, airlineCode);
		return list;
	}

	@Override
	public void deleteAirlineInfoById(Long id) {
		airticketDao.deleteAirlineInfoById(id);
	}

	@Override
	public void deleteAirlineInfoByAirTicketId(Long id) {
		airticketDao.deleteAirlineInfoByAirTicketId(id);
	}

	@Override
	public void deleteDocInfosByAirTicketId(Long id) {
		airticketDao.deleteDocInfosByAirTicketId(id);
	}

	@Override
	public void deleteIntermodalStrategiesByAirTicketId(Long id) {
		airticketDao.deleteIntermodalStrategiesByAirTicketId(id);
	}

	@Override
	public void deleteRelationByAirTicketId(Long id) {
		airticketDao.deleteAirlineInfoByAirTicketId(id);
		airticketDao.deleteIntermodalStrategiesByAirTicketId(id);
		airticketDao.deleteDocInfosByAirTicketId(id);
	}

	@Override
	public List<Map<String, String>> getDocsByAirTicketId(Long id) {
		return airticketDao.getDocsByAirTicketId(id);
	}

	@Override
	public List<Map<String, String>> getDeptList(Long id) {
		return airticketDao.getDeptList(id);
	}

	@Override
	public Page<ActivityAirTicket> findAirTicketList(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId, String orderBy) {
		// TODO Auto-generated method stub

		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();

		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));

		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}
		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.between("startingDate", startDate, endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}

		return activityAirTicketDao.find(page, dc);
	}

	/* 切位--机票产品列表 */
	@Override
	public Page<ActivityAirTicket> findAirTicketStock(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId, Long agentId, String orderBy) {
		// TODO Auto-generated method stub

		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();
		// 20151013机票产品增加团号搜索条件
		if (StringUtils.isNotBlank(airTicket.getGroupCode())) {
			dc.add(Restrictions.like("groupCode",
					"%" + airTicket.getGroupCode() + "%"));
		}
		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		if (agentId != null) {
			dc.add(Restrictions
					.sqlRestriction(
							"{alias}.id in (select activityId from airticketactivityreserve where agentId =(?) )",
							agentId.toString(), StringType.INSTANCE));
		}

		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));

		// 上架的产品可以进行切位操作 。2 表示上架，3表示下架
		dc.add(Restrictions.eq("productStatus", 2));

		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}
		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			dc.add(Restrictions.ge("startingDate", startDate));
		}

		if (StringUtils.isNotBlank(endTime)) {
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.le("startingDate", endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		if (StringUtils.isEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
//			page.setOrderBy("id");
		}

		return activityAirTicketDao.find(page, dc);
	}
	
	/**
	 * 获取机票订单的可切位产品
	 * @Description: 
	 * @param @param page 分页属性
	 * @param @param airTicket 机票产品信息
	 * @param @param departureCity 出发城市
	 * @param @param arrivedCity 到达城市
	 * @param @param minprice 成人最低价
	 * @param @param maxprice 成人最高价
	 * @param @param airType
	 * @param @param startTime 出团开始时间
	 * @param @param endTime 出团结束时间
	 * @param @param companyId 公司id
	 * @param @param agentId 批发商id
	 * @param @param orderBy 排序字段
	 * @param @param source 标识是切位还是返还切位调用(isReserve表示切位团期列表，isReturn表示返还切位团期列表)
	 * @param @return   
	 * @return Page<ActivityAirTicket>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-22 下午2:49:00
	 */
	@Override
	public Page<ActivityAirTicket> findAirTicketStock(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			String departureCity, String arrivedCity, BigDecimal minprice,
			BigDecimal maxprice, String airType, String startTime,
			String endTime, Long companyId, Long agentId,Integer freePositionStart,Integer freePositionEnd, String orderBy, String source) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();
		
		//添加出发日期过滤 需要大于当前日期
		dc.add(Restrictions.gt("startingDate", new Date()));
		
		// 20151013机票产品增加团号搜索条件
		if (StringUtils.isNotBlank(airTicket.getGroupCode())) {
			dc.add(Restrictions.like("groupCode",
					"%" + airTicket.getGroupCode() + "%"));
		}
		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();
		if (airTicket.getTicket_area_type() != null) {
			appendSql = " and b.ticket_area_type = ?";
			paramList.add(Integer.valueOf(airTicket.getTicket_area_type()));
			typeList.add(new IntegerType());
		}
		if (airTicket.getSpaceGrade() != null) {
			appendSql = " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}

		if (StringUtils.isNotBlank(airTicket.getAirlines())) {

			appendSql = " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}

		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";

			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}

		if (agentId != null) {
			dc.add(Restrictions
					.sqlRestriction(
							"{alias}.id in (select activityId from airticketactivityreserve where agentId =(?) )",
							agentId.toString(), StringType.INSTANCE));
		}
		if(freePositionStart != null){
			dc.add(Restrictions.ge("freePosition", freePositionStart));
		}
		if(freePositionEnd != null){
			dc.add(Restrictions.le("freePosition", freePositionEnd));
		}
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("proCompany", companyId));

		// 上架的产品可以进行切位操作 。2 表示上架，3表示下架
		dc.add(Restrictions.eq("productStatus", 2));

		if (StringUtils.isNotBlank(arrivedCity)) {
			dc.add(Restrictions.eq("arrivedCity", arrivedCity));
		}
		if (StringUtils.isNotBlank(departureCity)) {
			dc.add(Restrictions.eq("departureCity", departureCity));
		}

		if (minprice != null) {
			dc.add(Restrictions.ge("settlementAdultPrice", minprice));
		}
		if (maxprice != null) {
			dc.add(Restrictions.le("settlementAdultPrice", maxprice));
		}

		if (String.valueOf(airTicket.getProductStatus()).equals(
				Context.PRODUCT_OFFLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_ONLINE_STATUS)
				|| String.valueOf(airTicket.getProductStatus()).equals(
						Context.PRODUCT_TEMP_STATUS)) {
			dc.add(Restrictions.eq("productStatus",
					airTicket.getProductStatus()));
		}

		if (StringUtils.isNotBlank(startTime)) {
			java.util.Date startDate = DateUtils.dateFormat(startTime,
					"yyyy-MM-dd");
			dc.add(Restrictions.ge("startingDate", startDate));
		}

		if (StringUtils.isNotBlank(endTime)) {
			java.util.Date endDate = DateUtils
					.dateFormat(endTime, "yyyy-MM-dd");
			dc.add(Restrictions.le("startingDate", endDate));
		}

		if (StringUtils.isNotBlank(airType)) {
			dc.add(Restrictions.eq("airType", airType));
		}

		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}
		
		// 根据切位和归还切位信息进行筛选
		if(StringUtils.isEmpty(source) || StringUtils.equals("isReserve", source)) {
			dc.add(Restrictions.gt("freePosition",Integer.valueOf(0)));
		} else if(StringUtils.equals("isReturn", source)){
			dc.add(Restrictions.gt("payReservePosition",Integer.valueOf(0)));
		}

		return activityAirTicketDao.find(page, dc);
	}

	/**
	 * 获取团号
	 * 
	 * @param id
	 *            airticket_order表 main_order_id select orderNum from
	 *            productorder where id = 'main_order_id'
	 * @return
	 */

	@Override
	public ProductOrder getProductById(Long id) {

		return (ProductOrder) getHibernateSession().get(ProductOrder.class, id);

	}

	@Override
	public String getActivitygroupById(Long id) {

		return airticketDao.getActivitygroupById(id);
	}

	@Override
	public List<Map<String, Object>> getProductInfoForSettle(Long productId) {
		StringBuffer str = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C486锁定后的数据不计入结算单 add by shijun.liu 2015.12.25
			//结算单锁定之后录入的新订单数据(settle_locked_in == 1)，不进入结算单,解锁后(settle_locked_in == null)进入
			isLockedIn = " AND o.settle_locked_in is null ";
		}
		//0258 懿洋假期 发票税  王洋   2016.3.31
		String invoiceTax = "";
		String invoice_tax = "";
		if(Context.SUPPLIER_UUID_YYJQ.equals(currentCompanyUuid)){
			invoice_tax = "invoiceTax ";
			invoiceTax = " p.invoice_tax AS invoiceTax, ";
		}
		
		str.append("SELECT createBy,groupCode,GROUP_CONCAT(departureCity, '-',arrivedCity, ':',airType) AS productName,")
				.append(" groupOpenDate,groupCloseDate,orderPersonNumSum,lockStatus,forcastStatus, createById,").append(invoice_tax)
				.append(" FORMAT(settlementAdultPrice,2) AS settlementAdultPrice,currencyType FROM ")
				.append(" (SELECT p.id,(SELECT su.`name` FROM sys_user su WHERE su.id = p.createBy) AS createBy,")
				.append(" p.createBy as createById, CASE p.airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 ")
				.append(" THEN '单程' ELSE '其他' END AS airType, p.group_code AS groupCode,(select label from ")
				.append(" sys_dict where type = 'from_area' and delFlag = '0' and value = p.departureCity) AS departureCity,")
				.append(" (SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = p.arrivedCity) AS arrivedCity,")
				.append(" CASE p.airType WHEN 3 THEN (SELECT startTime FROM activity_flight_info af WHERE af.airticketId = p.id LIMIT 0,1) ")
				.append(" ELSE p.startingDate END AS groupOpenDate, ")
				.append(" CASE p.airType WHEN 3 THEN (SELECT arrivalTime FROM activity_flight_info af WHERE af.airticketId = p.id LIMIT 0,1) ")
				.append(" ELSE p.returnDate END AS groupCloseDate, ")
				.append(" sum(o.person_num) orderPersonNumSum,").append(invoiceTax)
				.append(" p.lockStatus, p.forcastStatus,p.settlementAdultPrice,p.currency_id AS currencyType FROM activity_airticket p ")
				.append(" left join airticket_order o on p.id = o.airticket_id AND o.order_state not in (7, 99, 111) ")
				.append(isLockedIn).append(" AND o.del_flag = '0' where p.id = ")
				.append(productId).append(" and p.proCompany = ").append(companyId).append(" GROUP BY p.id) t1 ");
		return activityAirTicketDao.findBySql(str.toString(), Map.class);
	}
	
	@Override
	public List<Map<String, Object>> getProductInfoForForcast(Long productId) {
		StringBuffer str = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C457锁定后的数据不计入预报单 add by shijun.liu 2015.12.25
			//预报单锁定之后录入的新订单数据(forecast_locked_in == 1)，不进入预报单,解锁后(forecast_locked_in == null)进入
			isLockedIn = " AND o.forecast_locked_in is null ";
		}
		str.append("SELECT createBy,groupCode,GROUP_CONCAT(departureCity, '-',arrivedCity, ':',airType) AS productName,")
				.append(" groupOpenDate,groupCloseDate,orderPersonNumSum,lockStatus,forcastStatus, createById, settlementAdultPrice,groupEndDate ")
				.append(" FROM ")
				.append(" (SELECT p.id,(SELECT su.`name` FROM sys_user su WHERE su.id = p.createBy) AS createBy,")
				.append(" p.createBy as createById, CASE p.airType WHEN 1 THEN '多段' WHEN 2 THEN '往返' WHEN 3 ")
				.append(" THEN '单程' ELSE '其他' END AS airType, p.group_code AS groupCode,(select label from ")
				.append(" sys_dict where type = 'from_area' and delFlag = '0' and value = p.departureCity) AS departureCity,")
				.append(" (SELECT NAME FROM sys_area area WHERE delFlag = '0' AND area.id = p.arrivedCity) AS arrivedCity,")
				.append(" p.startingDate AS groupOpenDate,p.returnDate AS groupCloseDate,sum(o.person_num) orderPersonNumSum,")
				.append(" p.lockStatus, p.forcastStatus, p.settlementAdultPrice, p.returnDate AS groupEndDate ")
				.append(" FROM activity_airticket p ")
				.append(" left join airticket_order o on p.id = o.airticket_id AND o.order_state not in (7, 99, 111) ")
				.append(isLockedIn).append(" AND o.del_flag = '0' where p.id = ").append(productId)
				.append(" and p.proCompany = ").append(companyId).append(isLockedIn).append(" GROUP BY p.id) t1 ");
		return activityAirTicketDao.findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		//拉美途 过滤预报单锁定之后生成的订单，退款数据 C457 add by shijun.liu 2015.12.21
		// forecast_locked_in = 1 表示是预报单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.forecast_locked_in is null ";
			lockedInRefund = " AND cost.forecast_locked_in is null ";
		}
		str.append("SELECT id,saler,agentName,")
				.append(" IFNULL(totalMoney, 0) AS totalMoney,")
				.append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
				.append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
				.append(" IFNULL(r.refundprice, 0) AS refundprice, ")
				.append(" orderPersonNum,")
				.append(" adultNum,childrenNum,specialNum,adultAmount,childrenAmount,specialAmount ")// 0546 wangyang 2016.11.9
				.append(" FROM (")
				.append(" SELECT o.id,")
				.append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
				.append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
				.append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM money_amount m1 ")
				.append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
				.append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
				.append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
				.append(" o.person_num AS orderPersonNum, ")
				// 0546 wangyang 2016.11.9 ---S
				.append(" o.adult_num AS adultNum, ")
				.append(" o.child_num AS childrenNum, ")
				.append(" o.special_num AS specialNum, ")
				.append(" p.settlementAdultPrice AS adultAmount, ")
				.append(" p.settlementcChildPrice AS childrenAmount, ")
				.append(" p.settlementSpecialPrice AS specialAmount ")
				// 0546 wangyang 2016.11.9 ---E
				.append(" FROM ").append(" airticket_order o, activity_airticket p ")
				.append(" WHERE o.del_flag = '0' ")
				.append(" AND o.order_state NOT IN (7, 99, 111)")
				.append(" AND p.id = o.airticket_id ")
				.append(" AND o.airticket_id = ").append(productId)
				.append(lockedIn)
				.append(" ) t1 LEFT JOIN (")
				.append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
				.append(" FROM cost_record cost ")
				.append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =")
				.append(orderType).append(" AND cost.budgetType = ")
				.append(CostManageService.BUDGET_TYPE).append(lockedInRefund)
				.append(" GROUP BY cost.orderId ").append(" ) r on r.orderId = t1.id ");
//		return activityAirTicketDao.findBySql(str.toString(), Map.class);
		
		// 0546 骡子假期 区分游客类型 wangyang 2016.11.9
		List<Map<String, Object>> orderIncomes = activityAirTicketDao.findBySql(str.toString(), Map.class);
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> exceptIncome4LZJQ = new ArrayList<>();
		// 报名游客类型 adult--成人 children--儿童 special--特殊人群
		String[] customerType = {"adult", "children", "special"};
			
		for (Map<String, Object> orderIncome : orderIncomes) {
			for (String type : customerType) {
				Integer num = (Integer) orderIncome.get(type + "Num");
				if (num > 0) { // 存在对应类型客户 拆分为一条数据
					Map<String, Object> income = new HashMap<>();
					BigDecimal price = new BigDecimal(orderIncome.get(type + "Amount").toString());
					BigDecimal totalPrice = price.multiply(new BigDecimal(num));
					
					// 组团社（渠道）
					income.put("agentName", orderIncome.get("agentName"));	
					// 单价
					income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));	
					// 人数
					income.put("personNum", num);	
					// 金额（人数*单价）
					income.put("totalPrice", MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));	
					// 销售姓名
					income.put("saler", orderIncome.get("saler"));			
					exceptIncome4LZJQ.add(income);
				}
			}
		}
		map.put("exceptIncome4LZJQ", exceptIncome4LZJQ);
		orderIncomes.add(map);
		return orderIncomes;
	}
	
	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_LZJQ.equals(currentCompanyUuid)){
			return getOrderAndRefundInfoSettleForLZJQ(productId,orderType);
		}
		//拉美途 过滤结算单锁定之后生成的订单，退款数据 C486 add by shijun.liu 2015.12.21
		// settle_locked_in = 1 表示是结算单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.settle_locked_in is null ";
			lockedInRefund = " AND cost.settle_locked_in is null ";
		}
		str.append("SELECT id,saler,agentName,")
				.append(" IFNULL(totalMoney, 0) AS totalMoney,")
				.append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
				.append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
				.append(" IFNULL(r.refundprice, 0) AS refundprice, ")
				.append(" orderPersonNum ")
				.append(" FROM (")
				.append(" SELECT o.id,")
				.append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
				.append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
				.append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM money_amount m1 ")
				.append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
				.append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
				.append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
				.append(" o.person_num AS orderPersonNum ")
				.append(" FROM ").append(" airticket_order o")
				.append(" WHERE o.del_flag = '0' ")
				.append(" AND o.order_state NOT IN (7, 99, 111)")
				.append(" AND o.airticket_id = ").append(productId)
				.append(lockedIn)
				.append(" ) t1 LEFT JOIN (")
				.append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
				.append(" FROM cost_record cost ")
				.append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =")
				.append(orderType).append(" AND cost.budgetType = ")
				.append(CostManageService.ACTUAL_TYPE).append(lockedInRefund);
				//start 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月25日09:52:06
				if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
					str.append(" AND cost.reviewStatus in ('审批通过', '审核通过') ");
				} else {
					str.append(" AND cost.reviewStatus not in ('已取消','已驳回') ");
				}
				//end 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy
				str.append(" GROUP BY cost.orderId ").append(" ) r on r.orderId = t1.id ");
		return activityAirTicketDao.findBySql(str.toString(), Map.class);
	}

	/**
	 * 针对骡子假期的查询使用下面的sql。
	 * @param productId
	 * @param orderType
	 * @return
	 * @author yudong.xu 2016.11.10
	 */
	private List<Map<String, Object>> getOrderAndRefundInfoSettleForLZJQ(Long productId, Integer orderType) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, saler, agentName, IFNULL(totalMoney, 0) AS totalMoney, IFNULL(accountedMoney, 0) AS ")
		.append("accountedMoney, IFNULL((totalMoney - accountedMoney), 0 ) AS notAccountedMoney, ")
		.append("IFNULL(r.refundprice, 0) AS refundprice,")
		.append("FORMAT(adultPrice, 2) AS adultPrice, FORMAT(childPrice, 2) AS childPrice, FORMAT(specialPrice, 2) ")
		.append("AS specialPrice, (adultPrice * adultNum) AS adultMoney, (childPrice * childNum) AS childMoney,")
		.append("(specialPrice * specialNum) AS specialMoney, adultNum, childNum, specialNum, orderPersonNum ")
		.append(" FROM ( SELECT o.id, ( SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId ) AS saler, ")
		.append("( SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id ) AS agentName,")
		.append("( SELECT ifnull( sum(m1.amount * m1.exchangerate), 0 ) FROM money_amount m1 WHERE ")
		.append("m1.serialNum = o.total_money ) AS totalMoney, ( SELECT ifnull( sum(m2.amount * m2.exchangerate), 0 ) ")
		.append(" FROM money_amount m2 WHERE m2.serialNum = o.accounted_money ) AS accountedMoney,")
		.append("p.settlementAdultPrice AS adultPrice, p.settlementcChildPrice AS childPrice, ")
		.append("p.settlementSpecialPrice AS specialPrice, o.adult_num AS adultNum, ")
		.append("o.child_num AS childNum, o.special_num AS specialNum, o.person_num AS orderPersonNum ")
		.append(" FROM airticket_order o LEFT JOIN activity_airticket p ON o.airticket_id = p.id ")
		.append(" WHERE o.del_flag = '0' AND o.order_state NOT IN (7, 99, 111) AND o.airticket_id = ? ) t1")
		.append(" LEFT JOIN (SELECT cost.orderId,sum(cost.price) AS refundprice FROM cost_record cost ")
		.append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType = ? AND cost.budgetType = 1  ")
		.append(" AND cost.reviewStatus not in ('已取消','已驳回') GROUP BY cost.orderId ) r on r.orderId = t1.id");
		return activityAirTicketDao.findBySql(sql.toString(), Map.class,productId,orderType);
	}

	/**
	 * 
	 **/
	public List<Map<String, Object>> getRefunifoForCastList(Long productId,
			Integer orderType) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_QZ) { // 签证
			sBuffer.append("SELECT t1.id, t1.saler, t1.agentName, IFNULL(sum(t1.totalMoney), 0) AS totalMoney, IFNULL(sum(t1.accountedMoney), 0) AS "
					+ "accountedMoney, IFNULL((totalMoney - accountedMoney), 0 ) AS notAccountedMoney, IFNULL(costrecord.refundprice, 0) AS "
					+ "refundprice FROM ( SELECT o.id, ( SELECT su. NAME FROM sys_user su WHERE su.id = o.create_by ) saler, ( SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id ) "
					+ "agentName, (select sum(ma1.amount * IFNULL(ma1.exchangerate, 1)) from money_amount ma1 where ma1.serialNum = o.total_money) AS totalMoney,	(select sum(ma2.amount * IFNULL(ma2.exchangerate, 1)) from money_amount ma2 where ma2.serialNum = o.accounted_money) AS accountedMoney  "
					+ "FROM visa_order o  "
					+ "WHERE o.del_flag = '0' AND o.payStatus <> 7 AND o.payStatus <> 99 AND o.visa_product_id = ");
			sBuffer.append(productId
					+ " ) t1 LEFT JOIN (select cr.orderId,sum(cr.price) AS refundprice from cost_record cr where cr.reviewType=1 AND cr.reviewStatus like '审核通过' GROUP BY cr.orderId) costrecord  on costrecord.orderId=t1.id  GROUP BY id order by saler");

		} else if (orderType == Context.ORDER_TYPE_JP) {// 机票
			sBuffer.append("SELECT t1.id, t1.saler, t1.agentName, IFNULL(sum(t1.totalMoney), 0) AS totalMoney, IFNULL(sum(t1.accountedMoney), 0) AS accountedMoney, "
					+ "IFNULL((totalMoney - accountedMoney), 0 ) AS notAccountedMoney, IFNULL(costrecord.refundprice, 0) AS refundprice FROM ( SELECT o.id, "
					+ "( SELECT su. NAME FROM sys_user su WHERE su.id = o.create_by ) saler, ( SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id ) "
					+ " agentName, (select sum(ma1.amount * IFNULL(ma1.exchangerate, 1)) from money_amount ma1 where ma1.serialNum = o.total_money) AS totalMoney,	(select sum(ma2.amount * IFNULL(ma2.exchangerate, 1)) from money_amount ma2 where ma2.serialNum = o.accounted_money) AS accountedMoney  FROM "
					+ "airticket_order o  "
					+ "WHERE o.del_flag = '0' AND o.order_state<>7 AND o.order_state<>99 AND o.order_state<>111 "
					+ "AND o.airticket_id = ");
			sBuffer.append(productId
					+ " ) t1 LEFT JOIN (select cr.orderId,sum(cr.price) AS refundprice from cost_record cr where cr.reviewType=1 AND cr.reviewStatus like '审核通过' GROUP BY cr.orderId) costrecord  on costrecord.orderId=t1.id GROUP BY id order by saler");

		} else {// 团期
			sBuffer.append("SELECT t1.id, t1.saler, t1.agentName, IFNULL(sum(t1.totalMoney), 0) AS totalMoney, IFNULL(sum(t1.accountedMoney), 0) "
					+ "AS accountedMoney, IFNULL((totalMoney - accountedMoney), 0 ) AS notAccountedMoney, IFNULL(costrecord.refundprice, 0) AS "
					+ "refundprice FROM ( SELECT o.id, ( SELECT su. NAME FROM sys_user su WHERE su.id = o.createBy ) saler, "
					+ "( SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany ) agentName, (select sum(ma1.amount * IFNULL(ma1.exchangerate, 1)) from money_amount ma1 where ma1.serialNum = o.total_money) AS totalMoney,	(select sum(ma2.amount * IFNULL(ma2.exchangerate, 1)) "
					+ "from money_amount ma2 where ma2.serialNum = o.accounted_money) AS accountedMoney  FROM productorder o  WHERE o.delFlag = '0' AND o.payStatus <> 7 AND o.payStatus <> 99 AND o.payStatus <> 111 AND o.productGroupId =");
			sBuffer.append(productId
					+ " ) t1 LEFT JOIN (select cr.orderId,sum(cr.price) AS refundprice from cost_record cr where cr.reviewType=1 AND cr.reviewStatus like '审核通过' GROUP BY cr.orderId) costrecord  on costrecord.orderId=t1.id GROUP BY t1.id order by saler");
		}
		list = activityAirTicketDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}

	/**
	 * 预报单预计收款查询
	 * */
	public List<Map<String, Object>> getCost(Long activityId,
			Integer orderType, Integer budgetType) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();

		if (budgetType == 1) { // 实际成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record WHERE activityid="
					+ activityId
					+ " AND ordertype="
					+ orderType
					+ " AND budgetType="
					+ budgetType
					+ " and reviewType=0 and review=2  and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record WHERE activityid="
					+ activityId
					+ " AND ordertype="
					+ orderType
					+ " AND budgetType="
					+ budgetType
					+ " and reviewType=2 and reviewStatus<>'已取消' and reviewStatus<>'已驳回' and delFlag='0') t ");

		} else { // 预算成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record WHERE activityid="
					+ activityId
					+ " AND ordertype="
					+ orderType
					+ " AND budgetType="
					+ budgetType
					+ " and reviewType=0  and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record WHERE activityid="
					+ activityId
					+ " AND ordertype="
					+ orderType
					+ " AND budgetType="
					+ budgetType
					+ " and reviewType=2  and reviewStatus<>'已取消' and delFlag='0') t ");
		}
		list = activityAirTicketDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}

	/**
	 * 预报单预计收款查询
	 * */
	public List<Map<String, Object>> getHotelCost(String activityUuid,
			Integer budgetType) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();

		if (budgetType == 1) { // 实际成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_hotel WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=0 and review=2  and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_hotel WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=2 and reviewStatus<>'已取消' and reviewStatus<>'已驳回' and delFlag='0') t ");

		} else { // 预算成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_hotel WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=0  and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_hotel WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=2  and reviewStatus<>'已取消' and delFlag='0') t ");
		}

		list = activityAirTicketDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}

	/**
	 * 预报单预计收款查询
	 * */
	public List<Map<String, Object>> getIslandCost(String activityUuid,
			Integer budgetType) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();

		if (budgetType == 1) { // 实际成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_island WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=0 and review=2 and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_island WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=2 and reviewStatus<>'已取消' and reviewStatus<>'已驳回' and delFlag='0') t ");

		} else { // 预算成本
			sBuffer.append(" SELECT  SUM(cost) as cost  FROM  (  SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_island WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=0  and  delFlag='0'");
			sBuffer.append(" UNION ALL ");
			sBuffer.append(" SELECT IFNULL(SUM(priceAfter), SUM(quantity*rate* price)) AS cost FROM cost_record_island WHERE activity_uuid='"
					+ activityUuid
					+ "' AND budgetType="
					+ budgetType
					+ " and reviewType=2  and reviewStatus<>'已取消' and delFlag='0') t ");

		}
		list = activityAirTicketDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}

	@Override
	public Page<ActivityAirTicket> findActivityAirTicketPageByOrder(
			Page<ActivityAirTicket> page, ActivityAirTicket airTicket,
			User user, Map<String, String> mapRequest) {

		DetachedCriteria dc = getDetachedCriteria(page, airTicket, user,
				mapRequest);
		Page<ActivityAirTicket> returnPage = activityAirTicketDao
				.find(page, dc);
		if (returnPage.getCount() == 0) {
			return returnPage;
		}
		List<ActivityAirTicket> activityAirTicketList = returnPage.getList();
		// 更新切位、订单数属性
		updateOtherPropertys(activityAirTicketList);
		
		// 为每个机票产品添加剩余切位人数
		addLeftPayReservePosition(activityAirTicketList);
		return returnPage;
	}

	/**
	 * 获取查询条件(使用map封装若干条件)
	 * 
	 * @author jyang
	 * @param page
	 * @param airTicket
	 * @param orderBy
	 * @return
	 */
	private DetachedCriteria getDetachedCriteria(Page<ActivityAirTicket> page,
			ActivityAirTicket airTicket, User user,
			Map<String, String> mapRequest) {

		String orderBy = mapRequest.get("orderBy");
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = "createDate";
		}
		String groupCodeOrActSer = StringUtils.trim(mapRequest
				.get("groupCodeOrActSer"));
		String haveYw = mapRequest.get("haveYw");
		String haveQw = mapRequest.get("haveQw");
		DetachedCriteria dc = activityAirTicketDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("productStatus",
				Integer.valueOf(Context.PRODUCT_ONLINE_STATUS)));
		if (StringUtils.isNotBlank(groupCodeOrActSer)) {
			dc.add(Restrictions.or(
					Restrictions.like("productCode", "%" + groupCodeOrActSer
							+ "%"),
					Restrictions.like("groupCode", "%" + groupCodeOrActSer
							+ "%")));
		}

		// 是否还有余位
		if (StringUtils.isNotBlank(haveYw)) {
			if ("1".equals(haveYw)) {
				dc.add(Restrictions.and(Restrictions.ne("freePosition", 0),
						Restrictions.isNotNull("freePosition")));
			} else {
				dc.add(Restrictions.or(Restrictions.eq("freePosition", 0),
						Restrictions.isNull("freePosition")));
			}
		}
		// 是否还有切位
		if (StringUtils.isNotBlank(haveQw)) {
			if ("1".equals(haveQw)) {
				dc.add(Restrictions.and(
						Restrictions.ne("payReservePosition", 0),
						Restrictions.isNotNull("payReservePosition")));
			} else {
				dc.add(Restrictions.or(
						Restrictions.eq("payReservePosition", 0),
						Restrictions.isNull("payReservePosition")));
			}
		}

		Disjunction ordj = Restrictions.or(Restrictions.eq("activityScope",
				Context.ACTIVITY_SCOPE_ALL));
		ordj.add(Restrictions.and(Restrictions.eq("activityScope",
				Context.ACTIVITY_SCOPE_COMPANY), Restrictions.eq("proCompany",
				user.getCompany().getId())));

		dc.add(Restrictions.sqlRestriction(" EXISTS(select 1 from sys_user a where a.id = {alias}.createBy and a.companyId = ?)", UserUtils.getUser().getCompany().getId(), new LongType()));
		
		if (user.getAgentId() != null) {
			ordj.add(Restrictions.and(
					Restrictions.eq("activityScope",
							Context.ACTIVITY_SCOPE_AGENT),
					Restrictions
							.sqlRestriction(
									" EXISTS(select 1 from sys_user a left join agentinfo b on a.agentId = b.id  where a.id = {alias}.createBy and b.id = ?)",
									user.getAgentId(), new LongType())));
		}
		dc.add(ordj);

		String appendSql = "";
		List<Object> paramList = new ArrayList<Object>();
		List<Type> typeList = new ArrayList<Type>();

		if (StringUtils.isNotBlank(airTicket.getTicket_area_type())) {
			appendSql += " and b.ticket_area_type = ?";
			paramList.add(airTicket.getTicket_area_type());
			typeList.add(new StringType());
			// dc.add(Restrictions.eq("ticket_area_type",
			// airTicket.getTicket_area_type()));
		}
		if (airTicket.getSpaceGrade() != null && airTicket.getSpaceGrade() != 0) {
			appendSql += " and b.spaceGrade = ?";
			paramList.add(airTicket.getSpaceGrade());
			typeList.add(new LongType());
		}
		if (StringUtils.isNotBlank(airTicket.getAirlines())) {
			appendSql += " and b.airlines = ?";
			paramList.add(airTicket.getAirlines());
			typeList.add(new StringType());
		}
		String temAppend = "group by {alias}.id having 1=1";
		StringBuffer temAppendBuf = new StringBuffer().append(temAppend);
		if (airTicket.getStartingDate() != null) {
			temAppendBuf
					.append(" and DATE_FORMAT(min(b.startTime),'%Y-%m-%d') = '"
							+ DateUtils.formatDate(airTicket.getStartingDate(),
									DateUtils.DATE_PATTERN_YYYY_MM_DD) + "'");
			// dc.add(Restrictions.ge("startingDate",
			// airTicket.getStartingDate()));

		}
		if (airTicket.getReturnDate() != null) {
			temAppendBuf
					.append(" and DATE_FORMAT(max(b.arrivalTime),'%Y-%m-%d') = '"
							+ DateUtils.formatDate(airTicket.getReturnDate(),
									DateUtils.DATE_PATTERN_YYYY_MM_DD) + "'");
			// dc.add(Restrictions.le("returnDate", airTicket.getReturnDate()));
		}
		if (!temAppend.equals(temAppendBuf.toString())) {
			appendSql += temAppendBuf.toString();
		}
		if (StringUtils.isNotBlank(appendSql)) {
			String sql = " EXISTS(select 1 from activity_flight_info b where b.airticketId = {alias}.id "
					+ appendSql + ")";
			dc.add(Restrictions.sqlRestriction(sql, paramList.toArray(),
					typeList.toArray(new Type[0])));
		}
		if (StringUtils.isNotBlank(airTicket.getAirType())) {
			dc.add(Restrictions.eq("airType", airTicket.getAirType()));
		}
		if (StringUtils.isNotBlank(airTicket.getArrivedCity())
				&& !"0".equals(airTicket.getArrivedCity())) {
			dc.add(Restrictions.eq("arrivedCity", airTicket.getArrivedCity()));
		}
		//操作人
		if (airTicket.getCreateBy() != null) {			
			if (airTicket.getCreateBy().getId() != null) {
				dc.add(Restrictions.eq("createBy.id", airTicket.getCreateBy().getId()));
			}
		}
		if (StringUtils.isNotBlank(airTicket.getDepartureCity())
				&& !"0".equals(airTicket.getDepartureCity())) {
			dc.add(Restrictions.eq("departureCity",
					airTicket.getDepartureCity()));
		}
		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("startingDate"));
		}
		if (StringUtils.isNotEmpty(airTicket.getReserveNumber())) {
			dc.add(Restrictions.eq("reservationsNum",
					Integer.valueOf(airTicket.getReserveNumber())));
		}

		return dc;
	}

	
	/**
	 * 为每个机票产品添加剩余切位人数
	 * @param activityAirTicketList
	 */
	private void addLeftPayReservePosition(List<ActivityAirTicket> activityAirTicketList) {
		for (ActivityAirTicket activityAirTicket : activityAirTicketList) {
			List<AirticketActivityReserve> airticketActivityReserveList = airticketActivityReserveDao.findByActivityId(activityAirTicket.getId());
			Integer totalNum = 0;
			for (AirticketActivityReserve airticketActivityReserve : airticketActivityReserveList) {
				Integer leftpayReservePosition = airticketActivityReserve.getLeftpayReservePosition();
				totalNum += leftpayReservePosition;
			}
			activityAirTicket.setAllLeftPayReservePosition(totalNum);
		}
	}
	

	/**
     * 根据机票产品id获取美途收入单信息
     * @Description: 
     * @param @param airTicketId
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author majiancheng
     * @date 2015-11-11 下午12:25:23
     */
    public Map<String, Object> getMeituIncomeInfoByAirTicketId(Long airTicketId) {
    	return activityAirTicketDao.getMeituIncomeInfoByAirTicketId(airTicketId);
    }

	@Override
	public void updateEntity(ActivityAirTicket airticket) {
		activityAirTicketDao.updateObj(airticket);
	}

	@Override
	public Map<String, Object> getLastCount(Long id) {
		StringBuffer sb = new StringBuffer();
        sb.append("select sum(child_num) orderPersonNumChild, sum(special_num) orderPersonNumSpecial  from airticket_order where airticket_id =? and del_flag = 0");
		List<Map <String, Object>> map =  airticketActivityReserveDao.findBySql(sb.toString(), Map.class, id);
        return map.get(0);
	}

	@Override
	public Map<String, Object> findByGoupid(Long id,String containSelf) {
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isBlank(containSelf)){
			sb.append("select SUM(child_num) orderPersonNumChild,sum(special_num) orderPersonNumSpecial from airticket_order where airticket_id =?  and order_state in (3,4,5)and del_flag = 0");
			List<Map <String, Object>> map =  airticketActivityReserveDao.findBySql(sb.toString(), Map.class, id);
			return map.get(0);
		}else{
			sb.append("select SUM(child_num) orderPersonNumChild,sum(special_num) orderPersonNumSpecial from airticket_order where airticket_id =?  and id != ? and order_state in (3,4,5)and del_flag = 0");
			List<Map <String, Object>> map =  airticketActivityReserveDao.findBySql(sb.toString(), Map.class, id,containSelf);
			return map.get(0);
		}
	}
	
}
