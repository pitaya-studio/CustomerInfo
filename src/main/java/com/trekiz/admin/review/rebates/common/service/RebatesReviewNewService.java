package com.trekiz.admin.review.rebates.common.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.rebates.singleGroup.repository.RebatesNewDao;
import com.trekiz.admin.review.rebates.visa.service.NewVisaRebateService;

import freemarker.template.TemplateException;

/**
 * 返佣审批service
 * @author yakun.bai
 * @Date 2015-12-3
 */
@Service
@Transactional(readOnly = true)
public class RebatesReviewNewService extends BaseService {
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");
	@Autowired
	private RebatesNewDao rebatesNewDao;
	@Autowired
	private AgentinfoDao agentDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private SystemService systemService;
	@Autowired
    private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private NewVisaRebateService newVisaRebateService;
	@Autowired
	private PlatBankInfoService bankInfoService;
	@Autowired
	private AgentinfoService agentInfoService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private OrderpayDao orderpayDao;
	/**
	 * @Description 返佣审核记录查询
	 * @author yakun.bai
	 * @Date 2015-12-4
	 */
	public Page<Map<String, Object>> getRebatesReviewList(Page<Map<String, Object>> pagePara, Map<String, String> params) {
		
		//获取返佣审批查询sql
		String sql = getReviewSql();
		
		//获取返佣审批查询条件sql
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
		for (Map<String, Object> map : list) {
			reviewId = map.get("reviewId");
			if (reviewId == null || "".equals(reviewId.toString())) {
				continue;
			}
			
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewId.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewId.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if (status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isNumber(status.toString())) {
				map.put("statusdesc" ,"无审核状态");
				continue;
			}
			if (ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())) {
				Object cReviewer = map.get("currentReviewer");
				String person = "未分配审核人";
				if (cReviewer != null && !StringUtil.isBlank(cReviewer.toString())) {
					person = getReviewerDesc(cReviewer);
					map.put("statusdesc" ,"待" + person + "审批");
				} else {
					map.put("statusdesc" ,person);
				}
			} else {
				map.put("statusdesc" ,getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			//对应需求号   签证订单团号  统一取订单所关联产品团号（环球行除外）
			if (Context.PRODUCT_TYPE_QIAN_ZHENG.equals(map.get("productType"))) {
				if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
					if (map.get("orderId") != null && StringUtils.isNotBlank(map.get("orderId").toString())) {						
						VisaOrder visaOrder =  visaOrderService.findVisaOrder(Long.parseLong(map.get("orderId").toString()));
						if (visaOrder != null) {					
							map.put("groupCode", visaOrder.getGroupCode());
						}
					} else {
						System.out.println(map.get("id"));
					}
				} else {
					if (map.get("productId") != null && StringUtils.isNotBlank(map.get("productId").toString())) {						
						VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(Long.parseLong(map.get("productId").toString()));
						if (visaProducts != null) {								
							map.put("groupCode",visaProducts.getGroupCode());
						}
					}
				}
			}
			
			reviewId = null;
			status = null;
		}
		page.setList(list);
		return page;
	}
	
	
	/**
	 * @Description 获取返佣审批查询sql
	 * @author yakun.bai
	 * @Date 2015-12-4
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
				append("r.related_object relatedObject,r.related_object_type relatedObjectType, r.related_object_name relatedObjectName, ").
				append("t.id rebatesId, t.costname costName, GROUP_CONCAT(CONCAT (t.currencyId, ' ', t.rebatesDiff)) rebatesMoney, ").
				append("t.currencyId currencyId, t.rebatesDiff rebatesDiff, r.pay_status paystatus, r.status status ").
			append("FROM review_new r left join rebates t on t.rid = r.id ").
			append("WHERE r.process_type = '" + Context.REBATES_FLOW_TYPE + "' AND r.company_id = '" + companyUuid + "' ").
			append("AND ((r.product_type IN (" + prdStr + ") AND r.dept_id IN (" + deptIdStr + ")) OR FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		return sql.toString();
	}
	
	/**
	 * @Description 获取返佣审批查询条件sql
	 * @author yakun.bai
	 * @Date 2015-12-4
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

		//审批供应商 added by zhenxing.yan
		String supplierId = params.get("supplierId");
		if (StringUtils.isNotBlank(supplierId)) {
			whereSql.append(" and r.related_object = " + supplierId + " ");
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
		
		//返佣开始金额
		String rebatesDiffBegin = params.get("rebatesDiffBegin");
		if (StringUtils.isNotBlank(rebatesDiffBegin)) {
			whereSql.append(" and t.rebatesDiff >= " + rebatesDiffBegin + " ");
		}
		
		//返佣结束金额
		String rebatesDiffEnd = params.get("rebatesDiffEnd");
		if (StringUtils.isNotBlank(rebatesDiffEnd)) {
			whereSql.append(" and t.rebatesDiff <= " + rebatesDiffEnd + " ");
		}
		
		//返佣结束金额
		String payStatus = params.get("payStatus");
		if (StringUtils.isNotBlank(payStatus)) {
			whereSql.append(" and r.pay_status = " + payStatus + " ");
		}
		
		//打印状态
		String printStatus = params.get("printStatus");
		if (StringUtils.isNotBlank(printStatus) && NumberUtils.isNumber(printStatus)) {
			whereSql.append(" and r.print_status = " + Integer.parseInt(printStatus) + " ");
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
				whereSql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) and status = 1 ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt) {
				whereSql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt) {
				whereSql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		
		//渠道结算类型
		String paymentType = params.get("paymentType");
		if(StringUtils.isNotBlank(paymentType)){
			whereSql.append(" and r.agent in (select id from agentinfo where paymentType = "+paymentType+") ");
		}
		//分组条件
		whereSql.append("GROUP BY rid");
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
	 * @Description 查询单团类和机票订单金额
	 * @author yakun.bai
	 * @Date 2015-12-10
	 */
	public Page<Map<String, Object>> setMoney(Page<Map<String, Object>> page) {
		
		if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
			List<Map<String, Object>> list = page.getList();
			// 单团类订单ids
			String commonIds = "";
			// 机票订单ids
			String airticketIds = "";
			// 签证订单ids
			String visaIds = "";
			for (Map<String, Object> map : list) {
				// 订单类型
				String productType = map.get("productType").toString();
				// 订单ID
				String orderId = map.get("orderId").toString();
				// 转团或改签后应收金额
				Object subtractPrice = map.get("subtractPrice");
				// 如果没有转团或改签后应收金额，则赋值为¥0.00
				if (subtractPrice == null || StringUtils.isBlank(subtractPrice.toString())) {
					map.put("subtractPrice", "¥0.00");
				}
				// 组合订单id
				if (Context.ORDER_TYPE_JP.toString().equals(productType)) {
					airticketIds += orderId + ",";
				} else if (Context.ORDER_TYPE_QZ.toString().equals(productType)) {
					visaIds += orderId + ",";
				} else {
					commonIds += orderId + ",";
				}
			}
			
			// 单团类订单
			List<Map<String, Object>> commonOrderList = null;
			// 机票订单
			List<Map<String, Object>> airticketOrderList = null;
			// 签证订单
			List<Map<String, Object>> visaOrderList = null;
			// 查询单团类订单
			if (StringUtils.isNotBlank(commonIds)) {
				commonOrderList = OrderCommonUtil.queryOrderMoney("productorder", commonIds.substring(0, commonIds.length() - 1));
			}
			// 查询机票类订单
			if (StringUtils.isNotBlank(airticketIds)) {
				airticketOrderList = OrderCommonUtil.queryOrderMoney("airticket_order", airticketIds.substring(0, airticketIds.length() - 1));
			}
			// 查询签证类订单
			if (StringUtils.isNotBlank(visaIds)) {
				visaOrderList = OrderCommonUtil.queryOrderMoney("visa_order", visaIds.substring(0, visaIds.length() - 1));
			}
			
			// 根据订单类型和订单ID获取对应订单应收、已收、达帐金额
			if (CollectionUtils.isNotEmpty(commonOrderList) || CollectionUtils.isNotEmpty(airticketOrderList) 
					|| CollectionUtils.isNotEmpty(visaOrderList)) {
				for (Map<String, Object> map : list) {
					String productType = map.get("productType").toString();
					String orderId = map.get("orderId").toString();
					// 如果是机票订单则给订单应收、已收、达帐金额赋值
					if (Context.ORDER_TYPE_JP.toString().equals(productType) && CollectionUtils.isNotEmpty(airticketOrderList)) {
						for (Map<String, Object> temp : airticketOrderList) {
							String tempOrderId = temp.get("orderId").toString();
							if (orderId.equals(tempOrderId)) {
								map.put("totalMoney", temp.get("totalMoney"));
								map.put("payedMoney", temp.get("payedMoney"));
								map.put("accountedMoney", temp.get("accountedMoney"));
								break;
							}
						}
					} 
					// 如果是签证订单则给订单应收、已收、达帐金额赋值
					else if (Context.ORDER_TYPE_QZ.toString().equals(productType) && CollectionUtils.isNotEmpty(visaOrderList)) {
						for (Map<String, Object> temp : visaOrderList) {
							String tempOrderId = temp.get("orderId").toString();
							if (orderId.equals(tempOrderId)) {
								map.put("totalMoney", temp.get("totalMoney"));
								map.put("payedMoney", temp.get("payedMoney"));
								map.put("accountedMoney", temp.get("accountedMoney"));
								break;
							}
						}
					} 
					// 如果是单团类订单则给订单应收、已收、达帐金额赋值
					else if (CollectionUtils.isNotEmpty(commonOrderList)) {
						for (Map<String, Object> temp : commonOrderList) {
							String tempOrderId = temp.get("orderId").toString();
							if (orderId.equals(tempOrderId)) {
								map.put("totalMoney", temp.get("totalMoney"));
								map.put("payedMoney", temp.get("payedMoney"));
								map.put("accountedMoney", temp.get("accountedMoney"));
								break;
							}
						}
					}
				}
			}
		}
		
