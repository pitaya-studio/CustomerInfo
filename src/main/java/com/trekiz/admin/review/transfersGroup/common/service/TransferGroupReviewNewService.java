package com.trekiz.admin.review.transfersGroup.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.StatisticAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.OrderProgressTrackingService;
import com.trekiz.admin.modules.order.service.OrderServiceForSaveAndModify;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.order.service.OrderStockService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.airticketChange.service.INewAirticketChangeService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.rebates.singleGroup.repository.RebatesNewDao;

/**
 * 转团审批service
 * @author yakun.bai
 * @Date 2015-12-7
 */
@Service
@Transactional(readOnly = true)
public class TransferGroupReviewNewService extends BaseService {
	
	@Autowired
	private RebatesNewDao rebatesNewDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
    private TravelerDao travelerDao;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
    private OfficeService officeService;
	@Autowired
    SysIncreaseService sysIncreaseService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
    private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
	@Autowired
	private OrderStockService orderStockService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private INewAirticketChangeService iNewAirticketChangeService;
	@Autowired
	private SystemService systemService;
	@Autowired
    private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private GroupControlBoardService groupControlBoardService;
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	@Autowired
    private OrderProgressTrackingService progressService;
	
	/**
	 * @Description 转团审核记录查询
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	public Page<Map<String, Object>> getTransferGroupReviewList(Page<Map<String, Object>> pagePara, Map<String, String> params) {
		
		//获取转团审批查询sql
		String sql = getReviewSql();
		
		//获取转团审批查询条件sql
		String whereSql = getReviewWhereSql(params);
		
		//排序
		String orderBy = params.get("orderBy");
		if (StringUtils.isNotBlank(orderBy)) {
			pagePara.setOrderBy(orderBy);
		} else {
			pagePara.setOrderBy("r.create_date DESC");
		}
		
		//执行SQL查询出列表数据
		Page<Map<String, Object>> page = rebatesNewDao.findBySql(pagePara, sql + whereSql, Map.class);
		
		//为列表数据组装审核变量
		List<Map<String, Object>> list = page.getList();
		Object reviewId = null;
		Object status = null;
		for(Map<String, Object> map : list){
			reviewId = map.get("reviewId");
			if(reviewId == null || "".equals(reviewId.toString())){
				continue;
			}
			
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewId.toString());
			handleMap4ManyTraveler(reviewMap);
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewId.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if(status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isNumber(status.toString())){
				map.put("statusdesc" ,"无审核状态");
				continue;
			}
			if(ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())){
				Object cReviewer = map.get("currentReviewer");
				String person = "未分配审核人";
				if(cReviewer != null && !StringUtil.isBlank(cReviewer.toString())){
					person = getReviewerDesc(cReviewer);
					map.put("statusdesc" ,"待" + person + "审批");
				} else {
					map.put("statusdesc" ,person);
				}
			} else {
				map.put("statusdesc" ,getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			reviewId = null;
			status = null;
			
			// 
			Integer productType = Integer.parseInt(map.get("productType").toString());
			if (productType == 7) {
				Long orderId = Long.parseLong(map.get("orderId").toString());
				AirticketOrder order = airticketOrderDao.getAirticketOrderById(orderId);
				map.put("productId", order.getAirticketId());
			}
		}
		page.setList(list);
		return page;
	}
	
	
	/**
	 * @Description 获取转团审批查询sql
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	private String getReviewSql() {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(
				userId, companyUuid, Context.REBATES_FLOW_TYPE);
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = "";
		if (deptIds != null && deptIds.size() > 0) {
			int n = 0;
			for (String str : deptIds) {
				if (n == 0) {
					deptIdStr += str;
					n++;
				} else {
					deptIdStr += "," + str;
				}
			}
		}
		if ("".equals(deptIdStr)) {//给默认值
			deptIdStr = "-1,-2";
		}
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = "";
		if (prds != null && prds.size() > 0) {
			int n = 0;
			for (String str : prds) {
				if (n == 0) {
					prdStr += str;
					n++;
				} else {
					prdStr += "," + str;
				}
			}
		}
		if ("".equals(prdStr)) {//给默认值
			prdStr = "-1,-2";
		}
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT r.order_no orderNum, r.id reviewId, r.order_id orderId, r.group_code groupCode, r.group_id groupId, ").
				append("r.product_name productName, r.product_id productId, r.product_type productType, r.create_date createDate, ").
				append("r.create_by createBy, r.agent agent, r.operator operator, r.traveller_id travellerId, r.last_reviewer lastReviewer, ").
				append("r.pay_status paystatus, r.status status ").
			append("FROM review_new r ").
			append("WHERE (r.process_type = '" + Context.REVIEW_FLOWTYPE_TRANSFER_GROUP + "' " +
						"OR r.process_type = '" + Context.REVIEW_FLOWTYPE_VISA_CHANGE + "') AND need_no_review_flag = 0 ").
			append("AND ((r.product_type IN (" + prdStr + ") AND r.dept_id IN (" + deptIdStr + ")) OR FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		return sql.toString();
	}
	
	/**
	 * @Description 获取转团审批查询条件sql
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	private String getReviewWhereSql(Map<String, String> params) {
		
		StringBuffer whereSql = new StringBuffer("");
		
		//产品编号或团期编号或订单编号
		String groupCode = params.get("groupCode");
		if (StringUtils.isNotBlank(groupCode)) {
			whereSql.append(" and (r.group_code like '%" + groupCode + "%' or r.product_name like '%" + groupCode +
					"%' or r.order_no like '%" + groupCode + "%') ");
		}
		
		//产品类型
		String productType = params.get("productType");
		if (StringUtils.isNotBlank(productType) && !"0".equals(productType)) {
			whereSql.append(" and r.product_type = " + productType + " ");
		}
		
		//渠道
		String agentId = params.get("agentId");
		if (StringUtils.isNotBlank(agentId)) {
			whereSql.append(" and r.agent = " + agentId + " ");
		}
		
		//申请开始时间
		String applyDateFrom = params.get("applyDateFrom");
		if (StringUtils.isNotBlank(applyDateFrom)) {
			whereSql.append(" and r.create_date >= '" + applyDateFrom + " 00:00:00' ");
		}
		
		//申请结束时间
		String applyDateTo = params.get("applyDateTo");
		if (StringUtils.isNotBlank(applyDateTo)) {
			whereSql.append(" and r.create_date <= '" + applyDateTo + " 23:59:59' ");
		}
		
		//申请人
		String applyPerson = params.get("applyPerson");
		if (StringUtils.isNotBlank(applyPerson)) {
			whereSql.append(" and r.create_by = " + applyPerson + " ");
		}
		
		//计调
		String operator = params.get("operator");
		if (StringUtils.isNotBlank(operator)) {
			whereSql.append(" and r.operator = " + operator + " ");
		}
		
		//审批状态  空 为全部 1 审批中 2 已通过 0 未通过
		String reviewStatus = params.get("reviewStatus");
		if (StringUtils.isNotBlank(reviewStatus) && NumberUtils.isNumber(reviewStatus)) {
			whereSql.append(" and r.status = " + Integer.parseInt(reviewStatus) + " ");
		}
		
		//游客
		String travelerName = params.get("travelerName");
		if (StringUtils.isNotBlank(travelerName)) {
			whereSql.append(" and r.traveller_name like '%" + travelerName + "%' ");
		}
		
		//状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批
		Long userId = UserUtils.getUser().getId();
		String tabStatus = params.get("tabStatus");
		if (StringUtils.isBlank(tabStatus)) {
			tabStatus = "1";
			params.put("tabStatus", tabStatus);
		}
		if (StringUtils.isNotBlank(tabStatus) && NumberUtils.isNumber(tabStatus) && !Context.REVIEW_TAB_ALL.equals(tabStatus)) {
			int tabStatusInt = Integer.parseInt(tabStatus);
			if (Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt) {
				whereSql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt) {
				whereSql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt) {
				whereSql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		//游客
		String paymentType = params.get("paymentType");
		if (StringUtils.isNotBlank(paymentType)) {
			whereSql.append(" and r.agent in (select id from agentinfo WHERE paymentType = "+paymentType+")");
		}
		return whereSql.toString();
	}
	
	/**
	 * 获取当前审核人描述 由id转化为name
	 * @param cReviewer
	 * @return
	 */
	private String getReviewerDesc(Object cReviewer) {
		String reviewers = cReviewer.toString();
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for(String temp : reviewArr){
			if(StringUtils.isBlank(temp)){
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if(n == 0){
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		return result;
	}
	
	/**
	 * 获取审核状态描述
	 * @param status
	 * @return
	 */
	private String getReviewStatus(Integer status) {
		if(ReviewConstant.REVIEW_STATUS_CANCELED == status){
			return ReviewConstant.REVIEW_STATUS_CANCELED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_PASSED == status){
			return ReviewConstant.REVIEW_STATUS_PASSED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_REJECTED == status){
			return ReviewConstant.REVIEW_STATUS_REJECTED_DES;
		} 
		return "无";
	}
	
	/**
	 * @Description 转团审核
	 * @param reviewId 审核id
	 * @param strResult 通过或驳回的标识
	 * @param denyReason 驳回原因
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-12-7
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> transferGroupReview(String reviewId, String strResult, String denyReason, HttpServletRequest request) throws Exception {
		
		// 返回值
		Map<String, String> result = Maps.newHashMap();
		result.put("result", "success");
		
		// 获取用户ID和公司ID
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		
		// 审批返回结果对象
		ReviewResult reviewResult;
		if (strResult.equals(ReviewConstant.REVIEW_OPERATION_PASS.toString())) {
			// 审批通过
			reviewResult = reviewService.approve(userId, companyId, null, permissionChecker, reviewId, denyReason, null);
		} else {
			// 审批驳回
			reviewResult = reviewService.reject(userId, companyId, null, reviewId, denyReason, null);
			// 查询转团申请信息
			Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
			// 修改游客状态
			String travelerId = reviewDetailMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();
			if (StringUtils.isNotBlank(travelerId)) {				
				String[] travelerIdArray = travelerId.split(",");
				if (travelerIdArray != null && travelerIdArray.length > 0) {
					for (int i = 0; i < travelerIdArray.length; i++) {
						Traveler traveler = travelerDao.findById(Long.parseLong(travelerIdArray[i]));
						traveler.setDelFlag(Integer.parseInt(Context.DEL_FLAG_NORMAL));
						travelerDao.save(traveler);
					}
				}
			}
		}
		if (!reviewResult.getSuccess()) {
			result.put("result", "fail");
			result.put("msg", reviewResult.getMessage());
		} else {
			result.put("result", "success");
		}
		// 如果审核通过并且当前层级为最高层级 则更改对应业务数据
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
			//查询转团申请信息
			Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
			// 转团审核成功后调用
			changeGroupSuccess(reviewDetailMap, request);
		}
		
		return result;
	}
	
	/**
	 * 转团审核成功后调用接口
	 * @param oldOrderId  原订单ID
	 * @param travelerId  转团游客ID
	 * @param newGroupId  新团期ID
	 * @param rid  审核记录ID
	 * @param request
	 * @return
	 * 返回 Map<String,Object>, 键值对：res：0；message：“”
	 * res：0 操作转团成功，1余位不足，2生成新订单失败, 3 数据输入有误
	 * message：失败原因。
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String>  changeGroupSuccess(Map<String, Object> reviewMap, HttpServletRequest request) throws Exception {
		// 返回数据
		Map<String, String> resultMap = Maps.newHashMap();
		// 转出团
		Long orderId = Long.parseLong(reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID).toString());
		ProductOrderCommon order = validateOrder(orderId, resultMap);  // 校验订单是否为空
		ActivityGroup oldGroup = activityGroupService.findById(order.getProductGroupId());
		// 转入团
		Long newGroupId = Long.parseLong(reviewMap.get(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_ID).toString());
		ActivityGroup newGroup = validateGroup(newGroupId, resultMap);  // 校验新团期是否为空
		// 转出订单参与本条审批的转团游客
		List<Traveler> travelers = new ArrayList<>();
		// 模式选择（一对一转团：无flag，或flag == false；多对一转团：有flag，且flag == true）
		Object transGroupMany2One = reviewMap.get(Context.REVIEW_VARIABLE_TRANSGROUP_MANY2ONE_FLAG);
		if (transGroupMany2One != null && (boolean)transGroupMany2One) {
			/** 多对一（多位游客在同一审批中，转入同一订单） */
			// 游客（旧）
			String travelerIDString = reviewMap.get(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS).toString();
			if (StringUtils.isBlank(travelerIDString) || travelerIDString.split(",").length == 0) {
				throw new IllegalArgumentException("游客ID获取失败！");
			}
			String[] travelerIDArray = travelerIDString.split(",");  // 诸位游客 IDs
			List<Map<String, Object>> travelerMapList = new ArrayList<>();  // 诸位游客信息 map
			// 组织转团游客信息
			for (int i = 0; i < travelerIDArray.length; i++) {
				Map<String, Object> travelerInfoMap = new HashMap<>();
				Long travelerId = null;
				try {
					travelerId = Long.parseLong(travelerIDArray[i]);
				} catch (Exception e) {
					throw new IllegalArgumentException("游客ID解析失败！");
				}
				// 游客id
				travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId.toString());
				// 游客 name
				travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerId.toString()));
				// 游客 结算价
				travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, reviewMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE + "_" + travelerId.toString()));
				// 游客 扣减金额
				travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE, reviewMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE + "_" + travelerId.toString()));
				// 游客 备注
				travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK + "_" + travelerId.toString()));
				// 游客实体
				Traveler traveler = validateTraveler(travelerId, resultMap);  // 校验游客是否为空
				travelerInfoMap.put("traveler", traveler);
				travelers.add(traveler);
				travelerMapList.add(travelerInfoMap);
			}
		} else {
			/** 一对一（一游客一审批一订单） */
			// 游客ID
			Long travelerId = Long.parseLong(reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString());
			// 校验游客是否为空
			Traveler traveler = validateTraveler(travelerId, resultMap);
			travelers.add(traveler);
		}
		
		// 生成新订单
		ProductOrderCommon newOrder = createNewOrder(reviewMap, order, newGroup, travelers);
		// 获取原订单联系人
		List<OrderContacts> list =  orderContactsService.findOrderContactsByOrderIdAndOrderType(order.getId(), order.getOrderStatus());
		// 保存新订单及联系人并判断团期是否有余位
		newOrder = saveOrder(newOrder, list, request);
		// 保存新订单游客
		List<Traveler> newTravelers = new ArrayList<>();
		for (Traveler temptraveler : travelers) {
			newTravelers.add(getNewTraveler(newGroup, temptraveler, newOrder));
			// 扣减原订单人数及金额 、原游客状态改为已转团
			handleOrderAndTravelerInfo(order, temptraveler);
			temptraveler.setDelFlag(Context.TRAVELER_DELFLAG_TURNROUNDED);
			travelerDao.save(temptraveler);
		}
		// 处理老订单：归还余位   转团的人数取转团游客集合大小 forbug15605
		updateOldOrder(order, request, travelers.size());
		// 为保证转团成功后必须占位，在转团生成订单后根据新订单状态，若为未支付定金或未支付全款状态，则模拟一次支付，确保新订单能占位
		orderPay(newOrder, request);
		// 0524需求 团期余位变化,记录在团控板中
		long createBy = Long.parseLong(reviewMap.get("createBy").toString());
		// 转入团
		groupControlBoardService.insertGroupControlBoard(7, travelers.size(), "系统转团操作,从"+oldGroup.getGroupCode()+"团转入" + travelers.size() + "人", newGroupId, createBy);
		// 转出团
		groupControlBoardService.insertGroupControlBoard(6, travelers.size(), "订单"+order.getOrderNum()+"从"+oldGroup.getGroupCode() + "团转入"+newGroup.getGroupCode()+"团,转团" + travelers.size() + "人", oldGroup.getId(), createBy);
		// 0524需求 团期余位变化,记录在团控板中
		// 把新订单ID放入审核扩展字段
		if (null != newOrder && 0 < newOrder.getId()) {
			reviewMap.get("id");
			ReviewNew reviewNew = reviewService.getReview(reviewMap.get("id").toString());
			if (reviewNew.getIdLong() == null) {
				String sql = "select MIN(id_long) from review_new";
				List<Integer> minVal = travelerDao.findBySql(sql.toString());
				Long minIdVal = null;
				if (CollectionUtils.isNotEmpty(minVal)) {
					minIdVal = minVal.get(0).longValue() - 1;
				} else {
					minIdVal = 0L;
				}
				reviewNew.setIdLong(minIdVal);
			}
			reviewNew.setExtend1(newOrder.getId().toString());
			String newTravelerIdString = "";
			for (Traveler newtraveler : newTravelers) {
				if (StringUtils.isNotBlank(newTravelerIdString)) {
					newTravelerIdString += ",";
				}
				newTravelerIdString += newtraveler.getId().toString();
			}
			reviewNew.setExtend2(newTravelerIdString);
			reviewNew.setExtend3(newGroupId.toString());
		}