		return page;
	}
	
	/**
	 * @Description 返佣审核
	 * @param reviewId 审核id
	 * @param strResult 通过或驳回的标识
	 * @param denyReason 驳回原因
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-12-10
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> rebatesReview(String reviewId, String strResult, String denyReason, HttpServletRequest request) {
		
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
			// 审核通过之后对成本进行更改 add by chy 2015年12月18日19:56:22 审核操作通过时更新成本记录
			ReviewNew review = reviewService.getReview(reviewId);
			commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());
		} else {
			// 审批驳回
			reviewResult = reviewService.reject(userId, companyId, null, reviewId, denyReason, null);
			// 审核驳回之后对成本进行更改
			ReviewNew review = reviewService.getReview(reviewId);
			commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());//第二个参数改为审核记录的审核状态 by chy 2015年12月18日19:55:39
		}
		if (!reviewResult.getSuccess()) {
			result.put("result", "fail");
			result.put("msg", reviewResult.getMessage());
		} else {
			result.put("result", "success");
		}
		// 如果审核通过并且当前层级为最高层级 则更改对应业务数据
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewId);
			//产品类型
			String productType = reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE).toString();
			//机票产品时需要处理的逻辑
			if (Context.PRODUCT_TYPE_AIRTICKET.toString().equals(productType)) {
				String orderId = request.getParameter("orderId");
				rebatesNewDao.getSession().flush();
				this.reviewSuccess(orderId,reviewId);
			} else if (Context.PRODUCT_TYPE_QIAN_ZHENG.toString().equals(productType)) {
				
				newVisaRebateService.updateExtend3ByReviewSuccess(reviewMap, reviewId);
			}
//			// 审核通过之后对成本进行更改 注释掉了by chy 2015-12-18 19:55:57 改为审核操作是通过时调用
//			ReviewNew review = reviewService.getReview(reviewId);
//			commonReviewService.updateCostRecordStatus(review, Integer.parseInt(strResult));
		}
		
		return result;
	}
	
	/**
	 * @Description 批量审批（单团类、机票、签证）
	 * @author yakun.bai
	 * @Date 2015-12-9
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, String> batchReview(String revids, String remark, String v, HttpServletRequest request) {
		
		// 返回值
		Map<String, String> result = Maps.newHashMap();
		result.put("result", "success");
		
		String reviewIds[] = revids.split(",");
		
		for (String reviewId : reviewIds) {
			if (StringUtils.isNotBlank(reviewId)) {
				try {
					result = rebatesReview(reviewId.split("@")[0], v, remark, request);
					if (!"success".equals(result.get("result").toString())) {
						result.put("result", "fail");
						if ( StringUtils.isNotBlank(result.get("msg"))) {
							result.put("msg", result.get("msg"));
							throw new Exception(result.get("msg"));
						} else {
							result.put("msg", "未知错误");
							throw new Exception("未知错误");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return result;
				}
			}
		}
		return result;
	}
	/**
	 * 机票返佣审批通过时调用的方法
	 */
	@Transient
	public void reviewSuccess(String orderId,String reviewId) {
		//获取订单最新已通过流程的总差额
		List<Object[]> newtotalList = getnewTeamRebatesList(Long.parseLong(orderId));
		List<Object[]> oldtotalList = getoldTeamRebatesList(Long.parseLong(orderId));
		List<Object[]> onetotalList = getoneTeamRebatesList(Long.parseLong(orderId));
		
		String newtotal = "";
		String oldtotal = "";
		
		if(oldtotalList!= null && oldtotalList.size()>0 && null!=oldtotalList.get(0)[1]){
			for(Object[] rebates : oldtotalList){
				if(("").equals(rebates[1].toString()) ){
					for(Object[] o1 : onetotalList){
						oldtotal = o1[0].toString();
					}
				}else{
					oldtotal = rebates[1].toString();
				}
			}
		}
		for (Object[] rebates : newtotalList ) {
			
			if(!("").equals(oldtotal)){
				newtotal = oldtotal + "+" + rebates[1].toString();
			}else{
				newtotal =  rebates[1].toString();
			}
		}
		List<RebatesNew> rebatesList =  rebatesNewDao.findOneByRidList(reviewId);
		for (RebatesNew rebates : rebatesList) {
			//更新所有订单累计的总金额
			String sql = "update rebates  set allCumulative = ? where orderId = ? and orderType = 7";
			rebatesNewDao.updateBySql(sql, newtotal,rebates.getOrderId());
		}
	}
	
	/**
	 * 机票返佣根据订单获取最新已通过流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getnewTeamRebatesList(Long orderId){
		String sql = "select r.id,r.nowCumulative,r.allCumulative from rebates as r  where r.rid =(select rn.id from rebates as re,  review_new as rn  where re.orderId = rn.order_id  and rn.status=2 and re.orderId = ? ORDER BY rn.update_date DESC LIMIT 1)";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getoldTeamRebatesList(Long orderId){
		String sql = "SELECT r.nowCumulative, 	r.allCumulative,rn.id  FROM rebates AS r ,review_new as rn  WHERE rn.id = r.rid and  r.rid IN ( SELECT 	ren.id 	FROM rebates AS re,review_new AS ren	WHERE	re.orderId = ren.order_id 	AND ren.status = 2	AND re.orderId = ? 	)  GROUP BY rn.id  ORDER BY rn.update_date DESC limit 1,1";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getoneTeamRebatesList(Long orderId){
		String sql  ="SELECT 	r.nowCumulative, r.allCumulative, rn.id FROM 	rebates AS r, review_new AS rn  WHERE rn.id = r.rid AND  r.rid IN ( SELECT 	ren.id 	FROM 	rebates AS re, review_new AS ren WHERE re.orderId = ren.order_id 	AND ren.status = 2 	AND re.orderId = ?  ORDER BY ren.update_date DESC )  GROUP BY 	rn.id limit 1;";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	/**
	 * 封装打印数据---仅供返佣审批页面使用
	 * @param reviewId
	 * @return
	 */
	public Map<String,Object> buildPrintData(String reviewId) {
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
		List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管
		List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
		List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
		List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
		String deptmanager = getNames(executives);//主管
		String majorCheckPerson = getNames(managers);//总经理 
		String financeManage = getNames(finances);//财务主管
		String cashier = getNames(cashiers);//出纳
		String auditor = getNames(reviewers);//审核
		map.put("cashier", cashier);
		map.put("deptmanager", deptmanager);
		map.put("auditor", auditor);
		map.put("majorCheckPerson", majorCheckPerson);
		map.put("financeManage", financeManage);
		
		//返佣申请审核相关信息
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		
		if (reviewDetailMap != null) {
			map.put("createDate", DateUtils.dateFormat(reviewDetailMap.get("createDate").toString()));	//填写日期
			String printTime = objToString(reviewDetailMap.get("printDate"));
			if (StringUtils.isNotBlank(printTime)) {
				map.put("firstPrintTime",  DateUtils.dateFormat(printTime));	//首次打印时间
			} else {
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
			
			User user = UserUtils.getUser(objToString(reviewDetailMap.get("createBy")));
			if (null != user) {
				map.put("operatorName", user.getName());	//经办人(返佣申请人)
				map.put("payee", user.getName());	//领款人(返佣申请人)
			} else {
				map.put("operatorName", "");//经办人(返佣申请人)
				map.put("payee", "");	//领款人(返佣申请人)
			}
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String payStatus = objToString(reviewDetailMap.get("payStatus"));
			if (!Context.SUPPLIER_UUID_LMT.equals(companyUuid) && !Context.SUPPLIER_UUID_HQX.equals(companyUuid) && "1".equals(payStatus)) {
				Date confirmPayDate = DateUtils.dateFormat(objToString(reviewDetailMap.get("updateDate")));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
		}
		
		//返佣申请相关信息
		RebatesNew rebates = this.findRebatesByRid(reviewId);
		if (rebates != null ) {
			map.put("remark",  rebates.getRemark());//备注
			String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称
			
			List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(rebates.getNewRebates());
			MoneyAmount money = new MoneyAmount();
			if (moneyList != null && moneyList.size() > 0) {
				for (MoneyAmount moneyAmount : moneyList) {
					if (rebates.getCurrencyId().longValue() == moneyAmount.getCurrencyId().longValue() ) {
						money = moneyAmount;
						break;
					}
				}
			}
			
			BigDecimal newRebates = new BigDecimal(0);
			newRebates = money.getAmount() == null ? newRebates:money.getAmount();	//金额
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额
			
			//如果是非人民币
			if (!currencyName.contains("人民币")) {
				BigDecimal currencyExchangerate = new BigDecimal(1);
				currencyExchangerate = rebates.getCurrencyExchangerate();	
				//转换成人民币金额 = 金额 * 汇率
				totalRMBMoney = new BigDecimal(newRebates.multiply(currencyExchangerate).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				map.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(currencyExchangerate.toString(), "#,##0.0000"));//汇率
			} else {
				totalRMBMoney = newRebates;
				map.put("currencyExchangerate", "1.0000");//汇率
			}
			
			map.put("currencyName", currencyName);	//币种名称
			map.put("money", MoneyNumberFormat.getThousandsMoney(newRebates.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));	//币种金额
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额
			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO).replaceAll("-",""))); //合计人民币金额大写
			}			
		} else {
			map.put("currencyName", "");	//币种名称
			map.put("money", "");	//币种金额
			map.put("currencyExchangerate", "");//汇率
			map.put("totalRMBMoney", ""); //合计人民币金额
			map.put("remark",  "");//备注
			map.put("totalRMBMoneyName", ""); //合计人民币金额大写
		}
		map.put("costname",  "报销");//款项
		
		// 渠道名称
		String orderCompanyName = "";
		if (StringUtils.isNotBlank(objToString(reviewDetailMap.get("agent")))) {
			Agentinfo agentInfo = agentDao.findAgentInfoById(Long.parseLong(objToString(reviewDetailMap.get("agent"))));
			if (agentInfo != null) {
				orderCompanyName = agentInfo.getAgentName();
			}
		}	
		map.put("orderCompanyName", orderCompanyName);
		map.put("accountName", "");
		
		//环球行团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		}else {
			map.put("groupCodeName", "团号");
		}
		
		return map;
	}
	
	/**
	 * 封装打印数据
	 * @param reviewId
	 * @param payId
	 * @return
	 */
	public Map<String,Object> buildPrintData(String reviewId, String payId) {
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
		List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管
		List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
		List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
		List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
		String deptmanager = getNames(executives);//主管
		String majorCheckPerson = getNames(managers);//总经理 
		String financeManage = getNames(finances);//财务主管
		String cashier = getNames(cashiers);//出纳
		String auditor = getNames(reviewers);//审核
		map.put("cashier", cashier);
		map.put("deptmanager", deptmanager);
		map.put("auditor", auditor);
		map.put("majorCheckPerson", majorCheckPerson);
		map.put("financeManage", financeManage);
		
		//返佣申请审核相关信息
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		
		if (reviewDetailMap != null) {
			map.put("createDate", DateUtils.dateFormat(reviewDetailMap.get("createDate").toString()));	//填写日期
			String printTime = objToString(reviewDetailMap.get("printDate"));
			if (StringUtils.isNotBlank(printTime)) {
				map.put("firstPrintTime",  DateUtils.dateFormat(printTime));	//首次打印时间
			} else {
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
			
			String payStatus = objToString(reviewDetailMap.get("payStatus"));
			if (!Context.SUPPLIER_UUID_HQX.equals(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && "1".equals(payStatus)) {
				Date confirmPayDate = DateUtils.dateFormat(objToString(reviewDetailMap.get("updateDate")));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
			//添加公司的uuid到map--0419-djw
			map.put("companyId", reviewDetailMap.get("companyId"));
		}
		
		//返佣申请相关信息
		BigDecimal currencyExchangerate = new BigDecimal(1);
		RebatesNew rebates = this.findRebatesByRid(reviewId);
		if (rebates != null ) {
			map.put("remark",  rebates.getRemark());//备注
			String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称			
			//如果是非人民币
			if (!currencyName.contains("人民币")) {
				currencyExchangerate = rebates.getCurrencyExchangerate();				
				map.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(currencyExchangerate.toString(), "#,##0.0000"));//汇率
			} else {
				map.put("currencyExchangerate", "1.0000");//汇率
			}
			
			map.put("currencyName", currencyName);	//币种名称	
		} else {
			map.put("currencyName", "");	//币种名称
			map.put("currencyExchangerate", "");//汇率
			map.put("remark",  "");//备注
		}

		User user = UserUtils.getUser(objToString(reviewDetailMap.get("createBy")));
		if (null != user) {
			map.put("operatorName", user.getName());	//经办人(返佣申请人)
			map.put("payee", user.getName());	//领款人(返佣申请人)
		} else {
			map.put("operatorName", "");//经办人(返佣申请人)
			map.put("payee", "");	//领款人(返佣申请人)
		}
		//支付金额,45需求，以每次支付的金额为准, 单币种
		List<Object[]> moneys = null;
		PayInfoDetail payDetail = null;
		if(StringUtils.isNotBlank(payId)) {
			payDetail = refundService.getPayInfoByPayId(payId, rebates.getOrderType().toString());
			if(payDetail != null) {
				String moneyStr = payDetail.getMoneyDispStyle();
				moneys = MoneyNumberFormat.getMoneyFromString(moneyStr, "\\+");
				Integer payType = payDetail.getPayType();
				if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && (1 == payType || 3 == payType)){
					map.put("payee", payDetail.getPayerName());
					map.put("isYJXZ","YJXZ");
				}
			}
		}
		if(CollectionUtils.isNotEmpty(moneys)) {
			BigDecimal totalMoney = new BigDecimal(Double.valueOf(moneys.get(0)[1].toString()));
			map.put("money", MoneyNumberFormat.getThousandsMoney(totalMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
			
			BigDecimal totalRMBMoney = totalMoney.multiply(currencyExchangerate);
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO).replaceAll("-",""))); //合计人民币金额大写
			}
		}else {
			map.put("money","");	//人民币金额
			map.put("totalRMBMoney", "");
			map.put("totalRMBMoneyName", "");
		}
		
		map.put("costname",  "报销");//款项
		
		// 渠道名称 或者供应商名称
		String orderCompanyName = "";
		String accountName = "";  //账户名称
		//判断是供应商还是渠道商
		String relatedObjectType = objToString(reviewDetailMap.get("relatedObjectType"));
		if(StringUtils.isNotBlank(relatedObjectType) && relatedObjectType.equals("2")) {    //供应商
			orderCompanyName = objToString(reviewDetailMap.get("relatedObjectName"));
			String supplierId = objToString(reviewDetailMap.get("relatedObject"));     //供应商id 
			String bankType = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_TYPE));   //境内账户or境外账户
			String bankName = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_BANK));   //银行名称
			String bankAccount = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_CODE));   //银行账号
			accountName = bankInfoService.getAccountName(Long.valueOf(supplierId), Context.PLAT_TYPE_SUP, bankName, bankAccount, bankType);
		}else {  //渠道商
			if (StringUtils.isNotBlank(objToString(reviewDetailMap.get("agent")))) {
				Agentinfo agentInfo = agentDao.findAgentInfoById(Long.parseLong(objToString(reviewDetailMap.get("agent"))));
				if (agentInfo != null) {
					orderCompanyName = agentInfo.getAgentName();
					accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(), "");
				}
			}	
		}
		
		map.put("orderCompanyName", orderCompanyName);
		map.put("accountName", accountName);
		//添加支付方式 -0419-djw
		List<Orderpay> orderpay = orderpayDao.findOrderpayByOrderId(rebates.getOrderId());
		map.put("payType", orderpay.get(0).getPayType());
		
		//环球行团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		}else {
			map.put("groupCodeName", "团号");
		}
				
		return map;
	}
	
	/**
	 * @Description 把对象转为字符串，如果对象为空则返回空字符串
	 * @author yakun.bai
	 * @Date 2015-12-10
	 */
	private String objToString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * @Description 根据审核id查询返佣信息
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	public RebatesNew findRebatesByRid(String id) {
		return rebatesNewDao.findOneByRid(id);
	}
	
	/**
	 * 获取user的名称
	 * @param Users
	 * @return
	 */
	private String getNames(List<User> users) {
		String res = " ";
		int n = 0;
		if(users == null || users.size() == 0){
			return res;
		}
		for(User user : users){
			if(n==0){
				res = res.trim();
				res += user.getName();
				n++;
			} else {
				res += "," + user.getName();
			}
		}
		return res;
	}
	
	/**
	 * 生成返佣审核单下载文件-仅供返佣审批功能页面使用
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createRebatesReviewSheetDownloadFile4Visa(String reviewId) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();

		// 获取activiti审核信息
		Map<String, Object> reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate",sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate").toString())));// 填写日期

			// --- wangxinwei 20151008 added：需求C221，处理确认付款日期
			// 下述语句由于查询到的记录中为空值,所以加了条件限制-tgy
			if (null != reviewAndDetailInfoMap.get("updateDate")) {
				root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 更新日期
			} else {
				root.put("revUpdateDate", "");// 更新日期
			}
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());

			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
				// 针对环球行用户，团号规则改成C460V3之前的取值 update by shijun.liu 2016.05.12
				root.put("ordergroupcode", visaOrder.getGroupCode());// 团号
				root.put("groupCodeName", "订单团号");
			}else{
				root.put("ordergroupcode", visaProducts.getGroupCode());// 团号
				root.put("groupCodeName", "团号");
			}
			
			root.put("costname", "报销");// 款项
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);

			// --------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes").toString();
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes").toString();
			String remark = (trvrebatesnotes + groupcurrencyMarks).replace("#@!#!@#", "");
			root.put("remark", remark.trim()); // 摘要 项目备注

			// --------------获取收款单位--------------
			String orderCompanyName = "";
			String accountName = "";
			String agentId = reviewAndDetailInfoMap.get("agent").toString();
			if (StringUtils.isNotBlank(agentId)) {
				Agentinfo agentInfo = agentInfoService.findOne(Long.parseLong(agentId));
				if (agentInfo != null) {
					orderCompanyName = agentInfo.getAgentName();
				}
			}
			root.put("orderCompanyName", orderCompanyName);
			root.put("accountName", accountName);

			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			// 条件判断主要是为了避免为数据库中相关记录为空值的情况-tgy
			if (null != reviewAndDetailInfoMap.get("updateDate")) {
				root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 付款日期
			} else {
				root.put("payDate", "");// 付款日期
			}

			root.put("currencyName", "人民币"); //币种中文名称
			root.put("currencyExchangerate", "1.0000"); //汇率
		}
		
		BigDecimal totalRMBMoney = new BigDecimal(0);
		List<RebatesNew> rebatesList = rebatesNewDao.findListByRid(reviewId);
		if(CollectionUtils.isNotEmpty(rebatesList)) {
			for(RebatesNew rebates : rebatesList) {
				BigDecimal rebatesDiff = rebates.getRebatesDiff();
				BigDecimal exchangerate = rebates.getCurrencyExchangerate();
				if(exchangerate == null) {
					exchangerate = currencyService.findCurrency(rebates.getCurrencyId()).getCurrencyExchangerate();
				}
				totalRMBMoney = totalRMBMoney.add(rebatesDiff.multiply(exchangerate));
			}
		}
		root.put("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
		root.put("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
		root.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
		
		// 出纳以外的最后一个审批人：对签证借款流程来说level为3
		/**
		 * 通过性方式获取审核人职务
		 */
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		// 获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService
				.obtainReviewer4Receipt(companyUUid,
						ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);// e5dbd01ec2f649e39d458540a91aa03b
		List<User> executives = valueMap
				.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);// 主管审批
		List<User> general_managers = valueMap
				.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);// 总经理
		List<User> financial_executives = valueMap
				.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);// 财务主管
		List<User> cashiers = valueMap
				.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);// 出纳
		List<User> reviewers = valueMap
				.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);// 审核

		String executive = getNames(executives);// 主管审批
		String general_manager = getNames(general_managers);// 总经理
		String financial_executive = getNames(financial_executives);// 财务主管
		String cashier = getNames(cashiers);// 出纳
		String reviewer = getNames(reviewers);// 审核

		root.put("majorCheckPerson", general_manager);// 复合，主管审批 都是最后一个的审批人
		root.put("jdmanager", executive);
		root.put("cwmanager", financial_executive);
		root.put("cw", financial_executive);
		root.put("cashier", cashier);
		root.put("deptmanager", reviewer);// 这是为了解决bug-11216而采取的办法,
		// 虽然解决了问题,但是不符合代码规范!-tgy
		root.put("reviewer", reviewer);// 审核-部门主管

		// ----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1
		// 显示update时间，0不显示
		// ----- 除拉美途，北京环球行国际旅行社有限责任公司 都按照此规则
		Long companyId = UserUtils.getUser().getCompany().getId();
		String companyUUId = UserUtils.getUser().getCompany().getUuid();
		String payStatus = reviewAndDetailInfoMap.get("payStatus").toString();
		/*
		 * 环球行687a816f5077a811e5bc1e000c29cf2586拉美图88
		 * 7a81a26b77a811e5bc1e000c29cf2586
		 */
		if (companyId == 88 || companyId == 68
				|| Context.SUPPLIER_UUID_LMT.equals(companyUUId) || Context.SUPPLIER_UUID_HQX.equals(companyUUId)) {
			root.put("payStatus", "0");
		} else {
			root.put("payStatus", payStatus);
		}

		return FreeMarkerUtil.generateFile("visarebatesreview.ftl","visarebatesreview.doc", root);
	}
	
	/**
	 * 生成返佣审核单下载文件-tgy
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createRebatesReviewSheetDownloadFile4Visa(String reviewId, String payId) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		
		//获取activiti审核信息
		Map<String,Object>  reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate").toString())));// 填写日期
			
			// --- wangxinwei 20151008 added：需求C221，处理确认付款日期
			//下述语句由于查询到的记录中为空值,所以加了条件限制-tgy
			if(null!=reviewAndDetailInfoMap.get("updateDate")){
				root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 更新日期
			}else{
				root.put("revUpdateDate","");// 更新日期
			}
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			root.put("costname",  "报销");//款项
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			//  C460V3 签证订单  的团号  统一改为取  签证订单所关联的产品团号
			// C460V3 签证订单团号统一取点单所关联的产品团号
			//model.addAttribute("ordergroupcode", visaOrder.getGroupCode());// 团号
			if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
				// 针对环球行用户，团号规则改成C460V3之前的取值 update by shijun.liu 2016.05.12
				root.put("ordergroupcode", visaOrder.getGroupCode());// 团号
				root.put("groupCodeName", "订单团号");
			}else{
				root.put("ordergroupcode", visaProducts.getGroupCode());// 团号
				root.put("groupCodeName", "团号");
			}
			
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
						
			//--------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes").toString();
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes").toString();
			String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", "");
			root.put("remark", remark.trim()); //摘要  项目备注
//			root.put("remark1", ""); //摘要  付款备注
						
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			//条件判断主要是为了避免为数据库中相关记录为空值的情况-tgy
			if (null != reviewAndDetailInfoMap.get("updateDate")) {
				root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 付款日期
			}else{
				root.put("payDate", "");// 付款日期
			}			
		}
		
		Map<String, BigDecimal> rateMap = Maps.newHashMap();
		List<RebatesNew> rebatesList = rebatesNewDao.findListByRid(reviewId);
		if(CollectionUtils.isNotEmpty(rebatesList)) {
			for(RebatesNew rebates : rebatesList) {
				String currencyMark = rebates.getCurrency().getCurrencyMark().trim();   //币种标识
				if(!rateMap.containsKey(currencyMark)) {
					//-----解决bug#13724中,当人民币对应的汇率为空时,报null异常---start------------
					if ("¥".equals(currencyMark)) {
						rateMap.put(currencyMark, new BigDecimal(1));
					}else{
						rateMap.put(currencyMark, rebates.getCurrencyExchangerate());
					}
					//-----解决bug#13724中,当人民币对应的汇率为空时,报null异常---end------------
				}
			}
		}		
		//45需求，凭单中的金额以每次支付的金额为准
		List<Object[]> moneys = null;
		PayInfoDetail payDetail = null;
		if(StringUtils.isNotBlank(payId)) {
			payDetail = refundService.getPayInfoByPayId(payId, Context.PRODUCT_TYPE_QIAN_ZHENG.toString());
			if(payDetail != null && StringUtils.isNotBlank(payDetail.getMoneyDispStyle())) {
				moneys = MoneyNumberFormat.getMoneyFromString(payDetail.getMoneyDispStyle(), "\\+");
			}
		}
		if(CollectionUtils.isNotEmpty(moneys)) {
			BigDecimal totalRMBMoney = new BigDecimal(0);
			for(Object[] money : moneys) {
				String currencyMark = money[0].toString();
				if(rateMap.containsKey(currencyMark)){
					BigDecimal RMBMoney = rateMap.get(currencyMark).multiply(new BigDecimal(Double.valueOf(money[1].toString())));
					totalRMBMoney = totalRMBMoney.add(RMBMoney);	
				}else {
					Currency currency = currencyService.findCurrencyByCurrencyMark(currencyMark,UserUtils.getUser().getCompany().getId());
					BigDecimal rate = null==currency? new BigDecimal(0):currency.getCurrencyExchangerate();
					BigDecimal RMBMoney = rate.multiply(new BigDecimal(Double.valueOf(money[1].toString())));
					totalRMBMoney = totalRMBMoney.add(RMBMoney);						
				}
			}

			root.put("currencyName", "人民币");
			root.put("currencyExchangerate", "1.0000");//汇率默认为1
			
			root.put("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
			root.put("rebatesAmount",MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
			root.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
		}else {
			root.put("currencyExchangerate", "");
			root.put("payRebatesAmount", "");
			root.put("currencyName", "");
			
			root.put("rebatesAmount","");// 返佣金额
			root.put("rebatesAmountDx", "");// 返佣金额大写
		}
		
		//渠道名称或者供应商名称
		String orderCompanyName = "";
		String accountName = "";  //账户名称
		//判断是供应商还是渠道商
		String relatedObjectType = objToString(reviewAndDetailInfoMap.get("relatedObjectType"));
		if(StringUtils.isNotBlank(relatedObjectType) && relatedObjectType.equals("2")) {    //供应商
			orderCompanyName = objToString(reviewAndDetailInfoMap.get("relatedObjectName"));
			String supplierId = objToString(reviewAndDetailInfoMap.get("relatedObject"));     //供应商id 
			String bankType = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_TYPE));   //境内账户or境外账户
			String bankName = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_BANK));   //银行名称
			String bankAccount = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_CODE));   //银行账号
			accountName = bankInfoService.getAccountName(Long.valueOf(supplierId), Context.PLAT_TYPE_SUP, bankName, bankAccount, bankType);
		}else {
			if (StringUtils.isNotBlank(objToString(reviewAndDetailInfoMap.get("agent")))) {
				Agentinfo agentInfo = agentInfoService.findAgentInfoById(Long.parseLong(objToString(reviewAndDetailInfoMap.get("agent"))));
				if (agentInfo != null) {
					orderCompanyName = agentInfo.getAgentName();
					accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(), "");
				}
			}	
		}
		
		root.put("accountName", accountName);
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		/**
         * 通过性方式获取审核人职务 
         */
        String companyUUid = UserUtils.getUser().getCompany().getUuid();
        //获取单据审批人员Map
        MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);//e5dbd01ec2f649e39d458540a91aa03b      
        List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管审批
        List<User> general_managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
        List<User> financial_executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
        List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
        List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
        
        String  executive = getNames(executives);//主管审批
        String general_manager = getNames(general_managers);//总经理 
        String financial_executive = getNames(financial_executives);//财务主管
        String cashier = getNames(cashiers);//出纳
        String  reviewer = getNames(reviewers);//审核
        
		
		root.put("majorCheckPerson", general_manager);//复合，主管审批  都是最后一个的审批人
		root.put("jdmanager", executive);
		root.put("cwmanager", financial_executive);	
		root.put("cw", financial_executive);
		root.put("cashier", cashier);
		root.put("deptmanager", reviewer);//这是为了解决bug-11216而采取的办法,
		                                  //虽然解决了问题,但是不符合代码规范!-tgy
		root.put("reviewer", reviewer);//审核-部门主管
		
		//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		String companyUUId = UserUtils.getUser().getCompany().getUuid();
		String  payStatus = reviewAndDetailInfoMap.get("payStatus").toString();
		/*环球行68
		*7a816f5077a811e5bc1e000c29cf2586
		*拉美图88
		*7a81a26b77a811e5bc1e000c29cf2586
		*/
		if (Context.SUPPLIER_UUID_LMT.equals(companyUUId) || Context.SUPPLIER_UUID_HQX.equals(companyUUId)) {
			root.put("payStatus","0");
		}else {
			root.put("payStatus",payStatus);
		}
		//----0419需求------djw-----------------
		if ((payDetail.getPayType() == 1 || payDetail.getPayType() == 3)&&("7a81b21a77a811e5bc1e000c29cf2586".endsWith(companyUUId))) {
			
			if ("非签约渠道".equals(orderCompanyName)) {
				orderCompanyName = "直客";
			}
			root.put("orderCompanyName", orderCompanyName);
			return FreeMarkerUtil.generateFile("visarebatesreview4YJXZ.ftl", "visarebatesreview.doc", root);
			
		}else{
			root.put("orderCompanyName", orderCompanyName);
			return FreeMarkerUtil.generateFile("visarebatesreview.ftl", "visarebatesreview.doc", root);
		}
		
	}
}