//		// 更新预统计表中的对应记录 yudong.xu 2017.1.18
//		statisticAnalysisService.updateStatisticRecord(order.getId(),order.getOrderStatus()); // 更新原有订单数据
//		statisticAnalysisService.updateStatisticRecord(newOrder.getId(),newOrder.getOrderStatus()); // 更新新订单数据
		
		orderDateSaveOrUpdateService.updatePeopleAndMoneyPro(order.getId(),order.getOrderStatus());// 更新原有订单数据
		if (orderDateSaveOrUpdateService.whetherAddOrUpdate(newOrder)) {
			orderDateSaveOrUpdateService.addOrderDataStatistics(newOrder);	//  添加或更新新订单数据
		}

		// 添加订单跟踪记录
		progressService.addOrderProgrssTracking(newOrder, false);
		
        return resultMap;
	}   
	
	/**
	 * @Description 校验订单是否为空
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-12-8
	 */
	private ProductOrderCommon validateOrder(Long orderId, Map<String, String> resultMap) throws Exception {
		ProductOrderCommon order = null;
		if (orderId != null) {
			order = productorderDao.findOne(orderId);
			if (order == null) {
				resultMap.put("result", "error");
				resultMap.put("message", "根据订单ID:" + orderId + ", 找不到原订单");
				throw new IllegalArgumentException("查询不到订单");
			}
		} else {
			resultMap.put("result", "error");
			resultMap.put("message", "订单ID为空");
			throw new IllegalArgumentException("订单ID为空");
		}
		return order;
	}
	
	/**
	 * @Description 校验游客是否为空
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private Traveler validateTraveler(Long travelerId, Map<String, String> resultMap) throws Exception {
		Traveler traveler = null;
		if (travelerId != null) {
			traveler = travelerDao.findById(travelerId);
			if (traveler == null) {
				resultMap.put("result", "error");
				resultMap.put("message", "根据游客ID:" + travelerId + ", 找不到游客");
				throw new IllegalArgumentException("查询不到游客");
			}
			if (traveler.getDelFlag() == Context.TRAVELER_DELFLAG_EXITED) {
				resultMap.put("result", "error");
				resultMap.put("message", "游客已退团，不能再转团");
				throw new IllegalArgumentException("已退团游客不能再转团");
			}
		} else {
			resultMap.put("result", "error");
			resultMap.put("message", "游客ID为空");
			throw new IllegalArgumentException("游客ID为空");
		}
		return traveler;
	}
	
	/**
	 * @Description 校验新团期是否为空
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private ActivityGroup validateGroup(Long groupId, Map<String, String> resultMap) throws Exception {
		ActivityGroup group = null;
		if (groupId != null) {
			group = activityGroupService.findById(groupId);
			if (group == null) {
				resultMap.put("result", "error");
				resultMap.put("message", "根据团期编号:" + group + ", 找不到团期");
				throw new IllegalArgumentException("查询不到团期");
			}
		} else {
			resultMap.put("result", "error");
			resultMap.put("message", "游客ID为空");
			throw new IllegalArgumentException("团期ID为空");
		}
		return group;
	}
	

	/**
	 * @Description 创建新订单
	 * @param reviewMap 申请map
	 * @param oldOrder 老订单
	 * @param newGroup 新团期
	 * @param travelers 旧订单游客（本审批）
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private ProductOrderCommon createNewOrder(Map<String, Object> reviewMap, ProductOrderCommon oldOrder, ActivityGroup newGroup, List<Traveler> travelers) {
		
		ProductOrderCommon newOrder = new ProductOrderCommon();
		// 获取新团期产品
		TravelActivity travelActivity = travelActivityService.findById(Long.valueOf(newGroup.getSrcActivityId()));
		
		/** 订单ID */
		newOrder.setId(null);
		/** 产品ID */
		newOrder.setProductId(Long.valueOf(newGroup.getSrcActivityId()));
		/** 团期ID */
		newOrder.setProductGroupId(newGroup.getId());
		/** 价格类型 */
		newOrder.setPriceType(oldOrder.getPriceType());
		/** 价格类型 */
		newOrder.setAgentType(oldOrder.getAgentType());
		/** 渠道ID（同原订单） */
		newOrder.setOrderCompany(oldOrder.getOrderCompany());
		/** 渠道名称（同原订单） */
		newOrder.setOrderCompanyName(oldOrder.getOrderCompanyName());
		if (newOrder.getAgentType() != -1) {
    		Rate rate = RateUtils.getRate(newGroup.getId(), 2, newOrder.getOrderCompany());
    		// 订单保存费率（包含渠道费率）
    		if (rate != null) {
    			newOrder.setQuauqProductChargeType(rate.getQuauqRateType());
    			newOrder.setQuauqProductChargeRate(rate.getQuauqRate());
    			newOrder.setQuauqOtherChargeType(rate.getQuauqOtherRateType());
    			newOrder.setQuauqOtherChargeRate(rate.getQuauqOtherRate());
    			newOrder.setPartnerProductChargeType(rate.getAgentRateType());
    			newOrder.setPartnerProductChargeRate(rate.getAgentRate());
    			newOrder.setPartnerOtherChargeType(rate.getAgentOtherRateType());
    			newOrder.setPartnerOtherChargeRate(rate.getAgentOtherRate());
    			newOrder.setCutChargeType(rate.getChouchengRateType());
    			newOrder.setCutChargeRate(rate.getChouchengRate());
    		}
    	}
		/** 订单预定时间 */
		newOrder.setOrderTime(new Date());
		/** 预定人数 */
		newOrder.setOrderPersonNum(travelers.size());
//		Agentinfo agentinfo = agentinfoDao.findOne(oldOrder.getOrderCompany());
		/** 占位方式 1： 切位，0或空值： 占位 */
		newOrder.setPlaceHolderType(0);
//		User saler = oldOrder.getOrderSaler();
//		if (saler == null) {
//			List<User> salerUsers = UserUtils.getUserListByIds(agentinfo.getAgentSalerId());
//			saler = salerUsers.get(0);
//		}
//		newOrder.setOrderSaler(oldOrder.getOrderSaler());
		/** 销售 */
		newOrder.setSalerId(oldOrder.getSalerId());
		/** 销售姓名 */
		newOrder.setSalerName(oldOrder.getSalerName());
		/** 付款方式 */
		String payMode = reviewMap.get(Context.REVIEW_VARIABLE_KEY_PAY_TYPE).toString();
		newOrder.setPayMode(payMode);
		/** 占位保留天数 */
		Double remainDays = null;
		if (reviewMap.get(Context.REVIEW_VARIABLE_KEY_REMAIN_DAYS) != null) {
			remainDays = Double.parseDouble(reviewMap.get(Context.REVIEW_VARIABLE_KEY_REMAIN_DAYS).toString());
		}
		newOrder.setRemainDays(remainDays);
		/** 创建者：审批申请创建人 */
		newOrder.setCreateBy(UserUtils.getUser(Long.parseLong(reviewMap.get("createBy").toString())));
		/** 订单类型 */
		newOrder.setOrderStatus(travelActivity.getActivityKind());  // 
		/** 默认为补位订单：防止被取消 */
		newOrder.setIsAfterSupplement(1);
		
		// add by lt
		newOrder.setSeenFlag(0);
		
		// 支付状态：1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消
		// 支付方式：1定金占位，2预占位，3全款支付，4资料占位，5担保占位，6确认单占位
		if ("1".equals(payMode)) {
			newOrder.setPayStatus(2);
		} else if ("2".equals(payMode)) {
			newOrder.setPayStatus(3);
		} else if ("3".equals(payMode)) {
			newOrder.setPayStatus(1);
		} else if ("4".equals(payMode)) {
			newOrder.setPayStatus(3);
		} else if ("5".equals(payMode)) {
			newOrder.setPayStatus(3);
		} else if ("6".equals(payMode)) {
			newOrder.setPayStatus(3);
		} else if ("7".equals(payMode)) {
			newOrder.setPayStatus(3);
		} else if ("8".equals(payMode)) {
			newOrder.setPayStatus(8);
		} else {
			newOrder.setPayStatus(99);
		}
		
		// 订单编号
		String companyName = officeService.get(travelActivity.getProCompany()).getName();
		String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, travelActivity.getProCompany(),null, Context.ORDER_NUM_TYPE);
		newOrder.setOrderNum(orderNum);

		// 计算不同游客类型的人数 
		Integer adultNum = 0;
		Integer childNum = 0;
		Integer specialNum = 0;
		for (Traveler traveler : travelers) {
			// 1成人、2儿童、3特殊人群
			if (traveler.getPersonType().intValue() == Context.PERSON_TYPE_ADULT) {
				adultNum += 1;
			} else if (traveler.getPersonType().intValue() == Context.PERSON_TYPE_CHILD) {
				childNum += 1;
			} else if (traveler.getPersonType().intValue() == Context.PERSON_TYPE_SPECIAL) {
				specialNum += 1;
			} else {
				// 暂无其他类型
			}
		}
		newOrder.setOrderPersonNumAdult(adultNum);
		newOrder.setOrderPersonNumChild(childNum);
		newOrder.setOrderPersonNumSpecial(specialNum);
		
		return newOrder;
	}
	
	/**
	 * @Description 保存订单并校验余位
	 * @author yakun.bai
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 * @Date 2015-12-8
	 */
	private ProductOrderCommon saveOrder(ProductOrderCommon newOrder, List<OrderContacts> list, HttpServletRequest request) throws Exception {
		ProductOrderCommon order = null;
		Map<String,Object> backOrderMap = new HashMap<String,Object>();
		// 校验余位并保存订单
		backOrderMap = orderServiceForSaveAndModify.saveOrder(newOrder, list, request);
		if (null != backOrderMap.get("errorMsg")) {
			// 余位不足
			throw new Exception(backOrderMap.get("errorMsg").toString());
		}
		order = (ProductOrderCommon)backOrderMap.get("productOrder");
		orderStockService.changeGroupFreeNum(newOrder, null, Context.StockOpType.TRANSFER_GROUP);
		if (Context.PRICE_TYPE_QUJ == order.getPriceType()) {
			orderServiceForSaveAndModify.setOrderChargePrice(order, true);
		}
		return order;
	}
	
	/**
	 * @Description 创建订单新游客
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private Traveler getNewTraveler(ActivityGroup newGroup,Traveler oldTraveler, ProductOrderCommon newOrder) {
		
		// 创建新游客
		Traveler newTraveler = new Traveler();
		// 复制转团游客属性
		BeanUtils.copyProperties(oldTraveler, newTraveler);
		//赋值新游客特有属性
		newTraveler.setId(null);
		newTraveler.setVisa(null);
		newTraveler.setDelFlag(0);
		newTraveler.setOrderId(newOrder.getId());
		newTraveler.setOrderType(newOrder.getOrderStatus());
		newTraveler.setPayPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setIsAirticketFlag("0");
		newTraveler.setOriginalPayPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setCostPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setSubtractMoneySerialNum(null);
		newTraveler.setSingleDiff(newGroup.getSingleDiff());
		// 保存游客
		newTraveler = travelerDao.save(newTraveler);
		// 计算游客原始应收价，根据游客类型
		BigDecimal amount = new BigDecimal(0);
		String currency = "1";
		// 使用订单记录的 报名时价格信息（TODO 是否保险有待考证）
//		List<MoneyAmount> adultMoneyAmounts = moneyAmountService.findBySerialNum(newOrder.getSettlementAdultPrice());
//		List<MoneyAmount> childMoneyAmounts = moneyAmountService.findBySerialNum(newOrder.getSettlementcChildPrice());
//		List<MoneyAmount> specialMoneyAmounts = moneyAmountService.findBySerialNum(newOrder.getSettlementSpecialPrice());
		// 团期中价格：区分游客类型、价格类型
		BigDecimal adultPrice = BigDecimal.ZERO;
		BigDecimal childPrice = BigDecimal.ZERO;
		BigDecimal specialPrice = BigDecimal.ZERO;
		if (Context.PRICE_TYPE_ZKJ == newOrder.getPriceType()) {  // 直客价格体系
			adultPrice = newGroup.getSuggestAdultPrice();
        	childPrice = newGroup.getSuggestChildPrice();
        	specialPrice = newGroup.getSuggestSpecialPrice();
		} else if (Context.PRICE_TYPE_QUJ == newOrder.getPriceType()) {  // quauq价格体系
			currency = newGroup.getCurrencyType();
			String payDepositCurrencyId = null;
			String adultCurrencyId = null;
			String childCurrencyId = null;
			String specialCurrencyId = null;
			String singleDiffCurrencyId = null;
			String[] currencyArr = {"1","1","1","1","1","1","1","1","1","1","1"};
		    if (currency != null) {
		    	currencyArr = currency.split(",");
		    }
			// 为避免旧的不合法的数据，把币种取成同行价币种
			if (currencyArr.length == 14) {
				adultCurrencyId = currencyArr[9];
				childCurrencyId = currencyArr[10];
				specialCurrencyId = currencyArr[11];
			} else if (currencyArr.length == 11) {
				adultCurrencyId = currencyArr[6];
				childCurrencyId = currencyArr[7];
				specialCurrencyId = currencyArr[8];
			} else {					
				adultCurrencyId = currencyArr[0];
				childCurrencyId = currencyArr[1];
				specialCurrencyId = currencyArr[2];
			}
        	Rate rate = RateUtils.getRate(newGroup.getId(), 2, newOrder.getOrderCompany());
    		// 获取推广价（包含渠道费率）
        	adultPrice = OrderCommonUtil.getRetailPrice(newGroup.getSettlementAdultPrice(), newGroup.getQuauqAdultPrice(), rate, Long.parseLong(adultCurrencyId));
        	childPrice = OrderCommonUtil.getRetailPrice(newGroup.getSettlementcChildPrice(), newGroup.getQuauqChildPrice(), rate, Long.parseLong(childCurrencyId));
	   		specialPrice = OrderCommonUtil.getRetailPrice(newGroup.getSettlementSpecialPrice(), newGroup.getQuauqSpecialPrice(), rate, Long.parseLong(specialCurrencyId));
        	
		} else {  // 同行价格体系
			adultPrice = newGroup.getSettlementAdultPrice();
        	childPrice = newGroup.getSettlementcChildPrice();
        	specialPrice = newGroup.getSettlementSpecialPrice();
		}
		// 此出获取对应币种（游客类型、价格类型）（由于团期币种array较为繁琐，且已知的4中价格类型的币种分布是一致的，故而使用同行价币种代替）
		if (Context.PERSON_TYPE_ADULT == newTraveler.getPersonType()) {
			amount = adultPrice;
			currency = newGroup.getCurrencyType().split(",")[0];
		} else if (Context.PERSON_TYPE_CHILD == newTraveler.getPersonType()) {
			amount = childPrice;
			currency = newGroup.getCurrencyType().split(",")[1];
		} else {
			amount = specialPrice;
			currency = newGroup.getCurrencyType().split(",")[2];
		}
		// 游客单价
		newTraveler.setSrcPrice(amount);
		// 游客单价币种
		String oldPayPriceString = "￥0.00";
		if (StringUtils.isNotBlank(currency)) {
			Currency srcCurrency = currencyService.findCurrency(Long.parseLong(currency));
			oldPayPriceString = srcCurrency.getCurrencyMark() + "0.00";
			newTraveler.setSrcPriceCurrency(srcCurrency);
		}
		// 添加游客原始应收价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getOriginalPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		// 添加游客结算价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		// 添加游客成本价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getCostPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		// 单房差金额添加到结算价
		if (newTraveler.getHotelDemand() != null && newTraveler.getSingleDiff() != null 
				&& newTraveler.getSingleDiffCurrency() != null && newTraveler.getSingleDiffNight() != null) {
			if (newTraveler.getHotelDemand() == 1) {
				List<MoneyAmount> moneyAmountList = Lists.newArrayList();
				String payPriceSerialNum = newTraveler.getPayPriceSerialNum();
				// 单房差
				Integer SinglecurrencyId = newTraveler.getSingleDiffCurrency().getId().intValue();
				if (SinglecurrencyId == Integer.parseInt(currency)) {
					Integer singleDiffNight = newTraveler.getSingleDiffNight();
					BigDecimal singleDiffNightAmount = newTraveler.getSingleDiff().multiply(new BigDecimal(singleDiffNight));
					// 游客结算价
					amount = amount.add(singleDiffNightAmount);
					MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, Integer.parseInt(currency), amount, newTraveler.getId(),
							Context.MONEY_TYPE_JSJ, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
					moneyAmountList.add(moneyAmount);
					moneyAmountService.saveOrUpdateMoneyAmounts(newTraveler.getPayPriceSerialNum(), moneyAmountList);
					
					// 订单结算价
					Long currencyId_RMB = currencyService.findRMBCurrency(UserUtils.getUser().getCompany().getId()).getId();
					String totalMoney = newOrder.getTotalMoney();
					MoneyAmount addMoneyAmount = new MoneyAmount();
					if (Context.PRICE_TYPE_QUJ == newOrder.getPriceType()) {  // quauq订单还要保存服务费，总额添加服务费
						List<MoneyAmount> tobeAddAmounts = new ArrayList<>();
						//  单房差moneyAmount
						addMoneyAmount.setAmount(singleDiffNightAmount);
						addMoneyAmount.setCurrencyId(SinglecurrencyId);
						tobeAddAmounts.add(addMoneyAmount);
						MoneyAmount rmbAmount = new MoneyAmount();
						//  单房差moneyAmount 对应的RMB服务费
						BigDecimal singleDiffNight_RMB = newTraveler.getSingleDiffCurrency().getConvertLowest().multiply(singleDiffNightAmount);
//						BigDecimal chargeRate = newOrder.getOrderChargeRate();
//						if (chargeRate == null) {
//							chargeRate = UserUtils.getUser().getCompany().getChargeRate();
//						}
//						rmbAmount.setAmount(singleDiffNight_RMB.multiply(chargeRate));
						rmbAmount.setCurrencyId(Integer.parseInt(currencyId_RMB.toString()));
						List<MoneyAmount> tobeAddAmounts2 = new ArrayList<>();
						tobeAddAmounts2.add(rmbAmount);
						tobeAddAmounts2 = moneyAmountService.calculation4MoneyAmountList(Context.ADD, tobeAddAmounts, tobeAddAmounts2);
						//
						List<MoneyAmount> totalmAmounts = moneyAmountService.findAmountBySerialNum(totalMoney);
						// 订单总额
						List<MoneyAmount> tempAmountsSave = new ArrayList<>();
						tempAmountsSave.addAll(moneyAmountService.calculation4MoneyAmountList(Context.ADD, totalmAmounts, tobeAddAmounts2));
						for (MoneyAmount moneyAmount2 : tempAmountsSave) {
							moneyAmount2.setSerialNum(totalMoney);
							moneyAmount2.setUid(newOrder.getId());
							moneyAmount2.setOrderType(newOrder.getOrderStatus());
							moneyAmount2.setBusindessType(1);
							moneyAmount2.setMoneyType(Context.MONEY_TYPE_YSH);
						}
						moneyAmountService.saveMoneyAmounts(tempAmountsSave);
						// 服务费
//						List<MoneyAmount> chargeAmounts = moneyAmountService.findAmountBySerialNum(newOrder.getQuauqServiceCharge());
//						List<MoneyAmount> diffChargeAmounts = new ArrayList<>();
//						diffChargeAmounts.add(rmbAmount);
//						chargeAmounts = moneyAmountService.calculation4MoneyAmountList(Context.ADD, chargeAmounts, diffChargeAmounts);
//						chargeAmounts.get(0).setSerialNum(newOrder.getQuauqServiceCharge());
//						chargeAmounts.get(0).setUid(newOrder.getId());
//						chargeAmounts.get(0).setOrderType(newOrder.getOrderStatus());
//						chargeAmounts.get(0).setBusindessType(1);
//						chargeAmounts.get(0).setMoneyType(Context.MONEY_TYPE_CHARGE);
//						moneyAmountService.saveMoneyAmounts(chargeAmounts);
					} else {
						addMoneyAmount.setAmount(singleDiffNightAmount);
						moneyAmountService.saveOrUpdateMoneyAmount(addMoneyAmount, totalMoney, "add", null);
					}
				} else {
					MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, Integer.parseInt(currency), amount, newTraveler.getId(),
							Context.MONEY_TYPE_JSJ, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
					MoneyAmount moneyAmountSingle = new MoneyAmount(payPriceSerialNum, SinglecurrencyId, newTraveler.getSingleDiff(), newTraveler.getId(),
							Context.MONEY_TYPE_JSJ, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
					moneyAmountList.add(moneyAmount);
					moneyAmountList.add(moneyAmountSingle);
					moneyAmountService.saveOrUpdateMoneyAmounts(newTraveler.getPayPriceSerialNum(), moneyAmountList);
				}
			}
		}
		// 记录结算价变动 for quauq
		String newPayPriceString = moneyAmountService.getMoneyStr(newTraveler.getPayPriceSerialNum());
		if (Context.PRICE_TYPE_QUJ == newOrder.getPriceType()) {
			// 记录结算价变动日志
			StringBuffer logContent = new StringBuffer();
			logContent.append("QUAUQ订单");
			logContent.append("###");
			logContent.append("转团新订单");
			logContent.append("###");
			logContent.append(newTraveler.getName() + "游客结算价从" + oldPayPriceString + "修改为" + newPayPriceString);
			this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, logContent.toString(), Context.log_state_add, newTraveler.getOrderType(), newTraveler.getOrderId());
		}
		// 如果是优加或奢华之旅，则需要添加团控列表
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_SHZL.equals(companyUuid) || Context.SUPPLIER_UUID_YJ.equals(companyUuid)) {
			
		}
		
		return newTraveler;
	}
	
	/**
	 * 生成流水表金额记录
	 * @param newGroup 新团期实体,
	 * @param serialNum UUID
	 * @param amount 金额币种id
	 * @param amount 金额
	 * @param uid 订单ID或游客ID
	 * @param moneyType 款项类型
	 * @param orderType 产品类型
	 * @param busindessType 业务类型
	 * @param createdBy 创建者
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private boolean MoneyAmountList(ActivityGroup newGroup, String serialNum, String currency, BigDecimal amount,
			Long uid, Integer moneyType, Integer orderType, Integer busindessType, Long createdBy) {
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		if(null == newGroup || StringUtils.isBlank(newGroup.getCurrencyType())){
			return false;
		}
		MoneyAmount moneyAmount = null;
		moneyAmount = new MoneyAmount(serialNum, Integer.parseInt(currency), 
				amount, uid, moneyType, orderType, busindessType, UserUtils.getUser().getId());
		moneyAmountList.add(moneyAmount);
		return moneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}
	
	/**
	 * 订单金额与人数、游客金额修改（退团和转团共用）
	 * @param productOrder
	 * @param traveler
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	private void handleOrderAndTravelerInfo(ProductOrderCommon productOrder, Traveler traveler) {
		 /** 扣减订单人数 */
        int personType = traveler.getPersonType();
        switch(personType){
        	case Context.PERSON_TYPE_ADULT:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
        	break;
        	case Context.PERSON_TYPE_CHILD:
        		productOrder.setOrderPersonNumChild(productOrder.getOrderPersonNumChild() - 1);
        	break;
        	case Context.PERSON_TYPE_SPECIAL:
        		productOrder.setOrderPersonNumSpecial(productOrder.getOrderPersonNumSpecial() - 1);
        	break;
        	default:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
            break;
        }
        productOrder.setOrderPersonNum(productOrder.getOrderPersonNum() - 1);
        productorderDao.save(productOrder);
        
        //第二步，更新游客状态，改为已退团，并扣减相应金额
        reducePrice(productOrder, traveler);
	}
	
	/**
	 * 修改游客成本、结算价；修改订单成本、结算价
	 * 订单成本价、结算价保留扣减金额；游客成本、结算价改成扣减金额
	 * @param productOrder
	 * @param traveler
	 * @author yunpeng.zhang
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void reducePrice(ProductOrderCommon productOrder, Traveler traveler) {
		// 获取游客扣减金额
		List<MoneyAmount> subtractMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getSubtractMoneySerialNum());
		// 获取游客的结算价
		List<MoneyAmount> payPriceList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
		// TODO 为了保持原游客结算价的纯洁性，可以使用clone计算再传值
//		MoneyAmount moneyAmount = new MoneyAmount();
//		BeanUtils.copyProperties(subtractMoneyAmount, moneyAmount);
		
		String oldPayPriceString = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum());
		
		//修改订单成本价、结算价：先减去游客结算价，如果有扣减金额，再加上扣减金额
		if (CollectionUtils.isNotEmpty(payPriceList)) {
			//如果游客有扣减金额，则需要在订单成本价、结算价加上此金额再减去游客成本、结算价
			if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
				MoneyAmount subtractMoneyAmount = subtractMoneyList.get(0);
				for (MoneyAmount moneyAmonut : payPriceList) {
					if (moneyAmonut.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
						moneyAmonut.setAmount(moneyAmonut.getAmount().subtract(subtractMoneyAmount.getAmount()));
						break;
					}
				}
				//订单应收价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getTotalMoney(), false, Context.MONEY_TYPE_YSH, productOrder.getOrderStatus());
				//订单成本价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getCostMoney(), false, Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus());
				// 恢复原游客结算价对象金额的值（加上扣减金额）（作用：本结算价对象只是逻辑删除，其serialNum依旧保存在审批数据中，在审批列表详情等地需要展示“转团前结算价”）
				for (MoneyAmount moneyAmonut : payPriceList) {
					if (moneyAmonut.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
						moneyAmonut.setAmount(moneyAmonut.getAmount().add(subtractMoneyAmount.getAmount()));
						break;
					}
				}
			} else {
				//订单应收价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getTotalMoney(), false, Context.MONEY_TYPE_YSH, productOrder.getOrderStatus());
				//订单成本价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getCostMoney(), false, Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus());
			}
		}
		
		//修改游客结算价、成本价：如果有游客扣减金额，则改为扣减金额，如果没有则都置为0
		if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
			//修改游客结算价
			moneyAmountService.delMoneyAmountBySerialNum2Delflag(traveler.getPayPriceSerialNum());
			String payMoneyserialNum = UuidUtils.generUuid();
			traveler.setPayPriceSerialNum(payMoneyserialNum);
			MoneyAmount payMoneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyList.get(0), payMoneyAmount);
			payMoneyAmount.setId(null);
			payMoneyAmount.setSerialNum(payMoneyserialNum);
			payMoneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
			moneyAmountService.saveOrUpdateMoneyAmount(payMoneyAmount);
			//修改游客成本价 (TODO 转团后游客成本价不再取同行价？单房差、其他费用怎么算？还是直接取结算价？位置差额怎么处理？)
			moneyAmountService.delMoneyAmountBySerialNum2Delflag(traveler.getCostPriceSerialNum());
			String costMoneyserialNum = UuidUtils.generUuid();
			traveler.setCostPriceSerialNum(costMoneyserialNum);
			MoneyAmount costMoneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyList.get(0), costMoneyAmount);
			costMoneyAmount.setId(null);
			costMoneyAmount.setSerialNum(costMoneyserialNum);
			costMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
			moneyAmountService.saveOrUpdateMoneyAmount(costMoneyAmount);
			
		}
		//保存游客
		traveler.setDelFlag(Context.TRAVELER_DELFLAG_EXITED);
		travelerDao.save(traveler);
		// 重新计算quauq订单的总额和服务费
		if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType()) {
			orderServiceForSaveAndModify.setOrderChargePrice(productOrder, false);
		}
		// 记录结算价变动 for quauq
		String newPayPriceString = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum());
		if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType()) {
			// 记录结算价变动日志
			StringBuffer logContent = new StringBuffer();
			logContent.append("QUAUQ订单");
			logContent.append("###");
			logContent.append("转团");
			logContent.append("###");
			logContent.append(traveler.getName() + "游客结算价从" + oldPayPriceString + "修改为" + newPayPriceString);
			this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, logContent.toString(), Context.log_state_add, traveler.getOrderType(), traveler.getOrderId());
		}
	}
	
	/**
	 * 金额相加或相减并保存
	 * @param priceList 要进行计算的金额List
	 * @param serialNum 进行相加减的金额序列号
	 * @param isAdd 是否进行相加
	 * @param moneyType 金额种类
	 * @param orderType 订单类型
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	private void handlePrice(List<MoneyAmount> priceList, String serialNum, boolean isAdd, Integer moneyType, Integer orderType) {
		for (MoneyAmount subtractMoneyAmount : priceList) {
			MoneyAmount moneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyAmount, moneyAmount);
			moneyAmount.setId(null);
			moneyAmount.setSerialNum(serialNum);
			moneyAmount.setMoneyType(moneyType);
			moneyAmount.setOrderType(orderType);
			moneyAmount.setBusindessType(1);
			if (!isAdd) {
				moneyAmount.setAmount(subtractMoneyAmount.getAmount().multiply(new BigDecimal(-1)));
			}
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", moneyType);
		}
	}
	
	/**
	 * @Description 原订单归还余位
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private void updateOldOrder(ProductOrderCommon order, HttpServletRequest request, int tranferPersonNum) throws Exception {
		
		// 判断是否需要归还余位
        Map<String,String> rMap = orderStatisticsService.getPlaceHolderInfo(order.getId(), order.getOrderStatus().toString());
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		// 占位人数
//        		Integer holdNum = order.getOrderStatus().intValue() == Context.ORDER_TYPE_CRUISE ? order.getRoomNumber() : order.getOrderPersonNum();
//        		if (holdNum == null) {
//        			holdNum = 0;
//				}
        		Map<String,String> pMap = orderStatisticsService.saveActivityGroupPlaceHolderChange(order.getId(),
        				order.getOrderStatus().toString(), tranferPersonNum, request, -1);  // 转团操作,0524团控板在增余位那边不做处理
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
        	throw new Exception("归还余位失败！");
        }
	}
	
	/**
	 * @Description 订单模拟支付（保证占位）
	 * @author yakun.bai
	 * @Date 2015-12-8
	 */
	private void orderPay(ProductOrderCommon order, HttpServletRequest request) throws Exception {
		
        String newOrderPayStatus = order.getPayStatus().toString();
        if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, newOrderPayStatus) ||
        	StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, newOrderPayStatus)) {
        	//模拟支付
        	Integer payPriceType = 1;
        	if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, newOrderPayStatus)) {
        		payPriceType = 1;
        	}
        	if (StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, newOrderPayStatus)) {
        		payPriceType = 3;
        	}
        	List<MoneyAmount> moneyList = moneyAmountService.findBySerialNum(order.getTotalMoney());
        	if (CollectionUtils.isNotEmpty(moneyList)) {
        		Integer[] currencyIdPrice = new Integer[1];
        		currencyIdPrice[0] = moneyList.get(0).getCurrencyId();
        		BigDecimal[]dqzfprice = new BigDecimal[1];
        		dqzfprice[0] = BigDecimal.ZERO;
        		orderPayService.savePay("",order.getId(), order.getOrderNum(), 1, order.getOrderStatus(), 3, payPriceType, 1, currencyIdPrice,
        				dqzfprice, "", null, null, "", "", "", "", "", null, null, "yes", null, request, new ModelMap());
        	}
        }
	}
	
	/**
	 * @Description 批量审批（转团和改签）
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-12-9
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> batchReview(String revids, String remark, String v, HttpServletRequest request) throws Exception {
		
		// 返回值
		Map<String, String> result = Maps.newHashMap();
		result.put("result", "success");
		
		// 获取用户ID和公司ID
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		
		// 审批返回结果对象
		ReviewResult reviewResult;
		
		String reviewIds[] = revids.split(",");
		
		for (String reviewId : reviewIds) {
			if (StringUtils.isNotBlank(reviewId)) {
				// 订单类型
				String orderType = reviewId.split("@")[1];
				
				// 机票审核
				if (Context.ORDER_TYPE_JP.toString().equals(orderType)) {
					if (v.equals(ReviewConstant.REVIEW_OPERATION_PASS.toString())) {
						// 审批通过
						reviewResult = reviewService.approve(userId, companyId, null, permissionChecker, reviewId.split("@")[0],remark, null);
						if (reviewResult.getReviewStatus() == 2) {
							//审批通过之后执行的业务操作
							iNewAirticketChangeService.changeOrExit(reviewId.split("@")[0], null, null);
						}
					} else {
						// 审批驳回
						reviewResult = reviewService.reject(userId, companyId, null, reviewId.split("@")[0], remark, null);
					}
					
					if (!reviewResult.getSuccess()) {
						result.put("result", "fail");
						result.put("msg", reviewResult.getMessage());
						throw new Exception();
					} else {
						result.put("result", "success");
					}
				} 
				// 单团类审核
				else {
					result = transferGroupReview(reviewId.split("@")[0], v, remark, request);
					if (!"success".equals(result.get("result").toString())) {
						result.put("result", "fail");
						result.put("msg", "单团审批报错");
						throw new Exception(result.get("msg").toString());
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 处理游客多对一转团(单个map)
	 * @param transferGroupList
	 */
	public void handleMap4ManyTraveler(Map<String, Object> transGroupMap) {
		// 如果是多游客转入同一订单，则重新组织名字、价格等
		Object transGroupMany2One = transGroupMap.get(Context.REVIEW_VARIABLE_TRANSGROUP_MANY2ONE_FLAG);
		List<Map<String, Object>> travelerMapList = new ArrayList<>();  // 诸位游客信息 map
		if (transGroupMany2One != null && (boolean)transGroupMany2One) {
			// 获取 游客id
			String travelerIDString = "";
			if (transGroupMap.get(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS) != null) {
				travelerIDString = transGroupMap.get(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS).toString();
			}
			if (StringUtils.isNotBlank(travelerIDString) && travelerIDString.split(",").length > 0) {
				String[] travelerIDArray = travelerIDString.split(",");  // 诸位游客 IDs
				// 要覆盖的信息（逗号隔开）
				String travelerNames = "";  // 游客姓名
				List<MoneyAmount> payPriceSum = new ArrayList<>();  // 游客应收总和
				List<MoneyAmount> subtractSum = new ArrayList<>();  // 游客扣减金额总和	
				// 组织转团游客信息
				for (int i = 0; i < travelerIDArray.length; i++) {
					Map<String, Object> travelerInfoMap = new HashMap<>();
					Long travelerId = Long.parseLong(travelerIDArray[i]);
					
					// 游客姓名
					if (StringUtils.isNotBlank(travelerNames)) {							
						travelerNames += ",";
					}
					String travelerName = transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerId.toString()).toString();
					travelerNames += travelerName;
					
					// 游客id
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId.toString());
					// 游客 name
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerId.toString()));
					// 游客 结算价
					String payPriceSerial = transGroupMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE + "_" + travelerId.toString()).toString();
					travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, moneyAmountService.getMoneyStr(payPriceSerial));
					payPriceSum.addAll(moneyAmountService.getMoneyAmonutListIgnoreDelflag(payPriceSerial));
					// 游客 扣减金额
					String subtractSerial = transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE + "_" + travelerId.toString()).toString();
					travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE, moneyAmountService.getMoneyStr(subtractSerial));
					subtractSum.addAll(moneyAmountService.getMoneyAmonutListIgnoreDelflag(subtractSerial));
					// 游客 备注
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK + "_" + travelerId.toString()));
					// 游客实体
					Traveler traveler = travelerDao.findById(travelerId);  // 校验游客是否为空
					travelerInfoMap.put("traveler", traveler);
					travelerMapList.add(travelerInfoMap);
				}
				// 覆盖原有数据
				transGroupMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerNames);
				transGroupMap.put("payPriceSumString", moneyAmountService.getMoneyStrFromAmountList("mark", moneyAmountService.sameCurrencySum(payPriceSum)));
				transGroupMap.put("subtractSumString", moneyAmountService.getMoneyStrFromAmountList("mark", moneyAmountService.sameCurrencySum(subtractSum)));
				transGroupMap.put("travelerMapList", travelerMapList);
			}
		} else {
			Map<String, Object> travelerInfoMap = new HashMap<>();
			Long travelerId = Long.parseLong(transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString());
			// 游客id
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId.toString());
			// 游客 name
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME));
			// 游客 结算价
			//String payPriceMoneyStr = moneyAmountService.getMoneyStr(transGroupMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE).toString());
			// forbug 改签审核页面原始应收价格为0
			String payPriceMoneyStr = transGroupMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE).toString();
			travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, payPriceMoneyStr);
			// 游客 扣减金额
			String substractMoneyStr = moneyAmountService.getMoneyStr(transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE) == null ? null : transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE).toString());
			travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE, substractMoneyStr);
			// 游客 备注
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK));
			
			//游客信息
			Traveler traveler = travelerDao.findById(travelerId);
			travelerInfoMap.put("traveler", traveler);
			travelerMapList.add(travelerInfoMap);
			transGroupMap.put(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS, travelerId.toString());
			transGroupMap.put("payPriceSumString", payPriceMoneyStr);
			transGroupMap.put("subtractSumString", substractMoneyStr);
			transGroupMap.put("travelerMapList", travelerMapList);
		}
	}
	
}